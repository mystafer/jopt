package jopt.csp.util;




/**
 * A sorted set of integer intervals
 */
public class IntIntervalSet extends IntSet implements IntervalSet {
    private static final int INIT_SIZE = 8;
    protected static final int FREE_POSITION_MARKER = -12345;

	protected IntIntervalSetListener cpsListener;
	protected int cpsCallback;
	
    protected long longSize;
    protected int size;
    
    protected int firstIntervalIdx;
    protected int lastIntervalIdx;
    protected int intervalEntries;
    protected int[] startVals;
    protected int[] endVals;
    protected int[] prevIdxList;
    protected int[] nextIdxList;
    // Header for a mini linked list of positions that once were occupied
    // but are now free.
    protected int firstVacatedPosition;

    /**
     *  Creates a new set
     */
    public IntIntervalSet() {
        startVals = new int[INIT_SIZE];
        endVals = new int[INIT_SIZE];
        prevIdxList = new int[INIT_SIZE];
        nextIdxList = new int[INIT_SIZE];
        clear();
    }
    
    /**
     * Constructor for cloning a set
     * 
     * @param set set to be cloned
     */
    protected IntIntervalSet(IntIntervalSet set) {
    	this.longSize = set.longSize;
        this.size = set.size;
        this.firstIntervalIdx = set.firstIntervalIdx;
        this.lastIntervalIdx = set.lastIntervalIdx;
        this.startVals = (int[]) set.startVals.clone();
        this.endVals = (int[]) set.endVals.clone();
        this.intervalEntries = set.intervalEntries;
        this.prevIdxList = (int[]) set.prevIdxList.clone();
        this.nextIdxList = (int[]) set.nextIdxList.clone();
        this.firstVacatedPosition = set.firstVacatedPosition;
    }
    
    /**
     * Returns the size of the set
     */
    public int size() {
        return size;
    }

    /**
     * Removes all values from the set
     */
    public void clear() {
        firstIntervalIdx = -1;
        lastIntervalIdx = -1;
        intervalEntries = 0;
        prevIdxList[0] = -1;
        nextIdxList[0] = -1;
        longSize = 0;
        size = 0;
        firstVacatedPosition = -1;
    }

    /**
     * Returns first interval index in list
     */
    public int getFirstIntervalIndex() {
    	return firstIntervalIdx;
    }
    
    /**
     * Returns the last index in list
     */
    public int getLastIntervalIndex() {
        return lastIntervalIdx;
    }
    
    /**
     * Returns next interval index in list
     * 
     * @param idx   Index preceding index that is to be found
     */
    public int getNextIntervalIndex(int idx) {
    	return nextIdxList[idx];
    }
    
    /**
     * Returns previous interval index in list
     * 
     * @param idx   Index following index that is to be found
     */
    public int getPreviousIntervalIndex(int idx) {
        return prevIdxList[idx];
    }
    
    /**
     * Returns start value for interval at a given index
     */
    public int getIntervalStart(int idx) {
        return startVals[idx];
    }
    
    /**
     * Returns end value for interval at a given index
     */
    public int getIntervalEnd(int idx) {
        return endVals[idx];
    }
    
    /**
     * Returns minimum value for set
     */
    public int getMin() {
        if (firstIntervalIdx == -1)
            return Integer.MAX_VALUE;
        return startVals[firstIntervalIdx];
    }
    
    /**
     * Returns the start of the interval at the specified index
     */
    public int getMin(int intervalIdx) {
        return startVals[intervalIdx];
    }

    /**
     * Returns maximum value for set
     */
    public int getMax() {
        if (lastIntervalIdx == -1)
            return Integer.MIN_VALUE;
        return endVals[lastIntervalIdx];
    }
    
    /**
     * Returns the end of the interval at the specified index
     */
    public int getMax(int intervalIdx) {
        return endVals[intervalIdx];
    }
    
    public boolean isIntervalContained(int start, int end) {
    	return (indexOfValue(start)>=0)&&(indexOfValue(start)==indexOfValue(end));
    }
    
    public boolean isIntervalEmpty(int start, int end) {
    	return (indexOfValue(start)<0)&&(indexOfValue(start)==indexOfValue(end));
    }
    
    public boolean isEmpty() {
    	return (firstIntervalIdx<0);
    }
    
    /**
     * Returns an int interval set of all the intervals that are free between the specified start and end
     * @param start start of the interval
     * @param end   end of the interval
     * @return an int interval set of all the intervals that are free between start and end
     */
    public IntIntervalSet getFreeIntervalsBetween(int start, int end) {
    	IntIntervalSet set = new IntIntervalSet();
    	set.add(start,end);
    	
    	int index = indexOfValue(start);
    	int endIndex = indexOfValue(end);
    	if ((index==endIndex)&&(index<0)) {
    		return set;
    	}
    	if (index<0) {
    		index = -(index+1);
    	}
    	else {
    		set.remove(start, getIntervalEnd(index));
    		index = getNextIntervalIndex(index);
    	}
    	if (endIndex<0) {
    		endIndex = -(endIndex+1);
    	}
    	else {
    		set.remove(getIntervalStart(endIndex),end);
    	}
    	while ((index>=0)&&(index!=endIndex)) {
    		set.remove(getIntervalStart(index),getIntervalEnd(index));
    		index = getNextIntervalIndex(index);
    	}
    	return set;
    }
    
    /**
     *  Returns index of a value in the list
     */
    public int indexOfValue(int val) {
        if (size == 0) return -1;
        
        int idx = firstIntervalIdx;
        while (idx >= 0) {
            int start = startVals[idx];
            
            // if value is not in any interval, capture
            // index that interval should be recorded within
            if (val < start) {
                return -(idx+1);
            }
            
            // value is below end, interval is located
            if (val <= endVals[idx]) {
                return idx;
            }
            
            idx = nextIdxList[idx];
        }
        
        return Integer.MIN_VALUE;
    }

    /**
     * Utility function to add interval data into storage arrays and return
     * index of location where data was stored.  Forward and backward linking
     * must be handled by the caller.
     */
    protected int recordInterval(int start, int end) {
        // determine index to store interval data
        int idx;
        if (firstVacatedPosition >= 0) {
            idx = firstVacatedPosition;
            firstVacatedPosition = nextIdxList[idx];
        } else {
            idx = intervalEntries++;
            if (idx >= startVals.length) enlargeArrays();
        }
        
        // store interval data
        startVals[idx] = start;
        endVals[idx] = end;
        prevIdxList[idx] = -1;
        nextIdxList[idx] = -1;
        
        return idx;
    }
    
    private void enlargeArrays() {
        int oldSize = startVals.length;
        int newSize = (oldSize*3/2) + 2;
        
        // Expand startVals
        int[] intTmp = new int[newSize];
        System.arraycopy(startVals, 0, intTmp, 0, oldSize);
        this.startVals = intTmp;
        
        // Expand endVals
        intTmp = new int[newSize];
        System.arraycopy(endVals, 0, intTmp, 0, oldSize);
        this.endVals = intTmp;
        
        // Expand prevIdxList
        intTmp = new int[newSize];
        System.arraycopy(prevIdxList, 0, intTmp, 0, oldSize);
        this.prevIdxList = intTmp;
        
        // Expand nextIdxList
        intTmp = new int[newSize];
        System.arraycopy(nextIdxList, 0, intTmp, 0, oldSize);
        this.nextIdxList = intTmp;
    }
    
    /**
     *  Returns true if value is contained in set
     */
    public boolean contains(int val) {
        return indexOfValue(val) >= 0;
    }
    
    /**
     * Returns the index of the interval containing the specified value
     * or -1 if no such value exists
     */
    public int intervalIdxOf(int val) {
        int index = indexOfValue(val);
        return (index >= 0) ? index : -1;
    }

    /**
     * Adds a value to set
     */
    public void add(int val) {
        add(val, val);
    }
    
    /**
     * Adds a value to set
     */
    public void add(IntIntervalSet set) {
    	int index = set.getFirstIntervalIndex();
    	while(index>=0) {
    		this.add(set.getMin(index),set.getMax(index));
    		index = set.getNextIntervalIndex(index);
    	}
    }

    /**
     * Utility function to remove interval from storage arrays and update
     * previous and next interval indices
     */
    protected void freeInterval(int idx) {
        // Before we start manipulating things, we need to remember how they were
        int tempPrev = prevIdxList[idx];
        int tempNext = nextIdxList[idx];
        
        // Put this interval at the front of the linked list of vacant positions
        nextIdxList[idx] = firstVacatedPosition;
        firstVacatedPosition = idx;
        // Mark this position as a free (useful for some methods that scan
        // entire array in a non-linked fashion)
        prevIdxList[idx] = FREE_POSITION_MARKER;
        
        if ((idx ==firstIntervalIdx)&&(idx ==lastIntervalIdx)) {
        	firstIntervalIdx = -1;
        	lastIntervalIdx = -1;
        }
        else if (idx == firstIntervalIdx) {
        	firstIntervalIdx = tempNext;
        	if (tempNext>=0)
        		prevIdxList[tempNext] = tempPrev;	
        }
        else if (idx == lastIntervalIdx) {
        	lastIntervalIdx = tempPrev;
        	if (tempPrev>=0)
        		nextIdxList[tempPrev] = tempNext;	
        }
        else {
        	if (tempNext>=0)
        		prevIdxList[tempNext] = tempPrev;
        	if (tempPrev>=0) 
        		nextIdxList[tempPrev] = tempNext;
        }
    }
    
    /**
     * Utility function to adjust size of set
     */
    protected void adjustSize(long change) {
        // update size of set
        longSize += change;
        if (longSize >= Integer.MAX_VALUE)
            size = Integer.MAX_VALUE;
        else
            size = (int) longSize;
    }

    /**
     * Adds a range of values to set
     */
    public void add(int start, int end) {
    	this.add(start, end, true);
    }
    
    /**
     * Adds a range of values to set
     */
	public void add(int start, int end, boolean notify) {
    	if (start > end) return;
        
        // determine index of first [potentially] affected interval
        // by getting the index of the start of the added interval
        int idx = indexOfValue(start);
        
        // check if interval is before first interval, completely between two intervals, or after last interval 
        if ((idx < 0 && indexOfValue(end) == idx) || idx == Integer.MIN_VALUE)
            idx = this.addWithoutOverlap(start, end, idx, notify);
        else
            idx = this.addWithOverlap(start, end, idx, notify);
        
        // check for adjacent intervals
        if (idx >= 0)
            this.checkForAdjacentIntervals(idx);
    }
    
    /**
     * Removes any parts that both do not have in common from this.
     */
    public void intersect( IntIntervalSet intersector) {
    	this.removeEndingBefore(intersector.getMin());
    	this.removeStartingAfter(intersector.getMax());
    	int index = intersector.firstIntervalIdx;
    	if (index<0) {
    		return;
    	}
    	int nextIndex = intersector.getNextIntervalIndex(index);
    	while ((index>=0) && (nextIndex>=0)) {
    		int gapStart = intersector.getIntervalEnd(index)+1;
    		int gapEnd = intersector.getIntervalStart(nextIndex)-1;
    		this.remove(gapStart,gapEnd);
    		index = nextIndex;
    		nextIndex = intersector.getNextIntervalIndex(nextIndex);
    	}
    }
    
    
    /**
     * Handles adding intervals that do not overlap any existing intervals
     * @return index of newly created interval
     */
    private int addWithoutOverlap(int start, int end, int idx, boolean notify) {
        // create interval data
        int newIdx = recordInterval(start, end);

        // the added interval is the first interval added to this set, hurray!
        // TODO: this is a hack.  we shouldn't have to check both of these conditions...
        // perhaps this is here because of trouble elsewhere
        if ((size == 0)||((firstIntervalIdx==-1)&&(lastIntervalIdx==-1))) {
        	size=0;
            firstIntervalIdx = newIdx;
            lastIntervalIdx = newIdx;
        }
        // the added interval lies completely before the first interval
        else if (idx == -(firstIntervalIdx+1)) {
            // adjust prev / next index lists
            prevIdxList[firstIntervalIdx] = newIdx;
            nextIdxList[newIdx] = firstIntervalIdx;
            
            // indicate that the new interval is the first
            prevIdxList[newIdx] = -1;
            firstIntervalIdx = newIdx;
        }
        // the added interval lies completely after the last interval
        else if (idx == Integer.MIN_VALUE) {
            // adjust prev / next index lists
            nextIdxList[lastIntervalIdx] = newIdx;
            prevIdxList[newIdx] = lastIntervalIdx;
            
            // indicate that the new interval is the last
            nextIdxList[newIdx] = -1;
            lastIntervalIdx = newIdx;
        }
        // the added interval lies completely between two existing intervals
        else {
            // convert idx to non-negative value indicating the index of the
            // interval that the added interval precedes, see example:
            //      Existing Intervals
            //  |---|     |---|         |---|
            //    1         2             3
            //              Added Interval
            //                   xxxx
            // idx = 3
            idx = -idx - 1;
            
            // adjust prev / next index lists
            int prev = prevIdxList[idx];
            nextIdxList[newIdx] = idx;
            prevIdxList[idx] = newIdx;
            nextIdxList[prev] = newIdx;
            prevIdxList[newIdx] = prev;
        }
        
        adjustSize( ((long)end) - ((long)start) + 1);
        if (notify) {
        	notifyIntervalAddition(start, end);
        }
        
        return newIdx;
    }
    
    /**
     * Handles adding intervals that overlap at least one existing interval 
     */
    private int addWithOverlap(int start, int end, int idx, boolean notify) {
        // if the start of the interval to add is not in any existing interval,
        // convert idx to the index of the first interval the interval to add overlaps
        if (idx < 0) idx = -idx - 1;
        
        // check if interval to add is completely contained within another interval
        int s = startVals[idx];
        int e = endVals[idx];
        if (s <= start && end <= e) return -1;
        
        // determine start of new interval and record value of previous / next indexes
        int newStart = (s<start) ? s : start;
        int prevIdx = prevIdxList[idx];
        int nextIdx = nextIdxList[idx];
        
        // If new interval represents range |--------|, interval
        // represented by xxx will be removed in this block.  Intervals
        // represented by ooo are handled in following loop
        //
        //            |----|
        // .... .....   xxxxx .... .....
        //
        //            |----|
        // .... ..... xxxxx .... .....
        //
        //              |----|
        // .... ..... xxxxx    .... .....
        //
        //              |----|
        // .... ..... xxxxx ooo  .... .....
        //
        //              |--------|
        // .... ..... xxxxx ooo  ooo .... .....
        freeInterval(idx);

        // notify of gap !!! being filled
        //
        //           |-----------------|
        // .... .....!!!xxxx xxxxx xxxx   .... .....
        long growth = 0;
        if (start<s) {
            growth += s-start;
            if (notify) {
            	notifyIntervalAddition(start, s-1);
            }
        }
        
        // If new interval represents range |--------|, all intervals
        // represented by xxx will be removed in this loop.  The interval
        // ooo should already have been removed
        //
        // this loop will also send notifications for gaps [1..5]
        //
        //               |--------------------------|
        // .... ..... ooooo xxxx  xxxxx  xxxx  xxxx xxxx .... .....
        //                 1    2      3     4     5
        //
        //           |------------------------|
        // .... ..... ooooo xxxx  xxxxx  xxxx   ... .....
        //                 1    2      3     
        int nextStart = (nextIdx>=0) ? startVals[nextIdx] : 0;
        while (nextIdx>=0 && nextStart <= end) {
            // notify of gap being filled
            int gapStart = e+1;
            growth += nextStart - gapStart;
            if (notify) {
            	notifyIntervalAddition(gapStart, nextStart-1);
            }
            
            // retrieve start and end of interval that will be removed
            idx = nextIdx;
            s = startVals[idx];
            e = endVals[idx];
            
            // retrieve index of next interval range
            nextIdx = nextIdxList[idx];
            if (nextIdx>=0)
                nextStart = startVals[nextIdx];
            
            // remove overlapping interval
            freeInterval(idx);
        }

        // notify of gap !!! being filled
        //
        //           |------------------|
        // .... .....  xxxx xxxxx xxxx!!! .... .....
        int newEnd = end;
        if (e<end) {
            growth += end - e;
            int gapStart = e+1;
            if (notify) {
            	notifyIntervalAddition(gapStart, end);
            }
        }
        else if (e>end) {
            newEnd = e;
        }
        
        // create interval data
        idx = recordInterval(newStart, newEnd);
        
        // connect new index with previous and next lists
        prevIdxList[idx] = prevIdx;
        nextIdxList[idx] = nextIdx;
        
        // adjust prev/next/first/last indexes
        if (prevIdx >= 0)
            nextIdxList[prevIdx] = idx;
        else
            firstIntervalIdx = idx;
        
        if (nextIdx >= 0)
            prevIdxList[nextIdx] = idx;
        else
            lastIntervalIdx = idx;
        
        // update size of set
        adjustSize(growth);
        
        return idx;
    }
    
    /**
     * Checks for and combines with intervals adjacent to the interval
     * at the specified index.
     */
    private void checkForAdjacentIntervals(int newIdx) {
    	
        // if the interval has no previous interval, its start is clearly not adjacent to anything
        if (newIdx != firstIntervalIdx) {
            int previousIdx = prevIdxList[newIdx];
            if (startVals[newIdx]-1 == endVals[previousIdx]) {
                // the start of the added interval is adjacent to the end of an existing interval
                startVals[newIdx] = startVals[previousIdx];
                int idxBeforePrevious = prevIdxList[previousIdx];
                prevIdxList[newIdx] = idxBeforePrevious;
                if (idxBeforePrevious != -1)
                    nextIdxList[idxBeforePrevious] = newIdx;
                else
                    firstIntervalIdx = newIdx;
                    
                freeInterval(previousIdx);
            }
        }
        
        // if the interval has no next interval, its end is clearly not adjacent to anything
        if (newIdx != lastIntervalIdx) {
            int nextIdx = nextIdxList[newIdx];
            if (endVals[newIdx]+1 == startVals[nextIdx]) {
                // the end of the added interval is adjacent to the start of an existing interval
                endVals[newIdx] = endVals[nextIdx];
                int idxAfterNext = nextIdxList[nextIdx];
                nextIdxList[newIdx] = idxAfterNext;
                if (idxAfterNext != -1)
                    prevIdxList[nextIdxList[nextIdx]] = newIdx;
                else
                    lastIntervalIdx = newIdx;
                
                freeInterval(nextIdx);
            }
        }
    }

    /**
     * Removes a value from the set
     */
    public void remove(int val) {
        remove(val, val);
    }
    
    /**
     * Removes a range of values from the set
     */
    public void remove(int start, int end) {
    	remove(start, end, true);
    }
    
    /**
     * Removes a range of values from the set
     */
    public void remove(int start, int end, boolean notify) {
        
        // if there's nothing to remove or nothing to remove from, return
        if (start > end || size==0) return;
        
        // determine index of first [potentially] affected interval
        int idx = indexOfValue(start);
        
        // return if the interval to remove is beyond the end of all intervals;
        // note that |---| is being removed
        //
        //            |---|
        // ..... xxxx
        if (idx == Integer.MIN_VALUE) return;
        
        // if the start of the interval to remove is not in any existing interval,
        // convert idx to the index of the first interval the interval to remove [might] overlap
        if (idx < 0) idx = -idx - 1;
        
        // retrieve start and end of first [potentially] affected interval
        int s = startVals[idx];
        int e = endVals[idx];
        
        // return if the interval to remove is before the start of the [potentially]
        // affected interval
        //
        //        |---|
        //  .....       sxxe ..... xxxx
        if (end < s) return;
        
        // if we've made it this far without returning, we know that at least one interval is affected
        
        long sizeChange = 0;
        if (end <= e) {
            // only one interval is affected by the removal
            sizeChange = removeSingleAffectedInterval(start, end, s, e, idx, notify);
        }
        else {
            // potentially more than one interval is affected by the removal
            sizeChange = removeMultiAffectedInterval(start, end, s, e, idx, notify);
        }
        
        // update size of set
        if (sizeChange != 0)
            adjustSize(-sizeChange);
    }
    
    /**
     * Helper method to process removals with only one affected interval
     * 
     * @param start Beginning of the interval to remove 
     * @param end   End of the interval to remove
     * @param s     Beginning of the affected interval
     * @param e     End of the affected interval
     * @param idx   Index of the affected interval
     * @param notify Whether or not to notify listeners of the removals
     * @return      the resulting size change
     */
    private long removeSingleAffectedInterval(int start, int end, int s, int e, int idx, boolean notify) {
        // removal range end is within first (and only) affected interval;
        // note that |---| is being removed
        //
        //      |---|
        // ..... sxxxxe  .....
        //
        //          |---|
        // .....  sxxxxxe  .....
        //
        //        |---|
        // .....  sxxxxxe  .....
        //
        //          |---|
        // .....  sxxxxxxxe  .....
        //
        //        |---|
        // .....  sxxxe  .....
        
        // initialize indices of previous and next intervals
        int prevIdx = prevIdxList[idx];
        int nextIdx = nextIdxList[idx];
        long sizeChange = 0;
        
        // determine start and end of values to remove
        int removeStart = (s>start) ? s : start;
        int removeEnd = (e<end) ? e : end;
        
        // be careful when computing size changes to prevent overflow 
        sizeChange = removeEnd;
        sizeChange -= removeStart;
        sizeChange++;
        
        // remove affected interval from set
        freeInterval(idx);

        // notify of values removed
        if (notify) {
            notifyIntervalRemoval(removeStart, removeEnd);
        }
        
        // create a new previous interval if start of current interval
        // is not completely within removal range
        if (s < start) {
            int tmp = recordInterval(s, start-1);
            
            // if the altered interval was the first interval
            if (prevIdx < 0)
                firstIntervalIdx = tmp;
            else
                nextIdxList[prevIdx] = tmp;
            
            prevIdxList[tmp] = prevIdx;
            prevIdx = tmp;
        }
        
        // create a new next interval if end of current interval
        // is not completely within removal range
        if (end < e) {
            int tmp = recordInterval(end+1, e);
            
            // if the altered interval was the last interval
            if (nextIdx < 0)
                lastIntervalIdx = tmp;
            else
                prevIdxList[nextIdx] = tmp;
            
            nextIdxList[tmp] = nextIdx;
            nextIdx = tmp;
        }
        
        // adjust prev/next/first/last indexes
        if (prevIdx >= 0)
            nextIdxList[prevIdx] = nextIdx;
        else
            firstIntervalIdx = nextIdx;
        
        if (nextIdx >= 0)
            prevIdxList[nextIdx] = prevIdx;
        else
            lastIntervalIdx = prevIdx;
        
        return sizeChange;
    }
    
    /**
     * Helper method to process removals with [potentially] many affected intervals
     * 
     * @param start Beginning of the interval to remove 
     * @param end   End of the interval to remove
     * @param s     Beginning of the first affected interval
     * @param e     End of the first affected interval
     * @param idx   Index of the first affected interval
     * @param notify Whether or not to notify listeners of the removals
     * @return the  resulting size change
     */
    private long removeMultiAffectedInterval(int start, int end, int s, int e, int idx, boolean notify) {
        int prevIdx = prevIdxList[idx];
        int nextIdx = nextIdxList[idx];
        long sizeChange = 0;
        
        // Interval xxxyyy is removed in this block and interval
        // yyy is notified to listener
        //
        //               |-------------|
        // .... ..... xxxyyy oooo oooo   .....
        //
        //            |-------------|
        // .... ..... yyyyyy oooo oooo .....
        //
        //          |-------------|
        // .... ..... yyyyyy oooo oooo .....
        
        // remove first affected interval from set
        freeInterval(idx);
        
        int removeStart = (s < start) ? start : s;
        
        sizeChange = e-removeStart+1;
        if (notify) {
            notifyIntervalRemoval(removeStart, e);
        }
        
        // if a portion of the first affected interval remains, be sure to add it back
        // xxx is added back into the set of intervals
        //
        //               |-------------|
        // .... ..... xxxyyy oooo oooo   .....
        if (s < start) {
            int tmp = recordInterval(s, start-1);
            
            if (prevIdx < 0)
                firstIntervalIdx = tmp;
            else
                nextIdxList[prevIdx] = tmp;
            
            prevIdxList[tmp] = prevIdx;
            
            if (nextIdx < 0)
                lastIntervalIdx = tmp;
            else
                prevIdxList[nextIdx] = tmp;
            
            nextIdxList[tmp] = nextIdx;
            
            prevIdx = tmp;
        }
        
        // Removes intervals xxxx
        //
        //                |-------------|
        // .... .....  oooooo xxxx xxxxxx ....
        //
        //                |-------------|
        // .... .....  oooooo xxxx xxxx   ....
        //
        //             |-------------|
        // .... .....  oooooo xxxx oooo  ....
        if (nextIdx >= 0) {
            int nextEnd = endVals[nextIdx];
            while (nextIdx>=0 && nextEnd <= end) {
                // retrieve start and end of interval that will be removed
                idx = nextIdx;
                s = startVals[idx];
                e = endVals[idx];
                
                // notify of removal
                sizeChange += e-s+1;
                if (notify) {
                    notifyIntervalRemoval(s, e);
                }
                // retrieve index of next interval
                nextIdx = nextIdxList[idx];
                if (nextIdx>=0)
                    nextEnd = endVals[nextIdx];
                
                // remove overlapping interval
                freeInterval(idx);
            }
        }
        
        // Interval yyyxxx is removed in this block and interval
        // yyy is notified to listener
        //
        //              |-------------|
        // .... .....ooooo ooo ooo  yyyxx
        if (nextIdx >= 0) {
            int nextStart = startVals[nextIdx];
            if (nextIdx >= 0 && nextStart <= end) {
                // retrieve start and end of interval that will be removed
                idx = nextIdx;
                s = startVals[idx];
                e = endVals[idx];
                
                // notify of removal
                sizeChange += end-s+1;
                if (notify) {
                    notifyIntervalRemoval(s, end);
                }
                // retrieve index of next interval
                nextIdx = nextIdxList[idx];
                
                // remove last affected interval from set
                freeInterval(idx);
                
                // if a portion of the last affected interval remains, be sure to add it back
                // xxx is added back into the set of intervals
                //
                //              |-------------|
                // .... .....ooooo ooo ooo  yyyxxx
                if (end < e) {
                    int tmp = recordInterval(end+1, e);
                    
                    if (nextIdx < 0)
                        lastIntervalIdx = tmp;
                    else
                        prevIdxList[nextIdx] = tmp;
                    
                    nextIdxList[tmp] = nextIdx;
                    
                    if (prevIdx < 0)
                        firstIntervalIdx = tmp;
                    else
                        nextIdxList[prevIdx] = tmp;
                    
                    prevIdxList[tmp] = prevIdx;
                }
            }
        }
        
        return sizeChange;
    }

    /**
     * Removes all values above and including given value
     */
    public void removeStartingFrom(int val) {
        if (size==0) return;
        remove(val, endVals[lastIntervalIdx]);
    }

    /**
     * Removes all values below and including given value
     */
    public void removeEndingAt(int val) {
        if (size==0) return;
        remove(startVals[firstIntervalIdx], val);
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public int getNextHigher(int val) {
        if (size==0) return val;
        
        // if value is before minimum value, return min
        int min = startVals[firstIntervalIdx];
        if (val < min) return min;
        
        // if value is beyond max value, return value passed
        int max = endVals[lastIntervalIdx];
        if (val >= max) return val;
        
        // retrieve index of next value
        int next = val+1;
        int idx = indexOfValue(next);
        
        // if value exists, return it
        if (idx>=0) return next;
        
        // return next value
        if (idx!=Integer.MIN_VALUE) {
        	idx = -idx - 1;
        	return startVals[idx];
        }
        else {
        	return val;
        }
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public int getNextLower(int val) {
        if (size==0) return val;
        
        // if value is beyond max value, return max
        int max = endVals[lastIntervalIdx];
        if (val > max) return max;
        
        // if value is before minimum value, return value passed
        int min = startVals[firstIntervalIdx];
        if (val <= min) return val;
        
        // retrieve index of previous value
        int prev = val-1;
        int idx = indexOfValue(prev);
        
        // if value exists, return it
        if (idx>=0) return prev;
        
        // return next value
        if (idx!=Integer.MIN_VALUE) {
        	idx = -idx - 1;
        	idx = getPreviousIntervalIndex(idx);
        	if (idx<0) {
        		return val;
        	}
        	return endVals[idx];
        }
        else {
        	return val;
        }
    }

    // javadoc inherited
    public IntervalIterator intervals() {
        return new IntIntervalIterator();
    }
    
    // javadoc inherited
    public NumberIterator values() {
        return new IntIntervalNumberIterator();
    }
    
    /**
     * Creates a duplicate of this set
     */
    public Object clone() {
        return new IntIntervalSet(this);
    }
    
    /**
     * Sets listener for set to be notified of changes
     */
    public void setListener(IntIntervalSetListener listener, int callback) {
    	cpsListener = listener;
    	cpsCallback = callback;
    	
    }


    /**
     * Returns listener that is currently assigned to set
     */
    public IntIntervalSetListener getListener() {
    	return cpsListener;
    }
    
    /**
     * Notifies listener of addition of an interval
     */
    protected void notifyIntervalAddition(int start, int end) {
    	if (cpsListener==null) return;
    	cpsListener.intervalAdded(cpsCallback,start,end);
    }

    /**
     * Notifies listener of removal of an interval
     */
    protected void notifyIntervalRemoval(int start, int end) {
    	if (cpsListener==null) return;
    	cpsListener.intervalRemoved(cpsCallback,start,end);
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("{");
        
        int idx = firstIntervalIdx;
        while (idx>=0)  {
        	buf.append("[");
            buf.append(startVals[idx]);
            buf.append("..");
            buf.append(endVals[idx]);
            buf.append("]");
            
            idx = nextIdxList[idx];
            if (idx>=0) buf.append(", ");
        }
        
        buf.append("}");
        
        return buf.toString();
    }
    
    /**
     * Iterator for integer intervals
     */
    private class IntIntervalIterator implements IntervalIterator {
        private boolean first;
    	private int idx;
        
        public IntIntervalIterator() {
        	first = size > 0;
        }
        
		public boolean hasNext() {
            if (size == 0)
                return false;
                
			return first || (idx>=0 && nextIdxList[idx] >= 0);
		}

		public int nextInt() {
            if (first) {
            	idx = firstIntervalIdx;
				first = false;
            }
            else { 
            	idx = nextIdxList[idx];
            }
            
            return startVals[idx];
		}

        public long nextLong() {
            return nextInt();
        }
        
        public float nextFloat() {
            return nextInt();
        }
        
        public double nextDouble() {
            return nextInt();
        }

		public int endInt() {
            return endVals[idx];
		}

		public long endLong() {
            return endInt();
		}

		public float endFloat() {
            return endInt();
		}

		public double endDouble() {
            return endInt();
		}
    }
    
    /**
     * Iterator for integer values
     */
    private class IntIntervalNumberIterator extends NumberIterator {
        private int curIntervalIdx;
        private int nextIntervalIdx;
        private int n;
        private int endVal;
        
        public IntIntervalNumberIterator() {
            this.curIntervalIdx = -1;
            this.nextIntervalIdx = firstIntervalIdx;
        }
        
        /**
         * returns true if more elements exist
         */
        public boolean hasNext() {
            return curIntervalIdx >= 0 || nextIntervalIdx >= 0;
        }

        /**
         * Returns next element
         */
        public Number next() {
            // Check if returning values from an interval
            if (curIntervalIdx<0) {
                // make next interval as current interval
                curIntervalIdx = nextIntervalIdx;
                nextIntervalIdx = nextIdxList[curIntervalIdx];
                
                // retrieve start and end values of interval
                n = startVals[curIntervalIdx];
                endVal = endVals[curIntervalIdx];

                // If start equals end, reset interval
                if (n == endVal) curIntervalIdx = -1;

                return this;
            }

            n++;

            // Reset interval if n equals last value
            if (n == endVal) curIntervalIdx = -1;

            // Return next value in enumeration
            return this;
        }
        
        public int intValue() {
            return n;
        }
        
        public long longValue() {
        	return n;
        }
        
        public float floatValue() {
            return n;
        }
        
        public double doubleValue() {
            return n;
        }
    }
    
    /**
     * An equals method for comparing if two set objects are equivalent
     * @param set   IntIntervalSet to compare this to
     * @return      true if the objects are determined to be equal
     */
    public boolean equals(IntIntervalSet set) {
    	if ((set.getMin()!=getMin())||
    			(set.getMax()!=getMax())) {
    		return false;
    	}
    	int thisIndex = firstIntervalIdx;
    	int otherIndex = set.firstIntervalIdx;
    	while((thisIndex>=0)&&(otherIndex>=0)) {
    		if (getIntervalStart(thisIndex)!= set.getIntervalStart(otherIndex)) {
    			return false;
    		}
    		if (getIntervalEnd(thisIndex)!= set.getIntervalEnd(otherIndex)) {
    			return false;
    		}
    		thisIndex = getNextIntervalIndex(thisIndex);
    		otherIndex = set.getNextIntervalIndex(otherIndex);
    	}
    	return ((thisIndex<0)&&(otherIndex<0));
    }    
}

