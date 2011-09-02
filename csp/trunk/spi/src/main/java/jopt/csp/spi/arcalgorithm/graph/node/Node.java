package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.Storable;

public interface Node extends ChoicePointDataSource, Storable {
    /**
     * Returns name of node
     */
    public String getName();
    
    /**
     * Sets name of node
     */
    public void setName(String name);
    
    /**
     * Returns true if domain for this node is bound
     */
    public boolean isBound();

    /**
     * Returns true if a value is contained in this node's domain
     */
    public boolean isInDomain(Object val);

    /**
     * Returns size of domain for this node.  If domain is infinite, value
     * is Integer.MAX_VALUE
     */
    public int getSize();
    
    /**
     * Clears the delta set for this node's domain
     */
    public void clearDelta();

    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps);
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet();
    
    /**
     * Returns true if a node is connected to a graph
     */
    public boolean inGraph();

    /**
     * Indicates to node when it is added to a graph
     */
    public void addedToGraph();

    /**
     * Indicates to node when it is disconnected from a graph
     */
    public void removedFromGraph();
}