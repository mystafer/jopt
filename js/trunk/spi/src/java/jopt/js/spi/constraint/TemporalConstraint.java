package jopt.js.spi.constraint;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.js.api.variable.SchedulerExpression;
import jopt.js.spi.graph.arc.TemporalArc;
import jopt.js.spi.variable.ActivityExpr;

/**
 * A constraint to maintain temporal relations between activities.
 * 
 * @author jboerkoel
 */
public class TemporalConstraint extends AbstractConstraint implements VariableChangeSource, SchedulerConstraint{

	//Maintains a copy of arcs if one has been produced
	protected Arc[] arcs;
	//int representing delay between two activities
	private int delay;
	//BEFORE vs. AFTER vs. AT
	private int relation;
	//START, DURATION, END of source activity
	private int sourceEntity;
	//START, DURATION, END of target activity
	private int targetEntity;
	
	//Source activity or expression, only one will be used
	private ActivityExpr sourceActivity;
	private IntExpr sourceExpr;
	//Target activity or expression, only one of the following will be used
	private ActivityExpr targetActivity;
	private IntExpr targetExpr;
	
	//A copy of all expressions, only maintained if it has been asked for 
	SchedulerExpression[] expressions;
	
    /**
     * Constructs a temporal constraint
     * @param source Activity that acts the source
     * @param target activity that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     * @param delay the amount of delay that must occur between the two 
     */
    public TemporalConstraint(ActivityExpr source, ActivityExpr target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceActivity = source;
        this.targetActivity = target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal constraint
     * @param source expr that acts the source
     * @param target activity that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     * @param delay the amount of delay that must occur between the two 
     */
    public TemporalConstraint(IntExpr source, ActivityExpr target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceExpr = source;
        this.targetActivity = target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal constraint
     * @param source Activity that acts the source
     * @param target expression that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     * @param delay the amount of delay that must occur between the two 
     */
    public TemporalConstraint(ActivityExpr source, IntExpr target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceActivity = source;
        this.targetExpr = target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal constraint
     * @param source Activity that acts the source
     * @param target activity that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     */
    public TemporalConstraint(ActivityExpr source, ActivityExpr target, int sourceEntity, int targetEntity,int relation) {
    	this(source,target,sourceEntity,targetEntity,relation,0);
    }
    
    /**
     * Constructs a temporal constraint
     * @param source expression that acts the source
     * @param target activity that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     */
    public TemporalConstraint(IntExpr source, ActivityExpr target, int sourceEntity, int targetEntity,int relation) {
    	this(source,target,sourceEntity,targetEntity,relation,0);
    }
    
    /**
     * Constructs a temporal constraint
     * @param source Activity that acts the source
     * @param target expression that acts as the target
     * @param sourceEntity an int representing which of the entities (start, end, duration) of the source is being referenced
     * @param targetEntity an int representing which of the entities (start, end, duration) of the target is being referenced
     * @param relation an int representing a constant BEFORE, AFTER, AT
     */
    public TemporalConstraint(ActivityExpr source, IntExpr target, int sourceEntity, int targetEntity,int relation) {
    	this(source,target,sourceEntity,targetEntity,relation,0);
    }
	
	public SchedulerExpression[] getExpressions() {
		if (expressions == null) {
			if (sourceActivity != null) {
				if (targetActivity != null) {
					expressions = new SchedulerExpression[2];
					expressions[1] = targetActivity; 
				}
				else {
					expressions = new SchedulerExpression[1];	
				}
				expressions[0] = sourceActivity;
			}
			else {
				if (targetActivity != null) {
					expressions = new SchedulerExpression[1];
					expressions[0] = targetActivity; 
				}
				else {
					expressions = new SchedulerExpression[0];	
				}
			}
		}
		return expressions;
	}
    
    //a method that returns the opposite relation ie after --> before etc
    private int getOppositeRelation(int relation) {
    	switch (relation) {
    		case TemporalArc.AFTER:
    			return TemporalArc.BEFORE;
    		case TemporalArc.BEFORE:
    			return TemporalArc.AFTER;
    		case TemporalArc.AT:
    			return TemporalArc.AT;
    	}
    	return relation;
    }
	
	//private helper method to create the arcs
	private Arc[] createArcs() {
		if (sourceExpr!=null) {
			TemporalArc arc1 = new TemporalArc((IntNode)sourceExpr.getNode(),targetActivity.getNode(),sourceEntity,targetEntity,relation, delay);
			TemporalArc arc2 = new TemporalArc(targetActivity.getNode(),(IntNode)sourceExpr.getNode(),targetEntity,sourceEntity,getOppositeRelation(relation), delay);
			return new Arc[] {arc1,arc2};
		}
		else if (targetExpr!=null) {
			TemporalArc arc1 = new TemporalArc(sourceActivity.getNode(),(IntNode)targetExpr.getNode(),sourceEntity,targetEntity,relation, delay);
			TemporalArc arc2 = new TemporalArc((IntNode)targetExpr.getNode(),sourceActivity.getNode(),targetEntity,sourceEntity,getOppositeRelation(relation), delay);
			return new Arc[] {arc1,arc2};
		}
		else {
			TemporalArc arc1 = new TemporalArc(sourceActivity.getNode(),targetActivity.getNode(),sourceEntity,targetEntity,relation, delay);
			TemporalArc arc2 = new TemporalArc(targetActivity.getNode(),sourceActivity.getNode(),targetEntity,sourceEntity,getOppositeRelation(relation), delay);
			return new Arc[] {arc1,arc2};
		}
	}
	
	/**
	 * Posts the constraint to the graph
	 */
    public void postToGraph() {
        if (graph!=null) {
            if (sourceActivity!= null) {
            	sourceActivity.updateGraph(graph);
            }
            else {
            	sourceExpr.updateGraph(graph);
            }
            if (targetActivity!= null) {
            	targetActivity.updateGraph(graph);
            }
            else {
            	targetExpr.updateGraph(graph);
            }
        	
        	// create arcs for variables
            if (arcs == null) arcs = createArcs();
            for (int i=0; i<arcs.length; i++)
                graph.addArc(arcs[i]);
        }
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
        super.addVariableChangeListener(listener);
        if (sourceActivity!= null) {
        	sourceActivity.addVariableChangeListener(listener);
        }
        else {
        	sourceExpr.addVariableChangeListener(listener);
        }
        if (targetActivity!= null) {
        	targetActivity.addVariableChangeListener(listener);
        }
        else {
        	targetExpr.addVariableChangeListener(listener);
        }
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
