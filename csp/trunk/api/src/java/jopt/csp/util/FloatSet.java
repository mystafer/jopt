package jopt.csp.util;


/**
 * Base class for float sets
 */
public abstract class FloatSet implements NumSet {

    /**
     * Removes all values from the set
     */
    public abstract void clear();

    /**
     * Returns minimum value for set
     */
    public abstract float getMin();

    /**
     * Returns maximum value for set
     */
    public abstract float getMax();

    /**
     *  Returns true if value is contained in set
     */
    public abstract boolean contains(float val);

    /**
     * Adds a value to set
     */
    public abstract void add(float val);

    /**
     * Adds a range of values to set
     */
    public abstract void add(float start, float end);

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
                add(intervalIter.nextFloat(), intervalIter.endFloat());
            }
        }
        
        // is a sparse set
        else {
            // loop over values in set
            NumberIterator valIter = set.values();
            
            // remove individual values
            while (valIter.hasNext()) {
                valIter.next();
                add(valIter.floatValue());
            }
        }
    }
    
    /**
     * Removes a value from the set
     */
    public abstract void remove(float val);

    /**
     * Removes a range of values from the set
     */
    public abstract void remove(float start, float end);

    /**
     * Removes all values above given value
     */
    public abstract  void removeStartingAfter(float val);

    /**
     * Removes all values above and including given value
     */
    public abstract void removeStartingFrom(float val);

    /**
     * Removes all values below given value
     */
    public abstract void removeEndingBefore(float val);
    
    /**
     * Removes all values below and including given value
     */
    public abstract void removeEndingAt(float val);
    
    /**
     * Removes all values in a given set
     */
    public final void removeAll(NumSet set) {
        // check if set is an interval set
        if (set instanceof IntervalSet) {
            // loop over intervals in set
            IntervalSet iset = (IntervalSet) set;
            IntervalIterator intervalIter = iset.intervals();
            
            // remove values from each interval
            while (intervalIter.hasNext()) {
                remove(intervalIter.nextFloat(), intervalIter.endFloat());
            }
        }
        
        // is a sparse set
        else {
            // loop over values in set
            NumberIterator valIter = set.values();
            
            // remove individual values
            while (valIter.hasNext()) {
                valIter.next();
                remove(valIter.floatValue());
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
    public abstract float getNextHigher(float val);

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public abstract float getNextLower(float val);
    
    /**
     * Creates a duplicate of this set
     */
    public abstract Object clone();
    
    /**
     * Releases an object that is not needed for pooling purposes
     */
    protected void releaseObject(Object o){}
}
