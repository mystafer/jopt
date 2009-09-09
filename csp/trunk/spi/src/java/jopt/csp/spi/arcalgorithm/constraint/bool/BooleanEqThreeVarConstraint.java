package jopt.csp.spi.arcalgorithm.constraint.bool;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryBoolEqThreeVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericBoolEqThreeVarArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryBoolEqArc;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;

/**
 * Constraint representing (X == Y) = Z
 */
public class BooleanEqThreeVarConstraint extends ThreeVarConstraint {
	/**
	 * A constructor to create a BooleanEqConstraint using three boolean expressions.
	 * @param x		boolean expression representing one of the source values
	 * @param y		boolean expression representing one of the source values
	 * @param notY	boolean representing whether we deal y or !y
	 * @param z		boolean expression representing whether or not the equality of x and y is true
	 */
	public BooleanEqThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, BoolExpr z) {
		super(x, y, notY, z, false);
	}
	/**
	 * A constructor to create a BooleanEqConstraint using three boolean expressions.
	 * @param x		boolean expression representing one of the source values
	 * @param y		boolean representing one of the source values
	 * @param z		boolean expression representing whether or not the equality of x and y is true
	 */
	public BooleanEqThreeVarConstraint(BoolExpr x, boolean y, BoolExpr z) {
		super(x, null, false, z, y);
	}

	/**
	 * A constructor to create a BooleanEqConstraint using three boolean expressions.
	 * @param x		boolean expression representing one of the source values
	 * @param y		generic boolean representing one of the source values
	 * @param z		boolean expression representing whether or not the equality of x and y is true
	 */
	public BooleanEqThreeVarConstraint(BoolExpr x, GenericBooleanConstant y, BoolExpr z) {
		super(x, null, false, z, y);
	}	
	/**
	 * A constructor to create a BooleanEqConstraint using three boolean expressions.
	 * @param x		boolean expression representing one of the source values
	 * @param y		boolean expression representing one of the source values
	 * @param notY	boolean representing whether we deal y or !y
	 * @param z		constant boolean representing whether or not the equality of x and y is true
	 */
	public BooleanEqThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, boolean z) {
	    super(x, y, notY, null, z);
	}
	/**
	 * A constructor to create a BooleanEqConstraint using three boolean expressions.
	 * @param x		boolean expression representing one of the source values
	 * @param y		boolean expression representing one of the source values
	 * @param notY	boolean representing whether we deal y or !y
	 * @param z		boolean expression representing whether or not the equality of x and y is true
	 */
	public BooleanEqThreeVarConstraint(BoolExpr x, BoolExpr y, boolean notY, GenericBooleanConstant z) {
	    super(x, y, notY, null, z);
	}
	
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(BoolExpr xexpr, BoolExpr yexpr, BoolExpr zexpr, Boolean boolVal, GenericBooleanConstant gc) {
        boolean boolConst = constVal;
        if (boolVal!=null) boolConst=boolVal.booleanValue();
        AbstractConstraint constraint = null;
        if (yexpr==null) {
            if (gc!=null)
                constraint= new BooleanEqThreeVarConstraint(xexpr, gc, zexpr);
            else
                constraint= new BooleanEqThreeVarConstraint(xexpr, boolConst, zexpr);
        }
        else {
            constraint= new BooleanEqThreeVarConstraint(xexpr, yexpr, false, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createOpposite() {
        if (yexpr==null)
            if (genConstVal == null) {
                return new BooleanEqThreeVarConstraint(xexpr, constVal, zexpr.notExpr());
            }
            else {
                return new BooleanEqThreeVarConstraint(xexpr, genConstVal, zexpr.notExpr());
            }
        else if (zexpr==null)
           if(genConstVal == null) {
               return new BooleanEqThreeVarConstraint(xexpr, yexpr, notY, !constVal);
           }
           else {
               return new BooleanEqThreeVarConstraint(xexpr, yexpr, notY, genConstVal.getOpposite());
           }
        else
            return new BooleanEqThreeVarConstraint(xexpr, yexpr, notY, zexpr.notExpr());
    }

    /**
     * Creates generic boolean arcs
     */
    protected Arc[] createGenericArcs() {
        if (yexpr==null) {
            if (genConstVal==null) {
                // (X == y) = Z
                return new Arc[] {
                        new GenericBoolEqThreeVarArc(xexpr.getNode(), false, constVal, zexpr.getNode(), false),
                        new GenericBoolEqThreeVarArc(zexpr.getNode(), false, constVal, xexpr.getNode(), false)
                };
            }
            else {
//              (X == y) = Z
                return new Arc[] {
                        new GenericBoolEqThreeVarArc(xexpr.getNode(), false, genConstVal, zexpr.getNode(), false),
                        new GenericBoolEqThreeVarArc(zexpr.getNode(), false, genConstVal, xexpr.getNode(), false)
                };
            }
        }
        
        else if (zexpr==null) {
            if (genConstVal==null) {
                // (X == Y) = z
                return new Arc[] {
                        new GenericBoolEqThreeVarArc(xexpr.getNode(), false, constVal, yexpr.getNode(), notY),
                        new GenericBoolEqThreeVarArc(yexpr.getNode(), notY, constVal, xexpr.getNode(), false)
                };
            }
            else{
                // (X == Y) = z
                return new Arc[] {
                    new GenericBoolEqThreeVarArc(xexpr.getNode(), false, genConstVal, yexpr.getNode(), notY),
                    new GenericBoolEqThreeVarArc(yexpr.getNode(), notY, genConstVal, xexpr.getNode(), false)
                };                
            }
        }
        
        else {
            // (X == Y) = Z
            return new Arc[] {
                new GenericBoolEqThreeVarArc(xexpr.getNode(), false, yexpr.getNode(), notY, zexpr.getNode(), false),
                new GenericBoolEqThreeVarArc(zexpr.getNode(), false, yexpr.getNode(), notY, xexpr.getNode(), false),
                new GenericBoolEqThreeVarArc(zexpr.getNode(), false, xexpr.getNode(), false, yexpr.getNode(), notY)
            };
        }
    }
    
    /**
     * Creates standard boolean arcs
     */
    protected Arc[] createStandardArcs() {
        BooleanNode xnode = (xexpr!=null) ? (BooleanNode)xexpr.getNode() : null;
        BooleanNode ynode = (yexpr!=null) ? (BooleanNode)yexpr.getNode() : null;
        BooleanNode znode = (zexpr!=null) ? (BooleanNode)zexpr.getNode() : null;
        
    	if (yexpr==null) {
    		// (X == y) = Z
    		return new Arc[] {
				new BinaryBoolEqThreeVarArc(xnode, false, constVal, znode, false),
				new BinaryBoolEqThreeVarArc(znode, false, constVal, xnode, false)
    		};
    	}
    	
    	else if (zexpr==null) {
            // (X == Y) = z
            return new Arc[] {
                new BinaryBoolEqThreeVarArc(xnode, false, constVal, ynode, notY),
				new BinaryBoolEqThreeVarArc(ynode, notY, constVal, xnode, false)
            };
        }
    	
    	else {
	    	// (X == Y) = Z
	    	return new Arc[] {
	    	    new TernaryBoolEqArc(xnode, false, ynode, notY, znode, false),
	    	    new TernaryBoolEqArc(znode, false, ynode, notY, xnode, false),
	    	    new TernaryBoolEqArc(znode, false, xnode, false, ynode, notY)
	    	};
    	}
    }
    
    //javadoc is inherited
    protected boolean violated() {
        return (isAnyTrue(currentZ, currentGz) &&
              		((isAnyTrue(currentX, currentGx) && isAnyFalse(currentY, currentGy)) ||
               		(isAnyFalse(currentX, currentGx) && isAnyTrue(currentY, currentGy))))
                ||
       			(isAnyFalse(currentZ, currentGz) &&
       			    ((isAnyTrue(currentX, currentGx) && isAnyTrue(currentY, currentGy)) ||
       			    (isAnyFalse(currentX, currentGx) && isAnyFalse(currentY, currentGy))));
    }    
    
    /**
     * Returns string representation of this three variable equality constraint
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("X[");
        buf.append(xexpr);
        buf.append("] == ");
        
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
