package jopt.csp.spi.arcalgorithm.variable;

import java.util.HashMap;
import java.util.Iterator;

import jopt.csp.spi.solver.VariableChangeEvent;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.PropagationFailureException;
/**
 * Base class for all constraints, sets, variables and expressions that are
 * able to inform others of a change event.
 */
public abstract class VariableChangeBase implements VariableChangeSource {
    protected HashMap<VariableChangeListener, Integer> variableListeners;

    /**
     * Constructor
     */
    protected VariableChangeBase() {
        this.variableListeners = new HashMap<VariableChangeListener, Integer>();
    }

    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener) {
	if (listener!=null && variableListeners!=null) {
	    // increment counter for listener
	    Integer count = (Integer) variableListeners.get(listener);
	    if (count==null)
		count = new Integer(1);
	    else
		count = new Integer(count.intValue()+1);

	    variableListeners.put(listener, count);
	}
    }

    /**
     * Removes a variable listener from this variable
     */
    public void removeVariableChangeListener(VariableChangeListener listener) {
	if (listener!=null && variableListeners!=null) {
	    // decrement counter for listener
	    Integer count = (Integer) variableListeners.get(listener);

	    // check if already removed
	    if (count!=null) {
		// check if listener counter is now going to be zero,
		// remove if necessary
		if (count.intValue()==1)
		    variableListeners.remove(listener);
		else
		    variableListeners.put(listener, new Integer(count.intValue()-1));
	    }
	}
    }

    /**
     * Fires a variable change event to registered listeners
     */
    protected void fireChangeEvent() throws PropagationFailureException {
	// Create variable event to fire
	VariableChangeEvent event = new VariableChangeEvent(this);

	// Notify listeners
	if (variableListeners != null) {
	    Iterator<VariableChangeListener> iterator = variableListeners.keySet().iterator();
	    while (iterator.hasNext()) {
		VariableChangeListener listener = (VariableChangeListener) iterator.next();
		listener.variableChange(event);
	    }
	}
    }
}