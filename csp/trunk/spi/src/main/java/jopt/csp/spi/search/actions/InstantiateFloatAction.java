/*
 * InstantiateAction.java
 * 
 * Created on May 11, 2005
 */
package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.util.FloatUtil;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will attempt to assign a value to an real-number based variable using the
 * search tree.  In this context
 * "instantiate" is constraint satisfaction terminology for exploring possible values
 * in the domain of a variable trying to find one that satisfies the constraints; it has
 * nothing to do with the Java instantiation of a class.
 */
public class InstantiateFloatAction extends AbstractSearchNodeAction {
    private CspFloatVariable var;
    private float precision;
    private boolean minFirst;
    
    /**
     * Creates new instantiation action
     * 
     * @param var         Variable to instantiate
     * @param precision   Minimum precision to which variable domain will be reduced
     */
    public InstantiateFloatAction(CspFloatVariable var, float precision) {
        this(var, precision, true);
    }
    
    /**
     * Creates new instantiation action
     * 
     * @param var         Variable to instantiate
     * @param precision   Minimum precision to which variable domain will be reduced
     * @param minFirst    True if bottom half of domain should be used to restrict domain first, 
     *                          false to use upper half
     */
    public InstantiateFloatAction(CspFloatVariable var, float precision, boolean minFirst) {
        this.var = var;
        this.precision = precision;
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
        
        CspFloatVariable floatVar = (CspFloatVariable) var;
        float min = floatVar.getMin();
        float max = floatVar.getMax();
        
        // check if variable is within precision
        if (FloatUtil.isEqual(max, min, precision))
            return null;
        
        // calculate median value for restricting range of variable
        float median = (max - min) / 2f + min;
        
        // return choice to assign the value
        // or remove it
        return choice(new AssignRange(median, minFirst),
                      new AssignRange(median, !minFirst));
    }

    public String toString() {
        return "instantiate(" + var + ", precision=" + precision + ", min-first=" + minFirst + ")";
    }
    
    /**
     * Action that assigns either a max or min to a numeric variable
     */
    private class AssignRange implements SearchAction {
        private float val;
        private boolean assignMax;
        
        protected AssignRange(float val, boolean assignMax) {
            this.val = val;
            this.assignMax = assignMax;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            if (assignMax)
                var.setMax(val);
            else
                var.setMin(val);
            
            if (!var.isBound())
                return InstantiateFloatAction.this;
            else
                return null;
        }
    }
}
