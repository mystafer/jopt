package jopt.csp.util;


/**
 * A sorted set of integer values
 */
public class IntSparseSet extends IntSet {

    protected SortableIntList values;
    protected int callback;
    protected IntSparseSetListener listener;
    
    private IntSparseIterator valueIterator;

    /**
     *  Creates a new set
     */
    public IntSparseSet() {
        values = new SortableIntList();
    }
    
    /**
     * Cloning constructor
     */
    private IntSparseSet(IntSparseSet set) {
        values = new SortableIntList(set.values);
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
            int val = values.removeElementAt(0);
            
            // notify listener
            if (listener!=null)
                listener.valueRemoved(callback, val);
        }
    }

    /**
     * Returns minimum value for set
     */
    public int getMin() {
        if (values.size()==0) return Integer.MAX_VALUE;
    	return values.get(0);
    }

    /**
     * Returns maximum value for set
     */
    public int getMax() {
        if (values.size()==0) return Integer.MIN_VALUE;
        return values.get(values.size()-1);
    }

    /**
     *  Returns index of a value in the list
     */
    private int indexOfValue(int val) {
        if (values.size()==0) return -1;
        
        // initialize range of indices that might contain
        // value to entire list
        int lowIdx = 0;
        int highIdx = values.size()-1;
        
        // loop through indices and locate value
        while (lowIdx <= highIdx) {
            int idx = (lowIdx+highIdx)/2;
            int v = values.get(idx);
            
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
    public boolean contains(int val) {
    	return indexOfValue(val) >= 0;
    }

    /**
     * Adds a value to set
     */
    public void add(int val) {
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
    public void add(int start, int end) {
        for (int i=start; i<=end; i++)
            add(i);
    }

    /**
     * Removes a value from the set
     */
    public void remove(int val) {
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
    public void remove(int start, int end) {
        if (values.size()==0) return;
        
        for (int i=start; i<=end; i++)
            remove(i);
    }

    /**
     * Removes all values above and including given value
     */
    public void removeStartingFrom(int val) {
        if (values.size()==0) return;
        
        // remove range of values between val and max
        int max = values.get(values.size()-1);
        remove(val, max);
    }

    /**
     * Removes all values below and including given value
     */
    public void removeEndingAt(int val) {
        if (values.size()==0) return;
        
        // remove range of values between min and val 
        int min = values.get(0);
        remove(min, val);
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public int getNextHigher(int val) {
        if (values.size()==0) return val;
        
        // if value is before minimum value, return min
        int min = values.get(0);
        if (val < min)
            return min;
        
        // if value is beyond max value, return value passed
        int max = values.get(values.size()-1);
        if (val >= max)
            return val;
        
        // retrieve index of next value
        int next = val+1;
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
    public int getNextLower(int val) {
        if (values.size()==0) return val;
        
        // if value is beyond max value, return max
        int max = values.get(values.size()-1);
        if (val > max)
            return max;
        
        // if value is before minimum value, return value passed
        int min = values.get(0);
        if (val <= min)
            return val;
        
        // retrieve index of next value
        int prev = val-1;
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
    public void setListener(IntSparseSetListener listener, int callback) {
        this.listener = listener;
        this.callback = callback;
    }

    /**
     * Returns listener that is currently assigned to set
     */
    public IntSparseSetListener getListener() {
        return listener;
    }
    
    /**
     * Creates a duplicate of this set
     */
    public Object clone() {
        return new IntSparseSet(this);
    }
    
    /** 
     * returns an iterator containing all numbers in the set
     */
    public NumberIterator values() {
        if (valueIterator == null)
        	valueIterator = new IntSparseIterator();
        else
            valueIterator.reset();
        
        return valueIterator;
    }
    
    public String toString() {
    	return values.toString();
    }

    /**
     * Iterator for integer values
     */
    private class IntSparseIterator extends NumberIterator {
        private static final long serialVersionUID = 1L;
		
		private int idx;
        
        public IntSparseIterator() {
        	this.idx = -1;
        }
        
        public void reset() {
            idx = -1;
        }
        
        public boolean hasNext() { 
            int s = values.size()-1;
            return s >= 0 && idx < s;  
        }
        
        public Number next() {
            idx++;
            return this;
        }
        
        public int intValue() {
        	return values.get(idx);
        }
        
        public long longValue() {
            return values.get(idx);
        }
        
        public float floatValue() {
            return values.get(idx);
        }
        
        public double doubleValue() {
            return values.get(idx);
        }
    }
}
