package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.util.GenericIndex;


public interface GenericBoolExpr extends BoolExpr {
    /**
     * Returns the internal variable corresponding to the associated index's current value
     */
    public BoolExpr getBoolExpressionForIndex();
    
    /**
     * Returns the number of expressions that are wrapped by this generic expression
     */
    public int getExpressionCount();
    
    /**
     * returns a boolean expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public BoolExpr getBoolExpression(int offset);

    /**
     * Returns the generic indices that is associated with this expression
     */
    public GenericIndex[] getGenericIndices();
    
    /**
     * Returns true if this expression contains the given index
     */
    public boolean containsIndex(GenericIndex index);

    /**
     * Returns a fragment of this expression given an array of indices that
     * represent the portion of the fragment.  If the indices for the fragment
     * cover all indices this expression is based upon, the specific expression
     * specified will be returned. If the indices do not cover all indices, a 
     * generic variable will be returned based upon the remaining indices in this
     * expression composed of all the variables selected by the fragment indices.
     * 
     * @param fragIndices   Indices that represent the fragment to return.  Each index
     *                          must have a current value for this function to work correctly
     * @return Numeric expression that is a fragment of this expression 
     */
    public BoolExpr createFragment(GenericIndex fragIndices[]);

    /**
     * Returns true if any expression in the array evaluates to false
     */
    public boolean isAnyFalse();
    
    /**
     * Returns true if any expression in the array evaluates to false within
     * start and end indices
     */
    public boolean isAnyFalse(int s, int e);
    
    /**
     * Returns true if all expressions in the array evaluate to false
     */
    public boolean isAllFalse();
    
    /**
     * Returns true if all expressions in the array evaluate to false within
     * start and end indices
     */
    public boolean isAllFalse(int s, int e);
    
    /**
     * Returns true if any expression in the array evaluates to false
     */
    public boolean isAnyTrue();
    
    /**
     * Returns true if any expression in the array evaluates to true within
     * start and end indices
     */
    public boolean isAnyTrue(int s, int e);
    
    /**
     * Returns true if all nodes in the array evaluate to true
     */
    public boolean isAllTrue();
    
    /**
     * Returns true if all expressions in the array evaluate to true within
     * start and end indices
     */
    public boolean isAllTrue(int s, int e);
}