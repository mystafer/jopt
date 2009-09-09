package jopt.csp.solution;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Solution to a set variable
 */
public class SetSolution implements VariableSolution {
	private CspSetVariable var;
    private Set possibleSet;
    private Set requiredSet;
    
    /**
     * Initializes internal solution information for variable
     * 
     * @param var   Variable to store solution information about
     */
    public SetSolution(CspSetVariable var) {
    	this.var = var;
        store();
    }
    
    // javadoc inherited from VariableSolution
    public CspVariable getVariable() {
        return var;
    }
    
    // javadoc inherited from VariableSolution
    public boolean isBound() {
    	return possibleSet.equals(requiredSet);
    }
    
    /**
     * Returns the set of possible values for the variable
     */
    public Set getPossibleSet() {
        return possibleSet;
    }
    
    /**
     * Returns the set of required values for the variable
     */
    public Set getRequiredSet() {
        return requiredSet;
    }
    
    /**
     * Sets the set of possible values for the variable
     */
    public void setPossibleSet(Set possibleSet) {
        this.possibleSet = possibleSet;
    }
    
    /**
     * Set the set of required values for the variable
     */
    public void setRequiredSet(Set requiredSet) {
        this.requiredSet = requiredSet;
    }

    // javadoc inherited from VariableSolution
    public void store() {
        this.possibleSet = Collections.unmodifiableSet(new HashSet(var.getPossibleSet()));
        this.requiredSet = Collections.unmodifiableSet(new HashSet(var.getRequiredSet()));
    }

    // javadoc inherited from VariableSolution
    public void restore() throws PropagationFailureException {
        // add all required values from solution
        var.addRequired(requiredSet);
        
        // remove all values from variable that are not possible
        // in solution
        HashSet removeVals = new HashSet(var.getPossibleSet());
        removeVals.removeAll(possibleSet);
        var.removePossible(removeVals);
    }

    // javadoc inherited from Object
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        if (var.getName() == null)
            buf.append("~");
        else
            buf.append(var.getName());
        
        buf.append(":");
        buf.append("[Required:");
        buf.append(requiredSet);
        buf.append(",Possible:");
        buf.append(possibleSet);
        buf.append("]");
        
        return buf.toString();
    }

    // javadoc inherited from Object
    public int hashCode() {
        return var.getName().hashCode();
    }

    // javadoc inherited from Object
    public boolean equals(Object obj) {
        if (!(obj instanceof SetSolution)) return false;
        SetSolution s = (SetSolution) obj;
        
        if (!s.var.equals(var)) return false;
        
        if (s.isBound() != isBound()) return false;
        
        if (!s.possibleSet.equals(possibleSet)) return false;
        if (!s.requiredSet.equals(requiredSet)) return false;
        
        return true;
    }
    
    // javadoc inherited from Object
    public Object clone() {
        SetSolution s = new SetSolution(var);
        s.possibleSet = new HashSet(possibleSet);
        s.requiredSet = new HashSet(requiredSet);
        return s;
    }
}
