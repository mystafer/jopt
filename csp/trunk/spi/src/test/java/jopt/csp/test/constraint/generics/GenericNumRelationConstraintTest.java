package jopt.csp.test.constraint.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
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
 * Test for NumRelationConstraint violation and propagation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class GenericNumRelationConstraintTest extends TestCase {

	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable x11;
	IntVariable x12;
	IntVariable x13;
	IntVariable x21;
	IntVariable x22;
	IntVariable x23;
	IntVariable x31;
	IntVariable x32;
	IntVariable x33;
	IntVariable x111;
	IntVariable x121;
	IntVariable x131;
	IntVariable x211;
	IntVariable x221;
	IntVariable x231;
	IntVariable x311;
	IntVariable x321;
	IntVariable x331;
	IntVariable x112;
	IntVariable x122;
	IntVariable x132;
	IntVariable x212;
	IntVariable x222;
	IntVariable x232;
	IntVariable x312;
	IntVariable x322;
	IntVariable x332;
	IntVariable x113;
	IntVariable x123;
	IntVariable x133;
	IntVariable x213;
	IntVariable x223;
	IntVariable x233;
	IntVariable x313;
	IntVariable x323;
	IntVariable x333;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable y11;
	IntVariable y12;
	IntVariable y13;
	IntVariable y21;
	IntVariable y22;
	IntVariable y23;
	IntVariable y31;
	IntVariable y32;
	IntVariable y33;
	IntVariable y111;
	IntVariable y121;
	IntVariable y131;
	IntVariable y211;
	IntVariable y221;
	IntVariable y231;
	IntVariable y311;
	IntVariable y321;
	IntVariable y331;
	IntVariable y112;
	IntVariable y122;
	IntVariable y132;
	IntVariable y212;
	IntVariable y222;
	IntVariable y232;
	IntVariable y312;
	IntVariable y322;
	IntVariable y332;
	IntVariable y113;
	IntVariable y123;
	IntVariable y133;
	IntVariable y213;
	IntVariable y223;
	IntVariable y233;
	IntVariable y313;
	IntVariable y323;
	IntVariable y333;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	IntVariable b1;
	IntVariable b2;
	IntVariable b3;
	GenericIntExpr xiexpr;
	GenericIntExpr aiexpr;
	GenericIntExpr xijexpr;
	GenericIntExpr xijkexpr;
	GenericIntExpr yiexpr;
	GenericIntExpr yjexpr;
	GenericIntExpr yijexpr;
	GenericIntExpr yijkexpr;
	GenericIntExpr zjexpr;
	GenericIntExpr bjexpr;
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
        x1 = new IntVariable("x1", 0, 1);
        x2 = new IntVariable("x2", 0, 1);
        x3 = new IntVariable("x3", 0, 1);
        a1 = new IntVariable("a1", 0, 100);
        a2 = new IntVariable("a2", 0, 100);
        a3 = new IntVariable("a3", 0, 100);
        x11 = new IntVariable("x11", 0, 1);
        x12 = new IntVariable("x12", 0, 1);
        x13 = new IntVariable("x13", 0, 1);        
        x21 = new IntVariable("x21", 0, 1);
        x22 = new IntVariable("x22", 0, 1);
        x23 = new IntVariable("x23", 0, 1);
        x31 = new IntVariable("x31", 0, 1);
        x32 = new IntVariable("x32", 0, 1);
        x33 = new IntVariable("x33", 0, 1);
        x111 = new IntVariable("x111", 0, 1);
        x121 = new IntVariable("x121", 0, 1);
        x131 = new IntVariable("x131", 0, 1);        
        x211 = new IntVariable("x211", 0, 1);
        x221 = new IntVariable("x222", 0, 1);
        x231 = new IntVariable("x231", 0, 1);
        x311 = new IntVariable("x311", 0, 1);
        x321 = new IntVariable("x321", 0, 1);
        x331 = new IntVariable("x331", 0, 1);
        x112 = new IntVariable("x112", 0, 1);
        x122 = new IntVariable("x122", 0, 1);
        x132 = new IntVariable("x132", 0, 1);        
        x212 = new IntVariable("x212", 0, 1);
        x222 = new IntVariable("x222", 0, 1);
        x232 = new IntVariable("x232", 0, 1);
        x312 = new IntVariable("x312", 0, 1);
        x322 = new IntVariable("x322", 0, 1);
        x332 = new IntVariable("x332", 0, 1);
        x113 = new IntVariable("x113", 0, 1);
        x123 = new IntVariable("x123", 0, 1);
        x133 = new IntVariable("x133", 0, 1);        
        x213 = new IntVariable("x213", 0, 1);
        x223 = new IntVariable("x223", 0, 1);
        x233 = new IntVariable("x233", 0, 1);
        x313 = new IntVariable("x313", 0, 1);
        x323 = new IntVariable("x323", 0, 1);
        x333 = new IntVariable("x333", 0, 1);
        y1 = new IntVariable("y1", 0, 1);
        y2 = new IntVariable("y2", 0, 1);
        y3 = new IntVariable("y3", 0, 1);
        y11 = new IntVariable("y11", 0, 1);
        y12 = new IntVariable("y12", 0, 1);
        y13 = new IntVariable("y13", 0, 1);        
        y21 = new IntVariable("y21", 0, 1);
        y22 = new IntVariable("y22", 0, 1);
        y23 = new IntVariable("y23", 0, 1);
        y31 = new IntVariable("y31", 0, 1);
        y32 = new IntVariable("y32", 0, 1);
        y33 = new IntVariable("y33", 0, 1);
        y111 = new IntVariable("y11", 0, 1);
        y121 = new IntVariable("y12", 0, 1);
        y131 = new IntVariable("y13", 0, 1);        
        y211 = new IntVariable("y21", 0, 1);
        y221 = new IntVariable("y22", 0, 1);
        y231 = new IntVariable("y23", 0, 1);
        y311 = new IntVariable("y31", 0, 1);
        y321 = new IntVariable("y32", 0, 1);
        y331 = new IntVariable("y33", 0, 1);
        y112 = new IntVariable("y11", 0, 1);
        y122 = new IntVariable("y12", 0, 1);
        y132 = new IntVariable("y13", 0, 1);        
        y212 = new IntVariable("y21", 0, 1);
        y222 = new IntVariable("y22", 0, 1);
        y232 = new IntVariable("y23", 0, 1);
        y312 = new IntVariable("y31", 0, 1);
        y322 = new IntVariable("y32", 0, 1);
        y332 = new IntVariable("y33", 0, 1);
        y113 = new IntVariable("y11", 0, 1);
        y123 = new IntVariable("y12", 0, 1);
        y133 = new IntVariable("y13", 0, 1);        
        y213 = new IntVariable("y21", 0, 1);
        y223 = new IntVariable("y22", 0, 1);
        y233 = new IntVariable("y23", 0, 1);
        y313 = new IntVariable("y31", 0, 1);
        y323 = new IntVariable("y32", 0, 1);
        y333 = new IntVariable("y33", 0, 1);
        z1 = new IntVariable("z1", 0, 1);
        z2 = new IntVariable("z2", 0, 1);
        z3 = new IntVariable("z3", 0, 1);
        b1 = new IntVariable("b1", 0, 100);
        b2 = new IntVariable("b2", 0, 100);
        b3 = new IntVariable("b3", 0, 100);
        y = new IntVariable("y", 0, 1);
        z = new IntVariable("z", 0, 1);
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        aiexpr = (GenericIntExpr)varFactory.genericInt("ai", idxI, new CspIntVariable[]{a1, a2, a3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        xijkexpr = (GenericIntExpr)varFactory.genericInt("xijk", new GenericIndex[]{idxI, idxJ, idxK}, new CspIntVariable[]{x111,x121,x131,x211,x221,x231,x311,x321,x331,
                																											x112,x122,x132,x212,x222,x232,x312,x322,x332,
                																											x113,x123,x133,x213,x223,x233,x313,x323,x333});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yijkexpr = (GenericIntExpr)varFactory.genericInt("yijk", new GenericIndex[]{idxI, idxJ, idxK}, new CspIntVariable[]{y111,y121,y131,y211,y221,y231,y311,y321,y331,
																															y112,y122,y132,y212,y222,y232,y312,y322,y332,
																															y113,y123,y133,y213,y223,y233,y313,y323,y333});
        zjexpr = (GenericIntExpr)varFactory.genericInt("zj", idxJ, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
        bjexpr = (GenericIntExpr)varFactory.genericInt("bj", idxJ, new CspIntVariable[]{b1, b2, b3});
	}
	
	public void tearDown() {
		x1 = null;
		x2 = null;
		x3 = null;
		a1 = null;
		a2 = null;
		a3 = null;
		x11 = null;
		x12 = null;
		x13 = null;
		x21 = null;
		x22 = null;
		x23 = null;
		x31 = null;
		x32 = null;
		x33 = null;
		x111 = null;
		x121 = null;
		x131 = null;
		x211 = null;
		x221 = null;
		x231 = null;
		x311 = null;
		x321 = null;
		x331 = null;
		x112 = null;
		x122 = null;
		x132 = null;
		x212 = null;
		x222 = null;
		x232 = null;
		x312 = null;
		x322 = null;
		x332 = null;
		x113 = null;
		x123 = null;
		x133 = null;
		x213 = null;
		x223 = null;
		x233 = null;
		x313 = null;
		x323 = null;
		x333 = null;
		y1 = null;
		y2 = null;
		y3 = null;
		y11 = null;
		y12 = null;
		y13 = null;
		y21 = null;
		y22 = null;
		y23 = null;
		y31 = null;
		y32 = null;
		y33 = null;
		y111 = null;
		y121 = null;
		y131 = null;
		y211 = null;
		y221 = null;
		y231 = null;
		y311 = null;
		y321 = null;
		y331 = null;
		y112 = null;
		y122 = null;
		y132 = null;
		y212 = null;
		y222 = null;
		y232 = null;
		y312 = null;
		y322 = null;
		y332 = null;
		y113 = null;
		y123 = null;
		y133 = null;
		y213 = null;
		y223 = null;
		y233 = null;
		y313 = null;
		y323 = null;
		y333 = null;
		z1 = null;
		z2 = null;
		z3 = null;
		b1 = null;
		b2 = null;
		b3 = null;
		xiexpr = null;
		aiexpr = null;
		xijexpr = null;
		xijkexpr = null;
		yiexpr = null;
		yjexpr = null;
		yijexpr = null;
		yijkexpr = null;
		zjexpr = null;
		bjexpr = null;
		zkexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}
	
	public void testGenericNEQGenericDiffIndexViolate() {
	    NumConstraint constraint = null;
	    try {
	        constraint = (NumConstraint) aiexpr.neq(bjexpr);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        a1.setValue(12);
	        b3.setValue(12);
	        assertTrue("constraint is now violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testRelationConstraintPropagationLEQGenericVarCase1() {
	    try {
	        store.addConstraint(xiexpr.leq(z));
	        x1.setValue(0);
	        x2.setValue(0);
	        x3.setValue(1);
	        assertTrue("z still has a min of 0", z.getMin() == 0);
	        assertFalse("z is not bound", z.isBound());
	        store.propagate();
		}
		catch (PropagationFailureException pfe) {
		    fail();
		}
		assertTrue("z has a min of 1", z.getMin() == 1);
	}
	
	public void testRelationConstraintPropagationLEQGenericVarCase2() {
	    try {
	        store.addConstraint(xiexpr.leq(z));
	        z.setValue(0);
	        assertTrue("xiexpr still has a max of 1", xiexpr.getSmallestMax() == 1);
	        assertFalse("xiexpr is not bound", xiexpr.isBound());
	        store.propagate();
		}
		catch (PropagationFailureException pfe) {
		    fail();
		}
		assertTrue("xiepr has a max of 0", xiexpr.getLargestMax() == 0);
	}
	
	public void testRelationConstraintPropagationLEQGenericIJGenericJCase1() {
	    try {
	        store.addConstraint(xijexpr.leq(zjexpr));
	        x11.setValue(0);
	        x12.setValue(0);
	        x13.setValue(1);
	        x21.setValue(0);
	        x22.setValue(0);
	        x23.setValue(0);
	        x31.setValue(0);
	        x32.setValue(0);
	        x33.setValue(0);
	        store.propagate();
		}
		catch (PropagationFailureException pfe) {
		    fail();
		}
		assertTrue("z3 has a min of 1", z3.getMin() == 1);
		assertTrue("z2 has a min of 0", z2.getMin() == 0);
		assertTrue("z1 has a min of 0", z1.getMin() == 0);
		assertFalse("zj is not bound", zjexpr.isBound());
		assertTrue("z3 is bound", z3.isBound());
	}
	
	public void testRelationConstraintPropagationLEQGenericIJGenericJCase2() {
	    try {
	        store.addConstraint(xijexpr.leq(zjexpr));
	        z3.setValue(0);
	        store.propagate();
		}
		catch (PropagationFailureException pfe) {
		    fail();
		}
		assertTrue("x13 has a max of 0", x13.getMax() == 0);
		assertTrue("x23 has a max of 0", x23.getMax() == 0);
		assertTrue("x33 has a max of 0", x33.getMax() == 0);
		assertTrue("x13 is bound", x13.isBound());
		assertTrue("x23 is bound", x23.isBound());
		assertTrue("x33 is bound", x33.isBound());
		assertTrue("x11 has a max of 1", x11.getMax() == 1);
		assertTrue("x12 has a max of 1", x12.getMax() == 1);
		assertTrue("x21 has a max of 1", x21.getMax() == 1);
		assertTrue("x22 has a max of 1", x22.getMax() == 1);
		assertTrue("x31 has a max of 1", x31.getMax() == 1);
		assertTrue("x32 has a max of 1", x32.getMax() == 1);
		assertFalse("xijexpr is not bound", xijexpr.isBound());
	}
	
	public void testGenericVarLEQSameIndexDouble() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
		    solver.setAutoPropagate(false);
		    
		    CspConstraint constraint = xijexpr.leq(yijexpr);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y11.setValue(1);
	        assertTrue(solver.propagate());
	        
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y12.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x12 is bound", x12.isBound());
	        assertEquals("x12 is 0", 0, x12.getMin());
	        assertEquals("x12 is 0", 0, x12.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x13 is still unbound", x11.isBound());
	        assertFalse("x21 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x23 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("x32 is still unbound", x11.isBound());
	        assertFalse("x33 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y13.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x13 is bound", x13.isBound());
	        assertEquals("x13 is 0", 0, x13.getMin());
	        assertEquals("x13 is 0", 0, x13.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x21 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x23 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("x32 is still unbound", x11.isBound());
	        assertFalse("x33 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y23.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x23 is bound", x23.isBound());
	        assertEquals("x23 is 0", 0, x23.getMin());
	        assertEquals("x23 is 0", 0, x23.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x21 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("x32 is still unbound", x11.isBound());
	        assertFalse("x33 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y32.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x32 is bound", x32.isBound());
	        assertEquals("x32 is 0", 0, x32.getMin());
	        assertEquals("x32 is 0", 0, x32.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x21 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("x33 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y21.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x21 is bound", x21.isBound());
	        assertEquals("x21 is 0", 0, x21.getMin());
	        assertEquals("x21 is 0", 0, x21.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("x33 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y33.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x33 is bound", x33.isBound());
	        assertEquals("x33 is 0", 0, x33.getMin());
	        assertEquals("x33 is 0", 0, x33.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("x31 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y31.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x31 is bound", x31.isBound());
	        assertEquals("x31 is 0", 0, x31.getMin());
	        assertEquals("x31 is 0", 0, x31.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertFalse("x22 is still unbound", x11.isBound());
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y22.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x22 is bound", x22.isBound());
	        assertEquals("x22 is 0", 0, x22.getMin());
	        assertEquals("x22 is 0", 0, x22.getMax());
	        //the remaining variables are unbound
	        assertFalse("x11 is still unbound", x11.isBound());
	        assertTrue("constraint is true ", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testGenericVarLEQSameIndexTriple() {
	    try {
	        CspSolver solver = CspSolver.createSolver();
		    solver.setAutoPropagate(false);
		    
		    CspConstraint constraint = xijkexpr.leq(yijkexpr);
	        solver.addConstraint(constraint);
	        assertFalse("constraint is not true yet", constraint.isTrue());
	        assertFalse("constraint is not false yet", constraint.isFalse());
	        
	        y111.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x111 is bound", x111.isBound());
	        assertEquals("x111 is 0", 0, x111.getMin());
	        assertEquals("x111 is 0", 0, x111.getMax());
	        
	        y222.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x222 is bound", x222.isBound());
	        assertEquals("x222 is 0", 0, x222.getMin());
	        assertEquals("x222 is 0", 0, x222.getMax());
	        
	        y333.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x333 is bound", x333.isBound());
	        assertEquals("x333 is 0", 0, x333.getMin());
	        assertEquals("x333 is 0", 0, x333.getMax());
	        
	        y321.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x321 is bound", x321.isBound());
	        assertEquals("x321 is 0", 0, x321.getMin());
	        assertEquals("x321 is 0", 0, x321.getMax());
	        
	        y213.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x213 is bound", x213.isBound());
	        assertEquals("x213 is 0", 0, x213.getMin());
	        assertEquals("x213 is 0", 0, x213.getMax());
	        
	        y132.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x132 is bound", x132.isBound());
	        assertEquals("x132 is 0", 0, x132.getMin());
	        assertEquals("x132 is 0", 0, x132.getMax());
	        
	        y113.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x113 is bound", x113.isBound());
	        assertEquals("x113 is 0", 0, x113.getMin());
	        assertEquals("x113 is 0", 0, x113.getMax());
	        
	        y311.setValue(0);
	        assertTrue(solver.propagate());
	        
	        assertTrue("x311 is bound", x311.isBound());
	        assertEquals("x311 is 0", 0, x311.getMin());
	        assertEquals("x311 is 0", 0, x311.getMax());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
}
