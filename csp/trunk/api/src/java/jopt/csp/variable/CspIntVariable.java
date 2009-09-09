package jopt.csp.variable;

import jopt.csp.util.IntSet;


/**
 * Interface implemented by integer based variables.  
 * The domain that is associated with this type of variable can be modified
 * unlike CspIntExpr objects.
 */
public interface CspIntVariable extends CspIntExpr, CspNumVariable {
	/**
     * Returns the maximum value of this variable's domain
     * @return maximum value of this variable's domain
     */
    public int getMax();
    
    /**
     * Returns the minimum value of this variable's domain
     * @return minimum value of this variable's domain
     */
    public int getMin();
    
	/**
     * Returns true if the specified value is in this variable's domain
     * @param	val		value to check if it is in the domain
     * @return	true if value is present in the domain
     */
    public boolean isInDomain(int val);
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     *@param	val		value to set as the domain's maximum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMax(int val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified minimum value.
     *
     * @param	val		value to set as the domain's minimum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMin(int val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     * 
     * @param	val		value to reduce the domain to
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setValue(int val) throws PropagationFailureException;
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @param	val		value to remove from variable's domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeValue(int val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @param	start	value to set as domain's minimum value
     * @param	end		value to set as domain's maximum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setRange(int start, int end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @param	start	minimum value of range to remove
     * @param	end		maximum value of range to remove
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeRange(int start, int end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @param	set set of values to remove from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeAll(IntSet set) throws PropagationFailureException;
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     * @param	val		value of which to obtain the next higher
     * @return	the next value in the domain that is that is higher than val in the domain, if one is present
     * 			else returns the same number back 
     */
    public int getNextHigher(int val);
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     * @param	val		value of which to obtain the next lower
     * @return	the next value in the domain that is lower than val in the domain, if one is present
     * 			else returns the same number back
     */
    public int getNextLower(int val);
}