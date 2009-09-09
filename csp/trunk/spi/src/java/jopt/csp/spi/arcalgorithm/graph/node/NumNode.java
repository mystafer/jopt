package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.util.TrigExpr;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

public interface NumNode extends Node, NodeChangeSource, TrigExpr {
    /**
     * Returns the maximum domain value for this node
     */
    public Number getMax();

    /**
     * Returns the minimum domain value for this node
     */
    public Number getMin();

    /**
     * Returns the next higher domain value for a given n.  Returns n
     * if no higher value exists
     */
    public Number getNextHigher(Number n);

    /**
     * Returns the next lower domain value for a given n.  Returns n
     * if no lower value exists
     */
    public Number getNextLower(Number n);

    /**
     * Restricts domain to all values less than or equal to value given
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setMax(Number n) throws PropagationFailureException;

    /**
     * Restricts domain to all values greater than or equal to value given
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setMin(Number n) throws PropagationFailureException;

    /**
     * Removes a single value from the domain of this node
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeValue(Number n) throws PropagationFailureException;

    /**
     * Restricts domain to a single value
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setValue(Number n) throws PropagationFailureException;

    /**
     * Removes a range of values from the domain of this node
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeRange(Number start, Number end)
            throws PropagationFailureException;

    /**
     * Restricts domain of this node to a range of values
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setRange(Number start, Number end)
            throws PropagationFailureException;

    /**
     * Removes all values in this node's domain contained within set S
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeDomain(NumSet s) throws PropagationFailureException;

    /**
     * Restricts node's domain to values contained within set S
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException;

    /**
     * Returns domain of node as a set of Number and NumInterval objects
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public NumSet getDomain();

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Number val);
    
    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values();
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues();

    /**
     * Returns precision associated with this domain for real numbers
     */
    public double getPrecision();
    
    /**
     * Returns the type of number this node is based on.
     */
    public int getNumberType();

}