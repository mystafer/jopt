package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z is superset of X
 */
public class BinarySetSupersetArc extends BinarySetArc {
    private boolean strict;
    
    /**
     * Constructor
     *
     * @param   source     Source node in equation
     * @param   target     Target node in equation
     * @param   strict     True if target is a strict superset of source
     */
    public BinarySetSupersetArc(SetNode source, SetNode target, boolean strict) {
        super(source, target);
        this.strict = strict;
    }

    public void propagate() throws PropagationFailureException {
        SetNode x = (SetNode) source;
        SetNode z = (SetNode) target;
        
        // check if deltas are supported
        if (useDeltas) {
            // Iterate over newly required values in x and add requirement to z
            Iterator iterator = x.getRequiredDeltaSet().iterator();
            while (iterator.hasNext())
                z.addRequired(iterator.next());
        }
        else {
            // Iterate over required values in x and add requirement to z
            Iterator iterator = x.getRequiredSet().iterator();
            while (iterator.hasNext())
                z.addRequired(iterator.next());
        }
        
        // check if this is a strict superset arc
        // and ensure x is not equal to z
        if (strict && z.isBound() && (x.getRequiredCardinality() == z.getRequiredCardinality()))
            throw new PropagationFailureException();
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(target);
        if (strict)
            buf.append(") is a strict superset of X(");
        else
            buf.append(") is superset or equal to X(");
        buf.append(source);
        buf.append(")");
        
        return buf.toString();
    }
}
