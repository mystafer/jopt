/*
 * GenericNumSumArc.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Ai, Zi < Aj, Zi > Aj, etc.
 */
public class GenericNumRelationArc extends GenericNumArc implements NumArc {
    private MutableNumber work = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   a           A variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumRelationArc(Node a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   a           A constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumRelationArc(Number a, Node z, int nodeType, int arcType) {
        super(a, (Number) null, z, nodeType, arcType);
    }
    
    /**
     * Constructor
     *
     * @param   a           A generic constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumRelationArc(GenericNumConstant a, Node z, int nodeType, int arcType) {
        super(a, (GenericNumConstant) null, z, nodeType, arcType);
    }
    
    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
            switch(arcType) {
                case GEQ:
                setMinZ(getLargestMinX());
                    break;
                    
                case GT:
                NumberMath.next(getLargestMinX(), getPrecisionZ(), work);
                setMinZ(work);
                    break;
                    
                case LEQ:
                setMaxZ(getSmallestMaxX());
                    break;
                    
                case LT:
                NumberMath.previous(getSmallestMaxX(), getPrecisionZ(), work);
                setMaxZ(work);
                    break;
                    
                case EQ:
                setRangeZ(getLargestMinX(), getSmallestMaxX());
                    break;
                    
                case NEQ:
                    if (isBoundX())
                	removeValueZ(getSmallestMaxX());
        }
    }
}
