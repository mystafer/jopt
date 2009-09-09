package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumAsinArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumSinArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumAsinArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSinArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;

/**
 * Constraint representing sin(A) = Z
 */
public class SinConstraint extends TwoVarTrigConstraint {
    
    public SinConstraint(NumExpr a, NumExpr z) {
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
        Arc a1 = new GenericNumSinArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

        // asin(Z) = A
        Arc a2 = new GenericNumAsinArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
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
        
        // sin(A) = Z
        Arc a1 = new BinaryNumSinArc(a, z, nodeType, arcType1);

        // asin(Z) = A
        Arc a2 = new BinaryNumAsinArc(z, a, nodeType, arcType2);
        
        return new Arc[] { a1, a2 };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr) {
        return new SinConstraint(aexpr, zexpr);
    }
}