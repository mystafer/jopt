package jopt.csp.spi.arcalgorithm.domain;


/**
 * Interface implemented by all csp domains
 */
public interface Domain {
    
    
    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound();

    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * Note: if a particular implementation does not support
     * the storage of state information, this simply returns null
     * 
     * @return an Object representing the current state information for this domain
     */
    public Object getDomainState();
 
    /**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * Note: if a particular implementation does not support
     * the restoration of state information, this method does nothing
     * 
     * @param state The state information to which this domain should be set
     */
    public void restoreDomainState(Object state);
    
    /**
     * Returns a copy of this domain
     */
    public Object clone();
    
    /**
     * Returns true if last operation caused a change to occur in the domain
     */
    public boolean changed();

    /**
     * Clears the delta set for this domain
     */
    public void clearDelta();
}