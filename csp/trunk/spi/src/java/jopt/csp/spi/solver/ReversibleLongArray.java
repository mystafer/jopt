package jopt.csp.spi.solver;

import java.util.Arrays;

/**
 * Creates a long array that is reversible with the choicepoint stack.  The
 * stack must be set on the array for this functionality to work properly.
 * 
 * @author  Chris Johnson
 */
public class ReversibleLongArray extends ReversibleNumberArray {
    private ChoicePointDataMap cpdata;
    private long vals[];
    
    /**
     * Constructor
     */
    public ReversibleLongArray(int size) {
        super();
        vals = new long[size];
    }
    
    /**
     * Sets the value of the reversible array at a particular index
     */
    public void adjValue(int idx, Number val) {
    	this.vals[idx] += val.longValue();
    }
    
    /**
     * Returns the value of the reversible array at a particular index
     */
    public Number getValue(int idx) {
    	return new Long(vals[idx]);
    }
    
    protected void restoreState() {
    	if (cpdata!=null && cpdata.containsKey("v"))
    		this.vals = (long[]) cpdata.get("v");
    }
    
    protected void storeState() {
        if (cpdata!=null) cpdata.put("v", vals.clone());
    }

    public void clear() {
        Arrays.fill(vals, 0);
    }
}
