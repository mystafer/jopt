package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Binary arc that constrains a set variable to contain values contained in a specific
 * variable
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class BinarySetMemberOfSetReflexArc extends BinaryArc implements NumArc {

    private SetNode set;
    
    public BinarySetMemberOfSetReflexArc(NumNode expr, SetNode set) {
        super(expr, set);
        this.set = set;
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        NumNode x = (NumNode) source;
        
        // check if deltas are available
        if (useDeltas) {
            // remove values no longer possible in delta from target
            NumberIterator removedIter = x.deltaValues();
            while (removedIter.hasNext())
                set.removePossible(removedIter.next());
        }
        
        // deltas are not possible, need to iterate over values in
        // target and make sure they are still possible
        else {
            Iterator targetIter = set.getPossibleSet().iterator();
            while (targetIter.hasNext()) {
            	Number val = (Number) targetIter.next();
                
                // remove the value if it is not possible
                if (!x.isInDomain(val))
                    set.removePossible(val);
            }
        }
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
