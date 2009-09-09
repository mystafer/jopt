/*
 * GlobalCardinalityCountArc.java
 * 
 * Created on Jul 28, 2005
 */
package jopt.csp.spi.arcalgorithm.graph.arc.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericArc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that ensures all ensures all variables occur the same number of times as specified by the
 * associated integer node.  An arc that counts the number of occurrences of each value is another way of looking at it.
 * 
 * @author Jim Boerkoel
 * @version $Revision $
 */
public class GlobalCardinalityCountArc extends GenericArc implements NumArc {
    
//    private int size;    
    private IntNode[] count;
    int[] targetSize;
    int[] ub;
    int[] lb;
    private Number vals[];
    private boolean removeZeros=false;
    
    /**
     * An arc to propagate changes so that the sources reflect the cardinality bounds of values on the sources
     * @param sources - all sources
     * @param targets - variables whose values are to be bound
     * @param vals - all values of which we are counting 
     * @param count - variables to represent the count of each value among the target variables
     */
    public GlobalCardinalityCountArc(NumNode sources[],NumNode targets[], Number vals[], IntNode[] count) {
        super(sources, targets);
        
        // initialize source dependencies to nodes
        sourceDependencies = new int[sources.length];
        for (int i=0; i<sources.length; i++){
            sourceDependencies[i] = DomainChangeType.DOMAIN;
        }
        this.vals = (Number[])vals.clone();
        this.count= new IntNode[count.length];
        
        Arrays.sort(this.vals);
        
        //We will leave all arrays sorted by the vals
        for (int i=0; i< vals.length; i++) {
            Number num = vals[i];
            int index = Arrays.binarySearch(this.vals,num);
            this.count[index] = count[i];
            if (count[i].getMax().intValue()==0) {
            	removeZeros = true;
            }
        }
        targetSize = new int[targets.length];
        ub = new int[count.length];
        lb = new int[count.length];
        
        for (int i=0; i<count.length;i++) {
        	targetSize[i] = targets[i].getSize();
        }
        
        for (int i=0; i<count.length; i++) {
        	ub[i] = count[i].getMax().intValue();
        	lb[i] = count[i].getMin().intValue();
        }
    }
    
    //Utility to convert a node array to a numNode array
    private static NumNode[] toNumNodeArray(Node[] sources) {
        NumNode[] numSources = new NumNode[sources.length];
        for (int i=0; i< sources.length; i++) {
            numSources[i] = (NumNode)sources[i];
        }
        return numSources;
    }
    
    /**
     * Handles removing the value that is bound to a source node from all
     * target nodes
     * 
     * @param src       Node that must be bound for value to be removed from targets
     * @param targets   Array of nodes that value will be removed from
     */
    private void propagateNumNodeBoundUB(Number src, NumNode targets[]) throws PropagationFailureException {
        int[] ubTot = new int[count.length];
        int total=0;
        for (int i=0;i<count.length;i++) {
            total+=count[i].getMax().intValue();
            ubTot[i]=total;
        }
        
        if (total<targets.length) {
        	throw new PropagationFailureException("Cardinality Constraint has been over constrained");
        }
        
        Arrays.sort(targets, new NumNodeComparator(src));
        Number removeMin = null;
        Number removeMax = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;		
        int minIndex = 0;
        int maxIndex =0;
        for (int i=0; i< targets.length; i++) {
            //Here we build the maximal interval that can be removed
            if(min > targets[i].getMin().intValue()) {
                min = targets[i].getMin().intValue();
                int tempIndex = Arrays.binarySearch(vals,((MutableNumber)targets[i].getMin()).toConst()); 
                if (tempIndex>=0) {
                    minIndex = tempIndex; 
                }
            }
            if (max < targets[i].getMax().intValue()) {
                max = targets[i].getMax().intValue();
                int tempIndex = Arrays.binarySearch(vals,((MutableNumber)targets[i].getMax()).toConst()); 
                if (tempIndex>=0) {
                    maxIndex = tempIndex;
                }
            }
            if ((removeMin!=null)&&(removeMax!=null)) {
                //This statement will allow more values to be removed than would normally occur.  This provides for slightly greater than 
                //                if ((NumberMath.compare(targets[i].getMin(), removeMin,0,NumberMath.INTEGER)
                //                    *(NumberMath.compare(removeMax,targets[i].getMax(),0,NumberMath.INTEGER)))<=0) {
                targets[i].removeRange(removeMin, removeMax);
                //                }
            }
            
            int tempInt = -1;
            if (maxIndex==minIndex) {
                tempInt = count[minIndex].getMax().intValue()-1;
            }
            else {
                tempInt= ubTot[maxIndex]-ubTot[minIndex];
            }
            if (i>=tempInt) {
                removeMin= new Integer(min);
                removeMax= new Integer(max);
            }
        }
    }
    
    
    /**
     * This method will propagate, and eliminate any ranges that it can, thus each interval that is left will be consistent.  
     * @param srcVals The num nodes that changed and for which this propagation is taking place
     * @param targets The list of all nodes that could be affected by the change
     * @throws PropagationFailureException 
     */
    public void propagateNumNodeRangeUB(HashSet srcVals, NumNode[] targets)throws PropagationFailureException {
        //We now sort based NumNodeSizeComparator
        Arrays.sort(targets, new NumNodeSizeComparator(srcVals));
        //We thus iterate through all nodes, hopefully building Hall sets on the way, meaning any member of a Hall set can and should be removed
        HashSet visitedVals = new HashSet();
        HashSet removableVals = new HashSet();
        int counter=0;
        int curSize = 0;
        for (int i=0; i<targets.length; i++) {
            //In this case, no more vals will be able to be removed from subsequent nodes
            //        	if (visitedVals.size()>=targets.length) {
            //        	    for (; i<targets.length; i++) {
            //        	        Iterator iter = removableVals.iterator();
            //                    while (iter.hasNext()) {
            //                        Number num =(Number)iter.next();
            //                        targets[i].removeValue(num);
            //                    }        
            //        	    }
            //        	    return;
            //        	}
            //In this case we have a hall interval, all subsequent intervals can safely remove these vals
            if (curSize==(counter)){
                Iterator iter = visitedVals.iterator();
                while (iter.hasNext()) {
                    removableVals.add(iter.next());    
                }   
            }
            //Remove all removable values
            Iterator iter = removableVals.iterator();
            while (iter.hasNext()) {
                Number num =(Number)iter.next();
                targets[i].removeValue(num);
            }
            //increment counter and add all values remaining to visitedVals
            counter++;
            Number val = ((MutableNumber)targets[i].getMin()).toConst();
            for (int k=0; k<targets[i].getSize(); k++) {
                if (!removableVals.contains(val)) {
                    if (!visitedVals.contains(val)) {
                        visitedVals.add(val);
                        int index=Arrays.binarySearch(vals,val);
                        curSize += count[index].getMax().intValue();
                    }
                }
                val = ((MutableNumber)targets[i].getNextHigher(val)).toConst();
            }
        }
    }

//    Utility method that could be useful later
//    /**
//     * This method will propagate the lower bound in such a way to acheive domain consistency at a cost no much different than bounds consistency, 
//     * thus we will use this for all kinds of propagation.
//     * @param src - the node for which this propagation is occurring
//     * @param targets - the nodes that could potentially be affected by a change to this particular source
//     * @throws PropagationFailureException
//     */
//    private void propagateLowerBound(NumNode src, NumNode[] targets) throws PropagationFailureException{
////    	First we parse out an array of numbers that we need to check for lower bound consistency
//        NumberIterator numIter = src.deltaValues();
//        ArrayList numsToCheck = new ArrayList();
//        while (numIter.hasNext()){
//            numsToCheck.add(new Integer(numIter.next().intValue()));
//        }
//        Number[] valsToCheck = (Number[]) numsToCheck.toArray(new Number[0]);
//        propagateLowerBound(valsToCheck, targets);
//    }
    
    /**
     * This method will propagate the lower bound in such a way to acheive domain consistency at a cost no much different than bounds consistency, 
     * thus we will use this for all kinds of propagation.
     * @param src - the node for which this propagation is occurring
     * @param targets - the nodes that could potentially be affected by a change to this particular source
     * @throws PropagationFailureException
     */
    private void propagateLowerBound(Number[] valsToCheck, NumNode[] targets) throws PropagationFailureException{
        //Next we create buckets into which we will fill references
        //The buckets will be variable capacity depending on the minimum cadinality of each value
        int[][] buckets = new int[valsToCheck.length][];
        for (int i=0; i<valsToCheck.length; i++) {
            int index = Arrays.binarySearch(vals, valsToCheck[i]);
            if ((index>=0)&& (count[index].getMin().intValue()>=0)) {
                buckets[i] = new int[count[index].getMin().intValue()];
            }
            else {
                buckets[i]=null;
            }
            //fill buckets with negs
            if (buckets[i]!=null) {
                for (int j=0; j<buckets[i].length; j++) {
                    buckets[i][j]=-1;
                }
            }
        }	
        int[] bucketIndices = new int[buckets.length];
        //We fill the buckets with references to the indices of nodes that still hold such values
        for (int i=0; i<targets.length; i++) {
            for (int j=0; j<valsToCheck.length;j++){
                //This statement can replaced by the subsequent commented if statement for bounds consistency
                if(targets[i].isInDomain(valsToCheck[j])){
                    //	            if ((targets[i].getMin().doubleValue()<= valsToCheck[j].doubleValue())&&
                    //	                (targets[i].getMax().doubleValue()>= valsToCheck[j].doubleValue())) {
                    if((buckets[j]!=null)&&(bucketIndices[j]<buckets[j].length)) {
                        buckets[j][bucketIndices[j]]=i;
                        bucketIndices[j]++;
                    }
                    else{
                        buckets[j]=null;
                    }
                }
            }
        }
        
        //Now we check for failure or for assignability
        for (int i=0; i<buckets.length; i++) {
            if(buckets[i]!=null ){
                for (int j=0; j<buckets[i].length; j++) {
                    //If the bucket is not null, it means that there was exactly the right number
                    if (buckets[i][j]>=0){
                        targets[buckets[i][j]].setValue(valsToCheck[i]);
                    }
                    //If there is a bucket with a negative number in it yet, it means that there were not enough of such values left,
                    //meaning the lower bound constraint cannot be met
                    else {
                        throw new PropagationFailureException("GlobalCardinalityConsraint: Lower Bound Constraint violated");
                    }
                }
            }
        }
    }
    
    /**
     * Guarantees domain consistency among all targets, requires no src since this is a hyper arc
     * @param targets - all nodes potentially needed to be altered
     * @throws PropagationFailureException
     */
    public void propagateNumNodeDomainUB(NumNode targets[]) throws PropagationFailureException{
        //First we sort out only the non bound nodes
        HashSet tempUnboundTargets = new HashSet();
        HashSet boundVals = new HashSet();
        int[] sizeCounter = new int[targets.length+1];
        for (int i=0; i<targets.length; i++) {
            if (!targets[i].isBound()) {
                tempUnboundTargets.add(targets[i]);
            }
            else {
                Number num = targets[i].getMin();
                if (num instanceof MutableNumber) {
                    num = ((MutableNumber)num).toConst();
                }
                if (boundVals.contains(num)) {
                    throw new PropagationFailureException("Two nodes are bound to the same value");
                }
                boundVals.add(num);
            }
            if (sizeCounter.length>targets[i].getSize()) {
                sizeCounter[targets[i].getSize()]++;
            }
        }
        
        
        
        //If no nodes are bound, no need to propagate  this is overly strict, perhaps lighten this requirement
        boolean valid=false;
        for (int i=1; i<sizeCounter.length-1; i++) {
            if (sizeCounter[i]>=i) {
                valid = true;
            }
        }
        //No improvement gained from propagating
        if (!valid) {
            return;
        }
        //Everything is bound! 
        if (tempUnboundTargets.size()==0) {
            return;
        }
        //Make it into an array for faster access
        NumNode[] unboundTargets = (NumNode[]) tempUnboundTargets.toArray(new NumNode[0]);
        
        //Here we set up an array of sets that we will use to notify that we have found a valid matching which this is a part of 
        ArrayList[] valsToMatch = new ArrayList[unboundTargets.length];
        HashSet[] matchedVals = new HashSet[unboundTargets.length];
        
        //populate vals to Match
        for (int i=0; i<unboundTargets.length; i++) {
            valsToMatch[i] = new ArrayList();
            matchedVals[i] = new HashSet();
            Number val = unboundTargets[i].getMin();
            for (int j=0; j<unboundTargets[i].getSize(); j++) {
                if (val instanceof MutableNumber) {
                    val = ((MutableNumber)val).toConst();
                }
                if (!valsToMatch[i].contains(val)) {
                    valsToMatch[i].add(val);
                }
                val = unboundTargets[i].getNextHigher(val);
            }
            if (val instanceof MutableNumber) {
                val = ((MutableNumber)val).toConst();
            }
            if (!valsToMatch[i].contains(val)) {
                valsToMatch[i].add(val);
            }
        }
        
        //populate available values
        ArrayList availVals = new ArrayList();
        //TODO acount for values that have no max, but are in domains
        for (int i=0;i<vals.length;i++){
            for(int j=0; j<count[i].getMax().intValue();j++) {
                availVals.add(vals[i]);
            }
        }
        
        Iterator numIter = boundVals.iterator();
        while (numIter.hasNext()) {
            availVals.remove(numIter.next());
        }
        
        
        
        
        boolean matchingFound=false;
        //Next we will try make sure all values are part of a valid combination.  We need to only check values remaining
        for (int i=0; i< unboundTargets.length; i++) {
            //We want to either match every value, or remove it.
            
            
            while (valsToMatch[i].size()>0) {
                
                //grab the current first remaining element
                Number num = (Number) valsToMatch[i].get(0);
                if (num instanceof MutableNumber) {
                    num = ((MutableNumber)num).toConst();
                }
                if (boundVals.contains(num) || !match(valsToMatch, matchedVals, num , i, new Number[unboundTargets.length], availVals)) {
                    try {
                        unboundTargets[i].removeValue(num);
                    }
                    catch (PropagationFailureException pfe) {
                        throw new PropagationFailureException("Attempted to remove last possible variable value");
                    }
                    valsToMatch[i].remove(num);
                }
                else {
                    matchingFound = true;
                }
            }
        }
        
        if (matchingFound == false) {
            System.out.println("No valid matchings found");
            for (int i=0; i< targets.length; i++) {
                System.out.println(i+": "+targets[i]);
            }
            throw new PropagationFailureException("No valid matchings found");
        }
        else {
            for (int i=0; i<targets.length; i++) {
                targetSize[i] = targets[i].getSize();
            }
            for (int i=0; i<count.length; i++) {
            	ub[i] = count[i].getMax().intValue();
            	lb[i] = count[i].getMin().intValue();
            }
        }
    }
    
    /**
     * This will return whether a matching exists when assigning the Number num to Assign to the variable at index indexToAssign
     * @param valsToMatch - These are the values of the variables that need to be part of a matching yet, thus one should try to use these values first
     * @param matchedVals - These are values that have been found to be part of a valid matching
     * @param numToAssign - THe number to assigne the variable located at indexToAssignTo
     * @param indexToAssignTo - The index of the variable that you are trying to match on this current pass
     * @param assignment - It is the number assigned to the variable at its resepctive index
     * @param usedNums - A list of numbers that have already been assigned to variables so as not to assign a number twice
     * @return true if such a matching exists, false elsewise
     */
    private boolean match(ArrayList[] valsToMatch, HashSet[] matchedVals, Number numToAssign, int indexToAssignTo, Number[] assignment, ArrayList availNums){
        
        //TODO:  improvments may be made with early exit condtions etc. investigate these
        //IE:  If there are three to assign yet and three that are able to be assign, assign more restricted variables first.
        //Perhaps use a TreeSet sorted on size instead of an array, that way we are always assigning the most restricted first, meaning we will 
        //get failure / success faster  since there are fewer choicepoints at high Levels perhaps a special object to maintain such info
        
        
        //Assign this number, and add it to the list of assigned numbers
        assignment[indexToAssignTo] = numToAssign;
        availNums.remove(numToAssign);
        
        boolean isAssignement = true;
        for (int i=0; i<assignment.length; i++) {
            if (assignment[i]==null) {
                isAssignement = false;
                break;
            }
        }
        //If this is the case, we have a valid matching
        if (isAssignement){
            //Remove elements of matched set from vals to match, and place them in matched Vals
            for (int i=0; i<assignment.length; i++) {
                valsToMatch[i].remove(assignment[i]);
                matchedVals[i].add(assignment[i]);
                availNums.add(assignment[i]);
            }
            return true;
        }
        //        //We will try to significantly reduce backoff time by going in order of most remaining vals
        //        int nextIndexToAssignTo = -1;
        //        double minVal = Double.MAX_VALUE;
        //        for (int i=0; i<assignment.length; i++) {
        //            if (assignment[i]==null){
        //                double counter=0;
        //	            for (int j=0; j< valsToMatch[i].size(); j++) {
        //	                if (!usedNums.contains(valsToMatch[i].get(j))) {
        //	                    counter +=1.1;
        //	                }
        //	            }
        //	            Iterator matchedIter = matchedVals[i].iterator();
        //	            while (matchedIter.hasNext()) {
        //	                if (!usedNums.contains(matchedIter.next())) {
        //	                    counter +=1.0;
        //	                }
        //	            }
        //	            if (counter<minVal) {
        //	                minVal=counter;
        //	                nextIndexToAssignTo = i;
        //	            }
        //            }
        //        }
        int nextIndexToAssignTo = (indexToAssignTo+1)%valsToMatch.length;
        //We try the next elements valsToMatch yet first, hoping to use it in a matching
        Iterator numIter = valsToMatch[nextIndexToAssignTo].iterator();
        while(numIter.hasNext()){
            Number num = (Number)numIter.next();
            if (num instanceof MutableNumber) {
                num = ((MutableNumber)num).toConst();
            }
            //If this hasnt been used yet, we try this
            if (availNums.contains(num)) {
                //We return true if this works, otherwise we try the next value
                if(match(valsToMatch, matchedVals, num, nextIndexToAssignTo, assignment, availNums)) {
                    return true;
                }
                //remove the num from the current assignment
                else {
                    availNums.add(num);
                    assignment[nextIndexToAssignTo]=null;
                }
            }
        }
        //Next we try the already matched vals, in hopes to find any sort of matching that works at this point
        numIter = matchedVals[nextIndexToAssignTo].iterator();
        while(numIter.hasNext()){
            Number num = (Number)numIter.next();
            //If this hasnt been used yet, we try this
            if (availNums.contains(num)) {
                //We return true if this works, otherwise we try the next value
                if(match(valsToMatch, matchedVals, num, nextIndexToAssignTo, assignment, availNums)) {
                    return true;
                }
                //              remove the num from the current assignment
                else {
                    availNums.add(num);
                    assignment[nextIndexToAssignTo]=null;
                }
            }
        }
        availNums.add(numToAssign);
        return false;
    }
    
    /**
     * This method will remove all values that from all targets that can show up a maximum of zero times
     * @throws PropagationFailureException
     */
    private void removeZeros() throws PropagationFailureException {
        for (int i=0; i<count.length; i++) {
            if (count[i].getMax().intValue()==0){
                for (int j=0; j<targets.length; j++){
                    ((NumNode)targets[j]).removeValue(vals[i]);
                }
            }
        }
        removeZeros=false;
    }
    
    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException { 
    	propagate();
//    	if (removeZeros) {
//            removeZeros();
//        }
//        switch(strength) {
//            case CspAlgorithmStrength.BOUNDS_CONSISTENCY :
//                this.propagateNumNodeBoundUB((NumNode)src, toNumNodeArray(targets));
//            this.propagateLowerBound((NumNode)src,toNumNodeArray(targets));
//            break;
//            case CspAlgorithmStrength.RANGE_CONSISTENCY :
//                this.propagateNumNodeRangeUB((NumNode)src, toNumNodeArray(targets));
//            this.propagateLowerBound((NumNode)src,toNumNodeArray(targets));
//            default:
//                this.propagateNumNodeDomainUB(toNumNodeArray(targets));
//            this.propagateLowerBound((NumNode)src,toNumNodeArray(targets));
//        }
        
    }
    
    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
    	NumNode[] numTargets = toNumNodeArray(targets);
    	if ((strength==CspAlgorithmStrength.ARC_CONSISTENCY)||(strength==CspAlgorithmStrength.HYPER_ARC_CONSISTENCY)) {
    		this.propagateNumNodeDomainUB(numTargets);
    		return;
    	}
    	//Otherwise we will check to see what cals need to be propagated for
    	int[] tempTargetsSize = new int[targetSize.length];
        int[] tempUB = new int[ub.length];
        int[] tempLB = new int[lb.length];
        int minTotal = 0;
    	for (int i=0; i<count.length; i++) {
    		tempUB[i] = count[i].getMax().intValue();
    		tempLB[i] = count[i].getMin().intValue();
    		minTotal += count[i].getMin().intValue();
        }
    	
    	//Readjust maximum, this way we have tightest constraints possible
    	if (minTotal<targets.length) {
    		for (int i=0;i<count.length;i++) {
    			count[i].setMax(new Integer(count[i].getMin().intValue()+(targets.length-minTotal)));
    			tempUB[i] = count[i].getMax().intValue();
    		}
    	}
    	
        for (int i=0; i<targets.length; i++) {
        	tempTargetsSize[i] = targets[i].getSize();
        }
        	
        HashSet valsUB = new HashSet();
        HashSet valsLB = new HashSet();
        boolean propUB=false;
        boolean propLB=false;
        
        for (int i=0; i<count.length; i++) {
        	if (tempUB[i]<ub[i]) {
        		propUB=true;
        		if (tempUB[i]==0) {
        			removeZeros=true;
        		}
        		valsUB.add(vals[i]);
        	}
        	if (tempLB[i]>=lb[i]) {
        		propLB=true;
        		valsLB.add(vals[i]);
        	}
        }
        
        for (int i=0; i<targets.length; i++) {
        	if (tempTargetsSize[i]!=targetSize[i]) {
        		valsUB.add((new MutableNumber((((NumNode)targets[i]).getMax().intValue()+((NumNode)targets[i]).getMin().intValue())/2)).toConst());
        		propUB=true;
        		propLB=true;
        		NumberIterator numIter = ((NumNode)targets[i]).deltaValues();
                while (numIter.hasNext()){
                	valsLB.add(new Integer(numIter.next().intValue()));
                }
        	}
        }
        
    	//Now we check to see what all needs to be propagated
    	if (removeZeros) {
            removeZeros();
        }
    	
    	if (propUB) {
	    	//Next we perform the remove due to upper bound
	        if (strength==CspAlgorithmStrength.BOUNDS_CONSISTENCY) {
	            Iterator numIterator = valsUB.iterator();
	            while (numIterator.hasNext()) {
	            	this.propagateNumNodeBoundUB((Number)numIterator.next(), numTargets);
	            }
	        }
	        else if (strength==CspAlgorithmStrength.RANGE_CONSISTENCY) {
	        	this.propagateNumNodeRangeUB(valsUB, numTargets);
	        }
    	}
    	if (propLB) {
	        //Now we perform lower bound propagation
	        this.propagateLowerBound((Number[])valsLB.toArray(new Number[0]),numTargets);
    	}
    	//Reset values so that we know next time wether or not iteration is worth it
    	for (int i=0; i<targets.length; i++) {
            targetSize[i] = targets[i].getSize();
        }
        for (int i=0; i<count.length; i++) {
        	ub[i] = count[i].getMax().intValue();
        	lb[i] = count[i].getMin().intValue();
        }
    }
    
    public class NumNodeMaxComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((NumNode)o1).getMax().intValue()-((NumNode)o2).getMax().intValue();
        }
    }
    
    public class NumNodeComparator implements Comparator {
        Number val;
        public NumNodeComparator(Number valToCompare){
            val=valToCompare;
        }
        //This method compares two NumNode objects based on the sum of the absoulte value difference between val and the NumNodes extrema 
        public int compare(Object o1, Object o2) {
            //Calc diff 1
            double diff1 = Math.abs(val.doubleValue()-((NumNode)o1).getMin().doubleValue());
            diff1+= Math.abs(val.doubleValue()-((NumNode)o1).getMax().doubleValue());
            //Calc diff
            double diff2 = Math.abs(val.doubleValue()-((NumNode)o2).getMin().doubleValue());
            diff2+= Math.abs(val.doubleValue()-((NumNode)o2).getMax().doubleValue());
            if (diff1<diff2) {
                return -1;
            }
            else if (diff1==diff2) {
                return 0;
            }
            else{
                return 1;
            }
        }
    }
    public class NumNodeSizeComparator implements Comparator {
        HashSet vals;
        public NumNodeSizeComparator(HashSet valsToCompare){
            vals = valsToCompare;
        }
        //Compare by rank, where rank is the number of elements not in vals
        public int compare(Object o1, Object o2) {
            int rank1= ((NumNode)o1).getSize();
            int rank2= ((NumNode)o2).getSize();
            
            Number thisVal =((NumNode)o1).getMin(); 
            for (int i=0; i<((NumNode)o1).getSize(); i++){ 
                if (vals.contains(thisVal)) {
                    rank1--;
                }
                thisVal=((NumNode)o1).getNextHigher(thisVal);
            }
            
            //Here we see if the value is contained in object1, we first check if it is the max to avoid costly iteration
            thisVal =((NumNode)o2).getMin(); 
            for (int i=0; i<((NumNode)o2).getSize(); i++){ 
                if (vals.contains(thisVal)) {
                    rank2--;
                }
                thisVal=((NumNode)o2).getNextHigher(thisVal);
            }
            
            if ((rank1-rank2)==0) {
                return ((NumNode)o1).getSize()-((NumNode)o2).getSize();
            }
            else {
                return rank1-rank2;
            }
        }
    }
    
    public int compare(Object o1, Object o2) {
        int size1= ((NumNode)o1).getSize();
        int size2= ((NumNode)o2).getSize();
        return size1-size2;
    }
}
