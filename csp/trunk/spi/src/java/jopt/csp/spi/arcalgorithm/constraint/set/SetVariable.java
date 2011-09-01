package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.domain.SetDomain;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Variable that will be reduced to one or more required set of values
 */
public interface SetVariable<T> {
	/**
	 * Returns name of node
	 */
	public String getName();

	/**
	 * Sets name of node
	 */
	public void setName(String name);

	/**
	 * Retrieves node for an expression
	 */
	public SetNode<T> getNode();

	/**
	 * Returns domain for variable
	 */
	public SetDomain<T> getDomain();

	/**
	 * Adds arcs representing this expression to the node arc graph
	 */
	public void updateGraph(NodeArcGraph graph);
    
    /**
     * Returns possible set of values
     */
    public Set<T> getPossibleSet();

    /**
     * Returns required set of values
     */
    public Set<T> getRequiredSet();

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound();
}