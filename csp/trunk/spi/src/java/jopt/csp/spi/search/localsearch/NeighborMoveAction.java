package jopt.csp.spi.search.localsearch;

import jopt.csp.search.CurrentNeighbor;
import jopt.csp.search.Metaheuristic;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.goal.FirstSolutionGoal;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.spi.search.tree.SearchTechniqueChange;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that performs actions necessary to move to a neighboring solution during
 * local search operations
 * 
 * @author Nick Coleman
 * @version $Revision: 1.3 $
 */
public class NeighborMoveAction extends AbstractSearchNodeAction {
    private ConstraintStore store;
    private SolverSolution solution;
    private Neighborhood hood;
    private Metaheuristic meta;
    private SearchGoal goal;

    /**
     * Creates new local move action
     * 
     * @param store     Constraint store to post solution changes
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param meta      Metaheuristic used to guide the search
     * @param goal      Search goal used to select neighboring solution
     */
    public NeighborMoveAction(ConstraintStore store, SolverSolution solution, Neighborhood hood, Metaheuristic meta, SearchGoal goal) {
        this.store = store;
        this.solution = solution;
        this.hood = hood;
        this.meta = meta;
        this.goal = goal;
        
        // assume first solution is desired if no goal specified
        if (goal==null)
            goal = new FirstSolutionGoal();
    }
    
    /**
     * Creates new local move action
     * 
     * @param store     Constraint store to post solution changes
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param meta      Metaheuristic used to guide the search
     */
    public NeighborMoveAction(ConstraintStore store, SolverSolution solution, Neighborhood hood, Metaheuristic meta) {
        this(store, solution, hood, meta, null);
    }
    
    /**
     * Creates new local move action
     * 
     * @param store     Constraint store to post solution changes
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param goal      Search goal used to select neighboring solution
     */
    public NeighborMoveAction(ConstraintStore store, SolverSolution solution, Neighborhood hood, SearchGoal goal) {
        this(store, solution, hood, null, goal);
    }
    
    /**
     * Creates new local move action
     * 
     * @param store     Constraint store to post solution changes
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     */
    public NeighborMoveAction(ConstraintStore store, SolverSolution solution, Neighborhood hood) {
        this(store, solution, hood, null, null);
    }
    
    // javadoc inherited from SearchAction
    public SearchAction performAction() throws PropagationFailureException {
        // used to hold current neighbor applied to problem while browsing
        CurrentNeighbor current = new CurrentNeighbor();
        
        // create browse neighbors search action
        SearchAction browse = null;
        if (meta==null)
            browse = new BrowseNeighborhoodAction(store, solution, hood, current);
        else
            browse = new BrowseNeighborhoodAction(store, solution, hood, meta, current);
        
        // create search to find neighbor based on goal
        SearchAction findNeighbor = new SearchTechniqueChange(goal, browse);
        
        // create action to select neighbor solution and update initial solution
        SearchAction select = new SelectCurrentNeighborAction(store, solution, current);
        
        // combine find neighbor and select it into single action
        return combineActions(findNeighbor, select);
    }

}
