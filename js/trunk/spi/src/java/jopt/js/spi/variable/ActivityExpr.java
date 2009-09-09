package jopt.js.spi.variable;

import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.arcalgorithm.variable.VariableChangeBase;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.util.TransitionTimeTable;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.Resource;
import jopt.js.api.variable.ResourceSet;
import jopt.js.api.variable.SchedulerExpression;
import jopt.js.spi.constraint.ForwardCheckConstraint;
import jopt.js.spi.constraint.TemporalConstraint;
import jopt.js.spi.domain.activity.ActOperationDomain;
import jopt.js.spi.domain.activity.ActResourceDomain;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.activity.IntIntervalActivityDomain;
import jopt.js.spi.domain.listener.OperationDomainListener;
import jopt.js.spi.graph.arc.TemporalArc;
import jopt.js.spi.graph.node.ActivityNode;

/**
 * Expression to hold pertinent activity information 
 * 
 * @author James Boerkoel
 */
public class ActivityExpr extends VariableChangeBase implements Activity, OperationDomainListener, SchedulerExpression {
    protected ActivityNode actNode;
    protected ActivityDomain actDomain;
    protected String name;
    
    public static final int START = 0;
    public static final int END = 1;
    public static final int DURATION = 2;
    public static final int ALL = 3;
    
    //mainained in case users find a need to reference start, end, or duration as an int expression
    protected IntExpr startTimeExpr;
    protected IntExpr endTimeExpr;
    protected IntExpr durationExpr;
    
    protected TransitionTimeTable tranTimeTable;
    protected int sourceCategory;
    protected int destinationCategory;
    
	/**
     * Constructs an Activity Expression 
     * @param name name of the activity
     * @param id id of the activity 
     * @param est earliest start time of activity
     * @param lst latest start time of activity
     * @param durMin minimum duration of the activity
     * @param durMax maximum duration of the activity
     */
    public ActivityExpr(String name, int id, int est, int lst, int durMin, int durMax){
    	this.name = name;
    	actDomain = new ActivityDomain(est,lst, durMin,durMax);
    	actNode = new ActivityNode(name, actDomain);
    	this.actDomain.addOperationListener(this);
    	actNode.setID(id);
    	this.destinationCategory = id;
    	this.sourceCategory = id;
    }
    
	/**
     * Constructs an Activity Expression 
     * @param name name of the activity
     * @param id id of the activity 
     * @param est earliest start time of activity
     * @param lst latest start time of activity
     * @param durMin minimum duration of the activity
     * @param durMax maximum duration of the activity
     * @param sourceCat source category
     * @param destCat destination category
     */
    public ActivityExpr(String name, int id, int est, int lst, int durMin, int durMax, int sourceCat, int destCat){
    	this.name = name;
    	actDomain = new ActivityDomain(est,lst, durMin, durMax);
    	actNode = new ActivityNode(name, actDomain);
    	this.actDomain.addOperationListener(this);
    	actNode.setID(id);
    	this.destinationCategory = destCat;
    	this.sourceCategory = sourceCat;
    }
    
    /**
     * Returns the destination (ie. "transition to") category
     * @return the destination category
     */
    public int getTransitionToCategory() {
		return destinationCategory;
	}

    /**
     * Sets the destination category and makes sure to update the transit table if one has been registered
     * @param destinationCategory
     */
	public void setTransitionToCategory(int destinationCategory) {
		if (tranTimeTable == null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		this.destinationCategory = destinationCategory;
		int[] opIDs = actNode.getOperationIDs();
		for (int i=0; i<opIDs.length; i++) {
			tranTimeTable.registerOperationID(opIDs[i],this.sourceCategory, this.destinationCategory);
		}
	}
	
    /** 
     * Returns true if all operations have been assigned to a resource
     * @return true if all operations have been assigned
     */
	public boolean operationsAssigned() {
		return actDomain.operationsAssigned();
	}

    /**
     * Returns all resource sets for any operation that still has multiple possibilities for which
     * resource will service the operation 
     * @return set of all alternative resource sets
     */
	public ResourceSet[] getAllAlternativeResourceSets() {
		return actDomain.getAllAlternativeResourceSets();
	}
	
	/**
	 * Returns the source (ie "transition from") category
	 * @return source category
	 */
	public int getTransitionFromCategory() {
		return sourceCategory;
	}

	/**
	 * Sets the source category and makes sure to update the transit table if one has been registered
	 * @param sourceCategory new source category
	 */
	public void setTransitionFromCategory(int sourceCategory) {
		if (tranTimeTable == null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		this.sourceCategory = sourceCategory;
		int[] opIDs = actNode.getOperationIDs();
		for (int i=0; i<opIDs.length; i++) {
			tranTimeTable.registerOperationID(opIDs[i],this.sourceCategory, this.destinationCategory);
		}
	}
	
	// javadoc is inherited
	public int getNumUnassignedOperations() {
		return (actDomain.getOperationCount() - actDomain.getAssignedOperations().length);
	}
	
    // javadoc is inherited
	public void setTransitTimeFrom(Activity sourceAct, int transitTime) {
		if (tranTimeTable==null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		tranTimeTable.add(sourceAct.getTransitionFromCategory(),this.getTransitionToCategory(),transitTime);
	}
		
    // javadoc is inherited
	public void setTransitTimeTo(Activity destAct, int transitTime) {
		if (tranTimeTable==null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		tranTimeTable.add(this.getTransitionFromCategory(),destAct.getTransitionToCategory(),transitTime);
	}
	
    // javadoc is inherited
	public void setTransitTimeFrom(int sourceCat, int transitTime) {
		if (tranTimeTable==null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		tranTimeTable.add(sourceCat,this.getTransitionToCategory(),transitTime);
	}
		
    // javadoc is inherited
	public void setTransitTimeTo(int destCat, int transitTime) {
		if (tranTimeTable==null) {
			tranTimeTable = TransitionTimeTable.getInstance();
		}
		tranTimeTable.add(this.getTransitionFromCategory(),destCat,transitTime);
	}
    
    // javadoc is inherited
	public boolean isBuilt() {
		return actDomain.isBuilt();
	}
	
    // javadoc is inherited
	public void setBuilt(boolean built) {
		actDomain.setBuilt(built);
	}
	
    // javadoc is inherited
    public int getID() {
    	return actDomain.getID();
    }
    
    // javadoc is inherited
    public int getEarliestStartTime() {
    	return actDomain.getEarliestStartTime();
    }
    
    // javadoc is inherited
    public void addResource(int operationID, int resourceID, int start, int end, int minDur, int maxDur) throws PropagationFailureException {
    	actDomain.addResource(operationID,resourceID,start,end,minDur,maxDur);
    }
    
    // javadoc is inherited
    public CspConstraint require(Resource[] possResourceSet, int capacity) throws PropagationFailureException{
    	
    	ActOperationDomain oa = new ActOperationDomain(ActOperationDomain.REQUIRES,capacity, capacity);
    	int est = getEarliestStartTime();
    	//Make corrections if activity is empty
    	if (est==Integer.MAX_VALUE) {
    		est = Integer.MIN_VALUE;
    	}
    	int lst = getLatestStartTime();
    	if (lst==Integer.MIN_VALUE) {
    		lst = Integer.MAX_VALUE;
    	}
    	int eet = getEarliestEndTime();
    	if (eet == Integer.MAX_VALUE) {
    		eet = Integer.MIN_VALUE;
    	}
    	int let = getLatestEndTime();
    	if (let == Integer.MIN_VALUE) {
    		let = Integer.MAX_VALUE;
    	}
    	int durMin = getDurationMin();
    	int durMax = getDurationMax();
    	for (int i=0; i<possResourceSet.length; i++) {
    		ActResourceDomain ra = new ActResourceDomain(possResourceSet[i].getID(),est,lst,durMin,durMax);
    		ra.setPointer(possResourceSet[i]);
    		IntIntervalSet timeline = possResourceSet[i].findAvailIntervals(est,let,capacity);
    		ra.setTimeline(timeline,false);
    		oa.addResource(ra);
    	}
    	
    	actDomain.addOperation(oa);
    	if (tranTimeTable!=null) {
    		tranTimeTable.registerOperationID(oa.getID(),this.sourceCategory,this.destinationCategory);
    	}
    	
    	for (int i=0; i< possResourceSet.length; i++) {
    		((ResourceExpr)possResourceSet[i]).getNode().setPotentialOperationTimeline(oa.getID(),this.actDomain.getPotentialUsageTimeline(oa.getID(), possResourceSet[i].getID()));
    		((ResourceExpr)possResourceSet[i]).getNode().setActualOperationTimeline(oa.getID(),this.actDomain.getActualUsageTimeline(oa.getID(), possResourceSet[i].getID()));
    	}
    	
    	return new ForwardCheckConstraint(this,possResourceSet,oa.getID());
    }
    
    // javadoc is inherited
    public CspIntExpr getStartTimeExpr(){
    	if (startTimeExpr==null) {
    		startTimeExpr = new IntExpr(name+" start time",new IntIntervalActivityDomain(actDomain,IntIntervalActivityDomain.START));
    	}
    	return startTimeExpr;
    }
    
    // javadoc is inherited
    public CspIntExpr getEndTimeExpr(){
    	if (endTimeExpr==null) {
    		endTimeExpr = new IntExpr(name+" end time",new IntIntervalActivityDomain(actDomain,IntIntervalActivityDomain.END));
    	}
    	return endTimeExpr;	
    }
    
    // javadoc is inherited
    public CspIntExpr getDurationExpr(){
    	if (durationExpr==null) {
    		durationExpr = new IntExpr(name+" duration",new IntIntervalActivityDomain(actDomain,IntIntervalActivityDomain.DURATION));
    	}
    	return durationExpr;	
    }

    // javadoc is inherited
    public int getEarliestStartTime(int operationID, int resourceID) {
    	return actDomain.getEarliestStartTime(operationID, resourceID);
    }
    
    /**
     * Gets the node corresponding to this expression
     * @return node corresponding to this expression
     */
    public ActivityNode getNode() {
    	if (actNode==null) {
    		this.actNode = new ActivityNode(name, actDomain);
    	}
    	return actNode;
    }
    
    // javadoc is inherited
    public void updateGraph(NodeArcGraph graph) {
        if (!graph.containsNode(getNode())) {
            graph.addNode(getNode());
        }
    }
    
    // javadoc is inherited
    public int getLatestStartTime() {
    	return actDomain.getLatestStartTime();
    }

    // javadoc is inherited
    public int getLatestStartTime(int operationID, int resourceID){
    	return actDomain.getLatestStartTime(operationID, resourceID);
    }
    
    // javadoc is inherited
    public int getEarliestEndTime() {
    	return actDomain.getEarliestEndTime();
    }

    // javadoc is inherited
    public int getEarliestEndTime(int operationID, int resourceID) {
    	return actDomain.getEarliestEndTime(operationID, resourceID);
    }
    
    // javadoc is inherited
    public int getLatestEndTime() {
    	return actDomain.getLatestEndTime();
    }

    // javadoc is inherited
    public int getLatestEndTime(int operationID, int resourceID) {
    	return actDomain.getLatestEndTime(operationID, resourceID);
    }
    
    // javadoc is inherited
    public void setEarliestStartTime( int resourceID, int est) throws PropagationFailureException {
    	actDomain.setEarliestStartTime(resourceID,est);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setEarliestStartTime(int est) throws PropagationFailureException {
    	actDomain.setEarliestStartTime(est);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setStartTime(int resourceID, int n) throws PropagationFailureException {
    	actDomain.setEarliestStartTime(resourceID, n);
    	actDomain.setLatestStartTime(resourceID, n);
    	fireChangeEvent();
    }

    // javadoc is inherited
    public void setLatestStartTime(int resourceId, int lst) throws PropagationFailureException {
    	actDomain.setLatestStartTime(resourceId, lst);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setLatestStartTime(int lst) throws PropagationFailureException {
    	actDomain.setLatestStartTime(lst);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setEarliestEndTime(int resourceIdx, int eet) throws PropagationFailureException {
    	actDomain.setEarliestEndTime(resourceIdx, eet);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setEarliestEndTime(int eet) throws PropagationFailureException {
    	actDomain.setEarliestEndTime(eet);
    	fireChangeEvent();
    }

    // javadoc is inherited
    public void setLatestEndTime(int resourceIdx, int let) throws PropagationFailureException{
    	actDomain.setLatestEndTime(resourceIdx, let);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setLatestEndTime(int let) throws PropagationFailureException {
    	actDomain.setLatestEndTime(let);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setDurationMax(int durMax) throws PropagationFailureException {
    	actDomain.setDurationMax(durMax);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public void setDurationMin(int durMin) throws PropagationFailureException{
    	actDomain.setDurationMin(durMin);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public int getDurationMax() {
    	return actDomain.getDurationMax();
    }
    
    // javadoc is inherited
    public int getDurationMin() {
    	return actDomain.getDurationMin();
    }
    
    // javadoc is inherited
    public void removeStartTime(int n) throws PropagationFailureException {
    	actDomain.removeStartTimes(n,n);
    	fireChangeEvent();
    }

    // javadoc is inherited
    public void setStartTime(int n) throws PropagationFailureException {
    	actDomain.setEarliestStartTime(n);
    	actDomain.setLatestStartTime(n);
    	fireChangeEvent();
    }

    // javadoc is inherited
    public void removeStartTimes(int start, int end) throws PropagationFailureException {
    	actDomain.removeStartTimes(start,end);
    	fireChangeEvent();
    }

    // javadoc is inherited
    public void setStartTimeRange(int start, int end) throws PropagationFailureException {
    	actDomain.setEarliestStartTime(start);
    	actDomain.setLatestStartTime(end);
    	fireChangeEvent();
    }
    
    // javadoc is inherited
    public boolean isBound() {
    	return actDomain.isBound();
    }
    
    // javadoc is inherited
    public void setTimeline(int operationID,int resourceID, IntIntervalSet timeline) throws PropagationFailureException {
    	actDomain.setTimeline(operationID,resourceID,timeline);
    }

    // javadoc is inherited
	public int[] getRequiredResources() {
		return actDomain.getRequiredResources();
	}
    
    // javadoc is inherited
    public int getAvailResourceCount(int operationId) {
        return actDomain.getAvailResourceCount(operationId);
    }
    
    // javadoc is inherited
    public void setRequiredResource(int operationId, int resourceId) throws PropagationFailureException {
        actDomain.setRequiredResource(operationId, resourceId);
    }
    
    // javadoc is inherited
    public void removePossibleResource(int operationId, int resourceId) throws PropagationFailureException {
        actDomain.removePossibleResource(operationId, resourceId);
    }
	
    // javadoc is inherited
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) actDomain).setChoicePointStack(cps);
    }
    
    // javadoc is inherited
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) actDomain).choicePointStackSet();
    }

	public String toString() {
		if (name!=null)
			return "\n"+name+"\n "+actDomain.toString();
		return actDomain.toString();
	}
	
    // javadoc is inherited
	public CspConstraint endsBeforeStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.START,TemporalArc.BEFORE);
	}
    
    // javadoc is inherited
	public CspConstraint endsBeforeEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.END,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint endsAfterStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.START,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint endsAfterEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.END,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint endsAtStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.START,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint endsAtEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.END,TemporalArc.END,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint startsBeforeStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.START,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint startsBeforeEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.END,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint startsAfterStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.START,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint startsAfterEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.END,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint startsAtStartOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.START,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint startsAtEndOf(Activity act){
		return new TemporalConstraint(this,(ActivityExpr)act,TemporalArc.START,TemporalArc.END,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint endsBeforeStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.START,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint endsBeforeEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.END,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint endsAfterStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.START,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint endsAfterEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.END,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint endsAtStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.START,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint endsAtEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.END,TemporalArc.END,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint startsBeforeStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.START,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint startsBeforeEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.END,TemporalArc.BEFORE);
	}
	
    // javadoc is inherited
	public CspConstraint startsAfterStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.START,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint startsAfterEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.END,TemporalArc.AFTER);
	}
	
    // javadoc is inherited
	public CspConstraint startsAtStartOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.START,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public CspConstraint startsAtEndOf(CspIntExpr expr){
		return new TemporalConstraint(this,(IntExpr)expr,TemporalArc.START,TemporalArc.END,TemporalArc.AT);
	}
	
    // javadoc is inherited
	public void operationRuntimeChange() throws PropagationFailureException{
		fireChangeEvent();
	}
	
    // javadoc is inherited
	public void operationRequiredResourceChange() throws PropagationFailureException{
		fireChangeEvent();
	}
    
    // javadoc is inherited
	public void operationCapacityChange() throws PropagationFailureException{
		fireChangeEvent();
	}
	
//  /**
//  * Returns an int representing the four ways that the operation can use a resource
//  * @param operationID id of operation
//  * @return int representing PRODUCES, CONSUMES, PROVIDES, REQUIRES
//  */
// public int getUsageType(int operationID) {
//  return actDomain.getUsageType(operationID);
// }
// 
// /**
//  * Sets the way that the given operation will use resources
//  * @param operationID id of operation
//  * @param usage new type of the usage
//  */
// public void setUsageType(int operationID,int usage) {
//  actDomain.setUsageType(operationID,usage);
// }
// 
// /**
//  * Returns the minimum capacity of given operation
//  * @param operationID id of operation
//  * @return min capacity of given operation
//  */
// public int getCapacityMin(int operationID) {
//  return actDomain.getCapacityMin(operationID);
// }
// 
// /**
//  * Returns the maximum capacity of given operation
//  * @param operationID id of operation
//  * @return max capacity of given operation
//  */
// public int getCapacityMax(int operationID) {
//  return actDomain.getCapacityMax(operationID);
// }
// 
// /**
//  * Sets the minimum capacity of given operation
//  * @param operationID id of operation
//  * @param min new minimum
//  */
// public void setCapacityMin(int operationID, int min) throws PropagationFailureException{
//  actDomain.setCapacityMin(operationID, min);
// }
// 
// /**
//  * Sets the maximum capacity of given operation
//  * @param operationID id of operation
//  * @param max new maximum
//  */
// public void setCapacityMax(int operationID, int max) throws PropagationFailureException{
//  actDomain.setCapacityMax(operationID, max);
// }
// 
// /**
//  * Sets the capacity of give operation
//  * @param operationID id of operation
//  * @param val new capacity
//  */
// public void setCapacity(int operationID, int val) throws PropagationFailureException{
//  actDomain.setCapacity(operationID, val);
// }
// 
// /**
//  * Sets the capacity of given operation to a range of valid values
//  * @param operationID id of operation 
//  * @param start start of interval to restrict capacity to
//  * @param end end of interval to restrict capacity to
//  */
// public void setCapacityRange(int operationID, int start, int end) throws PropagationFailureException{
//  actDomain.setCapacityRange(operationID, start, end);
// }
//  
//  /**
//   * Forces the operation with the specified operationI to use
//   * the resource with the specified resourceId.
//   * 
//   * @param operationId the id of the operation for which we are setting the required resource
//   * @param resourceId the id of the resource on which adjustments should be made
//   * 
//   * @throws PropagationFailureException if the assignment causes any errors
//   */
//  public void setRequiredResource(int operationId, int resourceId)
//          throws PropagationFailureException {
//      actDomain.setRequiredResource(operationId,resourceId);
//      fireChangeEvent();
//  }
//
//  /**
//   * Returns the ids of the potential resources the operation with the specified operationID could 
//   * still be assigned to.
//   * @param operationId id of the operation of which we are asking for remaining resources
//   * @return ids of remaining resources
//   */
//  public int[] getRemainingResources(int operationId) {
//      return actDomain.getRemainingResources(operationId);
//  }
//
//  /**
//  * Removes the resource at the specified resource index from the list
//  * of possible resources for the operation at the specified operation index.
//  * 
//  * @throws PropagationFailureException if the reduction causes any errors
//  */
// public void removePossibleResource(int operationIdx, int resourceIdx) throws PropagationFailureException {
//  actDomain.removePossibleResource(operationIdx,resourceIdx);
//  fireChangeEvent();
// }
// 
//  public boolean isOperationAssignedToResource(int operationID) {
//      return actDomain.isOperationAssignedToResource(operationID);
//  }
// 
// /**
//   *  Restores variable information from stored data.
//   */
// public void restoreState(Object state) {
//  actDomain.restoreDomainState(state);
// }
// 
// public void clearDelta() {
//  actDomain.clearDelta();
// }
//
//  /**
//   * Stores appropriate data for future restoration.
//   */
//   public Object getState() {
//      return actDomain.getDomainState();
//   }
//   
//   /**
//   * Returns the number of remaining resources that can be
//   * used by the operation with the specified id.
//   */
//  public int getAvailResourceCount(int operationId) {
//      return actDomain.getAvailResourceCount(operationId);
//  }
//  
//  public int getRequiredResource(int operationID) {
//      return actDomain.getRequiredResource(operationID);
//  }
}