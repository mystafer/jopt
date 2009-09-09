package jopt.csp.search;

import jopt.csp.solution.SolverSolution;

/**
 * Represents a set of neighboring solutions that are related to an initial
 * solution.  
 * <p>
 * Note that neighbors can define solutions to variables that should 
 * not be updated from the original solution as well as assign values 
 * to variables not in the scope of the original solution.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.7 $
 */
public interface Neighborhood {
    /**
     * Sets the initial solution to which this neighborhood is related
     */
    public void setInitialSolution(SolverSolution initial);
    
	/**
     * Returns the number of potential solutions contained in the neighborhood
	 */
    public int size();
    
    /**
     * Returns the neighboring solution at index <code>i</code>
     * @param i     Index of neighbor within neighborhood
     * @return      Solution given by the neighbor or null if some pre-propagation
     *              checks show that the neighbor is invalid (and will fail constraint
     *              satisfaction).
     */
    public SolverSolution getNeighbor(int i);
    
    /**
     * Indicates that a neighbor has been selected from this neighborhood
     * and is about to be used to update the initial solution.  This is useful
     * if the neighborhood will adjust the order in which neighbors are produced.
     */
    public void neighborSelected(int i);
    
    /**
     * Returns the neighborhood to which the last selected neighbor belongs  
     * @return Neighborhood represented by the latest selected neighbor
     * 			null if no such neighbor has been selected
     */
	public Neighborhood getSelectedNeighborhood();
    
	/**
     * Returns the offset (ie. index) at which the selected neighbor was found within the
     * selected neighborhood (as returned by getSelectedNeighborhood())  
     * @return the offset into neighborhood represented by the latest selected neighbor
     * 			-1 if no such neighbor has been selected
     */
    public int getSelectedNeighborhoodOffset();
}
