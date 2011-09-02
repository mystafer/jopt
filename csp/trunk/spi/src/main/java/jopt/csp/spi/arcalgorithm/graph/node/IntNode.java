package jopt.csp.spi.arcalgorithm.graph.node;

import jopt.csp.spi.arcalgorithm.domain.DomainChangeSource;
import jopt.csp.spi.arcalgorithm.domain.IntDomain;
import jopt.csp.spi.arcalgorithm.domain.IntIntervalDomain;
import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NameUtil;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Node for integer domains
 */
public class IntNode extends AbstractNode implements NumNode {

    protected IntDomain domain;
    
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    private MutableNumber nextHigher = new MutableNumber();
    private MutableNumber nextLower = new MutableNumber();

    /**
     * Constructor
     */
    public IntNode(String name, IntDomain domain) {
        super(name);
        this.domain = domain;

        // Listen for changes to domain
        ((DomainChangeSource) domain).addDomainChangeListener(new DomainListener());
    }
    
    /**
     * Constructor that gives the node a unique name and a domain from
     * Integer.MIN_VALUE to Integer.MAX_VALUE
     */
    public IntNode() {
        this(NameUtil.nextName()+"tempNode", new IntIntervalDomain(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    /**
     * Returns precision associated with this domain for real numbers
     */
    public double getPrecision() {
    	return 0;
    }
    
    /**
     * Returns the maximum domain value for this node
     */
    public Number getMax() {
        max.setIntValue(domain.getMax());
        return max;
    }

    /**
     * Returns the minimum domain value for this node
     */
    public Number getMin() {
        min.setIntValue(domain.getMin());
        return min;
    }

    // javadoc is inherited
    public Number getNumMax() {
        return getMax();
    }

    // javadoc is inherited
    public Number getNumMin() {
        return getMin();
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Number val) {
        return NumberMath.isIntEquivalent(val) && domain.isInDomain(val.intValue());
    }
    
    /**
     * Returns the next higher domain value for a given n.  Returns n
     * if no higher value exists
     */
    public Number getNextHigher(Number n) {
        nextHigher.setIntValue(domain.getNextHigher(NumberMath.intFloor(n)));
        return nextHigher;
    }

    /**
     * Returns the next lower domain value for a given n.  Returns n
     * if no lower value exists
     */
    public Number getNextLower(Number n) {
        nextLower.setIntValue(domain.getNextLower(NumberMath.intCeil(n)));
        return nextLower;
    }

    /**
     * Returns true if domain for this node is bound to a single value
     */
    public boolean isBound() {
        return domain.isBound();
    }

    /**
     * Returns true if a value n is contained in this node's domain
     */
    public boolean isInDomain(Object n) {
        return isInDomain((Number) n);
    }

    /**
     * Returns size of domain for this node.  If domain is infinite, value
     * is Integer.MAX_VALUE
     */
    public int getSize() {
        return domain.getSize();
    }

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        return domain.values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        return domain.deltaValues();
    }
    
    /**
     * Restricts domain to all values less than or equal to value given
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setMax(Number n) throws PropagationFailureException {
        domain.setMax(NumberMath.intFloor(n));
    }

    /**
     * Restricts domain to all values greater than or equal to value given
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setMin(Number n) throws PropagationFailureException {
        domain.setMin(NumberMath.intCeil(n));
    }

    /**
     * Removes a single value from the domain of this node
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeValue(Number n) throws PropagationFailureException {
        if (NumberMath.isIntEquivalent(n))
            domain.removeValue(n.intValue());
    }

    /**
     * Restricts domain to a single value
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setValue(Number n) throws PropagationFailureException {
        if (!NumberMath.isIntEquivalent(n)) throw new PropagationFailureException();
        domain.setValue(n.intValue());
    }

    /**
     * Removes a range of values from the domain of this node
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeRange(Number start, Number end) throws PropagationFailureException {
        domain.removeRange(NumberMath.intCeil(start), NumberMath.intFloor(end));
    }

    /**
     * Restricts domain of this node to a range of values
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setRange(Number start, Number end) throws PropagationFailureException {
        domain.setRange(NumberMath.intCeil(start), NumberMath.intFloor(end));
    }

    /**
     * Removes all values in this node's domain contained within set S
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void removeDomain(NumSet s) throws PropagationFailureException {
        domain.removeDomain(s);
    }

    /**
     * Restricts node's domain to values contained within set S
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        domain.setDomain(s);
    }

    /**
     * Returns domain of node as a set of Number and NumInterval objects
     *
     * @throws PropagationFailureException  If domain is empty
     */
    public NumSet getDomain() {
        return domain.toSet();
    }
    
    /**
     * Clears the delta set for this node's domain
     */
    public void clearDelta() {
        domain.clearDelta();
    }
    
    /**
     * Sets the choicepoint stack associated with this graph
     * Can only be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) domain).setChoicePointStack(cps);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) domain).choicePointStackSet();
    }
    	
    /**
     * Returns string representation of node
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(getName());
        buf.append(":");
        buf.append(domain);
        
        return buf.toString();
    }

    /**
	 * Stores appropriate data for future restoration.
	 */
    public Object getState() {
    	return domain.getDomainState();
    }
    
    /**
	 *  Restores variable information from stored data.
	 */
    public void restoreState(Object state) {
    	domain.restoreDomainState(state);
    }
    
    /**
     * Returns the type of number this node is based on.
     */
    public int getNumberType() {
        return NumConstants.INTEGER;
    }
}

