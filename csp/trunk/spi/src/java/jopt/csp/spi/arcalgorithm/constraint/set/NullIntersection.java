package jopt.csp.spi.arcalgorithm.constraint.set;

import java.util.ArrayList;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericSetNullIntersectionArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeSetNullIntersectionArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing intersection( sources ) = {empty set}
 */
public class NullIntersection extends SetConstraint {
    private Set consta;
    private Set constb;
    
    public NullIntersection(SetVariable sources[]) {
        super(sources, sources);
    }
    public NullIntersection(SetVariable a, SetVariable b) {
        super(new SetVariable[]{a, b}, new SetVariable[]{a, b});
    }
    public NullIntersection(SetVariable a, Set constb) {
        super(new SetVariable[]{a}, new SetVariable[]{a});
        this.constb = constb;
    }
    public NullIntersection(Set consta, SetVariable b) {
        super(new SetVariable[]{b}, new SetVariable[]{b});
        this.consta = consta;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode sourceNodes[] = getSetSourceNodes();
        ArrayList arcs = new ArrayList();
        
        if (sourceNodes.length > 1)
            arcs.add(new GenericSetNullIntersectionArc(sourceNodes));
        else if (consta != null)
            arcs.add(new NodeSetNullIntersectionArc(consta, sourceNodes[0]));
        else
            arcs.add(new NodeSetNullIntersectionArc(sourceNodes[0], constb));
        
        return (Arc[]) arcs.toArray(new Arc[arcs.size()]);
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