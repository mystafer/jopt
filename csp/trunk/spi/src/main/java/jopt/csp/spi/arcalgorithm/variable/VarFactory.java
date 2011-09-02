package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashSet;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.bool.BoolExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.IntSparseDomain;
import jopt.csp.spi.arcalgorithm.domain.LongIntervalDomain;
import jopt.csp.spi.arcalgorithm.domain.LongSparseDomain;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.util.IntSparseSet;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspBooleanVariable;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleCast;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatCast;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspGenericBooleanConstant;
import jopt.csp.variable.CspGenericBooleanExpr;
import jopt.csp.variable.CspGenericDoubleConstant;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericFloatConstant;
import jopt.csp.variable.CspGenericFloatExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntConstant;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspGenericLongConstant;
import jopt.csp.variable.CspGenericLongExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntSetVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongCast;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspNumVariable;
import jopt.csp.variable.CspSetConstraints;
import jopt.csp.variable.CspVariableFactory;

/**
 * Implementation of CspVariables for creating implementations of variables
 */
public class VarFactory implements CspVariableFactory {
    private Set<String> nameSet = new HashSet<String>();
    private double defaultPrecision;
    
    /**
     * Sets the default precision used when creating real variables
     */
    public void setDefaultPrecision(double p) {
    	this.defaultPrecision = p;
    }
    
    /**
     * Returns the default precision used when creating real variables
     */
    public double getDefaultPrecision() {
        return this.defaultPrecision;
    }
    
    /**
     * Returns a variable math object for extended math operations
     */
    public CspMath getMath() {
        return new VarMath();
    }
    
    /**
     * Returns an object used to create constraints on sets
     */
    public <T> CspSetConstraints<T> getSetConstraints() {
    	return new SetConstraints<T>();
    }
    
    /**
     * Creates an instance of a CspIntVariable object;
	 * uses an interval domain by default
     * 
     * @param name      Unique name assigned to variable
     * @param min       Lower bound for variable's domain
     * @param max       Upper bound for variable's domain
     */
    public CspIntVariable intVar(String name, int min, int max) {
        return intVar(name, min, max, false);
    }
    
    /**
     * Creates an instance of a CspIntVariable object;
	 * uses an interval domain by default
     * 
     * @param name      Unique name assigned to variable
     * @param vals		Set of sparse values for variable's domain
     */
    public CspIntVariable intVar(String name, IntSparseSet vals) {
        return new IntVariable(name, new IntSparseDomain(vals));
    }
    
    
    
    /**
	 * Creates an instance of a CspIntVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @param sparse	Specifies the use of a sparse or interval domain
	 */
	public CspIntVariable intVar(String name, int min, int max, boolean sparse) {
        name = checkName(name);
		if(sparse) {
			return new IntVariable(name, new IntSparseDomain(min, max));
		}
		else {
			return new IntVariable(name, new IntIntervalDomain(min, max));
		}
	}
    
    /**
     * Creates an instance of a CspIntVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param var       Existing variable with a domain to clone
     */
    public CspIntVariable intVar(String name, CspIntVariable var) {
        name = checkName(name);
        return new IntVariable(name, (IntVariable) var);
    }
    
    /**
     * Creates an instance of a CspIntVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     */
    public CspIntVariable intVar(String name, CspIntExpr expr) {
        name = checkName(name);
        return new IntVariable(name, expr);
    }

    /**
     * Creates an instance of a CspLongVariable object;
     * uses an interval domain by default
     * 
     * @param name      Unique name assigned to variable
     * @param min       Lower bound for variable's domain
     * @param max       Upper bound for variable's domain
     */
    public CspLongVariable longVar(String name, long min, long max) {
        return longVar(name, min, max, false);
    }
    
    /**
	 * Creates an instance of a CspLongVariable object
	 * 
	 * @param name      Unique name assigned to variable
	 * @param min       Lower bound for variable's domain
	 * @param max       Upper bound for variable's domain
	 * @param sparse	Specifies the use of a sparse or interval domain
	 */
	public CspLongVariable longVar(String name, long min, long max, boolean sparse) {
        name = checkName(name);
		if(sparse) {
			return new LongVariable(name, new LongSparseDomain(min, max));
		}
		else {
			return new LongVariable(name, new LongIntervalDomain(min, max));
		}
	}
    
    /**
     * Creates an instance of a CspLongVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param var       Existing variable with a domain to clone
     */
    public CspLongVariable longVar(String name, CspLongVariable var) {
        name = checkName(name);
        return new LongVariable(name, (LongVariable) var);
    }
    
    /**
     * Creates an instance of a CspLongVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     */
    public CspLongVariable longVar(String name, CspLongCast expr) {
        name = checkName(name);
        return new LongVariable(name, (LongCast)expr);
    }

    /**
     * Creates an instance of a CspFloatVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param min       Lower bound for variable's domain
     * @param max       Upper bound for variable's domain
     */
    public CspFloatVariable floatVar(String name, float min, float max) {
        name = checkName(name);
    	 FloatVariable v = new FloatVariable(name, min, max);
         v.setPrecision(defaultPrecision);
         return v;
    }
    
    /**
     * Creates an instance of a CspFloatVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param var       Existing variable with a domain to clone
     */
    public CspFloatVariable floatVar(String name, CspFloatVariable var) {
        name = checkName(name);
        FloatVariable v = new FloatVariable(name, (FloatVariable) var);
        v.setPrecision(defaultPrecision);
        return v;
    }
    
    /**
     * Creates an instance of a CspFloatVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     */
    public CspFloatVariable floatVar(String name, CspFloatCast expr) {
        name = checkName(name);
        FloatVariable v = new FloatVariable(name, (FloatCast)expr);
        v.setPrecision(defaultPrecision);
        return v;
    }


    /**
     * Creates an instance of a CspDoubleVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param min       Lower bound for variable's domain
     * @param max       Upper bound for variable's domain
     */
    public CspDoubleVariable doubleVar(String name, double min, double max) {
        name = checkName(name);
        DoubleVariable v = new DoubleVariable(name, min, max);
        v.setPrecision(defaultPrecision);
        return v;
    }
    
    /**
     * Creates an instance of a CspDoubleVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param var       Existing variable with a domain to clone
     */
    public CspDoubleVariable doubleVar(String name, CspDoubleVariable var) {
        name = checkName(name);
        DoubleVariable v = new DoubleVariable(name, (DoubleVariable) var);
        v.setPrecision(defaultPrecision);
        return v;
    }
    
    /**
     * Creates an instance of a CspDoubleVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     */
    public CspDoubleVariable doubleVar(String name, CspDoubleCast expr) {
        name = checkName(name);
        DoubleVariable v = new DoubleVariable(name, (DoubleCast)expr);
        v.setPrecision(defaultPrecision);
        return v;
    }

    /**
     * Creates an instance of a CspIntSetVariable object
     * 
     * @param name      Unique name assigned to variable
     * @param min       Lower bound for variable's domain
     * @param max       Upper bound for variable's domain
     */
    public CspIntSetVariable intSetVar(String name, int min, int max) {
        name = checkName(name);
        return new IntSetVariable(name, min, max);
    }
    
    /**
     * Creates a generic index for use with generic variables
     * 
     * @param name      Unique name assigned to index
     * @param size      Total number of variables that will be used with index
     */
    public CspGenericIndex genericIndex(String name, int size) {
        name = checkName(name);
        return new GenericIndex(name, size);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex index, CspIntVariable variables[]) {
    	return genericInt(name, new CspGenericIndex[]{index}, variables);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex indices[], CspIntVariable variables[]) {
        name = checkName(name);
        return new GenericIntExpr(name, VarMath.toGenericArray(indices), toNumExprArray(variables));
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex index, int min, int max) {
        return genericInt(name, new CspGenericIndex[]{index}, min, max);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericIntExpr genericInt(String name, CspGenericIndex indices[], int min, int max) {
        name = checkName(name);
        GenericIndex idxArray[] = VarMath.toGenericArray(indices);
        
        // create array of expressions to wrap
        int totalExprs = VarMath.totalGenericSize(idxArray);
        NumExpr exprs[] = new NumExpr[totalExprs];
        for (int i=0; i<totalExprs; i++)
            exprs[i] = (NumExpr)intVar("_" + name + i, min, max);
        
        
        return new GenericIntExpr(name, idxArray, exprs);
    }

    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex index, CspLongVariable variables[]) {
        return genericLong(name, new CspGenericIndex[]{index}, variables);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex indices[], CspLongVariable variables[]) {
        name = checkName(name);
        return new GenericLongExpr(name, VarMath.toGenericArray(indices), toNumExprArray(variables));
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex index, int min, int max) {
        return genericLong(name, new CspGenericIndex[]{index}, min, max);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericLongExpr genericLong(String name, CspGenericIndex indices[], int min, int max) {
        name = checkName(name);
        GenericIndex idxArray[] = VarMath.toGenericArray(indices);
        
        // create array of expressions to wrap
        int totalExprs = VarMath.totalGenericSize(idxArray);
        NumExpr exprs[] = new NumExpr[totalExprs];
        for (int i=0; i<totalExprs; i++)
            exprs[i] = (NumExpr)longVar("_" + name + i, min, max);
        
        
        return new GenericLongExpr(name, idxArray, exprs);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex index, CspFloatVariable variables[]) {
        return genericFloat(name, new CspGenericIndex[]{index}, variables);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex indices[], CspFloatVariable variables[]) {
        name = checkName(name);
        return new GenericFloatExpr(name, VarMath.toGenericArray(indices), toNumExprArray(variables));
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex index, int min, int max) {
        return genericFloat(name, new CspGenericIndex[]{index}, min, max);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericFloatExpr genericFloat(String name, CspGenericIndex indices[], int min, int max) {
        name = checkName(name);
        GenericIndex idxArray[] = VarMath.toGenericArray(indices);
        
        // create array of expressions to wrap
        int totalExprs = VarMath.totalGenericSize(idxArray);
        NumExpr exprs[] = new NumExpr[totalExprs];
        for (int i=0; i<totalExprs; i++)
            exprs[i] = (NumExpr)floatVar("_" + name + i, min, max);
        
        
        return new GenericFloatExpr(name, idxArray, exprs);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex index, CspDoubleVariable variables[]) {
        return genericDouble(name, new CspGenericIndex[]{index}, variables);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param variables Array of variables generic variable wraps
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex indices[], CspDoubleVariable variables[]) {
        name = checkName(name);
        return new GenericDoubleExpr(name, VarMath.toGenericArray(indices), toNumExprArray(variables));
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param index     Index variable is based upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex index, int min, int max) {
        return genericDouble(name, new CspGenericIndex[]{index}, min, max);
    }
    
    /**
     * Produces a generic variable that wraps a set of nodes
     * 
     * @param name      Unique name assigned to variable
     * @param indices   Array of indices variable is indexed upon
     * @param min       Minimum value of domain of internal variables
     * @param max       Maximum value of domain of internal variables
     */
    public CspGenericDoubleExpr genericDouble(String name, CspGenericIndex indices[], int min, int max) {
        name = checkName(name);
        GenericIndex idxArray[] = VarMath.toGenericArray(indices);
        
        // create array of expressions to wrap
        int totalExprs = VarMath.totalGenericSize(idxArray);
        NumExpr exprs[] = new NumExpr[totalExprs];
        for (int i=0; i<totalExprs; i++)
            exprs[i] = (NumExpr)doubleVar("_" + name + i, min, max);
        
        
        return new GenericDoubleExpr(name, idxArray, exprs);
    }
    
    /**
     * Produces a generic variable that wraps a set of expressions
     * 
     * @param name      	Unique name assigned to variable
     * @param indices   	Array of indices variable is indexed upon
     * @param expressions 	Array of expressions that the generic expression wraps
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex indices[], CspBooleanExpr expressions[]) {
        name = checkName(name);
        return new GenericBooleanExpr(name, VarMath.toGenericArray(indices), toBoolExprArray(expressions));
    }
    
    /**
     * Produces a generic variable that wraps a constraint
     * 
     * @param name      	Unique name assigned to variable
     * @param indices   	Array of indices variable is indexed upon
     * @param constraint 	Constraint that wrapped by the generic expression
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex indices[], CspConstraint constraint) {
        name = checkName(name);
        return new GenericBooleanExpr(name, VarMath.toGenericArray(indices), constraint);
    }
    
    /**
     * Produces a generic variable that wraps a set of expressions
     * 
     * @param name      	Unique name assigned to variable
     * @param index			Index variable is indexed upon
     * @param expressions 	Array of expressions that the generic expression wraps
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex index, CspBooleanExpr expressions[]) {
        return genericBoolean(name, new CspGenericIndex[]{index}, expressions);
    }
    
    /**
     * Produces a generic variable that wraps a constraint
     * 
     * @param name      	Unique name assigned to variable
     * @param index   		Index variable is indexed upon
     * @param constraint 	Constraint that wrapped by the generic expression
     */
    public CspGenericBooleanExpr genericBoolean(String name, CspGenericIndex index, CspConstraint constraint) {
        return genericBoolean(name, new CspGenericIndex[]{index}, constraint);
    }    
    
    /**
     * Converts an array of CSP expressions to internal numeric expressions
     */
    public static NumExpr[] toNumExprArray(CspNumVariable variables[]) {
        NumExpr varArray[] = new NumExpr[variables.length];
        for (int i=0; i<variables.length; i++)
            varArray[i] = (NumExpr) variables[i];
        return varArray;
    }
    
    /**
     * Converts an array of CSP expressions to internal numeric expressions
     */
    public static NumExpr[] toNumExprArray(CspBooleanVariable variables[]) {
        IntExpr varArray[] = new IntExpr[variables.length];
        for (int i=0; i<variables.length; i++)
            varArray[i] = (IntExpr) variables[i];
        return varArray;
    }
    
    /**
     * Converts an array of CSP expressions to internal boolean expressions
     */
    public static BoolExpr[] toBoolExprArray(CspBooleanExpr expressions[]) {
        BoolExpr exprArray[] = new BoolExpr[expressions.length];
        for (int i=0; i<expressions.length; i++)
            exprArray[i] = (BoolExpr) expressions[i];
        return exprArray;
    }
    
    /**
     * Converts an array of CSP GenericIndex's to GenericIndex's
     */
    public static GenericIndex[] toGenericIndex(CspGenericIndex expressions[]) {
        if (expressions == null) return null;
        GenericIndex exprArray[] = new GenericIndex[expressions.length];
        for (int i=0; i<expressions.length; i++)
            exprArray[i] = (GenericIndex) expressions[i];
        return exprArray;
    }
    
    /**
     * Creates a boolean expression that wraps a constraint
     * 
     * @param name          Name of the new expression
     * @param constraint    Constraint that boolean variable will represent
     * @return Boolean expression to be used in building other expressions
     */
    public CspBooleanExpr booleanExpr(String name, CspConstraint constraint) {
        name = checkName(name);
        return new BooleanExpr(name, constraint);
    }
    
    /**
     * Creates a boolean variable that wraps a constraint
     * 
     * @param name			Name of the new variable
     * @param constraint	Constraint that boolean variable will represent
     * @return Boolean variable to be used in boolean expressions
     */
    public CspBooleanVariable booleanVar(String name, CspConstraint constraint) {
        name = checkName(name);
    	return new BooleanVariable(name, constraint);
    }
    
    /**
     * Creates an instance of a CspBooleanVariable object based on an expression.
     * The expression is constrained to be equal to the new variable.
     * 
     * @param name      Unique name assigned to variable
     * @param expr      Expression that variable is based upon
     */
    public CspBooleanVariable booleanVar(String name, CspBooleanExpr expr) {
        name = checkName(name);
        return new BooleanVariable(name, expr);
    }
    
    /**
     * Creates a boolean variable
     * 
     * @param name			Name of the new variable
     * @return Boolean variable to be used in boolean expressions
     */
    public CspBooleanVariable booleanVar(String name) {
        name = checkName(name);
    	return new BooleanVariable(name);
    }
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of Integer objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex indices[], Integer nodes[]) {
        name = checkName(name);
        return new GenericIntConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of ints containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex indices[], int nodes[]) {
        name = checkName(name);
        return new GenericIntConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of Double objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex indices[], Double nodes[]) {
        name = checkName(name);
        return new GenericDoubleConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of doubles containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex indices[], double nodes[]) {
        name = checkName(name);
        return new GenericDoubleConstant(name, indices, nodes);
    }

    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of Float objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex indices[], Float nodes[]) {
        name = checkName(name);
        return new GenericFloatConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of floats containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex indices[], float nodes[]) {
        name = checkName(name);
        return new GenericFloatConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of Long objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex indices[], Long nodes[]) {
        name = checkName(name);
        return new GenericLongConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param indices   Array of indices constant is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex indices[], long nodes[]) {
        name = checkName(name);
        return new GenericLongConstant(name, indices, nodes);
    }
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Integer objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex index, Integer nodes[]) {
        return genericIntConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic int constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of ints containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericIntConstant genericIntConst(String name, CspGenericIndex index, int nodes[]) {
        return genericIntConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Double objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex index, Double nodes[]) {
        return genericDoubleConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic double constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of doubles containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericDoubleConstant genericDoubleConst(String name, CspGenericIndex index, double nodes[]) {
        return genericDoubleConst(name, new CspGenericIndex[]{index}, nodes);
    }

    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Float objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex index, Float nodes[]) {
        return genericFloatConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic float constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of floats containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericFloatConstant genericFloatConst(String name, CspGenericIndex index, float nodes[]) {
        return genericFloatConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of Long objects containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex index, Long nodes[]) {
        return genericLongConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic long constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericLongConstant genericLongConst(String name, CspGenericIndex index, long nodes[]) {
        return genericLongConst(name, new CspGenericIndex[]{index}, nodes);
    }
    
    /**
     * Creates a generic boolean constant
     * @param name		Name of the new constant
     * @param indices		Index variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericBooleanConstant genericBooleanConst(String name, CspGenericIndex indices[], boolean nodes[]) {
        name = checkName(name);
        return new GenericBooleanConstant(name, indices, nodes);
    }    
    
    /**
     * Creates a generic boolean constant
     * @param name		Name of the new constant
     * @param index		Index variable is indexed upon
     * @param nodes		Array of longs containing the values for the constants
     * @return			CspGenericIntConstant to be used in Generic expressions
     */
    public CspGenericBooleanConstant genericBooleanConst(String name, CspGenericIndex index, boolean nodes[]) {
        return genericBooleanConst(name,new CspGenericIndex[]{index},nodes);
    }
    
    /**
     * Helper function to ensure a unique name is given to a generated variable
     */
    private String checkName(String name) {
        if (name==null)
            name = NameUtil.nextName();
        
        if (nameSet.contains(name))
            throw new RuntimeException("variable name must be unique");
        
        nameSet.add(name);
        
        return name;
    }
}
