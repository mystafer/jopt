package jopt.csp.spi.arcalgorithm.variable;

import java.util.Collection;

import jopt.csp.spi.arcalgorithm.domain.IntSetDomain;
import jopt.csp.spi.arcalgorithm.graph.node.IntSetNode;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;
import jopt.csp.variable.CspIntSetVariable;
import jopt.csp.variable.PropagationFailureException;

public class IntSetVariable extends SetVariableBase<Integer> implements CspIntSetVariable, Variable {
    private SetNode<Integer> node;
    
    /**
     * Constructor
     */
    public IntSetVariable(String name, IntSetDomain domain) {
        super(name, domain);
    }
    
    /**
     * Constructor
     */
    public IntSetVariable(String name, int min, int max) {
        this(name, new IntSetDomain(min, max));
    }
    
    /**
     * Constructor
     */
    public IntSetVariable(String name, Collection<Integer> values) {
        this(name, new IntSetDomain(values));
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
    public SetNode<Integer> getNode() {
        if (node == null) node = new IntSetNode(name, getIntSetDomain());
        return node;
    }

    /**
     * Returns domain for variable
     */
    private IntSetDomain getIntSetDomain() {
        return (IntSetDomain) domain;
    }
    
    /**
	 * Returns true if value is in domain
	 */
	public boolean isInDomain(int val) {
		return this.getIntSetDomain().isInDomain(val);
	}

	/**
	 * Returns true if value is required
	 */
	public boolean isRequired(int value) {
		return this.getIntSetDomain().isRequired(value);
	}

	/**
	 * Returns true if value is possible
	 */
	public boolean isPossible(int value) {
		return this.getIntSetDomain().isPossible(value);
	}

    /**
     * Adds a required value to the set
     */
    public void addRequired(int value) throws PropagationFailureException {
    	addRequired(new Integer(value));
    }
    
    /**
     * Removes a value from the possible set
     */
    public void removePossible(int value) throws PropagationFailureException {
        removePossible(new Integer(value));
    }
}