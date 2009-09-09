package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Xi^2, Zi < Xj^2, Zi > Xj^2, etc.
 */
public class GenericNumSquareArc extends GenericNumArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumSquareArc(Node x, Node z, int nodeType, int arcType) {
        super(x, (Number) null, z, nodeType, arcType);
    }

    /**
     * Constructor
     */
    public GenericNumSquareArc(Number a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     */
    public GenericNumSquareArc(GenericNumConstant a, Node z, int nodeType, int arcType) {
        super(a, (GenericNumConstant) null, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        NumberMath.squareNoInvalid(getLargestMinX(), nodeType, v1);
        NumberMath.squareNoInvalid(getSmallestMaxX(), nodeType, v2);
        NumberMath.min(v1, v2, min);
        NumberMath.max(v1, v2, max);
        
        switch(arcType) {
            case GEQ:
                setMinZ(min);
                break;

            case GT:
                NumberMath.next(min, getPrecisionZ(), min);
                setMinZ(min);
                break;

            case LEQ:
                setMaxZ(max);
                break;

            case LT:
                NumberMath.previous(max, getPrecisionZ(), max);
                setMaxZ(max);
                break;

            case EQ:
                setRangeZ(min, max);
                break;

            case NEQ:
                if (isBoundX()) removeValueZ(min);
        }
    }
}
