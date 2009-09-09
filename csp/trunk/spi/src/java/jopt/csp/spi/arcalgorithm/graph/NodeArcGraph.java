package jopt.csp.spi.arcalgorithm.graph;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.solver.ChoicePointStack;

/**
 * Interface for a graph that tracks all nodes and arcs within
 * a CLP problem. The graph can be used by an AC algorithm
 * in order to ensure node consistency.
 * 
 * The graph is required to track source node dependencies
 * so that an algorithm can determine what arcs should be
 * propagated in response to different types of node changes.
 * 
 * A domain sourced arc is the most basic type of arc.  Any changes
 * to the source node should cause the connect arc to be
 * propagated.
 * 
 * A range arc is one that only needs to be propagated when
 * the minimum or maximum value of the source node changes. For
 * example, if an arc only needs to ensure that the target node
 * is greater than the source node, the arc only needs to be
 * propagated when the minimum value for the source node changes.
 * 
 * A value arc is one that only needs to be propagated when
 * the source node is bound. This is useful for arcs such as
 * source != target where the values to remove from the target
 * cannot be determined until source is bound.
 * 
 * @author Nick
 */
public interface NodeArcGraph {
    /**
     * Adds a node to the set of nodes contained in this graph
     */
    public void addNode(Node node);
    
    /**
     * Adds an arc to the graph and connects all nodes contained within it
     */
    public void addArc(Arc arc);
    
    /**
     * Retrieves a set of value dependent source arcs for node
     */
    public Set getValueSourceArcs(Node node);
    
    /**
     * Retrieves a set of range dependent source arcs for node
     */
    public Set getRangeSourceArcs(Node node);
    
    /**
     * Retrieves a set of domain dependent source arcs for node
     */
    public Set getDomainSourceArcs(Node node);
    
    /**
     * Returns true if graph contains specific node
     */
    public boolean containsNode(Node node);
    
    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps);
    
    /**
     * Returns all the nodes contained in the graph
     */
    public Set getAllNodes();
    
    /**
     * Returns all the arcs contained in the graph
     */
    public Set getAllArcs();
    
    /**
     * Returns an object containing current state of the graph: the arcs and
     * nodes in the graph along with domain information of the nodes.  This
     * state can be restored with the <code>restoreGraphState</code> method.
     */
    public Object getGraphState();

    /**
     * Restores the current state of the graph.  Restores the sets of arcs
     * and nodes as well as domain information for each node.
     */
    public void restoreGraphState(Object state);

    /**
     * Returns string with node information that
     * can be compared for a change to the graph
     */
    public String nodesDescription();
}