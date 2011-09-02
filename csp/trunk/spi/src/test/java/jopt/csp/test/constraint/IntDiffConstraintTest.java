/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.DiffConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for DiffConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class IntDiffConstraintTest extends TestCase {

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
	
	public void tearDown() {
		x1 = null;
		x2 = null;
		x3 = null;
		store = null;
	}
	
	public void testDiffConstraintViolationGTVarVarVarViolate() {
		DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(5));
			x2.setDomainMin(new Integer(2));
			x3.setDomainMin(new Integer(4));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationLEQVarVarVarNoViolate() {
		DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(4));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMax(new Integer(6));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationLEQVarVarVarViolate() {
		DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(78));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMax(new Integer(5));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQVarVarVarViolate() {
	    DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(7));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMax(new Integer(3));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQVarVarVarNoViolate() {
	    DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testDiffConstraintViolationNEQVarVarVarViolate() {
	    DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(7));
			x2.setDomainValue(new Integer(3));
			x3.setDomainValue(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationNEQUnboundVarVarVarNoViolate() {
	    DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testDiffConstraintViolationNEQBoundVarVarVarNoViolate() {
	    DiffConstraint constraint = new DiffConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testNormalConstNormalConstAPIDiff() {
	    CspConstraint constraint = x1.subtract(x2).subtract(x3).subtract(7).eq(5);
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
		    assertEquals("x1Min should be 12 now",12, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x2Max should be 88 now",88, x2.getMax());
			assertEquals("x3Max should be 88 now",88, x3.getMax());
			x1.setDomainMax(new Integer(60));
		    assertEquals("x1Min should be 12 still",12, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 60 now",60, x1.getMax());
			assertEquals("x2Max should be 88 now",88, x2.getMax());
			assertEquals("x3Max should be 88 now",88, x3.getMax());
			store.propagate();
			assertEquals("x1Min should be 12 still",12, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 60 now",60, x1.getMax());
			assertEquals("x2Max should be 48 now",48, x2.getMax());
			assertEquals("x3Max should be 48 now",48, x3.getMax());
			x2.setValue(5);
			x1.setValue(23);
			store.propagate();
			assertEquals("x1Min should be 23 still",23, x1.getMin());
			assertEquals("x2Min should be 5 still",5, x2.getMin());
			assertEquals("x3Min should be 6 still",6, x3.getMin());
			assertEquals("x1Max should be 23 now",23, x1.getMax());
			assertEquals("x2Max should be 5 now",5, x2.getMax());
			assertEquals("x3Max should be 6 now",6, x3.getMax());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	public void testNormalConstNormalConstInMiddleAPIDiff() {
	    CspConstraint constraint = x1.subtract(x2).subtract(7).subtract(x3).eq(5);
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
	        assertEquals("x1Min should be 12 now",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 100 now",100, x1.getMax());
	        assertEquals("x2Max should be 88 now",88, x2.getMax());
	        assertEquals("x3Max should be 88 now",88, x3.getMax());
	        x1.setDomainMax(new Integer(60));
	        assertEquals("x1Min should be 12 still",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 60 now",60, x1.getMax());
	        assertEquals("x2Max should be 88 now",88, x2.getMax());
	        assertEquals("x3Max should be 88 now",88, x3.getMax());
	        store.propagate();
	        assertEquals("x1Min should be 12 still",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 60 now",60, x1.getMax());
	        assertEquals("x2Max should be 48 now",48, x2.getMax());
	        assertEquals("x3Max should be 48 now",48, x3.getMax());
	        x2.setValue(5);
	        x1.setValue(23);
	        store.propagate();
	        assertEquals("x1Min should be 23 still",23, x1.getMin());
	        assertEquals("x2Min should be 5 still",5, x2.getMin());
	        assertEquals("x3Min should be 6 still",6, x3.getMin());
	        assertEquals("x1Max should be 23 now",23, x1.getMax());
	        assertEquals("x2Max should be 5 now",5, x2.getMax());
	        assertEquals("x3Max should be 6 now",6, x3.getMax());
	        assertTrue("constraint should now be true", constraint.isTrue());
	        assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	public void testNormalConstNormalConstInBeginAPIDiff() {
	    CspConstraint constraint = x1.subtract(7).subtract(x2).subtract(x3).eq(5);
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
	        assertEquals("x1Min should be 12 now",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 100 now",100, x1.getMax());
	        assertEquals("x2Max should be 88 now",88, x2.getMax());
	        assertEquals("x3Max should be 88 now",88, x3.getMax());
	        x1.setDomainMax(new Integer(60));
	        assertEquals("x1Min should be 12 still",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 60 now",60, x1.getMax());
	        assertEquals("x2Max should be 88 now",88, x2.getMax());
	        assertEquals("x3Max should be 88 now",88, x3.getMax());
	        store.propagate();
	        assertEquals("x1Min should be 12 still",12, x1.getMin());
	        assertEquals("x2Min should be 0 still",0, x2.getMin());
	        assertEquals("x3Min should be 0 still",0, x3.getMin());
	        assertEquals("x1Max should be 60 now",60, x1.getMax());
	        assertEquals("x2Max should be 48 now",48, x2.getMax());
	        assertEquals("x3Max should be 48 now",48, x3.getMax());
	        x2.setValue(5);
	        x1.setValue(23);
	        store.propagate();
	        assertEquals("x1Min should be 23 still",23, x1.getMin());
	        assertEquals("x2Min should be 5 still",5, x2.getMin());
	        assertEquals("x3Min should be 6 still",6, x3.getMin());
	        assertEquals("x1Max should be 23 now",23, x1.getMax());
	        assertEquals("x2Max should be 5 now",5, x2.getMax());
	        assertEquals("x3Max should be 6 now",6, x3.getMax());
	        assertTrue("constraint should now be true", constraint.isTrue());
	        assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithAllNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(-10).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(8);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -3", -3, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 8", 8, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithSomeNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(10).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is -15", -15, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(-13);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -4", -4, a.getMax());
	        assertEquals("min of c is -15", -15, c.getMin());
	        assertEquals("max of c is -13", -13, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithNegativesAndPositivesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(10).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of c is -15", -15, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(-12);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -3", -3, a.getMax());
	        assertEquals("min of c is -15", -15, c.getMin());
	        assertEquals("max of c is -12", -12, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithAllNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -4, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -3", -3, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(0);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -3", -3, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -3", -3, c.getMin());
	        assertEquals("max of c is 0", 0, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithSomeNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -2", -2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -9", -9, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(-6);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -3", -3, a.getMax());
	        assertEquals("min of b is 1", 1, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -9", -9, c.getMin());
	        assertEquals("max of c is -6", -6, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationLTWithNegativesAndPositives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).lt(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of b is -2", -2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -9", -9, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(-5);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 0", 0, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -9", -9, c.getMin());
	        assertEquals("max of c is -5", -5, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationGEQWithAllNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", -4, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).geq(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	        
	        c.setMin(0);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is -4", -4, b.getMin());
	        assertEquals("max of b is -2", -2, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationEQWithSomeNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable b = new IntVariable("b", 2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).eq(c));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 2", 2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -10", -10, c.getMin());
	        assertEquals("max of c is -4", -4, c.getMax());
	        
	        c.setMin(-6);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -4", -4, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of b is 2", 2, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is -6", -6, c.getMin());
	        assertEquals("max of c is -4", -4, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testDiffConstraintPropagationNEQWithNegativesAndPositives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, 2);
	        IntVariable b = new IntVariable("b", -2, 4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(a.subtract(b).neq(c));
	        
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
	        assertFalse("c does not contain -4", c.isInDomain(-4));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
