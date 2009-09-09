package jopt.csp.search;

/**
 * This class is used to limit a search.  Potential limits
 * include those based on time, the number of nodes explored,
 * the number of failures encountered, etc.
 */
public interface SearchLimit extends Cloneable {
	
	/**
	 * Initializes search limit based on a starting search node
	 * 
	 * @param node	Node that search tree starts with to initialize search limit
	 */
	public void init(SearchNode node);
	
	/**
	 * Called before each search node is activated by search tree to
	 * determine if a search should continue
	 * 
	 * @param node	Node to evaluate to determine if search should continue
	 * @return True if node is okay to activate, false if the search should end
	 */
	public boolean isOkToContinue(SearchNode node);
	
	public Object clone();
}
