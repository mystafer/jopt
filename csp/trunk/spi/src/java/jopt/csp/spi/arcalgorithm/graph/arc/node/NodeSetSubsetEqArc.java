package jopt.csp.spi.arcalgorithm.graph.arc.node;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z is subset or equal to x
 */
public class NodeSetSubsetEqArc extends NodeSetArc {
    private Set constVals;
    private boolean constSource;
    private boolean strict;

    /**
     * Internal Constructor
     */
    private NodeSetSubsetEqArc(SetNode node, Set constVals, boolean constSource, boolean strict) {
        super(node);
        this.constVals = constVals;
        this.constSource = constSource;
        this.strict = strict;
    }

    /**
     * Constructor
     */
    public NodeSetSubsetEqArc(SetNode x, Set z, boolean strict) {
        this(x, z, false, strict);
    }

    /**
     * Constructor
     */
    public NodeSetSubsetEqArc(Set x, SetNode z, boolean strict) {
        this(z, x, true, strict);
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

        if (strict)
            buf.append(") is a strict subset of ");
        else
            buf.append(") is subset or equal to ");

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
        if (constSource) {
            SetNode z = (SetNode) node;

            // Iterate over values in z removing values not possible in x
            Iterator iterator = z.getPossibleSet().iterator();
            while (iterator.hasNext()) {
                Object zval = iterator.next();

                if (!constVals.contains(zval))
                    z.removePossible(zval);
            }

            // check if this is a strict subset arc
            if (strict && z.isBound()) {
                // ensure x is not equal to z
                if (z.getRequiredCardinality() == constVals.size())
                    throw new PropagationFailureException();
                
                // if z is one value smaller than x, remove all values from z that are not required
                if (z.getRequiredCardinality() == constVals.size() - 1) {
                    
                    // iterate over smaller set
                    Iterator valIter = null;
                    if (constVals.size() < z.getPossibleCardinality())
                        valIter = constVals.iterator();
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

        else {
            SetNode x = (SetNode) node;

            // Iterate over values in required values in x and add requirement to z
            Iterator iterator = constVals.iterator();
            while (iterator.hasNext()) {
                Object zval = iterator.next();
                x.addRequired(zval);
            }

            // check if this is a strict superset arc
            // and ensure x is not equal to constant values
            if (strict && x.isBound() && (x.getRequiredCardinality() == constVals.size()))
                throw new PropagationFailureException();
        }
    }
}
