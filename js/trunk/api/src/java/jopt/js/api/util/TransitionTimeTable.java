package jopt.js.api.util;

import jopt.csp.util.SortableIntList;

/**
 *
 * TransitionTimeTable is a singleton class to keep track of the amount of time it takes
 * to transition between two different locations, a source and destination.
 *
 * @author jboerkoel
 *
 */
public class TransitionTimeTable {

	private static TransitionTimeTable ttt;

	private DoubleIndexedIntList table;

	private SortableIntList sourceIndices;
	private SortableIntList destIndices;
	private SortableIntList sourceRefNumber;
	private SortableIntList destRefNumber;

	private SortableIntList operationIDs;
	private SortableIntList operationToSourceRefNum;
	private SortableIntList operationToDestRefNum;

	private boolean anyAdded = false;

	private TransitionTimeTable() {
		table = new DoubleIndexedIntList();
		sourceIndices = new SortableIntList();
		destIndices = new SortableIntList();
		destRefNumber= new SortableIntList();
		sourceRefNumber = new SortableIntList();
		operationIDs = new SortableIntList();
		operationToDestRefNum = new SortableIntList();
		operationToSourceRefNum = new SortableIntList();
	}

	/**
	 *  Gets an instance of the TransitionTimeTable.
	 *  This enforces that there can only be one transition time table
	 *  at a time.
	 */
	public static TransitionTimeTable getInstance() {
		if (ttt==null) {
			ttt = new TransitionTimeTable();
		}
		return ttt;
	}

	/**
	 *  Registers an operation ID with a specific source and destination reference number.
	 *  This allows multiple different operations to be considered as the same source or destination
	 *  reference number.
	 *
	 * @param operationID the id of the operation, and
	 *
	 */
	public void registerOperationID(int operationID, int sourceRefNum, int destRefNum) {
		int index = operationIDs.binarySearch(operationID);
		if (index<0) {
			index = -index-1;
			operationIDs.add(index, operationID);
			operationToSourceRefNum.add(index,sourceRefNum);
			operationToDestRefNum.add(index,destRefNum);
		}
		else {
			operationToSourceRefNum.set(index,sourceRefNum);
			operationToDestRefNum.set(index,destRefNum);
		}
	}

	/**
	 * Returns the source number for a given operation Id
	 * @param opID operation id
	 * @return int representing the coresponding source num
	 */
	public int getSourceNumByOp(int opID){
		int index = operationIDs.binarySearch(opID);
		if (index<0) return index;
		return operationToSourceRefNum.get(index);
	}

	/**
	 * Returns the destination number for a given operation Id
	 * @param opID operation id
	 * @return int representing the coresponding destination num
	 */
	public int getDestNumByOp(int opID){
		int index = operationIDs.binarySearch(opID);
		if (index<0) return index;
		return operationToDestRefNum.get(index);
	}

	/**
	 * Adds an entry to the table given the between two operations 
	 * @param sourceOpId id of the source operation
	 * @param destOpId id of the destination operation
	 * @param transitTime the amount of time that it takes to transition between the given source and destination
	 */
	public void addByOp(int sourceOpId, int destOpId, int transitTime) {
		add(getSourceNumByOp(sourceOpId),getDestNumByOp(destOpId),transitTime);
	}

	/**
	 * Adds an entry to the table given source and destination numbers
	 * @param sourceRefNum number of the source
	 * @param destRefNum number of the destination
	 * @param transitTime the amount of time that it takes to transition between the given source and destination
	 */
	public void add(int sourceRefNum, int destRefNum, int transitTime) {
		anyAdded = true;
		int sourceIndex = sourceRefNumber.binarySearch(sourceRefNum);
		int destIndex = destRefNumber.binarySearch(destRefNum);

		if(sourceIndex<0) {
			sourceIndex = -sourceIndex-1;
			int newIndex = sourceIndices.size();
			sourceRefNumber.add(sourceIndex,sourceRefNum);
			sourceIndices.add(sourceIndex,newIndex);
			sourceIndex = newIndex;
		}
		else {
			sourceIndex = sourceIndices.get(sourceIndex);
		}

		if(destIndex<0) {
			destIndex = -destIndex-1;
			int newIndex = destIndices.size();
			destRefNumber.add(destIndex,destRefNum);
			destIndices.add(destIndex,newIndex);
			destIndex = newIndex;
		}
		else {
			destIndex = destIndices.get(destIndex);
		}

		table.set(sourceIndex,destIndex,transitTime);
	}

	/**
	 * Returns the entry for the amount of time it takes to transition from
	 * the source to the destination
	 * @param sourceRefNum number of the source
	 * @param destRefNum number of the destination
	 * @return int representing the time it takes to transtion from the source to the destination
	 */
	public int get(int sourceRefNum, int destRefNum) {
		try {
			if (!anyAdded) return 0;
			int sourceIndex = sourceRefNumber.binarySearch(sourceRefNum);
			sourceIndex = sourceIndices.get(sourceIndex);
			int destIndex = destRefNumber.binarySearch(destRefNum);
			destIndex = destIndices.get(destIndex);
			return table.get(sourceIndex,destIndex);
		}
		catch (IndexOutOfBoundsException ioobe) {
			return 0;
		}
	}

	/**
	 * Returns the entry for the amount of time it takes to transition from
	 * the source to the destination give their respective operation ids
	 * @param sourceOpId id of the source operation
	 * @param destOpId id of the destination operation
	 * @return int representing the time it takes to transtion from the source to the destination
	 */
	public int getByOp(int sourceOpId, int destOpId) {
		if (!anyAdded) return 0;
		return get(getSourceNumByOp(sourceOpId),getDestNumByOp(destOpId));
	}

	/**
	 * Creates a String object that represents the useful information in this class
	 * @return String object that represents the useful information in this class
	 */
	public String toString() {
		String string = "";
		for (int i=0; i<sourceRefNumber.size(); i++) {
			string+="[";
			for (int j=0; j<destRefNumber.size(); j++) {
				string+=get(sourceRefNumber.get(i),destRefNumber.get(j))+",";
			}
			string+="]\n";
		}
		return string;
	}


}
