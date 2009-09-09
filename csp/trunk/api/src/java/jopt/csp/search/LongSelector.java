package jopt.csp.search;

import jopt.csp.variable.CspLongVariable;


/**
 * Interface to implement to control the selection of values
 * when reducing long domains.
 */
public interface LongSelector {
    public long select(CspLongVariable var);
}