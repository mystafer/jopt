package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of intervals from a set
 */
public interface IntIntervalSetListener {
	public void intervalAdded(int callback, int start, int end);
    public void intervalRemoved(int callback, int start, int end);
}
