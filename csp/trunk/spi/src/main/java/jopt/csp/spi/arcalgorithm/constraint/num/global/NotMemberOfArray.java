package jopt.csp.spi.arcalgorithm.constraint.num.global;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.TwoVarConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GenericNumNotMemberOfArrayReflexArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperNumNotMemberOfArrayArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;

/**
 * Constraint representing an expression is a member of a set
 */
public class NotMemberOfArray extends TwoVarConstraint {
    private NumExpr sourceExprs[];
    
    public NotMemberOfArray(NumExpr sources[], NumExpr expr) {
        super((Number) null, expr, EQ);

        if (expr instanceof GenericNumExpr)
            throw new IllegalArgumentException("generic expressions not supported by not-member-of constraint");
        
        for (int i=0; i<sources.length; i++)
            if (sources[i] instanceof GenericNumExpr)
                throw new IllegalArgumentException("generic expressions not supported by not-member-of constraint");
            
        this.sourceExprs = sources;
    }
    
    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        NumNode target = (NumNode) zexpr.getNode();
        
        // create source node array
        NumNode sources[] = new NumNode[sourceExprs.length];
        for (int i=0; i<sources.length; i++)
            sources[i] = (NumNode) sourceExprs[i].getNode();
        
        // create binary arcs between nodes
        return new Arc[]{
            new HyperNumNotMemberOfArrayArc(sources, target),
            new GenericNumNotMemberOfArrayReflexArc(target, sources)
        };
    }
    
    // javadoc inherited from TwoVarConstraint
    protected AbstractConstraint createConstraint(NumExpr aexpr, NumExpr zexpr, Number numConst, GenericNumConstant gc, int constraintType) {
        return null;
    }
    
    // javadoc inherited from TwoVarConstraint
    public boolean violated() {
        return false;
    }
}