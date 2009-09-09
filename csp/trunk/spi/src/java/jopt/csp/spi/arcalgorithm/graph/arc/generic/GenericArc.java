package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.AbstractArc;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;

/**
 * An arc that can have several source and several target nodes.
 */
public abstract class GenericArc extends AbstractArc {
    protected Node sources[];
    protected Node targets[];
    protected int sourceDependencies[];
    
    /**
     * Constructor
     */
    protected GenericArc(Node sources[], Node targets[]) {
        this.sources = sources;
        this.targets = targets;
    }
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType() {
        return Arc.GENERIC;
    }
    
    /**
     * Returns the source nodes of this Arc
     */
    public Node[] getSourceNodes() {
        return sources;
    }

    /**
     * Returns the target nodes of this Arc
     */
    public Node[] getTargetNodes() {
        return targets;
    }

    /**
     * Returns the dependency type of target node on source node
     */
    public int[] getSourceDependencies() {
        if (sourceDependencies == null) {
            sourceDependencies = new int[sources.length];
            for (int i=0; i<sources.length; i++)
                sourceDependencies[i] = DomainChangeType.DOMAIN;
        }
        
        return sourceDependencies;
    }
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
    	return Arc.GENERIC;
    }
}