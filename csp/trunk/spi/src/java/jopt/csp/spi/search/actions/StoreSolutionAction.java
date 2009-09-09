package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will store all information for the current problem state in 
 * a solution that can be restored at a later date.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.4 $
 * @see RestoreSolutionAction
 */
public class StoreSolutionAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution solution;
    
    /**
     * Creates new store solution action
     * 
     * @param store     Constraint store containing problem where data will be gathered from
     * @param solution  Solution to store information in
     */
    public StoreSolutionAction(ConstraintStore store, SolverSolution solution) {
        this.store = store;
        this.solution = solution;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        // overwrite solution with current problem state
        store.storeSolution(solution);
        return null;
    }
}
