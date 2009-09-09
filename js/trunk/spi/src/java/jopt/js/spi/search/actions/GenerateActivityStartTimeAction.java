package jopt.js.spi.search.actions;

import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.ActivitySelector;
import jopt.js.api.search.StartTimeSelector;
import jopt.js.api.variable.Activity;

/**
 * Action to assign start times to a list of activities
 */
public class GenerateActivityStartTimeAction extends AbstractSearchNodeAction {
    private Activity[] activities;
    private StartTimeSelector startTimeSelector;
    private ActivitySelector activitySelector;
    
    /**
     * Creates a new generate start activity
     * @param activities activities that need to have the start time set
     * @param startTimeSelector the selector that chooses which start time should be tried next
     */
    public GenerateActivityStartTimeAction(Activity activities[], StartTimeSelector startTimeSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
    }
    
    /**
     * Creates a new generate start activity
     * @param activities activities that need to have the start time set
     */
    public GenerateActivityStartTimeAction(Activity activities[]) {
        this.activities = activities;
    }
    
    
    /**
     * Creates a new generate start activity
     * @param activities activities that need to have the start time set
     * @param startTimeSelector the selector that chooses which start time should be tried next
     * @param activitySelector selects which activity to instantiate next
     */
    public GenerateActivityStartTimeAction(Activity activities[], ActivitySelector activitySelector,StartTimeSelector startTimeSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate start activity
     * @param activities activities that need to have the start time set
     * @param activitySelector selects which activity to instantiate next
     */
    public GenerateActivityStartTimeAction(Activity activities[], ActivitySelector activitySelector) {
        this.activities = activities;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Attempts to assign start times to activities
     * @return the next step in the search process
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
    	if (activitySelector == null) {
	        // build list of instantiate actions for variables
	        LinkedList instantiateActions = new LinkedList();
	        for (int i=0; i<activities.length; i++) {
	        	Activity activity = activities[i];
	            
	            // don't bother instantiating a variable that is already bound
	            if (activity.getEarliestStartTime() != activity.getLatestStartTime())
	            	instantiateActions.add(new InstantiateActivityStartTimeAction(activity, startTimeSelector));
	        }
	        
	        // if only one action, return it
	        if (instantiateActions.size() == 1)
	            return (SearchAction) instantiateActions.get(0);
	        
	        // if more than one action return a combined action
	        if (instantiateActions.size() > 0)
	            return combineActions(instantiateActions);
	        
	        // no variables need to be instantiated
	        return null;
    	}
    	else {
    		Activity nextActivity = activitySelector.select(activities);
    		while (nextActivity.getEarliestStartTime() == nextActivity.getLatestStartTime()) {
    			nextActivity = activitySelector.select(activities);
    		}
    		return combineActions(new InstantiateActivityStartTimeAction(nextActivity, startTimeSelector), this);
    	}
    }

    public String toString() {
        return "generate(" + activities + ")";
    }
    
}
