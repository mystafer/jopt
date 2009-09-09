package jopt.js.spi.util;

import jopt.csp.util.SortableIntList;

/**
 * A class that is accessed statically to ensure that resource ids are all unique
 * 
 * @author James Boerkoel
 */
public class IDStore {

	//Stores all the IDs
	private static SortableIntList ids;
	static int counter = 1;
	
	/**
	 * A method to see if an id is valid (ie not used yet)
	 * @param id id that we are verifying
	 * @return true if the id is available
	 */
	public static boolean registerID(int id) {
		if (ids == null) {
			ids = new SortableIntList();
			ids.add(id);
			return true;
		}
		int index = ids.binarySearch(id);
		if (index >=0 ) {
			return false;
		}
		else {
			ids.add(-index-1, id);
			return true;
		}
	}
	
	/**
	 * Returns a unique ID that has not been used yet and registers the id as used
	 * @return a new unique ID
	 */
	public static int generateUniqueID() {
		while (!registerID(counter)) {
			counter++;
		}
		return counter;
	}
	
}
