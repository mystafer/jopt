package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumAbsArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumAbsReflectArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumAbsArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumAbsReflectArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeNumAbsArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;

/**
 * Constraint representing |A| = Z and |a| = Z
 */
public class AbsConstraint extends TwoVarConstraint {
    MutableNumber minAbs = new MutableNumber();
    MutableNumber maxAbs = new MutableNumber();
    MutableNumber v1 = new MutableNumber();
    MutableNumber v2 = new MutableNumber();
    MutableNumber v3 = new MutableNumber();
    
    public AbsConstraint(NumExpr a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public AbsConstraint(Number a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public AbsConstraint(GenericNumConstant a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected final Arc[] createArcs() {
        if (gaexpr!=null || gzexpr!=null)
            return createGenericArcs();
        else
            return createStandardArcs();
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        if (aexpr == null) {
            if(constVal != null) {
                return new Arc[] {
                    new GenericNumAbsArc(constVal, zexpr.getNode(), nodeType, ThreeVarConstraint.equivalentArcType(constraintType))
                };
            }
            else {
                return new Arc[] {
                    new GenericNumAbsArc(genConstVal, zexpr.getNode(), nodeType, ThreeVarConstraint.equivalentArcType(constraintType))
                };
            }
        }
        else {
            // Create arcs
            Arc a1 = new GenericNumAbsArc(aexpr.getNode(), zexpr.getNode(), nodeType, ThreeVarConstraint.equivalentArcType(constraintType));
            Arc a2 = new GenericNumAbsReflectArc(zexpr.getNode(), aexpr.getNode(), nodeType, ThreeVarConstraint.oppositeArcType(constraintType));
            
            return new Arc[] { a1, a2 };
        }
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createStandardArcs() {
        NumNode a = (aexpr!=null) ? (NumNode) aexpr.getNode() : null;
        NumNode z = (zexpr!=null) ? (NumNode) zexpr.getNode() : null;
        
        if (a == null) {
            return new Arc[] {
                new NodeNumAbsArc(constVal, z, nodeType, ThreeVarConstraint.equivalentArcType(constraintType), true)
            };
        }
        else {
            // Create arcs
            Arc a1 = new BinaryNumAbsArc(a, z, nodeType, ThreeVarConstraint.equivalentArcType(constraintType));
            Arc a2 = new BinaryNumAbsReflectArc(z, a, nodeType, ThreeVarConstraint.oppositeArcType(constraintType));
            
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
                constraint= new AbsConstraint(genConst, zexpr, constraintType);
            else
                constraint= new AbsConstraint(numConst, zexpr, constraintType);
        }
        else {
            constraint= new AbsConstraint(aexpr, zexpr, constraintType);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from TwoVarConstraint
    public boolean violated() {
        // Z must have a positive value for constraint to be satisfied
        if (NumberMath.compareToZero(maxZ(), 0) < 0)
            return true;
        
        double precision = getPrecision();
        NumExpr a = aexpr;
        NumExpr z = zexpr;
        
        switch (constraintType) {
            // |A| < Z
            case LT:
                return NumberMath.compare(minA(), maxZ(), precision, nodeType) >= 0;
                
            // |A| <= Z
            case LEQ:
                return NumberMath.compare(minA(), maxZ(), precision, nodeType) > 0;
            
            // |A| > Z
            case GT:
                return NumberMath.compare(maxA(), minZ(), precision, nodeType) <= 0;
                
            // |A| >= Z
            case GEQ:
                return NumberMath.compare(maxA(), minZ(), precision, nodeType) < 0;
            
            // |A| != Z
            case NEQ:
                if (a==null) {
                    if (z.isBound()) {
                        NumberMath.abs(constVal, v1);
                        return z.isInDomain(v1);
                    }
                    return false;
                }
                else if (z==null) {
                    if (a.isBound()) {
                        if (a.isInDomain(constVal)) {
                            return true;
                        }
                        else {
                            NumberMath.neg(constVal, v1);
                            return a.isInDomain(v1);
                        }
                    }
                    return false;
                }
                else if (a.isBound()) {
                    NumberMath.abs(aexpr.getNumMin(), v1);
                    return z.isInDomain(v1);
                }
                else if (z.isBound()) {
                    if (a.isInDomain(zexpr.getNumMin())) return true;
                    NumberMath.neg(zexpr.getNumMin(), v1);
                    return a.isInDomain(v1);
                }
                else
                    return false;
            
            // |A| == Z
            default:
                if (a==null) {
                    if (z.isBound()) {
                        NumberMath.neg(zexpr.getNumMin(), v1);
                        return !z.isInDomain(v1);
                    }
                    return false;
                }
                if (z==null) {
                    if (a.isBound()) {
                        if (a.isInDomain(constVal)) return false;
                        NumberMath.neg(constVal, v1);
                        return !a.isInDomain(v1);
                    }
                    return false;
                }
                if (!a.isBound() || !z.isBound()) 
                	return false;
                
                NumberMath.abs(aexpr.getNumMin(), v1);
                return z.isInDomain(v1);
        }
    }
    
    /**
     * Returns min abs of node A
     */
    protected Number minA() {
        // TODO: null check on aexpr is repeated later ... what gives?
        if (aexpr==null) {
            NumberMath.abs(constVal, minAbs);
            return minAbs;
        }
        
        // Retrieve zero in same number type as a domain
        Number zero = NumberMath.zero(aexpr.getNumberType());
        
        // Determine minimum absolute value of a
        minAbs.set(zero);
        if (aexpr!=null) {
            if (!aexpr.isInDomain(zero)) {
                Number nh = aexpr.getNextHigher(zero);
                NumberMath.abs(nh, v1);
                
                Number nl = aexpr.getNextLower(zero);
                NumberMath.abs(nl, v2);
                NumberMath.min(v1, v2, minAbs);
            }
        }
        
        // determine smallest min based by looping through generic a
        else {
            for (int i=0; i<gaexpr.getExpressionCount(); i++) {
                NumExpr n = gaexpr.getNumExpression(i);
                if (!n.isInDomain(zero)) {
                    Number nh = aexpr.getNextHigher(zero);
                    NumberMath.abs(nh, v1);
                    
                    Number nl = aexpr.getNextLower(zero);
                    NumberMath.abs(nl, v2);
                    NumberMath.min(v1, v2, v3);
                    
                    // use calculated value if no smallest value located yet
                    if (NumberMath.isZero(minAbs)) 
                        minAbs.set(v3);
                    else
                        NumberMath.min(minAbs, v3, minAbs);
                }
                else {
                    minAbs.set(zero);
                    break;
                }
            }
        }
        
        return minAbs;
    }

    /**
     * Returns max abs of node A
     */
    protected Number maxA() {
        if (aexpr==null) {
            NumberMath.abs(constVal, maxAbs);
            return maxAbs;
        }
        
        // Determine maximum absolute value of a
        NumberMath.abs(aexpr.getNumMin(), v1);
        NumberMath.abs(aexpr.getNumMax(), v2);
        NumberMath.max(v1, v2, maxAbs);
        return maxAbs;
    }
}