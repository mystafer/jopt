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
 * Implementation of the Set<T>Constraints interface for creating constraints
 * from the API
 */
@SuppressWarnings("unchecked")
public class SetConstraints<T> implements CspSetConstraints<T> {
    // javadoc inherited from CspSet<T>Constraints
	public CspConstraint eqIntersection(CspSetVariable<T> x, CspSetVariable<T> y, CspSetVariable<T> z) {
        return new EqIntersection<T>((SetVariable<T>) x, (SetVariable<T>) y, (SetVariable<T>) z);
    }
    
    // javadoc inherited from CspSet<T>Constraints
	public CspConstraint eqIntersection(CspSetVariable<T> sources[], CspSetVariable<T> target) {
        return new EqIntersection<T>(toSetVarArray(sources), (SetVariable<T>) target);
    }
    
    // javadoc inherited from CspSet<T>Constraints
	public CspConstraint eqPartition(CspSetVariable<T> x, CspSetVariable<T> y, CspSetVariable<T> z) {
        return new EqPartition<T>((SetVariable<T>) x, (SetVariable<T>) y, (SetVariable<T>) z);
    }
    
    // javadoc inherited from CspSet<T>Constraints
	public CspConstraint eqPartition(CspSetVariable<T> sources[], CspSetVariable<T> target) {
        return new EqPartition<T>(toSetVarArray(sources), (SetVariable<T>) target);
    }
    
    // javadoc inherited from CspSet<T>Constraints
	public CspConstraint eqPartition(CspSetVariable<T> x, CspSetVariable<T> y, CspSetVariable<T> z, boolean advancedFilter) {
        return new EqPartition<T>((SetVariable<T>) x, (SetVariable<T>) y, (SetVariable<T>) z, advancedFilter);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqPartition(CspSetVariable<T> sources[], CspSetVariable<T> target, boolean advancedFilter) {
        return new EqPartition<T>(toSetVarArray(sources), (SetVariable<T>) target, advancedFilter);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqUnion(CspSetVariable<T> x, CspSetVariable<T> y, CspSetVariable<T> z) {
        return new EqUnion<T>((SetVariable<T>) x, (SetVariable<T>) y, (SetVariable<T>) z);
    }

    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqUnion(CspSetVariable<T> sources[], CspSetVariable<T> target) {
        return new EqUnion<T>(toSetVarArray(sources), (SetVariable<T>) target);
    }

    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqUnion(CspSetVariable<T> x, CspSetVariable<T> y, CspSetVariable<T> z, CspSetVariable<T> intersect) {
        return new EqUnion<T>((SetVariable<T>) x, (SetVariable<T>) y, (SetVariable<T>) z, (SetVariable<T>) intersect);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqUnion(CspSetVariable<T> sources[], CspSetVariable<T> target, boolean advancedFilter) {
        return new EqUnion<T>(toSetVarArray(sources), (SetVariable<T>) target, advancedFilter);
    }

    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint nullIntersection(CspSetVariable<T> sources[]) {
        return new NullIntersection<T>(toSetVarArray(sources));
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint nullIntersection(CspSetVariable<T> a, CspSetVariable<T> b) {
        return new NullIntersection<T>((SetVariable<T>) a, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint nullIntersection(CspSetVariable<T> a, Set<T> constb) {
        return new NullIntersection<T>((SetVariable<T>) a, constb);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint nullIntersection(Set<T> consta, CspSetVariable<T> b) {
        return new NullIntersection<T>(consta, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqSubset(CspSetVariable<T> a, CspSetVariable<T> b) {
        return new EqSubset<T>((SetVariable<T>) a, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqSubset(Set<T> a, CspSetVariable<T> b) {
        return new EqSubset<T>(a, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint eqSubset(CspSetVariable<T> a, Set<T> b) {
        return new EqSubset<T>((SetVariable<T>) a, b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint strictSubset(CspSetVariable<T> a, CspSetVariable<T> b) {
        return new StrictSubset<T>((SetVariable<T>) a, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint strictSubset(Set<T> a, CspSetVariable<T> b) {
        return new StrictSubset<T>(a, (SetVariable<T>) b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint strictSubset(CspSetVariable<T> a, Set<T> b) {
        return new StrictSubset<T>((SetVariable<T>) a, b);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint memberOfSet(CspIntSetVariable set, CspIntExpr expr) {
    	return new MemberOfSet((SetVariable<Number>) set, (NumExpr) expr);
    }
    
    // javadoc inherited from CspSet<T>Constraints
    public CspConstraint notMemberOfSet(CspIntSetVariable set, CspIntExpr expr) {
        return new NotMemberOfSet((SetVariable<Number>) set, (NumExpr) expr);
    }
    
    /**
     * Converts an array of CSP expressions to internal Set<T> variables
     */
	public static <T> SetVariable<T>[] toSetVarArray(CspSetVariable<T> variables[]) {
        SetVariable<T> varArray[] = new SetVariable[variables.length];
        for (int i=0; i<variables.length; i++)
            varArray[i] = (SetVariable<T>) variables[i];
        return varArray;
    }
    
}
