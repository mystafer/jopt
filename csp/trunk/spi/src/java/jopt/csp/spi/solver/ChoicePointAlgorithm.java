package jopt.csp.spi.solver;

import jopt.csp.variable.CspAlgorithm;


/**
 * Base class for Generic Arc Consistency algorithms that use
 * a node or arc queue for propagation
 */
public interface ChoicePointAlgorithm extends CspAlgorithm {
    /**
     * Sets the choicepoint stack associated with this algorithm.
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps);

    /**
     * Returns the current state of the problem
     */
    public Object getProblemState();
    
    /**
     * Restores the current state of the problem
     */
    public void restoreProblemState(Object state);
}