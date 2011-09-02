/*
 * IntDomain.java
 * 
 * Created on Aug 12, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by integer domains
 */
public interface IntDomain extends NumDomain {
	/**
	 * Returns maximum value of domain
	 */
	public int getMax();

	/**
	 * Returns minimum value of domain
	 */
	public int getMin();

	/**
	 * Returns true if value is in domain
	 */
	public boolean isInDomain(int val);

	/**
	 * Attempts to set the maximum value of the domain.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMax(int val) throws PropagationFailureException;

	/**
	 * Attempts to set the minimum value of the domain.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMin(int val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a single value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setValue(int val) throws PropagationFailureException;

	/**
	 * Attempts to remove a single value from the domain
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeValue(int val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to within a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setRange(int start, int end) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain by removing a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeRange(int start, int end)
			throws PropagationFailureException;

	/**
	 * Returns the next higher value in the domain or current value if none
	 * exists
	 */
	public int getNextHigher(int val);

	/**
	 * Returns the next lower value in the domain or current value if none
	 * exists
	 */
	public int getNextLower(int val);
}