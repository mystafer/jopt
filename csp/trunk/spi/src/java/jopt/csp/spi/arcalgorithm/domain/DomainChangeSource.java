package jopt.csp.spi.arcalgorithm.domain;

public interface DomainChangeSource {
    /**
     * Adds a listener to this domain interested in domain, range and value
     * events
     */
    public void addDomainChangeListener(DomainChangeListener listener);

    /**
     * Removes a domain listener from this domain
     */
    public void removeDomainChangeListener(DomainChangeListener listener);
    
    /**
     * Adds a listener to this domain interested in range and value
     * events
     */
    public void addRangeChangeListener(DomainChangeListener listener);

    /**
     * Removes a range listener from this domain
     */
    public void removeRangeChangeListener(DomainChangeListener listener);
    
    /**
     * Adds a listener to this domain only interested in value events
     */
    public void addValueChangeListener(DomainChangeListener listener);

    /**
     * Removes a value listener from this domain
     */
    public void removeValueChangeListener(DomainChangeListener listener);
}