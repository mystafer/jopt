package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstTargetXorArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstXorArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolConstTargetXorArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolXorArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolXorArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint that represents two expressions being XOR-ed together
 */
public class BooleanXorConstraint extends ThreeVarConstraint {
	public BooleanXorConstraint(BoolExpr x, BoolExpr y, boolean notY, BoolExpr z) {
		super(x, y, notY, z, false);
	}
	public BooleanXorConstraint(BoolExpr x, boolean y, BoolExpr z) {
		super(x, null, false, z, y);
	}
	public BooleanXorConstraint(BoolExpr x, BoolExpr y, boolean notY, boolean z) {
        super(x, y, notY, null, z);
    }
	public BooleanXorConstraint(BoolExpr x, GenericBooleanConstant y, BoolExpr z) {
		super(x, null, false, z, y);
	}
	public BooleanXorConstraint(BoolExpr x, BoolExpr y, boolean notY, GenericBooleanConstant z) {
        super(x, y, notY, null, z);
    }
	
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        boolean boolConst = constVal;
        if (boolVal!=null) boolConst=boolVal.booleanValue();
        AbstractConstraint constraint=null;
        if (yexpr==null) {
            if (gc!=null)
                constraint= new BooleanXorConstraint(xexpr, gc, zexpr);
            else
                constraint= new BooleanXorConstraint(xexpr, boolConst, zexpr);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new BooleanXorConstraint(xexpr, yexpr, false, gc);
            else
                constraint= new BooleanXorConstraint(xexpr, yexpr, false, boolConst);
        }
        else {
            constraint= new BooleanXorConstraint(xexpr, yexpr, false, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createOpposite() {
        if (genConstVal != null) {
            if (yexpr==null)
                return new BooleanXorConstraint(xexpr, genConstVal, zexpr.notExpr());
            else if (zexpr==null)
                return new BooleanXorConstraint(xexpr, yexpr, notY, genConstVal.getOpposite());
            else
                return new BooleanXorConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
        else {
            if (yexpr==null)
                return new BooleanXorConstraint(xexpr, constVal, zexpr.notExpr());
            else if (zexpr==null)
                return new BooleanXorConstraint(xexpr, yexpr, notY, !constVal);
            else
                return new BooleanXorConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
    }
    
    /**
     * Creates generic boolean arcs
     */
    protected Arc[] createGenericArcs() {
        if (genConstVal == null) {
            //If y is null, it means that user intended to replace it with the constant value
            if (yexpr==null) {
                return new Arc[] {
                        new GenericBoolXorArc(xexpr.getNode(), false, constVal, zexpr.getNode(), false),
                        new GenericBoolXorArc(zexpr.getNode(), false, constVal, xexpr.getNode(), false),
                };
            }
            
            //If z is null, it means that user intended to replace it with the constant value
            if (zexpr==null) {
                return new Arc[] {
                        new GenericBoolConstTargetXorArc(xexpr.getNode(), false, constVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetXorArc(yexpr.getNode(), notY, constVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X XOR Y = Z
                return new Arc[] {
                        new GenericBoolXorArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolXorArc(yexpr.getNode(), notY, zexpr.getNode(), false, xexpr.getNode(), false),
                        new GenericBoolXorArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
                };
            }
        }
        else {
            //If y is null, it means that user intended to replace it with the constant value
            if (yexpr==null) {
                return new Arc[] {
                        new GenericBoolXorArc(xexpr.getNode(), false, genConstVal, zexpr.getNode(), false),
                        new GenericBoolXorArc(zexpr.getNode(), false, genConstVal, xexpr.getNode(), false),
                };
            }
            
            //If z is null, it means that user intended to replace it with the constant value
            if (zexpr==null) {
                return new Arc[] {
                        new GenericBoolConstTargetXorArc(xexpr.getNode(), false, genConstVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetXorArc(yexpr.getNode(), notY, genConstVal, xexpr.getNode(), false)
                };
            }
            
            else {
                // X XOR Y = Z
                return new Arc[] {
                        new GenericBoolXorArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                        new GenericBoolXorArc(yexpr.getNode(), notY, zexpr.getNode(), false, xexpr.getNode(), false),
                        new GenericBoolXorArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
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
    			new BinaryBoolConstXorArc(constVal,xnode, false, znode, false),
    			new BinaryBoolConstXorArc(constVal,znode, false, xnode, false),
    		};
    	}
    	
        //If z is null, it means that user intended to replace it with the constant value
        if (zexpr==null) {
            return new Arc[] {
                new BinaryBoolConstTargetXorArc(ynode, notY, constVal, xnode, false),
                new BinaryBoolConstTargetXorArc(xnode, false, constVal, ynode, notY)
            };
        }
        
    	else {
	    	// X XOR Y = Z
	    	return new Arc[] {
	    	    new TernaryBoolXorArc(xnode, false, ynode, notY, znode, false),
	    	    new TernaryBoolXorArc(ynode, notY, znode, false, xnode, false),
	    	    new TernaryBoolXorArc(znode, false, xnode, false, ynode, notY)
	    	};
    	}
    }

    //javadoc is inherited
    protected boolean violated() {
        if (isAnyTrue(currentZ, currentGz)) {
        	return (isAnyTrue(currentX, currentGx) && isAnyTrue(currentY, currentGy)) ||
                   (isAnyFalse(currentX, currentGx) && isAnyFalse(currentY, currentGy));
        }
        else if (isAnyFalse(currentZ, currentGz)) {
            return (isAnyTrue(currentX, currentGx) && isAnyFalse(currentY, currentGy)) ||
                   (isAnyFalse(currentX, currentGx) && isAnyTrue(currentY, currentGy));
        }
        
        return false;
    }    

    
    //  javadoc is inherited
    public String toString() {
        StringBuffer buf = new StringBuffer("X[");
        buf.append(xexpr);
        buf.append("] ^ ");
        
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
