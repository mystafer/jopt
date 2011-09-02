package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = logX(Y), etc.
 */
public class TernaryNumLogArc extends TernaryNumArc {
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
    public TernaryNumLogArc(NumNode x, NumNode y, NumNode z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType, NumOperation.LOGARITHM);
    }

    protected void propagateBounds() throws PropagationFailureException {
            // Determine min & max values
        TrigMath.logMinMax(x.getMin(), x.getMax(), y.getMin(), y.getMax(), z.getPrecision(), min, max);
        
        // Make sure value is a number
        if (min.isNaN() || max.isNaN())
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
                if (x.isBound() && y.isBound())
                    z.removeValue(min);
        }
    }

    protected void propagateEqArcConsistent(NumNode src) throws PropagationFailureException {
        // not necessary for real domains
    }

    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
    	// not necessary for real domains
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" log base X(");
        buf.append(x);
        buf.append(") of Y(");
        buf.append(y);
        buf.append(")");
    }
}
