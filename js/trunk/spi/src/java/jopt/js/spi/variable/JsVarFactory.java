package jopt.js.spi.variable;

import jopt.csp.spi.arcalgorithm.variable.VarFactory;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.JsVariableFactory;
import jopt.js.api.variable.Resource;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.domain.resource.GranularResourceDomain;
import jopt.js.spi.domain.resource.UnaryResourceDomain;

public class JsVarFactory extends VarFactory implements JsVariableFactory {

	/**
	 * Creates an activity
	 * @param name name of the activity
	 * @param id id of this activity
	 * @param est earliest start time
	 * @param lst latest start time
	 * @param durMin minimum duration
	 * @param durMax maximum duration
	 */
	public Activity createActivity(String name, int id, int est, int lst, int durMin, int durMax) {
		return new ActivityExpr(name, id, est, lst, durMin, durMax);
	}
	
	/**
	 * Creates an activity
	 * @param name name of the activity
	 * @param id id of this activity
	 * @param est earliest start time
	 * @param lst latest start time
	 * @param duration duration
	 */
	public Activity createActivity(String name, int id, int est, int lst, int duration) {
		return new ActivityExpr(name, id, est, lst, duration, duration);
	}

	/**
	 * Creates a unary resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 */
	public Resource createUnaryResource(String name, int startTime, int endTime) {
		return new ResourceExpr(name, new UnaryResourceDomain(startTime, endTime));
	}

	/**
	 * Creates a discrete resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available at all time intervals
	 */
	public Resource createDiscreteResource(String name, int startTime, int endTime, int capacity) {
		return new ResourceExpr(name, new DiscreteResourceDomain(startTime, endTime, capacity));
	}
	
	/**
	 * Creates a discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available per time interval
	 */
	public Resource createDiscreteResource(String name, int startTime, int endTime, int[] capacity) {
		return new ResourceExpr(name, new DiscreteResourceDomain(startTime, endTime, capacity));
	}

	/**
	 * Creates a granular unary resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularUnaryResource(String name, int startTime, int endTime, int bucketSize, int offset) {
		return new ResourceExpr(name, new GranularResourceDomain(new UnaryResourceDomain(startTime, endTime),bucketSize,offset));
	}

	/**
	 * Creates a granular discrete resource
	 * @param name name of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available at all time intervals
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularDiscreteResource(String name, int startTime,
			int endTime, int capacity, int bucketSize, int offset) {
		return new ResourceExpr(name, new GranularResourceDomain(new DiscreteResourceDomain(startTime, endTime, capacity), bucketSize, offset));
	}
	
	/**
	 * Creates a granular discrete resource
	 * @param name names of the resource
	 * @param startTime earliest time that the resource is available to be used
	 * @param endTime latest time that the resource is available to be used
	 * @param capacity amount of resource available per time interval (ie per bucket)
	 * @param bucketSize size of the bucket that encapsulates time
	 * @param offset the offset from the first time interval that the bucket should start
	 */
	public Resource createGranularDiscreteResource(String name, int startTime,
			int endTime, int[] capacity, int bucketSize, int offset) {
		
		int[] neoCapacity = new int[(endTime-startTime)+1];
		
		for (int i=0; i<offset; i++){
			neoCapacity[i] = capacity[0];
		}
		
		int index = offset;
		
		for (int i=1; i<capacity.length; i++) {
			for (int j=0; j<4; j++) {
				if (index<neoCapacity.length) {
					neoCapacity[index] = capacity[i];
					index++;
				}
			}
		}
		
		return new ResourceExpr(name, new GranularResourceDomain(new DiscreteResourceDomain(startTime, endTime, neoCapacity), bucketSize, offset));
	}
}
