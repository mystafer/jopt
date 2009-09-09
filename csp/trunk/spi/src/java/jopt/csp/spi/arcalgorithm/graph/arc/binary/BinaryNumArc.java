package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.NumOperation;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;

/**
 * Abstract base arc for numeric arc with 2 variables
 */
public abstract class BinaryNumArc extends BinaryArc implements NumArc {
    protected NumNode x;
    protected Number xconst;
    protected NumNode y;
    protected Number yconst;
    protected NumNode z;
    protected int nodeType;
    protected int arcType;
    protected int operation;
    protected boolean isRealType;
    
    /**
     * Constructor
     */
    protected BinaryNumArc(Node source, Node target) {
        super(source, target);
    }
    
    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   xconst      X constant in equation (x or xconst must be null)
     * @param   y           Y variable in equation
     * @param   yconst      Y constant in equation (y or yconst must be null)
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     * @param   operation   Type of operation being performed (+, -, *, /)
     */
    protected BinaryNumArc(NumNode x, Number xconst, NumNode y, Number yconst,
        NumNode z, int nodeType, int arcType, int operation) 
    {
        super(x!=null ? x : y, z);
        this.x = x;
        this.xconst = NumberMath.toConst(xconst);
        this.y = y;
        this.yconst = NumberMath.toConst(yconst);
        this.z = z;
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.operation = operation;
        this.isRealType = NumberMath.isRealType(nodeType);
        
        // Set source dependency value
        if (arcType == NEQ) sourceDependency = DomainChangeType.VALUE;
        else if (arcType != EQ || isRealType) sourceDependency = DomainChangeType.RANGE;
        else sourceDependency = DomainChangeType.DOMAIN;
    }

    /**
     * Returns true if constraint is binary
     */
    public boolean isBinary() {
        return true;
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        targetExprToString(buf);
        
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
     * Updates buffer with target expression
     */
    protected void targetExprToString(StringBuffer buf) {
        buf.append("Z(");
        buf.append(z);
        buf.append(") ");
    }
    
    /**
     * Updates buffer with expression representing arc
     */
    protected void exprToString(StringBuffer buf) {
        if (x == null) {
            buf.append(" x(");
            buf.append(xconst);
            buf.append(")");
        }
        else {
            buf.append(" X(");
            buf.append(x);
            buf.append(")");
        }
        
        buf.append(" ");
        buf.append(NumOperation.operationSymbol(operation));
        buf.append(" ");
        
        if (y == null) {
            buf.append("y(");
            buf.append(yconst);
            buf.append(")");
        }
        else {
            buf.append("Y(");
            buf.append(y);
            buf.append(")");
        }
    }

    public final void propagate() throws PropagationFailureException {
        propagateBounds();
        
        // Only need to enumerate over individual values for equality
        // constraint when performing arc consitency on a finite domain
        if (arcType == EQ && strength>=CspAlgorithmStrength.ARC_CONSISTENCY && !isRealType) {
            if (useDeltas)
                propagateEqArcConsistent();
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
    protected abstract void propagateEqArcConsistent() throws PropagationFailureException;

    /**
     * Called if arc consistency strength is required to ensure an equality constraint enforces
     * this level of filtering if domain deltas are to be used
     */
    protected abstract void propagateEqArcConsistentNoDeltas() throws PropagationFailureException;
}
