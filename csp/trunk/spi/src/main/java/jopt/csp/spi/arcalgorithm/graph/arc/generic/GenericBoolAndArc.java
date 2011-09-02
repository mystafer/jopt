package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X and Y
 */
public class GenericBoolAndArc extends GenericBoolArc implements NumArc {
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
    public GenericBoolAndArc(Node x, boolean notX, Node y, boolean notY, Node z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         y constant of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public GenericBoolAndArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
        super(x, notX, new Boolean(y), z, notZ);
    }
    
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         y constant of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public GenericBoolAndArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }


    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        // Y does not matter at this point
        if (isAnyXFalse()) {
            // F and T = F
            // F and F = F
            setTargetFalse();
        }
        
        // X does not matter at this point
        else if (isAnyYFalse()) {
            // T and F = F
            // F and F = F
            setTargetFalse();
        }
        
        // T and T = T
        else if (isAnyXTrue() && isAnyYTrue()) {
            setTargetTrue();
        }
    }
}
