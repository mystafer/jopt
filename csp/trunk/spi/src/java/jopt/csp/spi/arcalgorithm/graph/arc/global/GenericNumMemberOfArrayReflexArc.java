package jopt.csp.spi.arcalgorithm.graph.arc.global;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.NumberList;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that constraints a numeric variable to be a member of an array
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class GenericNumMemberOfArrayReflexArc extends GenericArc implements NumArc {

    public GenericNumMemberOfArrayReflexArc(Node expr, NumNode targets[]) {
        super(new Node[]{expr}, targets);
    }
    
    /**
     * Propagates changes in source set nodes
     */
    private void propagateToTarget(NumNode z) throws PropagationFailureException {
        NumNode x = (NumNode) sources[0];
        
        // check if deltas are available
        if (useDeltas) {
            
            // remove values no longer possible in delta from target
            NumberIterator removedIter = x.deltaValues();
            while (removedIter.hasNext())
                z.removeValue((Number) removedIter.next());
        }
        
        // deltas are not possible, need to iterate over values in
        // target and make sure they are still possible
        else {
            NumberList numList = NumberList.pool.borrowList();
            
            try {
                NumberIterator targetIter = z.values();
                while (targetIter.hasNext()) {
                    Number val = (Number) targetIter.next();
                    
                    // remove the value if it is not possible
                    if (!x.isInDomain(val))
                        numList.add(val);
                }
                
                // remove invalid values
                for (int i=0; i<numList.size(); i++)
                    z.removeValue(numList.get(i));
            }
            finally {
            	NumberList.pool.returnList(numList);
            }
        }
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        for (int i=0; i<targets.length; i++)
        	propagateToTarget((NumNode) targets[i]);
    }
}
