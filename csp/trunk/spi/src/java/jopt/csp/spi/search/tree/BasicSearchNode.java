package jopt.csp.spi.search.tree;

import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.variable.PropagationFailureException;


/**
 * Basic implementation of search node that is produced by a node generator
 * 
 * @author Chris Johnson
 * @author Nick Coleman
 */
public class BasicSearchNode implements TreeNode {
    /**
     * The node has not been activated, it's search action has not been performed
     * and possible children have not yet been created
     */
    private final static int OPEN           = 0;
    /**
     * A node ends up closed after being activated or reactivated.  Possible child
     * nodes have been created.
     */
    private final static int CLOSED         = 1;
    /**
     * The node has been pruned and has had all children and state
     * information discarded.
     */
    private final static int PRUNED         = 2;
    /**
     * Similar to pruned in that all children and state information has
     * been discarded, but it is possible to reactive this node and
     * recalculate state.
     */
    private final static int DEACTIVATED    = 3;
    /**
     * An open node that has been disconnected from it's parent node--
     * perhaps only temporarily.  It can be reconnected.
     */
    private final static int DISCONNECTED   = 4;
    
	private TreeNode parent;
    // Child number within parent.  First child starts with zero.  Root node = -1.
    private int childNum;
    // Action to be run upon activation and possible reactivation.
    private SearchAction action;
    private int depth;
    private boolean binary;
    
    private LinkedList children;
    private LinkedList path;
    private BinaryPath binaryPath;
    private Object stateData;
    private int nodeState;
    private SearchGoal goal;
    private double objectiveVal;
    
    /**
     * Default constructor used for the creation of root nodes
     * 
     * @param action        Action performed by node when it is activated
     */
    public BasicSearchNode(SearchAction action) {
        this(null, -1, action);
    }
    
	/**
	 * Constructor for attaching a child node to the tree
	 * 
	 * @param parent            the parent of this search node
     * @param childNum          Number of child within parent
     * @param action            Action performed by node when it is activated
	 */
	private BasicSearchNode(TreeNode parent, int childNum, SearchAction action) {
        this.childNum = childNum;
        this.action = action;
        this.parent = parent;
        this.depth = (parent!=null) ? parent.getDepth() + 1 : 0;
        this.nodeState = OPEN;
        this.binary = childNum < 2 && (parent==null || parent.isBinary());
	}
    
    // javadoc inherited
    public SearchAction getAction() {
    	return action;
    }
    
    // inherited from SearchNode
    public int getDepth() {
        return depth;
    }
    
    // javadoc inherited from TreeNode
    public int getChildNumberWithinParent() {
    	return childNum;
    }
    
    // javadoc inherited from TreeNode
	public TreeNode getParent() {
		return parent;
	}
    
    // javadoc inherited from TreeNode
    public List getPath() {
    	if (path==null) {
            path = new LinkedList();
            
            if (parent!=null) {
                path.addAll(parent.getPath());
                path.add(new Integer(childNum));
            }
        }
        
        return path;
    }
    
    // javadoc inherited from TreeNode
    public BitSet getBinaryPath() {
        // compute path if necessary
        if (binaryPath==null) {
            
            // root node, no path exists
            if (parent==null) {
                binaryPath = new BinaryPath(0);
            }
            
            // compute path from parent
            else {
                binaryPath = new BinaryPath(depth);
                binaryPath.or(parent.getBinaryPath());
                
                // check if this is a move to the right
                if (childNum>0) binaryPath.set(depth-1, true);
                else binaryPath.set(depth-1, false);
            }
        }
        
        return binaryPath;
    }
    
    // javadoc inherited from TreeNode
    public SearchTechniqueChange activate(SearchGoal goal) throws PropagationFailureException {
        // only an open node can be activated
        if (nodeState==OPEN) {
            // if a goal is present, store the best known objective 
            // value at this time and update bounds for problem
            if (goal!=null) {
                this.goal = goal;
                this.objectiveVal = goal.bestObjectiveValue();
                goal.updateBoundForOpenNode();
            }

            // expand node to perform any associated actions
            SearchTechniqueChange tc = expandNode();
            nodeState=CLOSED;
            return tc;
        }
        else
            throw new IllegalStateException("only an open node can be activated");
    }
    
    // javadoc inherited from TreeNode
    public void deactivate() {
        // only closed node can be deactivated
        if (nodeState==CLOSED) {
            // calculate path to this node in case it will
            // be needed later
            if (binary)
                getBinaryPath();
            else
                getPath();
            
            nodeState = DEACTIVATED;
            children = null;
            parent = null;
            stateData = null;
        }
        else
            throw new IllegalStateException("only a closed node can be deactivated");
    }

    // javadoc inherited from TreeNode
    public void reactivate(TreeNode parent) {
        //if (nodeState!=DEACTIVATED) throw new IllegalStateException("only a deactivated node can be reactivated");
        try {
            this.parent = parent;
            nodeState=CLOSED;

            // update bounds for problem
            if (goal!=null)
                goal.returnBoundToObjectiveValue(objectiveVal);

            expandNode();
        }
        catch(PropagationFailureException propx) {
            throw new IllegalStateException("reactivating a previously visited search node should not cause a failure");
        }
    }
    
    // javadoc inherited from TreeNode
    public void disconnect() {
        //if (nodeState != OPEN) throw new IllegalStateException("only an open node can be disconnected" + nodeState);
        nodeState = DISCONNECTED;
        parent = null;
    }

    // javadoc inherited from TreNode
    public void reconnect(TreeNode parent) {
        nodeState = OPEN;
        this.parent = parent;
    }
    
    public boolean equals(Object obj) {
        if (obj==null) return false;
    	if (!(obj instanceof BasicSearchNode)) return false;
        
        BasicSearchNode bn = (BasicSearchNode) obj;
        if (bn.getDepth()!= depth) return false;
        if (bn.isBinary() != binary) return false;
        
        if (binary)
            return getBinaryPath().equals(bn.getBinaryPath());
        else
            return getPath().equals(bn.getPath());
    }
    
    // Necessary because we have overridden equals
    public int hashCode() {
    	if (binary)
    		return getBinaryPath().hashCode();
    	else
    		return getPath().hashCode();
    }

    /**
     * Handles activating this node and producing any child nodes.
     */
    private SearchTechniqueChange expandNode() throws PropagationFailureException {
        try {
            // Create null techniqueChange that may be filled and modified by any of several
            // nextActions that happen to be of type SearchTechniqueChange
            SearchTechniqueChange techniqueChange = null;
            
            // process actions contained by this node
            SearchAction nextAction = action;
            while (nextAction!=null) {
                
                // If action is a choice point, create child nodes
                if (nextAction instanceof ChoicePoint) {
                    createChildren((ChoicePoint) nextAction);
                    
                    // Choice points don't have an action, so no need to performAction
                    break;
                }
                
                // If action is a technique change, create a new SearchTechniqueChange
                // that is identical except it doesn't have any action associated with it.
                // This is returned for use by search.
                // If the while loop runs into more than one SearchTechniqueChange
                // the goal, technique and limit may be overwritten by this second change.
                else if (nextAction instanceof SearchTechniqueChange) {
                    // Create object if it doesn't already exist
                	if (techniqueChange == null)
                        techniqueChange = new SearchTechniqueChange();
                	
                	// Copy non-null, non-action values from nextAction to techniqueChange
                	SearchTechniqueChange thisChange = (SearchTechniqueChange) nextAction;
                	if (thisChange.getGoal()!=null) 
                		techniqueChange.setGoal(thisChange.getGoal());
                	if (thisChange.getTechnique()!=null)
                		techniqueChange.setTechnique(thisChange.getTechnique());
                	if (thisChange.getLimit()!=null)
                		techniqueChange.setLimit(thisChange.getLimit());
                }
                
                // execute action and retrieve next action to process
                nextAction = nextAction.performAction();
            }
            
            return techniqueChange;
        }
        catch(PropagationFailureException propx) {
            prune();
            throw propx;
        }

    }
    
    /**
     * Creates the children list of child nodes from a ChoicePoint.
     * 
     * @param choicepoint ChoicePoint to create child nodes from.
     */
    private void createChildren(ChoicePoint choicepoint) {
        children = new LinkedList();
        Iterator childIter = choicepoint.getChildActions().iterator();
        int nextChildNum = 0;
        while (childIter.hasNext()) {
            // create node wrapping action
            SearchAction childAction = (SearchAction) childIter.next();
            TreeNode child = new BasicSearchNode(this, nextChildNum++, childAction);
            children.add(child);
        }
    }
    
    // javadoc inherited from TreeNode
    public void prune() {
        nodeState = PRUNED;
        action = null;
        children = null;
        stateData = null;
    }
    
    // javadoc inherited from TreeNode
    public boolean isClosed() {
    	return nodeState != OPEN && nodeState != DISCONNECTED;
    }
    
    // javadoc inherited from TreeNode
    public boolean isPruned() {
        return nodeState == PRUNED;
    }
    
    // javadoc inherited from TreeNode
    public boolean isDeactivated() {
    	return nodeState == DEACTIVATED;
    }
    
    // javadoc inherited from TreeNode
    public boolean isDisconnected() {
        return nodeState == DEACTIVATED;
    }
    
    // javadoc inherited from TreeNode
    public int getChildCount() {
        if (children==null) return 0;
    	return children.size();
    }

    // javadoc inherited from TreeNode
    public TreeNode getChild(int index) {
        if (children==null) return null;
        return (TreeNode) children.get(index);
    }
    
    // javadoc inherited from TreeNode
    public TreeNode getNextOpenChild() {
        if (children==null) return null;
        
        for (int i=0; i<children.size(); i++) {
            TreeNode n = (TreeNode) children.get(i);
            if (!n.isClosed()) return n;
        }
        
        return null;
    }
    
    // javadoc inherited from TreeNode
    public Object getStateData() {
    	return stateData;
    }
    
    // javadoc inherited from TreeNode
    public void setStateData(Object stateData) {
    	this.stateData = stateData;
    }
    
    // javadoc inherited from TreeNode
    public boolean isBinary() {
    	return binary;
    }
    
    // javadoc inherited from java.lang.Object
    public void setGoal(SearchGoal goal) {
        this.goal = goal;
    }
    
    // javadoc inherited from java.lang.Object
    public SearchGoal getGoal() {
        return goal;
    }
    
    // javadoc inherited from java.lang.Object
    public void setObjectiveVal(double objectiveVal) {
        this.objectiveVal = objectiveVal;
    }
    
    // javadoc inherited from java.lang.Object
    public double getObjectiveVal() {
        return objectiveVal;
    }
    
    // javadoc inherited from java.lang.Object
    public String toString() {
        if (path==null) getPath();

        StringBuffer b = new StringBuffer("{bp:");
        b.append(getBinaryPath());
        b.append(", p:");
        b.append(getPath());
        b.append(", s:");
        b.append(nodeState);
        
        BasicSearchNode t = (BasicSearchNode) this.getParent();
        while (t!=null) {
            b.append(" ->");
            b.append(t.nodeState);
            t = (BasicSearchNode) t.getParent();
        }
        
        b.append("}");
        
        return b.toString();
    }
    
    // utility class to override toString display
    private class BinaryPath extends BitSet {
        private int nbits;
        private String display;
        
    	public BinaryPath(int nbits) {
    		super(depth);
            this.nbits = nbits;
        }
        
        public String toString() {
            // create display string if not already initialized
        	if (display==null) {
        		StringBuffer buf = new StringBuffer("[");
                for (int i=0; i<nbits; i++) {
                    if (i>0) buf.append(", ");
                    
                	if (get(i))
                        buf.append("1");
                    else
                        buf.append("0");
                }
                buf.append("]");
                
                display = buf.toString();
            }
            
            return display;
        }
    }
}
