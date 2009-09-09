package jopt.js.spi.domain.activity;

import jopt.csp.spi.arcalgorithm.domain.AbstractDomain;
import jopt.csp.spi.arcalgorithm.domain.IntDomain;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.listener.ActivityDomainListener;

/**
 * IntDomain that allows users to interact with the entities of an activity (start time, end time, duration)
 * as if they were themselves IntDomain objects.  Prevents having to create arcs from the activity node
 * to nodes representing start times, end times, and durations of the associated activity; instead, the
 * times can be modified directly.
 * 
 * @author James Boerkoel
 */
public class IntIntervalActivityDomain extends AbstractDomain implements IntDomain, ActivityDomainListener {

	//Activity domain
	private ActivityDomain actDom;
	private ChoicePointStack cps;
	private int type;
	//constants to denote types
	public static final int START = 0;
	public static final int END = 1;
	public static final int DURATION = 2;
	
	/**
	 * Constructs an IntIntervalActivity
	 * @param actDom the activity domain which is being abstracted
	 * @param type describes which entity is being abstracted (START, END, DURATION)
	 */
	public IntIntervalActivityDomain (ActivityDomain actDom, int type) {
		this.actDom = actDom;
		actDom.addActivityListener(this);
		this.type = type;
	}
	
    /**
     * Returns true if the choicepoint stack has been set
     */
    public boolean choicePointStackSet() {
    	return (cps!=null);
    }
    
    /**
     * Sets the choicepoint stack associated with this data source object
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	this.cps = cps;
    }
    
	/**
	 * Returns maximum value of the domain
	 */
	public int getMax() {
		switch (type) {
			case START:
				return actDom.getLatestStartTime();
			case END:
				return actDom.getLatestEndTime();
			default:
				return actDom.getDurationMax();	
		}
	}

	/**
	 * Returns minimum value of the domain
	 */
	public int getMin() {
		switch (type) {
			case START:
				return actDom.getEarliestStartTime();
			case END:
				return actDom.getEarliestEndTime();
			default:
				return actDom.getDurationMin();	
		}
	}

	/**
	 * Returns true if value is in the domain
	 */
	public boolean isInDomain(int val) {
		switch (type) {
			case START:
				return actDom.isPossibleStartTime(val);
			case END:
				return actDom.isPossibleEndTime(val);
			default:
				return actDom.isPossibleDuration(val);
		}
	}

	/**
	 * Attempts to set the maximum value of the domain.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMax(int val) throws PropagationFailureException {
		switch (type) {
			case START:
				actDom.setLatestStartTime(val);
				break;
			case END:
				actDom.setLatestEndTime(val);
				break;
			default:
				actDom.setDurationMax(val);
				break;
		}
	}

	/**
	 * Attempts to set the minimum value of the domain.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setMin(int val) throws PropagationFailureException{
		switch (type) {
			case START:
				actDom.setEarliestStartTime(val);
				break;
			case END:
				actDom.setEarliestEndTime(val);
				break;
			default:
				actDom.setDurationMin(val);
				break;
		}
	}

	/**
	 * Attempts to reduce domain to a single value.
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setValue(int val) throws PropagationFailureException {
		switch (type) {
			case START:
				actDom.setStartTime(val);
				break;
			case END:
				actDom.setEndTime(val);
				break;
			default:
				actDom.setDuration(val);
				break;
		}
	}

	/**
	 * Attempts to remove a single value from the domain
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeValue(int val) throws PropagationFailureException {
		switch (type) {
			case START:
				actDom.removeStartTime(val);
				break;
			case END:
				actDom.removeEndTime(val);
				break;
			default:
				actDom.removeDurationRange(val,val);
				break;
		}
	}

	/**
	 * Attempts to reduce domain to within a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void setRange(int start, int end) throws PropagationFailureException{
		switch (type) {
			case START:
				actDom.setStartTimes(start,end);
				break;
			case END:
				actDom.setEndTimes(start,end);
				break;
			default:
				actDom.setDurationRange(start,end);
				break;
		}
	}

	/**
	 * Attempts to reduce domain by removing a range of values
	 *
	 * @throws PropagationFailureException      If domain is empty
	 */
	public void removeRange(int start, int end)
			throws PropagationFailureException {
		switch (type) {
			case START:
				actDom.removeStartTimes(start,end);
				break;
			case END:
				actDom.removeEndTimes(start,end);
				break;
			default:
				actDom.removeDurationRange(start,end);
				break;
		}
	}

	/**
	 * Returns the next higher value in the domain or current value if none
	 * exists
	 */
	public int getNextHigher(int val) {
		switch (type) {
			case START:
				return actDom.getNextStartTime(val);
			case END:
				return actDom.getNextEndTime(val);
			default:
				return actDom.getNextDuration(val);
		}
	}

	/**
	 * Returns the next lower value in the domain or current value if none
	 * exists
	 */
	public int getNextLower(int val) {
		switch (type) {
			case START:
				return actDom.getPrevStartTime(val);
			case END:
				return actDom.getPrevEndTime(val);
			default:
				return actDom.getPrevDuration(val);
		}
	}
	
    /**
     * Returns size of domain
     */
    public int getSize() {
    	switch (type) {
			case START:
				return actDom.getNumStartTimes();
			case END:
				return actDom.getNumEndTimes();
			default:
				return actDom.getDurationSize();
		}
    }
    
    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        // TODO:  Add this functionality
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet vals) throws PropagationFailureException {
        // TODO: add this functionality
    }

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        // TODO: add this functionality
    	return null;
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
    	throw new UnsupportedOperationException();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet() {
        // TODO: add this functionality
    	return null;
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet(){
    	throw new UnsupportedOperationException();
    }
    
    public boolean isBound() {
    	return (getSize()==1);
    }
    
    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * Note: if a particular implementation does not support
     * the storage of state information, this simply returns null
     * 
     * @return an Object representing the current state information for this domain
     */
    public Object getDomainState() {
    	return actDom.getDomainState();
    }
 
    /**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * Note: if a particular implementation does not support
     * the restoration of state information, this method does nothing
     * 
     * @param state The state information to which this domain should be set
     */
    public void restoreDomainState(Object state) {
    	actDom.restoreDomainState(state);
    }
    
    /**
     * Returns a copy of this domain
     */
    public Object clone() {
        // TODO add this functionality correctly (ie needs to create a new copy of start time interval etc)
    	return null;
    }
    
    /**
     * Returns true if last operation caused a change to occur in the domain
     */
    public boolean changed() {
    	throw new UnsupportedOperationException();
    }

    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
    	//Delta is not set, so we ignore these calls
    }
    
    public void domainReductionAction(int entityChanged, int start, int end) throws PropagationFailureException{
    	if (entityChanged==type) {
    		notifyDomainChange();
    	}
    }
    
    public void fireAnyAction() throws PropagationFailureException{
    	notifyDomainChange();
    }
}
