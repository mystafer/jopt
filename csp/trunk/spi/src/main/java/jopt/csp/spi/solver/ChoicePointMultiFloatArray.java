/*
 * ChoicePointIntArray.java
 * 
 * Created on Nov 18, 2005
 */
package jopt.csp.spi.solver;

import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * Contains multiple float arrays that store data for an object that needs to record changes in a
 * {@link ChoicePointStack} so the changes can be pushed onto the stack and
 * later rolled back when the stack is popped.
 * <p>
 * This class is not thread safe.
 *  
 * @author Nick Coleman
 * @version $Revision: 1.3 $ 
 */
public class ChoicePointMultiFloatArray implements ChoicePointNumArraySet.StackList {
	private ArrayIntList basePointerStack;
    private ArrayIntList sizeStack;
    private float dataStacks[][];
    private int basePointer;
    private int size;
    private int depth;
    private int capacity;
    private int listCnt;
    
    
    /**
     * Creates a new <code>ChoicePointMultiLongArray</code>
     * 
     * @param listCnt   Number of lists to maintain
     */
    public ChoicePointMultiFloatArray(int listCnt) {
        this.listCnt = listCnt;
        this.capacity = 10;
        
        // create list objects
        this.basePointerStack = new ArrayIntList();
        this.sizeStack = new ArrayIntList();
        this.dataStacks = new float[listCnt][capacity];
        
        // initialize pointers
        this.basePointer = 0;
        this.size = 0;
        this.depth = 0;
    }
    
    /**
     * Performs actions necessary to push data
     */
    public void push() {
        // push pointer information onto stacks
        basePointerStack.add(basePointer);
        sizeStack.add(size);
        
        // update pointer information
        basePointer += size;
        size = 0;
        depth++;
    }
    
    // javadoc inherited
    public void pushDelta(Object delta) {
        push();
        
        // check if any delta data was pushed
        if (delta!=null) {
            // make sure room exists in internal array
        	float data[][] = (float[][]) delta;
            int newSize = data[0].length;  
            ensureCapacity(basePointer + newSize);
            
            // copy data into array
            for (int i=0; i<listCnt; i++) {
            	System.arraycopy(data[i], 0, dataStacks[i], basePointer, newSize);
            }
            
            size = newSize;
        }
    }
    
    /**
     * Performs actions necessary to pop data
     */
    public void pop() {
        // reset to previous position
        if (depth>0) {
            depth--;
            basePointer = basePointerStack.removeElementAt(depth);
            size = sizeStack.removeElementAt(depth);
        }
        
        // clear all pointer information
        else {
            depth = 0;
            basePointer = 0;
            size = 0;
        }
    }
    
    // javadoc inherited
    public Object popDelta() {
        // capture data for delta
        float data[][] = null;
        if (size>0) {
        	data = new float[listCnt][size];
            for (int i=0; i<listCnt; i++) {
                System.arraycopy(dataStacks[i], basePointer, data[i], 0, size);
            }
        }
        
        // pop data from stack
        pop();
        
        return data;
    }
    
    /**
     * Returns current size of list
     */
    public int size() {
    	return size;
    }
    
    /**
     * Ensures capacity exists in all internal value arrays
     * 
     * @param offset offset that needs to be available in new list
     */
    private void ensureCapacity(int offset) {
        // expand stack arrays if necessary
        if (offset >= capacity) {
            int oldCapacity = capacity;
            capacity = ((int) (((double) offset) * 1.25f)) + 1;
            
            // create new stack and copy data from old stack
            for (int i=0; i<listCnt; i++) {
                float[] newStack = new float[capacity];
                System.arraycopy(dataStacks[i], 0, newStack, 0, oldCapacity);
                
                // update reference to new stack
                dataStacks[i] = newStack;
            }
        }
    }
    
    /**
     * Adds a value to the end of the list
     * 
     * @param listIdx   Index of list to add value
     * @param val       Value to add to list
     * @return offset of new value in list
     */
    public int add(int listIdx, float val) {
        int offset = size;
    	set(listIdx, offset, val);
        return offset;
    }
    
    /**
     * Stores a value in list
     * 
     * @param listIdx   Index of list to add value
     * @param offset    Offset of value in list
     * @param val       Value to append to the list
     */
    public void set(int listIdx, int offset, float val) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("invalid value offset");
        
        // convert to an actual offset in arrays
        int actualOffset = basePointer + offset;
        
        // check if offset is beyond current size of list
        if (offset >= size) {
            // ensure capacity exists for offset requested 
            ensureCapacity(actualOffset);
            
            // initialize values to zero that have not already been accessed
            int lastOffset = basePointer + size;
            for (int i=0; i<listCnt; i++) {
                for (int j=lastOffset; j<actualOffset; j++) {
                    dataStacks[i][j] = 0;
                }
            }
            
            size = offset+1;
        }
        
        // store value at requested position
        dataStacks[listIdx][actualOffset] = val;
    }
    
    /**
     * Retrieves a value in list
     * 
     * @param listIdx   Index of list to add value
     * @param offset    Offset of value in list
     */
    public float get(int listIdx, int offset) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("invalid value offset");
        
        // any value beyond total size has not been set yet
        if (offset >= size) return 0;
        
        // convert to an actual offset in arrays
        int actualOffset = basePointer + offset;
        
        // return value at requested position
        return dataStacks[listIdx][actualOffset];
    }
    
    
    /**
     * Retrieves a value from list
     * 
     * @param listIdx   Index of list to add value
     * @param offset    Offset of value in list
     */
    public float remove(int listIdx, int offset) {
        if (offset < 0)
            throw new IndexOutOfBoundsException("invalid value offset");
        
        // any value beyond total size has not been set yet
        if (offset >= size) return 0;
        
        // convert to an actual offset in arrays
        int actualOffset = basePointer + offset;
        float dataStack[] = dataStacks[listIdx];
        float val = dataStack[actualOffset]; 
        
        // remove value at requested position
        if (offset < size-1)
        	System.arraycopy(dataStack, actualOffset, dataStack, actualOffset-1, size-offset-1);
        size--;
        
        return val;
    }
}
