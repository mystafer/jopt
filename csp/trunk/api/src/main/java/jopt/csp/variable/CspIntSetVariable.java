package jopt.csp.variable;


/**
 * Interface implemented by variables representing a set of integer values
 */
public interface CspIntSetVariable extends CspSetVariable<Integer> {
	/**
	 * Returns true if the specified value is in this variable's domain
	 * @param	val		value to check if it is in the domain
	 * @return	true if value is in domain
	 */
	public boolean isInDomain(int val);

	/**
	 * Returns true if the specified value is required
	 * @param	value	value to check if it is required
	 * @return	true if value is required
	 */
	public boolean isRequired(int value);

	/**
	 * Returns true if the specified value is possible
	 * @param	value	value to verify if it is possible
	 * @return true if value is possible
	 */
	public boolean isPossible(int value);

	/**
	 * Adds a required value to the set
	 * @param	value	value to add to the required set
	 */
	public void addRequired(int value) throws PropagationFailureException;

	/**
	 * Removes a value from the possible set
	 * @param	value	value to remove from the required set
	 */
	public void removePossible(int value) throws PropagationFailureException;
}