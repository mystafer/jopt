package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z is subset of X
 */
public class BinarySetSubsetArc extends BinarySetArc {
    private boolean strict;
    
    /**
     * Constructor
     *
     * @param   source     Source node in equation
     * @param   target     Target node in equation
     * @param   strict     True if target is a strict subset of source
     */
    public BinarySetSubsetArc(SetNode source, SetNode target, boolean strict) {
        super(source, target);
        this.strict = strict;
    }

    public void propagate() throws PropagationFailureException {
        SetNode x = (SetNode) source;
        SetNode z = (SetNode) target;

        // check if deltas are supported
        if (useDeltas) {
            // Iterate over newly removed possible values in x removing values in z
            Iterator iterator = x.getPossibleDeltaSet().iterator();
            while (iterator.hasNext())
                z.removePossible(iterator.next());
        }
        
        // deltas are not supported
        else {
            // remove values from z no longer supported by x
            Iterator zIter = z.getPossibleSet().iterator();
            while (zIter.hasNext()) {
                
                // if value in z is not possible in x, it must
                // be removed from z
            	Object v = zIter.next();
                if (!x.isPossible(v))
                    z.removePossible(v);
            }
        }
        
        // check if this is a strict subset arc
        if (strict && x.isBound()) {
            // ensure x is not equal to z
    		if (x.getRequiredCardinality() == z.getRequiredCardinality())
                throw new PropagationFailureException();
            
            // if z is one value smaller than x, remove all values from z that are not required
            if (z.getRequiredCardinality() == x.getRequiredCardinality() - 1) {
                
                // iterate over smaller set
            	Iterator valIter = null;
                if (x.getRequiredCardinality() < z.getPossibleCardinality())
                    valIter = x.getRequiredSet().iterator();
                else
                    valIter = z.getPossibleSet().iterator();
                
                // remove any value not already required by z
                while (valIter.hasNext()) {
                    Object val = valIter.next();
                    
                	if (!z.isRequired(val))
                        z.removePossible(val);
                }
            }
        }
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Z(");
        buf.append(target);
        if (strict)
            buf.append(") is a strict subset of X(");
        else
        	buf.append(") is subset or equal to X(");
        buf.append(source);
        buf.append(")");

        return buf.toString();
    }
}
