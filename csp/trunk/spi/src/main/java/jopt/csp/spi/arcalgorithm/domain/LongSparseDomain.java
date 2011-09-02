package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointLongArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.LongSparseSet;
import jopt.csp.util.LongSparseSetListener;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;

/**
 * Long domain that stores data as individual values
 */
public class LongSparseDomain extends BaseLongDomain implements LongSparseSetListener {
    private final static int CALLBACK_VALUES        = 0;
    private final static int CALLBACK_DELTA         = 1;
    
	private ChoicePointStack cps;
    private ChoicePointNumArraySet cpdata;
    private ChoicePointLongArray cpValues;
    private ChoicePointLongArray cpDeltaAdded;
    private ChoicePointLongArray cpDeltaRemoved;
    
    /**
     * Constructor for cloning
     */
    private LongSparseDomain(LongSparseDomain domain) {
        super();
        
        this.nestedChild = domain.nestedChild;
        this.values.addAll(domain.values);
        this.delta.addAll(domain.delta);
        ((LongSparseSet) values).setListener(this, CALLBACK_VALUES);
        ((LongSparseSet) delta).setListener(this, CALLBACK_DELTA);
    }
    
    /**
     * Constructor
     */
    public LongSparseDomain(long min, long max) {
        super();
        
        values.add(min, max);
        ((LongSparseSet) values).setListener(this, CALLBACK_VALUES);
        ((LongSparseSet) delta).setListener(this, CALLBACK_DELTA);

        // Make sure domain is not empty
        if (values.size()<=0)
            throw new RuntimeException("Domain is empty");
    }

    
    // javadoc inherited
    public void valueAdded(int callback, long val) {
        // addition of data can be safely ignored for values list
        if (callback == CALLBACK_DELTA) {
            cpDeltaAdded.add(val);
        }
    }
    
    // javadoc inherited
    public void valueRemoved(int callback, long val) {
        // removal of data can be safely ignored for delta list
        if (callback == CALLBACK_VALUES) {
            changeDetected = true;
            
            //if cpValues has not been set, we do not have to worry about it
            //since the choice point will not be utilized
            if (this.cpdata != null && !cpdata.isWorking()) {
            	cpValues.add(val);
            }
            delta.add(val);
        }
    }
    
    // javadoc inherited
    protected NumSet createEmptySet() {
        return new LongSparseSet();
    }
    
    public Object clone() {
        return new LongSparseDomain(this);
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
	            this.cpValues = cpdata.newLongList();
	            this.cpDeltaAdded = cpdata.newLongList();
	            this.cpDeltaRemoved = cpdata.newLongList();
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
                values.add(cpValues.get(i));
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
                deltaRemovedRecord.add(cpDeltaRemoved.get(i));
        }
        
        // remove values added to delta
        if (cpDeltaAdded.size()>0) {
            for (int i=0; i<cpDeltaAdded.size(); i++)
                deltaAddedRecord.add(cpDeltaAdded.get(i));
        }
        
        notifyChoicePointPop();
    }

    // javadoc is inherited
    public final void beforeChoicePointPushEvent() {
        // copy values from delta records to cps
        NumberIterator numIter = deltaAddedRecord.values();
        while (numIter.hasNext()) {
            numIter.next();
            cpDeltaAdded.add(numIter.longValue());
        }
        
        numIter = deltaRemovedRecord.values();
        while (numIter.hasNext()) {
            numIter.next();
            cpDeltaRemoved.add(numIter.longValue());
        }
    }

    // javadoc is inherited
    public final void afterChoicePointPushEvent() {
        // ensure all values have been removed
        if (cpValues.size()>0) {
            for (int i=0; i<cpValues.size(); i++)
                values.remove(cpValues.get(i));
        }
        
        // copy values from cps to delta
        deltaAddedRecord.clear();
        deltaRemovedRecord.clear();
        
        // restore values removed from delta
        if (cpDeltaRemoved.size()>0) {
            for (int i=0; i<cpDeltaRemoved.size(); i++)
                delta.remove(cpDeltaRemoved.get(i));
        }
        
        // remove values added to delta
        if (cpDeltaAdded.size()>0) {
            for (int i=0; i<cpDeltaAdded.size(); i++)
                delta.add(cpDeltaAdded.get(i));
        }
        
        // reset delta cleared flag
        deltaCleared = false;
        
        notifyChoicePointPush();
    }
}