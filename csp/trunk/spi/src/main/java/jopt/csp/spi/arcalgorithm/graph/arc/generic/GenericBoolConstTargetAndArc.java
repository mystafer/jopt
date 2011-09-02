package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z and X = y
 */
public class GenericBoolConstTargetAndArc extends GenericBoolArc implements NumArc {
    
    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y portion of equation
     * @param z         Z portion
     * @param notZ      True if Z portion of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolConstTargetAndArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
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
    public GenericBoolConstTargetAndArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        if (isAnyYTrue()) {
            // Z[T] && X[T] = y[T]
            if (isAnyXTrue())
            	setTargetTrue();
            
            // Z[T] && X[F] = y[T] - failure!
            else if (isAnyXFalse())
                throw new PropagationFailureException();
        }
        
        // Z[F] && X[T] = y[F]
        else if (isAnyXTrue()) setTargetFalse();
        
        // Z[?] && X[F] = y[F]
        // (Z can be anything if x if false and y is false);
    }
}
