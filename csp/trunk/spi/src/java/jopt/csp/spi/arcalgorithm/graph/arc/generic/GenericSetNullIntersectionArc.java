package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing null-intersection of source nodes
 */
public class GenericSetNullIntersectionArc extends GenericSetArc {
    /**
     * Constructor
     *
     * @param   sources     Source nodes in equation
     */
    public GenericSetNullIntersectionArc(SetNode[] sources) {
        super(sources, sources);
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Nodes with null-intersection ( ");
        
        for (int i=0; i<sources.length; i++) {
            if (i>0) buf.append(", ");
            buf.append(sources[i]);
        }
        
        buf.append(" )");
        
        return buf.toString();
    }

    /**
     * Handles propagation useing the source nodes delta or complete required set
     */
    private void filterTarget(Node src, boolean requiredDelta) throws PropagationFailureException {
        SetNode x = (SetNode) src;

        // Loop over recently required values in X and remove as possibility
        // from all other variables
        Iterator iterator = requiredDelta ? x.getRequiredDeltaSet().iterator() : x.getRequiredSet().iterator();
        while (iterator.hasNext()) {
            Object xval = iterator.next();

            // Loop over other other variables
            for (int j=0; j<sources.length; j++) {
                SetNode z = (SetNode) sources[j];

                // Skip if x and z are same node
                if (!x.equals(z))
                    z.removePossible(xval);
            }
        }
    }

    public void propagate() throws PropagationFailureException {
        for (int i=0; i<sources.length; i++)
            filterTarget(sources[i], false);
    }

    public void propagate(Node src) throws PropagationFailureException {
        filterTarget(src, useDeltas);
    }
}
