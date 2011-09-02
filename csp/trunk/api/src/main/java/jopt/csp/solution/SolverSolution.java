package jopt.csp.solution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspBooleanVariable;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariable;

/**
 * Implementation of a solution that is produced by a solver
 */
public class SolverSolution extends SolutionScope {
    protected HashMap<CspVariable, VariableSolution> varSolutions;
    protected HashSet<CspVariable> nonRestoreVars;
    protected double objectiveVal;
    
    /**
     * Creates a new solution for the scope requested
     * 
     * @param scope     Scope of variables to include within solution
     */
    public SolverSolution(SolutionScope scope) {
        super(scope);
        this.varSolutions = new HashMap<CspVariable, VariableSolution>();
        this.nonRestoreVars = new HashSet<CspVariable>();
        
        // add solutions for each variable within request
        if (variables.size()>0) {
        	Iterator<CspVariable> varIter = variables.iterator();
            while (varIter.hasNext()) {
                CspVariable var = (CspVariable) varIter.next();
                varSolutions.put(var, createSolution(var));
            }
        }
    }
    
    /**
     * Creates a new solution with no variables defined within the
     * scope.  Variables will need to be added with the <code>add</code>
     * method.
     */
    public SolverSolution() {
        this(null);
    }
    
    /**
     * Copies all data in one solution over the data in this solution.
     * This data includes both variable- and objective-based information.
     */
    public void copy(SolverSolution sol) {
        nonRestoreVars.clear();
        nonRestoreVars.addAll(sol.nonRestoreVars);
        
        // duplicate variable solutions
        varSolutions.clear();
        Iterator<CspVariable> varIter = sol.variables.iterator();
        while (varIter.hasNext()) {
            CspVariable var = (CspVariable) varIter.next();
            variables.add(var);
            
            VariableSolution vs = (VariableSolution) sol.getSolution(var);
            varSolutions.put(var, (VariableSolution) vs.clone());
        }
        
        // copy objective
        objectiveType = sol.objectiveType;
        objectiveExpr = sol.objectiveExpr;
        objectiveVal = sol.objectiveVal;
    }
    
    /**
     * Adds a variable and creates a solution for it
     */
    protected void addSolution(CspVariable var) {
        if (var==null) return;
        
        // make sure variable is not already in solution
        if (!contains(var)) {
            variables.add(var);
    
            // store solution
            varSolutions.put(var, createSolution(var));
        }
    }
    
    // javadoc inherited from SolutionScope
    public void add(CspVariable var) {
        addSolution(var);
    }
    
    /**
     * Adds an integer variable solution
     * 
     * @return Solution stored for variable
     */
    public IntSolution add(CspIntVariable var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Adds a long variable solution
     * 
     * @return Solution stored for variable
     */
    public LongSolution add(CspLongVariable var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Adds a float variable solution
     * 
     * @return Solution stored for variable
     */
    public FloatSolution add(CspFloatVariable var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Adds a double variable solution
     * 
     * @return Solution stored for variable
     */
    public DoubleSolution add(CspDoubleVariable var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Adds a boolean variable solution
     * 
     * @return Solution stored for variable
     */
    public BooleanSolution add(CspBooleanVariable var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Adds a set variable solution
     * 
     * @return Solution stored for variable
     */
    public <T> SetSolution<T> add(CspSetVariable<T> var) {
        addSolution(var);
        return getSolution(var);
    }
    
    /**
     * Handles creating a specific solution object for a variable
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected VariableSolution createSolution(CspVariable var) {
        VariableSolution sol = null;
        
        // create appropriate solution for variable
        if (var instanceof CspIntVariable)
            sol = new IntSolution((CspIntVariable) var);
        else if (var instanceof CspLongVariable)
            sol = new LongSolution((CspLongVariable) var);
        else if (var instanceof CspFloatVariable)
            sol = new FloatSolution((CspFloatVariable) var);
        else if (var instanceof CspDoubleVariable)
            sol = new DoubleSolution((CspDoubleVariable) var);
        else if (var instanceof CspSetVariable)
            sol = new SetSolution((CspSetVariable) var);
        else if (var instanceof CspBooleanVariable)
            sol = new BooleanSolution((CspBooleanVariable) var);
        
        return sol;
    }
    
    // javadoc inherited from SolutionScope
    public void remove(CspVariable var) {
        varSolutions.remove(var);
        
        super.remove(var);
    }
    
    /**
     * Returns a stored solution for an integer variable
     */
    public IntSolution getSolution(CspIntVariable var) {
        return (IntSolution) getSolution((CspVariable) var);
    }
    
    /**
     * Returns a stored solution for a long variable
     */
    public LongSolution getSolution(CspLongVariable var) {
        return (LongSolution) getSolution((CspVariable) var);
    }
    
    /**
     * Returns a stored solution for a float variable
     */
    public FloatSolution getSolution(CspFloatVariable var) {
        return (FloatSolution) getSolution((CspVariable) var);
    }
    
    /**
     * Returns a stored solution for a double variable
     */
    public DoubleSolution getSolution(CspDoubleVariable var) {
        return (DoubleSolution) getSolution((CspVariable) var);
    }
    
    /**
     * Returns a stored solution for a set variable
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> SetSolution<T> getSolution(CspSetVariable<T> var) {
        return (SetSolution) getSolution((CspVariable) var);
    }

    /**
     * Returns a stored solution for a boolean variable
     */
    public BooleanSolution getSolution(CspBooleanVariable var) {
        return (BooleanSolution) getSolution((CspVariable) var);
    }
    
    /**
     * Returns a stored solution for a variable
     */
    public VariableSolution getSolution(CspVariable var) {
        Object sol = varSolutions.get(var);
        if (sol==null) return null;
        return (VariableSolution) sol;
    }
    
    /**
     * Returns true if the state of a variable contained within this
     * solution will be restored when the solution is restored
     */
    public boolean isRestorable(CspVariable var) {
        return !nonRestoreVars.contains(var);
    }

    /**
     * True if a variable contained within this solution is 
     * to be restored when the solution is restored
     */
    public void setRestorable(CspVariable var, boolean restore) {
        if (restore)
            nonRestoreVars.remove(var);
        else
            nonRestoreVars.add(var);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        // loop over variables and convert solutions to strings
        Iterator<CspVariable> varIter = variables.iterator();
        while (varIter.hasNext()) {
            // retrieve next variable and solution
            CspVariable var = (CspVariable) varIter.next();
            VariableSolution sol = (VariableSolution) varSolutions.get(var);
            
            // add solution to string
            if (buf.length()>0) buf.append("\n");
            buf.append(sol);
            
            // check if solution will be restored to solver
            if (nonRestoreVars.contains(var))
                buf.append(" -> will not be restored");
        }
        
        // append objective value if it exists
        switch (objectiveType) {
            case MINIMIZE:
                buf.append("\nminimize objective val: ");
                buf.append(objectiveVal);
                break;
                
            case MAXIMIZE:
                buf.append("\nmaximize objective val: ");
                buf.append(objectiveVal);
                break;
        }
        
        return buf.toString();
    }

    /**
     * Helper function to verify a variable exists.
     * Throws an exception if the specified variable is not in scope.
     */
    private void verifyInScope(CspVariable var) {
        if (!contains(var))
            throw new RuntimeException("solution does not contain " + var);
    }
    
    /**
     * Returns true if variable is bound in this SolverSolution
     */
    public boolean isBound(CspIntVariable var) {
        verifyInScope(var);
        return getSolution(var).isBound();
    }
    
    /**
     * Retrieves minimum value of solution
     */
    public int getMin(CspIntVariable var) {
        verifyInScope(var);
    	return getSolution(var).getMin();
    }

    /**
     * Retrieves maximum value of solution
     */
    public int getMax(CspIntVariable var) {
        verifyInScope(var);
        return getSolution(var).getMax();
    }

    /**
     * Returns the value of the solution for the specified variable.
     * This function will throw an exception if the variable is not already bound. 
     */
    public int getValue(CspIntVariable var) {
        verifyInScope(var);
        return getSolution(var).getValue();
    }
    
    /**
     * Sets minimum value of solution for the specified variable
     */
    public void setMin(CspIntVariable var, int min) {
        add(var).setMin(min);
    }

    /**
     * Sets maximum value of solution for the specified variable
     */
    public void setMax(CspIntVariable var, int max) {
        add(var).setMax(max);
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(CspIntVariable var, int val) {
        add(var).setValue(val);
    }

    
    /**
     * Retrieves minimum value of solution for the specified variable
     */
    public long getMin(CspLongVariable var) {
        verifyInScope(var);
        return getSolution(var).getMin();
    }

    /**
     * Retrieves maximum value of solution for the specified variable
     */
    public long getMax(CspLongVariable var) {
        verifyInScope(var);
        return getSolution(var).getMax();
    }

    /**
     * Returns the value of the solution.  This function will
     * throw an exception if the variable is not already bound. 
     */
    public long getValue(CspLongVariable var) {
        verifyInScope(var);
        return getSolution(var).getValue();
    }
    
    /**
     * Sets minimum value of solution for the specified variable
     */
    public void setMin(CspLongVariable var, long min) {
        add(var).setMin(min);
    }

    /**
     * Sets maximum value of solution for the specified variable
     */
    public void setMax(CspLongVariable var, long max) {
        add(var).setMax(max);
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(CspLongVariable var, long val) {
        add(var).setValue(val);
    }


    /**
     * Retrieves minimum value of solution for the specified variable
     */
    public float getMin(CspFloatVariable var) {
        verifyInScope(var);
        return getSolution(var).getMin();
    }

    /**
     * Retrieves maximum value of solution for the specified variable
     */
    public float getMax(CspFloatVariable var) {
        verifyInScope(var);
        return getSolution(var).getMax();
    }

    /**
     * Returns the value of the solution.  This function will
     * throw an exception if the variable is not already bound. 
     */
    public float getValue(CspFloatVariable var) {
        verifyInScope(var);
        return getSolution(var).getValue();
    }
    
    /**
     * Sets minimum value of solution for the specified variable
     */
    public void setMin(CspFloatVariable var, float min) {
        add(var).setMin(min);
    }

    /**
     * Sets maximum value of solution for the specified variable
     */
    public void setMax(CspFloatVariable var, float max) {
        add(var).setMax(max);
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(CspFloatVariable var, float val) {
        add(var).setValue(val);
    }


    /**
     * Retrieves minimum value of solution for the specified variable
     */
    public double getMin(CspDoubleVariable var) {
        verifyInScope(var);
        return getSolution(var).getMin();
    }

    /**
     * Retrieves maximum value of solution for the specified variable
     */
    public double getMax(CspDoubleVariable var) {
        verifyInScope(var);
        return getSolution(var).getMax();
    }

    /**
     * Returns the value of the solution.  This function will
     * throw an exception if the variable is not already bound. 
     */
    public double getValue(CspDoubleVariable var) {
        verifyInScope(var);
        return getSolution(var).getValue();
    }
    
    /**
     * Sets minimum value of solution for the specified variable
     */
    public void setMin(CspDoubleVariable var, double min) {
        add(var).setMin(min);
    }

    /**
     * Sets maximum value of solution for the specified variable
     */
    public void setMax(CspDoubleVariable var, double max) {
        add(var).setMax(max);
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(CspDoubleVariable var, double val) {
        add(var).setValue(val);
    }
    

    /**
     * Returns true if boolean variable is NOT satisfied in solution
     */
    public boolean isFalse(CspBooleanVariable var) {
        verifyInScope(var);
        return getSolution(var).isFalse();
    }
    
    /**
     * Returns true if boolean variable is satisfied in solution
     */
    public boolean isTrue(CspBooleanVariable var) {
        verifyInScope(var);
        return getSolution(var).isTrue();
    }

    /**
     * Sets the value of the specified variable to true
     */
    public void setTrue(CspBooleanVariable var) {
        add(var).setTrue();
    }
    
    /**
     * Sets the value of the specified variable to false
     */
    public void setFalse(CspBooleanVariable var) {
        add(var).setFalse();
    }
    
    /**
     * Clears the isTrue / isFalse flag and sets variable to
     * an unbound state
     */
    public void clear(CspBooleanVariable var) {
        add(var).clear();
    }


    /**
     * Returns the set of possible values for the variable
     */
    public <T> Set<T> getPossibleSet(CspSetVariable<T> var) {
        verifyInScope(var);
        return getSolution(var).getPossibleSet();
    }
    
    /**
     * Returns the set of required values for the variable
     */
    public <T> Set<T> getRequiredSet(CspSetVariable<T> var) {
        verifyInScope(var);
        return getSolution(var).getRequiredSet();
    }
    
    /**
     * Sets the set of possible values for the variable
     */
    public <T> void setPossibleSet(CspSetVariable<T> var, Set<T> possibleSet) {
        add(var).setPossibleSet(possibleSet);
    }
    
    /**
     * Sets the set of required values for the variable
     */
    public <T> void setRequiredSet(CspSetVariable<T> var, Set<T> requiredSet) {
        add(var).setRequiredSet(requiredSet);
    }

    /**
     * Returns the value stored for this solution's objective expression
     */
    public double getObjectiveVal() {
    	return objectiveVal;
    }
    
    /**
     * Sets the value stored for this solution's objective expression
     */
    public void setObjectiveVal(double num) {
    	this.objectiveVal = num;
    }

    /**
     * Returns objective value as an integer type
     */
    public int getIntObjectiveVal() {
        return (int) objectiveVal;
    }
    
    /**
     * Returns objective value as a long type
     */
    public long getLongObjectiveVal() {
        return (long) objectiveVal;
    }
    
    /**
     * Returns objective value as an integer type
     */
    public float getFloatObjectiveVal() {
        return (float) objectiveVal;
    }
    
    /**
     * Sets an objective to minimize the specified expression
     * 
     * @param expr  Expression that is to be minimized
     */
    public void setMinimizeObjective(CspNumExpr expr) {
    	super.setMinimizeObjective(expr);
        this.objectiveVal = DoubleUtil.getMax(expr);
    }
    
    /**
     * Sets an objective to maximize the specified expression
     * 
     * @param expr  Expression that is to be maximized
     */
    public void setMaximizeObjective(CspNumExpr expr) {
        super.setMaximizeObjective(expr);
        this.objectiveVal = DoubleUtil.getMin(expr);
    }
    
    /**
     * Returns true if any of the variables in the specified SolverSolution differ from
     * those contained in this <code>SolverSolution</code> object
     * 
     * @param neighbor	The SolverSolution to which this solution is compared
     * @return	A boolean indicating whether or not the specified solution is different from this one
     */
    public boolean isDifferent(SolverSolution neighbor) {
        // check for changes to variables between specified neighbor and this solution
        Iterator<CspVariable> varIter = neighbor.variables().iterator();
        while (varIter.hasNext()) {
            CspVariable var = (CspVariable) varIter.next();
            
            // retrieve variable-specific solutions for this and neighbor
            VariableSolution thisVarSol = getSolution(var);
            VariableSolution neighborVarSol = neighbor.getSolution(var);
            
            if(!thisVarSol.equals(neighborVarSol)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates a neighboring solution that will produce a resulting solution from
     * an initial solution
     * 
     * @param initial   Solution neighbor will be relative to
     * @param result    Solution that will be produced when neighbor is applied to initial solution
     * @return Neighboring solution that will convert <code>initial</code> to <code>result</code>
     */
    public static SolverSolution createNeighbor(SolverSolution initial, SolverSolution result) {
        SolverSolution neighbor = new SolverSolution();
        
        // check for changes to variables in initial solution
        Iterator<CspVariable> varIter = initial.variables.iterator();
        while (varIter.hasNext()) {
            CspVariable var = (CspVariable) varIter.next();
            
            // check if variable is in result
            if (result.contains(var)) {
            	// retrieve solutions from both initial and result
                VariableSolution isol = initial.getSolution(var);
                boolean irestore = initial.isRestorable(var);
                VariableSolution rsol = result.getSolution(var);
                boolean rrestore = result.isRestorable(var);

                // check if both solutions are same regarding variable
                if ((irestore != rrestore)  || !isol.equals(rsol)) {
                    // add change to neighbor
                    neighbor.variables.add(var);
                    neighbor.varSolutions.put(var, (VariableSolution) rsol.clone());
                    neighbor.setRestorable(var, rrestore);
                }
            }
        }
        
        // check for new variables not in initial solution
        varIter = result.variables.iterator();
        while (varIter.hasNext()) {
            CspVariable var = (CspVariable) varIter.next();
            
            // check if variable is not initial solution
            if (!initial.contains(var)) {
                // retrieve solution from result and add to neighbor
                VariableSolution rsol = result.getSolution(var);
                boolean rrestore = result.isRestorable(var);

                // add change to neighbor
                neighbor.variables.add(var);
                neighbor.varSolutions.put(var, (VariableSolution) rsol.clone());
                neighbor.setRestorable(var, rrestore);
            }
        }
        
        return neighbor;
    }
    
    /**
     * Recalculates any internal stats that are stored with the solution
     */
    public void recalcStatistics() {}
}
