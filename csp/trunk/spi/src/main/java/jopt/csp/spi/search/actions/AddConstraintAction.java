package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;

/**
 * Adds a constraint during a search that can be undone as search
 * routine backtracks
 * 
 * @author Nick Coleman
 * @version $Revision: 1.2 $
 */
public class AddConstraintAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private CspConstraint constraint;
    
    /**
     * Creates new add constraint action
     * 
     * @param store			Constraint store containing problem where data will be gathered from
     * @param constraint	Constraint to add during searching
     */
    public AddConstraintAction(ConstraintStore store, CspConstraint constraint) {
        this.store = store;
        this.constraint = constraint;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        store.addConstraint(constraint, false);
        return null;
    }
}
