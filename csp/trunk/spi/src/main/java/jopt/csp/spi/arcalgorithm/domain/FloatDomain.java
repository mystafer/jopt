/*
 * FloatDomain.java
 * 
 * Created on Aug 12, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by float domains
 */
public interface FloatDomain extends NumDomain {
	/**
	 * Returns maximum value of domain
	 */
	public float getMax();

	/**
	 * Returns minimum value of domain
	 */
	public float getMin();

	/**
	 * Returns true if value is in domain
	 */
	public boolean isInDomain(float val);

	/**
	 * Attempts to reduce domain to a maximum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMax(float val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a minimum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMin(float val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a single value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setValue(float val) throws PropagationFailureException;

	/**
	 * Attempts to remove a single value from the domain
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeValue(float val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to within a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setRange(float start, float end)
			throws PropagationFailureException;

	/**
	 * Attempts to reduce domain by removing a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeRange(float start, float end)
			throws PropagationFailureException;

	/**
	 * Returns the next higher value in the domain or current value if none
	 * exists
	 */
	public float getNextHigher(float val);

	/**
	 * Returns the next lower value in the domain or current value if none
	 * exists
	 */
	public float getNextLower(float val);

	/**
	 * Sets precision of set
	 */
	public void setPrecision(float precision);

	/**
	 * Returns precision of set
	 */
	public float getPrecision();
}