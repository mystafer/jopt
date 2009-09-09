package jopt.csp.spi.arcalgorithm.variable;

import java.util.Set;

import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.set.EqIntersection;
import jopt.csp.spi.arcalgorithm.constraint.set.EqPartition;
import jopt.csp.spi.arcalgorithm.constraint.set.EqSubset;
import jopt.csp.spi.arcalgorithm.constraint.set.EqUnion;
import jopt.csp.spi.arcalgorithm.constraint.set.MemberOfSet;
import jopt.csp.spi.arcalgorithm.constraint.set.NotMemberOfSet;
import jopt.csp.spi.arcalgorithm.constraint.set.NullIntersection;
import jopt.csp.spi.arcalgorithm.constraint.set.SetVariable;
import jopt.csp.spi.arcalgorithm.constraint.set.StrictSubset;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntSetVariable;
import jopt.csp.variable.CspSetConstraints;
import jopt.csp.variable.CspSetVariable;

/**
 * Implementation of the SetConstraints interface for creating constraints
 * from the API
 */
public class SetConstraints implements CspSetConstraints {
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqIntersection(CspSetVariable x, CspSetVariable y, CspSetVariable z) {
        return new EqIntersection((SetVariable) x, (SetVariable) y, (SetVariable) z);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqIntersection(CspSetVariable sources[], CspSetVariable target) {
        return new EqIntersection(toSetVarArray(sources), (SetVariable) target);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqPartition(CspSetVariable x, CspSetVariable y, CspSetVariable z) {
        return new EqPartition((SetVariable) x, (SetVariable) y, (SetVariable) z);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqPartition(CspSetVariable sources[], CspSetVariable target) {
        return new EqPartition(toSetVarArray(sources), (SetVariable) target);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqPartition(CspSetVariable x, CspSetVariable y, CspSetVariable z, boolean advancedFilter) {
        return new EqPartition((SetVariable) x, (SetVariable) y, (SetVariable) z, advancedFilter);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqPartition(CspSetVariable sources[], CspSetVariable target, boolean advancedFilter) {
        return new EqPartition(toSetVarArray(sources), (SetVariable) target, advancedFilter);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqUnion(CspSetVariable x, CspSetVariable y, CspSetVariable z) {
        return new EqUnion((SetVariable) x, (SetVariable) y, (SetVariable) z);
    }

    // javadoc inherited from CspSetConstraints
    public CspConstraint eqUnion(CspSetVariable sources[], CspSetVariable target) {
        return new EqUnion(toSetVarArray(sources), (SetVariable) target);
    }

    // javadoc inherited from CspSetConstraints
    public CspConstraint eqUnion(CspSetVariable x, CspSetVariable y, CspSetVariable z, CspSetVariable intersect) {
        return new EqUnion((SetVariable) x, (SetVariable) y, (SetVariable) z, (SetVariable) intersect);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqUnion(CspSetVariable sources[], CspSetVariable target, boolean advancedFilter) {
        return new EqUnion(toSetVarArray(sources), (SetVariable) target, advancedFilter);
    }

    // javadoc inherited from CspSetConstraints
    public CspConstraint nullIntersection(CspSetVariable sources[]) {
        return new NullIntersection(toSetVarArray(sources));
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint nullIntersection(CspSetVariable a, CspSetVariable b) {
        return new NullIntersection((SetVariable) a, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint nullIntersection(CspSetVariable a, Set constb) {
        return new NullIntersection((SetVariable) a, constb);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint nullIntersection(Set consta, CspSetVariable b) {
        return new NullIntersection(consta, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqSubset(CspSetVariable a, CspSetVariable b) {
        return new EqSubset((SetVariable) a, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqSubset(Set a, CspSetVariable b) {
        return new EqSubset(a, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint eqSubset(CspSetVariable a, Set b) {
        return new EqSubset((SetVariable) a, b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint strictSubset(CspSetVariable a, CspSetVariable b) {
        return new StrictSubset((SetVariable) a, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint strictSubset(Set a, CspSetVariable b) {
        return new StrictSubset(a, (SetVariable) b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint strictSubset(CspSetVariable a, Set b) {
        return new StrictSubset((SetVariable) a, b);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint memberOfSet(CspIntSetVariable set, CspIntExpr expr) {
    	return new MemberOfSet((SetVariable) set, (NumExpr) expr);
    }
    
    // javadoc inherited from CspSetConstraints
    public CspConstraint notMemberOfSet(CspIntSetVariable set, CspIntExpr expr) {
        return new NotMemberOfSet((SetVariable) set, (NumExpr) expr);
    }
    
    /**
     * Converts an array of CSP expressions to internal set variables
     */
    public static SetVariable[] toSetVarArray(CspSetVariable variables[]) {
        SetVariable varArray[] = new SetVariable[variables.length];
        for (int i=0; i<variables.length; i++)
            varArray[i] = (SetVariable) variables[i];
        return varArray;
    }
    
}
