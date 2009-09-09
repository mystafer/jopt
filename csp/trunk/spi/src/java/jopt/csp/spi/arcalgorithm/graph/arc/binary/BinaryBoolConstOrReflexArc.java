package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z -> A OR CONST
 */
public class BinaryBoolConstOrReflexArc extends BinaryBoolArc {
    
	private boolean constVal;
	
	/**
     * Constraint
     * 
     * @param constVal  constBool to OR with a
     * @param a         Left side of equation
     * @param notA      True if right side of equation is equal to !A, false if right side is equal to A
     * @param z         Right side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public BinaryBoolConstOrReflexArc(boolean constVal, BooleanNode a, boolean notA, BooleanNode z, boolean notZ) {
        super(a, notA, z, notZ);
        this.constVal = constVal;
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
    	if((aIsFalse())&&(constVal)) {
    		throw new PropagationFailureException();
    	}
    	else if((aIsFalse())&&(!constVal)) {
    		setTargetFalse();
    	}
    	else if((aIsTrue())&&(!constVal)) {
    		setTargetTrue();
    	}
    	
    }
}
