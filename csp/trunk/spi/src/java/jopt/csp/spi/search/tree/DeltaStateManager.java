package jopt.csp.spi.search.tree;

import jopt.csp.spi.solver.ConstraintStore;

/**
 * State manager that records state of search tree using deltas within
 * choicepoint stack.
 * <p>
 * <b>Design Notes</b><br>
 * The original implementation of this search was based upon a class named
 * <code>SearchNodeLight</code> which only stored the delta of the changes
 * from the parent node that was contained in the choicepoint stack.  Upon
 * returning to this child, the delta could be pushed back onto the stack.
 * This was considered to be a better solution than storing the entire
 * state of the problem, but still had the disadvantage of using a lot
 * of memory when the problem was quit large.  Because of this limitation,
 * much thought was put into creating the {@link RecalculatingStateManager}
 * which uses a minimal amount of memory, but has extra overhead required
 * to perform the same work as done before if we wanted to move to another
 * node. 
 * <p>
 * This type of state management requires that a search tree be crawled
 * from parent to child and child to parent; it does not allow for a jump
 * directly from one node to another. This state manager can only be used with
 * the {@link CrawlingSearchTree}. 
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 * @author Nick Coleman
 * @version $Revision: 1.8 $
 * @see ProblemStateManager
 * @see RecalculatingStateManager
 * @see CrawlingSearchTree
 */
public class DeltaStateManager implements TreeStateManager {
    private ConstraintStore store;
    
    /**
     * Basic constructor
     * 
     * @param store the <code>ConstraintStore</code> associated with this problem
     */
    public DeltaStateManager(ConstraintStore store) {
        this.store = store;
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToClosedNode(TreeNode previous, TreeNode current) {
        store.getChoicePointStack().pushDelta(current.getStateData());
    }
    
    // javadoc inherited from TreeStateManager
    public void ascendedToClosedNode(TreeNode previous, TreeNode current) {
        Object delta = store.getChoicePointStack().popDelta();
    	previous.setStateData(delta);
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToOpenNode(TreeNode previous, TreeNode current) {
        	store.getChoicePointStack().push();
    }
    
    // javadoc inherited from TreeStateManager
    public void jumpedToClosedNode(TreeNode previous, TreeNode current) {
    	throw new UnsupportedOperationException("choice point state manager cannot jump from one node to another");
    }
}
