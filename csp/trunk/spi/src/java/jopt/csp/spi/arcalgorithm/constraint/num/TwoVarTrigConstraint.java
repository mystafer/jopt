package jopt.csp.spi.arcalgorithm.constraint.num;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.NumConstants;

/**
 * Base class for trig constraints such as Cos, Sin, etc.
 */
public abstract class TwoVarTrigConstraint extends TwoVarConstraint {
    
    public TwoVarTrigConstraint(NumExpr a, NumExpr z) {
        super(a, z, NumConstants.EQ);
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
    protected abstract Arc[] createGenericArcs();
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected abstract Arc[] createStandardArcs();
    
    // javadoc inherited from TwoVarConstraint
    protected final AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numVal, GenericNumConstant genConst, int constraintType) {
        AbstractConstraint constraint = createConstraintFragment(aexpr, zexpr);
        constraint.associateToGraph(graph);
        return constraint;
    }
    
    // javadoc inherited from TwoVarConstraint
    protected abstract TwoVarTrigConstraint createConstraintFragment(NumExpr aexpr, NumExpr zexpr);
    
    // javadoc inherited from TwoVarConstraint
    public final boolean violated() {
        throw new UnsupportedOperationException("violated operations are not supported on trig arcs");
    }
}