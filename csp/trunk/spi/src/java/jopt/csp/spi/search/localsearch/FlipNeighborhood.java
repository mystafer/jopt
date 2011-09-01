package jopt.csp.spi.search.localsearch;

import java.util.Arrays;
import java.util.List;

import jopt.csp.search.Neighborhood;
import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspIntVariable;

/**
 * Creates a neighbor hood that is useful for flipping 0 and 1
 * values on binary variables.  For each variable defined, a
 * neighboring solution will be defined that either assigns
 * the value to 1 or the value to 0.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.7 $
 * @see BrowseNeighborhoodAction
 */
public class FlipNeighborhood implements Neighborhood {
    private SolverSolution initial;
    private List<CspIntVariable> vars;
    private int lastSelected=-1;
    
    /**
     * Creates a flip neighborhood base on an array of binary variables
     */
    public FlipNeighborhood(CspIntVariable vars[]) {
        this.vars = Arrays.asList(vars);
    }

    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
    	this.initial = initial;
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return vars.size();
    }

    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int i) {
        // retrieve initial solution for variable
        CspIntVariable var = (CspIntVariable) vars.get(i);
        
        // determine value to assign to variable
        int val = 0;
        if (initial.isBound(var) && initial.getValue(var) == 0)
            val = 1;
        
        // create neighboring solution
        SolverSolution sol = new SolverSolution();
        sol.setValue(var, val);
        
        return sol;
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
}
