package jopt.csp.solution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariable;

/**
 * A collection of variables that are contained within a solution
 */
public class SolutionScope {
    protected final static int MINIMIZE    = 1;
    protected final static int MAXIMIZE    = 2;
    
	protected List variables;
    protected int objectiveType; // minimize or maximize
    protected CspNumExpr objectiveExpr;
    
    /**
     * Creates a new solution request
     */
    public SolutionScope(){
    	this(null);
    }
    
    /**
     * Creates a new solution request
     * 
     * @param scope     Scope of variables that should be included within request
     */
    public SolutionScope(SolutionScope scope) {
        if (scope!=null) {
            this.variables = new LinkedList(scope.variables());
            this.objectiveType = scope.objectiveType;
            this.objectiveExpr = scope.objectiveExpr;
        }
        else
        	this.variables = new LinkedList();
    }
    
    /**
     * Adds a variable to record in a solution
     */
    public void add(CspVariable var) {
    	variables.add(var);
    }
    
    /**
     * Adds an array of variables to record in a solution
     */
    public void add(CspVariable[] vars) {
        for (int i=0; i<vars.length; i++)
        	add(vars[i]);
    }
    
    /**
     * Removes a variable from the scope of a solution request
     */
    public void remove(CspVariable var) {
    	variables.remove(var);
    }
    
    /**
     * Returns true if a variable is contained within the scope of the solution
     */
    public boolean contains(CspVariable var) {
    	return variables.contains(var);
    }

    /**
     * Returns the list of variables within the scope of the solution
     */
    public List variables() {
        return Collections.unmodifiableList(variables);
    }
    
    /**
     * Sets an objective to minimize the specified expression
     * 
     * @param expr  Expression that is to be minimized
     */
    public void setMinimizeObjective(CspNumExpr expr) {
        this.objectiveType = MINIMIZE;
        this.objectiveExpr = expr;
    }
    
    /**
     * Sets an objective to maximize the specified expression
     * 
     * @param expr  Expression that is to be maximized
     */
    public void setMaximizeObjective(CspNumExpr expr) {
        this.objectiveType = MAXIMIZE;
        this.objectiveExpr = expr;
    }
    
    /**
     * Clears the objective that was previously set
     */
    public void clearObjective() {
        this.objectiveType = 0;
        this.objectiveExpr = null;
    }
    
    /**
     * Returns true if the objective of this solution is to minimize
     * an expression
     */
    public boolean isMinimizeObjective() {
    	return objectiveType == MINIMIZE;
    }

    /**
     * Returns true if the objective of this solution is to maximize
     * an expression
     */
    public boolean isMaximizeObjective() {
        return objectiveType == MAXIMIZE;
    }
    
    /**
     * Returns the objective expression
     */
    public CspNumExpr getObjectiveExpression() {
    	return objectiveExpr;
    }
    
    /**
     * Returns objective int expression
     */
    public CspIntExpr getIntObjectiveExpression() {
        return (CspIntExpr) objectiveExpr;
    }
    
    /**
     * Returns objective long expression
     */
    public CspLongExpr getLongObjectiveExpression() {
        return (CspLongExpr) objectiveExpr;
    }
    
    /**
     * Returns objective float expression
     */
    public CspFloatExpr getFloatObjectiveExpression() {
        return (CspFloatExpr) objectiveExpr;
    }
    
    /**
     * Returns objective double expression
     */
    public CspDoubleExpr getDoubleObjectiveExpression() {
        return (CspDoubleExpr) objectiveExpr;
    }
}
