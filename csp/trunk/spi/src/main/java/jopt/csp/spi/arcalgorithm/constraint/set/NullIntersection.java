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
public class NullIntersection<T> extends SetConstraint<T> {
    private Set<T> consta;
    private Set<T> constb;
    
    public NullIntersection(SetVariable<T> sources[]) {
        super(sources, sources);
    }
    @SuppressWarnings("unchecked")
	public NullIntersection(SetVariable<T> a, SetVariable<T> b) {
        super(new SetVariable[]{a, b}, new SetVariable[]{a, b});
    }
    @SuppressWarnings("unchecked")
	public NullIntersection(SetVariable<T> a, Set<T> constb) {
        super(new SetVariable[]{a}, new SetVariable[]{a});
        this.constb = constb;
    }
    @SuppressWarnings("unchecked")
	public NullIntersection(Set<T> consta, SetVariable<T> b) {
        super(new SetVariable[]{b}, new SetVariable[]{b});
        this.consta = consta;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode<T> sourceNodes[] = getSetSourceNodes();
        ArrayList<Arc> arcs = new ArrayList<Arc>();
        
        if (sourceNodes.length > 1)
            arcs.add(new GenericSetNullIntersectionArc<T>(sourceNodes));
        else if (consta != null)
            arcs.add(new NodeSetNullIntersectionArc<T>(consta, sourceNodes[0]));
        else
            arcs.add(new NodeSetNullIntersectionArc<T>(sourceNodes[0], constb));
        
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