package jopt.csp.solution;

import jopt.csp.variable.CspBooleanVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Solution to a boolean variable
 */
public class BooleanSolution implements VariableSolution {
	private CspBooleanVariable var;
    private boolean isTrue;
    private boolean isFalse;
    
    /**
     * Initializes internal solution information for variable
     * 
     * @param var   Variable to store solution information about
     */
    public BooleanSolution(CspBooleanVariable var) {
    	this.var = var;
        store();
    }
    
    // javadoc inherited from VariableSolution
    public CspVariable getVariable() {
        return var;
    }
    
    // javadoc inherited from VariableSolution
    public boolean isBound() {
    	return isTrue != isFalse;
    }
    
    /**
     * Returns true if boolean variable is NOT satisfied in solution
     */
    public boolean isFalse() {
    	return isFalse;
    }
    
    /**
     * Returns true if boolean variable is satisfied in solution
     */
    public boolean isTrue() {
    	return isTrue;
    }

    /**
     * Sets the value of this variable to true
     */
    public void setTrue() {
    	isTrue = true;
        isFalse = false;
    }
    
    /**
     * Sets the value of this variable to false
     */
    public void setFalse() {
        isFalse = true;
        isTrue = false;
    }
    
    /**
     * Clears the isTrue / isFalse flag and sets variable to
     * an unbound state
     */
    public void clear() {
        isFalse = false;
        isTrue = false;
    }

    // javadoc inherited from VariableSolution
    public void store() {
        this.isTrue = var.isTrue();
        this.isFalse = var.isFalse();
    }

    // javadoc inherited from VariableSolution
    public void restore() throws PropagationFailureException {
        if (isTrue) var.setTrue();
        else if (isFalse) var.setFalse();
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
        
        if (isTrue)
            buf.append("true");
        else if (isFalse)
            buf.append("false");
        else
            buf.append("undefined");
        
        buf.append("]");
        
        return buf.toString();
    }

    // javadoc inherited from Object
    public Object clone() {
    	BooleanSolution s = new BooleanSolution(var);
        s.isTrue = isTrue;
        s.isFalse = isFalse;
        return s;
    }
    
    // javadoc inherited from Object
    public int hashCode() {
    	return var.getName().hashCode();
    }
    
    // javadoc inherited from Object
    public boolean equals(Object obj) {
    	if (!(obj instanceof BooleanSolution)) return false;
        BooleanSolution b = (BooleanSolution) obj;
        
        if (!b.var.equals(var)) return false;
        
        return b.isTrue == isTrue && b.isFalse == isFalse();
    }
}

