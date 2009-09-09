package jopt.csp.test.constraint.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SummationConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class SummationConstraintTest extends TestCase {

    IntVariable w1;
	IntVariable w2;
	IntVariable w3;
	IntVariable w4;
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
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	GenericIntExpr whexpr;
	GenericIntExpr xiexpr;
	GenericIntExpr xijexpr;
	GenericIntExpr xjiexpr;
	GenericIntExpr yiexpr;
	GenericIntExpr yjexpr;
	GenericIntExpr yijexpr;
	GenericIntExpr yjkexpr;
	GenericIntExpr ziexpr;
	GenericIntExpr zjexpr;
	GenericIntExpr zkexpr;
	ConstraintStore store;
	CspVariableFactory varFactory;
	GenericIndex idxH;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
	IntVariable y;
	IntVariable z;
    NumConstraint constraint;
    CspMath math;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
		idxH = new GenericIndex("h", 4);
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
		w1 = new IntVariable("w1", 0, 100);
        w2 = new IntVariable("w2", 0, 100);
        w3 = new IntVariable("w3", 0, 100);
        w4 = new IntVariable("w4", 0, 100);
        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x11 = new IntVariable("x11", 1, 10);
        x12 = new IntVariable("x12", 1, 10);
        x13 = new IntVariable("x13", 1, 10);        
        x21 = new IntVariable("x21", 3, 20);
        x22 = new IntVariable("x22", 3, 20);
        x23 = new IntVariable("x23", 3, 20);
        x31 = new IntVariable("x31", 7, 30);
        x32 = new IntVariable("x32", 7, 30);
        x33 = new IntVariable("x33", 7, 30);
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        y11 = new IntVariable("y11", 0, 100);
        y12 = new IntVariable("y12", 0, 100);
        y13 = new IntVariable("y13", 0, 100);        
        y21 = new IntVariable("y21", 0, 100);
        y22 = new IntVariable("y22", 0, 100);
        y23 = new IntVariable("y23", 0, 100);
        y31 = new IntVariable("y31", 0, 100);
        y32 = new IntVariable("y32", 0, 100);
        y33 = new IntVariable("y33", 0, 100);
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 200);
        whexpr = (GenericIntExpr)varFactory.genericInt("wh", idxH, new CspIntVariable[]{w1, w2, w3, w4});
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        xjiexpr = (GenericIntExpr)varFactory.genericInt("xji", new GenericIndex[]{idxJ, idxI}, new CspIntVariable[]{x11,x21,x31,x12,x22,x32,x13,x23,x33});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yjkexpr = (GenericIntExpr)varFactory.genericInt("yjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zjexpr = (GenericIntExpr)varFactory.genericInt("zj", idxJ, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
        math = store.getConstraintAlg().getVarFactory().getMath();
	}
	
	public void tearDown() {
	    w1 = null;
		w2 = null;
		w3 = null;
		w4 = null;
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
		z1 = null;
		z2 = null;
		z3 = null;
		whexpr = null;
		xiexpr = null;
		xijexpr = null;
		xjiexpr = null;
		yiexpr = null;
		yjexpr = null;
		yijexpr = null;
		yjkexpr = null;
		ziexpr = null;
		zjexpr = null;
		zkexpr = null;
		store = null;
		varFactory = null;
		idxH = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	    constraint = null;
	    math = null;
	}
	
    private class IdxRestriction implements CspGenericIndexRestriction {
        private GenericIndex idx;
        
        public IdxRestriction(GenericIndex idx) {
            this.idx = idx;
        }
        
        public boolean currentIndicesValid() {
            return idx.currentVal() < 3;
        }
    }
    
    private class LessThanTwoIndexRestriction implements CspGenericIndexRestriction {
        private GenericIndex idx;
        
        public LessThanTwoIndexRestriction(GenericIndex idx) {
            this.idx = idx;
        }
        
        public boolean currentIndicesValid() {
            return idx.currentVal() < 2;
        }
    }
    
    private class LessThanThreeIndexRestriction implements CspGenericIndexRestriction {
        private GenericIndex idx;
        
        public LessThanThreeIndexRestriction(GenericIndex idx) {
            this.idx = idx;
        }
        
        public boolean currentIndicesValid() {
            return idx.currentVal() < 3;
        }
    }
    
	public void testSummationLTNoViolate() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).lt(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMin(new Integer(5));
	        x2.setDomainMin(new Integer(2));
	        x3.setDomainMin(new Integer(1));
	        z.setDomainMax(new Integer(9));
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationLTViolate() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).lt(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMin(new Integer(5));
	        x2.setDomainMin(new Integer(3));
	        x3.setDomainMin(new Integer(1));
	        z.setDomainMax(new Integer(9));
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationGTEViolate() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).geq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMax(new Integer(5));
	        x2.setDomainMax(new Integer(2));
	        x3.setDomainMax(new Integer(1));
	        z.setDomainMin(new Integer(9));
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		    assertTrue("verify propagation fails", 1==1);
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationGTENoViolateEqual() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).geq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMax(new Integer(5));
	        x2.setDomainMax(new Integer(4));
	        x3.setDomainMax(new Integer(16));
	        z.setDomainMin(new Integer(25));
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationGTENoViolateNotEqual() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).geq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMax(new Integer(5));
	        x2.setDomainMax(new Integer(4));
	        x3.setDomainMax(new Integer(16));
	        z.setDomainMin(new Integer(12));
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationNoViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z1.setValue(4);
	        z2.setValue(6);
	        z3.setValue(5);
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z1.setValue(6);
	        z2.setValue(6);
	        z3.setValue(6);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationWithRestrictionsNoViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestriction);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(90);
	        z1.setValue(4);
	        z2.setValue(5);
	        z3.setValue(70);
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationWithRestrictionViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z1.setValue(4);
	        z2.setValue(6);
	        z3.setValue(5);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
		assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationNEQSummationWithRestrictionsNoViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestriction);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.neq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(6);
	        x2.setValue(4);
	        x3.setValue(2);
	        z1.setValue(3);
	        z2.setValue(5);
	        z3.setValue(4);
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationNEQSummationWithRestrictionsViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestriction);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.neq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        z1.setValue(4);
	        z2.setValue(5);
	        assertTrue("constraint is now violated", constraint.isViolated(false));
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
	}
	
	public void testSummationNEQSummationWithDiffRestrictionsViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestrictionI = new LessThanTwoIndexRestriction(idxI);
	        LessThanThreeIndexRestriction idxRestrictionH = new LessThanThreeIndexRestriction(idxH);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestrictionI);
	        CspIntExpr sum2 = math.summation(whexpr, new GenericIndex[]{idxH}, idxRestrictionH);
	        constraint = (NumConstraint) sum1.neq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        w1.setValue(2);
	        w2.setValue(3);
	        w3.setValue(4);
	        assertTrue("constraint is now violated", constraint.isViolated(false));
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
	}
	
	public void testSummationNEQSummationWithDiffRestrictionsNoViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestrictionI = new LessThanTwoIndexRestriction(idxI);
	        LessThanThreeIndexRestriction idxRestrictionH = new LessThanThreeIndexRestriction(idxH);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestrictionI);
	        CspIntExpr sum2 = math.summation(whexpr, new GenericIndex[]{idxH}, idxRestrictionH);
	        constraint = (NumConstraint) sum1.neq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        w1.setValue(2);
	        w2.setValue(3);
	        w3.setValue(5);
	        assertFalse("constraint is not violated", constraint.isViolated(false));
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
	}
	
	public void testSummationWithRestrictionNEQGenericVarDiffIndexNoViolate() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.neq(zjexpr);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(6);
	        x2.setValue(4);
	        x3.setValue(2);
	        z1.setValue(12);
	        z2.setValue(13);
	        z3.setValue(14);
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationTransposedIndiciesNoViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        y13.setValue(8);
	        y23.setValue(5);
	        y33.setValue(2);
	        //propagation is necessary for the changes to the summation to take place
	        store.propagate();
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQSummationTransposedIndicesViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(4);
	        y21.setValue(4);
	        y31.setValue(4);
	        y12.setValue(5);
	        y22.setValue(5);
	        y32.setValue(5);
	        y13.setValue(6);
	        y23.setValue(6);
	        y33.setValue(6);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
//	public void testSummationEQSummationTransposedIndicesWithTargetRestrictionNoViolate() {
//	    try {
//	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
//	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
//	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
//	        constraint = (NumConstraint) sum1.eq(sum2);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
//	        
//	        y11.setValue(10);
//	        y21.setValue(20);
//	        y31.setValue(30);
//	        y12.setValue(15);
//	        y22.setValue(40);
//	        y32.setValue(60);
//	        y13.setValue(35);
//	        y23.setValue(40);
//	        y33.setValue(60);
//	        //propagation is necessary for the changes to the summation to take place
//	        store.propagate();
//	    }
//	    catch(PropagationFailureException pfe) {
//	        fail();
//	    }
//	    assertFalse("constraint is not violated", constraint.isViolated(false));
//	}
//	
//	public void testSummationEQSummationTransposedIndiciesWithTargetRestrictionViolate() {
//	    try {
//	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
//	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
//	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
//	        constraint = (NumConstraint) sum1.eq(sum2);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
//	        
//	        y11.setValue(10);
//	        y21.setValue(20);
//	        y31.setValue(30);
//	        y12.setValue(15);
//	        y22.setValue(40);
//	        y32.setValue(60);
//	        y13.setValue(34);
//	        y23.setValue(40);
//	        y33.setValue(60);
//	        store.propagate();
//	        //unreachable
//	        assertTrue("verify propagation fails", 1==2);
//	    }
//	    catch(PropagationFailureException pfe) {
//		    //propagation, in this case, should cause an exception
//	    }
//	    assertTrue("constraint is now violated", constraint.isViolated(false));
//	}

	public void testSummationEQVarViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        y13.setValue(5);
	        y23.setValue(5);
	        y33.setValue(6);
	        z.setValue(15);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationEQVarNoViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        y13.setValue(5);
	        y23.setValue(5);
	        y33.setValue(5);
	        z.setValue(15);
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationWithRestrictionEQConstViolate() {
	    try {
	        LessThanTwoIndexRestriction sourceRestrictionJ = new LessThanTwoIndexRestriction(idxJ);
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI, idxJ}, sourceRestrictionJ);
	        constraint = (NumConstraint) sum1.eq(100);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(5);
	        y12.setValue(10);
	        y13.setValue(100);
	        y21.setValue(15);
	        y22.setValue(20);
	        y23.setValue(100);
	        y31.setValue(25);
	        y32.setValue(30);
	        y33.setValue(100);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch (PropagationFailureException pfe) {
	        //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationWithRestrictionEQConstNoViolate() {
	    try {
	        LessThanTwoIndexRestriction sourceRestrictionJ = new LessThanTwoIndexRestriction(idxJ);
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI, idxJ}, sourceRestrictionJ);
	        constraint = (NumConstraint) sum1.eq(100);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(5);
	        y12.setValue(10);
	        y13.setValue(100);
	        y21.setValue(15);
	        y22.setValue(20);
	        y23.setValue(100);
	        y31.setValue(25);
	        y32.setValue(25);
	        y33.setValue(100);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationWithRestrictionSingleIndexEQConstViolate() {
	    try {
	        LessThanTwoIndexRestriction sourceRestrictionJ = new LessThanTwoIndexRestriction(idxJ);
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxJ}, sourceRestrictionJ);
	        constraint = (NumConstraint) sum1.eq(100);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(75);
	        y12.setValue(25);
	        y13.setValue(100);
	        y21.setValue(60);
	        y22.setValue(40);
	        y23.setValue(100);
	        y31.setValue(50);
	        y32.setValue(51);
	        y33.setValue(100);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch (PropagationFailureException pfe) {
	        //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testSummationWithRestrictionSingleIndexEQConstNoViolate() {
	    try {
	        LessThanTwoIndexRestriction sourceRestrictionJ = new LessThanTwoIndexRestriction(idxJ);
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxJ}, sourceRestrictionJ);
	        constraint = (NumConstraint) sum1.eq(100);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(75);
	        y12.setValue(25);
	        y13.setValue(100);
	        y21.setValue(60);
	        y22.setValue(40);
	        y23.setValue(100);
	        y31.setValue(50);
	        y32.setValue(50);
	        y33.setValue(100);
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
    public void testVarEQSummationViolate() {
	    try {
	        CspGenericIntExpr sum1 = (CspGenericIntExpr) math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) z.eq(sum1);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        y13.setValue(5);
	        y23.setValue(5);
	        y33.setValue(6);
	        z.setValue(15);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
    
    public void testVarEQSummationNoViolate() {
	    try {
	        CspGenericIntExpr sum1 = (CspGenericIntExpr) math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) z.eq(sum1);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        y13.setValue(5);
	        y23.setValue(5);
	        y33.setValue(5);
	        z.setValue(15);
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is now violated", constraint.isViolated(false));
	}
	
	public void testGenericVarEQSummationSingleIndexViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) zjexpr.eq(sum1);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(6);
	        y32.setValue(8);
	        y13.setValue(5);
	        y23.setValue(7);
	        y33.setValue(9);
	        z1.setValue(15);
	        z2.setValue(18);
	        z3.setValue(22);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testGenericVarEQSummationViolate() {
	    try {
	        LessThanTwoIndexRestriction sourceRestriction = new LessThanTwoIndexRestriction(idxI);
	        constraint = (NumConstraint) ziexpr.eq(math.summation(yiexpr, new GenericIndex[]{idxI}, sourceRestriction));
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y1.setValue(3);
	        y2.setValue(5);
	        y3.setValue(7);
	        z1.setValue(8);
	        z2.setValue(8);
	        z3.setValue(22);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testGenericVarEQSummationSingleIndexNoViolate() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) zjexpr.eq(sum1);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(6);
	        y32.setValue(8);
	        y13.setValue(5);
	        y23.setValue(7);
	        y33.setValue(9);
	        z1.setValue(15);
	        z2.setValue(18);
	        z3.setValue(21);
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	    assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQViolateNotEqualGT() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMax(new Integer(5));
	        x2.setDomainMax(new Integer(4));
	        x3.setDomainMax(new Integer(16));
	        z.setDomainMin(new Integer(52));
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
		assertTrue("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQViolateNotEqualLT() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMin(new Integer(5));
	        x2.setDomainMin(new Integer(4));
	        x3.setDomainMin(new Integer(16));
	        z.setDomainMax(new Integer(12));
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
		assertTrue("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQVarNoViolateNotEqualEQ() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z.setValue(15);
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationEQVarNoViolateNotEqualNEQ() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).eq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMin(new Integer(5));
	        x2.setDomainMax(new Integer(94));
	        x3.setDomainMin(new Integer(16));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationNEQVarNoViolate() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).neq(z);	        
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));
	        
	        x1.setDomainMin(new Integer(5));
	        x2.setDomainMax(new Integer(94));
	        x3.setDomainMin(new Integer(16));
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertFalse("constraint is not violated", constraint.isViolated(false));
	}
	
	public void testSummationNEQVarViolate() {
	    try {
	        IdxRestriction sourceRestriction = new IdxRestriction(idxI);
	        constraint = (NumConstraint) math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction).neq(z);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not violated yet", constraint.isViolated(false));

	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z.setValue(15);
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
		}
		catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		}
        
        
        try {
            store.reset();
            x1.setValue(5);
            x2.setValue(4);
            x3.setValue(6);
            z.setValue(15);
    		assertTrue("constraint is now violated", constraint.isViolated(false));
        }
        catch(PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
	}
	
	public void testSummationSingleIndexEQConstViolate() {
	    try {
	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null).eq(20);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not yet violated", constraint.isViolated(false));

	        y11.setDomainValue(new Integer(5));
	        y21.setDomainMin(new Integer(10));
	        y31.setDomainValue(new Integer(6));
	        store.propagate();
	        //unreachable
	        assertTrue("verify propagation fails", 1==2);
	    }
	    catch (PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
	    }
	    assertTrue("constraint is now violated", constraint.isViolated(false));
	}
	
//	public void testSummationSingleIndexEQConstWithTargetRestrictionViolate() {
//	    try {
//	        LessThanTwoIndexRestriction targetRestriction = new LessThanTwoIndexRestriction(idxJ);
//	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction).eq(20);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated", constraint.isViolated(false));
//	        
//	        y11.setValue(5);
//	        y21.setValue(7);
//	        y31.setValue(8);
//	        y12.setValue(6);
//	        y22.setValue(5);
//	        y32.setValue(10);
//	        store.propagate();
//	        //unreachable
//	        assertTrue("verify propagation fails", 1==2);
//	    }
//	    catch (PropagationFailureException pfe) {
//	        //propagation, in this case, should cause an exception
//	    }
//	    assertTrue("constraint is now violated", constraint.isViolated(false));
//	}
//	
//	public void testSummationSingleIndexEQConstWithTargetRestrictionNoViolate() {
//	    try {
//	        LessThanTwoIndexRestriction targetRestriction = new LessThanTwoIndexRestriction(idxJ);
//	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction).eq(20);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated", constraint.isViolated(false));
//	        
//	        y11.setValue(5);
//	        y21.setValue(7);
//	        y31.setValue(8);
//	        y12.setValue(6);
//	        y22.setValue(5);
//	        y32.setValue(9);
//	    }
//	    catch (PropagationFailureException pfe) {
//	        fail();
//	    }
//	    assertFalse("constraint is not violated", constraint.isViolated(false));
//	}
//	
//	public void testSummationSingleIndexEQVarWithTargetRestrictionViolate() {
//	    try {
//	        LessThanTwoIndexRestriction targetRestriction = new LessThanTwoIndexRestriction(idxI);
//	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxJ}, null, targetRestriction).eq(z);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated", constraint.isViolated(false));
//	        
//	        y11.setValue(5);
//	        y12.setValue(7);
//	        y13.setValue(8);
//	        y21.setValue(6);
//	        y22.setValue(5);
//	        y23.setValue(10);
//	        store.propagate();
//	        //unreachable
//	        assertTrue("verify propagation fails", 1==2);
//	    }
//	    catch (PropagationFailureException pfe) {
//	        //propagation, in this case, should cause an exception
//	    }
//	    assertTrue("constraint is now violated", constraint.isViolated(false));
//	}
//	
//	public void testSummationSingleIndexEQVarWithTargetRestrictionNoViolate() {
//	    try {
//	        LessThanTwoIndexRestriction targetRestriction = new LessThanTwoIndexRestriction(idxJ);
//	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction).eq(20);
//	        store.addConstraint(constraint);
//	        assertFalse("constraint is not violated", constraint.isViolated(false));
//	        
//	        y11.setValue(5);
//	        y12.setValue(7);
//	        y13.setValue(8);
//	        y21.setValue(6);
//	        y22.setValue(5);
//	        y23.setValue(9);
//	    }
//	    catch (PropagationFailureException pfe) {
//	        fail();
//	    }
//	    assertFalse("constraint is not violated", constraint.isViolated(false));
//	}
//	
	public void testSummationSingleIndexEQConstNoViolate() {
	    try {
	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null).eq(20);
	        store.addConstraint(constraint);
	        assertFalse("constraint is not yet violated", constraint.isViolated(false));

	        y11.setDomainMin(new Integer(20));
	        y22.setDomainMin(new Integer(19));
	        y33.setDomainMin(new Integer(18));
	        
	        assertFalse("constraint is not violated", constraint.isViolated(false));
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSummationRestrictedIndexEQVarPropagation() {
	    try {
	        LessThanTwoIndexRestriction sourceRestriction = new LessThanTwoIndexRestriction(idxJ);
	        constraint = (NumConstraint) math.summation(xijexpr, new GenericIndex[]{idxI, idxJ}, sourceRestriction).eq(z);
	        store.addConstraint(constraint);
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("z min is 22", z.getMin() == 22);
		assertTrue("z max is 120", z.getMax() == 120);
	}
	
	public void testSummationUnrestrictedIndexEQVarPropagation() {
	    try {
	        constraint = (NumConstraint) math.summation(xijexpr, new GenericIndex[]{idxI, idxJ}, null).eq(z);
	        store.addConstraint(constraint);
	        store.propagate();
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		assertTrue("z min is 33", z.getMin() == 33);
		assertTrue("z max is 180", z.getMax() == 180);
	}
	
	public void testSummationSingleIndexEQConstCompletePropagation() {
	    try {
	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null).eq(20);
	        store.addConstraint(constraint);
	        store.propagate();
	        assertEquals("max of y11 var is 20", 20, y11.getMax());
	        assertEquals("max of y12 var is 20", 20, y12.getMax());
	        assertEquals("max of y13 var is 20", 20, y13.getMax());
	        assertEquals("max of y21 var is 20", 20, y21.getMax());
	        assertEquals("max of y22 var is 20", 20, y22.getMax());
	        assertEquals("max of y23 var is 20", 20, y23.getMax());
	        assertEquals("max of y31 var is 20", 20, y31.getMax());
	        assertEquals("max of y32 var is 20", 20, y32.getMax());
	        assertEquals("max of y33 var is 20", 20, y33.getMax());

	        y11.setDomainValue(new Integer(5));
	        y21.setDomainMin(new Integer(10));
	        store.propagate();
	        assertEquals("max of y31 var is 5", 5, y31.getMax());
	        
	        y31.setDomainValue(new Integer(4));
	        store.propagate();	        
	        assertEquals("max of y21 var is 11", 11, y21.getMax());
	        assertEquals("min of y21 var is 11", 11, y21.getMin());
	        assertTrue("y21 is bound", y21.isBound());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testVarEQSummationSingleIndexPropagation() {
	    try {
	        CspGenericIntExpr sum1 = (CspGenericIntExpr) math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) z.eq(sum1);
	        store.addConstraint(constraint);
	        
//	        store.setAutoPropagate(true);
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
            store.propagate();
	        assertEquals("max of y32 is 6", 6, y32.getMax());
	        assertEquals("min of y32 is 6", 6, y32.getMin());
	        y13.setValue(5);
            store.propagate();
	        y23.setValue(5);
            store.propagate();
	        assertEquals("max of y33 is 5", 5, y33.getMax());
	        assertEquals("min of y33 is 5", 5, y33.getMin());
	        z.setValue(15);
            store.propagate();
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSummationSingleIndexEQConstPropagation() {
	    try {
	        constraint = (NumConstraint) math.summation(yijexpr, new GenericIndex[]{idxI}, null).eq(20);
	        store.addConstraint(constraint);
	        
	        store.setAutoPropagate(true);
	        
	        y11.setDomainMin(new Integer(5));
	        y21.setDomainMin(new Integer(10));
	        assertEquals("max of y31 is 5", 5, y31.getMax());
	        y11.setDomainMax(new Integer(6));
	        y21.setDomainMax(new Integer(11));
	        assertEquals("min of y31 is 3", 3, y31.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSummationNEQGenericVarDiffIdxPropagation() {
	    try {
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.neq(zjexpr);
	        store.addConstraint(constraint);
	        
	        x1.setValue(6);
	        x2.setValue(4);
	        z1.setValue(12);
	        store.propagate();
	        assertFalse("x3 cannot be 2", x3.isInDomain(2));
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
	}
	
	public void testSummationEQSummationSameIdxPropagation() {
	    try {
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(ziexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        
	        x1.setValue(5);
	        x2.setValue(4);
	        x3.setValue(6);
	        z1.setValue(4);
	        z2.setValue(6);
	        store.propagate();
	        assertEquals("max of z3 is 5", 5, z3.getMax());
	        assertEquals("min of z3 is 5", 5, z3.getMin());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testSummationEQGenericVarSameIdxPropagation() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        constraint = (NumConstraint) sum1.eq(zjexpr);
	        store.addConstraint(constraint);
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y12.setValue(4);
	        y22.setValue(6);
	        y13.setValue(5);
	        y23.setValue(7);
	        z1.setValue(15);
	        store.propagate();
	        assertEquals("max of y31 is 7", 7, y31.getMax());
	        assertEquals("min of y31 is 7", 7, y31.getMin());
	        z2.setValue(18);
	        store.propagate();
	        assertEquals("max of y32 is 8", 8, y32.getMax());
	        assertEquals("min of y32 is 8", 8, y32.getMin());
	        z3.setRange(20, 30);
	        store.propagate();
	        assertEquals("max of y33 is 18", 18, y33.getMax());
	        assertEquals("min of y33 is 8", 8, y33.getMin());
	    }
	    catch(PropagationFailureException pfe) {
		    //propagation, in this case, should cause an exception
		    assertTrue("verify propagation fails", 1==1);
	    }
	}
	
	public void testSummationWithRestrictionNEQGenericVarDiffIndexPropagation() {
	    try {
	        LessThanTwoIndexRestriction idxRestriction = new LessThanTwoIndexRestriction(idxI);
	        CspIntExpr sum1 = math.summation(xiexpr, new GenericIndex[]{idxI}, idxRestriction);
	        constraint = (NumConstraint) sum1.neq(zjexpr);
	        store.addConstraint(constraint);
	        
	        x1.setValue(6);
	        x2.setValue(4);
	        store.propagate();
	        assertFalse("z1 cannot be 10", z1.isInDomain(10));
	        assertFalse("z2 cannot be 10", z2.isInDomain(10));
	        assertFalse("z3 cannot be 10", z3.isInDomain(10));
		}
		catch(PropagationFailureException pfe) {
		    fail();
		}
	}
	
	public void testSummationWithRestrictionEQConstPropagation() {
	    try {
	        LessThanTwoIndexRestriction sourceRestrictionJ = new LessThanTwoIndexRestriction(idxJ);
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI, idxJ}, sourceRestrictionJ);
	        constraint = (NumConstraint) sum1.eq(100);
	        store.addConstraint(constraint);

	        store.setAutoPropagate(true);
	        
	        y11.setValue(5);
	        y12.setValue(10);
	        y21.setValue(15);
	        y22.setValue(20);
	        y31.setMax(30);
	        assertEquals("min of y32 is 20", 20, y32.getMin());
	        y31.setValue(25);
	        assertEquals("max of y32 is 25", 25, y32.getMax());
	        assertEquals("min of y32 is 25", 25, y32.getMin());
	    }
	    catch (PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testSummationEQSummationTransposedIndiciesPropagation() {
	    try {
	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
	        constraint = (NumConstraint) sum1.eq(sum2);
	        store.addConstraint(constraint);
	        
	        store.setAutoPropagate(true);
	        
	        y11.setValue(3);
	        y21.setValue(5);
	        y31.setValue(7);
	        y12.setValue(4);
	        y22.setValue(5);
	        y32.setValue(6);
	        assertEquals("max of y13 is 8", 8, y13.getMax());
	        assertEquals("min of y13 is 8", 8, y13.getMin());
	        assertEquals("max of y23 is 5", 5, y23.getMax());
	        assertEquals("min of y23 is 5", 5, y23.getMin());
	        assertEquals("min of y33 is 0", 0, y33.getMin());
	        assertEquals("max of y33 is 100", 100, y33.getMax());
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
//	public void testSummationEQSummationTransposedIndicesWithTargetRestrictionPropagation() {
//	    try {
//	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
//	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
//	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
//	        constraint = (NumConstraint) sum1.eq(sum2);
//	        store.addConstraint(constraint);
//	        
//	        store.setAutoPropagate(true);
//	        
//	        y11.setValue(10);
//	        y21.setValue(20);
//	        y31.setValue(30);
//	        y12.setValue(15);
//	        assertEquals("max of y13 is 35", 35, y13.getMax());
//	        assertEquals("min of y13 is 35", 35, y13.getMin());
//	    }
//	    catch(PropagationFailureException pfe) {
//	        fail();
//	    }
//	}
}
