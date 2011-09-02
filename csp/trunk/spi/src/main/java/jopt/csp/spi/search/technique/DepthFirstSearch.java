package jopt.csp.spi.search.technique;

import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.search.tree.TreeNode;

/**
 * Implementation of a depth-first search that loops over the search tree.  This
 * technique involves starting at the root node and exploring as far down individual
 * branches as possible before backtracking.  It works by opening the first child node
 * it finds at each search node and going as far down as possible before going back up
 * to find other un-explored paths.
 */
public class DepthFirstSearch implements TreeSearchTechnique {
    // javadoc inherited from SearchTechnique
    public void nextMove(Move move, SearchTree tree, TreeNode currentNode, boolean isRoot) {
        // retrieve next open child to move to
        TreeNode childNode = currentNode.getNextOpenChild();
        
        // if no more nodes, move up in tree
        if (childNode==null) {
        	// no more moves if at root
            if (isRoot)
                move.setMovement(NONE);
            else
                move.setMovement(PARENT);
        }
        
        // move to child
        else {
        	move.setMovement(CHILD);
            move.setChildNum(childNode.getChildNumberWithinParent());
        }
    }
    
    public boolean equals(Object obj) {
    	return obj instanceof DepthFirstSearch;
    }
    
    // Necessary because we have overridden equals.
    public int hashCode() {
    	return 1;
    }
}
