package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumAcosArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumCosArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumAcosArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumCosArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;

/**
 * Constraint representing acos(A) = Z
 */
public class AcosConstraint extends TwoVarTrigConstraint {
    
    public AcosConstraint(NumExpr a, NumExpr z) {
        super(a, z);
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // acos(A) = Z
        Arc a1 = new GenericNumAcosArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

        // cos(Z) = A
        Arc a2 = new GenericNumCosArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
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
        
        // acos(A) = Z
        Arc a1 = new BinaryNumAcosArc(a, z, nodeType, arcType1);

        // cos(Z) = A
        Arc a2 = new BinaryNumCosArc(z, a, nodeType, arcType2);
        
        return new Arc[] { a1, a2 };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr) {
        return new AcosConstraint(aexpr, zexpr);
    }
}