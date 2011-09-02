/*
 * ImproveSolutionAction.java
 * 
 * Created on Jun 22, 2005
 */
package jopt.csp.spi.search.localsearch;

import jopt.csp.search.SearchAction;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will post a constraint during searching that will require additional 
 * solutions located to be better than the solution given to this action.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.3 $
 * @see jopt.csp.spi.search.actions.AddConstraintAction
 */
public class ImproveSolutionAction implements SearchAction {
    private ConstraintStore store;
	private SolverSolution solution;
    private double step;
    
    public ImproveSolutionAction(ConstraintStore store, SolverSolution solution, double step) {
        this.store = store;
    	this.solution = solution;
        this.step = step;
    }

    // javadoc inherited from SearchAction
    public SearchAction performAction() throws PropagationFailureException {
        // retrieve objective information for solution
        CspNumExpr obj = solution.getObjectiveExpression();
        double objVal = solution.getObjectiveVal();
        
        // create constraint that will improve solution
        CspConstraint constraint = null;
        if (solution.isMinimizeObjective()) {
            double newObj = objVal - step;
            constraint = createImprovementConstraint(obj, newObj, true);
        }
        else if (solution.isMaximizeObjective()) {
            double newObj = objVal + step;
            constraint = createImprovementConstraint(obj, newObj, false);
        }
        
        // post constraint if it was created
        if (constraint!=null)
            store.addConstraint(constraint, false);
        
        return null;
    }
    
    /**
     * Creates constraint to improve an objective
     * 
     * @param obj           Objective expression to improve upon
     * @param newObjVal     New objective value that expression is constrained against
     * @param lessThan      True if objective should be <= to new val, false if objective should be >= new val
     */
    public static CspConstraint createImprovementConstraint(CspNumExpr obj, double newObjVal, boolean lessThan) {
        // int
        if (obj instanceof CspIntExpr) {
        	CspIntExpr expr = (CspIntExpr) obj;
            
            if (lessThan)
            	return expr.leq(DoubleUtil.intFloor(newObjVal));
            else
                return expr.geq(DoubleUtil.intCeil(newObjVal));
        }
        
        // long
        else if (obj instanceof CspLongExpr) {
            CspLongExpr expr = (CspLongExpr) obj;
            
            if (lessThan)
                return expr.leq(DoubleUtil.longFloor(newObjVal));
            else
                return expr.geq(DoubleUtil.longCeil(newObjVal));
        }
        
        // float
        else if (obj instanceof CspFloatExpr) {
            CspFloatExpr expr = (CspFloatExpr) obj;
            
            if (lessThan)
                return expr.leq((float) newObjVal);
            else
                return expr.geq((float) newObjVal);
        }
        
        // long
        else {
            CspDoubleExpr expr = (CspDoubleExpr) obj;
            
            if (lessThan)
                return expr.leq(newObjVal);
            else
                return expr.geq(newObjVal);
        }
    }
}
