package jopt.csp.spi.arcalgorithm.constraint.num;

import java.util.Collection;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.TrigExpr;

public interface NumExpr extends TrigExpr {
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
     * Returns collection of all nodes used to build this expression 
     */
    public Collection<Node> getNodeCollection();
    
    /**
     * Returns array of arcs that will affect the boolean true / false
     * value of this constraint upon a change
     */
    public Arc[] getBooleanSourceArcs();
    
    /**
     * Returns the type of expression: Int, Long, etc.
     */
    public int getNumberType();
    
    /**
     * Returns minimum value of this variable's domain
     */
    public Number getNumMin();
    
    /**
     * Returns maximum value of this variable's domain
     */
    public Number getNumMax();
    
    /**
     * Returns the next higher value in this variable's domain or current value if none
     * exists
     */
    public Number getNextHigher(Number val);

    /**
     * Returns the next lower value in this variable's domain or current value if none
     * exists
     */
    public Number getNextLower(Number val);
    
    /**
     * Returns true if this expression's domain is bound to a value
     */
    public boolean isBound();

    /**
     * Sets precision associated with this expression's domain for real numbers
     */
    public void setPrecision(double p);
    
    /**
     * Returns precision associated with this expression's domain for real numbers
     */
    public double getPrecision();
}