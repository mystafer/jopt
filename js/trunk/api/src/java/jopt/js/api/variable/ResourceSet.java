package jopt.js.api.variable;

import jopt.csp.variable.PropagationFailureException;

/**
 * This is a class to represent a set of resources that is possible for a given operation (require, produces, etc) of an activity
 * and allows selecting one of them as the resource to actually be used.
 * 
 * @author jboerkoel
 */
public interface ResourceSet {

	/**
	 * Returns the number of equivalent resources (ie the number of resources in this set)
	 * @return the number of possible resources that can be used in place of each other
	 */
	public int size() ;
	
	/**
	 * Returns a representation of the resource at the specified index.
	 * @param index index of resource being inquired about
	 * @return Resource located at specified index
	 */
	public Resource get(int index);
	
	/**
	 * Assigns the activity to use the resource at the given index
	 * @param index index of resource
	 */
	public void assignTo(int index) throws PropagationFailureException;
	
	/**
	 * Removes the resource at the given index for the activity
	 * @param index index of resource
	 */
	public void removeAt(int index) throws PropagationFailureException;

}