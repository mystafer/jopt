package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Xi * Yi, Zi < Xj * Yi, Zi > Xj * Yk, etc with special division-related behavior.
 * For an explanation of this arc's behavior, check out
 * {@link jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumQuotReflexArc}.
 * @see GenericNumQuotArc
 */
public class GenericNumQuotReflexArc extends GenericNumArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private MutableNumber v3 = new MutableNumber();
    private MutableNumber v4 = new MutableNumber();
    
    private Number smallestMinY;
    private Number smallestMaxY;
    private Number largestMinX;
    private Number largestMaxX;
    private Number smallestMinX;
    private Number smallestMaxX;
    private Number largestMinY;
    private Number largestMaxY;
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumQuotReflexArc(Node x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotReflexArc(Number x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotReflexArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotReflexArc(Node x, Number y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotReflexArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotReflexArc(Number x, Number y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        smallestMinY = null;
        smallestMaxY = null;
        largestMinX = null;
        largestMaxX = null;
        smallestMinX = null;
        smallestMaxX = null;
        largestMinY = null;
        largestMaxY = null;
        
        Number zero = NumberMath.zero(nodeType);
        boolean yContainsZero = isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()));
        
        switch(arcType) {
            case GEQ:
                if (yContainsZero) {
                    if (NumberMath.isPositive(getSmallestMinX())) {
                        removeValueZ(zero);
                    }
                }
                // all y's are either strictly negative or strictly positive
                else if (NumberMath.isNegative(getLargestMaxY())) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.max(v1, v2, v3, v4, max);
                    setMaxZ(max);
                }
                else { // getSmallestMinY().doubleValue() > 0
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.min(v1, v2, v3, v4, min);
                    setMinZ(min);
                }
                break;
                
            case GT:
                if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {
                    if (NumberMath.compareToZero(getSmallestMinX(), getPrecisionZ()) >= 0) {
                        removeValueZ(zero);
                    }
                }
                // all y's are either strictly negative or strictly positive
                else if (getLargestMaxY().doubleValue() < 0) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.max(v1, v2, v3, v4, max);
                    NumberMath.previous(max, getPrecisionZ(), max);
                    setMaxZ(max);
                }
                else { // getSmallestMinY().doubleValue() > 0
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.min(v1, v2, v3, v4, min);
                    NumberMath.next(min, getPrecisionZ(), min);
                    setMinZ(min);
                }
                break;
                
            case LEQ:
                if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {
                    if (getLargestMaxX().doubleValue() < 0) {
                        removeValueZ(zero);
                    }
                }
                // all y's are either strictly negative or strictly positive
                else if (getLargestMaxY().doubleValue() < 0) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.min(v1, v2, v3, v4, min);
                    setMinZ(min);
                }
                else { // getSmallestMinY().doubleValue() > 0
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.max(v1, v2, v3, v4, max);
                    setMaxZ(max);
                }
                break;
                
            case LT:
                if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {
                    if (NumberMath.compareToZero(getLargestMaxX(), getPrecisionZ()) <= 0) {
                        removeValueZ(zero);
                    }
                }
                // all y's are either strictly negative or strictly positive
                else if (getLargestMaxY().doubleValue() < 0) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.min(v1, v2, v3, v4, min);
                    NumberMath.next(min, getPrecisionZ(), min);
                    setMinZ(min);
                }
                else { // getSmallestMinY().doubleValue() > 0
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                    NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                    NumberMath.max(v1, v2, v3, v4, max);
                    NumberMath.previous(max, getPrecisionZ(), max);
                    setMaxZ(max);
                }
                break;
                
            case EQ:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.min(v1, v2, v3, v4, min);
                NumberMath.max(v1, v2, v3, v4, max);
                setRangeZ(min, max);
                break;
                
            case NEQ:
                if (isBoundX() && isBoundY()) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    removeValueZ(v1);
                }
        }
    }

    /**
     * Fetches the largest 'minimum' value from all internal nodes for the current X node
     */
    protected Number getLargestMinX() {
        if (largestMinX==null) largestMinX = super.getLargestMinX();
        return largestMinX;
    }

    /**
     * Fetches the largest 'maximum' value from all internal nodes for the current X node
     */
    protected Number getLargestMaxX() {
        if (largestMaxX==null) largestMaxX = super.getLargestMaxX();
        return largestMaxX;
    }

    /**
     * Fetches the smallest 'minimum' value from all internal nodes for the current X node
     */
    protected Number getSmallestMinX() {
        if (smallestMinX==null) smallestMinX = super.getSmallestMinX();
        return smallestMinX;
    }

    /**
     * Fetches the smallest 'maximum' value from all internal nodes for the current X node
     */
    protected Number getSmallestMaxX() {
        if (smallestMaxX==null) smallestMaxX = super.getSmallestMaxX();
        return smallestMaxX;
    }

    /**
     * Fetches the largest 'minimum' value from all internal nodes for the current Y node
     */
    protected Number getLargestMinY() {
        if (largestMinY==null) largestMinY = super.getLargestMinY();
        return largestMinY;
    }

    /**
     * Fetches the largest 'maximum' value from all internal nodes for the current Y node
     */
    protected Number getLargestMaxY() {
        if (largestMaxY==null) largestMaxY = super.getLargestMaxY();
        return largestMaxY;
    }

    /**
     * Fetches the smallest 'minimum' value from all internal nodes for the current Y node
     */
    protected Number getSmallestMinY() {
        if (smallestMinY==null) smallestMinY = super.getSmallestMinY();
        return smallestMinY;
    }

    /**
     * Fetches the smallest 'maximum' value from all internal nodes for the current Y node
     */
    protected Number getSmallestMaxY() {
        if (smallestMaxY==null) smallestMaxY = super.getSmallestMaxY();
        return smallestMaxY;
    }
}
