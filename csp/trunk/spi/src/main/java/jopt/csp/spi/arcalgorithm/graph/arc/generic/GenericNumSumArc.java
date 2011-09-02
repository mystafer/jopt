/*
 * GenericNumSumArc.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Xi + Yi, Zi < Xj + Yi, Zi > Xj + Yk, etc.
 */
public class GenericNumSumArc extends GenericNumArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(Node x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(Number x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }    
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(Node x, Number y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
        
    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSumArc(Number x, Number y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
            switch(arcType) {
                case GEQ:
                NumberMath.addNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, min);
                setMinZ(min);
                    break;
                    
                case GT:
                NumberMath.addNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, min);
                NumberMath.next(min, getPrecisionZ(), min);
                setMinZ(min);
                    break;
          
                case LEQ:
                NumberMath.addNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, max);
                setMaxZ(max);
                    break;
                    
                case LT:
                NumberMath.addNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, max);
                NumberMath.previous(max, getPrecisionZ(), max);
                setMaxZ(max);
                    break;
           
                case EQ:
                NumberMath.addNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, min);
                NumberMath.addNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, max);
                setRangeZ(min, max);
                    break;
                    
                case NEQ:
                if (isBoundX() && isBoundY()) {
                    NumberMath.addNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, min);
                    removeValueZ(min);
        }
        }
    }
}
