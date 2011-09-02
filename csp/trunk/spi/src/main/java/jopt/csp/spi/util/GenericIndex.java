/*
 * GenericIndex.java
 * 
 * Created on Mar 19, 2005
 */
package jopt.csp.spi.util;

import jopt.csp.variable.CspGenericIndex;

/**
 * Represents subscript for a generic variable, such as i for Xi + Yi > Zi.
 */
public class GenericIndex implements CspGenericIndex {
	private String name;
    private int size;
    private int currentVal;
    
    public GenericIndex(String name, int size) {
    	this.name = name;
        this.size = size;
        this.currentVal = -1;
    }
    
    /**
     * Sets the name of this index
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this index
     */
    public String getName() {
        return name;
    }

    /**
     * Resets index to initial value
     */
    public void reset() {
        currentVal = -1;
    }
    
    /**
     * Move the index to the next value
     */
    public boolean next() {
    	if (currentVal < size-1) {
    		currentVal++;
            return true;
        }
        else
            return false;
    }
    
    /**
     * Move the index to the next value
     */
    public boolean hasNext() {
    	return (currentVal < size-1);
    }
    
    /**
     * Returns the size of the index
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns the current numeric value of the index
     */
    public int currentVal() {
    	return currentVal;
    }
    
    /**
     * Sets the current value of the index
     */
    public void changeVal(int v) {
        if (v < 0 || v >= size) {
            throw new IllegalStateException("value out of bounds for index: " + v);
        }
        
    	this.currentVal = v;
    }
    
    public int hashCode() {
    	return name.hashCode();
    }
    
    // This has to be final because the way it's currently done with
    // instanceof instead of getClass means that if it is overridden
    // it will violate the reflexive contract for equals.
    public final boolean equals(Object obj) {
    	if (obj == null || !(obj instanceof GenericIndex))
            return false;
        
        GenericIndex i = (GenericIndex) obj;
        return i.name.equals(this.name);
    }

    // inherit javadoc from java.lang.Object
    public String toString() {
        return "idx-" + name + "[" + currentVal + "]";
    }
}
