package jopt.js.spi;


import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchManager;
import jopt.csp.search.SearchTechnique;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.JsSolver;
import jopt.js.api.search.JsLocalSearch;
import jopt.js.api.search.JsSearchActions;
import jopt.js.api.search.JsSearchGoals;
import jopt.js.api.search.JsSearchManager;
import jopt.js.api.search.JsSearchTechniques;
import jopt.js.api.variable.JsVariableFactory;
import jopt.js.api.variable.SchedulerExpression;
import jopt.js.spi.constraint.SchedulerConstraint;
import jopt.js.spi.search.JsSearchManagerImpl;
import jopt.js.spi.variable.JsVarFactory;

public class JsSolverImpl extends JsSolver{

	JsVariableFactory varFactory;
	JsSearchManager jsSearchMgr;
	boolean editable = true;
	HashSet expressions;
	
	/**
	 * Returns the variable factory for the algorithm the solver
	 * is based upon
	 */
	public JsVariableFactory getJsVarFactory() {
		if (varFactory==null) {
			varFactory = new JsVarFactory();
		}
		return varFactory;
	}
	
    // javadoc inherited from CspSolver
    protected void initSolver(CspAlgorithm alg, SearchManager searchMgr) {
        super.initSolver(alg,searchMgr);
        jsSearchMgr = new JsSearchManagerImpl(getJsVarFactory(),store);
        //We set this to be false to begin with, since this is going to start in an editable state
        this.setAutoPropagate(false);
        expressions = new HashSet();
    }
	
	/**
	 * Returns a SearchActions object that is used to create common search
	 * operations
	 */
	public JsSearchActions getJsSearchActions() {
		return jsSearchMgr.getSearchActions();
	}
	
	/**
	 * Returns a SearchGoals object that is will create common goals
	 * for guiding searches
	 */
	public JsSearchGoals getJsSearchGoals() {
		return jsSearchMgr.getSearchGoals();
	}
	
	/**
	 * Returns a SearchTechniques object that is used to create common techniques
	 * for guiding searches such as Breadth First Searching and Depth
	 * First Searching
	 */
	public JsSearchTechniques getJsSearchTechniques() {
		return jsSearchMgr.getSearchTechniques();
	}
	
	
	/**
	 * Returns a LocalSearch object that is used to create common objects
	 * for use during local neighborhood search operations
	 */
	public JsLocalSearch getJsLocalSearch() {
		return jsSearchMgr.getLocalSearch();
	}

	public void problemBuilt() {
		this.editable = false;
		Iterator expIter = expressions.iterator();
		while (expIter.hasNext()) {
			((SchedulerExpression)expIter.next()).setBuilt(true);
		}
		this.propagate();
		this.setAutoPropagate(true);
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
	public boolean solve(SearchAction action, SearchGoal goal, SearchTechnique technique, boolean continuallyImprove, boolean reset) {
		problemBuilt();
		return super.solve(action,goal, technique, continuallyImprove, reset);
	}
	
    // javadoc inherited from CspSolver
    public void addConstraint(CspConstraint constraint) throws PropagationFailureException {
    	if (constraint instanceof SchedulerConstraint) {
	    	SchedulerExpression[] exprs = ((SchedulerConstraint)constraint).getExpressions();
	    	for (int i=0; i<exprs.length; i++) {
	    		expressions.add(exprs[i]);
	    	}
    	}
        super.addConstraint(constraint);
    }
	
}
