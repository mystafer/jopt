package jopt.csp.spi.solver;

/**
 * @author Chris Johnson
 */
public interface VariableChangeSource {
	/**
     * Adds a listener interested in variable change events
     */
	public void addVariableChangeListener(VariableChangeListener listener);
	
	/**
     * Removes a variable listener from this variable
     */
	public void removeVariableChangeListener(VariableChangeListener listener);
}
