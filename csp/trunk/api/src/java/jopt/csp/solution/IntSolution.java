package jopt.csp.solution;

import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Solution to an integer variable
 */
public class IntSolution implements VariableSolution {
    private CspIntVariable var;
    private int min;
    private int max;
    
    /**
     * Creates solution for a variable
     */
    protected IntSolution(CspIntVariable var) {
        this.var = var;
        store();
    }

    // javadoc inherited from VariableSolution
    public CspVariable getVariable() {
        return var;
    }
    
    // javadoc inherited from VariableSolution
    public boolean isBound() {
    	return min == max;
    }
    
    /**
     * Retrieves minimum value of solution
     */
    public int getMin() {
    	return min;
    }

    /**
     * Retrieves maximum value of solution
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets minimum value of solution
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets maximum value of solution
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Sets both the min / max value of solution to a single value 
     */
    public void setValue(int val) {
        this.min = val;
        this.max = val;
    }

    /**
     * Returns the value of the solution.  This function will
     * throw an exception if the variable is not already bound. 
     */
    public int getValue() {
    	if (!isBound())
    		throw new RuntimeException("variable is not bound to a single value");
    	return min;
    }

    // javadoc inherited from VariableSolution
    public void store() {
    	this.min = var.getMin();
    	this.max = var.getMax();
    }
    
    // javadoc inherited from VariableSolution
    public void restore() throws PropagationFailureException {
    	var.setMin(min);
    	var.setMax(max);
    }
    
    // javadoc inherited from Object
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	
    	if (var.getName() == null)
    		buf.append("~");
    	else
    		buf.append(var.getName());
    	
    	buf.append(":");
    	buf.append("[");
    	
    	if (isBound()) {
    		buf.append(min);
    	}
    	else {
    		buf.append(min);
    		buf.append("...");
    		buf.append(max);
    	}
    	
    	buf.append("]");
    	
    	return buf.toString();
    }
    
    // javadoc inherited from Object
    public Object clone() {
    	IntSolution s = new IntSolution(var);
    	s.min = min;
    	s.max = max;
    	return s;
    }
    
    // javadoc inherited from Object
    public int hashCode() {
    	return var.getName().hashCode();
    }
    
    // javadoc inherited from Object
    public boolean equals(Object obj) {
    	if (!getClass().isInstance(obj)) return false;
    	IntSolution n = (IntSolution) obj;
    	
    	if (!n.var.equals(var)) return false;
    	
    	if (n.isBound() != isBound()) return false;
    	
    	if (n.min != min) return false;
    	if (n.max != max) return false;
    	
    	return true;
    }
}
