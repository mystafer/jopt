/*
 * DoubleClass.java
 * 
 * Created on Apr 11, 2005
 */
package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.domain.LongDomain;
import jopt.csp.variable.CspLongCast;

/**
 * Interface allowing other variables and expressions to be casted to a long
 * type for use with long expressions
 */
public interface LongCast extends CspLongCast, FloatCast {
    /**
     * Returns double domain for this expression
     */
    public LongDomain getLongDomain();
}
