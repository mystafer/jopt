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
 * Advises neighborhoods and metaheuristics that the current neighbor is about
 * to be made the new initial solution.  This is preferable to the 
 * {@link jopt.csp.spi.search.actions.StoreSolutionAction StoreSolutionAction}
 * since it will call the <code>neighborSelected()<code> method on the
 * neighborhood that produced the solution before the initial solution is
 * updated. 
 * 
 * @author Nick Coleman
 * @version $Revision: 1.2 $
 */
public class SelectCurrentNeighborAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution initial;
    private CurrentNeighbor current;

    /**
     * Creates new select neighbor action
     * 
     * @param store     Constraint store containing problem
     * @param initial   Initial solution neighbor is based upon that will be updated
     * @param current   Current neighboring solution that is applied to the problem
     */
    public SelectCurrentNeighborAction(ConstraintStore store, SolverSolution initial, CurrentNeighbor current) {
        this.store = store;
        this.initial = initial;
        this.current = current;
    }
    
    // javadoc inherited from SearchAction
    public SearchAction performAction() throws PropagationFailureException {
        // notify neighborhood of selection
        Neighborhood hood = current.getNeighborhood();
        hood.neighborSelected(current.getIndex());
        
        // notify metaheuristic of selection
        Metaheuristic meta = current.getMetaheuristic();
        if (meta!=null)
        	meta.neighborSelected(current.getSolution());
        
        // update initial solution to current problem state
        store.storeSolution(initial);
        
        return null;
    }
}
