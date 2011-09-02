package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import java.util.ArrayList;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Binary arc that constraints a numeric variable to be a member of a set
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class BinarySetMemberOfSetArc extends BinarySetArc {

    private SetNode<Number> set;
    private ArrayList<Number> workingList;
    
    public BinarySetMemberOfSetArc(SetNode<Number> set, NumNode expr) {
        super(set, expr);
        this.set = set;
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        NumNode z = (NumNode) target;
        
        // check if deltas are available
        if (useDeltas) {
            
            // remove values no longer possible in delta from target
            Iterator<Number> removedIter = set.getPossibleDeltaSet().iterator();
            while (removedIter.hasNext())
                z.removeValue((Number) removedIter.next());
        }
        
        // deltas are not possible, need to iterate over values in
        // target and make sure they are still possible
        else {
            // initialize working list
            if (workingList == null)
                workingList = new ArrayList<Number>();
            else
            	workingList.clear();
            
            // build list of values to remove
            NumberIterator targetIter = z.values();
            while (targetIter.hasNext()) {
            	Number val = (Number) targetIter.next();
                
                // remove the value if it is not possible
                if (!set.isPossible(val))
                    workingList.add(val);
            }
            
            // remove all values in the list
            for (int i=0; i<workingList.size(); i++)
                set.removePossible(workingList.get(i));
        }
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
