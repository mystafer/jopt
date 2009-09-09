package jopt.csp.spi.arcalgorithm.graph.node;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.IntSetDomain;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

public class IntSetNode extends AbstractNode implements SetNode {
    private IntSetDomain domain;

    /**
     * Constructor
     */
    public IntSetNode(String name, IntSetDomain domain) {
        super(name);
        this.domain = domain;
        
        // Listen for changes to domain
        ((DomainChangeSource) domain).addDomainChangeListener(new DomainListener());
    }

    /**
     * Returns true if domain for this node is bound to a single value
     */
    public boolean isBound() {
        return domain.isBound();
    }

    /**
     * Returns true if a value n is contained in this node's domain
     */
    public boolean isInDomain(Object n) {
        Number i = (Number) n;
        if (!NumberMath.isIntEquivalent(i))
            return false;
        else
            return domain.isInDomain(i.intValue());
    }

    /**
     * Returns size of domain for this node.  If domain is infinite, value
     * is Integer.MAX_VALUE
     */
    public int getSize() {
        return domain.getSize();
    }

    /**
     * Returns true if domain of node is over real intervals
     */
    public boolean isOverRealInterval() {
        return false;
    }

    /**
     * Returns true if value is required
     */
    public boolean isRequired(Object n) {
        Number i = (Number) n;
        if (!NumberMath.isIntEquivalent(i))
            return false;
        else
            return domain.isRequired(i.intValue());
    }
    
    /**
     * Returns true if value is possible
     */
    public boolean isPossible(Object n) {
        Number i = (Number) n;
        if (!NumberMath.isIntEquivalent(i))
            return false;
        else
            return domain.isPossible(i.intValue());
    }
    
    /**
     * Adds a required value to the set
     */
    public void addRequired(Object n) throws PropagationFailureException {
        Number i = (Number) n;
        if (!NumberMath.isIntEquivalent(i))
            throw new PropagationFailureException();
        else
            domain.addRequired(i.intValue());
    }

    /**
     * Removes a value from the possible set
     */
    public void removePossible(Object n) throws PropagationFailureException {
        Number i = (Number) n;
        if (!NumberMath.isIntEquivalent(i))
            throw new PropagationFailureException();
        else
            domain.removePossible(i.intValue());
    }
    
    /**
     * Returns possible set of values
     */
    public Set getPossibleSet() {
        return domain.getPossibleSet();
    }

    /**
     * Returns required set of values
     */
    public Set getRequiredSet() {
        return domain.getRequiredSet();
    }

    /**
     * Returns the possible-delta set
     */
    public Set getPossibleDeltaSet() {
        return domain.getPossibleDeltaSet();
    }

    /**
     * Returns the required-delta set
     */
    public Set getRequiredDeltaSet() {
        return domain.getRequiredDeltaSet();
    }
    
    /**
     * Returns possible cardinality
     */
    public int getPossibleCardinality() {
        return domain.getPossibleCardinality();
    }
    
    /**
     * Returns required cardinality
     */
    public int getRequiredCardinality() {
        return domain.getRequiredCardinality();
    }
    
    /**
     * Clears the delta set for this node's domain
     */
    public void clearDelta() {
        domain.clearDelta();
    }
    
    public Iterator values() {
        return domain.values();
    }
    
    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) domain).setChoicePointStack(cps);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) domain).choicePointStackSet();
    }
    	
    /**
     * Returns string representation of node
     */
    public String toString() {
        return getName() + ":" + domain.toString();
    }

    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return domain.getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	domain.restoreDomainState(state);
    }
    
    public int getDomainSize() {
		return domain.getSize();
	}
	
	public boolean isDomainBound() {
		return domain.isBound();
	}
	
	public boolean isDomainOverRealInterval() {
		return domain.isOverRealInterval();
	}
 }

