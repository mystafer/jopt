package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing A OR CONST -> Z
 */
public class BinaryBoolConstOrArc extends BinaryBoolArc {
    
	private boolean constVal;
	
	/**
     * Constraint
     * 
     * @param constVal  constBool to OR with a
     * @param a         Right side of equation
     * @param notA      True if right side of equation is equal to !A, false if right side is equal to A
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolConstOrArc(boolean constVal, BooleanNode a, boolean notA, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
        this.constVal=constVal;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        // a:T xor T -> T
        if ((aIsTrue())&&(constVal)) setTargetTrue();
        // a:T xor F -> T
        else if ((aIsTrue())&&(!constVal)) setTargetTrue();
        // a:F xor T -> T        
        else if ((aIsFalse())&&(constVal)) setTargetTrue();
        // a:F xor F -> F        
        else if ((aIsFalse())&&(!constVal)) setTargetFalse();
    }
}
