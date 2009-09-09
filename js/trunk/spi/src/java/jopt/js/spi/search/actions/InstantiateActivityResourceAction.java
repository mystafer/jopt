package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.search.ResourceSelector;
import jopt.js.api.search.ResourceSetSelector;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.ResourceSet;

/**
 * Action that will "instantiate" an activity by assigning it a resource.
 */
public class InstantiateActivityResourceAction extends AbstractSearchNodeAction {
    private Activity activity;
    private ResourceSetSelector resSetSelector;
    private ResourceSelector resSelector;
    //TODO :eventually add a selector, for now we will just use est!
    
    /**
     * Creates a new instantiate activity resource action
     * @param activity activity to which resources will be assigned
     * @param resSelector selects which resource to assign an alternative resource set
     * @param resSetSelector selects which resource set to assign first
     */
    public InstantiateActivityResourceAction(Activity activity, ResourceSelector resSelector, ResourceSetSelector resSetSelector) {
        this.activity = activity;
        this.resSetSelector = resSetSelector;
        this.resSelector = resSelector;
    }
    
    /**
     * Attempts to a assign a resource to an activity
     * @return the next step in the search process
     * @throws PropagationFailureException
     */
    public SearchAction performAction() throws PropagationFailureException {
        if (activity.isBound()) return null;
        
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
    	//If there is only one alternative resource set, we simply use it as the next set to choose among
    	// or if there is no resource set selector, we simply use the first 
    	else  {
    		ars = altResSets[0];
    	}
    	
    	//Next we select which of the given resources in the alternative resource set we would like to select 
    	//as the resource actually used
    	int resourceIndex = 0;
    	//If there is a selector, use it to find at which resource should try to assign first, else
    	//we will just use the first one (the one at index 0)
    	if (resSelector != null) {
    		resourceIndex = resSelector.select(ars);
    	}
    	
    	//if a start time has already been assigned, we then want to assign a resource
    	AssignAlternativeResourceAction aara = new AssignAlternativeResourceAction(ars, resourceIndex);
    	RemoveAlternativeResourceAction rara = new RemoveAlternativeResourceAction(ars, resourceIndex);
    	//we either assign a resource, or if it fails, remove that resource and try again
    	return choice(combineActions(aara, this), combineActions(rara, this));
    }
    
    public String toString() {
        return "instantiate(" + activity + ")";
    }
}
