package jopt.js.spi.search;

import jopt.csp.spi.search.LocalSearchImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.js.api.search.JsLocalSearch;

public class JsLocalSearchImpl extends LocalSearchImpl implements JsLocalSearch {
    
    public JsLocalSearchImpl(ConstraintStore store) {
        super(store);
    }
}
