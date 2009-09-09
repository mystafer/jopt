package jopt.csp.spi.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import jopt.csp.search.IntegerSelector;
import jopt.csp.search.LongSelector;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SetSelector;
import jopt.csp.search.VariableSelector;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.actions.AddConstraintAction;
import jopt.csp.spi.search.actions.GenerateDoubleAction;
import jopt.csp.spi.search.actions.GenerateFloatAction;
import jopt.csp.spi.search.actions.GenerateIntegerAction;
import jopt.csp.spi.search.actions.GenerateLongAction;
import jopt.csp.spi.search.actions.GenerateSetAction;
import jopt.csp.spi.search.actions.LookAheadAction;
import jopt.csp.spi.search.actions.NonBinaryGenerateIntegerAction;
import jopt.csp.spi.search.actions.RestoreSolutionAction;
import jopt.csp.spi.search.actions.SplitGenerateIntegerAction;
import jopt.csp.spi.search.actions.SplitGenerateLongAction;
import jopt.csp.spi.search.actions.StoreSolutionAction;
import jopt.csp.spi.search.tree.ChoicePoint;
import jopt.csp.spi.search.tree.CombinedAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariable;

/**
 * Creates and returns common search actions that can be used
 * to locate solutions to a CSP problem.  Also adds variables
 * to the constraint store.
 */
public class SearchActionsImpl implements SearchActions {
	private ConstraintStore store;

	public SearchActionsImpl(ConstraintStore store) {
		this.store = store;
	}

	//clone constructor
	protected SearchActionsImpl(SearchActionsImpl sai) {
		this.store = sai.store;
	}

	/**
	 * Adds all variables to the constraint store to ensure their states are
	 * maintained correctly while searching
	 */
	private void addVarsToCs(CspVariable vars[]) {
		for (int i=0; i<vars.length; i++)
			store.addVariable(vars[i], true);
	}

	/**
	 * Adds all variables to the constraint store to ensure their states are
	 * maintained correctly while searching
	 */
	private void addVarsToCs(Collection vars) {
		Iterator varIter = vars.iterator();
		while (varIter.hasNext())
			store.addVariable((CspVariable) varIter.next(), true);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspIntVariable vars[]) {
		addVarsToCs(vars);
		return new GenerateIntegerAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspIntVariable vars[], IntegerSelector selector) {
		addVarsToCs(vars);
		return new GenerateIntegerAction(vars, selector, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspIntVariable vars[], IntegerSelector selector, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new GenerateIntegerAction(vars, selector, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspLongVariable vars[]) {
		addVarsToCs(vars);
		return new GenerateLongAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspLongVariable vars[], LongSelector selector) {
		addVarsToCs(vars);
		return new GenerateLongAction(vars, selector, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspLongVariable vars[], LongSelector selector,VariableSelector varSelector) {
		addVarsToCs(vars);
		return new GenerateLongAction(vars, selector, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspFloatVariable vars[], float precision) {
		addVarsToCs(vars);
		return new GenerateFloatAction(vars, precision, true, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspFloatVariable vars[], float precision, boolean minFirst) {
		addVarsToCs(vars);
		return new GenerateFloatAction(vars, precision, minFirst, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspFloatVariable vars[], float precision, boolean minFirst, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new GenerateFloatAction(vars, precision, minFirst, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspDoubleVariable vars[], double precision) {
		addVarsToCs(vars);
		return new GenerateDoubleAction(vars, precision, true, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspDoubleVariable vars[], double precision, boolean minFirst) {
		addVarsToCs(vars);
		return new GenerateDoubleAction(vars, precision, minFirst, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspDoubleVariable vars[], double precision, boolean minFirst, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new GenerateDoubleAction(vars, precision, minFirst, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction generateNonBinary(CspIntVariable vars[]) {
		addVarsToCs(vars);
		return new NonBinaryGenerateIntegerAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction generateNonBinary(CspIntVariable vars[], VariableSelector varSelector) {
		addVarsToCs(vars);
		return new NonBinaryGenerateIntegerAction(vars,varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspIntVariable vars[]) {
		addVarsToCs(vars);
		return new SplitGenerateIntegerAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspIntVariable vars[], boolean minFirst) {
		addVarsToCs(vars);
		return new SplitGenerateIntegerAction(vars, minFirst,null);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspIntVariable vars[], boolean minFirst, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new SplitGenerateIntegerAction(vars, minFirst,varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspLongVariable vars[]) {
		addVarsToCs(vars);
		return new SplitGenerateLongAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspLongVariable vars[], boolean minFirst) {
		addVarsToCs(vars);
		return new SplitGenerateLongAction(vars, minFirst, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction splitGenerate(CspLongVariable vars[], boolean minFirst, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new SplitGenerateLongAction(vars, minFirst, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspSetVariable vars[]) {
		addVarsToCs(vars);
		return new GenerateSetAction(vars);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspSetVariable vars[], SetSelector selector) {
		addVarsToCs(vars);
		return new GenerateSetAction(vars, selector, null);
	}

	// javadoc inherited from SearchActions
	public SearchAction generate(CspSetVariable vars[], SetSelector selector, VariableSelector varSelector) {
		addVarsToCs(vars);
		return new GenerateSetAction(vars, selector, varSelector);
	}

	// javadoc inherited from SearchActions
	public SearchAction storeSolution(SolverSolution solution) {
		return new StoreSolutionAction(store, solution);
	}

	// javadoc inherited from SearchActions
	public SearchAction restoreSolution(SolverSolution solution) {
		addVarsToCs(solution.variables());
		return new RestoreSolutionAction(store, solution);
	}

	// javadoc inherited from SearchActions
	public SearchAction addConstraint(CspConstraint constraint) {
		return new AddConstraintAction(store, constraint);
	}

	// javadoc inherited from SearchActions
	public SearchAction choice(SearchAction action1, SearchAction action2) {
		return new ChoicePoint(action1, action2);
	}

	// javadoc inherited from SearchActions
	public SearchAction choice(SearchAction actions[]) {
		return new ChoicePoint(Arrays.asList(actions));
	}

	// javadoc inherited from SearchActions
	public SearchAction combine(SearchAction action1, SearchAction action2) {
		return new CombinedAction(action1, action2);
	}

	// javadoc inherited from SearchActions
	public SearchAction combine(SearchAction action1, SearchAction action2, SearchAction action3) {
		return new CombinedAction(action1, action2, action3);
	}

	// javadoc inherited from SearchActions
	public SearchAction combine(SearchAction actions[]) {
		return new CombinedAction(Arrays.asList(actions));
	}

	// javadoc inherited from SearchActions
	public SearchAction lookAhead(SearchAction pseudoChange, SearchAction successAction, SearchAction failureAction) {
		return new LookAheadAction(store, pseudoChange, successAction, failureAction);
	}
}
