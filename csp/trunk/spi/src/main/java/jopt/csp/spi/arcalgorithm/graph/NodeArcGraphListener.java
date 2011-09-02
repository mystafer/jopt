package jopt.csp.spi.arcalgorithm.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NodeChangeEvent;

/**
 * Interface for objects that need to watch the graph for arc and node
 * addition, removal and changes.  Used by the arc consistency algorithms
 * to observe the initial creation of the graph and any subsequent arcs
 * being added during operations.
 */
public interface NodeArcGraphListener {
    /**
     * Called when an arc is added to the graph as a result of
     * arcs being posted by constraints.
     */
    public void arcAddedEvent(NodeArcGraph graph, Arc arc);
    
    /**
     * Called when an arc is removed from the graph, generally
     * because the choice point stack is being popped.
     */
    public void arcRemovedEvent(NodeArcGraph graph, Arc arc);
    
    /**
     * Called when a node is removed from the graph, generally
     * because the choice point stack is being popped.
     */
    public void nodeRemovedEvent(NodeArcGraph graph, Node n);
    
    /**
     * Called when a node in the graph has a domain change.
     * Node change events arise from changes in domain, including
     * a change in range (bounds) or a domain being bound to a
     * single value.
     */
    public void nodeChangedEvent(NodeArcGraph graph, NodeChangeEvent evt);
}