package jopt.csp.spi.search.actions;

import java.util.Arrays;
import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.search.VariableSelector;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will generate values for an array of variables.  It tries to
 * bind each variable to a single value such that all constraints are satisfied
 * and finishes searching upon finding the first consistent assignment.
 */
public class GenerateFloatAction extends AbstractSearchNodeAction {
	private CspFloatVariable vars[];
	private float precision;
	private boolean minFirst;
	private VariableSelector varSelector;

	/**
	 * Creates new generation action
	 * 
	 * @param vars          Variable to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 */
	public GenerateFloatAction(CspFloatVariable vars[], float precision) {
		this(vars, precision, true, null);
	}

	/**
	 * Creates new generation action
	 * 
	 * @param vars          Variable to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 * @param varSelector	Used to select next variable to instantiate
	 */
	public GenerateFloatAction(CspFloatVariable vars[], float precision, boolean minFirst, VariableSelector varSelector) {
		this.vars = vars;
		this.minFirst = minFirst;
		this.precision = precision;
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
				CspFloatVariable var = (CspFloatVariable) varSelector.next();

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new InstantiateFloatAction(var, precision, minFirst));
				}
			}
			varSelector.reset();
		}
		else {
			for (int i=0; i<vars.length; i++) {
				CspFloatVariable var = vars[i];

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new InstantiateFloatAction(var, precision, minFirst));
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

	public String toString() {
		return "generate(" + Arrays.asList(vars) + ")";
	}
}
