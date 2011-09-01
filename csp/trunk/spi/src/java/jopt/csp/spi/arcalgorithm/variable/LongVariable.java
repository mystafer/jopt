package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashMap;

import jopt.csp.spi.arcalgorithm.domain.BaseLongDomain;
import jopt.csp.spi.arcalgorithm.domain.LongIntervalDomain;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.util.LongSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.PropagationFailureException;

public class LongVariable extends LongExpr implements CspLongVariable, Variable {
    
	/**
     * Constructor
     */
    public LongVariable(String name, BaseLongDomain domain) {
        super(name, domain);
    }
    
    /**
     * Constructor
     */
    public LongVariable(String name, long min, long max) {
        super(name, new LongIntervalDomain(min, max));
    }
    
    /**
     * Constructor
     */
    public LongVariable(String name, LongVariable var) {
        this(name, (BaseLongDomain) var.domain.clone());
    }
    
    /**
     * Constructor
     */
    public LongVariable(String name, LongCast expr) {
        this(name, expr.getLongDomain().getMin(), expr.getLongDomain().getMax());
        this.constraint = expr.eq(this);
    }
    
    /**
     * Returns the size of this variable's domain
     */
    public int getSize() {
    	return this.getLongDomain().getSize();
    }
    
    /**
     * Returns true if value is in this variable's domain
     */
    public boolean isInDomain(long val) {
    	return this.getLongDomain().isInDomain(val);
    }
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(long val) throws PropagationFailureException {
    	this.getLongDomain().setMax(val);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainMax(Number val) throws PropagationFailureException {
    	setMax(val.longValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainMin(Number val) throws PropagationFailureException {
    	setMin(val.longValue());
    }      
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(long val) throws PropagationFailureException {
    	this.getLongDomain().setMin(val);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(long val) throws PropagationFailureException {
    	this.getLongDomain().setValue(val);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */    
    public void setDomainValue(Number val) throws PropagationFailureException {
    	this.setValue(val.longValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainValues(NumSet s) throws PropagationFailureException {
    	this.getLongDomain().setDomain(s);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(long val) throws PropagationFailureException {
    	this.getLongDomain().removeValue(val);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a set of values from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeAll(LongSet vals) throws PropagationFailureException {
    	this.getLongDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValue(Number val) throws PropagationFailureException {
    	this.removeValue(val.longValue());
    }        
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValues(NumSet vals) throws PropagationFailureException {
    	this.getLongDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(long start, long end) throws PropagationFailureException {
    	this.getLongDomain().setRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(long start, long end) throws PropagationFailureException {
    	this.getLongDomain().removeRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getLongDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.setRange(start.longValue(),end.longValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.removeRange(start.longValue(),end.longValue());
    }    
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     */
    public long getNextHigher(long val) {
    	return this.getLongDomain().getNextHigher(val);
    }
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     */
    public long getNextLower(long val) {
    	return this.getLongDomain().getNextLower(val);
    }
    
    /**
     * Clears the delta set for this variable's domain
     */
    public void clearDelta() {
    	this.getLongDomain().clearDelta();
    }
    
    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return this.getLongDomain().getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	this.getLongDomain().restoreDomainState(state);
    }
    
    /**
     * Clones this variables
     */
    @SuppressWarnings("unchecked")
	public Object clone() {
        LongVariable var = new LongVariable(name, this);
        var.variableListeners = (HashMap<VariableChangeListener, Integer>)this.variableListeners.clone();
        return var;
    }
    
}