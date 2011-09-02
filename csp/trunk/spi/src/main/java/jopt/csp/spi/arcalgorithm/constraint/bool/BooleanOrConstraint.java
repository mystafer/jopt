package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstOrArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstOrReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstTargetOrArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolConstTargetOrArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolOrArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolOrReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolOrArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolOrReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint that restricts a BoolExpr to be true if either of two other BoolExpr are true
 */
public class BooleanOrConstraint extends ThreeVarConstraint {
	public BooleanOrConstraint(BoolExpr x, BoolExpr y, boolean notY, BoolExpr z) {
		super(x, y, notY, z, false);
	}
	public BooleanOrConstraint(BoolExpr x, boolean y, BoolExpr z) {
		super(x, null, false, z, y);
	}
	public BooleanOrConstraint(BoolExpr x, BoolExpr y, boolean notY, boolean z) {
        super(x, y, notY, null, z);
    }
	public BooleanOrConstraint(BoolExpr x, GenericBooleanConstant y, BoolExpr z) {
		super(x, null, false, z, y);
	}
	public BooleanOrConstraint(BoolExpr x, BoolExpr y, boolean notY, GenericBooleanConstant z) {
        super(x, y, notY, null, z);
    }
	
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        boolean boolConst = constVal;
        if (boolVal!=null) boolConst=boolVal.booleanValue();
        AbstractConstraint constraint=null;
        if (yexpr==null) {
            if (gc!=null)
                constraint= new BooleanOrConstraint(xexpr, gc, zexpr);
            else
                constraint= new BooleanOrConstraint(xexpr, boolConst, zexpr);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new BooleanOrConstraint(xexpr, yexpr, false, gc);
            else
                constraint= new BooleanOrConstraint(xexpr, yexpr, false, boolConst);
        }
        else {
            constraint= new BooleanOrConstraint(xexpr, yexpr, false, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createOpposite() {
        if (genConstVal != null) {
            if (yexpr==null)
                return new BooleanOrConstraint(xexpr, genConstVal, zexpr.notExpr());
            else if (zexpr==null)
                return new BooleanOrConstraint(xexpr, yexpr, notY, genConstVal.getOpposite());
            else
                return new BooleanOrConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
        else {
            if (yexpr==null)
                return new BooleanOrConstraint(xexpr, constVal, zexpr.notExpr());
            else if (zexpr==null)
                return new BooleanOrConstraint(xexpr, yexpr, notY, !constVal);
            else
                return new BooleanOrConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
    }

    /**
     * Creates generic boolean arcs
     */
    protected Arc[] createGenericArcs() {
        if (genConstVal==null) {
            //If y is null, it means that user intended to replace it with the constant value
            if (yexpr==null) {
                return new Arc[] {
                        new GenericBoolOrArc(xexpr.getNode(), false, constVal, zexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, constVal, xexpr.getNode(), false)
                };
            }
            
            //If z is null, it means that user intended to replace it with the constant value
            else if (zexpr==null) {
                return new Arc[] {
                        new GenericBoolConstTargetOrArc(xexpr.getNode(), false, constVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetOrArc(yexpr.getNode(), notY, constVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X & Y = Z
                return new Arc[] {
                        new GenericBoolOrArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
                };
            }
        }
        else {
            //If y is null, it means that user intended to replace it with the constant value
            if (yexpr==null) {
                return new Arc[] {
                        new GenericBoolOrArc(xexpr.getNode(), false, genConstVal, zexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, genConstVal, xexpr.getNode(), false)
                };
            }
            
            //If z is null, it means that user intended to replace it with the constant value
            else if (zexpr==null) {
                return new Arc[] {
                        new GenericBoolConstTargetOrArc(xexpr.getNode(), false, genConstVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetOrArc(yexpr.getNode(), notY, genConstVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X & Y = Z
                return new Arc[] {
                        new GenericBoolOrArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                        new GenericBoolOrReflexArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
                };
            }
        }
    }
    
    /**
     * Creates standard boolean arcs
     */
    protected Arc[] createStandardArcs() {
        BooleanNode xnode = (xexpr!=null) ? (BooleanNode)xexpr.getNode() : null;
        BooleanNode ynode = (yexpr!=null) ? (BooleanNode)yexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
    	//If y is null, it means that user intended to replace it with the constant value
    	if (yexpr==null) {
    		return new Arc[] {
    			new BinaryBoolConstOrArc(constVal,xnode, false, znode, false),
				new BinaryBoolConstOrReflexArc(constVal,znode, false, xnode, false)
    		};
    	}

        //If z is null, it means that user intended to replace it with the constant value
        else if (zexpr==null) {
            return new Arc[] {
                new BinaryBoolConstTargetOrArc(ynode, notY, constVal, xnode, false),
                new BinaryBoolConstTargetOrArc(xnode, false, constVal, ynode, notY)
            };
        }
        
    	else {
	    	// X & Y = Z
	    	return new Arc[] {
	    	    new TernaryBoolOrArc(xnode, false, ynode, notY, znode, false),
	    	    new TernaryBoolOrReflexArc(znode, false, ynode, notY, xnode, false),
	    	    new TernaryBoolOrReflexArc(znode, false, xnode, false, ynode, notY)
	    	};
    	}
    }

    //javadoc is inherited
    protected boolean violated() {
        return  (isAnyFalse(currentZ, currentGz) && (isAnyTrue(currentX, currentGx) || isAnyTrue(currentY, currentGy))) ||
                (isAnyTrue(currentZ, currentGz) && isAnyFalse(currentX, currentGx) && isAnyFalse(currentY, currentGy));
    }    
    
    //  javadoc is inherited
    public String toString() {
        StringBuffer buf = new StringBuffer("X[");
        buf.append(xexpr);
        buf.append("] or ");
        
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
        buf.append(zexpr);
        
        return buf.toString();
    }
}
