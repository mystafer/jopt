package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of values from an integer set
 */
public interface IntSparseSetListener {
	public void valueAdded(int callback, int val);
    public void valueRemoved(int callback, int val);
}
