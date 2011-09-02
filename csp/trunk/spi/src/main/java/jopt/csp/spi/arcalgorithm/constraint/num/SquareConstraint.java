package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumSquareArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumSquareReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSquareArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSquareReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeNumSquareArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeNumSquareReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Constraint representing a^2 = Z, A^2 = z, etc
 */
public class SquareConstraint extends TwoVarConstraint {
    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    private MutableNumber v3 = new MutableNumber();
    private MutableNumber min = new MutableNumber();
    private MutableNumber max = new MutableNumber();
    
    public SquareConstraint(NumExpr a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public SquareConstraint(NumExpr a, Number z, int constraintType) {
        super(a, z, constraintType);
    }
    public SquareConstraint(Number a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public SquareConstraint(NumExpr a, GenericNumConstant z, int constraintType) {
        super(a, z, constraintType);
    }
    public SquareConstraint(GenericNumConstant a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }    
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        if (gaexpr!=null || gzexpr!=null)
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
        
        // A^2 < z
        if (zexpr == null) {
            if (constVal!=null) {
                return new Arc[] {
                        // sqrt(z) > A
                        new GenericNumSquareReflexArc(constVal, aexpr.getNode(), nodeType, arcType2)
                };
            }
            else {
                return new Arc[] {
                        // sqrt(z) > A
                        new GenericNumSquareReflexArc(genConstVal, aexpr.getNode(), nodeType, arcType2)
                };    
            }
        }
        
        // a^2 < Z
        else if (aexpr==null) {
            if (constVal != null) {
                return new Arc[] {
                        // a^2 < Z
                        new GenericNumSquareArc(constVal, zexpr.getNode(), nodeType, arcType1)
                };
            }
            else {
                return new Arc[] {
                        // a^2 < Z
                        new GenericNumSquareArc(genConstVal, zexpr.getNode(), nodeType, arcType1)
                };
            }
        }
        
        // A^2 < Z
        else {
            // A^2 < Z
            Arc a1 = new GenericNumSquareArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);

            // sqrt(Z) > A
            Arc a2 = new GenericNumSquareReflexArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
            return new Arc[] { a1, a2 };
        }
    }

    /**
     * Creates standard numeric arcs
     */
    private Arc[] createStandardArcs() {
        NumNode a = (aexpr!=null) ? (NumNode) aexpr.getNode() : null;
        NumNode z = (zexpr!=null) ? (NumNode) zexpr.getNode() : null;
        
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // A^2 < z
        if (z == null) {
            return new Arc[] {
                // sqrt(z) > A
                new NodeNumSquareReflexArc(constVal, a, nodeType, arcType2)
            };
        }
        
        // a^2 < Z
        else if (a==null) {
            return new Arc[] {
                // a^2 < Z
                new NodeNumSquareArc(constVal, z, nodeType, arcType1)
            };
        }
        
        // A^2 < Z
        else {
            // A^2 < Z
            Arc a1 = new BinaryNumSquareArc(a, z, nodeType, arcType1);

            // sqrt(Z) > A
            Arc a2 = new BinaryNumSquareReflexArc(z, a, nodeType, arcType2);
            
            return new Arc[] { a1, a2 };
        }
    }
    
    // javadoc inherited from TwoVarConstraint
    protected AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numVal, GenericNumConstant genConst, int constraintType) {
        Number numConst = constVal;
        if (numVal!=null) numConst=numVal;
        AbstractConstraint constraint = null;
        if (aexpr==null) {
            if(genConst!=null)
                constraint= new SquareConstraint(genConst, zexpr, constraintType);
            else
                constraint= new SquareConstraint(numConst, zexpr, constraintType);
        }
        else if (zexpr==null) {
            if(genConst!=null)
                constraint= new SquareConstraint(aexpr, genConst, constraintType);
            else
                constraint= new SquareConstraint(aexpr, numConst, constraintType);
        }
        else {
            constraint= new SquareConstraint(aexpr, zexpr, constraintType);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from TwoVarConstraint
    public boolean violated() {
        Number largestMinA = getLargestMin(currentA, currentGa);
        Number smallestMaxA = getSmallestMax(currentA, currentGa);
        NumberMath.multiplyNoInvalid(largestMinA, largestMinA, nodeType, v1);
        NumberMath.multiplyNoInvalid(largestMinA, smallestMaxA, nodeType, v2);
        NumberMath.multiplyNoInvalid(smallestMaxA, smallestMaxA, nodeType, v3);
        
        double precision = getPrecision();
        
        switch (constraintType) {
            // X * Y < Z
            case ThreeVarConstraint.LT:
                NumberMath.min(v1, v2, v3, min);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
                
            // X * Y <= Z
            case ThreeVarConstraint.LEQ:
                NumberMath.min(v1, v2, v3, min);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
                
            // X * Y > Z
            case ThreeVarConstraint.GT:
                NumberMath.max(v1, v2, v3, max);
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
                
            // X * Y >= Z
            case ThreeVarConstraint.GEQ:
                NumberMath.max(v1, v2, v3, max);
                return NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
            
            // X * Y != Z
            case ThreeVarConstraint.NEQ:
                if ( ((aexpr==null || aexpr.isBound()) || (currentA!=null && currentA.isBound()) || (currentGa!=null && currentGa.isBound())) &&
                 	 ((zexpr==null || zexpr.isBound()) || (currentZ!=null && currentZ.isBound()) || (currentGz!=null && currentGz.isBound())) )
                {
                    NumberMath.min(v1, v2, v3, min);
                    return NumberMath.compare(min, getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
                }
                else
                    return false;

            // X * Y == Z
            default:
                NumberMath.min(v1, v2, v3, min);
                NumberMath.max(v1, v2, v3, max);
                return NumberMath.compare(min, getSmallestMax(currentZ, currentGz), precision, nodeType) > 0	||
                       NumberMath.compare(max, getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        }
    }
}