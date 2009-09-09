package jopt.csp.spi.arcalgorithm.graph.arc;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.variable.PropagationFailureException;

public interface Arc {
    /**
     * A node arc is a circular arc pointing back at itself; a single node is both the
     * source and the target of this arc.
     * Such arcs only need to be tested for consistency at the first propagation event
     * because subsequent domain reductions will not cause them to become internally
     * inconsistent.
     */
    public final static int NODE    	= 0;
    /**
     * An arc directed from an individual source node to a different individual target node.
     */
    public final static int BINARY  	= 1;
    /**
     * An arc that might have more than one source node but only a single target node.
     */
    public final static int HYPER   	= 2;
    /**
     * An arc that can have several source and several target nodes.
     */
    public final static int GENERIC 	= 3;
    /**
     * An arc related to the js module: a scheduler arc.
     */
    public final static int SCHEDULE	= 4;
    
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType();
    
    /**
     * Sets the strength of the algorithm that is propagating the arc.  This
     * may affect how much filtering (domain reduction) occurs by the constraint.
     */
    public void setAlgorithmStrength(int strength);
    
    /**
     * Sets a flag indicating if the algorithm wants domain delta's to be
     * consulted when arc is propagating.  The default value for this
     * flag is true.
     */
    public void setUseDomainDeltas(boolean useDeltas);

    /**
     * Attempts to reduce values in target node domain based on values
     * in all source nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException;

    /**
     * Attempts to reduce values in target node domain based on values
     * in given source node
     *
     * @param src   Source node that caused propagation to occur
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate(Node src) throws PropagationFailureException;
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity();
}