package jopt.csp.spi.arcalgorithm.graph.arc.global;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that constraints a numeric variable to be a member of an array
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class GenericNumNotMemberOfArrayReflexArc extends GenericArc implements NumArc {

    public GenericNumNotMemberOfArrayReflexArc(Node expr, NumNode targets[]) {
        super(new Node[]{expr}, targets);
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
            // propagation is only necessary when source is bound
            NumNode source = (NumNode) sources[0];
            if (source.isBound()) {
                
                // remove bound value from each target
            Number val = source.getMin();
                for (int i=0; i<targets.length; i++)
                	((NumNode) targets[i]).removeValue(val);
        }
    }
}
