/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint.generics.constantGen;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SumConstraint violation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class ConstantGenericSumConstraintTest extends TestCase {

    GenericIntConstant yiconst;
    GenericIntConstant yjconst;
    GenericIntConstant yijconst;
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
	GenericIntExpr ziexpr;
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

        yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{2,6,5});
        yjconst = new GenericIntConstant("yjconst", new GenericIndex[]{idxJ}, new int[]{36,19,23});
        yijconst = new GenericIntConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new int[]{10,15,20,30,40,50,75,85,95});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
	}
	
	public void tearDown() {
	    yiconst = null;
	    yjconst = null;
	    yijconst = null;
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
		ziexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}
	
	public void testConstantGetMinMax() {
	    assertEquals(yiconst.getMax(),new Integer(6));
	    assertEquals(yiconst.getMin(),new Integer(2));
	    assertEquals(yiconst.getNumConstants()[2],new MutableNumber((int)5));
	    GenericIntConstant numconst = (GenericIntConstant)yiconst.generateNumConstant(new Integer(3));
	    assertEquals(numconst.getConstantCount(),1);
	    assertEquals(numconst.getMax(),new Integer(3));
	    assertEquals(numconst.getMin(),new Integer(3));
	    
	}
	public void testConstantSumEq() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		try {
			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(35));
			assertFalse("constraint is not false yet", constraint.isFalse());
			z1.setDomainMin(new Integer(51));
			assertFalse("constraint is not false yet", constraint.isFalse());
			z1.setDomainMin(new Integer(52));
			assertFalse("constraint is not violated yet", constraint.isFalse());
			z1.setDomainMin(new Integer(53));
			assertTrue("constraint is now false", constraint.isFalse());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}

	public void testSumConstantEq2() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		try {
			x2.setDomainMax(new Integer(50));
			z2.setDomainMin(new Integer(35));
			assertFalse("constraint is not violated yet", constraint.isFalse());
			z2.setDomainMin(new Integer(51));
			assertFalse("constraint is not violated yet", constraint.isFalse());
			z2.setDomainMin(new Integer(53));
			assertFalse("constraint is not violated yet", constraint.isFalse());
			z2.setDomainMin(new Integer(57));
			assertTrue("constraint is violated", constraint.isFalse());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
	public void testSumConstantTrue() {
		SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			x1.setValue(17);
			z2.setValue(35);
			x3.setValue(69);
			z3.setValue(74);
			assertFalse("constraint is not violated yet", constraint.isTrue());
			x2.setValue(29);
			assertFalse("constraint is not violated yet", constraint.isTrue());
			z1.setValue(19);
			assertFalse("constraint is not false ", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testAPISumConstantTrue() {
		CspConstraint constraint = ((CspGenericIntExpr)xiexpr.add(yiconst)).eq(ziexpr);
	    //SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			x1.setValue(17);
			z2.setValue(35);
			x3.setValue(69);
			z3.setValue(74);
			assertFalse("constraint is not violated yet", constraint.isTrue());
			x2.setValue(29);
			assertFalse("constraint is not violated yet", constraint.isTrue());
			z1.setValue(19);
			assertFalse("constraint is not false ", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testSumConstantDiffIndex() {
		SumConstraint constraint = new SumConstraint(xiexpr, yjconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not violated yet", constraint.isViolated(false));
		try {
			x2.setDomainMax(new Integer(50));
			z2.setDomainMin(new Integer(35));
			assertFalse("constraint is not violated yet", constraint.isViolated(false));
			z2.setDomainMin(new Integer(69));
			assertFalse("constraint is not violated yet", constraint.isViolated(false));
			z2.setDomainMin(new Integer(70));
			assertTrue("constraint is violated", constraint.isViolated(false));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
	
	public void testSumConstantDoubleIndex() {
		SumConstraint constraint = new SumConstraint(xiexpr, yijconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(35));
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z1.setDomainMin(new Integer(60));
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z1.setDomainMin(new Integer(61));
			assertTrue("constraint is false ", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
	public void testFragementCreationFalse() {
	    SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		idxI.changeVal(0);
		GraphConstraint singleConst = constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
		try {
			x2.setDomainMax(new Integer(50));
			z2.setDomainMin(new Integer(35));
			assertFalse("entire constraint is not false yet", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			z2.setDomainMin(new Integer(69));
			assertTrue("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(52));
			assertTrue("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(53));
			assertTrue("entire constraint is false", constraint.isFalse());
			assertTrue("single constraint is false", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	public void testFragementCreationTrue() {
	    SumConstraint constraint = new SumConstraint(xiexpr, yiconst, ziexpr, ThreeVarConstraint.EQ);
		assertFalse("constraint is not false yet", constraint.isFalse());
		idxI.changeVal(1);
		GraphConstraint singleConst = constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
		try {

			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(52));
			assertFalse("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			x1.setDomainMax(new Integer(50));
			z1.setDomainMin(new Integer(53));
			assertTrue("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is false", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			x2.setValue(12);
			assertTrue("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertFalse("single constraint is not true yet", singleConst.isTrue());
			z2.setValue(18);
			assertTrue("entire constraint is false", constraint.isFalse());
			assertFalse("single constraint is not false yet", singleConst.isFalse());
			assertTrue("single constraint is true", singleConst.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
	
}
