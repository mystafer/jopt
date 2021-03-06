package jopt.csp.spi.search.actions;

import java.util.Arrays;
import java.util.LinkedList;

import jopt.csp.search.SearchAction;
import jopt.csp.search.VariableSelector;
import jopt.csp.spi.search.tree.AbstractSearchNodeAction;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Action that will generate values for an array of variables
 */
public class SplitGenerateIntegerAction extends AbstractSearchNodeAction {
	private CspIntVariable vars[];
	private boolean minFirst;
	private VariableSelector varSelector;

	/**
	 * Creates new generation action
	 * 
	 * @param vars          Variable to instantiate
	 */
	public SplitGenerateIntegerAction(CspIntVariable vars[]) {
		this(vars, true, null);
	}

	/**
	 * Creates new generation action
	 * 
	 * @param vars          Variable to instantiate
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 */
	public SplitGenerateIntegerAction(CspIntVariable vars[], boolean minFirst, VariableSelector varSelector) {
		this.vars = vars;
		this.minFirst = minFirst;
		this.varSelector = varSelector;
	}

	/**
	 * Called by search tree to execute this action.
	 * 
	 * @return Next action to execute in search
	 */
	public SearchAction performAction() throws PropagationFailureException {
		// build list of instantiate actions for variables
		LinkedList<SplitInstantiateIntegerAction> instantiateActions = new LinkedList<SplitInstantiateIntegerAction>();
		if(varSelector != null) {
			varSelector.reset();
			varSelector.setVariables(vars);
			while(varSelector.hasNext()) {
				CspIntVariable var = (CspIntVariable) varSelector.next();

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new SplitInstantiateIntegerAction(var, minFirst));
				}
			}
		}
		else {
			for (int i=0; i<vars.length; i++) {
				CspIntVariable var = vars[i];

				// don't bother instantiating a variable that is already bound
				if (!var.isBound()) {
					instantiateActions.add(new SplitInstantiateIntegerAction(var, minFirst));
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
		return "split-instantiate(" + Arrays.asList(vars) + ")";
	}

}
