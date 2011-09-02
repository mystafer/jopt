/*
 * DoubleClass.java
 * 
 * Created on Apr 11, 2005
 */
package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.domain.FloatDomain;
import jopt.csp.variable.CspFloatCast;

/**
 * Interface allowing other variables and expressions to be casted to a float
 * type for use with float expressions
 */
public interface FloatCast extends CspFloatCast, DoubleCast {
    /**
     * Returns double domain for this expression
     */
    public FloatDomain getFloatDomain();
}
