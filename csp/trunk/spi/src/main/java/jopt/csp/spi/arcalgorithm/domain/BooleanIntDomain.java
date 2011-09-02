package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.PropagationFailureException;

/**
 * Concrete boolean domain
 */
public class BooleanIntDomain extends IntSparseDomain implements BooleanDomain {
    /**
     * Constructor
     */
    public BooleanIntDomain() {
        super(0, 1);
    }
    
    /**
     * Constructor
     */
    public BooleanIntDomain(boolean tf) {
        this(tf ? 1 : 0);
    }

    /**
     * Constructor for cloning domain
     */
    public BooleanIntDomain(int tfVal) {
        super(tfVal, tfVal);
    }

    /**
     * Returns true if a value is contained in this node's domain
     */
    public boolean isInDomain(boolean val) {
    	if (val)
            return isInDomain(1);
        else
            return isInDomain(0);
    }

    /**
     * Attempts to reduce domain to a true value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setTrue() throws PropagationFailureException {
    	setValue(1);
    }

    /**
     * Attempts to reduce domain to a false value
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setFalse() throws PropagationFailureException {
    	setValue(0);
    }

    /**
     * Returns true if this domain is bound to a true value
     */
    public boolean isTrue() {
    	return isBound() && getMin()==1;
    }

    /**
     * Returns true if this domain is bound to a false value
     */
    public boolean isFalse() {
        return isBound() && getMin()==0;
    }
    
    public Object clone() {
        if (isTrue())
            return new BooleanIntDomain(1);
        else if (isFalse())
            return new BooleanIntDomain(0);
        else
        	return new BooleanIntDomain();
    }
}
