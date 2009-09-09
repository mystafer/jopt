package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberList;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing X = |Z|
 */
public class BinaryNumAbsReflectArc extends BinaryNumArc {
    private MutableNumber v1;
    private MutableNumber v2;
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumAbsReflectArc(NumNode x, NumNode z, int nodeType, int arcType) {
        super(x, null, null, null, z, nodeType, arcType, NumOperation.ABS);
        this.v1 = new MutableNumber();
        this.v2 = new MutableNumber();
    }

    /**
     * Returns min abs of node A
     */
    private Number minX() throws PropagationFailureException {
        Number xMax = x.getMax();
        Number xMin = x.getMin();
        
        // retrieve negative of max value
        NumberMath.neg(xMax, v1);
        NumberMath.min(xMin, v1, v1);
        
        return v1;
    }

    /**
     * Returns max abs of node A
     */
    private Number maxX() {
        Number xMax = x.getMax();
        Number xMin = x.getMin();
        
        // retrieve negative of min value
        NumberMath.neg(xMin, v2);
        NumberMath.max(xMax, v2, v2);
        
        return v2;
    }
    
    protected void propagateBounds() throws PropagationFailureException {
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
            
            z.removeValue(deltaIter);
            NumberMath.neg(deltaIter, v1);
            z.removeValue(v1);
        }
    }
            
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumberList numList = NumberList.pool.borrowList();
        
        try {
            // loop over values recently removed from x
            NumberIterator zIter = x.values();
            while (zIter.hasNext()) {
                Number v = zIter.next();
                
                // if neither v nor -v is in x, remove v from z
                NumberMath.neg(v, v1);
                if (!x.isInDomain(v) && !x.isInDomain(v1))
                    numList.add(v);
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
        buf.append(" abs-reflect X(");
        buf.append(x);
        buf.append(") ");
    }
}
