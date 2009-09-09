package jopt.csp.spi.search.actions;

import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SetSelector;
import jopt.csp.search.VariableSelector;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will generate values for an array of variables.  It tries to
 * bind each variable to a single value such that all constraints are satisfied
 * and finishes searching upon finding the first consistent assignment.
 */
public class GenerateSetAction extends AbstractSearchNodeAction {
	private CspSetVariable vars[];
	private SetSelector selector;
	private VariableSelector varSelector;

	/**
	 * Creates new generation action
	 * 
	 * @param vars  Variable to instantiate
	 */
	public GenerateSetAction(CspSetVariable vars[]) {
		this(vars, null, null);
	}

	/**
	 * Creates new generation action
	 * 
	 * @param vars      Variable to instantiate
	 * @param selector  Used to select next value to reduce domain of variable
	 */
	public GenerateSetAction(CspSetVariable vars[], SetSelector selector, VariableSelector varSelector) {
		this.vars = vars;
		this.selector = selector;
		this.varSelector = varSelector;
	}

	/**
	 * Called by search tree to execute this action.
	 * 
	 * @return Next action to execute in search
	 */
	public SearchAction performAction() throws PropagationFailureException {
		// build list of instantiate actions for variables
		LinkedList instantiateActions = new LinkedList();
		if(varSelector != null) {
			varSelector.reset();
			varSelector.setVariables(vars);
			while(varSelector.hasNext()) {
				CspSetVariable var = (CspSetVariable) varSelector.next();

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new InstantiateSetAction(var, selector));
				}
			}
			varSelector.reset();
		}
		else {
			for (int i=0; i<vars.length; i++) {
				CspSetVariable var = vars[i];

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new InstantiateSetAction(var, selector));
				}
			}
		}
		// if only one action, return it
		if (instantiateActions.size()==1)
			return (SearchAction) instantiateActions.get(0);

		// if more than one action return a combined action
		if (instantiateActions.size()>0)
			return combineActions(instantiateActions);

		// no variables need to be instantiated
		return null;
	}
}
