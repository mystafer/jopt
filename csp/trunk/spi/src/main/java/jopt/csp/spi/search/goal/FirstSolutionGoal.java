/*
 * Minimize.java
 * 
 * Created on May 26, 2005
 */
package jopt.csp.spi.search.goal;

import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchNode;
import jopt.csp.search.SearchNodeReference;
import jopt.csp.variable.PropagationFailureException;

/**
 * A goal for finding the first feasible solution.  The goal is satisfied
 * by any solution that the search tree finds.
 */
public class FirstSolutionGoal implements SearchGoal {
    private SearchNodeReference solutionRef;
    
    // javadoc inherited from SearchGoal
    public boolean isOkToActivate(SearchNode node) {
        return (solutionRef==null);
    }

    // javadoc inherited from SearchGoal
    public boolean solutionFound(SearchNodeReference treeLocationRef) {
        // This goal is certainly not very picky!  If the constraints
        // are satisfied, the goal has been reached (regardless
        // of any sort of "objective value".
        solutionRef = treeLocationRef;
        return true;
    }
    
    // javadoc inherited from SearchGoal
    public double bestObjectiveValue() {
        return 0d;
    }

    // javadoc inherited from SearchGoal
    public void returnBoundToObjectiveValue(double objective) {
    }
    
    // javadoc inherited from SearchGoal
    public void updateBoundForOpenNode() throws PropagationFailureException {
    }
    
    // javadoc inherited from SearchGoal
    public int getSolutionReferenceCount() {
        return (solutionRef!=null) ? 1 : 0;
    }
    
    // javadoc inherited from SearchGoal
    public SearchNodeReference getSolutionReference(int n) {
        if (n==0)
            return solutionRef;
        else
            return null;
    }
    
    public Object clone() {
    	return new FirstSolutionGoal();
    }
}
