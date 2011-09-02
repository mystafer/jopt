package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing X = Y -> Z
 */
public class GenericBoolImpliesYReflexArc extends GenericBoolArc {
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
    public GenericBoolImpliesYReflexArc(Node x, boolean notX, Node y, boolean notY, Node z, boolean notZ) {
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
    public GenericBoolImpliesYReflexArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
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
    public GenericBoolImpliesYReflexArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         x constant of equation
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public GenericBoolImpliesYReflexArc(boolean x, Node y, boolean notY, Node z, boolean notZ) {
        super(new Boolean(x), y, notY, z, notZ);
    }
    
    /**
     * Constraint
     * 
     * @param x         x constant of equation
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    public GenericBoolImpliesYReflexArc(GenericBooleanConstant x, Node y, boolean notY, Node z, boolean notZ) {
        super(x,y, notY, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        // Truth table for Y -> Z = X
        //
        // X  Y  Z
        // -------
        // T  T  T
        // T  F  ?
        // F  T  F
        // F  F  (invalid)

        if (isAnyXTrue()) {
            if (isAnyYTrue())
                setTargetTrue();
        }
        
        else if (isAnyXFalse()) {
            setTargetFalse();
            if (isAnyYFalse()) 
                throw new PropagationFailureException();
        }
        
    }
}
