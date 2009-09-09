package jopt.csp.spi.search.limit;

import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNode;

/**
 * Search limit that will stop activating search nodes after a configurable
 * fixed time based on the value System.currentTimeMillis()
 */
public class DeadlineLimit implements SearchLimit {

	private long endTime;
	
	/**
	 * Creates a new deadline limit search limiter
	 * 
	 * @param endTime	Time in milliseconds to end search
	 */
	public DeadlineLimit(long endTime) {
		this.endTime = endTime;
	}
	
	// javadoc inherited
	public void init(SearchNode node) {
	}
	
	// javadoc inherited
	public boolean isOkToContinue(SearchNode node) {
		return endTime > System.currentTimeMillis();
	}
	
	public Object clone() {
		return new DeadlineLimit(endTime);
	}

}
