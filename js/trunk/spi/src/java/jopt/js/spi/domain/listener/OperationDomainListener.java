package jopt.js.spi.domain.listener;

import jopt.csp.variable.PropagationFailureException;

/**
 * Interface that can be implemented by a class that wishes to be notified of changes to the 
 * operations of an activity domain
 * 
 * @author James Boerkoel
 */
public interface OperationDomainListener {

    /**
     * Fired when the start time, end time, or duration of an operation is altered
     * @throws PropagationFailureException
     */
    public void operationRuntimeChange() throws PropagationFailureException;
    
    /**
     * Fired when a resource is added or eliminated as a possibility
     * @throws PropagationFailureException
     */
    public void operationRequiredResourceChange() throws PropagationFailureException;
    
    /**
     * Fired when the capacity required is altered
     * @throws PropagationFailureException
     */
    public void operationCapacityChange() throws PropagationFailureException;
	    
}
