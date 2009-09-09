package jopt.csp.spi.arcalgorithm.constraint.bool;

import java.util.Iterator;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolImpliesReflexTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolImpliesTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolImpliesReflexTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolImpliesTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.util.GenericIndexManager;

/**
 * Constraint representing A -> Z
 */
public class BooleanImpliesTwoVarConstraint extends TwoVarConstraint {
    public BooleanImpliesTwoVarConstraint(BoolExpr a, BoolExpr z) {
        super(a, z, false);
    }
    
    //  javadoc is inherited
    protected AbstractConstraint createConstraint(BoolExpr aexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        AbstractConstraint constraint = null;
        if (gc!=null)
            return null; // unsupported for BooleanImpliesTwoVarConstraint
        else
            constraint= new BooleanImpliesTwoVarConstraint(aexpr, zexpr);
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    /**
     * Returns a constraint that is the exact opposite of this constraint
     */
    protected AbstractConstraint createOpposite() {
        return new BooleanImpliesTwoVarConstraint(aexpr.notExpr(), zexpr);
    }
    
    //  javadoc is inherited
    protected Arc[] createGenericArcs() {
        // A -> Z
        return new Arc[] {
                new GenericBoolImpliesTwoVarArc(aexpr.getNode(), false, zexpr.getNode(), false),
                new GenericBoolImpliesReflexTwoVarArc(zexpr.getNode(), false, aexpr.getNode(), false)
        };
    }
    
    //  javadoc is inherited
    protected Arc[] createStandardArcs() {
        BooleanNode anode = (aexpr!=null) ? (BooleanNode)aexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        // A -> Z
        return new Arc[] {
                new BinaryBoolImpliesTwoVarArc(anode, false, znode, false),
                new BinaryBoolImpliesReflexTwoVarArc(znode, false, anode, false)
        };
    }
    
    //javadoc is inherited
    protected boolean violated() {
        return isAnyTrue(currentA, currentGa) && isAnyFalse(currentZ, currentGz);
    }
    
    /**
     * Returns true if constraint cannot be dissatisfied.
     * Note that implication cannot simply check its opposite because valid conditions such
     * as A[F]->Z[T] are not necessarily false in the opposite constraint A->!Z
     */
    public boolean isTrue() {
        GenericIndexManager gim = this.getIndexManager();
        Iterator gimIt = gim.allIterator();
        // null iterator means no need for looping
        if(gimIt == null) {
            return checkForSatisfaction(gim);
        }
        
        while(gimIt.hasNext()) {
            gimIt.next();
            if(!checkForSatisfaction(gim)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkForSatisfaction(GenericIndexManager idxMgr) {
        //retrieve next z to apply changes
        if (!idxMgr.zIndexIsRestricted()) {
            currentZ = null;
            currentGz = gzexpr;
            
            if (gzexpr==null) 
                currentZ = zexpr;
            else if (gzexpr!=null && !idxMgr.useCombinedZ()) 
                currentZ = gzexpr.getBoolExpressionForIndex();
        }
        
        // retrieve sources to apply changes
        if (!idxMgr.xIndexIsRestricted()) {
            currentA = null;
            currentGa = gaexpr;
            
            if (gaexpr==null)
                currentA = aexpr;
            else if (gaexpr!=null && !idxMgr.useCombinedX())
                currentA = gaexpr.getBoolExpressionForIndex();
        }
        
        return isAllFalse(currentA, currentGa) || isAllTrue(currentZ, currentGz);
    }
    
    //  javadoc is inherited
    public String toString() {
        StringBuffer buf = new StringBuffer("A[");
        buf.append(aexpr);
        buf.append("] -> ");
        buf.append("Z[");
        buf.append(zexpr);
        buf.append("]");
        
        return buf.toString();
    }
}
