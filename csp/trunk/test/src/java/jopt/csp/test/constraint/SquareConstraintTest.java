package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SquareConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SquareConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class SquareConstraintTest extends TestCase {

	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	ConstraintStore store;
	CspVariableFactory varFactory;
	IntVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
        a1 = new IntVariable("a1", 0, 100);
        a2 = new IntVariable("a2", 0, 100);
        a3 = new IntVariable("a3", 0, 100);        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        z = new IntVariable("z", 0, 100);
	}
	
	public void tearDown() {
		a1 = null;
		a2 = null;
		a3 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		store = null;
		varFactory = null;
		z = null;
	}
	
	public void testSquareConstraintViolationEQVarVarNoViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainValue(new Integer(2));
			a2.setDomainValue(new Integer(4));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationEQVarVarViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainValue(new Integer(2));
			a2.setDomainValue(new Integer(5));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationLTVarVarViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMin(new Integer(4));
			a2.setDomainMax(new Integer(16));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationLTVarVarNoViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMin(new Integer(4));
			a2.setDomainMax(new Integer(17));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationNEQVarVarNoViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMin(new Integer(4));
			a2.setDomainMax(new Integer(17));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationNEQVarVarViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainValue(new Integer(7));
			a2.setDomainValue(new Integer(49));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationGEQVarVarNoViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainValue(new Integer(7));
			a2.setDomainValue(new Integer(49));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintViolationGEQVarVarViolate() {
		SquareConstraint constraint = new SquareConstraint(a1, a2, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMax(new Integer(7));
			a2.setDomainMin(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintPropagationGEQ() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	        
	        c.setMin(16);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 4", 4, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 16", 16, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQConstC() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        Number c = new Integer(9);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 3", 3, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQConstCPartial() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        Number c = new Integer(10);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 4", 4, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQConstA() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        Number a = new Integer(4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 16", 16, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQPartial() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	        
	        c.setMin(17);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 5", 5, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 17", 17, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -2", -2, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	        
	        c.setMin(16);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -6", -6, a.getMin());
	        assertEquals("max of a is -4", -4, a.getMax());
	        assertEquals("min of c is 16", 16, c.getMin());
	        assertEquals("max of c is 36", 36, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGEQWithPositivesAndNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -7, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -7", -7, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 49", 49, c.getMax());
	        
	        c.setMin(4);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -7", -7, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertTrue("a does contain -2", a.isInDomain(-2));
	        assertFalse("a does not contain -1", a.isInDomain(-1));
	        assertFalse("a does not contain 0", a.isInDomain(0));
	        assertFalse("a does not contain 1", a.isInDomain(1));
	        assertTrue("a does contain 2", a.isInDomain(2));
	        assertEquals("min of c is 4", 4, c.getMin());
	        assertEquals("max of c is 49", 49, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationGT() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.GT));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 35", 35, c.getMax());
	        
	        c.setMin(16);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 5", 5, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 16", 16, c.getMin());
	        assertEquals("max of c is 35", 35, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationLTPartial() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.LT));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(17);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 4", 4, a.getMax());
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 17", 17, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationLTWithPositivesAndNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -2, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.LT));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -2", -2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 1", 1, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setMax(17);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -2", -2, a.getMin());
	        assertEquals("max of a is 4", 4, a.getMax());
	        assertEquals("min of c is 1", 1, c.getMin());
	        assertEquals("max of c is 17", 17, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationLTConstA() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);

	        Number a = new Integer(4);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.LT));
	        
	        solver.propagate();
	        
	        assertEquals("min of c is 17", 17, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationLEQWithNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -6, -2);
	        IntVariable c = new IntVariable("c", -100, -5);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.LEQ));
	        
	        assertFalse("propagation should fail", solver.propagate());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationEQWithPositivesAndNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -7, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.EQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -7", -7, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 49", 49, c.getMax());
	        
	        c.setMin(5);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -7", -7, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertTrue("a does contain -3", a.isInDomain(-3));
	        assertFalse("a does not contain -2", a.isInDomain(-2));
	        assertFalse("a does not contain -1", a.isInDomain(-1));
	        assertFalse("a does not contain 0", a.isInDomain(0));
	        assertFalse("a does not contain 1", a.isInDomain(1));
	        assertFalse("a does not contain 2", a.isInDomain(2));
	        assertTrue("a does contain 3", a.isInDomain(3));
	        assertEquals("min of c is 5", 5, c.getMin());
	        assertEquals("max of c is 49", 49, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSquareConstraintPropagationNEQWithPositivesAndNegatives() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", -5, 6);
	        IntVariable c = new IntVariable("c", -100, 100);
	        
	        solver.addConstraint(new SquareConstraint(a, c, ThreeVarConstraint.NEQ));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -5", -5, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of c is -100", -100, c.getMin());
	        assertEquals("max of c is 100", 100, c.getMax());
	        
	        c.setValue(16);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is -5", -5, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertFalse("a does not contain 4", a.isInDomain(4));
	        assertFalse("a does not contain -4", a.isInDomain(-4));
	        assertEquals("min of c is 16", 16, c.getMin());
	        assertEquals("max of c is 16", 16, c.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
