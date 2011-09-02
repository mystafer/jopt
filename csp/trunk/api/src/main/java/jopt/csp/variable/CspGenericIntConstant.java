package jopt.csp.variable;

/**
 * Interface for CspGenericIntConstant objects
 * 
 * @author Jim Boerkoel
 */
public interface CspGenericIntConstant extends CspGenericNumConstant{
    
    /**
     * Returns the number at the current index value as an Integer object
     * @return	Integer object currently being indexed
     */
    public Integer getIntegerForIndex();
    
    /**
     * Returns all the Integer objects wrapped by this generic constant
     * @return array of all Integer objects wrapped by this generic constant
     */
    public Integer[] getIntegerConstants();
    
    /**
     * Returns the minimal value of all the constant values as an Integer object
     * @return Integer object representing the minimum value of all the constant values
     */
    public Integer getMin();
    
    /**
     * Returns the maximum value of all the constant values as an Integer object
     * @return Integer object representing the maximum value of all the constant values
     */
    public Integer getMax();
}