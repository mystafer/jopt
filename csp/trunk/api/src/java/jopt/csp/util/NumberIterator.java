package jopt.csp.util;

/**
 * Iterator for numbers
 */
public abstract class NumberIterator extends Number {
	/**
     * Returns true if another value exists
	 */
    public abstract boolean hasNext();
    
    /**
     * Returns next value in list
     */
    public abstract Number next();
}
