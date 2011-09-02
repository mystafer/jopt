package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
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
public class HyperNumMemberOfArrayArc extends HyperArc implements NumArc {

    public HyperNumMemberOfArrayArc(NumNode sources[], NumNode expr) {
        super(sources, expr);
    }
    
    /**
     * Propagates changes in source set nodes
     */
    private void propagateFromSource(Node sources[]) throws PropagationFailureException {
        NumNode z = (NumNode) target;
        
        // check if deltas are available
        if (useDeltas) {
            
            // remove values no longer possible in delta from target
            for (int i=0; i<sources.length; i++) {
            	NumNode source = (NumNode) sources[i];
                NumberIterator removedIter = source.deltaValues();
                while (removedIter.hasNext())
                    z.removeValue((Number) removedIter.next());
            }
        }
        
        // deltas are not possible, need to iterate over values in
        // target and make sure they are still possible
        else {
            NumberList numList = NumberList.pool.borrowList();
                
            try {
                NumberIterator targetIter = z.values();
                while (targetIter.hasNext()) {
                	Number val = (Number) targetIter.next();
                    
                    // remove values no longer possible in delta from target
                    for (int i=0; i<sources.length; i++) {
                        NumNode source = (NumNode) sources[i];
                        
                        // remove the value if it is not possible
                        if (!source.isInDomain(val))
                            numList.add(val);
                    }
                }
                
                // remove values that were identified as invalid
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
        propagateFromSource(new Node[]{src});
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        propagateFromSource(sources);
    }
}
