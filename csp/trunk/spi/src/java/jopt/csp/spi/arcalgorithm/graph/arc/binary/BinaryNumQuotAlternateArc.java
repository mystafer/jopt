package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X / y, Z < x / Y, etc with multiplication- and division-related behavior.
 * For an explanation of this arc's behavior, check out
 * {@link jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumQuotAlternateArc}.
 * @see BinaryNumQuotArc
 */
public class BinaryNumQuotAlternateArc extends BinaryNumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   yconst      Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumQuotAlternateArc(NumNode x, Number yconst, NumNode z, int nodeType, int arcType) {
        super(x, null, null, yconst, z, nodeType, arcType, NumOperation.DIVIDE);
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
    public BinaryNumQuotAlternateArc(Number xconst, NumNode y, NumNode z, int nodeType, int arcType) {
        super(null, xconst, y, null, z, nodeType, arcType, NumOperation.DIVIDE);
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        // Determine min and max values for X and Y
        Number zero = NumberMath.zero(nodeType);
        Number xval1 = null;
        Number xval2 = null;
        Number yval1 = null;
        Number yval2 = null;
        boolean yContainsZero = false;
        boolean zContainsZero = z.isInDomain(zero) || (NumberMath.isPositive(z.getMax()) && NumberMath.isNegative(z.getMin()));
        if (x != null) {
            xval1 = x.getMin();
            xval2 = x.getMax();
            yval1 = yconst;
            yval2 = yconst;
            yContainsZero = NumberMath.compareToZero(yconst, z.getPrecision()) == 0;
        }
        else {
            xval1 = xconst;
            xval2 = xconst;
            yval1 = y.getMin();
            yval2 = y.getMax();
            yContainsZero = y.isInDomain(zero) || (NumberMath.isPositive(y.getMax()) && NumberMath.isNegative(y.getMin()));
        }
        
        switch(arcType) {
            case GEQ:
                if (yContainsZero || zContainsZero) {}
                // y and z are either strictly negative or strictly positive
                else if ( (NumberMath.isPositive(z.getMin()) && NumberMath.isPositive(yval1)) || 
                          (NumberMath.isNegative(z.getMax()) && NumberMath.isNegative(yval2)) ) {
                    NumberMath.divideCeil(xval1, yval1, nodeType, v1);
                    NumberMath.divideCeil(xval2, yval2, nodeType, v2);
                    NumberMath.min(v1, v2, min);
                    z.setMin(min);
                }
                else { // (z+ && y-) || (z- && y+)
                    NumberMath.divideFloor(xval1, yval1, nodeType, v1);
                    NumberMath.divideFloor(xval2, yval2, nodeType, v2);
                    NumberMath.max(v1, v2, max);
                    z.setMax(max);
                }
                break;

            case GT:
                if (yContainsZero || zContainsZero) {}
                // y and z are either strictly negative or strictly positive
                else if ( (NumberMath.isPositive(z.getMin()) && NumberMath.isPositive(yval1)) || 
                          (NumberMath.isNegative(z.getMax()) && NumberMath.isNegative(yval2)) ) {
                    NumberMath.divideFloor(xval1, yval1, nodeType, v1);
                    NumberMath.divideFloor(xval2, yval2, nodeType, v2);
                    NumberMath.next(v2, z.getPrecision(), v2);
                    NumberMath.min(v1, v2, min);
                    z.setMin(min);
                }
                else { // (z+ && y-) || (z- && y+)
                    NumberMath.divideCeil(xval1, yval1, nodeType, v1);
                    NumberMath.divideCeil(xval2, yval2, nodeType, v2);
                    NumberMath.previous(v2, z.getPrecision(), v2);
                    NumberMath.max(v1, v2, max);
                    z.setMax(max);
                }
                break;

            case LEQ:
                if (yContainsZero || zContainsZero) {}
                // y and z are either strictly negative or strictly positive
                else if ( (NumberMath.isPositive(z.getMin()) && NumberMath.isPositive(yval1)) || 
                          (NumberMath.isNegative(z.getMax()) && NumberMath.isNegative(yval2)) ) {
                    NumberMath.divideFloor(xval1, yval1, nodeType, v1);
                    NumberMath.divideFloor(xval2, yval2, nodeType, v2);
                    NumberMath.max(v1, v2, max);
                    z.setMax(max);
                }
                else { // (z+ && y-) || (z- && y+)
                    NumberMath.divideCeil(xval1, yval1, nodeType, v1);
                    NumberMath.divideCeil(xval2, yval2, nodeType, v2);
                    NumberMath.min(v1, v2, min);
                    z.setMin(min);
                }
                break;

            case LT:
                if (yContainsZero || zContainsZero) {}
                // y and z are either strictly negative or strictly positive
                else if ( (NumberMath.isPositive(z.getMin()) && NumberMath.isPositive(yval1)) || 
                          (NumberMath.isNegative(z.getMax()) && NumberMath.isNegative(yval2)) ) {
                    NumberMath.divideCeil(xval1, yval1, nodeType, v1);
                    NumberMath.divideCeil(xval2, yval2, nodeType, v2);
                    NumberMath.previous(v2, z.getPrecision(), v2);
                    NumberMath.max(v1, v2, max);
                    z.setMax(max);
                }
                else { // (z+ && y-) || (z- && y+)
                    NumberMath.divideFloor(xval1, yval1, nodeType, v1);
                    NumberMath.divideFloor(xval2, yval2, nodeType, v2);
                    NumberMath.next(v2, z.getPrecision(), v2);
                    NumberMath.min(v1, v2, min);
                    z.setMin(min);
                }
                break;

            case EQ:
                if (yContainsZero) {}
                else {
                    NumberMath.divideCeil(xval1, yval1, nodeType, v1);
                    NumberMath.divideCeil(xval2, yval2, nodeType, v2);
                    NumberMath.min(v1, v2, min);

                    NumberMath.divideFloor(xval1, yval1, nodeType, v1);
                    NumberMath.divideFloor(xval2, yval2, nodeType, v2);
                    NumberMath.max(v1, v2, max);
                    
                    z.setRange(min, max);
                }
                break;
            
            case NEQ:
                if ( (x==null || x.isBound()) && (y==null || y.isBound()) ) {
                    NumberMath.divideNoInvalid(xval1, yval1, nodeType, v1);
                    z.removeValue(v1);
                }
                break;
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        // loop over delta values from x
        if (x!=null) {
            NumberIterator deltaIter = x.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = deltaIter.next();
                
                // make sure division yields a valid value before removing it
                NumberMath.divide(v, yconst, nodeType, v1);
                if (!v1.isInvalid())
                    z.removeValue(v1);
            }
        }
        
        // loop over delta values from y
        else {
            NumberIterator deltaIter = y.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = deltaIter.next();
                
                // make sure division yields a valid value before removing it
                NumberMath.divide(v, yconst, nodeType, v1);
                if (!v1.isInvalid())
                    z.removeValue(v1);
            }
        }
    }
        
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumberList numList = NumberList.pool.borrowList();
        
        try {
            NumberIterator zIter = z.values();
    
            // Loop over all values in z and determine if each is supported
            // by X and Y
            while (zIter.hasNext()) {
                Number zval = zIter.next();
    
                if (x!=null) {
                    // Determine value of x for z
                    NumberMath.multiply(zval, yconst, nodeType, v1);
    
                    // If val is not in x domain, remove from z
                    if (v1.isInvalid() || !x.isInDomain(v1))
                        numList.add(v1);
                }
                else {
                    // Determine value of y for z
                    NumberMath.divide(xconst, zval, nodeType, v1);
    
                    // If val is not in y domain, remove from z
                    if (v1.isInvalid() || !y.isInDomain(v1))
                        numList.add(v1);
                }
            }
            
            // remove invalid values
            for (int i=0; i<numList.size(); i++)
                z.removeValue(numList.get(i));
        }
        
        // release any working pooled data
        finally {
            NumberList.pool.returnList(numList);
        }
    }
}
