package jopt.csp.spi.arcalgorithm.domain;

import java.math.BigInteger;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.util.LongIntervalSet;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

public class LongComputedDomain extends AbstractDomain implements LongDomain {
    private LongDomain domA;
    private long constA;
    private LongDomain domB;
    private long constB;
    private int operation;
    private long min;
    private long max;
    private boolean needsRecalc;
    private DelegatedDomainListener changeListener;

    private LongComputedDomain(LongDomain domA, long constA, LongDomain domB, long constB, int operation) {
        this.domA = domA;
        this.constA = constA;
        this.domB = domB;
        this.constB = constB;
        this.operation = operation;
        this.needsRecalc = true;

        // Set listener for delegated domains
        this.changeListener = new DelegatedDomainListener();
        if (domA != null) ((DomainChangeSource) domA).addRangeChangeListener(changeListener);
        if (domB != null) ((DomainChangeSource) domB).addRangeChangeListener(changeListener);
    }

    public LongComputedDomain(LongDomain domA, LongDomain domB, int operation) {
        this(domA, 0, domB, 0, operation);
    }
    public LongComputedDomain(long a, LongDomain domB, int operation) {
        this(null, a, domB, 0, operation);
    }
    public LongComputedDomain(LongDomain domA, long b, int operation) {
        this(domA, 0, null, b, operation);
    }
    public LongComputedDomain(LongDomain domA, int operation) {
        this(domA, 0, null, 0, operation);
    }

    /**
     * Returns size of domain
     */
    public int getSize() {
        BigInteger s = BigInteger.valueOf(max).subtract(BigInteger.valueOf(min));
        if (s.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0)
            return Integer.MAX_VALUE;
        return s.intValue() + 1;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return (domA == null || domA.isBound()) &&
               (domB == null || domB.isBound());
    }

    /**
     * Recalculates min and max values
     */
    private void recalc() {
        BigInteger min = null;
        BigInteger max = null;
        BigInteger mina = (domA != null) ? BigInteger.valueOf(domA.getMin()) : BigInteger.valueOf(constA);
        BigInteger maxa = (domA != null) ? BigInteger.valueOf(domA.getMax()) : BigInteger.valueOf(constA);
        BigInteger minb = (domB != null) ? BigInteger.valueOf(domB.getMin()) : BigInteger.valueOf(constB);
        BigInteger maxb = (domB != null) ? BigInteger.valueOf(domB.getMax()) : BigInteger.valueOf(constB);

        switch(operation) {
            case NumOperation.ADD:
                min = mina.add(minb);
                max = maxa.add(maxb);
                break;

            case NumOperation.SUBTRACT:
                min = mina.subtract(maxb);
                max = maxa.subtract(minb);
                break;

            case NumOperation.MULTIPLY:
                min = maxa.multiply(maxb);
	            min = min.min(mina.multiply(minb));
	            min = min.min(mina.multiply(maxb));
	            min = min.min(maxa.multiply(minb));
                max = maxa.multiply(maxb);
                max = max.max(mina.multiply(minb));
                max = max.max(mina.multiply(maxb));
                max = max.max(maxa.multiply(minb));
                break;

            case NumOperation.DIVIDE:
                // Value is infinite
                if (minb.longValue() <= 0 && 0 <= maxb.longValue()) {
                    min = BigInteger.valueOf(Long.MIN_VALUE);
                    max = BigInteger.valueOf(Long.MAX_VALUE);
                }
                else {
	                min = maxa.divide(maxb);
	                min = min.min(mina.divide(minb));
	                min = min.min(mina.divide(maxb));
	                min = min.min(maxa.divide(minb));
	                max = maxa.divide(maxb);
	                max = max.max(mina.divide(minb));
	                max = max.max(mina.divide(maxb));
	                max = max.max(maxa.divide(minb));
                }
                break;

            case NumOperation.SQUARE:
                if (mina.longValue() <= 0 && 0 <= maxa.longValue())
                    min = BigInteger.ZERO;
                else {
                    min = maxa.multiply(maxa);
                    min = min.min(mina.multiply(mina));
                }
                max = maxa.multiply(maxa);
                max = max.max(mina.multiply(mina));
                break;

            case NumOperation.ABS:
                if (maxa.longValue() < 0)
            		min = maxa.abs();
            	else if (mina.longValue() > 0)
            		min = mina.abs();
            	else
            		min = BigInteger.ZERO;
            
            	max = maxa.abs().max(mina.abs());
                break;
        }

        // Check for overflow
        if (min.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
            this.min = Long.MAX_VALUE;
        if (min.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            this.min = Long.MIN_VALUE;
        if (max.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
            this.max = Long.MAX_VALUE;
        if (max.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0)
            this.max = Long.MIN_VALUE;

        this.min = min.longValue();
        this.max = max.longValue();
        needsRecalc = false;
    }

    /**
     * Returns maximum value of domain
     */
    public long getMax() {
        if (needsRecalc) recalc();
        return max;
    }

    /**
     * Returns minimum value of domain
     */
    public long getMin() {
        if (needsRecalc) recalc();
        return min;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(long val) {
        if (needsRecalc) recalc();
        return min <= val && val <= max;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public long getNextHigher(long val) {
        if (needsRecalc) recalc();
        if (val < min) return min;
        if (val >= max) return val;
        return val + 1;
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public long getNextLower(long val) {
        if (needsRecalc) recalc();
        if (val > max) return max;
        if (val <= min) return val;
        return val - 1;
    }

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        return toSet().values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        return getDeltaSet().values();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet() {
        if (needsRecalc) recalc();
        
        LongIntervalSet set = new LongIntervalSet();
        set.add(min, max);
        return set;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return new LongIntervalSet();
    }
    
    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        // computed domain - do nothing
    }

    /**
     * Returns string representation of domain
     */
    public String toString() {
        if (needsRecalc) recalc();
        StringBuffer buf = new StringBuffer();

        buf.append("[");
        buf.append(min);
        if (min != max) {
            buf.append("..");
            buf.append(max);
        }
        buf.append("]");

        return buf.toString();
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        // computed domain - do nothing
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        return false;
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(long val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(long val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(long val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(long val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(long start, long end) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(long start, long end) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet vals) throws PropagationFailureException {
        // computed domain - do nothing
    }
    
    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
	public Object getDomainState() {
		// computed domain - return null
		return null;
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public void restoreDomainState(Object state) {
		// computed domain - do nothing
	}
	
    /**
     * Helper class to listen when DOM A or DOM B changes
     */
    private class DelegatedDomainListener implements DomainChangeListener {
        /**
         * Method invoked by domain when a domain change event is fired
         */
        public void domainChange(DomainChangeEvent ev) throws PropagationFailureException {
            needsRecalc = true;
            notifyRangeChange();
        }
        
        public void choicePointPop() {
            needsRecalc = true;
            notifyChoicePointPop();
        }
        
        public void choicePointPush() {
            needsRecalc = true;
            notifyChoicePointPush();
        }
    }

    public Object clone() {
        return new LongComputedDomain(domA, constA, domB, constB, operation);
    }
}