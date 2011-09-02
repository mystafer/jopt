/*
 * Created on May 25, 2005
 */
package jopt.csp.variable;



/**
 * Base interface for boolean expressions
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 */
public abstract interface CspBooleanExpr extends CspIntExpr {
    
    /**
	 * Creates a boolean expression resulting from anding another boolean expression
	 * with this expression
	 * 
	 * @param	expr	expression that this boolean expression will be ANDed with
	 * @return CspBooleanExpr representing the expression (this AND expr)
	 */
	public CspBooleanExpr and (CspBooleanExpr expr);

	/**
	 * Creates a boolean expression resulting from anding a constraint
	 * with this expression
	 * 
	 * @param	cons	constraint that this boolean expression will be ANDed with
	 * @return CspBooleanExpr representing the expression (this AND cons)
	 */
	public CspBooleanExpr and (CspConstraint cons);

	/**
	 * Creates a boolean expression resulting from anding a constant
	 * with this expression
	 * 
	 * @param 	val		constant value to AND this boolean expression with
	 * @return CspBooleanExpr representing the expression (this AND val)
	 */
	public CspBooleanExpr and (boolean val);
	
	/**
	 * Creates a boolean expression resulting from anding a generic constant
	 * with this expression
	 * 
	 * @param 	val		generic constant value to AND this boolean expression with
	 * @return CspBooleanExpr representing the expression (this AND val)
	 */
	public CspBooleanExpr and (CspGenericBooleanConstant val);

    /**
     * Returns a boolean expression equal to the negation of this one
     * @return CspBooleanExpr representing the expression (!this)
     */
    public CspBooleanExpr not();

	/**
	 * Creates a boolean expression resulting from xoring a boolean expression
	 * with this expression
	 * 
	 * @param	expr	expression that this expression will be XORed with
	 * @return CspBooleanExpr representing the expression (this XOR expr)
	 */
	public CspBooleanExpr xor (CspBooleanExpr expr);

	/**
	 * Creates a boolean expression resulting from xoring a constraint
	 * with this expression
	 * 
	 * @param	cons 	constraint involved in creating an expression representing an XOR with this
	 * @return CspBooleanExpr representing the expression (this XOR cons)
	 */
	public CspBooleanExpr xor (CspConstraint cons);

	/**
	 * Creates a boolean expression resulting from xoring a constant
	 * with this expression
	 * 
	 * @param	val		the boolean involved in creating an expression representing an XOR with this
	 * @return CspBooleanExpr representing the expression (this XOR val)
	 */
	public CspBooleanExpr xor (boolean val);
	
	/**
	 * Creates a boolean expression resulting from xoring a generic constant
	 * with this expression
	 * 
	 * @param	val		the generic boolean involved in creating an expression representing an XOR with this
	 * @return CspBooleanExpr representing the expression (this XOR val)
	 */
	public CspBooleanExpr xor (CspGenericBooleanConstant val);
    
	/**
	 * Creates a boolean expression resulting from oring a boolean expression
	 * with this expression
	 * 
	 * @param 	expr 	expression that this expression will be ORed with
	 * @return CspBooleanExpr representing the expression (this OR expr)
	 */
	public CspBooleanExpr or (CspBooleanExpr expr);

	/**
	 * Creates a boolean expression resulting from oring a constraint
	 * with this expression
	 * 
	 * @param 	cons	constraint involved in creating an expression representing an OR with this
	 * @return 	CspBooleanExpr representing the expression (this OR cons)
	 */
	public CspBooleanExpr or (CspConstraint cons);

	/**
	 * Creates a boolean expression resulting from oring a constant
	 * with this expression
	 * 
	 * @param 	val		the boolean involved in creating an expression representing an OR with this
	 * @return 	CspBooleanExpr representing the expression (this OR val)
	 */
	public CspBooleanExpr or (boolean val);
	
	/**
	 * Creates a boolean expression resulting from oring a generic constant
	 * with this expression
	 * 
	 * @param 	val		the generic boolean involved in creating an expression representing an OR with this
	 * @return 	CspBooleanExpr representing the expression (this OR val)
	 */
	public CspBooleanExpr or (CspGenericBooleanConstant val);
    
    /**
     * Creates a boolean expression resulting from this expression
     * implying another expression
     * 
     * @param 	expr	expression that this expression will imply (this -> expr)
	 * @return 	CspBooleanExpr representing the expression (this -> expr)
     */
    public CspBooleanExpr implies(CspBooleanExpr expr);

    /**
     * Creates a boolean expression resulting from this expression
     * implying a constant value
     * 
     * @param 	val		the boolean involved in creating an expression representing implication (this -> val)
	 * @return 	CspBooleanExpr representing the expression (this -> val)
     */
    public CspBooleanExpr implies(boolean val);
    
    /**
     * Creates a boolean expression resulting from this expression
     * implying a generic constant value
     * 
     * @param 	val		the generic boolean involved in creating an expression representing implication (this -> val)
	 * @return 	CspBooleanExpr representing the expression (this -> val)
     */
    public CspBooleanExpr implies(CspGenericBooleanConstant val);

    /**
     * Creates a boolean expression resulting from this expression
     * implying a constraint
     * 
     * @param 	cons	constraint involved in creating an expression representing implication (this -> cons)
	 * @return 	CspBooleanExpr representing the expression (this -> cons)
     */
	public CspBooleanExpr implies(CspConstraint cons);
	
	/**
	 * Creates a boolean expression resulting from setting a boolean expression
	 * equal to this expression
	 * 
	 * @param 	expr	expression that this expression will be equal to
	 * @return 	CspBooleanExpr representing the expression (this == expr)
	 */
	public CspBooleanExpr eq (CspBooleanExpr expr);

	/**
	 * Creates a boolean expression resulting from setting a constraint
	 * equal to this expression
	 * 
	 * @param 	cons	constraint involved in creating an expression representing equality between this and a constraint
	 * @return 	CspBooleanExpr representing the expression (this == cons)
	 */
	public CspBooleanExpr eq (CspConstraint cons);

	/**
	 * Creates a boolean expression resulting from setting a constant
	 * equal to this expression
	 * 
	 * @param 	val		the boolean involved in creating an expression representing equality
	 * @return 	CspBooleanExpr representing the expression (this == val)
	 */
	public CspBooleanExpr eq (boolean val);	
	
    /**
     * Returns true if boolean expression cannot be satisfied
     * 
     * @return		true - boolean expression cannot be satisfied;
     * 				false - boolean expression can potentially be satisfied
     */
    public boolean isFalse();
    
    /**
     * Returns whether or not the expression is satisfied yet or not
     * 
     * @return		true - boolean expression cannot be dissatisfied;
     * 				false - boolean expression can potentially be dissatisfied
     */
    public boolean isTrue();
    
    /**
     * Returns a constraint that represents this boolean expression
     * that can be used in the solver
     * 
     * @return		constraint representing this boolean expression
     */
    public CspConstraint toConstraint();
    
}

