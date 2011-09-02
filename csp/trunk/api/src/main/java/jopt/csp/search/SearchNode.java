package jopt.csp.search;

/**
 * A <code>SearchNode</code> is a portion of the search tree that is
 * processed by a {@link Search} algorithm.  The <code>SearchNode</code>
 * is also used by goals to indicate a location within a search tree
 * where a solution is contained. 
 * 
 * @author Nick Coleman
 */
public interface SearchNode {
	/**
	 * Returns the depth of this node within the search tree
	 */
	public int getDepth();
}