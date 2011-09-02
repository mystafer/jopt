package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.NumRelationConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints
 * 
 * @author Chris Johnson
 */
public class NumRelationConstraintTest extends TestCase {

	CspSolver solver;
	CspVariableFactory varFactory;
	IntVariable x;
	IntVariable y;
	IntVariable z;
	
	public void setUp () {
	    solver = CspSolver.createSolver();
	    solver.setAutoPropagate(false);
        x = new IntVariable("x1", 0, 100);
        y = new IntVariable("y1", 0, 100);
        z = new IntVariable("z1", 0, 100);
	}
	
	public void tearDown() {
		solver = null;
		varFactory = null;
		x = null;
		y = null;
		z = null;
	}
	
	public void testVarEQVarNoViolate() {
	    try {
	        CspConstraint constraint = x.eq(z);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setValue(1);
	        
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        assertEquals("z is 1", 1, z.getMax());
	        assertEquals("z is 1", 1, z.getMin());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarEQVarViolate() {
	    try {
	        CspConstraint constraint = x.eq(z);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setValue(1);
	        z.setMin(2);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarEQConstNoViolate() {
	    try {
	        CspConstraint constraint = x.eq(1);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setValue(1);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarEQConstViolate() {
	    try {
	        CspConstraint constraint = x.eq(2);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setValue(1);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarLEQVarNoViolate() {
	    try {
	        CspConstraint constraint = x.leq(z);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setMin(7);
	        
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
	        
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        assertEquals("min of Z is 7", 7, z.getMin());
	        assertEquals("max of Z is 100", 100, z.getMax());
	        
	        x.setValue(8);
	        
	        assertTrue(solver.propagate());
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        assertEquals("min of z is 8", 8, z.getMin());
	        assertEquals("max of z is 100", 100, z.getMax());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarLEQVarViolate() {
	    try {
	        CspConstraint constraint = x.leq(z);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setMin(7);
	        z.setMax(6);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarLEQConstNoViolate() {
	    try {
	        CspConstraint constraint = x.leq(1);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setMax(1);
	        
	        assertTrue("constraint is true", constraint.isTrue());
	        assertFalse("constraint is not false", constraint.isFalse());
	        
	        assertTrue(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testVarLEQConstViolate() {
	    try {
	        CspConstraint constraint = x.leq(2);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setMin(3);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstGEQVarViolate() {
	    try {
	        CspConstraint constraint = new NumRelationConstraint(new Integer(2), x, NumConstants.GEQ);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        x.setMin(3);
	        
	        assertFalse("constraint is not true", constraint.isTrue());
	        assertTrue("constraint is false", constraint.isFalse());
	        
	        assertFalse(solver.propagate());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testNumRelationConstraintPropagationGTConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        
	        solver.addConstraint(a.gt(-4));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -3", -3, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNumRelationConstraintPropagationGT() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.gt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is -3", -3, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNumRelationConstraintPropagationEQConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        
	        solver.addConstraint(a.eq(-3));
	        
	        solver.propagate();
	        
	        assertTrue("a is bound", a.isBound());
	        assertEquals("min of a is -3", -3, a.getMin());
	        assertEquals("max of a is -3", -3, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNumRelationConstraintPropagationNEQConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        
	        solver.addConstraint(a.neq(-3));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertFalse("a does not contain -3", a.isInDomain(-3));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNumRelationConstraintPropagationLEQ() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.leq(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of c is -6", -6, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNumRelationConstraintPropagationGEQExpr() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 10);
	        IntVariable b = new IntVariable("b", 5, 10);
	        IntVariable c = new IntVariable("c", 3, 5);
	        
	        solver.addConstraint(a.geq(b.add(c)));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 8", 8, a.getMin());
	        assertEquals("max of a is 10", 10, a.getMax());
	        assertEquals("min of b is 5", 5, b.getMin());
	        assertEquals("max of b is 7", 7, b.getMax());
	        assertEquals("min of c is 3", 3, c.getMin());
	        assertEquals("max of c is 5", 5, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
