package jopt.csp.spi.arcalgorithm.graph.arc.node;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z(target) has null-intersection x(source)
 */
public class NodeSetNullIntersectionArc<T> extends NodeSetArc<T> {
    private Set<T> constVals;
    private boolean constSource;
    
    /**
     * Internal Constructor
     */
    private NodeSetNullIntersectionArc(SetNode<T> node, Set<T> constVals, boolean constSource) {
        super(node);
        this.constVals = constVals;
        this.constSource = constSource;
    }
    
    /**
     * Constructor
     */
    public NodeSetNullIntersectionArc(SetNode<T> x, Set<T> z) {
        this(x, z, false);
    }
    
    /**
     * Constructor
     */
    public NodeSetNullIntersectionArc(Set<T> x, SetNode<T> z) {
        this(z, x, true);
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        if (constSource) {
            buf.append("Z(");
            buf.append(node);
        }
        else {
            buf.append("z(");
            buf.append(constVals);
        }
        
        buf.append(") has null-intersection with ");
        
        if (constSource) {
            buf.append("x(");
            buf.append(constVals);
        }
        else {
            buf.append("X(");
            buf.append(node);
        }
        
        buf.append(")");
        
        return buf.toString();
    }

    /**
     * Attempts to reduce values in target node domain based on values
     * in source node(s)
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    public void propagate() throws PropagationFailureException {
        @SuppressWarnings("unchecked")
		SetNode<T> snode = (SetNode<T>) node;
        
        // Loop over constant values and remove required values from variable
        Iterator<T> iterator = constVals.iterator();
        while (iterator.hasNext()) {
            Object val = iterator.next();
            snode.removePossible(val);
        }
    }
}
