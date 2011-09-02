/*
 * DoubleClass.java
 * 
 * Created on Apr 11, 2005
 */
package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.variable.CspDoubleCast;

/**
 * Interface allowing other variables and expressions to be casted to a double
 * type for use with double expressions
 */
public interface DoubleCast extends CspDoubleCast, NumExpr {
    /**
     * Returns double domain for this expression
     */
    public DoubleDomain getDoubleDomain();
}
