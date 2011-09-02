package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumQuotAlternateArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumQuotArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumQuotReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumQuotAlternateArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumQuotArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumQuotReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumQuotAlternateArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumQuotArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumQuotReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Constraint representing X / y < Z, X / Y > z, etc.
 */
public class QuotConstraint extends ThreeVarConstraint {
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private MutableNumber v3 = new MutableNumber();
    private MutableNumber v4 = new MutableNumber();
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
     
    public QuotConstraint(NumExpr x, NumExpr y, NumExpr z, int constraintType) {
        super(x, y, z, null, null, constraintType);
    }
    public QuotConstraint(NumExpr x, NumExpr y, Number z, int constraintType) {
        super(x, y, null, z, null, constraintType);
    }
    public QuotConstraint(NumExpr x, Number y, NumExpr z, int constraintType) {
        super(x, null, z, y, null, constraintType);
    }
    public QuotConstraint(Number x, NumExpr y, NumExpr z, int constraintType) {
        super(null, y, z, x, null, constraintType);
    }
    public QuotConstraint(NumExpr x, NumExpr y, GenericNumConstant z, int constraintType) {
        super(x, y, null, null, z, constraintType);
    }
    public QuotConstraint(NumExpr x, GenericNumConstant y, NumExpr z, int constraintType) {
        super(x, null, z, null, y, constraintType);
    }
    public QuotConstraint(GenericNumConstant x, NumExpr y, NumExpr z, int constraintType) {
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
        
        
        // X / Y < z
        if (zexpr == null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                //z * Y > X
                a1 = new GenericNumQuotReflexArc(constVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);

                // X / z < Y
                a2 = new GenericNumQuotAlternateArc(xexpr.getNode(), constVal, yexpr.getNode(), nodeType, arcType1);
            }
            else {
                //z * Y > X
                a1 = new GenericNumQuotReflexArc(genConstVal, yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);

                // X / z < Y
                a2 = new GenericNumQuotAlternateArc(xexpr.getNode(), genConstVal, yexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // X / y < Z
        else if (yexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                // Z * y > X
                a1 = new GenericNumQuotReflexArc(zexpr.getNode(), constVal, xexpr.getNode(), nodeType, arcType2);
                
                // X / y < Z
                a2 = new GenericNumQuotArc(xexpr.getNode(), constVal, zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // Z * y > X
                a1 = new GenericNumQuotReflexArc(zexpr.getNode(), genConstVal, xexpr.getNode(), nodeType, arcType2);
                
                // X / y < Z
                a2 = new GenericNumQuotArc(xexpr.getNode(), genConstVal, zexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // x / Y < Z
        else if (xexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if (constVal != null) {
                // x / Z < Y
                a1 = new GenericNumQuotAlternateArc(constVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
                
                // x / Y < Z
                a2 = new GenericNumQuotArc(constVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // x / Z < Y
                a1 = new GenericNumQuotAlternateArc(genConstVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
                
                // x / Y < Z
                a2 = new GenericNumQuotArc(genConstVal, yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            }
            return new Arc[] { a1, a2 };
        }
        
        // X / Y < Z
        else {
            // X / Y < Z
            Arc a1 = new GenericNumQuotArc(xexpr.getNode(), yexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

            // Z * Y > X
            Arc a2 = new GenericNumQuotReflexArc(zexpr.getNode(), yexpr.getNode(), xexpr.getNode(), nodeType, arcType2);

            // X / Z < Y
            Arc a3 = new GenericNumQuotAlternateArc(xexpr.getNode(), zexpr.getNode(), yexpr.getNode(), nodeType, arcType1);
            
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
        
        // X / Y < z
        if (z == null) {
            // z * Y > X
            Arc a1 = new BinaryNumQuotReflexArc(constVal, y, x, nodeType, arcType2);

            // X / z < Y
            Arc a2 = new BinaryNumQuotAlternateArc(x, constVal, y, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X / y < Z
        else if (y==null) {
            // Z * y > X
            Arc a1 = new BinaryNumQuotReflexArc(z, constVal, x, nodeType, arcType2);

            // X / y < Z
            Arc a2 = new BinaryNumQuotArc(x, constVal, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // x / Y < Z
        else if (x==null) {
            // x / Z < Y
            Arc a1 = new BinaryNumQuotAlternateArc(constVal, z, y, nodeType, arcType1);

            // x / Y < Z
            Arc a2 = new BinaryNumQuotArc(constVal, y, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X / Y < Z
        else {
            // X / Y < Z
            Arc a1 = new TernaryNumQuotArc(x, y, z, nodeType, arcType1);

            // Z * Y > X
            Arc a2 = new TernaryNumQuotReflexArc(z, y, x, nodeType, arcType2);

            // X / Z < Y
            Arc a3 = new TernaryNumQuotAlternateArc(x, z, y, nodeType, arcType1);
            
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
                constraint= new QuotConstraint(gc, yexpr, zexpr, constraintType);
            else
                constraint= new QuotConstraint(numConst, yexpr, zexpr, constraintType);
        }
        else if (yexpr==null) {
            if (gc!=null)
                constraint= new QuotConstraint(xexpr, gc, zexpr, constraintType);
            else
                constraint= new QuotConstraint(xexpr, numConst, zexpr, constraintType);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new QuotConstraint(xexpr, yexpr, gc, constraintType);
            else
                constraint= new QuotConstraint(xexpr, yexpr, numConst, constraintType);
        }
        else
            constraint= new QuotConstraint(xexpr, yexpr, zexpr, constraintType);
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    public boolean violated() {
        // calculate min and max values
        Number largestMinX = getLargestMin(currentX, currentGx);
        Number smallestMaxX = getSmallestMax(currentX, currentGx);
        Number largestMinY = getLargestMin(currentY, currentGy);
        Number smallestMaxY = getSmallestMax(currentY, currentGy);
        NumberMath.divideCeil(smallestMaxX, smallestMaxY, nodeType, v1);
        NumberMath.divideCeil(largestMinX, largestMinY, nodeType, v2);
        NumberMath.divideCeil(smallestMaxX, largestMinY, nodeType, v3);
        NumberMath.divideCeil(largestMinX, smallestMaxY, nodeType, v4);
        NumberMath.min(v1, v2, v3, v4, min);
        NumberMath.max(v1, v2, v3, v4, max);
        
        double precision = getPrecision();
        
        switch (constraintType) {
            // X / Y < Z
            case ThreeVarConstraint.LT:
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
                
            // X / Y <= Z
            case ThreeVarConstraint.LEQ:
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
                
            // X / Y > Z
            case ThreeVarConstraint.GT:
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
                
            // X / Y >= Z
            case ThreeVarConstraint.GEQ:
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
                
            // X / Y != Z
            case ThreeVarConstraint.NEQ:
                boolean isXDetermined = ((xexpr==null || xexpr.isBound()) || (currentX!=null && currentX.isBound()) || (currentGx!=null && currentGx.isBound()));
        		boolean isYDetermined = ((yexpr==null || yexpr.isBound()) || (currentY!=null && currentY.isBound()) || (currentGy!=null && currentGy.isBound()));
        		boolean isZDetermined = ((zexpr==null || zexpr.isBound()) || (currentZ!=null && currentZ.isBound()) || (currentGz!=null && currentGz.isBound()));
        		boolean isXRelevant = !(yexpr!=null && yexpr.isBound() && (NumberMath.isZero(yexpr.getNumMax())));
	            if ( (isXDetermined || !isXRelevant) && (isYDetermined) && isZDetermined) 
	       	    {
                    return NumberMath.compare(min, getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
                }
                else
                    return false;

            // X / Y == Z
            default:
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0 ||
                NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        }
    }
}