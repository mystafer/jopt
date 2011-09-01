package jopt.csp.spi.search.tree;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchNodeReference;


/**
 * Search tree for node generators that need to traverse through each node in
 * the tree to ensure the state of the problem is maintained correctly.
 * <p>
 * <b>Design Notes</b><br>
 * This tree along with the {@link DeltaStateManager} evolved from a class
 * named <code>SearchNodeLight</code> which stored only changes in the problem
 * that occurred when moving from the parent node in a tree to the child node.
 * If you want to know more about how state of the problem is stored and restored
 * as this tree is moved through, see the documentation in {@link DeltaStateManager}.
 * <p>
 * A new state manager was created after the searching technique was refactored
 * to create an optimal usage of memory, but at the cost of computation.  Since
 * this tree guaranteed that movement would always be parent-2-child and 
 * child-2-parent, a state manager named {@link RecalculatingStateManager} was
 * created that could release nodes that were not in a direct path from the
 * current node to the root.  When a previous node needed to be reached, the
 * manager would recompute all the steps needed to reach it.
 * <p>
 * This is the default search tree that is used by the <code>CspSolver</code>.
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 * @author Nick Coleman
 * @version $Revision: 1.14 $
 * @see DeltaStateManager
 * @see RecalculatingStateManager
 */
public class CrawlingSearchTree extends AbstractSearchTree {
    private BitSet moveworkBits;
    
    /**
     * Constructor for a search tree used to manage nodes
     * 
     * @param rootNode  Root node of the tree
     */
    public CrawlingSearchTree(TreeNode rootNode, TreeStateManager stateMgr) {
        super(rootNode, stateMgr);
    }
    
    /**
     * Returns a reference that can be returned to using
     * <code>returnToReference<code>
     */
    public SearchNodeReference getReferenceForNode(TreeNode n) {
        CrawlingNodeRef ref = new CrawlingNodeRef();
        ref.binary = n.isBinary();
        ref.closed = n.isClosed();
        ref.depth = n.getDepth();
        ref.goal = n.getGoal();
        ref.objectiveVal = n.getObjectiveVal();
            
        if (n.isBinary())
            ref.path = n.getBinaryPath();
        else
        	ref.path = n.getPath();
        
        return ref;
    }

    /**
     * Moves to location in tree to which reference refers
     */
    public void returnToReference(SearchNodeReference ref) {
        CrawlingNodeRef cref = (CrawlingNodeRef) ref;
        
        // request move to root
        if (cref.depth==0) {
            while (!currentNode.equals(rootNode))
                moveToParent();
            
            return;
        }

        // use binary crawl for speed if possible
        if (cref.binary && currentNode.isBinary())
        	crawlBinary(cref);
        else
        	crawlNonBinary(cref);
    }
    
    /**
     * Moves to a specific node using non-binary paths from the
     * root to the current and new nodes 
     */
    @SuppressWarnings("unchecked")
	private void crawlNonBinary(CrawlingNodeRef ref) {
        // determine maximum possible common depth that 
        List<Integer> currentPath = currentNode.getPath();
        List<Integer> nPath = null;
        if (ref.path instanceof List)
            nPath = (List<Integer>) ref.path;
        else
            nPath = convertToNonBinary((BitSet) ref.path, ref.depth);
        int commonDepth = Math.min(currentPath.size(), nPath.size());
        
        // determine depth of common parent
        int commonParentDepth = 0;
        for (int i=0; i<commonDepth; i++) {
            Integer currentMove = (Integer) currentPath.get(i);
        	Integer nChildMove = (Integer) nPath.get(i);
            
            // exit when movements are different
            if (!currentMove.equals(nChildMove))
                break;
            
            commonParentDepth++;
        }
        
        // move to common parent depth from common node
        while (currentNode.getDepth()>commonParentDepth) {
            moveToParent();
        }
        
        // move down to requested node
        for (int i=commonParentDepth; i<nPath.size()-1; i++) {
            Integer childNum = (Integer) nPath.get(i);
            TreeNode child = currentNode.getChild(childNum.intValue());
            moveToClosedChild(child);
        }
        
        // check if we are moving to an open or closed node
        Integer childNum = (Integer) nPath.get(nPath.size()-1);
        TreeNode n = currentNode.getChild(childNum.intValue());
        if (ref.closed)
            moveToClosedChild(n);
        else
            moveToOpenChild(n);
    }

    /**
     * Moves to a specific node using non-binary paths from the
     * root to the current and new nodes 
     */
    private void crawlBinary(CrawlingNodeRef ref) {
        BitSet nPath = (BitSet) ref.path;
        
        // request move from root
        if (currentNode.equals(rootNode)) {
            
            // recompute moves to new node
            for (int i=0; i<ref.depth-1; i++) {
                
                // if bit is set, move to right
                if (nPath.get(i))
                    moveToClosedChild(currentNode.getChild(1));
                else
                    moveToClosedChild(currentNode.getChild(0));
            }
        }
        
        // move backwards in list until common parent is located
        // then recompute back to child
        else {
            BitSet currentPath = currentNode.getBinaryPath();
            int maxBits = Math.max(currentNode.getDepth(), ref.depth);
            
            // initialize work bits
            if (moveworkBits==null || moveworkBits.size()<maxBits) 
                moveworkBits = new BitSet(maxBits*2);
            else
                moveworkBits.clear();
            moveworkBits.or(currentPath);
            moveworkBits.xor(nPath);
            
            // determine depth of common parent
            int commonParentDepth = moveworkBits.nextSetBit(0);
            if (commonParentDepth<0) commonParentDepth = currentNode.getDepth()-1;
            
            // move to common parent from current node
            int curDepth = currentNode.getDepth();
            for (int i=commonParentDepth; i<curDepth; i++)
                moveToParent();
            
            // recompute moves to new node
            for (int i=commonParentDepth; i<ref.depth-1; i++) {
                
                // if bit is set, move to right
                if (nPath.get(i))
                    moveToClosedChild(currentNode.getChild(1));
                else
                    moveToClosedChild(currentNode.getChild(0));
            }
        }
        
        // check if we are moving to an open or closed node
        if (ref.depth>0) {
            int finalMove = nPath.get(ref.depth-1) ? 1 : 0;
            if (ref.closed) {
                TreeNode n = currentNode.getChild(finalMove);
                n.setGoal(ref.goal);
                n.setObjectiveVal(ref.objectiveVal);
                moveToClosedChild(n);
            }
            else
                moveToOpenChild(currentNode.getChild(finalMove));
        }
    }
    
    /**
     * Helper function to convert a binary path to a non-binary path
     */
    private List<Integer> convertToNonBinary(BitSet path, int depth) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i=0; i<depth; i++) {
            if (path.get(i))
            	list.addLast(new Integer(1));
            else
                list.addLast(new Integer(0));
        }
    	return list;
    }

    // javadoc inherited from SearchTree
    public SearchTree subtree() {
        return new CrawlingSearchTree(currentNode, stateMgr);
    }

    private class CrawlingNodeRef implements SearchNodeReference {
        public boolean binary;
    	public Object path;
        public boolean closed;
        public int depth;
        public SearchGoal goal;
        public double objectiveVal;
        
        public String toString() {
        	return "{cref: binary(" + binary + "), closed(" + closed + "), depth(" + depth + "), path(" + path + ")}";
        }
    }
}
