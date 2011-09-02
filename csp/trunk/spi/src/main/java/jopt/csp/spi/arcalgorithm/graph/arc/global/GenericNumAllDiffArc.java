/*
 * GenericNumAllDiffArc.java
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
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;

/**
 * Generic arc that ensures all ensures all variables take on different values.
 * As each variable is bound, its value is removed from all other variables.
 * 
 * @author Nick Coleman
 * @author Jim Boerkoel
 * @version $Revision $
 */
public class GenericNumAllDiffArc extends GenericArc implements NumArc {
    // Keeps track of the sum of the sizes of all the domains involved
    // if this doesn't change, we will know that we don't have to repropagate
    // initializes to zero to guarantee that it will run on the first propagate
    // pass
    private int sumOfDomainSizes;    

    private GenericNumAllDiffArc(Node sources[], boolean isGeneric) {
        // The sources and targets are identical when doing NumAllDifferent
        super(sources, sources);
        // initialize source dependencies to nodes; all of them are domain dependencies
        sourceDependencies = new int[sources.length];
        for (int i=0; i<sources.length; i++){
            sourceDependencies[i] = DomainChangeType.DOMAIN;
        }
        // Need to calculate this value at the beginning
        // calculateSumOfDomainSizes();
        // Now we are initializing to zero instead of calculating from the beginning
    }

    private NumNode[] targetNumNodeArray() {
        NumNode[] numNodeArray = new NumNode[targets.length];
        for (int i=0; i<targets.length; i++) {
            numNodeArray[i] = (NumNode)targets[i];
        }
        return numNodeArray;
    }

    public GenericNumAllDiffArc(NumNode sources[]) {
        this(sources, false);
    }

    public GenericNumAllDiffArc(GenericNumNode source) {

        this(source.getNodes(), true);
    }


    /**
     * Handles removing the value that is bound to a source node from all
     * target nodes
     * 
     * @param src       Node that must be bound for value to be removed from targets
     * @param targets   Array of nodes that value will be removed from
     *
    private void propagateNumNodeBound(NumNode src, NumNode targets[]) throws PropagationFailureException {
        //Change this to work for all kinds
        if (src.getSize()==targets.length)
            return;
        Number avg = new MutableNumber((src.getMax().intValue()+src.getMin().intValue())/2);
        Arrays.sort(targets, new NumNodeComparator(avg));
        Number removeMin = null;
        Number removeMax = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;		
        for (int i=0; i< targets.length; i++) {
            //Here we build the maximal interval that can be removed
            if(min > targets[i].getMin().intValue()) {
                min = targets[i].getMin().intValue();
            }
            if (max < targets[i].getMax().intValue()) {
                max = targets[i].getMax().intValue();
        	}
            if ((removeMin!=null)&&(removeMax!=null)) {
                //This statement will allow more values to be removed than would normally occur.  This provides for slightly greater than 
//                if ((NumberMath.compare(targets[i].getMin(), removeMin,0,NumberMath.INTEGER)
//                    *(NumberMath.compare(removeMax,targets[i].getMax(),0,NumberMath.INTEGER)))<=0) {
                    targets[i].removeRange(removeMin, removeMax);
//                }
            }
            if ((max-min)==i) {
                removeMin= new Integer(min);
                removeMax= new Integer(max);
            }
        }
    }
     */

    /**
     * Guarantees domain consistency among all targets, requires no source since this is a hyper arc
     * @throws PropagationFailureException
     */
    @SuppressWarnings("unchecked")
	public void propagateNumNodeDomain () throws PropagationFailureException {
        // For efficiency, we cast all the targets to start with
        NumNode targets[] = targetNumNodeArray();
        
        // First we sort out only the non bound nodes
        HashSet<NumNode> tempUnboundTargets = new HashSet<NumNode>();
        HashSet<Number> boundVals = new HashSet<Number>();
        int newSizeSum=0;
        // TODO: Possible bug... it's not always true that the domains are the same size as
        // the number of targets (not everything is a sudoko puzzle!)
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
            // Accumulate count of total number of values in domains of all targets
            newSizeSum+=targets[i].getSize();
        }
    
        // If this number of possible values is no different than the last propagation, then
        // no additional values have been removed from the domain and the problem must still be
        // consistent--as it was after the last propagation.  Thus there is no need to propagate
        // again.  sumOfDomainSizes is initialized to zero which guarantees that it will be
        // propagated the first time propagate() is called.
        if (newSizeSum==sumOfDomainSizes) {
            return;
        }
    
        //If no nodes are bound, no need to propagate (this is overly strict, perhaps lighten this requirement)
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
    
        //Here we set up an array of sets that we will use to notify that we have found a
        //valid matching which this is a part of.  When a value gets matched, it gets moved
        //from the valsToMatch list to the matchedVals hashSet
		ArrayList<Number>[] valsToMatch = new ArrayList[unboundTargets.length];
		HashSet<Number>[] matchedVals = new HashSet[unboundTargets.length];
    
        //populate vals to Match
        for (int i=0; i<unboundTargets.length; i++) {
            valsToMatch[i] = new ArrayList<Number>();
            matchedVals[i] = new HashSet<Number>();
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
    
        boolean matchingFound=false;
        //Next we will try make sure all values are part of a valid combination.  We need to only check values remaining
        for (int i=0; i< unboundTargets.length; i++) {
            
            //For every value in each unbound target, we want to either match it or remove it.
            while (valsToMatch[i].size()>0) {
    
                //grab the next remaining unmatched value in the i'th unbound target
                Number num = (Number) valsToMatch[i].get(0);
                if (num instanceof MutableNumber) {
                    num = ((MutableNumber)num).toConst();
                }
                if (boundVals.contains(num) || !match(valsToMatch, matchedVals, num , i, new Number[unboundTargets.length], (HashSet<Number>)boundVals.clone())) {
                    try {
                        unboundTargets[i].removeValue(num);
                    }
                    catch (PropagationFailureException pfe) {
                        throw new PropagationFailureException();
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

        calculateSumOfDomainSizes();
    }
    
    /**
     * This will return whether a matching exists when assigning the Number num to Assign to the variable at index indexToAssign.
     * Uses recursion.
     * 
     * @param valsToMatch - These are the values of the variables that need to be part of a matching yet, thus one should try to use these values first
     * @param matchedVals - These are values that have been found to be part of a valid matching
     * @param numToAssign - THe number to assigne the variable located at indexToAssignTo
     * @param indexToAssignTo - The index of the variable that you are trying to match on this current pass
     * @param assignment - It is the number assigned to the variable at its resepctive index
     * @param usedNums - A list of numbers that have already been assigned to variables so as not to assign a number twice
     * @return true if such a matching exists, false elsewise
     */
    private static boolean match(ArrayList<Number>[] valsToMatch, HashSet<Number>[] matchedVals, Number numToAssign, int indexToAssignTo, Number[] assignment, HashSet<Number> usedNums){

        //TODO:  improvments may be made with early exit conditions etc. investigate these
        //IE:  If there are three to assign yet and three that are able to be assign, assign more restricted variables first.
        //Perhaps use a TreeSet sorted on size instead of an array, that way we are always assigning the most restricted first, meaning we will 
        //get failure / success faster  since there are fewer choicepoints at high Levels perhaps a special object to maintain such info


        //Assign this number, and add it to the list of assigned numbers
        assignment[indexToAssignTo] = numToAssign;
        usedNums.add(numToAssign);

        boolean isAssignment = true;
        for (int i=0; i<assignment.length; i++) {
            if (assignment[i]==null) {
                isAssignment = false;
                break;
            }
        }
        //If this is the case, we have a valid matching
        if (isAssignment){
            //Remove elements of matched set from vals to match, and place them in matched Vals
            for (int i=0; i<assignment.length; i++) {
                valsToMatch[i].remove(assignment[i]);
                matchedVals[i].add(assignment[i]);
            }
            return true;
        }
//      //We will try to significantly reduce backoff time by going in order of most remaining vals
//      int nextIndexToAssignTo = -1;
//      double minVal = Double.MAX_VALUE;
//      for (int i=0; i<assignment.length; i++) {
//      if (assignment[i]==null){
//      double counter=0;
//      for (int j=0; j< valsToMatch[i].size(); j++) {
//      if (!usedNums.contains(valsToMatch[i].get(j))) {
//      counter +=1.1;
//      }
//      }
//      Iterator matchedIter = matchedVals[i].iterator();
//      while (matchedIter.hasNext()) {
//      if (!usedNums.contains(matchedIter.next())) {
//      counter +=1.0;
//      }
//      }
//      if (counter<minVal) {
//      minVal=counter;
//      nextIndexToAssignTo = i;
//      }
//      }
//      }
        // Increment indexToAssignTo
        // The remainder function % here wraps around to zero if the index goes above
        // valsToMatch.length.
        int nextIndexToAssignTo = (indexToAssignTo+1)%valsToMatch.length;
        //We try the next elements valsToMatch yet first, hoping to use it in a matching
        Iterator<Number> numIter = valsToMatch[nextIndexToAssignTo].iterator();
        while(numIter.hasNext()){
            Number num = (Number)numIter.next();
            if (num instanceof MutableNumber) {
                num = ((MutableNumber)num).toConst();
            }
            //If this hasn't been used yet, we try this
            if (!usedNums.contains(num)) {
                //We return true if this works, otherwise we try the next value
                if(match(valsToMatch, matchedVals, num, nextIndexToAssignTo, assignment, usedNums)) {
                    return true;
                }
                //remove the num from the current assignment
                else {
                    usedNums.remove(num);
                    assignment[nextIndexToAssignTo]=null;
                }
            }
        }
        //Next we try the already matched vals, in hopes to find any sort of matching that works at this point
        numIter = matchedVals[nextIndexToAssignTo].iterator();
        while(numIter.hasNext()){
            Number num = (Number)numIter.next();
            //If this hasnt been used yet, we try this
            if (!usedNums.contains(num)) {
                //We return true if this works, otherwise we try the next value
                if(match(valsToMatch, matchedVals, num, nextIndexToAssignTo, assignment, usedNums)) {
                    return true;
                }
//              remove the num from the current assignment
                else {
                    usedNums.remove(num);
                    assignment[nextIndexToAssignTo]=null;
                }
            }
        }

        return false;
    }
    
    private void calculateSumOfDomainSizes() {
        sumOfDomainSizes=0;
        for (int i=0; i<targets.length; i++){
            sumOfDomainSizes+=targets[i].getSize();
        }
    }

    /**
     * This method will propagate, and eliminate any ranges that it can, thus each interval that is left will be consistent.  
     * @param src  The node that changed and for which this propagation is taking place
     * @throws PropagationFailureException 
     */
    public void propagateNumNodeRange(NumNode src)throws PropagationFailureException {
        // For efficiency, we cast all the targets to start with
        NumNode targets[] = targetNumNodeArray();
        
        //In this case, there is no purpose in propagating
        if (src.getSize()==targets.length)
            return;
        //Now we collect all the values that are left in src in the HashSet src
        HashSet<Number> thisVals = new HashSet<Number>(); 
        Number cVal = src.getMin();
        for (int i =0; i<src.getSize(); i++){
            thisVals.add(cVal);
            cVal = src.getNextHigher(cVal);
        }

        //We now sort based NumNodeSizeComparator
        Arrays.sort(targets, new NumNodeSizeComparator(thisVals));
        //We thus iterate through all nodes, hopefully building Hall sets on the way, meaning any member of a Hall set can and should be removed
        HashSet<Number> visitedVals = new HashSet<Number>();
        HashSet<Number> removableVals = new HashSet<Number>();
        int counter=0;
        for (int i=0; i<targets.length; i++) {
            //In this case, no more vals will be able to be removed from subsequent nodes
            if (visitedVals.size()>=targets.length) {
                for (; i<targets.length; i++) {
                    Iterator<Number> iter = removableVals.iterator();
                    while (iter.hasNext()) {
                        Number num =(Number)iter.next();
                        targets[i].removeValue(num);
                    }        
                }
                return;
            }
            //In this case we have a hall interval, all subsequent intervals can safely remove these vals
            if (visitedVals.size()==counter){
                Iterator<Number> iter = visitedVals.iterator();
                while (iter.hasNext()) {
                    removableVals.add(iter.next());    
                }   
            }
            //Remove all removable values
            Iterator<Number> iter = removableVals.iterator();
            while (iter.hasNext()) {
                Number num =(Number)iter.next();
                targets[i].removeValue(num);
            }
            //increment counter and add all values remaining to visitedVals
            counter++;
            Number val = ((MutableNumber)targets[i].getMin()).toConst();
            for (int k=0; k<targets[i].getSize(); k++) {
                if (!removableVals.contains(val)) {
                    visitedVals.add(val);
                }
                val = ((MutableNumber)targets[i].getNextHigher(val)).toConst();
            }
        }
        calculateSumOfDomainSizes();
    }

    // javadoc inherited from Arc
    public void propagate(Node src) throws PropagationFailureException { 
        switch(strength) {
        case CspAlgorithmStrength.BOUNDS_CONSISTENCY :
//          this.propagateNumNodeBound((NumNode)src, toNumNodeArray(targets));
//          break;
        case CspAlgorithmStrength.RANGE_CONSISTENCY :
            this.propagateNumNodeRange((NumNode)src);
            break;
        default:
            this.propagateNumNodeDomain();
        break;
        }
    }

    // javadoc inherited from Arc
    public void propagate() throws PropagationFailureException {
        switch(strength) {
        case CspAlgorithmStrength.BOUNDS_CONSISTENCY :
//          for (int i=0; i<targets.length; i++){
//          this.propagateNumNodeBound((NumNode)targets[i], toNumNodeArray(targets));
//          }
//          break;
        case CspAlgorithmStrength.RANGE_CONSISTENCY :
            for (int i=0; i<targets.length; i++){
                this.propagateNumNodeRange((NumNode)targets[i]);
            }
            break;
        default:
            this.propagateNumNodeDomain();
        break;
        }
    }

    public class NumNodeComparator implements Comparator<NumNode> {
        Number val;
        public NumNodeComparator(Number valToCompare){
            val=valToCompare;
        }
        //This method compares two NumNode objects based on the sum of the absoulte value difference between val and the NumNodes extrema 
        public int compare(NumNode o1, NumNode o2) {
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

    public class NumNodeSizeComparator implements Comparator<NumNode> {
        HashSet<Number> vals;
        public NumNodeSizeComparator(HashSet<Number> valsToCompare){
            vals = valsToCompare;
        }
        //Compare by rank, where rank is the number of elements not in vals
        public int compare(NumNode o1, NumNode o2) {
            NumNode node1 = (NumNode)o1;
            NumNode node2 = (NumNode)o2;
            int rank1= node1.getSize();
            int rank2= node2.getSize();

            Number thisVal =node1.getMin(); 
            for (int i=0; i<node1.getSize(); i++){ 
                if (vals.contains(thisVal)) {
                    rank1--;
                }
                thisVal=node1.getNextHigher(thisVal);
            }

            //Here we see if the value is contained in object1, we first check if it is the max to avoid costly iteration
            thisVal =node2.getMin(); 
            for (int i=0; i<node2.getSize(); i++){ 
                if (vals.contains(thisVal)) {
                    rank2--;
                }
                thisVal=node2.getNextHigher(thisVal);
            }

            if ((rank1-rank2)==0) {
                return node1.getSize()-node2.getSize();
            }
            else {
                return rank1-rank2;
            }
        }
    }

    public class NodeSizeComparator implements Comparator<NumNode> {
        //compare strictly by size of number of values left in nodes
        public int compare(NumNode o1, NumNode o2) {
            int size1= ((NumNode)o1).getSize();
            int size2= ((NumNode)o2).getSize();
            return size1-size2;
        }
    }

}
