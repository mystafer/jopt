package jopt.csp.spi.search.localsearch;

import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * This action will browse a list of neighboring solutions to an solution.
 * The neighbor can define solutions to variables that should not be updated
 * from the original solution as well as values to assign to variables not
 * in the scope of the original solution.  Each neighbor will be applied
 * as an alternative solution to produce a set of choices.
 * 
 * If you want to want to restore a neighboring solution to a problem so the
 * exact solution stored, you may want to refer to the <code>CspSolver</code> class's
 * {@link jopt.csp.CspSolver#reset()} and 
 * {@link jopt.csp.CspSolver#restoreNeighboringSolution(SolverSolution, SolverSolution)}
 * 
 * This action is not a replacement of the restore neighboring solution
 * method in the CspSolver class. It is just a convenient way of calling 
 * the restore during a search.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.7 $
 * @see jopt.csp.spi.search.actions.RestoreSolutionAction
 * @see jopt.csp.CspSolver#reset()
 * @see jopt.csp.CspSolver#restoreNeighboringSolution(SolverSolution, SolverSolution)
 */
public class BrowseNeighborsAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution initial;
    private List neighbors;
    
    /**
     * Creates new scan neighbors action
     * 
     * @param store         Constraint store containing problem where solution will be restored
     * @param initial       Initial solution related to neighbor
     * @param neighbors     List of neighboring solutions to initial, each to be
     *                      applied as an alternative choices
     */
    public BrowseNeighborsAction(ConstraintStore store, SolverSolution initial, List neighbors) {
        this(store, initial, neighbors, true);
    }
    
    /**
     * Internal constructor to save memory by reusing previous list
     */
    private BrowseNeighborsAction(ConstraintStore store, SolverSolution initial, List neighbors, boolean clone) {
        this.store = store;
        this.initial = initial;

        if (clone)
            this.neighbors = new LinkedList(neighbors);
        else
        	this.neighbors = neighbors;
    }
    
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        // fetch first neighbor to restore and create action to restore it
        SolverSolution firstNeighbor = (SolverSolution) neighbors.get(0);
        SearchAction restore = new RestoreNeighbor(store, initial, firstNeighbor);
        
        // check if other neighbors exist in the list
        if (neighbors.size()>1) {
            List otherNeighbors = neighbors.subList(1, neighbors.size());
        	SearchAction scanOthers = new BrowseNeighborsAction(store, initial, otherNeighbors, false);
            return choice(restore, scanOthers);
        }
        
        return restore;
    }

    /**
     * Action that restores a neighboring solution
     */
    private static class RestoreNeighbor implements SearchAction {
        private ConstraintStore store;
        private SolverSolution initial;
        private SolverSolution neighbor;
        
        protected RestoreNeighbor(ConstraintStore store, SolverSolution initial, SolverSolution neighbor) {
            this.store = store;
            this.initial = initial;
            this.neighbor = neighbor;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            store.restoreNeighboringSolution(initial, neighbor);
            return null;
        }
    }
}
