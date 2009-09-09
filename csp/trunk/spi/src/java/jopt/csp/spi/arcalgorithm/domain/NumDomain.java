package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for all numeric based domain interfaces
 */
public interface NumDomain extends Domain {
    /**
     * Returns size of domain
     */
    public int getSize();

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException;

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet vals) throws PropagationFailureException;

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values();

    /**
     * Returns iterator for the node's delta set containing values that have been removed
     * from the domain since clearDelta was last run.
     */
    public NumberIterator deltaValues();

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet();

    /**
     * Returns the delta set containing values that have been removed
     * from the domain since clearDelta was last run.
     */
    public NumSet getDeltaSet();
}