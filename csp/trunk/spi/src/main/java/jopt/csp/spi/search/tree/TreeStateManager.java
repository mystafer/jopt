package jopt.csp.spi.search.tree;



/**
 * Action that is performed by a search node when it is visited
 * 
 * @author Nick Coleman
 */
public interface TreeStateManager {
    /**
     * Called by search tree when the current node is changed to a child
     * node of existing current node.
     * Node must have previously been activated.
     * 
     * @param previous      Node that was current in tree
     * @param current       Node that is now current in tree
     */
    public void descendedToClosedNode(TreeNode previous, TreeNode current);
    
    /**
     * Called by search tree when the current node is changed to a parent
     * node of existing current node
     * Node must have previously been activated.
     * 
     * @param previous      Node that was current in tree
     * @param current       Node that is now current in tree
     */
    public void ascendedToClosedNode(TreeNode previous, TreeNode current);
    
    /**
     * Called by search tree when the current node in the tree has changed
     * from one node to another and no direct relationship between the two nodes
     * may exist
     * Node must have previously been activated.
     * 
     * @param previous      Node that was current in tree
     * @param current       Node that is now current in tree
     */
    public void jumpedToClosedNode(TreeNode previous, TreeNode current);

    /**
     * Called by search tree when the node that is being moved to is a
     * node that has not yet been activated
     * 
     * @param previous      Node that was current in tree
     * @param current       Node that is now current in tree
     */
    public void descendedToOpenNode(TreeNode previous, TreeNode current);
}
