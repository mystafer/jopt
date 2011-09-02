/*
 * BooleanDomain.java
 * 
 * Created on Jun 1, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by boolean domains
 */
public interface BooleanDomain extends IntDomain {
	/**
	 * Returns true if a value is contained in this node's domain
	 */
	public boolean isInDomain(boolean val);

	/**
	 * Attempts to reduce domain to a true value
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setTrue() throws PropagationFailureException;

	/**
	 * Attempts to reduce domain to a false value
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setFalse() throws PropagationFailureException;

	/**
	 * Returns true if this domain is bound to a true value
	 */
	public boolean isTrue();

	/**
	 * Returns true if this domain is bound to a false value
	 */
	public boolean isFalse();
}