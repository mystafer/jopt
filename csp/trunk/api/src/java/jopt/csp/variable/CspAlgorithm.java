package jopt.csp.variable;


/**
 * Interface implemented by propagation algorithms supported by the CspSolver.
 * Such algorithms do not use a tree search but work only with the variables,
 * constraints and domains--generally with the goal of domain reduction.  Current
 * implementations are all arc-consistency algorithms.
 * 
 * @author Nick Coleman
 */
public interface CspAlgorithm {
    /**
     * Adds a variable to be managed by this algorithm.  The variable need not
     * be a part of any posted constraint.
     * @param var	the variable to be added to and managed by the algorithm
     */
    public void addVariable(CspVariable var);
    
	/**
	 * Adds a constraint to be managed by this algorithm
	 * @param constraint	constraint that is to be added to and managed by the algorithm
	 */
	public void addConstraint(CspConstraint constraint);

	/**
	 * Propagates constraints added to algorithm reducing domains of
	 * variables to which constraints are applied.
	 *
	 * @throws PropagationFailureException      If unable to successfully propagate constraints
	 */
	public void propagate() throws PropagationFailureException;

    /**
     * Returns the variable factory for this algorithm
     * @return a variable factory used to create variables for expressions, constraints, etc.
     */
    public CspVariableFactory getVarFactory();
    
}