package jopt.js.spi.graph.arc;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.SchedulerArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.graph.node.ActivityNode;
import jopt.js.spi.graph.node.ResourceNode;

/**
 * Arc to enforce and maintain consistency between resources and activities
 * 
 * @author James Boerkoel
 */
public class ForwardCheckReflexArc extends SchedulerArc {
    protected ActivityNode target;
    protected ResourceNode[] sources;
    protected int[] sourceIDs;
    protected int operationID;
    
    /**
     * Constructs a forward check reflex arc
     * @param sources resource nodes that are the targets
     * @param target activity node which is the source
     * @param operationID operationID of operation that this arc represents
     */
    public ForwardCheckReflexArc(ResourceNode sources[], ActivityNode target, int operationID) {
    	this.target = target;
        sourceIDs = new int[sources.length];
        for (int i=0; i<sources.length; i++) {
        	sourceIDs[i] = sources[i].getID();
        }
        Arrays.sort(sourceIDs);
        this.sources= new ResourceNode[sources.length];
        
        //This will ensure that resources are ordered for fast and easy access later
        for (int i=0; i<sourceIDs.length; i++) {
        	for (int j=0; j<sources.length; j++) {
        		if (sourceIDs[i] == sources[j].getID()) {
        			this.sources[i] = sources[j];
        			break;
        		}
        	}
        }
        this.operationID = operationID;
    }
    
    
    // javadoc is inherited
    public int getArcType() {
        return Arc.SCHEDULE;
    }
    
    // javadoc is inherited
    public Node[] getSourceNodes() {
        if (sources != null) {
        	return sources;
        }
        else {
        	return null;
        }
    }

    // javadoc is inherited
    public Node[] getTargetNodes() {
    	 if (target != null) {
    		 return new ActivityNode[] {target};
         }
         else {
         	return null;
         }
    }
    
    // javadoc is inherited
    public int[] getSourceDependencies() {
        if (sourceDependencies == null) {
            sourceDependencies = new int[sources.length];
            for (int i=0; i<sources.length; i++)
                sourceDependencies[i] = DomainChangeType.DOMAIN;
        }
        
        return sourceDependencies;
    }
    
    // javadoc is inherited
    public void propagate() throws PropagationFailureException {
    	for (int i=0; i<sources.length; i++) {
    		if (sources[i] != null) {
    			propagate(sources[i]);
    		}
    	}
    }

    // javadoc is inherited
    public void propagate(Node src) throws PropagationFailureException {
    	if (target.isOperationAssignedToResource(this.operationID)) {
    		if ((target.getRequiredResource(this.operationID)!=	((ResourceNode)src).getID())){
    			return;
    		}
    	}
    	int resID = ((ResourceNode)src).getID();
    	int minCap = target.getCapacityMin(this.operationID);

    	int est = target.getEarliestStartTime();
    	int let = target.getLatestEndTime();
    	int minDur = target.getDurationMin();
    	IntIntervalSet timeline = ((ResourceNode)src).findAvailIntervals(operationID,est,let,minCap);
    	target.setTimeline(operationID,resID,timeline);
    	est = target.getEarliestStartTime();
    	let = target.getLatestEndTime();
    	
    	if ((target.isOperationAssignedToResource(this.operationID)) && ((target.getRequiredResource(this.operationID)==((ResourceNode)src).getID()))){
    		int maxCap = ((ResourceNode)src).maxAvailableResource(operationID, est, let, minDur);
    		target.setCapacityMax(this.operationID, maxCap);
    	}
    }
}