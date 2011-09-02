/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.QuotConstraint;
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
 * Test for QuotConstraint violation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericQuotConstraintTest extends TestCase {

	IntVariable a;
	IntVariable b;
	IntVariable c;
	GenericIntExpr ga;
	GenericIntExpr gb;
	GenericIntExpr gc;
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
		GenericIndex singleIdx = new GenericIndex("i",1);
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
        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{20,25,30});
        ga = (GenericIntExpr)varFactory.genericInt("ga", singleIdx, new CspIntVariable[]{a});
        gb = (GenericIntExpr)varFactory.genericInt("gb", singleIdx, new CspIntVariable[]{b});
        gc = (GenericIntExpr)varFactory.genericInt("gc", singleIdx, new CspIntVariable[]{c});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
	}

	public void tearDown() {
		a = null;
		b = null;
		c = null;
		ga = null;
		gb = null;
		gc = null;
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
	
	public void testQuotConstraintViolationGTGenericVarVarViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, z, ThreeVarConstraint.GT);
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
	
	public void testQuotConstraintViolationGTGenericConstVarViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, new Integer(2), z, ThreeVarConstraint.GT);
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
	
	public void testQuotConstraintViolationGTGenericVarConstNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, new Integer(19), ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMin(new Integer(2));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationGTGenericGenericVarSameIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GT);
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
	
	public void testQuotConstraintViolationGTGenericGenericVarSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(23));
			x2.setDomainMax(new Integer(45));
			x3.setDomainMax(new Integer(23));
			y1.setDomainMin(new Integer(12));
			y2.setDomainMin(new Integer(23));
			y3.setDomainMin(new Integer(11));
			z.setDomainMin(new Integer(1));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericGenericVarQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.GT);
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
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericGenericVarQuotQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(98));
			x2.setDomainMax(new Integer(85));
			x3.setDomainMax(new Integer(73));
			y1.setDomainMin(new Integer(12));
			y2.setDomainMin(new Integer(12));
			y3.setDomainMin(new Integer(13));
			z.setDomainMin(new Integer(3));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericVarGenericSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(40));
			x3.setDomainMax(new Integer(20));
			y.setDomainMin(new Integer(2));
			z1.setDomainMin(new Integer(29));
			z2.setDomainMin(new Integer(19));
			z3.setDomainMin(new Integer(9));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericVarGenericSameIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(40));
			x3.setDomainMax(new Integer(20));
			y.setDomainMin(new Integer(2));
			z1.setDomainMin(new Integer(29));
			z2.setDomainMin(new Integer(19));
			z3.setDomainMin(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericVarGenericQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(40));
			x3.setDomainMax(new Integer(20));
			y.setDomainMin(new Integer(2));
			z1.setDomainMin(new Integer(9));
			z2.setDomainMin(new Integer(5));
			z3.setDomainMin(new Integer(1));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericVarGenericQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(40));
			x3.setDomainMax(new Integer(20));
			y.setDomainMin(new Integer(2));
			z1.setDomainMin(new Integer(9));
			z2.setDomainMin(new Integer(14));
			z3.setDomainMin(new Integer(1));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationGTGenericGenericGenericQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(80));
			x2.setDomainMax(new Integer(60));
			x3.setDomainMax(new Integer(40));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(4));
			y3.setDomainMin(new Integer(2));
			z1.setDomainMin(new Integer(7));
			z2.setDomainMin(new Integer(6));
			z3.setDomainMin(new Integer(5));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationGTGenericGenericGenericQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GT);
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
		assertTrue("constraint now is violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationGTGenericGenericGenericSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GT);
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
	
	public void testQuotConstraintViolationGTGenericGenericGenericSameIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GT);
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
		assertTrue("constraint now is violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationLEQGenericGenericGenericQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LEQ);
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
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationLEQGenericGenericGenericQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(78));
			x2.setDomainMin(new Integer(78));
			x3.setDomainMin(new Integer(67));
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
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationLEQGenericGenericGenericSameIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(50));
			x2.setDomainMin(new Integer(50));
			x3.setDomainMin(new Integer(50));
			y1.setDomainMax(new Integer(10));
			y2.setDomainMax(new Integer(20));
			y3.setDomainMax(new Integer(50));
			z1.setDomainMax(new Integer(4));
			z2.setDomainMax(new Integer(2));
			z3.setDomainMax(new Integer(0));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationLEQGenericGenericGenericSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LEQ);
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
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericVarVarViolate() {
	    QuotConstraint constraint = new QuotConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(80));
			x3.setDomainMin(new Integer(70));
			y.setDomainMin(new Integer(10));
			z.setDomainMin(new Integer(11));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericVarVarNoViolate() {
	    QuotConstraint constraint = new QuotConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(80));
			x3.setDomainMin(new Integer(70));
			y.setDomainMin(new Integer(10));
			z.setDomainMin(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericGenericVarSameIndexViolate() {
	    QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(85));
			x3.setDomainMin(new Integer(80));
			y1.setDomainMin(new Integer(9));
			y2.setDomainMin(new Integer(5));
			y3.setDomainMin(new Integer(7));
			z.setDomainMin(new Integer(13));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericGenericVarSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(85));
			x3.setDomainMin(new Integer(80));
			y1.setDomainMin(new Integer(10));
			y2.setDomainMin(new Integer(5));
			y3.setDomainMin(new Integer(7));
			z.setDomainMin(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}

	public void testQuotConstraintViolationEQGenericGenericVarQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(85));
			x3.setDomainMin(new Integer(80));
			y1.setDomainMin(new Integer(9));
			y2.setDomainMin(new Integer(5));
			y3.setDomainMin(new Integer(7));
			z.setDomainMin(new Integer(13));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationEQGenericGenericVarQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(85));
			x3.setDomainMin(new Integer(80));
			y1.setDomainMin(new Integer(9));
			y2.setDomainMin(new Integer(5));
			y3.setDomainMin(new Integer(7));
			z.setDomainMin(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testQuotConstraintViolationEQGenericGenericGenericSameIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(80));
			x2.setDomainMin(new Integer(60));
			x3.setDomainMin(new Integer(40));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(10));
			z1.setDomainMin(new Integer(20));
			z2.setDomainMin(new Integer(17));
			z3.setDomainMin(new Integer(11));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericGenericGenericQuotIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(80));
			x3.setDomainMin(new Integer(70));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(8));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMin(new Integer(13));
			z2.setDomainMin(new Integer(12));
			z3.setDomainMin(new Integer(11));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationEQGenericGenericGenericSimilarIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(80));
			x2.setDomainMin(new Integer(60));
			x3.setDomainMin(new Integer(40));
			y1.setDomainMin(new Integer(8));
			y2.setDomainMin(new Integer(5));
			y3.setDomainMin(new Integer(8));
			z1.setDomainMin(new Integer(21));
			z2.setDomainMin(new Integer(15));
			z3.setDomainMin(new Integer(10));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQGenericVarVarViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, y, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(90));
			x2.setDomainValue(new Integer(80));
			x3.setDomainValue(new Integer(70));
			y.setDomainValue(new Integer(10));
			z.setDomainValue(new Integer(8));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQGenericGenericVarSameIndexNoViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(90));
			x2.setDomainValue(new Integer(85));
			x3.setDomainValue(new Integer(80));
			y1.setDomainValue(new Integer(9));
			y2.setDomainValue(new Integer(5));
			y3.setDomainValue(new Integer(7));
			z.setDomainValue(new Integer(18));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintViolationNEQGenericGenericGenericQuotIndexViolate() {
		QuotConstraint constraint = new QuotConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(90));
			x2.setDomainValue(new Integer(60));
			x3.setDomainValue(new Integer(30));
			y1.setDomainValue(new Integer(6));
			y2.setDomainValue(new Integer(10));
			y3.setDomainValue(new Integer(15));
			z1.setDomainValue(new Integer(20));
			z2.setDomainValue(new Integer(9));
			z3.setDomainValue(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testQuotConstraintWithNegativesGeneric() {
	    CspConstraint constraint = ga.divide(gb).lt(gc);
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
	
	public void testQuotConstraintWithZeroGeneric() {
	    CspConstraint constraint = ga.divide(gb).lt(gc);
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
	        assertEquals("b should be -100 .. 100", -100, b.getMin());
	        assertEquals("b should be -100 .. 100", 100, b.getMax());
	        assertEquals("c should be 1 .. 100", 1, c.getMin());
	        assertEquals("c should be 1 .. 100", 100, c.getMax());
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	}
	
	public void testQuotConstraintWithNoNegativesGeneric() {
	    CspConstraint constraint = gc.lt(ga.divide(gb));
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
}
