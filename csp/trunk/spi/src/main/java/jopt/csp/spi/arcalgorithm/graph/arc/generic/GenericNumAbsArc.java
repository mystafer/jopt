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
public class GenericNumAbsArc extends GenericNumArc implements NumArc {
    private MutableNumber minAbs = new MutableNumber();
    private MutableNumber maxAbs = new MutableNumber();
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
    public GenericNumAbsArc(Node a, Node z, int nodeType, int arcType) {
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
    public GenericNumAbsArc(Number a, Node z, int nodeType, int arcType) {
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
    public GenericNumAbsArc(GenericNumConstant a, Node z, int nodeType, int arcType) {
        super(a, (GenericNumConstant) null, z, nodeType, arcType);
    }
    
    protected void propagateCurrentNodes() throws PropagationFailureException {
        Number smallestMaxX = getSmallestMaxX();
        double precision = getPrecisionZ();
        
        // Z can only contain positive values
        if (NumberMath.compareToZero(smallestMaxX, precision) < 0)
            throw new PropagationFailureException();

        switch(arcType) {
            case GEQ:
                setMinZ(minX(precision));
                break;

            case GT:
                NumberMath.next(minX(precision), precision, minAbs);
                setMinZ(minAbs);
                break;

            case LEQ:
                setMaxZ(maxX(smallestMaxX));
                break;

            case LT:
                NumberMath.previous(maxX(smallestMaxX), precision, maxAbs);
                setMaxZ(maxAbs);
                break;

            case EQ:
                setRangeZ(minX(precision), maxX(smallestMaxX));
                break;

            case NEQ:
                if (isBoundX()) removeValueZ(maxX(smallestMaxX));
        }
    }
    
    /**
     * Returns min abs of node A
     */
    private Number minX(double precision) throws PropagationFailureException {
        // Retrieve zero in same number type as a domain
        Number zero = NumberMath.zero(nodeType);
        
        // Determine minimum absolute value of a
        minAbs.set(zero);
        if (!isInDomainX(zero)) {
            Number nh = getNextHigherX(zero);
            Number nl = getNextLowerX(zero);
            NumberMath.abs(nl, minAbs);
            
            if (NumberMath.compareToZero(nh, precision) == 0 && NumberMath.compareToZero(nl, precision) == 0)
                throw new PropagationFailureException();

            NumberMath.min(nh, minAbs, minAbs);
        }
        
        return minAbs;
    }

    /**
     * Returns max abs of node A
     */
    private Number maxX(Number smallestMaxX) {
        NumberMath.abs(getLargestMinX(), v1);
        NumberMath.abs(smallestMaxX, v2);
        NumberMath.max(v1, v2, maxAbs);
        return maxAbs;
    }
}
