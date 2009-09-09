package jopt.js.spi.search.actions;

import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.ActivitySelector;
import jopt.js.api.search.ResourceSelector;
import jopt.js.api.search.ResourceSetSelector;
import jopt.js.api.variable.Activity;

/**
 * Action to assign resources to a list of activities
 */
public class GenerateActivityResourceAction extends AbstractSearchNodeAction {
    private Activity[] activities;
    private ResourceSelector resSelector;
    private ResourceSetSelector resSetSelector;
    private ActivitySelector activitySelector;
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     * @param resSelector the resource selector
     * @param resSetSelector the resource set selector
     */
    public GenerateActivityResourceAction(Activity activities[], ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
        this.activities = activities;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
    }
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     * @param resSelector the resource selector
     */
    public GenerateActivityResourceAction(Activity activities[], ResourceSelector resSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
    }
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     */
    public GenerateActivityResourceAction(Activity activities[]) {
    	this.activities = activities;
    }
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     * @param resSelector the resource selector
     * @param resSetSelector the resource set selector
     * @param activitySelector selects which activity should be instantiated next
     */
    public GenerateActivityResourceAction(Activity activities[], ActivitySelector activitySelector,ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
        this.activities = activities;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     * @param resSelector the resource selector
     * @param activitySelector selects which activity should be instantiated next
     */
    public GenerateActivityResourceAction(Activity activities[], ActivitySelector activitySelector, ResourceSelector resSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a generate activity resource action
     * @param activities list of activities to bind
     * @param activitySelector selects which activity should be instantiated next
     */
    public GenerateActivityResourceAction(Activity activities[], ActivitySelector activitySelector) {
    	this.activities = activities;
    	this.activitySelector = activitySelector;
    }
    
    /**
     * Attempts to assign resources to activities
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
	            if (activity.getNumUnassignedOperations() > 0)
	            	instantiateActions.add(new InstantiateActivityResourceAction(activity, resSelector, resSetSelector));
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
    		while (nextActivity.getNumUnassignedOperations() <= 0) {
    			nextActivity = activitySelector.select(activities);
    		}
    		return combineActions(new InstantiateActivityResourceAction(nextActivity, resSelector, resSetSelector), this);
    	}
    }

    public String toString() {
        return "generate(" + activities + ")";
    }
    
}
