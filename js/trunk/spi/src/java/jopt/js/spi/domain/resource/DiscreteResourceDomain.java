package jopt.js.spi.domain.resource;

import jopt.csp.spi.solver.ChoicePointMultiIntArray;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.js.api.variable.Resource;

import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * Extension of a standard resource domain.
 * 
 * Maintains the available amount of the resource with respect to time.  
 * It will also track how much of a resource could potentially be used.  
 * 
 * @author James Boerkoel
 */
public class DiscreteResourceDomain extends ResourceDomain {
    
	//Data structures to maintain the amount of this resource that is still up for grabs 
	private IntValIntervalSet amountRemaining;
	ChoicePointMultiIntArray remainingChanges;
	
	/**
	 * Constructs a resource domain
	 * @param startTime earliest time that this resource is available
	 * @param endTime latest time that this resource is available
	 * @param resourceQuantity the quantity in which this resource is available
	 */
	public DiscreteResourceDomain(int startTime, int endTime, int resourceQuantity){
		super(startTime, endTime);
		amountRemaining= new IntValIntervalSet();
		amountRemaining.add(startTime, endTime, resourceQuantity);
	}
	
	/**
	 * Constructs a resource domain
	 * @param startTime earliest time that this resource is available
	 * @param endTime latest time that this resource is available
	 * @param resourceQuantity the quantity in which this resource is available
	 */
	public DiscreteResourceDomain(int startTime, int endTime, int resourceQuantity[]){
		super(startTime, endTime);
		amountRemaining = new IntValIntervalSet();
		for (int i=0; i<resourceQuantity.length; i++) {
			amountRemaining.add(startTime+i,startTime+i, resourceQuantity[i]);
		}
	}
	
	//helper constructor for cloning
	private DiscreteResourceDomain(DiscreteResourceDomain rd) {
		super(rd.getResourceStart(), rd.getResourceStart());
		this.amountRemaining = (IntValIntervalSet)rd.amountRemaining.clone();	
	}
	
	/**
	 * Returns an int representing the type of this resource
	 * @return Resource.DISCRETE
	 */
	public int getType() {
		return Resource.DISCRETE;
	}
	
	//The discrete domain does not really need this information
	public void registerAllocatedOperation(int opID, int est, int let, int minDur) {
	}

	// javadoc is inherited
	public IntIntervalSet findAvailIntervals(int start, int end, int quantity) {
		if (isBuilt()) {
			if (start < beginTime.getMin()) {
				start = beginTime.getMin();
			}
			if (end > completionTime.getMax()) {
				end = completionTime.getMax();
			}
		}
		if (!isResourceAvailable()) {
			return new IntIntervalSet();
		}
		return amountRemaining.getAllRangesWithMinWorth(quantity,start,end);
	}
	
    // javadoc is inherited
	public IntIntervalSet findAvailIntervals(int operationID,int start, int end, int quantity) {
		if (isBuilt()) {
			if (start < beginTime.getMin()) {
				start = beginTime.getMin();
			}
			if (end > completionTime.getMax()) {
				end = completionTime.getMax();
			}
		}
		if (!isResourceAvailableToOperation(operationID)) {
			return new IntIntervalSet();
		}
		IntValIntervalSet totalAvail = new IntValIntervalSet();
		totalAvail.add(amountRemaining);
		totalAvail.add(actualUsage.getTimelineForID(operationID));
		return totalAvail.getAllRangesWithMinWorth(quantity,start,end);
	}
	
	// javadoc is inherited
	public boolean isResourceAvailable(int operationID, int start, int end, int quantity) {
		if (isBuilt()&&((start < beginTime.getMin())||(end > completionTime.getMax()))) {
			return false;
		}
		if (!isResourceAvailableToOperation(operationID)) {
			return false;
		}
		int maxOverInterval = amountRemaining.getMinWorthOverRange(start,end);
		return (maxOverInterval>=quantity);
	}
	
	//We use the reports from the method to update the amountRemaining
	protected void registerUsageChange(int operationID,IntValIntervalSet delta)  {
		amountRemaining.remove(delta);
		if (remainingChanges!=null) {
			int intIdx = delta.getFirstIntervalIndex();
			while (intIdx >= 0) {
				int start = delta.getIntervalStart(intIdx);
				int end = delta.getIntervalEnd(intIdx);
				int capacity = delta.getWorth(intIdx);
				
				intIdx = delta.getNextIntervalIndex(intIdx);
				//Record change
				int index = remainingChanges.add(0,start);
				remainingChanges.set(1,index,end);
				remainingChanges.set(2,index,capacity);
				remainingChanges.set(3,index,operationID);
			}
		}
	}
	
	/**
	 * This method will return the total amount of resource available.  That is,
	 * the sum of the capacity over time.
	 * @return sum of available capacity over time
	 */
	public int getTotalCapacityAvailable() {
		int sum=0;
		int index = amountRemaining.getFirstIntervalIndex();
		while(index>=0) {
			int worth = amountRemaining.getWorth(index);
			int start = amountRemaining.getIntervalStart(index);
			int end = amountRemaining.getIntervalEnd(index);
			sum += ((end-start)+1)*worth;
			index = amountRemaining.getNextIntervalIndex(index);
		}
		return sum;
	}
    
    // javadoc is inherited
    public int maxAvailableResource(int start, int end) {
        if (isBuilt()&&((start < beginTime.getMin())||(end > completionTime.getMax()))) {
            return 0;
        }
        if (!isResourceAvailable()) {
            return 0;
        }
        return amountRemaining.getMinWorthOverRange(start,end);
    }
	
    // javadoc is inherited
	public int maxAvailableResource(int operationID, int start, int end) {
		if (isBuilt()&&((start < beginTime.getMin())||(end > completionTime.getMax()))) {
			return 0;
		}
		if (!isResourceAvailableToOperation(operationID)) {
			return 0;
		}
		IntValIntervalSet totalAvail = new IntValIntervalSet();
		totalAvail.add(amountRemaining);
		totalAvail.add(actualUsage.getTimelineForID(operationID));
		return totalAvail.getMinWorthOverRange(start,end);
	}
    
    // javadoc is inherited
    public int maxAvailableResource(int operationID, int start, int end, int minDur) {
        if ((1+end - start) < minDur) {
            return 0;
        }
        IntValIntervalSet totalAvail = (IntValIntervalSet)amountRemaining.clone();
        totalAvail.add(actualUsage.getTimelineForID(operationID));

        int index = totalAvail.indexOfValue(start);
        if (index<0) {
            index = -index-1;
        }
        int endIndex = totalAvail.indexOfValue(end);
        if (endIndex<0) {
            endIndex = -endIndex+1;
        }

        //fill indices and worthes with relative data
        ArrayIntList indices = new ArrayIntList();
        ArrayIntList worthes = new ArrayIntList();
        while (index>=0) {
            indices.add(index);
            worthes.add(totalAvail.getWorth(index));
            index = totalAvail.getNextIntervalIndex(index);
            if (index==endIndex) {
                break;
            }
        }

        int maxValue = 0;
        for (int i=0; i<indices.size(); i++) {
            index = indices.get(i);
            int val = worthes.get(i);
            //if this val is better than the previous best, we see if there is a big enough interval for this to work
            if (val > maxValue) {
                int startValIndex = i;
                int endValIndex = i;
                //If consecutive intervals have at least the same val, it would still be enough to give us a max of val
                while ((startValIndex>0)&&(val<=worthes.get(startValIndex))) {
                    startValIndex--;
                }
                while ((endValIndex+1<worthes.size())&&(val<=worthes.get(endValIndex))) {
                    endValIndex++;
                }
                int startInt = totalAvail.getIntervalStart(indices.get(startValIndex));
                int endInt = totalAvail.getIntervalEnd(indices.get(endValIndex));
                if (startInt < start) {
                    startInt = start;
                }
                if (endInt < end) {
                    endInt = end;
                }
                //in this case, we know that there is enough resource at val or higher to cover at least minDur of need
                if ((endInt - startInt) > minDur) {
                    maxValue = val;
                }
            }
        }
        return maxValue;    
    }
	
	//gets the intervals already allocated to operation with operationID
	private IntValIntervalSet getIntervalForID(int operationID) {
		return actualUsage.getTimelineForID(operationID);
	}
	
	/**
	 * Checks to see if allocating this resource to specified operationID would violate the numOperations extremes or not
	 * @param operationID id of operation attempting to be added
	 * @return true if there is room to add this opearation id with respect to numOperations
	 */
	private boolean isResourceAvailableToOperation(int operationID) {
		if (!isBuilt()) {
			return true;
		}
		//We check to see first if the operation has already been assigned to the resource in the past
		IntIntervalSet iis = actualUsage.getTimelineForID(operationID);
		if ((iis!=null)&&(!iis.isEmpty())) {
			return isResourceAvailable();
		}
		else {
			return ((getNumberOfOperationsAssigned()+1)<=numOperations.getMax());
		}
	}
	
	/**
	 * Checks to see if allocating this resource to specified operationID would violate the numOperations extremes or not
	 * @return true if there is room to add this opearation id with respect to numOperations
	 */
	private boolean isResourceAvailable() {
		if (!isBuilt()) {
			return true;
		}
		return (getNumberOfOperationsAssigned()<=numOperations.getMax());
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
        if (cpsSet && this.cpdata!=null) {
            throw new IllegalStateException("Choice point stack already set for domain");
        }
        if (cps==null) {
        	throw new IllegalStateException("Cannot set a null CPS");
        }

        // TODO: set the choice point stack on internal utilities
        
        this.cpdata = cps.newNumStackSet(this);
        this.remainingChanges= cpdata.newMultiIntList(4);
        
        if (cps != null)
            cpsSet = true;
        
        else
            cpsSet = false;
    }
    
    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
        for (int i=0; i<remainingChanges.size(); i++) {
        	amountRemaining.add(remainingChanges.get(0,i),remainingChanges.get(1,i),remainingChanges.get(2,i));
        	IntValIntervalSet ivis = getIntervalForID(remainingChanges.get(3,i));
        	ivis.remove(remainingChanges.get(0,i),remainingChanges.get(1,i),remainingChanges.get(2,i));
        }
        if (remainingChanges.size()<=0) {
        	remainingChanges.add(0,Integer.MIN_VALUE);
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
    	//    	 restore altered data
    	if (!(remainingChanges.size()==1) || remainingChanges.get(0,0)!=Integer.MIN_VALUE) {
	        for (int i=0; i<remainingChanges.size(); i++) {
	        	amountRemaining.remove(remainingChanges.get(0,i),remainingChanges.get(1,i),remainingChanges.get(2,i));
	        	IntValIntervalSet ivis = getIntervalForID(remainingChanges.get(3,i));
	        	ivis.add(remainingChanges.get(0,i),remainingChanges.get(1,i),remainingChanges.get(2,i)); 
	        }
        }
    }
    
    
    // javadoc is inherited
    public Object clone() {
        return new DiscreteResourceDomain(this);
    }
    
    // javadoc is inherited
    public String toString() {
    	return "Resource: remaining:"+amountRemaining.toString();
    }
	
}
