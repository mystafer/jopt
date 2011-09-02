package jopt.csp.variable;

/**
 * Interface implemented by generic boolean expressions.  
 */
public interface CspGenericBooleanExpr extends CspBooleanExpr {
    /**
	 * returns a boolean expression from the internal array
	 * 
	 * @param offset  Offset of expression in internal expression array
	 * @return	CspBooleanExpr at the index of the offset
	 */
	public CspBooleanExpr getExpression(int offset);
	
	/**
     * Returns the number of expressions that are wrapped by this generic node
     * @return number of expressions that are wrapped by this generic node
     */
    public int getExpressionCount() ;
	
	
	
}
