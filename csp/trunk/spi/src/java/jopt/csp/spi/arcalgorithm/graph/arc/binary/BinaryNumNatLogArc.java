package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = natural log of X
 */
public class BinaryNumNatLogArc extends BinaryNumArc {
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
    public BinaryNumNatLogArc(NumNode x, NumNode z, int nodeType, int arcType) {
        super(x, null, null, null, z, nodeType, arcType, NumOperation.NAT_LOG);
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        // Calculate min and max values
        TrigMath.natLogMinMax(z, x, z.getPrecision(), min, max);
            
        if (min.isInvalid() || max.isInvalid())
            throw new PropagationFailureException();
        
        switch(arcType) {
            case GEQ:
                z.setMin(min);
                break;

            case GT:
                NumberMath.next(min, z.getPrecision(), min);
                z.setMin(min);
                break;

            case LEQ:
                z.setMax(max);
                break;

            case LT:
                NumberMath.previous(max, z.getPrecision(), max);
                z.setMax(max);
                break;

            case EQ:
                z.setRange(min, max);
                break;
                
            case NEQ:
                if (x.isBound()) z.removeValue(min);
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        // not necessary for real numbers
    }
        
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        // not necessary for real numbers
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" natural log of X(");
        buf.append(x);
        buf.append(") ");
    }
}
