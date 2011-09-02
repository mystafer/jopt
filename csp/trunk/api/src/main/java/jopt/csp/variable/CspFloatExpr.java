package jopt.csp.variable;


/**
 * Interface implemented by float precision floating point numeric expressions.  
 * The domain of this type of object is computed and changes to it will have no
 * effect.
 */
public interface CspFloatExpr extends CspFloatCast {
	/**
	 * Returns minimal value of expression
	 * @return minimum of this expression's domain
	 */
	public float getMin();

	/**
	 * Returns maximum value of expression
	 * @return maximum of this expression's domain
	 */
	public float getMax();

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	f	value to add to this
	 * @return	CspFloatExpr representing this+f
	 */
	public CspFloatExpr add(float f);

	/**
	 * Returns an expression representing the sum of this expression
	 * with a static value
	 * @param	d	double value to add to this
	 * @return	CspDoubleExpr representing this+d
	 */
	public CspDoubleExpr add(double d);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * @param	expr	expression to add to this
	 * @return	CspFloatExpr representing this+expr
	 */
	public CspFloatExpr add(CspFloatCast expr);

	/**
	 * Returns an expression representing the sum of this expression
	 * with another expression
	 * 
	 * @param	expr	double expression to add to this
	 * @return	CspDoubleExpr representing this+expr
	 */
	public CspDoubleExpr add(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	f	float to subract from this
	 * @return	CspFloatExpr representing this - f
	 */
	public CspFloatExpr subtract(float f);

	/**
	 * Returns an expression representing the difference of this expression
	 * with a static value
	 * @param	d	double to subract from this
	 * @return	CspDoubleExpr representing this - d
	 */
	public CspDoubleExpr subtract(double d);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	float expression to subract from this
	 * @return	CspFloatExpr representing this - expr
	 */
	public CspFloatExpr subtract(CspFloatCast expr);

	/**
	 * Returns an expression representing the difference of this expression
	 * with another expression
	 * @param	expr	double expression to subract from this
	 * @return	CspDoubleExpr representing this - expr
	 */
	public CspDoubleExpr subtract(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	f	float to multiply by this
	 * @return	CspFloatExpr representing this * f
	 */
	public CspFloatExpr multiply(float f);

	/**
	 * Returns an expression representing the product of this expression
	 * with a static value
	 * @param	d	double to multiply by this
	 * @return	CspDoubleExpr representing this * d
	 */
	public CspDoubleExpr multiply(double d);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	float expression to multiply by this
	 * @return	CspFloatExpr representing this * expr
	 */
	public CspFloatExpr multiply(CspFloatCast expr);

	/**
	 * Returns an expression representing the product of this expression
	 * with another expression
	 * @param	expr	double expression to multiply by this
	 * @return	CspDoubleExpr representing this * expr
	 */
	public CspDoubleExpr multiply(CspDoubleExpr expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	f	float value to divide by this
	 * @return	CspFloatExpr representing this / f
	 */
	public CspFloatExpr divide(float f);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with a static value
	 * @param	d	double to divide by this
	 * @return	CspDoubleExpr representing this / d
	 */
	public CspDoubleExpr divide(double d);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param	expr	float expression to divide by this
	 * @return	CspFloatExpr representing this / expr
	 */
	public CspFloatExpr divide(CspFloatCast expr);

	/**
	 * Returns an expression representing the quotient of this expression
	 * with another expression
	 * @param	expr	double expression to divide by this
	 * @return	CspDoubleExpr representing this / expr
	 */
	public CspDoubleExpr divide(CspDoubleExpr expr);

	/**
	 * Returns constraint restricting this expression to a value
	 * @param	val		float to constrain the domain to
	 * @return	CspConstraint constraining this to val
	 */
	public CspConstraint eq(float val);

	/**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		float to constrain the domain to be less than or equal to
	 * @return	CspConstraint constraining this to be less than or equal to val
	 */
	public CspConstraint leq(float val);

	/**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		float to constrain the domain to be greater than or equal to
	 * @return	CspConstraint constraining this to be greater than or equal to val
	 */
	public CspConstraint geq(float val);
	
	/**
	 * Returns constraint restricting this expression to a value
	 * @param	val		generic float to constrain the domain to
	 * @return	CspConstraint constraining this to val
	 */
	public CspConstraint eq(CspGenericFloatConstant val);

	/**
	 * Returns constraint restricting this expression to values below
	 * and including a given maximum
	 * @param	val		generic float to constrain the domain to be less than or equal to
	 * @return	CspConstraint constraining this to be less than or equal to val
	 */
	public CspConstraint leq(CspGenericFloatConstant val);

	/**
	 * Returns constraint restricting this expression to values above
	 * and including a given minimum
	 * @param	val		generic float to constrain the domain to be greater than or equal to
	 * @return	CspConstraint constraining this to be greater than or equal to val
	 */
	public CspConstraint geq(CspGenericFloatConstant val);

    /**
     * Returns a constraint restricting this expression to be between a min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  minExclusive true if start of range does not include minimum value
     * @param  max  value that this expression must be less than
     * @param  maxExclusive true if end of range does not include maximum value 
     * @return constraint restricting this expression to be between a min and max.
     */
    public CspConstraint between(float min, boolean minExclusive, float max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to be  between or equal
     * min and max.
     * 
     * @param  min  value that this expression must be greater than
     * @param  max  value that this expression must be less than
     * @return constraint restricting this expression to be between or equal to min and max
     */
    public CspConstraint between(float min, float max);
    
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
    public CspConstraint notBetween(float min, boolean minExclusive, float max, boolean maxExclusive);
    
    /**
     * Returns a constraint restricting this expression to not fall within a given range
     * greater than or equal to a min value up to less than or equal a max value
     * 
     * @param  min          start of values that this expression may not contain
     * @param  max          start of values that this expression may not contain
     * @return constraint restricting this expression to not fall within a given range
     */
    public CspConstraint notBetween(float min, float max);
    
    /**
     * Sets precision associated with this expression
     * @param	p	double representing the precision associated with this expression
     */
    public void setPrecision(double p);
    
    /**
     * Returns precision associated with this expression
     * @return	precision associated with this expression
     */
    public double getPrecision();
}