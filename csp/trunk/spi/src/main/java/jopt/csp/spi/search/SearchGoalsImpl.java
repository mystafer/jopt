/*

 * SearchGoalsImpl.java
 * 
 * Created on May 27, 2005
 */
package jopt.csp.spi.search;

import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.spi.search.goal.FirstSolutionGoal;
import jopt.csp.spi.search.goal.MaximizeGoal;
import jopt.csp.spi.search.goal.MinimizeGoal;
import jopt.csp.spi.search.goal.StrictlyMaximizeGoal;
import jopt.csp.spi.search.goal.StrictlyMinimizeGoal;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariableFactory;

/**
 * Creates and returns common search goals that can be used
 * to build a search for locating specific solutions for
 * a CSP problem
 */
public class SearchGoalsImpl implements SearchGoals {
	protected CspVariableFactory varFact;
    protected ConstraintStore store;
    
    public SearchGoalsImpl(CspVariableFactory varFact, ConstraintStore store) {
        this.varFact = varFact;
        this.store = store;
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal minimize(CspNumExpr expr) {
        return new MinimizeGoal(varFact, store, expr, false);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal maximize(CspNumExpr expr) {
        return new MaximizeGoal(varFact, store, expr, false);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal strictlyMinimize(CspNumExpr expr, double stepSize) {
        return new StrictlyMinimizeGoal(varFact, store, expr, stepSize, false);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal strictlyMaximize(CspNumExpr expr, double stepSize) {
        return new StrictlyMaximizeGoal(varFact, store, expr, stepSize, false);
    }
    
//  javadoc inherited from SearchGoals
    public SearchGoal minimize(CspNumExpr expr, boolean useCurrentState) {
        return new MinimizeGoal(varFact, store, expr, useCurrentState);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal maximize(CspNumExpr expr, boolean useCurrentState) {
        return new MaximizeGoal(varFact, store, expr, useCurrentState);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal strictlyMinimize(CspNumExpr expr, double stepSize, boolean useCurrentState) {
        return new StrictlyMinimizeGoal(varFact, store, expr, stepSize, useCurrentState);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal strictlyMaximize(CspNumExpr expr, double stepSize, boolean useCurrentState) {
        return new StrictlyMaximizeGoal(varFact, store, expr, stepSize, useCurrentState);
    }
    
    // javadoc inherited from SearchGoals
    public SearchGoal first() {
        return new FirstSolutionGoal();
    }
}