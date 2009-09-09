package jopt.js.spi.domain.activity;

import java.util.HashMap;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.variable.Resource;
import jopt.js.spi.util.CPSIntIntervalSet;
import jopt.js.spi.util.IDStore;

/**
 * Domain for resources within the context of operations (and activities).
 * 
 * @author James Boerkoel
 */
public class ActResourceDomain implements ChoicePointDataSource {
	
	protected final static int CPS_START = 0;
	protected final static int CPS_END = 1;
	protected final static int CPS_DURATION = 2;
	protected final static int EVENT_START = 3;
	protected final static int EVENT_END = 4;
	protected final static int EVENT_DURATION = 5;
	
	protected boolean cpsEvent = false;
	
	//maintains the data for potential operational times of this resource activity combination
	protected CPSIntIntervalSet startTimes;
	protected CPSIntIntervalSet endTimes;
	protected CPSIntIntervalSet duration;
	
	ChoicePointStack cps;
	
	protected Resource res;
	
	protected int id;
	
	ActResourceDomainListener actResListener;
	
	/**
	 * Creates a resource domain to keep track of the amount of resource associated with an operation
     * within an activity
	 * @param est earliest start time that this is resource is available to the operation
	 * @param lst latest start time that this resource is available to the operation
	 * @param eet earliest end time that this resource is available to the operation 
	 * @param let latest end time that this resource is available to the operation
	 * @param minDur the minimum duration of this resource
	 * @param maxDur the maximum duration of this resource
	 */
	public ActResourceDomain(int est, int lst, int eet, int let, int minDur, int maxDur){
		this(IDStore.generateUniqueID(), est, lst, eet, let, minDur, maxDur);
	}
	
	/**
	 * Creates a resource domain to keep track of the amount of resource associated with an operation
     * within an activity
	 * @param id id to set to this resource, must be unique
	 * @param est earliest start time that this is resource is available to the operation
	 * @param lst latest start time that this resource is available to the operation
	 * @param eet earliest end time that this resource is available to the operation 
	 * @param let latest end time that this resource is available to the operation
	 * @param minDur the minimum duration of this resource
	 * @param maxDur the maximum duration of this resource
	 */
	public ActResourceDomain(int id,int est, int lst, int eet, int let, int minDur, int maxDur){
		this.id = id;
		startTimes = new CPSIntIntervalSet();
		endTimes = new CPSIntIntervalSet();
		duration = new CPSIntIntervalSet();
		try {
			startTimes.add(est,lst);
			endTimes.add(eet,let);
			duration.add(minDur,maxDur);
			makeBoundConsistent(new ActDelta());
		}
		catch (PropagationFailureException pfe) {}
	}
	
	protected ActResourceDomain(){}
	
	/**
	 * Creates a resource domain to keep track of the amount of resource associated with an operation
     * within an activity
	 * @param est earliest start time that this is resource is available to the operation
	 * @param lst latest start time that this resource is available to the operation
	 * @param minDur the minimum duration of this resource
	 * @param maxDur the maximum duration of this resource
	 */
	public ActResourceDomain(int est, int lst, int minDur, int maxDur){
		this(IDStore.generateUniqueID(),est,lst,est+(minDur-1),lst+(maxDur-1),minDur,maxDur);
	}
	
	/**
	 * Creates a resource domain to keep track of the amount of resource associated with an operation
     * within an activity
	 * @param id id to set to this resource, must be unique
	 * @param est earliest start time that this is resource is available to the operation
	 * @param lst latest start time that this resource is available to the operation
	 * @param minDur the minimum duration of this resource
	 * @param maxDur the maximum duration of this resource
	 */
	public ActResourceDomain(int id,int est, int lst, int minDur, int maxDur){
		this(id,est,lst,est+(minDur-1),lst+(maxDur-1),minDur,maxDur);
	}
	
	/**
	 * Sets the listener for events from this object
	 * @param listener the listener to notify when changes occur
	 */
	public void setListener(ActResourceDomainListener listener) {
		actResListener = listener;
	}
	
	/**
	 * Notifies listeners of a change to the this domain
	 * @param delta values that have been removed
	 * @throws PropagationFailureException
	 */
	private void notifyDelta(ActDelta delta) throws PropagationFailureException {
		if (actResListener != null)
			actResListener.deltaRemoved(id, delta);
	}
	
	/**
	 * Notifies listeners of a start time change
	 * @param set of start time values that have been removed
	 * @throws PropagationFailureException
	 */
	private void notifyStartTimeDelta(IntIntervalSet delta) throws PropagationFailureException {
		if (actResListener != null)
			actResListener.startTimesRemoved(id, delta);
	}
	
	/**
	 * Notifies listeners of an end time change
	 * @param set of end time values that have been removed
	 * @throws PropagationFailureException
	 */
	private void notifyEndTimeDelta(IntIntervalSet delta) throws PropagationFailureException {
		if (actResListener != null)
			actResListener.endTimesRemoved(id, delta);
	}
	
	/**
	 * Notifies listeners of a duration change
	 * @param duration values that have been removed
	 * @throws PropagationFailureException
	 */
	private void notifyDurationDelta(IntIntervalSet delta) throws PropagationFailureException {
		if (actResListener != null)
			actResListener.durationRemoved(id, delta);
	}
	
	/**
	 * Sets the object to which this domain is related
	 * @param res the resource whose domain is represented by this object
	 */
	public void setPointer(Resource res) {
		this.res = res;
	}
	
	/**
	 * Retrieves the object to which this domain is related if it has been set;
     * otherwise, returns null
	 * @return resource whose domain is represented by this object
	 */
	public Resource getPointer() {
		return res;
	}
	
	//javadoc inherited from Object
	public String toString() {
		return "\nID: "+id+"\n start: "+startTimes+"\n duration: "+duration+"\n end: "+endTimes;
	}
	
	/**
	 * Helper method to make the domain 'bounds consistent' (based on min/max values).
     * This is sufficient for domain consistency whenever 
     * there are no gaps within the individual data sets.
	 */
	private void makeBoundConsistent(ActDelta actDelta) throws PropagationFailureException {
		if (!duration.isEmpty()) {
			if (actDelta != null) {
				actDelta.duration.add(duration.removeEndingBeforeGetDelta(endTimes.getMin()-startTimes.getMax()+1));
				actDelta.duration.add(duration.removeStartingAfterGetDelta(endTimes.getMax()-startTimes.getMin()+1));
			}
			else {
				duration.removeEndingBefore(endTimes.getMin()-startTimes.getMax()+1);
				duration.removeStartingAfter(endTimes.getMax()-startTimes.getMin()+1);
			}
		}
		if (!endTimes.isEmpty()) {
			if (actDelta != null) {
				actDelta.endTimes.add(endTimes.removeEndingBeforeGetDelta(startTimes.getMin()+(duration.getMin()-1)));
				actDelta.endTimes.add(endTimes.removeStartingAfterGetDelta(startTimes.getMax()+(duration.getMax()-1)));
			}
			else {
				endTimes.removeEndingBefore(startTimes.getMin()+(duration.getMin()-1));
				endTimes.removeStartingAfter(startTimes.getMax()+(duration.getMax()-1));
			}
		}
		if (!startTimes.isEmpty()) {
			if (actDelta != null) {
				actDelta.startTimes.add(startTimes.removeEndingBeforeGetDelta(endTimes.getMin()-(duration.getMax()-1)));
				actDelta.startTimes.add(startTimes.removeStartingAfterGetDelta(endTimes.getMax()-(duration.getMin()-1)));
			}
			else {
				startTimes.removeEndingBefore(endTimes.getMin()-(duration.getMax()-1));
				startTimes.removeStartingAfter(endTimes.getMax()-(duration.getMin()-1));
			}
		}
	}
	
	/**
	 * Sets the id of the resource
	 * @param id new id
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the id for this resource
	 * @return	id of this resource
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Returns the timeline for which this resource can be used
	 * @return the timeline for which this resource can be used
	 */
	public IntIntervalSet getPotentialUsageTimeline() {
		IntIntervalSet puTime = new IntIntervalSet();
		int index = startTimes.getFirstIntervalIndex();
		int durMax = duration.getMax();
		while (index >= 0) {
			puTime.add(startTimes.getMin(index),startTimes.getMax(index) + (durMax-1));
			index = startTimes.getNextIntervalIndex(index);
		}
		return puTime;
	}
	
	/**
	 * Returns the timeline that this resource is actually being used
     * (in the context of an operation)
	 * @return the timeline that this resource is actually being used
	 */
	public IntIntervalSet getActualUsageTimeline() {
		IntIntervalSet puTime = new IntIntervalSet();
		if (startTimes.size() == 0) {
			return puTime;
		}
		int durMin = duration.getMin();
		if (startTimes.getMin() + (durMin-1) >= startTimes.getMax()) {
			puTime.add(startTimes.getMax(),startTimes.getMin() + (durMin-1));
		}
		return puTime;
	}
	
	/**
	 * Returns the set of valid start times
	 * as an int interval set
	 */
	public IntIntervalSet getStartTimes() {
		return startTimes;
	}
	
	/**
	 * Returns the set of valid end times
	 * as an int interval set
	 */
	public IntIntervalSet getEndTimes() {
		return endTimes;
	}
	
	/**
	 * Returns the set of valid duration times
	 * as an int interval set
	 */
	public IntIntervalSet getDuration() {
		return duration;
	}
	
	/**
	 * Returns the earliest start time for this domain
	 * @return integer representing the earliest start time
	 */
	public int getEarliestStartTime() {
		return startTimes.getMin();
	}
	
	/**
	 * Returns the latest start time for this domain
	 * @return integer representing the latest start time
	 */
	public int getLatestStartTime() {
		return startTimes.getMax();
	}
	
	/**
	 * Returns the earliest end time for this domain
	 * @return integer representing the earliest end time
	 */
	public int getEarliestEndTime() {
		return endTimes.getMin();
	}
	
	/**
	 * Returns the latest end time for this domain
	 * @return integer representing the latest end time
	 */
	public int getLatestEndTime() {
		return endTimes.getMax();
	}
	
	/**
	 * Set the earliest start time for this resource 
	 * @param est the earliest start time
     * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setEarliestStartTime(int est, boolean notify) throws PropagationFailureException {
		if ((startTimes.getMin()<est)&&(!startTimes.isEmpty())) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.startTimes = startTimes.removeEndingBeforeGetDelta(est);
				makeBoundConsistent(delta);
				notifyDelta(delta);
			}
			else {
				startTimes.removeEndingBefore(est);
				makeBoundConsistent(null);
			}
		}
	}
	
	/**
	 * Set the latest start time for this resource 
	 * @param lst the latest start time
     * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setLatestStartTime(int lst, boolean notify) throws PropagationFailureException {
		if ((startTimes.getMax()>lst)&&(!startTimes.isEmpty())) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.startTimes = startTimes.removeStartingAfterGetDelta(lst);
				makeBoundConsistent(delta);
				notifyDelta(delta);
			}
			else {
				startTimes.removeStartingAfter(lst);
				makeBoundConsistent(null);
			}
		}
	}
	
	/**
	 * Set the earliest end time for this resource 
	 * @param eet the earliest end time
     * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setEarliestEndTime(int eet, boolean notify) throws PropagationFailureException {
		if ((!endTimes.isEmpty())&&(endTimes.getMin()<eet)) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.endTimes.add(endTimes.removeEndingBeforeGetDelta(eet));
				makeBoundConsistent(delta);
				notifyDelta(delta);
			}
			else {
				endTimes.removeEndingBefore(eet);
				makeBoundConsistent(null);
			}
		}
	}
	
	/**
	 * Set the latest end time for this resource 
	 * @param let the latest end time
     * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setLatestEndTime(int let, boolean notify) throws PropagationFailureException {
		if ((endTimes.getMax()>let)&&(!endTimes.isEmpty())) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.endTimes.add(endTimes.removeStartingAfterGetDelta(let));
				makeBoundConsistent(delta);
				notifyDelta(delta);
			}
			else {
				endTimes.removeStartingAfter(let);
				makeBoundConsistent(null);
			}
		}
	}
	
	/**
	 * Sets the maximum allowed duration
	 * @param durMax new maximum for duration
     * @param notify whether or not to notify listeners of the change
	 * @throws PropagationFailureException
	 */
	public void setDurationMax(int durMax, boolean notify) throws PropagationFailureException {
		if (duration.getMax()>durMax) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.duration.add(duration.removeStartingAfterGetDelta(durMax));
				makeDomainConsistent(delta);
				notifyDelta(delta);
			}
			else {
				duration.removeStartingAfter(durMax);
				makeDomainConsistent(null);
			}
		}
	}
	
	protected void removeDurationRange(int min, int max, boolean notify) throws PropagationFailureException {
		if ((duration.getMax()>max)||(duration.getMin()<min)) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.duration.add(duration.removeGetDelta(min, max));
				makeDomainConsistent(delta);
				notifyDelta(delta);
			}
			else {
				duration.remove(min, max);
				makeDomainConsistent(null);
			}
		}
	}
	
	
	/**
	 * Sets the minimum allowed duration
	 * @param durMin new minimum for duration
	 * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setDurationMin(int durMin, boolean notify) throws PropagationFailureException {
		if (duration.getMin()<durMin) {
			if (notify) {
				ActDelta delta = new ActDelta();
				delta.duration.add(duration.removeEndingBeforeGetDelta(durMin));
				makeDomainConsistent(delta);
				notifyDelta(delta);
			}
			else {
				duration.removeEndingBefore(durMin);
				makeDomainConsistent(null);
			}
		}
	}
	
	/**
	 * Sets the maximum and minimum allowed duration
	 * @param durMin new minimum for duration
	 * @param durMax new maximum for duration
	 * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setDurationRange(int durMin, int durMax, boolean notify) throws PropagationFailureException {
		//This will have no affect in this case, so we return
		if ((duration.getMin()>=durMin) && (duration.getMax()<=durMax)) {
			return;
		}
		int currentMin = duration.getMin();
		int currentMax = duration.getMax();
		if (durMin>currentMin){
			setDurationMin(durMin, notify);
		}
		if (durMax<currentMax){
			setDurationMax(durMax,notify);
		}
	}
	
	/**
	 * Sets the duration to a single value
	 * @param duration the value to set the duration to
	 * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException	
	 */
	public void setDuration(int duration, boolean notify) throws PropagationFailureException {
		setDurationRange(duration, duration, notify);
	}
	
	/**
	 * This method will adjust start, end, and duration times to account for a resource
     * that is available during these specific times
	 * This method is at least bounds consistent.
	 * @param timeline
	 * @param notify whether or not to notify listeners of the change
     * @throws PropagationFailureException
	 */
	public void setTimeline(IntIntervalSet timeline, boolean notify) throws PropagationFailureException {
		//TODO:  Figure out a way to do all this behavior before notifying the listeners
		
        //Take care of the bounds
		setEarliestStartTime(timeline.getMin(), notify);
		setLatestEndTime(timeline.getMax(), notify);
		int durMin = getDurationMin();
		setLatestStartTime(timeline.getMax()-(durMin-1), notify);
		setEarliestEndTime(timeline.getMin()+(durMin-1), notify);
		
		//Now we take care of any and all gaps in the middle
		//To make only bounds consistent, remove this section of code
		int index = timeline.getFirstIntervalIndex();
		if (index<0) {
			//In this case it is an empty timeline, so we remove all values from this
			removeStartTimes(getEarliestStartTime(),getLatestStartTime(), notify);
			return;
		}
		int nextIndex = timeline.getNextIntervalIndex(index);
		while ((index>=0)&&(nextIndex>=0)) {
			int gapStart = timeline.getMax(index)+1;
			int gapEnd = timeline.getMin(nextIndex)-1;
			
			removeStartTimes(gapStart-(durMin-1),gapEnd, notify);
			removeEndTimes(gapStart,gapEnd+(durMin-1), notify);
			
			index= nextIndex;
			nextIndex = timeline.getNextIntervalIndex(nextIndex);
		}
	}
	
    /**
     * Helper method called to ensure 'domain consistency' (based on all values)
     */
	private void makeDomainConsistent(ActDelta delta) throws PropagationFailureException {
		makeBoundConsistent(delta);
		int durationMin = duration.getMin();
		int durationMax = duration.getMax();
		//Iterate through all the intervals of both end and start vals
		//and try to remove all applicable gaps.
		int index  = startTimes.getFirstIntervalIndex();
		while(startTimes.getNextIntervalIndex(index)>=0) {
			//identify the gap
			int gapStart = startTimes.getMax(index);
			int gapEnd = startTimes.getMin(startTimes.getNextIntervalIndex(index));
			if ((gapStart+(durationMax-1))<=(gapEnd+(durationMin-1))) {
				if (delta!=null) {
					delta.endTimes.add(endTimes.removeGetDelta((gapStart+(durationMax-1)),(gapEnd+(durationMin-1))));
				}
				else {
					endTimes.remove((gapStart+(durationMax-1)),(gapEnd+(durationMin-1)));
				}
			}
		}
		index  = endTimes.getFirstIntervalIndex();
		while(endTimes.getNextIntervalIndex(index)>=0) {
			//identify the gap
			int gapStart = endTimes.getMax(index);
			int gapEnd = endTimes.getMin(endTimes.getNextIntervalIndex(index));
			if ((gapStart-(durationMin-1))<=(gapEnd-(durationMax-1))) {
				if (delta!=null) {
					delta.startTimes.add(startTimes.removeGetDelta((gapStart-(durationMin-1)),(gapEnd-(durationMax-1))));
				}
				else {
					startTimes.remove((gapStart-(durationMin-1)),(gapEnd-(durationMax-1)));
				}
			}
		}
	}
	
	/**
	 * Returns the maximum duration
	 * @return max duration
	 */
	public int getDurationMax(){
		return duration.getMax();
	}
	
	/**
	 * Returns the minimum duration
	 * @return min duration
	 */
	public int getDurationMin(){
		return duration.getMin();
	}
	
	/**
	 * Removes a single value from the start times of this domain
     * @param n time to remove
     * @param notify whether or not to notify the listener
	 * @throws PropagationFailureException
	 */
	public void removeStartTime(int n, boolean notify) throws PropagationFailureException {
		removeStartTimes(n, n, notify);
	}
	
	/**
	 * Restricts the start time to a single value
	 * @param n time to remove
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void setStartTime(int n, boolean notify) throws PropagationFailureException {
		setStartTimeRange(n,n, notify);
	}
	
	/**
	 * Removes a range of values from start times of this domain
	 * @param start start value of the range to be removed
	 * @param end end value of the range to be removed
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void removeStartTimes(int start, int end, boolean notify) throws PropagationFailureException {
		if (notify) {
			ActDelta delta = new ActDelta();
			//    	This will have no affect, so we return
	    	if (this.startTimes.isIntervalEmpty(start,end)) {
	    		return;
	    	}
			delta.startTimes.add(startTimes.removeGetDelta(start,end));
			int durMin = duration.getMin();
			int durMax = duration.getMax();
			if ((start+(durMax-1))<=(end+(durMin-1))) {
				delta.endTimes.add(endTimes.removeGetDelta((start+(durMax-1)),(end+(durMin-1))));
			}
			notifyDelta(delta);
		}
		else {
			//    	This will have no affect, so we return
	    	if (this.startTimes.isIntervalEmpty(start,end)) {
	    		return;
	    	}
			startTimes.remove(start,end);
			int durMin = duration.getMin();
			int durMax = duration.getMax();
			if ((start+(durMax-1))<=(end+(durMin-1))) {
				endTimes.remove((start+(durMax-1)),(end+(durMin-1)));
			}
		}
	}
	
	/**
	 * Removes a set of times from the start times of this domain
	 * @param times set of times to be removed
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
    public void removeStartTimes(IntIntervalSet times, boolean notify) throws PropagationFailureException {
    	if (notify) {
    		IntIntervalSet delta = startTimes.removeAllGetDelta(times);
    		notifyStartTimeDelta(delta);
    	}
    	else {
    		startTimes.removeAll(times);
    	}
    }

	/**
	 * Removes a set of times from the end times of this domain
	 * @param times set of times to be removed
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
    public void removeEndTimes(IntIntervalSet times, boolean notify) throws PropagationFailureException {
    	if (notify) {
    		IntIntervalSet delta = endTimes.removeAllGetDelta(times);
    		notifyEndTimeDelta(delta);
    	}
    	else {
    		endTimes.removeAll(times);
    	}
    }
    
	/**
	 * Removes a set of times from the duration of this domain
	 * @param times set of times to be removed
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
    public void removeDuration(IntIntervalSet times, boolean notify) throws PropagationFailureException {
    	if (notify) {
    		IntIntervalSet delta = duration.removeAllGetDelta(times);
    		notifyDurationDelta(delta);
    	}
    	else {
    		duration.removeAll(times);
    	}
    }
    
	/**
	 * Removes times specified in the ActDelta from start, end, and duration times
	 * @param delta collection of times to be removed
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
    public void removeDelta(ActDelta delta, boolean notify) throws PropagationFailureException {
    	if (notify) {
	    	ActDelta newDelta = new ActDelta();
	    	newDelta.startTimes=startTimes.removeAllGetDelta(delta.startTimes);
	    	newDelta.endTimes=endTimes.removeAllGetDelta(delta.endTimes);
	    	newDelta.duration=duration.removeAllGetDelta(delta.duration);
	    	notifyDelta(newDelta);
    	}
    	else {
    		startTimes.removeAll(delta.startTimes);
	    	endTimes.removeAll(delta.endTimes);
	    	duration.removeAll(delta.duration);
    	}
    }
	
	/**
	 * Restricts the start times of this domain to a range of values
	 * @param start start value of the range to restrict start times to
	 * @param end end value of the range to restrict start times to
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void setStartTimeRange(int start, int end, boolean notify) throws PropagationFailureException {
		setEarliestStartTime(start, notify);
		setLatestStartTime(end, notify);
	}
	
	/**
	 * Removes a single value from the end times of this domain
	 * @param n time to remove
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void removeEndTime(int n, boolean notify) throws PropagationFailureException {
		removeEndTimes(n,n, notify);
	}
	
	/**
	 * Restricts the end times of this domain to a single value
	 * @param n time to remove
     * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void setEndTime(int n, boolean notify) throws PropagationFailureException {
		setEndTimeRange(n,n, notify);
	}
	
	/**
	 * Removes a range of values from the end times of this domain
	 * @param start start value of the range to remove from end times 
	 * @param end end value of the range to remove from end times 
	 * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void removeEndTimes(int start, int end, boolean notify) throws PropagationFailureException {
		if (notify) {
			ActDelta delta = new ActDelta();
			//    	This will have no affect, so we return
	    	if (this.endTimes.isIntervalEmpty(start,end)) {
	    		return;
	    	}
			delta.endTimes.add(endTimes.removeGetDelta(start,end));
			int durMin = duration.getMin();
			int durMax = duration.getMax();
			if ((start-(durMin-1))<=(end-(durMax-1))) {
				delta.startTimes.add(startTimes.removeGetDelta((start-(durMin-1)),(end-(durMax-1))));
			}
			notifyDelta(delta);
		}
		else {
			//    	This will have no affect, so we return
	    	if (this.endTimes.isIntervalEmpty(start,end)) {
	    		return;
	    	}
			endTimes.remove(start,end);
			int durMin = duration.getMin();
			int durMax = duration.getMax();
			if ((start-(durMin-1))<=(end-(durMax-1))) {
				startTimes.remove((start-(durMin-1)),(end-(durMax-1)));
			}
		}
	}
	
	/**
	 * Restricts the end times of this domain to a range of values
	 * @param start start value of the range to restrict end times to
	 * @param end end value of the range to restrict end times to
	 * @param notify whether or not to notify the listener
     * @throws PropagationFailureException
	 */
	public void setEndTimeRange(int start, int end, boolean notify) throws PropagationFailureException {
		//This action will be inaffective, thus we return
		if ((endTimes.getMin()>=start)&&(endTimes.getMax()<=end)) {
			return;
		}
		setEarliestEndTime(start, notify);
		setLatestEndTime(end, notify);
	}
	
	// javadoc is inherited
	public void clearDelta() {
		// NOTE: current implementation does not support deltas during the propagation process
	}
	
	// javadoc is inherited
	public Object getDomainState() {
		HashMap stateInfo = new HashMap();
		stateInfo.put("start", startTimes.clone());
		stateInfo.put("end", endTimes.clone());
		stateInfo.put("dur", duration.clone());
		
		return stateInfo;
	}
	
	// javadoc is inherited
	public void restoreDomainState(Object state){
		HashMap stateInfo = (HashMap) state;
		startTimes = (CPSIntIntervalSet) ((CPSIntIntervalSet) stateInfo.get("start")).clone();
		endTimes = (CPSIntIntervalSet) ((CPSIntIntervalSet) stateInfo.get("end")).clone();
		duration= (CPSIntIntervalSet)((CPSIntIntervalSet) stateInfo.get("dur")).clone();
	}
	
	//returns if there are no valid start and or end times
	protected boolean isEmpty(){
		return ((startTimes.size()==0)||(endTimes.size()==0));
	}
	
	// javadoc is inherited
	public boolean choicePointStackSet() {
		if(cps!=null)
			return true;
		return false;
	}
	
	// javadoc is inherited
	public void setChoicePointStack(ChoicePointStack cps) {
		if (cps == null) return;
		
		if (cps!=null) {
			this.cps = cps;
			((CPSIntIntervalSet)startTimes).setChoicePointStack(cps);
			((CPSIntIntervalSet)endTimes).setChoicePointStack(cps);
			((CPSIntIntervalSet)duration).setChoicePointStack(cps);
		}
	}
	
}
