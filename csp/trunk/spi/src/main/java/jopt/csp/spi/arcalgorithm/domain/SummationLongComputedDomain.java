package jopt.csp.spi.arcalgorithm.domain;

import java.math.BigInteger;
import java.util.Arrays;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.util.LongIntervalSet;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

public class SummationLongComputedDomain extends AbstractDomain implements LongDomain {
    private SummationLongDomainExpression aexpr;
    private CspGenericIndexRestriction idxRestriction;
    private IndexIterator idxIter;
    private long min;
    private long max;
    private boolean needsRecalc;

    /**
     * Constructor
     */
    public SummationLongComputedDomain(SummationLongDomainExpression aexpr, CspGenericIndexRestriction idxRestriction) {
        this.aexpr = aexpr;
        this.idxRestriction = idxRestriction;
        this.idxIter = new IndexIterator(Arrays.asList(aexpr.getIndices()));
        this.needsRecalc = true;
        
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
     * Returns size of domain
     */
    public int getSize() {
        if (needsRecalc) recalc();
        BigInteger s = BigInteger.valueOf(max).subtract(BigInteger.valueOf(min));
        if (s.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0)
            return Integer.MAX_VALUE;
        return s.intValue() + 1;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        if (needsRecalc) recalc();
        return max == min;
    }

    /**
     * Recalculates min and max values
     */
    private void recalc() {
        this.min = 0;
        this.max = 0;
        idxIter.reset();
    	while(idxIter.hasNext()) {
            idxIter.next();
    	    if(idxRestriction == null || idxRestriction.currentIndicesValid()) {
    	        min += aexpr.getDomainMinForIndex();
    	        max += aexpr.getDomainMaxForIndex();
    	    }
    	}
        needsRecalc = false;
    }

    /**
     * Returns the maximum value of domain
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
        return new SummationLongComputedDomain(aexpr, idxRestriction);
    }
}