package jopt.csp.spi.search.tree;

import jopt.csp.spi.solver.ConstraintStore;

/**
 * State manager that records the complete state of the problem at every
 * node in the search tree.
 * <p>
 * <b>Design Notes</b><br>
 * The original implementation of search was based upon a class named
 * <code>SearchNodeHeavy</code> which stored the entire state of the problem.
 * This ProblemStateManager is similar in that it stores the entire state of
 * the problem.
 * It was extremely fast to move between different nodes in the search tree,
 * but it used a lot of memory.  The amount of memory used was negligible
 * on small problems, but could be quite expensive in problems that
 * use a lot of variables with a large search tree.
 * <p>
 * Since we had the ability to store data in the choice point stack
 * for rolling back to previous states, it was decided that a lot of memory
 * could be saved if only the deltas (changes in domain) from the parent node
 * to a child node were stored. Based on this idea, another node named the
 * <code>SearchNodeLight</code> was created that only stored the deltas between
 * parent and child nodes. The reincarnation of this state management can be
 * found in the {@link DeltaStateManager}.
 * <p>
 * Another advantage this type of state management offers over the delta
 * management was the ability to jump from any node in the tree to
 * any other node directly which was quite useful in a BFS search.  The
 * delta state management requires the search to crawl from the current
 * node up through each parent and down to the desired child to jump from
 * one node to another. This type of jumping movement is incorporated
 * into the {@link JumpingSearchTree} which uses this state manager
 * object exclusively.  However, this manager can also be used
 * in the {@link CrawlingSearchTree} if desired.
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 * @author Nick Coleman
 * @version $Revision: 1.11 $
 * @see DeltaStateManager
 * @see CrawlingSearchTree
 * @see JumpingSearchTree
 */
public class ProblemStateManager implements TreeStateManager {
    private ConstraintStore store;
    
    /**
     * Basic constructor
     * 
     * @param store the <code>ConstraintStore</code> associated with this problem
     */
    public ProblemStateManager(ConstraintStore store) {
        this.store = store;
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToClosedNode(TreeNode previous, TreeNode current) {
        jump(previous, current, true);
    }
    
    // javadoc inherited from TreeStateManager
    public void ascendedToClosedNode(TreeNode previous, TreeNode current) {
        jump(previous, current, true);
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToOpenNode(TreeNode previous, TreeNode current) {
        jump(previous, current, false);
    }
    
    // javadoc inherited from TreeStateManager
    public void jumpedToClosedNode(TreeNode previous, TreeNode current) {
        jump(previous, current, true);
    }

    
    private void jump(TreeNode previous, TreeNode current, boolean closed) {
        // store complete state of problem within previous node if not already
        // recorded
        if (!previous.isPruned() && previous.getStateData()==null) {
            previous.setStateData(store.getCurrentState());
        }
        
        // if current is closed, state information should exist that needs to be restored
        if (closed) {
            store.restoreState(current.getStateData());
        }
    }
}
