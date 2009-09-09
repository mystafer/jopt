package jopt.js.spi.search;

import jopt.csp.spi.solver.ConstraintStore;
import jopt.js.api.search.JsLocalSearch;
import jopt.js.api.search.JsSearchActions;
import jopt.js.api.search.JsSearchGoals;
import jopt.js.api.search.JsSearchManager;
import jopt.js.api.search.JsSearchTechniques;
import jopt.js.api.variable.JsVariableFactory;

public class JsSearchManagerImpl implements JsSearchManager {
    private JsVariableFactory varFact;
    private ConstraintStore store;
    private JsSearchActions actions;
    private JsSearchGoals goals;
    private JsSearchTechniques techniques;
    private JsLocalSearch localSearch;
    
    public JsSearchManagerImpl(JsVariableFactory varFact, ConstraintStore store) {
        this.varFact = varFact;
        this.store = store;
    }
    
	// javadoc inherited from SearchManager
    public JsSearchActions getSearchActions() {
        if (actions == null) actions = new JsSearchActionsImpl(store);
        return actions;
    }

    // javadoc inherited from SearchManager
    public JsSearchGoals getSearchGoals() {
        if (goals == null) goals = new JsSearchGoalsImpl(varFact, store);
        return goals;
    }

    // javadoc inherited from SearchManager
    public JsSearchTechniques getSearchTechniques() {
        if (techniques == null) techniques = new JsSearchTechniquesImpl(store);
        return techniques;
    }

    // javadoc inherited from SearchManager
    public JsLocalSearch getLocalSearch() {
        if (localSearch == null) localSearch = new JsLocalSearchImpl(store);
        return localSearch;
    }
}
