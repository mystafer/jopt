package jopt.csp.spi.arcalgorithm.domain;

import jopt.csp.spi.solver.ChoicePointDoubleArray;
import jopt.csp.spi.solver.ChoicePointNumArraySet;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.DoubleSparseSet;
import jopt.csp.util.DoubleSparseSetListener;
import jopt.csp.util.NumSet;
import jopt.csp.util.NumberIterator;

/**
 * Double domain that stores data as individual values
 */
public class DoubleSparseDomain extends BaseDoubleDomain implements DoubleSparseSetListener {
    private final static int CALLBACK_VALUES        = 0;
    private final static int CALLBACK_DELTA         = 1;

    
	private ChoicePointStack cps;
    private ChoicePointNumArraySet cpdata;
    private ChoicePointDoubleArray cpValues;
    private ChoicePointDoubleArray cpDeltaAdded;
    private ChoicePointDoubleArray cpDeltaRemoved;
    
    /**
     * Constructor for cloning
     */
    private DoubleSparseDomain(DoubleSparseDomain domain) {
        super();
        
        this.nestedChild = domain.nestedChild;
        this.values.addAll(domain.values);
        this.delta.addAll(domain.delta);
        ((DoubleSparseSet) values).setListener(this, CALLBACK_VALUES);
        ((DoubleSparseSet) delta).setListener(this, CALLBACK_DELTA);
    }
    
    /**
     * Constructor
     */
    public DoubleSparseDomain(int min, int max) {
        super();
        
        values.add(min, max);
        ((DoubleSparseSet) values).setListener(this, CALLBACK_VALUES);
        ((DoubleSparseSet) delta).setListener(this, CALLBACK_DELTA);

        // Make sure domain is not empty
        if (values.size()<=0)
            throw new RuntimeException("Domain is empty");
    }

    // javadoc inherited
    public void valueAdded(int callback, double val) {
        // addition of data can be safely ignored for values list
        if (callback == CALLBACK_DELTA) {
            cpDeltaAdded.add(val);
        }
    }
    
    // javadoc inherited
    public void valueRemoved(int callback, double val) {
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
        return new DoubleSparseSet();
    }
    
    public Object clone() {
        return new DoubleSparseDomain(this);
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
	            this.cpValues = cpdata.newDoubleList();
	            this.cpDeltaAdded = cpdata.newDoubleList();
	            this.cpDeltaRemoved = cpdata.newDoubleList();
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
            cpDeltaAdded.add(numIter.doubleValue());
        }
        
        numIter = deltaRemovedRecord.values();
        while (numIter.hasNext()) {
            numIter.next();
            cpDeltaRemoved.add(numIter.doubleValue());
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
