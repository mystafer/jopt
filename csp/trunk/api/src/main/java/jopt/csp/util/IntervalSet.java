package jopt.csp.util;

/**
 * Interface implemented by sets based on intervals
 */
public interface IntervalSet {
	/**
     * Returns an iterator for intervals contained in set
	 */
    public IntervalIterator intervals();
}
