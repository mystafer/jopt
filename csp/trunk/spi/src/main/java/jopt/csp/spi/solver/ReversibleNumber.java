/*
 * ReversibleNumber.java
 * 
 * Created on Jul 6, 2005
 */
package jopt.csp.spi.solver;

/**
 * Creates a number that is reversible with the choicepoint stack.  The
 * stack must be set on the number for this to function properly.
 * 
 * @author  Nick Coleman
 */
public class ReversibleNumber implements ChoicePointEntryListener {
    private ChoicePointDataMap cpdata;
    private Number val;
    
    /**
     * Sets the value of the reversible number
     */
    public void setValue(Number val) {
    	this.val = val;
    }
    
    /**
     * Returns the value of the reversible number
     */
    public Number getValue() {
    	return val;
    }
    
    // javadoc is inherited
    public void beforeChoicePointPopEvent() {
        restoreState();
    }

    // javadoc is inherited
    public void afterChoicePointPopEvent() {
    }

    // javadoc is inherited
    public void beforeChoicePointPushEvent() {
        if (cpdata!=null) cpdata.put("v", val);
    }

    // javadoc is inherited
    public void afterChoicePointPushEvent() {
        restoreState();
    }

    /**
     * Handles restoring state when choicepoint event occurs
     */
    private void restoreState() {
    	if (cpdata!=null && cpdata.containsKey("v"))
    		this.val = (Number) cpdata.get("v");
    }

    /**
     * Sets the choicepoint stack associated with this number.  Can only
     * be set once
     */
    public void setChoicePointStack(ChoicePointStack cps) {
        if (this.cpdata!=null && cps!=null) {
            throw new IllegalStateException("Choice point stack already set for number");
        }

        if (cps==null) {
            if (cpdata!=null) cpdata.close();
            this.cpdata=null;
        }
        else
            this.cpdata = cps.newDataMap(this);
    }
    
}
