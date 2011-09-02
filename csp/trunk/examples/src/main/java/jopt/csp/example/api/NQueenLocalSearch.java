package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.search.LocalSearch;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Solve an NxN problem using local search
 * 
 * This implementation is based on an algorithm contained in the published paper by
 * R. Sosic and J. Gu., "Efficient Local Search with Conflict Minimization: A Case Study of the N-Queens Problem." 
 *      IEEE Transactions on Knowledge and Data Engineering, Vol. 6, 5, pp. 661-668, Oct 1994.
 * 
 * This is not the most efficient implementation of this algorithm since it uses Arc Consistency
 * with a backtracking solver that adds quite a bit of overhead, but it does give
 * a little insight into how local search could be used with the <code>CspSolver</code>
 *  
 * @author Nick Coleman
 * @version $Revision: 1.4 $
 */
public class NQueenLocalSearch {
    //n is size of the problem
    private static int n = 10;

    // maximum number of search steps that will be performed before problem
    // will be re-initialized
    private static int MAX_SEARCH_STEPS = (n<=25) ? 100 * n : 7000;
    
    // value using during initialization that is based on mathmatical analysis
    // of algorithm
    private static double INITIAL_LOOP_FACTOR = 3.08d;
    
    private CspSolver solver;
    private CspVariableFactory varFactory;
    private SearchActions searchActions;
    private LocalSearch localSearch;
    private SolverSolution solution;
    private CspIntVariable xvars[];
    
    public NQueenLocalSearch() {
        this.solver = CspSolver.createSolver();
        this.varFactory = solver.getVarFactory();
        this.searchActions = solver.getSearchActions();
        this.localSearch = solver.getLocalSearch();
        this.solution = new SolverSolution();
        
        // define x variables where
        // xi is located in the ith column of the board on
        // a row number to be assigned to the variable
        this.xvars = new CspIntVariable[n]; 
        for (int i=0; i<n; i++)
            xvars[i] = varFactory.intVar("x" + (i+1), 1, n);
    }
    
    // function called to solve the problem
    private void solve() throws Exception {
        boolean solutionFound = false;
        while (!solutionFound) {
            // create initial solution and determine number of queens that 
            // have collisions to the left in solution
            int initialCollisions = initialSearch();
            
            // perform local search looking for solutions
            solutionFound = finalSearch(initialCollisions);
        }

        // output solution
        System.out.println("-- final");
        System.out.println(solution);
    }
    
    /**
     * Perform local search to find final solution
     */
    private boolean finalSearch(int initialCollisions) {
        // remove any constraint from initial search
        solver.clear();
        
        int searchSteps = 0;
        for (int i = n-initialCollisions; i<n; i++) {
            // retrieve var i
            CspIntVariable varI = xvars[i];
            
            // add collision constraints for i and restore solution to see if it is valid
            boolean noCollisions = addTotalCollisionConstraints(i) &&
                                   solver.solve(searchActions.restoreSolution(solution));
            
            // repeat until no collisions detected for column i
            while (!noCollisions) {
                // exit search if a maximum number of steps has ben exceeded
                if (searchSteps++ > MAX_SEARCH_STEPS)
                    return false;
                
                // find random value between 1 and n
                int j = random(0, n-1);
                
                // no point processing a move that won't change anything
                if (i!=j) {
                    // retrieve var j
                    CspIntVariable varJ = xvars[j];
                    
                    // create a neighboring solution that swaps vars i and j
                    SolverSolution neighbor = new SolverSolution();
                    neighbor.setValue(varI, solution.getValue(varJ));
                    neighbor.setValue(varJ, solution.getValue(varI));
                    
                    // reset solver with collision constraint for i and j
                    solver.clear();
                    noCollisions = addTotalCollisionConstraints(i) &&
                                   addTotalCollisionConstraints(j);
                    
                    if (noCollisions) {
                        // create action to move to next solution
                        SearchAction moveToNeighbor = localSearch.moveToNeighbor(solution, neighbor);
                        SearchAction storeSolution = searchActions.storeSolution(solution);
                        SearchAction move = searchActions.combine(moveToNeighbor, storeSolution);
                        
                        // perform local search and determine if solution exists
                        noCollisions = solver.solve(move);
                    }
                }
            }
        }
        
        return true;
    }
    
    /**
     * Returns a random value between i and j
     */
    private int random(int i, int j) {
        return (int) Math.round(Math.random() * (j-i)) + i;
    }
    
    /**
     * Initializes solution for n-queen problem before local search starts
     * 
     * @return number of columns with collisions to the left
     */
    private int initialSearch() {
        // remove any constraint from previous search
        solver.clear();
        
        // start solution with each queen is on same column as row
        for (int i=0; i<n; i++)
            solution.setValue(xvars[i], i+1);
        
        // attempt to place queens with no collisions to the left
        int j = 0;
        for (int i=0; i<(INITIAL_LOOP_FACTOR * n) && j < n; i++) {
            // find random value between j and n
            int m = random(j, n-1);
            
            // no point processing a move that won't change anything
            if (j!=m) {
                // retrieve values for m and j
                CspIntVariable varM = xvars[m];
//                int oldM = solution.getValue(varM);
                
                CspIntVariable varJ = xvars[j];
//                int oldJ = solution.getValue(varJ);
                
                // create a neighboring solution that swaps vars m and j
                SolverSolution neighbor = new SolverSolution();
                neighbor.setValue(varM, solution.getValue(varJ));
                neighbor.setValue(varJ, solution.getValue(varM));
                
                // add constraints to restrict conflicts to left of j
                solver.clear();
                boolean noCollisions = addPartialCollisionConstraints(j);
                
                // if constraints were added successfull, attempt to
                // move to neighboring solution
                if (noCollisions) {
                    // create action to move to next solution
                    SearchAction moveToNeighbor = localSearch.moveToNeighbor(solution, neighbor);
                    SearchAction storeSolution = searchActions.storeSolution(solution);
                    SearchAction move = searchActions.combine(moveToNeighbor, storeSolution);
                    
                    // perform local search and determine if solution exists.
                    // if move exists, all cols to left of j are not in conflict, move to next j
                    if (solver.solve(move))
                        j++;
                }
            }
        }
        
        // place queens with collisions
        for (int i=j; i < n; i++) {
            // find random value between i and n
            int m = random(i, n-1);
            
            // swap values for i and m
            // swap values at m and j
            CspIntVariable mVar = xvars[m];
            int oldM = solution.getValue(mVar);
            
            CspIntVariable iVar = xvars[i];
            int oldI = solution.getValue(iVar);
            
            solution.setValue(mVar, oldI);
            solution.setValue(iVar, oldM);
        }
        
        return n - j;
    }
    
    /**
     * Adds constraints to store that ensure no collisions are made 
     * with any other queens to the left of a given column
     * 
     * @param col       Column constraints should be posted for
     * @return False if unable to add collision constraints
     */
    private boolean addPartialCollisionConstraints(int col) {
        return addCollisionConstraints(col, col-1);
    }
    
    /**
     * Adds constraints to store that ensure no collisions are made 
     * with any other queens for a given column
     * 
     * @param col       Column constraints should be posted for
     * @param lastCol   Last column that should be constrained from left
     * @return False if unable to add collision constraints
     */
    private boolean addTotalCollisionConstraints(int col) {
        return addCollisionConstraints(col, n-1);
    }
    
    /**
     * Adds constraints to store that ensure no collisions are made 
     * with a given column and all left columns up to a final column
     * 
     * @param col       Column constraints should be posted for
     * @param lastCol   Last column that should be constrained from left
     * @return False if unable to add collision constraints
     */
    private boolean addCollisionConstraints(int col, int lastCol) {
        try {
            CspIntVariable colVar = xvars[col];
            
            // loop over all columns and add constraints
            CspMath varMath = solver.getVarFactory().getMath();
            for (int i=0; i<=lastCol; i++) {
                CspIntVariable var = xvars[i];

                // don't add constraint from column to itself
                if (i != col) {
                    // Prevent diagonal attack
                    CspIntExpr rowDistance = varMath.abs(var.subtract(colVar));
                    int colDistance = Math.abs(col-i);
                    solver.addConstraint(rowDistance.neq(colDistance));
                }
            }
            
            return true;
        }
        catch(PropagationFailureException propx) {
            return false;
        }
    }
    
    public static void main(String[] args) throws Exception {
        new NQueenLocalSearch().solve();
    }
}
