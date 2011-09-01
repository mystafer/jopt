package jopt.csp.variable;

import jopt.csp.util.IntSparseSet;


/**
 * Interface of object that is used to generate instances of csp variables 
 */
public interface CspVariableFactory {
    /**
     * Sets the default precision used when creating real variables
     * @param p	value representing the minimum guaranteed precision
     */
    public void setDefaultPrecision(double p);
    
    /**
     * Returns the default precision used when creating real variables
     * @return value representing the minimum guaranteed precision
     */
    public double getDefaultPrecision();
    
    /**
     * Returns a variable math object for advanced math operations
     * @return	CspMath object 
     */
    public CspMath getMath();

    /**
     * Returns an object used to create constraints on sets
     * @return CspSetConstraints object used to create constraint on sets
     */
    public <T> CspSetConstraints<T> getSetConstraints();

	/**
	 * Creates an instance of a CspIntVariable object;
	 * uses an interval-based domain by default
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @return	CspIntVariable object populated by vals between min and max
	 */
	public CspIntVariable intVar(String name, int min, int max);
	
    /**
     * Creates an instance of a CspIntVariable object;
	 * uses an interval domain by default
     * 
     * @param name      Unique name assigned to variable
     * @param vals		Set of sparse values for variable's domain
     */
    public CspIntVariable intVar(String name, IntSparseSet vals);
	
	/**
	 * Creates an instance of a CspIntVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @param sparse	Specifies the use of a sparse- or interval-based domain
	 * @return	CspIntVariable object populated by vals between min and max
	 */
	public CspIntVariable intVar(String name, int min, int max, boolean sparse);

	/**
	 * Creates an instance of a CspIntVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param var       Existing variable with a domain to clone
	 * @return	CspIntVariable object populated by vals in var
	 */
	public CspIntVariable intVar(String name, CspIntVariable var);

	/**
	 * Creates an instance of a CspIntVariable object based on an expression.
	 * The expression is constrained to be equal to the new variable.
	 * 
	 * @param name      Unique name assigned to variable
	 * @param expr      Expression that variable is based upon
	 * @return	CspIntVariable object constrained to be equal to expr
	 */
	public CspIntVariable intVar(String name, CspIntExpr expr);

	/**
	 * Creates an instance of a CspLongVariable object;
	 * uses an interval-based domain by default
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @return	CspLongVariable object populated by vals between min and max
	 */
	public CspLongVariable longVar(String name, long min, long max);
	
	/**
	 * Creates an instance of a CspLongVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @param sparse	Specifies the use of a sparse- or interval-based domain
	 * @return	CspLongVariable object populated by vals between min and max
	 */
	public CspLongVariable longVar(String name, long min, long max, boolean sparse);

	/**
	 * Creates an instance of a CspLongVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param var       Existing variable with a domain to clone
	 * @return	CspLongVariable object populated by vals from the domain of var
	 */
	public CspLongVariable longVar(String name, CspLongVariable var);

	/**
	 * Creates an instance of a CspLongVariable object based on an expression.
	 * The expression is constrained to be equal to the new variable.
	 * 
	 * @param name      Unique name assigned to variable
	 * @param expr      Expression that variable is based upon
	 * @return	CspLongVariable object constrainted to be equal to expr
	 */
	public CspLongVariable longVar(String name, CspLongCast expr);

	/**
	 * Creates an instance of a CspFloatVariable object;
	 * uses an interval-based domain by default
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @return	CspFloatVariable object populated by vals between min and max
	 */
	public CspFloatVariable floatVar(String name, float min, float max);

	/**
	 * Creates an instance of a CspFloatVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param var       Existing variable with a domain to clone
	 * @return	CspFloatVariable object populated by vals contained in var's domain
	 */
	public CspFloatVariable floatVar(String name, CspFloatVariable var);

	/**
	 * Creates an instance of a CspFloatVariable object based on an expression.
	 * The expression is constrained to be equal to the new variable.
	 * 
	 * @param name      Unique name assigned to variable
	 * @param expr      Expression that variable is based upon
	 * @return	CspFloatVariable object constrained to be equal to expr
	 */
	public CspFloatVariable floatVar(String name, CspFloatCast expr);

	/**
	 * Creates an instance of a CspDoubleVariable object;
	 * uses an interval domain by default
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @return	CspDoubleVariable object populated by vals between min and max
	 */
	public CspDoubleVariable doubleVar(String name, double min, double max);
	
	/**
	 * Creates an instance of a CspDoubleVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param var       Existing variable with a domain to clone
	 * @return	CspDoubleVariable object populated by vals from the domain of var
	 */
	public CspDoubleVariable doubleVar(String name, CspDoubleVariable var);

	/**
	 * Creates an instance of a CspDoubleVariable object based on an expression.
	 * The expression is constrained to be equal to the new variable.
	 * 
	 * @param name      Unique name assigned to variable
	 * @param expr      Expression that variable is based upon
	 * @return	CspDoubleVariable object constrainted to be equal to expr
	 */
	public CspDoubleVariable doubleVar(String name, CspDoubleCast expr);

	/**
	 * Creates an instance of a CspIntSetVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @return	CspIntSetVariable object populated by vals 
	 */
	public CspIntSetVariable intSetVar(String name, int min, int max);

    /**
     * Creates a generic index for use with generic variables
     * 
     * @param name      Unique name assigned to index
     * @param size      Total number of variables that will be used with index
     * @return generic index with given name and size
     */
    public CspGenericIndex genericIndex(String name, int size);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over index, containing all variables
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex index, CspIntVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over indices, containing all variables
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex indices[], CspIntVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over index and wrapping variableswith given min and max
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex index, int min, int max);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over indices and wrapping variableswith given min and max
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex indices[], int min, int max);

    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over index and wrapping variables
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex index, CspLongVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over indices and wrapping variables
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex indices[], CspLongVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over index and wrapping variableswith given min and max
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex index, int min, int max);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over indices and wrapping variableswith given min and max
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex indices[], int min, int max);

    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over index and wrapping variables
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex index, CspFloatVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over indices and wrapping variables
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex indices[], CspFloatVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over index and wrapping variables with given min and max
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex index, int min, int max);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over indices and wrapping variables with given min and max
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex indices[], int min, int max);

    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over index and wrapping variables 
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex index, CspDoubleVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     * @return	generic expression indexed over indices and wrapping variables
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex indices[], CspDoubleVariable variables[]);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over index and wrapping variables with min and max
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex index, int min, int max);
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     * @return	generic expression indexed over indices and wrapping variables with min and max
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex indices[], int min, int max);
    
    /**
     * Produces a generic variable that wraps a set of expressions
     * 
     * @param name      	Unique name assigned to variable
     * @param indices   	Array of indices variable is indexed upon
     * @param expressions 	Array of expressions that the generic expression wraps
     * @return	generic boolean expression indexed over indices and wrapping expressions
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex indices[], CspBooleanExpr expressions[]);

    /**
     * Produces a generic variable that wraps a set of expressions
     * 
     * @param name      	Unique name assigned to variable
     * @param index			Index variable is indexed upon
     * @param expressions 	Array of expressions that the generic expression wraps
     * @return	generic boolean expression indexed over index and wrapping expressions
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex index, CspBooleanExpr expressions[]);
    
    /**
     * Produces a generic variable that wraps a constraint
     * 
     * @param name      	Unique name assigned to variable
     * @param indices   	Array of indices variable is indexed upon
     * @param constraint 	Constraint that wrapped by the generic expression
     * @return	generic boolean expression indexed over indices and wrapping constraint
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex indices[], CspConstraint constraint);
    
    /**
     * Produces a generic variable that wraps a constraint
     * 
     * @param name      	Unique name assigned to variable
     * @param index		   	Index variable is indexed upon
     * @param constraint 	Constraint that wrapped by the generic expression
     * @return	generic boolean expression indexed over index and wrapping constraint
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex index, CspConstraint constraint);
    
    /**
     * Creates a boolean variable that wraps a constraint
     * 
     * @param name          Name of the new variable
     * @param constraint    Constraint that boolean variable will represent
     * @return Boolean variable to be used in boolean expressions wrapping constraint
     */
    public CspBooleanVariable booleanVar(String name, CspConstraint constraint);
    
    /**
     * Creates an instance of a CspBooleanVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     * @return Boolean variable to be used in boolean expressions wrapping expr
     */
    public CspBooleanVariable booleanVar(String name, CspBooleanExpr expr);
    
    /**
     * Creates a boolean variable
     * 
     * @param name          Name of the new variable
     * @return Boolean variable to be used in boolean expressions
     */
    public CspBooleanVariable booleanVar(String name);
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of Integer objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex indices[], Integer nodes[]) ;
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of ints containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex indices[], int nodes[]);
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of Double objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex indices[], Double nodes[]);
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of doubles containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex indices[], double nodes[]);

    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of Float objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex indices[], Float nodes[]);
    
    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of floats containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex indices[], float nodes[]);
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of Long objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex indices[], Long nodes[]);
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param indices   Array of indices variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex indices[], long nodes[]);
    
    /**
     * Creates a generic boolean constant
     * @param name		Name of the new constant
     * @param indices   Array of booleans constant is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericBooleanConstant genericBooleanConst(String name, CspGenericIndex indices[], boolean nodes[]);
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Integer objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex index, Integer nodes[]) ;
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of ints containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex index, int nodes[]);
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Double objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex index, Double nodes[]);
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of doubles containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex index, double nodes[]);
    
    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Float objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex index, Float nodes[]);
    
    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of floats containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex index, float nodes[]);
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Long objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex index, Long nodes[]);
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex index, long nodes[]);
    
    /**
     * Creates a generic boolean constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericBooleanConstant genericBooleanConst(String name, CspGenericIndex index, boolean nodes[]);
}