package jopt.csp.variable;

import java.util.Set;

/**
 * Base interface class for set based variables
 */
public abstract interface CspSetVariable extends CspVariable{
	/**
	 * Returns the name of the variable
	 * @return name of this variable
	 */
	public String getName();

	/**
	 * Sets the name of the variable
	 * @param	name	new name for this variable
	 */
	public void setName(String name);
	
	/**
     * Returns the possible set of values in this variable's domain
     * @return	set of possible values
     */
    public Set getPossibleSet();

    /**
     * Returns the required set of values in this variable's domain
     * @return set of required values
     */
    public Set getRequiredSet();

    /**
     * Returns the cardinality of the set of possible values
     * @return number of items in the possible set
     */
    public int getPossibleCardinality();
    
    /**
     * Returns the cardinality of the set of required values
     * @return number of items in required set
     */
    public int getRequiredCardinality();

    /**
     * Returns 1 + (cardinality of possible) - (cardinality of required)
     * @return 1 + (cardinality of possible) - (cardinality of required)
     */
    public int getSize();

	/**
	 * Adds a required value to the set of required values
	 * @param	req		object to add to required set
	 */
    public void addRequired(Object req) throws PropagationFailureException;
    
	/**
	 * Returns true if the specified value is in this variable's domain
	 * @param	val		object being inquired about
	 * @return		true if val is in domain
	 */
	public boolean isInDomain(Object val);

	/**
	 * Returns true if the specified value is in the set of required values
	 * @param	value		object being inquired about
	 * @return				true if val is set of required
	 */
	public boolean isRequired(Object value);

	/**
	 * Returns true if the specified value is in the set of possible values
	 * @param	value		object being inquired about
	 * @return				true if val is set of required
	 */
	public boolean isPossible(Object value);

	/**
	 * Removes a value from the possible set
	 *  @param	value		object to be removed from possible
	 */
	public void removePossible(Object value) throws PropagationFailureException;
    
	/**
	 * Removes a set of values from the possible set
	 * @param	values		set of value to be removed from possible
	 */
	public void removePossible(Set values) throws PropagationFailureException;
	
	/**
	 * Adds a set of required values
	 * @param	req		set of value to be added to required
	 */
    public void addRequired(Set req) throws PropagationFailureException;	
	
}