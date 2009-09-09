package jopt.js.spi.constraint;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.Resource;
import jopt.js.api.variable.SchedulerExpression;
import jopt.js.spi.graph.arc.ForwardCheckArc;
import jopt.js.spi.graph.arc.ForwardCheckReflexArc;
import jopt.js.spi.graph.node.ResourceNode;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.spi.variable.ResourceExpr;

/**
 * A constraint to maintain forward check consistency on activities and resources.
 * 
 * @author jboerkoel
 */
public class ForwardCheckConstraint extends AbstractConstraint implements VariableChangeSource, SchedulerConstraint{

	//Maintains a copy of arcs if one has been produced
	protected Arc[] arcs;
	//This is the operation ID that this arc represents
	private int operationID;
	//Activity that is involved with the constraint
	ActivityExpr activity;
	//Array of resources involved in this constraint
	ResourceExpr[] resources;
	//A copy of all expressions, only maintained if it has been asked for 
	SchedulerExpression[] expressions;
	
    /**
	 * Creates a Forward Check Constraint
	 * @param activity activity involved in this constraint
	 * @param resources resources used in this constraint
	 * @param operationID id of the operation that is represented by this constraint
	 */
	public ForwardCheckConstraint(Activity activity, Resource[] resources, int operationID) {
		this.operationID = operationID;
		this.activity = (ActivityExpr)activity;
		this.resources = new ResourceExpr[resources.length];
		for (int i=0; i<resources.length; i++) {
			this.resources[i] = (ResourceExpr) resources[i];
		}
		
		
	}
	
	public SchedulerExpression[] getExpressions() {
		if (expressions ==null) {
			expressions = new SchedulerExpression[resources.length+1];
			for (int i=0;i<resources.length; i++) {
				expressions[i] = resources[i];
			}
			expressions[resources.length] = activity;
		}
		return expressions;
	}
	
	//creates arcs to post to graph
	private Arc[] createArcs() {
		ResourceNode[] resNodes = new ResourceNode[resources.length];
		for (int i=0; i< resources.length; i++) {
			resNodes[i] = resources[i].getNode();
		}
		ForwardCheckArc arc1 = new ForwardCheckArc(activity.getNode(), resNodes,operationID);
		ForwardCheckReflexArc arc2 = new ForwardCheckReflexArc(resNodes, activity.getNode(), operationID);
		return new Arc[] {arc1,arc2};
	}
	
	/**
	 * Posts this constraint to the graph by making sure all relevant arcs and expressions have been added.
	 */
    public void postToGraph() {
        if (graph!=null) {
            if (activity != null) activity.updateGraph(graph);
            if (resources != null) {
            	for (int i=0; i<resources.length; i++) {
            		resources[i].updateGraph(graph);
            	}
            }
        	
        	// create arcs for variables
            if (arcs == null) arcs = createArcs();
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }
    
    /**
     * Adds a listener interested in variable change events
     * @param listener listener to variable changes
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);
        if (activity!=null)
            ((VariableChangeSource) activity).addVariableChangeListener(listener);
        for (int i=0;i<resources.length; i++) {
            ((VariableChangeSource) resources[i]).addVariableChangeListener(listener);
        }
    }
    
    /**
     * Returns the id of the operation represented by this constraint.
     * @return operation id of the operation that this constraint represents
     */
    public int getOperationID() {
    	return operationID;
    }
    
    //TODO: do any of the next methods make sense?  if not, maybe we should extend at a lower level than abstract method
    //javadoc inherited
	protected AbstractConstraint createConstraintFragment(GenericIndex[] indices) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//javadoc inherited
	public Arc[] getBooleanSourceArcs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//javadoc inherited
	public Node[] getBooleanSourceNodes() {
		// TODO Auto-generated method stub
		return null;
	}
	
    //javadoc inherited
	protected AbstractConstraint createOpposite() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
