package jopt.csp.variable;

/**
 * Interface for CspGenericLongConstant objects
 * 
 * @author Jim Boerkoel
 */
public interface CspGenericLongConstant extends CspGenericNumConstant {
    
    /**
     * Returns the number at the current index value as a Long object
     * @return	Long object representing the index currently being referenced to
     */
    public Long getLongForIndex();

    /**
     * Returns all the Long objects wrapped by this generic constant
     * @return an array of all the Long objects being wrapped by this generic constant
     */
    public Long[] getLongConstants();

    /**
     * Returns the minimal value of all the constant values as a Long object
     * @return returns a Long with the minimum value of all the constant values
     */
    public Long getMin();

    /**
     * Returns the maximum value of all the constant values as a Long object
     * @return returns a Long with the maximum value of all the constant values
     */
    public Long getMax();
}