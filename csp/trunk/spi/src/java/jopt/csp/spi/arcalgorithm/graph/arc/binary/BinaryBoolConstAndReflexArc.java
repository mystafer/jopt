package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z && y = X
 */
public class BinaryBoolConstAndReflexArc extends BinaryBoolArc {
	private boolean constVal;
	
	/**
     * Creates new and reflex constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if x portion of equation is equal to !X, false if equal to X
     * @param y         Y portion of equation
     * @param z         Z portion of equation
     * @param notZ      True if z portion of equation is equal to !Z, false if equal to Z
     */
    public BinaryBoolConstAndReflexArc(BooleanNode x, boolean notX, boolean y, BooleanNode z, boolean notZ) {
        super(x, notX, z, notZ);
        this.constVal = y;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
    	if(aIsTrue()) {
            // Z[T] && y[T] = X[T]
            if (constVal)
            	setTargetTrue();
            
            // Z[!] && y[F] = X[T] - x cannot be true if Y is false
            else
                throw new PropagationFailureException();
        }
        
        // Z[F] && y[T] = X[F]
    	else if(aIsFalse() && constVal) setTargetFalse();
        
        // Z[?] && y[F] = X[F]
        // (z doesn't matter if X is false and Y is false)
    }
}
