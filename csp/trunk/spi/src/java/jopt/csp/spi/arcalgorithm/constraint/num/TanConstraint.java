package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumAtanArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumTanArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumAtanArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumTanArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;

/**
 * Constraint representing tan(A) = Z
 */
public class TanConstraint extends TwoVarTrigConstraint {
    
    public TanConstraint(NumExpr a, NumExpr z) {
        super(a, z);
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // os(A) = Z
        Arc a1 = new GenericNumTanArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

        // atan(Z) = A
        Arc a2 = new GenericNumAtanArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
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
        
        // tan(A) = Z
        Arc a1 = new BinaryNumTanArc(a, z, nodeType, arcType1);

        // atan(Z) = A
        Arc a2 = new BinaryNumAtanArc(z, a, nodeType, arcType2);
        
        return new Arc[] { a1, a2 };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr) {
        return new TanConstraint(aexpr, zexpr);
    }
}