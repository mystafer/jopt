package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.util.NumConstants;
import jopt.csp.util.LongSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for long domains
 */
public abstract class BaseLongDomain extends BaseNumDomain implements LongDomain {
    protected LongSet values;
    protected LongSet delta;
    protected LongSet deltaAddedRecord;
    protected LongSet deltaRemovedRecord;

    /**
     * Constructor for cloning
     */
    protected BaseLongDomain() {
        super(NumConstants.LONG);
        this.values = (LongSet) super.values;
        this.delta = (LongSet) super.delta;
        this.deltaAddedRecord = (LongSet) super.deltaAddedRecord;
        this.deltaRemovedRecord = (LongSet) super.deltaRemovedRecord;
    }
    
    /**
     * Returns maximum value of domain
     */
    public long getMax() {
        return values.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public long getMin() {
        return values.getMin();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return values.getMax() == values.getMin();
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(long val) {
        return values.contains(val);
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(long val) throws PropagationFailureException {
        // check for valid value
        if (val < values.getMin())
            throw new PropagationFailureException();

        // check if value will trigger a change
        long prevMax = values.getMax();
        if (val < prevMax) {
            values.removeStartingFrom(val + 1);
            
            if (isChangeDetected()) {
                if (isBound())
                    notifyValueChange(0, this.getMax()-prevMax);
                else
                	notifyRangeChange(0, this.getMax()-prevMax);
            }
        }
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(long val) throws PropagationFailureException {
        // check for valid value
        if (val > values.getMax())
            throw new PropagationFailureException();

        // check if value will trigger a change
        long prevMin = values.getMin();
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
    public void setValue(long val) throws PropagationFailureException {
        // check if value is in set
        if (values.contains(val)) {
            long prevMax = values.getMax();
            long prevMin = values.getMin();
            // reduce set to single value
            if (values.size()>1) { 
                values.removeEndingAt(val-1);
                values.removeStartingFrom(val + 1);
                
                if ((this.getMin()!=prevMin) || (this.getMax()!=prevMax)) {   
                    notifyValueChange(this.getMin()-prevMin,
                                    this.getMax()-prevMax);
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
    public void removeValue(long val) throws PropagationFailureException {
        // record previous size of set
        long prevMin = values.getMin();
        long prevMax = values.getMax();
        
        // remove the value
        values.remove(val);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            long min = values.getMin();
            long max = values.getMax();
            
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
    public void setRange(long start, long end) throws PropagationFailureException {
        // record previous size of set
        long prevMin = values.getMin();
        long prevMax = values.getMax();
        
        // remove values
        values.removeEndingBefore(start);
        values.removeStartingAfter(end);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            long min = values.getMin();
            long max = values.getMax();
            
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
    public void removeRange(long start, long end) throws PropagationFailureException {
        // record previous size of set
        long prevMin = values.getMin();
        long prevMax = values.getMax();
        
        // remove values
        values.remove(start, end);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            long min = values.getMin();
            long max = values.getMax();
            
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
        long prevMin = values.getMin();
        long prevMax = values.getMax();
        
        // remove values
        values.retainAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            long min = values.getMin();
            long max = values.getMax();
            
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
        long prevMin = values.getMin();
        long prevMax = values.getMax();
        
        // remove values
        values.removeAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            long min = values.getMin();
            long max = values.getMax();
            
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
    public long getNextHigher(long val) {
        return values.getNextHigher(val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public long getNextLower(long val) {
        return values.getNextHigher(val);
    }

    public void dump() {
    	System.out.println(this + ": " + changed + ", Min[" + getMin() + "], Max[" + getMax() + "], Bound[" + isBound() + "], Size[" + getSize() + "]");
    }
}
