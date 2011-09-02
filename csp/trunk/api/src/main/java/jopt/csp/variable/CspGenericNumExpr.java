/*
 * CspGenericExpr.java
 * 
 * Created on Apr 20, 2005
 */
package jopt.csp.variable;


/**
 * Interface for generic number expressions
 */
public interface CspGenericNumExpr extends CspNumExpr {
	/**
	 * Returns the indices that are associated with this expression
	 * @return array of indices that this expression is indexed over
	 */
	public CspGenericIndex[] getIndices();

	/**
	 * Returns the number of expressions that are wrapped by this generic expression
	 * @return	number of expression that are apart of this generic expression
	 */
	public int getExpressionCount();
}