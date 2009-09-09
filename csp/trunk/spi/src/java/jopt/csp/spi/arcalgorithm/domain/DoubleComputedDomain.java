package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.util.DoubleIntervalSet;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

public class DoubleComputedDomain extends AbstractDomain implements DoubleDomain {
    private DoubleDomain domA;
    private DoubleDomain domB;
    private double constA;
    private double constB;
    private int operation;
    private double min;
    private double max;
    private boolean needsRecalc;
    private DelegatedDomainListener changeListener;
    private double precision;

    private DoubleComputedDomain(DoubleDomain domA, double constA, DoubleDomain domB, double constB, int operation) {
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

    public DoubleComputedDomain(DoubleDomain domA, DoubleDomain domB, int operation) {
        this(domA, 0, domB, 0, operation);
    }
    public DoubleComputedDomain(double a, DoubleDomain domB, int operation) {
        this(null, a, domB, 0, operation);
    }
    public DoubleComputedDomain(DoubleDomain domA, double b, int operation) {
        this(domA, 0, null, b, operation);
    }
    public DoubleComputedDomain(DoubleDomain domA, int operation) {
        this(domA, 0, null, 0, operation);
    }

    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(double p) {
        this.precision = p;
    }
    
    /**
     * Returns precision associated with this domain
     */
    public double getPrecision() {
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
        min = 0;
        max = 0;
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

            case NumOperation.POWER:
                min = Math.pow(mina, minb);
	            min = Double.isNaN(min) ? Math.pow(mina, maxb) : Math.min(min, Math.pow(mina, maxb));
	            min = Double.isNaN(min) ? Math.pow(maxa, minb) : Math.min(min, Math.pow(maxa, minb));
	            min = Double.isNaN(min) ? Math.pow(maxa, maxb) : Math.min(min, Math.pow(maxa, maxb));
                max = Math.pow(mina, minb);
                max = Double.isNaN(max) ? Math.pow(mina, maxb) : Math.max(max, Math.pow(mina, maxb));
                max = Double.isNaN(max) ? Math.pow(maxa, minb) : Math.max(max, Math.pow(maxa, minb));
                max = Double.isNaN(max) ? Math.pow(maxa, maxb) : Math.max(max, Math.pow(maxa, maxb));
                break;

            case NumOperation.EXP:
                min = Math.exp(mina);
            	min = Double.isNaN(min) ? Math.exp(maxa) : Math.min(min, Math.exp(maxa));
                max = Math.exp(mina);
                max = Double.isNaN(max) ? Math.exp(maxa) : Math.max(max, Math.exp(maxa));
                break;

            case NumOperation.NAT_LOG:
                min = Math.log(mina);
            	min = Double.isNaN(min) ? Math.log(maxa) : Math.min(min, Math.log(maxa));
                max = Math.log(mina);
                max = Double.isNaN(max) ? Math.log(maxa) : Math.max(max, Math.log(maxa));
                break;

            case NumOperation.SIN:
                min = Math.sin(mina);
            	min = Double.isNaN(min) ? Math.sin(maxa) : Math.min(min, Math.sin(maxa));
                max = Math.sin(mina);
                max = Double.isNaN(max) ? Math.sin(maxa) : Math.max(max, Math.sin(maxa));
                break;

            case NumOperation.COS:
                min = Math.cos(mina);
            	min = Double.isNaN(min) ? Math.cos(maxa) : Math.min(min, Math.cos(maxa));
                max = Math.cos(mina);
                max = Double.isNaN(max) ? Math.cos(maxa) : Math.max(max, Math.cos(maxa));
                break;

            case NumOperation.TAN:
                min = Math.tan(mina);
            	min = Double.isNaN(min) ? Math.tan(maxa) : Math.min(min, Math.tan(maxa));
                max = Math.tan(mina);
                max = Double.isNaN(max) ? Math.tan(maxa) : Math.max(max, Math.tan(maxa));
                break;

            case NumOperation.ASIN:
                min = Math.asin(mina);
            	min = Double.isNaN(min) ? Math.asin(maxa) : Math.min(min, Math.asin(maxa));
                max = Math.asin(mina);
                max = Double.isNaN(max) ? Math.asin(maxa) : Math.max(max, Math.asin(maxa));
                break;

            case NumOperation.ACOS:
                min = Math.acos(mina);
            	min = Double.isNaN(min) ? Math.acos(maxa) : Math.min(min, Math.acos(maxa));
                max = Math.acos(mina);
                max = Double.isNaN(max) ? Math.acos(maxa) : Math.max(max, Math.acos(maxa));
                break;

            case NumOperation.ATAN:
                min = Math.atan(mina);
            	min = Double.isNaN(min) ? Math.atan(maxa) : Math.min(min, Math.atan(maxa));
                max = Math.atan(mina);
                max = Double.isNaN(max) ? Math.atan(maxa) : Math.max(max, Math.atan(maxa));
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

        needsRecalc = false;
    }

    /**
     * Returns maximum value of domain
     */
    public double getMax() {
        if (needsRecalc) recalc();
        return max;
    }

    /**
     * Returns minimum value of domain
     */
    public double getMin() {
        if (needsRecalc) recalc();
        return min;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(double val) {
        if (needsRecalc) recalc();
        return min <= val && val <= max;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        if (needsRecalc) recalc();
        if (val < min) return min;
        if (val >= max) return val;
        return DoubleUtil.next(val + precision);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
        if (needsRecalc) recalc();
        if (val > max) return max;
        if (val <= min) return val;
        return DoubleUtil.previous(val - precision);
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
        
        DoubleIntervalSet set = new DoubleIntervalSet();
        set.add(min, max);
        return set;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return new DoubleIntervalSet();
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
     */
    public void setMax(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a minimum value.
     */
    public void setMin(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a single value.
     */
    public void setValue(double val) {
        // computed domain - do nothing
    }

    /**
     * Attempts to remove a single value from the domain
     */
    public void removeValue(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to within a range of values
     */
    public void setRange(double start, double end){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain by removing a range of values
     */
    public void removeRange(double start, double end) {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     */
    public void setDomain(NumSet s){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     */
    public void removeDomain(NumSet vals) {
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
        return new DoubleComputedDomain(domA, constA, domB, constB, operation);
    }
}