package jopt.csp.spi.arcalgorithm.util;

import java.util.HashMap;
import java.util.LinkedList;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.SchedulerArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.util.DomainChangeType;

/**
 * Represents a queue of nodes to be propagated
 */
public class NodeQueue {
    // Map associating nodes with Integer event types as defined in NodeChangeEvent class
    private HashMap<Node,Integer> nodeMap;
    // Map associating nodes with ArcQueue objects
    private HashMap<Node, ArcQueue> nodeArcs;
    // List containing Node objects
    private LinkedList<Node> list;
    private ArcQueue currentArcs;
    
    /**
     * Constructor
     */
    public NodeQueue() {
        this.nodeMap = new HashMap<Node,Integer>();
        this.nodeArcs = new HashMap<Node, ArcQueue>();
        this.list = new LinkedList<Node>();
    }
    
    /**
     * Removes all nodes in the queue
     */
    public void clear() {
    	nodeMap.clear();
    	nodeArcs.clear();
    	list.clear();
    	currentArcs = null;
    }
    
    /**
     * Returns true if a node is in the queue
     */
    public boolean contains(Node n) {
    	return nodeMap.containsKey(n);
    }
    
    /**
     * Returns size of arc queue
     */
    public int size() {
        return list.size();
    }
    
    /**
     * Returns true if queue has a next arc available
     */
    public boolean hasNext() {
        return !list.isEmpty();
    }
    
    /**
     * Returns next node on queue or null if none is available
     */
    public Node next() {
        NodeChangeEvent evt = nextChange();
        if (evt == null) return null;
        return evt.getNode();
    }
    
    /**
     * Returns next node change on queue or null if none is available
     */
    public NodeChangeEvent nextChange() {
        if (list.isEmpty()) return null;
        
        Node node = (Node) list.removeFirst();
        Integer type = (Integer) nodeMap.remove(node);
        this.currentArcs = (ArcQueue) nodeArcs.remove(node);
        
        NodeChangeEvent ev = new NodeChangeEvent(node);
        ev.setType(type.intValue());
        return ev;
    }
    
    /**
     * Returns the current arc list associated with the last retrieved node
     */
    public ArcQueue getCurrentArcs() {
    	return currentArcs;
    }
    
    /**
     * Adds an node to the queue
     */
    public void add(Node node) {
        add(node, DomainChangeType.VALUE, null);
    }
    

    /**
     * Adds a node change to the queue.  If the node is already in the queue
     * and the eventType is the same or lower than the existing eventType,
     * no change occurs.  If, however, the new eventType is higher (more
     * specific) than the existing eventType, the eventType is upgraded
     * and the new arc queue overwrites the existing arc queue associated
     * with the node (even if the new arc queue is null).
     */
    public void add(Node node, int eventType, ArcQueue arcs) {
        Integer cur = (Integer) nodeMap.get(node);
        
        // Node is not currently a member of the map
        if (cur == null) {
            list.add(node);
            nodeMap.put(node, new Integer(eventType));
            if (arcs!=null) nodeArcs.put(node, arcs);
            else nodeArcs.remove(node);
        }
        
        // Change event type in HashMap if new event is higher than current
        // i.e. range=1 replaces domain=0 and value=2 replaces range=1
        else if (eventType > cur.intValue()) {
            nodeMap.put(node, new Integer(eventType));
            if (arcs!=null) nodeArcs.put(node, arcs);
            else nodeArcs.remove(node);
        }
    }

    /**
     * Adds an node change to the queue
     */
    public void add(Node node, int eventType) {
    	add(node, eventType, null);
    }

    /**
     * Adds source nodes of a node arc to the queue
     */
    public void addNodeArc(NodeArc arc) {
        add(arc.getTargetNode(), DomainChangeType.VALUE, null);
    }

    /**
     * Adds source nodes of a binary arc to the queue
     */
    public void addBinaryArc(BinaryArc arc) {
        add(arc.getSourceNode(), DomainChangeType.VALUE, null);
    }

    /**
     * Adds source nodes of a hyper arc to the queue
     */
    public void addHyperArc(HyperArc arc) {
        Node sources[] = arc.getSourceNodes();
        for (int i=0; i<sources.length; i++)
            add(sources[i], DomainChangeType.VALUE, null);
    }

    /**
     * Adds source nodes of a generic arc to the queue
     */
    public void addGenericArc(GenericArc arc) {
        Node sources[] = arc.getSourceNodes();
        for (int i=0; i<sources.length; i++)
            add(sources[i], DomainChangeType.VALUE, null);
    }
    
    /**
     * Adds source nodes of a schedule arc to the queue
     */
    public void addScheduleArc(SchedulerArc arc) {
        Node sources[] = arc.getSourceNodes();
        for (int i=0; i<sources.length; i++)
            add(sources[i]);
    }

    /**
     * Adds source nodes of arc to queue
     */
    public void addArc(Arc arc) {
        switch(arc.getArcType()) {
            case Arc.NODE:
                addNodeArc((NodeArc) arc);
                break;
            
            case Arc.BINARY:
                addBinaryArc((BinaryArc) arc);
                break;
            
            case Arc.HYPER:
                addHyperArc((HyperArc) arc);
                break;
                
            case Arc.GENERIC:
                addGenericArc((GenericArc) arc);
                break;
            case Arc.SCHEDULE:
                addScheduleArc((SchedulerArc) arc);
                break;
        }
    }

    /**
     * Removes a node change from the queue
     */
    public NodeChangeEvent remove(Node node) {
        if (list.remove(node)) {
            Integer type = (Integer) nodeMap.remove(node);
        	nodeArcs.remove(node);
            
            NodeChangeEvent ev = new NodeChangeEvent(node);
            ev.setType(type.intValue());
            
            return ev;
        }
        
        return null;
    }

    public String toString() {
    	StringBuffer buf = new StringBuffer("{");
    	for (int i=0; i<list.size(); i++) {
    		if (i>0) buf.append(", ");
    		Node n = (Node) list.get(i);
    		buf.append(n);
    		buf.append("=");
    		buf.append(nodeMap.get(n));
    	}
    	buf.append("}");
    		
        return buf.toString();
    }
}