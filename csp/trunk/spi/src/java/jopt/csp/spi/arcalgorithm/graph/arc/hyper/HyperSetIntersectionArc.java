package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = intersection( sources )
 */
public class HyperSetIntersectionArc extends HyperSetArc {
    /**
     * Constructor
     *
     * @param   sources     Source nodes in equation
     * @param   target      Target node in equation
     */
    public HyperSetIntersectionArc(SetNode[] sources, SetNode target) {
        super(sources, target);
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(target);
        buf.append(") =  intersection of ( ");

        for (int i=0; i<sources.length; i++) {
            if (i>0) buf.append(", ");
            buf.append(sources[i]);
        }

        buf.append(" )");

        return buf.toString();
    }

    /**
     * Filters target nodes using delta information in source nodes
     */
    private void propagateWithDeltas(Node src) throws PropagationFailureException {
        SetNode x = (SetNode) src;
        SetNode z = (SetNode) target;

        // Loop over values recently removed from source and remove from target
        Iterator iterator = x.getPossibleDeltaSet().iterator();
        while (iterator.hasNext())
            z.removePossible(iterator.next());

        // Loop over values recently required int source
        iterator = x.getRequiredDeltaSet().iterator();
        while (iterator.hasNext()) {
            Number val = (Number) iterator.next();

            // Check if value is already required by target
            if (!z.isRequired(val)) {
                // Determine if value is required in all sources
                boolean required = true;
                for (int i=0; i<sources.length; i++) {
                    SetNode source = (SetNode) sources[i];
                    if (source!=x) {
                        // check if it is not required by any other source
                    	if (!source.isRequired(val)) {
                            required = false;
                            break;
                        }
                    }
                }

                // Add value to target required set if
                // is required by all other sources
                if (required)
                    z.addRequired(val);
            }
        }
    }

    /**
     * Filters target nodes without using delta information in source nodes
     */
    private void propagateNoDeltas() throws PropagationFailureException {
        SetNode z = (SetNode) target;
        
        // create set of required values for z
        SetNode firstSource = (SetNode) sources[0];
        HashSet requiredVals = new HashSet(firstSource.getRequiredSet());
        for (int i=1; i<sources.length; i++) {
            SetNode source = (SetNode) sources[i];
            requiredVals.retainAll(source.getRequiredSet());
        }
        
        // add required values to z
        Iterator valIter = requiredVals.iterator();
        while (valIter.hasNext())
            z.addRequired(valIter.next());
        
        // build set of possible values for z
        HashSet possibleVals = new HashSet(firstSource.getPossibleSet());
        for (int i=1; i<sources.length; i++) {
            SetNode source = (SetNode) sources[i];
            possibleVals.retainAll(source.getPossibleSet());
        }
        
        // build set of values to remove
        HashSet valsToRemove = new HashSet(z.getPossibleSet());
        valsToRemove.removeAll(possibleVals);
        
        // remove values no longer possible from z
        valIter = valsToRemove.iterator();
        while (valIter.hasNext())
            z.removePossible(valIter.next());
    }

    public final void propagate() throws PropagationFailureException {
        propagateNoDeltas();
    }

    public final void propagate(Node src) throws PropagationFailureException {
        if (useDeltas)
            propagateWithDeltas(src);
        else
            propagateNoDeltas();
    }
}
