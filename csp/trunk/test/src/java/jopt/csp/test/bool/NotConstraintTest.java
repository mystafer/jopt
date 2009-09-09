package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanNotConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests some different comparison techniques on the
 * TernarnyNumSumArc
 * 
 * @author Nick
 */
public class NotConstraintTest extends TestCase {
	private BooleanVariable aVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private BooleanNotConstraint constraint;
	
    public NotConstraintTest(java.lang.String testName) {
        super(testName);
    }

    
    public void setUp() {
    	store= new ConstraintStore(SolverImpl.createDefaultAlgorithm());
    	store.setAutoPropagate(false);
    	aVar = new BooleanVariable("a");
    	zVar = new BooleanVariable("z");
    	((VariableChangeSource) aVar).addVariableChangeListener(store);
    	((VariableChangeSource) zVar).addVariableChangeListener(store);
    	constraint = new BooleanNotConstraint(aVar,zVar);
    }
    
    public void tearDown() {
    	aVar = null;
    	zVar = null;
    	store = null;
    	constraint = null;
    }
    
    /**
     * 
     */
    public void testSetAFalse() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Now we determine one of the variables to be false
            aVar.setFalse();

            //Now the constraint is not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is now true
            assertTrue("the constraint is now true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse()); 
            
            //Z should now be false
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar should now be true", zVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    /**
     * 
     */
    public void testSetATrue() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Now we determine one of the variables to be false
            aVar.setTrue();

            //Now the constraint is not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is now true
            assertTrue("the constraint is now true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse()); 

            //Z should now be false
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar should now be true", zVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }

    /**
     * 
     */
    public void testSetZTrue() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Now we determine one of the variables to be false
            zVar.setTrue();

            //Now the constraint is not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //Now the constraint is now true
            assertTrue("the constraint is now true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse()); 

            //Z should now be false
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertTrue("aVar isFalse should now be true", aVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    /**
     * 
     */
    public void testSetZFalse() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Now we determine one of the variables to be false
            zVar.setFalse();

            //Now the constraint is not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is now true
            assertTrue("the constraint is now true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());            
            

            //A should now be True
            assertTrue("aVar isTrue should be true", aVar.isTrue());
            assertFalse("aVar isFalse should now be false", aVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    /**
     * 
     */
    public void testPropFail() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Now we determine one of the variables to be false
            aVar.setFalse();
            zVar.setFalse();
            
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertTrue("the unbound constraint is false", constraint.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            fail();            
        }
        catch (PropagationFailureException pfe) {
        	
		}    
    }
    
    
}
