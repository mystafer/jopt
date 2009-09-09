package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of intervals from a set
 */
public interface DoubleIntervalSetListener {
	public void intervalAdded(int callback, double start, double end);
    public void intervalRemoved(int callback, double start, double end);
}
