/*
 * SearchGoals.java
 * 
 * Created on May 27, 2005
 */
package jopt.csp.search;

import jopt.csp.variable.CspNumExpr;

/**
 * Interface implemented by a class that creates and 
 * returns common search goals that can be used
 * to build a search for locating specific solutions in
 * a CSP problem
 */
public interface SearchGoals {
    /**
     * Creates a goal that will minimize a numeric expression
     * 
     * @param expr  Expression that should be minimized
     */
    public SearchGoal minimize(CspNumExpr expr);
    
    /**
     * Creates a goal that will maximize a numeric expression
     * 
     * @param expr  Expression that should be maximized
     */
    public SearchGoal maximize(CspNumExpr expr);
    
    /**
     * Creates a goal that will minimize a numeric expression.
     * It will only obtain a solution if it is better than 
     * the previous.
     * 
     * @param expr  Expression that should be minimized
     * @param stepSize - the size of the step to take when enforcing a new max or minimum
     */
    public SearchGoal strictlyMinimize(CspNumExpr expr, double stepSize) ;
    
    /**
     * Creates a goal that will maximize a numeric expression
     * It will only obtain a solution if it is better than 
     * the previous.
     * 
     * @param expr  Expression that should be maximized
     * @param stepSize - the size of the step to take when enforcing a new max or minimum
     */
    public SearchGoal strictlyMaximize(CspNumExpr expr, double stepSize) ;
    
    /**
     * Creates a goal that will minimize a numeric expression
     * 
     * @param expr  Expression that should be minimized
     * @param useCurrentState - if true, uses the current state of the problem as a bound 
     */
    public SearchGoal minimize(CspNumExpr expr, boolean useCurrentState);
    
    /**
     * Creates a goal that will maximize a numeric expression
     * 
     * @param expr  Expression that should be maximized
     * @param useCurrentState - if true, uses the current state of the problem as a bound
     */
    public SearchGoal maximize(CspNumExpr expr, boolean useCurrentState);
    
    /**
     * Creates a goal that will minimize a numeric expression.
     * It will only obtain a solution if it is better than 
     * the previous.
     * 
     * @param expr  Expression that should be minimized
     * @param stepSize - the size of the step to take when enforcing a new max or minimum
     * @param useCurrentState - if true, uses the current state of the problem as a bound
     */
    public SearchGoal strictlyMinimize(CspNumExpr expr, double stepSize, boolean useCurrentState) ;
    
    /**
     * Creates a goal that will maximize a numeric expression
     * It will only obtain a solution if it is better than 
     * the previous.
     * 
     * @param expr  Expression that should be maximized
     * @param stepSize - the size of the step to take when enforcing a new max or minimum
     * @param useCurrentState - if true, uses the current state of the problem as a bound
     */
    public SearchGoal strictlyMaximize(CspNumExpr expr, double stepSize, boolean useCurrentState) ;
    
    /**
     * Creates a goal that will direct the search to end
     * after the first solution is found.
     */
    public SearchGoal first();
}