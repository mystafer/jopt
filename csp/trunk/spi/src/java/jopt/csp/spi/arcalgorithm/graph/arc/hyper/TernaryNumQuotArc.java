package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X / Y, Z < X / Y, Z > X / Y, etc.
 */
public class TernaryNumQuotArc extends TernaryNumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private MutableNumber v3 = new MutableNumber();
    private MutableNumber v4 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public TernaryNumQuotArc(NumNode x, NumNode y, NumNode z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType, NumOperation.DIVIDE);
    }

    protected void propagateBounds() throws PropagationFailureException {
        Number zero = NumberMath.zero(nodeType);
        boolean yContainsZero = y.isInDomain(zero) || (NumberMath.isPositive(y.getMax()) && NumberMath.isNegative(y.getMin()));
    
            // Calculate quotients of min and max of nodes
            switch(arcType) {
                case GEQ:
                    if (x.isBound()&& x.isInDomain(zero))
                        z.setMin(zero);
                    else if (yContainsZero)
                    z.setMin(NumberMath.minValue(nodeType));
                    else
                    z.setMin(getMinDivideCeil());
                    break;
    
                case GT:
                if (x.isBound()&& x.isInDomain(zero)) {
                    NumberMath.next(zero, z.getPrecision(), min);
                    z.setMin(min);
                }
                else if (yContainsZero) {
                    z.setMin(NumberMath.minValue(nodeType));
                }
                else {
                    NumberMath.next(getMinDivideFloor(), z.getPrecision(), min);
                    z.setMin(min);
                }
                    break;
    
                case LEQ:
                    if (x.isBound()&& x.isInDomain(zero))
                        z.setMax(zero);
                    else if (yContainsZero)
                    z.setMax(NumberMath.maxValue(nodeType));
                    else
                    z.setMax(getMaxDivideFloor());
                    break;
    
                case LT:
                if (x.isBound()&& x.isInDomain(zero)) {
                    NumberMath.previous(zero, z.getPrecision(), max);
                    z.setMax(max);
                }
                else if (yContainsZero) {
                    z.setMax(NumberMath.maxValue(nodeType));
                }
                else {
                    NumberMath.previous(getMaxDivideCeil(), z.getPrecision(), max);
                    z.setMax(max);
                }
                    break;
    
                case EQ:
                    if (x.isBound()&& x.isInDomain(zero))
                        z.setValue(zero);
                    else if (yContainsZero) {}
                    else
                    z.setRange(getMinDivideCeil(), getMaxDivideFloor());
                    break;
                    
                case NEQ:
                    if (x.isBound() && y.isBound()) {
                        if (!y.isInDomain(zero) || !x.isInDomain(zero)) {
                            if (nodeType == NumberMath.INTEGER || nodeType == NumberMath.LONG) {
                            NumberMath.divideFloor(x.getMin(), y.getMin(), nodeType, v1);
                            z.removeValue(v1);
                            }
                            else {
                            NumberMath.divide(x.getMin(), y.getMin(), nodeType, v1);
                            z.removeValue(v1);
                        }
                    }
                }
                break;
        }
    }
    
    private Number getMaxDivideCeil() {
        NumberMath.divideCeil(x.getMax(), y.getMax(), nodeType, v1);
        NumberMath.divideCeil(x.getMin(), y.getMin(), nodeType, v2);
        NumberMath.divideCeil(x.getMax(), y.getMin(), nodeType, v3);
        NumberMath.divideCeil(x.getMin(), y.getMax(), nodeType, v4);
        NumberMath.max(v1, v2, v3, v4, max);
        return max;
    }
    
    private Number getMinDivideCeil() {
        NumberMath.divideCeil(x.getMax(), y.getMax(), nodeType, v1);
        NumberMath.divideCeil(x.getMin(), y.getMin(), nodeType, v2);
        NumberMath.divideCeil(x.getMax(), y.getMin(), nodeType, v3);
        NumberMath.divideCeil(x.getMin(), y.getMax(), nodeType, v4);
        NumberMath.min(v1, v2, v3, v4, min);
        return min;
    }
    
    private Number getMaxDivideFloor() {
        NumberMath.divideFloor(x.getMax(), y.getMax(), nodeType, v1);
        NumberMath.divideFloor(x.getMin(), y.getMin(), nodeType, v2);
        NumberMath.divideFloor(x.getMax(), y.getMin(), nodeType, v3);
        NumberMath.divideFloor(x.getMin(), y.getMax(), nodeType, v4);
        NumberMath.max(v1, v2, v3, v4, max);
        return max;
    }
    
    private Number getMinDivideFloor() {
        NumberMath.divideFloor(x.getMax(), y.getMax(), nodeType, v1);
        NumberMath.divideFloor(x.getMin(), y.getMin(), nodeType, v2);
        NumberMath.divideFloor(x.getMax(), y.getMin(), nodeType, v3);
        NumberMath.divideFloor(x.getMin(), y.getMax(), nodeType, v4);
        NumberMath.min(v1, v2, v3, v4, min);
        return min;
    }

    protected void propagateEqArcConsistent(NumNode src) throws PropagationFailureException {
        propagateEqArcConsistentNoDeltas();
    }

    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumberList numList = NumberList.pool.borrowList();
        
        try {
            // Determine whether to loop over x or y
            boolean loopOnX = x.getSize() <= y.getSize();
    
            // Loop over all values in z and determine if each is supported
            // by X and Y
            NumberIterator zIter = z.values();
            while (zIter.hasNext()) {
                Number zval = (Number) zIter.next();
                boolean supported = false;
    
                // Loop on X values
                if (loopOnX) {
                    // Loop over all elements in x and search for support for z
                    NumberIterator xIter = x.values();
                    while (xIter.hasNext()) {
                        Number xval = (Number) xIter.next();
    
                        // Z = X / Y ->  Y = X / Z
                        NumberMath.divide(xval, zval, nodeType, v1);
    
                        // Determine if yval is in domain of y
                        if (!v1.isInvalid() && y.isInDomain(v1)) {
                            supported = true;
                            break;
                        }
                    }
                }
    
                // Loop on Y values
                else {
                    // Loop over all elements in y and search for support for z
                    NumberIterator yIter = y.values();
                    while (yIter.hasNext()) {
                        Number yval = (Number) yIter.next();
    
                        // Z = X / Y ->  X = Y * Z
                        NumberMath.multiply(yval, zval, nodeType, v1);
    
                        // Determine if yval is in domain of y
                        if (!v1.isInvalid() && x.isInDomain(v1)) {
                            supported = true;
                            break;
                        }
                    }
                }
    
                // Remove value if it is not supported
                if (!supported) numList.add(zval);
            }
            
            // remove values that were identified as invalid
            for (int i=0; i<numList.size(); i++)
                z.removeValue(numList.get(i));
        }
        
        // release any working pooled data
        finally {
            NumberList.pool.returnList(numList);
        }
    }
}
