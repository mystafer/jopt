/*
 * AssignIntegerAction.java
 * 
 * Created on Jun 16, 2005
 */
package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;


/**
 * Action that assigns a single value to a numeric variable
 */
public class AssignIntegerAction implements SearchAction {
    private CspIntVariable var;
    private int val;
    
    public AssignIntegerAction(CspIntVariable var, int val) {
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