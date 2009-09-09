/*
 * Created on Feb 9, 2005
 */
package jopt.csp.spi.util;

import java.util.Arrays;

import jopt.csp.variable.CspVariable;

/**
 * A container class that stores domain information for variables in a solution
 * to a constraint satisfaction problem.  It is responsible for requesting state
 * (i.e. domain) information, storing it locally and then restoring this stored
 * information to variables when <code>restoreState()</code> is called. 
 *
 * @author Chris Johnson
 */
public class StateStore {

	protected Storable storedVars[];
	protected Object storedState[];
	
	/**
	 * Automatically stores the data for each <code>Storable</code> in the vars array.
	 * 
	 * @param vars An array of <code>Storable</code> objects for which we need to retain information
	 */
	public StateStore(Storable[] vars) {
		this.storedVars = vars;
		this.storedState = new Object[vars.length];
		this.storeState();
	}
	
    /**
     * Automatically stores the data for each <code>Storable</code> in the vars array.
     * 
     * @param vars An array of <code>Storable</code> objects for which we need to retain information
     */
    public StateStore(CspVariable[] vars) {
        this((Storable[]) Arrays.asList(vars).toArray(new Storable[vars.length]));
    }
    
	/**
	 * Stores the necessary information for each <code>Storable</code> in this <code>StateStore</code>
	 */
	public void storeState() {
		for(int i=0; i<storedVars.length; i++) {
			storedState[i] = storedVars[i].getState();
		}
	}
	
	/**
	 * Restores the state information stored in this particular <code>StateStore</code>
	 */
	public void restoreState() {
		for(int i=0; i<storedVars.length; i++) {
			storedVars[i].restoreState(storedState[i]);
		}
	}
	
	/**
	 * Retrieves the array of Storables whose information is present in this StateStore.
	 * 
	 * @return An array of <code>Storable</code> objects indicating which variables
	 * have state information stored in this <code>StateStore</code>
	 */
	public Storable[] getStoredVars() {
		return storedVars;
	}
}
