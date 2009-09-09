package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z || X = y 
 */
public class BinaryBoolConstTargetOrArc extends BinaryBoolArc {
    
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
    public BinaryBoolConstTargetOrArc(BooleanNode x, boolean notX, boolean y, BooleanNode z, boolean notZ) {
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
        if(!y) {
            // Z[F] || X[F] = y[F]
            setTargetFalse();
        }
        else if (aIsFalse()) {
            // Z[T] || X[F] = y[T]
            setTargetTrue();
        }
        
        // Z[?] || X[T] = y[T]
        // (Z can be anything if x if true and y is true);
    }
}
