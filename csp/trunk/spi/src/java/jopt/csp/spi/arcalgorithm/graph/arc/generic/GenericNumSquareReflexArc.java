package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing sqrt(Xi) = Zi, sqrt(Xi) < Z, etc with special square-related behavior
 */
public class GenericNumSquareReflexArc extends GenericNumArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSquareReflexArc(Node x, Node z, int nodeType, int arcType) {
        super(x, (Number) null, z, nodeType, arcType);
    }

    /**
     * Constructor
     */
    public GenericNumSquareReflexArc(Number a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     */
    public GenericNumSquareReflexArc(GenericNumConstant a, Node z, int nodeType, int arcType) {
        super(a, (GenericNumConstant) null, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        double precision = getPrecisionZ();
        Number zero = NumberMath.zero(nodeType);
        Number xmax = getSmallestMaxX();
        Number xmin = getLargestMinX();
        
        // Handle neq arc
        if (arcType == NEQ) {
            if (isBoundX() && NumberMath.compareToZero(xmax, precision) >= 0) {
                NumberMath.sqrtNoInvalid(xmax, nodeType, max);
                NumberMath.neg(max, min);
                removeValueZ(min);
                removeValueZ(max);
            }
            return;
        }
        
        switch(arcType) {
            case GEQ:
                if (NumberMath.compareToZero(xmin, precision) <= 0) {}
                else {
                    NumberMath.sqrtCeil(xmin, nodeType, max);
                    NumberMath.neg(max, min);
                    NumberMath.next(min, precision, min);
                    NumberMath.previous(max, precision, max);
                    removeRangeZ(min, max);
                }
                break;
                
            case GT:
                if (min.doubleValue() < 0) {}
                else if (NumberMath.compareToZero(xmin, getPrecisionZ()) == 0) {
                    removeValueZ(zero);
                }
                else {
                    NumberMath.sqrtFloor(xmin, nodeType, max);
                    NumberMath.neg(max, min);
                    removeRangeZ(min, max);
                }
                break;
                
            case LEQ:
                if (max.doubleValue() < 0) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else if (NumberMath.compareToZero(xmax, getPrecisionZ()) == 0) {
                    setValueZ(zero);
                }
                else {
                    NumberMath.sqrtFloor(xmax, nodeType, max);
                    NumberMath.neg(max, min);
                    setRangeZ(min, max);
                }
                break;
                
            case LT:
                if (NumberMath.compareToZero(xmax, getPrecisionZ()) <= 0) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else {
                    NumberMath.sqrtCeil(xmax, nodeType, max);
                    NumberMath.neg(max, min);
                    NumberMath.next(min, precision, min);
                    NumberMath.previous(max, precision, max);
                    setRangeZ(min, max);
                }
                break;
                
            case EQ:
                if (xmax.doubleValue() < 0) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else if (NumberMath.compareToZero(xmax, getPrecisionZ()) == 0) {
                    setValueZ(zero);
                }
                else if (NumberMath.compareToZero(xmin, getPrecisionZ()) <= 0) {
                    NumberMath.sqrtFloor(xmax, nodeType, max);
                    NumberMath.neg(max, min);
                    setRangeZ(min, max);
                }
                else {
                    NumberMath.sqrtFloor(xmax, nodeType, max);
                    NumberMath.neg(max, min);
                    setRangeZ(min, max);
                    
                    NumberMath.sqrtCeil(xmin, nodeType, max);
                    NumberMath.neg(max, min);
                    NumberMath.next(min, precision, min);
                    NumberMath.previous(max, precision, max);
                }
                break;
        }
    }
}
