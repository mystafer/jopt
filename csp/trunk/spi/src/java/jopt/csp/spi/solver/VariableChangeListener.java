package jopt.csp.spi.solver;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface to implement in order to receive events generated by variables
 *
 * @author Chris Johnson
 */
public interface VariableChangeListener {
	/**
	 * Method invoked by a variable when a variable change event is fired
	 */
	public void variableChange(VariableChangeEvent ev) throws PropagationFailureException;
}