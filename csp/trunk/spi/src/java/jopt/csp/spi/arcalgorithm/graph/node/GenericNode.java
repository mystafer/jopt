/*
 * Created on Jun 14, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.util.GenericIndex;

/**
 * Interface implemented by all generic nodes
 * 
 * @author Chris Johnson
 */
public interface GenericNode {
    
    /**
     * Returns the generic index that is associated with this node
     */
    public GenericIndex[] getIndices();

    /**
     * Returns the internal node corresponding to the associated index's current value
     */
    public Node getNodeForIndex();

    /**
     * Returns the number of nodes that are wrapped by this generic node
     */
    public int getNodeCount();

    /**
     * Returns a node from the internal array
     * 
     * @param offset  Offset of node in internal node array
     */
    public Node getNode(int offset);

    /**
     * Updates associated indices to values corresponding to the node offset in
     * the internal array
     */
    public void setIndicesToNodeOffset(int offset);
}