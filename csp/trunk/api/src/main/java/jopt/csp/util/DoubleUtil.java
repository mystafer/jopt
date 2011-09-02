package jopt.csp.util;

import jopt.csp.solution.DoubleSolution;
import jopt.csp.solution.FloatSolution;
import jopt.csp.solution.IntSolution;
import jopt.csp.solution.LongSolution;
import jopt.csp.solution.VariableSolution;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspFloatExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspLongExpr;
import jopt.csp.variable.CspNumExpr;



/**
 * Double type utility functions
 */
public class DoubleUtil {
	public final static double DEFAULT_PRECISION    = 2.10e-11d;
	
	private final static double SMALLEST_NEGATIVE  = Double.longBitsToDouble(0x8000000000000001L);
	private final static double SMALLEST_POSITIVE  = Double.longBitsToDouble(0x0000000000000001L);
    private final static double SMALLEST_POS_NORMALIZED = Double.longBitsToDouble(0x0010000000000000L);
    private final static double SMALLEST_NEG_NORMALIZED = Double.longBitsToDouble(0x8010000000000000L);
	
	/**
     * Returns the next higher (towards positive infinity) double precision floating-point value. 
	 */
	public static double next(double v) {
        if (v>SMALLEST_POS_NORMALIZED) {
            // Uses the fact that double precision numbers have a 52-bit mantissa
            // and the way doubles are rounded.  We use 1.5e-16 but any value
            // strictly between 0.5*2^-52 and 0.75*2^-52 should work, if the java
            // VM is doing IEEE 754 correctly.
            return v*(1.5e-16d)+v;
        } else if (v==Double.NEGATIVE_INFINITY) {
            // If we don't handle this, it tries to add positive
            // infinity to negative infinity and gives NaN.
            return Double.NEGATIVE_INFINITY;
        } else if (v<SMALLEST_NEG_NORMALIZED) {
            // Similar to first situation above.
            return v*(-1.5e-16d)+v;
        } else {
            // When numbers are not normalized, the distance from one to the next is
            // constant.
            return v+SMALLEST_POSITIVE;
        }
	}
	
    /**
     * Returns the next lower (towards negative infinity) double precision floating-point value. 
     */
    public static double previous(double v) {
        if (v<SMALLEST_NEG_NORMALIZED) {
            // Uses the fact that double precision numbers have a 52-bit mantissa
            // and the way doubles are rounded.  We use 1.5e-16 but any value
            // strictly between 0.5*2^-52 and 0.75*2^-52 should work, if the java
            // VM is doing IEEE 754 correctly.
            return v*(1.5e-16d)+v;
        } else if (v==Double.POSITIVE_INFINITY) {
            // If we don't handle this, it tries to add positive
            // infinity to negative infinity and gives NaN.
            return Double.POSITIVE_INFINITY;
        } else if (v>SMALLEST_POS_NORMALIZED) {
            // Similar to first situation above.
            return v*(-1.5e-16d)+v;
        } else {
            // When numbers are not normalized, the distance from one to the next is
            // constant.
            return v+SMALLEST_NEGATIVE;
        }
    }
	
	/**
	 * Returns a min or max value if it is close enough to a value for comparison based on a precision value
	 * 
	 * @param minCurrent        Minimum possible current value that will be returned if new value is close enough given scale
	 * @param maxCurrent        Maximum possible current value that will be returned if new value is close enough given scale
	 * @param newVal            Value that is to be checked to ensure it is close enough to another value
	 * @param precision     Precision values must fall within to be equal
	 */
	public static double returnMinMaxIfClose(double minCurrent, double maxCurrent, double newVal, double precision) {
		if (Double.isInfinite(newVal) || Double.isNaN(newVal) || Double.isNaN(minCurrent) || Double.isNaN(maxCurrent)) 
			return newVal;
		
		if (isEqual(minCurrent, newVal, precision))
			return minCurrent;
		
		if (isEqual(maxCurrent, newVal, precision))
			return maxCurrent;
		
		return newVal;
	}
	
	/**
	 * Compares two values using a given precision to determine if they are equal
	 * 
	 * @param val1          First value to compare       
	 * @param val2          Second value to compare
	 * @param precision     Precision values must fall within to be equal
	 */
	public static int compare(double val1, double val2, double precision) {
		if (isEqual(val1, val2, precision))
			return 0;
		else if (val1 < val2)
			return -1;
		else
			return 1;
	}
	
    /**
     * Returns true if two values are within precision of one another. Precision value should be
     * less than 0.1 or else this procedure doesn't make sense.  Negative and positive infinity
     * are considered equal to -Double.MAX_VALUE and Double.MAX_VALUE respectively.
     * 
     * @param val1          First value to compare       
     * @param val2          Second value to compare
     * @param precision     Precision values must fall within to return true.
     *                      When comparing values where at least one of them is of magnitude
     *                      less than one, this is an absolute precision: a simple difference.
     *                      When comparing values both greater than one, this is a fractional
     *                      error not an absolute plus or minus bounds. Therefore 1,000,000
     *                      and 1,000,001 are equal if precision is set to .000002.
     */
    public static boolean isEqual(double val1, double val2, double precision) {
        // Don't waste time doing calculations if this is true!
        if (val1 == val2) return true;
        
        // ensure precision is not less than minimum precision
        precision = Math.max(precision, DEFAULT_PRECISION);
        
        double mult = 1+precision;
        
        if (val1>0) {
            if (val1!=Double.POSITIVE_INFINITY) {
                return ( ((val2 <= val1*mult) || (val2 <= val1+precision)) && ((val2 >= val1/mult) || (val2 >= val1-precision)) );
            } else {
                return (val2 >= Double.MAX_VALUE/mult);
            }
        } else {
            if (val1!=Double.NEGATIVE_INFINITY) {
                return ( ((val2 >= val1*mult) || (val2 >= val1-precision)) && ((val2 <= val1/mult) || (val2 <= val1+precision)) );
            } else {
                return (val2 <= -Double.MAX_VALUE/mult);
            }
        }
    }
	
	/**
	 * Returns integer value for a number rounding using ceiling
	 * method if necessary
	 */
	public static int intCeil(double n) {
		double ceil = Math.ceil(n);
		if (ceil >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
		if (ceil <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
		return (int) ceil;
	}
	
	/**
	 * Returns integer value for a number rounding using floor
	 * method if necessary
	 */
	public static int intFloor(double n) {
		double floor = Math.floor(n);
		if (floor >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
		if (floor <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
		return (int) floor;
	}
	
	
	/**
	 * Returns true if value is equivalent to an integer value
	 */
	public static boolean isIntEquivalent(double val) {
		if (Double.isInfinite(val) || Double.isNaN(val))
			return false;
		if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE)
			return false;
		
		// Check if value is a fraction
		int i = (int) val;
		return i == val;
	}
	
	/**
	 * Returns long value for a number rounding using ceiling
	 * method if necessary
	 */
	public static long longCeil(double n) {
		double ceil = Math.ceil(n);
		if (ceil >= Long.MAX_VALUE) return Long.MAX_VALUE;
		if (ceil <= Long.MIN_VALUE) return Long.MIN_VALUE;
		return (long) ceil;
	}
	
	/**
	 * Returns long value for a number rounding using floor
	 * method if necessary
	 */
	public static long longFloor(double n) {
		double floor = Math.floor(n);
		if (floor >= Long.MAX_VALUE) return Long.MAX_VALUE;
		if (floor <= Long.MIN_VALUE) return Long.MIN_VALUE;
		return (long) floor;
	}
	
	/**
	 * Returns true if value is equivalent to a long value
	 */
	public static boolean isLongEquivalent(double val) {
		if (Double.isInfinite(val) || Double.isNaN(val))
			return false;
		if (val < Long.MIN_VALUE || val > Long.MAX_VALUE)
			return false;
		
		// Check if value is a fraction
		long i = (long) val;
		return i == val;
	}
    
    /**
     * Returns minimum value of a numeric expression as a double type
     */
    public static double getMin(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMin();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMin();
        
        if (expr instanceof CspFloatExpr)
            return ((CspFloatExpr) expr).getMin();
        
        return ((CspDoubleExpr) expr).getMin();
    }
    
    /**
     * Returns maximum value of a numeric expression as a double type
     */
    public static double getMax(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMax();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMax();
        
        if (expr instanceof CspFloatExpr)
            return ((CspFloatExpr) expr).getMax();
        
        return ((CspDoubleExpr) expr).getMax();
    }

    /**
     * Returns minimum value of a numeric expression as a double type
     */
    public static double getMin(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMin();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMin();
        
        if (sol instanceof FloatSolution)
            return ((FloatSolution) sol).getMin();
        
        return ((DoubleSolution) sol).getMin();
    }
    
    /**
     * Returns maximum value of a numeric expression as a double type
     */
    public static double getMax(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMax();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMax();
        
        if (sol instanceof FloatSolution)
            return ((FloatSolution) sol).getMax();
        
        return ((DoubleSolution) sol).getMax();
    }
}