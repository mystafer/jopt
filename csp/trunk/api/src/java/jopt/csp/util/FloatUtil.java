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
 * Float type utility functions
 */
public class FloatUtil {
    public final static float DEFAULT_PRECISION    = 2.10e-11f;
    
    private final static float SMALLEST_NEGATIVE    = Float.intBitsToFloat(0x80000001);
    private final static float SMALLEST_POSITIVE    = Float.intBitsToFloat(0x00000001);
    
    /**
     * Returns the next float value in sequence for a given float
     */
    public static float next(float v) {
		// Handle Infinity and NaN
		if (Float.isInfinite(v) || Float.isNaN(v))
			return v;

		// Handle Negative and Positive 0
		if (v==0) return SMALLEST_POSITIVE;
		
		// Convert double to bits
		int floatBits = Float.floatToIntBits(v);
		
		// Return next double
		if (v>0)
			return Float.intBitsToFloat(floatBits + 1);
		else
			return Float.intBitsToFloat(floatBits - 1);
    }
    
    /**
     * Returns the previous float value in sequence for a given float
     */
    public static float previous(float v) {
		// Handle Infinity and NaN
		if (Float.isInfinite(v) || Float.isNaN(v))
			return v;
		
		// Handle Negative and Positive 0
		if (v==0) return SMALLEST_NEGATIVE;
		
		// Convert double to bits
		int floatBits = Float.floatToIntBits(v);
		
		// Return previous double
		if (v>0)
			return Float.intBitsToFloat(floatBits - 1);
		else
			return Float.intBitsToFloat(floatBits + 1);
    }

    /**
     * Returns a min or max value if it close enough to a value for comparison based on a precision value
     * 
     * @param minCurrent        Minimum possible current value that will be returned if new value is close enough given scale
     * @param maxCurrent        Maximum possible current value that will be returned if new value is close enough given scale
     * @param newVal            Value that is to be checked to ensure it is close enough to another value
     * @param precision     Precision values must fall within to be equal
     */
    public static float returnMinMaxIfClose(float minCurrent, float maxCurrent, float newVal, float precision) {
        if (Float.isInfinite(newVal) || Float.isNaN(newVal) || Float.isNaN(minCurrent) || Float.isNaN(maxCurrent)) 
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
    public static int compare(float val1, float val2, float precision) {
        if (isEqual(val1, val2, precision))
            return 0;
        else if (val1 < val2)
            return -1;
        else
            return 1;
    }
    
    /**
     * Returns true if two values are within precision of one another
     * 
     * @param val1          First value to compare       
     * @param val2          Second value to compare
     * @param precision     Precision values must fall within to return true.
     */
    public static boolean isEqual(float val1, float val2, float precision) {
        if (val1 == val2) return true;
        
        // ensure precision is not less than minimum precision
        precision = Math.max(precision, DEFAULT_PRECISION);
        
        // swap infinity values with largest / smallest values
        if (val1 == Float.NEGATIVE_INFINITY)
            val1 = -Float.MAX_VALUE;
        else if (val1 == Float.POSITIVE_INFINITY)
            val1 = Float.MAX_VALUE;
        
        if (val2 == Float.NEGATIVE_INFINITY)
            val2 = -Float.MAX_VALUE;
        else if (val2 == Float.POSITIVE_INFINITY)
            val2 = Float.MAX_VALUE;
        
        // determine minimum and maximum values
        float min = Math.min(val1, val2);
        float max = Math.max(val1, val2);
        float divisor = Math.max(1, Math.abs(min));
        
        float diff = Math.max(max - min, 1);
        float p = diff / divisor;
        return p <= precision;
    }

    /**
     * Returns integer value for a number rounding using ceiling
     * method if necessary
     */
    public static int intCeil(float n) {
        double ceil = Math.ceil(n);
        if (ceil >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (ceil <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) ceil;
    }
    
    /**
     * Returns integer value for a number rounding using floor
     * method if necessary
     */
    public static int intFloor(float n) {
        double floor = Math.floor(n);
        if (floor >= Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (floor <= Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) floor;
    }
    
    
    /**
     * Returns true if value is equivalent to an integer value
     */
    public static boolean isIntEquivalent(float val) {
        if (Float.isInfinite(val) || Float.isNaN(val))
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
    public static long longCeil(float n) {
        double ceil = Math.ceil(n);
        if (ceil >= Long.MAX_VALUE) return Long.MAX_VALUE;
        if (ceil <= Long.MIN_VALUE) return Long.MIN_VALUE;
        return (long) ceil;
    }
    
    /**
     * Returns long value for a number rounding using floor
     * method if necessary
     */
    public static long longFloor(float n) {
        double floor = Math.floor(n);
        if (floor >= Long.MAX_VALUE) return Long.MAX_VALUE;
        if (floor <= Long.MIN_VALUE) return Long.MIN_VALUE;
        return (long) floor;
    }
    
    /**
     * Returns true if value is equivalent to a long value
     */
    public static boolean isLongEquivalent(float val) {
        if (Float.isInfinite(val) || Float.isNaN(val))
            return false;
        if (val < Long.MIN_VALUE || val > Long.MAX_VALUE)
            return false;
        
        // Check if value is a fraction
        long i = (long) val;
        return i == val;
    }
    
    /**
     * Returns minimum value of a numeric expression as a float type
     */
    public static float getMin(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMin();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMin();
        
        if (expr instanceof CspFloatExpr)
            return ((CspFloatExpr) expr).getMin();
        
        return (float) ((CspDoubleExpr) expr).getMin();
    }
    
    /**
     * Returns maximum value of a numeric expression as a float type
     */
    public static float getMax(CspNumExpr expr) {
        if (expr instanceof CspIntExpr)
            return ((CspIntExpr) expr).getMax();
        
        if (expr instanceof CspLongExpr)
            return ((CspLongExpr) expr).getMax();
        
        if (expr instanceof CspFloatExpr)
            return ((CspFloatExpr) expr).getMax();
        
        return (float) ((CspDoubleExpr) expr).getMax();
    }

    /**
     * Returns minimum value of a numeric expression as a double type
     */
    public static float getMin(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMin();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMin();
        
        if (sol instanceof FloatSolution)
            return ((FloatSolution) sol).getMin();
        
        return (float) ((DoubleSolution) sol).getMin();
    }
    
    /**
     * Returns maximum value of a numeric expression as a double type
     */
    public static float getMax(VariableSolution sol) {
        if (sol instanceof IntSolution)
            return ((IntSolution) sol).getMax();
        
        if (sol instanceof LongSolution)
            return ((LongSolution) sol).getMax();
        
        if (sol instanceof FloatSolution)
            return ((FloatSolution) sol).getMax();
        
        return (float) ((DoubleSolution) sol).getMax();
    }
}