package jopt.csp.spi.search.localsearch;

import java.util.Arrays;
import java.util.List;

import jopt.csp.search.Neighborhood;
import jopt.csp.solution.DoubleSolution;
import jopt.csp.solution.FloatSolution;
import jopt.csp.solution.IntSolution;
import jopt.csp.solution.LongSolution;
import jopt.csp.solution.SolverSolution;
import jopt.csp.solution.VariableSolution;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.IntegerUtil;
import jopt.csp.util.LongUtil;
import jopt.csp.variable.CspNumVariable;

/**
 * Creates a neighbor hood where each neighbor selects one
 * variable from the initial solution and swaps its value
 * with another variable.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.5 $
 * @see BrowseNeighborhoodAction
 */
public class SwapNeighborhood implements Neighborhood {
    private SolverSolution initial;
    private List<CspNumVariable> vars;
    private int size;
    private int lastSelected = -1;
    /**
     * Creates a swap neighborhood base on an array of binary variables
     */
    public SwapNeighborhood(CspNumVariable vars[]) {
        this.vars = Arrays.asList(vars);
        this.size = calculateSize();
    }
    
    /**
     * Calculates size of neighborhood
     */
    private int calculateSize() {
        int n = vars.size();
    	return n * (n-1) / 2;
    }

    // javadoc inherited from Neighborhood
    public void setInitialSolution(SolverSolution initial) {
    	this.initial = initial;
    }
    
    // javadoc inherited from Neighborhood
    public int size() {
    	return size;
    }

    // javadoc inherited from Neighborhood
    public SolverSolution getNeighbor(int i) {
        
        // determine index and offset of base variable being swapped
        int baseIdx = 0;
        int baseOffset = 0;
        for (int j=0; j<vars.size(); j++) {
            baseIdx = j;
        	int baseSize = vars.size() - j - 1;
            
            if (baseOffset + baseSize > i)
                break;
            else
                baseOffset += baseSize;
        }
        
        // determine index of variable to swap with base
        int swapIdx = baseIdx + i - baseOffset + 1;
        
        // retrieve variables to swap
        CspNumVariable baseVar = (CspNumVariable) vars.get(baseIdx);
        CspNumVariable swapVar = (CspNumVariable) vars.get(swapIdx);
        
        // create neighboring solution
        SolverSolution sol = new SolverSolution();
        sol.add(baseVar);
        sol.add(swapVar);
        
        // retrieve numeric solutions for variables
        VariableSolution newBaseSol = sol.getSolution(baseVar);
        VariableSolution newSwapSol = sol.getSolution(swapVar);
        
        // retrieve iniitial solutions for variables
        VariableSolution initialBaseSol = initial.getSolution(baseVar);
        VariableSolution initialSwapSol = initial.getSolution(swapVar);
        
        // swap values in variable solutions
        assignMinMax(newBaseSol, initialSwapSol);
        assignMinMax(newSwapSol, initialBaseSol);
        
        return sol;
    }
    
    /**
     * Utility function set set min and max values in first solution from values in second
     * solution performing type conversion as necessary
     */
    private void assignMinMax(VariableSolution target, VariableSolution source) {
        if (target instanceof IntSolution) {
        	IntSolution isol = (IntSolution) target;
            isol.setMin(IntegerUtil.getMin(source));
            isol.setMax(IntegerUtil.getMax(source));
        }
        else if (target instanceof LongSolution) {
            LongSolution lsol = (LongSolution) target;
            lsol.setMin(LongUtil.getMin(source));
            lsol.setMax(LongUtil.getMax(source));
        }
        else if (target instanceof FloatSolution) {
            FloatSolution fsol = (FloatSolution) target;
            fsol.setMin(FloatUtil.getMin(source));
            fsol.setMax(FloatUtil.getMax(source));
        }
        else {
            DoubleSolution dsol = (DoubleSolution) target;
            dsol.setMin(DoubleUtil.getMin(source));
            dsol.setMax(DoubleUtil.getMax(source));
        }
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
