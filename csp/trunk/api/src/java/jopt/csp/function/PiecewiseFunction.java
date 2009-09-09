package jopt.csp.function;

/**
 * Interface implemented by piecewise functions that may be used with some constraints.
 * 
 * @author Nick Coleman
 * @version $Revision: 1.1 $
 */
public interface PiecewiseFunction {

	/**
	 * Returns the smallest of allowed X for function
	 */
	public abstract double getIntervalMinX();

	/**
	 * Returns the largest of allowed X for function
	 */
	public abstract double getIntervalMaxX();

	/**
	 * Returns the value of Y at a given point X
	 */
	public abstract double getY(double x);

	/**
	 * Returns the minimal Y of the function over the given range of X
	 */
	public abstract double getMinY(double x1, double x2);

	/**
	 * Returns the maximal Y of the function over the given range of X
	 */
	public abstract double getMaxY(double x1, double x2);

}