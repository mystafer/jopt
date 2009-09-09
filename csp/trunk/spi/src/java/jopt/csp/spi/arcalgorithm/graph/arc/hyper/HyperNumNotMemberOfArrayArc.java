package jopt.csp.spi.arcalgorithm.graph.arc.hyper;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that constraints a numeric variable to not be a member of an array
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class HyperNumNotMemberOfArrayArc extends HyperArc implements NumArc {

    public HyperNumNotMemberOfArrayArc(NumNode sources[], Node expr) {
        super(sources, expr);
    }
    
    /**
     * Propagates changes in source set nodes
     */
    private void propagateFromSource(Node sources[]) throws PropagationFailureException {
        NumNode z = (NumNode) target;
        
        // remove values any bound value in sources from target
        for (int i=0; i<sources.length; i++) {
        	NumNode source = (NumNode) sources[i];
            
            if (source.isBound())
                z.removeValue(source.getMin());
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
