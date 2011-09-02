package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NodeMath;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = |X|
 */
public class BinaryNumAbsArc extends BinaryNumArc {
    private MutableNumber v1;
    private MutableNumber v2;
    private MutableNumber minXretVal;
    private MutableNumber maxXretVal;
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumAbsArc(NumNode x, NumNode z, int nodeType, int arcType) {
        super(x, null, null, null, z, nodeType, arcType, NumOperation.ABS);
        this.v1 = new MutableNumber();
        this.v2 = new MutableNumber();
        this.minXretVal = new MutableNumber();
        this.maxXretVal = new MutableNumber();
    }
    
    /**
     * Returns min abs of node A
     */
    private Number minX() throws PropagationFailureException {
        Number retval = x.getMin();
        
        // Retrieve zero in same number type as a domain
        int nodeType = NodeMath.nodeType(x);
        Number zero = NumberMath.zero(nodeType);
        
        // Determine minimum absolute value of a
        if (!x.isInDomain(zero)) {
            // determine next highest and next lowest values
            NumberMath.next(zero, z.getPrecision(), v1);
            NumberMath.previous(zero, z.getPrecision(), v2);
            
            // determine abs of high and low
            NumberMath.abs(v1, v1);
            NumberMath.abs(v2, v2);
            
            // determine if bound to zero
            if (NumberMath.isZero(v1) && NumberMath.isZero(v2))
                throw new PropagationFailureException();

            // update the return value
            NumberMath.min(v1, v2, minXretVal);
            retval = minXretVal;
        }
        
        return retval;
    }

    /**
     * Returns max abs of node A
     */
    private Number maxX() {
        // retrieve min and max values
        Number min = x.getMin();
        Number max = x.getMin();
        
        // determine abs of min and max
        NumberMath.abs(min, v1);
        NumberMath.abs(max, v2);
        
        // Determine maximum absolute value of a
        NumberMath.max(v1, v2, maxXretVal);
        
        return maxXretVal;
    }
    
    protected void propagateBounds() throws PropagationFailureException {
        // Z can only contain positive values
        if (NumberMath.isNegative(x.getMax()))
            throw new PropagationFailureException();

        switch(arcType) {
            case GEQ:
                z.setMin(minX());
                break;

            case GT:
                NumberMath.next(minX(), z.getPrecision(), v1);
                z.setMin(v1);
                break;

            case LEQ:
                z.setMax(maxX());
                break;

            case LT:
                NumberMath.previous(maxX(), z.getPrecision(), v2);
                z.setMax(v2);
                break;

            case EQ:
                z.setRange(minX(), maxX());
                break;

            case NEQ:
                if (x.isBound()) z.removeValue(minX());
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
        // loop over values recently removed from x
        NumberIterator deltaIter = x.deltaValues();
        while (deltaIter.hasNext()) {
            deltaIter.next();
            
        	// check if negative value of number exists in x
            // otherwise remove it from z
            NumberMath.neg(deltaIter, v1);
            if (!x.isInDomain(v1))
                z.removeValue(v1);
        }
    }
            
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumberList numList = NumberList.pool.borrowList();
        
        try {
            NumberIterator zIter = z.values();
            while (zIter.hasNext()) {
                zIter.next();
                
                // if neither v nor -v is in x, remove v from z
                NumberMath.neg(zIter, v1);
                if (x.isInDomain(zIter) && !x.isInDomain(v1))
                    numList.add(v1);
            }
            
            for (int i=0; i<numList.size(); i++)
                z.removeValue(numList.get(i));
        }
        finally {
        	NumberList.pool.returnList(numList);
        }
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" |X(");
        buf.append(x);
        buf.append(")| ");
    }
}
