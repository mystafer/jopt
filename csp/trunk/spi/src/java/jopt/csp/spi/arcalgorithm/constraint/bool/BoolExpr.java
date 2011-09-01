package jopt.csp.spi.arcalgorithm.constraint.bool;

import java.util.Collection;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.CspConstraint;

public interface BoolExpr {
    /**
     * Returns the name of this expression
     */
    public String getName();

    /**
     * Retrieves node for an expression
     */
    public Node getNode();

    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph);

    /**
     * Returns true if expression cannot be satisfied
     */
    public boolean isFalse();
    
    /**
     * Returns true if expression cannot be dissatisfied
     */
    public boolean isTrue();
    
    /**
     * Returns a BoolExpr equal to the Not of this one
     */
    public BoolExpr notExpr();
    
    /**
     * Returns collection of all nodes used to build this expression 
     */
    public Collection<Node> getNodeCollection();
    
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     * 
     */
    public Arc[] getBooleanSourceArcs();
    /**
     * Returns true if this expression's domain is bound to a value
     */
    public boolean isBound();
    
    /**
	 * Returns a constraint that represents this boolean expression
	 * that can be used in the solver
	 */
	public CspConstraint toConstraint();
}