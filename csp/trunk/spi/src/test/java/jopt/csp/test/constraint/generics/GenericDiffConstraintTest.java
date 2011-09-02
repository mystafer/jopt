package jopt.csp.test.constraint.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.DiffConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
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
 * Test for DiffConstraint violation and propagation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericDiffConstraintTest extends TestCase {

	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	GenericIntConstant yiconst;
	GenericIntExpr xiexpr;
	GenericIntExpr yiexpr;
	GenericIntExpr yjexpr;
	GenericIntExpr ziexpr;
	GenericIntExpr zkexpr;
	ConstraintStore store;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
	IntVariable y;
	IntVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
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
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{20,25,30});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
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
		yiconst = null;
		xiexpr = null;
		yiexpr = null;
		yjexpr = null;
		ziexpr = null;
		zkexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}
	
	public void testDiffConstraintViolationGTGenericVarVarViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(59));
			x2.setDomainMax(new Integer(48));
			x3.setDomainMax(new Integer(33));
			y.setDomainMin(new Integer(33));
			z.setDomainMin(new Integer(30));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericConstVarViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, new Integer(20), z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			z.setDomainMin(new Integer(20));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericVarConstNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, new Integer(20), ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(19));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericGenericVarSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(7));
			x2.setDomainMax(new Integer(5));
			x3.setDomainMax(new Integer(6));
			y1.setDomainMin(new Integer(6));
			y2.setDomainMin(new Integer(4));
			y3.setDomainMin(new Integer(2));
			z.setDomainMin(new Integer(8));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericGenericVarSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(23));
			x2.setDomainMax(new Integer(45));
			x3.setDomainMax(new Integer(23));
			y1.setDomainMin(new Integer(12));
			y2.setDomainMin(new Integer(23));
			y3.setDomainMin(new Integer(11));
			z.setDomainMin(new Integer(3));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericVarGenericSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(10));
			z1.setDomainMin(new Integer(49));
			z2.setDomainMin(new Integer(39));
			z3.setDomainMin(new Integer(29));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericVarGenericSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(10));
			z1.setDomainMin(new Integer(49));
			z2.setDomainMin(new Integer(39));
			z3.setDomainMin(new Integer(30));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericVarGenericDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(10));
			z1.setDomainMin(new Integer(29));
			z2.setDomainMin(new Integer(19));
			z3.setDomainMin(new Integer(24));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericVarGenericDiffIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(10));
			z1.setDomainMin(new Integer(29));
			z2.setDomainMin(new Integer(30));
			z3.setDomainMin(new Integer(24));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericGenericVarDiffIndexViolateCase1() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(92));
			x2.setDomainMax(new Integer(72));
			x3.setDomainMax(new Integer(83));
			y1.setDomainMin(new Integer(54));
			y2.setDomainMin(new Integer(32));
			y3.setDomainMin(new Integer(44));
			z.setDomainMin(new Integer(62));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericGenericVarDiffIndexViolateCase2() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(56));
			x2.setDomainMax(new Integer(45));
			x3.setDomainMax(new Integer(33));
			y1.setDomainMin(new Integer(45));
			y2.setDomainMin(new Integer(34));
			y3.setDomainMin(new Integer(25));
			z.setDomainMin(new Integer(20));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericGenericVarDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(56));
			x2.setDomainMax(new Integer(45));
			x3.setDomainMax(new Integer(38));
			y1.setDomainMin(new Integer(36));
			y2.setDomainMin(new Integer(34));
			y3.setDomainMin(new Integer(25));
			z.setDomainMin(new Integer(1));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationGTGenericGenericGenericDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(31));
			x2.setDomainMax(new Integer(34));
			x3.setDomainMax(new Integer(33));
			y1.setDomainMin(new Integer(25));
			y2.setDomainMin(new Integer(23));
			y3.setDomainMin(new Integer(24));
			z1.setDomainMin(new Integer(1));
			z2.setDomainMin(new Integer(2));
			z3.setDomainMin(new Integer(5));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	
	public void testDiffConstraintViolationGTGenericGenericGenericDiffIndexViolateCase1() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(31));
			x2.setDomainMax(new Integer(34));
			x3.setDomainMax(new Integer(33));
			y1.setDomainMin(new Integer(25));
			y2.setDomainMin(new Integer(23));
			y3.setDomainMin(new Integer(24));
			z1.setDomainMin(new Integer(1));
			z2.setDomainMin(new Integer(2));
			z3.setDomainMin(new Integer(97));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericGenericGenericDiffIndexViolateCase2() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(68));
			x2.setDomainMax(new Integer(70));
			x3.setDomainMax(new Integer(72));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z1.setDomainMin(new Integer(73));
			z2.setDomainMin(new Integer(82));
			z3.setDomainMin(new Integer(83));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericGenericGenericSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(68));
			x2.setDomainMax(new Integer(70));
			x3.setDomainMax(new Integer(72));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z1.setDomainMin(new Integer(4));
			z2.setDomainMin(new Integer(7));
			z3.setDomainMin(new Integer(8));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationGTGenericGenericGenericSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(55));
			x2.setDomainMax(new Integer(65));
			x3.setDomainMax(new Integer(75));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(25));
			y3.setDomainMin(new Integer(45));
			z1.setDomainMin(new Integer(82));
			z2.setDomainMin(new Integer(92));
			z3.setDomainMin(new Integer(92));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationLEQGenericGenericGenericDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(20));
			x2.setDomainMin(new Integer(25));
			x3.setDomainMin(new Integer(30));
			y1.setDomainMax(new Integer(15));
			y2.setDomainMax(new Integer(12));
			y3.setDomainMax(new Integer(10));
			z1.setDomainMax(new Integer(55));
			z2.setDomainMax(new Integer(60));
			z3.setDomainMax(new Integer(70));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should not be violated because it is equal to
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	
	public void testDiffConstraintViolationLEQGenericGenericGenericDiffIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(24));
			x2.setDomainMin(new Integer(25));
			x3.setDomainMin(new Integer(30));
			y1.setDomainMax(new Integer(15));
			y2.setDomainMax(new Integer(18));
			y3.setDomainMax(new Integer(10));
			z1.setDomainMax(new Integer(1));
			z2.setDomainMax(new Integer(1));
			z3.setDomainMax(new Integer(2));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}		
	public void testDiffConstraintViolationLEQGenericGenericGenericSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(50));
			x2.setDomainMin(new Integer(50));
			x3.setDomainMin(new Integer(50));
			y1.setDomainMax(new Integer(10));
			y2.setDomainMax(new Integer(20));
			y3.setDomainMax(new Integer(50));
			z1.setDomainMax(new Integer(35));
			z2.setDomainMax(new Integer(35));
			z3.setDomainMax(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated, but would not be violated if using other constraints
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}	
	public void testDiffConstraintViolationLEQGenericGenericGenericSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(40));
			x2.setDomainMin(new Integer(50));
			x3.setDomainMin(new Integer(60));
			y1.setDomainMax(new Integer(30));
			y2.setDomainMax(new Integer(20));
			y3.setDomainMax(new Integer(50));
			z1.setDomainMax(new Integer(12));
			z2.setDomainMax(new Integer(35));
			z3.setDomainMax(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should never be violated.
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}		
	
	public void testDiffConstraintViolationEQGenericVarVarViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(10));
			z.setDomainMin(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericVarVarNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(20));
			x2.setDomainMin(new Integer(30));
			x3.setDomainMin(new Integer(40));
			y.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericGenericVarSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(45));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(55));
			y1.setDomainMin(new Integer(10));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(30));
			z.setDomainMin(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericGenericVarSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(15));
			x2.setDomainMin(new Integer(20));
			x3.setDomainMin(new Integer(25));
			y1.setDomainMin(new Integer(25));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(15));
			z.setDomainMax(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericGenericVarDiffIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(95));
			x2.setDomainMax(new Integer(85));
			x3.setDomainMax(new Integer(75));
			y1.setDomainMin(new Integer(70));
			y2.setDomainMin(new Integer(60));
			y3.setDomainMin(new Integer(50));
			z.setDomainMin(new Integer(6));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationEQGenericGenericVarDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(30));
			x2.setDomainMin(new Integer(20));
			x3.setDomainMin(new Integer(10));
			y1.setDomainMin(new Integer(10));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(30));
			z.setDomainMax(new Integer(60));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testDiffConstraintViolationEQGenericGenericGenericSameIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(30));
			x3.setDomainMax(new Integer(40));
			y1.setDomainMin(new Integer(15));
			y2.setDomainMin(new Integer(10));
			y3.setDomainMin(new Integer(15));
			z1.setDomainMin(new Integer(6));
			z2.setDomainMin(new Integer(5));
			z3.setDomainMin(new Integer(4));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericGenericGenericDiffIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(42));
			z2.setDomainMax(new Integer(42));
			z3.setDomainMax(new Integer(42));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationEQGenericGenericGenericSimilarIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(75));
			x2.setDomainMax(new Integer(76));
			x3.setDomainMax(new Integer(77));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(34));
			y3.setDomainMin(new Integer(33));
			z1.setDomainMin(new Integer(41));
			z2.setDomainMin(new Integer(42));
			z3.setDomainMin(new Integer(43));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationNEQGenericVarVarViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, y, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(20));
			x2.setDomainValue(new Integer(30));
			x3.setDomainValue(new Integer(40));
			y.setDomainValue(new Integer(10));
			z.setDomainValue(new Integer(20));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationNEQGenericGenericVarSameIndexNoViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(10));
			x2.setDomainValue(new Integer(20));
			x3.setDomainValue(new Integer(30));
			y1.setDomainValue(new Integer(11));
			y2.setDomainValue(new Integer(21));
			y3.setDomainValue(new Integer(31));
			z.setDomainValue(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testDiffConstraintViolationNEQGenericGenericGenericDiffIndexViolate() {
		DiffConstraint constraint = new DiffConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(30));
			x2.setDomainValue(new Integer(30));
			x3.setDomainValue(new Integer(30));
			y1.setDomainValue(new Integer(20));
			y2.setDomainValue(new Integer(20));
			y3.setDomainValue(new Integer(20));
			z1.setDomainValue(new Integer(10));
			z2.setDomainValue(new Integer(10));
			z3.setDomainValue(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testGenericConstNormalConstAPIDiff() {
	    CspConstraint constraint = xiexpr.subtract(ziexpr).subtract(yiconst).eq(5);
	    assertFalse("constraint is not true still", constraint.isTrue());
	    assertFalse("constraint is not false still", constraint.isFalse());
		try {
		    store.addConstraint(constraint);
			assertEquals("z1Max should be 100 still",100, z1.getMax());
			assertEquals("x1Max should be 100 still",100, x1.getMax());
			assertEquals("z2Max should be 100 still",100, z2.getMax());
			assertEquals("x2Max should be 100 still",100, x2.getMax());
			assertEquals("z3Max should be 100 still",100, z3.getMax());
			assertEquals("x3Max should be 100 still",100, x3.getMax());
		    store.propagate();
		    assertEquals("z1Max should be 75 still",75, z1.getMax());
			assertEquals("x1Max should be 100 still",100, x1.getMax());
			assertEquals("z2Max should be 70 still",70, z2.getMax());
			assertEquals("x2Max should be 100 still",100, x2.getMax());
			assertEquals("z3Max should be 65 still",65, z3.getMax());
			assertEquals("x3Max should be 100 still",100, x3.getMax());
			assertEquals("z1Min should be 0 still",0, z1.getMin());
			assertEquals("x1Min should be 25 still",25, x1.getMin());
			assertEquals("z2Min should be 0 still",0, z2.getMin());
			assertEquals("x2Min should be 30 still",30, x2.getMin());
			assertEquals("z3Min should be 0 still",0, z3.getMin());
			assertEquals("x3Min should be 35 still",35, x3.getMin());
			z1.setValue(12);
			x2.setValue(32);
			x3.setValue(50);
			store.propagate();
			assertEquals("z1Max should be 12 still",12, z1.getMax());
			assertEquals("x1Max should be 37 still",37, x1.getMax());
			assertEquals("z2Max should be 2 still",2, z2.getMax());
			assertEquals("x2Max should be 32 still",32, x2.getMax());
			assertEquals("z3Max should be 15 still",15, z3.getMax());
			assertEquals("x3Max should be 50 still",50, x3.getMax());
			assertEquals("z1Min should be 12 still",12, z1.getMin());
			assertEquals("x1Min should be 37 still",37, x1.getMin());
			assertEquals("z2Min should be 2 still",2, z2.getMin());
			assertEquals("x2Min should be 32 still",32, x2.getMin());
			assertEquals("z3Min should be 15 still",15, z3.getMin());
			assertEquals("x3Min should be 50 still",50, x3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
}
