package jopt.csp.spi.search.tree;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchTechnique;
import jopt.csp.variable.PropagationFailureException;

/**
 * Special action that changes the goal or search technique being used
 * at this point in the search tree
 * 
 * @author  Nick Coleman
 */
public final class SearchTechniqueChange implements SearchAction {
    private SearchGoal goal;
    private SearchTechnique technique;
    private SearchLimit limit;
    private SearchAction action;

    /**
     * Creates a new search technique change
     *   
     * @param goal          New goal to locate for this part of sub-tree
     * @param technique     Search technique used to iterate over this part of sub-tree
     * @param limit         Search limit used to control search
     * @param action        Action that will generate tree to be searched
     */
    public SearchTechniqueChange(SearchGoal goal, SearchTechnique technique, SearchLimit limit, SearchAction action) {
        this.goal = goal;
        this.technique = technique;
        this.limit = limit;
        this.action = action;
    }

    /**
     * Creates a new search technique change
     *   
     * @param goal          New goal to locate for this part of sub-tree
     * @param technique     Search technique used to iterate over this part of sub-tree
     * @param action        Action that will generate tree to be searched
     */
    public SearchTechniqueChange(SearchGoal goal, SearchTechnique technique, SearchAction action) {
    	this(goal, technique, null, action);
    }
    
    /**
     * Creates a new goal change without changing current search technique
     * 
     * @param goal          New goal to locate for this part of sub-tree
     * @param action        Action that will generate tree to be searched
     */
    public SearchTechniqueChange(SearchGoal goal, SearchAction action) {
        this(goal, null, null, action);
    }
    
    /**
     * Creates a search technique change without affecting current goal
     * 
     * @param technique     Search technique used to iterate over this part of sub-tree
     * @param action        Action that will generate tree to be searched
     */
    public SearchTechniqueChange(SearchTechnique technique, SearchAction action) {
        this(null, technique, null, action);
    }
    
    /**
     * Creates a search limit without affecting current goal
     * 
     * @param limit         Search limit used to control search
     * @param action        Action that will generate tree to be searched
     */
    public SearchTechniqueChange(SearchLimit limit, SearchAction action) {
        this(null, null, limit, action);
    }
    
    /**
     * Simple constructor for no changes
     */
    public SearchTechniqueChange() {
    }
    
    /**
     * This method returns the SearchAction <code>action</code> passed into
     * constructor
     */
    public SearchAction performAction() throws PropagationFailureException {
        return action;
    }

    public String toString() {
        return "goal-change(goal: " + goal + ", technique: " + technique + ", limit: " + limit + ", action: " + action + ")";
    }
    
    public void setGoal(SearchGoal goal) {
    	this.goal = goal;
    }
	public SearchGoal getGoal() {
		return goal;
	}
    
	public void setTechnique(SearchTechnique technique) {
		this.technique = technique;
	}
	public SearchTechnique getTechnique() {
		return technique;
	}
	
	public void setLimit(SearchLimit limit) {
		this.limit = limit;
	}
	public SearchLimit getLimit() {
		return limit;
	}
}