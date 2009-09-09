package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = A -> b  or Z = a -> B
 */
public class BinaryBoolImpliesThreeVarArc extends BinaryBoolArc {
    private boolean bConst;
    private boolean constVal;
    
    /**
     * Constraint
     * 
     * @param a         A portion of equation
     * @param notA      True if A portion of equation is equal to !A, false if right side is equal to A
     * @param b         B portion of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolImpliesThreeVarArc(BooleanNode a, boolean notA, boolean b, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
        this.bConst = true;
        this.constVal=b;
    }

    /**
     * Constraint
     * 
     * @param a         A portion of equation
     * @param b         B portion of equation
     * @param notB      True if B portion of equation is equal to !B, false if right side is equal to B
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolImpliesThreeVarArc(boolean a, BooleanNode b, boolean notB, BooleanNode z, boolean notZ) {
        super(b, notB, z, notZ);
        this.bConst = false;
        this.constVal=a;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        if (bConst) {
            if (aIsFalse() || constVal) {
            	setTargetTrue();
            }
            else if (aIsTrue() && !constVal) {
            	setTargetFalse();
            }
        }
        
        else {
            if (!constVal || aIsTrue()) {
                setTargetTrue();
            }
            else if (constVal && aIsFalse()) {
                setTargetFalse();
            }
        }
    }
}
