package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.arc.AbstractArc;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.PropagationFailureException;

public abstract class NodeArc extends AbstractArc {
    protected Node node;
    
    /**
     * Constructor
     */
    protected NodeArc(Node node) {
        this.node = node;
    }
    
    /**
     * Returns the source node of this Arc
     */
    public Node getSourceNode() {
        return node;
    }
    
    /**
     * Returns the source node of this Arc
     */
    public Node getTargetNode() {
        return node;
    }
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType() {
        return Arc.NODE;
    }
    
    
    /**
     * Same as propagate
     */
    public void propagate(Node n) throws PropagationFailureException {
        propagate();
    }
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
    	return 0;
    }
}