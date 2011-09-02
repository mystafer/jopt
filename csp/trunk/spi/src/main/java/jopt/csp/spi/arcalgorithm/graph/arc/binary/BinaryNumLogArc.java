package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing logx(Y) > Z or logX(y) < Z, etc
 */
public class BinaryNumLogArc extends BinaryNumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   yconst      Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumLogArc(NumNode x, Number yconst, NumNode z, int nodeType, int arcType) {
        super(x, null, null, yconst, z, nodeType, arcType, NumOperation.LOGARITHM);
    }

    /**
     * Constructor
     *
     * @param   xconst      X constant in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumLogArc(Number xconst, NumNode y, NumNode z, int nodeType, int arcType) {
        super(null, xconst, y, null, z, nodeType, arcType, NumOperation.LOGARITHM);
    }

    protected void propagateBounds() throws PropagationFailureException {
        Number xmin = (xconst == null) ? x.getMin() : xconst;
        Number xmax = (xconst == null) ? x.getMax() : xconst;
        Number ymin = (yconst == null) ? y.getMin() : yconst;
        Number ymax = (yconst == null) ? y.getMax() : yconst;

        // Determine min & max values
        TrigMath.logMinMax(xmin, xmax, ymin, ymax, z.getPrecision(), min, max);
        
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
        if (x == null) {
            buf.append(" log base x(");
            buf.append(xconst);
            buf.append(") of ");
        }
        else {
            buf.append(" log base X(");
            buf.append(x);
            buf.append(") of ");
        }

        if (y == null) {
            buf.append("y(");
            buf.append(yconst);
            buf.append(")");
        }
        else {
            buf.append("Y(");
            buf.append(y);
            buf.append(")");
        }
    }
}
