package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Wraps an integer domain for use with Long variables and expressions
 */
public class LongDomainWrapper implements LongDomain, ChoicePointDataSource {
    private IntDomain dom;

    public LongDomainWrapper(IntDomain dom) {
        this.dom = dom;
    }

    /**
     * Returns size of domain
     */
    public int getSize() {
        return dom.getSize();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        return dom.isBound();
    }

    /**
     * Returns maximum value of domain
     */
    public long getMax() {
        return dom.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public long getMin() {
        return dom.getMin();
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Number val) {
        return isInDomain(val.longValue());
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(long val) {
        if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE) return false;
        return dom.isInDomain((int) val);
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public Number getNextHigher(Number val) {
        return new Long(getNextHigher(val.longValue()));
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public long getNextHigher(long val) {
        if (val < Integer.MIN_VALUE || val >= Integer.MAX_VALUE) return val;
        return dom.getNextHigher((int) val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public long getNextLower(long val) {
        if (val <= Integer.MIN_VALUE || val > Integer.MAX_VALUE) return val;
        return dom.getNextLower((int) val);
    }

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        return dom.values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        return dom.deltaValues();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet() {
        return dom.toSet();
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        return dom.getDeltaSet();
    }
    
    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        dom.clearDelta();
    }

    /**
     * Returns string representation of domain
     */
    public String toString() {
        return dom.toString();
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
    	((ChoicePointDataSource) dom).setChoicePointStack(cps);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        return ((ChoicePointDataSource) dom).choicePointStackSet();
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(long val) throws PropagationFailureException {
        if (val < Integer.MAX_VALUE)
            dom.setMax((int) val);
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(long val) throws PropagationFailureException {
        if (val > Integer.MIN_VALUE)
            dom.setMin((int) val);
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(long val) throws PropagationFailureException {
        if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE)
            throw new PropagationFailureException();
        dom.setValue((int) val);
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(long val) throws PropagationFailureException {
        if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE)
            dom.removeValue((int) val);
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(long start, long end) throws PropagationFailureException {
        int s;
        if (start < Integer.MIN_VALUE) s = Integer.MIN_VALUE;
        else if (start > Integer.MAX_VALUE) s = Integer.MAX_VALUE;
        else s = (int) start;

        
        int e;
        if (end < Integer.MIN_VALUE) e = Integer.MIN_VALUE;
        else if (end > Integer.MAX_VALUE) e = Integer.MAX_VALUE;
        else  e = (int) end;

        dom.setRange(s, e);
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(long start, long end) throws PropagationFailureException {
        int s;
        if (start < Integer.MIN_VALUE) s = Integer.MIN_VALUE;
        else if (start > Integer.MAX_VALUE) return;
        else s = (int) start;

        int e;
        if (end < Integer.MIN_VALUE) return;
        else if (end > Integer.MAX_VALUE) e = Integer.MAX_VALUE;
        else e = (int) end;

        dom.removeRange(s, e);
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        dom.setDomain(s);
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet s) throws PropagationFailureException {
        dom.removeDomain(s);
    }

    /**
     * Adds a listener to this domain interested in domain, range and value
     * events
     */
    public void addDomainChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).addDomainChangeListener(listener);
    }

    /**
     * Removes a domain listener from this domain
     */
    public void removeDomainChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).removeDomainChangeListener(listener);
    }

    /**
     * Adds a listener to this domain interested in range and value
     * events
     */
    public void addRangeChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).addRangeChangeListener(listener);
    }

    /**
     * Removes a range listener from this domain
     */
    public void removeRangeChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).removeRangeChangeListener(listener);
    }

    /**
     * Adds a listener to this domain only interested in value events
     */
    public void addValueChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).addValueChangeListener(listener);
    }

    /**
     * Removes a value listener from this domain
     */
    public void removeValueChangeListener(DomainChangeListener listener) {
        ((DomainChangeSource) dom).removeValueChangeListener(listener);
    }

    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
	public Object getDomainState() {
		return dom.getDomainState();
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public void restoreDomainState(Object state) {
		dom.restoreDomainState(state);
	}
	
	/**
     * Returns true if last operation caused a change to occur in  the domain
     */
	public boolean changed() {
		return dom.changed();
	}
	
    public Object clone() {
        return new LongDomainWrapper(dom);
    }
}