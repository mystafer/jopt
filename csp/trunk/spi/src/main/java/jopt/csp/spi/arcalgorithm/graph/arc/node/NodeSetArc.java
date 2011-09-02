package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.arc.SetArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Abstract base arc for set arc with 1 variable
 */
public abstract class NodeSetArc<T> extends NodeArc implements SetArc {
    /**
     * Constructor
     */
    public NodeSetArc(SetNode<T> node) {
        super(node);
    }
}
