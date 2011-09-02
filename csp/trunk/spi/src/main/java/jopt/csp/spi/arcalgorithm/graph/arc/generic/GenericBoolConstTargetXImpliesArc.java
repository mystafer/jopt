package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z -> X = y
 */
public class GenericBoolConstTargetXImpliesArc extends GenericBoolArc implements NumArc {
    
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Z portion
     * @param notZ      True if Z portion of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolConstTargetXImpliesArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
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
    public GenericBoolConstTargetXImpliesArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        if (isAnyYTrue()) {
            if (isAnyXFalse()) {
                // Z[F] -> X[F] = y[T]
                setTargetFalse();
            }
            // Z[?] -> X[T] = y[T]
            // when X is true and y is true, nothing is known about Z
        }
        else {
            if (isAnyXTrue()) {
                // X[?] -> X[T] = y[F] - failure!
                throw new PropagationFailureException();
            }
            else if (isAnyXFalse()) {
                // X[T] -> X[F] = y[F]
                setTargetTrue();
            }
        }
    }
}
