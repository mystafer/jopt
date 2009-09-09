package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.solver.VariableChangeListener;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.GenericIndexManager;
import jopt.csp.spi.util.NumberMath;

/**
 * Base class for numeric based constraints
 */

public abstract class NumConstraint extends AbstractConstraint implements VariableChangeSource {
    protected NumExpr zexpr;
    protected GenericNumExpr gzexpr;
    protected Number constVal;
    protected GenericNumConstant genConstVal;
    
    protected Number currentConstVal;
    protected GenericNumConstant currentGenConstVal;
    
    protected int nodeType;
    protected int constraintType;
    
    protected NumExpr currentZ;
    protected GenericNumExpr currentGz;
    
    protected GenericIndexManager gim;
    
    /**
     * Internal Constructor
     */
    protected NumConstraint(NumExpr zexpr, Number constVal, GenericNumConstant genConstVal, int constraintType) 
    {
        this.zexpr = zexpr;
        this.constVal = constVal;
        this.genConstVal = genConstVal;
        this.currentGenConstVal = genConstVal;
        this.currentConstVal = NumberMath.toConst(constVal);
        
        if (zexpr instanceof GenericNumExpr) 
            gzexpr = (GenericNumExpr) zexpr;
        
        // Determine node type
        if (zexpr!=null) this.nodeType = zexpr.getNumberType();
        if (constVal!=null) this.nodeType = Math.max(nodeType, NumberMath.numberType(constVal));
        if (genConstVal!=null) this.nodeType = genConstVal.getNumberType();
        
        this.constraintType = constraintType;
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
    
    /**
     * Retrieve a <code>GenericIndexManager</code> for this NumConstraint
     * 
     * @return an index manager taking into account the appropriate restrictions
     */
    protected abstract GenericIndexManager getIndexManager();
    
  /**
  * Returns array of arcs that will affect the boolean true / false
  * value of this constraint upon a change
  * @param	useConstraint		a boolean that determines if the constraint at that 
  * 								level should be added.  Constraints immediatly below a boolean
  * 								constraint should not be added, while all others below should.
  */
 public abstract Arc[] getBooleanSourceArcs(boolean useConstraint);
    
    /**
     * Determines arc type for arcs with same relation as constraint
     */
    protected int equivalentArcType() {
        return equivalentArcType(constraintType);
    }
    
    /**
     * Determines arc type for arcs with opposite relation as constraint
     */
    protected int oppositeArcType() {
        return oppositeArcType(constraintType);
    }
    
    /**
     * Returns opposite constraint type for current constraint
     */
    protected int oppositeConstraintType() {
        return oppositeConstraintType(constraintType);
    }
    
    /**
     * Determines opposite constraint type for a given type
     */
    protected static int oppositeConstraintType(int constraintType) {
        int arcType = NumArc.NEQ;
        
        switch (constraintType) {
            case NumConstraint.LT:
                arcType = NumArc.GEQ;
            break;
            
            case NumConstraint.LEQ:
                arcType = NumArc.GT;
            break;
            
            case NumConstraint.GT:
                arcType = NumArc.LEQ;
            break;
            
            case NumConstraint.GEQ:
                arcType = NumArc.LT;
            break;
            
            case NumConstraint.NEQ:
                arcType = NumArc.EQ;
            break;

        }
        
        return arcType;
    }
    
    /**
     * Determines arc type for arcs with same relation as constraint.
     * For example, if the constraint represents Z < A and the arc represents
     * Z < A, this method is used to retrieve the appropriate type.
     */
    protected static int oppositeArcType(int constraintType) {
        int arcType = NumArc.EQ;
        
        switch (constraintType) {
            case NumConstraint.LT:
                arcType = NumArc.LT;
            break;
            
            case NumConstraint.LEQ:
                arcType = NumArc.LEQ;
            break;
            
            case NumConstraint.GT:
                arcType = NumArc.GT;
            break;
            
            case NumConstraint.GEQ:
                arcType = NumArc.GEQ;
            break;
            
            case NumConstraint.NEQ:
                arcType = NumArc.NEQ;
        }
        
        return arcType;
    }
    
    /**
     * Determines arc type for arcs with opposite relation as constraint.
     * For example, if the constraint represents A < Z but the arc represents
     * Z > A, this method is used to retrieve the appropriate 'reverse' type.
     */
    protected static int equivalentArcType(int constraintType) {
        int arcType = NumArc.EQ;
        
        switch (constraintType) {
            case NumConstraint.LT:
                arcType = NumArc.GT;
            break;
            
            case NumConstraint.LEQ:
                arcType = NumArc.GEQ;
            break;
            
            case NumConstraint.GT:
                arcType = NumArc.LT;
            break;
            
            case NumConstraint.GEQ:
                arcType = NumArc.LEQ;
            break;
            
            case NumConstraint.NEQ:
                arcType = NumArc.NEQ;
        }
        
        return arcType;
    }
    
    /**
     * Returns min node Z
     */
    protected Number minZ() {
        if (zexpr!=null) return zexpr.getNumMin();
        if (currentConstVal != null) return currentConstVal;
        return currentGenConstVal.getNumMin();
    }
    
    /**
     * Returns max node Z
     */
    protected Number maxZ() {
        if (zexpr!=null) return zexpr.getNumMax();
        if (currentConstVal != null) return currentConstVal; 
        return currentGenConstVal.getNumMin();
    }
    
    public Number getSmallestMin(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMin();
        if (gn!=null) return gn.getNumSmallestMin();
        if (currentConstVal != null) return currentConstVal; 
        return currentGenConstVal.getNumMin();
        
    }
    
    public Number getLargestMin(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMin();
        if (gn!=null) return gn.getNumLargestMin();
        //Notice that we want the largest min, so if we are dealing with 
        //a generic const, we want the maxsince its as if it is a bound generic variable
        if (currentConstVal != null) return currentConstVal;
        return currentGenConstVal.getNumMax();
    }
    
    public Number getSmallestMax(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMax();
        if (gn!=null) return gn.getNumSmallestMax();
        //Notice that we want the smaillest max, so if we are dealing with 
        //a generic const, we want the min since its as if it is a bound generic variable
        if (currentConstVal != null) return currentConstVal; 
        return currentGenConstVal.getNumMin();
    }
    
    public Number getLargestMax(NumExpr n, GenericNumExpr gn) {
        if (n!=null) return n.getNumMax();
        if (gn!=null) return gn.getNumLargestMax();
        if (currentConstVal != null) return currentConstVal;
        return currentGenConstVal.getNumMax();
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        if (zexpr!=null)
            ((VariableChangeSource) zexpr).addVariableChangeListener(listener);
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        if (zexpr!=null)
            ((VariableChangeSource) zexpr).removeVariableChangeListener(listener);
    }
}