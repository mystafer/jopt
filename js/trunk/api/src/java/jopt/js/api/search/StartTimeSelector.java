package jopt.js.api.search;

import jopt.js.api.variable.Activity;


/**
 * Interface to implement to control the selection of start time values
 * when attempting to assign a start time to an activity
 */
public interface StartTimeSelector {
	
    /**
	 * Returns a start time to try to assign to given activity
     * 
	 * @param act activity from which to select a start time
	 * @return the start time selected for this activity
	 */
    public int select(Activity act);
}