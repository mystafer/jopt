package jopt.js.spi.search.actions;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.ResourceSet;


/**
 * Action that removes a resource from an alternative resource set
 */
public class RemoveAlternativeResourceAction extends AbstractSearchNodeAction {
    
	private ResourceSet altResSet;
    private int resourceIndex;
	
    /**
     * Creates a RemoveAlternativeResourceAction
     * @param altResSet set of resources
     * @param resourceIndex index into the set of the resource to remove
     */
	public RemoveAlternativeResourceAction(ResourceSet altResSet, int resourceIndex) {
		this.altResSet = altResSet;
		this.resourceIndex = resourceIndex;
	}
	
    /**
     * Sets the resource set from which a resource will be removed
     * @param ars the resource set
     */
	public void setAssignAlternativeResourceSet(ResourceSet ars) {
		this.altResSet = ars;
	}
	
    /**
     * Sets the index into the resource set that will be removed
     * @param resourceIndex the index
     */
	public void setResourceIndex(int resourceIndex) {
		this.resourceIndex = resourceIndex;
	}
	
    /**
     * Removes the resource at resource index from the resource set
     * @return null as there is no further action necessary
     * @throws PropagationFailureException
     */
	public SearchAction performAction() throws PropagationFailureException {
		altResSet.removeAt(resourceIndex);
		return null;
	}
	
	public String toString() {
		return "remove-resource(" + altResSet + ") to resource index: "+resourceIndex;
	}
}