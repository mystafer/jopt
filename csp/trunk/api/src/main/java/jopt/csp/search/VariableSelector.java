package jopt.csp.search;

import jopt.csp.variable.CspVariable;

/**
 * Interface to implement to control the selection of a variable
 * when attempting to generate a possible solution to a set of variables.
 */
public interface VariableSelector {
	/**
	 * Initialize the variables from which to select
	 * @param vars	Array of variables
	 */
	public void setVariables(CspVariable vars[]);

	/**
	 * Returns true if there is one or more variables that 
	 * have not been selected.
	 * @return	true if more variables to select, false otherwise
	 */
	public boolean hasNext();

	/**
	 * Selects the next variable from a set of variables.
	 * @return	Selected CspVariable
	 */
	public CspVariable next();

	/**
	 * Resets the selectors set of variables
	 */
	public void reset();
}
