package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc that further filters target nodes of a union based on the knowledge that when
 * only one node contains a possible value that is required in the union, it must
 * also be required by that node.
 */
public class GenericSetUnionAdvancedFilterArc<T> extends GenericSetArc {
    /**
     * Constructor
     *
     * @param   union       Source node in equation
     * @param   targets     Target nodes in equation
     */
    public GenericSetUnionAdvancedFilterArc(SetNode<T> union, SetNode<T> targets[])
    {
        super(new SetNode[]{union}, targets);
    }

    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Partition of Z( ");
        
        for (int i=0; i<targets.length; i++) {
        	if (i>0) buf.append(", ");
            buf.append(targets[i]);
        }
        
        buf.append(" ) = X( ");
        buf.append(sources[0]);
        buf.append(" ) filtered with knowledge of null intersection )");

        return buf.toString();
    }

    public void propagate() throws PropagationFailureException {
        @SuppressWarnings("unchecked")
		SetNode<T> union = (SetNode<T>) this.sources[0];
        @SuppressWarnings("unchecked")
		SetNode<T> targets[] = (SetNode<T>[]) this.targets;
        
        // Calculate required cardinality union should be if targets
        // had correct cardinality
        int cardinality = 0;
        for (int i=0; i<targets.length; i++)
            cardinality += targets[i].getRequiredCardinality();

        // If union cardinality does not match calculated cardinality, update targets
        if (cardinality != union.getRequiredCardinality()) {
            // repeat process until no more values can be found
            boolean foundReq = false;
            do {
                foundReq = false;
                
                // loop over newly required values in union
                // and check if it is still in the intersection
                Iterator<T> iterator = useDeltas ? union.getRequiredDeltaSet().iterator() : union.getRequiredSet().iterator();
                while (iterator.hasNext()) {
                    Object zval = iterator.next();
    
                    // search for node that is possible
                    SetNode<T> possibleNode = null;
                    for (int i=0; i<targets.length; i++) {
                        SetNode<T> target = targets[i];
                        
                        // value is already required, exit loop
                        if (target.isRequired(zval)) {
                            possibleNode = null;
                        	break;
                        }
                        
                        // located a node that is possible
                        else if (target.isPossible(zval)) {
                            // multiple possible nodes, can't require this value
                            if (possibleNode != null) {
                            	possibleNode = null;
                                break;
                            }
                            else {
                            	possibleNode = target;
                            }
                        }
                    }
                    
                    // a possible node was located for the value,
                    // require it
                    if (possibleNode != null) {
                        foundReq = true;
                        possibleNode.addRequired(zval);
                    }
                }
            } while (foundReq);
        }
    }

    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
