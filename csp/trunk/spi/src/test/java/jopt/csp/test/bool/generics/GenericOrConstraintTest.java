package jopt.csp.test.bool.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanOrConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.GenericBoolExpr;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test generic booleans with the or operation
 *  
 * @author Chris Johnson
 */
public class GenericOrConstraintTest extends TestCase {
    
    BooleanVariable x1;
	BooleanVariable x2;
	BooleanVariable x3;
	BooleanVariable x11;
	BooleanVariable x12;
	BooleanVariable x13;
	BooleanVariable x21;
	BooleanVariable x22;
	BooleanVariable x23;
	BooleanVariable x31;
	BooleanVariable x32;
	BooleanVariable x33;
	BooleanVariable y11;
	BooleanVariable y12;
	BooleanVariable y13;
	BooleanVariable y21;
	BooleanVariable y22;
	BooleanVariable y23;
	BooleanVariable y31;
	BooleanVariable y32;
	BooleanVariable y33;	
	BooleanVariable y1;
	BooleanVariable y2;
	BooleanVariable y3;
	BooleanVariable z1;
	BooleanVariable z2;
	BooleanVariable z3;
	BooleanVariable x;
	BooleanVariable y;
	BooleanVariable z;
	GenericBoolExpr xiexpr;
	GenericBoolExpr xijexpr;
	GenericBoolExpr yijexpr;
	GenericBoolExpr yikexpr;
	GenericBoolExpr yiexpr;
	GenericBoolExpr yjexpr;
	GenericBoolExpr ziexpr;
	GenericBoolExpr zjexpr;
	GenericBoolExpr zkexpr;
	ConstraintStore store;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
    
    public GenericOrConstraintTest(java.lang.String testName) {
        super(testName);
    }

    
    public void setUp() {
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
        varFactory = store.getConstraintAlg().getVarFactory();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
        x1 = new BooleanVariable("x1");
        x2 = new BooleanVariable("x2");
        x3 = new BooleanVariable("x3");
        x11 = new BooleanVariable("x11");
        x12 = new BooleanVariable("x12");
        x13 = new BooleanVariable("x13");        
        x21 = new BooleanVariable("x21");
        x22 = new BooleanVariable("x22");
        x23 = new BooleanVariable("x23");
        x31 = new BooleanVariable("x31");
        x32 = new BooleanVariable("x32");
        x33 = new BooleanVariable("x33");
        y11 = new BooleanVariable("y11");
        y12 = new BooleanVariable("y12");
        y13 = new BooleanVariable("y13");        
        y21 = new BooleanVariable("y21");
        y22 = new BooleanVariable("y22");
        y23 = new BooleanVariable("y23");
        y31 = new BooleanVariable("y31");
        y32 = new BooleanVariable("y32");
        y33 = new BooleanVariable("y33"); 
        y1 = new BooleanVariable("y1");
        y2 = new BooleanVariable("y2");
        y3 = new BooleanVariable("y3");        
        z1 = new BooleanVariable("z1");
        z2 = new BooleanVariable("z2");
        z3 = new BooleanVariable("z3");
        x = new BooleanVariable("x");
        y = new BooleanVariable("y");
        z = new BooleanVariable("z");
        xiexpr = new GenericBooleanExpr("xi", new GenericIndex[]{idxI}, new BooleanVariable[]{x1, x2, x3});
        xijexpr = new GenericBooleanExpr("xij", new GenericIndex[]{idxI, idxJ}, new BooleanVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        yijexpr = new GenericBooleanExpr("yij", new GenericIndex[]{idxI, idxJ}, new BooleanVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yikexpr = new GenericBooleanExpr("yik", new GenericIndex[]{idxI, idxK}, new BooleanVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yiexpr = new GenericBooleanExpr("yi", new GenericIndex[]{idxI}, new BooleanVariable[]{y1, y2, y3});
        yjexpr = new GenericBooleanExpr("yj", new GenericIndex[]{idxJ}, new BooleanVariable[]{y1, y2, y3});
        ziexpr = new GenericBooleanExpr("zi", new GenericIndex[]{idxI}, new BooleanVariable[]{z1, z2, z3});
        zjexpr = new GenericBooleanExpr("zj", new GenericIndex[]{idxJ}, new BooleanVariable[]{z1, z2, z3});
        zkexpr = new GenericBooleanExpr("zk", new GenericIndex[]{idxK}, new BooleanVariable[]{z1, z2, z3});
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
    	x = null;
    	y = null;
    	z = null;
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
    }
    
    public void testSettingX1FalseGenericVarConstTrue() {
        try {
            BooleanOrConstraint constraint = new BooleanOrConstraint(xiexpr, y, false, true);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();

            //The constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Now the constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is determined
            assertTrue("y isTrue should be true", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
		}
    }
    
    public void testSettingX1FalseGenericVarConstFalse() {
        try {
            BooleanOrConstraint constraint = new BooleanOrConstraint(xiexpr, y, false, false);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();

            //The constraint remains undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is determined
            assertFalse("y isTrue should be false", y.isTrue());
            assertTrue("y isFalse should be true", y.isFalse());
            //The other variables should be determined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertTrue("x2 isFalse should be true", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertTrue("x3 isFalse should be true", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
}
