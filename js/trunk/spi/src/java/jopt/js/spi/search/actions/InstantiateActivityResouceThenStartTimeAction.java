package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.ResourceSelector;
import jopt.js.api.search.ResourceSetSelector;
import jopt.js.api.search.StartTimeSelector;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.ResourceSet;

/**
 * Action that will "instantiate" an activity by assigning it a resource and then a start time.  
 * If no selector is specified, the earliest available start time of the activity will be used.
 */
public class InstantiateActivityResouceThenStartTimeAction extends AbstractSearchNodeAction {
    private Activity activity;
    private StartTimeSelector startTimeSelector;
    private ResourceSetSelector resSetSelector;
    private ResourceSelector resSelector;
    
    private AssignActivityStartTimeAction aasta;
    private RemoveActivityStartTimeAction rasta;
    private AssignAlternativeResourceAction aara;
    private RemoveAlternativeResourceAction rara;
    
    /**
     * Creates new instantiate activity resource then start time action
     * @param activity       	Activity to instantiate
     * @param startTimeSelector	selector to select what start time should be tried next
     * @param resSelector 		selects which resource to try to assign next
     * @param resSetSelector	selects which resource set should be instantiated next
     */
    public InstantiateActivityResouceThenStartTimeAction(Activity activity, StartTimeSelector startTimeSelector,
    											ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
        this.activity = activity;
        this.startTimeSelector = startTimeSelector;
        this.resSetSelector = resSetSelector;
        this.resSelector = resSelector;
        aasta = new AssignActivityStartTimeAction(activity, Integer.MIN_VALUE);
        rasta = new RemoveActivityStartTimeAction(activity, Integer.MIN_VALUE);
        aara = new AssignAlternativeResourceAction(null, Integer.MIN_VALUE);
        rara = new RemoveAlternativeResourceAction(null, Integer.MIN_VALUE);
    }
    
    /**
     * Assigns a resource and start time to each activity
     * @return the next step in the search process
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (activity.isBound()) return null;
        
        if (activity.getNumUnassignedOperations() > 0){
        	//First we choose an alternative resource set to work with
        	ResourceSet[] altResSets = activity.getAllAlternativeResourceSets();
        	
        	//Here we choose the alternative resource set to work with if there are more than one.  If a selector is specified
        	//we will use the selector to make the selection, else we will use the first one in the array
        	ResourceSet ars;
        	//If there are no alternative sets that need to be chosen, we simply return null
        	if (altResSets.length == 0) {
        		return null;
        	}
        	else if ((altResSets.length>1)&&(resSetSelector!=null)) {
        		ars = resSetSelector.select(altResSets);
        	}
        	//If there is only one alternative resource set, we simply choose it as the next set to choose among
        	// or if there is no resource set selector, we simply use the first 
        	else  {
        		ars = altResSets[0];
        	}
        	
        	//Next we select which of the given resources in the alternative resource set we would like to select 
        	// as the resource actually used
        	int resourceIndex = 0;
        	//If there is a selector, use it to find at which resource should try to assign first, else
        	//we will just use the first one (the one at index 0)
        	if (resSelector != null) {
        		resourceIndex = resSelector.select(ars);
        	}
        	
        	//if a start time has already been assigned, we then want to assign a resource
        	aara.setAssignAlternativeResourceSet(ars);
        	aara.setResourceIndex(resourceIndex);
        	rara.setAssignAlternativeResourceSet(ars);
        	rara.setResourceIndex(resourceIndex);
        	//we either assign a resource, or if it fails, remove that resource and try again
        	return choice(combineActions(aara, this), combineActions(rara, this));
        }
        
        else  {
	        // retrieve value of variable to restrict domain
        	int nextVal = 0;
        	if (startTimeSelector != null) {
        		nextVal = startTimeSelector.select(activity);
        	}
        	else {
        		nextVal = activity.getEarliestStartTime();	
        	}
	        
	        aasta.setVal(nextVal);
	        rasta.setVal(nextVal);
        	
	        // return choice to assign the value
	        // or remove it
	        return choice((aasta), combineActions(rasta, this));
        }
    }
    
    public String toString() {
        return "instantiate(" + activity + ")";
    }
}
