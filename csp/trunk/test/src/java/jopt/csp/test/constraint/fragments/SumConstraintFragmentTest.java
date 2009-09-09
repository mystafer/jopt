
package jopt.csp.test.constraint.fragments;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SumConstraint violation and propagation with fragments
 * 
 * @author Chris Johnson
 */
public class SumConstraintFragmentTest extends TestCase {

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
	GenericIntExpr zjexpr;
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
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yikexpr = (GenericIntExpr)varFactory.genericInt("yik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zjexpr = (GenericIntExpr)varFactory.genericInt("zj", idxJ, new CspIntVariable[]{z1, z2, z3});
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
		zjexpr = null;
		zkexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}
	
	public void testSumConstraintViolateEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x1.setValue(10);
	        y1.setValue(10);
	        z.setValue(21);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x1.setValue(10);
	        y1.setValue(10);
	        z.setValue(20);
	        x2.setValue(11);
	        y2.setValue(12);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.EQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        x1.setValue(10);
	        y1.setValue(10);
	        x2.setValue(11);
	        y2.setValue(12);
	        assertEquals("max of z is 20", 20, z.getMax());
	        assertEquals("min of z is 20", 20, z.getMin());
	        
	        assertEquals("max of x3 is 100", 100, x3.getMax());
	        assertEquals("min of x3 is 0", 0, x3.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintViolateEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, ziexpr, ThreeVarConstraint.EQ);
	        idxI.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x21.setValue(10);
	        y21.setValue(30);
	        x22.setValue(20);
	        y22.setValue(20);
	        x23.setValue(30);
	        y23.setValue(11);
	        z2.setValue(40);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, ziexpr, ThreeVarConstraint.EQ);
	        idxI.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x21.setValue(10);
	        y21.setValue(30);
	        x22.setValue(20);
	        y22.setValue(20);
	        x23.setValue(30);
	        y23.setValue(10);
	        z2.setValue(40);
	        
	        x31.setValue(21);
	        y31.setValue(21);
	        z3.setValue(40);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, ziexpr, ThreeVarConstraint.EQ);
	        idxI.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        y21.setValue(30);
	        z2.setValue(40);
	        assertEquals("max of x21 is 10", 10, x21.getMax());
	        assertEquals("min of x21 is 10", 10, x21.getMin());
	        
	        y31.setValue(10);
	        z3.setValue(21);
	        assertEquals("max of x31 is 100", 100, x31.getMax());	        
	        assertEquals("min of x31 is 0", 0, x31.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintViolateEQGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, zjexpr, ThreeVarConstraint.EQ);
	        idxJ.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x12.setValue(10);
	        y12.setValue(30);
	        x22.setValue(20);
	        y22.setValue(20);
	        x32.setValue(30);
	        y32.setValue(11);
	        z2.setValue(40);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateEQGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, zjexpr, ThreeVarConstraint.EQ);
	        idxJ.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x12.setValue(10);
	        y12.setValue(30);
	        x22.setValue(20);
	        y22.setValue(20);
	        x32.setValue(30);
	        y32.setValue(10);
	        z2.setValue(40);
	        
	        x13.setValue(21);
	        y13.setValue(21);
	        z3.setValue(40);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationEQGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, zjexpr, ThreeVarConstraint.EQ);
	        idxJ.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        y12.setValue(30);
	        z2.setValue(40);
	        assertEquals("max of x12 is 10", 10, x12.getMax());
	        assertEquals("min of x12 is 10", 10, x12.getMin());
	        
	        y13.setValue(10);
	        z3.setValue(21);
	        assertEquals("max of x13 is 100", 100, x13.getMax());	        
	        assertEquals("min of x13 is 0", 0, x13.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}

	public void testSumConstraintViolateNEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.NEQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x11.setValue(10);
	        y11.setValue(30);
	        x12.setValue(20);
	        y12.setValue(22);
	        x13.setValue(30);
	        y13.setValue(13);
	        z.setValue(40);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateNEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.NEQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x11.setValue(10);
	        y11.setValue(31);
	        x12.setValue(20);
	        y12.setValue(22);
	        x13.setValue(30);
	        y13.setValue(13);
	        z.setValue(40);
	        
	        x21.setValue(10);
	        y21.setValue(30);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationNEQGenericIFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.NEQ);
	        idxI.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        x11.setValue(10);
	        y11.setValue(31);
	        x12.setValue(20);
	        y12.setValue(22);
	        x13.setValue(30);
	        y13.setValue(13);
	        
	        x21.setValue(10);
	        y21.setValue(30);

	        assertFalse("z cannot be 41", z.isInDomain(41));
	        assertFalse("z cannot be 42", z.isInDomain(42));
	        assertFalse("z cannot be 43", z.isInDomain(43));
	        assertTrue("z can be 40", z.isInDomain(40));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintViolateLTGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.LT);
	        idxJ.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x11.setValue(10);
	        y11.setValue(30);
	        x21.setValue(18);
	        y21.setValue(20);
	        x31.setValue(30);
	        y31.setValue(7);
	        z.setValue(40);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateLTGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.LT);
	        idxJ.changeVal(0);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x11.setValue(9);
	        y11.setValue(30);
	        x21.setValue(18);
	        y21.setValue(20);
	        x31.setValue(30);
	        y31.setValue(7);
	        z.setValue(40);
	        
	        x12.setValue(41);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationLTGenericJFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xijexpr, yijexpr, z, ThreeVarConstraint.LT);
	        idxJ.changeVal(1);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxJ});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        y12.setValue(30);
	        z.setValue(40);
	        assertEquals("max of x12 is 9", 9, x12.getMax());
	        assertEquals("min of x12 is 0", 0, x12.getMin());
	        
	        y13.setValue(10);
	        assertEquals("max of x13 is 100", 100, x13.getMax());	        
	        assertEquals("min of x13 is 0", 0, x13.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintViolateGEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GEQ);
	        idxI.changeVal(2);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x3.setValue(10);
	        y3.setValue(29);
	        z.setValue(40);
	        assertTrue("frag is now violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintNoViolateGEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GEQ);
	        idxI.changeVal(2);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        assertFalse("frag is not violated yet", frag.isViolated(false));
	        x3.setValue(10);
	        y3.setValue(30);
	        x2.setValue(15);
	        y2.setValue(24);
	        z.setValue(40);
	        assertFalse("frag is not violated", frag.isViolated(false));
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSumConstraintPropagationGEQGenericFragment() {
	    try {
	        SumConstraint constraint = new SumConstraint(xiexpr, yiexpr, z, ThreeVarConstraint.GEQ);
	        idxI.changeVal(2);
	        SumConstraint frag = (SumConstraint) constraint.getGraphConstraintFragment(new GenericIndex[]{idxI});
	        store.addConstraint(frag);
	        store.setAutoPropagate(true);
	        
	        y3.setValue(30);
	        z.setValue(40);
	        assertEquals("max of x3 is 100", 100, x3.getMax());
	        assertEquals("min of x3 is 10", 10, x3.getMin());
	        
	        y2.setValue(10);
	        assertEquals("max of x2 is 100", 100, x2.getMax());	        
	        assertEquals("min of x2 is 0", 0, x2.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
}
