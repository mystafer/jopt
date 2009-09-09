package jopt.csp.example.api;

import java.util.ArrayList;
import java.util.Date;

import jopt.csp.CspSolver;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.variable.CspSetConstraints;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariableFactory;

/**
 * Simple example dealing with set-based variables.
 */
public class SetExample {
	
	//n is size of the problem
	private static int n = 3;

	public static void main(String[] args) throws Exception {
        CspSolver solver = CspSolver.createSolver();
		
		// define x variables
        CspVariableFactory varFactory = solver.getVarFactory();
        CspSetVariable xvars[] = new IntSetVariable[n]; 
		for (int i=0; i<n; i++)
            xvars[i] = varFactory.intSetVar("x" + (i+1), 0, 2);
		
        // The third is a union of the previous two
        CspSetConstraints setCnsts = varFactory.getSetConstraints();
    	solver.addConstraint(setCnsts.eqUnion(xvars[0], xvars[1], xvars[2]));
        
        // The first and second sets should have nothing in common
    	solver.addConstraint(setCnsts.nullIntersection(xvars[0], xvars[1]));
        
        for (int i=0; i<xvars.length; i++)
            System.out.println(xvars[i]);
        
        // create a scope of variables for solution
        SolutionScope scope = new SolutionScope();
        for (int i=0; i<xvars.length; i++)
            scope.add(xvars[i]);
        
        Date now = new Date();
        ArrayList solutions = new ArrayList();
        boolean solutionFound = solver.solve(xvars);
        int solutionNum = 1;
        while(solutionFound) {
            solutions.add(solver.storeSolution(scope));
            solutionFound = solver.nextSolution();
            solutionNum++;
        }
        
	    //time the generation of solutions
	    Date then = new Date();
	    long totalTime = then.getTime() - now.getTime();
	    System.out.println("totalTime = "+totalTime+"ms for "+solutions.size()+" solutions");

		for (int i=0; i<solutions.size(); i++) {
            SolverSolution sol = (SolverSolution) solutions.get(i);
            
		    System.out.println("----solution #"+(i+1)+"----");
            System.out.println(sol);
		}
	}
	
}
