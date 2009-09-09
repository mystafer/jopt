package jopt.csp.spi.arcalgorithm.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.PropagationFailureException;

public abstract class AbstractSetDomain extends AbstractDomain {
//  TODO: There is no indication anywhere as to why this class exists
//  instead of it all being in AbstractSetDomain. We need to document why
//  it is this way or move all the SetDomainBase code into AbstractSetDomain.
    protected SetDomainBase setdomain;
    
    /**
     * Constructor
     */
    protected AbstractSetDomain() {
        super(new HashSet(), null, new HashSet());
    }
    
    /**
     * Constructor
     */
    protected AbstractSetDomain(SetDomainBase base) {
        this();
        this.setdomain = base;
    }
    
    /**
     * Constructor
     */
    protected AbstractSetDomain(Collection col) {
        this(new SetDomainBase(col));
    }
    
    /**
     * Returns 1 + cardinality of possible - cardinality of required
     */
    public final int getSize() {
        return setdomain.getSize();
    }

    /**
     * Returns possible cardinality
     */
    public final int getPossibleCardinality() {
        return setdomain.getPossibleCardinality();
    }
    
    /**
     * Returns required cardinality
     */
    public final int getRequiredCardinality() {
        return setdomain.getRequiredCardinality();
    }
    
    //  javadoc is inherited
    public final boolean isBound() {
        return setdomain.isBound();
    }

    /**
     * Returns possible set of values
     */
    public final Set getPossibleSet() {
        return setdomain.getPossibleSet();
    }

    /**
     * Returns required set of values
     */
    public final Set getRequiredSet() {
        return setdomain.getRequiredSet();
    }

    /**
     * Returns the possible-delta set
     */
    public final Set getPossibleDeltaSet() {
        return setdomain.getPossibleDeltaSet();
    }

    /**
     * Returns the required-delta set
     */
    public final Set getRequiredDeltaSet() {
        return setdomain.getRequiredDeltaSet();
    }
        
    //  javadoc is inherited
    public final void clearDelta() {
        setdomain.clearDelta();
    }
    
    //  javadoc is inherited
    public final void setChoicePointStack(ChoicePointStack cps) {
        setdomain.setChoicePointStack(cps);
    }
    
    //  javadoc is inherited
    public final boolean choicePointStackSet() {
        return setdomain.choicePointStackSet();
    }
    
    /**
     * Returns iterator of possible values in domain
     */
    public final Iterator values() {
        return setdomain.values();
    }


    /**
     * Returns true if value is in domain
     */
    public final boolean isInDomain(Object val) {
    	return setdomain.isInDomain(val);
    }

    /**
     * Returns true if value is required
     */
    public final boolean isRequired(Object value) {
        return setdomain.isRequired(value);
    }
    
    /**
     * Returns true if value is possible
     */
    public final boolean isPossible(Object value) {
        return setdomain.isRequired(value);
    }
    
    /**
     * Adds a required value to the set
     */
    public final void addRequired(Object value) throws PropagationFailureException {
        setdomain.addRequired(value);

        // Notify listeners if domain changes
        if (setdomain.changed()) {
            notifyDomainChange();
        }
    }
    
    /**
     * Adds a set of values to the required set
     */
    public final void addRequired(Set values) throws PropagationFailureException {
        Iterator setIter = values.iterator();
        while (setIter.hasNext()) {
            addRequired(setIter.next());
        }
    }   
    
    /**
     * Removes a value from the possible set
     */
    public final void removePossible(Object value) throws PropagationFailureException {
        setdomain.removePossible(value);

        // Notify listeners if domain changes
        if (setdomain.changed()) {
            notifyDomainChange();
        }
    }
    
    /**
     * Removes a set of values from the possible set
     */
    public final void removePossible(Set values) throws PropagationFailureException {
        Iterator setIter = values.iterator();
        while (setIter.hasNext()) {
            removePossible(setIter.next());
        }
    }
    
    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
    public final Object getDomainState() {
        return this.setdomain.getDomainState();
    }
    
    /**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
    public final void restoreDomainState(Object state) {
        this.setdomain.restoreDomainState(state);
    }

    //  javadoc is inherited
    public String toString() {
        return "[Required:" + new TreeSet(getRequiredSet()).toString() + 
               ",Possible:" + new TreeSet(getPossibleSet()).toString() + "]";
    }
}