package jopt.csp.variable;

/**
 * Interface for CspGenericConstant objects
 * @author jboerkoel
 */
public interface CspGenericConstant {
    
    /**
     * Returns the set of indices used to index this constant
     * @return	array of indices that this generic constant is indexed over
     */
    public CspGenericIndex[] getIndices();

    /**
     * Returns the number of constants in this generic constant
     * @return	number of constants in this generic constant
     */
    public int getConstantCount();

    /**
     * Returns true if this generic constant contains the given index
     * @return true if this generic constant contains the given index
     */
    public boolean containsIndex(CspGenericIndex index);

    /**
     * Displays the name of this generic constant along with its indices
     * @return	string representing the name of this generic constant
     */
    public String toString();
    
    /**
     * Set the indices used to index the constants to represent the specified offset
     * @param	offset	offset to which to set the indices
     */
    public void setIndicesToNodeOffset(int offset);
}