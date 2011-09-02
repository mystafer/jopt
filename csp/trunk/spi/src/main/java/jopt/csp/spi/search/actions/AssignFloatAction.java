/*
 * AssignFloatAction.java
 * 
 * Created on Jun 16, 2005
 */
package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.PropagationFailureException;


/**
 * Action that assigns a single value to a numeric variable
 */
public class AssignFloatAction implements SearchAction {
    private CspFloatVariable var;
    private float val;
    
    public AssignFloatAction(CspFloatVariable var, float val) {
    	this.var = var;
        this.val = val;
    }
    
    public SearchAction performAction() throws PropagationFailureException {
        var.setValue(val);
    	return null;
    }
    
    public String toString() {
    	return "assign-number(" + var + ", " + val + ")";
    }
}