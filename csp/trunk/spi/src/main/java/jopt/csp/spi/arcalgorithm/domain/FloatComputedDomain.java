package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.util.FloatIntervalSet;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

public class FloatComputedDomain extends AbstractDomain implements FloatDomain {
    private FloatDomain domA;
    private float constA;
    private FloatDomain domB;
    private float constB;
    private int operation;
    private float min;
    private float max;
    private boolean needsRecalc;
    private DelegatedDomainListener changeListener;
    private float precision;
    
    private FloatComputedDomain(FloatDomain domA, float constA, FloatDomain domB, float constB, int operation) {
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

    public FloatComputedDomain(FloatDomain domA, FloatDomain domB, int operation) {
        this(domA, 0, domB, 0, operation);
    }
    public FloatComputedDomain(float a, FloatDomain domB, int operation) {
        this(null, a, domB, 0, operation);
    }
    public FloatComputedDomain(FloatDomain domA, float b, int operation) {
        this(domA, 0, null, b, operation);
    }
    public FloatComputedDomain(FloatDomain domA, int operation) {
        this(domA, 0, null, 0, operation);
    }

    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(float p) {
        this.precision = p;
    }
    
    /**
     * Returns precision associated with this domain
     */
    public float getPrecision() {
        return precision;
    }
    
    /**
     * Returns size of domain
     */
    public int getSize() {
        if (needsRecalc) recalc();
        if (isBound()) return 1;
        else return Integer.MAX_VALUE;
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
        double min = 0;
        double max = 0;
        double mina = (domA != null) ? domA.getMin() : constA;
        double maxa = (domA != null) ? domA.getMax() : constA;
        double minb = (domB != null) ? domB.getMin() : constB;
        double maxb = (domB != null) ? domB.getMax() : constB;

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
                    min = Float.NEGATIVE_INFINITY;
                    max = Float.POSITIVE_INFINITY;
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

        // Watch for overflow
        if (min > Float.MAX_VALUE)
            min = Float.POSITIVE_INFINITY;
        else if (min < -Float.MAX_VALUE)
            min = Float.NEGATIVE_INFINITY;
        if (max > Float.MAX_VALUE)
            max = Float.POSITIVE_INFINITY;
        else if (max < -Float.MAX_VALUE)
            max = Float.NEGATIVE_INFINITY;

        this.min = (float) min;
        this.max = (float) max;
        needsRecalc = false;
    }

    /**
     * Returns maximum value of domain
     */
    public float getMax() {
        if (needsRecalc) recalc();
        return max;
    }

    /**
     * Returns minimum value of domain
     */
    public float getMin() {
        if (needsRecalc) recalc();
        return min;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(float val) {
        if (needsRecalc) recalc();
        return min <= val && val <= max;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
        if (needsRecalc) recalc();
        if (val < min) return min;
        if (val >= max) return val;
        return FloatUtil.next(val + precision);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public float getNextLower(float val) {
        if (needsRecalc) recalc();
        if (val > max) return max;
        if (val <= min) return val;
        return FloatUtil.previous(val - precision);
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
        
        FloatIntervalSet set = new FloatIntervalSet();
        set.add(min, max);
        return set;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return new FloatIntervalSet();
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
    public void setMax(float val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(float val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(float val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(float val) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(float start, float end) throws PropagationFailureException {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(float start, float end) throws PropagationFailureException {
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
        return new FloatComputedDomain(domA, constA, domB, constB, operation);
    }
}