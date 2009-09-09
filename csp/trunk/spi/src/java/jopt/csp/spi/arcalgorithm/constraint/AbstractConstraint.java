package jopt.csp.spi.arcalgorithm.constraint;

import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.PostableConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.VariableChangeBase;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspBooleanExpr;

/**
 * An abstract class used to represent a constraint.  This class will contain all the items
 * that are common to boolean, numerical, and set constraints.  Each of the aforementioned
 * types of constraints will implement or extend <code>AbstractConstraint</code>. 
 *
 */
public abstract class AbstractConstraint extends VariableChangeBase implements NumConstants, GraphConstraint {
    protected AbstractConstraint opposite;
    protected NodeArcGraph graph;
    
    /**
     * Constructor
     */
    protected AbstractConstraint() {
    }
    
    /**
     * Associates a constraint to a graph
     */
    public void associateToGraph(NodeArcGraph graph) {
        this.graph = graph;
    }
    
    /**
     * Returns true if constraint cannot be satisfied
     */
    public boolean isFalse() {
        return isViolated(false);
    }
    
    /**
     * Returns true if constraint cannot be dissatisfied
     */
    public boolean isTrue() {
        AbstractConstraint opposite = (AbstractConstraint) getPostableOpposite();
        
        if (opposite==null)
            return false;
        else
        	return opposite.isViolated(true);
    }
    
    /**
     * Returns true if constraint cannot be satisfied.
     * If all violated is true, this method returns true only when all conditions are violated.
     * If all violated is false, this method returns true when any condition is violated.
     * The presence of multiple conditions is an issue when generic vars/exprs are involved.
     * 
     * @param allViolated Determines the criteria for violation
     */
    public boolean isViolated(boolean allViolated) {
    	return false;
    }
    
    /**
     * Returns a constraint that is the exact opposite of this constraint
     */
    protected abstract AbstractConstraint createOpposite();
    
    /**
     * Returns a constraint that is the opposite of this constraint
     */
    public final PostableConstraint getPostableOpposite() {
        if (opposite==null) opposite = createOpposite();
        if (opposite != null && graph!=null && opposite.graph==null) opposite.associateToGraph(graph);
        return opposite;
    }
    
    // javadoc inherited from GraphConstraint
    public final GraphConstraint getGraphConstraintFragment(GenericIndex indices[]) {
        return createConstraintFragment(indices);
    }
    
    // javadoc inherited from CspConstraint
    public final CspBooleanExpr toBoolean() {
        return new BooleanExpr(NameUtil.nextName(), this);
    }
    
    /**
     * Returns a constraint that is a fragment of the current constraint based upon
     * a set of indices that specifies what portion of constraint should be returned
     * 
     * @param indices   Array of indices that indicate fragment to return.  Each index
     *                  must be set to a specific value to indicate fragment desired.
     */
    protected abstract AbstractConstraint createConstraintFragment(GenericIndex indices[]);
}