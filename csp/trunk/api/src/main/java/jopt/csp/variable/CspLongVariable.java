package jopt.csp.variable;

import jopt.csp.util.LongSet;


/**
 * Interface implemented by long integer based variables.  
 * A domain is associated with this type of variable that can be modified
 * unlike CspLongExpr objects.
 */
public interface CspLongVariable extends CspLongExpr, CspNumVariable {
	/**
     * Returns the maximum value of this variable's domain
     * @return maximum value of this variable's domain
     */
    public long getMax();
    
    /**
     * Returns the minimum value of this variable's domain
     * @return minimum value of this variable's domain
     */
    public long getMin();
    
    /**
     * Returns true if the specified value is in this variable's domain
     * @param	val		value to check if it is in this variable's domain
     * @return	true if val is present in this variable's domain
     */
    public boolean isInDomain(long val);
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @param	val		long value to set as the domain's maximum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMax(long val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified minimum value.
     * @param	val		long value to set as the domain's minimum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMin(long val) throws PropagationFailureException;
    
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @param	val		long value to set as the domain's only value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setValue(long val) throws PropagationFailureException;
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @param	val		long value to remove from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeValue(long val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @param	set set of values to remove from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeAll(LongSet set) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @param start	long value to set as the domain's minimum value
     * @param end long value to set as the domain's maximum value
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setRange(long start, long end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @param start		minimum value of the range of values to be removed from the domain 
     * @param end		maximum value of the range of values to be removed from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeRange(long start, long end) throws PropagationFailureException;
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     * @param	val		value of which to obtain the next higher
     * @return	the next value in the domain that is that is higher than val in the domain, if one is present
     * 			else returns the same number back
     */
    public long getNextHigher(long val);
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     * @param	val		value of which to obtain the next lower
     * @return	the next value in the domain that is lower than val in the domain, if one is present
     * 			else returns the same number back
     */
    public long getNextLower(long val);
}