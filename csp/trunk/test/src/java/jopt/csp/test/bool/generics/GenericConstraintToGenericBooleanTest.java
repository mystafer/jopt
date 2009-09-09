package jopt.csp.test.bool.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.GenericBoolExpr;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test mapping generic constraints to generic booleans
 *  
 * @author Chris Johnson
 */
public class GenericConstraintToGenericBooleanTest extends TestCase {
    
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
	IntVariable y;
	IntVariable z;
	
    GenericBoolExpr biexpr;
	GenericBoolExpr bijexpr;
	GenericBoolExpr bikexpr;

	CspSolver solver;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
    
    public GenericConstraintToGenericBooleanTest(java.lang.String testName) {
        super(testName);
    }

    public void setUp () {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
        varFactory = solver.getVarFactory();
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
        y = null;
        z = null;
        biexpr = null;
        bijexpr = null;
        bikexpr = null;
    	solver = null;
    	varFactory = null;
        idxI = null;
        idxJ = null;
        idxK = null;
    }
    
    public void testGenericConstraintTrueToGenericExprSameIndex() {
        try {
            CspConstraint pconstraint = xiexpr.neq(yiexpr);
            biexpr = new GenericBooleanExpr("biexpr", new GenericIndex[]{idxI}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(biexpr, (GraphConstraint) pconstraint);
        	solver.addConstraint(constraint);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
            
            //Determine domains
            x1.setMax(40);
            y1.setMin(50);
            
            //Propagate
            solver.propagate();
            
            //B1 should be true, the remaining Bi's should be undetermined
            assertTrue("b1 isTrue should be true", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertTrue("biexpr isAnyTrue should be true", biexpr.isAnyTrue());
            
            //Determine remaining domains
            x2.setMax(49);
            y2.setMin(51);
            x3.setMax(65);
            y3.setMin(67);
            
            //Propagate
            solver.propagate();
            
            //Bi should be true
            assertTrue("b1 isTrue should be true", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertTrue("b2 isTrue should be true", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertTrue("b3 isTrue should be true", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertTrue("biexpr isTrue should be true", biexpr.isTrue());
            assertTrue("biexpr isAnyTrue should be true", biexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintTrueToGenericExprDiffIndex() {
        try {
            CspConstraint pconstraint = xiexpr.neq(yjexpr);
            bijexpr = new GenericBooleanExpr("bijexpr", new GenericIndex[]{idxI, idxJ}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(bijexpr, (GraphConstraint) pconstraint);
        	solver.addConstraint(constraint);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b11 isTrue should be false", bijexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bijexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bijexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bijexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bijexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertFalse("bijexpr isAnyFalse should be false", bijexpr.isAnyFalse());
            assertFalse("bijexpr isTrue should be false", bijexpr.isTrue());
            assertFalse("bijexpr isAnyTrue should be false", bijexpr.isAnyTrue());
            
            //Determine domains
            x1.setValue(40);
            y1.setValue(50);
            
            //Propagate
            solver.propagate();
            
            //Check the Bij's
            assertTrue("b11 isTrue should be true", bijexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bijexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bijexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bijexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bijexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertFalse("bijexpr isAnyFalse should be false", bijexpr.isAnyFalse());
            assertFalse("bijexpr isTrue should be false", bijexpr.isTrue());
            assertTrue("bijexpr isAnyTrue should be true", bijexpr.isAnyTrue());
            
            //Determine remaining domains
            x2.setValue(49);
            y2.setValue(51);
            x3.setValue(65);
            y3.setValue(67);
            
            //Propagate
            solver.propagate();
            
            //Bij should be true
            assertTrue("b11 isTrue should be true", bijexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bijexpr.getBoolExpression(0).isFalse());
            assertTrue("b12 isTrue should be true", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertTrue("b13 isTrue should be true", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertTrue("b21 isTrue should be true", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertTrue("b22 isTrue should be true", bijexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bijexpr.getBoolExpression(4).isFalse());
            assertTrue("b23 isTrue should be true", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertTrue("b31 isTrue should be true", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertTrue("b32 isTrue should be true", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertTrue("b33 isTrue should be true", bijexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertFalse("bijexpr isAnyFalse should be false", bijexpr.isAnyFalse());
            assertTrue("bijexpr isTrue should be true", bijexpr.isTrue());
            assertTrue("bijexpr isAnyTrue should be true", bijexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintFalseToGenericExprSameIndex() {
        try {
            CspConstraint pconstraint = xiexpr.neq(yiexpr);
            biexpr = new GenericBooleanExpr("biexpr", new GenericIndex[]{idxI}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(biexpr, (GraphConstraint) pconstraint);
            solver.addConstraint(constraint);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
            
            //Determine domains
            x1.setValue(40);
            y1.setValue(40);
            
            //Propagate
            solver.propagate();
            
            //B1 should be determined, the remaining Bi's should be undetermined
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertTrue("b1 isFalse should be true", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertTrue("biexpr isAnyFalse should be true", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
            
            //Determine remaining domains
            x2.setValue(49);
            y2.setValue(49);
            x3.setValue(51);
            y3.setValue(51);
            
            //Propagate
            solver.propagate();
            
            //Bi should be false
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertTrue("b1 isFalse should be true", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertTrue("b2 isFalse should be true", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertTrue("b3 isFalse should be true", biexpr.getBoolExpression(2).isFalse());
            
            assertTrue("biexpr isFalse should be true", biexpr.isFalse());
            assertTrue("biexpr isAnyFalse should be true", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintFalseToGenericExprDiffIndex() {
        try {
            CspConstraint pconstraint = xiexpr.neq(yjexpr);
            bijexpr = new GenericBooleanExpr("bijexpr", new GenericIndex[]{idxI, idxJ}, pconstraint);
            //CspConstraint constraint = new BooleanEqTwoVarConstraint(bijexpr, (GraphConstraint) pconstraint);
        	//solver.addConstraint(constraint);
        	solver.addConstraint((CspBooleanExpr)bijexpr);
            //The boolean variables involved should be undetermined
            assertFalse("b11 isTrue should be false", bijexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bijexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bijexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bijexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bijexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertFalse("bijexpr isAnyFalse should be false", bijexpr.isAnyFalse());
            assertFalse("bijexpr isTrue should be false", bijexpr.isTrue());
            assertFalse("bijexpr isAnyTrue should be false", bijexpr.isAnyTrue());
            
            //Determine domains
            x1.setValue(40);
            y1.setValue(40);
            
            //Propagate
            solver.propagate();
            
            //Check the Bij's
            assertFalse("b11 isTrue should be false", bijexpr.getBoolExpression(0).isTrue());
            assertTrue("b11 isFalse should be true", bijexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bijexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bijexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bijexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertTrue("bijexpr isAnyFalse should be true", bijexpr.isAnyFalse());
            assertFalse("bijexpr isTrue should be false", bijexpr.isTrue());
            assertFalse("bijexpr isAnyTrue should be false", bijexpr.isAnyTrue());
            
            //Determine remaining domains
            x2.setValue(49);
            y2.setValue(49);
            x3.setValue(51);
            y3.setValue(51);
            
            //Propagate
            solver.propagate();
            
            //Check the Bij's
            assertFalse("b11 isTrue should be false", bijexpr.getBoolExpression(0).isTrue());
            assertTrue("b11 isFalse should be true", bijexpr.getBoolExpression(0).isFalse());
            assertTrue("b12 isTrue should be true", bijexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bijexpr.getBoolExpression(1).isFalse());
            assertTrue("b13 isTrue should be true", bijexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bijexpr.getBoolExpression(2).isFalse());
            assertTrue("b21 isTrue should be true", bijexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bijexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bijexpr.getBoolExpression(4).isTrue());
            assertTrue("b22 isFalse should be true", bijexpr.getBoolExpression(4).isFalse());
            assertTrue("b23 isTrue should be true", bijexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bijexpr.getBoolExpression(5).isFalse());
            assertTrue("b31 isTrue should be true", bijexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bijexpr.getBoolExpression(6).isFalse());
            assertTrue("b32 isTrue should be true", bijexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bijexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bijexpr.getBoolExpression(8).isTrue());
            assertTrue("b33 isFalse should be true", bijexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bijexpr isFalse should be false", bijexpr.isFalse());
            assertTrue("bijexpr isAnyFalse should be true", bijexpr.isAnyFalse());
            assertFalse("bijexpr isTrue should be false", bijexpr.isTrue());
            assertTrue("bijexpr isAnyTrue should be true", bijexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintToGenericExprPostSameIndex() {
        try {
            CspConstraint pconstraint = xiexpr.add(yiexpr).eq(ziexpr);
            biexpr = new GenericBooleanExpr("biexpr", new GenericIndex[]{idxI}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(biexpr, (GraphConstraint) pconstraint);
            solver.addConstraint(constraint);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
            
            //Determine a domain and a boolean variable
            BooleanVariable b1 = new BooleanVariable("b1", (CspBooleanExpr) biexpr.getBoolExpression(0));
            solver.addConstraint(b1.eq((CspBooleanExpr) biexpr.getBoolExpression(0)));
            b1.setTrue();
            x1.setValue(40);
            y1.setValue(40);
            
            //Check int vars
            assertFalse("z1 is not bound", z1.isBound());
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
            
            //Propagate
            solver.propagate();
            
            //B1 should be determined, the remaining Bi's should be undetermined
            assertTrue("b1 isTrue should be true", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be true", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertTrue("biexpr isAnyTrue should be true", biexpr.isAnyTrue());
            
            //Check int vars
            assertEquals("z1 has to be 80", 80, z1.getMax());
            assertEquals("z1 has to be 80", 80, z1.getMin());
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintToGenericExprPostDiffIndex() {
        try {
            CspConstraint pconstraint = xiexpr.add(yiexpr).eq(zkexpr);
            bikexpr = new GenericBooleanExpr("bikexpr", new GenericIndex[]{idxI, idxK}, pconstraint);
        	solver.addConstraint((CspBooleanExpr)bikexpr);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b11 isTrue should be false", bikexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bikexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bikexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bikexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bikexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bikexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bikexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bikexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bikexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bikexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bikexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bikexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bikexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bikexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bikexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bikexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bikexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bikexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bikexpr isFalse should be false", bikexpr.isFalse());
            assertFalse("bikexpr isAnyFalse should be false", bikexpr.isAnyFalse());
            assertFalse("bikexpr isTrue should be false", bikexpr.isTrue());
            assertFalse("bikexpr isAnyTrue should be false", bikexpr.isAnyTrue());
            
            //Determine a domain and a boolean variable
            BooleanVariable b11 = new BooleanVariable("b11", (CspBooleanExpr) bikexpr.getBoolExpression(0));
            solver.addConstraint(b11.eq((CspBooleanExpr) bikexpr.getBoolExpression(0)));
            b11.setTrue();
            x1.setValue(40);
            y1.setValue(40);
            
            //Check int vars
            assertFalse("z1 is not bound", z1.isBound());
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
            
            //Propagate
            solver.propagate();
            
            //Check the Bij's
            assertFalse("b12 isTrue should be false", bikexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bikexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bikexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bikexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bikexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bikexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bikexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bikexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bikexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bikexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bikexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bikexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bikexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bikexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bikexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bikexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bikexpr isFalse should be false", bikexpr.isFalse());
            assertFalse("bikexpr isAnyFalse should be false", bikexpr.isAnyFalse());
            assertFalse("bikexpr isTrue should be false", bikexpr.isTrue());
            assertTrue("bikexpr isAnyTrue should be true", bikexpr.isAnyTrue());
            
            //Check int vars
            assertEquals("z1 has to be 80", 80, z1.getMax());
            assertEquals("z1 has to be 80", 80, z1.getMin());
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericConstraintToGenericExprPostAndFailSameIndex() {
        try {
            CspConstraint pconstraint = xiexpr.add(yiexpr).eq(ziexpr);
            biexpr = new GenericBooleanExpr("biexpr", new GenericIndex[]{idxI}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(biexpr, (GraphConstraint) pconstraint);
        	solver.addConstraint(constraint);
        	
            //Determine domains
            x2.setValue(49);
            y2.setValue(51);
            x3.setValue(48);
            y3.setValue(52);
            
            //Propagate
            solver.propagate();
            
            //Bi should be false
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("b2 isTrue should be false", biexpr.getBoolExpression(1).isTrue());
            assertFalse("b2 isFalse should be false", biexpr.getBoolExpression(1).isFalse());
            assertFalse("b3 isTrue should be false", biexpr.getBoolExpression(2).isTrue());
            assertFalse("b3 isFalse should be false", biexpr.getBoolExpression(2).isFalse());
            
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertFalse("biexpr isAnyFalse should be false", biexpr.isAnyFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isAnyTrue should be false", biexpr.isAnyTrue());
            
            //Determine some domains and a boolean variable
            BooleanVariable b1 = new BooleanVariable("b1", (CspBooleanExpr) biexpr.getBoolExpression(0));
            solver.addConstraint(b1.eq((CspBooleanExpr) biexpr.getBoolExpression(0)));
            b1.setTrue();
            x1.setValue(40);
            y1.setValue(40);
            z1.setValue(81);
            
            //Check int vars
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
            
            //Propagate
            assertFalse("Propagation should fail", solver.propagate());
        }
        catch (PropagationFailureException pfe) {
            fail();
		}
    }
    
    public void testGenericConstraintToGenericExprPostandFailDiffIndex() {
        try {
            CspConstraint pconstraint = xiexpr.add(yiexpr).eq(zkexpr);
            bikexpr = new GenericBooleanExpr("bikexpr", new GenericIndex[]{idxI, idxK}, pconstraint);
            CspConstraint constraint = new BooleanEqTwoVarConstraint(bikexpr, (GraphConstraint) pconstraint);
            solver.addConstraint(constraint);
        	
            //The boolean variables involved should be undetermined
            assertFalse("b11 isTrue should be false", bikexpr.getBoolExpression(0).isTrue());
            assertFalse("b11 isFalse should be false", bikexpr.getBoolExpression(0).isFalse());
            assertFalse("b12 isTrue should be false", bikexpr.getBoolExpression(1).isTrue());
            assertFalse("b12 isFalse should be false", bikexpr.getBoolExpression(1).isFalse());
            assertFalse("b13 isTrue should be false", bikexpr.getBoolExpression(2).isTrue());
            assertFalse("b13 isFalse should be false", bikexpr.getBoolExpression(2).isFalse());
            assertFalse("b21 isTrue should be false", bikexpr.getBoolExpression(3).isTrue());
            assertFalse("b21 isFalse should be false", bikexpr.getBoolExpression(3).isFalse());
            assertFalse("b22 isTrue should be false", bikexpr.getBoolExpression(4).isTrue());
            assertFalse("b22 isFalse should be false", bikexpr.getBoolExpression(4).isFalse());
            assertFalse("b23 isTrue should be false", bikexpr.getBoolExpression(5).isTrue());
            assertFalse("b23 isFalse should be false", bikexpr.getBoolExpression(5).isFalse());
            assertFalse("b31 isTrue should be false", bikexpr.getBoolExpression(6).isTrue());
            assertFalse("b31 isFalse should be false", bikexpr.getBoolExpression(6).isFalse());
            assertFalse("b32 isTrue should be false", bikexpr.getBoolExpression(7).isTrue());
            assertFalse("b32 isFalse should be false", bikexpr.getBoolExpression(7).isFalse());
            assertFalse("b33 isTrue should be false", bikexpr.getBoolExpression(8).isTrue());
            assertFalse("b33 isFalse should be false", bikexpr.getBoolExpression(8).isFalse());
            
            assertFalse("bikexpr isFalse should be false", bikexpr.isFalse());
            assertFalse("bikexpr isAnyFalse should be false", bikexpr.isAnyFalse());
            assertFalse("bikexpr isTrue should be false", bikexpr.isTrue());
            assertFalse("bikexpr isAnyTrue should be false", bikexpr.isAnyTrue());
            
            //Determine some domains and a boolean variable
            BooleanVariable b11 = new BooleanVariable("b11", (CspBooleanExpr) bikexpr.getBoolExpression(0));
            solver.addConstraint(b11.eq((CspBooleanExpr) bikexpr.getBoolExpression(0)));
            b11.setTrue();
            x1.setValue(40);
            y1.setValue(40);
            z1.setValue(81);
            
            //Check int vars
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
            
            //Propagate
            assertFalse("Propagation should fail", solver.propagate());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
}
