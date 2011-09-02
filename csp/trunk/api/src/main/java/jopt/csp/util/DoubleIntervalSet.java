package jopt.csp.util;



/**
 * A sorted set of double intervals
 */
public class DoubleIntervalSet extends DoubleSet implements IntervalSet {
    private static final int INIT_SIZE = 8;

    private DoubleIntervalSetListener listener;
    private int callback;
    private int size;
    private int intervalCount;
    
    private int firstIntervalIdx;
    private int lastIntervalIdx;
    private int intervalEntries;
    private double[] startVals;
    private double[] endVals;
    private int[] prevIdxList;
    private int[] nextIdxList;
    // Header for a mini linked list of positions that once were occupied
    // but are now free.
    protected int firstVacatedPosition;

    /**
     *  Creates a new set
     */
    public DoubleIntervalSet() {
        startVals = new double[INIT_SIZE];
        endVals = new double[INIT_SIZE];
        prevIdxList = new int[INIT_SIZE];
        nextIdxList = new int[INIT_SIZE];
        clear();
    }
    
    /**
     * Constructor for cloning set
     * 
     * @param set set to be cloned
     */
    private DoubleIntervalSet(DoubleIntervalSet set) {
        this.size = set.size;
        this.firstIntervalIdx = set.firstIntervalIdx;
        this.lastIntervalIdx = set.lastIntervalIdx;
        this.intervalEntries = set.intervalEntries;
        this.startVals = (double[]) set.startVals.clone();
        this.endVals = (double[]) set.endVals.clone();
        this.prevIdxList = (int[]) set.prevIdxList.clone();
        this.nextIdxList = (int[]) set.nextIdxList.clone();
        this.firstVacatedPosition = set.firstVacatedPosition;
        this.intervalCount = set.intervalCount;
    }
    
    /**
     * Returns the size of the set.
     * @return  As currently implemented, the size gives only 0, 1, or MAX_VALUE.
     *          This means that for three single-value intervals (three sparse values),
     *          while you might expect it to return 3, it instead returns MAX_VALUE.
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the number of intervals in the set
     */
    public int getIntervalCount() {
    	return intervalCount;
    }
    
    /**
     *  Returns index of a value in the list
     */
    public int getIntervalIndex(int idx) {
        if (size == 0 || idx < 0) return -1;
        
        int intervalIdx = firstIntervalIdx;
        for (int i=1; i<=idx; i++) {
        	intervalIdx = nextIdxList[intervalIdx];
        }
        
        return intervalIdx;
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
        size = 0;
        firstVacatedPosition = -1;
        intervalCount = 0;
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
     * @param idx   Index previous to index that is to be found
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
    public double getIntervalStart(int idx) {
        return startVals[idx];
    }
    
    /**
     * Returns end value for interval at a given index
     */
    public double getIntervalEnd(int idx) {
        return endVals[idx];
    }
    
    /**
     * Returns minimum value for set.
     * @return  Returns the minimum value for the set.  If the set is empty,
     *          returns positive infinity. 
     */
    public double getMin() {
        if (firstIntervalIdx == -1)
            return Double.POSITIVE_INFINITY;
        return startVals[firstIntervalIdx];
    }
    
    /**
     * Returns the start of the interval at the specified index
     */
    public double getMin(int intervalIdx) {
        return startVals[intervalIdx];
    }

    /**
     * Returns maximum value for set.
     * @return  Returns the maximum value for the set.  If the set is empty,
     *          returns negative infinity.
     */
    public double getMax() {
        if (lastIntervalIdx == -1)
            return Double.NEGATIVE_INFINITY;
        return endVals[lastIntervalIdx];
    }
    
    /**
     * Returns the end of the interval at the specified index
     */
    public double getMax(int intervalIdx) {
        return endVals[intervalIdx];
    }
    
    /**
     *  Returns index of a value in the list
     */
    private int indexOfValue(double val) {
        if (size == 0) return -1;
        
        int idx = firstIntervalIdx;
        while (idx >= 0) {
            double start = startVals[idx];
            
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
     * Utility function to added interval data into storage arrays and return
     * index of location where data was stored
     */
    private int recordInterval(double start, double end) {
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
        intervalCount++;
        
        return idx;
    }
    
    private void enlargeArrays() {
        int oldSize = startVals.length;
        int newSize = (oldSize*3/2) + 2;
        
        // Expand startVals
        double[] dblTmp = new double[newSize];
        System.arraycopy(startVals, 0, dblTmp, 0, oldSize);
        this.startVals = dblTmp;
        
        // Expand endVals
        dblTmp = new double[newSize];
        System.arraycopy(endVals, 0, dblTmp, 0, oldSize);
        this.endVals = dblTmp;
        
        // Expand prevIdxList
        int[] intTmp = new int[newSize];
        System.arraycopy(prevIdxList, 0, intTmp, 0, oldSize);
        this.prevIdxList = intTmp;
        
        // Expand nextIdxList
        intTmp = new int[newSize];
        System.arraycopy(nextIdxList, 0, intTmp, 0, oldSize);
        this.nextIdxList = intTmp;
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
        
        intervalCount--;
    }
    
    /**
     * Utility function to adjust size of set.
     * As currently implemented, the size gives only 0, 1, or MAX_VALUE.
     * This means that for three single-value intervals, while you might
     * expect it to return 3, it instead returns MAX_VALUE.
     */
    private void updateSize() {
        if(firstIntervalIdx == -1 && lastIntervalIdx == -1) {
            size = 0;
            return;
        }
        
        double min = getMin();
        double max = getMax();
        
        if (min == max)
            size = 1;
        else if (min > max)
            size = 0;
        else
            size = Integer.MAX_VALUE;
    }
    
    /**
     *  Returns true if value is contained in set
     */
    public boolean contains(double val) {
        return indexOfValue(val) >= 0;
    }
    
    /**
     * Returns the index of the interval containing the specified value
     * or -1 if no such value exists
     */
    public int intervalIdxOf(double val) {
        int index = indexOfValue(val);
        return (index >= 0) ? index : -1;
    }

    /**
     * Adds a value to set
     */
    public void add(double val) {
        add(val, val);
    }

    /**
     * Adds a range of values to set
     */
    public void add(double start, double end) {
        if (start > end) return;
        
        // determine index to insert interval
        int idx = indexOfValue(start);
        
        // check if interval is before first interval, completely between two intervals, or after last interval 
        if ((idx < 0 && indexOfValue(end) == idx) || idx == Integer.MIN_VALUE)
            idx = this.addWithoutOverlap(start, end, idx);
        else
            idx = this.addWithOverlap(start, end, idx);
        
        // check for adjacent intervals
        if (idx >= 0)
            this.checkForAdjacentIntervals(idx);
    }
    
    /**
     * Handles adding intervals that do not overlap any existing intervals
     */
    private int addWithoutOverlap(double start, double end, int idx) {
        // create interval data
        int newIdx = recordInterval(start, end);
        
        // the added interval is the first interval added to this set, hurray!
        if (size == 0) {
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
        
        updateSize();
        notifyIntervalAddition(start, end);
        return newIdx;
    }
    
    /**
     * Handles adding intervals that overlap at least one existing interval 
     */
    private int addWithOverlap(double start, double end, int idx) {
        // if the start of the interval to add is not in any existing interval,
        // convert idx to the index of the first interval the interval to add overlaps
        if (idx < 0) idx = -idx - 1;
        
        // check if interval to add is completely contained within another interval
        double s = startVals[idx];
        double e = endVals[idx];
        if (s <= start && end <= e) return -1;
        
        // determine start of new interval and record value of previous / next indexes
        double newStart = (s<start) ? s : start;
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
        if (start<s) {
            notifyIntervalAddition(start, DoubleUtil.previous(s));
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
        double nextStart = (nextIdx>=0) ? startVals[nextIdx] : 0;
        while (nextIdx>=0 && nextStart <= end) {
            // notify of gap being filled
            double gapStart = DoubleUtil.next(e);
            notifyIntervalAddition(gapStart, DoubleUtil.previous(nextStart));
            
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
        double newEnd = end;
        if (e<end) {
            double gapStart = DoubleUtil.next(e);
            notifyIntervalAddition(gapStart, end);
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
        updateSize();
        
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
            if (DoubleUtil.previous(startVals[newIdx]) == endVals[previousIdx]) {
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
            if (DoubleUtil.next(endVals[newIdx]) == startVals[nextIdx]) {
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
    public void remove(double val) {
        remove(val, val);
    }
    
    /**
     * Removes a range of values from the set
     */
    public void remove(double start, double end) {
        
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
        double s = startVals[idx];
        double e = endVals[idx];
        
        // return if the interval to remove is before the start of the [potentially]
        // affected interval
        //
        //        |---|
        //  .....       sxxe ..... xxxx
        if (end < s) return;
        
        // if we've made it this far without returning, we know that at least one interval is affected
        
        if (end <= e) {
            // only one interval is affected by the removal
            removeSingleAffectedInterval(start, end, s, e, idx);
        }
        else {
            // potentially more than one interval is affected by the removal
            removeMultiAffectedInterval(start, end, s, e, idx);
        }
        
        updateSize();
    }
    
    /**
     * Helper method to process removals with only one affected interval
     * 
     * @param start Beginning of the interval to remove 
     * @param end End of the interval to remove
     * @param s Beginning of the affected interval
     * @param e End of the affected interval
     * @param idx Index of the affected interval
     */
    private void removeSingleAffectedInterval(double start, double end, double s, double e, int idx) {
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
        
        // determine start and end of values to remove
        double removeStart = (s>start) ? s : start;
        double removeEnd = (e<end) ? e : end;
        
        // remove affected interval from set
        freeInterval(idx);

        // notify of values removed
        notifyIntervalRemoval(removeStart, removeEnd);
        
        // create a new previous interval if start of current interval
        // is not completely within removal range
        if (s < start) {
            int tmp = recordInterval(s, DoubleUtil.previous(start));
            
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
            int tmp = recordInterval(DoubleUtil.next(end), e);
            
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
    }
    
    /**
     * Helper method to process removals with [potentially] many affected intervals
     * 
     * @param start Beginning of the interval to remove 
     * @param end End of the interval to remove
     * @param s Beginning of the first affected interval
     * @param e End of the first affected interval
     * @param idx Index of the first affected interval
     */
    private void removeMultiAffectedInterval(double start, double end, double s, double e, int idx) {
        int prevIdx = prevIdxList[idx];
        int nextIdx = nextIdxList[idx];
        
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
        
        double removeStart = (s < start) ? start : s;
        notifyIntervalRemoval(removeStart, e);
        
        // if a portion of the first affected interval remains, be sure to add it back
        // xxx is added back into the set of intervals
        //
        //               |-------------|
        // .... ..... xxxyyy oooo oooo   .....
        if (s < start) {
            int tmp = recordInterval(s, DoubleUtil.previous(start));
            
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
            double nextEnd = endVals[nextIdx];
            while (nextIdx>=0 && nextEnd <= end) {
                // retrieve start and end of interval that will be removed
                idx = nextIdx;
                s = startVals[idx];
                e = endVals[idx];
                
                // notify of removal
                notifyIntervalRemoval(s, e);
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
            double nextStart = startVals[nextIdx];
            if (nextIdx >= 0 && nextStart <= end) {
                // retrieve start and end of interval that will be removed
                idx = nextIdx;
                s = startVals[idx];
                e = endVals[idx];
                
                // notify of removal
                notifyIntervalRemoval(s, end);
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
                    int tmp = recordInterval(DoubleUtil.next(end), e);
                    
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
    }

    /**
     * Removes all values above and including given value
     */
    public final void removeStartingAfter(double val) {
        if (size==0) return;
        if (val==Double.POSITIVE_INFINITY) return;
        if (val >= endVals[lastIntervalIdx]) return;
        remove(DoubleUtil.next(val), endVals[lastIntervalIdx]);
    }

    /**
     * Removes all values below given value
     */
    public final void removeEndingBefore(double val) {
        if (size==0) return;
        if (val==Double.NEGATIVE_INFINITY) return;
        if (val <= startVals[firstIntervalIdx]) return;
        remove(startVals[firstIntervalIdx], DoubleUtil.previous(val));
    }
    
    /**
     * Removes all values above and including given value
     */
    public void removeStartingFrom(double val) {
        if (size==0) return;
        remove(val, endVals[lastIntervalIdx]);
    }

    /**
     * Removes all values below and including given value
     */
    public void removeEndingAt(double val) {
        if (size==0) return;
        remove(startVals[firstIntervalIdx], val);
    }
    
    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        if (size==0) return val;
        
        // if value is before minimum value, return min
        double min = startVals[firstIntervalIdx];
        if (val < min) return min;
        
        // if value is beyond max value, return value passed
        double max = endVals[lastIntervalIdx];
        if (val >= max) return val;
        
        // retrieve index of next value
        double next = DoubleUtil.next(val);
        int idx = indexOfValue(next);
        
        // if value exists, return it
        if (idx>=0) return next;
        
        // return next value
        idx = -idx - 1;
        return startVals[idx];
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
        if (size==0) return val;
        
        // if value is beyond max value, return max
        double max = endVals[lastIntervalIdx];
        if (val > max) return max;
        
        // if value is before minimum value, return value passed
        double min = startVals[firstIntervalIdx];
        if (val <= min) return val;
        
        // retrieve index of previous value
        double prev = DoubleUtil.previous(val);
        int idx = indexOfValue(prev);
        
        // if value exists, return it
        if (idx>=0) return prev;
        
        // return next value
        idx = -idx - 1;
        return endVals[idx];
    }

    // javadoc inherited
    public IntervalIterator intervals() {
        return new DoubleIntervalIterator();
    }
    
    // javadoc inherited
    public NumberIterator values() {
        throw new UnsupportedOperationException("cannot iterate individual values of a set of double intervals");
    }
    
    /**
     * Creates a duplicate of this set
     */
    public Object clone() {
        return new DoubleIntervalSet(this);
    }
    
    /**
     * Sets listener for set to be notified of changes
     */
    public void setListener(DoubleIntervalSetListener listener, int callback) {
        this.listener = listener;
        this.callback = callback;
    }

    /**
     * Returns listener that is currently assigned to set
     */
    public DoubleIntervalSetListener getListener() {
        return listener;
    }
    
    /**
     * Notifies listener of addition of an interval
     */
    private void notifyIntervalAddition(double start, double end) {
        if (listener!=null)
            listener.intervalAdded(callback, start, end);
    }

    /**
     * Notifies listener of removal of an interval
     */
    private void notifyIntervalRemoval(double start, double end) {
        if (listener!=null)
            listener.intervalRemoved(callback, start, end);
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
     * Iterator for double intervals
     */
    private class DoubleIntervalIterator implements IntervalIterator {
        private boolean first;
        private int idx;
        
        public DoubleIntervalIterator() {
            first = size > 0;
        }
        
        public boolean hasNext() {
            if (size == 0)
                return false;
                
            return first || (idx>=0 && nextIdxList[idx] >= 0);
        }

        public int nextInt() {
            return (int) nextDouble();
        }

        public long nextLong() {
            return (long) nextDouble();
        }
        
        public float nextFloat() {
            return (float) nextDouble();
        }
        
        public double nextDouble() {
            if (first) {
                idx = firstIntervalIdx;
                first = false;
            }
            else { 
                idx = nextIdxList[idx];
            }
            
            return startVals[idx];
        }

        public int endInt() {
            return (int) endVals[idx];
        }

        public long endLong() {
            return (long) endVals[idx];
        }

        public float endFloat() {
            return (float) endVals[idx];
        }

        public double endDouble() {
            return endVals[idx];
        }
    }
}
