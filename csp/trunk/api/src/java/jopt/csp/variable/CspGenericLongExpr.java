package jopt.csp.variable;


/**
 * Interface for generic float expressions
 */
public interface CspGenericLongExpr extends CspGenericLongCast, CspLongExpr {
	/**
	 * Returns a numeric expression from the internal array
	 * 
	 * @param offset  Offset of expression in internal expression array
	 * @return	long expression representing the expression at index offset
	 */
	public CspLongCast getExpression(int offset);

	/**
	 * Returns the largest maximal value of all variables in the internal array
	 * @return 	long value representing the largest maximum of all the long expressions
	 */
	public long getLargestMax();

	/**
	 * Returns the largest maximal value of all variables in the internal array within
	 * start and end offsets
	 * 
	 * @param	start	offset of the beginning of the range to calculate over
	 * @param	end		offset of the end of the range to calculate over
	 * @return 	long value representing the largest maximum of all the long expressions
	 * 			between start and end	
	 */
	public long getLargestMax(int start, int end);

	/**
	 * Returns the smallest maximal value of all variables in the internal array
	 * @return 	long value representing the smallest maximum of all the long expressions
	 */
	public long getSmallestMax();

	/**
	 * Returns the smallest maximal value of all variables in the internal array within
	 * start and end offsets
	 * 
	 * @param	start	offset of the beginning of the range to calculate over
	 * @param	end		offset of the end of the range to calculate over
	 * @return 	long value representing the smallest maximum of all the long expressions
	 * 			between start and end
	 */
	public long getSmallestMax(int start, int end);

	/**
	 * Returns the largest minimal value of all variables in the internal array
	 * @return 	long value representing the largest minimum of all the long expressions
	 */
	public long getLargestMin();

	/**
	 * Returns the largest minimal value of all variables in the internal array within
	 * start and end offsets
	 * 
	 * @param	start	offset of the beginning of the range to calculate over
	 * @param	end		offset of the end of the range to calculate over
	 * @return 	long value representing the largest minimum of all the long expressions
	 * 			between start and end
	 */
	public long getLargestMin(int start, int end);

	/**
	 * Returns the smallest minimal value of all variables in the internal array
	 * @return 	long value representing the smallest minimum of all the long expressions
	 */
	public long getSmallestMin();

	/**
	 * Returns the smallest minimal value of all variables in the internal array within
	 * start and end offsets
	 * 
	 * @param	start	offset of the beginning of the range to calculate over
	 * @param	end		offset of the end of the range to calculate over
	 * @return 	long value representing the smallest minimum of all the long expressions 
	 * 			between start and end
	 */
	public long getSmallestMin(int start, int end);
	

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	l	generic long constant to add to this expression
	 * @return	generic long expression representing this+l
	 */
	public CspGenericLongExpr add(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * @param	l	generic long constant to subtract from this expression
	 * @return	generic long expression representing this-l
	 */
	public CspGenericLongExpr subtract(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	l	generic long constant to multiply by this expression
	 * @return	generic long expression representing this*l
	 */
	public CspGenericLongExpr multiply(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * @param	l	generic long constant to divide this expression by
	 * @return	generic long expression representing this/l
	 */
	public CspGenericLongExpr divide(CspGenericLongConstant l);
	
	/**
	 * Returns a constraint restricting this expression to a value
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be equal to val
	 */
	public CspConstraint eq(CspGenericLongConstant val);

	/**
	 * Returns a constraint restricting this expression to values below
	 * a given maximum
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be less than val
	 */
	public CspConstraint lt(CspGenericLongConstant val);

	/**
	 * Returns a constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be less than or equal to val
	 */
	public CspConstraint leq(CspGenericLongConstant val);

	/**
	 * Returns a constraint restricting this expression to values above
	 * a given minimum
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be greater than val
	 */
	public CspConstraint gt(CspGenericLongConstant val);

	/**
	 * Returns a constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be equal to be greater than val
	 */
	public CspConstraint geq(CspGenericLongConstant val);

	/**
	 * Returns a constraint restricting this expression to all values
	 * not equivalent to supplied value
	 * @param	val		value to constrain this expression to
	 * @return	constraint constraining this to be not equal to val
	 */
	public CspConstraint neq(CspGenericLongConstant val);
	
    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(long min, boolean minExclusive, long max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(long min, long max);
    
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
    public CspConstraint notBetween(long min, boolean minExclusive, long max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(long min, long max);
}