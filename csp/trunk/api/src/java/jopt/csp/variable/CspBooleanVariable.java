package jopt.csp.variable;

public interface CspBooleanVariable extends CspBooleanExpr, CspVariable {
    /**
     * Sets the domain value to true
     */
    public void setTrue() throws PropagationFailureException;
    
    /**
     * Sets the domain value to false
     */
    public void setFalse() throws PropagationFailureException;
}
