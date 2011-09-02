package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumLogArc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinaryNumPowerArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumLogArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumPowerArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumLogArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumPowerArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.NumConstants;

/**
 * Constraint representing x^Y = Z, X^Y = z, etc
 */
public class PowerConstraint extends ThreeVarConstraint {
    
    public PowerConstraint(NumExpr x, NumExpr y, NumExpr z) {
        super(x, y, z, null, null, NumConstants.EQ);
    }
    public PowerConstraint(NumExpr x, NumExpr y, Number z) {
        super(x, y, null, z, null, NumConstants.EQ);
    }
    public PowerConstraint(NumExpr x, Number y, NumExpr z) {
        super(x, null, z, y, null, NumConstants.EQ);
    }
    public PowerConstraint(Number x, NumExpr y, NumExpr z) {
        super(null, y, z, x, null, NumConstants.EQ);
    }
    public PowerConstraint(NumExpr x, NumExpr y, GenericNumConstant z) {
        super(x, y, null, null, z, NumConstants.EQ);
    }
    public PowerConstraint(NumExpr x, GenericNumConstant y, NumExpr z) {
        super(x, null, z, null, y, NumConstants.EQ);
    }
    public PowerConstraint(GenericNumConstant x, NumExpr y, NumExpr z) {
        super(null, y, z, null, x, NumConstants.EQ);
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected final Arc[] createArcs() {
        if (gxexpr!=null || gyexpr!=null || gzexpr!=null)
            return createGenericArcs();
        else
            return createStandardArcs();
    }
    
    /**
     * Creates generic numeric arcs
     */
    protected Arc[] createGenericArcs() {
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // X^Y < z
        if (zexpr == null) {
            Arc a1 = null;
            Arc a2 = null;
            if(constVal != null) {
                // z^(1/Y) > X
                a1 = new GenericNumPowerArc(constVal, yexpr.getNode(), true, xexpr.getNode(), nodeType, arcType2);

                // logX(z) > Y
                a2 = new GenericNumLogArc(xexpr.getNode(), constVal, yexpr.getNode(), nodeType, arcType2);
            }
            else {
                // z^(1/Y) > X
                a1 = new GenericNumPowerArc(genConstVal, yexpr.getNode(), true, xexpr.getNode(), nodeType, arcType2);

                // logX(z) > Y
                a2 = new GenericNumLogArc(xexpr.getNode(), genConstVal, yexpr.getNode(), nodeType, arcType2);
            }                
            return new Arc[] { a1, a2 };
        }
        
        // X^y < Z
        else if (yexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if(constVal != null) {
                // Z^(1/y) > X
                a1 = new GenericNumPowerArc(zexpr.getNode(), constVal, true, xexpr.getNode(), nodeType, arcType2);

                // X^y < Z
                a2 = new GenericNumPowerArc(xexpr.getNode(), constVal, false, zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // Z^(1/y) > X
                a1 = new GenericNumPowerArc(zexpr.getNode(), genConstVal, true, xexpr.getNode(), nodeType, arcType2);

                // X^y < Z
                a2 = new GenericNumPowerArc(xexpr.getNode(), genConstVal, false, zexpr.getNode(), nodeType, arcType1);
            }
            
            return new Arc[] { a1, a2 };
        }
        
        // x^Y < Z
        else if (xexpr==null) {
            Arc a1 = null;
            Arc a2 = null;
            if(constVal != null) {
                // logx(Z) > Y
                a1 = new GenericNumLogArc(constVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType2);

                // x^Y < Z
                a2 = new GenericNumPowerArc(constVal, yexpr.getNode(), false, zexpr.getNode(), nodeType, arcType1);
            }
            else {
                // logx(Z) > Y
                a1 = new GenericNumLogArc(genConstVal, zexpr.getNode(), yexpr.getNode(), nodeType, arcType2);

                // x^Y < Z
                a2 = new GenericNumPowerArc(genConstVal, yexpr.getNode(), false, zexpr.getNode(), nodeType, arcType1);
            }
            
            return new Arc[] { a1, a2 };
        }
        
        // X^Y < Z
        else {
            // X^Y < Z
            Arc a1 = new GenericNumPowerArc(xexpr.getNode(), yexpr.getNode(), false, zexpr.getNode(), nodeType, arcType1);

            // Z^(1/Y) > X
            Arc a2 = new GenericNumPowerArc(zexpr.getNode(), yexpr.getNode(), true, xexpr.getNode(), nodeType, arcType2);

            // logX(Z) > Y
            Arc a3 = new GenericNumLogArc(xexpr.getNode(), zexpr.getNode(), yexpr.getNode(), nodeType, arcType2);
            
            return new Arc[] { a1, a2, a3 };
        }
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createStandardArcs() {
        NumNode x = (xexpr!=null) ? (NumNode) xexpr.getNode() : null;
        NumNode y = (yexpr!=null) ? (NumNode) yexpr.getNode() : null;
        NumNode z = (zexpr!=null) ? (NumNode) zexpr.getNode() : null;
        
        // Determine arc types
        int arcType1 = equivalentArcType();
        int arcType2 = oppositeArcType();
        
        // X^Y < z
        if (z == null) {
            // z^(1/Y) > X
            Arc a1 = new BinaryNumPowerArc(constVal, y, true, x, nodeType, arcType2);

            // logX(z) > Y
            Arc a2 = new BinaryNumLogArc(x, constVal, y, nodeType, arcType2);
            
            return new Arc[] { a1, a2 };
        }
        
        // X^y < Z
        else if (y==null) {
            // Z^(1/y) > X
            Arc a1 = new BinaryNumPowerArc(z, constVal, true, x, nodeType, arcType2);

            // X^y < Z
            Arc a2 = new BinaryNumPowerArc(x, constVal, false, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // x^Y < Z
        else if (x==null) {
            // logx(Z) > Y
            Arc a1 = new BinaryNumLogArc(constVal, z, y, nodeType, arcType2);

            // x^Y < Z
            Arc a2 = new BinaryNumPowerArc(constVal, y, false, z, nodeType, arcType1);
            
            return new Arc[] { a1, a2 };
        }
        
        // X^Y < Z
        else {
            // X^Y < Z
            Arc a1 = new TernaryNumPowerArc(x, y, false, z, nodeType, arcType1);

            // Z^(1/Y) > X
            Arc a2 = new TernaryNumPowerArc(z, y, true, x, nodeType, arcType2);

            // logX(Z) > Y
            Arc a3 = new TernaryNumLogArc(x, z, y, nodeType, arcType2);
            
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
                constraint= new PowerConstraint(gc, yexpr, zexpr);
            else
                constraint= new PowerConstraint(numConst, yexpr, zexpr);
        }
        else if (yexpr==null) {
            if (gc!=null)
                constraint= new PowerConstraint(xexpr, gc, zexpr);
            else
                constraint= new PowerConstraint(xexpr, numConst, zexpr);
        }
        else if (zexpr==null) {
            if (gc!=null)
                constraint= new PowerConstraint(xexpr, yexpr, gc);
            else
                constraint= new PowerConstraint(xexpr, yexpr, numConst);
        }
        else {
            constraint= new PowerConstraint(xexpr, yexpr, zexpr);
        }
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from ThreeVarConstraint
    public boolean violated() {
        throw new UnsupportedOperationException("violated operations are not supported on trig arcs");
    }
}