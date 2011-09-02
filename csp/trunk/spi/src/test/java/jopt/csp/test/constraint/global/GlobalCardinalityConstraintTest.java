package jopt.csp.test.constraint.global;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.global.GlobalCardinalityConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints
 * 
 * @author Chris Johnson
 */
public class GlobalCardinalityConstraintTest extends TestCase {

	CspSolver solver;
	CspVariableFactory varFactory;
	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable a4;
	IntVariable a5;
	IntVariable a6;
	IntVariable[] aArray;
	Number[] vals;
	int[] ub;
	int[] lb;
	
	public void setUp () {
	    solver = CspSolver.createSolver();
	    solver.setAutoPropagate(false);
        a1 = new IntVariable("a1", 0, 5);
        a2 = new IntVariable("a2", 0, 5);
        a3 = new IntVariable("a3", 0, 5);
        a4 = new IntVariable("a4", 0, 5);
        a5 = new IntVariable("a5", 0, 5);
        a6 = new IntVariable("a6", 0, 5);
        vals = new Number[]{new Integer(0),new Integer(1),new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
        ub = new int[]{0,0,0,0,0,0};
        lb = new int[]{1,2,2,1,2,2};
        aArray = new IntVariable[] {a1,a2,a3,a4,a5,a6};
	}
	
	public void tearDown() {
		solver = null;
		varFactory = null;
		a1 = null;
		a2 = null;
		a3 = null;
		a4 = null;
		a5 = null;
		a6 = null;
		aArray = null;
		vals = null;
		ub = null;
		lb = null;
	}
	
	public void testSetRemove() {
	    try {
	        CspConstraint constraint = new GlobalCardinalityConstraint(aArray,vals,lb,ub);
	        solver.addConstraint(constraint);
	        a1.setMax(1);
	        a2.setMax(1);
	        a3.setMax(1);
	        solver.propagate();
	        
	        //Should have removed the values 0 and 1 from subsequent variables
	        assertFalse(a4.isInDomain(0));
	        assertFalse(a4.isInDomain(1));
	        assertFalse(a5.isInDomain(0));
	        assertFalse(a5.isInDomain(1));
	        assertFalse(a6.isInDomain(0));
	        assertFalse(a6.isInDomain(1));
	        
	        a5.setValue(3);
	        solver.propagate();

	        //should now remove 3 as well
	        assertFalse(a4.isInDomain(3));
	        assertFalse(a6.isInDomain(3));
	        
	        a2.setValue(0);
	        solver.propagate();
	        
	        assertTrue(a1.isBound());
	        assertTrue(a2.isBound());
	        assertTrue(a3.isBound());
	        assertTrue(a5.isBound());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testUniqueScenario() {
	    try {
			solver = CspSolver.createSolver();
		    solver.setAutoPropagate(false);
		    a1 = new IntVariable("a1", 1, 4);
		    a1.removeRange(2,3);
		    a2 = new IntVariable("a2", 2, 5);
		    a2.removeRange(3,4);
		    a3 = new IntVariable("a3", 1, 4);
		    a3.removeRange(2,3);	
		    a4 = new IntVariable("a4", 1,2);
		    a5 = new IntVariable("a5", 0, 5);
		    a5.removeRange(2,4);
		    a6 = new IntVariable("a6", 4, 5);
		    vals = new Number[]{new Integer(0),new Integer(1),new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
		    ub = new int[]{0,0,0,0,0,0};
		    lb = new int[]{1,1,1,1,1,1};
		    aArray = new IntVariable[] {a1,a2,a3,a4,a5,a6};
		    
		    solver.addConstraint(new GlobalCardinalityConstraint(aArray,vals,ub,lb));
		    solver.propagate();
		    
		    
	    }
	    catch (PropagationFailureException pfe) {
	    	fail();
	    }
	}
	
	public void testZeroUB(){
	    try {
	        int[] newUB = new int[]{6,0,6,0,6,6};
	        CspConstraint constraint = new GlobalCardinalityConstraint(aArray,vals,newUB,lb);
	        solver.addConstraint(constraint);
	        solver.propagate();
	        
	        assertFalse(a1.isInDomain(3));
	        assertFalse(a1.isInDomain(1));
	        assertFalse(a2.isInDomain(3));
	        assertFalse(a2.isInDomain(1));
	        assertFalse(a3.isInDomain(3));
	        assertFalse(a3.isInDomain(1));
	        assertFalse(a4.isInDomain(3));
	        assertFalse(a4.isInDomain(1));
	        assertFalse(a5.isInDomain(3));
	        assertFalse(a5.isInDomain(1));
	        assertFalse(a6.isInDomain(3));
	        assertFalse(a6.isInDomain(1));
	    }
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testLB() {
	    try{
	        lb = new int[]{1,1,1,2};
	        ub = new int[]{3,3,3,3};
	        vals = new Number[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
	        CspConstraint constraint = new GlobalCardinalityConstraint(aArray,vals,ub,lb);
	        solver.setAutoPropagate(false);
	        solver.addConstraint(constraint);
	        a1.setRange(2,2);
	        a2.setRange(1,2);
	        a3.setRange(2,3);
	        a4.setRange(2,3);
	        a5.setRange(1,4);
	        a6.setRange(3,4);
	        solver.propagate();

	        
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	    
	}


}
