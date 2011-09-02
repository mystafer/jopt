package jopt.csp.search;

import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.PropagationFailureException;

/**
 * Used to guide local searches.  Determines which moves are structurally valid.
 * 
 * During the verification of a neighbor's acceptability, the 
 * <code>NeighborCheck</code>, if specified, is used to further
 * limit the neighbors deemed valid.
 * 
 * The use of a <code>NeighborCheck</code> is highly recommended.
 * 
 * @see NeighborCheck
 */
public interface Metaheuristic {
	/**
	 * Sets the initial solution for this metaheuristic 
	 * 
	 * @param initial   Initial solution that will be used as new basis for the metaheuristic
	 * @return False if solution is an unacceptable solution and no movements should be allowed
	 * @throws PropagationFailureException  When unable to post constraints improving the objective
	 */
	public boolean setInitialSolution(SolverSolution initial)
			throws PropagationFailureException;

	/**
	 * Used to check the validity of the specified neighbor.  This method
	 * returns false if the metaheuristic can determine that the neighbor
	 * will be of no value, and it does so without requiring all the work
	 * of actually restoring the solution.
	 * 
	 * @param neighbor  Neighbor to check if acceptable
	 * @return True if neighbor is acceptable to restore
	 */
	public boolean isAcceptableNeighbor(SolverSolution neighbor);

    /**
     * Notifies the metaheuristic that a neighboring solution has just been
     * restored to the problem allowing the heuristic to determine if the
     * move has produced an allowed result.
     * 
     * @param neighbor  Neighbor that was restored to problem
     * @return True if neighbor produced a valid solution
     */
    public boolean isRestoredNeighborValid(SolverSolution neighbor);

	/**
	 * Called when a current neighbor has been selected to allow metaheuristic
     * to store data that will determine how future neighbors will be
     * selected. This method is called before the initial solution has
     * been altered.
	 * 
	 * @param neighbor  Neighbor that was selected
	 */
	public void neighborSelected(SolverSolution neighbor);
    
    /**
     * Called after the solver has failed to find a solution to determine if changes
     * can be made by the metaheuristic to allow additional searching to be performed
     * 
     * @return True if searching should continue
     */
    public boolean continueSearch();
    
    /**
     * Sets the <code>NeighborCheck</code> for this Metaheuristic
     * 
     * @param nc
     */
    public void setNeighborCheck(NeighborCheck nc);
    
    /**
     * Retrieves the <code>NeighborCheck</code> for this Metaheuristic
     * 
     * @return the NeighborCheck, if one has been set; otherwise, returns null
     */
    public NeighborCheck getNeighborCheck();
}