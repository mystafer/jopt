package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
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
 * Arc representing Z = |a|, |Z| < a, etc.
 */
public class NodeNumAbsArc extends NodeArc implements NumArc {
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
	private NumNode z;
    private Number a;
    private int nodeType;
    private int arcType;
    private boolean constAbs;
    private NumSet workingSet;

    /**
     * Constructor
     */
    public NodeNumAbsArc(Number a, NumNode z, int nodeType, int arcType, boolean constAbs) {
        super(z);
        this.z = z;
        this.a = NumberMath.toConst(a);
        this.nodeType = nodeType;
        this.arcType = arcType;
        this.constAbs = constAbs;
    }
    
    /** 
     * Attempts to reduce values in target node domain based on all values
     * in source node(s)
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
    	if (constAbs) propagateConstAbs();
    	else propagateVarAbs();
    }
    
    /** 
     * Performs propagation necessary when absolute value is taken on the
     * numeric variable
     */
    private void propagateVarAbs() throws PropagationFailureException {
    	// determine values that are being eliminated from range
    	NumberMath.abs(a, v1);
    	NumberMath.neg(v1, v2);
    	
        switch(arcType) {
            case GEQ:
                NumberMath.next(v1, z.getPrecision(), v1);
                NumberMath.previous(v2, z.getPrecision(), v2);
            	z.removeRange(v2, v1);
                break;
                
            case GT:
            	z.removeRange(v2, v1);
                break;
                
            case LEQ:
            	z.setRange(v2, v1);
                break;
                
            case LT:
                NumberMath.next(v1, z.getPrecision(), v1);
                NumberMath.previous(v2, z.getPrecision(), v2);
                z.setRange(v2, v1);
                break;
                
            case EQ:
                switch (nodeType) {
                    case INTEGER:
                        if (workingSet==null) workingSet = new IntSparseSet();
                        else workingSet.clear();
                        IntSparseSet iset = (IntSparseSet) workingSet;
                        iset.add(v1.intValue());
                        iset.add(v2.intValue());
                        z.setDomain(iset);
                        break;
                        
                    case LONG:
                        if (workingSet==null) workingSet = new LongSparseSet();
                        else workingSet.clear();
                        LongSparseSet lset = (LongSparseSet) workingSet;
                        lset.add(v1.longValue());
                        lset.add(v2.longValue());
                        z.setDomain(lset);
                        break;
                        
                    case FLOAT:
                        if (workingSet==null) workingSet = new FloatSparseSet();
                        else workingSet.clear();
                        FloatSparseSet fset = (FloatSparseSet) workingSet;
                        fset.add(v1.floatValue());
                        fset.add(v2.floatValue());
                        z.setDomain(fset);
                        break;
                        
                    default:
                        if (workingSet==null) workingSet = new DoubleSparseSet();
                        else workingSet.clear();
                        DoubleSparseSet dset = (DoubleSparseSet) workingSet;
                        dset.add(v1.doubleValue());
                        dset.add(v2.doubleValue());
                        z.setDomain(dset);
                        break;
                }
                break;
                
            case NEQ:
                z.removeValue(v1);
                z.removeValue(v2);
                break;
        }
    }

    /** 
     * Performs propagation necessary when absolute value is taken on the 
     * constant number
     */
    private void propagateConstAbs() throws PropagationFailureException {
        NumberMath.abs(a, v1);
        
        switch(arcType) {
            case GEQ:
                z.setMin(v1);
                break;
                
            case GT:
                NumberMath.next(v1, z.getPrecision(), v1);
                z.setMin(v1);
                break;
                
            case LEQ:
                z.setMax(v1);
                break;
                
            case LT:
                NumberMath.previous(v1, z.getPrecision(), v1);
                z.setMax(v1);
                break;
                
            case EQ:
                z.setValue(v1);
                break;
                
            case NEQ:
                z.removeValue(v1);
        }
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        if (!constAbs) buf.append("|");
        buf.append("Z(");
        buf.append(node);
        buf.append(")");
        if (!constAbs) buf.append("|");
        buf.append(" ");

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
        
        buf.append(" a(");
        if (constAbs) buf.append("|");
        buf.append(a);
        if (constAbs) buf.append("|");
        buf.append(")");
        
        return buf.toString();
    }
}
