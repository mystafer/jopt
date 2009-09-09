package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing X && y = Z 
 */
public class BinaryBoolConstSourceAndArc extends BinaryBoolArc {
    
	private boolean y;
	
	/**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if x portion is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolConstSourceAndArc(BooleanNode x, boolean notX, boolean y, BooleanNode z, boolean notZ) {
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
        if (!y) {
            // X[?] && y[F] = Z[F]
        	setTargetFalse();
        }
        
        else {
            // X[T] && y[T] = Z[T]
            if (aIsTrue())
            	setTargetTrue();
            
            // X[F] && y[T] = Z[F]
            else if (aIsFalse())
            	setTargetFalse();
        }
    }
}
