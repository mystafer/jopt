package jopt.csp.spi.arcalgorithm.graph.node;

import java.util.Set;

import jopt.csp.variable.PropagationFailureException;

public interface SetNode extends Node {
    /**
     * Returns true if value is required
     */
    public boolean isRequired(Object value);
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(Object value);
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(Object value) throws PropagationFailureException;

    /**
     * Removes a value from the possible set
     */
    public void removePossible(Object value) throws PropagationFailureException;
    
    /**
     * Returns possible set of values
     */
    public Set getPossibleSet() ;

    /**
     * Returns required set of values
     */
    public Set getRequiredSet();

    /**
     * Returns the possible-delta set
     */
    public Set getPossibleDeltaSet();

    /**
     * Returns the required-delta set
     */
    public Set getRequiredDeltaSet();
    
    /**
     * Returns possible cardinality
     */
    public int getPossibleCardinality();
    
    /**
     * Returns required cardinality
     */
    public int getRequiredCardinality();
}
