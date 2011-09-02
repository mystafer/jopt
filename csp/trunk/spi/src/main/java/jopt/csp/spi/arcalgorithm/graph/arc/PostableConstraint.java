/*
 * BooleanConstraint.java
 * 
 * Created on May 6, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.CspConstraint;

/**
 * A constraint that can be posted to a graph
 */
public interface PostableConstraint extends CspConstraint {
    /**
     * Posts a boolean constraint to the graph for use during propagation
     */
    public void postToGraph();

    /**
     * Returns a constraint that is the opposite of this constraint
     */
    public PostableConstraint getPostableOpposite();
    
    /**
     * Returns array of nodes that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Node[] getBooleanSourceNodes();
    
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs();
}
