package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = cos(Xi)
 */
public class GenericNumCosArc extends GenericNumTrigArc {
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
    public GenericNumCosArc(Node x, Node z, int nodeType, int arcType) {
        super(x, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumCosArc(Number x, Node z, int nodeType, int arcType) {
        super(x, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumCosArc(GenericNumConstant x, Node z, int nodeType, int arcType) {
        super(x, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        // Handle NEQ operation
        if (arcType==NEQ) throw new PropagationFailureException();

        double precision = getPrecisionZ();
        
        // Calculate min and max values
        if (currentXConst != null)
            TrigMath.cosMinMax(currentNz, currentXConst.doubleValue(), precision, min, max);
        else
            TrigMath.cosMinMax(currentNz, currentNx, precision, min, max);
        
        if (min.isInvalid() || max.isInvalid())
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
