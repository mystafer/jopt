package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z or X = y
 */
public class GenericBoolConstTargetOrArc extends GenericBoolArc implements NumArc {
    
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Z portion
     * @param notZ      True if Z portion of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolConstTargetOrArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
        super(x, notX, new Boolean(y), z, notZ);
    }
    
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Z portion
     * @param notZ      True if Z portion of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolConstTargetOrArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        if(isAnyYFalse()) {
            // Z[F] || X[F] = y[F]
            setTargetFalse();
        }
        else if (isAnyXFalse()) {
            // Z[T] || X[F] = y[T]
            setTargetTrue();
        }
        
        // Z[?] || X[T] = y[T]
        // (Z can be anything if x if true and y is true);
    }
}
