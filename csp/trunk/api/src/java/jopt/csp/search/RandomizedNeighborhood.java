package jopt.csp.search;

import java.util.Random;

import jopt.csp.search.Neighborhood;
import jopt.csp.solution.SolverSolution;

/**
 * A simple wrapper to other neighborhoods that randomizes the order
 * in which neighbors are obtained.
 * 
 * @author James Boerkoel
 */
public class RandomizedNeighborhood implements Neighborhood {
    
    private Neighborhood hood;
    private int size;
    private int[] offsets;
    
    /**
     * Creates a random neighborhood based on another neighborhood
     */
    public RandomizedNeighborhood(Neighborhood hood) {
        this.hood = hood;
    }
    
    /**
     * Creates a random, unified neighborhood based on a collection of other neighborhoods
     */
    public RandomizedNeighborhood(Neighborhood[] hoods) {
        this.hood = new UnifiedNeighborhood(hoods);
        size = hood.size();
        buildRandomOffset();
    }
    
    /**
     * Helper method to build and randomize an array which will
     * map requested indexes to neighborhood offsets
     */
    private void buildRandomOffset() {
    	offsets = new int[size];
    	for (int i=0; i< size; i++) {
    		offsets[i]=i;
    	}
    	//We will now shuffle the elements of this array with random swaps
    	Random rand = new Random();
    	
    	for (int i = size; i > 0; i--) {
    		int rint = rand.nextInt(i);
    		int temp = offsets[i-1];
    		offsets[i-1] = offsets[rint];
    		offsets[rint] = temp;
    	}
    }
    
    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
        hood.setInitialSolution(initial);
        size = hood.size();
        buildRandomOffset();
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return size;
    }

    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int index) {
        return hood.getNeighbor(offsets[index]);
    }
    
    // javadoc inherited from Neighborhood    
    public Neighborhood getSelectedNeighborhood() {
    	return hood.getSelectedNeighborhood();
    }
    
    // javadoc inherited from Neighborhood
    public int getSelectedNeighborhoodOffset() {
    	return hood.getSelectedNeighborhoodOffset();
    }
    
    // javadoc inherited from Neighborhood
    public void neighborSelected(int i) {
    	hood.neighborSelected(i);
    }
}
