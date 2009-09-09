package jopt.js.api.search;

/**
 * Interface for an implementation that will return classes that are used to
 * build a search plan for locating solutions to a Job Scheduling problem
 */
public interface JsSearchManager {
    /**
     * Returns a JsSearchActions object that is used to create common search
     * operations such as variable instantiation
     * @return A job scheduler-specific instance of SearchActions
     */
    public JsSearchActions getSearchActions();

    /**
     * Returns a JsSearchGoals object that is used to create common goals
     * for guiding searches
     * @return A job scheduler specific instance of SearchGoals
     */
    public JsSearchGoals getSearchGoals();

    /**
     * Returns SearchTechniques object that is used to create common techniques
     * for guiding searches such as Breadth First Searching and Depth
     * First Searching
     * @return A job scheduler-specific instance of SearchTechniques
     */
    public JsSearchTechniques getSearchTechniques();

    /**
     * Returns a JsLocalSearch object that is used to create common objects
     * for use during local neighborhood search operations
     * @return A job scheduler-specific instance of LocalSearch
     */
    public JsLocalSearch getLocalSearch();
}
