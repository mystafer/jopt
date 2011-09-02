package jopt.csp.search;

import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspIntVariable;

/**
 * Interface for a class that creates and returns common local search actions and objects. 
 * 
 * @author Nick Coleman
 * @version $Revision: 1.14 $
 */
public interface LocalSearch {
    /**
     * This action will browse a list of neighboring solutions based on an initial solution.
     * The neighbor can define solutions to variables that should not be updated
     * from the original solution as well as values to assign to variables not
     * in the scope of the original solution.  Each neighbor will be applied
     * as an alternative solution to produce a set of choices.
     * 
     * If you want to restore a neighboring solution to a problem so the
     * exact solution is restored, you may want to refer to the <code>CspSolver</code> class's
     * {@link jopt.csp.CspSolver#reset()} and 
     * {@link jopt.csp.CspSolver#restoreNeighboringSolution(SolverSolution, SolverSolution)}
     * 
     * This action is not a replacement of the restore neighboring solution
     * method in the CspSolver class. It is just a convenient way of calling 
     * the restore during a search.
     * 
     * @param initial       Initial solution related to neighbor
     * @param neighbors     Array of neighboring solutions to be browsed
     */
    public SearchAction browseNeighbors(SolverSolution initial, SolverSolution neighbors[]);

    /**
     * Restores a neighboring solution to the solver
     * 
     * @param initial       Initial solution related to neighbor
     * @param neighbor      Neighboring solution to be restored
     */
    public SearchAction moveToNeighbor(SolverSolution initial, SolverSolution neighbor);

    /**
     * Creates a neighborhood that is useful for flipping 0 and 1
     * values on binary variables.  For each variable defined, a
     * neighboring solution will be defined that either assigns
     * the variable's value to 1 or the variable's value to 0.
     * 
     * @param vars      Array of variables to be flipped to produce neighborhood
     */
    public Neighborhood flipNeighborhood(CspIntVariable vars[]);

    /**
     * Creates a neighborhood where each neighbor selects two
     * variables from the initial solution and swaps their values
     * 
     * @param vars      Array of variables to be swapped
     */
    public Neighborhood swapNeighborhood(CspIntVariable vars[]);
    
    /**
     * Creates a neighborhood from a combination of several neighborhoods
     * 
     * @param neighborhoods the neighborhoods to "combine"
     */
    public Neighborhood unifiedNeighborhood(Neighborhood[] neighborhoods);
    
    /**
     * Creates a unified, randomized neighborhood from the specified neighborhoods
     * 
     * @param neighborhoods the neighborhoods to "combine" and randomize
     */
    public Neighborhood randomize(Neighborhood[] neighborhoods);

    /**
     * Creates a randomized neighborhood from the specified neighborhood
     * 
     * @param neighborhood to be randomized
     */
    public Neighborhood randomize(Neighborhood neighborhood);

    /**
     * Creates a unified, randomized, weighted neighborhood using the specified
     * neighbors and weights.  Note that the number of neighborhoods used must
     * match the number of weights specified.
     * 
     * @param neighborhoods the neighborhoods to unify and randomize
     * @param weights the weights determining how frequently neighbors
     *                from the associated neighborhood are returned
     */
    public Neighborhood weightedRandomize(Neighborhood[] neighborhoods, double[] weights);

    /**
     * This action will browse a list of neighbors produced from a {@link jopt.csp.search.Neighborhood}
     * and attempt to apply each in turn in order to locate other valid solutions to
     * the problem. The application of a neighboring solution is very similar to
     * restoring a solution. It does not reset any of the variables, but simply constrains
     * them further to conform to the values of the currently selected neighbor.
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
     * @param initial       Initial solution related to neighbor
     * @param hood          Neighborhood of solutions, each to be applied as an alternative choices
     * @param current       Updated as neighbors are restored to solver to store the currently selected neighbor
     * @see #browseNeighbors(SolverSolution, SolverSolution[])
     * @see #selectCurrentNeighbor(SolverSolution, CurrentNeighbor)
     */
    public SearchAction browseNeighborhood(SolverSolution initial,
            Neighborhood hood, CurrentNeighbor current);

    /**
     * This action will browse a list of neighboring solutions with the guidance of 
     * a metaheuristic used to determine which solutions are acceptable.
     * 
     * @param initial       Initial solution related to neighbor
     * @param hood          Neighborhood of solutions, each to be applied as an alternative choice
     * @param meta          Metaheuristic used to guide the search
     * @param current       Updated as neighbors are restored to solver to store the currently selected neighbor
     * @see #browseNeighborhood(SolverSolution, Neighborhood, CurrentNeighbor)
     */
    public SearchAction browseNeighborhood(SolverSolution initial, Neighborhood hood, 
            Metaheuristic meta, CurrentNeighbor current);

    /**
     * Advises neighborhoods that the current neighbor is about
     * to be made the new initial solution.  This is preferable to the 
     * StoreSolutionAction since it will call the <code>neighborSelected()</code> method on the
     * neighborhood that produced the solution before the initial solution is
     * updated. 
     * 
     * @param initial   Initial solution neighbor is based upon that will be updated
     * @param current   Current neighboring solution that is applied to the problem
     */
    public SearchAction selectCurrentNeighbor(SolverSolution initial, CurrentNeighbor current);

    /**
     * Action that will post a constraint during searching that will require additional 
     * solutions located to be better than the solution given to this action. The constraint
     * is based upon the objective expression and value stored in the solution.
     * 
     * @param solution  Solution that should be improved upon
     * @param step      Positive value indicating amount objective value should be improved
     */
    public SearchAction improve(SolverSolution solution, int step);

    /**
     * Action that will post a constraint during searching that will require additional 
     * solutions located to be better than the solution given to this action. The constraint
     * is based upon the objective expression and value stored in the solution.
     * 
     * @param solution  Solution that should be improved upon
     * @param step      Positive value indicating amount objective value should be improved
     */
    public SearchAction improve(SolverSolution solution, float step);

    /**
     * Action that will post a constraint during searching that will require additional 
     * solutions located to be better than the solution given to this action. The constraint
     * is based upon the objective expression and value stored in the solution.
     * 
     * @param solution  Solution that should be improved upon
     * @param step      Positive value indicating amount objective value should be improved
     */
    public SearchAction improve(SolverSolution solution, long step);

    /**
     * Action that will post a constraint during searching that will require additional 
     * solutions located to be better than the solution given to this action. The constraint
     * is based upon the objective expression and value stored in the solution.
     * 
     * @param solution  Solution that should be improved upon
     * @param step      Positive value indicating amount objective value should be improved
     */
    public SearchAction improve(SolverSolution solution, double step);

    /**
     * Advanced action that performs all actions necessary to move to a neighboring solution during
     * local search operations and will only return the first valid neighboring solution
     * 
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param meta      Metaheuristic used to guide the search
     * @param goal      Search goal used to select neighboring solution
     */
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood,
            Metaheuristic meta, SearchGoal goal);

    /**
     * Advanced action that performs all actions necessary to move to a neighboring solution during
     * local search operations and will only return the first valid neighboring solution
     * 
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param meta      Metaheuristic used to guide the search
     */
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta);

    /**
     * Advanced action that performs all actions necessary to move to a neighboring solution during
     * local search operations and will only return the first valid neighboring solution
     * 
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     * @param goal      Search goal used to select neighboring solution
     */
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood, SearchGoal goal);

    /**
     * Advanced action that performs all actions necessary to move to a neighboring solution during
     * local search operations and will only return the first valid neighboring solution
     * 
     * @param solution  Solution that move is based upon and result will be stored in
     * @param hood      Neighborhood of solutions relative to initial
     */
    public SearchAction neighborMove(SolverSolution solution, Neighborhood hood);
    
    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood,
            Metaheuristic meta, SearchGoal goal, int numToCheck);

    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, Metaheuristic meta, int numToCheck);


    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, SearchGoal goal, int numToCheck);


    public SearchAction tabuMove(SolverSolution solution, Neighborhood hood, int numToCheck);

    /**
     * Tabu Search metaheuristic that tracks moves performed during a local search
     * and maintains lists of moves that are forbidden.
     * 
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @param forbiddenAlterMoves   Number of moves that will alter a previously changed variable in any way that are not allowed
     * @param objectiveGap          Amount objective value of move must be within initial objective value
     * @see #browseNeighborhood(SolverSolution, Neighborhood, Metaheuristic, CurrentNeighbor)
     */
    public Metaheuristic tabu(int forbiddenUndoMoves, int forbiddenAlterMoves, double objectiveGap);

    /**
     * Tabu Search metaheuristic that tracks moves performed during a local search
     * and maintains lists of moves that are forbidden.
     * 
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @param objectiveGap          Amount objective value of move must be within initial objective value
     * @see #browseNeighborhood(SolverSolution, Neighborhood, Metaheuristic, CurrentNeighbor)
     */
    public Metaheuristic tabu(int forbiddenUndoMoves, double objectiveGap);

    /**
     * Tabu Search metaheuristic that tracks moves performed during a local search
     * and maintains lists of moves that are forbidden.
     * 
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @see #browseNeighborhood(SolverSolution, Neighborhood, Metaheuristic, CurrentNeighbor)
     */
    public Metaheuristic tabu(int forbiddenUndoMoves);
}