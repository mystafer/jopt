package jopt.csp.spi.search.technique;

import jopt.csp.search.SearchNodeReference;
import jopt.csp.search.SearchTechnique;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.search.tree.TreeNode;

/**
 * Implementation of a breadth first search that loops over the search tree
 */
public interface TreeSearchTechnique extends SearchTechnique {
    public final static int NONE    =   0;
    public final static int PARENT  =   2;
    public final static int CHILD   =   3;
    public final static int JUMP    =   4;
    
    /**
     * Mutates current-move variable to return the next move to be
     * performed in search tree
     *  
     * @param move          Move to store movement information in
     * @param tree          Tree that is being iterated over by search
     * @param currentNode   Current node in tree
     * @param isRoot        True if the current node is the root node
     * @return  Next move to be performed by search
     */
    public void nextMove(Move move, SearchTree tree, TreeNode currentNode, boolean isRoot);

    /**
     * Contains movement information used by search to determine next node to
     * activate during iteration over search tree
     */
    public class Move {
        private int movement;
        private int childNum;
        private SearchNodeReference jumpRef;
        private Object techniqueData;
        
        public int getChildNum() {
            return childNum;
        }
        public void setChildNum(int childNum) {
            this.childNum = childNum;
        }
        
        public int getMovement() {
            return movement;
        }
        public void setMovement(int movement) {
            this.movement = movement;
        }
        
        public Object getTechniqueData() {
            return techniqueData;
        }
        public void setTechniqueData(Object techniqueData) {
            this.techniqueData = techniqueData;
        }
        
        public SearchNodeReference getJumpRef() {
            return jumpRef;
        }
        public void setJumpRef(SearchNodeReference jumpRef) {
            this.jumpRef = jumpRef;
        }
    }
}
