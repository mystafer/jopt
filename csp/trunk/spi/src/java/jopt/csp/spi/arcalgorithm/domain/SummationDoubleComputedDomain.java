package jopt.csp.spi.arcalgorithm.domain;

import java.util.Arrays;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.util.DoubleIntervalSet;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.PropagationFailureException;

public class SummationDoubleComputedDomain extends AbstractDomain implements DoubleDomain {
    private SummationDoubleDomainExpression aexpr;
    private CspGenericIndexRestriction idxRestriction;
    private IndexIterator idxIter;
    private double min;
    private double max;
    private boolean needsRecalc;
    private double precision;

    /**
     * Constructor
     */
    public SummationDoubleComputedDomain(SummationDoubleDomainExpression aexpr, CspGenericIndexRestriction idxRestriction) {
        this.aexpr = aexpr;
        this.idxRestriction = idxRestriction;
        this.idxIter = new IndexIterator(Arrays.asList(aexpr.getIndices()));
        this.needsRecalc = true;
        this.precision = DoubleUtil.DEFAULT_PRECISION;
        
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
    public void setPrecision(double p) {
        if (p <= DoubleUtil.DEFAULT_PRECISION)
            this.precision = DoubleUtil.DEFAULT_PRECISION;
        else
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
        if (isBound()) return 1;
        else return Integer.MAX_VALUE;
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        if (needsRecalc) recalc();
        return DoubleUtil.isEqual(max, min, precision);
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
        return DoubleUtil.compare(min, val, precision) <= 0 &&
               DoubleUtil.compare(val, max, precision) <= 0;
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        if (needsRecalc) recalc();
        if (DoubleUtil.compare(val, min, precision) < 0) return min;
        if (DoubleUtil.compare(val, max, precision) >= 0) return val;
        return DoubleUtil.next(val + precision);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
        if (needsRecalc) recalc();
        if (DoubleUtil.compare(val, max, precision) > 0) return max;
        if (DoubleUtil.compare(val, min, precision) <= 0) return val;
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
     *
     */
    public void setMax(double val) {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     */
    public void setMin(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     */
    public void setValue(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to remove a single value from the domain
     *
     */
    public void removeValue(double val){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     */
    public void setRange(double start, double end) {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     */
    public void removeRange(double start, double end){
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     */
    public void setDomain(NumSet s) {
        // computed domain - do nothing
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     */
    public void removeDomain(NumSet vals){
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
        return new SummationDoubleComputedDomain(aexpr, idxRestriction);
    }
}