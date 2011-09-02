package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.math.BigDecimal;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X^y, Z < x^Y, etc.
 */
public class BinaryNumPowerArc extends BinaryNumArc {
    private boolean yreciprocal;
    private Number yconstRecipMin;
    
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   yconst      Y constant in equation
     * @param   yreciprocal True if power should be raised to reciprocal of Y
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    private BinaryNumPowerArc(NumNode x, Number xconst, NumNode y, Number yconst, 
        boolean yreciprocal, NumNode z, int nodeType, int arcType) 
    {
        super(x, xconst, y, yconst, z, nodeType, arcType, NumOperation.POWER);
        this.yreciprocal = yreciprocal;
        // Calculate constant y reciprocal
        if (yreciprocal && yconst!=null) {
            double yval = yconst.doubleValue();
            
            if (yval == 0) {
            	yconstRecipMin = new Double(Double.NEGATIVE_INFINITY);
            }
            else if (Double.isInfinite(yval)) {
                yconstRecipMin = new Double(0);
            }
            else {
                BigDecimal one = BigDecimal.valueOf(1);
                BigDecimal yconstRecip = one.divide(new BigDecimal(yconst.doubleValue()), 20, BigDecimal.ROUND_DOWN);
                Number yconstRecipUp = yconstRecip.setScale(15, BigDecimal.ROUND_UP);
                Number yconstRecipDn = yconstRecip.setScale(15, BigDecimal.ROUND_DOWN);
                
                NumberMath.min(yconstRecipUp, yconstRecipDn, min);
                yconstRecipMin = min.toConst();
                NumberMath.max(yconstRecipUp, yconstRecipDn, max);
            }
        }
    }
                
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   yconst      Y constant in equation
     * @param   yreciprocal True if power should be raised to reciprocal of Y
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumPowerArc(NumNode x, Number yconst, boolean yreciprocal, NumNode z, int nodeType, int arcType) {
        this(x, null, null, yconst, yreciprocal, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   xconst      X constant in equation
     * @param   y           Y variable in equation
     * @param   yreciprocal True if power should be raised to reciprocal of Y
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumPowerArc(Number xconst, NumNode y, boolean yreciprocal, NumNode z, int nodeType, int arcType) {
        this(null, xconst, y, null, yreciprocal, z, nodeType, arcType);
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        // x is a const
        if (x==null) {
            TrigMath.powerMinMax(z, xconst, y, yreciprocal, z.getPrecision(), min, max);
        }
        
        // if y is const, determine if reciprocal of y should be used
        else if (yreciprocal) {
            TrigMath.powerMinMax(z, x, yconstRecipMin, z.getPrecision(), min, max);
        }
        
        // y is const
        else {
            TrigMath.powerMinMax(z, x, yconst, z.getPrecision(), min, max);
        }
        
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
            buf.append(" x(");
            buf.append(xconst);
            buf.append(") ^ ");
        }
        else {
            buf.append(" X(");
            buf.append(x);
            buf.append(") ^ ");
        }
        
        if (yreciprocal) buf.append("1/");
        
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
