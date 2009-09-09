package jopt.js.spi.domain.resource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import jopt.csp.spi.solver.ChoicePointMultiIntArray;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.csp.util.SortableIntList;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.util.TransitionTimeTable;
import jopt.js.api.variable.Resource;
import jopt.js.spi.util.IntStack;

/**
 * Extension of a standard resource domain.
 * 
 * Maintains the available amount of the resource with respect to time.  
 * It will also track how much of a resource could potentially be used.  
 * 
 * @author James Boerkoel
 */
public class UnaryResourceDomain extends ResourceDomain {

    //Maintains an instance of the ttt
	private TransitionTimeTable ttt;
	//Maintains changes to the resource allocation objects
	ChoicePointMultiIntArray changedRAOs;
	//Maintains all operations that have been allocated to resource, but not necessarily to a time 
	HashSet operationsAllocatedToResource; 
	
	/**
	 * Constructs a resource domain
	 * @param startTime earliest time that this resource is available
	 * @param endTime latest time that this resource is available
	 */
	public UnaryResourceDomain(int startTime, int endTime){
		super(startTime, endTime);
		ttt = TransitionTimeTable.getInstance();
	}

	// private constructor for cloning
	private UnaryResourceDomain(UnaryResourceDomain rd) {
		super(rd.getResourceStart(), rd.getResourceEnd());
		this.startTime = rd.startTime;
		this.endTime = rd.endTime;
	}
	
	// javadoc is inherited
	public int getType() {
		return Resource.UNARY;
	}
	
    // javadoc is inherited
	public int getNumberOfOperationsAssigned() {
		SortableIntList operations = new SortableIntList();
		int[] ids = actualUsage.getUtilizedTimelineIDs();
		for (int i=0; i<ids.length; i++){
			if (!operations.contains(ids[i])) {
				operations.add(ids[i]);
			}
		}
		if (operationsAllocatedToResource!=null) {
			Iterator opIter = operationsAllocatedToResource.iterator();
			while (opIter.hasNext()) {
				ResourceAllocationObject rao = (ResourceAllocationObject)opIter.next();
				if (!operations.contains(rao.opID)) {
					operations.add(rao.opID);
				}
			}
		}
		return operations.size();
	}
	
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int start, int end, int quantity) {
		if (quantity!=1) {
			return new IntIntervalSet();
		}
		if (isBuilt()) {
			if (numOperations.getMax()-actualUsage.count() <= 0) {
				return new IntIntervalSet();
			}
			if (beginTime.getMin()>start) {
				start = beginTime.getMin();
			}
			if (completionTime.getMax()<end) {
				end = completionTime.getMax();
			}
		}
		IntIntervalSet availInt = new IntIntervalSet();
		int availStart = this.startTime;
		int availEnd = this.endTime;
		if (start>availStart) {
			availStart = start;
		}
		if (end<availEnd) {
			availEnd = end;
		}
		availInt.add(availStart,availEnd);
		
		if (actualUsage.isIntervalEmpty(start,end)) {
			return availInt;
		}
		
		int index = actualUsage.indexOfValue(start);
		if (index<0) {
			index = -index-1;
		}
		while (index>=0) {
			availInt.remove(actualUsage.getIntervalStart(index),actualUsage.getIntervalEnd(index));
			index = actualUsage.getNextIntervalIndex(index);
		}
		return availInt;
	}
	
    // javadoc is inherited
	public int getTotalCapacityAvailable() {
		return this.findAvailIntervals(this.startTime,this.endTime,1).size();
	}
	
    // javadoc is inherited
	public void registerAllocatedOperation(int opID, int est, int let, int minDur) throws PropagationFailureException{
		if (operationsAllocatedToResource==null) {
			operationsAllocatedToResource = new HashSet();
		}
		
		if (changedRAOs!=null) {
			int index = changedRAOs.add(0,opID);
			changedRAOs.set(1,index,est);
			changedRAOs.set(2,index,let);
			changedRAOs.set(3,index,minDur);
		}
		
		//we must see if there is already an entry, if so we just update that
		Iterator opIter = operationsAllocatedToResource.iterator();
		while (opIter.hasNext()) {
			ResourceAllocationObject rao = (ResourceAllocationObject)opIter.next();
			if (rao.opID==opID) {
				rao.registerNew(est,let,minDur);
				makeConsistentWithRAO();
				return;
			}
		}
		
		ResourceAllocationObject rao = new ResourceAllocationObject(opID,est,let,minDur);
		operationsAllocatedToResource.add(rao);
		makeConsistentWithRAO();
	}
	
	//makes the sub domains consistent with respect to the rao objects
	private void makeConsistentWithRAO() throws PropagationFailureException{
		numOperations.setMin(this.getNumberOfOperationsAssigned());	
		Iterator raoIter = operationsAllocatedToResource.iterator();
		int minDuration=0;
		while (raoIter.hasNext()) {
			ResourceAllocationObject rao= (ResourceAllocationObject)raoIter.next();
			minDuration += rao.getMinDur();
		}
		makeSpan.setMin(minDuration);
	}
	
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int operationID,int start, int end, int quantity) {
		if (isBuilt()) {
			//If num Op is bound, it means no new operations allocated
			if (numOperations.getMax()-actualUsage.count() <= 0) {
				actualUsage.getTimelineForID(operationID);
			}
			if (beginTime.getMin()>start) {
				start = beginTime.getMin();
			}
			if (completionTime.getMax()<end) {
				end = completionTime.getMax();
			}
		}
		if (ttt==null) {
			return findAvailIntervals(start,end,quantity);
		}
		if (end<start) {
			return new IntIntervalSet();
		}
		IntIntervalSet availInt = new IntIntervalSet();
		int availStart = this.startTime;
		int availEnd = this.endTime;
		if (start>availStart) {
			availStart = start;
		}
		if (end<availEnd) {
			availEnd = end;
		}
		
		IntValIntervalSet operationUsage = ((IntValIntervalSet)actualUsage.getTimelineForID(operationID)); 
		if (!operationUsage.isEmpty()) {
			int index = actualUsage.indexOfValue((operationUsage.getMin()));
			int prevIndex = actualUsage.getPreviousIntervalIndex(index);
			int intervalStart=availStart;
			if (prevIndex >= 0) {
				int tempStart = actualUsage.getMax(prevIndex)+1;
				if (tempStart > intervalStart) {
					intervalStart= tempStart;
				}
			}
			int nextIndex = actualUsage.getNextIntervalIndex(index);
			int intervalEnd=availEnd;
			if (nextIndex >= 0) {
				int tempEnd = actualUsage.getMin(nextIndex)-1;
				if (tempEnd < intervalEnd) {
					intervalEnd= tempEnd;
				}
			}
			if (prevIndex >= 0 ) {
				intervalStart -= ttt.getByOp(operationID,actualUsage.getWorth(prevIndex));
			}
			if (nextIndex >= 0) {
				intervalEnd += ttt.getByOp(actualUsage.getWorth(nextIndex),operationID);
			}
			availInt.add(intervalStart,intervalEnd);
			return availInt;
		}
		
		availInt.add(availStart,availEnd);
		
		if (actualUsage.isEmpty()) {
			//This performs multi consistency check
			if ((operationsAllocatedToResource!=null)&&(operationsAllocatedToResource.size()>0)) {
				makeMultiConsistent(availInt,operationID,start,end);
			}
			return availInt;
		}
		
		int operationIDs[] = actualUsage.getUtilizedTimelineIDs();
		
		for (int i=0; i<operationIDs.length; i++) {
			if (operationIDs[i] != operationID) {
			    // Here we will make an assumption that there cant really be split resource usage of a given operation
				//If this causes problems, start here first...
				int intervalStart = actualUsage.getTimelineForID(operationIDs[i]).getMin();
				int intervalEnd = actualUsage.getTimelineForID(operationIDs[i]).getMax();
				intervalStart -= ttt.getByOp(operationID,operationIDs[i]);
				intervalEnd += ttt.getByOp(operationIDs[i],operationID);
				availInt.remove(intervalStart,intervalEnd);
			}
		}
		
		//Perform multi consistency check
		if ((operationsAllocatedToResource!=null)&&(operationsAllocatedToResource.size()>0)) {
			makeMultiConsistent(availInt,operationID,start,end);
		}
		return availInt;
	}
	
	//Makes it consistent with all the other rao objects on record
	private void makeMultiConsistent(IntIntervalSet availInt, int operationID, int start, int end) {
		ResourceAllocationObject[] rao = (ResourceAllocationObject[])operationsAllocatedToResource.toArray(new ResourceAllocationObject[0]);
		Arrays.sort(rao,new RAOComparator((start+end)/2));
		
		int minimumDuration = 0;
		int est = rao[0].getEst();
		int let = rao[0].getLet();
		
		for (int i=0; i<rao.length; i++) {
			if (rao[i].opID != operationID) {
				if (rao[i].getEst()<est) {
					est = rao[i].getEst();
				}
				if (rao[i].getLet()>let) {
					let = rao[i].getLet();
				}
				if ((let>end)||(est<start)) {
					break;
				}
				minimumDuration += rao[i].getMinDur();
				if ((let-minimumDuration)+1<=(est+minimumDuration)-1) {
					availInt.remove((let-minimumDuration)+1,(est+minimumDuration)-1);
				}
				
			}
		}
	}
	
    // javadoc is inherited
	public boolean isResourceAvailable(int operationID, int start, int end, int quantity) {
		IntIntervalSet iis = null;
		if (isBuilt() && ((beginTime.getMin()>start)&&(completionTime.getMax()<end))) {
				return false;
		}
		else if (isBuilt() && numOperations.getMax()-actualUsage.count() <= 0) {
				iis = actualUsage.getTimelineForID(operationID);
		}
		else {
			iis = findAvailIntervals(operationID,start,end,quantity);
		}
		return iis.isIntervalContained(start,end);
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int start, int end) {
		if (isBuilt()) {
			if (numOperations.getMax()-actualUsage.count() <= 0) {
				return 0;
			}
			if ((beginTime.getMin()>start)||(completionTime.getMax()<end)) {
				return 0;
			}
		}
		if (actualUsage.isIntervalEmpty(start,end)) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int operationID, int start, int end) {
		if (isBuilt()) {
			if ((beginTime.getMin()>start)&&(completionTime.getMax()<end)) {
				return 0;
			}
			
			if (numOperations.getMax()-actualUsage.count() <= 0){
				if (actualUsage.getTimelineForID(operationID).isEmpty()) {
					if (actualUsage.isIntervalEmpty(start,end)) {
						return 1;
					}
					return 0;
				}
			}
		}
		if (actualUsage.isIntervalEmpty(start,end)) {
			return 1;
		}
		else {
			IntIntervalSet set = (IntIntervalSet)actualUsage.getFreeIntervalsBetween(start,end);
			set.add(actualUsage.getTimelineForID(operationID));
			if (set.isIntervalContained(start, end)) {
				return 1;
			}
			return 0;
		}
	}
	
    // javadoc is inherited
	public int maxAvailableResource(int operationID, int start, int end, int minDur) {
		if (isBuilt()) {
			if ((beginTime.getMin()>start)&&(completionTime.getMax()<end)) {
				return 0;
			}
		}
		IntIntervalSet set = (IntIntervalSet)actualUsage.getFreeIntervalsBetween(start,end);
		set.add(actualUsage.getTimelineForID(operationID));
		//Set should now contain all the available resource, thus if there is any interval of minDur size within, 
		//we can return 1, otherwise we return 0;
		int index = set.getFirstIntervalIndex();
		while( index >= 0) {
			int intStart = set.getIntervalStart(index);
			int intEnd = set.getIntervalEnd(index);
			if ((intEnd-intStart+1) >= minDur) {
				return 1;
			}
			index = set.getNextIntervalIndex(index);
		}
		return 0;
		
	}
	
    // javadoc is inherited
    public boolean choicePointStackSet() {
        if(cpsSet)
            return true;
        return false;
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	super.setChoicePointStack(cps);
    	if (cps==null) return;
        if (cpsSet && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for domain");
        }

        // TODO: set the choice point stack on internal utilities
        
        this.cpdata = cps.newNumStackSet(this);
        this.changedRAOs = cpdata.newMultiIntList(4);
        
        if (cps != null)
            cpsSet = true;
        
        else
            cpsSet = false;
    }
    
    protected void registerUsageChange(int operationID, IntValIntervalSet delta)  {
        //Unary resource domains dont care about usage change, all information should already be processed
	}
    
    // javadoc is inherited
    //  this method reveals the beauty of nesting statements :)
    public void beforeChoicePointPopEvent() {
    	//The only thing that needs to change within a unary domain is the operations allocated to resource 
    	if (operationsAllocatedToResource !=null) {    	
			ResourceAllocationObject[] raoArray = (ResourceAllocationObject[])operationsAllocatedToResource.toArray(new ResourceAllocationObject[0]);	
			for (int i=0; i<changedRAOs.size(); i++) {
				int opID = changedRAOs.get(0,i);
				for (int j=0; j<raoArray.length; j++) {
					if (raoArray[j].getOpID()==opID) {
						raoArray[j].pop();
						if (raoArray[j].isEmpty()) {
							Iterator iter = operationsAllocatedToResource.iterator();
							while (iter.hasNext()) {
								if (((ResourceAllocationObject)iter.next()).getOpID()==opID) {
									iter.remove();
									break;
								}
							}
						}
					}
				}
			}
    	}
    }
    
    // javadoc is inherited
    public void afterChoicePointPopEvent() {
        
    }
    
    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        
    } 
    
    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        //The only thing that needs to change within a unary domain is the operations allocated to resources 
        if (changedRAOs.size()>0) {
        	for (int i=0; i<changedRAOs.size(); i++) {
        		int opID = changedRAOs.get(0,i);
        		Iterator opIter = operationsAllocatedToResource.iterator();
        		boolean raoUpdated = false;
        		while (opIter.hasNext()) {
        			ResourceAllocationObject rao = (ResourceAllocationObject)opIter.next();
        			if (rao.opID==opID) {
        				rao.registerNew(changedRAOs.get(1,i),changedRAOs.get(2,i),changedRAOs.get(3,i));
        				raoUpdated = true;
        			}
        		}
        		if (!raoUpdated) {
	        		ResourceAllocationObject rao = new ResourceAllocationObject(opID,changedRAOs.get(1,i),changedRAOs.get(2,i),changedRAOs.get(3,i));
	        		operationsAllocatedToResource.add(rao);
        		}
        	}
        	
        }
    }
    
    
    public Object clone() {
        return new UnaryResourceDomain(this);
    }
    
    /**
     * Maintains a "back-trackable" history of changes to registered object allocations
     */
    private class ResourceAllocationObject {
    	public int opID;
    	public IntStack estStack;
    	public IntStack letStack;
    	public IntStack minDurStack;
    	
    	ResourceAllocationObject(int opID,int est,int let,int minDur) {
    		this.opID = opID;
    		this.estStack = new IntStack();
    		this.letStack = new IntStack();
    		this.minDurStack = new IntStack();
    		estStack.push(est);
    		letStack.push(let);
    		minDurStack.push(minDur);
    	}
    	
    	public void registerNew(int est,int let,int minDur){
    		estStack.push(est);
    		letStack.push(let);
    		minDurStack.push(minDur);
    	}
    	
    	public boolean isEmpty() {
    		return estStack.empty();
    	}
    	
    	public void pop() {
    		estStack.pop();
    		letStack.pop();
    		minDurStack.pop();
    	}

    	public int getOpID() {
    		return opID;
    	}
    	
    	public int getEst() {
    		return estStack.peek();
    	}
    	
    	public int getLet() {
    		return letStack.peek();
    	}
    	
    	public int getMinDur() {
    		return minDurStack.peek();
    	}
    	
    	public int hashCode() {
    		return opID;
    	}
    }
    
    /**
     * Compares two Resource Allocation Objects
     */
    private class RAOComparator implements Comparator {
        int mean;
        public RAOComparator(int mean){
            this.mean = mean ;
        }
        //Compares two RAO objects based on the sum of the absoulte value difference between their extrema 
        public int compare(Object o1, Object o2) {
            //Calc diff 1
            int diff1 = Math.abs(mean -((ResourceAllocationObject)o1).getEst());
            diff1+= Math.abs(mean -((ResourceAllocationObject)o1).getLet());
            //Calc diff
            int diff2 = Math.abs(mean -((ResourceAllocationObject)o2).getEst());
            diff2+= Math.abs(mean -((ResourceAllocationObject)o2).getLet());
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
}
