package jopt.csp.variable;

/**
 * Interface implemented by constraints that are added to the
 * CspSolver
 */
public interface CspConstraint {
    /**
     * Returns true if constraint cannot be satisfied
     * @return true if constraint cannot be satisfied;
     * 		   false if constraint could still be satisfied
     */
    public boolean isFalse();
    
    /**
     * Returns true if constraint cannot be dissatisfied
     * @return true if constraint cannot be dissatisfied;
     * 		   false if constraint could still be dissatisfied
     */
    public boolean isTrue();

    /**
     * Returns a boolean expression that wraps this constraint
     * @return	returns boolean expression that evaluates to true of the constraint is true
     * 			and false when the constraint is false
     */
    public CspBooleanExpr toBoolean();
}