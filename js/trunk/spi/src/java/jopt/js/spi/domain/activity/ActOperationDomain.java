package jopt.js.spi.domain.activity;

import java.util.ArrayList;
import java.util.Iterator;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointMultiIntArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.SortableIntList;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.ResourceSet;
import jopt.js.api.variable.Resource;
import jopt.js.spi.domain.listener.OperationDomainListener;
import jopt.js.spi.util.IDStore;

/**
 * Domain for operations within the context of activities.
 * An operations start times, end times, and durations are defined as an or of all the resource associations that it encloses
 * 
 * @author James Boerkoel
 */
public class ActOperationDomain implements ActResourceDomainListener, ResourceSet, ChoicePointDataSource, ChoicePointEntryListener {
    
	//list of resources
	private ArrayList resources;
	
	//indexes of resources that are still possible
	private SortableIntList possibleResourceIndexList;
	//indexes of resources that are no longer feasible
	private SortableIntList eliminatedResourceIndexList;
	
    ChoicePointStack cps;

	private int id;
	
	//listens to changes
	private ArrayList listeners;
	
	private ActResourceDomainListener actResListener;
	
	//field representing how this resource is used REQUIRES, CONSUMES, PRODUCES, PROVIDES, etc
	private int usage;
	//a set representing the capacity of this operation
	private IntIntervalSet capacity;
	
	protected ChoicePointNumArraySet cpdata;
	ChoicePointMultiIntArray removedPossibleResources;
	
	//usage constants
	public final static int REQUIRES = 0;
	public final static int CONSUMES = 1;
	public final static int PRODUCES = 2;
	public final static int PROVIDES = 3;
	
	/**
	 * Constructs an operation association for use in an activity domain
	 * @param usage PRODUCES, PROVIDES, REQUIRES, or CONSUMES
	 * @param capacityMin the minimum capacity that this operation will use of resource
	 * @param capacityMax the maximum capacity that this operation could potentially use of determined resource
	 */
	public ActOperationDomain(int usage, int capacityMin, int capacityMax){
		super();
		resources = new ArrayList();
		capacity = new IntIntervalSet();
		capacity.add(capacityMin,capacityMax);
		possibleResourceIndexList = new SortableIntList();
		eliminatedResourceIndexList = new SortableIntList();
		this.id = IDStore.generateUniqueID();
		listeners = new ArrayList();
	}
    
	/**
	 * Adds a resource to the collection of possible resources that can be used to complete this task
	 * @param resource resource to supplement the possible resource set with
	 */
	public void addResource(ActResourceDomain resource) throws PropagationFailureException{
		resources.add(resource);
		resource.setListener(this);
		int resourceIndex = resources.size()-1;
		if(!resource.startTimes.isEmpty()) {
			addPossibleResource(resourceIndex);
		}
		else {
			eliminatePossibleResource(resourceIndex);
		}
		if ((cps!=null)&&(!resource.choicePointStackSet())) {
			resource.setChoicePointStack(cps);
		}
	}
	
	/**
	 * Returns the number of possible resources that may be used
	 * @return the number of possible resources that may be used
	 */
	public int size()  {
		return possibleResourceIndexList.size();
	}
	
	/**
	 * Returns the id for this operation
	 * @return the id for this operation
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Sets the id of this operation
	 * @param id new id for this operation
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Returns a representation of the resource at the specified index
	 * @param index index of resource being inquired about
	 * @return Resource located at specified index
	 */
	public Resource get(int index) {
		return ((ActResourceDomain)resources.get(possibleResourceIndexList.get(index))).getPointer();
	}
	
	/**
	 * Assigns the operation to the resource at the given index
	 * @param index of the resource to which this operation will be assigned
	 */
	public void assignTo(int index) throws PropagationFailureException{
		setRequiredResource(possibleResourceIndexList.get(index));
		//listener.operationRequiredResourceChange();
		for (int i=0; i<listeners.size(); i++) {
			((OperationDomainListener)listeners.get(i)).operationRequiredResourceChange();
		}
	}
	
	/**
	 * Removes the resource at the given index for this operation
	 * @param index index of the resource to remove from the list of possible resources
	 */
	public void removeAt(int index) throws PropagationFailureException {
		this.removePossibleResource(possibleResourceIndexList.get(index));
		for (int i=0; i<listeners.size(); i++) {
			((OperationDomainListener)listeners.get(i)).operationRequiredResourceChange();
		}
	}
	
	/**
	 * Adds a resource to this operation
	 * @param resourceID id of new resouce
	 * @param start start of new resource
	 * @param end end of new resource
	 * @param minDur minimum duration of new resource
	 * @param maxDur maximum duration of new resource
	 * @throws PropagationFailureException
	 */
	protected void addResource(int resourceID, int start, int end, int minDur, int maxDur) throws PropagationFailureException {
		ActResourceDomain ra = new ActResourceDomain(start,end,minDur,maxDur);
		ra.setListener(this);
		ra.setID(resourceID);
		addResource(ra);
	}
	
	/**
	 * Sets the operation domain listener on this object
	 * @param listener
	 */
	public void addListener(OperationDomainListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * Sets the act rs listener
	 * @param listener listener to be set
	 */
	public void setActResListener(ActResourceDomainListener listener) {
		this.actResListener = listener;
	}
	
	/**
	 * Returns the set that represents this operations start times
	 * @return IntIntervalSet that represents the operation start times
	 */
	public IntIntervalSet getStartTimes() {
		IntIntervalSet iis = new IntIntervalSet();
		for (int i=0; i< possibleResourceIndexList.size(); i++) {
			iis.add(((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getStartTimes());	
		}
		return iis;
	}
	
	/**
	 * Returns the set that represents this operations end times
	 * @return IntIntervalSet that represents the operation end times
	 */
	public IntIntervalSet getEndTimes() {
		IntIntervalSet iis = new IntIntervalSet();
		for (int i=0; i< possibleResourceIndexList.size(); i++) {
			iis.add(((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getEndTimes());	
		}
		return iis;
	}
	
	/**
	 * Returns the set that represents this operations duration
	 * @return IntIntervalSet that represents the operation duration
	 */
	public IntIntervalSet getDuration() {
		IntIntervalSet iis = new IntIntervalSet();
		for (int i=0; i< possibleResourceIndexList.size(); i++) {
			iis.add(((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getDuration());	
		}
		return iis;
	}
	
	
	
	/**
  	 * Called when an ActResourceDomain event notifies this operation domain
     * of a change to the specified resource's start times
  	 * @param resourceID id of resource that changed
  	 * @param delta the changes that occured to the start times
  	 */
	public void startTimesRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException {
		removeEmptyResources();
		if (delta.isEmpty()) {
  			return;
  		}
		if (actResListener!=null) {
			for (int i=0; i<possibleResourceIndexList.size(); i++){
				if (((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getID()!=resourceID) {
					IntIntervalSet currStart = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getStartTimes();
					delta.intersect(currStart.getFreeIntervalsBetween(delta.getMin(),delta.getMax()));
				}
			}
			if (delta.isEmpty()) {
	  			return;
	  		}
			actResListener.startTimesRemoved(resourceID, delta);
		}
	}
	/**
     * Called when an ActResourceDomain event notifies this operation domain
  	 * of a change to the specified resource's end times
  	 * @param resourceID id of resource that changed
  	 * @param delta set of changes that occured to the end times
  	 */
	public void endTimesRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException {
		removeEmptyResources();
		if (delta.isEmpty()) {
  			return;
  		}
		if (actResListener!=null) {
			for (int i=0; i<possibleResourceIndexList.size(); i++){
				if (((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getID()!=resourceID) {
					IntIntervalSet currEnd = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getEndTimes();
					delta.intersect(currEnd.getFreeIntervalsBetween(delta.getMin(),delta.getMax()));
				}
			}
			if (delta.isEmpty()) {
	  			return;
	  		}
			actResListener.endTimesRemoved(resourceID, delta);
		}
	}
	
	/**
     * Called when an ActResourceDomain event notifies this operation domain
  	 * of a change to the specified resource's duration
  	 * @param resourceID id of resource that changed
  	 * @param delta set of changes that occured to the duration
  	 */
  	public void durationRemoved(int resourceID, IntIntervalSet delta) throws PropagationFailureException {
  		removeEmptyResources();
  		if (delta.isEmpty()) {
  			return;
  		}
  		if (actResListener!=null) {
			for (int i=0; i<possibleResourceIndexList.size(); i++){
				if (((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getID()!=resourceID) {
					IntIntervalSet currDuration = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getDuration();
					delta.intersect(currDuration.getFreeIntervalsBetween(delta.getMin(),delta.getMax()));
				}
			}
			if (delta.isEmpty()) {
	  			return;
	  		}
			actResListener.durationRemoved(resourceID, delta);
		}
  	}
  	
  	/**
     * Called when an ActResourceDomain event notifies this operation domain
  	 * of a change to the resource domain
  	 * @param resourceID id of resource that changed
  	 * @param delta set of changes that occured to the resource
  	 */
  	public void deltaRemoved(int resourceID, ActDelta delta) throws PropagationFailureException {
  		removeEmptyResources();
  		if (delta.isEmpty()) {
  			return;
  		}
  		if (actResListener!=null) {
			for (int i=0; i<possibleResourceIndexList.size(); i++){
				if (((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getID()!=resourceID) {
					
					if (!delta.startTimes.isEmpty()) {
						IntIntervalSet currStart = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getStartTimes();
						delta.startTimes.intersect(currStart.getFreeIntervalsBetween(delta.startTimes.getMin(),delta.startTimes.getMax()));
					}
					
					if (!delta.endTimes.isEmpty()) {
						IntIntervalSet currEnd = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getEndTimes();
						delta.endTimes.intersect(currEnd.getFreeIntervalsBetween(delta.endTimes.getMin(),delta.endTimes.getMax()));
					}
					
					if (!delta.duration.isEmpty()) {
						IntIntervalSet currDuration = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getDuration();
						delta.duration.intersect(currDuration.getFreeIntervalsBetween(delta.duration.getMin(),delta.duration.getMax()));
					}
				}
			}
			if (delta.isEmpty()) {
	  			return;
	  		}
			actResListener.deltaRemoved(resourceID, delta);
		}
  	}
    
  	//utility method to remove any resources from the possible resource list that can no longer service this operation
    private void removeEmptyResources() throws PropagationFailureException{
    	boolean anyDelta = false;
    	for (int i=0;i<possibleResourceIndexList.size();i++) {
			if (((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).isEmpty()) {
				if (cpdata!=null) {
					int index = removedPossibleResources.add(0,possibleResourceIndexList.get(i));
		    		removedPossibleResources.set(1,index,1);
				}
				anyDelta = true;
				eliminatePossibleResource(possibleResourceIndexList.get(i));
				i--;
			}
		}
    	if (possibleResourceIndexList.size()==0) {
    		throw new PropagationFailureException("All possible resources have been removed from this operation");
    	}
    	if (anyDelta&&(possibleResourceIndexList.size()==1)&&(listeners.size()>0)) {
    		for (int i=0; i<listeners.size(); i++) {
				((OperationDomainListener)listeners.get(i)).operationRequiredResourceChange();
			}
    	}
    }
    
    /**
     * Returns true if there is only one potential resource assignment left
     * @return true if there is only one potential resource assignment left
     */
    public boolean isAssignedToResource() {
    	return (possibleResourceIndexList.size()==1);
    }
    
    /**
     * Helper method to return the resource index of a given id within this operation
     */
    protected int getResourceIndex(int resourceID) {
    	for (int i=0; i<resources.size(); i++) {
    		if (((ActResourceDomain)resources.get(i)).getID()==resourceID) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
	 * Returns an IntIntervalSet representing the times that this operation could potentially use the specified resource
	 * @param resourceIdx the index of which resource in question
	 * @return IntIntervalSet representing the times that this operation could potentially use the resource
	 */
	public IntIntervalSet getPotentialUsageTimeline(int resourceIdx) {
		return ((ActResourceDomain)resources.get(resourceIdx)).getPotentialUsageTimeline();
	}
    
	/**
	 * Returns an IntIntervalSet representing the time that this operation would definitely be using the specified resource
	 * @param resourceIdx the index of which resource in question
	 * @return IntIntervalSet representing the times that this operation would definitely use the resource
	 */
	public IntIntervalSet getActualUsageTimeline(int resourceIdx) {
		if (possibleResourceIndexList.size() >1) {
			return new IntIntervalSet();
		}
		else {
			return ((ActResourceDomain)resources.get(resourceIdx)).getActualUsageTimeline(); 
		}
	}
	
    /**
     * Returns the maximum duration of this operation on the specified resource
     * @param resourceIdx index of resource being inquired about
     * @return maximum duration this operation could have on this resource
     */
    public int getDurationMax(int resourceIdx){
    	return ((ActResourceDomain)resources.get(resourceIdx)).getDurationMax();
    }
    
    /**
     * Returns the minimum duration of this operation on the specified resource
     * @param resourceIdx index of resource being inquired about
     * @return minimum duration this operation could have on this resource
     */
    public int getDurationMin(int resourceIdx){
    	return ((ActResourceDomain)resources.get(resourceIdx)).getDurationMin();
    }
    
    /**
     * Returns an int representing the four ways that an operation can use a resource
     * @return int representing PRODUCES, CONSUMES, PROVIDES, REQUIRES
     */
    public int getUsageType() {
    	return usage;
    }
    
    /**
     * Sets the way that this operation will use resources
     * @param usage new type of the usage
     */
    public void setUsageType(int usage) {
    	this.usage = usage;
    }
    
    /**
     * Returns the minimum capacity of this operation
     * @return min capacity of this operation
     */
    public int getCapacityMin() {
    	return capacity.getMin();
    }
    
    /**
     * Returns the maximum capacity of this operation
     * @return max capacity of this operation
     */
    public int getCapacityMax() {
    	return capacity.getMax();
    }
    
    /**
     * Sets the minimum capacity of this operation
     * @param min new minimum
     */
    public void setCapacityMin(int min) throws PropagationFailureException{
    	if (min<capacity.getMax()) {
    		throw new PropagationFailureException("This eliminates all valids capacity for operation "+id);
    	}
    	if (min>capacity.getMin()) {
    		capacity.removeEndingBefore(min);
    		if (isAssignedToResource()) {
    			for (int i=0; i<listeners.size(); i++) {
    				((OperationDomainListener)listeners.get(i)).operationCapacityChange();
    			}
    		}
    	}
    }
    
    /**
     * Sets the maximum capacity of this operation
     * @param max new maximum
     */
    public void setCapacityMax(int max) throws PropagationFailureException{
    	if (max<capacity.getMin()) {
    		throw new PropagationFailureException("This eliminates all valids capacity for operation "+id);
    	}
    	if (max<capacity.getMax()) {
    		capacity.removeStartingAfter(max);
	    	if (isAssignedToResource()) {
	    		for (int i=0; i<listeners.size(); i++) {
	    			System.out.println("here!");
	    			((OperationDomainListener)listeners.get(i)).operationCapacityChange();
    			}
			}
    	}
    }
    
    /**
     * Sets the capacity of this operation
     * @param val new capacity
     */
    public void setCapacity(int val) throws PropagationFailureException {
    	if ((capacity.getMax()>val)||(capacity.getMin()<val)){
    		setCapacityRange(val,val);
    		if (isAssignedToResource()) {
    			for (int i=0; i<listeners.size(); i++) {
    				((OperationDomainListener)listeners.get(i)).operationCapacityChange();
    			}
    		}
    	}
    }
    
    /**
     * Sets the capacity to a range of valid values 
     * @param start start of interval to restrict capacity to
     * @param end end of interval to restrict capacity to
     */
    public void setCapacityRange(int start, int end) throws PropagationFailureException{
    	if ((capacity.getMax()>end)||(capacity.getMin()<start)){
    		setCapacityMin(start);
    		setCapacityMax(end);
    		if (isAssignedToResource()) {
    			for (int i=0; i<listeners.size(); i++) {
    				((OperationDomainListener)listeners.get(i)).operationCapacityChange();
    			}
    		}
    	}
    }
    
    /**
     * Removes a range of valid values from the capacity
     * @param start start of interval to remove
     * @param end end of interval to remove
     */
    public void removeCapacityRange(int start, int end) throws PropagationFailureException {
    	capacity.remove(start,end);
    	if (isAssignedToResource()) {
    		for (int i=0; i<listeners.size(); i++) {
				((OperationDomainListener)listeners.get(i)).operationCapacityChange();
			}
		}
    }
    
	/**
     * Returns the earliest start time for this operation with respect to the specified resource
     * @param resourceIdx index of resource being inqured about
     * @return earliest start time
     */
    public int getEarliestStartTime(int resourceIdx) {
    	return ((ActResourceDomain)resources.get(resourceIdx)).getEarliestStartTime();
    }
    
    /**
     * Returns the latest start time for this operation with respect to the specified resource
     * @param resourceIdx index of resource being inqured about
     * @return latest start time
     */
    public int getLatestStartTime(int resourceIdx) {
    	return ((ActResourceDomain)resources.get(resourceIdx)).getLatestStartTime();
    }
    
	/**
     * Returns the earliest end time for this operation with respect to the specified resource
     * @param resourceIdx index of resource being inqured about\
     * @return earliest end time
     */
    public int getEarliestEndTime(int resourceIdx) {
    	return ((ActResourceDomain)resources.get(resourceIdx)).getEarliestEndTime();
    }
    
    /**
     * Returns the latest end time for this operation with respect to the specified resource
     * @param resourceIdx index of resource being inqured about
     * @return latest end time
     */
    public int getLatestEndTime(int resourceIdx) {
    	return ((ActResourceDomain)resources.get(resourceIdx)).getLatestEndTime();
    }
    
    /**
     * Returns the number of resources that could possibly satisfy the constraints of this operation
     * @return the number of resources that could possibly satisfy the constraints of this operation
     */
    public int getPossibleResourceCount() {
    	return possibleResourceIndexList.size();
    }
    
    /**
     * Returns an array of ints representing the ids of the resources that could possibly satisfy the constraints of this operation
     * @return an array of ints representing the ids of the resources that could possibly satisfy the constraints of this operation
     */
    public int[] getPossibleResourceIDs() {
    	int[] possRes = new int[possibleResourceIndexList.size()];
    	for (int i=0; i<possRes.length;i++) {
    		possRes[i]=((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getID();
    	}
    	return possRes;
    }
    
    /**
     * Returns an array of resources that could possibly satisfy the constraints of this operation
     * @return an array of resources that could possibly satisfy the constraints of this operation
     */
    public Resource[] getPossibleResources() {
    	Resource[] possRes = new Resource[possibleResourceIndexList.size()];
    	for (int i=0; i<possRes.length;i++) {
    		possRes[i]=((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getPointer();
    	}
    	return possRes;
    }
    
    /**
     * Returns an array of ints representing the idxs of the resources that could possibly satisfy the constraints of this operation
     * @return an array of ints representing the idxs of the resources that could possibly satisfy the constraints of this operation
     */
    public int[] getPossibleResourceIndices() {
    	int[] possRes = new int[possibleResourceIndexList.size()];
    	for (int i=0; i<possRes.length;i++) {
    		possRes[i]=possibleResourceIndexList.get(i);
    	}
    	return possRes;
    }
    
    /**
     * Eliminates all possible resources but the one specified
     * @param resourceIdx index of the resource that this operation will use
     * @throws PropagationFailureException
     */
    public void setRequiredResource(int resourceIdx)throws PropagationFailureException{
    	for (int i=0;  i<possibleResourceIndexList.size();i++) {
    		int size = possibleResourceIndexList.size();
    		if (possibleResourceIndexList.get(i)!=resourceIdx) {
    			ActResourceDomain ra = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i)));
    			ra.removeStartTimes(ra.getEarliestStartTime(),ra.getLatestStartTime(),true);
    			ra.removeEndTimes(ra.getEarliestEndTime(),ra.getLatestEndTime(),true);
    		}
    		if (size > possibleResourceIndexList.size()) {
    			i--;
    		}
    	}
    	if (possibleResourceIndexList.size()==0) {
    		throw new PropagationFailureException("No possible resources can serve operation "+id);
    	}
    }
    
    /**
     * Returns an array of ints representing the ids of the resources associated with this operation
     * @return an array of ints representing the ids of the resources associated with this operation
     */
    public int[] getResourceIDs() {
    	int[] resID = new int[resources.size()];
    	for (int i=0;i<resources.size(); i++) {
    		resID[i] = ((ActResourceDomain)resources.get(i)).getID();
    	}
    	return resID;
    }
    
    /**
     * Removes the resource at resourceIdx from the list of possible resources for this operation
     * @param resourceIdx index of the resource being removed
     * @throws PropagationFailureException
     */
    public void removePossibleResource(int resourceIdx) throws PropagationFailureException {
    	int est = ((ActResourceDomain)resources.get(resourceIdx)).getEarliestStartTime();
    	int lst = ((ActResourceDomain)resources.get(resourceIdx)).getLatestStartTime();
    	int eet = ((ActResourceDomain)resources.get(resourceIdx)).getEarliestEndTime();
    	int let = ((ActResourceDomain)resources.get(resourceIdx)).getLatestEndTime();
    	((ActResourceDomain)resources.get(resourceIdx)).removeStartTimes(est, lst, true);
    	((ActResourceDomain)resources.get(resourceIdx)).removeEndTimes(eet, let, true);
    	eliminatePossibleResource(resourceIdx);
    	if (possibleResourceIndexList.size()==0) {
    		throw new PropagationFailureException("No possible resources can serve operation "+id);
    	}
    }
    
    /**
     * If this operation is assigned to a particular resource
     * the id of the required resource is returned; otherwise, -1 is returned.
     */
    public int getRequiredResource() {
    	if (possibleResourceIndexList.size()==1) {
    		return ((ActResourceDomain)resources.get(possibleResourceIndexList.get(0))).getID();
    	}
    	else {
    		return -1;
    	}
    }
    
    /**
     * Sets the timeline of the resource at resourceIdx
     * @param resourceIdx index of the resource being set
     * @param timeline timeline that the resource is being set to
     * @throws PropagationFailureException
     */
    public void setTimeline(int resourceIdx, IntIntervalSet timeline) throws PropagationFailureException {
    	((ActResourceDomain)resources.get(resourceIdx)).setTimeline(timeline,true);
    	try {
    		verifyResourceConsistency(resourceIdx);
    	}
    	catch (PropagationFailureException pfe) {
    		throw new PropagationFailureException("Error setting Timeline: "+pfe.getLocalizedMessage());
    	}
    	if (possibleResourceIndexList.size()==0) {
    		throw new PropagationFailureException("No possible resources can serve operation "+id);
    	}
    }
    
    
    /**
     * Set the earliest start time for the operation 
     * @param est the earliest start time
     * @throws PropagationFailureException
     */
    public void setEarliestStartTime(int est) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setEarliestStartTime(est, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Set the latest start time for the operation 
     * @param lst the latest start time
     * @throws PropagationFailureException
     */
    public void setLatestStartTime(int lst) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setLatestStartTime(lst, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Set the earliest end time for the operation
     * @param eet the earliest end time
     * @throws PropagationFailureException
     */
    public void setEarliestEndTime(int eet) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setEarliestEndTime(eet, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }

    /**
     * Set the latest end time for the operation
     * @param let the latest end time
     * @throws PropagationFailureException
     */
    public void setLatestEndTime(int let) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setLatestEndTime(let, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Sets the max duration for the operation
     * @param durMax new maximum duration of this operation
     * @throws PropagationFailureException
     */
    public void setDurationMax(int durMax)throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setDurationMax(durMax, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Sets the min duration for the operation
     * @param durMin new minimum duration of this operation
     * @throws PropagationFailureException
     */
    public void setDurationMin(int durMin)throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setDurationMin(durMin, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Sets the range of possible duration values for the operation
     * @param durMin new minimum duration of this operation
     * @param durMax new maximum duration of this operation
     * @throws PropagationFailureException
     */
    public void setDurationRange(int durMin, int durMax)throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).setDurationRange(durMin, durMax, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
		removeEmptyResources();
    }
    
    /**
     * Sets the duration of this operation to given value
     * @param duration val to set duration to
     * @throws PropagationFailureException
     */
    public void setDuration(int duration) throws PropagationFailureException {
    	setDurationRange(duration,duration);
    }
    
    /**
     * Removes a range of start time values from the domain of this operation
     * @param start start of the range of possible start times being removed
     * @param end end of the range of possible start times being removed
     * @throws PropagationFailureException
     */
    public void removeStartTimes(int start, int end) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeStartTimes(start,end, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Removes a range of end time values from the domain of this operation
     * @param start start of the range of possible end times being removed
     * @param end end of the range of possible end times being removed
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeEndTimes(int start, int end)throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeEndTimes(start,end, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Removes a single value from the start times of this operation
     * @param time time removed from possible start times
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeStartTime(int time)throws PropagationFailureException {
    	removeStartTimes(time,time);
    }
    
    /**
     * Removes a single time from the end times of this operation
     * @param time time removed from possible end times
     * @throws PropagationFailureException
     */
    public void removeEndTime(int time)throws PropagationFailureException {
    	removeEndTime(time);
    }
    
    /**
     * Removes a range of values from the duration of this operation
     * @param start start of the range of possible start times being removed
     * @param end end of the range of possible start times being removed
     * @throws PropagationFailureException
     */
    public void removeDuration(int start, int end)throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeDurationRange(start,end, true);
    	}
    	removeEmptyResources();
    }
    
    //make sure that all resources can be used, and if none can, throw a propagation failure exception
    private void verifyResourceConsistency(int resourceIndex) throws PropagationFailureException{
    	if (possibleResourceIndexList.size()==0) {
    			
				throw new PropagationFailureException("All possible resources have been removed for operation "+id);
			}
    	if (((ActResourceDomain)resources.get(resourceIndex)).isEmpty()) {
   			eliminatePossibleResource(resourceIndex);
   			if (possibleResourceIndexList.size()==0) {
   				throw new PropagationFailureException("All possible resources have been removed for operation "+id);
   			}
   		}
    }
    
    /**
     * Set the earliest start time for the specified resource 
     * @param est the earliest start time
     * @param resourceIndex index of the resource whose est is being set
     * @throws PropagationFailureException
     */
    public void setEarliestStartTime(int est, int resourceIndex) throws PropagationFailureException {
   		((ActResourceDomain)resources.get(resourceIndex)).setEarliestStartTime(est,true);
    }
    
    /**
     * Set the latest start time for the specified resource 
     * @param lst the latest start time
     * @param resourceIndex index of the resource whose lst is being set
     * @throws PropagationFailureException
     */
    public void setLatestStartTime(int lst, int resourceIndex) throws PropagationFailureException {
   		((ActResourceDomain)resources.get(resourceIndex)).setLatestStartTime(lst,true);
    }
    
    /**
     * Set the earliest end time for the specified resource 
     * @param eet the earliest end time
     * @param resourceIndex index of the resources whose eet is being set
     * @throws PropagationFailureException
     */
    public void setEarliestEndTime(int eet, int resourceIndex) throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).setEarliestEndTime(eet,true);
    }

    /**
     * Set the latest end time for the specified resource
     * @param let the latest end time
     * @param resourceIndex index of the resource whose let is being set
     * @throws PropagationFailureException
     */
    public void setLatestEndTime(int let, int resourceIndex) throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).setLatestEndTime(let,true);
    }
    
    /**
     * Sets the maximum duration of the resource at resourceIndex
     * @param durMax new maximum duration for resource
     * @param resourceIndex index of resource whose duration being set
     * @throws PropagationFailureException
     */
    public void setDurationMax(int durMax, int resourceIndex)throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).setDurationMax(durMax,true);
    }
    
    /**
     * Sets the minimum duration of the resource at resourceIndex
     * @param durMin new minimum duration for resource
     * @param resourceIndex index of resource whose duration is being set
     * @throws PropagationFailureException
     */
    public void setDurationMin(int durMin, int resourceIndex)throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).setDurationMin(durMin,true);
    }
    
    /**
     * Sets the a range of valid durations for the resource at resourceIndex
     * @param durMin new minimum duration for resource
     * @param durMax new maximum duration for resource
     * @param resourceIndex index of resource whose duration is being set
     * @throws PropagationFailureException
     */
    public void setDurationRange(int durMin, int durMax, int resourceIndex)throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).setDurationRange(durMin,durMax,true);
    }
    
    /**
     * Removes a range of values from the start times of this operation
     * @param start start of the range of possible start times being removed
     * @param end end of the range of possible start times being removed
     * @param resourceIndex index of resource whose start times are being altered
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeStartTimes(int start, int end, int resourceIndex) throws PropagationFailureException{
   		((ActResourceDomain)resources.get(resourceIndex)).removeStartTimes(start,end,true);
    }
    
    /**
     * Removes a range of values from the end times of this operation
     *@param start start of the range of possible end times being removed
     *@param end end of the range of possible end times being removed
     *@param resourceIndex index of resource whose end times are being altered
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeEndTimes(int start, int end, int resourceIndex)throws PropagationFailureException {
   		((ActResourceDomain)resources.get(resourceIndex)).removeEndTimes(start,end,true);
    }
    
    /**
     * Removes a set of times from start times for this operation
     * @param times set of times to remove
     * @throws PropagationFailureException
     */
    public void removeStartTimes(IntIntervalSet times) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeStartTimes(times, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }

    /**
     * Removes a set of times from end times for this operation
     * @param times set of times to remove
     * @throws PropagationFailureException
     */
    public void removeEndTimes(IntIntervalSet times) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeEndTimes(times, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Removes a set of times from duration times for this operation
     * @param times set of times to remove
     * @throws PropagationFailureException
     */
    public void removeDuration(IntIntervalSet times) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeDuration(times, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }
    
    /**
     * Removes set of times from start, end, and duration times
     * @param delta set of times to remove
     * @throws PropagationFailureException
     */
    public void removeDelta(ActDelta delta) throws PropagationFailureException {
    	for (int i=0; i< possibleResourceIndexList.size(); i++) {
    		int size = possibleResourceIndexList.size();
    		((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).removeDelta(delta, true);
    		if (possibleResourceIndexList.size()<size) {
    			i--;
    		}
    	}
    	removeEmptyResources();
    }

    
    // javadoc is inherited
    public boolean choicePointStackSet() {
        if(cps!=null)
            return true;
        return false;
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cps !=null) {
            throw new IllegalStateException("Choice point stack already set for domain");
        }
        this.cps=cps;
        
        Iterator iter = resources.iterator();
        while (iter.hasNext()) {
        	ActResourceDomain ra =((ActResourceDomain)iter.next()); 
        	if (!ra.choicePointStackSet()) {
        		ra.setChoicePointStack(cps);
        	}
        }
        this.cpdata = cps.newNumStackSet(this);
		this.removedPossibleResources = cpdata.newMultiIntList(2);
        
    }
    
    //helper to back off-removal of resources on a choice point pop
    private void addPossibleResource(int resourceIdx) {
    	eliminatedResourceIndexList.removeElement(resourceIdx);
    	if (!possibleResourceIndexList.contains(resourceIdx)) {
    		possibleResourceIndexList.add(resourceIdx);
    	}
    }
    
    //helper method to help record which resources have been eliminated as possibilities
    private void eliminatePossibleResource(int resourceIdx) {
    	if (!eliminatedResourceIndexList.contains(resourceIdx)) {
    		eliminatedResourceIndexList.add(resourceIdx);
    	}
    	possibleResourceIndexList.removeElement(resourceIdx);
    }
    
    /**
     * Returns true if operation has only one valid start time, end time, and resource
     * @return true if operation has only one valid start time, end time, and resource
     */
    public boolean isBound() {
    	return ((possibleResourceIndexList.size()==1)&&
    	(((ActResourceDomain)resources.get(possibleResourceIndexList.get(0))).getStartTimes().size() == 1) &&
    	(((ActResourceDomain)resources.get(possibleResourceIndexList.get(0))).getEndTimes().size() == 1));		
    }
    
    /**
     * Returns the earliest time that this operation could start
     * @return earliest time that this operation could start
     */
	public int getEarliestStartTime() {
		int est = Integer.MAX_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisEst = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getEarliestStartTime();
			if (thisEst < est) {
				est = thisEst;
			}
		}
		return est;
	}
	
	/**
     * Returns the earliest time that this operation could end
     * @return earliest time that this operation could end
     */
	public int getEarliestEndTime() {
		int eet = Integer.MAX_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisEet = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getEarliestEndTime();
			if (thisEet < eet) {
				eet = thisEet;
			}
		}
		return eet;
	}
	
	/**
     * Returns the latest time that this operation could start
     * @return latest time that this operation could start
     */
	public int getLatestStartTime() {
		int lst = Integer.MIN_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisLst = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getLatestStartTime();
			if (thisLst > lst) {
				lst = thisLst;
			}
		}
		return lst;
	}
	
	/**
     * Returns the latest time that this operation could end
     * @return latest time that this operation could end
     */
	public int getLatestEndTime() {
		int let = Integer.MIN_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisLet = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getLatestEndTime();
			if (thisLet > let) {
				let = thisLet;
			}
		}
		return let;
	}
	
	/**
     * Returns the maximum duration
     * @return maximum duration
     */
	public int getDurationMax() {
		int durMax = Integer.MIN_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisMax = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getDurationMax();
			if (thisMax > durMax) {
				durMax = thisMax;
			}
		}
		return durMax;
	}
	
	/**
     * Returns the minimum duration
     * @return minimum duration
     */
	public int getDurationMin(){
		int durMin = Integer.MAX_VALUE;
		for (int i=0; i<possibleResourceIndexList.size(); i++) {
			int thisMin = ((ActResourceDomain)resources.get(possibleResourceIndexList.get(i))).getDurationMin();
			if (thisMin < durMin) {
				durMin = thisMin;
			}
		}
		return durMin;		
	}
    
    // javadoc is inherited
	public void beforeChoicePointPopEvent() {
		for (int i=0; i<removedPossibleResources.size(); i++) {
			int resIdx = removedPossibleResources.get(0,i);
			boolean removed =  (removedPossibleResources.get(1,i)>0);
			if (removed) {
				addPossibleResource(resIdx);
			}
			else {
				eliminatePossibleResource(resIdx);
			}
		}
	}
	
	// javadoc is inherited
	public void afterChoicePointPopEvent() {
		
	}
	
	// javadoc is inherited
	public void beforeChoicePointPushEvent() {
		
	}
	
	// javadoc is inherited
	public void afterChoicePointPushEvent() {
		for (int i=0; i<removedPossibleResources.size(); i++) {
			int resIdx = removedPossibleResources.get(0,i);
			boolean added =  (removedPossibleResources.get(1,i)<0);
			if (added) {
				addPossibleResource(resIdx);
			}
			else {
				eliminatePossibleResource(resIdx);
			}
		}
	}
	
}
