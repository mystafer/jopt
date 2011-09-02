package jopt.csp.search;

/**
 * Interface for a class that will return classes used to
 * build a search plan for locating solutions to a CSP problem
 */
public interface SearchManager {
    /**
     * Returns SearchActions object that is used to create common search
     * operations such as variable instantiation for node generation
     */
    public SearchActions getSearchActions();

    /**
     * Returns SearchGoals object that is used to create common goals
     * for guiding searches
     */
    public SearchGoals getSearchGoals();
    
    /**
     * Returns SearchTechniques object that is used to create common techniques
     * for guiding searches such as Breadth First Searching and Depth
     * First Searching
     */
    public SearchTechniques getSearchTechniques();
    
    /**
     * Returns a LocalSearch object that is used to create common objects
     * for use during local neighborhood search operations
     */
    public LocalSearch getLocalSearch();
    
    /**
     * Returns a SearchLimits object that is used to create common limits
     * for use to control search operations
     */
    public SearchLimits getSearchLimits();
}
