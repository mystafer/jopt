package jopt.csp.spi.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import jopt.csp.search.VariableSelector;
import jopt.csp.variable.CspVariable;

/**
 * Implementation of a variable selector which selects the variable
 * with the smallest domain first.
 * @author kformsma
 *
 */
public class MinDomainVariableSelector implements VariableSelector {
    private LinkedList<CspVariable> variables;
    
    //Javadoc inherited
    public boolean hasNext() {
        return (variables != null && !variables.isEmpty());
    }
    
    //javadoc inherited
    public CspVariable next() {
        return (CspVariable) variables.remove();
    }
    
    //javadoc inherited
    public void reset() {
        variables = null;
    }

    //javadoc inherited
    public void setVariables(CspVariable[] vars) {
        variables = new LinkedList<CspVariable>();
        //Sort the variables by lowest domain first
        Arrays.sort(vars,new VariableCompare());
        for(int i=0;i<vars.length;i++) {
            variables.add(vars[i]);
        }
    }

    /**
     * Private helper class to compare two variables based on domain size
     * @author kformsma
     *
     */
    private class VariableCompare implements Comparator<CspVariable>{

        public int compare(CspVariable arg0, CspVariable arg1) {
            CspVariable a = (CspVariable) arg0;
            CspVariable b = (CspVariable) arg1;
            return (a.getSize() - b.getSize());
        }
        
    }

}
