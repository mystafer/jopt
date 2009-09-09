package jopt.js.spi.domain.activity;

import java.util.ArrayList;
import java.util.Iterator;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointIntArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.SortableIntList;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.exception.JobSchedulerRuntimeException;
import jopt.js.api.variable.ResourceSet;
import jopt.js.api.variable.Resource;
import jopt.js.spi.domain.listener.ActivityDomainListener;
import jopt.js.spi.domain.listener.OperationDomainListener;
import jopt.js.spi.util.CPSIntIntervalSet;

/**
 * A domain that will consistently maintain valid start times, end times, and durations
 * for an activity.  
 * 
 * @author James Boerkoel
 */
public class ActivityDomain  implements ChoicePointDataSource,ChoicePointEntryListener, ActResourceDomainListener {
	//These data structures will represent the start, end and duration times of the activity as a whole
	private CPSIntIntervalSet startTimes;
	private CPSIntIntervalSet endTimes;
	private CPSIntIntervalSet duration;
	
	//This keeps track of operations that are present, and also keeps track of any operations that have been removed for backtracking 
	private ArrayList operations;
	private ArrayList removedOperations;
	
	//These data structures are responsible for maintaining indexes into resources and operations
	private SortableIntList resourceIDs;
	private SortableIntList referenceIndexList;
	private SortableIntList operationIndexList;
	private SortableIntList resourceIndexList;
	private SortableIntList nextIndexList;
	
	//These maintain an reference into the index of the operation collection by id
	private SortableIntList operationID;
	private SortableIntList operationIdx;
	
	//Listeners
	private ArrayList operationListeners;
	private ArrayList activityListeners;
	
	//used to detect chages
	private int size = Integer.MAX_VALUE;
	
	private boolean isBuilt;
	
	private int id;

	protected ChoicePointNumArraySet cpdata;
	ChoicePointStack cps;
	ChoicePointIntArray operationsAdded;
	
	//initializes everything
	private ActivityDomain() {
		super();
		
		startTimes = new CPSIntIntervalSet();
		endTimes = new CPSIntIntervalSet();
		duration = new CPSIntIntervalSet();
		startTimes.add(Integer.MIN_VALUE,Integer.MAX_VALUE);
		endTimes.add(Integer.MIN_VALUE,Integer.MAX_VALUE);
		duration.add(Integer.MIN_VALUE,Integer.MAX_VALUE);
		
		operations = new ArrayList();
		removedOperations = new ArrayList();
		
		//Structures to maintain the relationship between resource ID's and an index into the lists representing 
		//the indexs of such structures in the activity and operations
		resourceIDs = new SortableIntList();
		referenceIndexList = new SortableIntList();
		//Lists that represent the offset that operation is within activity, and resource is within its operation respectively
		operationIndexList = new SortableIntList();
		resourceIndexList = new SortableIntList();
		//Represents the next time when this resource is used (ie by a different operation)
		nextIndexList = new SortableIntList();
		
		//Two lists to maintain the relationship of op id and index
		operationID = new SortableIntList();
		operationIdx = new SortableIntList();
		
		operationListeners = new ArrayList();
	}
	
	/**
	 * Constructs an activity domain
	 * @param est earliest start time of this activity
	 * @param lst latest start time of this activity
	 * @param durMin minimum duration that this activity will occur
	 * @param durMax maximum duration that this activity will occur
	 */
	public ActivityDomain(int est, int lst, int durMin, int durMax) {
		this();
		duration.setMin(durMin);
		duration.setMax(durMax);
		startTimes.setMin(est);
		startTimes.setMax(lst);
		endTimes.setMin(est+(durMin-1));
		endTimes.setMax(lst+(durMax-1));
	}
	
	/**
	 * This will add an already well-defined operation to the problem.  It will also
	 * add all the possible resources that the operation has been configured to use.
	 * @param operation ActOperationDomain representing the operation to add
	 * @throws PropagationFailureException if the addition causes an inconsitent state
	 */
	public void addOperation(ActOperationDomain operation)
	throws PropagationFailureException {
		//Otherwise we must simply remove any gaps, so that this contains only intersection,
		//Notice we also have to remove preexisting gaps in the domain from the operation since
		//those times have already been declared as invalid
	    
        //	make durations consistent
		this.duration.intersect(operation.getDuration());
		
		this.startTimes.intersect(operation.getStartTimes());
		
		this.endTimes.intersect(operation.getEndTimes());
		//here we reverse the process and remove in the other direction
		
		int index = this.duration.getFirstIntervalIndex();
		//delete all gaps
		while ((index >= 0) && (this.duration.getNextIntervalIndex(index) >= 0)) {
			operation.removeDuration(this.duration.getMax(index) + 1, this.duration.getMin(this.duration.getNextIntervalIndex(index)) - 1);
			index = this.duration.getNextIntervalIndex(index);
		}
		
		//delete the front and end
		//			operation.duration.removeEndingBefore(this.duration.getMin());
		//			operation.duration.removeStartingAfter(this.duration.getMax());
		operation.setDurationMin(this.duration.getMin());
		operation.setDurationMax(this.duration.getMax());
		
		//add start indices
		index = this.startTimes.getFirstIntervalIndex();
		while ((index >= 0)	&& (this.startTimes.getNextIntervalIndex(index) >= 0)) {
			operation.removeStartTimes(this.startTimes.getMax(index) + 1, this.startTimes.getMin(this.startTimes.getNextIntervalIndex(index)) - 1);
			index = this.startTimes.getNextIntervalIndex(index);
		}
		//delete the front and end
		//			operation.startTimes.removeEndingBefore(this.startTimes.getMin());
		//			operation.startTimes.removeStartingAfter(this.startTimes.getMax());
		operation.setEarliestStartTime(this.startTimes.getMin());
		operation.setLatestStartTime(this.startTimes.getMax());
		
		//add end indices
		index = this.endTimes.getFirstIntervalIndex();
		while ((index >= 0)	&& (this.endTimes.getNextIntervalIndex(index) >= 0)) {
			operation.removeEndTimes(this.endTimes.getMax(index) + 1, this.endTimes.getMin(this.endTimes.getNextIntervalIndex(index)) - 1);
			index = this.endTimes.getNextIntervalIndex(index);
		}
		//delete the front and end
		//			operation.endTimes.removeEndingBefore(this.endTimes.getMin());
		//			operation.endTimes.removeStartingAfter(this.endTimes.getMax());
		operation.setEarliestEndTime(this.endTimes.getMin());
		operation.setLatestEndTime(this.endTimes.getMax());
		
		operations.add(operation);
		if (isBuilt &&(operationsAdded != null)) {
			operationsAdded.add(operation.getID());
		}
		
//		if (operationListener != null) {
//			operation.setListener(operationListener);
//		}
		
		operation.setActResListener(this);
		
		for (int i=0; i< operationListeners.size(); i++) {
			operation.addListener((OperationDomainListener)operationListeners.get(i));
		}
		
		//Throw the operation id and corresponding index info into storage
		int opIdx = operationID.binarySearch(operation.getID());
		if (opIdx < 0) {
			opIdx = -opIdx - 1;
			operationID.add(opIdx, operation.getID());
			operationIdx.add(opIdx, operations.size() - 1);
		}
		
		//Now we throw the proper ids into the storage arrays
		int resourceIDs[] = operation.getResourceIDs();
		for (int i = 0; i < resourceIDs.length; i++) {
			int resID = resourceIDs[i];
			index = this.resourceIDs.binarySearch(resID);
			//Operation index will be the last in the array list since it was just added
			int operationIndex = operations.size() - 1;
			int resourceIndex = operation.getResourceIndex(resID);
			if (index < 0) {
				index = -index - 1;
				this.resourceIDs.add(index, resID);
				int dataIndex = this.resourceIndexList.size();
				this.referenceIndexList.add(index, dataIndex);
				operationIndexList.add(dataIndex, operationIndex);
				resourceIndexList.add(dataIndex, resourceIndex);
				nextIndexList.add(dataIndex, -1);
			} else {
				//Get us to the last index with the same resource id
				while (this.nextIndexList.get(index) >= 0) {
					index = this.nextIndexList.get(index);
				}
				int newIndex = this.resourceIndexList.size();
				this.nextIndexList.set(index, newIndex);
				operationIndexList.add(newIndex, operationIndex);
				resourceIndexList.add(newIndex, resourceIndex);
				nextIndexList.add(newIndex, -1);
			}
		}
		
		if ((!operation.choicePointStackSet()) && (cps != null)) {
			operation.setChoicePointStack(cps);
		}
		//If this causes the domain to be empty, we have stumbled across an invalid combination, and this must be removed
		if (this.isEmpty()) {
			throw new PropagationFailureException("Adding operation "+operation.getID()+" causes there to be no valid start times for this activity");
		}
	}
	
	/**
	 * Adds a resource to the operation specified by operationID.
	 * @param operationID id of operation to which the resource is being added
	 * @param resource ActResourceDomain representing the resource being added
	 */
	public void addResource(int operationID, ActResourceDomain resource) throws PropagationFailureException {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		((ActOperationDomain)operations.get(opIdx)).addResource(resource);
		//Now we make sure that the id gets added to the id lists
		int resID = resource.getID();
		int index = this.resourceIDs.binarySearch(resID);
		//Operation index will be the last in the array list since it was just added
		int operationIndex = operations.size() - 1;
		int resourceIndex = ((ActOperationDomain)operations.get(opIdx)).getResourceIndex(resID);
		if (index < 0) {
			index = -index - 1;
			this.resourceIDs.add(index, resID);
			int dataIndex = this.resourceIndexList.size();
			this.referenceIndexList.add(index, dataIndex);
			operationIndexList.add(dataIndex, operationIndex);
			resourceIndexList.add(dataIndex, resourceIndex);
			nextIndexList.add(dataIndex, -1);
		} else {
			//Get us to the last index with the same resource id
			while (this.nextIndexList.get(index) >= 0) {
				index = this.nextIndexList.get(index);
			}
			int newIndex = this.resourceIndexList.size();
			this.nextIndexList.set(index, newIndex);
			operationIndexList.add(newIndex, operationIndex);
			resourceIndexList.add(newIndex, resourceIndex);
			nextIndexList.add(newIndex, -1);
		}
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
		int opIdx = getOperationIdxForID(operationID);
		if (opIdx >=0) {
			addResource(operationID,new ActResourceDomain(resourceID,start,end,minDur,maxDur) );
		}
		else {
			ActResourceDomain ra = new ActResourceDomain(start,end,minDur,maxDur);
			ra.setID(resourceID);
			ActOperationDomain newOA = new ActOperationDomain(1,1,1);//TODO NOTICE THIS IS TEMP HACK
			newOA.setID(operationID);
			newOA.addResource(ra);
			addOperation(newOA);
		}
	}
	
	/**
	 * Access the isBuilt flag
	 * @return true if this activity has been marked as built
	 */
	public boolean isBuilt() {
		return isBuilt;
	}
    
	/**
	 * Set the isBuilt flag
	 * @param built new value for isBuilt
	 */
	public void setBuilt(boolean built) {
		this.isBuilt = built;
	}
	
	/**
	 * Returns all resource sets for any operation that still has multiple possibilities for which
	 * resource will service the operation 
	 * @return set of all alternative resource sets
	 */
	public ResourceSet[] getAllAlternativeResourceSets() {
		ArrayList unassigned = new ArrayList();
		for (int i=0; i<operations.size(); i++) {
			if (!((ActOperationDomain)operations.get(i)).isAssignedToResource()) {
				unassigned.add(operations.get(i));
			}
		}
		return (ResourceSet[])unassigned.toArray(new ResourceSet[0]);
	}
	
	/**
	 * Removes the operation from the activity
	 * @param operationID id of the operation to be removed
	 */
	private void removeOperation(int operationID){
		int opIdx = getOperationIdxForID(operationID);
		if (opIdx<0) return;
		//We must remove the operation
		int[] resourceIDs = ((ActOperationDomain)operations.get(opIdx)).getResourceIDs();
		
		for (int i=0; i<resourceIDs.length; i++) {
			int index = getFirstResourceIndexForID(resourceIDs[i]);
			int prevIndex=-1;
			int nextIndex = nextIndexList.get(index);
			while (index>=0){
				if (operationIndexList.get(index)==opIdx){
					if ((prevIndex==-1)&&(nextIndex==-1)) {
						int resIdx = this.resourceIDs.binarySearch(resourceIDs[i]);
						this.referenceIndexList.removeElementAt(resIdx);
						this.resourceIDs.removeElementAt(resIdx);
					}
					else {
						if (prevIndex>=0) {
							nextIndexList.set(prevIndex,nextIndex);
						}
					}
					operationIndexList.removeElementAt(index);
					resourceIndexList.removeElementAt(index);
					nextIndexList.removeElementAt(index);
					for (int j = 0; j< nextIndexList.size(); j++) {
						if (operationIndexList.get(j)>index) {
							operationIndexList.set(j,(operationIndexList.get(j)-1));
						}
						if (resourceIndexList.get(j)>index) {
							resourceIndexList.set(j,(resourceIndexList.get(j)-1));
						}
						if (nextIndexList.get(j)>index) {
							nextIndexList.set(j,(nextIndexList.get(j)-1));
						}
					}
					for (int j=0; j<referenceIndexList.size(); j++) {
						if (referenceIndexList.get(j)>index) {
							referenceIndexList.set(j,referenceIndexList.get(j)-1);
						}
					}
				}
				prevIndex=index;
				index = nextIndex;
				if (nextIndex>=0) {
					nextIndex = nextIndexList.get(nextIndex);
				}
			}
		}
		
		
		//Here we remove operation from the group
		//and add it to removed operations
		removedOperations.add(operations.remove(opIdx));
		operationIdx.removeElement(opIdx);
		this.operationID.removeElementAt(opIdx);
		//we must adjust any indexes that might have changed as a result of removing this operation
		for (int i=0; i<operationIdx.size(); i++) {
			if (operationIdx.get(i)>opIdx) {
				operationIdx.set(i,operationIdx.get(i)-1);
			}
		}
	}
	
	/**
	 * Sets the activity operation domain listener
	 * @param listener listner of this domain
	 */
	public void addOperationListener(OperationDomainListener listener) {
		operationListeners.add(listener);
		for (int i=0; i<operations.size(); i++) {
			((ActOperationDomain)operations.get(i)).addListener(listener);
		}
	}
	
	/**
	 * Sets the activity domain listener
	 * @param listener listner of this domain
	 */
	public void addActivityListener(ActivityDomainListener listener) {
		if (this.activityListeners==null) {
			this.activityListeners = new ArrayList();
		}
		activityListeners.add(listener);
		
	}
	
	/**
	 * This reports wehn a change in domain has occured since the last time this method was called
	 * @return true if the size of the data in the domain has changed
	 */
	private boolean isChanged() {
		int newSize = startTimes.size()+endTimes.size()+duration.size();
		if (newSize!=size) {
			size = newSize;
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * This is called by methods that could have potentially changed the domain;
	 * it checks to see if a change has occurred, and if so, reports it to its listeners
	 * @throws PropagationFailureException
	 */
	private void possibleDomainChange() throws PropagationFailureException {
		if (isChanged()) {
			if (activityListeners != null) {
				for (int i=0; i<activityListeners.size(); i++) {
					((ActivityDomainListener)activityListeners.get(i)).fireAnyAction();
				}
			}
			for (int i=0; i< operationListeners.size(); i++) {
				((OperationDomainListener)operationListeners.get(i)).operationRuntimeChange();
			}
		}
	}
	
	/**
	 * Gets the operation ids from which this activity is built
	 * @return operation ids that make up this activity
	 */
	public int[] getOperationIDs() {
		return (int[])operationID.toArray(new int[0]);
	}
	
	
	/**
	 * This sets a timeline on a given resource and operation combination.  Note that the timeline given
	 * is the time that the resource is available given the minimal capacity constraints of the resource,
	 * so it must calculate valid start times, end times, and duration based off of this timeline.
	 * @param operationID id of operation to alter
	 * @param resourceID id of resource to alter
	 * @param timeline a set of times for which the given resource is available given the constraints of the operation
	 * @throws PropagationFailureException
	 */
	public void setTimeline(int operationID, int resourceID, IntIntervalSet timeline) throws PropagationFailureException {
		int opIdx = getOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx,resourceID);
		getVerifiedOperationForID(operationID).setTimeline(resIdx,timeline);
		possibleDomainChange();
	}
	
	//This is a helper method to find the index of the first occurence of the given resourceID into the resourceIndex lists
	private int getFirstResourceIndexForID(int resID) {
		int index = this.resourceIDs.binarySearch(resID);
		if (index < 0)
			return index;
		else
			return this.referenceIndexList.get(index);
	}
	
	/**
	 * Returns the next highest valid start time
	 * @param startTime current start time
	 * @return the start that is the next higher in the list of possible valid start times
	 */
	public int getNextStartTime(int startTime) {
		return this.startTimes.getNextHigher(startTime);
	}
	
	/**
	 * Returns the next lower valid start time
	 * @param startTime current start time
	 * @return the start time that is the next lower in the list of possible valid end times
	 */
	public int getPrevStartTime(int startTime) {
		return this.startTimes.getNextLower(startTime);
	}
	
	/**
	 * Returns the next higher valid end time
	 * @param endTime current end time
	 * @return the end time that is the next higher in the list of possible valid end times
	 */
	public int getNextEndTime(int endTime) {
		return this.endTimes.getNextHigher(endTime);
	}
	
	/**
	 * Returns the next lower valid end time
	 * @param endTime current end time
	 * @return the end time that is the next lower in the list of possible valid end times
	 */
	public int getPrevEndTime(int endTime) {
		return this.endTimes.getNextLower(endTime);
	}
	
	/**
	 * Returns the next higher valid duration
	 * @param currDur current duration
	 * @return the duration that is the next higher in the list of possible valid durations
	 */
	public int getNextDuration(int currDur) {
		return this.duration.getNextHigher(currDur);
	}
	
	/**
	 * Returns the next lower valid duration
	 * @param currDur current duration
	 * @return the duration that is the next lower in the list of possible valid durations
	 */
	public int getPrevDuration(int currDur) {
		return this.duration.getNextLower(currDur);
	}
	
	/**
	 * Returns the number of valid start times remaining
	 * @return the number of valid start times remaining
	 */
	public int getNumStartTimes() {
		return startTimes.size();
	}
	
	/**
	 * Returns the number of valid end times remaining
	 * @return the number of valid end times remaining
	 */
	public int getNumEndTimes() {
		return endTimes.size();
	}
	
	/**
	 * Returns the number of possible values for the duration remaining
	 * @return the number of possible values for the duration remaining
	 */
	public int getDurationSize() {
		return duration.size();
	}
	
	/**
	 * Returns true if the start time is still in the list of valid possible start times
	 * @param startTime time given to see if it is still a valid start time
	 * @return true if the given time is still a time at which this activity could potentially start
	 */
	public boolean isPossibleStartTime(int startTime) {
		return startTimes.contains(startTime);
	}
	
	/**
	 * Returns true if the end time is still in the list of valid possible end times
	 * @param endTime time given to see if it is still a valid end time
	 * @return true if the given time is still a time at which this activity could potentially end 
	 */
	public boolean isPossibleEndTime(int endTime) {
		return endTimes.contains(endTime);
	}
	
	/**
	 * Returns true if the given duration is still in the list of valid possible durations
	 * @param dur val given to see if it is still a valid duration
	 * @return true if the given val is still a value of a possible duration
	 */
	public boolean isPossibleDuration(int dur) {
		return duration.contains(dur);
	}
	
	//gives the index of the operation within the activity
	private int getOperationIdxForID(int opID) {
		int index = this.operationID.binarySearch(opID);
		if (index < 0)
			return index;
		else
			return this.operationIdx.get(index);
	}
	
	//This is a helper method to find the index of the occurence of the given resourceID in the resourceIndex lists
	//that corresponds with the given operation index
	private int getVerifiedResourceIndexForIDPerOperationIndex(int opIdx, int resID) {
		int index = this.resourceIDs.binarySearch(resID);
		if (index < 0)
			throw new JobSchedulerRuntimeException("Resource "+resID+" is not defined on this activity");
		int rsIdx = this.referenceIndexList.get(index);
		while ((rsIdx >= 0) && (operationIndexList.get(rsIdx) != opIdx)) {
			rsIdx = nextIndexList.get(rsIdx);
		}
		if (rsIdx < 0)
			throw new JobSchedulerRuntimeException("Resource "+resID+" is not defined on this activity");
		else {
			if (resourceIndexList.get(rsIdx)>=0) {
				return resourceIndexList.get(rsIdx);
			}
			else {
				throw new JobSchedulerRuntimeException("Resource "+resID+" is not defined on this activity");
			}
		}
	}
	
	//gives the index of the operation within the activity
	private int getVerifiedOperationIdxForID(int opID) {
		int index = this.operationID.binarySearch(opID);
		if (index < 0)
			throw new JobSchedulerRuntimeException("Operation "+opID+" is not defined on this activity");
		else
			return this.operationIdx.get(index);
	}
	
	//gives the index of the operation within the activity
	private ActOperationDomain getVerifiedOperationForID(int opID) {
		int index = this.operationID.binarySearch(opID);
		if (index < 0)
			throw new JobSchedulerRuntimeException("Operation "+opID+" is not defined on this activity");
		else
			return (ActOperationDomain)operations.get(this.operationIdx.get(index));
	}
	
	//javadoc inherited
	public void afterChoicePointPushEvent() {
		if (operationsAdded != null) {
			for (int i=0; i<operationsAdded.size(); i++) {
				for (int j=0; j<removedOperations.size();j++) {
					if (((ActOperationDomain)removedOperations.get(j)).getID()==operationsAdded.get(i)) {
						try {
							this.addOperation(((ActOperationDomain)removedOperations.remove(j)));
						}
						catch (PropagationFailureException pfe) {
							//This should never occur!, so we dont check for it
						}
					}
				}
			}
		}
		size = startTimes.size()+endTimes.size()+duration.size();
	}
    
	//javadoc inherited
	public void beforeChoicePointPopEvent() {
		if (operationsAdded != null) {
			for (int i=0; i<operationsAdded.size(); i++) {
				removeOperation(operationsAdded.get(i));
			}
		}
		size = startTimes.size()+endTimes.size()+duration.size();
	}
	
	public void afterChoicePointPopEvent() {
	}
	
	public void beforeChoicePointPushEvent() {
	}
	
	/**
	 * Returns a set that represents the times that the 
	 * specified operation could use the specified resource
	 * @param operationID id operation being inquired about
	 * @param resourceID id of resource being inquired about
	 * @return IntIntervalSet representing the times that this specified operation could use the specified resource
	 */
	public IntIntervalSet getPotentialUsageTimeline(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx)).getPotentialUsageTimeline(resIdx);
	}
	
	/**
	 * Returns a set that represents the times that the 
	 * specified operation already actually use the specified resource
	 * @param operationID id operation being inquired about
	 * @param resourceID id of resource being inquired about
	 * @return IntIntervalSet representing the times that this specified operation already actually uses the specified resource
	 */
	public IntIntervalSet getActualUsageTimeline(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx)).getActualUsageTimeline(resIdx);
	}
	
	/**
	 * Returns the maximum duration of the specified operation on the specified resource
	 * @param operationID id of the operation that houses the resource for which we are asking for duration information
	 * @param resourceID id of the resource of which of which the maximum duration is sought
	 * @return duration max
	 */
	public int getDurationMax(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx)).getDurationMax(resIdx);
	}
	
	/**
	 * Returns the minimum duration of the specified operation on the specified resource
	 * @param operationID id of the operation that houses the resource for which we are asking for duration information
	 * @param resourceID id of the resource of which of which the minimum duration is sought
	 * @return duration min
	 */
	public int getDurationMin(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx)).getDurationMin(resIdx);
	}
	
	/**
	 * Returns the earliest start time for the given operation and resource id combination
	 * @param operationID id of the operation that houses given id
	 * @param resourceID id of the resource for which we are inquiring
	 * @return earliest start time
	 */
	public int getEarliestStartTime(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		if (opIdx < 0)
			return opIdx;
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		if (resIdx < 0)
			return resIdx;
		return ((ActOperationDomain) operations.get(opIdx)).getEarliestStartTime(resIdx);
	}
	
	/**
	 * Returns the latest start time for the given operation and resource id combination
	 * @param operationID id of the operation that houses given id
	 * @param resourceID id of the resource for which we are inquiring
	 * @return latest start time
	 */
	public int getLatestStartTime(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx))
		.getLatestStartTime(resIdx);
	}
	
	/**
	 * Returns the earliest end time for the given operation and resource id combination
	 * @param operationID id of the operation that houses given id
	 * @param resourceID id of the resource for which we are inquiring
	 *  @return earliest end time
	 */
	public int getEarliestEndTime(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx))
		.getEarliestEndTime(resIdx);
	}
	
	/**
	 * Returns the latest end time for the given operation and resource id combination
	 * @param operationID id of the operation that houses given id
	 * @param resourceID id of the resource for which we are inquiring
	 * @return latest end time
	 */
	public int getLatestEndTime(int operationID, int resourceID) {
		int opIdx = getVerifiedOperationIdxForID(operationID);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceID);
		return ((ActOperationDomain) operations.get(opIdx))
		.getLatestEndTime(resIdx);
	}
	
	/**
	 * Set the earliest start time for the activity
	 * @param est the earliest start time
     * @throws PropagationFailureException
	 */
	public void setEarliestStartTime(int est) throws PropagationFailureException {
		if (startTimes.getMin() >= est) {
            // no change
			return;
		}
		startTimes.setMin(est);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setEarliestStartTime(est);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the latest start time for the activity
	 * @param lst the latest start time
	 */
	public void setLatestStartTime(int lst) throws PropagationFailureException {
		if (startTimes.getMax() <= lst) {
            // no change
			return;
		}
		startTimes.setMax(lst);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setLatestStartTime(lst);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	private boolean isEmpty() {
		return (startTimes.isEmpty()||endTimes.isEmpty()||duration.isEmpty());
	}
	
	/**
	 * Set the earliest end time for the activity 
	 * @param eet the earliest end time
	 */
	public void setEarliestEndTime(int eet) throws PropagationFailureException {
		if (endTimes.getMin() >= eet) {
            // no change
			return;
		}
		endTimes.setMin(eet);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setEarliestEndTime(eet);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the latest end time for the activity
	 * @param let the latest end time
	 */
	public void setLatestEndTime(int let) throws PropagationFailureException {
		if (endTimes.getMax() <= let) {
            // no change
			return;
		}
		
		endTimes.setMax(let);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setLatestEndTime(let);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Sets the maximum that the duration is allowed to be
	 * @param durMax new duration maximum
	 */
	public void setDurationMax(int durMax) throws PropagationFailureException {
		if (duration.getMax() <= durMax) {
            // no change
			return;
		}
		duration.setMax(durMax);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setDurationMax(durMax);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Sets the minimum that the duration is allowed to be
	 * @param durMin new duration minimum
	 */
	public void setDurationMin(int durMin) throws PropagationFailureException {
		if (duration.getMin() >= durMin) {
            // no change
			return;
		}
		duration.setMin(durMin);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).setDurationMin(durMin);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Sets the start time of this activity to the specified time
	 * @param time time to set start time of activity to
	 * @throws PropagationFailureException
	 */
	public void setStartTime(int time) throws PropagationFailureException{
		setEarliestStartTime(time);
		setLatestStartTime(time);
	}
	
	/**
	 * Sets the end time of this activity to the specified time
	 * @param time time to set end time of activity to
	 * @throws PropagationFailureException
	 */
	public void setEndTime(int time) throws PropagationFailureException{
		setEarliestEndTime(time);
		setLatestEndTime(time);
	}
	
	/**
	 * Sets the maximum and minimum duration
	 * @param durMax new duration maximum
	 * @param durMin new duration minimum
     * @throws PropagationFailureException
	 */
	public void setDurationRange(int durMin, int durMax) throws PropagationFailureException {
		setDurationMin(durMin);
		setDurationMax(durMax);
	}
	
	/**
	 * Sets the duration to the specified amount
	 * @param duration new duration 
     * @throws PropagationFailureException
	 */
	public void setDuration(int duration) throws PropagationFailureException {
		setDurationRange(duration,duration);
	}
	
	/**
	 * Sets the maximum and minimum start time
	 * @param est new earliest start time
	 * @param lst new latest start time
     * @throws PropagationFailureException
	 */
	public void setStartTimes(int est, int lst)	throws PropagationFailureException {
		setEarliestStartTime(est);
		setLatestStartTime(lst);
	}
	
	/**
	 * Sets the maximum and minimum end time
	 * @param eet new end time minimum
	 * @param let new end timemaximum
     * @throws PropagationFailureException
	 */
	public void setEndTimes(int eet, int let) throws PropagationFailureException {
		setEarliestEndTime(eet);
		setLatestEndTime(let);
	}
	
	/**
	 * Removes a range of values from the valid start times
	 * @param start start value of the range of start times that are to be removed
	 * @param end end value of the range of start times that are to be removed
	 * @throws PropagationFailureException
	 */
	public void removeStartTimes(int start, int end) throws PropagationFailureException {
		if (this.startTimes.isIntervalEmpty(start, end)) {
            // no change
			return;
		}
		startTimes.remove(start, end);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).removeStartTimes(start, end);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Called when start times are removed from an operation
	 * @param operationID id of operation that changed
	 * @param delta the start times that have been removed from specified resource
	 */
	public void startTimesRemoved(int operationID, IntIntervalSet delta) throws PropagationFailureException {
		startTimes.removeAll(delta);
		for (int i = 0; i < operations.size(); i++) {
			if (((ActOperationDomain) operations.get(i)).getID() != operationID) {
				((ActOperationDomain) operations.get(i)).removeStartTimes(delta);
			}
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}

	/**
	 * Called when end times are removed from an operation
	 * @param operationID id of operation that changed
	 * @param delta the end times that have been removed from specified resource
	 */
	public void endTimesRemoved(int operationID, IntIntervalSet delta) throws PropagationFailureException {
		endTimes.removeAll(delta);
		for (int i = 0; i < operations.size(); i++) {
			if (((ActOperationDomain) operations.get(i)).getID() != operationID) {
				((ActOperationDomain) operations.get(i)).removeEndTimes(delta);
			}
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Called when the duration of an operation changes
	 * @param operationID id of operation that changed
	 * @param delta the duration times that have been removed from specified resource
	 */
  	public void durationRemoved(int operationID, IntIntervalSet delta) throws PropagationFailureException {
  		duration.removeAll(delta);
		for (int i = 0; i < operations.size(); i++) {
			if (((ActOperationDomain) operations.get(i)).getID() != operationID) {
				((ActOperationDomain) operations.get(i)).removeDuration(delta);
			}
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
  	}
  	
	/**
	 * Called when times are removed from an operation
	 * @param operationID id of operation that changed
	 * @param delta the start, end, and duration times that have been removed from specified resource
	 */
  	public void deltaRemoved(int operationID, ActDelta delta) throws PropagationFailureException {
  		startTimes.removeAll(delta.startTimes);
  		endTimes.removeAll(delta.endTimes);
  		duration.removeAll(delta.duration);
		for (int i = 0; i < operations.size(); i++) {
			if (((ActOperationDomain) operations.get(i)).getID() != operationID) {
				((ActOperationDomain) operations.get(i)).removeDelta(delta);
			}
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
  	}
	
	/**
	 * Removes specified time from being a possible start time for this activity
	 * @param time time that is being removed
	 * @throws PropagationFailureException
	 */
	public void removeStartTime(int time) throws PropagationFailureException {
		removeStartTimes(time, time);
	}
	
	/**
	 * Removes specified time from being a possible end time for this activity
	 * @param time time that is being removed
	 * @throws PropagationFailureException
	 */
	public void removeEndTime(int time) throws PropagationFailureException {
		removeEndTimes(time, time);
	}
	
	/**
	 * Removes a range of values from the valid end times of this domain
	 * @param start start value of the range of end times that are to be removed
	 * @param end end value of the range of end times that are to be removed
	 * @throws PropagationFailureException  If the domain becomes empty
	 */
	public void removeEndTimes(int start, int end)
	throws PropagationFailureException {
		if (this.endTimes.isIntervalEmpty(start, end)) {
            // no change
			return;
		}
		endTimes.remove(start, end);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).removeEndTimes(start, end);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Removes a range of values from the duration
	 * @param durMin start value of the range of durations that are to be removed
	 * @param durMax end value of the range of durations that are to be removed
	 * @throws PropagationFailureException  If the domain becomes empty
	 */
	public void removeDurationRange(int durMin, int durMax)
	throws PropagationFailureException {
		if (this.duration.isIntervalEmpty(durMin,durMax)) {
            // no change
			return;
		}
		duration.remove(durMin, durMax);
		for (int i = 0; i < operations.size(); i++) {
			((ActOperationDomain) operations.get(i)).removeDuration(durMin, durMax);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the earliest start time for the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param est the earliest start time
	 */
	public void setEarliestStartTime(int resourceID, int est)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setEarliestStartTime(est, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the latest start time for the specified resource
     * @param resourceID the id of the resource on which adjustments should be made
	 * @param lst the latest start time
	 */
	public void setLatestStartTime(int resourceID, int lst)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setLatestStartTime(lst, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the earliest end time for the specified resource
     * @param resourceID the id of the resource on which adjustments should be made 
	 * @param eet the earliest end time
	 */
	public void setEarliestEndTime(int resourceID, int eet)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setEarliestEndTime(eet, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
     * Set the latest end time for the specified resource
     * @param resourceID the id of the resource on which adjustments should be made
	 * @param let the latest end time
	 */
	public void setLatestEndTime(int resourceID, int let)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setLatestEndTime(let, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Set the maximum duration for the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param durMax new maximum duration
	 * @throws PropagationFailureException
	 */
	public void setDurationMax(int resourceID, int durMax)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setDurationMax(durMax, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
    
	/**
	 * Set the minimum duration for the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param durMin new minimum duration 
	 * @throws PropagationFailureException
	 */
	public void setDurationMin(int resourceID, int durMin)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setDurationMin(durMin, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Sets the range of valid durations for the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param durMin new minimum duration
	 * @param durMax new maximum duration
	 * @throws PropagationFailureException
	 */
	public void setDurationRange(int resourceID, int durMin, int durMax)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).setDurationRange(durMin, durMax,
							resourceIndexList.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Removes a range of values from the start times of the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param start start val for the range to be removed
	 * @param end end val for the range to be removed
	 * @throws PropagationFailureException  If the domain becomes empty
	 */
	public void removeStartTimes(int resourceID, int start, int end)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).removeStartTimes(start, end,
							resourceIndexList.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Removes a range of values from the end times of the specified resource
	 * @param resourceID the id of the resource on which adjustments should be made
	 * @param start start val of range to remove from end times 
	 * @param end end val of range to remove from end times
	 * @throws PropagationFailureException  If the domain becomes empty
	 */
	public void removeEndTimes(int resourceID, int start, int end)
	throws PropagationFailureException {
		int index = getFirstResourceIndexForID(resourceID);
		while (index >= 0) {
			((ActOperationDomain) operations.get(operationIndexList
					.get(index))).removeEndTimes(start, end, resourceIndexList
							.get(index));
			index = nextIndexList.get(index);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"Results in an empty activity domain.");
		}
		possibleDomainChange();
	}
	
	/**
	 * Returns the number of operations in this activity
	 */
	public int getOperationCount() {
		return operations.size();
	}
	
	/**
	 * Returns an array of ids of all operations assigned
	 * to a particular resource (but not necessarily bound to
	 * a particular time).
     * @return array of operationIds corresponding to assigned operations
	 */
	public int[] getAssignedOperations() {
		SortableIntList assignedOperations = new SortableIntList();
		for (int i = 0; i < operations.size(); i++) {
			if (((ActOperationDomain) operations.get(i))
					.isAssignedToResource()) {
				assignedOperations.add(((ActOperationDomain) operations
						.get(i)).getID());
			}
		}
		return assignedOperations.toArray();
	}
	
	
	/**
	 * Returns an array of ids of all operations not assigned
	 * to a particular resource.
     * @return array of operationIds corresponding to unassigned operations
	 */
	public int[] getUnassignedOperations() {
		SortableIntList assignedOperations = new SortableIntList();
		for (int i = 0; i < operations.size(); i++) {
			if (!((ActOperationDomain) operations.get(i))
					.isAssignedToResource()) {
				assignedOperations.add(((ActOperationDomain) operations
						.get(i)).getID());
			}
		}
		return assignedOperations.toArray();
	}
	
	/**
	 * Returns the number of resources that can still be
	 * assigned to the operation with the specified id
     * @param operationId the operation for which the resource count is desired
     * @return int representing the number of available resources
	 */
	public int getAvailResourceCount(int operationId) {
		try {
			return ((ActOperationDomain) operations
					.get(getVerifiedOperationIdxForID(operationId)))
					.getPossibleResourceCount();
		}
		catch(JobSchedulerRuntimeException ie) {
			return 0;
		}
	}
	
	/**
	 * Forces the operation with the specified operationId to use
	 * the resource with the specified resourceId.
	 * @param operationId the id of the operation for which we are setting the required resource
	 * @param resourceId the id of the resource to be used by the operation
	 * @throws PropagationFailureException if the assignment causes any errors
	 */
	public void setRequiredResource(int operationId, int resourceId) throws PropagationFailureException {
		int operationIdx = getVerifiedOperationIdxForID(operationId);
		int resIndex = getVerifiedResourceIndexForIDPerOperationIndex(operationIdx,
				resourceId);
		if (resourceId >= 0) {
			((ActOperationDomain) operations.get(operationIdx))
			.setRequiredResource(resIndex);
		}
		if (this.isEmpty()) {
			throw new PropagationFailureException(
					"This causes an empty domain on activity" + id);
		}
		possibleDomainChange();
	}
	
	/**
	 * Returns the ids of the resources to which the operation with the specified operationId 
	 * could still be assigned
	 * @param operationId id of the operation of which we are asking for remaining resources
	 * @return ids of remaining resources
	 */
	public int[] getRemainingResources(int operationId) {
		return ((ActOperationDomain) operations
				.get(getVerifiedOperationIdxForID(operationId))).getPossibleResourceIDs();
	}
	
	/**
     * An array of resourceIds indicating, for each operation, which resource is being used;
     * -1 is used to indicate that a particular opertation is not yet assigned to a resource.
	 * @return array of ids of the required resources
	 */
	public int[] getRequiredResources() {
		int[] resourceIDs = new int[operations.size()];
		for (int i=0; i<resourceIDs.length; i++) {
			resourceIDs[i] = getRequiredResource(((ActOperationDomain)operations.get(i)).getID());
		}
		return resourceIDs;
	}
	
	/**
	 * If the operation with the specified id is assigned to a particular resource
	 * the id of the required resource is returned; otherwise, -1 is returned
	 * @param operationId id of the operation of which we are asking for required resource
	 * @return id of required resource, -1 if none have been determined to be required yet
	 */
	public int getRequiredResource(int operationId) {
		return ((ActOperationDomain) operations
				.get(getVerifiedOperationIdxForID(operationId))).getRequiredResource();
	}
	
	/**
	 * A domain is bound if all operations have been assigned to a resource
     * and have a definite start time
	 * @return true if domain is bound
	 */
	public boolean isBound() {
		if (getEarliestStartTime() != getLatestStartTime()) {
    		return false;
    	}
		for (int i = 0; i < operations.size(); i++) {
			if (!((ActOperationDomain) operations.get(i)).isBound()) {
				return false;
			}
		}
		return true;
	}
    
	/** 
	 * Returns true if all operations have been assigned to a resource
	 * @return true if all operations have been assigned
	 */
	public boolean operationsAssigned() {
		for (int i=0; i<operations.size(); i++) {
			if (!((ActOperationDomain)operations.get(i)).isAssignedToResource()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Removes the resource with the specified resource id from the list
	 * of possible resources for the operation with the specified operation id
	 * @param operationId the id of the operation for which adjustments should be made
	 * @param resourceId the id of the resource on which adjustments should be made
	 * @throws PropagationFailureException if the reduction causes any errors
	 */
	public void removePossibleResource(int operationId, int resourceId)	throws PropagationFailureException {
		int opIdx = getVerifiedOperationIdxForID(operationId);
		int resIdx = getVerifiedResourceIndexForIDPerOperationIndex(opIdx, resourceId);
		((ActOperationDomain) operations.get(opIdx)).removePossibleResource(resIdx);
		if (this.isEmpty()) {
			throw new PropagationFailureException(
			"This causes an empty domain on the activity");
		}
		possibleDomainChange();
	}
	
	
	/**
	 * Returns an int representing one of the four ways that the operation can use a resource
	 * @param operationID id of operation
	 * @return int representing PRODUCES, CONSUMES, PROVIDES, REQUIRES
	 */
	public int getUsageType(int operationID) {
		return getVerifiedOperationForID(operationID).getUsageType();
	}
	
	/**
	 * Sets the way that the given operation will use resources
	 * @param operationID id of operation
	 * @param usage new type of the usage
	 */
	public void setUsageType(int operationID, int usage) {
		getVerifiedOperationForID(operationID).setUsageType(usage);
	}
	
	/**
	 * Returns the minimum capacity of the given operation
	 * @param operationID id of operation
	 * @return min capacity of given operation
	 */
	public int getCapacityMin(int operationID) {
		return getVerifiedOperationForID(operationID).getCapacityMin();
	}
	
	/**
	 * Returns the maximum capacity of the given operation
	 * @param operationID id of operation
	 * @return max capacity of given operation
	 */
	public int getCapacityMax(int operationID) {
		return getVerifiedOperationForID(operationID).getCapacityMin();
	}
	
	/**
	 * Sets the minimum capacity of the given operation
	 * @param operationID id of operation
	 * @param min new minimum
	 */
	public void setCapacityMin(int operationID, int min) throws PropagationFailureException{
		getVerifiedOperationForID(operationID).setCapacityMin(min);
	}
	
	/**
	 * Sets the maximum capacity of the given operation
	 * @param operationID id of operation
	 * @param max new maximum
	 */
	public void setCapacityMax(int operationID, int max) throws PropagationFailureException{
		getVerifiedOperationForID(operationID).setCapacityMax(max);
	}
	
	/**
	 * Sets the capacity of the given operation
	 * @param operationID id of operation
	 * @param val new capacity
	 */
	public void setCapacity(int operationID, int val) throws PropagationFailureException{
		getVerifiedOperationForID(operationID).setCapacity(val);
	}
	
	/**
	 * Sets the capacity of the given operation to a range of values
	 * @param operationID id of operation 
	 * @param start start of interval to restrict capacity to
	 * @param end end of interval to restrict capacity to
	 */
	public void setCapacityRange(int operationID, int start, int end) throws PropagationFailureException{
		getVerifiedOperationForID(operationID).setCapacityRange(start,end);
	}
	
    /**
     * Checks whether or not the specified operation is assigned to a resource
     * @param operationID the operation id of the operation in question
     * @return true if the operation is assigned to a resource; otherwise, returns false
     */
	public boolean isOperationAssignedToResource(int operationID) {
		try {
			return getVerifiedOperationForID(operationID).isAssignedToResource();
		}
		catch (JobSchedulerRuntimeException e) {
			return false;
		}
	}
	
	/**
	 * Returns the minimum of the duration sub domain
	 * @return the minimum of the duration sub domain
	 */
	public int getDurationMin() {
		return duration.getMin();
	}
	
	/**
	 * Returns the maximum of the duration sub domain
	 * @return the maximum of the duration sub domain
	 */
	public int getDurationMax() {
		return duration.getMax();
	}
	
	/**
	 * Returns the minimum of the start time sub domain
	 * @return the minimum of the start time sub domain
	 */
	public int getEarliestStartTime() {
		return startTimes.getMin();
	}
	
	/**
	 * Returns the minimum of the end time sub domain
	 * @return the minimum of the end time sub domain
	 */
	public int getEarliestEndTime() {
		return endTimes.getMin();
	}
	
	/**
	 * Returns the maximum of the start time sub domain
	 * @return the maximum of the start time sub domain
	 */
	public int getLatestStartTime() {
		return startTimes.getMax();
	}
	
	/**
	 * Returns the maximum of the end time sub domain
	 * @return the maximum of the end time sub domain
	 */
	public int getLatestEndTime() {
		return endTimes.getMax();
	}
	
	/**
	 * Returns the set that represents this activity's possible start times
	 * @return IntIntervalSet that represents this activity's possible start times
	 */
	public IntIntervalSet getStartTimes() {
		return startTimes;
	}
	
	/**
	 * Returns the set that represents this activity's possible end times
	 * @return IntIntervalSet that represents this activity's possible end times
	 */
	public IntIntervalSet getEndTimes() {
		return endTimes;
	}
	
	/**
	 * Returns the set that represents this activity's possible duration times
	 * @return IntIntervalSet that represents this activity's possible duration times
	 */
	public IntIntervalSet getDuration() {
		return duration;
	}
	
	/**
	 * Stores appropriate data for future restoration.
	 */
    public Object getDomainState() {
    	//TODO: support this
    	return null;
    }
	
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreDomainState(Object state) {
    	//TODO: support this
    }
    
    public void clearDelta() {
    	//TODO: support this
    }
	
	/**
	 * If the activity is bound, returns the start time of the activity;
	 * otherwise, -1 is returned.
	 */
	public int getBoundStartTime() {
		if (this.isBound()) {
			return startTimes.getMin();
		} else {
			return -1;
		}
	}
	
	/**
	 * If the activity is bound, returns the end time of the activity;
	 * otherwise, -1 is returned.
	 */
	public int getBoundEndTime() {
		if (this.isBound()) {
			return endTimes.getMin();
		} else {
			return -1;
		}
	}
	
	/**
	 * If the activity is bound, returns the duration of the activity;
	 * otherwise, -1 is returned.
	 */
	public int getBoundDuration() {
		if (this.isBound()) {
			return duration.getMin();
		} else {
			return -1;
		}
	}
	
	// javadoc is inherited
	public boolean choicePointStackSet() {
		if (cps != null)
			return true;
		return false;
	}
	
	// javadoc is inherited
	public void setChoicePointStack(ChoicePointStack cps) {
		if (cps==null) return;
		startTimes.setChoicePointStack(cps);
		endTimes.setChoicePointStack(cps);
		duration.setChoicePointStack(cps);
		if (this.cps != null) {
			throw new IllegalStateException(
			"Choice point stack already set for domain");
		}
		
		this.cps = cps;
		this.cpdata = cps.newNumStackSet(this);
		this.operationsAdded = cpdata.newIntList();
		
		Iterator iter = operations.iterator();
		while (iter.hasNext()) {
			ActOperationDomain oa = ((ActOperationDomain) iter.next());
			if (!oa.choicePointStackSet()) {
				oa.setChoicePointStack(cps);
			}
		}
	}
	
	/**
	 * Returns this activity's id
	 * @return id of this activity
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets this activity's id
	 * @param id new id for this activity
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	public String toString() {
		String string = " ST: "+startTimes;
		
		for (int i=0;i<operations.size(); i++) {
			string+="\n"+((ActOperationDomain)operations.get(i)).getID()+": ";
			Resource resIDs[] = ((ActOperationDomain)operations.get(i)).getPossibleResources();
			for (int j=0; j<resIDs.length; j++) {
				string+=resIDs[j].getName()+",";
			}
		}
		
		
		return string;
	}
	
}
