package jopt.csp.util;


/**
 * Base class for double sets
 */
public abstract class DoubleSet implements NumSet {

    /**
     * Removes all values from the set
     */
    public abstract void clear();

    /**
     * Returns minimum value for set
     */
    public abstract double getMin();

    /**
     * Returns maximum value for set
     */
    public abstract double getMax();

    /**
     *  Returns true if value is contained in set
     */
    public abstract boolean contains(double val);

    /**
     * Adds a value to set
     */
    public abstract void add(double val);

    /**
     * Adds a range of values to set
     */
    public abstract void add(double start, double end);

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
                add(intervalIter.nextDouble(), intervalIter.endDouble());
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
    public abstract void remove(double val);

    /**
     * Removes a range of values from the set
     */
    public abstract void remove(double start, double end);

    /**
     * Removes all values above given value
     */
    public abstract void removeStartingAfter(double val);

    /**
     * Removes all values above and including given value
     */
    public abstract void removeStartingFrom(double val);

    /**
     * Removes all values below given value
     */
    public abstract void removeEndingBefore(double val);
    
    /**
     * Removes all values below and including given value
     */
    public abstract void removeEndingAt(double val);
    
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
                remove(intervalIter.nextDouble(), intervalIter.endDouble());
            }
        }
        
        // is a sparse set
        else {
            // loop over values in set
            NumberIterator valIter = set.values();
            
            // remove individual values
            while (valIter.hasNext()) {
                valIter.next();
                remove(valIter.doubleValue());
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
    public abstract double getNextHigher(double val);

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public abstract double getNextLower(double val);
    
    /**
     * Creates a duplicate of this set
     */
    public abstract Object clone();
    
    /**
     * Releases an object that is not needed for pooling purposes
     */
    protected void releaseObject(Object o){}
}
