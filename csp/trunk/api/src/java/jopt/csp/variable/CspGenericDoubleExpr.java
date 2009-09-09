/*
 * CspGenericExpr.java
 * 
 * Created on Apr 20, 2005
 */
package jopt.csp.variable;



/**
 * Interface for generic double expressions
 */
public interface CspGenericDoubleExpr extends CspGenericDoubleCast, CspDoubleExpr {
	/**
	 * Returns a numeric expression from the internal array
	 * 
	 * @param offset  Offset of expression in internal expression array
	 * @return	double expression indexed at offset
	 */
	public CspDoubleExpr getExpression(int offset);

	/**
	 * Returns the largest maximal value of all variables in the internal array
	 * @return	double value of the largest maximum
	 */
	public double getLargestMax();

	/**
	 * Returns the largest maximal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	index for the beginning of the range
	 * @param	end		index for the end of the range
	 * @return	double value of the largest maximum in range
	 */
	public double getLargestMax(int start, int end);

	/**
	 * Returns the smallest maximal value of all variables in the internal array
	 * @return	double value of the smallest maximum
	 */
	public double getSmallestMax();

	/**
	 * Returns the smallest maximal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	index for the beginning of the range
	 * @param	end		index for the end of the range
	 * @return	double value of the smallest maximum in range
	 */
	public double getSmallestMax(int start, int end);

	/**
	 * Returns the largest minimal value of all variables in the internal array
	 * @return	double value of the largest minimum in range
	 */
	public double getLargestMin();

	/**
	 * Returns the largest minimal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	index for the beginning of the range
	 * @param	end		index for the end of the range
	 * @return	double value of the largest minimum in range
	 */
	public double getLargestMin(int start, int end);

	/**
	 * Returns the smallest minimal value of all variables in the internal array
	 * @return	double value of the smallest minimum in range
	 */
	public double getSmallestMin();

	/**
	 * Returns the smallest minimal value of all variables in the internal array within
	 * start and end indices
	 * @param	start	index for the beginning of the range
	 * @param	end		index for the end of the range
	 * @return	double value of the largest minimum in range
	 */
	public double getSmallestMin(int start, int end);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	d	generic constant to add to each value
	 * @return	a generic double expression represening this+d
	 */
	public CspGenericDoubleExpr add(CspGenericDoubleConstant d);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * @param	d	generic constant to subtract from each value
	 * @return	a generic double expression represening this-d
	 */
	public CspGenericDoubleExpr subtract(CspGenericDoubleConstant d);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	d	generic constant to multiply each value by
	 * @return	a generic double expression represening this*d
	 */
	public CspGenericDoubleExpr multiply(CspGenericDoubleConstant d);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * @param	d	generic constant to divide each value by
	 * @return	a generic double expression represening this/d
	 */
	public CspGenericDoubleExpr divide(CspGenericDoubleConstant d);
	
	/**
	 * Returns a constraint restricting this expression to a value
	 * @param	val		val to constraint this to
	 * @return	constraint representing this = val
	 */
	public CspConstraint eq(CspGenericDoubleConstant val);

	/**
	 * Returns a constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		val to constraint this to
	 * @return	constraint representing this <= val
	 */
	public CspConstraint leq(CspGenericDoubleConstant val);

	/**
	 * Returns a constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		val to constraint this to
	 * @return	constraint representing this >= val
	 */
	public CspConstraint geq(CspGenericDoubleConstant val);
	
    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(double min, boolean minExclusive, double max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(double min, double max);
    
    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * from a min to max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max          start of values that this expression may not contain
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(double min, boolean minExclusive, double max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(double min, double max);
}