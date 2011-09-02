package jopt.csp.spi.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import jopt.csp.variable.CspGenericIndex;



/**
 * Implementation of an iterator class that can be used
 * to loop over a combination of target and source indices
 * during processing of propagation events
 */
public class IndexIterator implements Iterator<CspGenericIndex> {
    private List<? extends CspGenericIndex> idxList;
    private boolean idxReady;
    private boolean firstMove;
    
    /**
     * Creates new index iterator that will loop over unique combinations
     * for a list of indices
     * 
     * @param indices   List of indices to iterator over
     */
    public IndexIterator(List<? extends CspGenericIndex> indices) 
    {
        if (indices.size()==0) {
            throw new IllegalStateException("No indices to iterate over");
        }
        
        this.idxList = indices;
        
        // initialize iterator
        reset();
    }
    
    /**
     * Returns true if another value exists in the iterator
     */
    public boolean hasNext() {
        return idxReady;
    }
    
    /**
     * Moves iterator to next index value
     */
    public CspGenericIndex next() {
        // ensure next value exists
        if (!idxReady) {
            throw new NoSuchElementException("no further index combinations exist");
        }
        
        moveToNextIndexCombination();
        
        return null;
    }
    
    /**
     * Internal function for updating indices to iterator over to next logical combination
     * to allow generic variables to be processed
     */
    private void moveToNextIndexCombination() {
        // search for target index combination
        if (firstMove) {
        	firstMove = false;
            idxReady = findFirstForIndexList(idxList);
        }
        else {
        	idxReady = findNextForIndexList(idxList);
        }
    }
    
    public void remove() {
    }

    /**
     * Updates a list of index objects to the first value in each index
     */
    private int resetIndexList(List<? extends CspGenericIndex> idxList) {
        int totalSize = 0;
        for (int i=0; i<idxList.size(); i++) {
            GenericIndex idx = (GenericIndex) idxList.get(i);
            totalSize += idx.size();
            idx.reset();
        }
        return totalSize;
    }

    /**
     * Locates the first unique index combination for a list of indices
     */
    private boolean findFirstForIndexList(List<? extends CspGenericIndex> idxList) {
        if (idxList.size() == 0) return false;
        
        // attempt to all indices first possible value
        boolean multipleCombinations = false;
        for (int i=0; i<idxList.size(); i++) {
            GenericIndex curIdx = (GenericIndex) idxList.get(i);
            if (curIdx.size()>1) multipleCombinations = true;
            
            // locate first index
            if (!curIdx.next())
                return false;
        }
        
        return multipleCombinations;
    }
    
    /**
     * Locates the next unique index combination for a list of indices
     */
    private boolean findNextForIndexList(List<? extends CspGenericIndex> idxList) {
        if (idxList.size() == 0) return false;
        
        // attempt to move first possible index in list to next value
        boolean hasNext = false;
        for (int i=idxList.size()-1; i>=0; i--) {
            GenericIndex curIdx = (GenericIndex) idxList.get(i);
            
            // next index found
            if (curIdx.next()) {
                // only one index in list, check if another value exists
                if (idxList.size()==1) {
                    hasNext = curIdx.currentVal() < curIdx.size()-1;
                }
                
                // check if another value exists by looping through all indices
                // when current index is the least significant
                else if (i==idxList.size()-1) {
                    for (int j=0; j<idxList.size(); j++) {
                        GenericIndex idx = (GenericIndex) idxList.get(j);
                        hasNext |= idx.currentVal() < idx.size()-1;
                        if (hasNext) break;
                    }
                }
                
                // reset lower indices to first value
                else {
                    boolean multipleLower = false;
                    for (int j=i+1; j<idxList.size(); j++) {
                        GenericIndex bottomIdx = (GenericIndex) idxList.get(j);
                        bottomIdx.reset();
                        bottomIdx.next();
                        if (bottomIdx.size()>1){
                            multipleLower = true;
                        }
                    }
                    //We check to see if there are more combinations prior to where we are at.
                    for (int j=i-1; j>=0; j--) {
                        GenericIndex upperIdx = (GenericIndex) idxList.get(j);
                        if (upperIdx.hasNext()) {
                            multipleLower = true;
                            break;    
                        }
                    }
                    
                    // another value must exist if there are multiple lower values
                    if (multipleLower) {
                        hasNext = true;
                    }
                    else {
                        hasNext = curIdx.currentVal() < curIdx.size()-1;
                    }
                }   
                
                break;
            }
        }
        
        return hasNext;
    }
    
    /**
     * Resets iterator to original position to allow restart
     */
    public void reset() {
        idxReady = resetIndexList(idxList) > 0;
        firstMove = true;
    }
}
