package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolEqTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolEqTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint that restricts a BoolExpr to be the opposite value of another BoolExp
 */
public class BooleanNotConstraint extends TwoVarConstraint {
    public BooleanNotConstraint(BoolExpr a, BoolExpr z) {
        super(a, z, false);
    }
    
    //  javadoc is inherited
    protected AbstractConstraint createConstraint(BoolExpr aexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        if (gc!=null)
            return null; // unsupported for BooleanNotConstraint
        else{
            AbstractConstraint constraint =  new BooleanNotConstraint(aexpr, zexpr);
            constraint.associateToGraph(graph);
            return constraint;
        }
    }
    
    //  javadoc is inherited
    protected AbstractConstraint createOpposite() {
        return new BooleanNotConstraint(aexpr, zexpr.notExpr());
    }
    
    //  javadoc is inherited
    protected Arc[] createGenericArcs() {
        // A != Z
        return new Arc[] {
                new GenericBoolEqTwoVarArc(aexpr.getNode(), false, zexpr.getNode(), true),
                new GenericBoolEqTwoVarArc(zexpr.getNode(), false, aexpr.getNode(), true)
        };
    }
    
    //  javadoc is inherited
    protected Arc[] createStandardArcs() {
        BooleanNode anode = (aexpr!=null) ? (BooleanNode)aexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        // A != Z
        return new Arc[] {
                new BinaryBoolEqTwoVarArc(anode, false, znode, true),
                new BinaryBoolEqTwoVarArc(znode, false, anode, true)
        };
    }
    
    //javadoc is inherited
    protected boolean violated() {
        return (isAnyTrue(currentZ, currentGz) && isAnyTrue(currentA, currentGa)) ||
        (isAnyFalse(currentZ, currentGz) && isAnyFalse(currentA, currentGa));
    }    
    
    //  javadoc is inherited
    public String toString() {
        StringBuffer buf = new StringBuffer("A[");
        buf.append(aexpr);
        buf.append("] != Z[");
        buf.append(zexpr);
        buf.append("]");
        return buf.toString();
    }
}
