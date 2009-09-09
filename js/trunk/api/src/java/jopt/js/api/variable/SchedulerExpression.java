package jopt.js.api.variable;

/**
 * An Interface to represent commonalities between any and all scheduling expressions
 * @author jboerkoel
 *
 */
public interface SchedulerExpression {

	/**
	 * Determines whether the problem has already been built.  This would imply that after this point, changes to 
	 * it will be under choice point control (ie. they can be undone/redone).
	 * @return true if is built already
	 */
	public boolean isBuilt() ;

	/**
	 * Sets whether or not this expression can be editable.  If true, it cannot be edited without the possibility of
	 * losing changes from choice point events
	 * @param built
	 */
	public void setBuilt(boolean built) ;
}
