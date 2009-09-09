package jopt.js.api.variable;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.PropagationFailureException;

/**
 * 
 * This interface is meant to encapsulate all the behavior of an activity
 * that needs to be exposed to the end user.  Any class that implements
 * this interface will be able to set up activities, build relationships
 * between activities and resources, and also establish transition times.
 * 
 * @author James Boerkoel
 *
 */
public interface Activity {
    
    /**
     * Returns the category that has been established as the "transition to" category of this activity. This
     * int is used to determine a relationship between this activity and a previous activity.  This
     * defaults to the id of the activity; however, it can be overwritten to allow for shared behavior between two
     * similar activities, ie two activities of the same "transition from" category.  
     * 
     * @return an identifier indicating the "transition to" category
     */
    public abstract int getTransitionToCategory() ;

    /**
     * Sets the "transition to" category and makes sure to update the transition table if one has been registered
     * @param transitionToCategory new "transition to" category
     */
	public abstract void setTransitionToCategory(int transitionToCategory) ;

	/**
     * Returns the category that has been established as the "transition from" category of this activity. This
     * int is used to determine a relationship between this activity and a subsequent activity.  This
     * defaults to the id of the activity; however, it can be overwritten to allow for shared behavior between two
     * similar activities, ie two activities of the same "transition from" category.  
     * 
     * @return the transition from category
     */
	public abstract int getTransitionFromCategory() ;

	/**
	 * Sets the "transition from" category and makes sure to update the transition table if one has been registered
	 * 
	 * @param transitFromCategory new transit from category
	 */
	public abstract void setTransitionFromCategory(int transitFromCategory) ;
	
	/**
	 * Gets the number of operations that are in need of a resource assignment
	 * @return the number of operations that are in need of a resource assignment
	 */
	public int getNumUnassignedOperations();
    
    /**
     * Returns the number of resources that can still be
     * assigned to the operation with the specified id
     * @param operationId the operation for which the resource count is desired
     * @return int representing the number of available resources
     */
    public int getAvailResourceCount(int operationId);
    
    /**
     * Forces the operation with the specified operationId to use
     * the resource with the specified resourceId.
     * @param operationId the id of the operation for which we are setting the required resource
     * @param resourceId the id of the resource to be used by the operation
     * @throws PropagationFailureException if the assignment causes any errors
     */
    public void setRequiredResource(int operationId, int resourceId) throws PropagationFailureException;
    
    /**
     * Removes the resource with the specified resource id from the list
     * of possible resources for the operation with the specified operation id
     * @param operationId the id of the operation for which adjustments should be made
     * @param resourceId the id of the resource on which adjustments should be made
     * @throws PropagationFailureException if the reduction causes any errors
     */
    public void removePossibleResource(int operationId, int resourceId) throws PropagationFailureException;
	
	/**
	 * Sets the transition time from the "transition from" activity to this activity  
	 * @param transitFromAct the activity that would theoretically occur immediatly prior to this one
	 * @param transitTime the time it takes to transition to this activity from the specified activity
	 */
	public abstract void setTransitTimeFrom(Activity transitFromAct, int transitTime) ;
	
	/**
	 * Sets the transition time from this activity to the "transition to" activity  
	 * @param transitToAct the activity that would theoretically follow this activity
	 * @param transitTime the time it takes to transition from this activity to the specified activity
	 */
	public abstract void setTransitTimeTo(Activity transitToAct, int transitTime);
	
	/**
	 * Sets the transition time for the "transition from" category for this activity 
	 * @param transitFromCat the "transition from" category of the activity that would theoretically occur immediatly prior to this one
	 * @param transitTime the time it takes to transition from an activity of the specified category to this activity
	 */
	public abstract void setTransitTimeFrom(int transitFromCat, int transitTime) ;

	/**
	 * Sets the transition time for the "transition to" category for this activity  
	 * @param transitToCat the "transition to" category of the activity that would theoretically follow this one
	 * @param transitTime the time it takes to transition from this activity to an activity of the specified category
	 */
	public abstract void setTransitTimeTo(int transitToCat, int transitTime);
	
	/**
	 * Gets all sets of alternative resources that exist within this activity
	 * @return An array of all resource sets that still have multiple possibilities
	 */
	public ResourceSet[] getAllAlternativeResourceSets() ;
	
	/**
	 * Returns the activity's id
	 * @return activity's id
	 */
	public abstract int getID();
	
	/**
	 * Returns the earliest start time for this activity
	 * @return earliest start time for activity
	 */
	public abstract int getEarliestStartTime();
	
	/**
	 * Builds a constraint that forces this activity to require one of resources in the given set in the capacity specified.  
	 * @param possResourceSet an array of equivalent {@link Resource} objects used to meet the needs of the activity  
	 * @param capacity the amount of the determined resource that will be required to accomplish the task
	 * @return {@link CspConstraint} that constrains the activity appropriately
	 * @throws PropagationFailureException
	 */
	public abstract CspConstraint require(Resource[] possResourceSet, int capacity) throws PropagationFailureException;

	/**
	 * Creates and adds the resource to the specified operation
	 * @param operationID id of operation to which we are adding the resource
	 * @param resourceID id of resource being added
	 * @param start earliest start time of resource
	 * @param end latest start time of resource
	 * @param minDur minimum duration of this 
	 * @param maxDur maximum duration
	 * @throws PropagationFailureException
	 */
	public abstract void addResource(int operationID, int resourceID,
			int start, int end, int minDur, int maxDur)
			throws PropagationFailureException;

	/**
	 * Returns an expression indicating the possible start times for this activity
	 * @return an expression indicating the possible start times for this activity
	 */
	public abstract CspIntExpr getStartTimeExpr();

	/**
	 * Returns an expression indicating the possible end times for this activity
	 * @return an expression indicating the possible end times for this activity
	 */
	public abstract CspIntExpr getEndTimeExpr();

	/**
	 * Returns an expression indicating the duration of this activity
	 * @return an expression indicating the duration of this activity
	 */
	public abstract CspIntExpr getDurationExpr();

	/**
	 * Returns the earliest start time for the specified operation and resource
	 * @param operationID id of operation that this resource is a part of
	 * @param resourceID id of resource whose domain we are altering
	 */
	public abstract int getEarliestStartTime(int operationID, int resourceID);

	/**
	 * Returns the latest start time for this activity
	 */
	public abstract int getLatestStartTime();

	/**
	 * Returns the latest start time for the specified operation and resource
	 * @param operationID id of operation that this resource is a part of
	 * @param resourceID id of resource whose domain we are altering
	 */
	public abstract int getLatestStartTime(int operationID, int resourceID);

	/**
	 * Returns the earliest end time for this activity
	 */
	public abstract int getEarliestEndTime();

	/**
	 *  Returns all resources that this activity uses 
	 */
	public int[] getRequiredResources();
	
	/**
	 * Returns the earliest end time for the specified operation and resource
	 * @param operationID id of operation that this resource is a part of
	 * @param resourceID id of resource whose domain we are altering
	 */
	public abstract int getEarliestEndTime(int operationID, int resourceID);

	/**
	 * Returns the latest end time for this activity
	 */
	public abstract int getLatestEndTime();

	/**
	 * Returns the latest end time for the specified operation and resource
	 * @param operationID id of operation that this resource is a part of
	 * @param resourceID id of resource whose domain we are altering
	 */
	public abstract int getLatestEndTime(int operationID, int resourceID);

	/**
	 * Set the earliest start time for the resource with the specified id
	 * @param resourceID id of resource
	 * @param est the earliest start time
	 */
	public abstract void setEarliestStartTime(int resourceID, int est)
			throws PropagationFailureException;

	/**
	 * Set the earliest start time for the activity
	 * @param est the earliest start time
	 */
	public abstract void setEarliestStartTime(int est)
			throws PropagationFailureException;

	/**
	 * Restricts the start time of the specified resource to a single value
	 * @param resourceID id of resource we are setting
	 * @param n time we are setting
	 * @throws PropagationFailureException  If domain is empty
	 */
	public abstract void setStartTime(int resourceID, int n)
			throws PropagationFailureException;

	/**
	 * Set the latest start time for resource with the specified id
	 * @param resourceId id of resource
	 * @param lst the latest start time
	 */
	public abstract void setLatestStartTime(int resourceId, int lst)
			throws PropagationFailureException;

	/**
	 * Set the latest start time for the activity
	 * @param lst the latest start time
	 */
	public abstract void setLatestStartTime(int lst)
			throws PropagationFailureException;

	/**
	 * Set the earliest end time for the resource at the specified index
	 * @param resourceIdx index of resource
	 * @param eet the earliest end time
	 */
	public abstract void setEarliestEndTime(int resourceIdx, int eet)
			throws PropagationFailureException;

	/**
	 * Set the earliest end time for the activity 
	 * @param eet the earliest end time
	 */
	public abstract void setEarliestEndTime(int eet)
			throws PropagationFailureException;

	/**
	 * Set the latest end time for the resource at the specified index
	 * @param resourceIdx index of resource
	 * @param let the latest end time
	 */
	public abstract void setLatestEndTime(int resourceIdx, int let)
			throws PropagationFailureException;

	/**
	 * Set the latest end time for the activity
	 * @param let the latest end time
	 */
	public abstract void setLatestEndTime(int let)
			throws PropagationFailureException;

	/**
	 * Sets the maximum duration of the activity
	 * @param durMax new maximum duration
	 * @throws PropagationFailureException
	 */
	public abstract void setDurationMax(int durMax)
			throws PropagationFailureException;

	/**
	 * Sets the minimum duration of the activity
	 * @param durMin new minimum duration
	 * @throws PropagationFailureException
	 */
	public abstract void setDurationMin(int durMin)
			throws PropagationFailureException;

	/**
	 * Gets the maximum duration
	 * @return maximum duration
	 */
	public abstract int getDurationMax();

	/**
	 * Gets the minimum duration
	 * @return minimum duration
	 */
	public abstract int getDurationMin();

	/**
	 * Removes a single value from the potential start times of this activity
	 *
	 *@param n time remove from possible start time
	 * @throws PropagationFailureException  If domain is empty
	 */
	public abstract void removeStartTime(int n)
			throws PropagationFailureException;

	/**
	 * Restricts the start time of this activity to a single value
	 *
	 *@param n time to set start time to
	 * @throws PropagationFailureException  If domain is empty
	 */
	public abstract void setStartTime(int n) throws PropagationFailureException;

	/**
	 * Removes a range of start time values from this activity
	 * @param start start of the range being removed
	 * @param end end of the range being removed
	 * @throws PropagationFailureException  If domain is empty
	 */
	public abstract void removeStartTimes(int start, int end)
			throws PropagationFailureException;

	/**
	 * Restricts the start time of this activity to be between start and end
	 * @param start new earliest start time
	 * @param end new latest end time
	 * @throws PropagationFailureException  If domain is empty
	 */
	public abstract void setStartTimeRange(int start, int end)
			throws PropagationFailureException;

	/**
	 * Returns true if all operation have a resource assigned to them and the activity has a unique start and end time
	 * @return true if all operation have a resource assigned to them and the activity has a unique start and end time
	 */
	public abstract boolean isBound();

	/**
	 * Sets the available timelines of the given resource for this activity / operation combination
	 * @param operationID id of the operation for which this is being set
	 * @param resourceID id of the resource who's availability is being reported
	 * @param timeline Set that represents the times that the resource is available to service the specified operation
	 * @throws PropagationFailureException
	 */
	public abstract void setTimeline(int operationID, int resourceID,
			IntIntervalSet timeline) throws PropagationFailureException;

	/**
	 * Sets the choicepoint stack associated with this activity
	 * @param cps ChoicePointStack that will have control over choice points events on this activity
	 */
	public abstract void setChoicePointStack(ChoicePointStack cps);

	/**
	 * Returns true if a call to setChoicePointStack will fail
	 * @return returns true if the choice point has already been set
	 */
	public abstract boolean choicePointStackSet();

	/**
	 * Returns a constraint constraining this activity to end before the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsBeforeStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end before the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsBeforeEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end after the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAfterStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end after the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAfterEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end at the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAtStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end at the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAtEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start before the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsBeforeStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start before the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsBeforeEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start after the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAfterStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start after the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAfterEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start at the start of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAtStartOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to start at the end of act
	 * @param act activity this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAtEndOf(Activity act);

	/**
	 * Returns a constraint constraining this activity to end before the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsBeforeStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to end before the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsBeforeEndOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to end after the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAfterStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to end after the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAfterEndOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to end at the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAtStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to end at the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint endsAtEndOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start before the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsBeforeStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start before the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsBeforeEndOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start after the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAfterStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start after the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAfterEndOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start at the start of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAtStartOf(CspIntExpr expr);

	/**
	 * Returns a constraint constraining this activity to start at the end of expr
	 * @param expr expr this is being related to
	 * @return constraint between the activities
	 */
	public abstract CspConstraint startsAtEndOf(CspIntExpr expr);
    
    /**
     * Adds nodes representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph);

}