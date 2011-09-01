package jopt.csp.search;

import jopt.csp.variable.CspSetVariable;

/**
 * Interface to implement to control the selection of values
 * when reducing set domains.
 */
public interface SetSelector<T> {
    public T select(CspSetVariable<T> var);
}