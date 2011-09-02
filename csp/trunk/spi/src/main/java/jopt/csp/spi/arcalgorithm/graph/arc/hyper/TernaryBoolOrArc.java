package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X or Y
 */
public class TernaryBoolOrArc extends TernaryBoolArc {
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public TernaryBoolOrArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (xIsTrue()) {
        	setTargetTrue();
        }
        else if (yIsTrue()) {
        	setTargetTrue();
        }
        else if ((yIsFalse())&&xIsFalse()) {
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
        buf.append("] or ");
        
        if (notY)
            buf.append("!Y[");
        else
            buf.append("Y[");
        buf.append(y);
        buf.append("] ");
        
        return buf.toString();
    }
}
