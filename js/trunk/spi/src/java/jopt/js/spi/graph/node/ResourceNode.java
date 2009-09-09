package jopt.js.spi.graph.node;

import jopt.csp.spi.arcalgorithm.graph.node.AbstractNode;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.DomainChangeType;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.domain.resource.ResourceDomain;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.spi.util.IDStore;
/**
 * Node to represent resources
 * 
 * @author jboerkoel
 */
public class ResourceNode extends AbstractNode {
    protected ResourceDomain domain;
    private int id;
    
    public static final int UNARY = 0;
    public static final int DISCRETE = 1;
    
    /**
     * Constructs a resource node from a name and domain
     * @param name name of this resource
     * @param domain domain for this node
     */
    public ResourceNode(String name, ResourceDomain domain) {
    	super(name);
    	this.domain = domain;
    	this.id = IDStore.generateUniqueID();
    }
    
    /**
     * Constructs a resource node with given name, domain, and id
     * @param name name of this resource
     * @param domain domain of this resource
     * @param id id for this resource
     */
    public ResourceNode(String name, ResourceDomain domain, int id) {
    	super(name);
    	this.domain = domain;
    	this.id = id;
    }
    
    /**
     * Indicates the time during which the specified operation may be using this resource
     * @param operationID id of operation
     * @param timeline times potentially being used
     * @throws PropagationFailureException
     */
    public void setPotentialOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
    	domain.setPotentialOperationTimeline(operationID, timeline);
    	if (domain.needsPropagation()) {
    		fireChangeEvent(DomainChangeType.DOMAIN);
    	}
    	domain.setNeedsPropagation(false);
	}
    
    /**
     * Registers the amount of time the specified operation will be using this resource
     * @param operationID id of operation
     * @param timeline times being used
     * @throws PropagationFailureException
     */
    public void setActualOperationTimeline(int operationID, IntIntervalSet timeline) throws PropagationFailureException{
    	domain.setActualOperationTimeline(operationID,timeline);
    	if (domain.needsPropagation()) {
    		fireChangeEvent(DomainChangeType.DOMAIN);
    	}
    	domain.setNeedsPropagation(false);
	}
    
	 /**
	 * This method will return the total amount of resource available.  That is,
	 * the sum of the capacity over time.
	 * @return sum of available capacity over time
	 */
	public int getTotalCapacityAvailable() {
		return domain.getTotalCapacityAvailable();
	}
	
    /**
     * Returns the type of resource that this node represents
     * @return int type of resource that this node represents
     */
    public int getType() {
    	if (domain instanceof UnaryResourceDomain) {
    		return UNARY;
    	}
    	else if (domain instanceof DiscreteResourceDomain) {
    		return DISCRETE;
    	}
    	else {
    		return Integer.MAX_VALUE;
    	}
    }
    
    /**
     * Returns the ID of this resource
     * @return  id of this resource
     */
    public int getID() {
    	return id;
    }
    
    /**
     * Sets the id of this resource
     * @param id new id
     */
    public void setID(int id) {
    	this.id=id;
    }
    
    /**
     * Lets the resource know that although a specific time has not necessary been decided, the specified
     * operation will use this resource.  This allows the resource to account for operations that have been
     * assigned to the resource but not completely bound
     * @param operationID operation id of operation using resource
     * @param est earliest start time
     * @param let latest end time
     * @param durMin minimum duration
     */
    public void registerAllocatedOperation(int operationID, int est, int let, int durMin) throws PropagationFailureException {
    	domain.registerAllocatedOperation(operationID, est, let, durMin);
    	fireChangeEvent(DomainChangeType.DOMAIN);
    }
	
	/**
	 * Returns a set indicating the intervals that this resource is available in the given quantity
     * within the specified range
     * @param start start of interval inquired about
     * @param end end of interval inquired about
     * @param quantity amound of resource required
     * @return An IntInterval set that represents the intervals that this resource is available in the given quantity for the specified interval
	 */
	public IntIntervalSet findAvailIntervals(int start, int end, int quantity) {
		return domain.findAvailIntervals(start,end,quantity);
	}
	
	/**
	 * Returns a set indicating the intervals that this resource is available to the given operation 
     * in the given quantity within the specified range
     * @param operationID operation for whome we are checking the availability
     * @param start start of interval inquired about
     * @param end end of interval inquired about
     * @param quantity amound of resource required
     * @return An IntInterval set that represents the intervals that this resource is available in the given quantity for the specified interval
	 */
	public IntIntervalSet findAvailIntervals(int operationID, int start, int end, int quantity) {
		return domain.findAvailIntervals(operationID,start,end,quantity);
	}
	
	/**
	 * Determines whether the resource is available in the specified quantity
     * between specified start and end times for the given operation
     * @param operationID the operationID of the operation in question
     * @param start start of interval being inquired about
     * @param end end of interval being inquired about
     * @param quantity quantity of resource needed
     * @return true if resource is available in quantity specified between start and end
	 */
	public boolean isResourceAvailable(int operationID,int start, int end, int quantity) {
		return domain.isResourceAvailable(operationID, start,end,quantity);
	}
	
	/**
	 * Gives the highest consistently available amount of this resource over the given range
     * @param start start of range being inquired about
     * @param end end of range being inquired about
     * @return returns the highest consistently available amount of this resource over the given range,
     *   that is, over the range, there is at least X left at all times
	 */
	public int maxAvailableResource(int start, int end) {
		return domain.maxAvailableResource(start,end);
	}
    
	/**
	 * Gives the highest consistently available amount of this resource over the given range
     * for the given operation
     * @param operationID the operationID of the operation in question
     * @param start start of range being inquired about
     * @param end end of range being inquired about
     * @return the highest consistently available amount of this resource
	 */
	public int maxAvailableResource(int operationID, int start, int end) {
		return domain.maxAvailableResource(operationID, start,end);
	}
	
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
	public int maxAvailableResource(int operationID, int start, int end, int minDur) {
		return domain.maxAvailableResource(operationID, start,end, minDur);
	}
	
	/**
	 * Returns true since this is always in a valid state by the nature of the domain
	 */
    public boolean isBound() {
    	return domain.isBound();
    }
    
    // javadoc is inherited
    public boolean isInDomain(Object val){
    	throw new UnsupportedOperationException("Operation not currently supported");
    }

    // javadoc is inherited
    public int getSize() {
    	throw new UnsupportedOperationException("Operation not currently supported");
    }
    
    // javadoc is inherited
    public Object getState() {
    	return domain.getDomainState();
    }
    
    // javadoc is inherited
    public void restoreState(Object state) {
    	domain.restoreDomainState(state);
    }
    
    // javadoc is inherited
    public void clearDelta() {
    	domain.clearDelta();
    }
    
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	if (!choicePointStackSet()) {
    		((ChoicePointDataSource) domain).setChoicePointStack(cps);
    	}
    }
    
    // javadoc is inherited
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) domain).choicePointStackSet();
    }

    // javadoc is inherited
    public String toString() {
    	return domain.toString();
    }

}