/*
 * Created on June 2, 2005
 */

package jopt.csp.test.constraint.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.NumConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.util.IntSparseSet;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericBooleanExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntConstant;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
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
public class GenericCombinationConstraintTest extends TestCase {
    
    IntVariable x1;
    IntVariable x2;
    IntVariable x3;
    IntVariable x11;
    IntVariable w11;
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
    IntVariable v11;
    IntVariable v12;
    IntVariable v13;
    IntVariable v21;
    IntVariable v22;
    IntVariable v23;
    IntVariable v31;
    IntVariable v32;
    IntVariable v33;
    IntVariable u1;
    IntVariable u2;
    IntVariable u3;
    IntVariable z1;
    IntVariable z2;
    IntVariable z3;
    IntVariable p1;
    IntVariable p2;
    IntVariable p3;
    IntVariable z11;
    IntVariable z12;
    IntVariable z13;
    IntVariable z21;
    IntVariable z22;
    IntVariable z23;
    IntVariable z31;
    IntVariable z32;
    IntVariable z33;
    GenericIntExpr xiexpr;
    GenericIntExpr xijexpr;
    GenericIntExpr xjiexpr;
    GenericIntExpr xjkexpr;
    GenericIntExpr xjikexpr;
    GenericIntExpr xijkexpr;
    GenericIntExpr yiexpr;
    GenericIntExpr yjexpr;
    GenericIntExpr ykexpr;
    GenericIntExpr yijexpr;
    GenericIntExpr yjkexpr;
    GenericIntExpr vikexpr;
    GenericIntExpr vjkexpr;
    GenericIntExpr uiexpr;
    GenericIntExpr ziexpr;
    GenericIntExpr zjexpr;
    GenericIntExpr pjexpr;
    GenericIntExpr zkexpr;
    GenericIntExpr zijexpr;
    GenericIntExpr zikexpr;
    GenericIntConstant ukconst;
    GenericIntConstant uiconst;
    GenericIntConstant uijconst;
    GenericIntConstant xiconst;
    GenericIntConstant vkconst;
    GenericIntConstant wkconst;
    GenericIntConstant wijconst;
    GenericIntConstant wjiconst;
    ConstraintStore store;
    CspVariableFactory varFactory;
    GenericIndex idxI;
    GenericIndex idxJ;
    GenericIndex idxK;
    IntVariable x;
    IntVariable y;
    IntVariable z;
    NumConstraint constraint;
    CspMath math;
    CspSolver solver;
    CspMath varMath;
    
    public void setUp () {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
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
        w11 = new IntVariable("x11", 0, 100);
        x12 = new IntVariable("x12", 0, 100);
        x13 = new IntVariable("x13", 0, 100);        
        x21 = new IntVariable("x21", 0, 100);
        x22 = new IntVariable("x22", 0, 100);
        x23 = new IntVariable("x23", 0, 100);
        x31 = new IntVariable("x31", 0, 100);
        x32 = new IntVariable("x32", 0, 100);
        x33 = new IntVariable("x33", 0, 100);
        x111 = new IntVariable("x111", 0, 1);
        x121 = new IntVariable("x121", 0, 1);
        x131 = new IntVariable("x131", 0, 1);        
        x211 = new IntVariable("x211", 0, 1);
        x221 = new IntVariable("x221", 0, 1);
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
        IntSparseSet v1SparseVals = new IntSparseSet();
        v1SparseVals.add(0);
        v1SparseVals.add(2);
        v1SparseVals.add(4);
        v1SparseVals.add(6);
        v1SparseVals.add(8);
        IntSparseSet v2SparseVals = new IntSparseSet();
        v2SparseVals.add(20);
        v2SparseVals.add(22);
        v2SparseVals.add(24);
        v2SparseVals.add(26);
        v2SparseVals.add(28);
        IntSparseSet v3SparseVals = new IntSparseSet();
        v3SparseVals.add(40);
        v3SparseVals.add(42);
        v3SparseVals.add(44);
        v3SparseVals.add(46);
        v3SparseVals.add(48);
        v11 = new IntVariable("v11", v1SparseVals);
        v12 = new IntVariable("v12", v1SparseVals);
        v13 = new IntVariable("v13", v1SparseVals);        
        v21 = new IntVariable("v21", v2SparseVals);
        v22 = new IntVariable("v22", v2SparseVals);
        v23 = new IntVariable("v23", v2SparseVals);
        v31 = new IntVariable("v31", v3SparseVals);
        v32 = new IntVariable("v32", v3SparseVals);
        v33 = new IntVariable("v33", v3SparseVals);
        u1 = new IntVariable("u1", 0, 8);
        u2 = new IntVariable("u2", 0, 8);
        u3 = new IntVariable("u3", 0, 8);
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        p1 = new IntVariable("p1", 0, 1);
        p2 = new IntVariable("p2", 0, 1);
        p3 = new IntVariable("p3", 0, 1);
        z11 = new IntVariable("z11", 0, 100);
        z12 = new IntVariable("z12", 0, 100);
        z13 = new IntVariable("z13", 0, 100);        
        z21 = new IntVariable("z21", 0, 100);
        z22 = new IntVariable("z22", 0, 100);
        z23 = new IntVariable("z23", 0, 100);
        z31 = new IntVariable("z31", 0, 100);
        z32 = new IntVariable("z32", 0, 100);
        z33 = new IntVariable("z33", 0, 100);
        x = new IntVariable("x", 0, 100);
        y = new IntVariable("y", 0, 100);
        z = new IntVariable("z", 0, 100);
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        xjiexpr = (GenericIntExpr)varFactory.genericInt("xji", new GenericIndex[]{idxJ, idxI}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        xjkexpr = (GenericIntExpr)varFactory.genericInt("xjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        xjikexpr = (GenericIntExpr)varFactory.genericInt("xjik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{x111,x112,x113,x211,x212,x213,x311,x312,x313,
                x121,x122,x123,x221, x222,x223,x321,x322,x323,
                x131,x132,x133,x231, x232,x233,x331,x332,x333});
        xijkexpr = (GenericIntExpr)varFactory.genericInt("xijk", new GenericIndex[]{idxI, idxJ, idxK}, new CspIntVariable[]{x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ykexpr = (GenericIntExpr)varFactory.genericInt("yk", idxK, new CspIntVariable[]{y1, y2, y3});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yjkexpr = (GenericIntExpr)varFactory.genericInt("yjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        vikexpr = (GenericIntExpr)varFactory.genericInt("vik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{v11,v12,v13,v21,v22,v23,v31,v32,v33});
        vjkexpr = (GenericIntExpr)varFactory.genericInt("vjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{v11,v12,v13,v21,v22,v23,v31,v32,v33});
        uiexpr = (GenericIntExpr)varFactory.genericInt("ui", idxI, new CspIntVariable[]{u1, u2, u3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zjexpr = (GenericIntExpr)varFactory.genericInt("zj", idxJ, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
        zijexpr = (GenericIntExpr)varFactory.genericInt("zij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{z11,z12,z13,z21,z22,z23,z31,z32,z33});
        zikexpr = (GenericIntExpr)varFactory.genericInt("zik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{z11,z12,z13,z21,z22,z23,z31,z32,z33});
        pjexpr = (GenericIntExpr)varFactory.genericInt("pj", idxJ, new CspIntVariable[]{p1, p2, p3});
        ukconst = new GenericIntConstant("ukconst", new GenericIndex[]{idxK}, new int[]{2,2,2});
        uiconst = new GenericIntConstant("ukconst", new GenericIndex[]{idxI}, new int[]{1,1,1});
        uijconst = new GenericIntConstant("uijconst", new GenericIndex[]{idxI, idxJ}, new int[]{1,1,1,1,1,1,1,1,1});
        xiconst = new GenericIntConstant("xiconst", new GenericIndex[]{idxI}, new int[]{4,3,2});
        vkconst = new GenericIntConstant("vkconst", new GenericIndex[]{idxK}, new int[]{0,0,0});
        wkconst = new GenericIntConstant("wkconst", new GenericIndex[]{idxK}, new int[]{1,1,1});
        wijconst = new GenericIntConstant("wijconst", new GenericIndex[]{idxI,idxJ}, new int[]{2,0,0,0,0,0,0,0,0});
        wjiconst = new GenericIntConstant("wjiconst", new GenericIndex[]{idxJ,idxI}, new int[]{2,2,2,2,2,2,2,2,2});
        math = store.getConstraintAlg().getVarFactory().getMath();
        varMath = store.getConstraintAlg().getVarFactory().getMath();
    }
    
    public void tearDown() {
        x1 = null;
        x2 = null;
        x3 = null;
        x11 = null;
        w11 = null;
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
        v11 = null;
        v12 = null;
        v13 = null;
        v21 = null;
        v22 = null;
        v23 = null;
        v31 = null;
        v32 = null;
        v33 = null;
        u1 = null;
        u2 = null;
        u3 = null;
        z1 = null;
        z2 = null;
        z3 = null;
        p1 = null;
        p2 = null;
        p3 = null;
        z11 = null;
        z12 = null;
        z13 = null;
        z21 = null;
        z22 = null;
        z23 = null;
        z31 = null;
        z32 = null;
        z33 = null;
        xiexpr = null;
        xijexpr = null;
        xjiexpr = null;
        xjkexpr = null;
        xjikexpr = null;
        xijkexpr = null;
        yiexpr = null;
        yjexpr = null;
        ykexpr = null;
        yijexpr = null;
        yjkexpr = null;
        vikexpr = null;
        vjkexpr = null;
        uiexpr = null;
        ziexpr = null;
        zjexpr = null;
        pjexpr = null;
        zkexpr = null;
        zijexpr = null;
        zikexpr = null;
        ukconst = null;
        uiconst = null;
        uijconst = null;
        xiconst = null;
        vkconst = null;
        wkconst = null;
        wijconst = null;
        wjiconst = null;
        store = null;
        varFactory = null;
        idxI = null;
        idxJ = null;
        idxK = null;
        x = null;
        y = null;
        z = null;
        constraint = null;
        math = null;
        solver = null;
        varMath = null;
    }
    
    public void testSummationOverMultiplicationEQVarViolate() {
        try {
            CspGenericIntExpr mult = (CspGenericIntExpr) y.multiply(xiexpr);
            CspIntExpr sum = math.summation(mult, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(z);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            y.setValue(3);
            x1.setValue(5);
            x2.setValue(5);
            x3.setMin(6);
            z.setMax(47);
        }
        catch (PropagationFailureException pfe) {
            //propagation should fail
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }
    
    public void testSummationOverMultiplicationEQVarNoViolate() {
        try {
            CspGenericIntExpr mult = (CspGenericIntExpr) y.multiply(xiexpr);
            CspIntExpr sum = math.summation(mult, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(z);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            y.setValue(3);
            x1.setValue(3);
            x2.setValue(4);
            x3.setMin(5);
            z.setMax(36);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
    
    public void testSummationOverMultiplicationEQVarPropagation() {
        try {
            CspGenericIntExpr mult = (CspGenericIntExpr) y.multiply(xiexpr);
            CspIntExpr sum = math.summation(mult, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(z);
            store.addConstraint(constraint);
            
            store.setAutoPropagate(true);
            
            y.setValue(3);
            x1.setValue(3);
            x2.setValue(4);
            x3.setMin(5);
            assertEquals("min of z is 36", 36, z.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testGenericMinusSummationEQConstViolate() {
        try {
            CspIntExpr sum = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspGenericIntExpr sub = (CspGenericIntExpr) yjexpr.subtract(sum);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            y1.setValue(20);
            x11.setValue(3);
            x21.setValue(5);
            x31.setValue(2);
            
            y2.setValue(30);
            x12.setValue(8);
            x22.setValue(7);
            x32.setValue(5);
            
            y3.setValue(40);
            x13.setValue(10);
            x23.setValue(12);
            x33.setValue(9);
        }
        catch (PropagationFailureException pfe) {
            //propagation should fail
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }
    
    public void testGenericMinusSummationEQConstNoViolate() {
        try {
            CspIntExpr sum = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspGenericIntExpr sub = (CspGenericIntExpr) yjexpr.subtract(sum);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            y1.setValue(20);
            x11.setValue(3);
            x21.setValue(5);
            x31.setValue(2);
            
            y2.setValue(30);
            x12.setValue(8);
            x22.setValue(7);
            x32.setValue(5);
            
            y3.setValue(40);
            x13.setValue(10);
            x23.setValue(12);
            x33.setValue(8);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
    
    public void testGenericMinusSummationEQConstPropagation() {
        try {
            CspIntExpr sum = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspGenericIntExpr sub = (CspGenericIntExpr) yjexpr.subtract(sum);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            
            store.setAutoPropagate(true);
            
            y1.setValue(20);
            x11.setValue(3);
            x21.setValue(5);
            assertEquals("max of x31 is 2", 2, x31.getMax());
            assertEquals("min of x31 is 2", 2, x31.getMin());
            
            y2.setValue(30);
            x12.setValue(8);
            x32.setValue(5);
            assertEquals("max of x22 is 7", 7, x22.getMax());
            assertEquals("min of x22 is 7", 7, x22.getMin());
            
            y3.setValue(40);
            x23.setValue(12);
            x33.setValue(8);
            assertEquals("max of x13 is 10", 10, x13.getMax());
            assertEquals("min of x13 is 10", 10, x13.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSummationMinusSummationEQConstViolate() {
        try {
            CspIntExpr sum1 = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x11.setValue(30);
            x21.setValue(50);
            x31.setValue(20);
            y11.setValue(27);
            y21.setValue(47);
            y31.setValue(16);
            
            x12.setValue(80);
            x22.setValue(70);
            x32.setValue(50);
            y12.setValue(75);
            y22.setValue(67);
            y32.setValue(48);
            
            x13.setValue(100);
            x23.setValue(20);
            x33.setValue(90);
            y13.setValue(96);
            y23.setValue(16);
            y33.setValue(89);
        }
        catch (PropagationFailureException pfe) {
            //propagation should fail
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }
    
    public void testSummationMinusSummationEQConstNoViolate() {
        try {
            CspIntExpr sum1 = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x11.setValue(30);
            x21.setValue(50);
            x31.setValue(20);
            y11.setValue(27);
            y21.setValue(47);
            y31.setValue(16);
            
            x12.setValue(80);
            x22.setValue(70);
            x32.setValue(50);
            y12.setValue(75);
            y22.setValue(67);
            y32.setValue(48);
            
            x13.setValue(100);
            x23.setValue(20);
            x33.setValue(90);
            y13.setValue(96);
            y23.setValue(16);
            y33.setValue(88);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
    
    public void testSummationMinusSummationEQConstPropagation() {
        try {
            CspIntExpr sum1 = math.summation(xijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(10);
            store.addConstraint(constraint);
            
            store.setAutoPropagate(true);
            
            x11.setValue(30);
            x21.setValue(50);
            x31.setValue(20);
            y11.setValue(27);
            y21.setValue(47);
            assertEquals("max of y31 is 16", 16, y31.getMax());
            assertEquals("min of y31 is 16", 16, y31.getMin());
            
            x12.setValue(80);
            x22.setValue(70);
            x32.setValue(50);
            y12.setValue(75);
            y32.setValue(48);
            assertEquals("max of y22 is 67", 67, y22.getMax());
            assertEquals("min of y22 is 67", 67, y22.getMin());
            
            x13.setValue(100);
            x23.setValue(20);
            x33.setValue(90);
            y23.setValue(16);
            y33.setValue(88);
            assertEquals("max of y13 is 96", 96, y13.getMax());
            assertEquals("min of y13 is 96", 96, y13.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSummationMinusSummationEQConstTransposedIndicesViolate() {
        try {
            CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(0);
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
    
    public void testSummationMinusSummationEQConstTransposedIndicesNoViolate() {
        try {
            CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(0);
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
    
    public void testSummationMinusSummationEQConstTransposedIndicesPropagation() {
        try {
            CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null);
            CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null);
            CspIntExpr sub = sum1.subtract(sum2);
            constraint = (NumConstraint) sub.eq(0);
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
    
    //    public void testSummationMinusSummationEQConstTransposedIndiciesWithTargetRestrictionViolate() {
    //	    try {
    //	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
    //	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
    //	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
    //	        CspIntExpr sub = sum1.subtract(sum2);
    //            constraint = (NumConstraint) sub.eq(0);
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
    //	    }
    //	    catch(PropagationFailureException pfe) {
    //		    //propagation, in this case, should cause an exception
    //	    }
    //	    assertTrue("constraint is now violated", constraint.isViolated(false));
    //	}
    
    //    public void testSummationMinusSummationEQConstTransposedIndicesWithTargetRestrictionNoViolate() {
    //	    try {
    //	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
    //	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
    //	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
    //	        CspIntExpr sub = sum1.subtract(sum2);
    //            constraint = (NumConstraint) sub.eq(0);
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
    //	    }
    //	    catch(PropagationFailureException pfe) {
    //	        fail();
    //	    }
    //	    assertFalse("constraint is not violated", constraint.isViolated(false));
    //	}
    
    //    public void testSummationMinusSummationEQConstTransposedIndicesWithTargetRestrictionPropagation() {
    //	    try {
    //	        LessThanOneIndexRestriction targetRestriction = new LessThanOneIndexRestriction(idxJ);
    //	        CspIntExpr sum1 = math.summation(yijexpr, new GenericIndex[]{idxI}, null, targetRestriction);
    //	        CspIntExpr sum2 = math.summation(yjkexpr, new GenericIndex[]{idxK}, null, targetRestriction);
    //	        CspIntExpr sub = sum1.subtract(sum2);
    //            constraint = (NumConstraint) sub.eq(0);
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
    //    }
    
    public void testSummationOverMultiplicationAndAdditionEQConstViolate() { 
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xiexpr.add(ziexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) yijexpr.multiply(addition);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(100);
            // sum(i) [yij*(xi+zi)] = 100 for all j
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x1.setValue(1);
            z1.setValue(1); // x1+z1=2
            x2.setValue(2);
            z2.setValue(3); // x2+z2=5
            x3.setValue(4);
            z3.setValue(6); // x3+z3=10
            
            y11.setValue(5);
            y21.setValue(4);
            y31.setValue(7);
            
            y12.setValue(10);
            y22.setValue(8);
            y32.setValue(4);
            
            y13.setValue(20);
            y23.setValue(6);
            y33.setValue(4);
        }
        catch (PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }
    
    public void testSummationOverMultiplicationAndAdditionEQConstNoViolate() { 
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xiexpr.add(ziexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) yijexpr.multiply(addition);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(100);
            // sum(i) [yij*(xi+zi)] = 100 for all j
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x1.setValue(1);
            z1.setValue(1); // x1+z1=2
            x2.setValue(2);
            z2.setValue(3); // x2+z2=5
            x3.setValue(4);
            z3.setValue(6); // x3+z3=10
            
            y11.setValue(5);
            y21.setValue(4);
            y31.setValue(7);
            
            y12.setValue(10);
            y22.setValue(8);
            y32.setValue(4);
            
            y13.setValue(20);
            y23.setValue(6);
            y33.setValue(3);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
    
    public void testSummationOverMultiplicationAndAdditionEQConstPropagation() { 
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xiexpr.add(ziexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) yijexpr.multiply(addition);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxI}, null);
            constraint = (NumConstraint) sum.eq(100);
            // sum(i) [yij*(xi+zi)] = 100 for all j
            store.addConstraint(constraint);
            
            store.setAutoPropagate(true);
            
            x1.setValue(1);
            z1.setValue(1); // x1+z1=2
            x2.setValue(2);
            z2.setValue(3); // x2+z2=5
            x3.setValue(4);
            z3.setValue(6); // x3+z3=10
            
            y11.setValue(5);
            y21.setMax(4);
            assertEquals("max of y31 is 9", 9, y31.getMax());
            assertEquals("min of y31 is 7", 7, y31.getMin());
            
            y12.setValue(10);
            y32.setValue(4);
            assertEquals("max of y22 is 8", 8, y22.getMax());
            assertEquals("min of y22 is 8", 8, y22.getMin());
            
            y23.setValue(6);
            y33.setValue(4);
            assertEquals("max of y13 is 15", 15, y13.getMax());
            assertEquals("min of y13 is 15", 15, y13.getMin());
        }
        catch (PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
    }
    
    public void testSumMinusSummationOverMultiplicationEQConstViolate() {
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xijexpr.add(zijexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) xjkexpr.multiply(yjkexpr);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxK}, null);
            CspGenericIntExpr subtraction = (CspGenericIntExpr) addition.subtract(sum);
            constraint = (NumConstraint) subtraction.eq(0);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x11.setValue(1);
            z11.setValue(5);
            x12.setValue(1);
            z12.setValue(11);
            x13.setValue(1);
            z13.setValue(14);
            
            x21.setValue(1);
            z21.setValue(5);
            x22.setValue(1);
            z22.setValue(11);
            x23.setValue(1);
            z23.setValue(14);
            
            x31.setValue(1);
            z31.setValue(5);
            x32.setValue(1);
            z32.setValue(11);
            x33.setValue(1);
            z33.setValue(14);
            
            y11.setValue(1);
            y12.setValue(2);
            y13.setValue(3);
            y21.setValue(2);
            y22.setValue(4);
            y23.setValue(6);
            y31.setValue(3);
            y32.setValue(5);
            y33.setValue(8);
        }
        catch (PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }
    
    public void testSumMinusSummationOverMultiplicationEQConstNoViolate() {
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xijexpr.add(zijexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) xjkexpr.multiply(yjkexpr);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxK}, null);
            CspGenericIntExpr subtraction = (CspGenericIntExpr) addition.subtract(sum);
            constraint = (NumConstraint) subtraction.eq(0);
            store.addConstraint(constraint);
            assertFalse("constraint is not violated yet", constraint.isViolated(false));
            
            x11.setValue(1);
            z11.setValue(5);
            x12.setValue(1);
            z12.setValue(11);
            x13.setValue(1);
            z13.setValue(14);
            
            x21.setValue(1);
            z21.setValue(5);
            x22.setValue(1);
            z22.setValue(11);
            x23.setValue(1);
            z23.setValue(14);
            
            x31.setValue(1);
            z31.setValue(5);
            x32.setValue(1);
            z32.setValue(11);
            x33.setValue(1);
            z33.setValue(14);
            
            y11.setValue(1);
            y12.setValue(2);
            y13.setValue(3);
            y21.setValue(2);
            y22.setValue(4);
            y23.setValue(6);
            y31.setValue(3);
            y32.setValue(5);
            y33.setValue(7);
        }
        catch (PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }
    
    public void testSumMinusSummationOverMultiplicationEQConstPropagation() {
        try {
            CspGenericIntExpr addition = (CspGenericIntExpr) xijexpr.add(zijexpr);
            CspGenericIntExpr multiplication = (CspGenericIntExpr) xjkexpr.multiply(yjkexpr);
            CspIntExpr sum = math.summation(multiplication, new GenericIndex[]{idxK}, null);
            CspGenericIntExpr subtraction = (CspGenericIntExpr) addition.subtract(sum);
            constraint = (NumConstraint) subtraction.eq(0);
            store.addConstraint(constraint);
            
            store.setAutoPropagate(true);
            
            x11.setValue(1);
            z11.setValue(5);
            x12.setValue(1);
            z12.setValue(11);
            x13.setValue(1);
            z13.setValue(14);
            
            x21.setValue(1);
            x22.setValue(1);
            x23.setValue(1);
            
            x31.setValue(1);
            x32.setValue(1);
            x33.setValue(1);
            
            y11.setValue(1);
            y12.setValue(2);
            assertEquals("max of y13 is 3", 3, y13.getMax());
            assertEquals("min of y13 is 3", 3, y13.getMin());
            y21.setValue(2);
            y23.setValue(6);
            assertEquals("max of y22 is 4", 4, y22.getMax());
            assertEquals("min of y22 is 4", 4, y22.getMin());
            y31.setValue(3);
            y32.setMax(5);
            assertEquals("max of y33 is 12", 12, y33.getMax());
            assertEquals("min of y33 is 7", 7, y33.getMin());
        }
        catch (PropagationFailureException pfe) {
            //propagation, in this case, should cause an exception
        }
    }
    
    public void testSummationOverMultiplicationWithSumPlusConstLEQConstant() {
        try {
            x11.setDomainRange(new Integer(0), new Integer(1));
            x12.setDomainRange(new Integer(0), new Integer(1));
            x13.setDomainRange(new Integer(0), new Integer(1));
            x21.setDomainRange(new Integer(0), new Integer(1));
            x22.setDomainRange(new Integer(0), new Integer(1));
            x23.setDomainRange(new Integer(0), new Integer(1));
            x31.setDomainRange(new Integer(0), new Integer(1));
            x32.setDomainRange(new Integer(0), new Integer(1));
            x33.setDomainRange(new Integer(0), new Integer(1));
            z1.setDomainRange(new Integer(0), new Integer(1));
            z2.setValue(0);
            z3.setValue(0);
            
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            CspGenericIntExpr zvar_plus_wconst = ziexpr.add(wijconst);
            CspGenericIntExpr xvar_mult_zplusw = xjkexpr.multiply(zvar_plus_wconst);
            CspGenericIntExpr sum = (CspGenericIntExpr)math.summation(xvar_mult_zplusw, new CspGenericIndex[]{idxI, idxJ}, null);
            CspGenericIntExpr vkconst_plus_sum = sum.add(vkconst);
            constraint = (NumConstraint)vkconst_plus_sum.leq(ukconst);
            
            solver.addConstraint(constraint);
            
            x11.setValue(1);
            x21.setValue(0);
            x31.setValue(0);
            
            assertTrue(solver.propagate());
            
            assertTrue("z1 should be bound", z1.isBound());
            assertEquals("z1 should be 0", 0, z1.getMax());
            assertEquals("z1 should be 0", 0, z1.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSummationOverMultiplicationWithSumLEQConstant() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            CspGenericIntExpr pvar_plus_wconst = pjexpr.add(wjiconst);
            CspGenericIntExpr xvar_mult_pplusw = xjikexpr.multiply(pvar_plus_wconst);
            CspGenericIntExpr sum = (CspGenericIntExpr)math.summation(xvar_mult_pplusw, new CspGenericIndex[]{idxJ, idxI}, null);
            constraint = (NumConstraint)sum.leq(ukconst);
            
            solver.addConstraint(constraint);
            
            x111.setValue(1);
            x121.setValue(0);
            x131.setValue(0);
            x211.setValue(0);
            x221.setValue(0);
            x231.setValue(0);
            x311.setValue(0);
            x321.setValue(0);
            x331.setValue(0);
            
            assertTrue(solver.propagate());
            
            assertTrue("p1 should be bound", p1.isBound());
            assertEquals("p1 should be 0", 0, p1.getMax());
            assertEquals("p1 should be 0", 0, p1.getMin());
            
            x112.setValue(0);
            x122.setValue(0);
            x132.setValue(0);
            x212.setValue(0);
            x222.setValue(1);
            x232.setValue(0);
            x312.setValue(0);
            x322.setValue(0);
            x332.setValue(0);
            
            assertTrue(solver.propagate());
            
            assertTrue("p2 should be bound", p2.isBound());
            assertEquals("p2 should be 0", 0, p2.getMax());
            assertEquals("p2 should be 0", 0, p2.getMin());
            
            x113.setValue(0);
            x123.setValue(0);
            x133.setValue(0);
            x213.setValue(0);
            x223.setValue(0);
            x233.setValue(0);
            x313.setValue(0);
            x323.setValue(0);
            x333.setValue(1);
            
            assertTrue(solver.propagate());
            
            assertTrue("p3 should be bound", p3.isBound());
            assertEquals("p3 should be 0", 0, p3.getMax());
            assertEquals("p3 should be 0", 0, p3.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    
    public void testBooleanEqualityToSum() {
        idxI = new GenericIndex("i", 2);
        idxJ = new GenericIndex("j", 2);
        xiexpr = (GenericIntExpr)varFactory.genericInt("~xi", new CspGenericIndex[]{idxI}, new CspIntVariable[]{x1,x2});
        xijexpr = (GenericIntExpr)varFactory.genericInt("~xij", new CspGenericIndex[]{idxI,idxJ}, new CspIntVariable[]{x11,x12,x21,x22});
        yiexpr = (GenericIntExpr)varFactory.genericInt("~yi", new CspGenericIndex[]{idxI}, new CspIntVariable[]{y1,y2});
        GenericIntExpr xisum =(GenericIntExpr)varMath.summation(xijexpr, new CspGenericIndex[]{idxJ}, null);
        GenericBooleanExpr aexpr = new GenericBooleanExpr("xi = 1",  new CspGenericIndex[]{idxI}, xiexpr.eq(1));
        GenericBooleanExpr bexpr = new GenericBooleanExpr("yi=sum(xij)",  new CspGenericIndex[]{idxI}, yiexpr.eq(xisum));
        
        try {
            solver.addConstraint(aexpr.eq(bexpr));
            //            solver.addConstraint(constraint);
            //            solver.addConstraint(yiexpr.eq(xisum));
            solver.propagate();
            
            x1.setValue(1);
            x11.setValue(1);
            solver.propagate();
            assertEquals("y1 should have a min of1",1,y1.getMin());
            
            x12.setValue(0);
            solver.propagate();
            
            assertTrue("y1should be bound to 1",y1.isBound());
            assertEquals("y1 should have a min of1",1,y1.getMin());
            
            x2.setValue(0);
            x21.setValue(0);
            x22.setValue(0);
            solver.propagate();
            
            assertFalse("y2 will not be bound",y2.isBound());
            assertEquals("y2 should have a min of1",1,y2.getMin());
            assertEquals("y2 should have a min of1",100,y2.getMax());
            
        }
        catch( PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    
    
    public void testSummationsWithBooleansAndImplicationInvolvingGenericConstants() {
        try {
            CspSolver solver = CspSolver.createSolver();
            CspMath varMath = solver.getVarFactory().getMath();
            solver.setAutoPropagate(false);
            
            CspGenericIntExpr xsum =(CspGenericIntExpr)varMath.summation(xijexpr, new CspGenericIndex[]{idxJ}, null);
            CspConstraint sumEq1 = xsum.geq(wkconst);
            CspGenericBooleanExpr left1BoolExpr = varFactory.genericBoolean(xsum.getName()+">=w_k", new CspGenericIndex[]{idxI, idxK}, sumEq1); 
            CspConstraint sumEq2 = xsum.leq(ukconst);
            CspGenericBooleanExpr left2BoolExpr = varFactory.genericBoolean(xsum.getName()+"<=u_k", new CspGenericIndex[]{idxI, idxK}, sumEq2);
            CspGenericBooleanExpr leftBoolExpr = (CspGenericBooleanExpr)left1BoolExpr.and(left2BoolExpr);
            CspGenericBooleanExpr rightBoolExpr = varFactory.genericBoolean("z_ik = "+xsum.getName(), new CspGenericIndex[]{idxI, idxK}, zikexpr.eq(xsum));
            solver.addConstraint(leftBoolExpr.implies(rightBoolExpr));
            
            x11.setValue(0);
            x12.setValue(1);
            x13.setValue(0);
            
            assertTrue(solver.propagate());
            
            assertEquals("z11 should be 1", 1, z11.getMin());
            assertEquals("z11 should be 1", 1, z11.getMax());
            assertEquals("z12 should be 1", 1, z12.getMin());
            assertEquals("z12 should be 1", 1, z12.getMax());
            assertEquals("z13 should be 1", 1, z13.getMin());
            assertEquals("z13 should be 1", 1, z13.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testBooleanSumImpliesAndLeftSideFalse(){
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.and(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.implies(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            x1.setMax(4);
            x2.setMax(3);
            x3.setMax(2);
            solver.propagate();
            assertEquals("x1 should have max 4",4,x1.getMax());
            assertEquals("x2 should have max 3",3,x2.getMax());
            assertEquals("x3 should have max 2",2,x3.getMax());
            //Make left side not true
            y.setValue(0);
            solver.propagate();
            
            assertEquals("x1 should have max 3",3,x1.getMax());
            assertEquals("x2 should have max 2",2,x2.getMax());
            assertEquals("x3 should have max 1",1,x3.getMax());
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }   
    }
    
    public void testGenericBooleanImpliesOrStatement() {
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspConstraint constraint2 = y.multiply(2).subtract(4).eq(4);
        CspConstraint constraint3 = ziexpr.eq(2);
        CspBooleanExpr bexpr1 = varFactory.genericBoolean("xi=xiconst", idxI,constraint1);
        CspBooleanExpr bexpr2 = varFactory.booleanVar("2y-4=4", constraint2);
        CspBooleanExpr bexpr3 = varFactory.booleanVar("z=2", constraint3);
        CspBooleanExpr rightExpr = bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.implies(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            x1.setValue(4);
            solver.propagate();
            
            assertFalse("Y should not be bound", y.isBound());
            assertFalse("Z should not be bound", z.isBound());
            
            //            x2.setValue(3);
            //            x3.setValue(2);
            //            solver.propagate();
            
            z1.setValue(1);
            solver.propagate();
            //Both Y and Z should be bound now
            assertTrue("Y should be bound", y.isBound());
            assertTrue("Z1 should be bound", z1.isBound());
            assertEquals("Y is equal to 4", 4, y.getMax());
            assertEquals("Z1 is equal to 1", 1, z1.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testBooleanSumEquals(){
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr xijsum = (GenericIntExpr)varMath.summation(xijexpr,new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = xijsum.eq(1);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(x_ij)=1)", new CspGenericIndex[]{idxI}, constraint2);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(bexpr2);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            
            x1.setValue(4);
            solver.propagate();
            
            //This should be enough to set sum(x1j) = 1
            x11.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x12 is 0", 0, x12.getMax());
            assertEquals("x12 is 0", 0, x12.getMin());
            assertEquals("x13 is 0", 0, x13.getMax());
            assertEquals("x13 is 0", 0, x13.getMin());
            
            x2.setMax(3);
            x21.setValue(0);
            x22.setValue(2);
            x23.setValue(0);
            assertTrue(solver.propagate());
            
            assertEquals("x2 is 0..2", 2, x2.getMax());
            assertEquals("x2 is 0..2", 0, x2.getMin());
            
            x31.setValue(0);
            x32.setValue(0);
            x33.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x3 is 2", 2, x3.getMax());
            assertEquals("x3 is 2", 2, x3.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanDoubleSumEquals(){
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr xijsum = (GenericIntExpr)varMath.summation(xijexpr,new CspGenericIndex[]{idxJ},null);
        CspIntExpr xsum = (IntExpr)varMath.summation(xijsum,new CspGenericIndex[]{idxI},null);
        CspConstraint constraint2 = xsum.eq(1);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspBooleanExpr bexpr2 = varFactory.booleanVar("SUM(SUM(x_ij))=1)", constraint2);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(bexpr2);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            
            x1.setValue(4);
            x11.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x12 is 0", 0, x12.getMax());
            assertEquals("x12 is 0", 0, x12.getMin());
            assertEquals("x13 is 0", 0, x13.getMax());
            assertEquals("x13 is 0", 0, x13.getMin());
            assertEquals("x21 is 0", 0, x21.getMax());
            assertEquals("x21 is 0", 0, x21.getMin());
            assertEquals("x22 is 0", 0, x22.getMax());
            assertEquals("x22 is 0", 0, x22.getMin());
            assertEquals("x23 is 0", 0, x23.getMax());
            assertEquals("x23 is 0", 0, x23.getMin());
            assertEquals("x31 is 0", 0, x31.getMax());
            assertEquals("x31 is 0", 0, x31.getMin());
            assertEquals("x32 is 0", 0, x32.getMax());
            assertEquals("x32 is 0", 0, x32.getMin());
            assertEquals("x33 is 0", 0, x33.getMax());
            assertEquals("x33 is 0", 0, x33.getMin());
            
            assertEquals("x2 is 3", 3, x2.getMax());
            assertEquals("x2 is 3", 3, x2.getMin());
            assertEquals("x3 is 2", 2, x3.getMax());
            assertEquals("x3 is 2", 2, x3.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanSumEqualsAnd(){
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr xijsum = (GenericIntExpr)varMath.summation(xijexpr,new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = xijsum.eq(1);
        CspConstraint constraint3 = y.eq(1);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(x_ij)=1)", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("y=1", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.and(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            solver.propagate();
            
            x1.setValue(4);
            assertTrue(solver.propagate());
            
            assertEquals("y is 1", 1, y.getMax());
            assertEquals("y is 1", 1, y.getMin());
            
            x11.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x12 is 0", 0, x12.getMax());
            assertEquals("x12 is 0", 0, x12.getMin());
            assertEquals("x13 is 0", 0, x13.getMax());
            assertEquals("x13 is 0", 0, x13.getMin());
            
            x3.setMax(2);
            x31.setValue(0);
            x32.setValue(0);
            x33.setValue(2);
            assertTrue(solver.propagate());
            
            assertEquals("x3 is 0..1", 1, x3.getMax());
            assertEquals("x3 is 0..1", 0, x3.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanSumImpliesAnd(){
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333,});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.and(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.implies(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            //Set left side true for i=1
            x1.setValue(4);
            
            assertTrue(solver.propagate());
            
            //              This should be enough to set 4y-2=2
            assertTrue("Y should be bound to 1",y.isBound());
            assertEquals("Y should be bound to 1",1, y.getMax());
            assertEquals("Y should be bound to 1",1, y.getMin());
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(0);
            x211.setValue(0);
            x212.setValue(0);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x313 should be bound to 1",x313.isBound());
            assertEquals("x313 should be bound to 1",1, x313.getMax());
            assertEquals("x313 should be bound to 1",1, x313.getMin());
            
            x2.setValue(3);
            x121.setValue(1);
            x122.setValue(0);
            x123.setValue(0);
            x221.setValue(0);
            x222.setValue(1);
            x223.setValue(0);
            x321.setValue(0);
            x322.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("Y should be bound to 1",y.isBound());
            assertEquals("Y should be bound to 1",1, y.getMax());
            assertEquals("Y should be bound to 1",1, y.getMin());
            
            assertTrue("x323 should be bound to 0",x323.isBound());
            assertEquals("x323 should be bound to 0",0, x323.getMax());
            assertEquals("x323 should be bound to 0",0, x323.getMin());
            
            x3.setMax(2);
            x131.setValue(1);
            x132.setValue(0);
            x133.setValue(0);
            x231.setValue(0);
            x232.setValue(1);
            x233.setValue(0);
            x331.setValue(0);
            x332.setValue(0);
            x333.setValue(1);
            assertTrue(solver.propagate());
            
            assertTrue("Y should be bound to 1",y.isBound());
            assertEquals("Y should be bound to 1",1, y.getMax());
            assertEquals("Y should be bound to 1",1, y.getMin());
            
            assertEquals("x3 should be bound to 1",1, x3.getMax());
            assertEquals("x3 should be bound to 0",0, x3.getMin());
            
            
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }

    public void testBooleanSumEqualsOra(){
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333,});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            //Set left side true for i=1
            x1.setValue(4);

            assertTrue(solver.propagate());

            assertFalse("Y should not be bound",y.isBound());
            
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(0);
            x211.setValue(0);
            x212.setValue(0);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(0);
            x313.setValue(0);
            assertTrue(solver.propagate());
            assertTrue("Y should  be bound",y.isBound());
            assertEquals("Y should be 1", y.getMax(),1); 
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanSumEqualsOr(){
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333,});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            //Set left side true for i=1
            x1.setValue(4);

            assertTrue(solver.propagate());

            assertFalse("Y should not be bound",y.isBound());
            
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(0);
            x211.setValue(0);
            x212.setValue(0);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(0);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            assertFalse("x313 should not be bound",x313.isBound());
            y.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x313 should be bound to 1",x313.isBound());
            assertEquals("x313 should be bound to 1",1, x313.getMax());
            assertEquals("x313 should be bound to 1",1, x313.getMin());
            
            x2.setValue(3);
            x121.setValue(1);
            x122.setValue(0);
            x123.setValue(0);
            x221.setValue(0);
            x222.setValue(1);
            x223.setValue(0);
            x321.setValue(0);
            x322.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x323 should be bound to 0",x323.isBound());
            assertEquals("x323 should be bound to 0",0, x323.getMax());
            assertEquals("x323 should be bound to 0",0, x323.getMin());
            
            x3.setMax(2);
            x131.setValue(1);
            x132.setValue(0);
            x133.setValue(0);
            x231.setValue(0);
            x232.setValue(1);
            x233.setValue(0);
            x331.setValue(0);
            x332.setValue(0);
            x333.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x3 should be bound to 1",1, x3.getMax());
            assertEquals("x3 should be bound to 0",0, x3.getMin());
            
            
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    public void testBooleanSumEqualsOrSingleIdxValue(){
        idxI = new GenericIndex("i",1);
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,
                x211,x212,x213,
                x311,x312,x313});
        xiconst = new GenericIntConstant("xiconst", new GenericIndex[]{idxI}, new int[]{4});
        xiexpr = (GenericIntExpr)varFactory.genericInt("x_1", idxI, new CspIntVariable[]{x1});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            //Set left side true for i=1
            x1.setValue(4);

            assertTrue(solver.propagate());
            

            assertFalse("Y should not be bound",y.isBound());
            
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(0);
            x211.setValue(0);
            x212.setValue(0);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(0);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            assertFalse("x313 should not be bound yet",x313.isBound());
            y.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x313 should be bound to 1",x313.isBound());
            assertEquals("x313 should be bound to 1",1, x313.getMax());
            assertEquals("x313 should be bound to 1",1, x313.getMin());
            
            
            
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanSumEqualsOrReverse(){
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333,});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.or(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            //Set left side true for i=1
            x1.setMax(4);
            x1.setMin(3);
            assertTrue(solver.propagate());

            assertFalse("Y should not be bound",y.isBound());
            
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(1);
            x211.setValue(0);
            x212.setValue(1);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(1);
            y.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x1 should be bound to 3",x1.isBound());
            assertEquals("x1 should be bound to 3",3, x1.getMax());
            assertEquals("x1 should be bound to 3",3, x1.getMin());
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    public void testBooleanSumEqualsXor(){
        xjikexpr = (GenericIntExpr)varFactory.genericInt("x_jik", new GenericIndex[]{idxJ, idxI, idxK}, new CspIntVariable[]{
                x111,x112,x113,x121,x122,x123,x131,x132,x133,
                x211,x212,x213,x221,x222,x223,x231,x232,x233,
                x311,x312,x313,x321,x322,x323,x331,x332,x333,});
        CspConstraint constraint1 = xiexpr.eq(xiconst);
        CspGenericIntExpr tempXji = (GenericIntExpr)varMath.summation(xjikexpr,new CspGenericIndex[]{idxK},null);
        CspGenericIntExpr tempSumXi = (GenericIntExpr)varMath.summation((GenericIntExpr)tempXji.multiply(2),new CspGenericIndex[]{idxJ},null);
        CspConstraint constraint2 = tempSumXi.eq(4);
        CspConstraint constraint3 = y.multiply(4).subtract(2).eq(2);
        CspGenericBooleanExpr bexpr1 = varFactory.genericBoolean("x_i=[4,3,2]", new CspGenericIndex[]{idxI}, constraint1);
        CspGenericBooleanExpr bexpr2 = varFactory.genericBoolean("SUM(2*SUM(x_jik))", new CspGenericIndex[]{idxI}, constraint2);
        CspGenericBooleanExpr bexpr3 = varFactory.genericBoolean("4y-2=2", new CspGenericIndex[]{idxI}, constraint3);
        CspGenericBooleanExpr rightExpr = (GenericBooleanExpr)bexpr2.xor(bexpr3);
        CspBooleanExpr finalBoolExpr = bexpr1.eq(rightExpr);
        try {
            solver.addConstraint(finalBoolExpr);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            //Set left side true for i=1
            x1.setValue(4);

            assertTrue(solver.propagate());

            assertFalse("Y should not be bound",y.isBound());
            
            x111.setValue(1);
            x112.setValue(0);
            x113.setValue(0);
            x211.setValue(0);
            x212.setValue(0);
            x213.setValue(0);
            x311.setValue(0);
            x312.setValue(0);
            assertTrue(solver.propagate());
            assertFalse("Y should not be bound",y.isBound());
            assertFalse("x313 should not be bound",x313.isBound());
            y.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x313 should be bound to 1",x313.isBound());
            assertEquals("x313 should be bound to 1",1, x313.getMax());
            assertEquals("x313 should be bound to 1",1, x313.getMin());
            
            x2.setValue(3);
            x121.setValue(1);
            x122.setValue(0);
            x123.setValue(0);
            x221.setValue(0);
            x222.setValue(1);
            x223.setValue(0);
            x321.setValue(0);
            x322.setValue(0);
            assertTrue(solver.propagate());
            
            assertTrue("x323 should be bound to 0",x323.isBound());
            assertEquals("x323 should be bound to 0",0, x323.getMax());
            assertEquals("x323 should be bound to 0",0, x323.getMin());
            
            x3.setMax(2);
            x131.setValue(1);
            x132.setValue(0);
            x133.setValue(0);
            x231.setValue(0);
            x232.setValue(1);
            x233.setValue(0);
            x331.setValue(0);
            x332.setValue(0);
            x333.setValue(1);
            assertTrue(solver.propagate());
            
            assertEquals("x3 should be bound to 0..1",1, x3.getMax());
            assertEquals("x3 should be bound to 0..1",0, x3.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }  
    }
    
    //Create a bunch of expressions only one of which can be true
    public void testBooleanAsNumMultXor() {
        
        
        BooleanExpr b1 = new BooleanExpr("b1", x.eq(5));
        BooleanExpr b2 = new BooleanExpr("b2", x.eq(y));
        BooleanExpr b3 = new BooleanExpr("b3", varMath.summation(xiexpr,new CspGenericIndex[]{idxI},null).eq(21));
        try{
            solver.addConstraint(b1.add(b2).add(b3).eq(1));
            assertTrue(solver.propagate());
            assertFalse("b1 is not true",b1.isTrue());
            assertFalse("b1 is not false",b1.isFalse());
            assertFalse("b2 is not true",b2.isTrue());
            assertFalse("b2 is not false",b2.isFalse());
            assertFalse("b3 is not true",b3.isTrue());
            assertFalse("b3 is not false",b3.isFalse());
            x1.setMin(41);
            assertTrue(solver.propagate());
            assertFalse("b1 is not true",b1.isTrue());
            assertFalse("b1 is not false",b1.isFalse());
            assertFalse("b2 is not true",b2.isTrue());
            assertFalse("b2 is not false",b2.isFalse());
            assertFalse("b3 is not true",b3.isTrue());
            assertTrue("b3 is false",b3.isFalse());
            
            x.setValue(21);
            assertTrue(solver.propagate());
            assertFalse("b1 is not true",b1.isTrue());
            assertTrue("b1 is not false",b1.isFalse());
            assertTrue("b2 is not true",b2.isTrue());
            assertFalse("b2 is not false",b2.isFalse());
            assertFalse("b3 is not true",b3.isTrue());
            assertTrue("b3 is false",b3.isFalse());
            assertTrue("y should be bound", y.isBound());
            assertEquals("y should be 21", 21, y.getMax());
            
        }
        catch (PropagationFailureException pfe) {
            fail(pfe.getMessage());
        }
    }
    public void testSummationsWithBooleansAndImplicationInvolvingGenericConstantsSummationAndDifferingIndices() {
        try {
            idxI = new GenericIndex("i",1);
            idxJ = new GenericIndex("j",1);
            idxK = new GenericIndex("k",1);
            IntVariable g111 = new IntVariable("g111", 0,100);
            IntVariable g112 = new IntVariable("g112", 0,100);
            GenericIndex idxM = new GenericIndex("m",2);
            CspGenericIntExpr xijkexpr = (GenericIntExpr)varFactory.genericInt("xijk1", new GenericIndex[]{idxI, idxJ, idxK}, new CspIntVariable[]{x111});
            CspGenericIntExpr gijmexpr = (GenericIntExpr)varFactory.genericInt("gjim", new GenericIndex[]{idxI, idxJ, idxM}, new CspIntVariable[]{g111,g112});
            CspGenericIntExpr wjkexpr = (GenericIntExpr)varFactory.genericInt("wjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{w11});
            CspGenericIntConstant qmconst = new GenericIntConstant("qmconst", new GenericIndex[]{idxM}, new int[]{0,60});
            
            
            CspGenericIntExpr xwsum =(CspGenericIntExpr)varMath.summation(xijkexpr.multiply(wjkexpr), new CspGenericIndex[]{idxK}, null);
            CspConstraint sumEq1 = xwsum.geq(qmconst);
            CspGenericBooleanExpr left1BoolExpr = varFactory.genericBoolean(xwsum.getName()+">=q_m",new CspGenericIndex[]{idxI,idxJ,idxM},sumEq1); 
            CspConstraint sumEq2 = xwsum.leq((CspGenericIntConstant)qmconst.add(new Integer(59)));
            CspGenericBooleanExpr left2BoolExpr = varFactory.genericBoolean(xwsum.getName()+"<=q_m+60",new CspGenericIndex[]{idxI,idxJ,idxM},sumEq2);

            CspGenericIntExpr xsum = (CspGenericIntExpr)varMath.summation(xijkexpr, new CspGenericIndex[]{idxK}, null);
            CspGenericBooleanExpr leftBoolExpr = (CspGenericBooleanExpr)left1BoolExpr.and(left2BoolExpr);
            CspGenericBooleanExpr rightBoolExpr = varFactory.genericBoolean("g_ijm = "+xsum.getName(),new CspGenericIndex[]{idxI,idxJ,idxM},gijmexpr.eq(xsum));
            solver.addConstraint(leftBoolExpr.implies(rightBoolExpr));
            assertTrue(solver.propagate());
            assertFalse(g111.isBound());
            assertFalse(((GenericIntExpr)xsum).getExpressionForIndex().isBound());
            w11.setValue(0);
            x111.setValue(1);
            assertTrue(solver.propagate());
            assertTrue(((GenericIntExpr)xsum).getExpressionForIndex().isBound());
//            assertTrue("left should be true",left1BoolExpr.isTrue());
            assertTrue("g111 should be bound",g111.isBound());
            assertFalse("g112 should not be bound",g112.isBound());
            
            
            
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleGenericMultiplicationWithAdditionSubtractionAndSimilarIndexing() {
        try {
            idxI = new GenericIndex("i",1);
            idxJ = new GenericIndex("j",2);
            idxK = new GenericIndex("k",1);
            IntSparseSet c1SparseVals = new IntSparseSet();
            c1SparseVals.add(0);
            c1SparseVals.add(2);
            c1SparseVals.add(4);
            c1SparseVals.add(6);
            c1SparseVals.add(8);
            IntSparseSet c2SparseVals = new IntSparseSet();
            c2SparseVals.add(10);
            c2SparseVals.add(12);
            c2SparseVals.add(14);
            c2SparseVals.add(16);
            c2SparseVals.add(18);
            IntVariable c11 = new IntVariable("c11", c1SparseVals);
            IntVariable c21 = new IntVariable("c21", c1SparseVals);
            IntVariable b111 = new IntVariable("b111", 0, 1);
            IntVariable b121 = new IntVariable("b121", 0, 1);
            CspGenericIntExpr bijkexpr = (GenericIntExpr)varFactory.genericInt("bijk", new GenericIndex[]{idxI, idxJ, idxK}, new CspIntVariable[]{b111, b121});
            CspGenericIntExpr cikexpr = (GenericIntExpr)varFactory.genericInt("cik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{c11});
            CspGenericIntExpr cjkexpr = (GenericIntExpr)varFactory.genericInt("cjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{c11, c21});
            CspGenericIntConstant aiconst = new GenericIntConstant("aiconst", new GenericIndex[]{idxI}, new int[]{1});
            
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            CspGenericIntExpr addition = (CspGenericIntExpr) cikexpr.add(aiconst);
            CspGenericIntExpr subtraction = addition.subtract(cjkexpr);
            CspGenericIntExpr multiplication = bijkexpr.multiply(subtraction);
            solver.addConstraint(multiplication.leq(0));
            
            assertEquals("Min of b is 0", 0, b111.getMin());
            assertEquals("Max of b is 1", 1, b111.getMax());
            assertEquals("Min of b is 0", 0, b121.getMin());
            assertEquals("Max of b is 1", 1, b121.getMax());
            
            assertTrue(solver.propagate());
            
            assertEquals("Min of b is 0", 0, b111.getMin());
            assertEquals("Max of b is 1", 1, b111.getMax());
            assertEquals("Min of b is 0", 0, b121.getMin());
            assertEquals("Max of b is 1", 1, b121.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testGenericMultiplicationWithAdditionSubtractionAndSimilarIndexing() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            
            CspGenericIntExpr addition = vikexpr.add(uiconst).add(uiexpr).add(uijconst);
            CspGenericIntExpr subtraction = addition.subtract(vjkexpr);
            CspGenericIntExpr multiplication = xijkexpr.multiply(subtraction);
            solver.addConstraint(multiplication.leq(0));
            
            assertEquals("Min of x is 0", 0, x111.getMin());
            assertEquals("Max of x is 1", 1, x111.getMax());
            assertEquals("Min of x is 0", 0, x121.getMin());
            assertEquals("Max of x is 1", 1, x121.getMax());
            assertEquals("Min of x is 0", 0, x131.getMin());
            assertEquals("Max of x is 1", 1, x131.getMax());
            assertEquals("Min of x is 0", 0, x211.getMin());
            assertEquals("Max of x is 1", 1, x211.getMax());
            assertEquals("Min of x is 0", 0, x221.getMin());
            assertEquals("Max of x is 1", 1, x221.getMax());
            assertEquals("Min of x is 0", 0, x231.getMin());
            assertEquals("Max of x is 1", 1, x231.getMax());
            assertEquals("Min of x is 0", 0, x311.getMin());
            assertEquals("Max of x is 1", 1, x311.getMax());
            assertEquals("Min of x is 0", 0, x321.getMin());
            assertEquals("Max of x is 1", 1, x321.getMax());
            assertEquals("Min of x is 0", 0, x331.getMin());
            assertEquals("Max of x is 1", 1, x331.getMax());
            assertEquals("Min of x is 0", 0, x112.getMin());
            assertEquals("Max of x is 1", 1, x112.getMax());
            assertEquals("Min of x is 0", 0, x122.getMin());
            assertEquals("Max of x is 1", 1, x122.getMax());
            assertEquals("Min of x is 0", 0, x132.getMin());
            assertEquals("Max of x is 1", 1, x132.getMax());
            assertEquals("Min of x is 0", 0, x212.getMin());
            assertEquals("Max of x is 1", 1, x212.getMax());
            assertEquals("Min of x is 0", 0, x222.getMin());
            assertEquals("Max of x is 1", 1, x222.getMax());
            assertEquals("Min of x is 0", 0, x232.getMin());
            assertEquals("Max of x is 1", 1, x232.getMax());
            assertEquals("Min of x is 0", 0, x312.getMin());
            assertEquals("Max of x is 1", 1, x312.getMax());
            assertEquals("Min of x is 0", 0, x322.getMin());
            assertEquals("Max of x is 1", 1, x322.getMax());
            assertEquals("Min of x is 0", 0, x332.getMin());
            assertEquals("Max of x is 1", 1, x332.getMax());
            assertEquals("Min of x is 0", 0, x113.getMin());
            assertEquals("Max of x is 1", 1, x113.getMax());
            assertEquals("Min of x is 0", 0, x123.getMin());
            assertEquals("Max of x is 1", 1, x123.getMax());
            assertEquals("Min of x is 0", 0, x133.getMin());
            assertEquals("Max of x is 1", 1, x133.getMax());
            assertEquals("Min of x is 0", 0, x213.getMin());
            assertEquals("Max of x is 1", 1, x213.getMax());
            assertEquals("Min of x is 0", 0, x223.getMin());
            assertEquals("Max of x is 1", 1, x223.getMax());
            assertEquals("Min of x is 0", 0, x233.getMin());
            assertEquals("Max of x is 1", 1, x233.getMax());
            assertEquals("Min of x is 0", 0, x313.getMin());
            assertEquals("Max of x is 1", 1, x313.getMax());
            assertEquals("Min of x is 0", 0, x323.getMin());
            assertEquals("Max of x is 1", 1, x323.getMax());
            assertEquals("Min of x is 0", 0, x333.getMin());
            assertEquals("Max of x is 1", 1, x333.getMax());
            
            assertTrue(solver.propagate());
            
            assertEquals("Min of x is 0", 0, x111.getMin());
            assertEquals("Max of x is 1", 1, x111.getMax());
            assertEquals("Min of x is 0", 0, x121.getMin());
            assertEquals("Max of x is 1", 1, x121.getMax());
            assertEquals("Min of x is 0", 0, x131.getMin());
            assertEquals("Max of x is 1", 1, x131.getMax());
            assertEquals("Min of x is 0", 0, x211.getMin());
            assertEquals("Max of x is 0", 0, x211.getMax());
            assertEquals("Min of x is 0", 0, x221.getMin());
            assertEquals("Max of x is 1", 1, x221.getMax());
            assertEquals("Min of x is 0", 0, x231.getMin());
            assertEquals("Max of x is 1", 1, x231.getMax());
            assertEquals("Min of x is 0", 0, x311.getMin());
            assertEquals("Max of x is 0", 0, x311.getMax());
            assertEquals("Min of x is 0", 0, x321.getMin());
            assertEquals("Max of x is 0", 0, x321.getMax());
            assertEquals("Min of x is 0", 0, x331.getMin());
            assertEquals("Max of x is 1", 1, x331.getMax());
            assertEquals("Min of x is 0", 0, x112.getMin());
            assertEquals("Max of x is 1", 1, x112.getMax());
            assertEquals("Min of x is 0", 0, x122.getMin());
            assertEquals("Max of x is 1", 1, x122.getMax());
            assertEquals("Min of x is 0", 0, x132.getMin());
            assertEquals("Max of x is 1", 1, x132.getMax());
            assertEquals("Min of x is 0", 0, x212.getMin());
            assertEquals("Max of x is 0", 0, x212.getMax());
            assertEquals("Min of x is 0", 0, x222.getMin());
            assertEquals("Max of x is 1", 1, x222.getMax());
            assertEquals("Min of x is 0", 0, x232.getMin());
            assertEquals("Max of x is 1", 1, x232.getMax());
            assertEquals("Min of x is 0", 0, x312.getMin());
            assertEquals("Max of x is 0", 0, x312.getMax());
            assertEquals("Min of x is 0", 0, x322.getMin());
            assertEquals("Max of x is 0", 0, x322.getMax());
            assertEquals("Min of x is 0", 0, x332.getMin());
            assertEquals("Max of x is 1", 1, x332.getMax());
            assertEquals("Min of x is 0", 0, x113.getMin());
            assertEquals("Max of x is 1", 1, x113.getMax());
            assertEquals("Min of x is 0", 0, x123.getMin());
            assertEquals("Max of x is 1", 1, x123.getMax());
            assertEquals("Min of x is 0", 0, x133.getMin());
            assertEquals("Max of x is 1", 1, x133.getMax());
            assertEquals("Min of x is 0", 0, x213.getMin());
            assertEquals("Max of x is 0", 0, x213.getMax());
            assertEquals("Min of x is 0", 0, x223.getMin());
            assertEquals("Max of x is 1", 1, x223.getMax());
            assertEquals("Min of x is 0", 0, x233.getMin());
            assertEquals("Max of x is 1", 1, x233.getMax());
            assertEquals("Min of x is 0", 0, x313.getMin());
            assertEquals("Max of x is 0", 0, x313.getMax());
            assertEquals("Min of x is 0", 0, x323.getMin());
            assertEquals("Max of x is 0", 0, x323.getMax());
            assertEquals("Min of x is 0", 0, x333.getMin());
            assertEquals("Max of x is 1", 1, x333.getMax());
            
//            assertEquals("Min of x is 0", 0, x111.getMin());
//            assertEquals("Max of x is 0", 0, x111.getMax());
//            assertEquals("Min of x is 0", 0, x121.getMin());
//            assertEquals("Max of x is 1", 1, x121.getMax());
//            assertEquals("Min of x is 0", 0, x131.getMin());
//            assertEquals("Max of x is 1", 1, x131.getMax());
//            assertEquals("Min of x is 0", 0, x211.getMin());
//            assertEquals("Max of x is 0", 0, x211.getMax());
//            assertEquals("Min of x is 0", 0, x221.getMin());
//            assertEquals("Max of x is 0", 0, x221.getMax());
//            assertEquals("Min of x is 0", 0, x231.getMin());
//            assertEquals("Max of x is 1", 1, x231.getMax());
//            assertEquals("Min of x is 0", 0, x311.getMin());
//            assertEquals("Max of x is 0", 0, x311.getMax());
//            assertEquals("Min of x is 0", 0, x321.getMin());
//            assertEquals("Max of x is 0", 0, x321.getMax());
//            assertEquals("Min of x is 0", 0, x331.getMin());
//            assertEquals("Max of x is 0", 0, x331.getMax());
//            assertEquals("Min of x is 0", 0, x112.getMin());
//            assertEquals("Max of x is 0", 0, x112.getMax());
//            assertEquals("Min of x is 0", 0, x122.getMin());
//            assertEquals("Max of x is 1", 1, x122.getMax());
//            assertEquals("Min of x is 0", 0, x132.getMin());
//            assertEquals("Max of x is 1", 1, x132.getMax());
//            assertEquals("Min of x is 0", 0, x212.getMin());
//            assertEquals("Max of x is 0", 0, x212.getMax());
//            assertEquals("Min of x is 0", 0, x222.getMin());
//            assertEquals("Max of x is 0", 0, x222.getMax());
//            assertEquals("Min of x is 0", 0, x232.getMin());
//            assertEquals("Max of x is 1", 1, x232.getMax());
//            assertEquals("Min of x is 0", 0, x312.getMin());
//            assertEquals("Max of x is 0", 0, x312.getMax());
//            assertEquals("Min of x is 0", 0, x322.getMin());
//            assertEquals("Max of x is 0", 0, x322.getMax());
//            assertEquals("Min of x is 0", 0, x332.getMin());
//            assertEquals("Max of x is 0", 0, x332.getMax());
//            assertEquals("Min of x is 0", 0, x113.getMin());
//            assertEquals("Max of x is 0", 0, x113.getMax());
//            assertEquals("Min of x is 0", 0, x123.getMin());
//            assertEquals("Max of x is 1", 1, x123.getMax());
//            assertEquals("Min of x is 0", 0, x133.getMin());
//            assertEquals("Max of x is 1", 1, x133.getMax());
//            assertEquals("Min of x is 0", 0, x213.getMin());
//            assertEquals("Max of x is 0", 0, x213.getMax());
//            assertEquals("Min of x is 0", 0, x223.getMin());
//            assertEquals("Max of x is 0", 0, x223.getMax());
//            assertEquals("Min of x is 0", 0, x233.getMin());
//            assertEquals("Max of x is 1", 1, x233.getMax());
//            assertEquals("Min of x is 0", 0, x313.getMin());
//            assertEquals("Max of x is 0", 0, x313.getMax());
//            assertEquals("Min of x is 0", 0, x323.getMin());
//            assertEquals("Max of x is 0", 0, x323.getMax());
//            assertEquals("Min of x is 0", 0, x333.getMin());
//            assertEquals("Max of x is 0", 0, x333.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
