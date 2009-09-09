package jopt.csp.test.bool.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanImpliesTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.GenericBoolExpr;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test generic booleans with the implies operation
 *  
 * @author Chris Johnson
 */
public class GenericImpliesTwoVarConstraintTest extends TestCase {
    
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
    
    public GenericImpliesTwoVarConstraintTest(java.lang.String testName) {
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
    
    public void testSettingX1FalseGenericVar() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, y);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            x1.setFalse();

            //The constraint remains undetermined until propagation
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

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingYFalseX1TrueGenericVar() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, y);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            y.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x1 isTrue should be false", x1.isTrue());
            assertFalse("x1 isFalse should be false", x1.isFalse());
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            
            //Determine another variable
            x1.setTrue();
            
            //Now the constraint is determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            //Propagate again
            store.propagate();

            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX1TrueYFalseGenericVar() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, y);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine the variables
            x1.setTrue();
            y.setFalse();

            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingXFalseVarGeneric() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(x, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            x.setFalse();

            //The constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());

            //The Yi's should be undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericSameIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY2FalseGenericGenericSameIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setTrue();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Now we determine another one of the variables
            y2.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //Check the other variables
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Check the other variables
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertTrue("x2 isFalse should be true", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericDiffIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yjexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY2FalseGenericGenericDiffIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yjexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setTrue();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Now we determine another one of the variables
            y2.setFalse();

            //The constraint is determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }

    public void testSettingX11FalseGenericGenericComboIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xijexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x11.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x12 isTrue should be false", x12.isTrue());
            assertFalse("x12 isFalse should be false", x12.isFalse());
            assertFalse("x13 isTrue should be false", x13.isTrue());
            assertFalse("x13 isFalse should be false", x13.isFalse());
            assertFalse("x21 isTrue should be false", x21.isTrue());
            assertFalse("x21 isFalse should be false", x21.isFalse());
            assertFalse("x22 isTrue should be false", x22.isTrue());
            assertFalse("x22 isFalse should be false", x22.isFalse());
            assertFalse("x23 isTrue should be false", x23.isTrue());
            assertFalse("x23 isFalse should be false", x23.isFalse());
            assertFalse("x31 isTrue should be false", x31.isTrue());
            assertFalse("x31 isFalse should be false", x31.isFalse());
            assertFalse("x32 isTrue should be false", x32.isTrue());
            assertFalse("x32 isFalse should be false", x32.isFalse());
            assertFalse("x33 isTrue should be false", x33.isTrue());
            assertFalse("x33 isFalse should be false", x33.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueGenericGenericSameIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setTrue();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The remaining variables remain undetermined until propagation
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            //Remaining Yi's are still undetermined
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY1FalseGenericGenericSameIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine some of the variables
            x1.setTrue();
            y1.setFalse();

            //The constraint is determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX1TrueGenericGenericDiffIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xiexpr, yjexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setTrue();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Now we will propagate
            store.propagate();

            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            //The other variables should be determined
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());

            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX11TrueGenericGenericComboSimilarIndex() {
        try {
            BooleanImpliesTwoVarConstraint constraint = new BooleanImpliesTwoVarConstraint(xijexpr, yiexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x11.setTrue();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x12 isTrue should be false", x12.isTrue());
            assertFalse("x12 isFalse should be false", x12.isFalse());
            assertFalse("x13 isTrue should be false", x13.isTrue());
            assertFalse("x13 isFalse should be false", x13.isFalse());
            assertFalse("x21 isTrue should be false", x21.isTrue());
            assertFalse("x21 isFalse should be false", x21.isFalse());
            assertFalse("x22 isTrue should be false", x22.isTrue());
            assertFalse("x22 isFalse should be false", x22.isFalse());
            assertFalse("x23 isTrue should be false", x23.isTrue());
            assertFalse("x23 isFalse should be false", x23.isFalse());
            assertFalse("x31 isTrue should be false", x31.isTrue());
            assertFalse("x31 isFalse should be false", x31.isFalse());
            assertFalse("x32 isTrue should be false", x32.isTrue());
            assertFalse("x32 isFalse should be false", x32.isFalse());
            assertFalse("x33 isTrue should be false", x33.isTrue());
            assertFalse("x33 isFalse should be false", x33.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            //Propagate
            store.propagate();
            
            //Now the constraint still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y2 isFalse should be false", y1.isFalse());
            //The remaining Yj's should be undetermined
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            //The remaining Xij's should be undetermined
            assertFalse("x12 isTrue should be false", x12.isTrue());
            assertFalse("x12 isFalse should be false", x12.isFalse());
            assertFalse("x13 isTrue should be false", x13.isTrue());
            assertFalse("x13 isFalse should be false", x13.isFalse());
            assertFalse("x21 isTrue should be false", x21.isTrue());
            assertFalse("x21 isFalse should be false", x21.isFalse());
            assertFalse("x22 isTrue should be false", x22.isTrue());
            assertFalse("x22 isFalse should be false", x22.isFalse());
            assertFalse("x23 isTrue should be false", x23.isTrue());
            assertFalse("x23 isFalse should be false", x23.isFalse());
            assertFalse("x31 isTrue should be false", x31.isTrue());
            assertFalse("x31 isFalse should be false", x31.isFalse());
            assertFalse("x32 isTrue should be false", x32.isTrue());
            assertFalse("x32 isFalse should be false", x32.isFalse());
            assertFalse("x33 isTrue should be false", x33.isTrue());
            assertFalse("x33 isFalse should be false", x33.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
}
