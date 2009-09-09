package jopt.csp.spi.search.tree;

import jopt.csp.search.SearchNodeReference;


/**
 * Base class for search trees that operate on basic search nodes
 */
public abstract class AbstractSearchTree implements SearchTree {
    protected SearchNodeReference rootRef;
    protected TreeNode rootNode;
    protected TreeNode currentNode;
    protected TreeStateManager stateMgr;
    protected int nextNodeId;
    
    /**
     * Constructor for a search tree used to manage nodes
     * 
     * @param rootNode  Root node of the tree
     */
    protected AbstractSearchTree(TreeNode rootNode, TreeStateManager stateMgr) {
        this.rootNode = rootNode;
        this.currentNode = rootNode;
        this.stateMgr = stateMgr;
    }
    
    /**
     * Returns the current search node in the search tree
     */
    public TreeNode getCurrentNode() {
    	return currentNode;
    }
    
    /**
     * Returns the number of children for the current node
     */
    public int getChildCount() {
        return currentNode.getChildCount();
    }
    
    /**
     * Returns the current depth of the tree
     */
    public int getDepth() {
        if (currentNode!=null)
        	return currentNode.getDepth();
        else
            return -1;
    }

    // javadoc inherited from SearchTree
    public TreeNode moveToRoot() {
    	if (rootRef==null)
    		rootRef = getReferenceForNode(rootNode);
        returnToReference(rootRef);
        
        return rootNode;
    }

    /**
     * Moves to the parent of the current node of the tree
     */
    public TreeNode moveToParent() {
        // check if current node is root
        if (currentNode.getDepth()>0) {
            TreeNode n = currentNode.getParent();
            TreeNode p = currentNode;
            
            // change the current node
            currentNode = (TreeNode) n;
            
            // notify state listener that current node has changed
            // to update state of problem appropriately
            stateMgr.ascendedToClosedNode(p, n);
        }
        
        return currentNode;
    }

    /**
     * Moves to the child with the given index
     */
    public TreeNode moveToChild(int index) {
        TreeNode n = currentNode.getChild(index);
        
        // move to child
        if (n.isClosed())
        	moveToClosedChild(n);
        else
            moveToOpenChild(n);
        
        return currentNode;
    }

    /**
     * Moves to an open child node of the current node
     * 
     * @param child     Open child node to make current node
     */
    protected TreeNode moveToOpenChild(TreeNode child) {
        TreeNode p = currentNode;
        
        // change the current node
        currentNode = child;
        
        // notify state listener that current node has changed
        // to update state of problem appropriately
        stateMgr.descendedToOpenNode(p, child);
        
        return currentNode;
    }

    /**
     * Moves to a closed child node of the current node
     * 
     * @param child     Closed child node to make current node
     */
    protected TreeNode moveToClosedChild(TreeNode child) {
        TreeNode p = currentNode;
        
        // change the current node
        currentNode = (TreeNode) child;
        
        // notify state listener that current node has changed
        // to update state of problem appropriately
        stateMgr.descendedToClosedNode(p, child);
        
        return currentNode;
    }

    /**
     * Moves to the next child that is not closed
     * 
     * @return new current node in tree or null if there is no child
     */
    public TreeNode moveToNextOpenChild() {
        TreeNode n = currentNode.getNextOpenChild();
        
        // check if open child was found
        if (n!=null) {
            return moveToOpenChild(n);
        }
        else
            return null;
    }
    
    /**
     * Returns a unique ID that can be assigned to a
     * node in the true
     */
    public int uniqueNodeId() {
        return nextNodeId++;
    }
}
