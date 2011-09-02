package jopt.csp.solution;

import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Interface implemented by all variable solutions
 */
public interface VariableSolution extends Cloneable {
    /**
     * Returns variable solution is based upon
     */
    public CspVariable getVariable();
    
	/**
	 * Returns true if variable is bound in solution
	 */
	public boolean isBound();
    
    /**
     * Causes variable solution to capture certain information about it's variable
     * and store it locally so that it can be restored later.  History
     * is not kept: calling this method overwrites any previous information
     * captured from previous calls to this method.
     */
    public void store();

    /**
     * Restores (certain) recorded information to the associated variable.  This
     * is the information stored when the <code>store()</code> was most recently
     * called.
     */
    public void restore() throws PropagationFailureException;

    // javadoc inherited from Object
    public Object clone();
}