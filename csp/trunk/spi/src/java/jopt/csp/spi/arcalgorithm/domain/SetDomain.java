package jopt.csp.spi.arcalgorithm.domain;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for all set based interfaces
 */
public interface SetDomain extends Domain {
    /**
     * Returns possible set of values
     */
    public Set getPossibleSet();

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
    
    /**
     * Returns 1 + cardinality of possible - cardinality of required
     */
    public int getSize();

    /**
     * Returns iterator of possible values in domain
     */
    public Iterator values();

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Object val);

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
     * Adds a set of values to the required set
     */
    public void addRequired(Set values) throws PropagationFailureException;
    
    /**
     * Removes a value from the possible set
     */
    public void removePossible(Object value) throws PropagationFailureException;
    
    /**
     * Removes a set of values from the possible set
     */
    public void removePossible(Set values) throws PropagationFailureException;
}
