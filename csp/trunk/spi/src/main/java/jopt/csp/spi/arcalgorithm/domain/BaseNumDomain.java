package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for numeric domains
 */
public abstract class BaseNumDomain extends AbstractDomain implements ChoicePointEntryListener {
    // values that make up the 'domain'
    protected NumSet values;
    
    // values that have been removed from the domain since last call to clearDelta
    protected NumSet delta;
    
    // choice point data recording collections
    protected NumSet deltaAddedRecord;
    protected NumSet deltaRemovedRecord;
    protected boolean deltaCleared;
    
    // flag used for detecting a change that indicates an event should be thrown
    protected boolean changeDetected;

    // holds numbers that will be set for events
    private MutableNumber eventMinChange;
    private MutableNumber eventMaxChange;
    
    // flag for indicating this domain is a nested child of another aggregate domain
    protected boolean nestedChild;
    
    /**
     * Initializes internal variables for a new domain
     */
    protected BaseNumDomain(int numberType) {
        // initialize sets
        this.values = createEmptySet();
        this.delta = createEmptySet();
        this.deltaAddedRecord = createEmptySet();
        this.deltaRemovedRecord = createEmptySet();
        this.deltaCleared = false;
        this.eventMinChange = new MutableNumber();
        this.eventMaxChange = new MutableNumber();
        this.event.setNumberChange(eventMinChange, eventMaxChange, numberType);
    }

    /**
	 * Returns true if this domain is a nested child of a larger
	 * aggregate domain
	 */
	public boolean isNestedChild() {
		return nestedChild;
	}

    /**
	 * Sets a flag indicating this domain is a nested child domain of
	 * a larger aggregate domain
	 */
	public void setNestedChild(boolean nestedChild) {
		this.nestedChild = nestedChild;
	}
    
    /**
     * Updates delta set with changes that occurred during last modification
     * and returns false if no changes exist
     */
    protected final boolean isChangeDetected() {
        boolean retval = changeDetected;
        changeDetected = false;
        return retval;
    }
    
    /**
     * Returns size of domain
     */
    public final int getSize() {
        return values.size();
    }

    /**
     * Returns iterator of values in node's domain
     */
    public final NumberIterator values() {
    	return values.values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        return delta.values();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public final NumSet toSet() {
        return values;
    }

    /**
     * Returns the delta set for this domain
     */
    public final NumSet getDeltaSet() {
        return delta;
    }
    
    /**
     * Clears the delta set for this domain
     */
    public final void clearDelta() {
        // update delta removed record once upon first time delta is cleared
        if (!deltaCleared) {
            deltaRemovedRecord.addAll(delta);
            deltaRemovedRecord.removeAll(deltaAddedRecord);
            deltaCleared = true;
        }
        
        // if delta is cleared, remove recordings of added values
        deltaAddedRecord.removeAll(delta);
        
        delta.clear();
    }

    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @return an Object containing the values of this domain
     */
	public final Object getDomainState() {
        return values.clone();
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public final void restoreDomainState(Object state) {
        values.clear();
        delta.clear();
        deltaAddedRecord.clear();
        deltaRemovedRecord.clear();
        deltaCleared = false;
        values.addAll((NumSet) state);
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyRangeChange(int min, int max) throws PropagationFailureException {
        eventMinChange.setIntValue(min);
        eventMaxChange.setIntValue(max);
        notifyRangeChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyRangeChange(long min, long max) throws PropagationFailureException {
        eventMinChange.setLongValue(min);
        eventMaxChange.setLongValue(max);
        notifyRangeChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyRangeChange(float min, float max) throws PropagationFailureException {
        eventMinChange.setFloatValue(min);
        eventMaxChange.setFloatValue(max);
        notifyRangeChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyRangeChange(double min, double max) throws PropagationFailureException {
        eventMinChange.setDoubleValue(min);
        eventMaxChange.setDoubleValue(max);
        notifyRangeChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyValueChange(int min, int max) throws PropagationFailureException{
        eventMinChange.setIntValue(min);
        eventMaxChange.setIntValue(max);
        notifyValueChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyValueChange(long min, long max) throws PropagationFailureException{
        eventMinChange.setLongValue(min);
        eventMaxChange.setLongValue(max);
        notifyValueChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyValueChange(float min, float max) throws PropagationFailureException{
        eventMinChange.setFloatValue(min);
        eventMaxChange.setFloatValue(max);
        notifyValueChange();
    }
    
    /**
     * Notifies listeners of domain change event
     */
    protected void notifyValueChange(double min, double max) throws PropagationFailureException{
        eventMinChange.setDoubleValue(min);
        eventMaxChange.setDoubleValue(max);
        notifyValueChange();
    }
    
    /**
     * Creates a new empty set
     */
    protected abstract NumSet createEmptySet();
    
    public abstract Object clone();

    public final String toString() {
        return values.toString();
    }
}
