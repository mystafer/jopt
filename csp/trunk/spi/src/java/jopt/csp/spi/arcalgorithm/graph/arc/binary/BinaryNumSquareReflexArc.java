package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing sqrt(X) = Z, sqrt(X) < Z etc with special square-related behavior
 */
public class BinaryNumSquareReflexArc extends BinaryNumArc {
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
    public BinaryNumSquareReflexArc(NumNode x, NumNode z, int nodeType, int arcType) {
        super(x, null, null, null, z, nodeType, arcType, NumOperation.SQRT);
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        Number zero = NumberMath.zero(nodeType);
        Number xmin = x.getMin();
        Number xmax = x.getMax();
        
        // Handle neq arc
        if (arcType == NEQ) {
            if (x.isBound() && (NumberMath.isPositive(xmax) || NumberMath.isZero(xmax))) {
                NumberMath.sqrtNoInvalid(xmax, nodeType, v1);
                z.removeValue(v1);
                NumberMath.neg(v1, v2);
                z.removeValue(v2);
            }
            return;
        }
        
        switch(arcType) {
            case GEQ:
                if (NumberMath.isNegative(xmin) || NumberMath.isZero(xmin)) {}
                else {
                    NumberMath.sqrtCeil(xmin, nodeType, v1);
                    NumberMath.previous(v1, z.getPrecision(), v2);
                    NumberMath.neg(v1, v1);
                    NumberMath.next(v1, z.getPrecision(), v1);
                    z.removeRange(v1, v2);
                }
                break;
                
            case GT:
                if (NumberMath.isNegative(xmin)) {}
                else if (NumberMath.isZero(xmin)) {
                    z.removeValue(zero);
                }
                else {
                    NumberMath.sqrtFloor(xmin, nodeType, v1);
                    NumberMath.neg(v1, v2);
                    z.removeRange(v2, v1);
                }
                break;
                
            case LEQ:
                if (NumberMath.isNegative(xmax)) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else if (NumberMath.isZero(xmax)) {
                    z.setValue(zero);
                }
                else {
                    NumberMath.sqrtFloor(xmax, nodeType, v1);
                    NumberMath.neg(v1, v2);
                    z.setRange(v2, v1);
                }
                break;
                
            case LT:
                if (NumberMath.isNegative(xmax) || NumberMath.isZero(xmax)) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else {
                    NumberMath.sqrtCeil(xmax, nodeType, v2);
                    NumberMath.neg(v2, v1);
                    NumberMath.next(v1, z.getPrecision(), v1);
                    NumberMath.previous(v2, z.getPrecision(), v2);
                    z.setRange(v1, v2);
                }
                break;
                
            case EQ:
                if (NumberMath.isNegative(xmax)) {
                    throw new PropagationFailureException("Squared numbers cannot be negative");
                }
                else if (NumberMath.isZero(xmax)) {
                    z.setValue(zero);
                }
                else if (NumberMath.isNegative(xmin) || NumberMath.isZero(xmin)) {
                    NumberMath.sqrtFloor(xmax, nodeType, v1);
                    NumberMath.next(v1,z.getPrecision(),v1);
                    NumberMath.neg(v1, v2);
                    z.setRange(v2, v1);
                }
                else {
                    NumberMath.sqrtFloor(xmax, nodeType, v1);
                    NumberMath.neg(v1, v2);
                    z.setRange(v2, v1);
                    
                    NumberMath.sqrtCeil(xmin, nodeType, v1);
                    NumberMath.previous(v1, z.getPrecision(), v1);
                    NumberMath.neg(v1, v2);
                    z.removeRange(v2, v1);
                }
                break;
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        NumberIterator deltaIter = x.deltaValues();
        while (deltaIter.hasNext()) {
            Number v = deltaIter.next();
            
            // determine if value is valid before removing it
            NumberMath.sqrt(v, nodeType, v1);
            if (!v1.isInvalid())
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
                NumberMath.square(zval, nodeType, v1);
    
                // If val is not in a domain, remove from z
                if (v1.isInvalid() || !x.isInDomain(v1))
                    numList.add(v1);
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
        buf.append(" sqrt of X(");
        buf.append(x);
        buf.append(") ");
    }
}
