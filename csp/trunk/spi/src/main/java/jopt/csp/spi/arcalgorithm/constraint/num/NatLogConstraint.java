package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumExpArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumNatLogArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumExpArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumNatLogArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;

/**
 * Constraint representing NatLog(A) = Z
 */
public class NatLogConstraint extends TwoVarTrigConstraint {
    
    public NatLogConstraint(NumExpr a, NumExpr z) {
        super(a, z);
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // NatLog(A) = Z
        Arc a1 = new GenericNumNatLogArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

        // Exp(Z) = A
        Arc a2 = new GenericNumExpArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
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
        
        // NatLog(A) = Z
        Arc a1 = new BinaryNumNatLogArc(a, z, nodeType, arcType1);

        // Exp(Z) = A
        Arc a2 = new BinaryNumExpArc(z, a, nodeType, arcType2);
        
        return new Arc[] { a1, a2 };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr) {
        return new NatLogConstraint(aexpr, zexpr);
    }
}