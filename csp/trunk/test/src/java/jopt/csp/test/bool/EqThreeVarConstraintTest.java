package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests the BooleanImpliesConstraint
 * 
 * @author Chris Johnson
 */
public class EqThreeVarConstraintTest extends TestCase {
	private BooleanVariable aVar;
    private BooleanVariable bVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private CspConstraint constraint;
	
    public EqThreeVarConstraintTest(java.lang.String testName) {
        super(testName);
    }
    
    public void setUp() {
    	store= new ConstraintStore(SolverImpl.createDefaultAlgorithm());
    	store.setAutoPropagate(false);
    	aVar = new BooleanVariable("a");
        bVar = new BooleanVariable("b");
        zVar = new BooleanVariable("z");
        constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, zVar);
    }
    
    public void tearDown() {
    	store= null;
    	aVar = null;
        bVar = null;
        zVar = null;
        constraint = null;
    }
    
    public void testSetAFalse() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Determine a variable
            aVar.setFalse();

            //complete state of all variables is not yet known
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //The constraint is not determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should is should be equal to constraint
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());            
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetATrue() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Determine a variable
            aVar.setTrue();

            //complete state of all variables is not yet known
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //The constraint is not determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should is should be equal to constraint
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());            
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetAFalseBFalse() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Determine the variables
            aVar.setFalse();
            bVar.setFalse();

            //complete state of all variables is not yet known
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //The constraint is now determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should is should be equal to constraint
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetZTrue() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Determine a variable
            zVar.setTrue();

            //the constraint cannot determined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //The constraint is still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //other vars should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());            
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetZFalse() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           

            //Determine a variable
            zVar.setFalse();

            //Now the constraint is still not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
    
            // the constraint is still not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetZFalseATrue() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           

            //Determine the variables
            zVar.setFalse();
            aVar.setTrue();

            //Now the constraint is still not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
    
            // the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should be determined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetAFalseConstZFalse() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, false);
            store.addConstraint(constraint);
            
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Determine a variable
            aVar.setFalse();

            //Now the constraint is still not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Propagate
            store.propagate();
            
            //The constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should be determined
            assertTrue("bVar isTrue should be true", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSetAFalseConstZTrue() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, true);
            store.addConstraint(constraint);
            
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Determine a variable
            aVar.setFalse();

            //Now the constraint is still not yet true or false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());            
            
            //Propagate
            store.propagate();
            
            //The other variable should be determined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
            //The constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSetATrueConstZFalse() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, false);
            store.addConstraint(constraint);
            
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Determine a variable
            aVar.setTrue();

            //The constraint is undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Propagate
            store.propagate();
            
            //The constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variable is determined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstraintDissatisfaction() {
        try {
            store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Determine both variables
            aVar.setTrue();
            bVar.setFalse();
            zVar.setTrue();
            
            //Verify that the constraint is dissatisfied
            assertTrue("aVar isTrue should be true", aVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar's constraint should not be true", constraint.isTrue());
            assertTrue("zVar's constraint should not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testPropFail() {
        try {
        	store.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Determine both variables
            aVar.setTrue();
            bVar.setFalse();
            zVar.setTrue();
            
            // ensure constraint computes as failed
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //if this propagates successfully, we should fail
            fail();            
        }
        catch (PropagationFailureException pfe) {
        	
		}    
    }
}
