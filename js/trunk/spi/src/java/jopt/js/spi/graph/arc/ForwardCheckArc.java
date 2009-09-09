package jopt.js.spi.graph.arc;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.SchedulerArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.graph.node.ActivityNode;
import jopt.js.spi.graph.node.ResourceNode;

/**
 * Arc to enforce and maintain consistency between activities and resources
 *
 *@author James Boerkoel
 */
public class ForwardCheckArc extends SchedulerArc {
	protected ActivityNode source;
	protected ResourceNode[] targets;
	protected int[] targetIDs;
	protected int operationID;
	
	/**
	 * Constructs a forward check ark
	 * @param source activity node on which this arc is defined
	 * @param targets resource node on which this arc is defined
	 * @param operationID operation id of the operation that this arc represents
	 */
	public ForwardCheckArc(ActivityNode source, ResourceNode[] targets, int operationID) {
		this.source = source;
		
		targetIDs = new int[targets.length];
		for (int i=0; i<targets.length; i++) {
			targetIDs[i] = targets[i].getID();
		}
		Arrays.sort(targetIDs);
		this.targets = new ResourceNode[targets.length];
		
		//This will ensure that resources are ordered for fast easy access later
		for (int i=0; i<targetIDs.length; i++) {
			for (int j=0; j<targets.length; j++) {
				if (targetIDs[i] == targets[j].getID()) {
					this.targets[i] = targets[j];
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
		if (source != null) {
			return new ActivityNode[] {source};
		}
		else {
			return null;
		}
	}
	
	/**
	 * Returns the target nodes of this Arc
	 */
	public Node[] getTargetNodes() {
		if (targets!=null) {
			return targets;
		}
		else {
			return null;
		}
	}
	
	
	//helper method returning the resource node for a given resource ID
	private ResourceNode getResourceNodeForID(int resourceID) {
		return targets[Arrays.binarySearch(this.targetIDs, resourceID)];
	}
	
    // javadoc is inherited
	public int[] getSourceDependencies() {
		if (sourceDependencies == null) {
			sourceDependencies = new int[]{DomainChangeType.DOMAIN};
		}
		
		return sourceDependencies;
	}
	
    // javadoc is inherited
	public void propagate() throws PropagationFailureException {
		for (int i=0; i<targetIDs.length; i++) {
			targets[i].setPotentialOperationTimeline(this.operationID,this.source.getPotentialUsageTimeline(this.operationID, targetIDs[i]));
			targets[i].setActualOperationTimeline(this.operationID,this.source.getActualUsageTimeline(this.operationID, targetIDs[i]));
		}   
		if (this.source.isOperationAssignedToResource(this.operationID)) {
			int resourceID = this.source.getRequiredResource(this.operationID);
			ResourceNode rn = getResourceNodeForID(resourceID);
			if (rn.getType() == ResourceNode.UNARY) {
				
				int est = this.source.getEarliestStartTime();
				int let = this.source.getLatestEndTime();
				int durMin = this.source.getDurationMin();
				rn.registerAllocatedOperation(operationID, est, let, durMin);
			}
		}
	}
	
    // javadoc is inherited
	public void propagate(Node src) throws PropagationFailureException {
		//Since there is only one source, we will always use the same source anyways, so this simply calls propagate
		propagate();
	}
}