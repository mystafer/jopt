package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = logX(y),  Z = logX(Y), etc.
 */
public class GenericNumLogArc extends GenericNumTrigArc {
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
    public GenericNumLogArc(Node x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumLogArc(Number x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumLogArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) {
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
    public GenericNumLogArc(Node x, Number y, Node z, int nodeType, int arcType) {
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
    public GenericNumLogArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        Number xmin = getSmallestMinX();
        Number xmax = getSmallestMaxX();
        Number ymin = getSmallestMinY();
        Number ymax = getSmallestMaxX();

        double precision = getPrecisionZ();
        
        // Determine min & max values
        TrigMath.logMinMax(xmin, xmax, ymin, ymax, precision, min, max);

        // Make sure value is a number
        if (min.isNaN() || max.isNaN())
            throw new PropagationFailureException();

        switch(arcType) {
            case GEQ:
                setMinZ(min);
                break;
    
            case GT:
                NumberMath.next(min, precision, min);
                setMinZ(min);
                break;
    
            case LEQ:
                setMaxZ(max);
                break;
    
            case LT:
                NumberMath.previous(max, precision, max);
                setMaxZ(max);
                break;
    
            case EQ:
                setRangeZ(min, max);
                break;
        }
    }
}
