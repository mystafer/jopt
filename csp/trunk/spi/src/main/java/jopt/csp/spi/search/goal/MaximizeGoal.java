package jopt.csp.spi.search.goal;

import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariableFactory;

/**
 * A goal for the the maximization of an objective function.  The goal
 * is satisfied whenever a solution is found which has an objective value
 * greater than or equal to the previous best solution.
 */
public class MaximizeGoal extends StrictlyMaximizeGoal {
    /**
     * Creates a goal that will maximize a numeric expression
     */
    public MaximizeGoal(CspVariableFactory varFact, ConstraintStore store, CspNumExpr expr, boolean isCurrentASolution) {
        super(varFact, store, expr, 0, isCurrentASolution);
        this.strict = false;
    }
    
    public Object clone() {
        return new MaximizeGoal(varFact, store, expr, false);
    }
}
