package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that creates a choicepoint where one choice
 * will instantiate an integer node by restricting the domain of a variable 
 * to the lower half of the domain and the other choice will use the upper half
 */
public class SplitInstantiateIntegerAction extends AbstractSearchNodeAction {
    private CspIntVariable var;
    private boolean minFirst;
    
    /**
     * Creates new instantiation action, defaults to bottom-half of domain first.
     * 
     * @param var         Variable to instantiate
     */
    public SplitInstantiateIntegerAction(CspIntVariable var) {
        this(var, true);
    }
    
    /**
     * Creates new instantiation action, allowing choice of bottom-half or top-half first.
     * 
     * @param var         Variable to instantiate
     * @param minFirst    True if bottom half of domain should be used to restrict domain first, 
     *                          false to use upper half
     */
    public SplitInstantiateIntegerAction(CspIntVariable var, boolean minFirst) {
        this.var = var;
        this.minFirst = minFirst;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        // check if variable is already bound
        if (var.isBound()) return null;
        
        int min = var.getMin();
        int max = var.getMax();
        
        // calculate mean, the middle of the interval
        int mean = (max - min) / 2 + min;
        
        // return choice to assign the value
        // or remove it
        return choice(new AssignRange(mean, minFirst),
                      combineActions(new AssignRange(mean+1, !minFirst), this));
    }

    public String toString() {
        return "split-instantiate(" + var + ")";
    }
    
    /**
     * Action that assigns either a max or min to a numeric variable
     */
    private class AssignRange implements SearchAction {
        private int val;
        private boolean assignMax;
        
        protected AssignRange(int val, boolean assignMax) {
            this.val = val;
            this.assignMax = assignMax;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            if (assignMax)
                var.setMax(val);
            else
                var.setMin(val);
            
            if (!var.isBound())
                return SplitInstantiateIntegerAction.this;
            else
                return null;
        }
        
        public String toString() {
            if (assignMax)
            	return "assign-max(" + var + ", " + val + ")";
            else
                return "assign-min(" + var + ", " + val + ")";
        }
    }
}
