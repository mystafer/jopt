package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.Activity;


/**
 * Action that assigns a single start time to an activity and then assigns the smallest duration
 */
public class AssignActivityStartTimeAction implements SearchAction {
    private Activity activity;
    private int val;
    
    /**
     * Creates an AssignActivityStartTimeAction
     * @param activity activity being assigned
     * @param val value being assigned as the start value
     */
    public AssignActivityStartTimeAction(Activity activity, int val) {
    	this.activity = activity;
        this.val = val;
    }
    
    /**
     * Sets the value that will be used as the start time for the associated activity
     * @param val the value
     */
    public void setVal(int val) {
    	this.val = val;
    }
    
    /**
     * Assigns the given value to be the start time of the given activity
     * @return null as there is no further action necessary
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
    	activity.setStartTime(val);
   		activity.setDurationMax(activity.getDurationMin()); 
        return null;
    }
    
    public String toString() {
    	return "assign-number(" + activity + ", " + val + ")";
    }
}