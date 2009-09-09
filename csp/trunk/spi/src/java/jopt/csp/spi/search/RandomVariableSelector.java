package jopt.csp.spi.search;

import java.util.ArrayList;
import java.util.Random;

import jopt.csp.search.VariableSelector;
import jopt.csp.variable.CspVariable;

/**
 * Implementation of a random variable selector
 * 
 * @author kformsma
 */

public class RandomVariableSelector implements VariableSelector {

    private ArrayList vars;
    private Random rand;
    
    //javadoc inherited
    public boolean hasNext() {
        if(vars!=null && !vars.isEmpty())
            return true;
        else
            return false;
    }

    //javadoc inherited
    public CspVariable next() {
        int num = rand.nextInt(vars.size());
        return (CspVariable) vars.remove(num);
    }

    //javadoc inherited
    public void reset() {
        vars = null;
        rand = null;
    }

    //javadoc inherited
    public void setVariables(CspVariable[] vars) {
        this.vars = new ArrayList();
        for(int i=0;i<vars.length;i++) {
            this.vars.add(vars[i]);
        }
        rand = new Random();
    }
}
