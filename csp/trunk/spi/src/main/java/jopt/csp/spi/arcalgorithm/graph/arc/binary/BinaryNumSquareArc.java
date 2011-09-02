package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X^2
 */
public class BinaryNumSquareArc extends BinaryNumArc {
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
    public BinaryNumSquareArc(NumNode x, NumNode z, int nodeType, int arcType) {
        super(x, null, null, null, z, nodeType, arcType, NumOperation.SQUARE);
    }

    protected void propagateBounds() throws PropagationFailureException {
        Number zero = NumberMath.zero(nodeType);
        
        NumberMath.squareNoInvalid(x.getMin(), nodeType, v1);
        NumberMath.squareNoInvalid(x.getMax(), nodeType, v2);
        NumberMath.min(v1, v2, min);
        NumberMath.max(v1, v2, max);
        
        switch(arcType) {
            case GEQ:
                if(NumberMath.isPositive(x.getMin()) || NumberMath.isNegative(x.getMax())) {
                    // x is strictly positive or strictly negative
                    z.setMin(min);
                }
                else {
                    z.setMin(zero);
                }
                break;

            case GT:
                if(NumberMath.isPositive(x.getMin()) || NumberMath.isNegative(x.getMax())) {
                    // x is strictly positive or strictly negative
                    NumberMath.next(min, z.getPrecision(), min);
                    z.setMin(min);
                }
                else {
                    NumberMath.next(zero, z.getPrecision(), min);
                    z.setMin(min);
                }
                break;

            case LEQ:
                z.setMax(max);
                break;

            case LT:
                NumberMath.previous(max, z.getPrecision(), max);
                z.setMax(max);
                break;

            case EQ:
                if(NumberMath.isPositive(x.getMin()) || NumberMath.isNegative(x.getMax())) {
                    // x is strictly positive or strictly negative
                    z.setRange(min, max);
                }
                else {
                    z.setMin(zero);
                    z.setMax(max);
                }
                break;

            case NEQ:
                if (x.isBound()) z.removeValue(min);
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        NumberIterator deltaIter = x.deltaValues();
        while (deltaIter.hasNext()) {
            Number v = deltaIter.next();
            NumberMath.squareNoInvalid(v, nodeType, v1);
            z.removeValue(v1);
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
    
                // Determine value of x for z
                NumberMath.sqrt(zval, nodeType, v1);
    
                // If val is not in a domain, remove from z
                if (v1.isInvalid()) {
                    numList.add(zval);
                }
                else if (!x.isInDomain(v1)) {
                    numList.add(zval);
                }
                else {
                	NumberMath.neg(v1, v2);
                    if (!x.isInDomain(v2))
                        numList.add(zval);
                }
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

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" X(");
        buf.append(x);
        buf.append(")^2 ");
    }
}
