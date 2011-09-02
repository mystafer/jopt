package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.util.NumConstants;
import jopt.csp.util.IntSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for integer domains
 */
public abstract class BaseIntDomain extends BaseNumDomain implements IntDomain {
    protected IntSet values;
    protected IntSet delta;
    protected IntSet deltaAddedRecord;
    protected IntSet deltaRemovedRecord;
    
    
    /**
     * Constructor for cloning
     */
    protected BaseIntDomain() {
        super(NumConstants.INTEGER);
        this.values = (IntSet) super.values;
        this.delta = (IntSet) super.delta;
        this.deltaAddedRecord = (IntSet) super.deltaAddedRecord;
        this.deltaRemovedRecord = (IntSet) super.deltaRemovedRecord;
    }
    
    /**
     * Returns maximum value of domain
     */
    public int getMax() {
        return values.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public int getMin() {
        return values.getMin();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return values.size() == 1;
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(int val) {
        return values.contains(val);
    }

    /**
     * Attempts to reduce domain by setting a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(int val) throws PropagationFailureException {
        // check for valid value
        if (val < values.getMin())
            throw new PropagationFailureException();

        // check if value will trigger a change
        int prevMax = values.getMax();
        if (val < prevMax) {
            values.removeStartingFrom(val + 1);
            // check if changes occurred
            if (isChangeDetected()) {
                if (isBound())
                    notifyValueChange(0, this.getMax()-prevMax);
                else
                	notifyRangeChange(0, this.getMax()-prevMax);
            }
        }
    }
    
    /**
     * Attempts to reduce domain by setting a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(int val) throws PropagationFailureException {
        // check for valid value
        if (val > values.getMax())
            throw new PropagationFailureException();

        int prevMin = values.getMin();
        // check if value will trigger a change
        if (val > prevMin) {
            values.removeEndingAt(val-1);
            if (isChangeDetected()) {
                if (isBound())
                    notifyValueChange(this.getMin()-prevMin, 0);
                else
                	notifyRangeChange(this.getMin()-prevMin, 0);
            }
        }
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(int val) throws PropagationFailureException {
        // check if value is in set
        if (values.contains(val)) {
            int prevMax = values.getMax();
            int prevMin = values.getMin();
            // reduce set to single value
            if (values.size()>1) { 
                values.removeEndingAt(val-1);
                values.removeStartingFrom(val + 1);
                
                if ((this.getMin()!=prevMin) || (this.getMax()!=prevMax)) {
                    notifyValueChange(this.getMin()-prevMin,this.getMax()-prevMax);
                }
            }
        }
        
        // value is not in set
        else
        	throw new PropagationFailureException();
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(int val) throws PropagationFailureException {
        // record previous size of set
        int prevMin = values.getMin();
        int prevMax = values.getMax();
        
        // remove the value
        values.remove(val);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            int min = values.getMin();
            int max = values.getMax();
            
            // check if bound
            if (min == max)
                notifyValueChange(min-prevMin, max-prevMax);
            
            // check if min or max changed
        	else if (prevMin!=min || prevMax!=max)
                notifyRangeChange(min-prevMin, max-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(int start, int end) throws PropagationFailureException {
        // record previous size of set
        int prevMin = values.getMin();
        int prevMax = values.getMax();
        
        // remove values
        values.removeEndingBefore(start);
        values.removeStartingAfter(end);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            int min = values.getMin();
            int max = values.getMax();
            
            // check if bound
            if (min == max)
                notifyValueChange(min-prevMin, max-prevMax);
            
            // check if min or max changed
            else if (prevMin!=min || prevMax!=max)
                notifyRangeChange(min-prevMin, max-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(int start, int end) throws PropagationFailureException {
        // record previous size of set
        int prevMin = values.getMin();
        int prevMax = values.getMax();
        
        // remove values
        values.remove(start, end);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            int min = values.getMin();
            int max = values.getMax();
            
            // check if bound
            if (min == max)
                notifyValueChange(min-prevMin, max-prevMax);
            
            // check if min or max changed
            else if (prevMin!=min || prevMax!=max)
                notifyRangeChange(min-prevMin, max-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        // record previous size of set
        int prevMin = values.getMin();
        int prevMax = values.getMax();
        
        // remove values
        values.retainAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            int min = values.getMin();
            int max = values.getMax();
            
            // check if bound
            if (min == max)
                notifyValueChange(min-prevMin, max-prevMax);
            
            // check if min or max changed
            else if (prevMin!=min || prevMax!=max)
                notifyRangeChange(min-prevMin, max-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet s) throws PropagationFailureException {
        // record previous size of set
        int prevMin = values.getMin();
        int prevMax = values.getMax();
        
        // remove values
        values.removeAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            int min = values.getMin();
            int max = values.getMax();
            
            // check if bound
            if (min == max)
                notifyValueChange(min-prevMin, max-prevMax);
            
            // check if min or max changed
            else if (prevMin!=min || prevMax!=max)
                notifyRangeChange(min-prevMin, max-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public int getNextHigher(int val) {
        return values.getNextHigher(val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public int getNextLower(int val) {
        return values.getNextLower(val);
    }

    public void dump() {
        System.out.println(this + ": " + changed + ", Min[" + getMin() + "], Max[" + getMax() + "], Bound[" + isBound() + "], Size[" + getSize() + "]");
    }
    
}
