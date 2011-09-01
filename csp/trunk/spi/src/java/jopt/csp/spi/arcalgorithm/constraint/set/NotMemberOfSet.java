package jopt.csp.spi.arcalgorithm.constraint.set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetNotMemberOfSetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetNotMemberOfSetReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing an expression is not a member of a set
 */
public class NotMemberOfSet extends SetConstraint<Number> {
    @SuppressWarnings("unchecked")
	public NotMemberOfSet(SetVariable<Number> set, NumExpr expr) {
        super(new SetVariable[]{set}, expr);

        if (expr instanceof GenericNumExpr)
            throw new IllegalArgumentException("generic expressions not supported by member-of constraint");
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        Node expr = exprSource.getNode();
        SetNode<Number> set = targets[0].getNode();
        
        return new Arc[]{
            new BinarySetNotMemberOfSetArc(set, (NumNode) expr),
            new BinarySetNotMemberOfSetReflexArc((NumNode) expr, set)
        };
    }
    
    // javadoc inherited from SetConstraint
    protected boolean isViolated() {
    	return false;
    }
    
    // javadoc inherited from AbstractConstraint
    protected AbstractConstraint createOpposite() {
        return null;
    }
}