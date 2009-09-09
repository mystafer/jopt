package jopt.csp.search;

import java.util.LinkedList;
import java.util.List;

import jopt.csp.solution.SolverSolution;

/**
 * A collection of neighboring solutions that are predefined
 * and capable of being browsed during a search.  Like a friendly
 * suburb where Mr. Rodgers might live.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.6 $
 */
public class SimpleNeighborhood implements Neighborhood {
    private List neighbors;
    private int lastSelected = -1;
    /**
     * Creates a simple neighborhood based on an initial solution
     */
    public SimpleNeighborhood() {
        this.neighbors = new LinkedList();
    }
    
    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
    	// ignore, this isn't really necessary
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return neighbors.size();
    }
    
    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int i) {
    	return (SolverSolution) neighbors.get(i);
    }
    
    // javadoc inherited from Neighborhood
    public void neighborSelected(int i) {
         this.lastSelected = i;
    }
    
    /**
     * Returns the Neighborhood represented by the latest selected Neighborhood  
     * @return Neighborhood represented by the latest selected Neighborhood
     */
	public Neighborhood getSelectedNeighborhood() {
		return this;
	}
    
	/**
     * Returns the offset into Neighborhood represented by the latest selected Neighborhood  
     * @return the offset into Neighborhood represented by the latest selected Neighborhood
     */
    public int getSelectedNeighborhoodOffset() {
    	return lastSelected;
    }
    
    /**
     * Adds a new neighbor to the neighborhood.  Hi Neighbor!
     */
    public void add(SolverSolution neighbor) {
    	neighbors.add(neighbor);
    }
}
