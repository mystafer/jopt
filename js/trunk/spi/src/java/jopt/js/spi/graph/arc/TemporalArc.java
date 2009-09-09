package jopt.js.spi.graph.arc;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.SchedulerArc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.graph.node.ActivityNode;

/**
 * Arc to represent enforced temporal relationships between activities
 * 
 * @author James Boerkoel
 */
public class TemporalArc extends SchedulerArc {
    protected ActivityNode sourceActivityNode;
    protected IntNode sourceIntNode;
    protected ActivityNode targetActivityNode;
    protected IntNode targetIntNode;

    protected int delay;
    
    protected int sourceEntity;
    protected int targetEntity;
    protected int relation;

    public final static int START = 0;
    public final static int END = 1;
    public final static int CONSTANT = 2;
    //relationship
    public final static int BEFORE = 0;
    public final static int AT = 1;
    public final static int AFTER = 2;
    
    /**
     * Constructs a temporal arc
     * @param source source activity of the relationship
     * @param target target activity of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     * @param delay the amount of delay enforced between source and target in before and after relationships
     */
    public TemporalArc(ActivityNode source, ActivityNode target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceActivityNode = source;
        this.targetActivityNode = target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal arc
     * @param source source of the relationship
     * @param target target activity of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     * @param delay the amount of delay enforced between source and target in before and after relationships
     */
    public TemporalArc(IntNode source, ActivityNode target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceIntNode = source;
        this.targetActivityNode = target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal arc
     * @param source source activity of the relationship
     * @param target target of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     * @param delay the amount of delay enforced between source and target in before and after relationships
     */
    public TemporalArc(ActivityNode source, IntNode target, int sourceEntity, int targetEntity,int relation, int delay) {
        this.sourceActivityNode = source;
        this.targetIntNode= target;
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.relation = relation;
        this.delay = delay;
    }
    
    /**
     * Constructs a temporal arc and assumes no delay between activities
     * @param source source activity of the relationship
     * @param target target activity of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     */
    public TemporalArc(ActivityNode source, ActivityNode target, int sourceEntity, int targetEntity, int relation) {
    	this(source, target, sourceEntity, targetEntity, relation, 0);
    }
    
    /**
     * Constructs a temporal arc and assumes no delay between activities
     * @param source source of the relationship
     * @param target target activity of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     */
    public TemporalArc(IntNode source, ActivityNode target, int sourceEntity, int targetEntity,int relation) {
    	this(source, target, sourceEntity, targetEntity, relation, 0);
    }
    
    /**
     * Constructs a temporal arc and assumes no delay between activities
     * @param source source activity of the relationship
     * @param target target of the relationship
     * @param sourceEntity entity being referenced from the source (start time, end time, duration)
     * @param targetEntity entity being referenced from the target (start time, end time, duration)
     * @param relation int representing the relationships (before, after, at)
     */
    public TemporalArc(ActivityNode source, IntNode target, int sourceEntity, int targetEntity,int relation) {
    	this(source, target, sourceEntity, targetEntity, relation, 0);
    }
    
    // javadoc is inherited
    public int getArcType() {
        return Arc.SCHEDULE;
    }
    
    // javadoc is inherited
    public Node[] getSourceNodes() {
        if (sourceActivityNode!=null) {
        	return new ActivityNode[] {sourceActivityNode};
        }
        else {
        	return new IntNode[] {sourceIntNode};
        }
    }

    // javadoc is inherited
    public Node[] getTargetNodes() {
    	 if (targetActivityNode!=null) {
         	return new ActivityNode[] {targetActivityNode};
         }
         else {
         	return new IntNode[] {targetIntNode};
         }
    }

    // javadoc is inherited
    public int[] getSourceDependencies() {
    	if (sourceDependencies == null) {
    		sourceDependencies = new int[]{DomainChangeType.DOMAIN};
    	}
    	
    	return sourceDependencies;
    }
    
    protected void propagateBounds() throws PropagationFailureException {
    	int min=Integer.MIN_VALUE;
    	int max=Integer.MAX_VALUE;
    	switch(sourceEntity) {
	    	case START: {
	    		min = sourceActivityNode.getEarliestStartTime();
	    		max = sourceActivityNode.getLatestStartTime();
	    		break;
	    	}
	    	case END: {
	    		min = sourceActivityNode.getEarliestEndTime();
	    		max = sourceActivityNode.getLatestEndTime();
	    		break;
	    	}
	    	
	    	case CONSTANT: {
	    		max = sourceIntNode.getMax().intValue();
	    		min = sourceIntNode.getMin().intValue();
	    		break;
	    	}
    	}
    	
    	switch (targetEntity) {
	    	case START: {
	    		switch (relation) {
	    			case BEFORE: {
	    				if (targetActivityNode.getEarliestStartTime()<(min+delay+1)) {
	    					targetActivityNode.setEarliestStartTime(min+delay+1);
	    				}
	    				break;
	    			}
	    			case AT: {
	    				if (targetActivityNode.getEarliestStartTime()<min) {
	    					targetActivityNode.setEarliestStartTime(min);	
	    				}
	    				if (targetActivityNode.getLatestStartTime() > max) {
	    					targetActivityNode.setLatestStartTime(max);
	    				}
	    				break;
	    			}
	    			case AFTER: {
	    				if (targetActivityNode.getLatestStartTime()> (max-delay-1)) {
	    					targetActivityNode.setLatestStartTime(max-delay-1);
	    				}
	    				break;
	    			}
	    		}
	    		break;
	    	}
	    	case END: {
	    		switch (relation) {
	    			case BEFORE: {
	    				if (targetActivityNode.getEarliestEndTime() < (min+delay+1)) {
	    					targetActivityNode.setEarliestEndTime(min+delay+1);
	    				}
	    				break;
	    			}
	    			case AT: {
	    				if (targetActivityNode.getEarliestEndTime() < (min)) {
	    					targetActivityNode.setEarliestEndTime(min);
	    				}
	    				if (targetActivityNode.getLatestEndTime() > max) {
	    					targetActivityNode.setLatestEndTime(max);
	    				}
	    				break;
	    			}
	    			case AFTER: {
	    				if (targetActivityNode.getLatestEndTime() > (max-delay-1)) {
	    					targetActivityNode.setLatestEndTime(max-delay-1);
	    				}
	    				break;
	    			}
	    		}
	    		break;
	    	}
	    	
	    	case CONSTANT: {
	    		switch (relation) {
	    			case BEFORE: {
	    				if (targetIntNode.getMin().intValue() < (min+delay+1)) {
	    					targetIntNode.setMin(new Integer(min+delay+1));
	    				}
	    				break;
	    			}
	    			case AT: {
	    				if (targetIntNode.getMin().intValue() < min) {
	    					targetIntNode.setMin(new Integer(min));
	    				}
	    				if (targetIntNode.getMax().intValue() > max) {
	    					targetIntNode.setMax(new Integer(max));
	    				}
	    				break;
	    			}
	    			case AFTER: {
	    				if (targetIntNode.getMax().intValue() > (max-delay-1)) {
	    					targetIntNode.setMax(new Integer(max-delay-1));
	    				}
	    				break;
	    			}
	    		}
	    		break;
	    	}
    	}
    	
    	
    }
    
    // javadoc is inherited
    public void propagate() throws PropagationFailureException {
    	//The only time we have to worry about propagating to
        //beyond 'bounds consitency' is if two conditions hold:
    	//1. The strength is CspAlgorithmStrength.RANGE_CONSISTENCY or higher, and
        //2. The relation is AT (equal)
    	//In all other cases, bounds consistency is sufficient.
    	
    	propagateBounds();
    	//TODO check for above conditions and write a domain consistent propagation technique
    }

    // javadoc is inherited
    public void propagate(Node src) throws PropagationFailureException {
    	//Since there is only ever going to be one source, we can simply call propagate
    	propagate();
    }
}