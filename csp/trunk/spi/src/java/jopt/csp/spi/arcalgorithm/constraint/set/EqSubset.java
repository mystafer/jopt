package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumCardinalityReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSubsetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSupersetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeSetSubsetEqArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.spi.util.NumConstants;

/**
 * Constraint representing target is subset of source
 */
public class EqSubset<T> extends SetConstraint<T> {
    private boolean constSource;
    private Set<T> constSet;
    
    @SuppressWarnings("unchecked")
	public EqSubset(SetVariable<T> source, SetVariable<T> target) {
        super(new SetVariable[] {source}, new SetVariable[]{target});
    }
    public EqSubset(Set<T> source, SetVariable<T> target) {
        this(target, target);
        this.constSource = true;
        this.constSet = source;
    }
    public EqSubset(SetVariable<T> source, Set<T> target) {
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
            // a cardinality arc is required to ensure that strict subset terms are met
            if (constSource) {
                arcs = new Arc[]{
                    new NodeSetSubsetEqArc<T>(constSet, targetNodes[0], false),
                    new BinaryNumCardinalityReflexArc<T>(new Integer(constSet.size()), targetNodes[0], NumConstants.LT)
                };
            }
            else {
                arcs = new Arc[]{
                    new NodeSetSubsetEqArc<T>(sourceNodes[0], constSet, false),
                    new BinaryNumCardinalityReflexArc<T>(new Integer(constSet.size()), sourceNodes[0], NumConstants.GT)
                };
            }
        }
        
        // create binary arcs between nodes
        else {
            arcs = new Arc[]{
        		new BinarySetSubsetArc<T>(sourceNodes[0], targetNodes[0], false),
                new BinarySetSupersetArc<T>(targetNodes[0], sourceNodes[0], false)
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