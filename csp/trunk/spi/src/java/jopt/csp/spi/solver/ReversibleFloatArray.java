package jopt.csp.spi.solver;

import java.util.Arrays;

/**
 * Creates a float array that is reversible with the choicepoint stack.  The
 * stack must be set on the array for this functionality to work properly.
 * 
 * @author  Chris Johnson
 */
public class ReversibleFloatArray extends ReversibleNumberArray {
    private ChoicePointDataMap cpdata;
    private float vals[];
    
    /**
     * Constructor
     */
    public ReversibleFloatArray(int size) {
        vals = new float[size];
    }
    
    /**
     * Sets the value of the reversible array at a particular index
     */
    public void adjValue(int idx, Number val) {
    	this.vals[idx] += val.floatValue();
    }
    
    /**
     * Returns the value of the reversible array at a particular index
     */
    public Number getValue(int idx) {
    	return new Float(vals[idx]);
    }
    
    protected void restoreState() {
    	if (cpdata!=null && cpdata.containsKey("v"))
    		this.vals = (float[]) cpdata.get("v");
    }
    
    protected void storeState() {
        if (cpdata!=null) cpdata.put("v", vals.clone());
    }

    public void clear() {
        Arrays.fill(vals, 0);
    }
}
