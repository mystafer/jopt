package jopt.csp.spi.search.technique;

import java.util.LinkedList;

import jopt.csp.search.SearchNodeReference;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.search.tree.TreeNode;

/**
 * Implementation of a breadth-first search that loops over the search tree.
 * In this type of search, all first-level nodes are explored, then second-level
 * nodes, etc.  As nodes are explored, their children are added to a FIFO queue
 * nodes to be visited.
 */
public class BreadthFirstSearch implements TreeSearchTechnique {
    // javadoc inherited from SearchTechnique
    public void nextMove(Move move, SearchTree tree, TreeNode currentNode, boolean isRoot) {
        // Queue of open nodes is actually stored in the previous move object, not in this class.
        // In a breadth-first search, open nodes are those whose children have not been enumerated
        // and added to the FIFO queue of nodes to visit.
        LinkedList openNodes = (LinkedList) move.getTechniqueData();
        
        // If this is the first move, initialize the open nodes queue
        if (openNodes == null) {
            openNodes = new LinkedList();
            move.setTechniqueData(openNodes);
        }
        
        // Current node should already be have enumerated any possible children.
        // Now we add all child nodes of current node to FIFO open nodes queue
        int childCount = currentNode.getChildCount();
        for (int i=0; i<childCount; i++)
        	openNodes.addLast(tree.getReferenceForNode(currentNode.getChild(i)));
        
        // return next node in queue to process
        SearchNodeReference nextNode = (openNodes.size()>0) ? (SearchNodeReference) openNodes.removeFirst() : null;
        
        // if no next node, return no moves
        if (nextNode==null) {
        	move.setMovement(NONE);
        }
        
        // next node located, return jump to node reference
        else {
        	move.setMovement(JUMP);
            move.setJumpRef(nextNode);
        }
    }
    
    public boolean equals(Object obj) {
        return obj instanceof BreadthFirstSearch;
    }
    
    // Necessary because we have overridden equals.
    public int hashCode() {
    	return 1;
    }
}
