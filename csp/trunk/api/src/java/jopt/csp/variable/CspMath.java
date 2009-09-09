package jopt.csp.variable;


/**
 * Interface for objects used to create advanced mathematical expressions
 */
public interface CspMath {

    /**
     * Creates integer expression representing a+b
     * @param   a       constant to be added
     * @param   bexpr   expression to be added to
     * @return          expression representing a + bexpr
     */
    public CspIntExpr add(int a, CspIntExpr bexpr);

    /**
     * Creates integer expression representing a+b
     * @param	a		constant to be added
     * @param	bexpr	expression to be added to
     * @return			expression representing a + bexpr
     */
    public CspLongExpr add(int a, CspLongExpr bexpr);

    /**
     * Creates integer expression representing a+b
     * @param	a		constant to be added
     * @param	bexpr	expression to be added to
     * @return			expression representing a + bexpr
     */
    public CspFloatExpr add(int a, CspFloatExpr bexpr);

    /**
     * Creates integer expression representing a+b
     * @param	a		constant to be added
     * @param	bexpr	expression to be added to
     * @return			expression representing a + bexpr
     */
    public CspDoubleExpr add(int a, CspDoubleExpr bexpr);

    /**
     * Creates integer expression representing a-b
     * @param	a		constant to start with
     * @param	bexpr	expression to be subtracted from constant
     * @return			expression representing a - bexpr
     */
    public CspIntExpr subtract(int a, CspIntExpr bexpr);

    /**
     * Creates integer expression representing a-b
     * @param	a		constant to start with
     * @param	bexpr	expression to be subtracted from constant
     * @return			expression representing a - bexpr
     */
    public CspLongExpr subtract(int a, CspLongExpr bexpr);

    /**
     * Creates integer expression representing a-b
     * @param	a		constant to start with
     * @param	bexpr	expression to be subtracted from constant
     * @return			expression representing a - bexpr
     */
    public CspFloatExpr subtract(int a, CspFloatExpr bexpr);

    /**
     * Creates integer expression representing a-b
     * @param	a		constant to start with
     * @param	bexpr	expression to be subtracted from constant
     * @return			expression representing a - bexpr
     */
    public CspDoubleExpr subtract(int a, CspDoubleExpr bexpr);

    /**
     * Creates integer expression representing a*b
     * @param	a		constant valued factor of this multiplication
     * @param	bexpr	expression factor of this multiplication
     * @return			expression representing a * bexpr
     */
    public CspIntExpr multiply(int a, CspIntExpr bexpr);

    /**
     * Creates long integer expression representing a*b
     * @param	a		constant valued factor of this multiplication
     * @param	bexpr	expression factor of this multiplication
     * @return			expression representing a * bexpr
     */
    public CspLongExpr multiply(int a, CspLongExpr bexpr);

    /**
     * Creates float expression representing a*b
     * @param	a		constant valued factor of this multiplication
     * @param	bexpr	expression factor of this multiplication
     * @return			expression representing a * bexpr
     */
    public CspFloatExpr multiply(int a, CspFloatExpr bexpr);

    /**
     * Creates double expression representing a*b
     * @param	a		constant valued factor of this multiplication
     * @param	bexpr	expression factor of this multiplication
     * @return			expression representing a * bexpr
     */
    public CspDoubleExpr multiply(int a, CspDoubleExpr bexpr);

    /**
     * Creates integer expression representing a/b
     * @param	a		constant dividend of this divide operation
     * @param	bexpr	expression divisor of this divide operation
     * @return			expression representing a / bexpr
     */
    public CspIntExpr divide(int a, CspIntExpr bexpr);

    /**
     * Creates long integer expression representing a/b
     * @param	a		constant dividend of this divide operation
     * @param	bexpr	expression divisor of this divide operation
     * @return			expression representing a / bexpr
     */
    public CspLongExpr divide(int a, CspLongExpr bexpr);

    /**
     * Creates float expression representing a/b
     * @param	a		constant dividend of this divide operation
     * @param	bexpr	expression divisor of this divide operation
     * @return			expression representing a / bexpr
     */
    public CspFloatExpr divide(int a, CspFloatExpr bexpr);

    /**
     * Creates double expression representing a/b
     * @param	a		constant dividend of this divide operation
     * @param	bexpr	expression divisor of this divide operation
     * @return			expression representing a / bexpr
     */
    public CspDoubleExpr divide(int a, CspDoubleExpr bexpr);

    /**
     * Creates integer expression representing |expr|
     * @param	expr	expression of which to obtain the absolute value
     * @return expression representing the absolute value of expr
     */
    public CspIntExpr abs(CspIntExpr expr);

    /**
     * Creates long expression representing |expr|
     * @param	expr	expression of which to obtain the absolute value
     * @return expression representing the absolute value of expr
     */
    public CspLongExpr abs(CspLongExpr expr);

    /**
     * Creates float expression representing |expr|
     * @param	expr	expression of which to obtain the absolute value
     * @return expression representing the absolute value of expr
     */
    public CspFloatExpr abs(CspFloatExpr expr);

    /**
     * Creates double expression representing |expr|
     * @param	expr	expression of which to obtain the absolute value
     * @return expression representing the absolute value of expr
     */
    public CspDoubleExpr abs(CspDoubleExpr expr);

    /**
     * Creates integer expression representing expr^2
     * @param	expr	expression of which to obtain the square value
     * @return expression representing the square of expr
     */
    public CspIntExpr square(CspIntExpr expr);

    /**
     * Creates long integer expression representing expr^2
     * @param	expr	expression of which to obtain the square value
     * @return expression representing the square of expr
     */
    public CspLongExpr square(CspLongExpr expr);

    /**
     * Creates float expression representing expr^2
     * @param	expr	expression of which to obtain the square value
     * @return expression representing the square of expr
     */
    public CspFloatExpr square(CspFloatExpr expr);

    /**
     * Creates double expression representing expr^2
     * @param	expr	expression of which to obtain the square value
     * @return expression representing the square of expr
     */
    public CspDoubleExpr square(CspDoubleExpr expr);

    /**
     * Creates double expression representing x^p
     * @param	x	base value of this exponent
     * @param	p	value of the power of which to raise x 
     * @return expression representing x^p
     */
    public CspDoubleExpr power(CspDoubleExpr x, double p);

    /**
     * Creates double expression representing x^p
     * @param	x	base value of this exponent
     * @param	p	value of the power of which to raise x 
     * @return expression representing x^p
     */
    public CspDoubleExpr power(CspDoubleExpr x, CspDoubleExpr p);

    /**
     * Creates double expression representing x^p
     * @param	x	base value of this exponent
     * @param	p	value of the power of which to raise x 
     * @return expression representing x^p
     */
    public CspDoubleExpr power(double x, CspDoubleExpr p);

    /**
     * Creates double expression representing e^x where
     * e is the natural logarithm base 
     * @param	x	value of the power of which to raise e 
     * @return expression representing e^x
     */
    public CspDoubleExpr exp(CspDoubleExpr x);

    /**
     * Creates double expression representing natural log(x)
     * @param	x	value of which to take the natural log
     * @return expression representing ln(x)
     */
    public CspDoubleExpr log(CspDoubleExpr x);

    /**
     * Creates double expression representing cos(x) in radians
     * @param	x	value of which to take cosine of
     * @return expression representing cos(x)
     */
    public CspDoubleExpr cos(CspDoubleExpr x);

    /**
     * Creates double expression representing arc cos(x) in radians
     * @param	x	value of which to take arccosine of
     * @return expression representing arccos(x)
     */
    public CspDoubleExpr acos(CspDoubleExpr x);

    /**
     * Creates double expression representing sin(x) in radians
     * @param	x	value of which to take sine of
     * @return expression representing sin(x)
     */
    public CspDoubleExpr sin(CspDoubleExpr x);

    /**
     * Creates double expression representing arc sin(x) in radians
     * @param	x	value of which to take arcsine of
     * @return expression representing arcsin(x)
     */
    public CspDoubleExpr asin(CspDoubleExpr x);

    /**
     * Creates double expression representing tan(x) in radians
     * @param	x	value of which to take tangent of
     * @return expression representing tan(x)
     */
    public CspDoubleExpr tan(CspDoubleExpr x);

    /**
     * Creates double expression representing arc sin(x) in radians
     * @param	x	value of which to take arctangent of
     * @return expression representing arctan(x)
     */
    public CspDoubleExpr atan(CspDoubleExpr x);

    /**
     * Constrains an array of variables to have different values
     * @param	exprs	array of expression objects that you would like to guarantee distinct
     * @return		constraint forcing each expression to be unique
     */
    public CspConstraint allDifferent(CspIntExpr exprs[]);

    /**
     * Constrains an array of variables to have different values
     * @param	exprs	array of expression objects that you would like to guarantee distinct
     * @return		constraint forcing each expression to be unique
     */
    public CspConstraint allDifferent(CspLongExpr exprs[]);

    /**
     * Constrains all elements of a generic variable to be different values
     * @param	expr	generic expression whose elements need to be different
     * @return		constraint forcing each element of the specified generic to be unique
     */
    public CspConstraint allDifferent(CspGenericIntExpr expr);

    /**
     * Constrains all elements of a generic variable to be different values
     * @param	expr	generic expression whose elements need to be different
     * @return		constraint forcing each element of the specified generic to be unique
     */
    public CspConstraint allDifferent(CspGenericLongExpr expr);

    /**
     * Generates a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     * @param exprs - collection of expressions whose elements need to occur at least lb and at most ub times
     * @param vals - the list of values that have been bounded to occur a certain number of times
     * @param lb - lower bound representing the minimum number of times that the val at the corresponding index occurs
     * @param ub - upper bound representing the maximum number of times that the val at the corresponding index occurs
     * @return - a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     */
    public CspConstraint globalCardinality(CspIntExpr exprs[], Number[] vals, int[]lb, int[]ub);

    /**
     * Generates a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     * @param exprs - generic expression whose elements need to occur at least lb and at most ub times
     * @param vals - the list of values that have been bounded to occur a certain number of times
     * @param lb - lower bound representing the minimum number of times that the val at the corresponding index occurs
     * @param ub - upper bound representing the maximum number of times that the val at the corresponding index occurs
     * @return - a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     */
    public CspConstraint globalCardinality(CspLongExpr exprs[],Number[] vals, int[]lb,int[]ub);

    /**
     * Generates a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     * @param expr - generic expression whose elements need to occur at least lb and at most ub times
     * @param vals - the list of values that have been bounded to occur a certain number of times
     * @param lb - lower bound representing the minimum number of times that the val at the corresponding index occurs
     * @param ub - upper bound representing the maximum number of times that the val at the corresponding index occurs
     * @return - a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     */
    public CspConstraint globalCardinality(CspGenericIntExpr expr,Number[] vals, int[]lb,int[]ub);

    /**
     * Generates a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     * @param expr - collection of expressions whose elements need to occur at least lb and at most ub times
     * @param vals - the list of values that have been bounded to occur a certain number of times
     * @param lb - lower bound representing the minimum number of times that the val at the corresponding index occurs
     * @param ub - upper bound representing the maximum number of times that the val at the corresponding index occurs
     * @return - a constraint that will cause the number of times vals occurs in exprs to be at least lb and at most ub
     */
    public CspConstraint globalCardinality(CspGenericLongExpr expr,Number[] vals, int[]lb,int[]ub);

    /**
     * A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     * @param exprs - the expressions whose values we will count
     * @param vals - the values we are concerned about counting
     * @param count - the int expression representing the number of occurrends of the corresponding value in exprs
     * @return -A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     */
    public CspConstraint globalCardCount(CspIntExpr exprs[], Number[] vals, CspIntExpr[] count) ;

    /**
     * A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     * @param exprs - the expressions whose values we will count
     * @param vals - the values we are concerned about counting
     * @param count - the int expression representing the number of occurrends of the corresponding value in exprs
     * @return -A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     */
    public CspConstraint globalCardCount(CspLongExpr exprs[], Number[] vals, CspIntExpr[] count);

    /**
     * A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     * @param expr - generic expression whose values we will count
     * @param vals - the values we are concerned about counting
     * @param count - the int expression representing the number of occurrends of the corresponding value in expr
     * @return -A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     */
    public CspConstraint globalCardCount(CspGenericIntExpr expr, Number[] vals, CspIntExpr[] count);

    /**
     * A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     * @param expr - generic expression whose values we will count
     * @param vals - the values we are concerned about counting
     * @param count - the int expression representing the number of occurrends of the corresponding value in expr
     * @return -A constraint that will count the number of occurrence of certain values and store that number in the corresponding count int expression
     */
    public CspConstraint globalCardCount(CspGenericLongExpr expr, Number[] vals, CspIntExpr[] count);

    /**
     * Constrains a numeric expression to be a member of an array.  This function
     * does not support generic expressions.
     * @param	sources		collection of expressions that expr must be a member of
     * @param	expr		expression that must be a member of sources
     * @return	constraint constraining expr to be a member of sources
     */
    public CspConstraint memberOfArray(CspIntExpr sources[], CspIntExpr expr);

    /**
     * Constrains a numeric expression to not be a member of an array.  This function
     * does not support generic expressions.
     * @param	sources		collection of expressions that expr must not be a member of
     * @param	expr		expression that must not be a member of sources
     * @return	constraint constraining expr to not be a member of sources
     */
    public CspConstraint notMemberOfArray(CspLongExpr sources[], CspLongExpr expr);

    /**
     * Constrains a numeric expression to not be a member of an array.  This function
     * does not support generic expressions.
     * @param	sources		collection of expressions that expr must not be a member of
     * @param	expr		expression that must not be a member of sources
     * @return	constraint constraining expr to not be a member of sources
     */
    public CspConstraint notMemberOfArray(CspIntExpr sources[], CspIntExpr expr);

    /**
     * Constrains a numeric expression to be a member of an array.  This function
     * does not support generic expressions.
     * @param	sources		collection of expressions that expr must not be a member of
     * @param	expr		expression that must not be a member of sources
     * @return	constraint constraining expr to not be a member of sources
     */
    public CspConstraint memberOfArray(CspLongExpr sources[], CspLongExpr expr);

    /**
     * Creates a variable equal to the summation of values in the generic
     * variable.  All indices in given range must exist in the variable X.
     * If additional indices exist in X not used within the range, the returned
     * expression will be a generic expression that is indexed on the leftover
     * indices.
     * 
     * @param x                         Generic variable summation is over
     * @param indexRange                Indices within generic variable X that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @return Expression that is equal to summation of X
     */
    public CspIntExpr summation(CspGenericIntExpr x, CspGenericIndex indexRange[], 
            CspGenericIndexRestriction sourceIdxRestriction);

    /**
     * Creates a variable equal to the summation of values in the generic
     * variable.  All indices in given range must exist in the variable X.
     * If additional indices exist in X not used within the range, the returned
     * expression will be a generic expression that is indexed on the leftover
     * indices.
     * 
     * @param x                         Generic variable summation is over
     * @param indexRange                Indices within generic variable X that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @return Expression that is equal to summation of X
     */
    public CspLongExpr summation(CspGenericLongExpr x, CspGenericIndex indexRange[], 
            CspGenericIndexRestriction sourceIdxRestriction);

    /**
     * Creates a variable equal to the summation of values in the generic
     * variable.  All indices in given range must exist in the variable X.
     * If additional indices exist in X not used within the range, the returned
     * expression will be a generic expression that is indexed on the leftover
     * indices.
     * 
     * @param x                         Generic variable summation is over
     * @param indexRange                Indices within generic variable X that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @return Expression that is equal to summation of X
     */
    public CspFloatExpr summation(CspGenericFloatExpr x, CspGenericIndex indexRange[], 
            CspGenericIndexRestriction sourceIdxRestriction);

    /**
     * Creates a variable equal to the summation of values in the generic
     * variable.  All indices in given range must exist in the variable X.
     * If additional indices exist in X not used within the range, the returned
     * expression will be a generic expression that is indexed on the leftover
     * indices.
     * 
     * @param x                         Generic variable summation is over
     * @param indexRange                Indices within generic variable X that summation is over
     * @param sourceIdxRestriction      Optional restriction of values placed on source index ranges
     * @return Expression that is equal to summation of X
     */
    public CspDoubleExpr summation(CspGenericDoubleExpr x, CspGenericIndex indexRange[], 
            CspGenericIndexRestriction sourceIdxRestriction); 
}