package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = cardinality(X)
 */
public class BinaryNumCardinalityArc extends BinaryNumArc {
    private Set constSource;
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   x          Source node in equation
     * @param   z          target expression in equation
     * @param   arcType    Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumCardinalityArc(SetNode x, NumNode z, int arcType) {
        super(x, z);
        this.arcType = arcType;
        this.sourceDependency = DomainChangeType.DOMAIN;
    }

    /**
     * Constructor
     *
     * @param   x          Constant source set in equation
     * @param   z          target expression in equation
     * @param   arcType    Arc relation type (Eq, Lt, Gt, etc.)
     */
    public BinaryNumCardinalityArc(Set x, NumNode z, int arcType) {
        super(z, z);
        this.arcType = arcType;
        this.sourceDependency = DomainChangeType.DOMAIN;
        this.constSource = x;
    }

    protected void propagateBounds() throws PropagationFailureException {
        NumNode z = (NumNode) target;
        
        // determine min and max for target
        if (constSource != null) {
            min.setIntValue(constSource.size());
            max.setIntValue(constSource.size());
        }
        else {
            SetNode set = (SetNode) source;
            min.setIntValue(set.getRequiredCardinality());
            max.setIntValue(set.getPossibleCardinality());
        }
        
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
        }
    }

    protected void propagateEqArcConsistent() throws PropagationFailureException {
    }
        
    protected void propagateEqArcConsistentNoDeltas() throws PropagationFailureException {
    }

    protected void targetExprToString(StringBuffer buf) {
        buf.append("Z(");
        buf.append(target);
        buf.append(") ");
    }
    
    protected void exprToString(StringBuffer buf) {
        buf.append(" cardinality of X(");
        buf.append(source);
        buf.append(")");
    }

}

