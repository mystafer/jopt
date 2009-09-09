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
 * Action to assign resources and then start times to a list of activities
 */
public class GenerateActivityResourceThenStartTimeAction extends AbstractSearchNodeAction {
    
    private Activity[] activities;
    private InstantiateActivityResouceThenStartTimeAction[] actions;
    private StartTimeSelector startTimeSelector;
    private ResourceSelector resSelector;
    private ResourceSetSelector resSetSelector;
    private ActivitySelector activitySelector;
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], StartTimeSelector startTimeSelector) {
        this.activities = activities;
        this.startTimeSelector = startTimeSelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[]) {
    	this.activities = activities;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ResourceSelector resSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param resSetSelector a selector to choose which resource set should be assigned first given a set of them
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], StartTimeSelector startTimeSelector,
    												ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param resSetSelector a selector to choose which resource set should be assigned first given a set of them
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[],ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
    	this.activities = activities;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector) {
        this.activities = activities;
        actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
        this.startTimeSelector = startTimeSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector) {
    	this.activities = activities;
    	actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
    	this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector, ResourceSelector resSelector) {
    	this.activities = activities;
    	actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector, ResourceSelector resSelector) {
    	this.activities = activities;
    	actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
        this.resSelector = resSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param startTimeSelector a selector to choose which start time should be assigned first
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param resSetSelector a selector to choose which resource set should be assigned first given a set of them
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector, StartTimeSelector startTimeSelector,
    												ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
    	this.activities = activities;
    	actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
        this.startTimeSelector = startTimeSelector;
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Creates a new generate activity then resource start action
     * @param activities list of activities from which to choose
     * @param resSelector a selector to choose which resource should be assigned given an alternate resource set
     * @param resSetSelector a selector to choose which resource set should be assigned first given a set of them
     * @param activitySelector selects which activity to bind first
     */
    public GenerateActivityResourceThenStartTimeAction(Activity activities[], ActivitySelector activitySelector, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
    	this.activities = activities;
    	actions = new InstantiateActivityResouceThenStartTimeAction[activities.length];
        this.resSelector = resSelector;
        this.resSetSelector = resSetSelector;
        this.activitySelector = activitySelector;
    }
    
    /**
     * Attempts to assign resources and then start times to activities
     * @return the next step in the search process
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
    	if(activitySelector == null) {
    		// build list of instantiate actions for variables
    		LinkedList instantiateActions = new LinkedList();
    		for (int i=0; i<activities.length; i++) {
    			Activity activity = activities[i];
    			
    			// don't bother instantiating a variable that is already bound
    			if (!activity.isBound())
    				instantiateActions.add(new InstantiateActivityResouceThenStartTimeAction(activity,startTimeSelector,resSelector,resSetSelector));
    		}
    		
    		// if only one action, return it
    		if (instantiateActions.size() == 1)
    			return (SearchAction) instantiateActions.get(0);
    		
    		// if more than one action return a combined action
    		if (instantiateActions.size( )> 0)
    			return combineActions(instantiateActions);
    		
    		// no variables need to be instantiated
    		return null;
    	}
    	else {
    		int nextActivityIndex = activitySelector.selectIndex(activities);
    		if (nextActivityIndex < 0) {
    			return null;
    		}
    		while (activities[nextActivityIndex].isBound()) {
    			nextActivityIndex = activitySelector.selectIndex(activities);
    			if (nextActivityIndex < 0) {
        			return null;
        		}
    		}
    		
    		if (actions[nextActivityIndex] == null) {
    			actions[nextActivityIndex] = new InstantiateActivityResouceThenStartTimeAction(activities[nextActivityIndex], startTimeSelector, resSelector, resSetSelector);
    		}
    		
    		return combineActions(actions[nextActivityIndex],this);
    	}
    }

    public String toString() {
        return "generate(" + activities + ")";
    }
    
}
