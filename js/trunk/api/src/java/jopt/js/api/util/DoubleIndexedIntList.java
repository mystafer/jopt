package jopt.js.api.util;

import java.util.ArrayList;

import jopt.csp.util.SortableIntList;

/**
 * A data structure that can grow dynamically in two dimensions.
 * 
 * @author James Boerkoel
 */
public class DoubleIndexedIntList {
	private ArrayList data;

	public DoubleIndexedIntList() {
		data = new ArrayList();
	}
	
	/**
	 * Adds element at index2 in the second index
	 * @param index2 - the second index
	 * @param element - the element being added
	 * @return the first index where this is placed
	 */
	public int add(int index2, int element) {
		int index1 = data.size();
		SortableIntList intList = new SortableIntList();
		intList.set(index2,element);
		data.set(index1,intList);
		return index1;
	}
	
	/**
	 * Gets the element at index index1,index2
	 * @param index1 - first index
	 * @param index2 - second index
	 * @return element at given indices
	 */
	 public int get(int index1, int index2){
	     return ((SortableIntList)data.get(index1)).get(index2);
	 }

	 /**
	  * Sets the element at index1,index2, to element
	  * @param index1 - first index
	  * @param index2 - second index
	  * @param element - new element being set
	  */
	 public void set(int index1, int index2, int element){
		 if (index1>=data.size()) {
			 SortableIntList intList = new SortableIntList();
			 intList.set(index2,element);
			 data.add(index1,intList);
			 return;
		 }
		 SortableIntList intList = ((SortableIntList)data.get(index1));
		 if (intList==null) {
			 intList = new SortableIntList();
			 intList.set(index2,element);
			 data.set(index1,intList);
			 return;
		 }
		 intList.set(index2,element);
	 }
	 
}
