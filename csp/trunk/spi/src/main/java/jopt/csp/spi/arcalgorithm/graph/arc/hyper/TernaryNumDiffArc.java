package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X - Y, Z < X - Y, Z > X - Y, etc.
 */
public class TernaryNumDiffArc extends TernaryNumArc {
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
    public TernaryNumDiffArc(NumNode x, NumNode y, NumNode z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType, NumOperation.SUBTRACT);
    }

    protected void propagateBounds() throws PropagationFailureException {
            switch(arcType) {
                case GEQ:
                NumberMath.subtractNoInvalid(x.getMin(), y.getMax(), nodeType, min);
                z.setMin(min);
                    break;
    
                case GT:
                NumberMath.subtractNoInvalid(x.getMin(), y.getMax(), nodeType, min);
                NumberMath.next(min, z.getPrecision(), min);
                z.setMin(min);
                    break;
    
                case LEQ:
                NumberMath.subtractNoInvalid(x.getMax(), y.getMin(), nodeType, max);
                z.setMax(max);
                    break;
    
                case LT:
                NumberMath.subtractNoInvalid(x.getMax(), y.getMin(), nodeType, max);
                NumberMath.previous(max, z.getPrecision(), max);
                z.setMax(max);
                    break;
    
                case EQ:
                NumberMath.subtractNoInvalid(x.getMin(), y.getMax(), nodeType, min);
                NumberMath.subtractNoInvalid(x.getMax(), y.getMin(), nodeType, max);
                z.setRange(min, max);
                    break;
    
                case NEQ:
                if (x.isBound() && y.isBound()) {
                    NumberMath.subtractNoInvalid(x.getMin(), y.getMin(), nodeType, min);
                    z.removeValue(min);
        }
        }
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
    
                        // Z = X - Y ->  Y = X - Z
                        NumberMath.subtract(xval, zval, nodeType, min);
    
                        // Determine if yval is in domain of y
                        if (!min.isInvalid() && y.isInDomain(min)) {
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
    
                        // Z = X - Y ->  X = Y + Z
                        NumberMath.add(yval, zval, nodeType, min);
    
                        // Determine if yval is in domain of y
                        if (!min.isInvalid() && x.isInDomain(min)) {
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
