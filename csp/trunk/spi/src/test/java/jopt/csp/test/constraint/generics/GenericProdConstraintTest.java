package jopt.csp.test.constraint.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.ProdConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for ProdConstraint violation and propagation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericProdConstraintTest extends TestCase {

	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
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
	GenericIntConstant yiconst;
	GenericIntConstant ziconst;
	GenericIntConstant yjconst;
	GenericIntConstant yijconst;
	
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
        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{2,3,4});
        ziconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{36,48,64});
        yjconst = new GenericIntConstant("yjconst", new GenericIndex[]{idxJ}, new int[]{36,19,23});
        yijconst = new GenericIntConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new int[]{10,15,20,30,40,50,75,85,95});
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
		yiconst = null;
		ziconst = null;
		yjconst = null;
		yijconst = null;
	}
	
	public void testGenericConstNormalConstAPIProd() {
	    yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{20,25,30});
	    CspConstraint constraint = xiexpr.multiply(ziexpr).multiply(yiconst).eq(15000);
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
		    //The largest divisor of 750 less than or equal to 100 is 75
		    assertEquals("z1Max should be 75 still",75, z1.getMax());
			assertEquals("x1Max should be 75 still",75, x1.getMax());
		    //The largest divisor of 600 less than or equal to 100 is 100 
			assertEquals("z2Max should be 100 still",100, z2.getMax());
			assertEquals("x2Max should be 100 still",100, x2.getMax());
		    //The largest divisor of 500 less than or equal to 500 is 100
			assertEquals("z3Max should be 100 still",100, z3.getMax());
			assertEquals("x3Max should be 100 still",100, x3.getMax());
			//This will have to be at least 10 since the max is 75
		    assertEquals("z1Min should be 10 still",10, z1.getMin());
			assertEquals("x1Min should be 10 still",10, x1.getMin());
			//The min will be 6 since the max is 100
			assertEquals("z2Min should be 6 still",6, z2.getMin());
			assertEquals("x2Min should be 6 still",6, x2.getMin());
			//Teh min will be 5 since max is 100
			assertEquals("z3Min should be 5 still",5, z3.getMin());
			assertEquals("x3Min should be 5 still",5, x3.getMin());
			z1.setValue(15);
			x2.setValue(12);
			x3.setValue(20);
			store.propagate();
			assertEquals("x1Max should be 50 still",50, x1.getMax());
			assertEquals("x1Min should be 50 still",50, x1.getMin());
			assertTrue("x1 should be bound",x1.isBound());
			
			assertEquals("z2 should be 50 still",50, z2.getMax());
			assertEquals("z2 should be 50 still",50, z2.getMin());
			assertTrue("z2 should be bound",z2.isBound());

			assertEquals("z3 should be 25 still",25, z3.getMax());
			assertEquals("z3 should be 25 still",25, z3.getMin());
			assertTrue("z3 should be bound",z3.isBound());
			
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testNormalConstGenericConstAPIProd() {
	    yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{12,14,16});
	    CspConstraint constraint = xiexpr.multiply(ziexpr).multiply(2).eq(yiconst);
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
		    assertEquals("z1Max should be 6 still",6, z1.getMax());
			assertEquals("x1Max should be 6 still",6, x1.getMax());
			assertEquals("z2Max should be 7 still",7, z2.getMax());
			assertEquals("x2Max should be 7 still",7, x2.getMax());
			assertEquals("z3Max should be 8 still",8, z3.getMax());
			assertEquals("x3Max should be 8 still",8, x3.getMax());
			z1.setValue(3);
			x2.setValue(1);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 2 now",2, x1.getMax());
			assertEquals("z2Max should be 7 now",7, z2.getMax());
			assertEquals("z3Max should be 4 still",4, z3.getMax());
			assertEquals("x1Min should be 2 now",2, x1.getMin());
			assertEquals("z2Min should be 7 still",7, z2.getMin());
			assertEquals("z3Min should be 4 still",4, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPIProd() {
	    CspConstraint constraint = xiexpr.multiply(ziexpr).multiply(yiconst).eq(ziconst);
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
		    assertEquals("z1Max should be 18 still",18, z1.getMax());
			assertEquals("x1Max should be 18 still",18, x1.getMax());
			assertEquals("z2Max should be 16 still",16, z2.getMax());
			assertEquals("x2Max should be 16 still",16, x2.getMax());
			assertEquals("z3Max should be 16 still",16, z3.getMax());
			assertEquals("x3Max should be 16 still",16, x3.getMax());
			z1.setValue(3);
			x2.setValue(1);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 6 now",6, x1.getMax());
			assertEquals("z2Max should be 16 now",16, z2.getMax());
			assertEquals("z3Max should be 8 still",8, z3.getMax());
			assertEquals("x1Min should be 6 now",6, x1.getMin());
			assertEquals("z2Min should be 16 still",16, z2.getMin());
			assertEquals("z3Min should be 8 still",8, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstGenericConstAPIProdDiffIndexGT() {
	    CspConstraint constraint = xiexpr.multiply(ziexpr).multiply(yijconst).gt(ziconst);
	    assertFalse("constraint is not true still", constraint.isTrue());
	    assertFalse("constraint is not false still", constraint.isFalse());
		try {
		    store.addConstraint(constraint);
			assertEquals("z1Min should be 0 still",0, z1.getMin());
			assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("z2Min should be 0 still",0, z2.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("z3Min should be 0 still",0, z3.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
		    store.propagate();
		    assertEquals("z1Min should be 1 still",1, z1.getMin());
			assertEquals("x1Min should be 1 still",1, x1.getMin());
			assertEquals("z2Min should be 1 still",1, z2.getMin());
			assertEquals("x2Min should be 1 still",1, x2.getMin());
			assertEquals("z3Min should be 1 still",1, z3.getMin());
			assertEquals("x3Min should be 1 still",1, x3.getMin());
			z1.setValue(3);
			x2.setValue(1);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 100 still",100, x1.getMax());
			assertEquals("z2Max should be 100 still",100, z2.getMax());
			assertEquals("z3Max should be 100 still",100, z3.getMax());
			assertEquals("x1Min should be 2 now",2, x1.getMin());
			assertEquals("z2Min should be 2 still",2, z2.getMin());
			assertEquals("z3Min should be 1 still",1, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testGenericConstMidGenericConstAPIProdDiffIndexGT() {
	    CspConstraint constraint = xiexpr.multiply(yijconst).multiply(ziexpr).gt(ziconst);
	    assertFalse("constraint is not true still", constraint.isTrue());
	    assertFalse("constraint is not false still", constraint.isFalse());
		try {
		    store.addConstraint(constraint);
			assertEquals("z1Min should be 0 still",0, z1.getMin());
			assertEquals("x1Min should be 0 still",0, x1.getMin());
			assertEquals("z2Min should be 0 still",0, z2.getMin());
			assertEquals("x2Min should be 0 still",0, x2.getMin());
			assertEquals("z3Min should be 0 still",0, z3.getMin());
			assertEquals("x3Min should be 0 still",0, x3.getMin());
		    store.propagate();
		    assertEquals("z1Min should be 1 still",1, z1.getMin());
			assertEquals("x1Min should be 1 still",1, x1.getMin());
			assertEquals("z2Min should be 1 still",1, z2.getMin());
			assertEquals("x2Min should be 1 still",1, x2.getMin());
			assertEquals("z3Min should be 1 still",1, z3.getMin());
			assertEquals("x3Min should be 1 still",1, x3.getMin());
			z1.setValue(3);
			x2.setValue(1);
			x3.setValue(2);
			store.propagate();
			assertEquals("x1Max should be 100 still",100, x1.getMax());
			assertEquals("z2Max should be 100 still",100, z2.getMax());
			assertEquals("z3Max should be 100 still",100, z3.getMax());
			assertEquals("x1Min should be 2 now",2, x1.getMin());
			assertEquals("z2Min should be 2 still",2, z2.getMin());
			assertEquals("z3Min should be 1 still",1, z3.getMin());
			assertTrue("constraint should now be true", constraint.isTrue());
			assertFalse("constraint should not be false", constraint.isFalse());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testProdConstraintViolationLTGenericVarVarViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(59));
			x2.setDomainMin(new Integer(48));
			x3.setDomainMin(new Integer(33));
			y.setDomainMin(new Integer(77));
			z.setDomainMax(new Integer(6));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericConstVarViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, new Integer(2), z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(30));
			x2.setDomainMin(new Integer(20));
			x3.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(60));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericVarConstNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, new Integer(81), ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(40));
			x2.setDomainMin(new Integer(30));
			x3.setDomainMin(new Integer(20));
			y.setDomainMin(new Integer(2));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericGenericVarSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationLTGenericGenericVarSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(1));
			x2.setDomainMin(new Integer(4));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z.setDomainMax(new Integer(15));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationLTGenericGenericVarDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationLTGenericGenericVarDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationLTGenericVarGenericSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(15));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(5));
			y.setDomainMin(new Integer(5));
			z1.setDomainMax(new Integer(76));
			z2.setDomainMax(new Integer(51));
			z3.setDomainMax(new Integer(26));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationLTGenericVarGenericSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMin(new Integer(15));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(5));
			y.setDomainMin(new Integer(5));
			z1.setDomainMax(new Integer(76));
			z2.setDomainMax(new Integer(51));
			z3.setDomainMax(new Integer(25));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationLTGenericVarGenericDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMin(new Integer(15));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(5));
			y.setDomainMin(new Integer(5));
			z1.setDomainMax(new Integer(76));
			z2.setDomainMax(new Integer(81));
			z3.setDomainMax(new Integer(86));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationLTGenericVarGenericDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    x1.setDomainMin(new Integer(15));
			x2.setDomainMin(new Integer(10));
			x3.setDomainMin(new Integer(5));
			y.setDomainMin(new Integer(5));
			z1.setDomainMax(new Integer(76));
			z2.setDomainMax(new Integer(81));
			z3.setDomainMax(new Integer(75));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationLTGenericGenericGenericDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
		//Notice that although if they were indexed separately this would be violate
		//They should not be violated here now
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericGenericGenericDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
		//Notice that although if they were indexed separately this would be violate
		//They should not be violated here now
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericGenericGenericSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(2));
			x2.setDomainMin(new Integer(2));
			x3.setDomainMin(new Integer(2));
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
		//Notice that although if they were indexed separately this would be violate
		//They should not be violated here now
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationLTGenericGenericGenericSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
		//Notice that although if they were indexed separately this would be violate
		//They should not be violated here now
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationGEQGenericGenericGenericDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(20));
			x2.setDomainMax(new Integer(25));
			x3.setDomainMax(new Integer(30));
			y1.setDomainMax(new Integer(15));
			y2.setDomainMax(new Integer(30));
			y3.setDomainMax(new Integer(10));
			z1.setDomainMin(new Integer(55));
			z2.setDomainMin(new Integer(60));
			z3.setDomainMin(new Integer(70));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should not be violated because it is equal to
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}	
	
	public void testProdConstraintViolationGEQGenericGenericGenericDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(8));
			x2.setDomainMax(new Integer(7));
			x3.setDomainMax(new Integer(6));
			y1.setDomainMax(new Integer(5));
			y2.setDomainMax(new Integer(7));
			y3.setDomainMax(new Integer(9));
			z1.setDomainMin(new Integer(77));
			z2.setDomainMin(new Integer(76));
			z3.setDomainMin(new Integer(73));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationGEQGenericGenericGenericSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMax(new Integer(2));
			x2.setDomainMax(new Integer(3));
			x3.setDomainMax(new Integer(4));
			y1.setDomainMax(new Integer(5));
			y2.setDomainMax(new Integer(4));
			y3.setDomainMax(new Integer(3));
			z1.setDomainMin(new Integer(35));
			z2.setDomainMin(new Integer(35));
			z3.setDomainMin(new Integer(35));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		//Should be violated, but would not be violated if using other constraints
		assertTrue("constraint is violated", constraint.isViolated(false));
	}	
	public void testProdConstraintViolationGEQGenericGenericGenericSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.GEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationEQGenericVarVarViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationEQGenericVarVarNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(2));
			x2.setDomainMin(new Integer(3));
			x3.setDomainMin(new Integer(4));
			y.setDomainMin(new Integer(10));
			z.setDomainMax(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationEQGenericGenericVarSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
	
	public void testProdConstraintViolationEQGenericGenericVarSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(4));
			x3.setDomainMin(new Integer(3));
			y1.setDomainMin(new Integer(2));
			y2.setDomainMin(new Integer(3));
			y3.setDomainMin(new Integer(4));
			z.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationEQGenericGenericVarDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
		//Notice that although if they were indexed separately this would be violate
		//They should not be violated here now
		assertTrue("constraint is violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationEQGenericGenericVarDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, z, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(3));
			x2.setDomainMin(new Integer(2));
			x3.setDomainMin(new Integer(1));
			y1.setDomainMin(new Integer(10));
			y2.setDomainMin(new Integer(20));
			y3.setDomainMin(new Integer(30));
			z.setDomainMax(new Integer(90));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));	
	}
	
	public void testProdConstraintViolationEQGenericGenericGenericSameIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
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
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationEQGenericGenericGenericDiffIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(49));
			z2.setDomainMax(new Integer(49));
			z3.setDomainMax(new Integer(49));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testProfConstraintViolationEQGenericGenericGenericSimilarIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, zkexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainMin(new Integer(5));
			x2.setDomainMin(new Integer(6));
			x3.setDomainMin(new Integer(7));
			y1.setDomainMin(new Integer(5));
			y2.setDomainMin(new Integer(6));
			y3.setDomainMin(new Integer(7));
			z1.setDomainMax(new Integer(41));
			z2.setDomainMax(new Integer(12));
			z3.setDomainMax(new Integer(12));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationNEQGenericVarVarViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, y, z, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(4));
			x2.setDomainValue(new Integer(5));
			x3.setDomainValue(new Integer(6));
			y.setDomainValue(new Integer(10));
			z.setDomainValue(new Integer(50));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testProdConstraintViolationNEQGenericGenericVarSameIndexNoViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.NEQ);
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
	
	public void testProdConstraintViolationNEQGenericGenericGenericDiffIndexViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yjexpr, zkexpr, ThreeVarConstraint.NEQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x1.setDomainValue(new Integer(4));
			y2.setDomainValue(new Integer(8));
			z3.setDomainValue(new Integer(32));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("constraint is violated", constraint.isViolated(false));
	}	
	
	public void testProdConstraintViolationGTGenericConstGenericVarViolate() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.GT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
			x1.setDomainMax(new Integer(45));
			x2.setDomainMax(new Integer(25));
			x3.setDomainMax(new Integer(15));
			store.propagate();
			assertEquals("the max should be 89",89,z1.getMax());
			assertEquals("the max should be 74",74,z2.getMax());
			assertEquals("the max should be 59",59,z3.getMax());
			
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			x1.setValue(40);
			x2.setValue(20);
			x3.setValue(12);
			store.propagate();
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is true", constraint.isTrue());
			assertEquals("z1 should be 79",79,z1.getMax());
			assertEquals("z2 should be 59",59,z2.getMax());
			assertEquals("z3 should be 47",47,z3.getMax());
			z1.setValue(77);
			z2.setValue(40);
			z3.setValue(30);
			store.propagate();
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testProdConstraintViolationGTGenericVarGenericConst() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, ziexpr, yiconst, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
			store.propagate();
			assertEquals("the max should be 2",2,x1.getMax());
			assertEquals("the max should be 3",3,x2.getMax());
			assertEquals("the max should be 4",4,x3.getMax());
			assertEquals("the max should be 2",2,z1.getMax());
			assertEquals("the max should be 3",3,z2.getMax());
			assertEquals("the max should be 4",4,z3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			x1.setValue(1);
			x2.setValue(3);
			x3.setValue(2);
			store.propagate();
			assertTrue("x should be bound now",xiexpr.isBound());
			assertEquals("the max of z1 should be 2",2,z1.getMax());
			assertEquals("the max of z2 should be 1",1,z2.getMax());
			assertEquals("the max of z3 should be 2",2,z3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testProdConstraintViolationGTGenericVarDoubleIndexedGenericConst() {
		ProdConstraint constraint = new ProdConstraint(xiexpr, yijconst, ziexpr, ThreeVarConstraint.LT);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
		    store.addConstraint(constraint);
			store.propagate();
			assertEquals("the max should be 4",4,x1.getMax());
			assertEquals("the max should be 1",1,x2.getMax());
			assertEquals("the max should be 1",1,x3.getMax());
			assertFalse("constraint is not false", constraint.isFalse());
			assertFalse("constraint is not true", constraint.isTrue());
			x1.setValue(1);
			x2.setValue(1);
			x3.setValue(0);
			store.propagate();
			assertEquals("the min of z1 should be 21",21,z1.getMin());
			assertEquals("the min of z2 should be 51",51,z2.getMin());
			assertEquals("the min of z3 should be 1",1,z3.getMin());
			
			assertFalse("constraint is not false", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testProdConstraintPropagationLEQWithPositiveAndNegativeZValuesGeneric() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a = new IntVariable("a", -6, -2);
            IntVariable b1 = new IntVariable("b1", -100, 100);
            IntVariable b2 = new IntVariable("b2", -100, 100);
            IntVariable b3 = new IntVariable("b3", -100, 100);
            CspGenericIntExpr biexpr = varFactory.genericInt("bi", idxI, new CspIntVariable[]{b1, b2, b3});
            IntVariable c = new IntVariable("c", -18, 12);
            
            solver.addConstraint(a.multiply(biexpr).leq(c));
            
            solver.propagate();
            
            assertEquals("min of b1 is -6", -6, b1.getMin());
            assertEquals("max of b1 is 100", 100, b1.getMax());
            assertEquals("min of b2 is -6", -6, b2.getMin());
            assertEquals("max of b2 is 100", 100, b2.getMax());
            assertEquals("min of b3 is -6", -6, b3.getMin());
            assertEquals("max of b3 is 100", 100, b3.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testProdConstraintPropagationGTWithPositiveAndNegativeZValuesGeneric() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            IntVariable a1 = new IntVariable("a1", -6, -2);
            IntVariable a2 = new IntVariable("a2", -6, 100);
            IntVariable a3 = new IntVariable("a3", -6, 100);
            CspGenericIntExpr aiexpr = varFactory.genericInt("ai", idxI, new CspIntVariable[]{a1, a2, a3});
            IntVariable b1 = new IntVariable("b1", -100, 100);
            IntVariable b2 = new IntVariable("b2", -100, 100);
            IntVariable b3 = new IntVariable("b3", -100, 100);
            CspGenericIntExpr biexpr = varFactory.genericInt("bi", idxI, new CspIntVariable[]{b1, b2, b3});
            IntVariable c = new IntVariable("c", -18, 12);
            
            solver.addConstraint(aiexpr.multiply(biexpr).gt(c));
            
            solver.propagate();
            
            assertEquals("min of b1 is -100", -100, b1.getMin());
            assertEquals("max of b1 is 8", 8, b1.getMax());
            assertEquals("min of b2 is -100", -100, b2.getMin());
            assertEquals("max of b2 is 100", 100, b2.getMax());
            assertEquals("min of b3 is -100", -100, b3.getMin());
            assertEquals("max of b3 is 100", 100, b3.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
