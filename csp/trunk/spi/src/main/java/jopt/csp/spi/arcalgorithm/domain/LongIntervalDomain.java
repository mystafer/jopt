package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointMultiLongArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntervalIterator;
import jopt.csp.util.IntervalSet;
import jopt.csp.util.LongIntervalSet;
import jopt.csp.util.LongIntervalSetListener;
import jopt.csp.util.NumSet;

/**
 * Long domain that stores data in interval objects
 */
public class LongIntervalDomain extends BaseLongDomain implements LongIntervalSetListener {
    private final static int CALLBACK_VALUES        = 0;
    private final static int CALLBACK_DELTA         = 1;
    
	private ChoicePointStack cps;
    private ChoicePointNumArraySet cpdata;
    private ChoicePointMultiLongArray cpValues;
    private ChoicePointMultiLongArray cpDeltaAdded;
    private ChoicePointMultiLongArray cpDeltaRemoved;
    
    /**
     * Constructor for cloning
     */
    private LongIntervalDomain(LongIntervalDomain domain) {
        super();
        
        this.nestedChild = domain.nestedChild;
        this.values.addAll(domain.values);
        this.delta.addAll(domain.delta);
        ((LongIntervalSet) values).setListener(this, CALLBACK_VALUES);
        ((LongIntervalSet) delta).setListener(this, CALLBACK_DELTA);
    }
    
    /**
     * Constructor
     */
    public LongIntervalDomain(long min, long max) {
        super();
        
        values.add(min, max);
        ((LongIntervalSet) values).setListener(this, CALLBACK_VALUES);
        ((LongIntervalSet) delta).setListener(this, CALLBACK_DELTA);

        // Make sure domain is not empty
        if (values.size()<=0)
            throw new RuntimeException("Domain is empty");
    }

    // javadoc inherited
    public void intervalAdded(int callback, long start, long end) {
        // addition of data can be safely ignored for values list
        if (callback == CALLBACK_DELTA) {
            deltaAddedRecord.add(start, end);
        }
    }
    
    // javadoc inherited
    public void intervalRemoved(int callback, long start, long end) {
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
        return new LongIntervalSet();
    }
    
    public Object clone() {
        return new LongIntervalDomain(this);
    }

    /**
     * Sets the choicepoint stack associated with this domain.  Can only
     * be set once
     */
    public final void setChoicePointStack(ChoicePointStack cps) {
    	if (this.cps == cps) return;
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
	            this.cpValues = cpdata.newMultiLongList(2);
	            this.cpDeltaAdded = cpdata.newMultiLongList(2);
	            this.cpDeltaRemoved = cpdata.newMultiLongList(2);
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
            int offset = cpDeltaAdded.add(0, interIter.nextLong());
            cpDeltaAdded.set(1, offset, interIter.endLong());
        }
        
        interSet = (IntervalSet) deltaRemovedRecord;
        interIter = interSet.intervals();
        while (interIter.hasNext()) {
            int offset = cpDeltaRemoved.add(0, interIter.nextLong());
            cpDeltaRemoved.set(1, offset, interIter.endLong());
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
