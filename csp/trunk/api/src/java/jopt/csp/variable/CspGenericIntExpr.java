/*
 * CspGenericExpr.java
 * 
 * Created on Apr 20, 2005
 */
package jopt.csp.variable;


/**
 * Interface for generic integer expressions
 */
public interface CspGenericIntExpr extends CspGenericLongCast, CspIntExpr {
	/**
	 * Returns a numeric expression from the internal array
	 * 
	 * @param offset  Offset of expression in internal expression array
	 * @return	int expression referenced to be the index at offset
	 */
	public CspIntExpr getExpression(int offset);

	/**
	 * Returns the largest maximal value of all variables in the internal array
	 * @return int value of the largest maximum of the variables
	 */
	public int getLargestMax();

	/**
	 * Returns the largest maximal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	offset of the beginning of the range to cover
	 * @param	end		offset of the end of the range to cover
	 * @return int value of the largest maximum of the variables
	 */
	public int getLargestMax(int start, int end);

	/**
	 * Returns the smallest maximal value of all variables in the internal array
	 * @return int value of the smallest maximum of the variables
	 */
	public int getSmallestMax();

	/**
	 * Returns the smallest maximal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	offset of the beginning of the range to cover
	 * @param	end		offset of the end of the range to cover
	 * @return int value of the smallest maximum of the variables
	 */
	public int getSmallestMax(int start, int end);

	/**
	 * Returns the largest minimal value of all variables in the internal array
	 * @return int value of the largest minimum of the variables
	 */
	public int getLargestMin();

	/**
	 * Returns the largest minimal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	offset of the beginning of the range to cover
	 * @param	end		offset of the end of the range to cover
	 * @return int value of the largest minimum of the variables
	 */
	public int getLargestMin(int start, int end);

	/**
	 * Returns the smallest minimal value of all variables in the internal array
	 * @return int value of the smallest minimum of the variables
	 */
	public int getSmallestMin();

	/**
	 * Returns the smallest minimal value of all variables in the internal array within
	 * start and end offsets
	 * @param	start	offset of the beginning of the range to cover
	 * @param	end		offset of the end of the range to cover
	 * @return int value of the smallest minimum of the variables
	 */
	public int getSmallestMin(int start, int end);
	
	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	i	generic int constant representing the value to be added to this
	 * @return generic int expression representing this + i 
	 */
	public CspGenericIntExpr add(CspGenericIntConstant i);
	
	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	i	generic int expression representing the value to be added to this
	 * @return generic int expression representing this + i
	 */
	public CspGenericIntExpr add(CspGenericIntExpr i);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	l	generic long constant representing the value to be added to this
	 * @return generic long expression representing this + l 
	 */
	public CspGenericLongExpr add(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * @param	f	generic float constant representing the value to be added to this
	 * @return generic float expression representing this + f
	 */
	public CspGenericFloatExpr add(CspGenericFloatConstant f);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic double constant representing the value to be added to this
	 * @return generic double expression representing this + d
	 */
	public CspGenericDoubleExpr add(CspGenericDoubleConstant d);
	
	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * @param	i	generic int constant representing the value to be subtracted from this
	 * @return generic int expression representing this - i
	 */
	public CspGenericIntExpr subtract(CspGenericIntConstant i);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * 
	 * @param	i	generic int expression representing the value to be subtracted from this
	 * @return generic int expression representing this - i
	 */
	public CspGenericIntExpr subtract(CspGenericIntExpr i);
	
	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * @param	l	generic long constant representing the value to be subtract from this
	 * @return generic long expression representing this - l 
	 */
	public CspGenericLongExpr subtract(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * 
	 * @param	f	generic float constant representing the value to be subtracted from this
	 * @return generic float expression representing this - f
	 */
	public CspGenericFloatExpr subtract(CspGenericFloatConstant f);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static generic value
	 * 
	 * @param	d	generic double constant representing the value to be subtracted from this
	 * @return generic double expression representing this - d
	 */
	public CspGenericDoubleExpr subtract(CspGenericDoubleConstant d);
	
	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * 
	 * @param	i	generic int constant representing the value to be multiplied by this
	 * @return generic int expression representing this * i
	 */
	public CspGenericIntExpr multiply(CspGenericIntConstant i);
	
	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	i	generic int expression representing the value to be multiplied by this
	 * @return generic int expression representing this * i
	 */
	public CspGenericIntExpr multiply(CspGenericIntExpr i);


	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	l	generic long constant representing the value to be multiply by this
	 * @return generic long expression representing this * l 
	 */
	public CspGenericLongExpr multiply(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	f	generic float constant representing the value to be multiply by this
	 * @return generic float expression representing this * f 
	 */
	public CspGenericFloatExpr multiply(CspGenericFloatConstant f);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static generic value
	 * @param	d	generic double constant representing the value to be multiply by this
	 * @return generic double expression representing this * d 
	 */
	public CspGenericDoubleExpr multiply(CspGenericDoubleConstant d);
	
	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * 
	 * @param	i	generic int constant representing the value to divide this by
	 * @return generic int expression representing this / i
	 */
	public CspGenericIntExpr divide(CspGenericIntConstant i);
	
	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * 
	 * @param	i	generic int expression representing the value to divide this by
	 * @return generic int expression representing this / i
	 */
	public CspGenericIntExpr divide(CspGenericIntExpr i);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * @param	l	generic long constant representing the value to divide this by
	 * @return generic long expression representing this / l 
	 */
	public CspGenericLongExpr divide(CspGenericLongConstant l);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * @param	f	generic float constant representing the value to divid this by
	 * @return generic float expression representing this / f 
	 */
	public CspGenericFloatExpr divide(CspGenericFloatConstant f);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static generic value
	 * @param	d	generic double constant representing the value to divide this by
	 * @return generic double expression representing this / d 
	 */
	public CspGenericDoubleExpr divide(CspGenericDoubleConstant d);
	
	/**
	 * Returns a constraint restricting this expression to a value
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be equal to val
	 */
	public CspConstraint eq(CspGenericIntConstant val);

	/**
	 * Returns a constraint restricting this expression to values below
	 * a given maximum
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be less than val
	 */
	public CspConstraint lt(CspGenericIntConstant val);

	/**
	 * Returns a constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be less than or equal to val
	 */
	public CspConstraint leq(CspGenericIntConstant val);

	/**
	 * Returns a constraint restricting this expression to values above
	 * a given minimum
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be greater than val
	 */
	public CspConstraint gt(CspGenericIntConstant val);

	/**
	 * Returns a constraint restricting this expression to values above
	 * and including a given minimum
	 * 
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be greater than or equal to val
	 */
	public CspConstraint geq(CspGenericIntConstant val);

	/**
	 * Returns a constraint restricting this expression to all values
	 * not equivalent to supplied value
	 * 
	 * @param	val		value to constaint this to
	 * @return	constraint constraining this to be not equal to val
	 */
	public CspConstraint neq(CspGenericIntConstant val);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(int min, boolean minExclusive, int max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(int min, int max);
    
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
    public CspConstraint notBetween(int min, boolean minExclusive, int max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(int min, int max);
}