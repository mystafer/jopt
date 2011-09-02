package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.SetArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;

/**
 * Abstract base arc for set arc with 3+ variables
 */
public abstract class HyperSetArc extends HyperArc implements SetArc {
    /**
     * Constructor
     *
     * @param   sources     Source nodes in equation
     * @param   target      Target node in equation
     */
    public HyperSetArc(Node[] sources, Node target) {
        super(sources, target);

        // Set source dependency value
        this.sourceDependencies = new int[sources.length];
        for (int i=0; i<sources.length; i++)
            sourceDependencies[i] = DomainChangeType.DOMAIN;
    }
}
