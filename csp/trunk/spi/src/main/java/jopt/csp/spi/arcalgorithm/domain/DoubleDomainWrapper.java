package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointDataSource;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;
import jopt.csp.variable.PropagationFailureException;

/**
 * Wraps an integer, long or float domain for use with Double
 * variables and expressions
 */
public class DoubleDomainWrapper implements DoubleDomain, ChoicePointDataSource {
    private LongDomain ldom;
    private FloatDomain fdom;

    public DoubleDomainWrapper(IntDomain idom) {
        this(new LongDomainWrapper(idom));
    }
    public DoubleDomainWrapper(LongDomain ldom) {
        this.ldom = ldom;
    }
    public DoubleDomainWrapper(FloatDomain fdom) {
        this.fdom = fdom;
    }

    /**
     * Sets precision associated with this domain
     */
    public void setPrecision(double p) {
        // do nothing for integer / long domains
        if (fdom!=null)
            fdom.setPrecision((float) p);
    }
    
    /**
     * Returns precision associated with this domain
     */
    public double getPrecision() {
        if (fdom!=null)
            return fdom.getPrecision();
        else
        	return DoubleUtil.DEFAULT_PRECISION;
    }
    
    /**
     * Returns size of domain
     */
    public int getSize() {
        if (ldom!=null)
            return ldom.getSize();
        else
            return fdom.getSize();
    }

    /**
     * Returns true if domain is bound to a value
     */
    public boolean isBound() {
        if (ldom!=null)
            return ldom.isBound();
        else
            return fdom.isBound();
    }

    /**
     * Returns maximum value of domain
     */
    public double getMax() {
        if (ldom!=null)
            return ldom.getMax();
        else
            return fdom.getMax();
    }

    /**
     * Returns minimum value of domain
     */
    public double getMin() {
        if (ldom!=null)
            return ldom.getMin();
        else
            return fdom.getMin();
    }

    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(Number val) {
        return isInDomain(val.doubleValue());
    }
    
    /**
     * Returns true if value is in domain
     */
    public boolean isInDomain(double val) {
        if (ldom!=null) {
            if (val < Long.MIN_VALUE || val > Long.MAX_VALUE) return false;
            return ldom.isInDomain((long) val);
        }
        else {
            if (val < -Float.MAX_VALUE || val > Float.MAX_VALUE) return false;
            return fdom.isInDomain((float) val);
        }
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public Number getNextHigher(Number val) {
        return new Double(getNextHigher(val.doubleValue()));
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public double getNextHigher(double val) {
        if (ldom!=null) {
            if (val < Long.MIN_VALUE || val >= Long.MAX_VALUE) return val;
            return ldom.getNextHigher((long) val);
        }
        else {
            if (val < -Float.MAX_VALUE || val >= Float.MAX_VALUE) return val;
            return fdom.getNextHigher((float) val);
        }
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public Number getNextLower(Number val) {
        return new Double(getNextLower(val.doubleValue()));
    }

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public double getNextLower(double val) {
        if (ldom!=null) {
            if (val <= Long.MIN_VALUE || val > Long.MAX_VALUE) return val;
            return ldom.getNextLower((long) val);
        }
        else {
            if (val <= -Float.MAX_VALUE || val > Float.MAX_VALUE) return val;
            return fdom.getNextLower((float) val);
        }
    }

    /**
     * Returns iterator of values in node's domain
     */
    public NumberIterator values() {
        if (ldom!=null)
        	return ldom.values();
        else
            return fdom.values();
    }
    
    /**
     * Returns iterator of values in node's delta
     */
    public NumberIterator deltaValues() {
        if (ldom!=null)
            return ldom.deltaValues();
        else
            return fdom.deltaValues();
    }

    /**
     * Returns set of Numbers and NumIntervals representing domain
     */
    public NumSet toSet() {
        if (ldom!=null)
            return ldom.toSet();
        else
        	return fdom.toSet();
    }

    /**
     * Returns the delta set for this domain
     */
    public NumSet getDeltaSet() {
        if (ldom!=null)
            return ldom.getDeltaSet();
        else
            return fdom.getDeltaSet();
    }
    
    /**
     * Clears the delta set for this domain
     */
    public void clearDelta() {
        if (ldom!=null)
            ldom.clearDelta();
        else
            fdom.clearDelta();
    }
    
    /**
     * Returns string representation of domain
     */
    public String toString() {
        if (ldom!=null)
            return ldom.toString();
        else
            return fdom.toString();
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (ldom!=null)
        	((ChoicePointDataSource) ldom).setChoicePointStack(cps);
        else
        	((ChoicePointDataSource) fdom).setChoicePointStack(cps);
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public boolean choicePointStackSet() {
        if (ldom!=null)
        	return ((ChoicePointDataSource) ldom).choicePointStackSet();
       	return ((ChoicePointDataSource) fdom).choicePointStackSet();
    }

    /**
     * Attempts to reduce domain to a maximum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMax(double val) throws PropagationFailureException {
        if (ldom!=null) {
            if (val < Long.MAX_VALUE)
                ldom.setMax((long) val);
        }
        else {
            if (val < Float.MAX_VALUE)
                fdom.setMax((float) val);
        }
    }

    /**
     * Attempts to reduce domain to a minimum value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setMin(double val) throws PropagationFailureException {
        if (ldom!=null) {
            if (val > Long.MIN_VALUE)
                ldom.setMin((long) val);
        }
        else {
            if (val > -Float.MAX_VALUE)
                fdom.setMin((float) val);
        }
    }

    /**
     * Attempts to reduce domain to a single value.
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setValue(double val) throws PropagationFailureException {
        if (ldom!=null) {
            if (val < Long.MIN_VALUE || val > Long.MAX_VALUE)
                throw new PropagationFailureException();
            ldom.setValue((long) val);
        }
        else {
            if (val < -Float.MAX_VALUE || val > Float.MAX_VALUE)
                throw new PropagationFailureException();
            fdom.setValue((float) val);
        }
    }

    /**
     * Attempts to remove a single value from the domain
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeValue(double val) throws PropagationFailureException {
        if (ldom!=null) {
            if (val >= Long.MIN_VALUE && val <= Long.MAX_VALUE)
                ldom.removeValue((long) val);
        }
        else {
            if (val >= -Float.MAX_VALUE && val <= Float.MAX_VALUE)
                fdom.removeValue((float) val);
        }
    }

    /**
     * Attempts to reduce domain to within a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setRange(double start, double end) throws PropagationFailureException {
        if (ldom!=null) {
            long s = (long) start;
            if (start < Long.MIN_VALUE) s = Long.MIN_VALUE;
            else if (start > Long.MAX_VALUE) s = Long.MAX_VALUE;

            long e = (long) end;
            if (e < Long.MIN_VALUE) e = Long.MIN_VALUE;
            else if (e > Long.MAX_VALUE) e = Long.MAX_VALUE;

            ldom.setRange(s, e);
        }
        else {
            float s = (float) start;
            if (start < -Float.MAX_VALUE) s = -Float.MAX_VALUE;
            else if (start > Float.MAX_VALUE) s = Float.MAX_VALUE;

            float e = (float) end;
            if (e < -Float.MAX_VALUE) e = -Float.MAX_VALUE;
            else if (e > Float.MAX_VALUE) e = Float.MAX_VALUE;

            fdom.setRange(s, e);
        }
    }

    /**
     * Attempts to reduce domain by removing a range of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeRange(double start, double end) throws PropagationFailureException {
        if (ldom!=null) {
            long s = (long) start;
            if (start < Long.MIN_VALUE) s = Long.MIN_VALUE;
            else if (start > Long.MAX_VALUE) return;

            long e = (long) end;
            if (e < Long.MIN_VALUE) return;
            else if (e > Long.MAX_VALUE) e = Long.MAX_VALUE;

            ldom.removeRange(s, e);
        }
        else {
            float s = (float) start;
            if (start < -Float.MAX_VALUE) s = -Float.MAX_VALUE;
            else if (start > Float.MAX_VALUE) return;

            float e = (float) end;
            if (e < -Float.MAX_VALUE) return;
            else if (e > Float.MAX_VALUE) e = Float.MAX_VALUE;

            fdom.removeRange(s, e);
        }
    }

    /**
     * Attempts to reduce a domain by restricting it to a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void setDomain(NumSet s) throws PropagationFailureException {
        if (ldom!=null)
            ldom.setDomain(s);
        else
            fdom.setDomain(s);
    }

    /**
     * Attempts to reduce a domain by removing a set of values
     *
     * @throws PropagationFailureException      If domain is empty
     */
    public void removeDomain(NumSet s) throws PropagationFailureException {
        if (ldom!=null)
            ldom.removeDomain(s);
        else
            fdom.removeDomain(s);
    }

    /**
     * Adds a listener to this domain interested in domain, range and value
     * events
     */
    public void addDomainChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).addDomainChangeListener(listener);
        else
            ((DomainChangeSource) fdom).addDomainChangeListener(listener);
    }

    /**
     * Removes a domain listener from this domain
     */
    public void removeDomainChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).removeDomainChangeListener(listener);
        else
            ((DomainChangeSource) fdom).removeDomainChangeListener(listener);
    }

    /**
     * Adds a listener to this domain interested in range and value
     * events
     */
    public void addRangeChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).addRangeChangeListener(listener);
        else
            ((DomainChangeSource) fdom).addRangeChangeListener(listener);
    }

    /**
     * Removes a range listener from this domain
     */
    public void removeRangeChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).removeRangeChangeListener(listener);
        else
            ((DomainChangeSource) fdom).removeRangeChangeListener(listener);
    }

    /**
     * Adds a listener to this domain only interested in value events
     */
    public void addValueChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).addValueChangeListener(listener);
        else
            ((DomainChangeSource) fdom).addValueChangeListener(listener);
    }

    /**
     * Removes a value listener from this domain
     */
    public void removeValueChangeListener(DomainChangeListener listener) {
        if (ldom!=null)
            ((DomainChangeSource) ldom).removeValueChangeListener(listener);
        else
            ((DomainChangeSource) fdom).removeValueChangeListener(listener);
    }

    /**
     * Stores all necessary information for this domain allowing it to be restored
     * to a previous state at a later point in time.
     * 
     * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
     */
	public Object getDomainState() {
		if (ldom!=null) {
			return ldom.getDomainState();
		}
		else {
			return fdom.getDomainState();
		}
	}
	
	/**
     * Restores a domain to a previous state using the information contained in
     * the state parameter.
     * 
	 * @see jopt.csp.spi.arcalgorithm.domain.Domain#restoreDomainState(java.lang.Object)
	 */
	public void restoreDomainState(Object state) {
		if (ldom!=null) {
			ldom.restoreDomainState(state);
		}
		else {
			fdom.restoreDomainState(state);
		}
	}
	
	/**
     * Returns true if last operation caused a change to occur in  the domain
     */
	public boolean changed() {
		if (ldom!=null) {
			return ldom.changed();
		}
		else {
			return fdom.changed();
		}
	}
	
    public Object clone() {
        if (ldom!=null)
            return new DoubleDomainWrapper(ldom);
        else
            return new DoubleDomainWrapper(fdom);
    }
}