package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = X * Y, Z < X * Y, Z > X * Y, etc with special division-related behavior.
 * Take, for example, the constraint A < B / C.  There needs to be 3 arcs for this particular constraint:
 * one with A as the target, one with B, and one with C.  Note, however, that in manipulating A < B / C to
 * make B the target, a multiplication by C (A * C < B) occurs.  While this is not an issue with EQ and
 * NEQ arc types, inequalities "flip signs" when multiplication by a negative number occurs.
 * This is taken into consideration in this particular arc.
 */
public class TernaryNumQuotReflexArc extends TernaryNumArc {
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
    public TernaryNumQuotReflexArc(NumNode x, NumNode y, NumNode z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType, NumOperation.MULTIPLY);
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        Number zero = NumberMath.zero(nodeType);
        boolean yContainsZero = y.isInDomain(zero) || (NumberMath.isNegative(y.getMin()) && NumberMath.isPositive(y.getMax()));
        
        NumberMath.multiplyNoInvalid(x.getMax(), y.getMax(), nodeType, v1);
        NumberMath.multiplyNoInvalid(x.getMin(), y.getMin(), nodeType, v2);
        NumberMath.multiplyNoInvalid(x.getMax(), y.getMin(), nodeType, v3);
        NumberMath.multiplyNoInvalid(x.getMin(), y.getMax(), nodeType, v4);
            
            switch(arcType) {
                case GEQ:
                    if (yContainsZero) {
                    if(NumberMath.isPositive(x.getMin())) {
                            z.removeValue(zero);
                        }
                    }
                // y is strictly negative or strictly positive
                else if (NumberMath.isNegative(y.getMax())) {
                    NumberMath.max(v1, v2, v3, v4, max);
                    z.setMax(max);
                }
                // y.getMin().isPositive()
                else { 
                    NumberMath.min(v1, v2, v3, v4, min);
                    z.setMin(min);
                }
                break;
                
            case GT:
                if (yContainsZero) {
                    if(NumberMath.isZero(x.getMin()) || NumberMath.isPositive(x.getMin())) {
                        z.removeValue(zero);
                    }
                }
                // y is strictly negative or strictly positive
                else if (NumberMath.isNegative(y.getMax())) {
                    NumberMath.max(v1, v2, v3, v4, max);
                    NumberMath.previous(max, z.getPrecision(), max);
                    z.setMax(max);
                }
                // y.getMin().isPositive()
                else {
                    NumberMath.min(v1, v2, v3, v4, min);
                    NumberMath.next(min, z.getPrecision(), min);
                    z.setMin(min);
                }
                break;
                
            case LEQ:
                if (yContainsZero) {
                    if(NumberMath.isNegative(x.getMax())) {
                        z.removeValue(zero);
                    }
                }
                // y is strictly negative or strictly positive
                else if (NumberMath.isNegative(y.getMax())) {
                    NumberMath.min(v1, v2, v3, v4, min);
                    z.setMin(min);
                }
                // y.getMin().isPositive()
                else {
                    NumberMath.max(v1, v2, v3, v4, max);
                    z.setMax(max);
                }
                break;
                
            case LT:
                if (yContainsZero) {
                    if(NumberMath.isZero(x.getMax()) || NumberMath.isNegative(x.getMax())) {
                        z.removeValue(zero);
                    }
                }
                // y is strictly negative or strictly positive
                else if (NumberMath.isNegative(y.getMax())) {
                    NumberMath.min(v1, v2, v3, v4, min);
                    NumberMath.next(min, z.getPrecision(), min);
                    z.setMin(min);
                }
                else { // y.getMin(rmgr).isPositive()
                    NumberMath.max(v1, v2, v3, v4, max);
                    NumberMath.previous(max, z.getPrecision(), max);
                    z.setMax(max);
                }
                break;
                
            case EQ:
                NumberMath.min(v1, v2, v3, v4, min);
                NumberMath.max(v1, v2, v3, v4, max);
                z.setRange(min, max);
                break;
                
            case NEQ:
                if (x.isBound() && y.isBound())
                    z.removeValue(v1);
        }
    }

    protected void propagateEqArcConsistent(NumNode src) throws PropagationFailureException {
        propagateEqArcConsistentNoDeltas();
    }

    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumberList numList = NumberList.pool.borrowList();
        
        try {
            NumberIterator zIter = z.values();
    
            // Z = X + Y implies X = Z - Y and Y = Z - X 
            // so, s2 = Z - s1 will work regardless of 
            // whether s1 is x or y
            // Make s1 the node with the smaller domain between x and y
            NumNode s1 = x;
            NumNode s2 = y;
            if (s1.getSize() > s2.getSize()) {
                s1 = y;
                s2 = x;
            }
    
            // Loop over all values in z and determine if each is supported
            // by X and Y
            while (zIter.hasNext()) {
                Number zval = (Number) zIter.next();
                boolean supported = false;
    
                // Loop over all elements in s1 and search for support for z
                NumberIterator s1Iter = s1.values();
                while (s1Iter.hasNext()) {
                    Number s1val = (Number) s1Iter.next();
    
                    // Determine if yval is in domain of y
                    NumberMath.divide(zval, s1val, nodeType, v1);
                    if (!v1.isInvalid() && s2.isInDomain(v1)) {
                        supported = true;
                        break;
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
