/*
 * Created on Feb 4, 2005
 */
package jopt.csp.spi.solver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jopt.csp.solution.SolverSolution;
import jopt.csp.solution.VariableSolution;
import jopt.csp.util.DoubleUtil;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * The <code>ConstraintStore</code> object is a basic wrapper for an object implementing the
 * <code>ChoicePointAlgorithm</code> interface; if no particular constraint propagation algorithm
 * is specified, a default version is provided.
 * <p>
 * If the autoPropagate flag is set, the <code>propagate</code> method of the wrapped
 * <code>ChoicePointAlgorithm</code> will be called whenever a variable is changed.  This includes
 * the addition of constraints as well as domain changes that occur outside the algorithm.
 * <p>
 * The <code>ConstraintStore</code> is responsible for activating the domain reduction algorithm
 * in response to variable changes.  It can then be used by a solver to carry out the
 * constraint satisfaction process serving as an intermediary between the solver and
 * the underlying domain reduction.
 * 
 * @author Chris Johnson
 *
 */
public class ConstraintStore implements VariableChangeListener {
    private ChoicePointAlgorithm constraintAlg;
    private boolean autoPropagate;
    private boolean isPropagating;
    private ChoicePointStack stack;
    private Set constraintSet;
    private Set variableSet;

    /**
     * Constructor taking a particular constraint reduction algorithm and autoPropagate value.
     * 
     * @param constraintAlg Any object implementing the <code>ChoicePointAlgorithm</code> interface.
     * @param autoPropagate 	Sets the auto update flag.
     * @param cps 			Sets the <code>ChoicePointStack</code>.
     */
    public ConstraintStore(ChoicePointAlgorithm constraintAlg, boolean autoPropagate, ChoicePointStack cps) {
        this.autoPropagate = autoPropagate;
        this.stack = cps;
        isPropagating = false;
        this.constraintAlg = constraintAlg;
        this.constraintSet = new HashSet();
        this.variableSet = new HashSet();

        // set choice point stack for algorithm
        if (cps!=null)
            this.constraintAlg.setChoicePointStack(cps);
    }

    /**
     * Constructor taking a particular constraint reduction algorithm and ChoicePointStack;
     * turns autoPropagate on.
     * 
     * @param constraintAlg Any object implementing the <code>ChoicePointAlgorithm</code> interface.
     * @param cps Sets the <code>ChoicePointStack</code>.
     */
    public ConstraintStore(ChoicePointAlgorithm constraintAlg, ChoicePointStack cps) {
        this(constraintAlg, true, cps);
    }

    /**
     * Constructor taking a particular constraint reduction algorithm and autoPropagate status but
     * uses a new ChoicePointStack.
     * 
     * @param constraintAlg		Any object implementing the <code>ChoicePointAlgorithm</code> interface.
     * @param autoUpdate	 	Sets the auto update flag.
     */
    public ConstraintStore(ChoicePointAlgorithm constraintAlg, boolean autoUpdate) {
        this(constraintAlg, autoUpdate, new ChoicePointStack());
    }

    /**
     * Constructor specifying a ChoicePointAlgorithm but using a new ChoicePointStack;
     * turns autoPropagate on.
     * 
     * @param constraintAlg Any object implementing the <code>ChoicePointAlgorithm</code> interface.
     */
    public ConstraintStore(ChoicePointAlgorithm constraintAlg) {
        this(constraintAlg, true, new ChoicePointStack());
    }

    /**
     * Used to access the <code>CspVarFactory</code> of the <code>ChoicePointAlgorithm</code>
     * wrapped by this <code>ConstraintStore</code>.
     * 
     * @return the wrapped constraint algorithm.
     */
    public ChoicePointAlgorithm getConstraintAlg() {
        return constraintAlg;
    }

    /**
     * Sets the auto propagate status of this <code>ConstraintStore</code>.
     * 
     * @param autoPropagate The new value of the autoPropagate flag.
     */
    public void setAutoPropagate(boolean autoPropagate) {
        this.autoPropagate = autoPropagate;
    }

    /**
     * Adds a constraint to the constraint reduction algorithm of this <code>ConstraintStore</code>.
     * Constraint will be kept after store is reset.
     * 
     * @param constraint       The constraint to be added to the reduction algorithm.
     * @throws If autoPropagate is set, this forces the wrapped algorithm to propagate the constraint.
     *  If the propagation fails, an appropriate exception is thrown.  If, however, the autoPropagate
     *  is not set, this method will not throw an exception.
     */
    public void addConstraint(CspConstraint constraint) throws PropagationFailureException {
        addConstraint(constraint, true);
    }

    /**
     * Adds a constraint to the constraint reduction algorithm of this <code>ConstraintStore</code>.
     * Allows you to specify whether the constraint will be kept after store is reset.
     * 
     * @param constraint       The constraint to be added to the reduction algorithm.
     * @param keepAfterReset   True if constraint should be kept after reset if performed
     * @throws If autoPropagate is set, this forces the wrapped algorithm to propagate the constraint.
     *  If the propagation fails, an appropriate exception is thrown.  If, however, the autoPropagate
     *  is not set, this method will not throw an exception.
     */
    public void addConstraint(CspConstraint constraint, boolean keepAfterReset) throws PropagationFailureException {
        if (keepAfterReset) constraintSet.add(constraint);

        constraintAlg.addConstraint(constraint);
        ((VariableChangeSource) constraint).addVariableChangeListener(this);
        if(autoPropagate) {
            this.propagate();
        }
    }

    /**
     * Adds a boolean expression as a constraint to the <code>ConstraintStore</code>.
     * 
     * @param bool	Boolean expression representing constraint
     */
    public void addConstraint(CspBooleanExpr bool) throws PropagationFailureException {
        addConstraint(bool.toConstraint(), true);
    }

    /**
     * Adds a variable that may not be a part of a constraint that is posted
     * to be managed by this algorithm.  This is primarily used for search actions that
     * may be generating variables that are not posted in any constraint, but will be
     * in the future.  This ensures the variable's state is maintained as the algorithm
     * searches for a solution.
     * 
     * @param var		       The variable to be added to the reduction algorithm.
     * @param keepAfterReset   True if variable should be kept after reset if performed
     */
    public void addVariable(CspVariable var, boolean keepAfterReset) {
        if (keepAfterReset) variableSet.add(var);
        constraintAlg.addVariable(var);
    }

    /**
     * Sets the <code>ChoicePointStack</code> of the constraint reduction algorithm.
     * 
     * @param choicepoint The <code>ChoicePointStack</code> to use.
     */
    public void setChoicePointStack(ChoicePointStack choicepoint) {
        stack = choicepoint;
        constraintAlg.setChoicePointStack(choicepoint);
    }

    /**
     * Retrieves the <code>ChoicePointStack</code> of the constraint reduction algorithm.
     * 
     * @return The <code>ChoicePointStack</code> used by the wrapped constraint algorithm
     */
    public ChoicePointStack getChoicePointStack() {
        return stack;
    }

    /**
     * Tells the constraint reduction algorithm to propagate the constraints reducing the domains
     * of its variables in an effort to reduce the search space.
     * 
     * @throws PropagationFailureException If the constraint algorithm is unable to propagate
     * its constraints.
     */
    public void propagate() throws PropagationFailureException {
        //set the propagation flag
        isPropagating = true;
        try{
            constraintAlg.propagate();
        }
        catch(PropagationFailureException propx) {
            //the propagation failed
            isPropagating = false;
            throw propx;
        }
        //the propagation succeeded
        isPropagating = false;
    }


    /**
     * Specified by the VariableChangeListener interface, this method responds to variable change
     * events by calling <code>update</code> when a variable change occurs and the proper conditions
     * are met (ie the algorithm is not currently propagating and the autopropagation flag is set).
     * 
     * @param ev The VariableChangeEvent fired as the result of a change to a particular variable
     * @throws PropagationFailureException If the constraint algorithm is unable to propagate
     * its constraints
     */
    public void variableChange(VariableChangeEvent ev) throws PropagationFailureException {
        //if the wrapped constraint reduction algorithm is still propagating, ignore the variable change
        //otherwise; if the autoPropagate flag is set, update the reduction by propagating the change
        if(!isPropagating && autoPropagate) {
            this.propagate();
        }
    }

    /**
     * Returns the current state of a constraint satisfaction problem
     * 
     * @return a <code>StateStore</code> object containing the state information for a
     * particular constraint satisfaction problem 
     */
    public Object getCurrentState() {
        return constraintAlg.getProblemState();
    }

    /**
     * Retrieves the auto propagation status
     * 
     * @return the auto propagate status of this <code>ConstraintStore</code>
     */
    public boolean getAutoPropagate() {
        return this.autoPropagate;
    }

    /**
     * Restores the state of the constraint satisfaction problem using the data stored in
     * the specified <code>StateStore</code> object.  If the autoPropagate flag is set in this
     * <code>ConstraintStore</code>, this method will also call <code>update</code>.
     * 
     * @param storedState The <code>StateStore</code> containing the restoration information
     * @throws PropagationFailureException If the constraint algorithm is unable to propagate
     * its constraints
     */
    public void restoreState(Object storedState) {
        constraintAlg.restoreProblemState(storedState);
    }

    /**
     * Resets all variables and removes all constraints and variables added to store
     */
    public void clear() {
        constraintSet.clear();
        variableSet.clear();
        stack.reset();
    }

    /**
     * Resets all variables by undoing all changes stored in the <code>ChoicePointStack</code>
     * of the wrapped algorithm.  Basically, this resets the constraint satisfaction problem.
     */
    public void reset() throws PropagationFailureException {
        stack.reset();

        // return all added constraints to the algorithm
        Iterator cnstIter = constraintSet.iterator();
        while (cnstIter.hasNext()) {
            CspConstraint constraint = (CspConstraint) cnstIter.next();
            constraintAlg.addConstraint(constraint);
        }

        // return all added variables to the algorithm
        Iterator varIter = variableSet.iterator();
        while (varIter.hasNext()) {
            CspVariable var = (CspVariable) varIter.next();
            constraintAlg.addVariable(var);
        }

        // update constraints all at once
        if(autoPropagate)
            propagate();
    }

    /**
     * Records current state of problem in a solution object for
     * the variables that are in the scope of the solution
     */
    public void storeSolution(SolverSolution solution) {
        // loop over variables and extract solution for each
        Iterator varIter = solution.variables().iterator();
        while (varIter.hasNext()) {
            // record data in solution
            CspVariable var = (CspVariable) varIter.next();
            VariableSolution sol = solution.getSolution(var);
            sol.store();
        }

        // store objective value of solution if objective
        // is set in scope
        if (solution.isMinimizeObjective()) {
            solution.setObjectiveVal(DoubleUtil.getMax(solution.getObjectiveExpression()));
        }
        else if (solution.isMaximizeObjective()) {
            solution.setObjectiveVal(DoubleUtil.getMin(solution.getObjectiveExpression()));
        }

        // update statistic information in solution based on new data
        solution.recalcStatistics();
    }

    /**
     * Restores a solution that was previously stored
     */
    public void restoreSolution(SolverSolution solution) throws PropagationFailureException {
        restoreNeighboringSolution(solution, null);
    }


    /**
     * Restores a neighboring solution to another solution that was previously stored.  A
     * neighbor does not need to include any variables that already exist in the initial solution
     * if the values of those variables are what is desired. If the neighbor wants
     * to restore a different value than the initial, it must include it in the neighboring
     * solution.  The neighbor may also define new variables not included in the
     * initial solution. 
     * 
     * @param initial   Initial solution that was previously stored
     * @param neighbor  Neighboring solution to initial solution
     */
    public void restoreNeighboringSolution(SolverSolution initial, SolverSolution neighbor) throws PropagationFailureException {
        boolean oldAutoPropagate = this.autoPropagate;
        this.autoPropagate = false;

        try {
            // loop over neighbor and extract solution for each variable
            if (neighbor!=null) {
                Iterator varIter = neighbor.variables().iterator();
                while (varIter.hasNext()) {
                    // make sure the solution is capable of being restored
                    CspVariable var = (CspVariable) varIter.next();
                    if (neighbor.isRestorable(var)) {
                        VariableSolution sol = neighbor.getSolution(var);
                        sol.restore();
                    }
                }
            }

            // loop over variables and extract solution for each (if it hasn't been done already)
            Iterator varIter = initial.variables().iterator();
            while (varIter.hasNext()) {
                // make sure the solution is capable of being restored
                CspVariable var = (CspVariable) varIter.next();
                if (initial.isRestorable(var) && (neighbor==null || !neighbor.contains(var))) {
                    VariableSolution sol = initial.getSolution(var);
                    sol.restore();
                }
            }

            // make sure to propagate changes if store is auto updating
            if (oldAutoPropagate)
                propagate();
        }
        finally {
            this.autoPropagate = oldAutoPropagate;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("<<<<<<< Constraint Store >>>>>>>\n");

        buf.append("===== Constraints =====\n");
        Iterator iterator = constraintSet.iterator();
        while (iterator.hasNext()) {
            buf.append(iterator.next());
            buf.append('\n');
        }

        buf.append("===== Variables =====\n");
        iterator = variableSet.iterator();
        while (iterator.hasNext()) {
            buf.append(iterator.next());
            buf.append('\n');
        }

        return buf.toString();
    }

}