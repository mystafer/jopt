package jopt.csp.util;

/**
 * Interface that can be implemented by a class that wishes to be notified of addition
 * and removal of values from a double set
 */
public interface DoubleSparseSetListener {
	public void valueAdded(int callback, double val);
    public void valueRemoved(int callback, double val);
}
