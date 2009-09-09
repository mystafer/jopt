package jopt.csp.spi.arcalgorithm.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.SchedulerArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeListener;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeSource;
import jopt.csp.spi.solver.ChoicePointDataMap;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.StateStore;
import jopt.csp.spi.util.Storable;

/**
 * Basic implementation for the NodeArcGraph that tracks arcs
 * and nodes added to the graph and listens to choicepoint
 * events that will rollback changes
 * 
 * @author Nick
 */
public class NodeArcGraphImpl implements NodeArcGraph, NodeChangeListener, ChoicePointEntryListener {
    private NodeArcGraphListener listener;
    private HashSet arcs;
    private HashMap nodes;
    private ChoicePointStack cps;
    private ChoicePointDataMap cpdata;
    private Storable nodesToStore[];
    
    /**
     * Constructor
     */
    public NodeArcGraphImpl() {
        this(null);
    }
    
    /**
     * Constructor
     */
    public NodeArcGraphImpl(NodeArcGraphListener listener) 
    {
        this.listener = listener;
        this.arcs = new HashSet();
        this.nodes = new HashMap();
    }
    
    /**
     * Returns all the nodes contained in the graph
     */
    public Set getAllNodes() {
        return nodes.keySet();
    }
    
    /**
     * Returns all the arcs contained in the graph
     */
    public Set getAllArcs() {
    	return arcs;
    }
    
    /**
     * Adds a node to the set of nodes contained in this graph
     */
    public void addNode(Node node) {
        // ignore node if already in graph
        if (node.inGraph()) return;
        
        // make sure node has a name
        if (node.getName()==null)
            throw new IllegalStateException("name must be set for node to add it to graph");
        
        // check if duplicate node
        if (!nodes.containsKey(node)) {
            // reset nodes for storage array
            nodesToStore = null;
            
        	// attach node to choicepoint
    		if (cps!=null && !node.choicePointStackSet())
    		    node.setChoicePointStack(cps);
        	
        	// record added node to choicepoint
        	if (cpdata!=null) 
                getAddedNodeSet().add(node);
        	
            // Create sets for tracking arc connections to node
            HashMap arcs = new HashMap();
            arcs.put("value", new HashSet());
            arcs.put("range", new HashSet());
            arcs.put("domain", new HashSet());
            nodes.put(node, arcs);
            
            // Add listener for node
            ((NodeChangeSource) node).addDomainChangeListener(this, null);
    
            // notify node of connection
            node.addedToGraph();

//System.out.println("* node added: " + node);
        }
    }
    
    /**
     * Connects a node to an arc's source
     */
    private void connectNodeSource(Node node, int sourceDependency, Arc arc) {
        HashMap arcs = (HashMap) nodes.get(node);
        
        // store added connections into choicepoint stack
        if (cpdata!=null)
        	getAddedConnectionSet().add(new Object[]{node, new Integer(sourceDependency), arc});
        
        switch (sourceDependency) {
            case DomainChangeType.VALUE:
                ((HashSet) arcs.get("value")).add(arc);
                break;

            case DomainChangeType.RANGE:
                ((HashSet) arcs.get("range")).add(arc);
                break;

            case DomainChangeType.DOMAIN:
                ((HashSet) arcs.get("domain")).add(arc);
        }
    }
    
    /**
     * Removes a node connection to an arc's source
     */
    private void disconnectNodeSource(Node node, int sourceDependency, Arc arc) {
        HashMap arcs = (HashMap) nodes.get(node);
        if (arcs==null) return;
        
        switch (sourceDependency) {
            case DomainChangeType.VALUE:
                ((HashSet) arcs.get("value")).remove(arc);
                break;

            case DomainChangeType.RANGE:
                ((HashSet) arcs.get("range")).remove(arc);
                break;

            case DomainChangeType.DOMAIN:
                ((HashSet) arcs.get("domain")).remove(arc);
        }
    }
    
    /**
     * Retrieves a set of value dependent source arcs for node
     */
    public Set getValueSourceArcs(Node node) {
        HashMap arcs = (HashMap) nodes.get(node);
        if (arcs == null) return null;
        return (Set) arcs.get("value");
    }
    
    /**
     * Retrieves a set of range dependent source arcs for node
     */
    public Set getRangeSourceArcs(Node node) {
        HashMap arcs = (HashMap) nodes.get(node);
        if (arcs == null) return null;
        return (Set) arcs.get("range");
    }
    
    /**
     * Retrieves a set of domain dependent source arcs for node
     */
    public Set getDomainSourceArcs(Node node) {
        HashMap arcs = (HashMap) nodes.get(node);
        if (arcs == null) return null;
        return (Set) arcs.get("domain");
    }
    
    /**
     * Returns true if graph contains specified node
     */
    public boolean containsNode(Node node) {
        return nodes.containsKey(node);
    }
    
    /**
     * Adds an arc to the graph and connects all nodes contained within it
     */
    public void addArc(Arc arc) {
        Node sources[] = null;
        Node targets[] = null;
        int sourceDependencies[] = null;
        
        switch(arc.getArcType()) {
	        case Arc.NODE:
	        	NodeArc narc = (NodeArc) arc;
	        	sources = new Node[]{narc.getSourceNode()};
	        	targets = new Node[]{narc.getTargetNode()};
	            break;
	            
	        case Arc.BINARY:
	        	BinaryArc barc = (BinaryArc) arc;
	        	sources = new Node[]{barc.getSourceNode()};
	        	targets = new Node[]{barc.getTargetNode()};
	        	sourceDependencies = new int[]{barc.getSourceDependency()};
	            break;
	        
	        case Arc.HYPER:
	            HyperArc harc = (HyperArc) arc;
	        	sources = harc.getSourceNodes();
	        	targets = new Node[]{harc.getTargetNode()};
	        	sourceDependencies = harc.getSourceDependencies();
	            break;
	        
	        case Arc.GENERIC:
	        	GenericArc gnarc = (GenericArc) arc;
	        	sources = gnarc.getSourceNodes();
	        	targets = gnarc.getTargetNodes();
	        	sourceDependencies = gnarc.getSourceDependencies();
	            break;
	        case Arc.SCHEDULE:
	        	SchedulerArc schedarc = (SchedulerArc) arc;
	        	sources = schedarc.getSourceNodes();
	        	targets = schedarc.getTargetNodes();
	        	sourceDependencies = schedarc.getSourceDependencies();
	    }
	
	    // only notify listeners first time arc is added
	    if (arcs.add(arc)) {
	    	// record data in choicepoint
	    	if (cpdata!=null) 
                getAddedArcSet().add(arc);
	    	
	    	// add source nodes to graph
	        for (int i=0; i<sources.length; i++) {
	            addNode(sources[i]);
	        }
	
	    	// add target nodes to graph
	        for (int i=0; i<targets.length; i++) {
	            addNode(targets[i]);
	        }

	    	// add source node dependencies
	        if (sourceDependencies != null) {
	        	for (int i=0; i<sources.length; i++)
	        		connectNodeSource(sources[i], sourceDependencies[i], arc);
	        }
	
	        // Notify listener of newly added arc
	        if (listener!=null) listener.arcAddedEvent(this, arc);

//System.out.println("* arc added: " + arc);
        }
    }

    /** 
     * Method invoked by node when a node change event is fired.
     * Node change events arise from changes in domain, including
     * a change in range (bounds) or a domain being bound to a
     * single value.
     */
    public void nodeChange(NodeChangeEvent ev) {
        listener.nodeChangedEvent(this, ev);
    }
    
    /**
     * Returns string with node information that
     * can be compared for a change to the graph
     */
    public String nodesDescription() {
        StringBuffer buf = new StringBuffer();
        Iterator iterator = nodes.keySet().iterator();
        while (iterator.hasNext()) {
            if (buf.length()>0) buf.append("\n");
            buf.append(iterator.next());
        }
        return buf.toString();
    }
    
    /**
     * Returns the set of added arcs for the current choicepoint
     */
    private HashSet getAddedArcSet() {
        if (cpdata==null) return null;
        HashSet added = (HashSet) cpdata.get("aa");
        if (added == null) {
        	added = new HashSet();
            cpdata.put("aa", added);
        }
        return added;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("<<<<<<< Node-Arc Graph >>>>>>>\n");
        
        buf.append("===== Nodes =====\n");
        buf.append(nodesDescription());
        buf.append('\n');
        
        buf.append("===== Arcs =====\n");
        Iterator iterator = arcs.iterator();
        while (iterator.hasNext()) {
            buf.append(iterator.next());
            buf.append('\n');
        }
        
        return buf.toString();
    }

    /**
     * Returns the set of added nodes for the current choicepoint
     */
    private HashSet getAddedNodeSet() {
        if (cpdata==null) return null;
        HashSet added = (HashSet) cpdata.get("an");
        if (added == null) {
        	added = new HashSet();
            cpdata.put("an", added);
        }
        return added;
    }

    /**
     * Returns the set of added source node-arc connections for the current choicepoint
     */
    private HashSet getAddedConnectionSet() {
        if (cpdata==null) return null;
        HashSet added = (HashSet) cpdata.get("cn");
        if (added == null) {
        	added = new HashSet();
            cpdata.put("cn", added);
        }
        return added;
    }
    
    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cpdata!=null) {
            throw new IllegalStateException("Choice point stack already set for node arc graph");
        }

        this.cps = cps;
        this.cpdata = cps.newDataMap(this, 10);
        
        // set the choicepoint stack for all nodes currently in the graph
    	Iterator iter = nodes.keySet().iterator();
    	while (iter.hasNext()) {
    		Node n = (Node) iter.next();
            if (cps!=null && !n.choicePointStackSet())
                n.setChoicePointStack(cps);
    	}
    }

    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
    	// remove added arcs
        Iterator iterator = getAddedArcSet().iterator();
        while (iterator.hasNext()) {
            Arc arc = (Arc) iterator.next();
            arcs.remove(arc);
            if (listener!=null) listener.arcRemovedEvent(this, arc);
//System.out.println("* arc removed: " + arc);
        }
    	
    	// remove added node-arc connections
    	HashSet addedConn = getAddedConnectionSet();
    	iterator = addedConn.iterator();
    	while (iterator.hasNext()) {
    		// retrieve connection information
    		Object data[] = (Object[]) iterator.next();
    		Node node = (Node) data[0];
    		int sourceDependency = ((Integer) data[1]).intValue();
    		Arc arc = (Arc) data[2];
    		
    		// disconnect arc
    		disconnectNodeSource(node, sourceDependency, arc);
    	}
        
        // remove added nodes
        HashSet addedSet = getAddedNodeSet();
        if (addedSet.size()>0) {
            // reset nodes for storage array
            nodesToStore = null;
            
            // iterator over nodes and remove them
            iterator = addedSet.iterator();
            while (iterator.hasNext()) {
                Node n = (Node) iterator.next();
                nodes.remove(n);
                
                // disconnect node from graph
                n.setChoicePointStack(null);

                // remove listener from node
                ((NodeChangeSource) n).removeDomainChangeListener(this);
                
                n.removedFromGraph();

                if (listener!=null) listener.nodeRemovedEvent(this, n);
//System.out.println("* node removed: " + n);
            }
        }
//System.out.println("* after graph rollback");
//dumpNodes();
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        // do nothing, state is recorded as changes occur
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        // add added arcs
        Iterator iterator = getAddedArcSet().iterator();
        while (iterator.hasNext()) {
            Arc a = (Arc) iterator.next();
            addArc(a);
        }
    }


    // javadoc is inherited
    public Object getGraphState() {
        // build map of contents for graph
        HashMap graphContents = new HashMap(3);
        graphContents.put("a", arcs);
        graphContents.put("n", nodes);
        
        // build array of nodes for storage
        if (nodesToStore==null)
        	nodesToStore = (Storable[]) nodes.keySet().toArray(new Storable[nodes.size()]);
        
        // store domain data within graph
        graphContents.put("d", new StateStore(nodesToStore));
        
        return graphContents;
    }

    // javadoc is inherited
    public void restoreGraphState(Object state) {
        // retrieve map of contents for graph
        HashMap graphContents = (HashMap) state;
        
        // restore arcs and nodes
        arcs = (HashSet) graphContents.get("a");
        nodes = (HashMap) graphContents.get("n");

        // restore domain data
        StateStore dataStore = (StateStore) graphContents.get("d");
        dataStore.restoreState();
    }
}