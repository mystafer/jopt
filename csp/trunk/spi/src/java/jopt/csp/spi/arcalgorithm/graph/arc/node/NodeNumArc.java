package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = a, Z < a, Z > a, etc.
 */
public class NodeNumArc extends NodeArc implements NumArc {
    private MutableNumber work = new MutableNumber();
    
    protected NumNode z;
    protected Number a;
    protected int nodeType;
    protected int arcType;

    /**
     * Constructor
     */
    public NodeNumArc(Number a, NumNode z, int nodeType, int arcType) {
        super(z);
        this.z = z;
        this.a = NumberMath.toConst(a);
        this.nodeType = nodeType;
        this.arcType = arcType;
    }
    
    /** 
     * Attempts to reduce values in target node domain based on all values
     * in source node(s)
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        switch(arcType) {
            case GEQ:
                z.setMin(a);
                break;
                
            case GT:
                NumberMath.next(a, z.getPrecision(), work);
                z.setMin(work);
                break;
                
            case LEQ:
                z.setMax(a);
                break;
                
            case LT:
                NumberMath.previous(a, z.getPrecision(), work);
                z.setMax(work);
                break;
                
            case EQ:
                z.setValue(a);
                break;
                
            case NEQ:
                z.removeValue(a);
        }
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(node);
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
        
        exprToString(buf);
        
        return buf.toString();
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" a(");
        buf.append(a);
        buf.append(")");
    }
}
