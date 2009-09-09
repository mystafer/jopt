package jopt.js.api.variable;

import jopt.csp.variable.CspAlgorithm;


public interface JsAlgorithm extends CspAlgorithm {

    /**
     * Returns the variable factory for this algorithm
     * @return a variable factory used to create variables for expressions, constraints, etc.
     */
    public JsVariableFactory getJsVarFactory();
	
}
