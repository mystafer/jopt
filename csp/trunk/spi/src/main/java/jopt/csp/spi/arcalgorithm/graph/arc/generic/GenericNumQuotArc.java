package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Xi / Yi, Zi < Xj / Yi, Zi > Xj / Yk, etc.
 */
public class GenericNumQuotArc extends GenericNumArc implements NumArc {
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
    public GenericNumQuotArc(Node x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotArc(Number x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotArc(Node x, Number y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
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
    public GenericNumQuotArc(Number x, Number y, Node z, int nodeType, int arcType) {
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
        
        // Calculate quotients of min and max of nodes
        switch(arcType) {
            case GEQ:
                if (isBoundX()&& isInDomainX(zero))
                    setMinZ(zero);
                else if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {}
                else
                    setMinZ(getMinDivideCeil());
                break;

            case GT:
                if (isBoundX()&& isInDomainX(zero)) {
                    NumberMath.next(zero, getPrecisionZ(), min);
                    setMinZ(min);
                }
                else if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {}
                else {
                    NumberMath.next(getMinDivideFloor(), getPrecisionZ(), min);
                    setMinZ(min);
                }
                break;

            case LEQ:
                if (isBoundX()&& isInDomainX(zero))
                    setMaxZ(zero);
                else if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {}
                else
                    setMaxZ(getMaxDivideFloor());
                break;

            case LT:
                if (isBoundX()&& isInDomainX(zero)) {
                    NumberMath.previous(zero, getPrecisionZ(), max);
                    setMaxZ(max);
                }
                else if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {}
                else {
                    NumberMath.previous(getMaxDivideCeil(), getPrecisionZ(), max);
                    setMaxZ(max);
                }
                break;

            case EQ:
                if (isBoundX() && isInDomainX(zero))
                    setRangeZ(zero,zero);
                else if (isInDomainY(zero) || (NumberMath.isPositive(getLargestMaxY()) && NumberMath.isNegative(getSmallestMinY()))) {}
                else
                    setRangeZ(getMinDivideCeil(), getMaxDivideFloor());
                break;
                
            case NEQ:
                if (isBoundX() && isBoundY()) {
                    if (!isInDomainX(zero) || !isInDomainY(zero)) {
                        if (nodeType == NumberMath.INTEGER || nodeType == NumberMath.LONG) {
                            NumberMath.divideFloor(getLargestMinX(), getLargestMinY(), nodeType, v1);
                            removeValueZ(v1);
                        }
                        else {
                            NumberMath.divide(getLargestMinX(), getLargestMinY(), nodeType, v1);
                            removeValueZ(v1);
                        }
                    }
                }
                break;
        }
    }
    
    private Number getMaxDivideCeil() {
        NumberMath.divideCeil(getLargestMinX(), getLargestMinY(), nodeType, v1);
        NumberMath.divideCeil(getSmallestMaxX(), getLargestMinY(), nodeType, v2);
        NumberMath.divideCeil(getLargestMinX(), getSmallestMaxY(), nodeType, v3);
        NumberMath.divideCeil(getSmallestMaxX(), getSmallestMaxY(), nodeType, v4);
        NumberMath.max(v1, v2, v3, v4, max);
        return max;
    }
    
    private Number getMinDivideCeil() {
        NumberMath.divideCeil(getLargestMinX(), getLargestMinY(), nodeType, v1);
        NumberMath.divideCeil(getSmallestMaxX(), getLargestMinY(), nodeType, v2);
        NumberMath.divideCeil(getLargestMinX(), getSmallestMaxY(), nodeType, v3);
        NumberMath.divideCeil(getSmallestMaxX(), getSmallestMaxY(), nodeType, v4);
        NumberMath.min(v1, v2, v3, v4, min);
        return min;
    }
    
    private Number getMaxDivideFloor() {
        NumberMath.divideFloor(getLargestMinX(), getLargestMinY(), nodeType, v1);
        NumberMath.divideFloor(getSmallestMaxX(), getLargestMinY(), nodeType, v2);
        NumberMath.divideFloor(getLargestMinX(), getSmallestMaxY(), nodeType, v3);
        NumberMath.divideFloor(getSmallestMaxX(), getSmallestMaxY(), nodeType, v4);
        NumberMath.max(v1, v2, v3, v4, max);
        return max;
    }
    
    private Number getMinDivideFloor() {
        NumberMath.divideFloor(getLargestMinX(), getLargestMinY(), nodeType, v1);
        NumberMath.divideFloor(getSmallestMaxX(), getLargestMinY(), nodeType, v2);
        NumberMath.divideFloor(getLargestMinX(), getSmallestMaxY(), nodeType, v3);
        NumberMath.divideFloor(getSmallestMaxX(), getSmallestMaxY(), nodeType, v4);
        NumberMath.min(v1, v2, v3, v4, min);
        return min;
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
