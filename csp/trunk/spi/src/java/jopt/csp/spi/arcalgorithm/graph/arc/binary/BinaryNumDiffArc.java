package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X - y, Z < x - Y, etc.
 */
public class BinaryNumDiffArc extends BinaryNumArc {
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
    public BinaryNumDiffArc(NumNode x, Number yconst, NumNode z, int nodeType, int arcType) {
        super(x, null, null, yconst, z, nodeType, arcType, NumOperation.SUBTRACT);
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
    public BinaryNumDiffArc(Number xconst, NumNode y, NumNode z, int nodeType, int arcType) {
        super(null, xconst, y, null, z, nodeType, arcType, NumOperation.SUBTRACT);
    }

    protected void propagateBounds() throws PropagationFailureException {
        Number xmin = xconst;
        Number xmax = xconst;
        Number ymin = yconst;
        Number ymax = yconst;

        if (x != null) {
            xmin = x.getMin();
            xmax = x.getMax();
        }
        else {
            ymin = y.getMin();
            ymax = y.getMax();
        }

        switch(arcType) {
            case GEQ:
                NumberMath.subtractNoInvalid(xmin, ymax, nodeType, min);
                z.setMin(min);
                break;

            case GT:
                NumberMath.subtractNoInvalid(xmin, ymax, nodeType, min);
                NumberMath.next(min, z.getPrecision(), min);
                z.setMin(min);
                break;

            case LEQ:
                NumberMath.subtractNoInvalid(xmax, ymin, nodeType, max);
                z.setMax(max);
                break;

            case LT:
                NumberMath.subtractNoInvalid(xmax, ymin, nodeType, max);
                NumberMath.previous(max, z.getPrecision(), max);
                z.setMax(max);
                break;

            case EQ:
                NumberMath.subtractNoInvalid(xmin, ymax, nodeType, min);
                NumberMath.subtractNoInvalid(xmax, ymin, nodeType, max);
                z.setRange(min, max);
                break;

            case NEQ:
                if (x.isBound() && y.isBound()) {
                    NumberMath.subtractNoInvalid(xmin, ymin, nodeType, min);
                    z.removeValue(min);
                }
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        // loop over delta values from x
        if (x!=null) {
            NumberIterator deltaIter = x.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = (Number) deltaIter.next();
                
                NumberMath.subtract(v, yconst, nodeType, min);
                if (!min.isInvalid()) z.removeValue(min);
            }
        }
        
        // loop over delta values from y
        else {
            NumberIterator deltaIter = y.deltaValues();
            while (deltaIter.hasNext()) {
                Number v = (Number) deltaIter.next();
                
                NumberMath.subtract(xconst, v, nodeType, min);
                if (!min.isInvalid()) z.removeValue(min);
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
                    NumberMath.add(zval, yconst, nodeType, min);
    
                    // If val is not in x domain, remove from z
                    if (min.isInvalid() || !x.isInDomain(min))
                        numList.add(min);
                }
                else {
                    // Determine value of y for z
                    NumberMath.subtract(xconst, zval, nodeType, min);
    
                    // If val is not in y domain, remove from z
                    if (min.isInvalid() || !y.isInDomain(min))
                        numList.add(min);
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
}
