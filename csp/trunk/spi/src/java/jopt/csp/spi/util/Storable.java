/*
 * Created on Feb 9, 2005
 */
package jopt.csp.spi.util;

/**
 * This interface should be implemented by variables that are capable of
 * storing state information to an object and then restoring their state
 * from that object at a later point in time.  This state information is
 * domain information.
 * 
 * @author Chris Johnson
 */
public interface Storable {
	/**
	 * Returns a string that can later be used to reference
	 * the stored data a later point
	 */
	public String getName();
	
	/**
	 * Stores appropriate data for future restoration.
	 */
	public Object getState();
	
	/**
	 *  Restores variable information from stored data.
	 */
	public void restoreState(Object state);
}
