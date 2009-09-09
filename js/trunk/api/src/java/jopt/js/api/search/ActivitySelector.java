package jopt.js.api.search;

import jopt.js.api.variable.Activity;


/**
 * Interface to implement to control the selection of which activity to schedule next while searching
 * over a list of activities
 */
public interface ActivitySelector {
	
    /**
	 * A method that will select one activity from an array of activities
	 * @param activities The group of activities from which one is selected
	 * @return The activity that has been selected
	 */
    public Activity select(Activity[] activities);

    /**
	 * A method that will select the index of one activity from an array of activities
	 * @param activities The group of activities from which the index of one is selected
	 * @return The index of the activity that has been selected
	 */
    public int selectIndex(Activity[] activities);
}