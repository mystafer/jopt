package jopt.csp.spi.arcalgorithm.graph.arc.binary;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.PropagationFailureException;

/**
 * Binary arc that constrains a set variable not to contain values contained in a specific
 * variable
 * 
 * @author Nick Coleman
 * @version $Revision $
 */
public class BinarySetNotMemberOfSetReflexArc extends BinarySetArc {

    public BinarySetNotMemberOfSetReflexArc(NumNode expr, SetNode<Number> set) {
        super(expr, set);

        if (expr instanceof GenericNumExpr)
            throw new IllegalArgumentException("generic expressions not supported by not-member-of constraint");
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        @SuppressWarnings("unchecked")
		SetNode<Number> set = (SetNode<Number>) target;
        NumNode x = (NumNode) source;
        
        // when x is bound, make sure it is not in set
        if (x.isBound()) {
        	set.removePossible(x.getMin());
        }
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException {
        propagate();
    }
}
