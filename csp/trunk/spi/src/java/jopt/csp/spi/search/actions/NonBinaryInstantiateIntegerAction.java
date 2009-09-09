package jopt.csp.spi.search.actions;

import java.util.ArrayList;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will produce a search node that attempts to assign
 * a value to an integer variable by creating a choicepoint where each route
 * will set the variable to a single value
 */
public class NonBinaryInstantiateIntegerAction extends AbstractSearchNodeAction {
    private CspIntVariable var;
    
    /**
     * Creates new instantiation action
     * 
     * @param var       Variable to instantiate
     */
    public NonBinaryInstantiateIntegerAction(CspIntVariable var) {
        this.var = var;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (var.isBound()) return null;
        
        // create list of actions for choicepoint
        ArrayList actions = new ArrayList();
        
        int prev;
        int val = var.getMin();
        do {
            actions.add(new AssignIntegerAction(var, val));
            
            // check if another value exists in domain
            prev = val;
            val = var.getNextHigher(val);
        }
        while (val!=prev);
        
        return choice(actions);
    }
}
