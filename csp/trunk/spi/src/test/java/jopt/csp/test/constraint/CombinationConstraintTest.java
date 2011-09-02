/*
 * Created on June 2, 2005
 */

package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for math intensive constraints combining multiple operations over generic vars
 * 
 * @author Chris Johnson
 * @author JB
 */
public class CombinationConstraintTest extends TestCase {
    
    IntVariable x1;
    IntVariable x2;
    IntVariable x3;
    IntVariable y1;
    IntVariable y2;
    IntVariable y3;
    IntVariable z1;
    IntVariable z2;
    IntVariable z3;
    IntVariable p1;
    IntVariable p2;
    IntVariable p3;
    ConstraintStore store;
    CspVariableFactory varFactory;
    IntVariable x;
    IntVariable y;
    IntVariable z;
    CspMath math;
    CspSolver solver;
    CspMath varMath;
    
    public void setUp () {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        store.setAutoPropagate(false);
        varFactory = store.getConstraintAlg().getVarFactory();
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        p1 = new IntVariable("p1", 0, 1);
        p2 = new IntVariable("p2", 0, 1);
        p3 = new IntVariable("p3", 0, 1);
        x = new IntVariable("x", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
        math = store.getConstraintAlg().getVarFactory().getMath();
        varMath = store.getConstraintAlg().getVarFactory().getMath();
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
        p1 = null;
        p2 = null;
        p3 = null;
        store = null;
        varFactory = null;
        x = null;
        y = null;
        z = null;
        math = null;
        solver = null;
        varMath = null;
    }
    
    public void testBooleanImpliesAndStatement() {
        CspConstraint constraint1 = x.eq(1);
        CspConstraint constraint2 = y.multiply(2).subtract(4).eq(4);
        CspConstraint constraint3 = z.eq(2);
        CspBooleanExpr bexpr1 = varFactory.booleanVar("x=1", constraint1);
        CspBooleanExpr bexpr2 = varFactory.booleanVar("2y-4=4", constraint2);
        CspBooleanExpr bexpr3 = varFactory.booleanVar("z=2", constraint3);
        CspBooleanExpr rightExpr = bexpr2.and(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.implies(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            x.setValue(1);
            solver.propagate();
            assertTrue("x=1 is true", constraint1.isTrue());
            //Both Y and Z should be bound now
            assertTrue("Y should be bound", y.isBound());
            assertTrue("Z should be bound", z.isBound());
            assertEquals("Y is equal to 4", 4, y.getMax());
            assertEquals("Z is equal to 2", 2, z.getMax());
            
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testBooleanImpliesOrStatement() {
        CspConstraint constraint1 = x.eq(1);
        CspConstraint constraint2 = y.multiply(2).subtract(4).eq(4);
        CspConstraint constraint3 = z.eq(2);
        CspBooleanExpr bexpr1 = varFactory.booleanVar("x=1", constraint1);
        CspBooleanExpr bexpr2 = varFactory.booleanVar("2y-4=4", constraint2);
        CspBooleanExpr bexpr3 = varFactory.booleanVar("z=2", constraint3);
        CspBooleanExpr rightExpr = bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.implies(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            x.setValue(1);
            solver.propagate();
            
            assertFalse("Y should not be bound", y.isBound());
            assertFalse("Z should not be bound", z.isBound());
            
            z.setValue(1);
            solver.propagate();
            //Both Y and Z should be bound now
            assertTrue("Y should be bound", y.isBound());
            assertTrue("Z should be bound", z.isBound());
            assertEquals("Y is equal to 4", 4, y.getMax());
            assertEquals("Z is equal to 1", 1, z.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testBooleanAsNumMathXor() {
        BooleanVariable b1 = new BooleanVariable("b1");
        BooleanVariable b2 = new BooleanVariable("b2");
        try {
            solver.addConstraint(b1.add(b2).eq(1));
            assertTrue(solver.propagate());
            assertFalse("b1 should not be bound",b1.isBound());
            assertFalse("b2 should not be bound",b2.isBound());
            b1.setFalse();
            assertTrue(solver.propagate());
            assertTrue("b1 should be bound",b1.isBound());
            assertTrue("b2 should be bound",b2.isBound());
            assertTrue("bw should be true",b2.isTrue());
        }
        catch (PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    public void testMultipleExpressionEquality() {
        try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable b = new IntVariable("b", 3, 4);
	        IntVariable c = new IntVariable("c", 0, 100);
	        IntVariable d = new IntVariable("d", 0, 100);
	        
	        CspIntExpr a_times_b = a.multiply(b);
	        CspIntExpr c_plus_d = c.add(d);
	        
	        solver.addConstraint(a_times_b.eq(c_plus_d));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 24", 24, c.getMax());
	        assertEquals("min of d is 0", 0, d.getMin());
	        assertEquals("max of d is 24", 24, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 24", 24, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 0", 0, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 48", 48, c_plus_d.getMax());
	        
	        c.setMax(2);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	        assertEquals("min of d is 4", 4, d.getMin());
	        assertEquals("max of d is 24", 24, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 24", 24, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 4", 4, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 26", 26, c_plus_d.getMax());
	        
	        d.setMax(6);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	        assertEquals("min of d is 4", 4, d.getMin());
	        assertEquals("max of d is 6", 6, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 8", 8, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 4", 4, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 8", 8, c_plus_d.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
    }
    
    public void testMultipleExpressionEvaluationWithoutConstraints() {
        try {
	        CspSolver solver = CspSolver.createSolver();
	        solver.setAutoPropagate(false);
	        
	        IntVariable a = new IntVariable("a", 2, 6);
	        IntVariable b = new IntVariable("b", 3, 4);
	        IntVariable c = new IntVariable("c", 0, 100);
	        IntVariable d = new IntVariable("d", 0, 100);
	        
	        CspIntExpr a_times_b = a.multiply(b);
	        CspIntExpr a_times_b_plus_x = a_times_b.add(x);
	        CspIntExpr c_plus_d = c.add(d);
	        
	        solver.addConstraint(a_times_b.eq(c_plus_d));
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 24", 24, c.getMax());
	        assertEquals("min of d is 0", 0, d.getMin());
	        assertEquals("max of d is 24", 24, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 24", 24, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 0", 0, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 48", 48, c_plus_d.getMax());
	        assertEquals("min of a_times_b_plus_x is 6", 6, a_times_b_plus_x.getMin());
	        assertEquals("max of a_times_b_plus_x is 124", 124, a_times_b_plus_x.getMax());
	        
	        c.setMax(2);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 6", 6, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	        assertEquals("min of d is 4", 4, d.getMin());
	        assertEquals("max of d is 24", 24, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 24", 24, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 4", 4, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 26", 26, c_plus_d.getMax());
	        assertEquals("min of a_times_b_plus_x is 6", 6, a_times_b_plus_x.getMin());
	        assertEquals("max of a_times_b_plus_x is 124", 124, a_times_b_plus_x.getMax());
	        
	        d.setMax(6);
	        
	        solver.propagate();
	        
	        assertEquals("min of a is 2", 2, a.getMin());
	        assertEquals("max of a is 2", 2, a.getMax());
	        assertEquals("min of b is 3", 3, b.getMin());
	        assertEquals("max of b is 4", 4, b.getMax());
	        assertEquals("min of c is 0", 0, c.getMin());
	        assertEquals("max of c is 2", 2, c.getMax());
	        assertEquals("min of d is 4", 4, d.getMin());
	        assertEquals("max of d is 6", 6, d.getMax());
	        
	        assertEquals("min of a_times_b is 6", 6, a_times_b.getMin());
	        assertEquals("max of a_times_b is 8", 8, a_times_b.getMax());
	        assertEquals("min of c_plus_d is 4", 4, c_plus_d.getMin());
	        assertEquals("max of c_plus_d is 8", 8, c_plus_d.getMax());
	        assertEquals("min of a_times_b_plus_x is 6", 6, a_times_b_plus_x.getMin());
	        assertEquals("max of a_times_b_plus_x is 108", 108, a_times_b_plus_x.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
    }
    
    public void testAbsWithNumRelation() {
        try {
            x1.setMin(1);
            x1.setMax(4);
            x2.setMin(2);
            x2.setMax(4);
            
            CspIntExpr x1MinusX2 = x2.subtract(x1);
            CspIntExpr diag = varMath.abs(x1MinusX2);
            CspConstraint constraint = diag.neq(1);
            
            solver.addConstraint(constraint);
            solver.propagate();
            x1.setValue(1);
            solver.propagate();
            assertEquals("x2: [3..4]", 3, x2.getMin());
            assertEquals("x2: [3..4]", 4, x2.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
}
