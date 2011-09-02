/*
 * LongDomain.java
 * 
 * Created on Aug 12, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by long domains
 */
public interface LongDomain extends NumDomain {
	/**
	 * Returns maximum value of domain
	 */
	public long getMax();

	/**
	 * Returns minimum value of domain
	 */
	public long getMin();

	/**
	 * Returns true if value is in domain
	 */
	public boolean isInDomain(long val);

	/**
	 * Attempts to reduce domain to a maximum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMax(long val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a minimum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMin(long val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a single value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setValue(long val) throws PropagationFailureException;

	/**
	 * Attempts to remove a single value from the domain
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeValue(long val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to within a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setRange(long start, long end)
			throws PropagationFailureException;

	/**
	 * Attempts to reduce domain by removing a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeRange(long start, long end)
			throws PropagationFailureException;

	/**
	 * Returns the next higher value in the domain or current value if none
	 * exists
	 */
	public long getNextHigher(long val);

	/**
	 * Returns the next lower value in the domain or current value if none
	 * exists
	 */
	public long getNextLower(long val);
}