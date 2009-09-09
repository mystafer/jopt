package jopt.csp.spi.arcalgorithm.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.PostableConstraint;
import jopt.csp.spi.util.GenericIndex;

/**
 * Constraint that is related to a graph
 */
public interface GraphConstraint extends PostableConstraint {
    /**
     * Associates a constraint to a graph
     */
    public void associateToGraph(NodeArcGraph graph);

    /**
     * Returns a constraint that is a fragment of the current constraint based upon
     * a set of indices that indication what portion of constraint should be returned
     * 
     * @param indices   Array of indices that indicate fragment to return.  Each index
     *                  must be set to a specific value to indicate fragment desired.
     */
    public GraphConstraint getGraphConstraintFragment(GenericIndex indices[]);
}