package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X xor Y
 */
public class TernaryBoolXorArc extends TernaryBoolArc {
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
    public TernaryBoolXorArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
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
            // T xor T = F
        	if (yIsTrue())
                setTargetFalse();
            
            // T xor F = T
            else if (yIsFalse())
                setTargetTrue();
        }
        
        else if (xIsFalse()) {
            // F xor T = T 
            if (yIsTrue())
                setTargetTrue();
            
            // F xor F = F
            else if (yIsFalse())
                setTargetFalse();
        }
    }
}
