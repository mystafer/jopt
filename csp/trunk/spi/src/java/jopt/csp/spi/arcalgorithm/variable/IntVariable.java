package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashMap;

import jopt.csp.spi.arcalgorithm.domain.BaseIntDomain;
import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.IntSparseDomain;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.util.IntSet;
import jopt.csp.util.IntSparseSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;

public class IntVariable extends IntExpr implements CspIntVariable, Variable {
    
	/**
     * Constructor
     */
    public IntVariable(String name, BaseIntDomain domain) {
        super(name, domain);
    }
	
    /**
     * Constructor
     */
    public IntVariable(String name, IntSparseSet vals) {
        super(name, new IntSparseDomain(vals));
    }
    
    /**
     * Constructor
     */
    public IntVariable(String name, int min, int max) {
        super(name, new IntIntervalDomain(min, max));
    }
    
    /**
     * Constructor
     */
    public IntVariable(String name, IntVariable var) {
        this(name, (BaseIntDomain) var.getIntDomain().clone());
    }
    
    /**
     * Constructor
     */
    public IntVariable(String name, CspIntExpr expr) {
        this(name, expr.getMin(), expr.getMax());
        this.constraint = expr.eq(this);
    }
    
    /**
     * Returns the size of this variable's domain
     */
    public int getSize() {
    	return this.getIntDomain().getSize();
    }
    
    /**
     * Returns true if value is in this variable's domain
     */
    public boolean isInDomain(int val) {
    	return this.getIntDomain().isInDomain(val);
    }
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(int val) throws PropagationFailureException {
    	this.getIntDomain().setMax(val);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(int val) throws PropagationFailureException {
    	this.getIntDomain().setMin(val);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
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
    	setMax(val.intValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainMin(Number val) throws PropagationFailureException {
    	setMin(val.intValue());
    }  
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(int val) throws PropagationFailureException {
    	this.getIntDomain().setValue(val);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */    
    public void setDomainValue(Number val) throws PropagationFailureException {
    	this.setValue(val.intValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainValues(NumSet s) throws PropagationFailureException {
    	this.getIntDomain().setDomain(s);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(int val) throws PropagationFailureException {
    	this.getIntDomain().removeValue(val);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a set of values from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeAll(IntSet vals) throws PropagationFailureException {
    	this.getIntDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValue(Number val) throws PropagationFailureException {
    	this.removeValue(val.intValue());
    }    
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainValues(NumSet vals) throws PropagationFailureException {
    	this.getIntDomain().removeDomain(vals);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(int start, int end) throws PropagationFailureException {
    	this.getIntDomain().setRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(int start, int end) throws PropagationFailureException {
    	this.getIntDomain().removeRange(start, end);
    	//notify listeners if the variable changed
    	if(this.getIntDomain().changed()) {
    		fireChangeEvent();
    	}
    }
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.setRange(start.intValue(),end.intValue());
    }
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomainRange(Number start, Number end) throws PropagationFailureException {
    	this.removeRange(start.intValue(),end.intValue());
    }        
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     */
    public int getNextHigher(int val) {
    	return this.getIntDomain().getNextHigher(val);
    }
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     */
    public int getNextLower(int val) {
    	return this.getIntDomain().getNextLower(val);
    }
    
    /**
     * Clears the delta set for this variable's domain
     */
    public void clearDelta() {
    	this.getIntDomain().clearDelta();
    }
    
    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return this.getIntDomain().getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	this.getIntDomain().restoreDomainState(state);
    }
    
    /**
     * Clones this variable
     */
    @SuppressWarnings("unchecked")
	public Object clone() {
        IntVariable var = new IntVariable(name, this);
        var.variableListeners = (HashMap<VariableChangeListener, Integer>)this.variableListeners.clone();
        return var;
   }
    
}