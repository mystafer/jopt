package jopt.csp.util;



/**
 * Interface implemented by all numeric sets
 */
public interface NumSet {
    /**
     * Returns the size of the set. If any real intervals
     * (e.g. float intervals or double intervals) are in the set,
     * returns Integer.MAX_VALUE.
     */
    public int size();

    /**
     * Removes all values from the set. Set will be empty after this method is run.
     */
    public void clear();

    /**
     * Creates a duplicate of this set
     */
    public Object clone();

    /**
     * Adds all values in another set to this set.  The set will become
     * the union of the two sets.
     */
    public void addAll(NumSet set);

    /**
     * Removes all values in a given set
     */
    public void removeAll(NumSet set);

    /**
     * Retains only given values in a given set.  The set will become
     * the intersection of the the two sets. 
     */
    public void retainAll(NumSet set);

    /** 
     * Returns an iterator containing all values in the set
     */
    public NumberIterator values();
}
