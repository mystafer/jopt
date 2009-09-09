package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for arcs that restrict numbers to a range 
 */
public abstract class GenericNumRangeArc extends GenericNumArc implements NumArc {
    protected double precision;
    protected boolean minExclusive;
    protected boolean maxExclusive;

    
    /**
     * Constructor
     *
     * @param   sourceMin       Source node representing the minimum bound in equation
     * @param   minExclusive    True if minimum of range is strictly greater than sourceMin
     * @param   sourceMax       Source node representing the maximum bound in equation
     * @param   maxExclusive    True if maximum of range is strictly less than sourceMax
     * @param   target          Target node in equation
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, Number sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumConstant sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(Number sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumConstant sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, NumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(GenericNumNode sourceMin, boolean minExclusive, NumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
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
     * @param   precision       Precision required for floating point values
     */
    public GenericNumRangeArc(NumNode sourceMin, boolean minExclusive, GenericNumNode sourceMax, boolean maxExclusive, GenericNumNode target, double precision) {
        super(sourceMin, sourceMax, target, target.getNumberType(), 0);
        this.minExclusive = minExclusive;
        this.maxExclusive = maxExclusive;
        this.precision = precision;
        this.sourceDependencies = new int[]{DomainChangeType.DOMAIN,DomainChangeType.DOMAIN};
    }
    
    
    public final void propagate() throws PropagationFailureException {
        propagate(null);
    }
}
