package jopt.csp.spi.arcalgorithm.domain;

import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;

public abstract class AbstractDomain implements Domain, DomainChangeSource, ChoicePointDataSource {
    protected boolean changed;
    
    private HashSet domainListeners;
    private HashSet rangeListeners;
    private HashSet valueListeners;
    
    protected DomainChangeEvent event;
    
    /**
     * Constructor
     */
    protected AbstractDomain(HashSet domainListeners, HashSet rangeListeners, HashSet valueListeners) {
        this.domainListeners = domainListeners;
        this.rangeListeners = rangeListeners;
        this.valueListeners = valueListeners;
        this.event = new DomainChangeEvent(this);
    }
    
    /**
     * Constructor
     */
    protected AbstractDomain() {
        this(new HashSet(), new HashSet(), new HashSet());
    }
    
    //  javadoc is inherited
    public void addDomainChangeListener(DomainChangeListener listener) {
        if (listener!=null && domainListeners!=null) domainListeners.add(listener);
    }

    //  javadoc is inherited
    public void removeDomainChangeListener(DomainChangeListener listener) {
        if (listener!=null && domainListeners!=null) domainListeners.remove(listener);
    }
    
    //  javadoc is inherited
    public void addRangeChangeListener(DomainChangeListener listener) {
        if (listener!=null && rangeListeners!=null) rangeListeners.add(listener);
    }

    //  javadoc is inherited
    public void removeRangeChangeListener(DomainChangeListener listener) {
        if (listener!=null && rangeListeners!=null) rangeListeners.remove(listener);
    }
    
    //  javadoc is inherited
    public void addValueChangeListener(DomainChangeListener listener) {
        if (listener!=null && valueListeners!=null) valueListeners.add(listener);
    }

    //  javadoc is inherited
    public void removeValueChangeListener(DomainChangeListener listener) {
        if (listener!=null && valueListeners!=null) valueListeners.remove(listener);
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyDomainChange() throws PropagationFailureException{
        fireChangeEvent(DomainChangeType.DOMAIN);
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyRangeChange() throws PropagationFailureException{
        fireChangeEvent(DomainChangeType.RANGE);
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyValueChange() throws PropagationFailureException{
        fireChangeEvent(DomainChangeType.VALUE);
    }
    
    
    protected void notifyChoicePointPop() {
        if (rangeListeners != null) {
            Iterator iterator = rangeListeners.iterator();
            while (iterator.hasNext()) {
                DomainChangeListener listener = (DomainChangeListener) iterator.next();
                listener.choicePointPop();
            }
        }
    }
    
    protected void notifyChoicePointPush() {
        if (rangeListeners != null) {
            Iterator iterator = rangeListeners.iterator();
            while (iterator.hasNext()) {
                DomainChangeListener listener = (DomainChangeListener) iterator.next();
                listener.choicePointPush();
            }
        }
    }
    
    //  javadoc is inherited
    public boolean changed() {
        boolean tmp = changed;
        changed = false;
        return tmp;
    }
    
    /**
     * Fires a change event
     */
    private void fireChangeEvent(int type) throws PropagationFailureException {
        event.setType(type); 
        //Notify value listeners
        if (type == DomainChangeType.VALUE && valueListeners != null) {
            Iterator iterator = valueListeners.iterator();
            while (iterator.hasNext()) {
                DomainChangeListener listener = (DomainChangeListener) iterator.next();
                listener.domainChange(event);
            }
        }

        // Notify range listeners
        if (type >= DomainChangeType.RANGE && rangeListeners != null) {
            Iterator iterator = rangeListeners.iterator();
            while (iterator.hasNext()) {
                DomainChangeListener listener = (DomainChangeListener) iterator.next();
                listener.domainChange(event);
            }
        }
        
        // Notify domain listeners
        if (domainListeners != null) {
            Iterator iterator = domainListeners.iterator();
            while (iterator.hasNext()) {
                DomainChangeListener listener = (DomainChangeListener) iterator.next();
                listener.domainChange(event);
            }
        }
        
        //firing an event indicates that the domain has changed
        this.changed = true;
    }
    
    //  javadoc is inherited
    public abstract Object clone();
}
