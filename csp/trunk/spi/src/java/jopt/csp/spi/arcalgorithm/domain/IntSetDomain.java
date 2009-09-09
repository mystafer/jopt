package jopt.csp.spi.arcalgorithm.domain;

import java.util.ArrayList;
import java.util.Collection;

import jopt.csp.variable.PropagationFailureException;

public class IntSetDomain extends AbstractSetDomain implements SetDomain {
    /**
     * Internal Constructor
     */
    private IntSetDomain(SetDomainBase base) {
        super(base);
    }
    
    /**
     * Creates a domain containing all values in collection
     */
    public IntSetDomain(Collection values) {
        super(values);
    }
    
    /**
     * Creates a domain containing all values from min to max
     */
    public IntSetDomain(int min, int max) {
        super();
        
        ArrayList list = new ArrayList();
        for (int i=min; i<=max; i++)
            list.add(new Integer(i));
        this.setdomain = new SetDomainBase(list);
    }
    
    /**
     * Returns true if domain over real intervals
     */
    public boolean isOverRealInterval() {
        return false;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(int val) {
        return setdomain.isInDomain(new Integer(val));
    }

    /**
     * Returns true if value is required
     */
    public boolean isRequired(int value) {
        return setdomain.isRequired(new Integer(value));
    }
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(int value) {
        return setdomain.isPossible(new Integer(value));
    }
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(int value) throws PropagationFailureException {
        setdomain.addRequired(new Integer(value));

        // Notify listeners if domain changes
        if (setdomain.changed()) notifyDomainChange();
    }

    /**
     * Removes a value from the possible set
     */
    public void removePossible(int value) throws PropagationFailureException {
        setdomain.removePossible(new Integer(value));

        // Notify listeners if domain changes
        if (setdomain.changed()) notifyDomainChange();
    }
    
    public Object clone() {
        return new IntSetDomain((SetDomainBase) setdomain.clone());
    }
}
