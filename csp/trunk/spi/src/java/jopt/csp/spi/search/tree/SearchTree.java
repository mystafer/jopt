package jopt.csp.spi.search.tree;

import jopt.csp.search.SearchNodeReference;

/**
 * Interface implemented by search trees that a search routine can traverse.
 * A search tree is composed of {@link TreeNode} objects that are linked
 * from parent to child. A tree is traversed by calling move methods
 * that change the current node in the tree.  The state of the problem
 * and the operations that must be performed to build the tree are all
 * contained within the search tree leaving a search routine to consider
 * only which node must be made current next.
 * <p>
 * <b>Design Notes</b><br>
 * The original DFS and BFS search routines were tightly coupled to the search
 * trees that were traversed.  In fact, we had two implementations of both of
 * these searches since we had two different types of search nodes 
 * (see {@link DeltaStateManager} and {@link TreeStateManager} for more
 * info).  This created a headache when we wanted to sliced base searching
 * since it too would require two different versions.
 * <p>
 * It was decided that the search techniques needed to be separated from
 * the search tree and the management of state of that tree had to
 * be separated as well.  The creation of this interface allowed a single
 * search technique to be created for each type (BFS, DFS, etc.) while
 * the method in which data was stored was independent of the technique.
 * <p>
 * Since movement through a tree is pretty standard regardless of how data
 * is stored, we separated the management of state into a separate
 * interface {@link TreeStateManager}.  This allows the {@link CrawlingSearchTree}
 * in particular to use different methods of memory management.
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 * @author Nick Coleman
 * @version $Revision: 1.7 $
 * @see TreeNode
 * @see DeltaStateManager
 * @see RecalculatingStateManager
 * @see jopt.csp.spi.search.technique.TreeSearchTechnique
 */
public interface SearchTree {
    /**
     * Returns the current search node in the search tree
     */
    public TreeNode getCurrentNode();
    
	/**
	 * Returns the number of children for the current node
	 */
	public int getChildCount();

	/**
	 * Returns the current depth of the tree
	 */
	public int getDepth();

    /**
     * Moves to the root node of the tree
     * 
     * @return new current node in tree
     */
    public TreeNode moveToRoot();
        
	/**
	 * Moves to the parent of the current node of the tree
     * 
     * @return new current node in tree
	 */
	public TreeNode moveToParent();

	/**
	 * Moves to the child with the given index
     * 
     * @return new current node in tree
	 */
	public TreeNode moveToChild(int index);

	/**
	 * Moves to the next child that is not closed
     * 
     * @return new current node in tree or null if there is no child
	 */
	public TreeNode moveToNextOpenChild();

    /**
     * Returns a unique ID that can be assigned to a
     * node in the true
     */
    public int uniqueNodeId();
    
    /**
     * Returns a reference that can be returned to using
     * <code>returnToReference<code>
     */
    public SearchNodeReference getReferenceForNode(TreeNode n);

    /**
     * Moves to location in tree to which reference refers
     */
    public void returnToReference(SearchNodeReference ref);
    
    /**
     * Returns a subtree that starts at the current node
     */
    public SearchTree subtree();
}