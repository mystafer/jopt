package jopt.js.api.search;

import jopt.js.api.variable.ResourceSet;


/**
 * Interface to implement to control the selection of which resources should be
 * tried when a set of alternatives are all possible.  Used in searching.
 */
public interface ResourceSelector {
	
    /**
	 * Returns the index of the resource within the specified set that should be selected
     * for assignment to / removal from an activity
	 *
     * @param altResSet A set of resources to choose from
	 * @return index into the Alternate Resource Set, indicating which resource in the set should be selected
	 */
    public int select(ResourceSet altResSet);
}