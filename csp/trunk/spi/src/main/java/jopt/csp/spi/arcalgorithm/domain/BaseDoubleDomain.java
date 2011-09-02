package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.util.NumConstants;
import jopt.csp.util.DoubleSet;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.NumSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for double domains
 */
public abstract class BaseDoubleDomain extends BaseNumDomain implements DoubleDomain {
    protected DoubleSet values;
    protected DoubleSet delta;
    protected DoubleSet deltaAddedRecord;
    protected DoubleSet deltaRemovedRecord;
    protected double precision;

    /**
     * Constructor for cloning
     */
    protected BaseDoubleDomain() {
        super(NumConstants.DOUBLE);
        this.values = (DoubleSet) super.values;
        this.delta = (DoubleSet) super.delta;
        this.deltaAddedRecord = (DoubleSet) super.deltaAddedRecord;
        this.deltaRemovedRecord = (DoubleSet) super.deltaRemovedRecord;
        this.precision = DoubleUtil.DEFAULT_PRECISION;
    }
    
    /**
     * Returns maximum value of domain
     */
    public double getMax() {
        return values.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public double getMin() {
        return values.getMin();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return DoubleUtil.isEqual(values.getMin(), values.getMax(), precision);
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(double val) {
        return values.contains(val);
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(double val) throws PropagationFailureException {
        // check for valid value
        int cmp = DoubleUtil.compare(val, values.getMin(), precision);
        if (cmp == 0) val = values.getMin();
        if (cmp < 0) throw new PropagationFailureException();

        // check if value will trigger a change
        double prevMax = values.getMax();
        if (DoubleUtil.compare(val, values.getMax(), precision) < 0) {
            values.removeStartingAfter(val);
            
            if (isChangeDetected())
                if (isBound())
                    notifyValueChange(0d, this.getMax()-prevMax);
                else
                    notifyRangeChange(0d, this.getMax()-prevMax);
        }
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(double val) throws PropagationFailureException {
        // check for valid value
        int cmp = DoubleUtil.compare(val, values.getMax(), precision);
        if (cmp == 0) val = values.getMax();
        if (cmp > 0) throw new PropagationFailureException();

        // check if value will trigger a change
        double prevMin = values.getMin();
        if (DoubleUtil.compare(val, values.getMin(), precision) > 0) {
            values.removeEndingBefore(val);
            
            if (isChangeDetected()) {
                if (isBound())
                    notifyValueChange(this.getMin()-prevMin, 0d);
                else
                    notifyRangeChange(this.getMin()-prevMin, 0d);
            }
        }
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(double val) throws PropagationFailureException {
        // check if value is in set
    	if (!values.contains(val)) {
    		double nh = values.getNextHigher(val);
    		double nl = values.getNextLower(val);
    		
    		// check if next higher and next lower are equivalent in precision
    		boolean nhOk = nh != val && DoubleUtil.isEqual(nh, val, precision);
    		boolean nlOk = nl != val && DoubleUtil.isEqual(nl, val, precision);
    		
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
    	
        double prevMin = this.getMin();
        double prevMax = this.getMax();
        
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
    public void removeValue(double val) throws PropagationFailureException {
    	removeRange(val, val);
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(double start, double end) throws PropagationFailureException {
    	// check if setting to a single value
    	if (start==end) {
    		setValue(start);
    	}
    	
    	// set to range of values
    	else {
	        // record previous size of set
	        double prevMin = values.getMin();
	        double prevMax = values.getMax();
	        
	        // remove values
        	if (!DoubleUtil.isEqual(start, prevMin, precision)) {
		        if (DoubleUtil.isEqual(start, prevMax, precision)) {
	        		values.removeEndingBefore(prevMax);
		        }
		        else
		        	values.removeEndingBefore(start);
        	}
	        
        	if (!DoubleUtil.isEqual(end, prevMax, precision)) {
		        if (DoubleUtil.isEqual(end, prevMin, precision)) {
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
	            
	            double min = values.getMin();
	            double max = values.getMax();
	            
	            // check if bound
	            if (DoubleUtil.isEqual(min, max, precision))
	                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
	            
	            // check if min or max changed
	            else if (!DoubleUtil.isEqual(prevMin, min, precision) || !DoubleUtil.isEqual(prevMax, max, precision))
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
    public void removeRange(double start, double end) throws PropagationFailureException {
        // record previous size of set
        double prevMin = values.getMin();
        double prevMax = values.getMax();
    	
        // remove values
        values.remove(start - precision, end + precision);
        
        // notify of change
        if (isChangeDetected()) {
            values.remove(start, end);
            
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            double min = values.getMin();
            double max = values.getMax();
            
            // check if bound
            if (DoubleUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!DoubleUtil.isEqual(prevMin, min, precision) || !DoubleUtil.isEqual(prevMax, max, precision))
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
        double prevMin = values.getMin();
        double prevMax = values.getMax();
    	
        // remove values
        values.retainAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            double min = values.getMin();
            double max = values.getMax();
            
            // check if bound
            if (DoubleUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!DoubleUtil.isEqual(prevMin, min, precision) || !DoubleUtil.isEqual(prevMax, max, precision))
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
        double prevMin = values.getMin();
        double prevMax = values.getMax();
    	
        // remove values
        values.removeAll(s);
        
        // notify of change
        if (isChangeDetected()) {
            // check that set is not null
            int newSize = values.size();
            if (newSize==0)
                throw new PropagationFailureException();
            
            double min = values.getMin();
            double max = values.getMax();
            
            // check if bound
            if (DoubleUtil.isEqual(min, max, precision))
                notifyValueChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // check if min or max changed
            else if (!DoubleUtil.isEqual(prevMin, min, precision) || !DoubleUtil.isEqual(prevMax, max, precision))
                notifyRangeChange(this.getMin()-prevMin, this.getMax()-prevMax);
            
            // some other value in domain was removed
            else notifyDomainChange();
        }
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        return values.getNextHigher(val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
        return values.getNextHigher(val);
    }

    /**
     * Sets precision of set
     */
    public final void setPrecision(double precision) {
    	if (precision < DoubleUtil.DEFAULT_PRECISION)
    		this.precision = DoubleUtil.DEFAULT_PRECISION;
    	else
    		this.precision = precision;
    }
    
    /**
     * Returns precision of set
     */
    public final double getPrecision() {
        return this.precision;
    }

    public void dump() {
        System.out.println(this + ": " + changed + ", Min[" + getMin() + "], Max[" + getMax() + "], Bound[" + isBound() + "], Size[" + getSize() + "]");
    }
}
