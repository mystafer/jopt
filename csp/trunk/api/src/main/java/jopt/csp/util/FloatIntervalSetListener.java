package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of intervals from a set
 */
public interface FloatIntervalSetListener {
	public void intervalAdded(int callback, float start, float end);
    public void intervalRemoved(int callback, float start, float end);
}
