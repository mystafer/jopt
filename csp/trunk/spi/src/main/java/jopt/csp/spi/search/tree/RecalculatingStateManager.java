package jopt.csp.spi.search.tree;

import jopt.csp.spi.solver.ConstraintStore;

/**
 * State manager that recalculates state of search tree as it is traversed
 * so that no unnecessary information or nodes are maintained.
 * <p>
 * <b>Design Notes</b><br>
 * This method was created one night as Nick bolted straight up from a sound
 * sleep and pronounced, "Eureka! I have the answer".
 * <p>
 * Ok, so it wasn't quite that dramatic, but it was a sudden answer that came
 * after much debate about how the state of a search tree could utilize an
 * optimal amount of memory. It is based on the assumption that any given
 * search node will produce the same children in the same way no
 * matter how many times the parent node is activated / recalculated.
 * <p>
 * This state manager takes advantage of this situation by creating references
 * to any node based on the path followed to reach the child from the root
 * of the tree. To reach a previously closed node, this manager simply
 * reactivates each child node as it is made current in the tree so that
 * the same child nodes will be produced as before.
 * <p>
 * This state manager can only be used with the {@link CrawlingSearchTree} and
 * is the default state manager used by the <code>CspSolver</code>.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.14 $
 * @see ProblemStateManager
 * @see DeltaStateManager
 * @see CrawlingSearchTree
 */
public class RecalculatingStateManager implements TreeStateManager {
    private ConstraintStore store;
    
    /**
     * Basic constructor
     * 
     * @param store the <code>ConstraintStore</code> associated with this problem
     */
    public RecalculatingStateManager(ConstraintStore store) {
        this.store = store;
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToClosedNode(TreeNode previous, TreeNode current) {
//System.out.println("descend to closed - p[" + previous + "]  c[" + current + "]");            
//System.out.println("goal before: " + previous.getGoal());
        store.getChoicePointStack().push();
        current.reactivate(previous);
//System.out.println("Reactivated: " + current);            
//System.out.println("goal after: " + current.getGoal());
    }
    
    // javadoc inherited from TreeStateManager
    public void ascendedToClosedNode(TreeNode previous, TreeNode current) {
//System.out.println("ascend to closed - p[" + previous + "]  c[" + current + "]");            
//System.out.println("goal before: " + previous.getGoal());
        store.getChoicePointStack().pop();
        
        // deactivate node to prune child nodes but allow tree to
        // travel back to this node again later
        if (!previous.isPruned()) {
            // disconnect child nodes
            for (int i=0; i<previous.getChildCount(); i++)
                previous.getChild(i).disconnect();
            
        	previous.deactivate();
//System.out.println("Deactivated: " + previous);            
        }
//System.out.println("goal after: " + current.getGoal());
    }
    
    // javadoc inherited from TreeStateManager
    public void descendedToOpenNode(TreeNode previous, TreeNode current) {
//System.out.println("descend to open - p[" + previous + "]  c[" + current + "]");            
//System.out.println("goal before: " + previous.getGoal());
    	store.getChoicePointStack().push();
        
        // reconnect current node to parent (just it case it was earlier disconnected)
        current.reconnect(previous);
//System.out.println("goal after: " + current.getGoal());
    }
    
    // javadoc inherited from TreeStateManager
    public void jumpedToClosedNode(TreeNode previous, TreeNode current) {
    	throw new UnsupportedOperationException("choice point state manager cannot jump from one node to another");
    }
}
