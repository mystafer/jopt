package jopt.csp.spi.solver;

/**
 * An array of numbers that is reversible with the choicepoint stack.  The
 * stack must be set on the array for this functionality to work properly.
 * Note that classes extending ReversibleNumberArray use primitive arrays
 * to store the actual data (for efficiency purposes).
 * 
 * @author  Chris Johnson
 */
public abstract class ReversibleNumberArray implements ChoicePointEntryListener {
    private ChoicePointDataMap cpdata;
    
    /**
     * Adds the specified val to the value of the reversible array at a particular index
     */
    public abstract void adjValue(int idx, Number val);
    
    /**
     * Returns the value of the reversible array at a particular index
     */
    public abstract Number getValue(int idx);
    
    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
        restoreState();
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        storeState();
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        restoreState();
    }

    /**
     * Handles restoring state when choicepoint event occurs
     */
    protected abstract void restoreState();
    
    /**
     * Handles storing state when choicepoint event occurs
     */
    protected abstract void storeState();

    /**
     * Sets the choicepoint stack associated with this array.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cpdata!=null && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for reversible array");
        }

        if (cps==null) {
            if (cpdata!=null) cpdata.close();
            this.cpdata=null;
        }
        else
            this.cpdata = cps.newDataMap(this);
    }
    
    public abstract void clear();
}
