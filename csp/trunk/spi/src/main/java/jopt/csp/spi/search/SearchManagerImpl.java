/*
 * SearchManagerImpl.java
 * 
 * Created on May 27, 2005
 */
package jopt.csp.spi.search;

import jopt.csp.search.LocalSearch;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SearchGoals;
import jopt.csp.search.SearchLimits;
import jopt.csp.search.SearchManager;
import jopt.csp.search.SearchTechniques;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspVariableFactory;

/**
 * Creates and returns classes that can be used to
 * create objects that are used to build search operations
 */
public class SearchManagerImpl implements SearchManager {
    private CspVariableFactory varFact;
    private ConstraintStore store;
    private SearchActions actions;
    private SearchGoals goals;
    private SearchTechniques techniques;
    private LocalSearch localSearch;
    private SearchLimits limits;
    
    public SearchManagerImpl(CspVariableFactory varFact, ConstraintStore store) {
        this.varFact = varFact;
        this.store = store;
    }
    
	// javadoc inherited from SearchManager
    public SearchActions getSearchActions() {
        if (actions==null) actions = new SearchActionsImpl(store);
        return actions;
    }

    // javadoc inherited from SearchManager
    public SearchGoals getSearchGoals() {
        if (goals==null) goals = new SearchGoalsImpl(varFact, store);
        return goals;
    }

    // javadoc inherited from SearchManager
    public SearchTechniques getSearchTechniques() {
        if (techniques==null) techniques = new SearchTechniquesImpl(store);
        return techniques;
    }

    // javadoc inherited from SearchManager
    public LocalSearch getLocalSearch() {
        if (localSearch==null) localSearch = new LocalSearchImpl(store);
        return localSearch;
    }
    
    // javadoc inheritied from SearchManager
    public SearchLimits getSearchLimits() {
    	if (limits == null) limits = new SearchLimitsImpl();
    	return limits;
    }
}
