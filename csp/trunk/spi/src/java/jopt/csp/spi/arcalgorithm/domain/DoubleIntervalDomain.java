package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointMultiDoubleArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.DoubleIntervalSet;
import jopt.csp.util.DoubleIntervalSetListener;
import jopt.csp.util.IntervalIterator;
import jopt.csp.util.IntervalSet;
import jopt.csp.util.NumSet;

/**
 * Double domain that stores data in interval objects
 */
public class DoubleIntervalDomain extends BaseDoubleDomain implements DoubleIntervalSetListener {
    private final static int CALLBACK_VALUES        = 0;
    private final static int CALLBACK_DELTA         = 1;
    
	private ChoicePointStack cps;
    private ChoicePointNumArraySet cpdata;
    private ChoicePointMultiDoubleArray cpValues;
    private ChoicePointMultiDoubleArray cpDeltaAdded;
    private ChoicePointMultiDoubleArray cpDeltaRemoved;
    
    /**
     * Constructor for cloning
     */
    private DoubleIntervalDomain(DoubleIntervalDomain domain) {
        super();
        
        this.nestedChild = domain.nestedChild;
        this.values.addAll(domain.values);
        this.delta.addAll(domain.delta);
        ((DoubleIntervalSet) values).setListener(this, CALLBACK_VALUES);
        ((DoubleIntervalSet) delta).setListener(this, CALLBACK_DELTA);
    }
    
    /**
     * Constructor
     */
    public DoubleIntervalDomain(double min, double max) {
        super();
        
        values.add(min, max);
        ((DoubleIntervalSet) values).setListener(this, CALLBACK_VALUES);
        ((DoubleIntervalSet) delta).setListener(this, CALLBACK_DELTA);

        // Make sure domain is not empty
        if (values.size()<=0)
            throw new RuntimeException("Domain is empty");
    }

    // javadoc inherited
    public void intervalAdded(int callback, double start, double end) {
        // addition of data can be safely ignored for values list
        if (callback == CALLBACK_DELTA) {
            deltaAddedRecord.add(start, end);
        }
    }
    
    // javadoc inherited
    public void intervalRemoved(int callback, double start, double end) {
        // removal of data can be safely ignored for delta list
        if (callback == CALLBACK_VALUES) {
            changeDetected = true;
            
            //if cpValues has not been set, we do not have to worry about it
            //since the choice point will not be utilized
            if (this.cpdata != null && !cpdata.isWorking()) {
	            int offset = cpValues.add(0, start);
	            cpValues.set(1, offset, end);
            }
            delta.add(start, end);
        }
    }
    
    // javadoc inherited
    protected NumSet createEmptySet() {
        return new DoubleIntervalSet();
    }
    
    public Object clone() {
        return new DoubleIntervalDomain(this);
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public final void setChoicePointStack(ChoicePointStack cps) {
    	if (this.cps==cps) return;
        if (this.cps!=null && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for domain");
        }
        
        // check if cps stack can be set or removed
        if (!this.nestedChild) {
	        this.cps = cps;
	
	        if (cps==null) {
	            if (cpdata!=null) cpdata.close();
	            this.cpdata=null;
	            this.cpValues = null;
	            this.cpDeltaAdded = null;
	            this.cpDeltaRemoved = null;
	        }
	        else {
	            this.cpdata = cps.newNumStackSet(this);
	            this.cpValues = cpdata.newMultiDoubleList(2);
	            this.cpDeltaAdded = cpdata.newMultiDoubleList(2);
	            this.cpDeltaRemoved = cpdata.newMultiDoubleList(2);
	        }
        }
    }
    
    /**
     * Returns true if a call to setChoicePointStack will fail
     */
    public final boolean choicePointStackSet() {
        if(this.cpdata==null)
            return false;
        return true;
    }

    // javadoc is inherited
    public final void beforeChoicePointPopEvent() {
        // restore values that were removed
        if (cpValues.size()>0) {
            for (int i=0; i<cpValues.size(); i++)
                values.add(cpValues.get(0, i), cpValues.get(1, i));
        }

        // restore delta information
        delta.removeAll(deltaAddedRecord);
        delta.addAll(deltaRemovedRecord);
        
        // reset delta cleared flag
        deltaCleared = false;
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
        deltaAddedRecord.clear();
        deltaRemovedRecord.clear();
        
        // restore values removed from delta
        if (cpDeltaRemoved.size()>0) {
            for (int i=0; i<cpDeltaRemoved.size(); i++)
                deltaRemovedRecord.add(cpDeltaRemoved.get(0, i), cpDeltaRemoved.get(1, i));
        }
        
        // remove values added to delta
        if (cpDeltaAdded.size()>0) {
            for (int i=0; i<cpDeltaAdded.size(); i++)
                deltaAddedRecord.add(cpDeltaAdded.get(0, i), cpDeltaAdded.get(1, i));
        }
        
        notifyChoicePointPop();
    }

    // javadoc is inherited
    public final void beforeChoicePointPushEvent() {
        // copy values from delta records to cps
        IntervalSet interSet = (IntervalSet) deltaAddedRecord;
        IntervalIterator interIter = interSet.intervals();
        while (interIter.hasNext()) {
            int offset = cpDeltaAdded.add(0, interIter.nextDouble());
            cpDeltaAdded.set(1, offset, interIter.endDouble());
        }
        
        interSet = (IntervalSet) deltaRemovedRecord;
        interIter = interSet.intervals();
        while (interIter.hasNext()) {
            int offset = cpDeltaRemoved.add(0, interIter.nextDouble());
            cpDeltaRemoved.set(1, offset, interIter.endDouble());
        }
    }

    // javadoc is inherited
    public final void afterChoicePointPushEvent() {
        // restore values that were removed
        if (cpValues.size()>0) {
            for (int i=0; i<cpValues.size(); i++)
                values.remove(cpValues.get(0,i), cpValues.get(1,i));
        }
        
        // copy values from cps to delta
        deltaAddedRecord.clear();
        deltaRemovedRecord.clear();
        
        // restore values removed from delta
        if (cpDeltaRemoved.size()>0) {
            for (int i=0; i<cpDeltaRemoved.size(); i++)
                delta.remove(cpDeltaRemoved.get(0,i), cpDeltaRemoved.get(1,i));
        }
        
        // restore values added to delta
        if (cpDeltaAdded.size()>0) {
            for (int i=0; i<cpDeltaAdded.size(); i++)
                delta.add(cpDeltaAdded.get(0,i), cpDeltaAdded.get(1,i));
        }
        
        // reset delta cleared flag
        deltaCleared = false;
        
        notifyChoicePointPush();
    }
}
