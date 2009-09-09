package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumRelationArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumRelationArc;
import jopt.csp.spi.arcalgorithm.graph.arc.node.NodeNumArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspGenericNumConstant;

/**
 * Constraint representing A = Z and a = Z and A = z
 */
public class NumRelationConstraint extends TwoVarConstraint {
    public NumRelationConstraint(NumExpr a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public NumRelationConstraint(Number a, NumExpr z, int constraintType) {
        super(a, z, constraintType);
    }
    public NumRelationConstraint(CspGenericNumConstant a, NumExpr z, int constraintType) {
        super((GenericNumConstant)a, z, constraintType);
    }
    public NumRelationConstraint(NumExpr a, Number z, int constraintType) {
        super(a, z, constraintType);
    }
    public NumRelationConstraint(NumExpr a, CspGenericNumConstant z, int constraintType) {
        super(a, (GenericNumConstant)z, constraintType);
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
        
        if (zexpr==null) {
            if (constVal!=null) {
                return new Arc[] {
                    new GenericNumRelationArc(constVal, aexpr.getNode(), nodeType, arcType2)
                };
            }
            else {
                return new Arc[] {
                    new GenericNumRelationArc(genConstVal, aexpr.getNode(), nodeType, arcType2)
                };
            }
        }
        
        else if (aexpr==null) {
            if (constVal!=null) {
                return new Arc[] {
                    new GenericNumRelationArc(constVal, zexpr.getNode(), nodeType, arcType1)
                };
            }
            else {
                return new Arc[] {
                    new GenericNumRelationArc(genConstVal, zexpr.getNode(), nodeType, arcType1)
                };
            }
        }
        
        else {
            Arc a1 = new GenericNumRelationArc(aexpr.getNode(), zexpr.getNode(), nodeType, arcType1);
            Arc a2 = new GenericNumRelationArc(zexpr.getNode(), aexpr.getNode(), nodeType, arcType2);
            
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
        
        // node arc
        if (z == null) {
            return new Arc[] {
                new NodeNumArc(constVal, a, nodeType, arcType2)
            };
        }
        if (a == null) {
            return new Arc[] {
                new NodeNumArc(constVal, z, nodeType, arcType1)
            };
        }
        
        // binary arc
        else {
            Arc a1 = new BinaryNumRelationArc(a, z, nodeType, arcType1);
            Arc a2 = new BinaryNumRelationArc(z, a, nodeType, arcType2);
            
            return new Arc[] { a1, a2 };
        }
    }
    
    // javadoc inherited from TwoVarConstraint
    protected AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numVal, GenericNumConstant gc, int constraintType) {
        Number numConst = constVal;
        if (numVal!=null) numConst=numVal;
        AbstractConstraint constraint = null;
        if (aexpr==null) {
            if (gc!=null)
                constraint= new NumRelationConstraint(gc, zexpr, constraintType);
            else
                constraint= new NumRelationConstraint(numConst, zexpr, constraintType);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new NumRelationConstraint(aexpr, gc, constraintType);
            else
                constraint= new NumRelationConstraint(aexpr, numConst, constraintType);
        }
        else {
            constraint= new NumRelationConstraint(aexpr, zexpr, constraintType);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from TwoVarConstraint
    public boolean violated() {
        double precision = getPrecision();
        
        switch (constraintType) {
            // A < Z
            case ThreeVarConstraint.LT:
                return NumberMath.compare(getLargestMin(currentA, currentGa),
                        				  getSmallestMax(currentZ, currentGz), precision, nodeType) >= 0;
                
            // A <= Z
            case ThreeVarConstraint.LEQ:
                return NumberMath.compare(getLargestMin(currentA, currentGa),
                        				  getSmallestMax(currentZ, currentGz), precision, nodeType) > 0;
                
            // A > Z
            case ThreeVarConstraint.GT:
                return NumberMath.compare(getSmallestMax(currentA, currentGa),
                        				  getLargestMin(currentZ, currentGz), precision, nodeType) <= 0;
                
            // A >= Z
            case ThreeVarConstraint.GEQ:
                return NumberMath.compare(getSmallestMax(currentA, currentGa), 
                        				  getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
                
            // A != Z
            case ThreeVarConstraint.NEQ:
                if ( ((aexpr==null || aexpr.isBound()) || (currentA!=null && currentA.isBound()) || (currentGa!=null && currentGa.isBound())) &&
                 	 ((zexpr==null || zexpr.isBound()) || (currentZ!=null && currentZ.isBound()) || (currentGz!=null && currentGz.isBound())) )
                {
                    //note that because the variables involved are bound,
        	        // min = max, so it should make no difference which is used
                    return NumberMath.compare(getSmallestMax(currentA, currentGa),
                            				  getLargestMin(currentZ, currentGz), precision, nodeType) == 0;
                }
                else
                    return false;
                
            // A == Z
            default:
                return NumberMath.compare(getLargestMin(currentA, currentGa),
                        				  getSmallestMax(currentZ, currentGz), precision, nodeType) > 0	||
                       NumberMath.compare(getSmallestMax(currentA, currentGa),
                               			  getLargestMin(currentZ, currentGz), precision, nodeType) < 0;
        }
    }

    
    /**
     * Returns string representation of arc
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (zexpr!=null) {
            buf.append("Z(");
            buf.append(zexpr);
        }
        else {
            buf.append("z(");
            buf.append(constVal);
        }
        buf.append(") ");

        switch(constraintType) {
            case GEQ:
                buf.append(">=");
                break;

            case GT:
                buf.append(">");
                break;

            case LEQ:
                buf.append("<=");
                break;

            case LT:
                buf.append("<");
                break;

            case EQ:
                buf.append("=");
                break;

            case NEQ:
                buf.append("!=");
        }

        if (aexpr!=null) {
            buf.append(" A(");
            buf.append(aexpr);
        }
        else {
            buf.append(" A(");
            buf.append(constVal);
        }
        buf.append(")");

        return buf.toString();
    }
}