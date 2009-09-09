/*
 * TrigExpr.java
 * 
 * Created on Jun 2, 2005
 */
package jopt.csp.spi.util;



/**
 * Interface implemented by classes that can be used in 
 * trig expressions
 */
public interface TrigExpr {
	/**
	 * Returns the maximum domain value for this expression
	 */
	public Number getNumMax();

	/**
	 * Returns the minimum domain value for this expression
	 */
	public Number getNumMin();

	/**
	 * Returns true if value is in domain of this expression
	 */
	public boolean isInDomain(Number val);
}