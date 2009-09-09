package jopt.mp.spi;

/**
 * Double type utility functions
 */
public class DoubleUtil {
	public final static double DEFAULT_PRECISION    = 2.10e-11d;
	public final static double INFINITY	= 1e20d;
	
	private final static double SMALLEST_NEGATIVE  = Double.longBitsToDouble(0x8000000000000001L);
	private final static double SMALLEST_POSITIVE  = Double.longBitsToDouble(0x0000000000000001L);
	
	/**
	 * Returns the next double value in sequence for a given double
	 */
	public static double next(double v) {
		// Handle Infinity and NaN
		if (Double.isInfinite(v) || Double.isNaN(v))
			return v;

		// Handle Negative and Positive 0
		if (v==0) return SMALLEST_POSITIVE;
		
		// Convert double to bits
		long doubleBits = Double.doubleToLongBits(v);
		
		// Return next double
		if (v>0)
			return Double.longBitsToDouble(doubleBits + 1);
		else
			return Double.longBitsToDouble(doubleBits - 1);
	}
	
	/**
	 * Returns the previous double value in sequence for a given double
	 */
	public static double previous(double v) {
		// Handle Infinity and NaN
		if (Double.isInfinite(v) || Double.isNaN(v))
			return v;
		
		// Handle Negative and Positive 0
		if (v==0) return SMALLEST_NEGATIVE;
		
		// Convert double to bits
		long doubleBits = Double.doubleToLongBits(v);
		
		// Return previous double
		if (v>0)
			return Double.longBitsToDouble(doubleBits - 1);
		else
			return Double.longBitsToDouble(doubleBits + 1);
	}
	
	/**
	 * Returns a min or max value if it close enough to a value for comparison based on a precision value
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
	 * Returns true if two values are within precision of one another
	 * 
	 * @param val1          First value to compare       
	 * @param val2          Second value to compare
	 * @param precision     Precision values must fall within to return true.
	 */
	public static boolean isEqual(double val1, double val2, double precision) {
		if (val1 == val2) return true;
		
		// ensure precision is not less than minimum precision
		precision = Math.max(precision, DEFAULT_PRECISION);
		
		// swap infinity values with largest / smallest values
		if (val1 == Double.NEGATIVE_INFINITY)
			val1 = -Double.MAX_VALUE;
		else if (val1 == Double.POSITIVE_INFINITY)
			val1 = Double.MAX_VALUE;
		
		if (val2 == Double.NEGATIVE_INFINITY)
			val2 = -Double.MAX_VALUE;
		else if (val2 == Double.POSITIVE_INFINITY)
			val2 = Double.MAX_VALUE;
		
		// determine minimum and maximum values
		double min = Math.min(val1, val2);
		double max = Math.max(val1, val2);
		double divisor = Math.max(1, Math.abs(min));
		
		double diff = max - min;
		double p = diff / divisor;
		return p <= precision;
	}
}