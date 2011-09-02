package jopt.csp.search;
/**
 * Creates and returns common search techniques that can be used
 * to traverse a search tree when attempting to locate solutions for
 * a CSP problem
 */
public interface SearchTechniques {
    /**
     * Creates a search object that can be used to search for a problem solution
     * as an alternative to the solve methods in the <code>CspSolver</code>
     * the uses a default DFS search technique
     * 
     * @param action        Action that will generate the tree to be searched
     */
    public Search search(SearchAction action);
    
    /**
     * Creates a search object that can be used to search for a problem solution
     * as an alternative to the solve methods in the <code>CspSolver</code>
     * 
     * @param action        Action that will generate the tree to be searched
     * @param technique     Technique used to iterate over the tree
     */
    public Search search(SearchAction action, SearchTechnique technique);
    
    /**
     * Creates a new breadth first search technique
     */
    public SearchTechnique bfs();
    
    /**
     * Creates a new depth first search technique
     */
    public SearchTechnique dfs();

    /**
     * Special action that changes the search technique being used
     * at this point in the search tree
     * 
     * @param technique     Search technique used to iterate over this part of the sub-tree
     * @param action        Action that will generate the tree to be searched
     */
    public SearchAction changeSearch(SearchTechnique technique, SearchAction action);
    
    /**
     * Special action that changes the goal being used
     * at this point in the search tree
     * 
     * @param goal          New goal for this part of the sub-tree
     * @param action        Action that will generate the tree to be searched
     */
    public SearchAction changeSearch(SearchGoal goal, SearchAction action);
    
    /**
     * Special action that changes the goal and search technique being used
     * at this point in the search tree
     * 
     * @param goal          New goal to for this part of the sub-tree
     * @param technique     Search technique used to iterate over this part of the sub-tree
     * @param action        Action that will generate the tree to be searched
     */
    public SearchAction changeSearch(SearchGoal goal, SearchTechnique technique, SearchAction action);
}
