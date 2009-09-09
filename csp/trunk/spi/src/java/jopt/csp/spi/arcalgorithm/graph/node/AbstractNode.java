package jopt.csp.spi.arcalgorithm.graph.node;

import java.util.HashMap;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.domain.DomainChangeEvent;
import jopt.csp.spi.arcalgorithm.domain.DomainChangeListener;
import jopt.csp.spi.util.DomainChangeType;

public abstract class AbstractNode implements Node, NodeChangeSource {
    private String name;
    
    private HashMap domainListeners;
    private HashMap rangeListeners;
    private HashMap valueListeners;
    private boolean inGraph;
    
    protected NodeChangeEvent event;
    
    /**
     * Constructor
     */
    public AbstractNode(String name) {
        if (name==null)
            throw new RuntimeException("node cannot be created without a name");
        
        this.name = name;
        this.domainListeners = new HashMap();
        this.rangeListeners = new HashMap();
        this.valueListeners = new HashMap();
        this.event = new NodeChangeEvent(this);
    }
    
    /**
     * Adds a listener to this node interested in domain, range and value
     * events
     */
    public void addDomainChangeListener(NodeChangeListener listener, Object callbackData) {
        if (listener!=null) domainListeners.put(listener, callbackData);
    }

    /**
     * Removes a domain listener from this node
     */
    public void removeDomainChangeListener(NodeChangeListener listener) {
        if (listener!=null) domainListeners.remove(listener);
    }
    
    /**
     * Adds a listener to this node interested in range and value
     * events
     */
    public void addRangeChangeListener(NodeChangeListener listener, Object callbackData) {
        if (listener!=null) rangeListeners.put(listener, callbackData);
    }

    /**
     * Removes a range listener from this node
     */
    public void removeRangeChangeListener(NodeChangeListener listener) {
        if (listener!=null) rangeListeners.remove(listener);
    }
    
    /**
     * Adds a listener to this node only interested in value events
     */
    public void addValueChangeListener(NodeChangeListener listener, Object callbackData) {
        if (listener!=null) valueListeners.put(listener, callbackData);
    }

    /**
     * Removes a value listener from this node
     */
    public void removeValueChangeListener(NodeChangeListener listener) {
        if (listener!=null) valueListeners.remove(listener);
    }
    
    /**
     * Notifies listeners of node change event and removes last propagated
     * arc setting
     */
    private void notifyDomainChange() {
        fireChangeEvent(DomainChangeType.DOMAIN);
    }
    
    /**
     * Notifies listeners of node change event and removes last propagated
     * arc setting
     */
    private void notifyRangeChange() {
        fireChangeEvent(DomainChangeType.RANGE);
    }
    
    /**
     * Notifies listeners of node change event and removes last propagated
     * arc setting
     */
    private void notifyValueChange() {
        fireChangeEvent(DomainChangeType.VALUE);
    }
    
    /**
     * Fires a change event for when the domain of a node changes.
     */
    protected void fireChangeEvent(int type) {
        // Notify value listeners
        if (type == DomainChangeType.VALUE) {
            Iterator iterator = valueListeners.keySet().iterator();
            while (iterator.hasNext()) {
                NodeChangeListener listener = (NodeChangeListener) iterator.next();
                event.setCallbackValue(valueListeners.get(listener));
                listener.nodeChange(event);
            }
        }

        // Notify range listeners
        if (type >= DomainChangeType.RANGE) {
            Iterator iterator = rangeListeners.keySet().iterator();
            while (iterator.hasNext()) {
                NodeChangeListener listener = (NodeChangeListener) iterator.next();
                event.setCallbackValue(rangeListeners.get(listener));
                listener.nodeChange(event);
            }
        }
        
        // Notify domain listeners
        Iterator iterator = domainListeners.keySet().iterator();
        while (iterator.hasNext()) {
            NodeChangeListener listener = (NodeChangeListener) iterator.next();
            event.setCallbackValue(domainListeners.get(listener));
            listener.nodeChange(event);
        }
    }

    /**
     * Returns true if a node is connected to a graph
     */
    public boolean inGraph() {
    	return inGraph;
    }

    /**
     * Indicates to node when it is added to a graph
     */
    public void addedToGraph() {
        if (inGraph) {
            throw new IllegalStateException("Node is already contained in a graph");
        }

    	inGraph = true;
    }

    /**
     * Indicates to node when it is disconnected from a graph
     */
    public void removedFromGraph() {
    	inGraph = false;
    }

    /**
     * Returns name of node
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets name of node
     */
    public final void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns a hash code based on this variable's name (if one exists)
     */
    public final int hashCode() {
        return this.name.hashCode();
    }
    
    
    /**
     * Returns true if name of objects are equal
     */
    public final boolean equals(Object obj) {
        if (obj==null) return false;
        
        // make sure nodes are same class type
        if (!getClass().equals(obj.getClass())) return false;

        AbstractNode n = (AbstractNode) obj;
        
        // make sure node names are the same
        return name.equals(n.name);
    }
    
    /**
     * Listens for domain changes to update node changed flag
     */
    public class DomainListener implements DomainChangeListener {
        /**
         * Method invoked by domain when a domain change event is fired
         */
        public void domainChange(DomainChangeEvent ev) {
//            log.trace("Node change: Type(" + ev.getType() + "), Node(" + DoubleNode.this + ")");

            switch (ev.getType()) {
                case DomainChangeType.VALUE:
                    event.setNumberChange(ev.getMinChange(), ev.getMaxChange(), ev.getType());
                	event.setType(ev.getType());    
                	notifyValueChange();
                    break;

                case DomainChangeType.RANGE:
                    event.setNumberChange(ev.getMinChange(), ev.getMaxChange(), ev.getNumberType());
                	event.setType(ev.getType());
                    notifyRangeChange();
                    break;

                default:
                    notifyDomainChange();
            }
        }
        
        // javadoc is inherited
        public void choicePointPop() {}
        
        // javadoc is inherited
        public void choicePointPush() {}
    }
}
