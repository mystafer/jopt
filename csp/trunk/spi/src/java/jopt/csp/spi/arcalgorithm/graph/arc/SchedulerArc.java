package jopt.csp.spi.arcalgorithm.graph.arc;

import jopt.csp.spi.arcalgorithm.graph.node.Node;

/**
 * Generic arc to represent commonalities between all Scheduling Arcs
 * @author jboerkoel
 *
 */
public abstract class SchedulerArc extends AbstractArc {
    
    protected int sourceDependencies[];
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType() {
        return Arc.SCHEDULE;
    }
    
    /**
     * Returns the source nodes of this Arc
     */
    public abstract Node[] getSourceNodes();
    /**
     * Returns the target nodes of this Arc
     */
    public abstract Node[] getTargetNodes();

    /**
     * Returns the dependency type of target node on source node
     */
    public abstract int[] getSourceDependencies();
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
    	return Arc.SCHEDULE;
    }
}