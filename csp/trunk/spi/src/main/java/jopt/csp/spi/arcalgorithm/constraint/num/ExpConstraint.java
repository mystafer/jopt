package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumExpArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumNatLogArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumExpArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumNatLogArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;

/**
 * Constraint representing Exp(A) = Z
 */
public class ExpConstraint extends TwoVarTrigConstraint {
    
    public ExpConstraint(NumExpr a, NumExpr z) {
        super(a, z);
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // Exp(A) = Z
        Arc a1 = new GenericNumExpArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

        // NatLog(Z) = A
        Arc a2 = new GenericNumNatLogArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
        return new Arc[] { a1, a2 };
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createStandardArcs() {
        NumNode a = (NumNode) aexpr.getNode();
        NumNode z = (NumNode) zexpr.getNode();
        
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // Exp(A) = Z
        Arc a1 = new BinaryNumExpArc(a, z, nodeType, arcType1);

        // NatLog(Z) = A
        Arc a2 = new BinaryNumNatLogArc(z, a, nodeType, arcType2);
        
        return new Arc[] { a1, a2 };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr) {
        return new ExpConstraint(aexpr, zexpr);
    }
}