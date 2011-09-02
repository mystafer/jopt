package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.PostableConstraint;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.PropagationFailureException;

/**
 * Assigns a boolean node to be equal to the result of an constraint
 */
public class GenericConstraint2BoolArc extends GenericArc {
	private PostableConstraint constraint;
    private BooleanNode bool;
    private boolean c2b;
    
    /**
     * Constructor for arc where boolean node is a target of a source constraint
     */
    public GenericConstraint2BoolArc(PostableConstraint constraint, BooleanNode bool) {
        super(constraint.getBooleanSourceNodes(), new Node[]{bool});
        
        this.constraint = constraint;
        this.bool = bool;
        this.c2b = true;
    }

    /**
     * Constructor for arc where constraint is a target of a source boolean node
     */
    public GenericConstraint2BoolArc(BooleanNode bool, PostableConstraint constraint) {
        super(new Node[]{bool}, new Node[]{});
        
        this.constraint = constraint;
        this.bool = bool;
        this.c2b = false;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        // boolean is target of constraint
        if (c2b) {
            if (constraint.isTrue()) 
                bool.setTrue();
            else if (constraint.isFalse()) 
                bool.setFalse();
        }
        
        // constraint is target of boolean
        else {
            if (bool.isTrue()) 
                constraint.postToGraph();
            else if (bool.isFalse()) 
                constraint.getPostableOpposite().postToGraph();
        }
    }

    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
    public void propagateBounds() throws PropagationFailureException {
        propagate();
    }
    public void propagateBounds(Node src) throws PropagationFailureException {
        propagate();
    }
    public void propagateDomain() throws PropagationFailureException {
        propagate();
    }
    public void propagateDomain(Node src) throws PropagationFailureException {
        propagate();
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        if (c2b) {
            StringBuffer buf = new StringBuffer("bool-Z(");
            buf.append(bool);
            buf.append(") eq constraint-A(");
            buf.append(constraint);
            buf.append(")");
            return buf.toString();
        }
        else {
            StringBuffer buf = new StringBuffer("constraint-Z(");
            buf.append(constraint);
            buf.append(") eq bool-A(");
            buf.append(bool);
            buf.append(")");
            return buf.toString();
        }
        
    }
}
