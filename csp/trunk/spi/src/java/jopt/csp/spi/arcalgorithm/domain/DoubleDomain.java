/*
 * DoubleDomain.java
 * 
 * Created on Aug 12, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by double domains
 */
public interface DoubleDomain extends NumDomain {
    /**
     * Returns maximum value of domain
     */
    public double getMax();

    /**
     * Returns minimum value of domain
     */
    public double getMin();
    
	/**
	 * Returns true if value is in domain
	 */
	public boolean isInDomain(double val);

	/**
	 * Attempts to reduce domain to a maximum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMax(double val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a minimum value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMin(double val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a single value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setValue(double val) throws PropagationFailureException;

	/**
	 * Attempts to remove a single value from the domain
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeValue(double val) throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to within a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setRange(double start, double end)
			throws PropagationFailureException;

	/**
	 * Attempts to reduce domain by removing a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeRange(double start, double end)
			throws PropagationFailureException;

	/**
	 * Returns the next higher value in the domain or current value if none
	 * exists
	 */
	public double getNextHigher(double val);

	/**
	 * Returns the next lower value in the domain or current value if none
	 * exists
	 */
	public double getNextLower(double val);

	/**
	 * Sets precision of set
	 */
	public void setPrecision(double precision);

	/**
	 * Returns precision of set
	 */
	public double getPrecision();
}