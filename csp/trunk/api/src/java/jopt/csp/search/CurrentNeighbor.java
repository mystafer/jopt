package jopt.csp.search;

import jopt.csp.solution.SolverSolution;

/**
 * Used to hold a neighboring solution and the neighborhood that produced
 * it.  This is used when browsing a neighborhood to maintain 
 * which neighbor is currently active in the solver.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.3 $
 */
public class CurrentNeighbor {
	private Neighborhood hood;
    private Metaheuristic metaheuristic;
    private SolverSolution solution;
    private int index;
    
	/**
	 * @return Returns the neighborhood that generated this neighbor.
	 */
	public Neighborhood getNeighborhood() {
		return hood;
	}
    
	/**
     * Sets the neighborhood that generated this neighbor
	 */
	public void setNeighborhood(Neighborhood hood) {
		this.hood = hood;
	}
    
	/**
	 * @return Returns a neighboring solution that was produced by the neighborhood.
	 */
	public SolverSolution getSolution() {
		return solution;
	}
    
	/**
     * Sets a neighboring solution that was produced by the neighborhood
	 */
	public void setSolution(SolverSolution solution) {
		this.solution = solution;
	}
    
	/**
	 * @return Returns the index of the neighboring solution within the neighborhood.
	 */
	public int getIndex() {
		return index;
	}
    
	/**
     * Sets the index of the neighboring solution within the neighborhood
	 */
	public void setIndex(int index) {
		this.index = index;
	}
    
    /**
     * @return Returns the metaheuristic that determined the neighbor to be valid
     */
	public Metaheuristic getMetaheuristic() {
		return metaheuristic;
	}
    
    /**
     * Sets the metaheuristic that determines neighbors to be valid
     */
	public void setMetaheuristic(Metaheuristic metaheuristic) {
		this.metaheuristic = metaheuristic;
	}
}
