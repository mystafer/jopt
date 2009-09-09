package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumProdArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumProdReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumProdArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumProdReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumProdArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumProdReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Constraint representing X * y < Z, X * Y > z, etc.
 */
public class ProdConstraint extends ThreeVarConstraint {
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    // Four variables used in the violated() method to determine overall maximum and minimum values
    // perhaps they should be moved inside the method and made local variables.
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private MutableNumber v3 = new MutableNumber();
    private MutableNumber v4 = new MutableNumber();
     
    public ProdConstraint(NumExpr x, NumExpr y, NumExpr z, int constraintType) {
        super(x, y, z, null, null, constraintType);
    }
    public ProdConstraint(NumExpr x, NumExpr y, Number z, int constraintType) {
        super(x, y, null, z, null, constraintType);
    }
    public ProdConstraint(NumExpr x, Number y, NumExpr z, int constraintType) {
        super(x, null, z, y, null, constraintType);
    }
    public ProdConstraint(Number x, NumExpr y, NumExpr z, int constraintType) {
        super(null, y, z, x, null, constraintType);
    }
    public ProdConstraint(NumExpr x, NumExpr y, GenericNumConstant z, int constraintType) {
        super(x, y, null, null, z, constraintType);
    }
    public ProdConstraint(NumExpr x, GenericNumConstant y, NumExpr z, int constraintType) {
        super(x, null, z, null, y, constraintType);
    }
    public ProdConstraint(GenericNumConstant x, NumExpr y, NumExpr z, int constraintType) {
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
        
        // X * Y < z
        if (zexpr == null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                // z / Y > X
                a1 = new GenericNumProdReflexArc(constVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);
                
                // z / X > Y
                a2 = new GenericNumProdReflexArc(constVal, xexpr.getNode(), yexpr.getNode(), nodeType, arcType2);
            }
            else {
                // z / Y > X
                a1 = new GenericNumProdReflexArc(genConstVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);
                
                // z / X > Y
                a2 = new GenericNumProdReflexArc(genConstVal, xexpr.getNode(), yexpr.getNode(), nodeType, arcType2);
            }
            return new Arc[] { a1, a2 };
            
        }
        
        // X * y < Z
        else if (yexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                // Z / y > X
                a1 = new GenericNumProdReflexArc(zexpr.getNode(), constVal, xexpr.getNode(), nodeType, arcType2);
                
                // X * y < Z
                a2 = new GenericNumProdArc(xexpr.getNode(), constVal, zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // Z / y > X
                a1 = new GenericNumProdReflexArc(zexpr.getNode(), genConstVal, xexpr.getNode(), nodeType, arcType2);
                
                // X * y < Z
                a2 = new GenericNumProdArc(xexpr.getNode(), genConstVal, zexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // x * Y < Z
        else if (xexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                // Z / x > Y
                a1 = new GenericNumProdReflexArc(zexpr.getNode(), constVal, yexpr.getNode(), nodeType, arcType2);
                
                // x * Y < Z
                a2 = new GenericNumProdArc(constVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // Z / x > Y
                a1 = new GenericNumProdReflexArc(zexpr.getNode(), genConstVal, yexpr.getNode(), nodeType, arcType2);
                
                // x * Y < Z
                a2 = new GenericNumProdArc(genConstVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // X * Y < Z
        else {
            // X * Y < Z
            Arc a1 = new GenericNumProdArc(xexpr.getNode(), yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

            // Z / Y > X
            Arc a2 = new GenericNumProdReflexArc(zexpr.getNode(), yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);

            // Z / X > Y
            Arc a3 = new GenericNumProdReflexArc(zexpr.getNode(), xexpr.getNode(), yexpr.getNode(), nodeType, arcType2);
            
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
        
        // X * Y < z
        if (z == null) {
            // z / Y > X
            Arc a1 = new BinaryNumProdReflexArc(constVal, y, x, nodeType, arcType2);

            // z / X > Y
            Arc a2 = new BinaryNumProdReflexArc(constVal, x, y, nodeType, arcType2);
            
            return new Arc[] { a1, a2 };
        }
        
        // X * y < Z
        else if (y==null) {
            // Z / y > X
            Arc a1 = new BinaryNumProdReflexArc(z, constVal, x, nodeType, arcType2);

            // X * y < Z
            Arc a2 = new BinaryNumProdArc(x, constVal, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // x * Y < Z
        else if (x==null) {
            // Z / x > Y
            Arc a1 = new BinaryNumProdReflexArc(z, constVal, y, nodeType, arcType2);

            // x * Y < Z
            Arc a2 = new BinaryNumProdArc(constVal, y, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X * Y < Z
        else {
            // X * Y < Z
            Arc a1 = new TernaryNumProdArc(x, y, z, nodeType, arcType1);

            // Z / Y > X
            Arc a2 = new TernaryNumProdReflexArc(z, y, x, nodeType, arcType2);

            // Z / X > Y
            Arc a3 = new TernaryNumProdReflexArc(z, x, y, nodeType, arcType2);
            
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
                constraint= new ProdConstraint(gc, yexpr, zexpr, constraintType);
            else
                constraint= new ProdConstraint(numConst, yexpr, zexpr, constraintType);
        }
        else if (yexpr==null) {
            if (gc!=null)
                constraint= new ProdConstraint(xexpr, gc, zexpr, constraintType);
            else
                constraint= new ProdConstraint(xexpr, numConst, zexpr, constraintType);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new ProdConstraint(xexpr, yexpr, gc, constraintType);
            else
                constraint= new ProdConstraint(xexpr, yexpr, numConst, constraintType);
        }
        else
            constraint= new ProdConstraint(xexpr, yexpr, zexpr, constraintType);
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    public boolean violated() {
        Number largestMinX = getLargestMin(currentX, currentGx);
        Number smallestMaxX = getSmallestMax(currentX, currentGx);
        Number largestMinY = getLargestMin(currentY, currentGy);
        Number smallestMaxY = getSmallestMax(currentY, currentGy);
        NumberMath.multiplyNoInvalid(largestMinX, largestMinY, nodeType, v1);
        NumberMath.multiplyNoInvalid(largestMinX, smallestMaxY, nodeType, v2);
        NumberMath.multiplyNoInvalid(smallestMaxX, largestMinY, nodeType, v3);
        NumberMath.multiplyNoInvalid(smallestMaxX, smallestMaxY, nodeType, v4);
        
        double precision = getPrecision();
        
        switch (constraintType) {
            // X * Y < Z
            case ThreeVarConstraint.LT:
                NumberMath.min(v1, v2, v3, v4, min);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
                
            // X * Y <= Z
            case ThreeVarConstraint.LEQ:
                NumberMath.min(v1, v2, v3, v4, min);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
                
            // X * Y > Z
            case ThreeVarConstraint.GT:
                NumberMath.max(v1, v2, v3, v4, max);
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
                
            // X * Y >= Z
            case ThreeVarConstraint.GEQ:
                NumberMath.max(v1, v2, v3, v4, max);
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
            
            // X * Y != Z
            case ThreeVarConstraint.NEQ:
                boolean isXDetermined = ((xexpr==null || xexpr.isBound()) || (currentX!=null && currentX.isBound()) || (currentGx!=null && currentGx.isBound()));
            	boolean isYDetermined = ((yexpr==null || yexpr.isBound()) || (currentY!=null && currentY.isBound()) || (currentGy!=null && currentGy.isBound()));
            	boolean isZDetermined = ((zexpr==null || zexpr.isBound()) || (currentZ!=null && currentZ.isBound()) || (currentGz!=null && currentGz.isBound()));
            	boolean isXRelevant = !(yexpr!=null && yexpr.isBound() && (NumberMath.isZero(yexpr.getNumMax())));
            	boolean isYRelevant = !(xexpr != null && xexpr.isBound() && (NumberMath.isZero(xexpr.getNumMax())));
                if ( (isXDetermined || !isXRelevant) && (isYDetermined || !isYRelevant) && isZDetermined) 
           	    {
                    NumberMath.min(v1, v2, v3, v4, min);
                    return NumberMath.compare(min, getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
                }
                else
                    return false;

            // X * Y == Z
            default:
                NumberMath.min(v1, v2, v3, v4, min);
                NumberMath.max(v1, v2, v3, v4, max);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0	||
                       NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        }
   }
}