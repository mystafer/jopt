package jopt.js.spi.domain.resource;

import jopt.csp.spi.arcalgorithm.domain.AbstractDomain;
import jopt.csp.spi.arcalgorithm.domain.IntDomain;
import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.util.IntIntervalCollection;

/**
 * Base resource domain class.  This is used as the basis of all other resource domains.
 */
public abstract class ResourceDomain extends AbstractDomain implements ChoicePointDataSource,ChoicePointEntryListener{

	//maintain the original start and end time of the availability of the resource
	protected int startTime;
	protected int endTime;
	//these data structures maintain the potential and actual usage of this resource by any and all operations
	protected IntIntervalCollection potentialUsage;
	protected IntIntervalCollection actualUsage;
	//these sub domains keep track of information such as number of operations, makeSpan, begin time, and completion time
    protected IntIntervalDomain numOperations;
    protected IntIntervalDomain makeSpan;
    protected IntIntervalDomain beginTime;
    protected IntIntervalDomain completionTime;
	
	//keeps track of whether of not the problem - as a whole - has been built
	private boolean isBuilt;
	
	//choice point information
	protected ChoicePointNumArraySet cpdata;
	protected boolean cpsSet;
	
	//flag to determine whether or not this domain has incurred changes worthy of propagation
	protected boolean needsPropagation; 
	
	/**
	 * Super class constructor that will initialize data
	 * @param startTime earliest time resource is available
	 * @param endTime latest time resource is available
	 */
	protected ResourceDomain(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		potentialUsage = new IntIntervalCollection();
		actualUsage = new IntIntervalCollection();
		numOperations = new IntIntervalDomain(0, Integer.MAX_VALUE);
		makeSpan = new IntIntervalDomain(0, (endTime - startTime));
		completionTime = new IntIntervalDomain(startTime, endTime);
		beginTime = new IntIntervalDomain(startTime, endTime);
        needsPropagation = true;
	}
	
	
	/**
	 * Determines if this domain is in "built" status
	 * @return true if domain is in built status
	 */
	public boolean isBuilt() {
		return isBuilt;
	}

	/**
	 * Sets the built status of this domain
	 * @param built boolean indication whether or not the problem has been built
	 */
	public void setBuilt(boolean built) {
		this.isBuilt = built;
		actualUsage.setBuilt(built);
		potentialUsage.setBuilt(built);
	}
	
	/**
	 * Returns an IntDomain representing the number of operations that use this resource
	 * @return IntDomain representing the number of operations that use this resource
	 */
	public IntDomain getNumOps() {
		return numOperations;
	}
	
	/**
	 * Returns an IntDomain representing the length of time that this resource is in use
	 * @return IntDomain representing the length of time that this resource is in use
	 */
	public IntDomain getMakeSpan() {
		return makeSpan;
	}
	
	/**
	 * Returns an IntDomain representing the first time that this resource will actually be used
	 * @return IntDomain representing the first time that this resource will actually be used
	 */
	public IntDomain getBeginTime() {
		return beginTime;
	}
	
	/**
	 * Returns an IntDomain representing the last time that this resource will actually be used
	 * @return IntDomain representing the last time that this resource will actually be used
	 */
	public IntDomain getCompleteTime() {
		return completionTime;
	}
	
	/**
	 * Returns a set indicating the intervals that this resource is available in the given quantity
     * within the specified range
	 * @param start start of interval inquired about
	 * @param end end of interval inquired about
	 * @param quantity amound of resource required
	 * @return An IntInterval set that represents the intervals that this resource is available in the given quantity for the specified interval
	 */
	public abstract IntIntervalSet findAvailIntervals(int start, int end, int quantity);

	/**
	 * Returns a set indicating the intervals that this resource is available to the given operation 
	 * in the given quantity within the specified range
	 * @param operationID operation for whome we are checking the availability
	 * @param start start of interval inquired about
	 * @param end end of interval inquired about
	 * @param quantity amound of resource required
	 * @return An IntInterval set that represents the intervals that this resource is available in the given quantity for the specified interval
	 */
	public abstract IntIntervalSet findAvailIntervals(int operationID, int start, int end, int quantity);

    /**
     * Determines whether the resource is available in the specified quantity
     * between specified start and end times for the given operation
     * @param operationID the operationID of the operation in question
     * @param start start of interval being inquired about
     * @param end end of interval being inquired about
     * @param quantity quantity of resource needed
     * @return true if resource is available in quantity specified between start and end
     */
	public abstract boolean isResourceAvailable(int operationID, int start, int end, int quantity);

    /**
     * Gives the highest consistently available amount of this resource over the given range
     * @param start start of range being inquired about
     * @param end end of range being inquired about
     * @return returns the highest consistently available amount of this resource over the given range,
     *   that is, over the range, there is at least X left at all times
     */
	public abstract int maxAvailableResource(int start, int end);
	
    /**
     * Gives the highest consistently available amount of this resource over the given range
     * for the given operation
     * @param operationID the operationID of the operation in question
     * @param start start of range being inquired about
     * @param end end of range being inquired about
     * @return the highest consistently available amount of this resource
     */
	public abstract int maxAvailableResource(int operationID, int start, int end);
    
    /**
     * Gives the highest consistently available amount of this resource over the given range
     * for the given operation and assuming that the reource will be used for at least the
     * specified duration
     * @param operationID the operationID of the operation in question
     * @param start start of range being inquired about
     * @param end end of range being inquired about
     * @param minDur the minimum duration
     * @return the highest consistently available amount of this resource
     */
    public  abstract int maxAvailableResource(int operationID, int start, int end, int minDur) ;
	
	//javadoc inherited
	public void setChoicePointStack(ChoicePointStack cps) {
		numOperations.setChoicePointStack(cps);
		makeSpan.setChoicePointStack(cps);
		completionTime.setChoicePointStack(cps);
		beginTime.setChoicePointStack(cps);
		actualUsage.setChoicePointStack(cps);
		potentialUsage.setChoicePointStack(cps);
	}
	
	/**
	 * Returns the number of operations currently assigned to the resource
	 * @return number of operations currently assigned to the resource
	 */
	public int getNumberOfOperationsAssigned() {
		return actualUsage.count();
	}
	
	/**
	 * Indicates the time during which the specified operation may be using this resource
	 * @param operationID id of operation
	 * @param timeline times potentially being used
	 * @throws PropagationFailureException
	 */
	public void setPotentialOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
		int puSize = potentialUsage.size();
		potentialUsage.set(operationID, timeline);
		if (potentialUsage.size()!= puSize) {
			needsPropagation = true;
		}
		if (isBuilt()) {
			int count = potentialUsage.count();	
			if (count < getNumberOfOperationsAssigned()) {
				//TODO: this should not happen, investigate if/why it does
			}
			if (count < numOperations.getMax()) {
				numOperations.setMax(count);
				needsPropagation=true;
			}
			if ((count > 0)&&(needsPropagation)){
				makeSpan.setMax(potentialUsage.getMax() - potentialUsage.getMin()+1);
				completionTime.setMax(potentialUsage.getMax());
				beginTime.setMin(potentialUsage.getMin());
				
				//This will probably seldom have an effect, since actual time will be able to give much tight bounds
				completionTime.setMin(potentialUsage.getMin());
				beginTime.setMax(potentialUsage.getMax());
			}
			else if (count == 0) {
				makeSpan.setMax(0);
			}
			if (needsPropagation){
				makeConsistent();
			}
		}
	}
	
	/**
	 * Registers the amount of time the specified operation will be using this resource
	 * @param operationID id of operation
	 * @param timeline times being used
	 * @throws PropagationFailureException
	 */
	public void setActualOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
		int auSize = actualUsage.size();
		IntValIntervalSet delta = actualUsage.set(operationID, timeline);
		this.registerUsageChange(operationID,delta);
		if (auSize!=actualUsage.size()) {
			needsPropagation = true;
		}
		if (isBuilt()) {
			int count = actualUsage.count();
			
			if (numOperations.getMin()<count) {
				numOperations.setMin(count);
				needsPropagation = true;
			}

			if ((count >0)&& needsPropagation) {
				makeSpan.setMin(actualUsage.getMax() - actualUsage.getMin()+1);
				completionTime.setMin(actualUsage.getMax());
				beginTime.setMax(actualUsage.getMin());
			}
			if (needsPropagation) {
				makeConsistent();
			}
		}
	}
	
	/**
	 * Makes all the sub domains 'bounds consistent' with each other
	 * @throws PropagationFailureException
	 */
	protected void makeConsistent() throws PropagationFailureException {
		makeSpan.setMin(completionTime.getMin()-beginTime.getMax()+1);
		makeSpan.setMax(completionTime.getMax()-beginTime.getMin()+1);

		completionTime.setMin(beginTime.getMin()+(makeSpan.getMin()-1));
		completionTime.setMax(beginTime.getMax()+(makeSpan.getMax()-1));

		beginTime.setMin(completionTime.getMin()-(makeSpan.getMax()-1));
		beginTime.setMax(completionTime.getMax()-(makeSpan.getMin()-1));
	}
	
	/**
	 * Registers any actual usage change [if child classes need to know]
	 * @param operationID the operation for which the usage changed
     * @param delta the amount and intervals for which the actual usage changed
	 */
	protected abstract void registerUsageChange(int operationID, IntValIntervalSet delta) ;
	
	
	/**
	 * Returns an int representing the type of resource this is domain represents
	 * @return Resource.UNARY, Resource.DISCRETE, etc.
	 */
	public abstract int getType();
	
	/**
	 * Obtains the first point for which this resource is, or ever was, available
	 * @return int representing the first point this resource is, or ever was, available
	 */
	public int getResourceStart() {
		return startTime;
	}
	
	/**
	 * This obtains the last point for which this resource is, or ever was, available
	 * @return int representing the last point this resource is, or ever was, available
	 */
	public int getResourceEnd() {
		return endTime;
	}
	
	/**
	 * This method will return the total amount of resource available.  That is,
	 * the sum of the capacity over time.
	 * @return sum of available capacity over time
	 */
	public abstract int getTotalCapacityAvailable();
	
	/**
	 * Checks to see if any operation requires the use of this resource at any time.
	 * @return true if any operation requires the use of this resource at any point in time.
	 */
	public boolean isUsed() {
		return (!actualUsage.isEmpty());
	}
	
	/**
	 * Lets the resource know that although a specific time has not necessary been decided, the specified
     * operation will use this resource.  This allows the resource to account for operations that have been
     * assigned to the resource but not completely bound
	 * @param opID operation id of operation using resource
	 * @param est earliest start time
	 * @param let latest end time
	 * @param minDur minimum duration
	 */
	public abstract void registerAllocatedOperation(int opID, int est, int let, int minDur) throws PropagationFailureException;
	
	/**
	 * Returns true since this is always in a valid state by the nature of the domain
	 */
	public boolean isBound() {
		return true;
	}
	
	//javadoc inherited
    public String toString() {
    	return " : "+actualUsage.toString();
    }

    /**
     * Returns whether or not this class has been altered, thus rendering its arcs in need of propagation
     * @return true if this class has been altered, thus rendering its arcs in need of propagation
     */
	public boolean needsPropagation() {
		return needsPropagation;
	}

	/**
	 * Sets the nnedsPropagation flag to the given boolean
	 * @param needsPropagation boolean to set the needs Propagation flag
	 */
	public void setNeedsPropagation(boolean needsPropagation) {
		this.needsPropagation = needsPropagation;
	}
	
    // javadoc is inherited
    public void clearDelta() {
        // NOTE: current implementation does not support deltas during the propagation process
    }

    // javadoc is inherited
    public Object getDomainState() {
    	//TODO: This needs to be reviewed
    	return null;
    }

    // javadoc is inherited
    public void restoreDomainState(Object state) {
//    	TODO: This needs to be reviewed
    }

}