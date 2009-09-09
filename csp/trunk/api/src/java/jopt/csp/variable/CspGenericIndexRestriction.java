/*
 * CspRangeRestriction.java
 * 
 * Created on Apr 29, 2005
 */
package jopt.csp.variable;

/**
 * Used during creation of constraints on generic variables to restrict ranges
 * and combinations of indices 
 */
public interface CspGenericIndexRestriction {
    
    /**
     * Returns true if the indices this restriction covers currently represent
     * a valid combination of values
     * @return	true if the current values of the indices represent a "valid" value
     */
	public boolean currentIndicesValid();
}
