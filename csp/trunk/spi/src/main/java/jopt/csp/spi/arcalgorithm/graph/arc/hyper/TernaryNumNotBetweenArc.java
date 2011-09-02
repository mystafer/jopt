package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z is outside of a range [min..max] or (min..max], etc.
 */
public class TernaryNumNotBetweenArc extends TernaryNumRangeArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node providing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node providing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumNotBetweenArc(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, 
            NumNode target, int nodeType) 
    {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, nodeType);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node providing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node providing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumNotBetweenArc(Number sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive,
            NumNode target, int nodeType) 
    {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, nodeType);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node providing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node providing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumNotBetweenArc(NumNode sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive,
            NumNode target, int nodeType) 
    {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, nodeType);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node providing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node providing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   nodeType        Node variable type (Integer, Long, Float, Decimal)
     */
    public TernaryNumNotBetweenArc(NumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive,
            NumNode target, int nodeType) 
    {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, nodeType);
    }
        

    protected void propagateBounds() throws PropagationFailureException {
        // determine current min and max values for source
        min.set((constMin!=null) ? constMin : sourceMin.getMin());
        max.set((constMax!=null) ? constMax : sourceMax.getMax());
        
        // adjust min and max for exclusive ends
        if (minExclusive)
            NumberMath.next(min, rangeTarget.getPrecision(), min);
        if (maxExclusive)
            NumberMath.previous(max, rangeTarget.getPrecision(), max);
        
        rangeTarget.removeRange(min, max);
    }


    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(target.toString());
        buf.append(" is outside of the range ");
        
        if (minExclusive) buf.append("(");
        else buf.append("[");
        
        buf.append(sourceMin);
        buf.append("...");
        buf.append(sourceMax);

        if (maxExclusive) buf.append(")");
        else buf.append("]");
        
        return buf.toString();
    }
}
