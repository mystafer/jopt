package jopt.csp.variable;

/**
 * Interface for CspGenericDoubleConstant objects
 * 
 * @author Jim Boerkoel
 */
public interface CspGenericDoubleConstant extends CspGenericNumConstant {
    
    /**
     * Returns the number at the current index value as a Double object
     * @return Double object referenced by the current index
     */
    public Double getDoubleForIndex();
    
    /**
     * Returns all the Double objects wrapped by this generic constant
     * @return	array of all Double objects wrapped by this generic constant
     */
    public Double[] getDoubleConstants();
    
    /**
     * Returns the minimal value of all the constant values as a Double object
     * @return	Double object representing the minimum value of all the constant values
     */
    public Double getMin();
    
    /**
     * Returns the maximum value of all the constant values as a Double object
     * @return	Double object representing the maximum value of all the constant values
     */
    public Double getMax();
}