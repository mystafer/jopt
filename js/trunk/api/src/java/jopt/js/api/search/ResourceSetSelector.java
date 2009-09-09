package jopt.js.api.search;

import jopt.js.api.variable.ResourceSet;


/**
 * Interface to implement to control the selection of which resource set should be
 * assigned first
 */
public interface ResourceSetSelector {
	
    /**
	 * Allows users to define a method of selecting an alternative ResourceSet from an array of such sets.
	 * 
     * @param altResSets An array of resource sets to choose from
	 * @return the alternative resource set chosen
	 */
    public ResourceSet select(ResourceSet[] altResSets);
}