package jopt.csp.spi.search.tree;

import jopt.csp.search.SearchNodeReference;

/**
 * Search tree for node generators that can jump directly to any
 * node in the tree and ensure the state of the problem is maintained correctly.
 * <p>
 * <b>Design Notes</b><br>
 * This tree along with the {@link ProblemStateManager} evolved from a class
 * named <code>SearchNodeHeavy</code> which stored the entire state of the problem.
 * If you want to know more about how state of the problem is stored and restored
 * as this tree is moved through, see the documentation in {@link ProblemStateManager}.
 * <p>
 * This tree has a computational advantage over the {@link CrawlingSearchTree} at
 * the possible disadvantage of memory usage. It is capable of jumping directly
 * from any node at any point in the search tree to another node located anywhere
 * else in the tree.  The {@link CrawlingSearchTree} can only get from one
 * node to another by moving through the tree from parent-to-child and child-to-parent.
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 * @author Nick Coleman
 * @version $Revision: 1.9 $
 * @see CrawlingSearchTree
 * @see TreeStateManager
 */
public class JumpingSearchTree extends AbstractSearchTree {
    /**
     * Constructor for a search tree used to manage nodes
     * 
     * @param rootNode  Root node of the tree
     */
    public JumpingSearchTree(TreeNode rootNode, ProblemStateManager stateMgr) {
        super(rootNode, stateMgr);
    }
    
    
    /**
     * Returns a reference that can be returned to using
     * <code>returnToReference<code>
     */
    public SearchNodeReference getReferenceForNode(TreeNode n) {
        JumpingNodeRef jref = new JumpingNodeRef();
        jref.n = n;
    	return jref;
    }

    /**
     * Moves to location in tree to which reference refers
     */
    public void returnToReference(SearchNodeReference ref) {
        JumpingNodeRef jref = (JumpingNodeRef) ref; 
        jumpToNode(jref.n);
    }
    
    /**
     * Method that handles jumping from current node to another node
     */
    private void jumpToNode(TreeNode n) {
        TreeNode p = currentNode;
        
        // notify state listener that current node has changed
        // to update state of problem appropriately
        if (n.isClosed()) {
            // jump directly to closed node
            currentNode = (TreeNode) n;
        	stateMgr.jumpedToClosedNode(p, n);
        }
        else if (!n.equals(rootNode)){
            // jump to parent of closed node and move down to open child
            currentNode = (TreeNode) n.getParent();
            stateMgr.jumpedToClosedNode(p, n.getParent());
            moveToOpenChild(n);
        }
    }

    // javadoc inherited from SearchTree
    public SearchTree subtree() {
        return new CrawlingSearchTree(currentNode, (ProblemStateManager) stateMgr);
    }
    
    private class JumpingNodeRef implements SearchNodeReference {
        public TreeNode n;
        
        public String toString() {
        	return "{jref: node(" + n + ")}";
        }
    }
}
