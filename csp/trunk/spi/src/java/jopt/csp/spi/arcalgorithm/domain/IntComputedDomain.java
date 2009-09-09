package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

public class IntComputedDomain extends AbstractDomain implements IntDomain {
    private IntDomain domA;
    private int constA;
    private IntDomain domB;
    private int constB;
    private int operation;
    private int min;
    private int max;
    private boolean needsRecalc;
    private DelegatedDomainListener changeListener;

    /**
     * Internal constructor
     */
    private IntComputedDomain(IntDomain domA, int constA, IntDomain domB, int constB, int operation) {
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

    public IntComputedDomain(IntDomain domA, IntDomain domB, int operation) {
        this(domA, 0, domB, 0, operation);
    }
    public IntComputedDomain(int a, IntDomain domB, int operation) {
        this(null, a, domB, 0, operation);
    }
    public IntComputedDomain(IntDomain domA, int b, int operation) {
        this(domA, 0, null, b, operation);
    }
    public IntComputedDomain(IntDomain domA, int operation) {
        this(domA, 0, null, 0, operation);
    }

    // javadoc inherited from NumDomain
    public void setPrecision(double p) {
    }
    
    // javadoc inherited from NumDomain
    public double getPrecision() {
        return 0;
    }
    
    /**
     * Returns size of domain
     */
    public int getSize() {
        if (needsRecalc) recalc();
        return max - min + 1;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        if (needsRecalc) recalc();
        return (max==min);
    }

    /**
     * Recalculates min and max values; done simultaneously for efficiency
     */
    private void recalc() {
        long min = 0;
        long max = 0;
        long mina = (domA != null) ? domA.getMin() : constA;
        long maxa = (domA != null) ? domA.getMax() : constA;
        long minb = (domB != null) ? domB.getMin() : constB;
        long maxb = (domB != null) ? domB.getMax() : constB;

        switch(operation) {
            case NumOperation.ADD:
                min = mina + minb;
            	max = maxa + maxb;
                break;

            case NumOperation.SUBTRACT:
                min = mina - maxb;
            	max = maxa - minb;
                break;

            case NumOperation.MULTIPLY:
                min = Math.min(maxa * maxb, mina * minb);
                min = Math.min(min, mina * maxb);
                min = Math.min(min, maxa * minb);
                max = Math.max(maxa * maxb, mina * minb);
                max = Math.max(max, mina * maxb);
                max = Math.max(max, maxa * minb);
                break;

            case NumOperation.DIVIDE:
                // Value is infinite
                if (minb <= 0 && 0 <= maxb) {
                    min = Integer.MIN_VALUE;
                    max = Integer.MAX_VALUE;
                }
                else {
	                min = Math.min(maxa / maxb, mina / minb);
	                min = Math.min(min, mina / maxb);
	                min = Math.min(min, maxa / minb);
	                max = Math.max(maxa / maxb, mina / minb);
	                max = Math.max(max, mina / maxb);
	                max = Math.max(max, maxa / minb);
                }
                break;

            case NumOperation.SQUARE:
                if (mina <= 0 && 0 <= maxa)
                    min = 0;
                else
                    min = Math.min(maxa * maxa, mina * mina);
            
                max = Math.max(maxa * maxa, mina * mina);
                break;

            case NumOperation.ABS:
            	if (maxa < 0)
            		min = Math.abs(maxa);
            	else if (mina > 0)
            		min = Math.abs(mina);
            	else
            		min = 0;
            	
            	max = Math.max(Math.abs(maxa), Math.abs(mina));
                break;
        }

        // Check for overflow
        if (min > Integer.MAX_VALUE)
            min = Integer.MAX_VALUE;
        else if (min < Integer.MIN_VALUE)
            min = Integer.MIN_VALUE;
        if (max > Integer.MAX_VALUE)
            max = Integer.MAX_VALUE;
        else if (max < Integer.MIN_VALUE)
            max = Integer.MIN_VALUE;

        this.min = (int) min;
        this.max = (int) max;
        needsRecalc = false;
    }

    /**
     * Returns the maximum value of domain
     */
    public int getMax() {
        if (needsRecalc) recalc();
        return max;
    }

    /**
     * Returns minimum value of domain
     */
    public int getMin() {
        if (needsRecalc) recalc();
        return min;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Number val) {
        if (needsRecalc) recalc();
        return isInDomain(val.intValue());
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(int val) {
        if (needsRecalc) recalc();
        return min <= val && val <= max;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public int getNextHigher(int val) {
        if (needsRecalc) recalc();
        if (val < min) return min;
        if (val >= max) return val;
        return val + 1;
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public int getNextLower(int val) {
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
        
        IntIntervalSet set = new IntIntervalSet();
        set.add(min, max);
        return set;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return new IntIntervalSet();
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
    public void setMax(int val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(int val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(int val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(int val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(int start, int end) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(int start, int end) throws PropagationFailureException {
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
        return new IntComputedDomain(domA, constA, domB, constB, operation);
    }
}