package jopt.csp.util;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleIterator;

/**
 * A sorted set of individual double values
 */
public class DoubleSparseSet extends DoubleSet {

    protected ArrayDoubleList values;
    protected DoubleSparseSetListener listener;
    protected int callback;

    /**
     *  Creates a new set
     */
    public DoubleSparseSet() {
        values = new ArrayDoubleList();
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
            double val = values.removeElementAt(0);
            
            // notify listener
            if (listener!=null)
                listener.valueRemoved(callback, val);
        }
    }

    /**
     * Returns minimum value for set
     */
    public double getMin() {
        if (values.size()==0) return Double.MAX_VALUE;
        return values.get(0);
    }

    /**
     * Returns maximum value for set
     */
    public double getMax() {
        if (values.size()==0) return -Double.MAX_VALUE; //can't be Double.MIN_VALUE, see API
        return values.get(values.size()-1);
    }

    /**
     * Returns index of a value in the list or a negative value indicating where it
     * should be if it were to be added (see details).
     * 
     * @return Index of value in list, if it exists.  If not, then it returns a negative value
     *         such that the index to insert the new element is (-returnvalue - 1)
     */
    private int indexOfValue(double val) {
        if (values.size()==0) return -1;
        
        // Initialize range of indices that might contain
        // value to entire list
        int lowIdx = 0;
        int highIdx = values.size()-1;
        
        // Loop to try to locate the value
        while (lowIdx <= highIdx) {
            // Calculate a rounded median index of the remaining values
            int idx = (lowIdx+highIdx)/2;
            double v = values.get(idx);
            
            // check if values are equivalent
            if (val==v) return idx;
            
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
    public boolean contains(double val) {
        return indexOfValue(val) >= 0;
    }

    /**
     * Adds a value to set
     */
    public void add(double val) {
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
     * Inherited method that doesn't make sense with a sparse set.  You cannot
     * add a range of values to sparse set.  This will throw an exception if called.
     */
    public void add(double start, double end) {
        throw new UnsupportedOperationException("cannot insert a range of real numbers into a sparse set");
    }

    // javadoc inherited
    public void remove(double val) {
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

    // javadoc inherited
    public void remove(double start, double end) {
        if (values.size()==0) return;
        
        // determine index of start value
        int idx = indexOfValue(start);
        if (idx<0) idx = -idx - 1;
        
        while (idx<values.size()) {
            double val = values.get(idx);
            
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

    // javadoc inherited
    public void removeStartingFrom(double val) {
        if (values.size()==0) return;
        
        // remove range of values between val and max
        double max = values.get(values.size()-1);
        remove(val, max);
    }
    
    // javadoc inherited
    public void removeStartingAfter(double val) {
        removeStartingFrom(DoubleUtil.next(val));
    }

    // javadoc inherited
    public void removeEndingAt(double val) {
        if (values.size()==0) return;
        
        // remove range of values between min and val 
        double min = values.get(0);
        remove(min, val);
    }
    
    // javadoc inherited
    public void removeEndingBefore(double val) {
        removeEndingAt(DoubleUtil.previous(val));
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        if (values.size()==0) return val;
        
        // if value is before minimum value, return min
        double min = values.get(0);
        if (val < min)
            return min;
        
        // if value is beyond max value, return value passed
        double max = values.get(values.size()-1);
        if (val >= max)
            return val;
        
        // retrieve index of next value
        double next = val+1;
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
    public double getNextLower(double val) {
        if (values.size()==0) return val;
        
        // if value is beyond max value, return max
        double max = values.get(values.size()-1);
        if (val > max)
            return max;
        
        // if value is before minimum value, return value passed
        double min = values.get(0);
        if (val <= min)
            return val;
        
        // retrieve index of next value
        double prev = val-1;
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
        DoubleSparseSet set = createEmptySet();
        
        // recreate interval list
        for (int i=0; i<values.size(); i++) {
            set.values.add(values.get(i));
        }
        
        return set;
    }
    
    /**
     * Creates a new empty set
     */
    protected DoubleSparseSet createEmptySet() {
        return new DoubleSparseSet();
    }

    /**
     * Sets listener for set to be notified of changes
     */
    public void setListener(DoubleSparseSetListener listener, int callback) {
        this.listener = listener;
        this.callback = callback;
    }

    /**
     * Returns listener that is currently assigned to set
     */
    public DoubleSparseSetListener getListener() {
        return listener;
    }
    
    /**
     * Creates a new number for use during iteration
     */
    protected Number createNumber(double val) {
        return new Double(val);
    }
    
    /** 
     * returns an iterator containing all numbers in the set
     */
    public NumberIterator values() {
        return new DoubleSparseIterator();
    }
    
    public String toString() {
        return values.toString();
    }

    /**
     * Iterator for double values
     */
    private class DoubleSparseIterator extends NumberIterator {
        private static final long serialVersionUID = 1L;
		
		private DoubleIterator iterator;
        private double n;
        
        public DoubleSparseIterator() {
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
            return (float) n;
        }
        
        public double doubleValue() {
            return n;
        }
    }
}
