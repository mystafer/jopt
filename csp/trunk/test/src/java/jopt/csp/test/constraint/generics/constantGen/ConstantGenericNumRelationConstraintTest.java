/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint.generics.constantGen;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints with generic constants
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class ConstantGenericNumRelationConstraintTest extends TestCase {

    GenericIntConstant yiconst;
    GenericIntConstant yjconst;
    GenericIntConstant yijconst;
	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	GenericIntExpr xiexpr;
	GenericIntExpr ziexpr;
	ConstraintStore store;
	CspSolver solver;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
	IntVariable y;
	IntVariable z;
	
	public void setUp () {
	    solver = CspSolver.createSolver();
	    solver.setAutoPropagate(false);
		varFactory = solver.getVarFactory();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);

        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);

        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{1,2,3});
        yjconst = new GenericIntConstant("yjconst", new GenericIndex[]{idxJ}, new int[]{4,5,6});
        yijconst = new GenericIntConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new int[]{1,2,3,4,5,6,7,8,9});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
	}
	
	public void tearDown() {
	    yiconst = null;
	    yjconst = null;
	    yijconst = null;
		x1 = null;
		x2 = null;
		x3 = null;
		y1 = null;
		y2 = null;
		y3 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		xiexpr = null;
		ziexpr = null;
		store = null;
		solver = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}

	public void testConstantEqSameIndexNoViolate() {
	    try {
	        CspConstraint constraint = xiexpr.eq(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setValue(3);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantEqSameIndexViolate() {
	    try {
	        CspConstraint constraint = xiexpr.eq(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setMin(4);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}

	public void testConstantLeqSameIndexNoViolate() {
	    try {
	        CspConstraint constraint = xiexpr.leq(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setMax(3);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantLeqSameIndexViolate() {
	    try {
	        CspConstraint constraint = xiexpr.leq(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setMin(4);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantLeqDiffIndexNoViolate() {
	    try {
	        CspConstraint constraint = xiexpr.leq(yjconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setMax(4);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantLeqDiffIndexViolate() {
	    try {
	        CspConstraint constraint = xiexpr.leq(yjconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(1);
	        x2.setValue(2);
	        x3.setMin(5);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantGtSameIndexNoViolate() {
	    try {
	        CspConstraint constraint = xiexpr.gt(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(2);
	        x2.setValue(4);
	        x3.setMin(5);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantGtSameIndexViolate() {
	    try {
	        CspConstraint constraint = xiexpr.gt(yiconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(2);
	        x2.setValue(3);
	        x3.setMax(3);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantGtDiffIndexNoViolate() {
	    try {
	        CspConstraint constraint = xiexpr.gt(yjconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(7);
	        x2.setValue(8);
	        x3.setMin(7);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantGtDiffIndexViolate() {
	    try {
	        CspConstraint constraint = xiexpr.gt(yjconst);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x1.setValue(7);
	        x2.setValue(8);
	        x3.setMax(5);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
}
