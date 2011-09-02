package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing X = Z -> Y
 */
public class TernaryBoolImpliesXReflexArc extends TernaryBoolArc {
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
    public TernaryBoolImpliesXReflexArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        // Truth table for Z -> Y = X
        //
        // X  Y  Z
        // -------
        // T  T  ?
        // T  F  F
        // F  T  (invalid)
        // F  F  T

        if (xIsTrue()) {
            if (yIsFalse())
                setTargetFalse();
        }
        
        else if (xIsFalse()) {
        	if (yIsFalse()) 
                setTargetTrue();
            else if (yIsTrue()) 
                throw new PropagationFailureException();
        }
        
    }
}
