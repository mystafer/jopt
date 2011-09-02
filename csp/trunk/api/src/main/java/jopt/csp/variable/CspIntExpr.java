package jopt.csp.variable;


/**
 * Interface implemented by integer based numeric expressions.  
 * The domain of this type of object is computed and changes to it will have no
 * effect.
 */
public interface CspIntExpr extends CspNumExpr {
	/**
	 * Returns the minimal value of the expression
	 * @return the minimum value of this expression's domain
	 */
	public int getMin();

	/**
	 * Returns the maximum value of the expression
	 * @return the maximum value of this expression's domain
	 */
	public int getMax();

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	i	value to add to this expression
	 * @return	expression representig this expression + i
	 */
	public CspIntExpr add(int i);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	l	value to add to this expression
	 * @return	expression representig this expression + l
	 */
	public CspLongExpr add(long l);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	f	value to add to this expression
	 * @return	expression representig this expression + f
	 */
	public CspFloatExpr add(float f);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	d	value to add to this expression
	 * @return	expression representig this expression + d
	 */
	public CspDoubleExpr add(double d);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param	expr	expression to add to this expression
	 * @return	expression representig this expression + expr
	 */
	public CspIntExpr add(CspIntExpr expr);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param	expr	expression to add to this expression
	 * @return	expression representig this expression + expr
	 */
	public CspLongExpr add(CspLongExpr expr);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param	expr	expression to add to this expression
	 * @return	expression representig this expression + expr
	 */
	public CspFloatExpr add(CspFloatExpr expr);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param	expr	expression to add to this expression
	 * @return	expression representig this expression + expr
	 */
	public CspDoubleExpr add(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	i 	value to subtract from this expression
	 * @return	expression representig this expression - i
	 */
	public CspIntExpr subtract(int i);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	l 	value to subtract from this expression
	 * @return	expression representig this expression - l
	 */
	public CspLongExpr subtract(long l);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	f 	value to subtract from this expression
	 * @return	expression representig this expression - f
	 */
	public CspFloatExpr subtract(float f);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	d 	value to subtract from this expression
	 * @return	expression representig this expression - d
	 */
	public CspDoubleExpr subtract(double d);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	expression to subtract from this expression
	 * @return	expression representig this expression - expr
	 */
	public CspIntExpr subtract(CspIntExpr expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	expression to subtract from this expression
	 * @return	expression representig this expression - expr
	 */
	public CspLongExpr subtract(CspLongExpr expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	expression to subtract from this expression
	 * @return	expression representig this expression - expr
	 */
	public CspFloatExpr subtract(CspFloatExpr expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	expression to subtract from this expression
	 * @return	expression representig this expression - expr
	 */
	public CspDoubleExpr subtract(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	i value to multiply this expression by
	 * @return	expression representig this expression * i
	 */
	public CspIntExpr multiply(int i);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	l value to multiply this expression by
	 * @return	expression representig this expression * l
	 */
	public CspLongExpr multiply(long l);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	f value to multiply this expression by
	 * @return	expression representig this expression * f
	 */
	public CspFloatExpr multiply(float f);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	d value to multiply this expression by
	 * @return	expression representig this expression * d
	 */
	public CspDoubleExpr multiply(double d);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	expression to multiply this expression by
	 * @return	expression representig this expression * expr
	 */
	public CspIntExpr multiply(CspIntExpr expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	expression to multiply this expression by
	 * @return	expression representig this expression * expr
	 */
	public CspLongExpr multiply(CspLongExpr expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	expression to multiply this expression by
	 * @return	expression representig this expression * expr
	 */
	public CspFloatExpr multiply(CspFloatExpr expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	expression to multiply this expression by
	 * @return	expression representig this expression * expr
	 */
	public CspDoubleExpr multiply(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	i	value to divide this expression by
	 * @return	expression representig this expression / i
	 */
	public CspIntExpr divide(int i);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	l	value to divide this expression by
	 * @return	expression representig this expression / l
	 */
	public CspLongExpr divide(long l);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	f	value to divide this expression by
	 * @return	expression representig this expression / f
	 */
	public CspFloatExpr divide(float f);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	d	value to divide this expression by
	 * @return	expression representig this expression / d
	 */
	public CspDoubleExpr divide(double d);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * 
	 * @param	expr	expression to divide this expression by
	 * @return	expression representig this expression / expr
	 */
	public CspIntExpr divide(CspIntExpr expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param	expr	expression to divide this expression by
	 * @return	expression representig this expression / expr
	 */
	public CspLongExpr divide(CspLongExpr expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param	expr	expression to divide this expression by
	 * @return	expression representig this expression / expr
	 */
	public CspFloatExpr divide(CspFloatExpr expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param	expr	expression to divide this expression by
	 * @return	expression representig this expression / expr
	 */
	public CspDoubleExpr divide(CspDoubleExpr expr);

	/**
	 * Returns constraint restricting this expression to a value
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be equal to val
	 */
	public CspConstraint eq(int val);

	/**
	 * Returns constraint restricting this expression to values below
	 * a given maximum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be less than val
	 */
	public CspConstraint lt(int val);

	/**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * 
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be less than or equal to val
	 */
	public CspConstraint leq(int val);

	/**
	 * Returns constraint restricting this expression to values above
	 * a given minimum
	 * 
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be greater than val
	 */
	public CspConstraint gt(int val);

	/**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be greater than or equal to val
	 */
	public CspConstraint geq(int val);

	/**
	 * Returns constraint restricting this expression to all values
	 * not equivalent to supplied value
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be not equal to val
	 */
	public CspConstraint neq(int val);
	
	/**
	 * Returns constraint restricting this expression to a value
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be equal to val
	 */
	public CspConstraint eq(CspGenericIntConstant val);

	/**
	 * Returns constraint restricting this expression to values below
	 * a given maximum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be less than val
	 */
	public CspConstraint lt(CspGenericIntConstant val);

	/**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be less than or equal to val
	 */
	public CspConstraint leq(CspGenericIntConstant val);

	/**
	 * Returns constraint restricting this expression to values above
	 * a given minimum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be greater than val
	 */
	public CspConstraint gt(CspGenericIntConstant val);

	/**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be greater than or equal to val
	 */
	public CspConstraint geq(CspGenericIntConstant val);

	/**
	 * Returns constraint restricting this expression to all values
	 * not equivalent to supplied value
	 * @param	val		value to constraint this expression to
	 * @return  constraint constraining this expression to be not equal to val
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