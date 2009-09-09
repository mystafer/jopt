/*
 * SummationDomainExpression.java
 * 
 * Created on May 31, 2005
 */
package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.variable.CspGenericIndex;


/**
 * Interface implemented by classes that are used to build
 * this type of computed domain
 */
public interface SummationDoubleDomainExpression {
	/**
     * Returns the minimum value of the expression that corresponds
     * to the current index combination
	 */
    public double getDomainMinForIndex();
    
    /**
     * Returns the maximum value of the expression that corresponds
     * to the current index combination
     */
    public double getDomainMaxForIndex();
    
    /**
     * Returns the generic index that is associated with this expression
     */
    public CspGenericIndex[] getIndices();
    
    /**
     * Returns a source for registering a domain change listener
     * corresponding to the current index combination
     */
    public DomainChangeSource getDomainChangeSourceForIndex();
}