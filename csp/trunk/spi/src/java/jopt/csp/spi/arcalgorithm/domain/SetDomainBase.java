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
public class SetDomainBase implements ChoicePointEntryListener {
	private ChoicePointStack cps;
    private ChoicePointDataMap cpdata;
    private HashSet possibleValues;
    private HashSet requiredValues;
    private HashSet possibleValuesDelta;
    private HashSet requiredValuesDelta;
    private boolean changed;
    
    /**
     * Constructor
     */
    public SetDomainBase(Collection col) {
        this.possibleValues = new HashSet(col);
        this.requiredValues = new HashSet();
        this.possibleValuesDelta = new HashSet();
        this.requiredValuesDelta = new HashSet();
    }
    
    /**
     * Internal constructor
     */
    private SetDomainBase(SetDomainBase base) {
        this.possibleValues = new HashSet(base.possibleValues);
        this.requiredValues = new HashSet(base.requiredValues);
        this.possibleValuesDelta = new HashSet(base.possibleValuesDelta);
        this.requiredValuesDelta = new HashSet(base.requiredValuesDelta);
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
    public boolean isRequired(Object value) {
        return requiredValues.contains(value);
    }
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(Object value) {
        return possibleValues.contains(value);
    }
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(Object value) throws PropagationFailureException {
        if (!possibleValues.contains(value)) throw new PropagationFailureException();
        internalAddRequiredValue(value);
    }

    /**
     * Removes a value from the possible set
     */
    public void removePossible(Object value) throws PropagationFailureException {
        if (requiredValues.contains(value)) throw new PropagationFailureException();
        internalRemovePossibleValue(value);
    }
    
    /**
     * Returns possible set of values
     */
    public Set getPossibleSet() {
        return new HashSet(possibleValues);
    }
    
    /**
     * Returns the possible-delta set
     */
    public Set getPossibleDeltaSet() {
        return new HashSet(possibleValuesDelta);
    }

    /**
     * Returns required set of values
     */
    public Set getRequiredSet() {
        return new HashSet(requiredValues);
    }
    
    /**
     * Returns the required-delta set
     */
    public Set getRequiredDeltaSet() {
        return new HashSet(requiredValuesDelta);
    }

    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        // Retrieve choice point sets
        Set reqAddedSet = getRequiredDeltaAddedSet();
        Set reqRemovedSet = getRequiredDeltaRemovedSet();
        Set posAddedSet = getPossibleDeltaAddedSet();
        Set posRemovedSet = getPossibleDeltaRemovedSet();
        
        // Update choice point if necessary
        if (reqAddedSet != null) {
            // Update required delta sets
            Iterator iterator = requiredValuesDelta.iterator();
            while (iterator.hasNext()) {
                Object val = iterator.next();

                // First attempt to remove from recently added set
                if (!reqAddedSet.remove(val))
                    reqRemovedSet.add(val);
            }
        
            // Update possible delta sets
            iterator = possibleValuesDelta.iterator();
            while (iterator.hasNext()) {
                Object val = iterator.next();

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
    public Iterator values() {
        return new SetDomainIterator();
    }

    /**
     * Adds a required value to the domain and records the change in the
     * choicepoint
     */
    private void internalAddRequiredValue(Object value) {
        // Attempt to add required value
        if (this.requiredValues.add(value)) {
            // Update changed flag and delta set
            this.changed = true;
            this.requiredValuesDelta.add(value);
            
            // Update choice point stack for added value
            HashSet s = getRequiredAddedValueSet();
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
    private void internalRemovePossibleValue(Object value) {
        // Attempt to remove possible value
        if (this.possibleValues.remove(value)) {
            // Update changed flag and delta set
            this.changed = true;
            this.possibleValuesDelta.add(value);
            
            // Update choice point stack for removed value
            HashSet s = getPossibleRemovedValueSet();
            if (s!=null) {
                s.add(value);
                getPossibleDeltaAddedSet().add(value);
            }
        }
    }

    /**
     * Returns the set of added required values for the current choicepoint
     */
    private HashSet getRequiredAddedValueSet() {
        if (cpdata==null) return null;
        HashSet added = (HashSet) cpdata.get("a");
        if (added == null) {
            added = new HashSet();
            cpdata.put("a", added);
        }
        return added;
    }

    /**
     * Returns the added values to the required delta-set for the current choicepoint
     */
    private HashSet getRequiredDeltaAddedSet() {
        if (cpdata==null) return null;
        HashSet deltaAdded = (HashSet) cpdata.get("rda");
        if (deltaAdded == null) {
            deltaAdded = new HashSet();
            cpdata.put("rda", deltaAdded);
        }
        return deltaAdded;
    }

    /**
     * Returns the removed values of the required delta-set for the current choicepoint
     */
    private HashSet getRequiredDeltaRemovedSet() {
        if (cpdata==null) return null;
        HashSet deltaRemoved = (HashSet) cpdata.get("rdr");
        if (deltaRemoved == null) {
            deltaRemoved = new HashSet();
            cpdata.put("rdr", deltaRemoved);
        }
        return deltaRemoved;
    }

    /**
     * Returns the added values to the possible delta-set for the current choicepoint
     */
    private HashSet getPossibleDeltaAddedSet() {
        if (cpdata==null) return null;
        HashSet deltaAdded = (HashSet) cpdata.get("pda");
        if (deltaAdded == null) {
            deltaAdded = new HashSet();
            cpdata.put("pda", deltaAdded);
        }
        return deltaAdded;
    }

    /**
     * Returns the removed values of the possible delta-set for the current choicepoint
     */
    private HashSet getPossibleDeltaRemovedSet() {
        if (cpdata==null) return null;
        HashSet deltaRemoved = (HashSet) cpdata.get("pdr");
        if (deltaRemoved == null) {
            deltaRemoved = new HashSet();
            cpdata.put("pdr", deltaRemoved);
        }
        return deltaRemoved;
    }

    /**
     * Returns the set of removed possible values for the current choicepoint
     */
    private HashSet getPossibleRemovedValueSet() {
        if (cpdata==null) return null;
        HashSet removed = (HashSet) cpdata.get("r");
        if (removed == null) {
            removed = new HashSet();
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
        Iterator iterator = getRequiredAddedValueSet().iterator();
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
        Iterator iterator = getRequiredAddedValueSet().iterator();
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
    private class SetDomainIterator implements Iterator {
        private Iterator iterator;

        /**
         * Constructor
         */
        public SetDomainIterator() {
            this.iterator = new HashSet(possibleValues).iterator();
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
        public Object next() {
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
	public Object getDomainState() {
		HashMap valuesMap = new HashMap(2);
		valuesMap.put("pv", possibleValues.clone());
		valuesMap.put("rv", requiredValues.clone());
		return valuesMap;
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public void restoreDomainState(Object state) {
		HashMap valuesMap = (HashMap) state;
		possibleValues = (HashSet) ((HashSet) valuesMap.get("pv")).clone();
		requiredValues = (HashSet) ((HashSet) valuesMap.get("rv")).clone();
		clearDelta();
	}

    public Object clone() {
        return new SetDomainBase(this);
    }
}