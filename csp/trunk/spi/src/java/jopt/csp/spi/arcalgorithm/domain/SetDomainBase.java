package jopt.csp.spi.arcalgorithm.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.solver.ChoicePointDataMap;
import jopt.csp.spi.solver.ChoicePointEntryListener;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.PropagationFailureException;

// TODO: There is no indication anywhere as to why this class exists
// instead of it all being in AbstractSetDomain. We need to document why
// it is this way or move all this code into AbstractSetDomain.
public class SetDomainBase<T> implements ChoicePointEntryListener {
	private ChoicePointStack cps;
    private ChoicePointDataMap cpdata;
    private HashSet<T> possibleValues;
    private HashSet<T> requiredValues;
    private HashSet<T> possibleValuesDelta;
    private HashSet<T> requiredValuesDelta;
    private boolean changed;
    
    /**
     * Constructor
     */
    public SetDomainBase(Collection<T> col) {
        this.possibleValues = new HashSet<T>(col);
        this.requiredValues = new HashSet<T>();
        this.possibleValuesDelta = new HashSet<T>();
        this.requiredValuesDelta = new HashSet<T>();
    }
    
    /**
     * Internal constructor
     */
    private SetDomainBase(SetDomainBase<T> base) {
        this.possibleValues = new HashSet<T>(base.possibleValues);
        this.requiredValues = new HashSet<T>(base.requiredValues);
        this.possibleValuesDelta = new HashSet<T>(base.possibleValuesDelta);
        this.requiredValuesDelta = new HashSet<T>(base.requiredValuesDelta);
    }

    /**
     * Returns true if required set of domain equals possible set for domain
     */
    public boolean isBound() {
        return requiredValues.size() == possibleValues.size();
    }

    /**
     * Returns true if a value is contained in this node's domain
     */
    public boolean isInDomain(Object val) {
        return possibleValues.contains(val);
    }
    
    /**
     * Returns 1 + cardinality of possible - cardinality of required
     */
    public int getSize() {
        return possibleValues.size() - requiredValues.size() + 1;
    }
    
    /**
     * Returns possible cardinality
     */
    public int getPossibleCardinality() {
        return possibleValues.size();
    }
    
    /**
     * Returns required cardinality
     */
    public int getRequiredCardinality() {
        return requiredValues.size();
    }
    
    /**
     * Returns true if last operation caused a change to occur in domain
     */
    public boolean changed() {
        boolean tmp = changed;
        changed = false;
        return tmp;
    }

    /**
     * Returns true if value is required
     */
    public boolean isRequired(T value) {
        return requiredValues.contains(value);
    }
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(T value) {
        return possibleValues.contains(value);
    }
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(T value) throws PropagationFailureException {
        if (!possibleValues.contains(value)) throw new PropagationFailureException();
        internalAddRequiredValue(value);
    }

    /**
     * Removes a value from the possible set
     */
    public void removePossible(T value) throws PropagationFailureException {
        if (requiredValues.contains(value)) throw new PropagationFailureException();
        internalRemovePossibleValue(value);
    }
    
    /**
     * Returns possible set of values
     */
    public Set<T> getPossibleSet() {
        return new HashSet<T>(possibleValues);
    }
    
    /**
     * Returns the possible-delta set
     */
    public Set<T> getPossibleDeltaSet() {
        return new HashSet<T>(possibleValuesDelta);
    }

    /**
     * Returns required set of values
     */
    public Set<T> getRequiredSet() {
        return new HashSet<T>(requiredValues);
    }
    
    /**
     * Returns the required-delta set
     */
    public Set<T> getRequiredDeltaSet() {
        return new HashSet<T>(requiredValuesDelta);
    }

    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        // Retrieve choice point sets
        Set<T> reqAddedSet = getRequiredDeltaAddedSet();
        Set<T> reqRemovedSet = getRequiredDeltaRemovedSet();
        Set<T> posAddedSet = getPossibleDeltaAddedSet();
        Set<T> posRemovedSet = getPossibleDeltaRemovedSet();
        
        // Update choice point if necessary
        if (reqAddedSet != null) {
            // Update required delta sets
            Iterator<T> iterator = requiredValuesDelta.iterator();
            while (iterator.hasNext()) {
                T val = iterator.next();

                // First attempt to remove from recently added set
                if (!reqAddedSet.remove(val))
                    reqRemovedSet.add(val);
            }
        
            // Update possible delta sets
            iterator = possibleValuesDelta.iterator();
            while (iterator.hasNext()) {
                T val = iterator.next();

                // First attempt to remove from recently added set
                if (!posAddedSet.remove(val))
                    posRemovedSet.add(val);
            }
        }
        
        // Clear set deltas
        requiredValuesDelta.clear();
        possibleValuesDelta.clear();
    }

    /**
     * Returns an iterator of possible values in the set
     */
    public Iterator<T> values() {
        return new SetDomainIterator();
    }

    /**
     * Adds a required value to the domain and records the change in the
     * choicepoint
     */
    private void internalAddRequiredValue(T value) {
        // Attempt to add required value
        if (this.requiredValues.add(value)) {
            // Update changed flag and delta set
            this.changed = true;
            this.requiredValuesDelta.add(value);
            
            // Update choice point stack for added value
            HashSet<T> s = getRequiredAddedValueSet();
            if (s!=null) {
                s.add(value);
                getRequiredDeltaAddedSet().add(value);
            }
        }
    }

    /**
     * Removes a possible value from the domain and records the change in the
     * choicepoint
     */
    private void internalRemovePossibleValue(T value) {
        // Attempt to remove possible value
        if (this.possibleValues.remove(value)) {
            // Update changed flag and delta set
            this.changed = true;
            this.possibleValuesDelta.add(value);
            
            // Update choice point stack for removed value
            HashSet<T> s = getPossibleRemovedValueSet();
            if (s!=null) {
                s.add(value);
                getPossibleDeltaAddedSet().add(value);
            }
        }
    }

    /**
     * Returns the set of added required values for the current choicepoint
     */
    private HashSet<T> getRequiredAddedValueSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> added = (HashSet<T>) cpdata.get("a");
        if (added == null) {
            added = new HashSet<T>();
            cpdata.put("a", added);
        }
        return added;
    }

    /**
     * Returns the added values to the required delta-set for the current choicepoint
     */
    private HashSet<T> getRequiredDeltaAddedSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> deltaAdded = (HashSet<T>) cpdata.get("rda");
        if (deltaAdded == null) {
            deltaAdded = new HashSet<T>();
            cpdata.put("rda", deltaAdded);
        }
        return deltaAdded;
    }

    /**
     * Returns the removed values of the required delta-set for the current choicepoint
     */
    private HashSet<T> getRequiredDeltaRemovedSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> deltaRemoved = (HashSet<T>) cpdata.get("rdr");
        if (deltaRemoved == null) {
            deltaRemoved = new HashSet<T>();
            cpdata.put("rdr", deltaRemoved);
        }
        return deltaRemoved;
    }

    /**
     * Returns the added values to the possible delta-set for the current choicepoint
     */
    private HashSet<T> getPossibleDeltaAddedSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> deltaAdded = (HashSet<T>) cpdata.get("pda");
        if (deltaAdded == null) {
            deltaAdded = new HashSet<T>();
            cpdata.put("pda", deltaAdded);
        }
        return deltaAdded;
    }

    /**
     * Returns the removed values of the possible delta-set for the current choicepoint
     */
    private HashSet<T> getPossibleDeltaRemovedSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> deltaRemoved = (HashSet<T>) cpdata.get("pdr");
        if (deltaRemoved == null) {
            deltaRemoved = new HashSet<T>();
            cpdata.put("pdr", deltaRemoved);
        }
        return deltaRemoved;
    }

    /**
     * Returns the set of removed possible values for the current choicepoint
     */
    private HashSet<T> getPossibleRemovedValueSet() {
        if (cpdata==null) return null;
        @SuppressWarnings("unchecked")
		HashSet<T> removed = (HashSet<T>) cpdata.get("r");
        if (removed == null) {
            removed = new HashSet<T>();
            cpdata.put("r", removed);
        }
        return removed;
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	if (this.cps == cps) return;
        if (this.cps!=null && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for domain");
        }
        this.cps = cps;

        if (cps==null) {
            if (cpdata!=null) cpdata.close();
            this.cpdata=null;
        }
        else
            this.cpdata = cps.newDataMap(this);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        if(this.cpdata==null)
            return false;
        return true;
    }

    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
        // Remove any values that were added to required set
        Iterator<T> iterator = getRequiredAddedValueSet().iterator();
        while (iterator.hasNext())
            requiredValues.remove(iterator.next());

        // Add any values that were removed from possible set
        iterator = getPossibleRemovedValueSet().iterator();
        while (iterator.hasNext())
            possibleValues.add(iterator.next());

        // Remove values added to delta of required
        iterator = getRequiredDeltaAddedSet().iterator();
        while (iterator.hasNext())
            requiredValuesDelta.remove(iterator.next());
        
        // Remove values added to delta of possible
        iterator = getPossibleDeltaAddedSet().iterator();
        while (iterator.hasNext())
            possibleValuesDelta.remove(iterator.next());
        
        // Add values removed from delta of required
        iterator = getRequiredDeltaRemovedSet().iterator();
        while (iterator.hasNext())
            requiredValuesDelta.add(iterator.next());
        
        // Add values removed from delta of possible
        iterator = getPossibleDeltaRemovedSet().iterator();
        while (iterator.hasNext())
            possibleValuesDelta.add(iterator.next());
        
        changed = false;
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        // do nothing, state is recorded as changes occur
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        // Add any values that were added to required set
        Iterator<T> iterator = getRequiredAddedValueSet().iterator();
        while (iterator.hasNext())
            requiredValues.add(iterator.next());

        // Removed any values that were removed from possible set
        iterator = getPossibleRemovedValueSet().iterator();
        while (iterator.hasNext())
            possibleValues.remove(iterator.next());

        // Add values added to delta of required
        iterator = getRequiredDeltaAddedSet().iterator();
        while (iterator.hasNext())
            requiredValuesDelta.add(iterator.next());
        
        // Add values added to delta of possible
        iterator = getPossibleDeltaAddedSet().iterator();
        while (iterator.hasNext())
            possibleValuesDelta.add(iterator.next());
        
        // Remove values removed from delta of required
        iterator = getRequiredDeltaRemovedSet().iterator();
        while (iterator.hasNext())
            requiredValuesDelta.remove(iterator.next());
        
        // Remove values removed from delta of possible
        iterator = getPossibleDeltaRemovedSet().iterator();
        while (iterator.hasNext())
            possibleValuesDelta.remove(iterator.next());
        
        changed = false;
    }
    
    /**
     * Helper class for enumerating values in set
     */
    private class SetDomainIterator implements Iterator<T> {
        private Iterator<T> iterator;

        /**
         * Constructor
         */
        public SetDomainIterator() {
            this.iterator = new HashSet<T>(possibleValues).iterator();
        }

        /**
         * returns true if more elements exits in enumeration
         */
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns next element in enumeration
         */
        public T next() {
            return iterator.next();
        }
        
        public void remove(){}
    }
    
    public String toString() {
        return "[Required:" + requiredValues.toString() + 
               ",Possible:" + possibleValues.toString() + "]";
    }
    
    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @return an Object containing the values of this domain
     */
	@SuppressWarnings("unchecked")
	public Object getDomainState() {
		HashMap<String, HashSet<T>> valuesMap = new HashMap<String, HashSet<T>>(2);
		valuesMap.put("pv", (HashSet<T>) possibleValues.clone());
		valuesMap.put("rv", (HashSet<T>) requiredValues.clone());
		return valuesMap;
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void restoreDomainState(Object state) {
		HashMap<String, HashSet<T>> valuesMap = (HashMap<String, HashSet<T>>) state;
		possibleValues = (HashSet<T>) (valuesMap.get("pv")).clone();
		requiredValues = (HashSet<T>) (valuesMap.get("rv")).clone();
		clearDelta();
	}

    public Object clone() {
        return new SetDomainBase<T>(this);
    }
}