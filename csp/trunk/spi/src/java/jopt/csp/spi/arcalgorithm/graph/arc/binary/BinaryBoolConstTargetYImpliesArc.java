package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing X -> Z = y 
 */
public class BinaryBoolConstTargetYImpliesArc extends BinaryBoolArc {
    
	private boolean y;
	
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if x portion is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Z portion of equation
     * @param notZ      True if z portion is equal to !Z, false if equal to Z
     */
    public BinaryBoolConstTargetYImpliesArc(BooleanNode x, boolean notX, boolean y, BooleanNode z, boolean notZ) {
        super(x, notX, z, notZ);
        this.y=y;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (y) {
            if (aIsTrue()) {
                // X[T] -> Z[T] = y[T]
                setTargetTrue();
            }
            // X[F] -> Z[?] = y[T]
            // when X is false and y is true, nothing is known about Z
        }
        else {
            if (aIsTrue()) {
                // X[T] -> Z[F] = y[F]
                setTargetFalse();
            }
            else if (aIsFalse()) {
                // X[F] -> Z[?] = y[F] - failure!
                throw new PropagationFailureException();
            }
        }
    }
}
