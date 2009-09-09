package jopt.js.spi.search.actions;

import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.ActivitySelector;
import jopt.js.api.search.ResourceSelector;
import jopt.js.api.search.ResourceSetSelector;
import jopt.js.api.search.StartTimeSelector;
import jopt.js.api.variable.Activity;

/**
 * Action to assign start times and then resources to a list of activities
 */
public class GenerateActivityStartTimeThenResourceAction extends AbstractSearchNodeAction {
    
    private Activity[] activities;
    private StartTimeSelector startTimeSelector;
    private ResourceSelector resSelector;
    private ResourceSetSelector resSetSelector;
    private ActivitySelector activitySelector;

    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], StartTimeSelector startTimeSelector) {
        this.activities = activities;
        this.startTimeSelector = startTimeSelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[]) {
    	this.activities = activities;
    }

    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     * @param resSelector selects which resource should be bound given an alternative resource set
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param resSelector selects which resource should be bound given an alternative resource set
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ResourceSelector resSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param resSetSelector  selects which resource set to bind first given an array of resource sets
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], StartTimeSelector startTimeSelector, 
    													ResourceSelector resSelector,ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param resSetSelector  selects which resource set to bind first given an array of resource sets
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[],ResourceSelector resSelector,ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector) {
        this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector) {
    	this.activities = activities;
    	this.activitySelector = activitySelector;
    }

    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector, ResourceSelector resSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param startTimeSelector selects which of the next start times should be selected next
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param resSetSelector  selects which resource set to bind first given an array of resource sets
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector, 
    													ResourceSelector resSelector,ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Create a new generate start time then resource action 
     * @param activities list of activities needing to be bound
     * @param resSelector selects which resource should be bound given an alternative resource set
     * @param resSetSelector  selects which resource set to bind first given an array of resource sets
     * @param activitySelector selects which activity should be bound first (next)
     */
    public GenerateActivityStartTimeThenResourceAction(Activity activities[], ActivitySelector activitySelector, ResourceSelector resSelector,ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Attempts to assign start times and then resources to activities
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
	            if (!activity.isBound())
	            	instantiateActions.add(new InstantiateActivityStartTimeThenResourceAction(activity, startTimeSelector, resSelector, resSetSelector));
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
    		while (nextActivity.isBound()) {
    			nextActivity = activitySelector.select(activities);
    		}
    		return combineActions(new InstantiateActivityStartTimeThenResourceAction(nextActivity, startTimeSelector, resSelector, resSetSelector), this);
    	}
    
    }

    public String toString() {
        return "generate(" + activities + ")";
    }
    
}
