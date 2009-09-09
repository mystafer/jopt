/*
 * Created on Mar 4, 2005
 */
package jopt.csp.variable;


/**
 * This interface should be implemented by all variables that allow their domain
 * properties to be accessed and/or modified.
 *
 * @author Chris Johnson
 */
public interface CspVariable {
    /**
     * Returns the name of this variable
     * @return	name of this variable
     */
    public String getName();

    /**
     * Returns the size of this variable's domain
     * @return	size of this variable's domain
     */
    public int getSize();

    /**
     * Returns true if this variable's domain is bound to a single value
     * @return true if variable's domain is bound to a single value
     */
    public boolean isBound();
}
