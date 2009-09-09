package jopt.csp.search;

import java.util.Random;

import jopt.csp.search.Neighborhood;
import jopt.csp.solution.SolverSolution;

/**
 * A wrapper of other neighborhoods that returns neighbors from the wrapped
 * neighborhoods in a random, weighted manner.  Differs from the
 * {@link RandomizedNeighborhood} in that the specified weights influence the
 * frequency of the neighbors returned from the associated neighborhood.
 * 
 * For example, if Neighborhood2 and Neighborhood1 are included with weights of
 * 2.0 and 1.0, respectively, the caller is twice as likely to get a neighbor
 * from Neighborhood2 (assuming the neighbors are requested in numeric order)
 * until Neighborhood2 is exhausted.
 * 
 * @author James Boerkoel
 */
public class WeightedRandomizedNeighborhood implements Neighborhood {
    
    private int size;
    private int[] hoodIdxs;
    private int[] hoodOffsets;
    private double[] adjustedWeights;
    private Random rand;
    private Neighborhood[] hoods;
    private double[] originalWeights;
    
    private int lastSelected;
    
    private int nullNeighborCount;
    
    /**
     * Creates a weighted, random, unified neighborhood based on a collection of other neighborhoods
     */
    public WeightedRandomizedNeighborhood(Neighborhood[] hoods, double[] weights) {
        this(hoods, weights, System.currentTimeMillis());
    }
    
    /**
     * Creates a weighted, random, unified neighborhood based on a collection of other neighborhoods
     * with a specific randomizer seed
     */
    public WeightedRandomizedNeighborhood(Neighborhood[] hoods, double[] weights, long randomSeed) {
    	this.hoods = hoods; 
    	this.originalWeights = weights;
    	this.rand = new Random(randomSeed);
    	this.adjustedWeights= new double[originalWeights.length];
    	
    	init();
    }
    
    private void init() {
        this.lastSelected = -1;
    	this.size = 0;
        this.nullNeighborCount = 0;
    	double totalWeight = 0;
        
        int[] currHoodSizes = new int[hoods.length];
    	
    	// calculate neighborhood size(s) and total weight
    	for (int i=0; i<originalWeights.length; i++) {
            int hoodSize = hoods[i].size();
            currHoodSizes[i] = hoodSize;
    		
    		if (hoodSize > 0) {
    			this.size += hoodSize;;
            	totalWeight += originalWeights[i];
            }
    	}
    	
        // normalize (turn into a percentage of the total) the original weights
    	for (int i=0; i<originalWeights.length; i++) {
    		if (currHoodSizes[i] > 0) {
    			this.adjustedWeights[i] = originalWeights[i]/totalWeight;
    		}
    		else {
    			this.adjustedWeights[i] = 0;
    		}
    	}
        
        // initialize the hood offsets and hood indexes and fill with -1
        this.hoodOffsets = new int[size];
        this.hoodIdxs = new int[size];
    	for (int i=0; i<size; i++) {
    		this.hoodOffsets[i] = -1;
    		this.hoodIdxs[i] = -1;
    	}
    	
        // determine from which neighborhood the neighbor at index 'i' will come
        for (int i=0; i<size; i++) {
            double randDoub = rand.nextDouble();
            int index = 0;
            while (randDoub - adjustedWeights[index] > 0) {
                randDoub -= adjustedWeights[index];
                index++;
            }
            hoodIdxs[i] = index;
            
            // if there are no more neighbors from the chosen neighborhood,
            // readjust the weights
            currHoodSizes[index]--;
            if (currHoodSizes[index] == 0){
                double weight = 1.0 - adjustedWeights[index];
                
                adjustedWeights[index] = 0;
                
                for (int j=0;j<adjustedWeights.length; j++) {
                    adjustedWeights[j]= adjustedWeights[j]/weight;
                }
            }
        }
    	
        // determine the offset into each neighborhood at which the neighbor at index 'i' resides
    	int[][] offsets = new int[hoods.length][];
    	int[] hoodCounter = new int[hoods.length];
    	for (int i=0; i<hoods.length; i++) {
            offsets[i] = getRandomizedArray(hoods[i].size());
    	}
    	for (int i=0; i<size; i++) {
    		int index = hoodIdxs[i];
    		this.hoodOffsets[i] = offsets[index][hoodCounter[index]];
    		hoodCounter[index]++;
    	}
    }
    
    /**
     * Helper method to return an array of the specified length with the
     * integers 0 through length-1 (inclusive) randomly distributed
     */
    private int[] getRandomizedArray(int length) {
    	int[] offsets = new int[length];
    	for (int i=0; i<length; i++) {
    		offsets[i]=i;
    	}
    	// shuffle the elements of this array
    	for (int i=length; i>0; i--) {
    		int rint = rand.nextInt(i);
    		int temp = offsets[i-1];
    		offsets[i-1] = offsets[rint];
    		offsets[rint] = temp;
    	}
    	return offsets;
    }
     	
    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
        for (int i=0; i<hoods.length;i++) {
            hoods[i].setInitialSolution(initial);
        }
        init();
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return size;
    }

    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int index) {
        if ((index > 0) && (index % 100 == 0))
            System.out.println("Neighbor "+(index+1)+" of "+this.size+" requested with "+nullNeighborCount+" discarded");
        
        SolverSolution neighbor = hoods[hoodIdxs[index]].getNeighbor(hoodOffsets[index]);
        
        if (neighbor == null)
            nullNeighborCount++;
        
        return neighbor;
    }

    /**
     * Because this neighborhood is a wrapper of other neighborhoods, this method will return
     * the actual neighborhood to which the most recently selected neighbor belongs; that is,
     * under normal circumstances it will <b>not</b> return a WeightedRandomizedNeighborhood
     * (unless, of course you wrapped a WeightedRandomizedNeighborhood with a
     * WeightedRandomizedNeighborhood, but that's not what I would call 'normal circumstances')
     * 
     * @return Neighborhood represented by the latest selected neighbor, or null if no neighbor
     * has been selected
     */
    public Neighborhood getSelectedNeighborhood() {
    	if (lastSelected<0) {
    		return null;
    	}
    	return hoods[hoodIdxs[lastSelected]];
    }
    
    /**
     * Because this neighborhood is a wrapper of other neighborhoods, this method will return
     * the actual offset (ie. index) into the neighborhood from which the neighbor actually came.
     * 
     * @return int indicating the offset of the lastest selected neighbor in the context of the
     * Neighborhood of the last selected neighbor; returns -1 if no neighbor has been selected
     */
    public int getSelectedNeighborhoodOffset() {
    	if (lastSelected<0) {
    		return lastSelected;
    	}
    	return hoodOffsets[lastSelected];
    }
    
    // javadoc inherited from Neighborhood
    public void neighborSelected(int i) {
    	lastSelected = i;
    	getSelectedNeighborhood().neighborSelected(getSelectedNeighborhoodOffset());
    }
    
}
