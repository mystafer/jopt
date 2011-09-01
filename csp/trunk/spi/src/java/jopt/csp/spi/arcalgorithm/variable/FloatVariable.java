package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashMap;

import jopt.csp.spi.arcalgorithm.domain.BaseFloatDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatIntervalDomain;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.util.FloatSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.PropagationFailureException;

public class FloatVariable extends FloatExpr implements CspFloatVariable, Variable {
    
	/**
     * Constructor
     */
    public FloatVariable(String name, BaseFloatDomain domain) {
        super(name, domain);
    }
    
    /**
     * Constructor
     */
    public FloatVariable(String name, float min, float max) {
        super(name, new FloatIntervalDomain(min, max));
    }
    
    /**
     * Constructor
     */
    public FloatVariable(String name, FloatVariable var) {
        this(name, (BaseFloatDomain) var.domain.clone());
    }
    
    /**
     * Constructor
     */
    public FloatVariable(String name, FloatCast expr) {
        this(name, expr.getFloatDomain().getMin(), expr.getFloatDomain().getMax());
        this.constraint = expr.eq(this);
    }
    
    /**
     * Returns the size of this variable's domain
     */
    public int getSize() {
    	return this.getFloatDomain().getSize();
    }
    
    /**
     * Returns true if value is in this variable's domain
     */
    public boolean isInDomain(float val) {
    	return this.getFloatDomain().isInDomain(val);
    }
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(float val) throws PropagationFailureException {
    	this.getFloatDomain().setMax(val);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(float val) throws PropagationFailureException {
    	this.getFloatDomain().setMin(val);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
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
    	setMax(val.floatValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainMin(Number val) throws PropagationFailureException {
    	setMin(val.floatValue());
    }      
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(float val) throws PropagationFailureException {
    	this.getFloatDomain().setValue(val);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */    
    public void setDomainValue(Number val) throws PropagationFailureException {
    	this.setValue(val.floatValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainValues(NumSet s) throws PropagationFailureException {
    	this.getFloatDomain().setDomain(s);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(float val) throws PropagationFailureException {
    	this.getFloatDomain().removeValue(val);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a set of values from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeAll(FloatSet vals) throws PropagationFailureException {
    	this.getFloatDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValue(Number val) throws PropagationFailureException {
    	this.removeValue(val.floatValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValues(NumSet vals) throws PropagationFailureException {
    	this.getFloatDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(float start, float end) throws PropagationFailureException {
    	this.getFloatDomain().setRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(float start, float end) throws PropagationFailureException {
    	this.getFloatDomain().removeRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getFloatDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.setRange(start.floatValue(),end.floatValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.removeRange(start.floatValue(),end.floatValue());
    }        
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
    	return this.getFloatDomain().getNextHigher(val);
    }
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     */
    public float getNextLower(float val) {
    	return this.getFloatDomain().getNextLower(val);
    }
    
    /**
     * Clears the delta set for this variable's domain
     */
    public void clearDelta() {
    	this.getFloatDomain().clearDelta();
    }
    
    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return this.getFloatDomain().getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	this.getFloatDomain().restoreDomainState(state);
    }
    
    /**
     * Clones this variable
     */
    @SuppressWarnings("unchecked")
	public Object clone() {
        FloatVariable var = new FloatVariable(name, this);
        var.variableListeners = (HashMap<VariableChangeListener, Integer>)this.variableListeners.clone();
        return var;
    }
    
}