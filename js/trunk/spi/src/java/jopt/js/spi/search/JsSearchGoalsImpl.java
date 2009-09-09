package jopt.js.spi.search;

import jopt.csp.spi.search.SearchGoalsImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspVariableFactory;
import jopt.js.api.search.JsSearchGoals;
import jopt.js.api.variable.JsVariableFactory;

public class JsSearchGoalsImpl extends SearchGoalsImpl implements JsSearchGoals {
	
	public JsSearchGoalsImpl(JsVariableFactory varFact, ConstraintStore store) {
        super((CspVariableFactory)varFact, store);
    }
    
}
