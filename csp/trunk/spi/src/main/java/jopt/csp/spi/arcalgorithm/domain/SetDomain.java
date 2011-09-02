package jopt.csp.spi.arcalgorithm.domain;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for all set based interfaces
 */
public interface SetDomain<T> extends Domain {
    /**
     * Returns possible set of values
     */
    public Set<T> getPossibleSet();

    /**
     * Returns required set of values
     */
    public Set<T> getRequiredSet();

    /**
     * Returns the possible-delta set
     */
    public Set<T> getPossibleDeltaSet();

    /**
     * Returns the required-delta set
     */
    public Set<T> getRequiredDeltaSet();
    
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
    public Iterator<T> values();

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(T val);

    /**
     * Returns true if value is required
     */
    public boolean isRequired(T value);
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(T value);
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(T value) throws PropagationFailureException;
    
    /**
     * Adds a set of values to the required set
     */
    public void addRequired(Set<T> values) throws PropagationFailureException;
    
    /**
     * Removes a value from the possible set
     */
    public void removePossible(T value) throws PropagationFailureException;
    
    /**
     * Removes a set of values from the possible set
     */
    public void removePossible(Set<T> values) throws PropagationFailureException;
}
