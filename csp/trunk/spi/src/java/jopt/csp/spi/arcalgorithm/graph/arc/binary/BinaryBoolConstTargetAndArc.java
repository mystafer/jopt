package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z && X = y 
 */
public class BinaryBoolConstTargetAndArc extends BinaryBoolArc {
    
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
    public BinaryBoolConstTargetAndArc(BooleanNode x, boolean notX, boolean y, BooleanNode z, boolean notZ) {
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
            // Z[T] && X[T] = y[T]
            if (aIsTrue())
            	setTargetTrue();
            
            // Z[T] && X[F] = y[T] - failure!
            else if (aIsFalse())
                throw new PropagationFailureException();
        }
        
        // Z[F] && X[T] = y[F]
        else if (aIsTrue()) setTargetFalse();
        
        // Z[?] && X[F] = y[F]
        // (Z can be anything if x if false and y is false);
    }
}
