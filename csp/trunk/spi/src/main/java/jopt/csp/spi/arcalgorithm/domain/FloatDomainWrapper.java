package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.FloatUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Wraps an integer or long domain for use with Float variables and expressions
 */
public class FloatDomainWrapper implements FloatDomain, ChoicePointDataSource {
    private LongDomain dom;

    public FloatDomainWrapper(IntDomain idom) {
        this(new LongDomainWrapper(idom));
    }
    public FloatDomainWrapper(LongDomain dom) {
        this.dom = dom;
    }

    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(float p) {
        // do nothing for integer / long domains
    }
    
    /**
     * Returns precision associated with this domain
     */
    public float getPrecision() {
        return FloatUtil.DEFAULT_PRECISION;
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
    public float getMax() {
        return dom.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public float getMin() {
        return dom.getMin();
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(float val) {
        if (val < Long.MIN_VALUE || val > Long.MAX_VALUE) return false;
        return dom.isInDomain((long) val);
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public float getNextHigher(float val) {
        if (val < Long.MIN_VALUE || val >= Long.MAX_VALUE) return val;
        return dom.getNextHigher((long) val);
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public float getNextLower(float val) {
        if (val <= Long.MIN_VALUE || val > Long.MAX_VALUE) return val;
        return dom.getNextLower((long) val);
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
    public void setMax(float val) throws PropagationFailureException {
        if (val < Long.MAX_VALUE)
            dom.setMax((long) val);
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(float val) throws PropagationFailureException {
        if (val > Long.MIN_VALUE)
            dom.setMin((long) val);
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(float val) throws PropagationFailureException {
        if (val < Long.MIN_VALUE || val > Long.MAX_VALUE)
            throw new PropagationFailureException();
        dom.setValue((long) val);
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(float val) throws PropagationFailureException {
        if (val >= Long.MIN_VALUE && val <= Long.MAX_VALUE)
            dom.removeValue((long) val);
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(float start, float end) throws PropagationFailureException {
        long s = (long) start;
        if (start < Long.MIN_VALUE) s = Long.MIN_VALUE;
        else if (start > Long.MAX_VALUE) s = Long.MAX_VALUE;

        long e = (long) end;
        if (e < Long.MIN_VALUE) e = Long.MIN_VALUE;
        else if (e > Long.MAX_VALUE) e = Long.MAX_VALUE;

        dom.setRange(s, e);
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(float start, float end) throws PropagationFailureException {
        long s = (long) start;
        if (start < Long.MIN_VALUE) s = Long.MIN_VALUE;
        else if (start > Long.MAX_VALUE) return;

        long e = (long) end;
        if (e < Long.MIN_VALUE) return;
        else if (e > Long.MAX_VALUE) e = Long.MAX_VALUE;

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
        return new FloatDomainWrapper(dom);
    }
}