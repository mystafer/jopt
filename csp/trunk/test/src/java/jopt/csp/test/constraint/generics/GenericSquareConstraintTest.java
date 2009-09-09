package jopt.csp.test.constraint.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.SquareConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SquareConstraint violation and propagation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericSquareConstraintTest extends TestCase {

	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	GenericIntExpr aiexpr;
	GenericIntExpr ziexpr;
	GenericIntExpr zkexpr;
	ConstraintStore store;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
	IntVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
        a1 = new IntVariable("a1", 0, 100);
        a2 = new IntVariable("a2", 0, 100);
        a3 = new IntVariable("a3", 0, 100);        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        z = new IntVariable("z", 0, 100);
        aiexpr = (GenericIntExpr)varFactory.genericInt("ai", idxI, new CspIntVariable[]{a1, a2, a3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
	}
	
	public void tearDown() {
		a1 = null;
		a2 = null;
		a3 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		aiexpr = null;
		ziexpr = null;
		zkexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		z = null;
	}
	
	public void testProdConstraintViolationGEQGenericGenericViolate() {
		SquareConstraint constraint = new SquareConstraint(aiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMax(new Integer(6));
			a2.setDomainMax(new Integer(7));
			a3.setDomainMax(new Integer(8));
			z1.setDomainMin(new Integer(64));
			z2.setDomainMin(new Integer(64));
			z3.setDomainMin(new Integer(64));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationGEQGenericGenericNoViolate() {
		SquareConstraint constraint = new SquareConstraint(aiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			a1.setDomainMax(new Integer(6));
			a2.setDomainMax(new Integer(7));
			a3.setDomainMax(new Integer(8));
			z1.setDomainMin(new Integer(32));
			z2.setDomainMin(new Integer(33));
			z3.setDomainMin(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSquareConstraintPropagationGEQGeneric() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a1 = new IntVariable("a1", 2, 6);
            IntVariable a2 = new IntVariable("a2", 3, 7);
            IntVariable a3 = new IntVariable("a3", 1, 8);
            CspGenericIntExpr aiexpr = varFactory.genericInt("aiexpr", idxI, new CspIntVariable[]{a1, a2, a3});
            IntVariable c = new IntVariable("c", -100, 100);
            
            solver.addConstraint(new SquareConstraint((NumExpr) aiexpr, c, ThreeVarConstraint.GEQ));
            
            solver.propagate();
            
            assertEquals("min of a1 is 2", 2, a1.getMin());
            assertEquals("max of a1 is 6", 6, a1.getMax());
            assertEquals("min of a2 is 3", 3, a2.getMin());
            assertEquals("max of a2 is 7", 7, a2.getMax());
            assertEquals("min of a3 is 1", 1, a3.getMin());
            assertEquals("max of a3 is 8", 8, a3.getMax());
            assertEquals("max of c is 36", 36, c.getMax());
            assertEquals("min of c is -100", -100, c.getMin());
            
            c.setMin(16);
            solver.propagate();
            
            assertEquals("min of a1 is 4", 4, a1.getMin());
            assertEquals("max of a1 is 6", 6, a1.getMax());
            assertEquals("min of a2 is 4", 4, a2.getMin());
            assertEquals("max of a2 is 7", 7, a2.getMax());
            assertEquals("min of a3 is 4", 4, a3.getMin());
            assertEquals("max of a3 is 8", 8, a3.getMax());
            assertEquals("max of c is 36", 36, c.getMax());
            assertEquals("min of c is 16", 16, c.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
