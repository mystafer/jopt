package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X * y, Z < x * Y, etc.
 */
public class BinaryNumProdArc extends BinaryNumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber p1 = new MutableNumber();
    private MutableNumber p2 = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   yconst      Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumProdArc(NumNode x, Number yconst, NumNode z, int nodeType, int arcType) {
        super(x, null, null, yconst, z, nodeType, arcType, NumOperation.MULTIPLY);
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
    public BinaryNumProdArc(Number xconst, NumNode y, NumNode z, int nodeType, int arcType) {
        super(null, xconst, y, null, z, nodeType, arcType, NumOperation.MULTIPLY);
    }

    protected void propagateBounds() throws PropagationFailureException {
        // Calculate min and max values
        if (x!=null) {
            NumberMath.multiplyNoInvalid(x.getMax(), yconst, nodeType, p1);
            NumberMath.multiplyNoInvalid(x.getMin(), yconst, nodeType, p2);
        }
        else {
            NumberMath.multiplyNoInvalid(xconst, y.getMax(), nodeType, p1);
            NumberMath.multiplyNoInvalid(xconst, y.getMin(), nodeType, p2);
        }
        NumberMath.min(p1, p2, min);
        NumberMath.max(p1, p2, max);
        
        // Make sure value is a number
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
        // loop over delta values from x
        if (x!=null) {
            NumberIterator deltaIter = x.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = deltaIter.next();
                NumberMath.multiplyNoInvalid(v, yconst, nodeType, min);
                z.removeValue(min);
            }
        }
        
        // loop over delta values from y
        else {
            NumberIterator deltaIter = y.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = deltaIter.next();
                NumberMath.multiplyNoInvalid(xconst, v, nodeType, min);
                z.removeValue(min);
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
                    NumberMath.divide(zval, yconst, nodeType, min);
    
                    // If val is not in x domain, remove from z
                    if (!min.isInvalid() || !x.isInDomain(min))
                        numList.add(min);
                }
                else {
                    // Determine value of y for z
                    NumberMath.divide(zval, xconst, nodeType, min);
    
                    // If val is not in y domain, remove from z
                    if (!min.isInvalid() || !y.isInDomain(min))
                        numList.add(min);
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
