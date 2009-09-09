package jopt.csp.util;

/**
 * Iterator for numbers
 */
public interface IntervalIterator {
	/**
     * Returns true if another value exists
	 */
    public boolean hasNext();
    
    /**
     * Returns start of next int interval
     */
    public int nextInt();
    
    /**
     * Returns start of next long interval
     */
    public long nextLong();
    
    /**
     * Returns start of next float interval
     */
    public float nextFloat();
    
    /**
     * Returns start of next double interval
     */
    public double nextDouble();
    
    /**
     * Returns end of current int interval
     */
    public int endInt();
    
    /**
     * Returns end of current long interval
     */
    public long endLong();
    
    /**
     * Returns end of current float interval
     */
    public float endFloat();
    
    /**
     * Returns end of current double interval
     */
    public double endDouble();
}
