package jopt.csp.util;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongIterator;

/**
 * A sorted set of long values
 */
public class LongSparseSet extends LongSet {

    protected ArrayLongList values;
    protected LongSparseSetListener listener;
    protected int callback;

    /**
     *  Creates a new set
     */
    public LongSparseSet() {
        values = new ArrayLongList();
    }
    
    /**
     * Cloning constructor
     */
    private LongSparseSet(LongSparseSet set) {
        values = new ArrayLongList(set.values);
    }
    
    /**
     * Returns the size of the set
     */
    public int size() {
        return values.size();
    }

    /**
     * Removes all values from the set
     */
    public void clear() {
        while (values.size()>0) {
            long val = values.removeElementAt(0);
            
            // notify listener
            if (listener!=null)
                listener.valueRemoved(callback, val);
        }
    }

    /**
     * Returns minimum value for set
     */
    public long getMin() {
        if (values.size()==0) return Long.MAX_VALUE;
        return values.get(0);
    }

    /**
     * Returns maximum value for set
     */
    public long getMax() {
        if (values.size()==0) return Long.MIN_VALUE;
        return values.get(values.size()-1);
    }

    /**
     *  Returns index of a value in the list
     */
    private int indexOfValue(long val) {
        if (values.size()==0) return -1;
        
        // initialize range of indices that might contain
        // value to entire list
        int lowIdx = 0;
        int highIdx = values.size()-1;
        
        // loop through indices and locate value
        while (lowIdx <= highIdx) {
            int idx = (lowIdx+highIdx)/2;
            long v = values.get(idx);
            
            // check if values are equivalent
            if (v==val) return idx;
            
            // if value is below, reduce high index to be index below current median
            if (val < v) highIdx = idx - 1;
            
            // otherwise value must be above
            else lowIdx = idx + 1;
        }
        
        // if value wasn't located, return a negative value
        // representing locating where value should be inserted in list
        return -(lowIdx+1);
    }

    /**
     *  Returns true if value is contained in set
     */
    public boolean contains(long val) {
        return indexOfValue(val) >= 0;
    }

    /**
     * Adds a value to set
     */
    public void add(long val) {
        // determine index to insert value
        int idx = indexOfValue(val);
        
        // check if value is already contained in list
        if (idx < 0) {
            // convert returned value
            // to location new value should be inserted
            idx = -idx - 1;
            values.add(idx, val);
            
            // notify listener
            if (listener!=null)
                listener.valueAdded(callback, val);
        }
    }

    /**
     * Adds a range of values to set
     */
    public void add(long start, long end) {
        for (long i=start; i<=end; i++)
            add(i);
    }

    /**
     * Removes a value from the set
     */
    public void remove(long val) {
        // determine index of value
        int idx = indexOfValue(val);
        
        // check if value is in list
        if (idx >= 0) {
            values.removeElementAt(idx);
            
            // notify listener
            if (listener!=null)
                listener.valueRemoved(callback, val);
        }
    }

    /**
     * Removes a range of values from the set
     */
    public void remove(long start, long end) {
        if (values.size()==0) return;
        
        for (long i=start; i<=end; i++)
            remove(i);
    }

    /**
     * Removes all values above and including given value
     */
    public void removeStartingFrom(long val) {
        if (values.size()==0) return;
        
        // remove range of values between val and max
        long max = values.get(values.size()-1);
        remove(val, max);
    }

    /**
     * Removes all values below and including given value
     */
    public void removeEndingAt(long val) {
        if (values.size()==0) return;
        
        // remove range of values between min and val 
        long min = values.get(0);
        remove(min, val);
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public long getNextHigher(long val) {
        if (values.size()==0) return val;
        
        // if value is before minimum value, return min
        long min = values.get(0);
        if (val < min)
            return min;
        
        // if value is beyond max value, return value passed
        long max = values.get(values.size()-1);
        if (val >= max)
            return val;
        
        // retrieve index of next value
        long next = val+1;
        int idx = indexOfValue(next);
        
        // if value exists, return it
        if (idx>=0) return next;
        
        // locate next value
        idx = -idx - 1;
        return values.get(idx);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public long getNextLower(long val) {
        if (values.size()==0) return val;
        
        // if value is beyond max value, return max
        long max = values.get(values.size()-1);
        if (val > max)
            return max;
        
        // if value is before minimum value, return value passed
        long min = values.get(0);
        if (val <= min)
            return val;
        
        // retrieve index of next value
        long prev = val-1;
        int idx = indexOfValue(prev);
        
        // if value exists, return it
        if (idx>=0) return prev;
        
        // locate interval with previous value
        idx = -idx - 1;
        return values.get(idx);
    }

    /**
     * Sets listener for set to be notified of changes
     */
    public void setListener(LongSparseSetListener listener, int callback) {
        this.listener = listener;
        this.callback = callback;
    }

    /**
     * Returns listener that is currently assigned to set
     */
    public LongSparseSetListener getListener() {
        return listener;
    }
    
    /**
     * Creates a duplicate of this set
     */
    public Object clone() {
        return new LongSparseSet(this);
    }
    
    /** 
     * returns an iterator containing all numbers in the set
     */
    public NumberIterator values() {
        return new LongSparseIterator();
    }
    
    public String toString() {
        return values.toString();
    }

    /**
     * Iterator for integer values
     */
    private class LongSparseIterator extends NumberIterator {
        private LongIterator iterator;
        private long n;
        
        public LongSparseIterator() {
            this.iterator = values.iterator();
        }
        
        public boolean hasNext() { 
            return iterator.hasNext();
        }
        
        public Number next() {
            n = iterator.next();
            return this;
        }
        
        public int intValue() {
            return (int) n;
        }
        
        public long longValue() {
            return n;
        }
        
        public float floatValue() {
            return n;
        }
        
        public double doubleValue() {
            return n;
        }
    }
}
