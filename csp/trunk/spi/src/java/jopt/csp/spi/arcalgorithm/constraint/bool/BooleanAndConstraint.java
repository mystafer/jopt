package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstAndReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstSourceAndArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolConstTargetAndArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolAndArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolAndReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolConstTargetAndArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolAndArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolAndReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint that restricts a BoolExpr to be the AND of two other BoolExpr
 */
public class BooleanAndConstraint extends ThreeVarConstraint {
	
    /**
     * Constrains z to be x AND y.  If notY is true, then z is constrained to be
     * x AND !y.
     * 
     * @param x		boolean expression to include in the AND expression
     * @param y		boolean expression to include in the AND expression
     * @param notY	boolean representing whether to use the original or opposite value of y
     * @param z		boolean expression to represent the value of the AND expression
     */
    public BooleanAndConstraint(BoolExpr x, BoolExpr y, boolean notY, BoolExpr z) {
		super(x, y, notY, z, false);
	}
    /**
     * Constrains z to be x AND y.
     * 
     * @param x		boolean expression to include in the AND expression
     * @param y		boolean to include in the AND expression
     * @param z		boolean expression to represent the value of the AND expression
     */
    public BooleanAndConstraint(BoolExpr x, boolean y, BoolExpr z) {
		super(x, null, false, z, y);
	}
    /**
     * Constrains z to be x AND y.  If notY is true, then z is constrained to be
     * x AND !y.
     * 
     * @param x		boolean expression to include in the AND expression
     * @param y		boolean expression to include in the AND expression
     * @param notY	boolean representing whether to use the original or opposite value of y
     * @param z		boolean to represent the value of the AND expression
     */
    public BooleanAndConstraint(BoolExpr x, BoolExpr y, boolean notY, boolean z) {
        super(x, y, notY, null, z);
    }
    /**
     * Constrains z to be x AND y.
     * 
     * @param x		boolean expression to include in the AND expression
     * @param y		generic boolean constant to include in the AND expression
     * @param z		boolean expression to represent the value of the AND expression
     */
    public BooleanAndConstraint(BoolExpr x, GenericBooleanConstant y, BoolExpr z) {
		super(x, null, false, z, y);
	}
    /**
     * Constrains z to be x AND y.  If notY is true, then z is constrained to be
     * x AND !y.
     * 
     * @param x		boolean expression to include in the AND expression
     * @param y		boolean expression to include in the AND expression
     * @param notY	boolean representing whether to use the original or opposite value of y
     * @param z		generic boolean constant to represent the value of the AND expression
     */
    public BooleanAndConstraint(BoolExpr x, BoolExpr y, boolean notY, GenericBooleanConstant z) {
        super(x, y, notY, null, z);
    }
	
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        boolean boolConst = constVal;
        if (boolVal!=null) boolConst=boolVal.booleanValue();
        AbstractConstraint constraint = null;
        if (yexpr==null) {
            if (gc!=null)
                constraint= new BooleanAndConstraint(xexpr, gc, zexpr);
            else
                constraint= new BooleanAndConstraint(xexpr, boolConst, zexpr);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new BooleanAndConstraint(xexpr, yexpr, false, gc);
            else
                constraint= new BooleanAndConstraint(xexpr, yexpr, false, boolConst);
        }
        else {
            constraint= new BooleanAndConstraint(xexpr, yexpr, false, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
        
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createOpposite() {
        if(genConstVal!=null) {
            if (yexpr==null)
                return new BooleanAndConstraint(xexpr, genConstVal, zexpr.notExpr());
            else if (zexpr==null) {;
                return new BooleanAndConstraint(xexpr, yexpr, notY, genConstVal.getOpposite());
            }
            else
                return new BooleanAndConstraint(xexpr, yexpr, notY, zexpr.notExpr());            
        }
        else {
            if (yexpr==null)
                return new BooleanAndConstraint(xexpr, constVal, zexpr.notExpr());
            else if (zexpr==null)
                return new BooleanAndConstraint(xexpr, yexpr, notY, !constVal);
            else
                return new BooleanAndConstraint(xexpr, yexpr, notY, zexpr.notExpr());
        }
    }

    // javadoc inherited from ThreeVarConstraint
    protected Arc[] createGenericArcs() {
        //If y is null, it means that user intended to replace it with the constant value
        if (yexpr==null) {
            if (genConstVal == null) {
                return new Arc[] {
                        new GenericBoolAndArc(xexpr.getNode(), false, constVal, zexpr.getNode(), false),
                        new GenericBoolAndReflexArc(zexpr.getNode(), false, constVal, xexpr.getNode(), false)
                };
            }
            else {
                return new Arc[] {
                        new GenericBoolAndArc(xexpr.getNode(), false, genConstVal, zexpr.getNode(), false),
                        new GenericBoolAndReflexArc(zexpr.getNode(), false, genConstVal, xexpr.getNode(), false)
                };    
            }
        }
        
        //If z is null, it means that user intended to replace it with the constant value
        else if (zexpr==null) {
            if (genConstVal == null) {
                return new Arc[] {
                        new GenericBoolConstTargetAndArc(xexpr.getNode(), false, constVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetAndArc(yexpr.getNode(), notY, constVal, xexpr.getNode(), false)
                };
            }
            else {
                return new Arc[] {
                        new GenericBoolConstTargetAndArc(xexpr.getNode(), false, genConstVal, yexpr.getNode(), notY),
                        new GenericBoolConstTargetAndArc(yexpr.getNode(), notY, genConstVal, xexpr.getNode(), false)
                    };    
            }
        }
        
        else {
            // X & Y = Z
            return new Arc[] {
                new GenericBoolAndArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                new GenericBoolAndReflexArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                new GenericBoolAndReflexArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
            };
        }
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected Arc[] createStandardArcs() {
        BooleanNode xnode = (xexpr!=null) ? (BooleanNode)xexpr.getNode() : null;
        BooleanNode ynode = (yexpr!=null) ? (BooleanNode)yexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
    	//If y is null, it means that user intended to replace it with the constant value
    	if (yexpr==null) {
    		return new Arc[] {
				new BinaryBoolConstSourceAndArc(xnode, false, constVal, znode, false),
				new BinaryBoolConstAndReflexArc(znode, false, constVal, xnode, false)
    		};
    	}
        
        //If z is null, it means that user intended to replace it with the constant value
        else if (zexpr==null) {
            return new Arc[] {
                new BinaryBoolConstTargetAndArc(ynode, notY, constVal, xnode, false),
                new BinaryBoolConstTargetAndArc(xnode, false, constVal, ynode, notY)
            };
        }
        
    	else {
	    	// X & Y = Z
	    	return new Arc[] {
	    	    new TernaryBoolAndArc(xnode, false, ynode, notY, znode, false),
	    	    new TernaryBoolAndReflexArc(znode, false, ynode, notY, xnode, false),
	    	    new TernaryBoolAndReflexArc(znode, false, xnode, false, ynode, notY)
	    	};
    	}
    }
    
    //javadoc is inherited
    protected boolean violated() {
        return  (isAnyTrue(currentZ, currentGz) && (isAnyFalse(currentX, currentGx) || isAnyFalse(currentY, currentGy))) ||
                (isAnyFalse(currentZ, currentGz) && isAnyTrue(currentX, currentGx) && isAnyTrue(currentY, currentGy));
    }    
    
    /**
     * Returns string representation of the and constraint
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("X[");
        buf.append(xexpr);
        buf.append("] and ");
        
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
        
        buf.append("= Z[");
        buf.append(zexpr);
        buf.append("]");
        
        return buf.toString();
    }
}
