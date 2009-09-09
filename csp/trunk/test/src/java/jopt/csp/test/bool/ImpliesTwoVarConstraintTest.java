package jopt.csp.test.bool;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanImpliesTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests the BooleanImpliesConstraint
 * 
 * @author Chris Johnson
 */
public class ImpliesTwoVarConstraintTest extends TestCase {
    private BooleanVariable aVar;
    private BooleanVariable bVar;
	private CspSolver solver;
	private CspConstraint constraint;
	
    public ImpliesTwoVarConstraintTest(java.lang.String testName) {
        super(testName);
    }
    
    public void setUp() {
    	solver = CspSolver.createSolver();
    	solver.setAutoPropagate(false);
    	aVar = new BooleanVariable("a");
        bVar = new BooleanVariable("b");
    }
    
    public void tearDown() {
    	aVar = null;
    	bVar = null;
    	solver = null;
    	constraint = null;
    }
    
    public void testSetAFalse() {
        try {
            constraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
        	solver.addConstraint(constraint);
        	
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Determine a variable
            aVar.setFalse();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            solver.propagate();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetATrue() {
        try {
            constraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
        	solver.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Determine a variable
            aVar.setTrue();

            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            
            //Now we will propagate
            solver.propagate();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertTrue("bVar isTrue should be true", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetBFalse() {
        try {
            constraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
        	solver.addConstraint(constraint);
        	
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Determine a variable
            bVar.setFalse();

            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            
            //Now we will propagate
            solver.propagate();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertTrue("aVar isFalse should be true", aVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testSetBTrue() {
        try {
            constraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
        	solver.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Determine a variable
            bVar.setTrue();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variable should still be undetermined
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            
            //Now we will propagate
            solver.propagate();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testConstraintDissatisfaction() {
        try {
            constraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
            solver.addConstraint(constraint);
        	//Cannot be determined yet
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
           
            //Determine both variables
            aVar.setTrue();
            bVar.setFalse();
            
            //Verify that the constraint is dissatisfied
            assertTrue("aVar isTrue should be true", aVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
            assertTrue("constraint is violated", constraint.isFalse());
            
            //Propagation should fail
            assertFalse("Propagation fails", solver.propagate());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
