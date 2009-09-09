/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
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
 * Test for SumConstraint violation and propagation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericSumConstraintTest extends TestCase {

	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable x11;
	IntVariable x12;
	IntVariable x13;
	IntVariable x21;
	IntVariable x22;
	IntVariable x23;
	IntVariable x31;
	IntVariable x32;
	IntVariable x33;
	IntVariable y11;
	IntVariable y12;
	IntVariable y13;
	IntVariable y21;
	IntVariable y22;
	IntVariable y23;
	IntVariable y31;
	IntVariable y32;
	IntVariable y33;	
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	GenericIntExpr xiexpr;
	GenericIntExpr xijexpr;
	GenericIntExpr yijexpr;
	GenericIntExpr yikexpr;
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
	GenericIntConstant yiconst;
	GenericIntConstant yijconst;
	GenericIntConstant ziconst;
	GenericIntConstant zjconst;
	
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
        x11 = new IntVariable("x11", 0, 100);
        x12 = new IntVariable("x12", 0, 100);
        x13 = new IntVariable("x13", 0, 100);        
        x21 = new IntVariable("x21", 0, 100);
        x22 = new IntVariable("x22", 0, 100);
        x23 = new IntVariable("x23", 0, 100);
        x31 = new IntVariable("x31", 0, 100);
        x32 = new IntVariable("x32", 0, 100);
        x33 = new IntVariable("x33", 0, 100);
        y11 = new IntVariable("y11", 0, 100);
        y12 = new IntVariable("y12", 0, 100);
        y13 = new IntVariable("y13", 0, 100);        
        y21 = new IntVariable("y21", 0, 100);
        y22 = new IntVariable("y22", 0, 100);
        y23 = new IntVariable("y23", 0, 100);
        y31 = new IntVariable("y31", 0, 100);
        y32 = new IntVariable("y32", 0, 100);
        y33 = new IntVariable("y33", 0, 100); 
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{20,25,30});
        yijconst = new GenericIntConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new int[]{1,2,3,4,5,6,7,8,9});
        ziconst = new GenericIntConstant("ziconst", new GenericIndex[]{idxI}, new int[]{40,60,80});
        zjconst = new GenericIntConstant("zjconst", new GenericIndex[]{idxJ}, new int[]{40,60,80});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yikexpr = (GenericIntExpr)varFactory.genericInt("yik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
	}
	
	public void tearDown() {
		x1 = null;
		x2 = null;
		x3 = null;
		x11 = null;
		x12 = null;
		x13 = null;
		x21 = null;
		x22 = null;
		x23 = null;
		x31 = null;
		x32 = null;
		x33 = null;
		y11 = null;
		y12 = null;
		y13 = null;
		y21 = null;
		y22 = null;
		y23 = null;
		y31 = null;
		y32 = null;
		y33 = null;	
		y1 = null;
		y2 = null;
		y3 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		xiexpr = null;
		xijexpr = null;
		yijexpr = null;
		yikexpr = null;
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
		yiconst = null;
		yijconst = null;
		ziconst = null;
		zjconst = null;
	}
	
	public void testSumConstraintViolationLTGenericVarVarViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(20));
			x2.setDomainMin(new Integer(30));
			x3.setDomainMin(new Integer(40));
			y.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationGTGenericConstVarViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, new Integer(20), z, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			z.setDomainMin(new Integer(60));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationGTGenericConstGenericVarViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			store.propagate();
			assertEquals("the max should be 79",79,z1.getMax());
			assertEquals("the max should be 74",74,z2.getMax());
			assertEquals("the max should be 69",69,z3.getMax());
			
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			x1.setValue(50);
			x2.setValue(40);
			x3.setValue(30);
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			z1.setValue(50);
			z2.setValue(40);
			z3.setValue(30);
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testSumConstraintViolationGTGenericVarGenericConst() {
		SumConstraint constraint = new SumConstraint(xiexpr, ziexpr, yiconst, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
			store.propagate();
			assertEquals("the max should be 20",20,x1.getMax());
			assertEquals("the max should be 25",25,x2.getMax());
			assertEquals("the max should be 30",30,x3.getMax());
			assertEquals("the max should be 20",20,z1.getMax());
			assertEquals("the max should be 25",25,z2.getMax());
			assertEquals("the max should be 30",30,z3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			x1.setDomainMax(new Integer(10));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMax(new Integer(10));
			store.propagate();
			assertEquals("the min of z1 should be 10",10,z1.getMin());
			assertEquals("the max of z2 should be 25",15,z2.getMax());
			assertEquals("the max of z3 should be 30",30,z3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			z1.setValue(17);
			z2.setValue(7);
			z3.setValue(27);
			store.propagate();
			assertTrue("x should be bound now",xiexpr.isBound());
			assertEquals("the min of x1 should be 10",3,x1.getMin());
			assertEquals("the max of x2 should be 25",18,x2.getMax());
			assertEquals("the max of x3 should be 30",3,x3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testSumConstraintViolationGTGenericVarConstNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, new Integer(80), ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMax(new Integer(41));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericVarSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(1));
			x2.setDomainMin(new Integer(3));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(2));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(6));
			z.setDomainMax(new Integer(8));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericVarSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(1));
			x2.setDomainMin(new Integer(4));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z.setDomainMax(new Integer(8));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationLTGenericGenericVarDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(92));
			x2.setDomainMin(new Integer(72));
			x3.setDomainMin(new Integer(83));
			y1.setDomainMin(new Integer(54));
			y2.setDomainMin(new Integer(32));
			y3.setDomainMin(new Integer(44));
			z.setDomainMax(new Integer(2));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationLTGenericGenericVarDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(1));
			x2.setDomainMin(new Integer(4));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z.setDomainMax(new Integer(88));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationGTGenericVarGenericSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(69));
			z2.setDomainMin(new Integer(59));
			z3.setDomainMin(new Integer(49));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationGTGenericVarGenericSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(70));
			z2.setDomainMin(new Integer(59));
			z3.setDomainMin(new Integer(49));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationGTGenericVarGenericDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(49));
			z2.setDomainMin(new Integer(45));
			z3.setDomainMin(new Integer(39));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationGTGenericVarGenericDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
		    x1.setDomainMax(new Integer(60));
			x2.setDomainMax(new Integer(50));
			x3.setDomainMax(new Integer(40));
			y.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(50));
			z2.setDomainMin(new Integer(45));
			z3.setDomainMin(new Integer(39));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(1));
			x2.setDomainMin(new Integer(4));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z1.setDomainMax(new Integer(55));
			z2.setDomainMax(new Integer(60));
			z3.setDomainMax(new Integer(70));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(68));
			x2.setDomainMin(new Integer(70));
			x3.setDomainMin(new Integer(72));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z1.setDomainMax(new Integer(55));
			z2.setDomainMax(new Integer(60));
			z3.setDomainMax(new Integer(70));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(68));
			x2.setDomainMin(new Integer(70));
			x3.setDomainMin(new Integer(72));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z1.setDomainMax(new Integer(74));
			z2.setDomainMax(new Integer(74));
			z3.setDomainMax(new Integer(77));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(12));
			z2.setDomainMax(new Integer(12));
			z3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericSimilarIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(12));
			z2.setDomainMax(new Integer(12));
			z3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationLTGenericGenericGenericSimilarIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(7));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(5));
			z1.setDomainMax(new Integer(13));
			z2.setDomainMax(new Integer(14));
			z3.setDomainMax(new Integer(15));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationGEQGenericGenericGenericDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(25));
			x3.setDomainMax(new Integer(30));
			y1.setDomainMax(new Integer(15));
			y2.setDomainMax(new Integer(30));
			y3.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(30));
			z2.setDomainMin(new Integer(25));
			z3.setDomainMin(new Integer(20));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should not be violated because it is equal to
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	
	public void testSumConstraintViolationGEQGenericGenericGenericDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(25));
			x3.setDomainMax(new Integer(30));
			y1.setDomainMax(new Integer(15));
			y2.setDomainMax(new Integer(30));
			y3.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(70));
			z2.setDomainMin(new Integer(72));
			z3.setDomainMin(new Integer(73));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationGEQGenericGenericGenericSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(20));
			x3.setDomainMax(new Integer(20));
			y1.setDomainMax(new Integer(10));
			y2.setDomainMax(new Integer(20));
			y3.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(35));
			z2.setDomainMin(new Integer(35));
			z3.setDomainMin(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated, but would not be violated if using other constraints
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationGEQGenericGenericGenericSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(20));
			x3.setDomainMax(new Integer(20));
			y1.setDomainMax(new Integer(30));
			y2.setDomainMax(new Integer(20));
			y3.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(35));
			z2.setDomainMin(new Integer(35));
			z3.setDomainMin(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should never be violated.
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationEQGenericVarVarViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(20));
			x2.setDomainMin(new Integer(30));
			x3.setDomainMin(new Integer(40));
			y.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(40));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationEQGenericVarVarNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
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
	
	public void testSumConstraintViolationEQGenericGenericVarSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(10));
			x2.setDomainMin(new Integer(20));
			x3.setDomainMin(new Integer(30));
			y1.setDomainMin(new Integer(10));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(30));
			z.setDomainMax(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationEQGenericGenericVarSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
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
	
	public void testSumConstraintViolationEQGenericGenericVarDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(90));
			x2.setDomainMin(new Integer(80));
			x3.setDomainMin(new Integer(70));
			y1.setDomainMin(new Integer(95));
			y2.setDomainMin(new Integer(85));
			y3.setDomainMin(new Integer(75));
			z.setDomainMax(new Integer(5));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));	
	}
	
	public void testSumConstraintViolationEQGenericGenericVarDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
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
	
	public void testSumConstraintViolationEQGenericGenericGenericSameIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(15));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(10));
			y3.setDomainMin(new Integer(15));
			z1.setDomainMax(new Integer(10));
			z2.setDomainMax(new Integer(20));
			z3.setDomainMax(new Integer(25));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationEQGenericGenericGenericDiffIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
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
	
	public void testSumConstraintViolationEQGenericGenericGenericSimilarIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(35));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(41));
			z2.setDomainMax(new Integer(12));
			z3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint now is violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationNEQGenericVarVarViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, y, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(20));
			x2.setDomainValue(new Integer(30));
			x3.setDomainValue(new Integer(40));
			y.setDomainValue(new Integer(10));
			z.setDomainValue(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintViolationNEQGenericGenericVarSameIndexNoViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
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
	
	public void testSumConstraintViolationNEQGenericGenericGenericDiffIndexViolate() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(10));
			y2.setDomainValue(new Integer(11));
			z1.setDomainValue(new Integer(21));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	public void testSumConstraintLTcomboIndexVarVarViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, y, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(10));
			x12.setDomainMin(new Integer(11));
			x13.setDomainMin(new Integer(12));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(30));
			x32.setDomainMin(new Integer(32));
			x33.setDomainMin(new Integer(30));
			y.setDomainMin(new Integer(30));
			z.setDomainMax(new Integer(62));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}	
	public void testSumConstraintLTcomboIndexVarVarNoViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, y, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(10));
			x12.setDomainMin(new Integer(11));
			x13.setDomainMin(new Integer(12));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(30));
			x32.setDomainMin(new Integer(32));
			x33.setDomainMin(new Integer(30));
			y.setDomainMin(new Integer(30));
			z.setDomainMax(new Integer(63));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}		
	public void testSumConstraintLTcomboIndexGenericVarViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(10));
			x12.setDomainMin(new Integer(11));
			x13.setDomainMin(new Integer(12));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(30));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(30));
			y1.setDomainMin(new Integer(30));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(42));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	public void testSumConstraintLTcomboIndexGenericVarNoViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y1.setDomainMin(new Integer(30));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(45));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	public void testSumConstraintLTcomboIndexGenericGenericNoViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yiexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y1.setDomainMin(new Integer(30));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(10));
			z1.setDomainMax(new Integer(44));
			z2.setDomainMax(new Integer(44));
			z3.setDomainMax(new Integer(45));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	public void testSumConstraintLTcomboIndexcomboIndexGenericViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y11.setDomainMin(new Integer(33));
			y12.setDomainMin(new Integer(30));
			y13.setDomainMin(new Integer(30));
			y21.setDomainMin(new Integer(20));
			y22.setDomainMin(new Integer(20));
			y23.setDomainMin(new Integer(21));
			y31.setDomainMin(new Integer(12));
			y32.setDomainMin(new Integer(10));
			y33.setDomainMin(new Integer(10));
			z1.setDomainMax(new Integer(44));
			z2.setDomainMax(new Integer(43));
			z3.setDomainMax(new Integer(45));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}	
	public void testSumConstraintLTcomboIndexcomboIndexGenericNoViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y11.setDomainMin(new Integer(33));
			y12.setDomainMin(new Integer(30));
			y13.setDomainMin(new Integer(30));
			y21.setDomainMin(new Integer(20));
			y22.setDomainMin(new Integer(20));
			y23.setDomainMin(new Integer(21));
			y31.setDomainMin(new Integer(12));
			y32.setDomainMin(new Integer(10));
			y33.setDomainMin(new Integer(10));
			z1.setDomainMax(new Integer(45));
			z2.setDomainMax(new Integer(45));
			z3.setDomainMax(new Integer(46));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is totally not violated", constraint.isViolated(false));
	}	
	public void testSumConstraintLTcomboIndexDiffcomboIndexGenericNoViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yikexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y11.setDomainMin(new Integer(33));
			y12.setDomainMin(new Integer(30));
			y13.setDomainMin(new Integer(30));
			y21.setDomainMin(new Integer(20));
			y22.setDomainMin(new Integer(20));
			y23.setDomainMin(new Integer(21));
			y31.setDomainMin(new Integer(12));
			y32.setDomainMin(new Integer(10));
			y33.setDomainMin(new Integer(10));
			z1.setDomainMax(new Integer(47));
			z2.setDomainMax(new Integer(45));
			z3.setDomainMax(new Integer(47));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	public void testSumConstraintLTcomboIndexDiffcomboIndexGenericViolate() {
		SumConstraint constraint = new SumConstraint(xijexpr, yikexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated still", constraint.isViolated(false));
		try {
			x11.setDomainMin(new Integer(11));
			x12.setDomainMin(new Integer(12));
			x13.setDomainMin(new Integer(13));
			x21.setDomainMin(new Integer(20));
			x22.setDomainMin(new Integer(23));
			x23.setDomainMin(new Integer(20));
			x31.setDomainMin(new Integer(33));
			x32.setDomainMin(new Integer(30));
			x33.setDomainMin(new Integer(34));
			y11.setDomainMin(new Integer(33));
			y12.setDomainMin(new Integer(30));
			y13.setDomainMin(new Integer(30));
			y21.setDomainMin(new Integer(20));
			y22.setDomainMin(new Integer(20));
			y23.setDomainMin(new Integer(21));
			y31.setDomainMin(new Integer(12));
			y32.setDomainMin(new Integer(10));
			y33.setDomainMin(new Integer(10));
			z1.setDomainMax(new Integer(45));
			z2.setDomainMax(new Integer(45));
			z3.setDomainMax(new Integer(46));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSumConstraintLTSingleIndexGenericVarVar() {
	    SumConstraint constraint = new SumConstraint(xiexpr, y, z, ThreeVarConstraint.LT);
	    assertFalse("constraint is not violated still", constraint.isFalse());
	    try {
	        x1.setValue(10);
	        y.setValue(19);
	        z.setValue(30);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not true", constraint.isTrue());
	    assertFalse("constraint is not false", constraint.isFalse());
	}
	
	public void testGenericConstNormalConstAPISum() {
	    CspConstraint constraint = xiexpr.add(ziexpr).add(yiconst).eq(50);
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
		    assertEquals("z1Max should be 30 still",30, z1.getMax());
			assertEquals("x1Max should be 30 still",30, x1.getMax());
			assertEquals("z2Max should be 25 still",25, z2.getMax());
			assertEquals("x2Max should be 25 still",25, x2.getMax());
			assertEquals("z3Max should be 20 still",20, z3.getMax());
			assertEquals("x3Max should be 20 still",20, x3.getMax());
			z1.setValue(12);
			x2.setValue(20);
			x3.setValue(2);
			assertEquals("x1Max should be 30 still",30, x1.getMax());
			assertEquals("z2Max should be 25 still",25, z2.getMax());
			assertEquals("z3Max should be 20 still",20, z3.getMax());
			store.propagate();
			assertEquals("x1Max should be 18 still",18, x1.getMax());
			assertEquals("z2Max should be 5 still",5, z2.getMax());
			assertEquals("z3Max should be 18 still",18, z3.getMax());
			assertEquals("x1Min should be 18 still",18, x1.getMin());
			assertEquals("z2Min should be 5 still",5, z2.getMin());
			assertEquals("z3Min should be 18 still",18, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testNormalConstGenericConstAPISum() {
	    CspConstraint constraint = xiexpr.add(ziexpr).add(10).eq(yiconst);
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
		    assertEquals("z1Max should be 10 still",10, z1.getMax());
			assertEquals("x1Max should be 10 still",10, x1.getMax());
			assertEquals("z2Max should be 15 still",15, z2.getMax());
			assertEquals("x2Max should be 15 still",15, x2.getMax());
			assertEquals("z3Max should be 20 still",20, z3.getMax());
			assertEquals("x3Max should be 20 still",20, x3.getMax());
			z1.setValue(8);
			x2.setValue(12);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 2 now",2, x1.getMax());
			assertEquals("z2Max should be 3 now",3, z2.getMax());
			assertEquals("z3Max should be 18 still",18, z3.getMax());
			assertEquals("x1Min should be 2 now",2, x1.getMin());
			assertEquals("z2Min should be 3 still",3, z2.getMin());
			assertEquals("z3Min should be 18 still",18, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPISum() {
	    CspConstraint constraint = xiexpr.add(ziexpr).add(yiconst).eq(ziconst);
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
		    assertEquals("z1Max should be 20 still",20, z1.getMax());
			assertEquals("x1Max should be 20 still",20, x1.getMax());
			assertEquals("z2Max should be 35 still",35, z2.getMax());
			assertEquals("x2Max should be 35 still",35, x2.getMax());
			assertEquals("z3Max should be 50 still",50, z3.getMax());
			assertEquals("x3Max should be 50 still",50, x3.getMax());
			z1.setValue(8);
			x2.setValue(12);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 12 now",12, x1.getMax());
			assertEquals("z2Max should be 23 now",23, z2.getMax());
			assertEquals("z3Max should be 48 still",48, z3.getMax());
			assertEquals("x1Min should be 12 now",12, x1.getMin());
			assertEquals("z2Min should be 23 still",23, z2.getMin());
			assertEquals("z3Min should be 48 still",48, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPISumDiffIndicesLEQ() {
	    CspConstraint constraint = xiexpr.add(ziexpr).add(yiconst).leq(zjconst);
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
		    assertEquals("z1Max should be 20 still",20, z1.getMax());
			assertEquals("x1Max should be 20 still",20, x1.getMax());
			assertEquals("x2Max should be 15 still",15, x2.getMax());
			assertEquals("z2Max should be 15 still",15, z2.getMax());
			assertEquals("z3Max should be 10 still",10, z3.getMax());
			assertEquals("x3Max should be 10 still",10, x3.getMax());
			z1.setValue(8);
			x2.setValue(12);
			x3.setValue(2);
			store.propagate();
			
			assertEquals("x1 Max should be 12 now",12, x1.getMax());
			assertEquals("z2 Max should be 3 now",3, z2.getMax());
			assertEquals("z3 Max should be 8 still",8, z3.getMax());
			assertEquals("x1 Min should be 0 still",0, x1.getMin());
			assertEquals("z2 Min should be 0 still",0, z2.getMin());
			assertEquals("z3 Min should be 0 still",0, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPISumComplexIndicesLEQ() {
	    CspConstraint constraint = xiexpr.add(ziexpr).add(yijconst).leq(yiconst);
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
		    assertEquals("z1Max should be 17 still",17, z1.getMax());
			assertEquals("x1Max should be 17 still",17, x1.getMax());
			assertEquals("x2Max should be 19 still",19, x2.getMax());
			assertEquals("z2Max should be 19 still",19, z2.getMax());
			assertEquals("z3Max should be 21 still",21, z3.getMax());
			assertEquals("x3Max should be 21 still",21, x3.getMax());
			z1.setValue(8);
			x2.setValue(12);
			x3.setValue(2);
			store.propagate();
			
			assertEquals("x1 Max should be 9 now",9, x1.getMax());
			assertEquals("z2 Max should be 7 now",7, z2.getMax());
			assertEquals("z3 Max should be 19 still",19, z3.getMax());
			assertEquals("x1 Min should be 0 still",0, x1.getMin());
			assertEquals("z2 Min should be 0 still",0, z2.getMin());
			assertEquals("z3 Min should be 0 still",0, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPISumDiffIndicesOneValEQ() {
	    zjconst = new GenericIntConstant("zjconst", new GenericIndex[]{idxJ}, new int[]{40,40,40});
	    CspConstraint constraint = xiexpr.add(ziexpr).add(yiconst).eq(zjconst);
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
		    assertEquals("z1Max should be 20 still",20, z1.getMax());
			assertEquals("x1Max should be 20 still",20, x1.getMax());
			assertEquals("x2Max should be 15 still",15, x2.getMax());
			assertEquals("z2Max should be 15 still",15, z2.getMax());
			assertEquals("z3Max should be 10 still",10, z3.getMax());
			assertEquals("x3Max should be 10 still",10, x3.getMax());
			z1.setValue(8);
			x2.setValue(12);
			x3.setValue(2);
			store.propagate();
			
			assertEquals("x1 Max should be 12 now",12, x1.getMax());
			assertEquals("z2 Max should be 3 now",3, z2.getMax());
			assertEquals("z3 Max should be 8 still",8, z3.getMax());
			assertEquals("x1 Min should be 12 still",12, x1.getMin());
			assertEquals("z2 Min should be 3 still",3, z2.getMin());
			assertEquals("z3 Min should be 8 still",8, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
}
