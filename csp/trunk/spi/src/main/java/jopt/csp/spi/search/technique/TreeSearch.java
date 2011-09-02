package jopt.csp.spi.search.technique;

import java.util.LinkedList;

import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNodeReference;
import jopt.csp.spi.search.tree.BasicSearchNode;
import jopt.csp.spi.search.tree.CrawlingSearchTree;
import jopt.csp.spi.search.tree.RecalculatingStateManager;
import jopt.csp.spi.search.tree.SearchTechniqueChange;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.search.tree.TreeNode;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;

/**
 * Implements a search that iterates over a tree of search nodes based on
 * a given technique.  The default technique used is Depth First.
 */
public class TreeSearch implements Search {
	protected SearchTree tree;
    private TreeSearchTechnique technique;
    private TreeSearchTechnique.Move move;
	private SearchGoal goal;
	private SearchLimit limit;
	private boolean limitInitialized;
	private boolean continuallyImprove;
    private boolean allSolutionsFound;
	private int nextGoalSolution;
    
    /**
     * Queue of techniques used higher in the tree that are currently suspended.
     * When navigation of the tree  
     */
    private LinkedList<PreviousTechniqueInfo> techniqueQueue;
    
    /**
     * Depth in the tree at which current search technique was engaged
     */ 
    private int currentTechniqueRootDepth;
    
    /**
     * Constructor for initializing search
     * 
     * @param tree          Tree for search to operate upon
     * @param technique     Technique used by search to iterate over tree
     */
    public TreeSearch(SearchTree tree, TreeSearchTechnique technique) {
        this.tree = tree;
        this.continuallyImprove = true;
        this.allSolutionsFound = false;
        this.nextGoalSolution = 0;
        this.techniqueQueue = new LinkedList<PreviousTechniqueInfo>();
        
        if (technique == null)
            this.technique = new DepthFirstSearch();
        else
            this.technique = technique;
    }
    
    /**
     * Constructor for initializing search with default depth first search
     * 
     * @param tree          Tree for search to operate upon
     */
    public TreeSearch(SearchTree tree) {
        this(tree, null);
    }
    
    /**
     * Creates a new depth first search object that uses a crawling search
     * tree with a recalculating state manager
     * 
     * @param store         Constraint store search is performed upon
     * @param node          Top node of a search tree
     * @param technique     Technique used by search to iterate over tree
     */
    public TreeSearch(ConstraintStore store, TreeNode node, TreeSearchTechnique technique) {
        this(new CrawlingSearchTree(node, new RecalculatingStateManager(store)), technique);
    }
    
    /**
     * Creates a new depth first search object that uses a crawling search
     * tree with a recalculating state manager
     * 
     * @param store         Constraint store search is performed upon
     * @param node          Top node of a search tree
     */
    public TreeSearch(ConstraintStore store, TreeNode node) {
        this(store, node, null);
    }
    
    /**
     * Creates a new depth first search object that uses a crawling search
     * tree with a recalculating state manager
     * 
     * @param store         Constraint store search is performed upon
     * @param action        Top action for tree
     * @param technique     Technique used by search to iterate over tree
     */
    public TreeSearch(ConstraintStore store, SearchAction action, TreeSearchTechnique technique) {
        this(store, new BasicSearchNode(action), technique);
    }
    
    /**
     * Creates a new depth first search object that uses a crawling search
     * tree with a recalculating state manager
     * 
     * @param store         Constraint store search is performed upon
     * @param action        Top action for tree
     */
    public TreeSearch(ConstraintStore store, SearchAction action) {
        this(store, action, null);
    }
    
    // javadoc inherited from Search
    public final void setGoal(SearchGoal goal) {
        this.goal = goal;
    }
    
    // javadoc inherited from Search
    public final void setContinuallyImprove(boolean improve) {
        this.continuallyImprove = improve;
    }
    
    // javadoc inherited from Search
    public final boolean nextSolution() {
        boolean solutionFound = false;
        
        // no goal specified or continual improvement has been requested, 
        // simply return the next solution located
        if (goal==null || continuallyImprove) {
            return findNextLeafNodeSatisfyingGoal();
        }
        
        // locate next solution for goal
        else {
            // verify that all solutions have been found
            if(!this.allSolutionsFound) {
                // locate all solutions
                while (findNextLeafNodeSatisfyingGoal()) {};
                
                // all solutions are now recorded
                this.allSolutionsFound = true;
            }
            
            // move to the next solution in goal
            if (nextGoalSolution < goal.getSolutionReferenceCount()) {
                SearchNodeReference nodeRef = goal.getSolutionReference(nextGoalSolution++);
//System.out.println("--- return to solution ---");
//System.out.println("solution " + nextGoalSolution + " of " + goal.getSolutionReferenceCount());
//System.out.println(nodeRef);
                tree.returnToReference(nodeRef);
                solutionFound = true;
            }
        }
        
        return solutionFound;
    }
    
    /**
     * Searches for a leaf node that satisfies the goal.  Returns true if
     * able to locate and activate such a node, false if no such node
     * can be found.
     */
    private boolean findNextLeafNodeSatisfyingGoal() {
    	// move to next open node
        TreeNode currentNode = moveToOpenNode();
        
        // initialize search limit if necessary
        if (limit!=null && !limitInitialized) {
        	limit.init(currentNode);
        	limitInitialized = true;
        }
        
        // loop until no more open nodes exist or search limit fails
        while (currentNode != null && (limit==null || limit.isOkToContinue(currentNode))) {
            // attempt to activate open node
            if (activateNode(currentNode)) {
                
            	// determine if current node is a leaf node
                // and an acceptable solution
                if (currentNode.getChildCount()==0 && isSolutionAcceptableToGoal(currentNode))
                    return true;
            }
            
            // move to next node and continue search
            currentNode = moveToOpenNode();
            
            // initialize search limit if necessary
            if (limit!=null && !limitInitialized) {
            	limit.init(currentNode);
            	limitInitialized = true;
            }
        }
        
        return false;
    }
    
    /**
     * Activates the current node and prunes it if activation fails
     * 
     * @param node  Node to activate
     * @return True if node was activated successfully
     */
    private boolean activateNode(TreeNode node) {
        boolean failure = false;
    
        // Right now this is only here as a fast-escape mechanism
        // for the FirstSolutionGoal to quickly prune the remainder
        // of the search tree and return.  Perhaps this could be
        // done better.
        if (goal!=null && !goal.isOkToActivate(node)) {
            node.prune();
            failure = true;
        }
        
        // current node is valid for goal
        else {
            try {
                // activate node and determine if a search technique change should be performed
                SearchTechniqueChange techniqueChange = node.activate(goal);
                if (techniqueChange!=null) {
                    
                    // push information about current technique and goal and 
                    PreviousTechniqueInfo prevInfo = new PreviousTechniqueInfo();
                    prevInfo.changeRootRef = tree.getReferenceForNode(node);
                    prevInfo.technique = technique;
                    prevInfo.goal = goal;
                    prevInfo.techniqueRootDepth = currentTechniqueRootDepth;
                    prevInfo.limit = limit;
                    techniqueQueue.addFirst(prevInfo);
                    
                    // change to new settings for node
                    if (techniqueChange.getTechnique()!=null)
                    	this.technique = (TreeSearchTechnique) techniqueChange.getTechnique();
                    
                    if (techniqueChange.getGoal()!=null)
                    	this.goal = techniqueChange.getGoal();
                    
                    if (techniqueChange.getLimit()!=null) {
                    	this.limit = (SearchLimit) techniqueChange.getLimit().clone();
                    	this.limitInitialized = false;
                    }
                    
                    this.currentTechniqueRootDepth = node.getDepth();
                }
            }
            catch (PropagationFailureException propx) {
                node.prune();
                failure = true;
            }
        }
        
        return !failure;
    }
    
    /**
     * Locates next open node to activate
     */
    private TreeNode moveToOpenNode() {
        TreeNode currentNode = tree.getCurrentNode();
        
        // loop until open node is located
        while (currentNode.isClosed()) {
            // Create move object if it doesn't already exist
            if (move==null) move = new TreeSearchTechnique.Move();
            
            // Determine next move to perform. To save memory, nextMove mutates
            // the passed in move object instead of creating a new one.
            technique.nextMove(move, tree, currentNode, currentNode.getDepth() == currentTechniqueRootDepth);
            
            switch(move.getMovement()) {
                // move to a parent node
                case TreeSearchTechnique.PARENT:
                    currentNode = tree.moveToParent();
                    break;
                    
                // move to a child node
                case TreeSearchTechnique.CHILD:
                    currentNode = tree.moveToChild(move.getChildNum());
                    break;
                    
                // jump to a node reference
                case TreeSearchTechnique.JUMP:
                    tree.returnToReference(move.getJumpRef());
                    currentNode = tree.getCurrentNode(); 
                    break;
                    
                // If none of the above, all nodes starting from the root of this
                // technique have been explored. Check if previous technique is stored
                // and (if so) resume exploring at a higher level with previous technique.
                default:
                    // no suspending technique changes to pop
                    if (techniqueQueue.size()==0) {
                    	return null;
                    }
                    
                    // return to suspended search operation using previous search technique
                    else {
                        // reset goal and technique
                        PreviousTechniqueInfo prevInfo = (PreviousTechniqueInfo) techniqueQueue.removeFirst();
                        this.technique = (TreeSearchTechnique) prevInfo.technique;
                        this.goal = prevInfo.goal;
                        this.currentTechniqueRootDepth = prevInfo.techniqueRootDepth;
                        this.limit = prevInfo.limit;
                        this.limitInitialized = true;
                        
                        // return to node where change took place
                        tree.returnToReference(prevInfo.changeRootRef);
                        currentNode = tree.getCurrentNode(); 
                    }
            }
        }
        
        return currentNode;
    }
    
    /**
     * Informs the goal of a possible solution and returns true if the goal says
     * that the solution is acceptable.  If goal is null, returns true.
     * 
     * @param node  Node to activate
     */
    private boolean isSolutionAcceptableToGoal(TreeNode node) {
        boolean solutionAcceptable;
        
        // goal exists for evaluating solution
        if (goal!=null) {
            SearchNodeReference ref = tree.getReferenceForNode(tree.getCurrentNode());
            solutionAcceptable = goal.solutionFound(ref);
        }
        else {
            // no goal specified -> solution is acceptable
            solutionAcceptable = true;
        }
        
        return solutionAcceptable;
    }
    
    /**
     * Class for holding information about a technique change for use
     * when returning to a previous search
     */
    private static class PreviousTechniqueInfo {
        public SearchNodeReference changeRootRef;
    	public TreeSearchTechnique technique;
        public SearchGoal goal;
        public int techniqueRootDepth;
        public SearchLimit limit;
    }
}
