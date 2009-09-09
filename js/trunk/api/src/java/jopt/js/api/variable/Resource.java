package jopt.js.api.variable;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.CspIntExpr;

public interface Resource {

	public static int UNARY = 0;
	public static int DISCRETE = 1;
	
	/**
	 * Returns an int representing this resource's type
	 * @return Resource.UNARY, Resource.DISCRETE, etc.
	 */
	public abstract int getType();
	
	/**
	 * Returns the id of this resource
	 * @return id of this resource
	 */
	public abstract int getID();

	/**
	 * Returns the name of this resource
	 * @return name of this resource
	 */
	public String getName() ;

	/**
	 * Get an expression that represents the number of operations using this resource.
	 * This is especially useful for creating/defining goals.
	 * @return an expression that represents the number of operations using this resource
	 */
	public CspIntExpr getNumOperationsExpr();
	
	/**
	 * Get an expression that represents the start time of this resource.
	 * This is especially useful for creating/defining goals.
	 * @return an expression that represents the start time of this resource
	 */
	public CspIntExpr getBeginTimeExpr();

	/**
	 * Get an expression that represents the completion time of this resource
	 * This is especially useful for creating/defining goals.
	 * @return an expression that represents the completion time of this resource
	 */
    public CspIntExpr getCompletionTimeExpr() ;
    
	/**
	 * Get an expression that represents the make span of this resource
	 * This is especially useful for creating/defining goals.
	 * @return an expression that represents the make span time of this resource
	 */
    public CspIntExpr getMakeSpanExpr() ;
	
	/**
	 * Finds all the intervals within specified range that have at least the specified quantity remaining available to anyone
	 * @param start start of window being inquired about
	 * @param end end of window being inquired about
	 * @param quantity the amount of the resource that is being inquired about
	 * @return IntInternalSet indicating the intervals containing sufficient amounts of the resource
	 */
	public abstract IntIntervalSet findAvailIntervals(int start, int end,
			int quantity);

	/**
	 * Determines whether this resource is available in quantity between specified start and end times
	 * @param operationID id of operation for which the resource is needed
	 * @param start start of interval being inquired about
	 * @param end end of interval being inquired about
	 * @param quantity quantity of resource needed
	 * @return true if resource is available in quantity specified between start and end
	 */
	public abstract boolean isResourceAvailable(int operationID, int start, int end, int quantity);

	/**
	 * Gives the lowest common denominator of resource available over specified range
	 * @param start start of range being inquired about
	 * @param end end of range being inquired about
	 * @return returns the highest common denominator, that is, over the range, there is at least X left, and X is as high as it can be
	 */
	public abstract int maxAvailableResource(int start, int end);

	//javadoc inherited
	public abstract boolean isBound();
	
	/**
	 * Returns whether or not any operations are allocated to use this resource 
	 * @return true if any operation uses this operation
	 */
	public abstract boolean isUsed();
	
	/**
	 *  Gets the earliest time that this resource is available
	 *  @return earliest time this resource is available
	 */
	public abstract int getResourceStart();
	
	/**
	 *  Gets the latest time that this resource is available
	 *  @return latest time that resource is available
	 */
	public abstract int getResourceEnd();
	
	/**
	 * Sets the choicepoint stack associated with the resource
	 */
	public abstract void setChoicePointStack(ChoicePointStack cps);

	/**
	 * Returns true if a call to setChoicePointStack will fail
	 */
	public abstract boolean choicePointStackSet();
    
    /**
     * Adds nodes representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph);

}