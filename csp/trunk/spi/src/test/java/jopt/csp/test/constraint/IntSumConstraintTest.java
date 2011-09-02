package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SumConstraint violation and propagation
 *
 * @author jboerkoel
 * @author Chris Johnson
 */
public class IntSumConstraintTest extends TestCase {

	private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private ConstraintStore store;

	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
	}

	protected void tearDown() {
		x1 = null;
		x2 = null;
		x3 = null;
		store = null;
	}

	public void testSumConstraintViolationLTVarVarVarViolate() {
		SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(99));
			x2.setDomainMin(new Integer(98));
			x3.setDomainMax(new Integer(3));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationGEQVarVarVarNoViolate() {
		SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(3));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMin(new Integer(6));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationGEQVarVarVarViolate() {
		SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(3));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMin(new Integer(7));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationEQVarVarVarViolate() {
	    SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(7));
			x2.setDomainMin(new Integer(3));
			x3.setDomainMax(new Integer(9));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationEQVarVarVarNoViolate() {
	    SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(7));
			x2.setDomainMin(new Integer(3));
			x3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationNEQVarVarVarViolate() {
	    SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(7));
			x2.setDomainValue(new Integer(3));
			x3.setDomainValue(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationNEQUnboundVarVarVarNoViolate() {
	    SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(7));
			x2.setDomainMin(new Integer(3));
			x3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}

	public void testSumConstraintViolationNEQBoundVarVarVarNoViolate() {
	    SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(7));
			x2.setDomainValue(new Integer(3));
			x3.setDomainValue(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}

	public void testNormalConstNormalConstAPISum() {
	    CspConstraint constraint = x1.add(x2).add(x3).add(7).eq(50);
	    try {
	        assertFalse("constraint is not true still", constraint.isTrue());
	        assertFalse("constraint is not false still", constraint.isFalse());
	        store.addConstraint(constraint);
	        assertEquals("x1Max should be 100 still",100, x1.getMax());
	        assertEquals("x2Max should be 100 still",100, x2.getMax());
	        assertEquals("x3Max should be 100 still",100, x3.getMax());
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        store.propagate();
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        x1.setDomainMin(new Integer(20));
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        store.propagate();
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 23 now",23, x2.getMax());
	        assertEquals("x3Max should be 23 now",23, x3.getMax());
	        x2.setValue(5);
	        x1.setValue(23);
	        store.propagate();
	        assertEquals("x1Min should be 23 still",23, x1.getMin());
	        assertEquals("x2Min should be 5 still",5, x2.getMin());
	        assertEquals("x3Min should be 15 still",15, x3.getMin());
	        assertEquals("x1Max should be 23 now",23, x1.getMax());
	        assertEquals("x2Max should be 5 now",5, x2.getMax());
	        assertEquals("x3Max should be 15 now",15, x3.getMax());
	        assertTrue("constraint should now be true", constraint.isTrue());
	        assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testNormalConstNormalConstInMiddleAPISum() {
	    CspConstraint constraint = x1.add(x2).add(7).add(x3).eq(50);
	    try {
	        assertFalse("constraint is not true still", constraint.isTrue());
	        assertFalse("constraint is not false still", constraint.isFalse());
	        store.addConstraint(constraint);
	        assertEquals("x1Max should be 100 still",100, x1.getMax());
	        assertEquals("x2Max should be 100 still",100, x2.getMax());
	        assertEquals("x3Max should be 100 still",100, x3.getMax());
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        store.propagate();
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        x1.setDomainMin(new Integer(20));
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        store.propagate();
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 23 now",23, x2.getMax());
	        assertEquals("x3Max should be 23 now",23, x3.getMax());
	        x2.setValue(5);
	        x1.setValue(23);
	        store.propagate();
	        assertEquals("x1Min should be 23 still",23, x1.getMin());
	        assertEquals("x2Min should be 5 still",5, x2.getMin());
	        assertEquals("x3Min should be 15 still",15, x3.getMin());
	        assertEquals("x1Max should be 23 now",23, x1.getMax());
	        assertEquals("x2Max should be 5 now",5, x2.getMax());
	        assertEquals("x3Max should be 15 now",15, x3.getMax());
	        assertTrue("x1Max should be bound", x1.isBound());
	        assertTrue("x2Max should be bound", x2.isBound());
	        assertTrue("x3Max should be bound", x3.isBound());
	        assertTrue("constraint should now be true", constraint.isTrue());
	        assertFalse("constraint should not be false", constraint.isFalse());

	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testNormalConstNormalConstInBeginAPISum() {
	    CspConstraint constraint = x1.add(7).add(x2).add(x3).eq(50);
	    try {
	        assertFalse("constraint is not true still", constraint.isTrue());
	        assertFalse("constraint is not false still", constraint.isFalse());
	        store.addConstraint(constraint);
	        assertEquals("x1Max should be 100 still",100, x1.getMax());
	        assertEquals("x2Max should be 100 still",100, x2.getMax());
	        assertEquals("x3Max should be 100 still",100, x3.getMax());
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        store.propagate();
	        assertEquals("x1Min should be 0 still",0, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        x1.setDomainMin(new Integer(20));
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 43 now",43, x2.getMax());
	        assertEquals("x3Max should be 43 now",43, x3.getMax());
	        store.propagate();
	        assertEquals("x1Min should be 20 still",20, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 43 now",43, x1.getMax());
	        assertEquals("x2Max should be 23 now",23, x2.getMax());
	        assertEquals("x3Max should be 23 now",23, x3.getMax());
	        x2.setValue(5);
	        x1.setValue(23);
	        store.propagate();
	        assertEquals("x1Min should be 23 still",23, x1.getMin());
	        assertEquals("x2Min should be 5 still",5, x2.getMin());
	        assertEquals("x3Min should be 15 still",15, x3.getMin());
	        assertEquals("x1Max should be 23 now",23, x1.getMax());
	        assertEquals("x2Max should be 5 now",5, x2.getMax());
	        assertEquals("x3Max should be 15 now",15, x3.getMax());
	        assertTrue("x1Max should be bound", x1.isBound());
	        assertTrue("x2Max should be bound", x2.isBound());
	        assertTrue("x3Max should be bound", x3.isBound());
	        assertTrue("constraint should now be true", constraint.isTrue());
	        assertFalse("constraint should not be false", constraint.isFalse());

	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithAllNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(-10).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is -13", -13, c.getMax());

	        c.setMin(-15);

	        solver.propagate();

	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is -15", -15, c.getMin());
	        assertEquals("max of c is -13", -13, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithSomeNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(10).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 7", 7, c.getMax());

	        c.setMin(5);

	        solver.propagate();

	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 7", 7, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithNegativesAndPositivesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(10).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 11", 11, c.getMax());

	        c.setMin(5);

	        solver.propagate();

	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 11", 11, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithAllNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -4, -2);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is -5", -5, c.getMax());

	        c.setMin(-8);

	        solver.propagate();

	        assertEquals("min of a is -5", -5, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -8", -8, c.getMin());
	        assertEquals("max of c is -5", -5, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithSomeNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -2", -2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 1", 1, c.getMax());

	        c.setMin(0);

	        solver.propagate();

	        assertEquals("min of a is -3", -3, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 1", 1, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationGTWithNegativesAndPositives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).gt(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of b is -2", -2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 5", 5, c.getMax());

	        c.setMin(0);

	        solver.propagate();

	        assertEquals("min of a is -3", -3, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of b is -1", -1, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 5", 5, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationLEQWithAllNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -4, -2);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).leq(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -10", -10, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());

	        c.setMax(-9);

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -5", -5, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -3", -3, b.getMax());
	        assertEquals("min of c is -10", -10, c.getMin());
	        assertEquals("max of c is -9", -9, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationEQWithSomeNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", 2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).eq(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 2", 2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -4", -4, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());

	        c.setMin(0);

	        solver.propagate();

	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 2", 2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintPropagationNEQWithNegativesAndPositives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);

	        solver.addConstraint(a.add(b).neq(c));

	        solver.propagate();

	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", 2, a.getMax());
	        assertEquals("min of b is -2", -2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());

	        a.setValue(-5);
	        b.setValue(-1);

	        solver.propagate();

	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        assertFalse("c does not contain -6", c.isInDomain(-6));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
