package jopt.csp.spi.search.localsearch;

import jopt.csp.search.Metaheuristic;
import jopt.csp.search.NeighborCheck;
import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.PropagationFailureException;

/**
 * Simple metaheuristic whose only impact on local search
 * is to check neighbors against the specified <code>NeighborCheck</code>.
 * 
 * If no <code>NeighborCheck</code> is specified, this
 * metaheuristic has no impact on local search.
 * 
 * @author cjohnson
 */
public class NeighborCheckMetaheuristic implements Metaheuristic {
    
    private NeighborCheck nc;
    
    /**
     * Creates a new neighbor check metaheuristic
     * 
     * @param nc    NeighborCheck implementation to use when checking neighbor acceptability
     */
    public NeighborCheckMetaheuristic(NeighborCheck nc) {
        this.nc = nc;
    }

    // javadoc inherited from Metaheuristic
    public boolean setInitialSolution(SolverSolution initial) throws PropagationFailureException {
        if (nc != null)
            nc.setInitialSolution(initial);
        
        return true;
    }
    
    // javadoc inherited from Metaheuristic
    public boolean isAcceptableNeighbor(SolverSolution neighbor) {
        if (nc != null)
            return nc.isValidNeighbor(neighbor);
        
        return true;
    }
    
    // javadoc inherited from Metaheuristic
    public boolean isRestoredNeighborValid(SolverSolution neighbor) {
    	return true;
    }
    
    // javadoc inherited from Metaheuristic
    public void neighborSelected(SolverSolution neighbor) {
    }
    
    // javadoc inherited from Metaheuristic
    public boolean continueSearch() {
        return true;
    }
    
    // javadoc inherited from Metaheuristic    
    public NeighborCheck getNeighborCheck() {
        return nc;
    }

    // javadoc inherited from Metaheuristic
    public void setNeighborCheck(NeighborCheck nc) {
        this.nc = nc;
    }
}
