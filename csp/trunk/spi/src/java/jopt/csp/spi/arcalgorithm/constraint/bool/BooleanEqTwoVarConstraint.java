package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.graph.NodeArcGraph;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolEqTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolEqTwoVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericConstraint2BoolArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.solver.VariableChangeListener;

/**
 * Constraint that represents X == Y where Y is either a constraint or boolean expression
 */
public class BooleanEqTwoVarConstraint extends TwoVarConstraint {
    protected GraphConstraint booleanConstraint;
    
    public BooleanEqTwoVarConstraint(BoolExpr zexpr, GraphConstraint booleanConstraint) {
        super(null, zexpr, false);
        this.booleanConstraint = booleanConstraint;
    }
    
    public BooleanEqTwoVarConstraint(BoolExpr aexpr, BoolExpr zexpr) {
        super(aexpr, zexpr, false);
    }
    
    //  javadoc is inherited
    public void associateToGraph(NodeArcGraph graph) {
        super.associateToGraph(graph);
        if(booleanConstraint != null) {
            booleanConstraint.associateToGraph(graph);
        }
    }
    
    //  javadoc is inherited
    protected AbstractConstraint createConstraint(BoolExpr aexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        AbstractConstraint constraint = null;
        if (gc!=null)
            return null; // unsupported for BooleanEqTwoVarConstraint
        else
            constraint= new BooleanEqTwoVarConstraint(aexpr, zexpr); 
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    //  javadoc is inherited
    protected AbstractConstraint createOpposite() {
        if (booleanConstraint!=null)
            return new BooleanEqTwoVarConstraint(zexpr, (GraphConstraint) booleanConstraint.getPostableOpposite());
        else
            return new BooleanEqTwoVarConstraint(zexpr, aexpr.notExpr());
    }
    
    //  javadoc is inherited
    protected Arc[] createGenericArcs() {
        if(booleanConstraint!=null) {
            return new Arc[]{};
        }
        return new Arc[] {
                new GenericBoolEqTwoVarArc(zexpr.getNode(), false, aexpr.getNode(), false),
                new GenericBoolEqTwoVarArc(aexpr.getNode(), false, zexpr.getNode(), false),
        };
    }
    
    //  javadoc is inherited
    protected Arc[] createStandardArcs() {
        BooleanNode anode = (aexpr!=null) ? (BooleanNode)aexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        if (booleanConstraint!=null) {
            return new Arc[] {
                    new GenericConstraint2BoolArc(booleanConstraint, znode),
                    new GenericConstraint2BoolArc(znode, booleanConstraint)
            };
        }
        else  {
            return new Arc[] {
                    new BinaryBoolEqTwoVarArc(znode, false, anode, false),
                    new BinaryBoolEqTwoVarArc(anode, false, znode, false),
            };
        }
    }
    
    // javadoc is inherited
    protected boolean violated() {
        if (booleanConstraint!=null) {
            return ((isAnyFalse(currentZ, currentGz) && booleanConstraint.isTrue()) ||
                    (isAnyTrue(currentZ, currentGz) && booleanConstraint.isFalse()));
        }
        else {
            return ((isAnyFalse(currentZ, currentGz) && isAnyTrue(currentA, currentGa)) ||
                    (isAnyTrue(currentZ, currentGz) && isAnyFalse(currentA, currentGa)));
        }
    }
    
    //  javadoc is inherited
    public String toString() {
        if (booleanConstraint!=null) {
            StringBuffer buf = new StringBuffer("constraint[");
            buf.append(booleanConstraint);
            buf.append("] = Z[");
            buf.append(zexpr);
            buf.append("]");
            return buf.toString();
        }
        else {
            StringBuffer buf = new StringBuffer("A[");
            buf.append(aexpr);
            buf.append("] = Z[");
            buf.append(zexpr);
            buf.append("]");
            return buf.toString();
        }
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener, boolean firstTime) {
        super.addVariableChangeListener(listener, firstTime);
        if (booleanConstraint!=null)
            ((AbstractConstraint)booleanConstraint).addVariableChangeListener(listener);
    }
    
    //  javadoc is inherited
    public void addVariableChangeListener(VariableChangeListener listener) {
        addVariableChangeListener(listener, true);
    }
    
    //  javadoc is inherited
    public void removeVariableChangeListener(VariableChangeListener listener) {
        super.removeVariableChangeListener(listener);
        if (booleanConstraint!=null)
            ((AbstractConstraint)booleanConstraint).removeVariableChangeListener(listener);
    }
    
}
