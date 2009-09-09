package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchTechnique;
import jopt.csp.variable.CspIntVariable;

/**
 * Implementation of N-queen problem.  Generic version of 4-queen
 * problem that will solve an NxN problem using a breadth first search
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 */
public class NQueenBFS extends NQueenBase {
    // creates a BFS search technique used to solve the problem
    protected void search(CspSolver solver, CspIntVariable xvars[]) {
        SearchAction genVars = solver.getSearchActions().generate(xvars);
        SearchTechnique bfs = solver.getSearchTechniques().bfs();
        
        // search for solutions
        boolean solutionFound = solver.solve(genVars, bfs);
        int solutionNum = 1;
        while(solutionFound) {
            System.out.println("----solution #"+solutionNum+"----");
            for(int j=0; j<xvars.length; j++) {
                System.out.println(xvars[j]);
            }    
            
            solutionFound = solver.nextSolution();
            solutionNum++;
        }
    }
    
	public static void main(String[] args) throws Exception {
        new NQueenBFS().solve();
	}
}
