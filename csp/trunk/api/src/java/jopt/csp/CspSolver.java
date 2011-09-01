/*
 * CspSolver.java
 * 
 * Created on May 27, 2005
 */
package jopt.csp;

import java.util.List;

import jopt.csp.search.LocalSearch;
import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.search.SearchLimits;
import jopt.csp.search.SearchManager;
import jopt.csp.search.SearchTechnique;
import jopt.csp.search.SearchTechniques;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspPreProcessor;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Class that is used to construct and solve CSP problems.  The solver can
 * be based on various different CSP algorithms and searching techniques, but it
 * also has default algorithms if the user does not wish to override these options.
 * 
 * @author  Nick Coleman
 */
public abstract class CspSolver {

    protected CspAlgorithm cspAlgorithm;
    protected SearchManager searchMgr;

    /**
     * Creates a new solver based upon a default generalized AC5 bounds algorithm with
     * a default search manager
     */
    public static CspSolver createSolver() {
        return createSolver(null, null);
    }

    /**
     * Creates a new solver based upon a given CSP algorithm and search manager
     * 
     * @param alg   Algorithm the new solver should be based upon.  If null, the
     *              implementing class should use a default of its choice. 
     * @param mgr   Search manager that will be used to locate solutions by solver.
     *              If null, the implementing class should use a default solver of its
     *              choice.
     */
    public static CspSolver createSolver(CspAlgorithm alg, SearchManager mgr) {
        try {
            CspSolver solver = (CspSolver) Class.forName("jopt.csp.spi.SolverImpl").newInstance();
            solver.initSolver(alg, mgr);
            return solver;
        }
        catch(Exception e) {
            throw new RuntimeException("unable to create solver instance", e);
        }
    }

    /**
     * Creates a new solver based upon a default generalized AC5 algorithm with
     * a specific search manager
     * 
     * @param mgr   Search manager that will be used to locate solutions by solver
     */
    public static CspSolver createSolver(SearchManager mgr) {
        return createSolver(null, mgr);
    }

    /**
     * Creates a new solver based upon a specific algorithm with the default
     * search manager
     * 
     * @param alg   Algorithm solver is based upon
     */
    public static CspSolver createSolver(CspAlgorithm alg) {
        return createSolver(alg, null);
    }

    /**
     * Returns the variable factory for the algorithm the solver
     * is based upon
     */
    public CspVariableFactory getVarFactory() {
        return cspAlgorithm.getVarFactory();
    }

    /**
     * Returns a SearchActions object that is used to create common search
     * operations
     */
    public SearchActions getSearchActions() {
        return searchMgr.getSearchActions();
    }

    /**
     * Returns a SearchGoals object that is will create common goals
     * for guiding searches
     */
    public SearchGoals getSearchGoals() {
        return searchMgr.getSearchGoals();
    }

    /**
     * Returns a SearchTechniques object that is used to create common techniques
     * for guiding searches such as Breadth First Searching and Depth
     * First Searching
     */
    public SearchTechniques getSearchTechniques() {
        return searchMgr.getSearchTechniques();
    }

    /**
     * Returns a LocalSearch object that is used to create common objects
     * for use during local neighborhood search operations
     */
    public LocalSearch getLocalSearch() {
        return searchMgr.getLocalSearch();
    }

    /**
     * Returns a SearchLimits object that is used to create common limits
     * for use to control search operations
     */
    public SearchLimits getSearchLimits() {
        return searchMgr.getSearchLimits();
    }

    /**
     * Initializes the solver based on the algorithm and
     * search manager that was given during solver creation.
     * If null arguments are supplied, defaults are used.
     */
    protected abstract void initSolver(CspAlgorithm alg, SearchManager searchMgr);

    /**
     * Retrieves the auto propagation status
     * 
     * @return the auto update status of this <code>CspSolver</code>
     */
    public abstract boolean getAutoPropagate();

    /**
     * Sets the auto propagation status of this <code>CspSolver</code>.
     * <p>
     * Specifying a value of 'true' will cause propagation to occur immediately.
     * 
     * @param autoPropagate	The new value of the autoUpdate flag.
     */
    public abstract void setAutoPropagate(boolean autoPropagate);
    
    /**
     * Adds a <code>CspPreProcessor</code> to the CSP solver.
     * The preprocessors are used whenever the CSP is reset.
     * 
     * @param preProcessor  the <code>CspPreProcessor</code> for the solver
     */
    public abstract void addPreProcessor(CspPreProcessor preProcessor);
    
    /**
     * Removes a <code>CspPreProcessor</code> associated with the
     * CSP solver
     * 
     * @return  the <code>CspPreProcessor</code>, or null if none exists
     */
    public abstract void removePreProcessor(CspPreProcessor preProcessor);
    
    /**
     * Retrieves the list of <code>CspPreProcessor</code>s associated
     * with the CSP solver
     * 
     * @return the List of pre-processorss
     */
    public abstract List<CspPreProcessor> getPreProcessors();

    /**
     * Propagates the constraints currently defined using the constraint reduction
     * algorithm of this <code>CspSolver</code>.  Note that calls to this method are
     * unnecessary when the auto propagate flag is set.
     * 
     * @return  True if propagation was successful, false if it fails
     */
    public abstract boolean propagate();

    /**
     * Adds a variable to be managed by the solver.
     * This ensures the variable's state is maintained 
     * as the solver searches for a solution.
     */
    public abstract void addVariable(CspVariable var);

    /**
     * Adds a constraint to the constraint reduction algorithm of this <code>CspSolver</code>.
     * Constraint will be kept after store is reset.
     * 
     * @param constraint The constraint to be added to the reduction algorithm.
     * @throws If auto propagate is set, this forces the wrapped algorithm to propagate the constraint.
     *  If the propagation fails, an appropriate exception is thrown.  If, however, the auto propagate
     *  flag is not set, this method will not throw an exception.
     */
    public abstract void addConstraint(CspConstraint constraint) throws PropagationFailureException;

    /**
     * Adds a constraint to the constraint reduction algorithm of this <code>CspSolver</code>.
     * Allows you to specify whether the constraint will be kept after store is reset.
     * 
     * @param constraint       The constraint to be added to the reduction algorithm.
     * @param keepAfterReset   True if constraint should be kept after reset if performed
     * @throws If autoPropagate is set, this forces the wrapped algorithm to propagate the constraint.
     *  If the propagation fails, an appropriate exception is thrown.  If, however, the autoPropagate
     *  is not set, this method will not throw an exception.
     */
    public abstract void addConstraint(CspConstraint constraint, boolean keepAfterReset) throws PropagationFailureException;

    /**
     * Adds a boolean expression as a constraint to the <code>CspSolver</code>.
     * 
     * @param bool  Boolean expression representing a constraint
     */
    public abstract void addConstraint(CspBooleanExpr bool) throws PropagationFailureException;

    /**
     * Resets all variables and removes all constraints and variables added to solver
     */
    public abstract void clear();

    /**
     * Resets all variables by undoing all changes stored in the <code>CspSolver</code>
     * for the wrapped algorithm and leaving any variables/constraints that have been added.
     */
    public abstract void reset();
    
    /**
     * Locates a solution for the current problem given a Search object.
     * 
     * @param search               Search object used to locate solutions
     * @param reset                True if state of problem should be reset before starting search
     * @return True if a solution was found 
     */
    public abstract boolean solve(Search search, boolean reset);

    /**
     * Resets the solver and locates a solution for the current problem given a Search object.
     * 
     * @param search               Search object used to locate solutions
     * @return True if a solution was found 
     */
    public boolean solve(Search search) {
        return solve(search, true);
    }

    /**
     * Locates a solution for the current problem.
     * 
     * @param action               Search action used to locate a solution
     * @param goal                 Goal to guide search towards a solution 
     * @param technique            Search technique used to locate a solution
     * @param continuallyImprove   True if each successive solution found will be an improvement over previous, false
     *                             if the best solution (according to the goal) is found during the original search
     * @param reset                True if state of problem should be reset before starting search
     * @return True if a solution was found 
     */
    public abstract boolean solve(SearchAction action, SearchGoal goal, SearchTechnique technique, boolean continuallyImprove, boolean reset);

    /**
     * Resets the solver and locates a solution for the current problem.
     * 
     * @param action    Search action used to locate a solution
     * @param goal      Goal to guide search towards a solution. 
     * @param technique Search technique used to locate a solution
     * @param continuallyImprove   True if successive solutions found will be an improvement over previous, false
     *                             if the best solution (according to the goal) is found during the original search
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchGoal goal, SearchTechnique technique, boolean continuallyImprove) {
        return solve(action, goal, technique, continuallyImprove, true);
    }

    /**
     * Resets the solver and locates a solution.
     * 
     * @param action    Search action used to locate a solution
     * @param goal      Goal to guide search towards a solution. 
     * @param technique Search technique used to locate a solution
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchGoal goal, SearchTechnique technique) {
        return solve(action, goal, technique, false, true);
    }

    /**
     * Resets the solver and locates a solution.
     * 
     * @param action    Search action used to locate a solution
     * @param technique Search technique used to locate a solution
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchTechnique technique) {
        return solve(action, null, technique, false, true);
    }

    /**
     * Locates a solution for the current problem.
     * 
     * @param action    Search action used to locate a solution
     * @param technique Search technique used to locate a solution
     * @param reset                True if state of problem should be reset before starting search
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchTechnique technique, boolean reset) {
        return solve(action, null, technique, false, reset);
    }

    /**
     * Resets the solver and locates a solution for the current problem 
     * given a search action that defines a set of operations that should 
     * be performed in order to locate a solution.  A default DFS searching 
     * technique is used to find the solution along with a goal to guide the
     * search towards that desired result.
     * 
     * @param action    Search action used to locate a solution
     * @param goal      Goal to guide search towards a solution. 
     * @param continuallyImprove   True if each successive solution found will be an improvement over previous, false
     *                             if the best solution (according to the goal) is found during the original search
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchGoal goal, boolean continuallyImprove) {
        return solve(action, goal, null, continuallyImprove, true);
    }


    /**
     * Resets the solver and locates a solution for the current problem 
     * given a search action that defines a set of operations that should 
     * be performed in order to locate a solution.  A default DFS searching 
     * technique is used to find the solution along with a goal to guide the
     * search towards that desired result.  This will return after
     * all solutions have been located and the best solution has
     * been found.
     * 
     * @param action    Search action used to locate a solution
     * @param goal      Goal to guide search towards a solution. 
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, SearchGoal goal) {
        return solve(action, goal, null, false, true);
    }


    /**
     * Resets the solver and locates a solution for the current problem 
     * given a search action that defines a set of operations that should 
     * be performed in order to locate a solution.  A default DFS searching 
     * technique is used to find the solution.
     * 
     * @param action    Search action used to locate a solution 
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action) {
        return solve(action, (SearchTechnique) null);
    }

    /**
     * Locates a solution for the current problem 
     * given a search action that defines a set of operations that should 
     * be searched in order to locate a solution.  A default DFS searching 
     * technique is used to find the solution.
     * 
     * @param action	Search action used to locate a solution 
     * @param reset		True if solver should be reset before solving
     * @return True if a solution was found 
     */
    public boolean solve(SearchAction action, boolean reset) {
        return solve(action, (SearchTechnique) null, reset);
    }

    /**
     * Resets the solver and locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * @param vars      Array of variables to instantiate
     * @return True if a solution was found 
     */
    public boolean solve(CspIntVariable vars[]) {
        return solve(searchMgr.getSearchActions().generate(vars));
    }

    /**
     * Locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * @param vars		Array of variables to instantiate
     * @param reset     True if solver should be reset before solving
     * @return True if a solution was found 
     */
    public boolean solve(CspIntVariable vars[], boolean reset) {
        return solve(searchMgr.getSearchActions().generate(vars),reset);
    }

    /**
     * Resets the solver and locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * @param vars      Array of variables to instantiate
     * @return True if a solution was found 
     */
    public boolean solve(CspLongVariable vars[]) {
        return solve(searchMgr.getSearchActions().generate(vars));
    }

    /**
     * Resets the solver and locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * Since real numbers can be divided an infinite number of times, a precision
     * value must be specified to indicate when the range of the variable is small
     * enough to consider the variable completely instantiated. 
     * 
     * @param vars          Array of variables to instantiate
     * @param precision     Minimum precision to which variable domain will be reduced
     * @return True if a solution was found 
     */
    public boolean solve(CspFloatVariable vars[], float precision) {
        return solve(searchMgr.getSearchActions().generate(vars, precision));
    }

    /**
     * Resets the solver and locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * Since real numbers can be divided an infinite number of times, a precision
     * value must be specified to indicate when the range of the variable is small
     * enough to consider the variable completely instantiated. 
     * 
     * @param vars          Array of variables to instantiate
     * @param precision     Minimum precision to which variable domain will be reduced
     * @return True if a solution was found 
     */
    public boolean solve(CspDoubleVariable vars[], double precision) {
        return solve(searchMgr.getSearchActions().generate(vars, precision));
    }

    /**
     * Resets the solver and locates a solution for an array of variables within the 
     * problem contained by the solver
     * 
     * @param vars      Array of variables to instantiate
     * @return True if a solution was found 
     */
    public <T> boolean solve(CspSetVariable<T> vars[]) {
        return solve(searchMgr.getSearchActions().generate(vars));
    }

    /**
     * Searches for another solution to a previous search initiated
     * by a call to a <code>solve</code> method.
     * 
     * @return True if a solution was found 
     */
    public abstract boolean nextSolution();

    /**
     * Returns a solution that has recorded the variable domain
     * information for the variables within the scope specified.
     * 
     * @param scope       Scope of variables to include in solution
     */
    public SolverSolution storeSolution(SolutionScope scope) {
        SolverSolution solution = new SolverSolution(scope);
        storeSolution(solution);
        return solution;
    }

    /**
     * Records domain values for variables that are defined in the 
     * scope of the specified solution.
     * 
     * @param solution    Solution object to record data that specifies
     *                    which variables should be captured in solution
     */
    public abstract void storeSolution(SolverSolution solution);

    /**
     * Calls {@link #reset()} before calling {@link #restoreSolution(SolverSolution)}
     * depending on boolean flag passed
     * 
     * @param solution    Solution object with recorded variable data that
     *                    should be restored
     * @param reset       True if solver should be reset before restoration
     */
    public void restoreSolution(SolverSolution solution, boolean reset) throws PropagationFailureException {
        if (reset) reset();
        restoreSolution(solution);
    }

    /**
     * Restores domain state information for variables defined within
     * a solution.  A restoration simply reduces the variables defined
     * within the solutions to be within the limits of the solution.  A
     * <code>reset</code> call must be made before restoring if the solution
     * should not be applied to the current state of the solver but
     * instead should be applied to the original state of the solver.
     * 
     * @param solution    Solution object with recorded variable data that
     *                    should be restored to problem
     */
    public abstract void restoreSolution(SolverSolution solution) throws PropagationFailureException;

    /**
     * Restores a neighboring solution to another solution that was previously stored.  A
     * neighbor does not need to include any variables that already exist in the initial solution
     * if the values of those variables should not be altered from the initial. If the neighbor wants
     * to restore a different value than the initial, it must include it in the neighboring
     * solution.  The neighbor may also define new variables not included in the
     * initial solution. 
     * 
     * @param initial   Initial solution that was previously stored
     * @param neighbor  Neighboring solution to restore
     */
    public abstract void restoreNeighboringSolution(SolverSolution initial, SolverSolution neighbor) throws PropagationFailureException;

}
