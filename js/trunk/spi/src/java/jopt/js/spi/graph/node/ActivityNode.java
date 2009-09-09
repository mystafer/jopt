package jopt.js.spi.graph.node;

import jopt.csp.spi.arcalgorithm.graph.node.AbstractNode;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActOperationDomain;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.listener.OperationDomainListener;
/**
 * Node to represent activities
 * 
 * @author jboerkoel
 */
public class ActivityNode extends AbstractNode implements OperationDomainListener{
    
    protected ActivityDomain actDom;
    
    /**
     * Constructs an ActivityNode given a name and an activity domain
     * @param name
     * @param actDom
     */
    public ActivityNode(String name, ActivityDomain actDom) {
    	super(name);
    	this.actDom = actDom;
    	this.actDom.addOperationListener(this);
    }
    
	/**
	 * Gets the operation ids that from which this activity is built
	 * @return operation ids that from which this activity is built
	 */
	public int[] getOperationIDs() {
		return actDom.getOperationIDs();
	}
    
    /**
     * Sets the id of this activity node by setting the id of its domain
     * @param id new id
     */
    public void setID(int id) {
    	actDom.setID(id);
    }
    
    /**
     * Returns the id of this activity 
     * @return id of this activity node
     */
    public int getID() {
    	return actDom.getID();
    }
    
	/**
     * Returns the earliest start time for this node
     * @return int that is the minimum start time for this activity
     */
    public int getEarliestStartTime() {
    	return actDom.getEarliestStartTime();
    }
    
    /**
     * Adds a resource to the operation specified by operationID.
     * @param operationID id of operation to which resource is being added
     * @param resourceID id of resource being added
     * @param start earliest availability of this resource
     * @param end latest availability of this resource
     * @param minDur minimum duration
     * @param maxDur maximum duration
     * @throws PropagationFailureException
     */
    public void addResource(int operationID, int resourceID, int start, int end, int minDur, int maxDur) throws PropagationFailureException {
    	actDom.addResource(operationID, resourceID, start, end, minDur, maxDur);
    	fireChangeEvent(DomainChangeType.DOMAIN);
    }

    /**
     * Returns a set that represents the times that the 
     * specified operation could use the specified resource
     * @param operationID id operation being inquired about
     * @param resourceID id of resource being inquired about
     * @return IntIntervalSet representing the times that this specified operation could use the specified resource 
     */
	public IntIntervalSet getPotentialUsageTimeline(int operationID, int resourceID) {
		return actDom.getPotentialUsageTimeline(operationID,resourceID);
	}
    
    /**
     * Returns a set that represents the times that the 
     * specified operation already actually use the specified resource
     * @param operationID id operation being inquired about
     * @param resourceID id of resource being inquired about
     * @return IntIntervalSet representing the times that this specified operation already actually uses the specified resource
     */
	public IntIntervalSet getActualUsageTimeline(int operationID, int resourceID) {
		return actDom.getActualUsageTimeline(operationID,resourceID);
	}
	
    /**
     * Returns the earliest start time for this node for the specified operation and resource
     * @param operationID id of operation being inquired about 
     * @param resourceID id of resource being inquired about 
     * @return int indicating the earliest start time
     */
    public int getEarliestStartTime(int operationID, int resourceID) {
    	return actDom.getEarliestStartTime(operationID, resourceID);
    }
    
    /**
     * Returns the latest start time for this node
     * @return the maximum start time for this node
     */
    public int getLatestStartTime() {
    	return actDom.getLatestStartTime();
    }

    /**
     * Returns the latest start time for this node for the specified operation and resource
     * @param operationID id of operation being inquired about 
     * @param resourceID id of resource being inquired about 
     * @return int indicating the latest start time
     */
    public int getLatestStartTime(int operationID, int resourceID){
    	return actDom.getLatestStartTime(operationID, resourceID);
    }
    
	/**
     * Returns the earliest end time for this node
     * @return returns the minimum of the end time
     */
    public int getEarliestEndTime() {
    	return (actDom.getEarliestEndTime());
    }

    /**
     * Returns the earliest end time for this node for the specified operation and resource
     * @param operationID id of operation being inquired about 
     * @param resourceID id of resource being inquired about 
     * @return int indicating the earliest end time
     */
    public int getEarliestEndTime(int operationID, int resourceID) {
    	return actDom.getEarliestEndTime(operationID, resourceID);
    }
    
    /**
     * Returns the latest end time for this node
     * @return returns the maximum of the end time
     */
    public int getLatestEndTime() {
    	return actDom.getLatestEndTime();
    }

    /**
     * Returns the latest end time for this node for the specified operation and resource
     * @param operationID id of operation being inquired about 
     * @param resourceID id of resource being inquired about 
     * @return int indicating the latest end time
     */
    public int getLatestEndTime(int operationID, int resourceID){
    	return actDom.getLatestEndTime(operationID, resourceID);
    }
    
    /**
     * Set the earliest start time for the specified resource
     * @param resourceID id of the resource in question
     * @param est the earliest start time
     * @throws PropagationFailureException
     */
    public void setEarliestStartTime(int resourceID, int est) throws PropagationFailureException {
    	actDom.setEarliestStartTime(resourceID,est);
    }
    
    /**
     * Set the earliest start time for the activity
     * @param est the earliest start time
     * @throws PropagationFailureException
     */
    public void setEarliestStartTime(int est) throws PropagationFailureException {
    	actDom.setEarliestStartTime(est);
    }

    /**
     * Set the latest start time for the specified resource
     * @param resourceId id of the resource
     * @param lst the latest start time
     * @throws PropagationFailureException
     */
    public void setLatestStartTime(int resourceId, int lst) throws PropagationFailureException {
    	actDom.setLatestStartTime(resourceId,lst);
    }
    
    /**
     * Set the latest start time for the activity
     * @param lst the latest start time
     * @throws PropagationFailureException
     */
    public void setLatestStartTime(int lst) throws PropagationFailureException {
    	actDom.setLatestStartTime(lst);
    }
    
    /**
     * Set the earliest end time for resource at resource index
     * @param resourceIdx index of the resource
     * @param eet the earliest end time
     * @throws PropagationFailureException
     */
    public void setEarliestEndTime(int resourceIdx, int eet) throws PropagationFailureException {
    	actDom.setEarliestEndTime(resourceIdx,eet);
    }
    
    /**
     * Set the earliest end time for the activity
     * @param eet the earliest end time
     * @throws PropagationFailureException
     */
    public void setEarliestEndTime(int eet) throws PropagationFailureException {
    	actDom.setEarliestEndTime(eet);
    }

    /**
     * Set the latest end time for the resource at resource index
     * @param resourceIdx index of the resource
     * @param let the latest end time
     * @throws PropagationFailureException
     */
    public void setLatestEndTime(int resourceIdx, int let) throws PropagationFailureException {
    	actDom.setLatestEndTime(resourceIdx,let);
    }
    
    /**
     * Set the latest end time for the activity
     * @param let the latest end time
     * @throws PropagationFailureException
     */
    public void setLatestEndTime(int let) throws PropagationFailureException {
    	actDom.setLatestEndTime(let);
    }
    
    /**
     * Sets the maximum duration of this activity node
     * @param durMax new duration maximum
     * @throws PropagationFailureException
     */
    public void setDurationMax(int durMax) throws PropagationFailureException {
    	actDom.setDurationMax(durMax);
    }
    
    /**
     * Sets the minimum duration of this activity node
     * @param durMin new duration minimum
     * @throws PropagationFailureException
     */
    public void setDurationMin(int durMin) throws PropagationFailureException {
    	actDom.setDurationMin(durMin);
    }
    
    /**
     * Returns the maximum duration of this activity node
     * @return the maximum duration of this activity node
     */
    public int getDurationMax() {
    	return actDom.getDurationMax();
    }
    
    /**
     * Returns the minimum duration of this activity node
     * @return the minimum duration of this activity node
     */
    public int getDurationMin() {
    	return actDom.getDurationMin();
    }
    
    /**
     * Removes specified time from being a possible start time for this activity
     * @param n time that is being removed
     * @throws PropagationFailureException
     */
    public void removeStartTime(int n) throws PropagationFailureException {
    	actDom.removeStartTime(n);
    }

    /**
     * Sets the start time of this activity to the specified time
     * @param n time to set start time of activity to
     * @throws PropagationFailureException
     */
    public void setStartTime(int n) throws PropagationFailureException {
    	actDom.setStartTime(n);
    }
    
    /**
     * Sets the start time of the given resource to the specified time
     * @param resourceID id of the resource in question
     * @param n time to set start time of activity to
     * @throws PropagationFailureException
     */
    public void setStartTime(int resourceID, int n) throws PropagationFailureException {
    	actDom.setEarliestStartTime(resourceID, n);
    	actDom.setLatestStartTime(resourceID, n);
    }

    /**
     * Removes a range of values from the valid start times
     * @param start start value of the range of start times that are to be removed
     * @param end end value of the range of start times that are to be removed
     * @throws PropagationFailureException
     */
    public void removeStartTimes(int start, int end) throws PropagationFailureException {
    	actDom.removeStartTimes(start, end);
    }

    /**
     * Sets the maximum and minimum start time
     * @param start new earliest start time
     * @param end new latest start time
     * @throws PropagationFailureException
     */
    public void setStartTimeRange(int start, int end) throws PropagationFailureException {
    	actDom.setStartTimes(start, end);
    }
    
    /**
     * An activity is bound if all operations have been assigned to a resource
     * and have a definite start time
     * @return true if activity node is bound
     */
    public boolean isBound() {
    	return actDom.isBound();
    }
    
    /**
     * This sets a timeline on a given resource and operation combination.
     * @param operationID id of operation to alter
     * @param resourceID id of resource to alter
     * @param timeline a set of times for which the given resource is available given the constraints of the operation
     * @throws PropagationFailureException
     */
    public void setTimeline(int operationID, int resourceID, IntIntervalSet timeline) throws PropagationFailureException {
    	actDom.setTimeline(operationID, resourceID, timeline);
    }
    
    // javadoc is inherited
    public boolean isInDomain(Object val){
    	throw new UnsupportedOperationException("Operation not currently supported");
    }
    
    // javadoc is inherited
    public int getSize() {
    	throw new UnsupportedOperationException("Operation not currently supported");
    }
    
    /**
	 * Returns the number of resources that can still be
     * assigned to the operation with the specified id.
     * @param operationId the operation for which the resource count is desired
     * @return int representing the number of available resources
	 */
	public int getAvailResourceCount(int operationId) {
		return actDom.getAvailResourceCount(operationId);
	}
	
	/**
	 * If the operation with the specified id is assigned to a particular resource
     * the id of the required resource is returned; otherwise, -1 is returned
     * @param operationID id of the operation of which we are asking for required resource
     * @return id of required resource, -1 if none have been determined to be required yet
	 */
	public int getRequiredResource(int operationID) {
		return actDom.getRequiredResource(operationID);
	}
	
	/**
     * Removes the resource with the specified resource id from the list
     * of possible resources for the operation with the specified operation id
     * @param operationId the id of the operation for which adjustments should be made
     * @param resourceId the id of the resource on which adjustments should be made
     * @throws PropagationFailureException if the reduction causes any errors
     */
    public void removePossibleResource(int operationId, int resourceId) throws PropagationFailureException {
    	actDom.removePossibleResource(operationId,resourceId);
    }
    
    /**
     * Checks whether or not the specified operation is assigned to a resource
     * @param operationID the operation id of the operation in question
     * @return true if the operation is assigned to a resource; otherwise, returns false
     */
	public boolean isOperationAssignedToResource(int operationID) {
		return actDom.isOperationAssignedToResource(operationID);
	}
    
	// javadoc is inherited
    public Object getState() {
    	return actDom.getDomainState();
    }
	
    // javadoc is inherited
    public void restoreState(Object state) {
    	actDom.restoreDomainState(state);
    }
    
    // javadoc is inherited
    public void clearDelta() {
    	actDom.clearDelta();
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) actDom).setChoicePointStack(cps);
    }

    // javadoc is inherited
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) actDom).choicePointStackSet();
    }

    /**
     * Returns an int representing one of the four ways that the operation can use a resource
     * @param operationID id of operation
     * @return int representing PRODUCES, CONSUMES, PROVIDES, REQUIRES
     */
    public int getUsageType(int operationID) {
    	return actDom.getUsageType(operationID);
    }
    
    /**
     * Sets the way that the given operation will use resources
     * @param operationID id of operation
     * @param usage new type of the usage
     */
    public void setUsageType(int operationID, int usage) {
    	actDom.setUsageType(operationID, usage);
    }
    
    /**
     * Returns the minimum capacity of given operation
     * @param operationID id of operation
     * @return min capacity of given operation
     */
    public int getCapacityMin(int operationID) {
    	return actDom.getCapacityMin(operationID);
    }
    
    /**
     * Returns the maximum capacity of given operation
     * @param operationID id of operation
     * @return max capacity of given operation
     */
    public int getCapacityMax(int operationID) {
    	return actDom.getCapacityMax(operationID);
    }
    
    /**
     * Sets the minimum capacity of given operation
     * @param operationID id of operation
     * @param min new minimum
     */
    public void setCapacityMin(int operationID, int min) throws PropagationFailureException{
    	actDom.setCapacityMin(operationID, min);
    }
    
    /**
     * Sets the maximum capacity of given operation
     * @param operationID id of operation
     * @param max new maximum
     */
    public void setCapacityMax(int operationID, int max) throws PropagationFailureException{
    	actDom.setCapacityMax(operationID, max);
    }
    
    /**
     * Sets the capacity of given operation
     * @param operationID id of operation
     * @param val new capacity
     */
    public void setCapacity(int operationID, int val) throws PropagationFailureException{
    	actDom.setCapacity(operationID, val);
    }
    
    /**
     * Sets the capacity of given operation to a range of valid values
     * @param operationID id of operation 
     * @param start start of interval to restrict capacity to
     * @param end end of interval to restrict capacity to
     */
    public void setCapacityRange(int operationID, int start, int end) throws PropagationFailureException {
    	actDom.setCapacityRange(operationID, start, end);
    }
	
	/**
	 * Forces the operation with the specified operationId to use
     * the resource with the specified resourceId.
     * @param operationId the id of the operation for which we are setting the required resource
     * @param resourceId the id of the resource to be used by the operation
     * @throws PropagationFailureException if the assignment causes any errors
	 */
	public void setRequiredResource(int operationId, int resourceId) throws PropagationFailureException {
		actDom.setRequiredResource(operationId,resourceId);
	}
	
	/**
	 * This will add an already well-defined operation to the problem.  It will also
     * add all the possible resources that the operation has been configured to use.
     * @param operation ActOperationDomain representing the operation to add
     * @throws PropagationFailureException if the addition causes an inconsitent state 
	 */
	public void addOperation(ActOperationDomain operation) throws PropagationFailureException {
		actDom.addOperation(operation);
	}
	
	/**
	 * Returns the ids of the resources to which the operation with the specified operationId 
     * could still be assigned
     * @param operationId id of the operation of which we are asking for remaining resources
     * @return ids of remaining resources
	 */
	public int[] getRemainingResources(int operationId) {
		return actDom.getRemainingResources(operationId);
	}
	
	public String toString() {
		return actDom.toString();
	}
	
	// javadoc is inherited
	public void operationRuntimeChange() {
		fireChangeEvent(DomainChangeType.DOMAIN);
	}
	
    // javadoc is inherited
	public void operationRequiredResourceChange() {
		fireChangeEvent(DomainChangeType.DOMAIN);
	}
    
    // javadoc is inherited
	public void operationCapacityChange(){
		fireChangeEvent(DomainChangeType.DOMAIN);
	}

}