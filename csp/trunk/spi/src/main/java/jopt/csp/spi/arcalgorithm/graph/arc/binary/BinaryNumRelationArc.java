package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = A, Z < A, Z > A, etc.
 */
public class BinaryNumRelationArc extends BinaryNumArc implements NumArc {
    private MutableNumber work = new MutableNumber();
    
    /**
     * Constructor
     */
    public BinaryNumRelationArc(NumNode a, NumNode z, int nodeType, int arcType) {
        super(a, z);
        this.nodeType = nodeType;
        this.arcType = arcType;

        // Set source dependency value
        if (arcType == NEQ) sourceDependency = DomainChangeType.VALUE;
        else if (arcType != EQ || NumberMath.isRealType(nodeType)) sourceDependency = DomainChangeType.RANGE;
        else sourceDependency = DomainChangeType.DOMAIN;
    }

    protected void propagateBounds() throws PropagationFailureException {
        NumNode a = (NumNode) source;
        NumNode z = (NumNode) target;
        
        Number amin = a.getMin();
        Number amax = a.getMax();
        
        switch(arcType) {
            case GEQ:
                z.setMin(amin);
                break;
    
            case GT:
                NumberMath.next(amin, z.getPrecision(), work);
                z.setMin(work);
                break;
    
            case LEQ:
                z.setMax(amax);
                break;
    
            case LT:
                NumberMath.previous(amax, z.getPrecision(), work);
                z.setMax(work);
                break;
    
            case EQ:
                z.setRange(amin, amax);
                break;
                
            case NEQ:
                if (a.isBound()) z.removeValue(amin);
        }
    }
        
    protected void propagateEqArcConsistent() throws PropagationFailureException {
        NumNode a = (NumNode) source;
        NumNode z = (NumNode) target;
        
        NumberIterator deltaIter = a.deltaValues();
        while (deltaIter.hasNext()) {
            Number v = deltaIter.next();
            z.removeValue(v);
        }
    }
        
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
        NumNode a = (NumNode) source;
        NumNode z = (NumNode) target;
        z.setDomain(a.getDomain());
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(target);
        buf.append(") ");

        switch(arcType) {
            case GEQ:
                buf.append(">=");
                break;

            case GT:
                buf.append(">");
                break;

            case LEQ:
                buf.append("<=");
                break;

            case LT:
                buf.append("<");
                break;

            case EQ:
                buf.append("=");
                break;

            case NEQ:
                buf.append("!=");
        }

        buf.append(" A(");
        buf.append(source);
        buf.append(")");

        return buf.toString();
    }
}
