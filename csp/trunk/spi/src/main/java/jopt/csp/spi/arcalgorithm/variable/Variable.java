package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.util.Storable;
import jopt.csp.variable.CspVariable;

/**
 * Interface that should be implemented by any variable
 * with a concrete domain
 */
public interface Variable extends Storable, CspVariable {
    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph);
}
