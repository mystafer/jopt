package jopt.csp.search;

/**
 * Interface for a class that returns common search limits.
 */
public interface SearchLimits {

	/**
	 * Creates a new time limit that will stop searching after a specified amount
	 * of time.
	 * 
	 * @param timeLimitMs	Time in milliseconds to limit search
	 * @return SearchLimit to control search
	 */
	public SearchLimit timeLimit(long timeLimitMs);
	
	/**
	 * Creates a new failure search limit that will stop searching after a specified
	 * number of backtracking failures during searching.
	 * 
	 * @param maxFailures	Maximum number of backtracks allowed before search stops
	 * @return SearchLimit to control search
	 */
	public SearchLimit failLimit(int maxFailures);
	
	/**
	 * Creates a new choicepoint search limit that will stop searching after
	 * a specified number of choicepoints have been evaluated during searching.
	 * 
	 * @param maxChoices	Maximum number of choicepoints to evaluate
	 * @return SearchLimit to control search
	 */
	public SearchLimit choiceLimit(int maxChoices);
	
	/**
	 * Creates a new deadline limit search limiter
	 * 
	 * @param endTime	Time at which the search should end
	 * @return SearchLimit to control search
	 */
	public SearchLimit deadlineLimit(long endTime);
	
	/**
	 * Creates a new combined limit search limit
	 * 
	 * @param limits	Search limits to combine into single limit
	 * @return SearchLimit to control search
	 */
	public SearchLimit combinedLimit(SearchLimit limits[]);
    
    /**
     * Special action that applies a limit to the search technique being used
     * at this point in the search tree
     * 
     * @param limit			Limit to apply to searching on supplied action
     * @param action        Action that will generate the tree to be searched
     */
    public SearchAction limitSearch(SearchLimit limit, SearchAction action);
}
