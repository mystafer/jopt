package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.GenericIndexManager;

/**
 * Base class for boolean constraints
 */
public abstract class BooleanConstraint extends AbstractConstraint implements VariableChangeSource {
    protected BoolExpr zexpr;
    protected GenericBoolExpr gzexpr;
    protected boolean constVal;
    protected GenericBooleanConstant genConstVal;
    
    protected Boolean currentConstVal;
    protected GenericBooleanConstant currentGenConstVal;
    
    protected BooleanNode boolSourceNodes[];
    protected Arc[] arcs;
    
    protected BoolExpr currentZ;
    protected GenericBoolExpr currentGz;
    
    protected GenericIndexManager gim;
    
    
    /**
     * Constructor taking in both a boolean expression and a constant value.
     * @param zexpr		-	the target node of this constraint
     * @param constVal	-	the constant value of the of the target of this boolean constraint
     */
    public BooleanConstraint(BoolExpr zexpr, boolean constVal) {
        this.zexpr = zexpr;
        this.constVal = constVal;
        this.currentConstVal = new Boolean(constVal);
        
        if (zexpr instanceof GenericBoolExpr) 
            gzexpr = (GenericBoolExpr) zexpr;
    }
    
    /**
     * Constructor taking in both a boolean expression and a constant value.
     * @param zexpr		-	the target node of this constraint
     * @param genConstVal	-	the generic constant value of the target of this boolean constraint
     */
    public BooleanConstraint(BoolExpr zexpr, GenericBooleanConstant genConstVal) {
        this.zexpr = zexpr;
        this.genConstVal = genConstVal;
        
        if (zexpr instanceof GenericBoolExpr) 
            gzexpr = (GenericBoolExpr) zexpr;
    }
    
    
    /**
     * Returns true if constraint cannot be satisfied.
     * If all violated is true, this method returns true only when all conditions are violated.
     * If all violated is false, this method returns true when any condition is violated.
     * The presence of multiple conditions is an issue when generic vars/exprs are involved.
     * 
     * @param allViolated Determines the criteria for violation
     */
    public abstract boolean isViolated(boolean allViolated);
    
    //  javadoc is inherited
    protected abstract AbstractConstraint createConstraintFragment(GenericIndex indices[]) ;
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        addVariableChangeListener(listener,false);
    }
    
    /**
     * Adds a listener interested in variable change events
     */
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        if (zexpr!=null){
            if (zexpr instanceof BooleanExpr) {
                ((BooleanExpr) zexpr).addVariableChangeListener(listener,firstTime);
            }
            else{
                ((VariableChangeSource) zexpr).addVariableChangeListener(listener);
            }
        }
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        if (zexpr!=null)
            ((VariableChangeSource) zexpr).removeVariableChangeListener(listener);
    }
    
    /**
     * Returns true if any expr is false
     */
    protected boolean isAnyFalse(BoolExpr b, GenericBoolExpr gb) {
        if (b!=null) return b.isFalse();
        if (gb!=null) return gb.isAnyFalse();
        if (currentConstVal!=null) return !currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyFalse();
        
    }
    
    /**
     * Returns true if any expr is true
     */
    protected boolean isAnyTrue(BoolExpr b, GenericBoolExpr gb) {
        if (b!=null) return b.isTrue();
        if (gb!=null) return gb.isAnyTrue();
        if (currentConstVal!=null) return currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyTrue();
        
    }
    
    /**
     * Returns true if all exprs are false
     */
    protected boolean isAllFalse(BoolExpr b, GenericBoolExpr gb) {
        if (b!=null) return b.isFalse();
        if (gb!=null) return gb.isAllFalse();
        return !constVal;
    }
    
    /**
     * Returns true if all exprs are true
     */
    protected boolean isAllTrue(BoolExpr b, GenericBoolExpr gb) {
        if (b!=null) return b.isTrue();
        if (gb!=null) return gb.isAllTrue();
        return constVal;
    }
    
    /**
     * Returns true if any Z node is false
     */
    protected boolean isAnyZFalse() {
        if (gzexpr != null) return gzexpr.isAnyFalse();
        if (zexpr != null) return zexpr.isFalse();
        if (currentConstVal!=null) return !currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyFalse();
    }
    
    /**
     * Returns true if any Z node is true
     */
    protected boolean isAnyZTrue() {
        if (gzexpr != null) return gzexpr.isAnyTrue();
        if (zexpr != null) return zexpr.isTrue();
        if (currentConstVal!=null) return currentConstVal.booleanValue(); 
        return currentGenConstVal.isAnyTrue();
    }
}
