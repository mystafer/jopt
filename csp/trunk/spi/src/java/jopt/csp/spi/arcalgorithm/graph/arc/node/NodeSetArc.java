package jopt.csp.spi.arcalgorithm.graph.arc.node;

import jopt.csp.spi.arcalgorithm.graph.arc.SetArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Abstract base arc for set arc with 1 variable
 */
public abstract class NodeSetArc extends NodeArc implements SetArc {
    /**
     * Constructor
     */
    public NodeSetArc(SetNode node) {
        super(node);
    }
}
