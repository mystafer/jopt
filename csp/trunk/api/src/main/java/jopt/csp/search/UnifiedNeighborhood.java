package jopt.csp.search;

import jopt.csp.search.Neighborhood;
import jopt.csp.solution.SolverSolution;

/**
 * Creates a neighborhood based on a combination of other neighborhoods.
 * 
 * @author Chris Johnson
 */
public class UnifiedNeighborhood implements Neighborhood {
    
    private Neighborhood[] neighborhoods;
    private int[] neighborhoodSizes;
    private int size;
    private int lastSelected;

    /**
     * Creates a unified neighborhood based on a collection of other neighborhoods
     */
    public UnifiedNeighborhood(Neighborhood neighborhoods[]) {
        this.neighborhoods = neighborhoods;
        this.calculateStatistics();
        this.lastSelected = -1;
    }
    
    /**
     * Helper method to calculate the size of the neighborhood
     * based on the neighborhoods specified during construction
     */
    private void calculateStatistics() {
        this.size = 0;
        neighborhoodSizes = new int[neighborhoods.length];
        for (int i=0; i<neighborhoods.length; i++) {
            neighborhoodSizes[i] = neighborhoods[i].size();
            this.size += neighborhoodSizes[i];
        }
    }

    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
        initial.recalcStatistics();
    	for (int i=0; i<neighborhoods.length; i++) {
            neighborhoods[i].setInitialSolution(initial);            
        }
        
        this.calculateStatistics();
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return size;
    }

    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int index) {
        
        // determine the neighborhood from which the neighbor will come
        // in addition to determining which neighbor within the hood will be used
        int neighborhoodNum = -1;
        for (int i=0; i<neighborhoods.length; i++) {
            if (index - neighborhoodSizes[i] < 0) {
                neighborhoodNum = i;
                break;
            }
            else {
                index -= neighborhoodSizes[i];
            }
        }
        
        return neighborhoods[neighborhoodNum].getNeighbor(index);
    }
    
    // javadoc inherited from Neighborhood
    public Neighborhood getSelectedNeighborhood() {
    	if (lastSelected<0) {
    		return null;
    	}
        // determine the neighborhood from which the neighbor will come
        // in addition to determining which neighbor within the hood will be used
        int neighborhoodNum = 0;
        int index = lastSelected;
        for (int i=0; i<neighborhoods.length; i++) {
            if (index - neighborhoodSizes[i] < 0) {
                neighborhoodNum = i;
                break;
            }
            else {
                index -= neighborhoodSizes[i];
            }
        }
        
        return neighborhoods[neighborhoodNum];
    }
    
    // javadoc inherited from Neighborhood
    public int getSelectedNeighborhoodOffset() {
        // determine the neighborhood from which the neighbor will come
        // in addition to determining which neighbor within the hood will be used
        int index = lastSelected;
        for (int i=0; i<neighborhoods.length; i++) {
            if (index - neighborhoodSizes[i] < 0) {
                break;
            }
            else {
                index -= neighborhoodSizes[i];
            }
        }
        
        return index;
    }
    
    // javadoc inherited from Neighborhood
    public void neighborSelected(int i) {
    	lastSelected = i;
    	getSelectedNeighborhood().neighborSelected(getSelectedNeighborhoodOffset());
    }
}
