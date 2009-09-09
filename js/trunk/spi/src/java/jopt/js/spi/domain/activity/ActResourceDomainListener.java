package jopt.js.spi.domain.activity;

import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of intervals from an ActResourceDomain
 */
public interface ActResourceDomainListener {
	/**
	 * A method called by an ActResourcedDomin any time start times are removed
	 * @param resourceID id of the resource for which the start times were removed
	 * @param delta the IntIntervalSet representing the values removed 
	 * @throws PropagationFailureException
	 */
	public void startTimesRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException;
	
	/**
	 * A method called by an ActResourcedDomin any time end times are removed
	 * @param resourceID id of the resource for which the start times were removed
	 * @param delta the IntIntervalSet representing the values removed 
	 * @throws PropagationFailureException
	 */
	public void endTimesRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException;
  	
	/**
	 * A method called by an ActResourcedDomin any time duration times are removed
	 * @param resourceID id of the resource for which the start times were removed
	 * @param delta the IntIntervalSet representing the values removed 
	 * @throws PropagationFailureException
	 */
	public void durationRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException;
  	
	/**
	 * A method called by an ActResourcedDomin any changes are made to any part of the resource
	 * @param resourceID id of the resource for which the start times were removed
	 * @param delta the ActDelta representing the values removed 
	 * @throws PropagationFailureException
	 */
	public void deltaRemoved(int resourceID, ActDelta delta) throws PropagationFailureException;
}
