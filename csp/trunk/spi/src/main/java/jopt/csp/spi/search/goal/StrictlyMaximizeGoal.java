package jopt.csp.spi.search.goal;

import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * A goal for the strict maximization of an objective function.  The goal
 * is satisfied when a solution is found which has an objective value
 * strictly greater than the previous best solution.
 */
public class StrictlyMaximizeGoal extends MinMaxSearchGoal {
	
	public double stepSize;
	
    /**
     * Creates a goal that will maximize a numeric expression
     */
    public StrictlyMaximizeGoal(CspVariableFactory varFact, ConstraintStore store, CspNumExpr expr, double stepSize, boolean isCurrentASolution) {
        super(varFact, store, expr, isCurrentASolution);
        this.stepSize = stepSize;
    }
    
    // javadoc inherited from MinMaxSearchGoal
    protected int compareObjectives(double bestVal, double newVal) {
        if (bestVal < newVal) return 1;
        if (bestVal > newVal) return -1;
        return 0;
    }

    // javadoc inherited from MinMaxSearchGoal
    protected double currentObjectiveVal() {
        return DoubleUtil.getMin(expr);
    }

    // javadoc inherited from MinMaxSearchGoal
    protected void updateBound(double objectiveVal) throws PropagationFailureException {
        if (var instanceof CspIntVariable) {
            CspIntVariable iv = (CspIntVariable) var;
            iv.setMin(DoubleUtil.intCeil(objectiveVal+stepSize));
        }
        else if (var instanceof CspLongVariable) {
            CspLongVariable lv = (CspLongVariable) var;
            lv.setMin(DoubleUtil.longCeil(objectiveVal+stepSize));
        }
        else if (var instanceof CspFloatVariable) {
            CspFloatVariable fv = (CspFloatVariable) var;
            fv.setMin((float) (objectiveVal+stepSize));
        }
        else {
            CspDoubleVariable dv = (CspDoubleVariable) var;
            dv.setMin(objectiveVal+stepSize);
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("Maximize expr[");
        buf.append(expr);
        buf.append("] - best obj[");
        buf.append(bestObjectiveVal);
        buf.append("], var[");
        buf.append(var);
        buf.append("]");
        return buf.toString();
    }
    
    public Object clone() {
        return new StrictlyMaximizeGoal(varFact, store, expr, stepSize, false);
    }
}
