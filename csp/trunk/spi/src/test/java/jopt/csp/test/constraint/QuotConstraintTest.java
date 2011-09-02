/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.QuotConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for QuotConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class QuotConstraintTest extends TestCase {

	IntVariable a;
	IntVariable b;
	IntVariable c;
	IntVariable d;
    IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	ConstraintStore store;
	CspVariableFactory varFactory;
	IntVariable y;
	IntVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
        a = new IntVariable("a", -100,100);
        b = new IntVariable("b", -100,100);
        c = new IntVariable("c", -100,100);
        d = new IntVariable("d", -100,100);
        
		x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
	}
	
	public void tearDown() {
		a = null;
		b = null;
		c = null;
		d = null;
	    x1 = null;
		x2 = null;
		x3 = null;
		y1 = null;
		y2 = null;
		y3 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		store = null;
		varFactory = null;
		y = null;
		z = null;
	}
	
	public void testQuotConstraintViolationGTVarVarVarViolate() {
		QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.GT);
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
	
	public void testQuotConstraintViolationLTEQVarVarVarNoViolate() {
		QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.LEQ);
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
	
	public void testQuotConstraintViolationLTEQVarVarVarViolate() {
		QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.LEQ);
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
	
	public void testQuotConstraintViolationEQVarVarVarViolate() {
	    QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(70));
			x2.setDomainMin(new Integer(5));
			x3.setDomainMin(new Integer(21));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQVarVarVarNoViolate() {
	    QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(70));
			x2.setDomainMax(new Integer(5));
			x3.setDomainMax(new Integer(14));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQVarVarVarViolate() {
	    QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(90));
			x2.setDomainValue(new Integer(10));
			x3.setDomainValue(new Integer(9));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQUnboundVarVarVarNoViolate() {
	    QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQBoundVarVarVarNoViolate() {
	    QuotConstraint constraint = new QuotConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(90));
			x2.setDomainValue(new Integer(10));
			x3.setDomainValue(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testNormalConstNormalConstAPIQuot() {
	    CspConstraint constraint = x1.divide(x2).divide(x3).divide(3).eq(4);
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
		    assertEquals("x1Min should be 0 now",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x2Max should be 100 now",100, x2.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x2.setDomainMax(new Integer(60));
		    assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			store.propagate();
			assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x1.setValue(96);
			x2.setValue(2);
			
			store.propagate();
			assertEquals("x1Min should be 96 still",96, x1.getMin());
			assertEquals("x2Min should be 2 still",2, x2.getMin());
			assertEquals("x3Min should be 4 still",4, x3.getMin());
			assertEquals("x1Max should be 96 now",96, x1.getMax());
			assertEquals("x2Max should be 2 now",2, x2.getMax());
			assertEquals("x3Max should be 4 now",4, x3.getMax());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNormalConstNormalConstInMiddleAPIQuot() {
	    CspConstraint constraint = x1.divide(x2).divide(3).divide(x3).eq(4);
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
		    assertEquals("x1Min should be 0 now",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x2Max should be 100 now",100, x2.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x2.setDomainMax(new Integer(60));
		    assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			store.propagate();
			assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 100 now",100, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x1.setValue(96);
			x2.setValue(2);
			
			store.propagate();
			assertEquals("x1Min should be 96 still",96, x1.getMin());
			assertEquals("x2Min should be 2 still",2, x2.getMin());
			assertEquals("x3Min should be 4 still",4, x3.getMin());
			assertEquals("x1Max should be 96 now",96, x1.getMax());
			assertEquals("x2Max should be 2 now",2, x2.getMax());
			assertEquals("x3Max should be 4 now",4, x3.getMax());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testNormalConstNormalConstInBeginAPIQuot() {
	    CspConstraint constraint = x1.divide(3).divide(x3).divide(x2).eq(4);
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
		    assertEquals("x1Min should be 0 now",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x1Max should be 99 now",99, x1.getMax());
			assertEquals("x2Max should be 100 now",100, x2.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x2.setDomainMax(new Integer(60));
		    assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 99 now",99, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			store.propagate();
			assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
			assertEquals("x2Max should be 60 now",60, x2.getMax());
			assertEquals("x1Max should be 99 now",99, x1.getMax());
			assertEquals("x3Max should be 100 now",100, x3.getMax());
			x1.setValue(96);
			x2.setValue(2);
			
			store.propagate();
			assertEquals("x1Min should be 96 still",96, x1.getMin());
			assertEquals("x2Min should be 2 still",2, x2.getMin());
			assertEquals("x3Min should be 4 still",4, x3.getMin());
			assertEquals("x1Max should be 96 now",96, x1.getMax());
			assertEquals("x2Max should be 2 now",2, x2.getMax());
			assertEquals("x3Max should be 4 now",4, x3.getMax());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationEQWithPositivesAndNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -2, 2);
            IntVariable c = new IntVariable("c", 2, 10);
            
            solver.addConstraint(a.divide(b).eq(c));
            
            solver.propagate();
            
            assertEquals("min of a is -20", -20, a.getMin());
            assertEquals("max of a is 20", 20, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationEQWithPositivesAndNegativesIncludingZero() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -4, 6);
            IntVariable c = new IntVariable("c", -2, 5);
            
            solver.addConstraint(a.divide(b).eq(c));
            
            solver.propagate();
            
            assertEquals("min of a is -20", -20, a.getMin());
            assertEquals("max of a is 30", 30, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationLTWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, -2);
            IntVariable c = new IntVariable("c", 3, 10);
            
            solver.addConstraint(a.divide(b).lt(c));
            
            solver.propagate();
            
            assertEquals("min of a is -49", -49, a.getMin());
            assertEquals("max of a is 100", 100, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationLEQWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, -2);
            IntVariable c = new IntVariable("c", 3, 10);
            
            solver.addConstraint(a.divide(b).leq(c));
            
            solver.propagate();
            
            assertEquals("min of a is -50", -50, a.getMin());
            assertEquals("max of a is 100", 100, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationGTWithNonZeroPossibilities() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, 2);
            IntVariable c = new IntVariable("c", 3, 10);
            
            solver.addConstraint(a.divide(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of a is -100", -100, a.getMin());
            assertEquals("max of a is 100", 100, a.getMax());
            assertFalse("a should not contain 0", a.isInDomain(0));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationGEQWithNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, -2);
            
            solver.addConstraint(a.divide(b).geq(10));
            
            solver.propagate();
            
            assertEquals("min of a is -100", -100, a.getMin());
            assertEquals("max of a is -20", -20, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationGTWithNegativesConst() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, -2);
            
            solver.addConstraint(a.divide(b).gt(10));
            
            solver.propagate();
            
            assertEquals("min of a is -100", -100, a.getMin());
            assertEquals("max of a is -21", -21, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationGTWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -100, 100);
            IntVariable b = new IntVariable("b", -5, -2);
            IntVariable c = new IntVariable("c", 10, 20);
            
            solver.addConstraint(a.divide(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of a is -100", -100, a.getMin());
            assertEquals("max of a is -21", -21, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testQuotConstraintPropagationWithNegatives() {
	    CspConstraint constraint = new QuotConstraint(a,b,c,NumConstants.LT);
	    try {
	        store.addConstraint(constraint);
	        store.propagate();
	        
	        //since b contains 0, there should be nothing that occurs
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. 100", -100, b.getMin());
	        assertEquals("b should be -100 .. 100", 100, b.getMax());
	        assertEquals("c should be -100 .. 100", -100, c.getMin());
	        assertEquals("c should be -100 .. 100", 100, c.getMax());
	        b.removeValue(0);
	        a.removeValue(0);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. 100", -100, b.getMin());
	        assertEquals("b should be -100 .. 100", 100, b.getMax());
	        assertEquals("c should be -100 .. 100", -100, c.getMin());
	        assertEquals("c should be -100 .. 100", 100, c.getMax());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	public void testQuotConstraintPropagationWithZero() {
	    CspConstraint constraint = new QuotConstraint(a,b,c,NumConstants.LT);
	    try {
	        store.addConstraint(constraint);
	        store.propagate();
	        //since b contains 0, there should be nothing that occurs
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. 100", -100, b.getMin());
	        assertEquals("b should be -100 .. 100", 100, b.getMax());
	        assertEquals("c should be -100 .. 100", -100, c.getMin());
	        assertEquals("c should be -100 .. 100", 100, c.getMax());
	        a.setValue(0);
	        store.propagate();
	        assertEquals("a should be 0", 0, a.getMin());
	        assertEquals("a should be 0", 0, a.getMax());
	        assertEquals("c should be 1 .. 100", 1, c.getMin());
	        assertEquals("c should be 1 .. 100", 100, c.getMax());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	public void testQuotConstraintPropagationWithZeroConst() {
	    CspConstraint constraint = a.divide(10).lt(c);
	    try {
	        store.addConstraint(constraint);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. 100", -100, b.getMin());
	        assertEquals("b should be -100 .. 100", 100, b.getMax());
	        assertEquals("c should be -9 .. 100", -9, c.getMin());
	        assertEquals("c should be -9 .. 100", 100, c.getMax());
	        a.setValue(0);
	        store.propagate();
	        assertEquals("a should be 0", 0, a.getMin());
	        assertEquals("a should be 0", 0, a.getMax());
	        assertEquals("c should be 1 .. 100", 1, c.getMin());
	        assertEquals("c should be 1 .. 100", 100, c.getMax());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	public void testQuotConstraintPropagationWithNoNegatives() {
	    CspConstraint constraint = c.lt(a.divide(b));
	    try {
	        b.setMin(1);
	        store.addConstraint(constraint);
	        store.propagate();
	        //since b contains 0, there should be nothing that occurs
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be 1 .. 100", 1, b.getMin());
	        assertEquals("b should be 1 .. 100", 100, b.getMax());
	        assertEquals("c should be -100 .. 100", -100, c.getMin());
	        assertEquals("c should be -100 .. 99", 99, c.getMax());
	        a.removeValue(0);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be 1 .. 100", 1, b.getMin());
	        assertEquals("b should be 1 .. 100", 100, b.getMax());
	        assertEquals("c should be -100 .. 99", -100, c.getMin());
	        assertEquals("c should be -100 .. 99", 99, c.getMax());
	        b.setValue(20);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be 20", 20, b.getMin());
	        assertEquals("b should be 20", 20, b.getMax());
	        assertEquals("c should be -100 .. 4", -100, c.getMin());
	        assertEquals("c should be -100 .. 4", 4, c.getMax());
	        a.setValue(-60);
	        store.propagate();
	        assertEquals("a should be -60", -60, a.getMin());
	        assertEquals("a should be -60", -60, a.getMax());
	        assertEquals("b should be 20", 20, b.getMin());
	        assertEquals("b should be 20", 20, b.getMax());
	        assertEquals("c should be -100 .. -4", -100, c.getMin());
	        assertEquals("c should be -100 .. -4", -4, c.getMax());
	        c.setValue(-42);
	        assertTrue(constraint.isTrue());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	public void testQuotConstraintPropagationWithNoPostives() {
	    CspConstraint constraint = c.lt(a.divide(b));
	    try {
	        b.setMax(-1);
	        store.addConstraint(constraint);
	        store.propagate();
	        //since b contains 0, there should be nothing that occurs
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. -1", -100, b.getMin());
	        assertEquals("b should be -100 .. -1", -1, b.getMax());
	        assertEquals("c should be -100 .. 100", -100, c.getMin());
	        assertEquals("c should be -100 .. 99", 99, c.getMax());
	        a.removeValue(0);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -100 .. -1", -100, b.getMin());
	        assertEquals("b should be -100 .. -1", -1, b.getMax());
	        assertEquals("c should be -100 .. 99", -100, c.getMin());
	        assertEquals("c should be -100 .. 99", 99, c.getMax());
	        b.setValue(-20);
	        store.propagate();
	        assertEquals("a should be -100 .. 100", -100, a.getMin());
	        assertEquals("a should be -100 .. 100", 100, a.getMax());
	        assertEquals("b should be -20", -20, b.getMin());
	        assertEquals("b should be -20", -20, b.getMax());
	        assertEquals("c should be -100 .. 4", -100, c.getMin());
	        assertEquals("c should be -100 .. 4", 4, c.getMax());
	        a.setValue(60);
	        store.propagate();
	        assertEquals("a should be 60", 60, a.getMin());
	        assertEquals("a should be 60", 60, a.getMax());
	        assertEquals("b should be -20", -20, b.getMin());
	        assertEquals("b should be -20", -20, b.getMax());
	        assertEquals("c should be -100 .. -4", -100, c.getMin());
	        assertEquals("c should be -100 .. -4", -4, c.getMax());
	        c.setValue(-42);
	        assertTrue(constraint.isTrue());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	
}
