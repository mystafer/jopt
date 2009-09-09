package jopt.csp.search;

import jopt.csp.solution.SolverSolution;

/**
 * Used in conjunction with a <code>Metaheuristic</code>
 * to further guide local searches.
 * 
 * Specifically, this is used to check neighboring solutions
 * before restoring and propagating them.  While the
 * <code>Metaheuristic</code> is typically responsible for
 * ensuring that the structure of a neighboring solution is
 * valid, the <code>NeighborCheck</code> commonly verifies
 * the quality of the solution.
 * 
 * @author cjohnson
 * @see Metaheuristic
 */
public interface NeighborCheck {
    /**
     * Sets the initial solution for this neighbor check
     * 
     * @param initial   Initial solution that will be used as new basis for the neighbor checking
     */
    public void setInitialSolution(SolverSolution initial);
    
    /**
     * Checks the quality of the specified neighbor during a call to
     * {@link Metaheuristic#isAcceptableNeighbor(SolverSolution)}.
     * 
     * @param neighbor  Represents the neighboring solution to verify
     * @return True if neighbor is worth using; otherwise, returns false
     */
    public boolean isValidNeighbor(SolverSolution neighbor);
}
