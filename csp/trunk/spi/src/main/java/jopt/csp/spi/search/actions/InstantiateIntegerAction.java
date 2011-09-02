package jopt.csp.spi.search.actions;

import jopt.csp.search.IntegerSelector;
import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will attempt to assign a value to an integer variable using the search tree.  In this context
 * "instantiate" is constraint satisfaction terminology for exploring possible values
 * in the domain of a variable trying to find one that satisfies the constraints; it has
 * nothing to do with the Java instantiation of a class.
 */
public class InstantiateIntegerAction extends AbstractSearchNodeAction {
    private CspIntVariable var;
    private IntegerSelector selector;
    
    /**
     * Creates new instantiation action
     * 
     * @param var       Variable to instantiate
     * @param selector  Selector to use in deciding which value in the domain of the
     *                  variable to try next, as we attempt to reduce the domain of the variable.
     *                  If none is given, the default is smallest value in domain first.
     */
    public InstantiateIntegerAction(CspIntVariable var, IntegerSelector selector) {
        this.var = var;
        this.selector = selector;
    }
    
    /**
     * Creates new instantiation action, using default selector
     * 
     * @param var   Variable to instantiate
     */
    public InstantiateIntegerAction(CspIntVariable var) {
        this(var, null);
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (var.isBound()) return null;
        
        // retrieve value of variable to restrict domain
        int nextVal;
        if (selector==null)
        	nextVal = var.getMin();
        else
            nextVal = selector.select(var);
        
        // return choice to assign the value
        // or remove it
        return choice(new AssignIntegerAction(var, nextVal),
                      combineActions(new RemoveIntegerAction(var, nextVal), this));
    }
    
    public String toString() {
        return "instantiate(" + var + ")";
    }
}
