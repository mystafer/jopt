package jopt.js.api.exception;

/**
 * A class to report any errors that are JobScheduler specific, but cannot be specified as a standard <code>PropagationFailureException</code>.
 * A typical example of such an exception would be trying to assign an activity to a resource no longer in the activity's domain.
 *
 * @author James Boerkoel
 */
public class JobSchedulerRuntimeException extends RuntimeException {

	/**
	 *  Creates a Job Scheduler Runtime Exception
	 *
	 * @param message the message of the exception
	 */
	public JobSchedulerRuntimeException(String message) {
		super(message);
	}
}
