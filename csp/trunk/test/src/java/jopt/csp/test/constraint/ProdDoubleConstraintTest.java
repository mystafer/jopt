package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.ProdConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.DoubleVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.util.IntSparseSet;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for ProdConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class ProdDoubleConstraintTest extends TestCase {

	DoubleVariable x1;
	DoubleVariable x2;
	DoubleVariable x3;
	DoubleVariable y1;
	DoubleVariable y2;
	DoubleVariable y3;
	DoubleVariable z1;
	DoubleVariable z2;
	DoubleVariable z3;
	ConstraintStore store;
	CspVariableFactory varFactory;
	DoubleVariable y;
	DoubleVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
        x1 = new DoubleVariable("x1", 0, 100);
        x2 = new DoubleVariable("x2", 0, 100);
        x3 = new DoubleVariable("x3", 0, 100);
        y1 = new DoubleVariable("y1", 0, 100);
        y2 = new DoubleVariable("y2", 0, 100);
        y3 = new DoubleVariable("y3", 0, 100);        
        z1 = new DoubleVariable("z1", 0, 100);
        z2 = new DoubleVariable("z2", 0, 100);
        z3 = new DoubleVariable("z3", 0, 100);
        y = new DoubleVariable("y", 0, 100);
        z = new DoubleVariable("z", 0, 100);
	}
	
	public void tearDown() {
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
	
	public void testProdConstraintViolationLTVarVarVarViolate() {
		ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Double(99));
			x2.setDomainMin(new Double(98));
			x3.setDomainMax(new Double(3));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationGEQVarVarVarNoViolate() {
		ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Double(3));
			x2.setDomainMax(new Double(3));
			x3.setDomainMin(new Double(6));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationGEQVarVarVarViolate() {
		ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Double(3));
			x2.setDomainMax(new Double(3));
			x3.setDomainMin(new Double(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationEQVarVarVarViolate() {
	    ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Double(7));
			x2.setDomainMin(new Double(3));
			x3.setDomainMax(new Double(9));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationEQVarVarVarNoViolate() {
	    ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Double(7));
			x2.setDomainMin(new Double(3));
			x3.setDomainMax(new Double(22));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationNEQVarVarVarViolate() {
	    ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Double(7));
			x2.setDomainValue(new Double(3));
			x3.setDomainValue(new Double(21));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationNEQUnboundVarVarVarNoViolate() {
	    ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Double(7));
			x2.setDomainMin(new Double(3));
			x3.setDomainMax(new Double(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationNEQBoundVarVarVarNoViolate() {
	    ProdConstraint constraint = new ProdConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
	    assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Double(7));
			x2.setDomainValue(new Double(3));
			x3.setDomainValue(new Double(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testNormalConstNormalConstAPIProd() {
	    CspConstraint constraint = x1.multiply(x2).multiply(x3).multiply(7).eq(420);
	    try {
		    assertFalse("constraint is not true", constraint.isTrue());
		    assertFalse("constraint is not false", constraint.isFalse());
		    store.addConstraint(constraint);
			assertEquals("x1Max should be 100 still",100.0, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 still",100.0, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 still",100.0, x3.getMax(),.0001);
			assertEquals("x1Min should be 0 still",0.0, x1.getMin(),.0001);
			assertEquals("x2Min should be 0 still",0.0, x2.getMin(),.0001);
			assertEquals("x3Min should be 0 still",0.0, x3.getMin(),.0001);
		    store.propagate();
		    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
			assertEquals("x1Max should be 100 now",100, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x1.setDomainMax(new Double(20));
		    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20.0, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100.0, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100.0, x3.getMax(),.0001);
			store.propagate();
			assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.030 still",0.030, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.030 still",0.030, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x2.setValue(5);
			store.propagate();
			assertEquals("x1Min should be 0.12 still",0.12, x1.getMin(),.0001);
			assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.6 still",0.6, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
			assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x1.setValue(2);
			store.propagate();
			assertEquals("x1Min should be 2 still",2, x1.getMin(),.0001);
			assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
			assertEquals("x3Min should be 6 still",6, x3.getMin(),.0001);
			assertEquals("x1Max should be 2 now",2, x1.getMax(),.0001);
			assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
			assertEquals("x3Max should be 6 now",6, x3.getMax(),.0001);
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
		public void testNormalConstNormalConstInMiddleAPIProd() {
		    CspConstraint constraint = x1.multiply(x2).multiply(7).multiply(x3).eq(420);
		    try {
			    assertFalse("constraint is not true still", constraint.isTrue());
			    assertFalse("constraint is not false still", constraint.isFalse());
			    store.addConstraint(constraint);
				assertEquals("x1Max should be 100 still",100, x1.getMax(),.0001);
				assertEquals("x2Max should be 100 still",100, x2.getMax(),.0001);
				assertEquals("x3Max should be 100 still",100, x3.getMax(),.0001);
				assertEquals("x1Min should be 0 still",0, x1.getMin(),.0001);
				assertEquals("x2Min should be 0 still",0, x2.getMin(),.0001);
				assertEquals("x3Min should be 0 still",0, x3.getMin(),.0001);
			    store.propagate();
			    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
				assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
				assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
				assertEquals("x1Max should be 100 now",100, x1.getMax(),.0001);
				assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
				assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
				x1.setDomainMax(new Double(20));
			    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
				assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
				assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
				assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
				assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
				assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
				store.propagate();
				assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
				assertEquals("x2Min should be 0.030 still",0.030, x2.getMin(),.0001);
				assertEquals("x3Min should be 0.030 still",0.030, x3.getMin(),.0001);
				assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
				assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
				assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
				x2.setValue(5);
				store.propagate();
				assertEquals("x1Min should be .12 still",.12, x1.getMin(),.0001);
				assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
				assertEquals("x3Min should be .6 still",.6, x3.getMin(),.0001);
				assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
				assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
				assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
				x1.setValue(2);
				store.propagate();
				assertEquals("x1Min should be 2 still",2, x1.getMin(),.0001);
				assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
				assertEquals("x3Min should be 6 still",6, x3.getMin(),.0001);
				assertEquals("x1Max should be 2 now",2, x1.getMax(),.0001);
				assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
				assertEquals("x3Max should be 6 now",6, x3.getMax(),.0001);
				assertTrue("constraint should now be true", constraint.isTrue());
				assertFalse("constraint should not be false", constraint.isFalse());
		    }
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	public void testNormalConstNormalConstInBeginAPIProd() {
	    CspConstraint constraint = x1.multiply(7).multiply(x2).multiply(x3).eq(420);
	    try {
		    assertFalse("constraint is not true still", constraint.isTrue());
		    assertFalse("constraint is not false still", constraint.isFalse());
		    store.addConstraint(constraint);
			assertEquals("x1Max should be 100 still",100, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 still",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 still",100, x3.getMax(),.0001);
			assertEquals("x1Min should be 0 still",0, x1.getMin(),.0001);
			assertEquals("x2Min should be 0 still",0, x2.getMin(),.0001);
			assertEquals("x3Min should be 0 still",0, x3.getMin(),.0001);
		    store.propagate();
		    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
			assertEquals("x1Max should be 100 now",100, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x1.setDomainMax(new Double(20));
		    assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.0060 still",0.0060, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.0060 still",0.0060, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			store.propagate();
			assertEquals("x1Min should be 0.0060 still",0.0060, x1.getMin(),.0001);
			assertEquals("x2Min should be 0.030 still",0.030, x2.getMin(),.0001);
			assertEquals("x3Min should be 0.030 still",0.030, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
			assertEquals("x2Max should be 100 now",100, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x2.setValue(5);
			store.propagate();
			assertEquals("x1Min should be .12 still",.12, x1.getMin(),.0001);
			assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
			assertEquals("x3Min should be .6 still",.6, x3.getMin(),.0001);
			assertEquals("x1Max should be 20 now",20, x1.getMax(),.0001);
			assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
			assertEquals("x3Max should be 100 now",100, x3.getMax(),.0001);
			x1.setValue(2);
			store.propagate();
			assertEquals("x1Min should be 2 still",2, x1.getMin(),.0001);
			assertEquals("x2Min should be 5 still",5, x2.getMin(),.0001);
			assertEquals("x3Min should be 6 still",6, x3.getMin(),.0001);
			assertEquals("x1Max should be 2 now",2, x1.getMax(),.0001);
			assertEquals("x2Max should be 5 now",5, x2.getMax(),.0001);
			assertEquals("x3Max should be 6 now",6, x3.getMax(),.0001);
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
	    }
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testProdConstraintPropagationLEQWithNegativeValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, -12);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            assertEquals("min of b is 2", 2, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithYZero() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", 0, 0);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", 12, 18);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            // B/c "a" is 0, nothing is known about "b"
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithYNegativeZZero() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            
            solver.addConstraint(a.multiply(b).leq(0));
            
            solver.propagate();
            
            assertEquals("min of b is 0", 0, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithYPositiveZZero() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", 2, 6);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            
            solver.addConstraint(a.multiply(b).leq(0));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 0", 0, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithPositiveAndNegativeYValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", 12, 18);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            // B/c "a" could be 0, nothing is known about "b"
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithPositiveAndNegativeSparseYValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntSparseSet aSparseVals = new IntSparseSet();
            aSparseVals.add(-6);
            aSparseVals.add(-4);
            aSparseVals.add(-2);
            aSparseVals.add(2);
            DoubleVariable a = new DoubleVariable("a", -6,2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", 12, 18);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            // B/c "a" could be both + or -, nothing is known about "b"
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithPositiveAndNegativeZValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, 12);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            assertEquals("min of b is -6", -6, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationLEQWithPositiveAndNegativeSparseZValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            IntSparseSet cSparseVals = new IntSparseSet();
            cSparseVals.add(-18);
            cSparseVals.add(-10);
            cSparseVals.add(-2);
            cSparseVals.add(6);
            cSparseVals.add(12);
            DoubleVariable c = new DoubleVariable("c", -18,12);
            
            solver.addConstraint(a.multiply(b).leq(c));
            
            solver.propagate();
            
            assertEquals("min of b is -6", -6, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithNegativeValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, -12);
            
            solver.addConstraint(a.multiply(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 9", 9, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithYNegativeZZero() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            
            solver.addConstraint(a.multiply(b).gt(new DoubleVariable("v",0,0)));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 0", 0, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithPositiveAndNegativeZValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, 12);
            
            solver.addConstraint(a.multiply(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 9", 9, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationEQWithNegativeValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, -12);
            
            solver.addConstraint(a.multiply(b).eq(c));
            
            solver.propagate();
            
            assertEquals("min of b is 2", 2, b.getMin(),.0001);
            assertEquals("max of b is 9", 9, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationNEQWithNegativeValues() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", -18, -12);
            
            solver.addConstraint(a.multiply(b).neq(c));
            a.setMax(-6);
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
            c.setMax(-18);
            
            solver.propagate();

	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithNonZeroPossibilitiesPositiveX() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", 0, 100);
            DoubleVariable c = new DoubleVariable("c", 12, 18);
            
            solver.addConstraint(a.multiply(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of b is 6", 6, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithNonZeroPossibilities() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            DoubleVariable c = new DoubleVariable("c", 12, 18);
            
            solver.addConstraint(a.multiply(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
            assertFalse("b should not contain 0", b.isInDomain(0));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithNonZeroPossibilitiesConstant() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", -100, 100);
            
            solver.addConstraint(a.multiply(b).gt(new DoubleVariable("v",7,7)));
            
            solver.propagate();
            
            assertEquals("min of b is -100", -100, b.getMin(),.0001);
            assertEquals("max of b is 100", 100, b.getMax(),.0001);
            assertFalse("b should not contain 0", b.isInDomain(0));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -10, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            
            solver.addConstraint(a.multiply(b).gt(c));
            
            solver.propagate();
            
            assertEquals("min of c is -100", -100, c.getMin(),.0001);
            assertEquals("max of c is 60", 60, c.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationNEQWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            
            solver.addConstraint(a.multiply(10).neq(c));
            a.setValue(-3);
            
            solver.propagate();
            
            assertEquals("min of c is -100", -100, c.getMin(),.0001);
            assertEquals("max of c is 100", 100, c.getMax(),.0001);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
