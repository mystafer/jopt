package jopt.csp.search;

import jopt.csp.variable.CspIntVariable;


/**
 * Interface to implement to control the selection of values
 * when reducing integer domains.
 */
public interface IntegerSelector {
    public int select(CspIntVariable var);
}