package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.util.NumConstants;
import jopt.csp.util.FloatSet;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.NumSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for float domains
 */
public abstract class BaseFloatDomain extends BaseNumDomain implements FloatDomain {
    protected FloatSet values;
    protected FloatSet delta;
    protected FloatSet deltaAddedRecord;
    protected FloatSet deltaRemovedRecord;
    protected float precision;
    
    /**
     * Constructor for cloning
     */
    protected BaseFloatDomain() {
        super(NumConstants.FLOAT);
        this.values = (FloatSet) super.values;
        this.delta = (FloatSet) super.delta;
        this.deltaAddedRecord = (FloatSet) super.deltaAddedRecord;
        this.deltaRemovedRecord = (FloatSet) super.deltaRemovedRecord;
        this.precision = FloatUtil.DEFAULT_PRECISION;
    }
    
    /**
     * Returns maximum value of domain
     */
    public float getMax() {
        return values.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public float getMin() {
        return values.getMin();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return FloatUtil.isEqual(values.getMin(), values.getMax(), precision);
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(float val) {
        return values.contains(val);
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(float val) throws PropagationFailureException {
        // check for valid value
        int cmp = FloatUtil.compare(val, values.getMin(), precision);
        if (cmp == 0) val = values.getMin();
        if (cmp < 0) throw new PropagationFailureException();

        // check if value will trigger a change
        float prevMax = values.getMax();
        if (FloatUtil.compare(val, prevMax, precision) < 0) {
            values.removeStartingAfter(val);
            
            if (isChangeDetected())
                if (isBound())
                    notifyValueChange(0f, this.getMax()-prevMax);
                else
                    notifyRangeChange(0f, this.getMax()-prevMax);
        }
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(float val) throws PropagationFailureException {
        // check for valid value
        int cmp = FloatUtil.compare(val, values.getMax(), precision);
        if (cmp == 0) val = values.getMax();
        if (cmp > 0) throw new PropagationFailureException();

        // check if value will trigger a change
        float prevMin = values.getMin();
        if (FloatUtil.compare(val, values.getMin(), precision) > 0) {
            values.removeEndingBefore(val);
            
            if (isChangeDetected()) {
                if (isBound())
                    notifyValueChange(this.getMin()-prevMin, 0f);
                else
                	notifyRangeChange(this.getMin()-prevMin, 0f);
            }
        }
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(float val) throws PropagationFailureException {
        // check if value is in set
    	if (!values.contains(val)) {
    		float nh = values.getNextHigher(val);
    		float nl = values.getNextLower(val);
    		
    		// check if next higher and next lower are equivalent in precision
    		boolean nhOk = nh != val && FloatUtil.isEqual(nh, val, precision);
    		boolean nlOk = nl != val && FloatUtil.isEqual(nl, val, precision);
    		
    		// value is invalid
    		if (!nhOk && !nlOk)
            	throw new PropagationFailureException();
    		
    		// both are ok, choose smallest difference
    		if (nhOk && nlOk) {
    			if (nh-val < val-nl)
    				val = nh;
    			else
    				val = nl;
    		}
    		
    		// next higher is valid, lower is not
    		else if (nhOk)
    			val = nh;
    		
    		// next lower is valid, higher is not
    		else if (nlOk)
    			val = nl;
    	}
    	
    	float prevMin = this.getMin();
    	float prevMax = this.getMax();
        
        // reduce set to single value
        if (values.size()>1) { 
            values.removeEndingBefore(val);
            values.removeStartingAfter(val);
            
            if ((this.getMin()!=prevMin) || (this.getMax()!=prevMax)) { 
                notifyValueChange(this.getMin()-prevMin,this.getMax()-prevMax);
            }
        }
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(float val) throws PropagationFailureException {
    	removeRange(val, val);
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(float start, float end) throws PropagationFailureException {
    	// check if setting to a single value
    	if (start==end) {
    		setValue(start);
    	}
    	
    	// set to range of values
    	else {
	        // record previous size of set
	        float prevMin = values.getMin();
	        float prevMax = values.getMax();
	        
	        // remove values
        	if (!FloatUtil.isEqual(start, prevMin, precision)) {
		        if (FloatUtil.isEqual(start, prevMax, precision)) {
	        		values.removeEndingBefore(prevMax);
		        }
		        else
		        	values.removeEndingBefore(start);
        	}
	        
        	if (!FloatUtil.isEqual(end, prevMax, precision)) {
		        if (FloatUtil.isEqual(end, prevMin, precision)) {
	        		values.removeStartingAfter(prevMin);
		        }
		        else
		        	values.removeStartingAfter(end);
        	}
	        
	        // notify of change
	        if (isChangeDetected()) {
	            // check that set is not null
	            int newSize = values.size();
	            if (newSize==0)
	                throw new PropagationFailureException();
	            
	            float min = values.getMin();
	            float max = values.getMax();
	            
	            // check if bound
	            if (FloatUtil.isEqual(min, max, precision))
	                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
	            
	            // check if min or max changed
	            else if (!FloatUtil.isEqual(prevMin, min, precision) || !FloatUtil.isEqual(prevMax, max, precision))
	                notifyRangeChange(this.getMin()-prevMin, this.getMax()-prevMax);
	            
	            // some other value in domain was removed
	            else notifyDomainChange();
	        }
    	}
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(float start, float end) throws PropagationFailureException {
        // record previous size of set
        float prevMin = values.getMin();
        float prevMax = values.getMax();
        
        // remove values
        values.remove(start - precision, end + precision);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            float min = values.getMin();
            float max = values.getMax();
            
            // check if bound
            if (FloatUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!FloatUtil.isEqual(prevMin, min, precision) || !FloatUtil.isEqual(prevMax, max, precision))
                notifyRangeChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
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
        float prevMin = values.getMin();
        float prevMax = values.getMax();
        
        // remove values
        values.retainAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            float min = values.getMin();
            float max = values.getMax();
            
            // check if bound
            if (FloatUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!FloatUtil.isEqual(prevMin, min, precision) || !FloatUtil.isEqual(prevMax, max, precision))
                notifyRangeChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
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
        float prevMin = values.getMin();
        float prevMax = values.getMax();
        
        // remove values
        values.removeAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            float min = values.getMin();
            float max = values.getMax();
            
            // check if bound
            if (FloatUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!FloatUtil.isEqual(prevMin, min, precision) || !FloatUtil.isEqual(prevMax, max, precision))
                notifyRangeChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
        return values.getNextHigher(val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public float getNextLower(float val) {
        return values.getNextHigher(val);
    }

    /**
     * Sets precision of set
     */
    public final void setPrecision(float precision) {
    	if (precision < FloatUtil.DEFAULT_PRECISION)
    		this.precision = FloatUtil.DEFAULT_PRECISION;
    	else
    		this.precision = precision;
    }
    
    /**
     * Returns precision of set
     */
    public final float getPrecision() {
        return this.precision;
    }

    public void dump() {
        System.out.println(this + ": " + changed + ", Min[" + getMin() + "], Max[" + getMax() + "], Bound[" + isBound() + "], Size[" + getSize() + "]");
    }
}
