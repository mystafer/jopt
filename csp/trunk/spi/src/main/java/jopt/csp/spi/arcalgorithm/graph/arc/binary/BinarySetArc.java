package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.graph.arc.SetArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;

/**
 * Abstract base arc for set arc with 2 variables
 */
public abstract class BinarySetArc extends BinaryArc implements SetArc {
    /**
     * Constructor
     *
     * @param   source      Source node in equation
     * @param   target      Target node in equation
     */
    public BinarySetArc(Node source, Node target) {
        super(source, target);

        // Set source dependency value
        this.sourceDependency = DomainChangeType.DOMAIN;
    }
}
