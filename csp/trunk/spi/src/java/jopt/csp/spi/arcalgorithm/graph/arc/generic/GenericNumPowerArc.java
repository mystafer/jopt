package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.math.BigDecimal;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.spi.util.TrigMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X^y,  Z = X^Y, etc.
 */
public class GenericNumPowerArc extends GenericNumTrigArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private boolean yreciprocal;
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumPowerArc(Node x, Node y, boolean yreciprocal, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
        this.yreciprocal = yreciprocal;
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
    public GenericNumPowerArc(Number x, Node y, boolean yreciprocal, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
        this.yreciprocal = yreciprocal;
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
    public GenericNumPowerArc(GenericNumConstant x, Node y, boolean yreciprocal, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
        this.yreciprocal = yreciprocal;
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
    public GenericNumPowerArc(Node x, Number y, boolean yreciprocal, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
        this.yreciprocal = yreciprocal;
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
    public GenericNumPowerArc(Node x, GenericNumConstant y, boolean yreciprocal, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
        this.yreciprocal = yreciprocal;
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
            // Handle NEQ operation
            if (arcType==NEQ) throw new PropagationFailureException();
    
            // Calculate constant y reciprocal
            Number yconstRecipMin = null;
            if (yreciprocal && currentYConst!=null) {
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
                BigDecimal yconstRecipUp = yconstRecip.setScale(15, BigDecimal.ROUND_UP);
                BigDecimal yconstRecipDn = yconstRecip.setScale(15, BigDecimal.ROUND_DOWN);
                yconstRecipMin = yconstRecipUp.min(yconstRecipDn);
            }
        }
        
        double precision = getPrecisionZ();
        
        // Calculate min and max values
        // x is a const
        if (currentXConst!=null) {
            TrigMath.powerMinMax(currentNz, currentXConst, currentNy, yreciprocal, precision, min, max);
        }
        
        // if y is const, determine if reciprocal of y should be used
        else if (yreciprocal) {
            TrigMath.powerMinMax(currentNz, currentNx, yconstRecipMin, precision, min, max);
        }
        
        // y is const
        else {
            TrigMath.powerMinMax(currentNz, currentNx, yconst, precision, min, max);
        }
        
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
