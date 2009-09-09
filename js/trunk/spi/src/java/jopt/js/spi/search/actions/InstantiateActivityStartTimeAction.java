package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.StartTimeSelector;
import jopt.js.api.variable.Activity;

/**
 * Action that will "instantiate" an activity by assigning it a start time.
 */
public class InstantiateActivityStartTimeAction extends AbstractSearchNodeAction {
    private Activity activity;
    private StartTimeSelector selector;
    
    /**
     * Creates a new instantiate activity start time action
     * @param activity activity whose start time will be assigned
     * @param selector selects which start time to assign to the activity
     */
    public InstantiateActivityStartTimeAction(Activity activity, StartTimeSelector selector) {
        this.activity = activity;
        this.selector = selector;
    }
    
    /**
     * Attempts to assign a start time to the activity
     * @return the next step in the search process
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (activity.isBound()) return null;
        
        // retrieve value of variable to restrict domain
        int nextVal=0;
        if (selector != null) {
        	nextVal = selector.select(activity);
        }
        else {
        	nextVal = activity.getEarliestStartTime();	
        }
        
        // return choice to assign the value
        // or remove it
        return choice(new AssignActivityStartTimeAction(activity, nextVal),
                      combineActions(new RemoveActivityStartTimeAction(activity, nextVal), this));
    }
    
    public String toString() {
        return "instantiate(" + activity + ")";
    }
}
