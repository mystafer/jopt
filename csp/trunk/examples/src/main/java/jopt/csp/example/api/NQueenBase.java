package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;

/**
 * Base class for generic version of 4-queen problem that 
 * will solve an NxN problem using different search techniques
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 */
public abstract class NQueenBase {
	//n is size of the problem
	private static int n = 4;

    // function called to solve the problem
	public void solve() throws Exception {
        // create engine used to solve problem
        CspSolver solver = CspSolver.createSolver();
		
		// define x variables where
        // xi is located in the ith column of the board on
        // a row number to be assigned to the variable
        CspVariableFactory varFactory = solver.getVarFactory();
        CspIntVariable xvars[] = null; 
        xvars = new CspIntVariable[n]; 
		for (int i=0; i<n; i++)
			xvars[i] = varFactory.intVar("x" + (i+1), 1, n);
		
        // prevent horizontal attack by keeping 2 queens
        // from being assigned to the same row number
        CspMath varMath = varFactory.getMath();
        solver.addConstraint(varMath.allDifferent(xvars));
        
        // Prevent diagonal attack by ensuring that no two queens
        // are placed the same number of rows and columns apart.
        //
        // This constraint can be visualized by picturing a square.
        // If point a is at (0,0) any point (1,1), (2,2) or even (n,n)
        // is a direct diagnal line on the board from point (0,0)
        //
        // If we can extend this logic when we look at a given random
        // point such as (3,4) on the board.  (1,2), (2,3), (4,5), etc
        // are all within a diagonal line of this point.  We can abstract
        // this by saying (3-n, 4-n) or (3+n, 4+n) is within the 
        // diagonal of the point (3,4).  Notice the distance of the row
        // from the origin point is the same as the distance of the column
        // from the origin point.
        //
        // So a constraint that eliminates diagonals can be built
        // by saying the distance between the rows of any two queens 
        // cannot be equal to the distance between the columns of the queens
	    for (int i=0; i<n; i++) {
            CspIntVariable xi = xvars[i];
            
		    for (int j=i+1; j<n; j++) {
		    	CspIntVariable xj = xvars[j];
                
                CspIntExpr rowDistance = varMath.abs(xj.subtract(xi));
                int colDistance = j-i;
		    	solver.addConstraint(rowDistance.neq(colDistance));
		    }
	    }
        
        // solve for the variables
        search(solver, xvars);
	}
    
    /**
     * function that is overridden to create a search
     * specific technique to solver
     */
    protected abstract void search(CspSolver solver, CspIntVariable xvars[]);
}
