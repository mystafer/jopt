package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashMap;

import jopt.csp.spi.arcalgorithm.domain.BaseDoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.DoubleIntervalDomain;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.util.DoubleSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.PropagationFailureException;

public class DoubleVariable extends DoubleExpr implements CspDoubleVariable, Variable {
	
    /**
     * Constructor
     */
    public DoubleVariable(String name, BaseDoubleDomain domain) {
    	super(name, domain);
    }

    /**
     * Constructor
     */
    public DoubleVariable(String name, double min, double max) {
        super(name, new DoubleIntervalDomain(min, max));
    }
    
    /**
     * Constructor
     */
    public DoubleVariable(String name, DoubleVariable var) {
        super(name, (BaseDoubleDomain) var.domain.clone());
    }
    
    /**
     * Constructor
     */
    public DoubleVariable(String name, DoubleCast expr) {
        this(name, expr.getDoubleDomain().getMin(), expr.getDoubleDomain().getMax());
        this.constraint = expr.eq(this);
    }
    
    /**
     * Returns the size of this variable's domain
     */
    public int getSize() {
    	return this.getDoubleDomain().getSize();
    }
    
    /**
     * Returns true if value is in this variable's domain
     */
    public boolean isInDomain(double val) {
    	return this.getDoubleDomain().isInDomain(val);
    }
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(double val) throws PropagationFailureException {
    	this.getDoubleDomain().setMax(val);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(double val) throws PropagationFailureException {
    	this.getDoubleDomain().setMin(val);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
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
    	setMax(val.doubleValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainMin(Number val) throws PropagationFailureException {
    	setMin(val.doubleValue());
    }      
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(double val) throws PropagationFailureException {
    	this.getDoubleDomain().setValue(val);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */    
    public void setDomainValue(Number val) throws PropagationFailureException {
    	this.setValue(val.doubleValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainValues(NumSet s) throws PropagationFailureException {
    	this.getDoubleDomain().setDomain(s);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(double val) throws PropagationFailureException {
    	this.getDoubleDomain().removeValue(val);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a set of values from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeAll(DoubleSet vals) throws PropagationFailureException {
    	this.getDoubleDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValue(Number val) throws PropagationFailureException {
    	removeValue(val.doubleValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValues(NumSet vals) throws PropagationFailureException {
    	this.getDoubleDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(double start, double end) throws PropagationFailureException {
    	this.getDoubleDomain().setRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(double start, double end) throws PropagationFailureException {
    	this.getDoubleDomain().removeRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getDoubleDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.setRange(start.doubleValue(),end.doubleValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.removeRange(start.doubleValue(),end.doubleValue());
    }        
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
    	return this.getDoubleDomain().getNextHigher(val);
    }
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
    	return this.getDoubleDomain().getNextLower(val);
    }
    
    /**
     * Clears the delta set for this variable's domain
     */
    public void clearDelta() {
    	this.getDoubleDomain().clearDelta();
    }
    
    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return this.getDoubleDomain().getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	this.getDoubleDomain().restoreDomainState(state);
    }
    
    /**
     * Clones this variable
     */
    @SuppressWarnings("unchecked")
	public Object clone() {
        DoubleVariable var = new DoubleVariable(name, this);
        var.variableListeners = (HashMap<VariableChangeListener, Integer>)this.variableListeners.clone();
        return var;
    }
    
}