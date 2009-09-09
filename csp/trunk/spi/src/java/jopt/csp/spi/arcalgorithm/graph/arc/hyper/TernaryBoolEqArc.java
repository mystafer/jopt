package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = (X == Y)
 */
public class TernaryBoolEqArc extends TernaryBoolArc {
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public TernaryBoolEqArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (xIsFalse() && yIsFalse()) {
            // Z[T] = (X[F] == Y[F])
        	setTargetTrue();
        }
        else if (xIsTrue() && yIsTrue()) {
            // Z[T] = (X[T] == Y[T])
        	setTargetTrue();
        }
        else if (xIsFalse() && yIsTrue()) {
            // Z[F] = (X[F] == Y[T])
            setTargetFalse();
        }
        else if (xIsTrue() && yIsFalse()) {
            // Z[F] = (X[T] == Y[F])
            setTargetFalse();
        }
    }

    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (notZ)
            buf.append("!Z[");
        else
            buf.append("Z[");
        buf.append(z);
        buf.append("] = ");
        
        if (notX)
            buf.append("!X[");
        else
            buf.append("X[");
        buf.append(x);
        buf.append("] == ");
        
        if (notY)
            buf.append("!Y[");
        else
            buf.append("Y[");
        buf.append(y);
        buf.append("] ");
        
        return buf.toString();
    }
}
