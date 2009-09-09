package jopt.csp.spi.arcalgorithm.constraint.set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSupersetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperSetIntersectionArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing intersection( sources ) = target
 */
public class EqIntersection extends SetConstraint {
    public EqIntersection(SetVariable x, SetVariable y, SetVariable z) {
        this(new SetVariable[] {x, y}, z);
    }
    public EqIntersection(SetVariable sources[], SetVariable target) {
        super(sources, new SetVariable[] {target});
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode sourceNodes[] = getSetSourceNodes();
        SetNode targetNodes[] = getSetTargetNodes();
        Arc arcs[] = new Arc[sourceNodes.length + 1];
        
        // Create arc forcing target to be intersection of nodes
        arcs[0] = new HyperSetIntersectionArc(sourceNodes, targetNodes[0]);
        
        // Create arc requiring source to require all values 
        // required by target
        for (int i=1; i<arcs.length; i++)
            arcs[i] = new BinarySetSupersetArc(targetNodes[0], sourceNodes[i-1], false);
        
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