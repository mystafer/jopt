package jopt.csp.spi.search.tree;

import java.util.BitSet;
import java.util.List;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * The <code>TreeNode.java</code> is an interface implemented by search nodes that are
 * used in searching in a tree-based structure.
 * 
 * @author Chris Johnson
 * @author Nick Coleman
 */
public interface TreeNode extends SearchNode {
	/**
	 * Returns the number of the child within the parent
	 */
	public int getChildNumberWithinParent();

	/**
	 * Retrieves the parent of this search node
	 * 
	 * @return the parent of this search node or null if no parent exists (root node)
	 */
	public TreeNode getParent();

	/**
	 * Called by search when node should be activated.  This will cause
     * the node to be closed and generate the children for this node.  Only an
     * open node can be activated.
     * 
     * @param goal  Optional goal to that is being sought after
     * @return Object containing change for technique to use in child nodes, null if no change
	 */
	public SearchTechniqueChange activate(SearchGoal goal) throws PropagationFailureException;

    /**
     * Called by search if child and state data should be discarded.
     * This method is similar to <code>prune()</code>, but it does not set 
     * the isPruned flag. This is used in re-calculating trees to
     * allow a node to be reactivated later. All references to other nodes
     * including child and parents are removed to allow node to be
     * reconnected at a later time.
     * 
     * A node can only be deactivated if it was already closed and is
     * not pruned.
     */
    public void deactivate();

    /**
     * Called by search if child and state data should be recalculated.
     * This method is similar to activate, but it will not fail since
     * it has already been calculated before.
     * 
     * A node should only be reactivated if it was previously deactivated.
     * 
     * @param parent        Parent node to reconnect node to tree
     */
    public void reactivate(TreeNode parent);

    /**
     * Called by search to disconnect a child node from a parent node.
     * Only an open node can be disconnected.
     * TODO: Currently {@link RecalculatingStateManager} does sometimes
     * call disconnect on a pruned node.  For this reason, the requirement
     * that only an open node can be disconnected has been temporarily 
     * removed.  Is this right in the long term?
     */
    public void disconnect();

    /**
     * Called by search to reconnect a child node to a parent node.
     */
    public void reconnect(TreeNode parent);

	/**
	 * Called by search to prune node which will discard all child nodes
     * and state information.  The isPruned flag is set after calling
     * this method.
	 */
	public void prune();

	/**
	 * Returns true if node is closed or pruned or deactivated.
	 */
	public boolean isClosed();

	/**
	 * Returns true if node has been pruned
	 */
	public boolean isPruned();

    /**
     * Returns true if node is closed and the <code>deactivate()</code>
     * method has been called on this node
     */
    public boolean isDeactivated();

    /**
     * Returns true if node is opened but is disconnected from it's parent
     * node
     */
    public boolean isDisconnected();
    
	/**
	 * Returns the number of children this node contains
	 */
	public int getChildCount();

	/**
	 * Returns the child at the specified index
	 */
	public TreeNode getChild(int index);

	/**
	 * Returns the next open child in the tree
	 */
	public TreeNode getNextOpenChild();

    /**
     * Returns a list of Integers containing the list of movements followed to reach
     * this node.  Each Integer represents a childNumber.
     */
    public List<Integer> getPath();
    
    /**
     * Returns a bitset representing the path from the root to this node as a series
     * of left-right movements.  Left is a zero value and right is a one.  Because
     * search nodes are allowed to have more than two children, any child that has
     * childNum > 0 is treated as a right-ward movement.  For this reason, getBinaryPath
     * does not return unique values for every node; two sibling nodes that are not the
     * first child of their parent will have identical binary paths.
     */
    public BitSet getBinaryPath();
    
    /**
     * Returns state data associated with this node
     */
    public Object getStateData();
    
    /**
     * Sets state data associated with this node
     */
    public void setStateData(Object stateData);
    
    /**
     * Returns true if this node is binary
     */
    public boolean isBinary();
    
    /**
     * Sets the goal value associated with this node
     */
    public void setGoal(SearchGoal goal);
    
    /**
     * Gets the goal value associated with this node
     */
    public SearchGoal getGoal();
    
    /**
     * Sets the objective value associated with this node
     */
    public void setObjectiveVal(double objectiveVal);
    
    /**
     * Gets the objective value associated with this node
     */
    public double getObjectiveVal();

    /**
     * Returns the search action associated with this node
     */
    public SearchAction getAction();
}