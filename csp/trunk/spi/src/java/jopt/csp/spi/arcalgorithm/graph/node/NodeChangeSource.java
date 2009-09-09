package jopt.csp.spi.arcalgorithm.graph.node;


public interface NodeChangeSource {
    /**
     * Adds a listener to this node interested in domain, range and value
     * events
     */
    public void addDomainChangeListener(NodeChangeListener listener, Object callbackData);

    /**
     * Removes a domain listener from this node
     */
    public void removeDomainChangeListener(NodeChangeListener listener);
    
    /**
     * Adds a listener to this node interested in range and value
     * events
     */
    public void addRangeChangeListener(NodeChangeListener listener, Object callbackData);

    /**
     * Removes a range listener from this node
     */
    public void removeRangeChangeListener(NodeChangeListener listener);
    
    /**
     * Adds a listener to this node only interested in value events
     */
    public void addValueChangeListener(NodeChangeListener listener, Object callbackData);

    /**
     * Removes a value listener from this node
     */
    public void removeValueChangeListener(NodeChangeListener listener);
}