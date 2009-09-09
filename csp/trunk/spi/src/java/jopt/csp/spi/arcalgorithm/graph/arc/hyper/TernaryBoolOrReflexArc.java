package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z or Y = X
 */
public class TernaryBoolOrReflexArc extends TernaryBoolArc {
    /**
     * Constraint
     * 
     * @param x         Right side of the equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         X portion of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public TernaryBoolOrReflexArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
    	if (xIsFalse()) setTargetFalse();
    	if (xIsTrue() && yIsFalse()) setTargetTrue();
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
        buf.append("] or ");
        
        if (notY)
            buf.append("!Y[");
        else
            buf.append("Y[");
        buf.append(y);
        buf.append("] = ");
        
        if (notX)
            buf.append("!X[");
        else
            buf.append("X[");
        buf.append(x);
        buf.append("] ");
        
        return buf.toString();
    }
}
