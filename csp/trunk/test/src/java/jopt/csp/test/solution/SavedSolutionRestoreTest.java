/*
 * Created on Mar 14, 2005
 */
package jopt.csp.test.solution;

import jopt.csp.spi.arcalgorithm.variable.DoubleVariable;
import jopt.csp.spi.arcalgorithm.variable.FloatVariable;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.StateStore;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntSetVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests the restoration (and "rerestoration") of stored states
 *
 * @author Chris Johnson
 */
public class SavedSolutionRestoreTest extends TestCase {
	
	CspDoubleVariable dv;
	CspIntVariable iv;
	CspFloatVariable fv;
	CspIntSetVariable isv;
	StateStore storedState;
		
    public SavedSolutionRestoreTest(String testName) {
        super(testName);
    }
    
    protected void setUp() {
    	//create a few variables
    	dv = new DoubleVariable("dv", (double)0, (double)10);
    	iv = new IntVariable("iv", 1, 5);
    	fv = new FloatVariable("fv", (float)2.0, (float)2.7);
    	isv = new IntSetVariable("isv", 6, 15);
    	CspVariable[] allVars = {dv, iv, fv, isv};
    	storedState = new StateStore(allVars);
    }
    
    protected void tearDown() {
    	dv = null;
    	iv = null;
    	fv = null;
    	isv = null;
    	storedState = null;
    }

    /**
     * Verifies that a valid StateStore is created and that
     * variables are properly initialized.
     */
    public void testStateStorage() {
    	verifyOriginalState();
    	assertNotNull("storedState", storedState);
    }
    
    /**
     * Makes alterations to every variable.  Verifies that the
     * alterations worked correctly.  Then, restores the problem 
     * to the stored (original) state.  Verifies that the restoration
     * worked on each involved variable.
     */
    public void testVariableRestoration() {
    	try {
    		//make changes to the variable domains
    		dv.setMax((double)5.5);
    		iv.removeValue(5);
    		fv.removeRange((float)2.3, (float)2.7);
    		isv.addRequired(8);
    		isv.removePossible(9);
    	}
		catch(PropagationFailureException propx) {
			//no propagation occurs and domains are known - should never happen
			fail();
		}
		//verify
		assertEquals("dv min", 0.0, dv.getMin(), 0.0001);
        assertEquals("dv max", 5.5, dv.getMax(), 0.0001);
        assertEquals("iv min", 1, iv.getMin());
        assertEquals("iv max", 4, iv.getMax());
        assertEquals("fv min", (float)2.0, fv.getMin(), (float)0.01);
        assertEquals("fv max", (float)2.3, fv.getMax(), (float)0.01);
        assertEquals("isv required", true, isv.isRequired(8));
        assertEquals("isv possible", 9, isv.getPossibleCardinality());
        
        //restore the original state
        storedState.restoreState();
		verifyOriginalState();
    }
    
    /**
     * Makes alterations to every variable.  Verifies that the
     * alterations worked correctly.  Then, restores the problem
     * to the stored (original) state (again).  Verifies that the
     * restoration worked on each involved variable.
     */
    public void testVariableReRestoration() {
    	try {
    		//make changes to the variable domains
    		dv.setMax((double)5.5);
    		iv.removeValue(5);
    		fv.removeRange((float)2.3, (float)2.7);
    		isv.addRequired(8);
    		isv.removePossible(9);
    	}
		catch(PropagationFailureException propx) {
			//no propagation occurs and domains are known - should never happen
			fail();
		}
		 //restore the original state
		storedState.restoreState();
		
		//make changes to the domains again
		try {
			dv.setMax((double)2.75);
			iv.removeRange(2, 4);
			fv.removeRange((float)2.0, (float)2.4);
			isv.addRequired(10);
			isv.removePossible(11);
		}
		catch(PropagationFailureException propx) {
			//no propagation occurs and domains are known - should never happen
			fail();
		}
		//verify
		assertEquals("dv min", 0.0, dv.getMin(), 0.0001);
        assertEquals("dv max", 2.75, dv.getMax(), 0.0001);
        assertEquals("iv min", 1, iv.getMin());
        assertEquals("iv max", 5, iv.getMax());
        assertEquals("fv min", (float)2.4, fv.getMin(), (float)0.01);
        assertEquals("fv max", (float)2.7, fv.getMax(), (float)0.01);
        assertEquals("isv required", true, isv.isRequired(10));
        assertEquals("isv possible", 9, isv.getPossibleCardinality());
        
        //restore the original state (again)
        storedState.restoreState();
		verifyOriginalState();
    }
    
    /**
     * Stores two partial states, makes changes to all the variables,
     * does a partial restoration, verifies, does the remaining restoration,
     * and verifies that the variables are back to their original state.
     */
    public void testPartialRestoration() {
    	//store solution states for some of the variables
        CspVariable[] threeVars = {dv, iv, fv};
        CspVariable[] singleVar = {isv};
		StateStore partialState = new StateStore(threeVars);
		StateStore remainingState = new StateStore(singleVar);
		
		try {
    		//make changes to the variable domains
    		dv.setMax((double)5.5);
    		iv.removeValue(5);
    		fv.removeRange((float)2.3, (float)2.7);
    		isv.addRequired(8);
    		isv.removePossible(9);
    	}
		catch(PropagationFailureException propx) {
			//no propagation occurs and domains are known - should never happen
			fail();
		}
		//restore partial state
		partialState.restoreState();
		//verify
		assertEquals("dv min", 0.0, dv.getMin(), 0);
    	assertEquals("dv max", 10.0, dv.getMax(), 0);
    	assertEquals("iv min", 1, iv.getMin());
    	assertEquals("iv max", 5, iv.getMax());
    	assertEquals("fv min", (float)2.0, fv.getMin(), (float)0.0);
    	assertEquals("fv max", (float)2.7, fv.getMax(), (float)0.0);
        assertEquals("isv required", true, isv.isRequired(8));
        assertEquals("isv possible", 9, isv.getPossibleCardinality());
        //restore the remaining variable
        remainingState.restoreState();
        verifyOriginalState();
    }
    
    /**
     * Helper method to verify the original state of the problem.
     */
    private void verifyOriginalState() {
    	//verify
    	assertEquals("dv min", 0.0, dv.getMin(), 0);
    	assertEquals("dv max", 10.0, dv.getMax(), 0);
    	assertEquals("iv min", 1, iv.getMin());
    	assertEquals("iv max", 5, iv.getMax());
    	assertEquals("fv min", (float)2.0, fv.getMin(), (float)0.0);
    	assertEquals("fv max", (float)2.7, fv.getMax(), (float)0.0);
    	assertEquals("isv required", 0, isv.getRequiredCardinality());
    	assertEquals("isv possible", 10, isv.getPossibleCardinality());
    }
}
