package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of values from a float set
 */
public interface FloatSparseSetListener {
	public void valueAdded(int callback, float val);
    public void valueRemoved(int callback, float val);
}
