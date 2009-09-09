package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for generic boolean arcs
 */
public abstract class TernaryBoolArc extends HyperArc {
    protected BooleanNode x;
    protected BooleanNode y;
    protected BooleanNode z;
    
    protected boolean notX;
    protected boolean notY;
    protected boolean notZ;
    
    /**
     * Constructor
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected TernaryBoolArc(BooleanNode x, boolean notX, BooleanNode y, boolean notY, BooleanNode z, boolean notZ) {
        super(new Node[]{x, y}, z);
        
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.notX = notX;
        this.notY = notY;
        this.notZ = notZ;

        // Set source dependency value
        this.sourceDependencies = new int[] {
        	DomainChangeType.DOMAIN,
			DomainChangeType.DOMAIN
        };
    }
    
    /**
     * Returns true if X portion of arc is true with appropriate not flag considered
     */
    protected boolean xIsTrue() {
        if (notX)
            return x.isFalse();
        else
            return x.isTrue();
    }
    
    /**
     * Returns true if X portion of arc is false with appropriate not flag considered
     */
    protected boolean xIsFalse() {
        if (notX)
            return x.isTrue();
        else
            return x.isFalse();
    }
    
    /**
     * Returns true if Y portion of arc is true with appropriate not flag considered
     */
    protected boolean yIsTrue() {
        if (notY)
            return y.isFalse();
        else
            return y.isTrue();
    }
    
    /**
     * Returns true if Y portion of arc is false with appropriate not flag considered
     */
    protected boolean yIsFalse() {
        if (notY)
            return y.isTrue();
        else
            return y.isFalse();
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
