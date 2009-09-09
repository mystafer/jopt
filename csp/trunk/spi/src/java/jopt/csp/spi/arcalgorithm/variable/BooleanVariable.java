/*
 * Created on May 10, 2005
 */
package jopt.csp.spi.arcalgorithm.variable;

import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.domain.BooleanDomain;
import jopt.csp.spi.arcalgorithm.domain.BooleanIntDomain;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspBooleanVariable;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;

/**
 * A class used to create a concrete boolean variable with a domain that can be altered.
 * A basic variable with an internal domain can be created or a variable can be
 * created based on a boolean expression or constraint.
 * 
 * @author jboerkoel
 * @author Nick Coleman
 */
public class BooleanVariable extends BooleanExpr implements CspBooleanVariable, Variable {
    
    
    /**
     * Constructor for standard variable
     */
    public BooleanVariable(String name) {
        super(name, new BooleanIntDomain()); 
    }
    
    /**
     * Constructor for variable with predefined domain
     */
    public BooleanVariable(String name, BooleanDomain dom) {
        super(name, dom); 
    }
    
    /**
     * Constructor for variable that wraps a constraint
     */
    public BooleanVariable(String name, CspConstraint constraint) {
        super(name, new BooleanIntDomain(), constraint);
    }
    
    /**
     * Constructor to convert an expression to a variable
     */
    public BooleanVariable(String name, CspBooleanExpr expr) {
        this(name);

        BooleanExpr wrappedExpr = (BooleanExpr) expr;
        if(wrappedExpr.calculated) {
            this.wrappedConstraint = wrappedExpr.createExternalConstraint();
            this.useBuildingBlockConstraint = true;
        }
        else {
            this.constraint = new BooleanEqTwoVarConstraint(this, wrappedExpr);
            //bexpr is set here because this will cause createBuildingBlockConstraint to
            //generate the correct constraint.  Also, it can be though of as 
            //the same situation as true = bexpr and false = bexpr since variable domains are set.
            this.bexpr = wrappedExpr;
            this.useBuildingBlockConstraint = true;
        }
    }
    
    /**
     * Stores appropriate data for future restoration.
     */
    public Object getState() {
        return domain.getDomainState();
    }
    
    /**
     *  Restores variable information from stored data.
     */
    public void restoreState(Object state) {
        domain.restoreDomainState(state);
    }

    // javadoc inherited
    public void setTrue() throws PropagationFailureException{
        getBooleanDomain().setTrue();
        fireChangeEvent();  
    }
    
    // javadoc inherited
    public void setFalse() throws PropagationFailureException{
        getBooleanDomain().setFalse();
        fireChangeEvent();
    }
}
