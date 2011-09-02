package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that creates a choicepoint where one choice
 * will instantiate an integer node by restricting the domain of a variable 
 * to the lower half of the domain and the other choice will use the upper half
 */
public class SplitInstantiateLongAction extends AbstractSearchNodeAction {
    private CspLongVariable var;
    private boolean minFirst;
    
    /**
     * Creates new instantiation action, defaults to bottom-half of domain first.
     * 
     * @param var         Variable to instantiate
     */
    public SplitInstantiateLongAction(CspLongVariable var) {
        this(var, true);
    }
    
    /**
     * Creates new instantiation action, allowing choice of bottom-half or top-half first.
     * 
     * @param var         Variable to instantiate
     * @param minFirst    True if bottom half of domain should be used to restrict domain first, 
     *                          false to use upper half
     */
    public SplitInstantiateLongAction(CspLongVariable var, boolean minFirst) {
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
        
        long min = var.getMin();
        long max = var.getMax();
        
        // calculate mean, the middle of the interval
        long mean = (max - min) / 2 + min;
        
        // return choice to assign the value
        // or remove it
        return choice(new AssignRange(mean, minFirst),
                      combineActions(new AssignRange(mean+1, !minFirst), this));
    }

    /**
     * Action that assigns either a max or min to a numeric variable
     */
    private class AssignRange implements SearchAction {
        private long val;
        private boolean assignMax;
        
        protected AssignRange(long val, boolean assignMax) {
            this.val = val;
            this.assignMax = assignMax;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            if (assignMax)
                var.setMax(val);
            else
                var.setMin(val);
            
            if (!var.isBound())
                return SplitInstantiateLongAction.this;
            else
                return null;
        }
    }
}
