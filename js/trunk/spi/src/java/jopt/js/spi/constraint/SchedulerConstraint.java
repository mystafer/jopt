package jopt.js.spi.constraint;

import jopt.js.api.variable.SchedulerExpression;

/**
 * An interface to contain all similarities between scheduling constraints
 * 
 * @author jboerkoel
 */
public interface SchedulerConstraint {
	
	/**
	 * Returns any and all Scheduler Expressions that are involved with the problem
	 * @return all expressions involved with the problem
	 */
	public SchedulerExpression[] getExpressions();
	
}
