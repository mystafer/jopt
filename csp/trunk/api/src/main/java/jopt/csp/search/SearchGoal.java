package jopt.csp.search;

import jopt.csp.variable.PropagationFailureException;

/**
 * <code>SearchGoal</code> is implemented by classes that guide searches towards
 * specific solutions (such as solutions that minimize or maximize a specified expression).
 */
public interface SearchGoal extends Cloneable {
    /**
     * This function must return true if the specified open node is acceptable
     * to activate.  Used by first-solution goal to prevent further exploration;
     * may be used for others at some point.
     * 
     * @param node  Node to check for ability to activate
     * @return True if search node is valid to activate
     */
    public boolean isOkToActivate(SearchNode node);

    /**
     * Called when searches encounter a solution to determine the proper
     * course of action based on whether the goal has been reached.
     * 
     * @param treeLocationRef	Reference to a node in the search tree where solution was located
     * @return True if solution is acceptable for goal, false if it should be discarded
     */
    public boolean solutionFound(SearchNodeReference treeLocationRef);
    
    /**
     * Returns the best objective value known given the current problem state
     * and previous solutions that have been found
     *
     * @return Best known objective value   
     */
    public double bestObjectiveValue();

    /**
     * Called by search to notify goal that the bound should be returned
     * to a previous objective value
     */
    public void returnBoundToObjectiveValue(double objective);
    
    /**
     * Called by search to notify goal that any changes to the problem
     * to update the bounds before an open node is activated should
     * be performed at this time.
     * 
     * @exception PropagationFailureException   If a failure occurred
     *                  attempting to update problem 
     */
    public void updateBoundForOpenNode() throws PropagationFailureException;
    
    /**
     * Returns the number of search nodes this goal has marked as
     * valid solutions acceptable to the goal.
     */
    public int getSolutionReferenceCount();
    
    /**
     * Returns the reference to a solution node for this goal
     * at the specified offset.  If no such solution exists,
     * null is returned.
     */
    public SearchNodeReference getSolutionReference(int n);
    
    public Object clone();
}
