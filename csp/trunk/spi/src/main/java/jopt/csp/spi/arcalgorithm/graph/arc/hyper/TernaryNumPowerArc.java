package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X^Y, Z < X^Y, Z > X^Y, etc.
 */
public class TernaryNumPowerArc extends TernaryNumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private boolean yreciprocal;

    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   yreciprocal True if power should be raised to reciprocal of Y
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public TernaryNumPowerArc(NumNode x, NumNode y, boolean yreciprocal, NumNode z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType, NumOperation.POWER);
        this.yreciprocal = yreciprocal;
    }

    protected void propagateBounds() throws PropagationFailureException {
            // Determine min & max values
        TrigMath.powerMinMax(z, x, y, yreciprocal, z.getPrecision(), min, max);
    
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
        buf.append(" X(");
        buf.append(x);
        buf.append(") ^ ");

        if (yreciprocal) buf.append("1/");

        buf.append("Y(");
        buf.append(y);
        buf.append(")");
    }
}
