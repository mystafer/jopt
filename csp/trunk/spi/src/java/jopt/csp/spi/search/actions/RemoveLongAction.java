/*
 * RemoveNumberAction.java
 * 
 * Created on Jun 16, 2005
 */
package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.PropagationFailureException;


/**
 * Action that removes a single value from the domain of a numeric variable
 */
public class RemoveLongAction implements SearchAction {
    private CspLongVariable var;
    private long val;
    
    public RemoveLongAction(CspLongVariable var, long val) {
        this.var = var;
        this.val = val;
    }
    
    public SearchAction performAction() throws PropagationFailureException {
        var.removeValue(val);
        return null;
    }
    
    public String toString() {
        return "remove-number(" + var + ", " + val + ")";
    }
}