package jopt.csp.spi;

import java.util.ArrayList;
import java.util.List;

import jopt.csp.CspSolver;
import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchManager;
import jopt.csp.search.SearchTechnique;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.SearchManagerImpl;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.technique.TreeSearchTechnique;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericBooleanExpr;
import jopt.csp.variable.CspPreProcessor;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Class that is used to construct and solve CSP problems.  The solver can
 * be based on various different CSP algorithms and searching techniques, but
 * has default algorithms if the user does not wish to override these options.
 * 
 * @author  Nick Coleman
 */
public class SolverImpl extends CspSolver {
    protected ConstraintStore store;
    private Search currentSearch;
    private List<CspPreProcessor> preProcessors;

    /**
     * Creates a default AC5 algorithm used when a new solver
     * is created (initiated) without specifying an algorithm.
     */
    public static ChoicePointAlgorithm createDefaultAlgorithm() {
        return new AC5(CspAlgorithmStrength.BOUNDS_CONSISTENCY);
    }

    //  javadoc inherited from CspSolver
    protected void initSolver(CspAlgorithm alg, SearchManager searchMgr) {
        // create default algorithm if not specified during creation
        this.cspAlgorithm = (alg!=null) ? alg : createDefaultAlgorithm();

        // create constraint store that wraps algorithm
        this.store = new ConstraintStore((ChoicePointAlgorithm) cspAlgorithm);

        // create default search manager if not specified during creation
        this.searchMgr = (searchMgr!=null) ? searchMgr : new SearchManagerImpl(getVarFactory(), store);
        
        this.preProcessors = new ArrayList<CspPreProcessor>();
    }

    // javadoc inherited from CspSolver
    public boolean getAutoPropagate() {
        return store.getAutoPropagate();
    }

    // javadoc inherited from CspSolver
    public void setAutoPropagate(boolean autoPropagate) {
        store.setAutoPropagate(autoPropagate);
        //If auto Propagation is being set to true, it is assumed that it will always be propagating all changes as they come, and thus always be
        //in a consistent state, thus we should be sure to propagate
        if (autoPropagate) {
            propagate();
        }
    }

    // javadoc inherited from CspSolver
    public void addPreProcessor(CspPreProcessor preProcessor) {
        preProcessors.add(preProcessor);
    }
    
    // javadoc inherited from CspSolver
    public void removePreProcessor(CspPreProcessor preProcessor) {
        preProcessors.remove(preProcessor);
    }
    
    // javadoc inherited from CspSolver
    public List<CspPreProcessor> getPreProcessors() {
        return preProcessors;
    }

    // javadoc inherited from CspSolver
    public boolean propagate() {
        try {
            store.propagate();
            return true;
        }
        catch(PropagationFailureException px) {
            return false;
        }
    }

    // javadoc inherited from CspSolver
    public void addVariable(CspVariable var) {
        store.addVariable(var, true);
    }

    // javadoc inherited from CspSolver
    public void addConstraint(CspConstraint constraint) throws PropagationFailureException {
        store.addConstraint(constraint);
    }

    // javadoc inherited from CspSolver
    public void addConstraint(CspConstraint constraint, boolean keepAfterReset) throws PropagationFailureException {
        store.addConstraint(constraint, keepAfterReset);
    }

    // javadoc inherited from CspSolver
    public void addConstraint(CspBooleanExpr bool) throws PropagationFailureException {
        store.addConstraint(bool);
    }

    // Not sure if this is (still) necessary...
    // javadoc inherited from CspSolver
    public void addConstraint(CspGenericBooleanExpr bool) throws PropagationFailureException {
        store.addConstraint(bool);
    }

    // javadoc inherited from CspSolver
    public void clear() {
        store.clear();
    }

    // javadoc inherited from CspSolver
    public void reset() {
        // by default, all added constraints and variables are reposted after resetting all domains
        try {
            store.reset();
        }
        catch(PropagationFailureException propx) {
            System.err.println("Propagation failure on reset");
            propx.printStackTrace();
        }
        
        try {
            for (int i=0; i<preProcessors.size(); i++) {
                ((CspPreProcessor) preProcessors.get(i)).preProcess();
            }
        }
        catch (PropagationFailureException pfe) {
            System.err.println("Propagation failure on preprocess");
            pfe.printStackTrace();
        }
    }
    
    // javadoc inherited from CspSolver
    public boolean solve(Search search, boolean reset) {
        // reset problem information (reset constraint store)
        if (reset) reset();

        // locate first solution
        this.currentSearch = search;
        return currentSearch.nextSolution();
    }

    // javadoc inherited from CspSolver
    public boolean solve(SearchAction action, SearchGoal goal, SearchTechnique technique, boolean continuallyImprove, boolean reset) {
        // create tree search to iterate over actions
        Search search = new TreeSearch(store, action, (TreeSearchTechnique) technique);
        search.setGoal(goal);
        search.setContinuallyImprove(continuallyImprove);

        return solve(search, reset);
    }

    // javadoc inherited from CspSolver
    public boolean nextSolution() {
        // attempt to locate another solution if a search is active
        if (currentSearch!=null && currentSearch.nextSolution()) 
            return true;

        // no other solutions exist
        currentSearch=null;
        return false;
    }

    // javadoc inherited from CspSolver
    public void storeSolution(SolverSolution solution) {
        store.storeSolution(solution);
    }

    // javadoc inherited from CspSolver
    public void restoreSolution(SolverSolution solution) throws PropagationFailureException {
        store.restoreSolution(solution);
    }

    // javadoc inherited from CspSolver
    public void restoreNeighboringSolution(SolverSolution initial, SolverSolution neighbor) throws PropagationFailureException {
        store.restoreNeighboringSolution(initial, neighbor);
    }
}
