package jopt.csp.spi.solver;

import java.util.Arrays;

/**
 * Creates a double array that is reversible with the choicepoint stack.  The
 * stack must be set on the array for this functionality to work properly.
 * 
 * @author  Chris Johnson
 */
public class ReversibleDoubleArray extends ReversibleNumberArray {
    private ChoicePointDataMap cpdata;
    private double vals[];
    
    /**
     * Constructor
     */
    public ReversibleDoubleArray(int size) {
        vals = new double[size];
    }
    
    /**
     * Sets the value of the reversible array at a particular index
     */
    public void adjValue(int idx, Number val) {
    	this.vals[idx] += val.doubleValue();
    }
    
    /**
     * Returns the value of the reversible array at a particular index
     */
    public Number getValue(int idx) {
    	return new Double(vals[idx]);
    }
    
    protected void restoreState() {
    	if (cpdata!=null && cpdata.containsKey("v"))
    		this.vals = (double[]) cpdata.get("v");
    }
    
    protected void storeState() {
        if (cpdata!=null) cpdata.put("v", vals.clone());
    }
    
    public void clear() {
        Arrays.fill(vals, 0);
    }
}
