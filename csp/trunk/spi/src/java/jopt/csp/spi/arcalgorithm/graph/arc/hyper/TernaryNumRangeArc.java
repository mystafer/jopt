package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for arcs that restrict numbers to a range 
 */
public abstract class TernaryNumRangeArc extends HyperArc implements NumArc {
    protected Number constMax;
    protected Number constMin;
    protected NumNode sourceMin;
    protected boolean minExclusive;
    protected NumNode sourceMax;
    protected boolean maxExclusive;
    protected NumNode rangeTarget;
    protected int nodeType;
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum bound in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum bound in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumRangeArc(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive,
            NumNode target, int nodeType) 
    {
        super(new Node[]{target}, target);
        this.constMin = NumberMath.toConst(sourceMin);
        this.minExclusive = minExclusive;
        this.constMax = NumberMath.toConst(sourceMax);
        this.maxExclusive = minExclusive;
        this.rangeTarget = target;
        this.nodeType = nodeType;
        this.sourceDependencies = new int[]{DomainChangeType.DOMAIN,DomainChangeType.DOMAIN};
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum bound in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum bound in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumRangeArc(NumNode sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, 
            NumNode target, int nodeType) 
    {
        super(new Node[]{sourceMin}, target);
        this.sourceMin = sourceMin;
        this.minExclusive = minExclusive;
        this.constMax = NumberMath.toConst(sourceMax);
        this.maxExclusive = minExclusive;
        this.rangeTarget = target;
        this.nodeType = nodeType;
        this.sourceDependencies = new int[]{DomainChangeType.DOMAIN,DomainChangeType.DOMAIN};
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum bound in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum bound in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumRangeArc(Number sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, 
            NumNode target, int nodeType) 
    {
        super(new Node[]{sourceMax}, target);
        this.constMin = NumberMath.toConst(sourceMin);
        this.minExclusive = minExclusive;
        this.sourceMax = sourceMax;
        this.maxExclusive = minExclusive;
        this.rangeTarget = target;
        this.nodeType = nodeType;
        this.sourceDependencies = new int[]{DomainChangeType.DOMAIN,DomainChangeType.DOMAIN};
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum bound in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum bound in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumRangeArc(NumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive,
            NumNode target, int nodeType) 
    {
        super(new Node[]{sourceMin, sourceMax}, target);
        this.sourceMin = sourceMin;
        this.minExclusive = minExclusive;
        this.sourceMax = sourceMax;
        this.maxExclusive = minExclusive;
        this.rangeTarget = target;
        this.nodeType = nodeType;
        this.sourceDependencies = new int[]{DomainChangeType.DOMAIN,DomainChangeType.DOMAIN};
    }
    
    
    public final void propagate() throws PropagationFailureException {
        propagate(null);
    }
    
    public final void propagate(Node src) throws PropagationFailureException {
        propagateBounds();
    }

    protected abstract void propagateBounds() throws PropagationFailureException;
}
