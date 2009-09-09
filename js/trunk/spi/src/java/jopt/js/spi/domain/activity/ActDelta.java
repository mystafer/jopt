package jopt.js.spi.domain.activity;

import jopt.csp.util.IntIntervalSet;

/**
 * A class to maintain changes to all aspects of an activity;
 * thus, it records changes to startTimes, duration, and endTimes.
 */
public class ActDelta {
	public IntIntervalSet startTimes;
	public IntIntervalSet duration;
	public IntIntervalSet endTimes;
	
	/**
	 * Creates a Act Delta object that simply wraps the three sub sets
	 */
	public ActDelta() {
		startTimes = new IntIntervalSet();
		duration = new IntIntervalSet();
		endTimes = new IntIntervalSet();
	}
	
	/**
	 * Adds the elements of this delta with another
	 * @param delta
	 */
	public void add(ActDelta delta) {
		this.startTimes.add(delta.startTimes);
		this.endTimes.add(delta.endTimes);
		this.duration.add(delta.duration);
	}
	
	/**
	 * Checks to see if the delta sets are empty or not
	 * @return true if all subsets are empty
	 */
	public boolean isEmpty() {
		return (startTimes.isEmpty()&&endTimes.isEmpty()&&duration.isEmpty());
	}
	
	//javadoc inherited
	public String toString() {
		return startTimes.toString()+"\n"+endTimes.toString()+"\n"+duration.toString();
	}
}
