package jopt.csp.variable;

import jopt.csp.util.FloatSet;


/**
 * Interface implemented by float precision floating point variables.  
 * The domain associated with this type of variable that be modified
 * unlike CspFloatExpr objects.
 */
public interface CspFloatVariable extends CspFloatExpr, CspNumVariable {
	/**
     * Returns the maximum value of this variable's domain
     * @return the maximum value of this variable's domain
     */
    public float getMax();
    
    /**
     * Returns the minimum value of this variable's domain
     * @return the minimum value of this variable's domain
     */
    public float getMin();
    
    /**
     * Returns true if the specified value is in this variable's domain
     * @param	val		value to check if it is in domain
     * @return	returns true if specified value is in this variable's domain
     */
    public boolean isInDomain(float val);
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     * @param	val	value to become the domains new maximum
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMax(float val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     *
     * @param	val	value to become the domains new minimum
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMin(float val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @param	val	single value to reduce this variable's domain to
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setValue(float val) throws PropagationFailureException;
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @param	val	value to remove from this variable's domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeValue(float val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @param	set set of values to remove from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeAll(FloatSet set) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *	@param	start	value to set as this variable's domain minimum
     *  @param	end		value to set as this variable's domain maximum
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setRange(float start, float end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     *	@param	start	minimum value of range of values being removed
     *  @param	end		maximum value of range of values being removed
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeRange(float start, float end) throws PropagationFailureException;
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     * 
     * @param	val		current value
     * @return	the value of the next higher entry in this variable's domain
     */
    public float getNextHigher(float val);
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     * @param	val		current value
     * @return	the value of the next lower entry in this variable's domain
     */
    public float getNextLower(float val);
}