package jopt.csp.spi.arcalgorithm.variable;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.set.SetVariable;
import jopt.csp.spi.arcalgorithm.domain.SetDomain;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.PropagationFailureException;

public abstract class SetVariableBase<T> extends VariableChangeBase implements CspSetVariable<T>, SetVariable<T>, Variable {
    protected String name;
    protected SetDomain<T> domain;
    
    /**
     * Constructor
     */
    protected SetVariableBase(String name, SetDomain<T> domain) {
        this.name = name;
        this.domain = domain;
    }
    
    /**
     * Returns name of node
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets name of node
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Retrieves node for an expression
     */
    public abstract SetNode<T> getNode();

    /**
     * Returns domain for variable
     */
    public SetDomain<T> getDomain() {
        return domain;
    }

    /**
     * Adds arcs representing this expression to the node arc graph
     */
    public void updateGraph(NodeArcGraph graph) {
    }
    
    /**
     * Returns a hash code based on this variable's name (if one exists)
     */
    public int hashCode() {
    	if(name != null) {
    		return this.name.hashCode();
    	}
    	else {
    		return super.hashCode();
    	}
    }
    
    /**
	 * Allows for equality comparisons between SetVariableBase objects
	 * 
	 * @param obj
	 * @return true if the specified object is a SetVariableBase
	 * and its name is identical to this SetVariableBase object
	 */
    // This has to be final because the way it's currently done with
    // instanceof instead of getClass means that if it is overridden
    // it will violate the reflexive contract for equals.
    @SuppressWarnings("unchecked")
	public final boolean equals(Object obj) {
    	if(obj instanceof SetVariableBase) {
    		return ((SetVariableBase<T>) obj).getName().equals(this.name);
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Returns true if value is in this variable's domain
     */
    public final boolean isInDomain(T val) {
        return domain.isInDomain(val);
    }

    /**
     * Returns true if the specified value is required
     */
    public final boolean isRequired(T value) {
        return domain.isRequired(value);
    }

    /**
     * Returns true if the specified value is possible
     */
    public final boolean isPossible(T value) {
        return domain.isPossible(value);
    }

    /**
     * Adds a required value to the set
     */
    public final void addRequired(T value) throws PropagationFailureException {
        domain.addRequired(value);
        
        //notify listeners if the variable changed
        if(domain.changed()) {
            fireChangeEvent();
        }
    }
    
    /**
     * Adds a set of values to the required set
     */
    public final void addRequired(Set<T> values) throws PropagationFailureException {
        domain.addRequired(values);
        
        //notify listeners if the variable changed
        if(domain.changed()) {
            fireChangeEvent();
        }
    }   
    
    /**
     * Removes a value from the possible set
     */
    public final void removePossible(T value) throws PropagationFailureException {
        domain.removePossible(value);
        
        //notify listeners if the variable changed
        if(domain.changed()) {
            fireChangeEvent();
        }
    }
    
    /**
     * Removes a set of values from the possible set
     */
    public final void removePossible(Set<T> values) throws PropagationFailureException {
        domain.removePossible(values);
        
        //notify listeners if the variable changed
        if(domain.changed()) {
            fireChangeEvent();
        }
    }
    
    /**
     * Clears the delta set for this domain
     */
    public final void clearDelta() {
        domain.clearDelta();
    }
    
//    /**
//     * Returns enumeration of possible values in domain
//     */
//    public Enumeration domainValues() {
//        return domain.values();
//    }
    
    /**
     * Returns 1 + cardinality of possible - cardinality of required
     */
    public final int getSize() {
        return domain.getSize();
    }
    
    /**
     * Returns possible cardinality
     */
    public final int getPossibleCardinality() {
        return domain.getPossibleCardinality();
    }
    
    /**
     * Returns the possible-delta set
     */
    public final Set<T> getPossibleDeltaSet() {
        return domain.getPossibleDeltaSet();
    }
    
    /**
     * Returns possible set of values in this variable's domain
     */
    public final Set<T> getPossibleSet() {
        return domain.getPossibleSet();
    }
    
    /**
     * Returns required cardinality
     */
    public final int getRequiredCardinality() {
        return domain.getRequiredCardinality();
    }
    
    /**
     * Returns the required-delta set
     */
    public final Set<T> getRequiredDeltaSet() {
        return domain.getRequiredDeltaSet();
    }
    
    /**
     * Returns required set of values in this variable's domain
     */
    public final Set<T> getRequiredSet() {
        return domain.getRequiredSet();
    }
    
    /**
     * Returns true if this variable's domain is bound to a value
     */
    public final boolean isBound() {
        return domain.isBound();
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
    
    public String toString() {
    	return this.name+":"+this.domain;
    }
}