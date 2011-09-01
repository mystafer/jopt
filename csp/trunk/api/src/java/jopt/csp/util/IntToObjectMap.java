package jopt.csp.util;

import java.util.ArrayList;

import org.apache.commons.collections.primitives.ArrayIntList;

/*
 * A Map that uses integer keys and Object values 
 */
public class IntToObjectMap<T> implements Cloneable{

    protected ArrayIntList keys;
    protected ArrayList<T> values;

    /**
     *  Creates a new map
     */
    public IntToObjectMap() {
        keys = new ArrayIntList();
        values = new ArrayList<T>();
    }
    
    /**
     * Copy constructor
     * @param im    IntMap to be copied.
     */
    public IntToObjectMap(IntToObjectMap<T> im) {
        keys = new ArrayIntList(im.keys);
        values = new ArrayList<T>(im.values);
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
        keys.clear();
        values.clear();
    }

    /**
     *  Returns index of a key in the list
     */
    private int indexOfKey(int key) {
        if (keys.size()==0) return -1;
        
        // initialize range of indices that might contain
        // value to entire list
        int lowIdx = 0;
        int highIdx = keys.size()-1;
        
        // loop through indices and locate value
        while (lowIdx <= highIdx) {
            int idx = (lowIdx+highIdx)/2;
            int k = keys.get(idx);
            
            // check if keys are equivalent
            if (k==key) return idx;
            
            // if key is below, reduce high index to be index below current median
            if (key < k) highIdx = idx - 1;
            
            // otherwise value must be above
            else lowIdx = idx + 1;
        }
        
        // if value wasn't located, return a negative value
        // representing locating where value should be inserted in list
        return -(lowIdx+1);
    }

    /**
     * Adds a key to set
     */
    public void put(int key, T val) {
        // determine index to insert key
        int idx = indexOfKey(key);
        
        // check if key is already contained in list
        if (idx < 0) {
            // convert returned value
            // to location new key should be inserted
            idx = -idx - 1;
            keys.add(idx, key);
            values.add(idx, val);
        }
        
        // overwrite existing entry
        else {
            values.set(idx, val);
        }
    }

    /**
     * Removes a key from the map
     */
    public void remove(int key) {
        // determine index of key
        int idx = indexOfKey(key);
        
        // check if key is in list
        if (idx >= 0) {
            keys.removeElement(key);
            values.remove(idx);
        }
    }
    
    /**
     * Returns the value associated with a key
     * 
     * @param key           Key to retrieve associated value
     * @return              Value associated with key. Null if not found.
     */
    public T get(int key) {
        // determine index of key
        int idx = indexOfKey(key);
        
        // check if key is in list
        if (idx >= 0)
            return values.get(idx);
        else
            return null;
    }
    
    /**
     * Returns true if this map contains a given key
     * 
     * @param key   Key to verify existence
     */
    public boolean containsKey(int key) {
        // determine index of key
        int idx = indexOfKey(key);
        
        // check if key is in list
        if (idx >= 0)
            return true;
        else
            return false;
    }
    
    /**
     * Returns an array of the keys currently in this map.
     * The returned array is independent of the actual keys.
     * 
     * @return int[] of keys
     */
    public int[] keySet() {
        return keys.toArray();
    }
    
    @SuppressWarnings("unchecked")
	public T[] valueSet() {
        return (T[]) values.toArray();
    }
    
    // javadoc is inherited
    public Object clone() {
        return new IntToObjectMap<T>(this);
    }
}
