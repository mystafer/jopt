package jopt.csp.spi.search.limit;

import jopt.csp.search.SearchLimit;
import jopt.csp.search.SearchNode;

/**
 * Search limit that will stop activating search nodes after a configurable
 * number of milliseconds
 */
public class TimeLimit implements SearchLimit {

	private long startTime;
	private long timeLimitMs;
	
	/**
	 * Creates a new time limit search limiter
	 * 
	 * @param timeLimitMs	Time in milliseconds to limit search
	 */
	public TimeLimit(long timeLimitMs) {
		this.timeLimitMs = timeLimitMs;
	}
	
	// javadoc inherited
	public void init(SearchNode node) {
		this.startTime = System.currentTimeMillis();
	}
	
	// javadoc inherited
	public boolean isOkToContinue(SearchNode node) {
		long duration = System.currentTimeMillis() - startTime;
		return duration < timeLimitMs;
	}
	
	public Object clone() {
		return new TimeLimit(timeLimitMs);
	}

}
