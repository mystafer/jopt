package jopt.csp.util;


/**
 * Base class for long sets
 */
public abstract class LongSet implements NumSet {

    /**
     *  Creates a new set
     */
    public LongSet() {
    }
    
    /**
     * Removes all values from the set
     */
    public abstract void clear();

    /**
     * Returns minimum value for set
     */
    public abstract long getMin();

    /**
     * Returns maximum value for set
     */
    public abstract long getMax();

    /**
     *  Returns true if value is contained in set
     */
    public abstract boolean contains(long val);

    /**
     * Adds a value to set
     */
    public abstract void add(long val);

    /**
     * Adds a range of values to set
     */
    public abstract void add(long start, long end);

    /**
     * Adds all values to set
     */
    public final void addAll(NumSet set) {
        // check if set is an interval set
        if (set instanceof IntervalSet) {
            // loop over intervals in set
            IntervalSet iset = (IntervalSet) set;
            IntervalIterator intervalIter = iset.intervals();
            
            // remove values from each interval
            while (intervalIter.hasNext()) {
                add(intervalIter.nextLong(), intervalIter.endLong());
            }
        }
        
        // is a sparse set
        else {
            // loop over values in set
            NumberIterator valIter = set.values();
            
            // remove individual values
            while (valIter.hasNext()) {
                valIter.next();
                add(valIter.longValue());
            }
        }
    }
    
    /**
     * Removes a value from the set
     */
    public abstract void remove(long val);

    /**
     * Removes a range of values from the set
     */
    public abstract void remove(long start, long end);

    /**
     * Removes all values above and including given value
     */
    public final void removeStartingAfter(long val) {
        if (val==Long.MAX_VALUE) return;
        removeStartingFrom(val+1);
    }

    /**
     * Removes all values above and including given value
     */
    public abstract void removeStartingFrom(long val);

    /**
     * Removes all values below and including given value
     */
    public final void removeEndingBefore(long val) {
        if (val==Long.MIN_VALUE) return;
        removeEndingAt(val-1);
    }
    
    /**
     * Removes all values below and including given value
     */
    public abstract void removeEndingAt(long val);
    
    /**
     * Removes all values in a given set
     */
    public final void removeAll(NumSet set) {
        if (size()==0) return;
        
        // check if set is an interval set
        if (set instanceof IntervalSet) {
            // loop over intervals in set
            IntervalSet iset = (IntervalSet) set;
            IntervalIterator intervalIter = iset.intervals();
            
            // remove values from each interval
            while (intervalIter.hasNext()) {
                remove(intervalIter.nextLong(), intervalIter.endLong());
            }
        }
        
        // is a sparse set
        else {
            // loop over values in set
            NumberIterator valIter = set.values();
            
            // remove individual values
            while (valIter.hasNext()) {
                valIter.next();
                remove(valIter.longValue());
            }
        }
        
    }
    
    /**
     * Retains only given values in a given set
     */
    public final void retainAll(NumSet set) {
        if (size()==0) return;
        
        // create a working set
        NumSet working = (NumSet) clone();
        
        // calculate values that should be removed from this set
        working.removeAll(set);
        
        // remove values no longer necessary
        removeAll(working);
        
        // release working set
        releaseObject(working);
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public abstract long getNextHigher(long val);

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public abstract long getNextLower(long val);
    
    /**
     * Creates a duplicate of this set
     */
    public abstract Object clone();
    
    /**
     * Releases an object that is not needed for pooling purposes
     */
    protected void releaseObject(Object o){}
}
