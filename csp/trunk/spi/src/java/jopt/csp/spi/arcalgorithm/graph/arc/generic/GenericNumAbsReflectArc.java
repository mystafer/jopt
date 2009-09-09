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
 * Constraint representing |Ai| = Zj and |a| = Zi, etc
 */
public class GenericNumAbsReflectArc extends GenericNumArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   a           A variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumAbsReflectArc(Node a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   a           A constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumAbsReflectArc(Number a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   a           A generic constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumAbsReflectArc(GenericNumConstant a, Node z, int nodeType, int arcType) {
        super(a, (GenericNumConstant) null, z, nodeType, arcType);
    }
    
    protected void propagateCurrentNodes() throws PropagationFailureException {
            switch(arcType) {
                case GEQ:
                setMinZ(minX());
                    break;
    
                case GT:
                NumberMath.next(minX(), getPrecisionZ(), min);
                setMinZ(min);
                    break;
    
                case LEQ:
                setMaxZ(maxX());
                    break;
    
                case LT:
                NumberMath.previous(maxX(), getPrecisionZ(), max);
                setMaxZ(max);
                    break;
    
                case EQ:
                v1.set(getLargestMinX());
                NumberMath.neg(v1, v2);
                NumberMath.min(v1, v2, min);
                NumberMath.max(v1, v2, max);
                setRangeZ(min, max);
                    break;
    
                case NEQ:
                if (isBoundX()) removeValueZ(minX());
        }
    }
    
    private Number minX() {
        return minReflect(getLargestMinX(), getSmallestMaxX());
    }

    private Number maxX() {
        return maxReflect(getLargestMinX(), getSmallestMaxX());
    }

    private Number minReflect(Number minX, Number maxX) {
        NumberMath.neg(maxX, v1);
        NumberMath.min(minX, v1, min);
        return min;
    }

    private Number maxReflect(Number minX, Number maxX) {
        NumberMath.neg(minX, v1);
        NumberMath.max(maxX, v1, max);
        return max;
    }
}
