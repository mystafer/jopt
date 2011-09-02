package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.arc.AbstractArc;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.PropagationFailureException;

public abstract class BinaryArc extends AbstractArc {
    protected Node source;
    protected int sourceDependency;
    protected Node target;
    
    /**
     * Constructor
     */
    protected BinaryArc(Node source, Node target) {
        this.source = source;
        this.target = target;
    }
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType() {
        return Arc.BINARY;
    }
    
    /**
     * Returns the source node of this Arc
     */
    public Node getSourceNode() {
        return source;
    }
    
    /**
     * Returns the dependency type of target node on source node
     */
    public int getSourceDependency() {
        return sourceDependency;
    }
    
    /**
     * Returns the target node of this Arc
     */
    public Node getTargetNode() {
        return target;
    }

    public void propagate(Node n) throws PropagationFailureException {
        propagate();
    }
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
    	return Arc.BINARY;
    }
}