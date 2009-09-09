package jopt.csp.util;

import org.apache.commons.collections.primitives.ArrayFloatList;
import org.apache.commons.collections.primitives.FloatIterator;

/**
 * A sorted set of individual float values
 */
public class FloatSparseSet extends FloatSet {

    protected ArrayFloatList values;
    protected FloatSparseSetListener listener;
    protected int callback;

    /**
     *  Creates a new set
     */
    public FloatSparseSet() {
        values = new ArrayFloatList();
    }
    
    /**
     * Returns the size of the set
     */
    public final int size() {
        return values.size();
    }

    /**
     * Removes all values from the set
     */
    public void clear() {
        while (values.size()>0) {
            float val = values.removeElementAt(0);
            
            // notify listener
            if (listener!=null)
                listener.valueRemoved(callback, val);
        }
    }

    /**
     * Returns minimum value for set
     */
    public float getMin() {
        if (values.size()==0) return Float.MAX_VALUE;
        return values.get(0);
    }

    /**
     * Returns maximum value for set
     */
    public float getMax() {
        if (values.size()==0) return -Float.MAX_VALUE; //can't be Float.MIN_VALUE, see API
        return values.get(values.size()-1);
    }

    /**
     * Returns index of a value in the list or a negative value indicating where it
     * should be if it were to be added (see details).
     * 
     * @return Index of value in list, if it exists.  If not, then it returns a negative value
     *         such that the index to insert the new element is (-returnvalue - 1)
     */
    private int indexOfValue(float val) {
        if (values.size()==0) return -1;
        
        // Initialize range of indices that might contain
        // value to entire list
        int lowIdx = 0;
        int highIdx = values.size()-1;
        
        // Loop to try to locate the value
        while (lowIdx <= highIdx) {
            // Calculate a rounded median index of the remaining values
            int idx = (lowIdx+highIdx)/2;
            float v = values.get(idx);
            
            // check if values are equivalent
            if (val == v) return idx;
            
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
    public boolean contains(float val) {
        return indexOfValue(val) >= 0;
    }

    /**
     * Adds a value to set
     */
    public void add(float val) {
        // determine index to insert value
        int idx = indexOfValue(val);
        
        // If the index is negative, then it doesn't already exist in the list.
        if (idx < 0) {
            // convert returned value
            // to location new value should be inserted
            // (see documentation for indexOfValue method)
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
    public void add(float start, float end) {
        throw new UnsupportedOperationException("cannot insert a range of real numbers into a sparse set");
    }

    /**
     * Removes a value from the set
     */
    public void remove(float val) {
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
    public void remove(float start, float end) {
        if (values.size()==0) return;
        
        // determine index of start value
        int idx = indexOfValue(start);
        if (idx<0) idx = -idx - 1;
        
        while (idx<values.size()) {
            float val = values.get(idx);
            
            // first value beyond end indicates no more need to remove values
            if (end > val)
            	break;
            
            // remove any value after start skipping values that are not
            // within the range
            if (start < val)
                values.removeElementAt(idx);
            else
                idx++;
        }
    }

    /**
     * Removes all values above and including given value
     */
    public void removeStartingFrom(float val) {
        if (values.size()==0) return;
        
        // remove range of values between val and max
        float max = values.get(values.size()-1);
        remove(val, max);
    }

    // javadoc inherited
    public void removeStartingAfter(float val) {
        removeStartingFrom(FloatUtil.next(val));
    }

    /**
     * Removes all values below and including given value
     */
    public void removeEndingAt(float val) {
        if (values.size()==0) return;
        
        // remove range of values between min and val 
        float min = values.get(0);
        remove(min, val);
    }
    
    // javadoc inherited
    public void removeEndingBefore(float val) {
        removeEndingAt(FloatUtil.previous(val));
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
        if (values.size()==0) return val;
        
        // if value is before minimum value, return min
        float min = values.get(0);
        if (val < min)
            return min;
        
        // if value is beyond max value, return value passed
        float max = values.get(values.size()-1);
        if (val >= max)
            return val;
        
        // retrieve index of next value
        float next = val+1;
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
    public float getNextLower(float val) {
        if (values.size()==0) return val;
        
        // if value is beyond max value, return max
        float max = values.get(values.size()-1);
        if (val > max)
            return max;
        
        // if value is before minimum value, return value passed
        float min = values.get(0);
        if (val <= min)
            return val;
        
        // retrieve index of next value
        float prev = val-1;
        int idx = indexOfValue(prev);
        
        // if value exists, return it
        if (idx>=0) return prev;
        
        // locate interval with previous value
        idx = -idx - 1;
        return values.get(idx);
    }

    /**
     * Creates a duplicate of this set
     */
    public Object clone() {
        // create new set
        FloatSparseSet set = createEmptySet();
        
        // recreate interval list
        for (int i=0; i<values.size(); i++) {
            set.values.add(values.get(i));
        }
        
        return set;
    }
    
    /**
     * Creates a new empty set
     */
    protected FloatSparseSet createEmptySet() {
        return new FloatSparseSet();
    }

    /**
     * Sets listener for set to be notified of changes
     */
    public void setListener(FloatSparseSetListener listener, int callback) {
        this.listener = listener;
        this.callback = callback;
    }

    /**
     * Returns listener that is currently assigned to set
     */
    public FloatSparseSetListener getListener() {
        return listener;
    }
    
    /**
     * Creates a new number for use during iteration
     */
    protected Number createNumber(float val) {
        return new Float(val);
    }
    
    /** 
     * returns an iterator containing all numbers in the set
     */
    public NumberIterator values() {
        return new FloatSparseIterator();
    }
    
    public String toString() {
        return values.toString();
    }

    /**
     * Iterator for float values
     */
    private class FloatSparseIterator extends NumberIterator {
        private FloatIterator iterator;
        private float n;
        
        public FloatSparseIterator() {
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
            return (long) n;
        }
        
        public float floatValue() {
            return n;
        }
        
        public double doubleValue() {
            return n;
        }
    }
}
