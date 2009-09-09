/*
 * SearchTechniquesImpl.java
 * 
 * Created on May 27, 2005
 */
package jopt.csp.spi.search;

import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchTechnique;
import jopt.csp.search.SearchTechniques;
import jopt.csp.spi.search.technique.BreadthFirstSearch;
import jopt.csp.spi.search.technique.DepthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.technique.TreeSearchTechnique;
import jopt.csp.spi.search.tree.SearchTechniqueChange;
import jopt.csp.spi.solver.ConstraintStore;

/**
 * Creates and returns common search techniques that can be used
 * to traverse a search tree when attempting to locate solutions for
 * a CSP problem
 */
public class SearchTechniquesImpl implements SearchTechniques {
    private ConstraintStore store;
    
    public SearchTechniquesImpl(ConstraintStore store) {
        this.store = store;
    }
    
    // javadoc inherited from SearchTechniques
    public Search search(SearchAction action) {
        return new TreeSearch(store, action, (TreeSearchTechnique) dfs());
    }
    
    // javadoc inherited from SearchTechniques
    public Search search(SearchAction action, SearchTechnique technique) {
        return new TreeSearch(store, action, (TreeSearchTechnique) technique);
    }
    
    // javadoc inherited from SearchTechniques
    public SearchTechnique bfs() {
        return new BreadthFirstSearch();
    }
    
    // javadoc inherited from SearchTechniques
    public SearchTechnique dfs() {
        return new DepthFirstSearch();
    }
    
    // javadoc inherited from SearchTechniques
    public SearchAction changeSearch(SearchTechnique technique, SearchAction action) {
    	return new SearchTechniqueChange(technique, action);
    }
    
    // javadoc inherited from SearchTechniques
    public SearchAction changeSearch(SearchGoal goal, SearchAction action) {
        return new SearchTechniqueChange(goal, action);
    }
    
    // javadoc inherited from SearchTechniques
    public SearchAction changeSearch(SearchGoal goal, SearchTechnique technique, SearchAction action) {
        return new SearchTechniqueChange(goal, technique, action);
    }
}
