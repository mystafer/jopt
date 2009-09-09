package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of intervals from a set
 */
public interface LongIntervalSetListener {
	public void intervalAdded(int callback, long start, long end);
    public void intervalRemoved(int callback, long start, long end);
}
