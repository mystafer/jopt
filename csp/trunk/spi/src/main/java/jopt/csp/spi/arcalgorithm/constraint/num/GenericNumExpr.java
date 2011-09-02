package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.util.GenericIndex;


public interface GenericNumExpr extends NumExpr {
    /**
     * Returns the internal variable corresponding to the associated index's current value
     */
    public NumExpr getNumExpressionForIndex();
    
    /**
     * Returns the number of expressions that are wrapped by this generic node
     */
    public int getExpressionCount();
    
    /**
     * returns a numeric expression from the internal array
     * 
     * @param offset  Offset of expression in internal expression array
     */
    public NumExpr getNumExpression(int offset);

    /**
     * Returns the generic indices that is associated with this expression
     */
    public GenericIndex[] getGenericIndices();
    
    /**
     * Returns true if this expression contains the given index
     */
    public boolean containsIndex(GenericIndex index);
    
    /**
     * Returns that largest maximal value of all variables in array
     */
    public Number getNumLargestMax();
    
    /**
     * Returns that largest maximal value of all variables in array within
     * start and end indices
     */
    public Number getNumLargestMax(int start, int end);

    /**
     * Returns that smallest maximal value of all variables in array
     */
    public Number getNumSmallestMax();
    
    /**
     * Returns that smallest maximal value of all variables in array within
     * start and end indices
     */
    public Number getNumSmallestMax(int start, int end);
    
    /**
     * Returns that largest minimal value of all variables in array
     */
    public Number getNumLargestMin();
    
    /**
     * Returns that largest minimal value of all variables in array within
     * start and end indices
     */
    public Number getNumLargestMin(int start, int end);
    
    /**
     * Returns that smallest minimal value of all variables in array
     */
    public Number getNumSmallestMin();
    
    /**
     * Returns that smallest minimal value of all variables in array within
     * start and end indices
     */
    public Number getNumSmallestMin(int start, int end);

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
    public NumExpr createFragment(GenericIndex fragIndices[]);
}