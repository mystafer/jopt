package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstTargetXImpliesArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstTargetYImpliesArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolImpliesThreeVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolImpliesXReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolConstTargetXImpliesArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolConstTargetYImpliesArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolImpliesThreeVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolImpliesXReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolImpliesYReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolImpliesArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolImpliesXReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolImpliesYReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint representing X -> Y = Z
 */
public class BooleanImpliesThreeVarConstraint extends ThreeVarConstraint {
    public BooleanImpliesThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, BoolExpr z) {
        super(x, y, notY, z, false);
    }
    public BooleanImpliesThreeVarConstraint(BoolExpr x, boolean y, BoolExpr z) {
        super(x, null, false, z, y);
    }
    public BooleanImpliesThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, boolean z) {
        super(x, y, notY, null, z);
    }
    public BooleanImpliesThreeVarConstraint(BoolExpr x, GenericBooleanConstant y, BoolExpr z) {
        super(x, null, false, z, y);
    }
    public BooleanImpliesThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, GenericBooleanConstant z) {
        super(x, y, notY, null, z);
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        boolean boolConst = constVal;
        if (boolVal!=null) boolConst=boolVal.booleanValue();
        AbstractConstraint constraint = null;
        if (yexpr==null) {
            if (gc!=null)
                constraint= new BooleanImpliesThreeVarConstraint(xexpr, gc, zexpr);
            else
                constraint= new BooleanImpliesThreeVarConstraint(xexpr, boolConst, zexpr);
        }
        else {
            constraint= new BooleanImpliesThreeVarConstraint(xexpr, yexpr, false, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createOpposite() {
        if (genConstVal == null) {
            if (yexpr==null)
                return new BooleanImpliesThreeVarConstraint(xexpr, constVal, zexpr.notExpr());
            else if (zexpr==null)
                return new  BooleanImpliesThreeVarConstraint(xexpr, yexpr, notY, !constVal);
            else
                return new BooleanImpliesThreeVarConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
        else {
            if (yexpr==null)
                return new BooleanImpliesThreeVarConstraint(xexpr, genConstVal, zexpr.notExpr());
            else if (zexpr==null)
                return new  BooleanImpliesThreeVarConstraint(xexpr, yexpr, notY, genConstVal.getOpposite());
            else
                return new BooleanImpliesThreeVarConstraint(xexpr, yexpr, notY, zexpr.notExpr());            
        }
    }
    
    //  javadoc is inherited
    protected Arc[] createGenericArcs() {
        if (genConstVal == null) {
            if (yexpr==null) {
                // X -> y = Z
                return new Arc[] {
                        new GenericBoolImpliesThreeVarArc(xexpr.getNode(), false, constVal, zexpr.getNode(), false),
                        new GenericBoolImpliesXReflexArc(zexpr.getNode(), false, constVal, xexpr.getNode(), false)
                };
            }
            
            else if (zexpr==null) {
                // X -> Y = z
                return new Arc[] {
                        new GenericBoolConstTargetYImpliesArc(xexpr.getNode(), false, constVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetXImpliesArc(yexpr.getNode(), notY, constVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X -> Y = Z
                return new Arc[] {
                        new GenericBoolImpliesThreeVarArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolImpliesXReflexArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                        new GenericBoolImpliesYReflexArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
                };
            }
        }
        else {
            if (yexpr==null) {
                // X -> y = Z
                return new Arc[] {
                        new GenericBoolImpliesThreeVarArc(xexpr.getNode(), false, genConstVal, zexpr.getNode(), false),
                        new GenericBoolImpliesXReflexArc(zexpr.getNode(), false, genConstVal, xexpr.getNode(), false)
                };
            }
            
            else if (zexpr==null) {
                // X -> Y = z
                return new Arc[] {
                        new GenericBoolConstTargetYImpliesArc(xexpr.getNode(), false, genConstVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetXImpliesArc(yexpr.getNode(), notY, genConstVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X -> Y = Z
                return new Arc[] {
                        new GenericBoolImpliesThreeVarArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolImpliesXReflexArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                        new GenericBoolImpliesYReflexArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
                };
            }
        }
    }
    
    //  javadoc is inherited
    protected Arc[] createStandardArcs() {
        BooleanNode xnode = (xexpr!=null) ? (BooleanNode)xexpr.getNode() : null;
        BooleanNode ynode = (yexpr!=null) ? (BooleanNode)yexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
        if (yexpr==null) {
            // X -> y = Z
            return new Arc[] {
                    new BinaryBoolImpliesThreeVarArc(xnode, false, constVal, znode, false),
                    new BinaryBoolImpliesXReflexArc(znode, false, constVal, xnode, false)
            };
        }
        
        else if (zexpr==null) {
            // X -> Y = z
            return new Arc[] {
                    new BinaryBoolConstTargetYImpliesArc(xnode, false, constVal, ynode, notY),
                    new BinaryBoolConstTargetXImpliesArc(ynode, notY, constVal, xnode, false)
            };
        }
        
        else {
            // X -> Y = Z
            return new Arc[] {
                    new TernaryBoolImpliesArc(xnode, false, ynode, notY, znode, false),
                    new TernaryBoolImpliesXReflexArc(znode, false, ynode, notY, xnode, false),
                    new TernaryBoolImpliesYReflexArc(znode, false, xnode, false, ynode, notY)
            };
        }
    }
    
    //javadoc is inherited
    protected boolean violated() {
        return (isAnyTrue(currentZ, currentGz) && isAnyTrue(currentX, currentGx) && isAnyFalse(currentY, currentGy)) ||
        (isAnyFalse(currentZ, currentGz) && (isAnyFalse(currentX, currentGx) || isAnyTrue(currentY, currentGy)));
    }    
    
    //  javadoc is inherited
    public String toString() {
        StringBuffer buf = new StringBuffer("X[");
        buf.append(xexpr);
        buf.append("] -> ");
        
        if (yexpr==null) {
            buf.append("y[");
            buf.append(constVal);
            buf.append("] ");
        }
        else {
            if (notY)
                buf.append("!Y[");
            else
                buf.append("Y[");
            buf.append(yexpr);
            buf.append("] ");
        }
        
        buf.append("= ");
        buf.append("Z[");
        buf.append(zexpr);
        buf.append("]");
        
        return buf.toString();
    }
}
