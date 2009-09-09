package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;

/**
 * Abstract base arc for numeric arc with 3 variables
 */
public abstract class TernaryNumArc extends HyperArc implements NumArc {
    protected NumNode x;
    protected NumNode y;
    protected NumNode z;
    protected int nodeType;
    protected int arcType;
    protected int operation;

    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     * @param   operation   Type of operation being performed (+, -, *, /)
     */
    public TernaryNumArc(NumNode x, NumNode y, NumNode z, int nodeType, int arcType, int operation) {
        super(new NumNode[]{x, y}, z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.operation = operation;

        // Set source dependency value
        this.sourceDependencies = new int[] {
            determineSourceDependency(x),
            determineSourceDependency(y)
        };
    }
    
    /**
     * Helper function to determine source dependency for a node
     */
    protected int determineSourceDependency(NumNode node) {
        if (arcType == NEQ) return DomainChangeType.VALUE;
        if (arcType != EQ || NumberMath.isRealType(nodeType)) return DomainChangeType.RANGE;
        return DomainChangeType.DOMAIN;
    }
    
    /**
     * Returns true if constraint is binary
     */
    public boolean isBinary() {
        return false;
    }
    
    public final void propagate() throws PropagationFailureException {
        propagate(null);
    }
    
    public final void propagate(Node src) throws PropagationFailureException {
        propagateBounds();
        
        // Only need to enumerate over individual values for equality
        // constraint when performing arc consitency on a finite domain
        if (arcType == EQ && strength>=CspAlgorithmStrength.HYPER_ARC_CONSISTENCY && !NumberMath.isRealType(nodeType)) {
            if (useDeltas && src!=null)
                propagateEqArcConsistent((NumNode) src);
            else
                propagateEqArcConsistentNoDeltas();
        }
    }
    
    /**
     * This is called whenever propagate is called to ensure min and max bounds are consistent
     */
    protected abstract void propagateBounds() throws PropagationFailureException;

    /**
     * Called if arc consistency strength is required to ensure an equality constraint enforces
     * this level of filtering if domain deltas are to be used
     */
    protected abstract void propagateEqArcConsistent(NumNode src) throws PropagationFailureException;

    /**
     * Called if arc consistency strength is required to ensure an equality constraint enforces
     * this level of filtering if domain deltas are to be used
     */
    protected abstract void propagateEqArcConsistentNoDeltas() throws PropagationFailureException;
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(z);
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
        buf.append(" X(");
        buf.append(x);
        buf.append(") ");
        buf.append(NumOperation.operationSymbol(operation));
        buf.append(" Y(");
        buf.append(y);
        buf.append(")");
    }
}
