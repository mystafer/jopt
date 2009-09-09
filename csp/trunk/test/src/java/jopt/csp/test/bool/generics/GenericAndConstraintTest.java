package jopt.csp.test.bool.generics;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanAndConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.GenericBoolExpr;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test generic booleans with the and operation
 *  
 * @author Chris Johnson
 */
public class GenericAndConstraintTest extends TestCase {
    
    GenericBooleanConstant yiconst;
    GenericBooleanConstant yjconst;
    GenericBooleanConstant yijconst;
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
    
    public GenericAndConstraintTest(java.lang.String testName) {
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
        yiconst = new GenericBooleanConstant("yiconst", new GenericIndex[]{idxI}, new boolean[]{false, false, true});
        yjconst = new GenericBooleanConstant("yjconst", new GenericIndex[]{idxJ}, new boolean[]{true, true, true});
        yijconst = new GenericBooleanConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new boolean[]{true, true, false, false, false, true, false, true, true});
    }
    
    public void tearDown() {
        yiconst = null;
        yjconst = null;
        yijconst = null;
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
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, true);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();

            //The constraint is determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX1FalseGenericVarConstFalse() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, false);
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

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericVarVar() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint not yet true
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            //Z should be undetermined
            assertFalse("z isTrue should be false", z.isTrue());
            assertTrue("z isFalse should be true", z.isFalse());
            //The remaining Xi's should be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueX2FalseGenericVarVar() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, z);
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
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Determine another variable
            x2.setFalse();
            
            //The other variables should still be undetermined
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should now be false
            assertFalse("z isTrue should be false", z.isTrue());
            assertTrue("z isFalse should be true", z.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueZTrueGenericVarVar() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, z);
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
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y and Z are still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Determine another variable
            z.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y should now be true
            assertTrue("y isTrue should be true", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseZTrueGenericVarVar() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, z);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            x1.setFalse();
            z.setTrue();
            
            //The constraint remains is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX1TrueYTrueGenericVarVar() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, z);
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
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            
            //Determine another variable
            y.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should now be true
            assertTrue("z isTrue should be true", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            //The remaining Xi's should be true as well
            assertTrue("x2 isTrue should be true", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertTrue("x3 isTrue should be true", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericVarSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            //Z should now be false
            assertFalse("z isTrue should be false", z.isTrue());
            assertTrue("z isFalse should be true", z.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY1TrueGenericGenericVarSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should now be true
            assertTrue("z isTrue should be true", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            //The remaining Yj's and Xi's should be true as well
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertTrue("x2 isTrue should be true", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertTrue("x3 isTrue should be true", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY2TrueGenericGenericVarSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            
            //Determine another variable
            y2.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //The constraint is still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z is still undetermined
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericVarGenericSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());

            //Z1 should now be false, the remainder should be undermined
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zi is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zi is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingY3FalseVarGenericGenericSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(x, yiexpr, false, ziexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            y3.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x isTrue should be false", x.isTrue());
            assertFalse("x isFalse should be false", x.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //X is still undetermined
            assertFalse("x isTrue should be false", x.isTrue());
            assertFalse("x isFalse should be false", x.isFalse());

            //Z3 should now be false, the remainder should be undermined
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertTrue("z3 isFalse should be true", z3.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zi is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zi is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericVarDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is true
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            //Z should now be false
            assertFalse("z isTrue should be false", z.isTrue());
            assertTrue("z isFalse should be true", z.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }

    public void testSettingX1TrueY1TrueGenericGenericVarDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should now be true
            assertTrue("z isTrue should be true", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            //The remaining Yj's and Xi's should be true as well
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertTrue("x2 isTrue should be true", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertTrue("x3 isTrue should be true", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
    	}
    }
    
    public void testSettingX1TrueY2TrueGenericGenericVarDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, z);
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
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            
            //Determine another variable
            y2.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
            //Propagate again
            store.propagate();
            
            //The constraint is now determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z is now determined
            assertTrue("z isTrue should be true", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            //The remaining Yj's and Xi's should be true as well
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertTrue("x2 isTrue should be true", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertTrue("x3 isTrue should be true", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericVarGenericDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, y, false, zjexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());

            //Zj should now be false
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertTrue("z2 isFalse should be true", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertTrue("z3 isFalse should be true", z3.isFalse());
            
            assertTrue("all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zj is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zi is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingY3FalseVarGenericGenericDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(x, yjexpr, false, ziexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            y3.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x isTrue should be false", x.isTrue());
            assertFalse("x isFalse should be false", x.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //X is still undetermined
            assertFalse("x isTrue should be false", x.isTrue());
            assertFalse("x isFalse should be false", x.isFalse());

            //Zi should now be false
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertTrue("z2 isFalse should be true", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertTrue("z3 isFalse should be true", z3.isFalse());
            
            assertTrue("all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zi is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zi is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX11FalseGenericGenericVarComboIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xijexpr, yiexpr, false, z);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x11.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x12 isTrue should be false", x2.isTrue());
            assertFalse("x12 isFalse should be false", x2.isFalse());
            assertFalse("x13 isTrue should be false", x3.isTrue());
            assertFalse("x13 isFalse should be false", x3.isFalse());
            assertFalse("x21 isTrue should be false", x2.isTrue());
            assertFalse("x21 isFalse should be false", x2.isFalse());
            assertFalse("x22 isTrue should be false", x3.isTrue());
            assertFalse("x22 isFalse should be false", x3.isFalse());
            assertFalse("x23 isTrue should be false", x2.isTrue());
            assertFalse("x23 isFalse should be false", x2.isFalse());
            assertFalse("x31 isTrue should be false", x3.isTrue());
            assertFalse("x31 isFalse should be false", x3.isFalse());
            assertFalse("x32 isTrue should be false", x2.isTrue());
            assertFalse("x32 isFalse should be false", x2.isFalse());
            assertFalse("x33 isTrue should be false", x3.isTrue());
            assertFalse("x33 isFalse should be false", x3.isFalse());
            assertFalse("y1 isTrue should be false", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z isTrue should be false", z.isTrue());
            assertFalse("z isFalse should be false", z.isFalse());
            
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
            //Z should now be false
            assertFalse("z isTrue should be false", z.isTrue());
            assertTrue("z isFalse should be true", z.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX11FalseGenericVarGenericComboIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xijexpr, y, false, ziexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x11.setFalse();

            //The constraint remains undetermined until propagation
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("x12 isTrue should be false", x2.isTrue());
            assertFalse("x12 isFalse should be false", x2.isFalse());
            assertFalse("x13 isTrue should be false", x3.isTrue());
            assertFalse("x13 isFalse should be false", x3.isFalse());
            assertFalse("x21 isTrue should be false", x2.isTrue());
            assertFalse("x21 isFalse should be false", x2.isFalse());
            assertFalse("x22 isTrue should be false", x3.isTrue());
            assertFalse("x22 isFalse should be false", x3.isFalse());
            assertFalse("x23 isTrue should be false", x2.isTrue());
            assertFalse("x23 isFalse should be false", x2.isFalse());
            assertFalse("x31 isTrue should be false", x3.isTrue());
            assertFalse("x31 isFalse should be false", x3.isFalse());
            assertFalse("x32 isTrue should be false", x2.isTrue());
            assertFalse("x32 isFalse should be false", x2.isFalse());
            assertFalse("x33 isTrue should be false", x3.isTrue());
            assertFalse("x33 isFalse should be false", x3.isFalse());
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Now we will propagate
            store.propagate();

            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("y isTrue should be false", y.isTrue());
            assertFalse("y isFalse should be false", y.isFalse());
            //Z1 should now be false, the remaining Zi's should be undetermined
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericGenericSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            //Z1 should now be false, the remainder should be undermined
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Z is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Z is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY1TrueGenericGenericGenericSameIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z1 should now be true, the remaining Zi's should be undetermined
            assertTrue("z1 isTrue should be true", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            //The remaining Yj's and Xi's should be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertFalse("not one Zi is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertTrue("at least one Zi is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseZTrueGenericGenericVarDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, z);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();
            z.setTrue();

            //The constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
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
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX1FalseGenericGenericGenericSimilarIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiexpr, false, zjexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            //Zj should now be false
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertTrue("z2 isFalse should be true", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertTrue("z3 isFalse should be true", z3.isFalse());
            
            assertTrue("all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zj is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zj is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY1TrueGenericGenericGenericSimilarIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z1 should now be true, the remaining Zi's should be undetermined
            assertTrue("z1 isTrue should be true", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            //The remaining Yj's should be true and Xi's should be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseGenericGenericGenericDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, zkexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            //Zk should now be false
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertTrue("z2 isFalse should be true", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertTrue("z3 isFalse should be true", z3.isFalse());
            
            assertTrue("all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zk is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zk is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1TrueY1TrueGenericGenericGenericDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, zkexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
            //The other variables should still be undetermined
            assertFalse("x2 isTrue should be false", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertFalse("x3 isTrue should be false", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Zk should now be true
            assertTrue("z1 isTrue should be true", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertTrue("z2 isTrue should be true", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertTrue("z3 isTrue should be true", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            //The remaining Yj's and Xi's should be true as well
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertTrue("x2 isTrue should be true", x2.isTrue());
            assertFalse("x2 isFalse should be false", x2.isFalse());
            assertTrue("x3 isTrue should be true", x3.isTrue());
            assertFalse("x3 isFalse should be false", x3.isFalse());
            
            assertFalse("all Z's are not false", ziexpr.isAllFalse());
            assertFalse("not one Zk is false", ziexpr.isAnyFalse());
            assertTrue("all Z's are true", ziexpr.isAllTrue());
            assertTrue("at least one Zk is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX1FalseZ3TrueGenericGenericGenericDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yjexpr, false, zkexpr);
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            x1.setFalse();
            z3.setTrue();

            //The constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            
            //Now we will propagate
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
		}
    }
    
    public void testSettingX11FalseGenericGenericGenericComboIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xijexpr, yiexpr, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            //Zi should now be false
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertTrue("z1 isFalse should be true", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertTrue("at least one Zk is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertFalse("no Zk is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX11TrueY1TrueGenericGenericGenericComboSimilarIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xijexpr, yiexpr, false, ziexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            
            //Determine another variable
            y1.setTrue();
            
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
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z1 should now be true, the remaining Zi's should be undetermined
            assertTrue("z1 isTrue should be true", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            //The remaining Yj's should be undetermined
            assertFalse("y2 isTrue should be false", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertFalse("y3 isTrue should be false", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            //Check the remaining Xij's
            assertTrue("x12 isTrue should be true", x12.isTrue());
            assertFalse("x12 isFalse should be false", x12.isFalse());
            assertTrue("x13 isTrue should be true", x13.isTrue());
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
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertFalse("not one Zi is false", ziexpr.isAnyFalse());
            assertFalse("not all Z's are true", ziexpr.isAllTrue());
            assertTrue("at least one Zi is true", ziexpr.isAnyTrue());
            
            assertFalse("not all X's are false", ziexpr.isAllFalse());
            assertFalse("not one Xij is false", ziexpr.isAnyFalse());
            assertFalse("not all Xij's are true", ziexpr.isAllTrue());
            assertTrue("at least one Xij is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSettingX11TrueZ1TrueGenericGenericGenericComboDiffIndex() {
        try {
            BooleanAndConstraint constraint = new BooleanAndConstraint(xijexpr, yiexpr, false, zkexpr);
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
            assertFalse("z1 isTrue should be false", z1.isTrue());
            assertFalse("z1 isFalse should be false", z1.isFalse());
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
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
            
            //Determine another variable
            z1.setTrue();
            
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
            assertFalse("z2 isTrue should be false", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertFalse("z3 isTrue should be false", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            
            //Propagate again
            store.propagate();
            
            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //All vars should be determined to be true
            assertTrue("z2 isTrue should be true", z2.isTrue());
            assertFalse("z2 isFalse should be false", z2.isFalse());
            assertTrue("z3 isTrue should be true", z3.isTrue());
            assertFalse("z3 isFalse should be false", z3.isFalse());
            assertTrue("y1 isTrue should be true", y1.isTrue());
            assertFalse("y1 isFalse should be false", y1.isFalse());
            assertTrue("y2 isTrue should be true", y2.isTrue());
            assertFalse("y2 isFalse should be false", y2.isFalse());
            assertTrue("y3 isTrue should be true", y3.isTrue());
            assertFalse("y3 isFalse should be false", y3.isFalse());
            //Check the remaining Xij's
            assertTrue("x12 isTrue should be true", x12.isTrue());
            assertFalse("x12 isFalse should be false", x12.isFalse());
            assertTrue("x13 isTrue should be true", x13.isTrue());
            assertFalse("x13 isFalse should be false", x13.isFalse());
            assertTrue("x21 isTrue should be true", x21.isTrue());
            assertFalse("x21 isFalse should be false", x21.isFalse());
            assertTrue("x22 isTrue should be true", x22.isTrue());
            assertFalse("x22 isFalse should be false", x22.isFalse());
            assertTrue("x23 isTrue should be true", x23.isTrue());
            assertFalse("x23 isFalse should be false", x23.isFalse());
            assertTrue("x31 isTrue should be true", x31.isTrue());
            assertFalse("x31 isFalse should be false", x31.isFalse());
            assertTrue("x32 isTrue should be true", x32.isTrue());
            assertFalse("x32 isFalse should be false", x32.isFalse());
            assertTrue("x33 isTrue should be true", x33.isTrue());
            assertFalse("x33 isFalse should be false", x33.isFalse());
            
            assertFalse("not all Z's are false", ziexpr.isAllFalse());
            assertFalse("not one Zi is false", ziexpr.isAnyFalse());
            assertTrue("all Z's are true", ziexpr.isAllTrue());
            assertTrue("at least one Zi is true", ziexpr.isAnyTrue());
            
            assertFalse("not all X's are false", ziexpr.isAllFalse());
            assertFalse("not one Xij is false", ziexpr.isAnyFalse());
            assertTrue("all Xij's are true", ziexpr.isAllTrue());
            assertTrue("at least one Xij is true", ziexpr.isAnyTrue());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testConstantAddSingleUniformIndexFalse() {
		BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiconst, ziexpr);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
		    //Since Y is a constant and Y1 and Y2 are false, setting z1 to true will make
		    //the constraint false.
		    z2.setTrue();
		    assertTrue("constraint is now false", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
    
    public void testConstantAddSingleUniformIndexFalseProp() {
		BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiconst, ziexpr);
		
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
		    store.addConstraint(constraint);
		    assertFalse("z1 is not false yet", z1.isFalse());
			assertFalse("z1 is not true yet", z1.isTrue());
			assertFalse("z2 is not false yet", z2.isFalse());
			assertFalse("z2 is not true yet", z2.isTrue());
			store.propagate();
			assertTrue("z1 is now false", z1.isFalse());
			assertFalse("z1 is not true yet", z1.isTrue());
			assertTrue("z2 is now false yet", z2.isFalse());
			assertFalse("z2 is not true yet", z2.isTrue());
			assertFalse("z3 is not false", z3.isFalse());
			assertFalse("z3 is not true yet", z3.isTrue());
			assertFalse("x3 is not false", x3.isFalse());
			assertFalse("x3 is not true yet", x3.isTrue());
			z3.setTrue();
		    store.propagate();
		    assertFalse("x3 is not false", x3.isFalse());
			assertTrue("x3 is now true", x3.isTrue());
		    
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
    
	public void testConstantAddSingleUniformIndexTrue() {
		BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, yiconst, ziexpr);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
		    //Since Y is a constant and Y1 and Y2 are false, setting we will set both z1
		    //and z2 to true.  But we are unable to determine if the constraint is true or false 
		    //yet
		    z1.setFalse();
		    z2.setFalse();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
		    x3.setTrue();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			//This should be enough to determine it is true, since the other unset 
			//booleans wont matter in its truth
			z3.setTrue();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantAddSingleUniformIndexFalse2() {
		BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, ziexpr,false, yiconst);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
		    //Since Y is a constant, and Y3 is true, this implies that both x3 and z3 must be true
		    //thus setting either to false will cause the constraint to be false
		    z3.setFalse();
		    assertTrue("constraint is now false", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testConstantAddSingleUniformIndexTrue2() {
	    BooleanAndConstraint constraint = new BooleanAndConstraint(xiexpr, ziexpr,false, yiconst);
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
		    //Since Y is a constant and Y1 and Y2 are false, setting we will set both z1
		    //and z2 to true.  But we are unable to determine if the constraint is true or false 
		    //yet
		    z1.setFalse();
		    z2.setFalse();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
		    x3.setTrue();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z3.setTrue();
		    assertFalse("constraint is not false yet", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
}
