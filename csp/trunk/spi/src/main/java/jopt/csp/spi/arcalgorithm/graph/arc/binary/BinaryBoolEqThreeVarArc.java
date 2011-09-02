package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = (A == b)
 */
public class BinaryBoolEqThreeVarArc extends BinaryBoolArc {
    private boolean constVal;
    
    /**
     * Constraint
     * 
     * @param a         A portion of equation
     * @param notA      True if A portion of equation is equal to !A, false if equal to A
     * @param b         B constant of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public BinaryBoolEqThreeVarArc(BooleanNode a, boolean notA, boolean b, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
        this.constVal=b;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (constVal) {
            if (aIsTrue()) {
                // Z[T] = (A[T] == b[T])
                setTargetTrue();
            }
            else if (aIsFalse()) {
                // Z[F] = (A[F] == b[T])
                setTargetFalse();
            }
        }
        else {
            if (aIsTrue()) {
                // Z[F] = (A[T] == b[F])
                setTargetFalse();
            }
            else if (aIsFalse()) {
                // Z[T] = (A[F] == b[F])
                setTargetTrue();
            }
        }
    }
}
