package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing A <- Z
 */
public class GenericBoolImpliesReflexTwoVarArc extends GenericBoolArc {
    /**
     * Constraint
     * 
     * @param a         A portion of equation
     * @param notA      True if A portion of equation is equal to !A, false if right side is equal to A
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public GenericBoolImpliesReflexTwoVarArc(Node a, boolean notA, Node z, boolean notZ) {
        super(a, notA, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        if (isAnyXFalse())
        	setTargetFalse();
    }
}
