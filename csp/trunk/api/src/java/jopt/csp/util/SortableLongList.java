/*
 * Created on Nov 21, 2005
 */
package jopt.csp.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongCollection;

/**
 * A flexible, sortable list of long primitives.  Borrows much
 * of its functionality from the ArrayLongList implementation
 * given in the Commons Primitives project 
 * (http://jakarta.apache.org/commons/primitives/index.html)
 * 
 * @author Chris Johnson
 */
public class SortableLongList extends ArrayLongList {

    private static final long serialVersionUID = 1L;
	
	private transient long[] data = null;
    private int size = 0;
    
    /** 
     * Construct an empty list with the default
     * initial capacity.
     */
    public SortableLongList() {
        this(8);
    }    

    /**
     * Construct an empty list with the given
     * initial capacity.
     * @throws IllegalArgumentException when <i>initialCapacity</i> is negative
     */
    public SortableLongList(int initialCapacity) {
        if(initialCapacity < 0) {
            throw new IllegalArgumentException("capacity " + initialCapacity);
        }
        data = new long[initialCapacity];
        size = 0;
    }    

    /** 
     * Constructs a list containing the elements of the given collection, 
     * in the order they are returned by that collection's iterator.
     * 
     * @see ArrayLongList#addAll(org.apache.commons.collections.primitives.LongCollection)
     * @param that the non-<code>null</code> collection of <code>long</code>s 
     *        to add
     * @throws NullPointerException if <i>that</i> is <code>null</code>
     */
    public SortableLongList(LongCollection that) { 
        this(that.size());
        addAll(that);
    }    

    public long get(int index) {
        checkRange(index);
        return data[index];
    }
    
    public int size() {
        return size;
    }
    
    /** 
     * Removes the element at the specified position in 
     * (optional operation).  Any subsequent elements 
     * are shifted to the left, subtracting one from their 
     * indices.  Returns the element that was removed.
     * 
     * @param index the index of the element to remove
     * @return the value of the element that was removed
     * 
     * @throws UnsupportedOperationException when this operation is not 
     *         supported
     * @throws IndexOutOfBoundsException if the specified index is out of range
     */
    public long removeElementAt(int index) {
        checkRange(index);
        incrModCount();
        long oldval = data[index];
        int numtomove = size - index - 1;
        if(numtomove > 0) {
            System.arraycopy(data,index+1,data,index,numtomove);
        }
        size--;
        return oldval;
    }
    
    /** 
     * Replaces the element at the specified 
     * position in me with the specified element
     * (optional operation). If specified index is
     * beyond the current size, the list grows
     * to accommodate it.  No IndexOutOfBoundsException
     * will occur during the set operation.
     * 
     * @param index the index of the element to change
     * @param element the value to be stored at the specified position
     * @return the value previously stored at the specified position
     * 
     * @throws UnsupportedOperationException when this operation is not 
     *         supported
     */
    public long set(int index, long element) {
        ensureCapacity(index+1);
        ensureSize(index+1);
        incrModCount();
        long oldval = data[index];
        data[index] = element;
        return oldval;
    }
        
    /** 
     * Inserts the specified element at the specified position 
     * (optional operation). Shifts the element currently 
     * at that position (if any) and any subsequent elements to the 
     * right, increasing their indices.  If the specified index is
     * beyond the current size, this method behaves like a call
     * to {@link #set(int, long)}.
     * 
     * @param index the index at which to insert the element
     * @param element the value to insert
     * 
     * @throws UnsupportedOperationException when this operation is not 
     *         supported
     * @throws IllegalArgumentException if some aspect of the specified element 
     *         prevents it from being added to me
     */
    public void add(int index, long element) {
        if (index >= size) {
            set(index, element);
        }
        else {
            incrModCount();
            ensureCapacity(size+1);
            int numtomove = size-index;
            System.arraycopy(data,index,data,index+1,numtomove);
            data[index] = element;
            size++;
        }
    }

    /** 
     * Increases my capacity, if necessary, to ensure that I can hold at 
     * least the number of elements specified by the minimum capacity 
     * argument without growing.
     */
    public void ensureCapacity(int mincap) {
        incrModCount();
        if(mincap > data.length) {
            int newcap = (data.length * 3)/2 + 1;
            long[] olddata = data;
            data = new long[newcap < mincap ? mincap : newcap];
            System.arraycopy(olddata,0,data,0,size);
        }
    }

    /** 
     * Reduce my capacity, if necessary, to match my
     * current {@link #size size}.
     */
    public void trimToSize() {
        incrModCount();
        if(size < data.length) {
            long[] olddata = data;
            data = new long[size];
            System.arraycopy(olddata,0,data,0,size);
        }
    }
    
    /**
     * Sorts the list into ascending numerical order via {@link java.util.Arrays#sort(long[])}
     */
    public void sort() {
        trimToSize();
        Arrays.sort(data);
    }
    
    /**
     * Reverses the order of the elements
     */
    public void reverse() {
        for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
            swap(i, j);
    }
    
    /**
     * Swaps the two specified elements.
     * (If the specified positions are equal, invoking this method leaves
     * the list unchanged.)
     */
    public void swap(int i, int j) {
        long tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }
    
    /**
     * Searches the list for the specified key via {@link java.util.Arrays#binarySearch(long[], long)}
     * <p>
     * The array must be sorted (as by the sort method, above) prior to making this call.
     * If it is not sorted, the results are undefined. If the list contains multiple elements
     * with the specified value, there is no guarantee which one will be found.
     * @param key the value to be searched for
     * @return index of the search key, if it is contained in the list; otherwise, (-(insertion point) - 1)
     */
    public int binarySearch(long key) {
        trimToSize();
        return Arrays.binarySearch(data, key);
    }
    
    /**
     * Sorts the specified range of the list into ascending numerical order
     * via {@link java.util.Arrays#sort(long[], int, int)}
     * @param fromIndex the index of the first element (inclusive) to be sorted
     * @param toIndex the index of the last element (exclusive) to be sorted
     */
    public void sort(int fromIndex, int toIndex) {
        trimToSize();
        Arrays.sort(data, fromIndex, toIndex);
    }

    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject();
        out.writeInt(data.length);
        for(int i=0;i<size;i++) {
            out.writeLong(data[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        data = new long[in.readInt()];
        for(int i=0;i<size;i++) {
            data[i] = in.readLong();
        }
    }
    
    private final void ensureSize(int potentialSize) {
        if(potentialSize > size) {
            size = potentialSize;
        }
    }
    
    private final void checkRange(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Should be at least 0 and less than " + size + ", found " + index);
        }
    }

}
