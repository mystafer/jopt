/*
 * CspAlgorithmStrength.java
 * 
 * Created on Jul 13, 2005
 */
package jopt.csp.variable;

/**
 * Enumeration of values that indicate the strength of a particular algorithm.
 * 
 * Hyper arc consistency is the most restrictive filtering strength.  This will
 * attempt to make all values in all domains consistent for binary and hyper
 * constraints. This is the most intensive constraint and takes the most time 
 * to execute.  The strengths of generic constraints are unique to each constraint
 * and are controlled independently of the algorithm.
 * 
 * Arc consistency differs from hyper arc consistency in that only binary
 * arcs will attempt to make all values within a domain consistent. This
 * filtering strength is sometimes referred to as domain consistency. 
 * 
 * Range consistency is less restrictive than arc consistency and ensures
 * the min and max values of each interval within the domain are consistent.
 * 
 * Bounds consistency is a relaxation of range consistency.  The only
 * values that must be consistent are the minimum and maximum values
 * of the domain.
 * 
 * Note: not all domains and constraints may support filtering to a range
 *       or arc consistency strength. A good example is constraints on
 *       domains with real numbers which may contain an infinite number 
 *       of values that cannot be filtered to arc consistency.  In this
 *       case, bounds consistency may be the best that is achieved.  The
 *       strength requirement indicates the maximum filtering level that
 *       is supported by the algorithm. 
 */
public interface CspAlgorithmStrength {
    
    /**
     * Hyper arc consistency is the most restrictive filtering strength.  This will
     * attempt to make all values in all domains consistent for binary and hyper
     * constraints. This is the most intensive constraint and takes the most time 
     * to execute.  The strengths of generic constraints are unique to each constraint
     * and are controlled independently of the algorithm.
     */
    public final static int HYPER_ARC_CONSISTENCY   = 3;
	
    /**
	 * Arc consistency differs from hyper arc consistency in that only binary
	 * arcs will attempt to make all values within a domain consistent. This
	 * filtering strength is sometimes referred to as domain consistency. 
	 */
    public final static int ARC_CONSISTENCY         = 2;
    
    /**
     * Range consistency is less restrictive than arc consistency and ensures
     * the min and max values of intervals within the domain are consistent.
     */
    public final static int RANGE_CONSISTENCY       = 1;
    
    /**
     * Bounds consistency is a relaxation of range consistency.  The only
     * values that must be consistent are the minimum and maximum values
     * of the domain.
     */
    public final static int BOUNDS_CONSISTENCY      = 0;
}
