package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.arcalgorithm.domain.BooleanDomain;
import jopt.csp.variable.PropagationFailureException;

public class BooleanNode extends IntNode {
    /**
     * Constructor
     */
    public BooleanNode(String name, BooleanDomain domain) {
        super(name, domain);
    }
    
    private BooleanDomain getBooleanDomain() {
    	return (BooleanDomain) domain;
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Object val) {
        Boolean bool = (Boolean) val;
        return getBooleanDomain().isInDomain(bool.booleanValue());
    }
    
    /**
     * Attempts to reduce domain to a true value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setTrue() throws PropagationFailureException {
        getBooleanDomain().setTrue();
    }
    
    /**
     * Attempts to reduce domain to a false value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setFalse() throws PropagationFailureException {
    	getBooleanDomain().setFalse();
    }
    
    /**
     * Returns true if this domain is bound to a true value
     */
    public boolean isTrue() {
        return getBooleanDomain().isTrue();
    }
    
    /**
     * Returns true if this domain is bound to a false value
     */
    public boolean isFalse() {
    	return getBooleanDomain().isFalse();
    }
}

