/*
 * InstantiateAction.java
 * 
 * Created on May 11, 2005
 */
package jopt.csp.spi.search.actions;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SetSelector;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will produce a search node that attempts to assign a value to
 * a set variable.  In this context "instantiate" is constraint satisfaction
 * terminology for exploring possible values in the domain of a variable trying
 * to find one that satisfies the constraints; it has nothing to do with the
 * Java instantiation of a class.
 */
public class InstantiateSetAction extends AbstractSearchNodeAction {
    private CspSetVariable var;
    private SetSelector selector;
    
    /**
     * Creates new instantiation action
     * 
     * @param var   Variable to instantiate
     */
    public InstantiateSetAction(CspSetVariable var) {
        this.var = var;
    }
    
    /**
     * Creates new instantiation action
     * 
     * @param var   Variable to instantiate
     */
    public InstantiateSetAction(CspSetVariable var, SetSelector selector) {
        this.var = var;
        this.selector = selector;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (var.isBound()) return null;
        
        Set possible = var.getPossibleSet();
        Set required = var.getRequiredSet();
        
        // retrieve first value that is not required
        Iterator possibleIter = possible.iterator();
        while (possibleIter.hasNext()) {
            
            // determine next value to use
            Object val = null;
            if (selector == null)
                val = possibleIter.next();
            else
                val = selector.select(var); 
            
            // if not required, return choice to assign the value
            // or remove it
            if (!required.contains(val)) {
                return choice(new AssignSet(val), new RemoveSet(val));
            }
        }
        
        return null;
    }
    
    /**
     * Action that assigns a single value to a set variable
     */
    private class AssignSet implements SearchAction {
        private Object val;
        
        protected AssignSet(Object val) {
            this.val = val;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            var.addRequired(val);
            
            if (!var.isBound())
            	return InstantiateSetAction.this;
            else
                return null;
        }
    }
    
    /**
     * Action that removes a single value from a set variable
     */
    private class RemoveSet implements SearchAction {
        private Object val;
        
        protected RemoveSet(Object val) {
            this.val = val;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            var.removePossible(val);
            
            if (!var.isBound())
                return InstantiateSetAction.this;
            else
                return null;
        }
    }
}
