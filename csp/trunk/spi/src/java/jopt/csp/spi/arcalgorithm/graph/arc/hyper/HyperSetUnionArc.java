package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = union( sources )
 */
public class HyperSetUnionArc extends HyperSetArc {
    /**
     * Constructor
     *
     * @param   sources     Source nodes in equation
     * @param   target      Target node in equation
     */
    public HyperSetUnionArc(SetNode[] sources, SetNode target) {
        super(sources, target);
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(target);
        buf.append(") =  union of ( ");
        
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
        
        // Loop over values recently required in source and require from target
        Iterator iterator = x.getRequiredDeltaSet().iterator();
        while (iterator.hasNext())
            z.addRequired(iterator.next());

        // Loop over values recently removed from source
        iterator = x.getPossibleDeltaSet().iterator();
        while (iterator.hasNext()) {
            Number val = (Number) iterator.next();
            
            // Check if value is already removed as a possibility by target
            if (z.isPossible(val)) {
                // Determine if value is possible in any other source
                boolean possible = false;
                for (int i=0; i<sources.length; i++) {
                    SetNode source = (SetNode) sources[i];
                    if (source!=x && source.isPossible(val)) {
                        possible = true;
                        break;
                    }
                }

                // Remove value from target if not possible by any other sources
                if (!possible) {
                    z.removePossible(val);
                }
            }
        }
    }

    /**
     * Filters target nodes without using delta information in source nodes
     */
    private void propagateNoDeltas() throws PropagationFailureException {
        SetNode z = (SetNode) target;
        
        // add any required value in sources to z
        for (int i=0; i<sources.length; i++) {
            SetNode source = (SetNode) sources[i];
            
            // add required values to z
            Iterator valIter = source.getRequiredSet().iterator();
            while (valIter.hasNext())
                z.addRequired(valIter.next());
        }
        
        // build set of values to remove
        HashSet valsToRemove = new HashSet(z.getPossibleSet());
        for (int i=0; i<sources.length; i++) {
            SetNode source = (SetNode) sources[i];
            valsToRemove.removeAll(source.getPossibleSet());
        }
        
        // remove values from z no longer supported
        Iterator valIter = valsToRemove.iterator();
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
