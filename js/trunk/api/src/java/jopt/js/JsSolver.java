package jopt.js;

import jopt.csp.search.SearchManager;
import jopt.csp.spi.SolverImpl;
import jopt.csp.variable.CspAlgorithm;
import jopt.js.api.search.JsLocalSearch;
import jopt.js.api.search.JsSearchActions;
import jopt.js.api.search.JsSearchGoals;
import jopt.js.api.search.JsSearchTechniques;
import jopt.js.api.variable.JsAlgorithm;
import jopt.js.api.variable.JsVariableFactory;

/**
 * Class that is used to construct and solve JS problems.  The JSSolver is built
 * on top of the CspSolver.  The solver can be based on various different CSP 
 * algorithms and searching techniques, but it also has default algorithms 
 * if the user does not wish to override these options.
 * @author James Boerkoel
 *
 */
public abstract class JsSolver extends SolverImpl{

	/**
	 * Creates a new solver based upon a default generalized AC5 bounds algorithm with
	 * a default search manager
	 * 
	 * @return an instance of an implementation of the JsSolver
	 */
	public static JsSolver createJsSolver() {
		return createSolver(null, null);
	}
	
	/**
	 * Creates a new solver based upon a given CSP algorithm and search manager
	 * 
	 * @param alg   Algorithm solver is based upon
	 * @param mgr   Search manager that will be used to locate solutions by solver
	 * 
	 * @return an instance of an implementation of the JsSolver
	 */
	public static JsSolver createSolver(JsAlgorithm alg, SearchManager mgr) {
		try {
			JsSolver solver = (JsSolver) Class.forName("jopt.js.spi.JsSolverImpl").newInstance();
			solver.initSolver((CspAlgorithm)alg, mgr);
			return solver;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("unable to create solver instance", e);
		}
	}
	
	/**
	 * This is called once the problem is in a state where any subsequent changes are backtrackable.
	 */
	public abstract void problemBuilt() ;
	
	/**
	 * Creates a new solver based upon a specific algorithm with the default
	 * search manager
	 * 
	 * @param alg   Algorithm solver is based upon
	 */
	public static JsSolver createSolver(JsAlgorithm alg) {
		return createSolver(alg, null);
	}
	
	/**
	 * Returns the variable factory for the algorithm the solver
	 * is based upon
	 * 
	 * @return JsVariableFactory for the algorithm the solver
	 * is based upon 
	 */
	public abstract JsVariableFactory getJsVarFactory();
	
	/**
	 * Returns a SearchActions object that is used to create common search
	 * operations
	 * 
	 * @return JsSearchActions that is used to create common search
	 * operations
	 */
	public abstract JsSearchActions getJsSearchActions();
	
	/**
	 * Returns a SearchGoals object that is will create common goals
	 * for guiding searches
	 * 
	 * @return JsSearchGoals that is will create common goals
	 * for guiding searches
	 */
	public abstract JsSearchGoals getJsSearchGoals() ;
	
	/**
	 * Returns a SearchTechniques object that is used to create common techniques
	 * for guiding searches such as Breadth First Searching and Depth
	 * First Searching
	 * 
	 * @return JsSearchTechniques that is used to create common techniques
	 * for guiding searches 
	 */
	public abstract JsSearchTechniques getJsSearchTechniques();
	
	
	/**
	 * Returns a LocalSearch object that is used to create common objects
	 * for use during local neighborhood search operations
	 * 
	 * @return JsLocalSearch is used to create common objects
	 * for use during local neighborhood search operations
	 */
	public abstract JsLocalSearch getJsLocalSearch() ;

}