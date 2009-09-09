package jopt.csp.spi.arcalgorithm.domain;

import java.util.Arrays;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.util.FloatIntervalSet;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

public class SummationFloatComputedDomain extends AbstractDomain implements FloatDomain {
    private SummationFloatDomainExpression aexpr;
    private CspGenericIndexRestriction idxRestriction;
    private IndexIterator idxIter;
    private float min;
    private float max;
    private boolean needsRecalc;
    private float precision;

    /**
     * Constructor
     */
    public SummationFloatComputedDomain(SummationFloatDomainExpression aexpr, CspGenericIndexRestriction idxRestriction) {
        this.aexpr = aexpr;
        this.idxRestriction = idxRestriction;
        this.idxIter = new IndexIterator(Arrays.asList(aexpr.getIndices()));
        this.needsRecalc = true;
        this.precision = FloatUtil.DEFAULT_PRECISION;
        
        // register domain listeners
        DelegatedDomainListener domainListener = new DelegatedDomainListener();
        idxIter.reset();
    	while(idxIter.hasNext()) {
            idxIter.next();
    	    if(idxRestriction == null || idxRestriction.currentIndicesValid()) {
    	        DomainChangeSource dcs = aexpr.getDomainChangeSourceForIndex();
    	        dcs.addRangeChangeListener(domainListener);
    	    }
    	}
    }

    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(float p) {
        if (p <= FloatUtil.DEFAULT_PRECISION)
            this.precision = FloatUtil.DEFAULT_PRECISION;
        else
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
        if (isBound()) return 1;
        else return Integer.MAX_VALUE;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        if (needsRecalc) recalc();
        return FloatUtil.isEqual(max, min, precision);
    }

    /**
     * Recalculates min and max values
     */
    private void recalc() {
        double min = 0;
        double max = 0;
        idxIter.reset();
    	while(idxIter.hasNext()) {
            idxIter.next();
    	    if(idxRestriction == null || idxRestriction.currentIndicesValid()) {
    	        min += aexpr.getDomainMinForIndex();
    	        max += aexpr.getDomainMaxForIndex();
    	    }
    	}
    	
        // Check for overflow
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
     * Returns the maximum value of domain
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
        return FloatUtil.compare(min, val, precision) <= 0 &&
               FloatUtil.compare(val, max, precision) <= 0;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
        if (needsRecalc) recalc();
        if (FloatUtil.compare(val, min, precision) < 0) return min;
        if (FloatUtil.compare(val, max, precision) >= 0) return val;
        return FloatUtil.next(val + precision);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public float getNextLower(float val) {
        if (needsRecalc) recalc();
        if (FloatUtil.compare(val, max, precision) > 0) return max;
        if (FloatUtil.compare(val, min, precision) <= 0) return val;
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
     */
    public void setMin(float val) {
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
     * Helper class to listen when any child domain changes
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
        return new SummationFloatComputedDomain(aexpr, idxRestriction);
    }
}