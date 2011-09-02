package jopt.csp.variable;

/**
 * Interface for CspGenericFloatConstant objects
 * 
 * @author Jim Boerkoel
 */
public interface CspGenericFloatConstant extends CspGenericNumConstant {

    /**
     * Returns the number at the current index value as a Float object
     * @return	Float object referenced by the current index
     */
    public Float getFloatForIndex();

    /**
     * Returns all the Float objects wrapped by this generic constant
     * @return	array of all Float objects wrapped by this generic constant
     */
    public Float[] getFloatConstants();

    /**
     * Returns the minimal value of all the constant values as a Float object
     * @return	Float object representing the minimum value of all the constant values
     */
    public Float getMin();

    /**
     * Returns the maximum value of all the constant values as a Float object
     * @return	Float object representing the maximum value of all the constant values
     */
    public Float getMax();
}