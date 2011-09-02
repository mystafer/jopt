package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.DoubleSparseSet;
import jopt.csp.util.FloatSparseSet;
import jopt.csp.util.IntSparseSet;
import jopt.csp.util.LongSparseSet;
import jopt.csp.util.NumSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = sqrt(a), Z < sqrt(a), etc with special square-related behavior
 */
public class NodeNumSquareReflexArc extends NodeNumArc {
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private NumSet workingSet;
    
    /**
     * Constructor
     */
    public NodeNumSquareReflexArc(Number a, NumNode z, int nodeType, int arcType) {
        super(a, z, nodeType, arcType);
    }

    /**
     * Attempts to reduce values in target node domain based on Min / Max values
     * in source node(s)
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        Number zero = NumberMath.zero(nodeType);
        
        switch(arcType) {
            case GEQ:
                if(NumberMath.compareToZero(a, z.getPrecision()) <= 0) {}
                else {
                    NumberMath.sqrtCeil(a, nodeType, v2);
                    NumberMath.neg(v2, v1);
                    NumberMath.next(v1, z.getPrecision(), v1);
                    NumberMath.previous(v2, z.getPrecision(), v2);
                    z.removeRange(v1, v2);
                }
                break;

            case GT:
                if (a.doubleValue() < 0) {}
                else if (NumberMath.compareToZero(a, z.getPrecision()) == 0) {
                    z.removeValue(zero);
                }
                else {
                    NumberMath.sqrtFloor(a, nodeType, v2);
                    NumberMath.neg(v2, v1);
                    z.removeRange(v1, v2);
                }
                break;

            case LEQ:
                if(a.doubleValue() < 0) {
                    throw new PropagationFailureException("squared numbers cannot be negative");
                }
                else {
                    NumberMath.sqrtFloor(a, nodeType, v2);
                    NumberMath.neg(v2, v1);
                    z.setRange(v1, v2);
                }
                break;

            case LT:
                if(NumberMath.compareToZero(a, z.getPrecision()) <= 0) {
                    throw new PropagationFailureException("squared numbers cannot be negative");
                }
                else {
                    NumberMath.sqrtCeil(a, nodeType, v2);
                    NumberMath.neg(v2, v1);
                    NumberMath.next(v1, z.getPrecision(), v1);
                    NumberMath.previous(v2, z.getPrecision(), v2);
                    z.setRange(v1, v2);
                }
                break;

            case EQ:
                NumberMath.sqrt(a, nodeType, v1);
                if (v1.isInvalid()) throw new PropagationFailureException();

                // Set domain to 2 values
                switch (nodeType) {
                    case INTEGER:
                        if (workingSet==null) workingSet = new IntSparseSet();
                        else workingSet.clear();
                        IntSparseSet iset = (IntSparseSet) workingSet;
                        iset.add(v1.intValue());
                        iset.add(-v1.intValue());
                        z.setDomain(iset);
                        break;
                        
                    case LONG:
                        if (workingSet==null) workingSet = new LongSparseSet();
                        else workingSet.clear();
                        LongSparseSet lset = (LongSparseSet) workingSet;
                        lset.add(v1.longValue());
                        lset.add(-v1.longValue());
                        z.setDomain(lset);
                        break;
                        
                    case FLOAT:
                        if (workingSet==null) workingSet = new FloatSparseSet();
                        else workingSet.clear();
                        FloatSparseSet fset = (FloatSparseSet) workingSet;
                        fset.add(v1.floatValue());
                        fset.add(-v1.floatValue());
                        z.setDomain(fset);
                        break;
                        
                    default:
                        if (workingSet==null) workingSet = new DoubleSparseSet();
                        else workingSet.clear();
                        DoubleSparseSet dset = (DoubleSparseSet) workingSet;
                        dset.add(v1.doubleValue());
                        dset.add(-v1.doubleValue());
                        z.setDomain(dset);
                        break;
                }
                break;

            case NEQ:
                NumberMath.sqrt(a, nodeType, v1);
                if (!v1.isInvalid()) {
                    z.removeValue(v1);
                    NumberMath.neg(v1, v1);
                    z.removeValue(v1);
                }
        }
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" sqrt of A(");
        buf.append(a);
        buf.append(")");
    }
}
