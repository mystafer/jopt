/*
 * Minimize.java
 * 
 * Created on May 26, 2005
 */
package jopt.csp.spi.search.goal;

import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariableFactory;

/**
 * A goal for the the minimization of an objective function.  The goal
 * is satisfied whenever a solution is found which has an objective value
 * less than or equal to the previous best solution.
 */
public class MinimizeGoal extends StrictlyMinimizeGoal {
    /**
     * Creates a goal that will minimize a numeric expression
     */
    public MinimizeGoal(CspVariableFactory varFact, ConstraintStore store, CspNumExpr expr, boolean isCurrentASolution) {
        super(varFact, store, expr, 0, isCurrentASolution);
        this.strict = false;
    }
    
    public Object clone() {
        return new MinimizeGoal(varFact, store, expr,false);
    }
}
