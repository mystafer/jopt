package jopt.js.spi.domain.listener;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface that can be implemented by a class that wishes to be notified of changes to the 
 * activity domain
 * 
 * @author James Boerkoel
 */
public interface ActivityDomainListener {

	public static final int START = 0;
	public static final int END = 1;
	public static final int DURATION = 2;
	
	/**
	 * Note that these will only be called if a resource is set and a change has affected the actual use of a resource
	 */
	
	/**
	 * This reports any domain reduction action that occurs, the entity that was changed, and the interval that was removed
     * @throws PropagationFailureException
	 */
	public void domainReductionAction(int entityChanged, int start, int end) throws PropagationFailureException;
	
    /**
	 * Occurs when any sort of domain change occurs
	 * @throws PropagationFailureException
	 */
	public void fireAnyAction() throws PropagationFailureException;
	
}
