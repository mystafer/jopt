package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z <- A,  Z <- ^A, ^Z <- A, etc
 * where if right side of the equation evaluates to true, the
 * left side must evaluate to true. 
 */
public class BinaryBoolEqTwoVarArc extends BinaryBoolArc {
    /**
     * Constraint
     * 
     * @param a         Right side of equation
     * @param notA      True if right side of equation is equal to !A, false if right side is equal to A
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolEqTwoVarArc(BooleanNode a, boolean notA, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        // T -> T
        if (aIsTrue()) setTargetTrue();
        else if (aIsFalse()) setTargetFalse();

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
        
        if (notA)
            buf.append("!A[");
        else
            buf.append("A[");
        buf.append(a);
        buf.append("]");
        
        return buf.toString();
    }
}
