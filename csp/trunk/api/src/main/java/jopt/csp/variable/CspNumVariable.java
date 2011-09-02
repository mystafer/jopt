/*
 * Created on Mar 4, 2005
 */
package jopt.csp.variable;

/**
 * This interface should be implemented by all numeric variables that 
 * allow their domain properties to be accessed and/or modified. 
 *
 * @author Chris Johnson
 */
public abstract interface CspNumVariable extends CspVariable, CspNumExpr {
}
