package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc that further filters target nodes of a union based on the source union and
 * and an intersection of the targets
 */
public class GenericSetUnionIntersectFilterArc extends GenericSetArc {
    private SetNode union;
    private SetNode targeta;
    private SetNode targetb;
    private SetNode intersect;

    /**
     * Constructor
     *
     * @param   union       Source node in equation
     * @param   targeta     First Target node in equation
     * @param   targetb     Second Target node in equation
     * @param   intersect   Intersection of target nodes used as a reference
     */
    public GenericSetUnionIntersectFilterArc(SetNode union, SetNode targeta,
        SetNode targetb, SetNode intersect)
    {
        super(new SetNode[]{union, intersect}, new SetNode[]{targeta, targetb});
        this.union = union;
        this.targeta = targeta;
        this.targetb = targetb;
        this.intersect = intersect;
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Union of Z( ");
        buf.append(targeta);
        buf.append(", ");
        buf.append(targetb);
        buf.append(" ) = X( ");
        buf.append(union);
        buf.append(" ) filtered with assistance of intersection ( ");
        buf.append(intersect);
        buf.append(" )");

        return buf.toString();
    }

    /**
     * Handles propagation using union's delta or complete required set
     */
    private void filterTarget(boolean requiredDelta) throws PropagationFailureException {
        // Calculate required cardinality union should be if targets
        // had correct cardinality
        int cardinality = targeta.getRequiredCardinality() +
            targetb.getRequiredCardinality() +
            -intersect.getRequiredCardinality();

        // If union cardinality does not match calculated cardinality, update targets
        if (cardinality != union.getRequiredCardinality()) {
            // repeat process until no more values can be found
            boolean foundReq = false;
            do {
                foundReq = false;
                
                // loop over newly required values in union
                // and check if it is still in the intersection
                Iterator iterator = requiredDelta ? union.getRequiredDeltaSet().iterator() : union.getRequiredSet().iterator();
                while (iterator.hasNext()) {
                    Object zval = iterator.next();
    
                    // Value is not in intersection, require it by one of the targets
                    if (!intersect.isPossible(zval))  {
                        // value is already required, skip value
                        if (!targeta.isRequired(zval) && !targetb.isRequired(zval)) {
                            // Determine target that should require
                            // the value
                            if (targeta.isPossible(zval))
                                targeta.addRequired(zval);
                            else
                                targetb.addRequired(zval);
                            
                            foundReq = true;
                        }
                    }
                }
            } while (foundReq);
        }
    }

    public void propagate() throws PropagationFailureException {
        filterTarget(false);
    }

    public void propagate(Node src) throws PropagationFailureException {
        filterTarget(useDeltas && src.equals(union));
    }
}
