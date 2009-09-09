package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumDiffArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumDiffArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumDiffArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Constraint representing X - y < Z, X - Y > z, etc.
 */
public class DiffConstraint extends ThreeVarConstraint {
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    public DiffConstraint(NumExpr x, NumExpr y, NumExpr z, int constraintType) {
        super(x, y, z, null, null, constraintType);
    }
    public DiffConstraint(NumExpr x, NumExpr y, Number z, int constraintType) {
        super(x, y, null, z, null, constraintType);
    }
    public DiffConstraint(NumExpr x, Number y, NumExpr z, int constraintType) {
        super(x, null, z, y, null, constraintType);
    }
    public DiffConstraint(Number x, NumExpr y, NumExpr z, int constraintType) {
        super(null, y, z, x,null, constraintType);
    }
    public DiffConstraint(NumExpr x, NumExpr y, GenericNumConstant z, int constraintType) {
        super(x, y, null, null, z, constraintType);
    }
    public DiffConstraint(NumExpr x, GenericNumConstant y, NumExpr z, int constraintType) {
        super(x, null, z, null, y, constraintType);
    }
    public DiffConstraint(GenericNumConstant x, NumExpr y, NumExpr z, int constraintType) {
        super(null, y, z, null, x, constraintType);
    }    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        if (gxexpr!=null || gyexpr!=null || gzexpr!=null)
            return createGenericArcs();
        else
            return createStandardArcs();
    }
    
    /**
     * Creates generic numeric arcs
     */
    private Arc[] createGenericArcs() {
        
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // X - Y < z
        if (zexpr == null) {
            Arc a1=null;
            Arc a2=null;
            if (constVal != null) {
                //z + Y > X
                a1 = new GenericNumSumArc(constVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);
                
                // X - z < Y
                a2 = new GenericNumDiffArc(xexpr.getNode(), constVal, yexpr.getNode(), nodeType, arcType1);
            }
            else {
//              z + Y > X
                a1 = new GenericNumSumArc(genConstVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);
                
                // X - z < Y
                a2 = new GenericNumDiffArc(xexpr.getNode(), genConstVal, yexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // X - y < Z
        else if (yexpr==null) {
            Arc a1=null;
            Arc a2=null;
            if (constVal != null) {
                // Z + y > X
                a1 = new GenericNumSumArc(zexpr.getNode(), constVal, xexpr.getNode(), nodeType, arcType2);
                
                // X - y < Z
                a2 = new GenericNumDiffArc(xexpr.getNode(), constVal, zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // Z + y > X
                a1 = new GenericNumSumArc(zexpr.getNode(), genConstVal, xexpr.getNode(), nodeType, arcType2);
                
                // X - y < Z
                a2 = new GenericNumDiffArc(xexpr.getNode(), genConstVal, zexpr.getNode(), nodeType, arcType1);                
            }
            return new Arc[] { a1, a2 };
        }
        
        // x - Y < Z
        else if (xexpr==null) {
            Arc a1=null;
            Arc a2=null;
            if (constVal != null) {
                // x - Z < Y
                a1 = new GenericNumDiffArc(constVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
                
                // x - Y < Z
                a2 = new GenericNumDiffArc(constVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // x - Z < Y
                a1 = new GenericNumDiffArc(genConstVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
                
                // x - Y < Z
                a2 = new GenericNumDiffArc(genConstVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);                
            }
            return new Arc[] { a1, a2 };
        }
        
        // X - Y < Z
        else {
            // X - Y < Z
            Arc a1 = new GenericNumDiffArc(xexpr.getNode(), yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

            // Z + Y > X
            Arc a2 = new GenericNumSumArc(zexpr.getNode(), yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);

            // X - Z < Y
            Arc a3 = new GenericNumDiffArc(xexpr.getNode(), zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
            
            return new Arc[] { a1, a2, a3 };
        }
    }
    
    /**
     * Creates standard numeric arcs
     */
    private Arc[] createStandardArcs() {
        NumNode x = (xexpr!=null) ? (NumNode) xexpr.getNode() : null;
        NumNode y = (yexpr!=null) ? (NumNode) yexpr.getNode() : null;
        NumNode z = (zexpr!=null) ? (NumNode) zexpr.getNode() : null;
    
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // X - Y < z
        if (z == null) {
            // z + Y > X
            Arc a1 = new BinaryNumSumArc(constVal, y, x, nodeType, arcType2);

            // X - z < Y
            Arc a2 = new BinaryNumDiffArc(x, constVal, y, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X - y < Z
        else if (y==null) {
            // Z + y > X
            Arc a1 = new BinaryNumSumArc(z, constVal, x, nodeType, arcType2);

            // X - y < Z
            Arc a2 = new BinaryNumDiffArc(x, constVal, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // x - Y < Z
        else if (x==null) {
            // x - Z < Y
            Arc a1 = new BinaryNumDiffArc(constVal, z, y, nodeType, arcType1);

            // x - Y < Z
            Arc a2 = new BinaryNumDiffArc(constVal, y, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X - Y < Z
        else {
            // X - Y < Z
            Arc a1 = new TernaryNumDiffArc(x, y, z, nodeType, arcType1);

            // Z + Y > X
            Arc a2 = new TernaryNumSumArc(z, y, x, nodeType, arcType2);

            // X - Z < Y
            Arc a3 = new TernaryNumDiffArc(x, z, y, nodeType, arcType1);
            
            return new Arc[] { a1, a2, a3 };
        }
    }

    // javadoc inherited from ThreeVarConstraint
    protected AbstractConstraint createConstraint(NumExpr xexpr, NumExpr yexpr, NumExpr zexpr, Number numVal, GenericNumConstant gc, int constraintType) {
        Number numConst = constVal;
        if (numVal!=null) numConst=numVal;
        AbstractConstraint constraint = null;
        if (xexpr==null) {
            if (gc!=null)
                constraint= new DiffConstraint(gc, yexpr, zexpr, constraintType);
            else
                constraint= new DiffConstraint(numConst, yexpr, zexpr, constraintType);
        }
        else if (yexpr==null) {
            if (gc!=null)
                constraint= new DiffConstraint(xexpr, gc, zexpr, constraintType);
            else
                constraint= new DiffConstraint(xexpr, numConst, zexpr, constraintType);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new DiffConstraint(xexpr, yexpr, gc, constraintType);
            else
                constraint= new DiffConstraint(xexpr, yexpr, numConst, constraintType);
        }
        else
            constraint= new DiffConstraint(xexpr, yexpr, zexpr, constraintType);
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    public boolean violated() {
        double precision = getPrecision();
        
        switch (constraintType) {
        	// X - Y < Z
        	case ThreeVarConstraint.LT:
                NumberMath.subtractNoInvalid(getLargestMin(currentX, currentGx), 
                                             getSmallestMax(currentY, currentGy), 
											 nodeType, v1);
          	    return NumberMath.compare(v1, getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
        	
        	// X - Y <= Z
        	case ThreeVarConstraint.LEQ:
                NumberMath.subtractNoInvalid(getLargestMin(currentX, currentGx), 
                                             getSmallestMax(currentY, currentGy), 
										     nodeType, v1);
          	    return NumberMath.compare(v1, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
        	
        	// X - Y > Z
        	case ThreeVarConstraint.GT:
                NumberMath.subtractNoInvalid(getSmallestMax(currentX, currentGx), 
                                             getLargestMin(currentY, currentGy), 
											 nodeType, v1);
          	    return NumberMath.compare(v1, getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
        	
        	// X - Y >= Z
        	case ThreeVarConstraint.GEQ:
                NumberMath.subtractNoInvalid(getSmallestMax(currentX, currentGx), 
                                             getLargestMin(currentY, currentGy), 
											 nodeType, v1);
          	    return NumberMath.compare(v1, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        	
        	// X - Y != Z
        	case ThreeVarConstraint.NEQ:
        	    if ( ((xexpr==null || xexpr.isBound()) || (currentX!=null && currentX.isBound()) || (currentGx!=null && currentGx.isBound())) &&
                 	 ((yexpr==null || yexpr.isBound()) || (currentY!=null && currentY.isBound()) || (currentGy!=null && currentGy.isBound())) &&
                 	 ((zexpr==null || zexpr.isBound()) || (currentZ!=null && currentZ.isBound()) || (currentGz!=null && currentGz.isBound())) )
        	    {
        	        // note that because the variables involved are bound,
        	        // min = max, so it should make no difference which is used
                    NumberMath.subtractNoInvalid(getLargestMin(currentX, currentGx), 
                                                 getLargestMin(currentY, currentGy), 
												 nodeType, v1);
          	        return NumberMath.compare(v1, getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
        	    }
        	    else
        	        return false;
        	
        	// X - Y == Z
        	default:
                NumberMath.subtractNoInvalid(getLargestMin(currentX, currentGx), 
                                             getSmallestMax(currentY, currentGy), 
											 nodeType, v1);
                NumberMath.subtractNoInvalid(getSmallestMax(currentX, currentGx), 
                		                     getLargestMin(currentY, currentGy), 
											 nodeType, v2);            
        	    return NumberMath.compare(v1, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0 ||
                       NumberMath.compare(v2, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        }
    }
    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        if (xexpr==null) {
            buf.append("x[");
            buf.append(constVal);
        }
        else {
            buf.append("X[");
            buf.append(xexpr);
        }
        buf.append("] - ");
        
        if (yexpr==null) {
            buf.append("y[");
            buf.append(constVal);
        }
        else {
            buf.append("Y[");
            buf.append(yexpr);
        }
        buf.append("] ");
        
        buf.append("= ");
        
        if (zexpr==null) {
            buf.append("z[");
            buf.append(constVal);
        }
        else {
            buf.append("Z[");
            buf.append(zexpr);
        }
        buf.append("]");
        
        return buf.toString();
    }
}