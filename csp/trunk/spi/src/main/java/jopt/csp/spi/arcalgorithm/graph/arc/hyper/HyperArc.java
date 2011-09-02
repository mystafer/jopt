package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.AbstractArc;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;

/**
 * An arc that might have more than one source node but only a single target node.
 */
public abstract class HyperArc extends AbstractArc {
    protected Node sources[];
    protected int sourceDependencies[];
    protected Node target;
    
    /**
     * Constructor
     */
    protected HyperArc(Node sources[], Node target) {
        this.sources = sources;
        this.target = target;
    }
    
    /**
     * Returns value indicating type of Arc
     */
    public int getArcType() {
        return Arc.HYPER;
    }
    
    
    /**
     * Returns the source node of this Arc
     */
    public Node[] getSourceNodes() {
        return sources;
    }
    
    /**
     * Returns the dependency type of target node on source node.
     * By default, all source dependencies are currently of type <code>DOMAIN</code>.
     * 
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
     * Returns the target node of this Arc
     */
    public Node getTargetNode() {
        return target;
    }
    
    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
    	return Arc.HYPER;
    }
}