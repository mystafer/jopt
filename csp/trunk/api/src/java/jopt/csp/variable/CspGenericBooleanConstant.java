package jopt.csp.variable;

/**
 * Interface for CspGenericBooleanConstant objects
 * 
 * @author jboerkoel
 */
public interface CspGenericBooleanConstant extends CspGenericConstant {

    /**
     * Returns the boolean value currently being referenced by the index
     * @return boolean value of the Boolean object currently being referenced by index
     */
    public boolean getBooleanForIndex();

    /**
     * Returns true if any of the Boolean constants are false
     * @return	true if any of the generic boolean constants are false
     */
    public boolean isAnyFalse();

    /**
     * Returns true if any of the Boolean constants are true
     * @return	true if any of the generic boolean constants are true
     */
    public boolean isAnyTrue();

    /**
     * Returns a String representing the name of this constant
     * @return	string representing name of this generic boolean constant
     */
    public String getName();

    /**
     * Returns all the constants wrapped by this generic constant
     * return	boolean array of all the boolean constants
     */
    public boolean[] getBooleanConstants();

    /**
     * Returns whether or not the specified boolean is one of the constants contained in this generic constant
     * @param	bool	boolean value to check if any of the constant values are equivalent to
     * @return	true if one of the constants is equivalent to the value of bool
     */
    public boolean contains(boolean bool);
    
    /**
     * Generates a CspGenericBooleanConstant based on only one boolean value
     * @param	bool	value of the genericBooleanConstant
     * @return	A generic boolean constant with only one value
     */
    public CspGenericBooleanConstant generateBooleanConstant(boolean bool);
    
    /**
     * Returns a CspGenericBooleanConstant based on the specified indices
     * @param	fragIndices		indices over which to create the fragment
     * @return  generic boolean constant over the indices passed in the fragIndices[]
     */
    public CspGenericBooleanConstant createFragment(CspGenericIndex fragIndices[]);
}