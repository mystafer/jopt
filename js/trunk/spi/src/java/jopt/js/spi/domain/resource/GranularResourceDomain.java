
package jopt.js.spi.domain.resource;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.csp.variable.PropagationFailureException;

/**
 * Extension of a standard resource domain.
 * 
 * Behavior is similar to a standard resource, but capacity is split into uniformly sized buckets.
 * The user simply needs to specify size of the bucket as well as an index to base the buckets off of.
 * 
 * The provided functionality is important for situations in which the unit of time used for resource
 * availability is not the same as that of the activities.  For instance, supposed an activity requires
 * X units of resource A every 15 minutes; thus, 1 unit of time = 15 minutes.  However, resource A
 * has Y units available each hour (60 minutes).  Rather than estimating the availability of resource
 * A to be Y/4 per unit of time, we can model resource A as a granular resource with a bucket size of 4. 
 *  
 * @author James Boerkoel
 */
public class GranularResourceDomain extends ResourceDomain {

	//Refers to the size that each bucket is; each bucket is treated as a unit
	//Thus a bucket of size 10 will use up all or nothing of the 10 consecutive units of the resource
	private int bucketSize;
	//The index off of which to count buckets.  If bucket size is 10, and offset is 3, then 13-22 would be a bucket,
    //23-32 would be a bucket, etc.
	private int offset;
	
	//This is actual domain used
	ResourceDomain resDom;
    
	/**
	 * Constructs an instance of a granular Resource Domain
	 * @param resDom resource domain to which make granular
	 * @param bucketSize number of units in one grouping
	 * @param offset an index off of which to base the buckets
	 */
	public GranularResourceDomain(ResourceDomain resDom, int bucketSize, int offset){
		super(resDom.startTime, resDom.endTime);
		this.resDom = resDom;
		this.bucketSize = bucketSize;
		this.offset = offset;
	}
	
    // javadoc is inherited
	public boolean isUsed() {
		return resDom.isUsed();
	}
	
    // javadoc is inherited
	public int getType() {
		return resDom.getType();
	}
	
	//this is a utility method that will translate a given start time to the beginning of that start time's "bucket"
	private int getActualStartTime(int start){
		return (((start-offset)/bucketSize)*bucketSize+offset);
	}
	
	//this is a utility method that will translate a given end time to the beginning of that end time's "bucket"
	private int getActualEndTime(int end){
		return ((((end-offset)/bucketSize)+1)*bucketSize+offset)-1;
	}
	
	//adjusts the intervals within this int interval set to start and end at bucket starts and ends
	private IntIntervalSet adjustTimeline(IntIntervalSet timeline) {
		IntIntervalSet iis = new IntIntervalSet();
		int index = timeline.getFirstIntervalIndex();
		while (index>=0) {
			iis.add(getActualStartTime(timeline.getIntervalStart(index)),getActualEndTime(timeline.getIntervalEnd(index)));
			index = timeline.getNextIntervalIndex(index);
		}
		return iis;
	}
	
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int start, int end, int quantity) {
		return resDom.findAvailIntervals(start,end,quantity);
	}
	
    // javadoc is inherited
	public void registerAllocatedOperation(int opID, int est, int let, int minDur) throws PropagationFailureException {
		resDom.registerAllocatedOperation(opID, est, let, minDur);
	}
	
    // javadoc is inherited
	public int getNumberOfOperationsAssigned() {
		return resDom.getNumberOfOperationsAssigned();
	}
	
    // javadoc is inherited
	public int getResourceStart() {
		return resDom.getResourceStart();
	}
	
    // javadoc is inherited
	public int getResourceEnd() {
		return resDom.getResourceEnd();
	}

    // javadoc is inherited
	public int getTotalCapacityAvailable() {
		return resDom.getTotalCapacityAvailable();
	}
	
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int operationID,int start, int end, int quantity) {
		return resDom.findAvailIntervals(operationID, start, end, quantity);
	}

    // javadoc is inherited
	public boolean isResourceAvailable(int operationID, int start, int end, int quantity) {
		return resDom.isResourceAvailable(operationID, start, end, quantity);
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int start, int end) {
		return resDom.maxAvailableResource(start, end);
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int operationID, int start, int end) {
		return resDom.maxAvailableResource(operationID, start, end);
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int operationID, int start, int end, int minDur) {
		return resDom.maxAvailableResource(operationID, start, end, minDur);
	}

    // javadoc is inherited
	public void setPotentialOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
		resDom.setPotentialOperationTimeline(operationID,adjustTimeline(timeline));
	}
	
    // javadoc is inherited
	public void setActualOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
		resDom.setActualOperationTimeline(operationID,adjustTimeline(timeline));
	}
	
    // javadoc is inherited
	protected void registerUsageChange(int operationID, IntValIntervalSet delta) {
		resDom.registerUsageChange(operationID,delta);
	}
	
    // javadoc is inherited
	public void clearDelta() {
		resDom.clearDelta();
	}
	
    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
    	resDom.beforeChoicePointPopEvent();
    }
    
    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    	resDom.afterChoicePointPopEvent();   
    }
    
    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
    	resDom.beforeChoicePointPushEvent();
    }
    
    // javadoc is inherited
    public void afterChoicePointPushEvent() {
    	resDom.afterChoicePointPushEvent();
    }
    
    public Object clone() {
    	return new GranularResourceDomain((ResourceDomain)resDom.clone(),bucketSize,offset);
    }
    
    // javadoc is inherited
    public Object getDomainState() {
        return resDom.getDomainState();
    }

    // javadoc is inherited
    public void restoreDomainState(Object state) {
        resDom.restoreDomainState(state);
    }
	
    // javadoc is inherited
    public boolean choicePointStackSet() {
        return resDom.choicePointStackSet();
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	resDom.setChoicePointStack(cps);
    }
	
    // javadoc is inherited
    public String toString() {
    	return resDom.toString();
    }
    
    // javadoc is inherited
	public boolean isBuilt() {
		return resDom.isBuilt();
	}

    // javadoc is inherited
	public void setBuilt(boolean built) {
		resDom.setBuilt(built);
	}
	
    // javadoc is inherited
	public boolean needsPropagation() {
		return resDom.needsPropagation();
	}

    // javadoc is inherited
	public void setNeedsPropagation(boolean needsPropagation) {
		resDom.setNeedsPropagation(needsPropagation);
	}
	
}
