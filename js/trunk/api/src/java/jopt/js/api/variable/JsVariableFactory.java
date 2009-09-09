package jopt.js.api.variable;

import jopt.csp.variable.CspVariableFactory;


public interface JsVariableFactory extends CspVariableFactory{

	/**
	 * Creates an activity
	 * @param name name of the activity
	 * @param id id of this activity
	 * @param est earliest start time
	 * @param lst latest start time
	 * @param durMin minimum duration
	 * @param durMax maximum duration
	 */
	public Activity createActivity(String name, int id, int est, int lst,
			int durMin, int durMax) ;
	
	/**
	 * Creates an activity
	 * @param name name of the activity
	 * @param id id of this activity
	 * @param est earliest start time
	 * @param lst latest start time
	 * @param duration duration
	 */
	public Activity createActivity(String name, int id, int est, int lst,
			int duration) ;

	/**
	 * Creates a unary resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 */
	public Resource createUnaryResource(String name, int startTime, int endTime) ;

	/**
	 * Creates a discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available
	 */
	public Resource createDiscreteResource(String name, int startTime,
			int endTime, int capacity) ;
	
	/**
	 * Creates a discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available per time interval
	 */
	public Resource createDiscreteResource(String name, int startTime,
			int endTime, int[] capacity) ;

	/**
	 * Creates a granular unary resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularUnaryResource(String name, int startTime, int endTime, int bucketSize, int offset) ;

	/**
	 * Creates a granular discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularDiscreteResource(String name, int startTime,
			int endTime, int capacity, int bucketSize, int offset) ;
	
	/**
	 * Creates a granular discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available per time interval
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularDiscreteResource(String name, int startTime,
			int endTime, int[] capacity, int bucketSize, int offset) ;
	
}
