package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Binary arc that constraints a numeric variable not to be a member of a set
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class BinarySetNotMemberOfSetArc extends BinarySetArc {

    private SetNode<Number> set;
    
    public BinarySetNotMemberOfSetArc(SetNode<Number> set, NumNode expr) {
        super(set, expr);
        this.set = set;
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        NumNode z = (NumNode) target;
        
        // check if deltas are available
        if (useDeltas) {
            
            // remove values in source required delta from target
            Iterator<Number> removedIter = set.getRequiredDeltaSet().iterator();
            while (removedIter.hasNext())
                z.removeValue((Number) removedIter.next());
        }
        
        // deltas are not possible, need to iterate over values in
        // target and make sure they are not required by set
        else {
            NumberIterator targetIter = z.values();
            while (targetIter.hasNext()) {
            	Number val = (Number) targetIter.next();
                
                // remove the value if it is required
                if (set.isRequired(val))
                    z.removeValue(val);
            }
        }
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
