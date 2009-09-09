package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for generic boolean arcs
 */
public abstract class BinaryBoolArc extends BinaryArc {
    protected BooleanNode a;
    protected BooleanNode z;
    
    protected boolean notA;
    protected boolean notZ;
    
    /**
     * Constructor
     * 
     * @param a         Right side of equation
     * @param notA      True if right side of equation is equal to !A, false if right side is equal to A
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected BinaryBoolArc(BooleanNode a, boolean notA, BooleanNode z, boolean notZ) {
        super(a, z);
        
        this.a = a;
        this.z = z;
        
        this.notA = notA;
        this.notZ = notZ;

        // Set source dependency value
        this.sourceDependency = DomainChangeType.DOMAIN;
    }

    /**
     * Returns true if A portion of arc is true with appropriate not flag considered
     */
    protected boolean aIsTrue() {
        if (notA)
            return a.isFalse();
        else
            return a.isTrue();
    }
    
    /**
     * Returns true if A portion of arc is false with appropriate not flag considered
     */
    protected boolean aIsFalse() {
        if (notA)
            return a.isTrue();
        else
            return a.isFalse();
    }
    
    /**
     * Sets Z portion of arc to true
     */
    protected void setTargetTrue() throws PropagationFailureException {
        if (notZ)
            z.setFalse();
        else
            z.setTrue();
    }
    
    /**
     * Returns Y portion of arc with appropriate not flag considered
     */
    protected void setTargetFalse() throws PropagationFailureException {
        if (notZ)
            z.setTrue();
        else
            z.setFalse();
    }
    
    public void propagate(Node src) throws PropagationFailureException {
    	propagate();
    }
}
