package jopt.csp.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * This action will apply all changes contained in the solution to 
 * variables in the problem.  It will not attempt to reset or backtrack
 * to the beginning of the search, but simply reduces the variables
 * in the solution to fit within the parameters given to restore.
 * 
 * If you want to want to restore the state of a problem to the
 * exact solution stored, you may want to refer to the <code>CspSolver</code> class's
 * {@link jopt.csp.CspSolver#reset()} and 
 * {@link jopt.csp.CspSolver#restoreSolution(SolverSolution)}
 * 
 * This action is not a replacement of the restore method in the CspSolver class.
 * It is just a convenient way of calling the restore during a search.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.6 $
 * @see jopt.csp.spi.search.actions.StoreSolutionAction
 * @see jopt.csp.CspSolver#reset()
 * @see jopt.csp.CspSolver#restoreSolution(SolverSolution)
 */
public class RestoreSolutionAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution solution;
    
    /**
     * Creates new restore solution action
     * 
     * @param store     Constraint store containing problem where solution will be restored
     * @param solution  Solution to problem that should be restored
     */
    public RestoreSolutionAction(ConstraintStore store, SolverSolution solution) {
        this.store = store;
        this.solution = solution;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        store.restoreSolution(solution);
        return null;
    }
}
