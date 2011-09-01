package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSubsetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSupersetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeSetSubsetEqArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing target is a strict subset of source
 */
public class StrictSubset<T> extends SetConstraint<T> {
    private boolean constSource;
    private Set<T> constSet;
    
    @SuppressWarnings("unchecked")
	public StrictSubset(SetVariable<T> source, SetVariable<T> target) {
        super(new SetVariable[] {source}, new SetVariable[]{target});
    }
    public StrictSubset(Set<T> source, SetVariable<T> target) {
        this(target, target);
        this.constSource = true;
        this.constSet = source;
    }
    public StrictSubset(SetVariable<T> source, Set<T> target) {
        this(source, source);
        this.constSource = true;
        this.constSet = target;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode<T> sourceNodes[] = getSetSourceNodes();
        SetNode<T> targetNodes[] = getSetTargetNodes();
        
        // create node arc with constant source
        Arc arcs[] = null;
        if (constSet != null) {
            Arc arc = null;
            if (constSource)
                arc = new NodeSetSubsetEqArc<T>(constSet, targetNodes[0], true);
            else
                arc = new NodeSetSubsetEqArc<T>(sourceNodes[0], constSet, true);
            
            arcs = new Arc[]{arc};
        }
        
        // create binary arcs between nodes
        else {
            arcs = new Arc[]{
                new BinarySetSubsetArc<T>(sourceNodes[0], targetNodes[0], true),
                new BinarySetSupersetArc<T>(targetNodes[0], sourceNodes[0], true)
            };
        }
        
        return arcs;
    }
    // javadoc inherited from SetConstraint
    protected boolean isViolated() {
    	return false;
    }
    
    // javadoc inherited from AbstractConstraint
    protected AbstractConstraint createOpposite() {
        return null;
    }
}