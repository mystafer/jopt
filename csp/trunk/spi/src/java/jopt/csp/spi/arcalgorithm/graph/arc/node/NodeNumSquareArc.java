package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = a^2, Z < a^2, etc.
 */
public class NodeNumSquareArc extends NodeNumArc {
    private MutableNumber work = new MutableNumber();
    
    /**
     * Constructor
     */
    public NodeNumSquareArc(Number a, NumNode z, int nodeType, int arcType) {
        super(a, z, nodeType, arcType);
    }

    /**
     * Attempts to reduce values in target node domain based on Min / Max values
     * in source node(s)
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        NumberMath.squareNoInvalid(a, nodeType, work);
        
        switch(arcType) {
            case GEQ:
                z.setMin(work);
                break;

            case GT:
                NumberMath.next(work, z.getPrecision(), work);
                z.setMin(work);
                break;

            case LEQ:
                z.setMax(work);
                break;

            case LT:
                NumberMath.previous(work, z.getPrecision(), work);
                z.setMax(work);
                break;

            case EQ:
                z.setValue(work);
                break;

            case NEQ:
                z.removeValue(work);
        }
    }

    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        buf.append(" A(");
        buf.append(a);
        buf.append(")^2");
    }
}
