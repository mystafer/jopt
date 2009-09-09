package jopt.csp.spi.search.localsearch;

import jopt.csp.search.CurrentNeighbor;
import jopt.csp.search.Metaheuristic;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * This action will browse a list of neighboring produced from a {@link jopt.csp.search.Neighborhood}
 * and attempt to apply each in turn in order to locate other valid solutions to
 * the problem. The application of a neighboring solution is very similar to
 * restoring a solution. It does not reset any of the variables, but simply constrains
 * them further to fit within the requested solution.
 * 
 * If you want to want to apply a neighboring solution to a problem so the
 * exact solution is restored, you may want to refer to the <code>CspSolver</code> class's
 * {@link jopt.csp.CspSolver#reset()} and 
 * {@link jopt.csp.CspSolver#restoreNeighboringSolution(SolverSolution, SolverSolution)}
 * 
 * This action is not a replacement of the restore neighboring solution
 * method in the CspSolver class. It is just a convenient way of calling 
 * the restore during a search.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.13 $
 * @see BrowseNeighborsAction
 * @see jopt.csp.spi.search.actions.RestoreSolutionAction
 * @see jopt.csp.CspSolver#reset()
 * @see jopt.csp.CspSolver#restoreNeighboringSolution(SolverSolution, SolverSolution)
 */
public class BrowseNeighborhoodAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution initial;
    private Neighborhood hood;
    private Metaheuristic meta;
    private CurrentNeighbor current;
    private int nextNeighbor;
    
    /**
     * Creates new scan neighbors action
     * 
     * @param store         Constraint store containing problem where solution will be restored
     * @param initial       Initial solution related to neighbor
     * @param hood          Neighborhood of solutions, each to be applied as an alternative choices
     * @param current       Updated as neighbors are restored to solver to store the currently selected neighbor
     */
    public BrowseNeighborhoodAction(ConstraintStore store, SolverSolution initial, Neighborhood hood, CurrentNeighbor current) {
        this(store, initial, hood, null, current);
    }
    
    /**
     * Creates new scan neighbors action
     * 
     * @param store         Constraint store containing problem where solution will be restored
     * @param initial       Initial solution related to neighbor
     * @param hood          Neighborhood of solutions, each to be applied as an alternative choices
     * @param meta          Metaheuristic used to filter invalid neighbors and guide search
     * @param current       Updated as neighbors are restored to solver to store the currently selected neighbor
     */
    public BrowseNeighborhoodAction(ConstraintStore store, SolverSolution initial, Neighborhood hood, Metaheuristic meta, CurrentNeighbor current) {
        this(store, initial, hood, meta, current, 0);
    }
    
    /**
     * Internal constructor to save memory by reusing previous list
     */
    private BrowseNeighborhoodAction(ConstraintStore store, SolverSolution initial, Neighborhood hood, Metaheuristic meta, CurrentNeighbor current, int nextNeighbor) {
        this.store = store;
        this.initial = initial;
        this.hood = hood;
        this.meta = meta;
        this.current = current;
        this.nextNeighbor = nextNeighbor;
    }
    
    /**
     * Called by search tree to execute this action.
     * 
     * @return Next action to execute in search
     */
    public SearchAction performAction() throws PropagationFailureException {
        // initialize neighborhood and metaheuristic before retrieving first neighbor
        if (nextNeighbor==0) {
            hood.setInitialSolution(initial);
            
            if (meta!=null) {
                meta.setInitialSolution(initial);
            }
        }
        
        // search for valid neighbor
        SolverSolution neighbor = null;
        while (neighbor==null && nextNeighbor < hood.size()) {
            // retrieve next neighbor
            neighbor = hood.getNeighbor(nextNeighbor);
            
            if (neighbor == null) {
                nextNeighbor++;
            }
            else {
                // check if neighbor is acceptable for metaheuristic
                if (meta!=null && !meta.isAcceptableNeighbor(neighbor)) {
                    neighbor = null;
                    nextNeighbor++;
                }
                
                // check if neighbor is different than the current solution
                if (neighbor!=null && !initial.isDifferent(neighbor)) {
                    neighbor = null;
                    nextNeighbor++;
                }
            }
        }
        
        // check if a neighboring solution was located
        if (neighbor==null)
            throw new PropagationFailureException();
        
        // create action to restore next neighbor
        SearchAction restore = new RestoreNeighbor(nextNeighbor, neighbor);
        
        // check if other neighbors exist in the list
        if (nextNeighbor < hood.size()-1) {
        	SearchAction browseOthers = new BrowseNeighborhoodAction(store, initial, hood, meta, current, nextNeighbor+1);
            return choice(restore, browseOthers);
        }
        
        return restore;
    }
    
    /**
     * Action that restores a neighboring solution
     */
    private class RestoreNeighbor implements SearchAction {
        private int index;
        private SolverSolution neighbor;
        
        protected RestoreNeighbor(int index, SolverSolution neighbor) {
            this.index = index;
            this.neighbor = neighbor;
        }
        
        public SearchAction performAction() throws PropagationFailureException {
            try {
                store.restoreNeighboringSolution(initial, neighbor);
                
                // update current neighbor
                current.setNeighborhood(hood);
                current.setMetaheuristic(meta);
                current.setSolution(neighbor);
                current.setIndex(index);
                
                return null;
            }
            catch (PropagationFailureException propx) {
                throw propx;
            }
        }
    }
}
