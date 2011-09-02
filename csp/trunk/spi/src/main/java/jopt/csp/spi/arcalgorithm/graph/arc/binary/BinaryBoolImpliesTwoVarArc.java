package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z <- A
 */
public class BinaryBoolImpliesTwoVarArc extends BinaryBoolArc {
    /**
     * Constraint
     * 
     * @param a         Right of equation
     * @param notA      True if A portion of equation is equal to !A, false if right side is equal to A
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolImpliesTwoVarArc(BooleanNode a, boolean notA, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (aIsTrue())
            setTargetTrue();
    }
}
