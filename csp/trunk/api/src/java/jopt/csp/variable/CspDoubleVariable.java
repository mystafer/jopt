package jopt.csp.variable;

import jopt.csp.util.DoubleSet;


/**
 * Interface implemented by double precision floating point variables.  
 * The domain associated with this type of variable can be modified
 * unlike CspDoubleExpr objects.
 */
public interface CspDoubleVariable extends CspDoubleExpr, CspNumVariable {
	/**
     * Returns the maximum value of this variable's domain
     * @return	the maximum value of this variable's domain
     */
    public double getMax();
    
    /**
     * Returns the minimum value of this variable's domain
     * @return the minimum value of this variable's domain
     */
    public double getMin();
    
    /**
     * Returns true if the specified value is in this variable's domain
     * @param	val		value that is to be checked for its presence in the domain
     * @return	boolean representing the presence of the val in the domain 
     */
    public boolean isInDomain(double val);
    
    /**
     * Attempts to reduce this variable's domain to be less than
     * the specified maximum value.
     *
     *@param	val		value to be set as the maximum of the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMax(double val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to be greater than
     * the specified minimum value.
     * @param	val		value to be set as the minimum of the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setMin(double val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to a single value.
     *
     * @param	val		value to restrict this variable's domain to
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setValue(double val) throws PropagationFailureException;
    
    /**
     * Attempts to remove a single value from this variable's domain
     *
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeValue(double val) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain to within a range of values
     *
     * @param	start	value to be set as the minimum of this variable's domain
     * @param	end		value to be set as the maximum of this variable's domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void setRange(double start, double end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a range of values
     *
     * @param	start	minimum value of range of values to be removed
     * @param	end		maximum value of range of values to be removed
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeRange(double start, double end) throws PropagationFailureException;
    
    /**
     * Attempts to reduce this variable's domain by removing a set of values
     *
     * @param	set set of values to remove from the domain
     * @throws PropagationFailureException      If this would cause the
     * domain to become empty
     */
    public void removeAll(DoubleSet set) throws PropagationFailureException;
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     * @param	val		value of the current value
     * @return	double representing the next higher value
     */
    public double getNextHigher(double val);
    
    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     * @param	val		value of the current value
     * @return	double representing the next lower value
     */
    public double getNextLower(double val);
}