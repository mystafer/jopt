package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of values from a long set
 */
public interface LongSparseSetListener {
	public void valueAdded(int callback, long val);
    public void valueRemoved(int callback, long val);
}
