package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z is within a range [min..max] or (min..max], etc.
 */
public class GenericNumBetweenArc extends GenericNumRangeArc implements NumArc {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(Number sourceMin, boolean minExclusive, Number sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(Number sourceMin, boolean minExclusive, NumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(Number sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(Number sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, Number sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(NumNode sourceMin, boolean minExclusive, Number sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, Number sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(NumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(NumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, NumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(GenericNumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumBetweenArc(NumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean  maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, minExclusive, sourceMax, maxExclusive, target, precision);
    }

    protected void propagateCurrentNodes() throws PropagationFailureException {
        Number currentMin = currentXConst;
        Number currentMax = currentYConst;
        if ((currentMin==null)&&(currentXGenConst!=null)) {
            currentMin = currentXGenConst.getNumMax();
        }
        if ((currentMax==null)&&(currentYGenConst!=null)) {
            currentMax = currentYGenConst.getNumMin();
        }
        
        if ((currentMin==null)&&(currentNx!=null)) {
            currentMin = currentNx.getMin();
        }
        if ((currentMax==null)&&(currentNy!=null)) {
            currentMax = currentNy.getMax();
        }
        
        if ((currentMin==null)&&(currentGx!=null)) {
            currentMin = currentGx.getSmallestMin();
        }
        if ((currentMax==null)&&(currentGy!=null)) {
            currentMax = currentGy.getLargestMax();
        }
        
        // determine and max values based on exclusive flags
        if (minExclusive)
            NumberMath.next(currentMin, precision, min);
        else
            min.set(currentMin);
        
        if (maxExclusive)
            NumberMath.previous(currentMax, precision, max);
        else
            max.set(currentMax);
        
        setRangeZ(min, max);
    }


    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (nz != null) buf.append(nz);
        else buf.append(gz);
        buf.append(" is within the range ");
        
        if (minExclusive) buf.append("(");
        else buf.append("[");
        
        if (xGenConst != null) buf.append(xGenConst);
        else if (xconst != null) buf.append(xconst);
        else if (nx != null) buf.append(nx);
        else buf.append(gx);

        buf.append("...");
        buf.append(gy);

        if (xGenConst != null) buf.append(xGenConst);
        else if (xconst != null) buf.append(xconst);
        else if (nx != null) buf.append(nx);
        else buf.append(gx);

        if (maxExclusive) buf.append(")");
        else buf.append("]");
        
        return buf.toString();
    }
}
