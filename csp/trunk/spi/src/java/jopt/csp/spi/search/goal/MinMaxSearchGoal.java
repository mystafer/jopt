package jopt.csp.spi.search.goal;

import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchNode;
import jopt.csp.search.SearchNodeReference;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspNumVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for maximization and minimization goals.
 */
public abstract class MinMaxSearchGoal implements SearchGoal {
    protected CspVariableFactory varFact;
    protected ConstraintStore store;
    
    // Expression to maximize or minimize, passed to the constructor
    protected CspNumExpr expr;
    
    // If expr is a variable, then this is equal to expr
    // If expr is not a concrete variable, then this a new variable created by createVar()
    protected CspNumVariable var;
	protected List solutionRefs = null;
    protected boolean hasSolution = false;
    protected double bestObjectiveVal;
    protected boolean constraintPosted = false;
    protected boolean strict = true;
    
    /**
     * Initializes internal goal variables
     * 
     * @param varFact	The variable factory used in variable creation
     * @param store		The constraint store associated with the problem
     * @param expr		The expression to be maximized or minimized
     */
    public MinMaxSearchGoal(CspVariableFactory varFact, ConstraintStore store, CspNumExpr expr, boolean isCurrentASolution) {
    	this.solutionRefs = new LinkedList();
        this.varFact = varFact;
        this.store = store;
        this.expr = expr;
        
        if (expr instanceof CspNumVariable) 
            var = (CspNumVariable) expr;
        else {
            createVar();
        }
        this.hasSolution = isCurrentASolution;
        this.bestObjectiveVal = currentObjectiveVal();
    }
    
    /**
     * Initializes internal goal variables
	 *
     */
    public MinMaxSearchGoal() {
    	this.solutionRefs = new LinkedList();
    }
    
    // javadoc inherited from SearchGoal
    public boolean isOkToActivate(SearchNode node) {
        return true;
    }

    /**
     * Called when searches encounter a solution to determine the proper course of action.
     * The tree location reference is passed in, but there is no need to pass in the state
     * of the problem because this object already knows the expression it is trying to
     * maximize or minimize.  It can look to that expression to see how good (or bad) the
     * current solution is.
     * 
     * @param treeLocationRef   Reference to a node in the search tree where solution was located
     * @return True if solution is acceptable for goal, false if it should be discarded
     */
    public boolean solutionFound(SearchNodeReference treeLocationRef) {
        boolean solutionIsOk = false;
        
        // since this is the first solution, it has to be the best
        if (!hasSolution) {
            bestObjectiveVal = currentObjectiveVal();
            hasSolution = true;
            solutionIsOk = true;
            solutionRefs.add(treeLocationRef);
        }
        
        // compare solutions to determine if node should be kept
        else {
            double newObjVal = currentObjectiveVal();
	        int comparison = compareObjectives(bestObjectiveVal, newObjVal);
	        
	        // determine if an equivalent solution has been found
	        if (!strict && comparison == 0) {
	        	solutionIsOk = true;
	        }
            // solution is new best and not just equivalent
            // to existing best
	        else if (comparison > 0) {
                solutionRefs.clear();
                bestObjectiveVal = newObjVal;
                solutionIsOk = true;
	        }

	        // record solution if it is ok
	        if (solutionIsOk)
	        	solutionRefs.add(treeLocationRef);
        }
        
        return solutionIsOk;
    }
    
    /**
     * Returns the current objective value based on the state of the
     * problem
     */
    protected abstract double currentObjectiveVal();
    
    /**
     * Compares two objective values to determine if the second value is
     * better than the first value
     * 
     * @param bestVal       Best known objective value in comparison
     * @param newVal        New possible value in comparison
     * @return  1 if is improvement, 
     *          0 if is equivalent to first,
     *          -1 if is worse  
     */
    protected abstract int compareObjectives(double bestVal, double newVal);
    
    // javadoc inherited from SearchGoal
    public double bestObjectiveValue() {
    	return bestObjectiveVal;
    }

    // javadoc inherited from SearchGoal
    public void returnBoundToObjectiveValue(double objective) {
        try {
            if (hasSolution)
            	updateBound(objective);
        }
        catch(PropagationFailureException propx) {
        	throw new IllegalStateException("should not be able to return to objective that will fail");
        }
    }
    
    // javadoc inherited from SearchGoal
    public void updateBoundForOpenNode() throws PropagationFailureException {
        // ignore autopropagation during change
        boolean autoProp = store.getAutoPropagate();
        store.setAutoPropagate(false);

        try {
            // post a constraint that will make additional objective
            // as good or better than current objective
            // create constraint if necessary
        	//If the implementor of this class is a Strictly*, it will only improve, and not return answers of equal value
            if (!constraintPosted) {
                // constraint expression to be equal to variable
                store.addConstraint(var.eq(expr), false);
                constraintPosted = true;
            }
            
            // update var's min / max value
            if (hasSolution) {
                updateBound(bestObjectiveVal);
            }
        }
        finally {
            // restore auto propagation, done under finally because we want
            // it to occur even if a PropagationFailureException occurs
            store.setAutoPropagate(autoProp);
        }
    }
    
    // javadoc inherited from SearchGoal
    public int getSolutionReferenceCount() {
        return solutionRefs.size();
    }
    
    // javadoc inherited from SearchGoal
    public SearchNodeReference getSolutionReference(int n) {
        if (n < solutionRefs.size())
            return (SearchNodeReference) solutionRefs.get(n);
        else
            return null;
    }

    /**
     * Updates the bound of the goal's variable based on the objective 
     * value that is given
     * 
     * @param n     New bound for variable
     */
    protected abstract void updateBound(double n) throws PropagationFailureException;
    
    /**
     * Creates a variable if the expression this goal is based upon is not concrete
     */
    private void createVar() {
        if (var==null) {
        	String varName = "_" + expr.getName() + "-bound";
            
        	if (expr instanceof CspIntExpr)
                var = varFact.intVar(varName, (CspIntExpr) expr);
            else if (expr instanceof CspLongExpr)
                var = varFact.longVar(varName, (CspLongExpr) expr);
            else if (expr instanceof CspFloatExpr)
                var = varFact.floatVar(varName, (CspFloatExpr) expr);
            else
                var = varFact.doubleVar(varName, (CspDoubleExpr) expr);
        }
    }
    
    public abstract Object clone();
}
