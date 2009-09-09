package jopt.js.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.constraint.ForwardCheckConstraint;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.api.variable.Activity;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.api.variable.Resource;
import jopt.js.spi.variable.ResourceExpr;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ForwardCheckConstraintTest extends TestCase {
    ActivityExpr act1;
    ActivityExpr act2;
    ResourceExpr res1;
    ResourceExpr res2;
    ResourceExpr res3;
    ForwardCheckConstraint con1;
    ForwardCheckConstraint con2;
    ForwardCheckConstraint con3;
    
	ConstraintStore store;
    
	public ForwardCheckConstraintTest(String testName) {
        super(testName);
    }
    
	
    public void setUp() {
    	store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		
    	/*
    	 * **ALL durations will be set at a constant 6**
    	 * 
    	 * Activity 1
    	 * 		OP1			0						 100
    	 * 			RS1		|-------------------------|
    	 * 					0						 100
    	 * 			RS2		|-------------------------|
    	 * 		OP2			0						 100
    	 * 			RS2		|-------------------------|
    	 * 					0						 100
    	 * 			RS3		|-------------------------|
    	 * 
    	 * Activity 2
    	 * 		OP3			0		 				 100
    	 * 			RS1		|-------------------------|
    	 * 					0		 				 100
    	 * 			RS2		|-------------------------|
    	 *					0		 				 100
    	 * 			RS3		|-------------------------|
    	 */
    	res1 = new ResourceExpr("res1",new DiscreteResourceDomain(0,100,1));
    	res2 = new ResourceExpr("res2",new DiscreteResourceDomain(0,100,1));
    	res3 = new ResourceExpr("res3",new DiscreteResourceDomain(0,100,1));
    	
    	try {
    		act1 = new ActivityExpr("act1",1,0,110,6,6);
        	act2 = new ActivityExpr("act2",2,0,110,6,6);
	    	
    		con1 = (ForwardCheckConstraint) act1.require(new ResourceExpr[]{res1,res2},1);
    		con2 = (ForwardCheckConstraint) act1.require(new ResourceExpr[]{res2,res3},1);
    		con3 = (ForwardCheckConstraint) act2.require(new ResourceExpr[]{res1,res2,res3},1);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    	
    }
    
    
    public void testSetup() {
    	assertEquals(0,act1.getEarliestStartTime());
    	assertEquals(5,act1.getEarliestEndTime());
    	assertEquals(0,act2.getEarliestStartTime());
    	assertEquals(5,act2.getEarliestEndTime());
    	
    	assertEquals(95,act1.getLatestStartTime());
    	assertEquals(100,act1.getLatestEndTime());
    	assertEquals(95,act2.getLatestStartTime());
    	assertEquals(100,act2.getLatestEndTime());
    	
    	assertEquals(2,act1.getAvailResourceCount(con1.getOperationID()));
    	assertEquals(2,act1.getAvailResourceCount(con2.getOperationID()));
    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
    	
    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
    	
    	assertEquals(1,res1.maxAvailableResource(0,100));
    	assertEquals(1,res2.maxAvailableResource(0,100));
    	assertEquals(1,res3.maxAvailableResource(0,100));
    }
    
    public void testUsingResource() {
    	try {
    		
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(2,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	store.addConstraint(con1);
	    	store.addConstraint(con2);
	    	store.addConstraint(con3);
	    	store.propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(2,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	act1.setRequiredResource(con1.getOperationID(),res1.getID());
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(2,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	act1.setStartTime(res1.getID(),0);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(0,act1.getLatestStartTime());
	    	assertEquals(5,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(2,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	//This should let act know that it uses res 1 for op1 at 0 and res 2 for op 2 at time 0
	    	act1.setEarliestStartTime(res3.getID(),10);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(0,act1.getLatestStartTime());
	    	assertEquals(5,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(1,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(3,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(0,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	//Since resource 1 and 2 are used at time 0 - 5, the earliest start time for activity 2 must now be 6
	    	act2.removePossibleResource(con3.getOperationID(),res3.getID());
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(6,act2.getEarliestStartTime());
	    	assertEquals(11,act2.getEarliestEndTime());
	    	
	    	assertEquals(0,act1.getLatestStartTime());
	    	assertEquals(5,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(1,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(2,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(0,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	act2.setEarliestStartTime(90);
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(90,act2.getEarliestStartTime());
	    	assertEquals(95,act2.getEarliestEndTime());
	    	
	    	assertEquals(0,act1.getLatestStartTime());
	    	assertEquals(5,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(1,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(2,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(0,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	//this will cause the time 95 to be unavailable on res1 
	    	act2.removePossibleResource(con3.getOperationID(),res2.getID());
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(90,act2.getEarliestStartTime());
	    	assertEquals(95,act2.getEarliestEndTime());
	    	
	    	assertEquals(0,act1.getLatestStartTime());
	    	assertEquals(5,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(1,act1.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(0,act1.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(con1.getOperationID()));
	    	assertEquals(0,act2.getAvailResourceCount(con2.getOperationID()));
	    	assertEquals(1,act2.getAvailResourceCount(con3.getOperationID()));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(6,94));
	    	assertEquals(0,res1.maxAvailableResource(95,95));
	    	assertEquals(1,res1.maxAvailableResource(96,100));
	    	assertEquals(0,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
    	}
    	catch (PropagationFailureException pfe) {
    		pfe.printStackTrace();
    		fail(pfe.getLocalizedMessage());
    	}
    }
    
    public void testNormalForwardConstraint() {
      	CspSolver solver = CspSolver.createSolver();
    	
    	Resource resource0 = new ResourceExpr("res1",new UnaryResourceDomain(0,100));
    	Resource resource1 = new ResourceExpr("res2",new UnaryResourceDomain(0,100));
    	
    	Activity activity1 = new ActivityExpr("a",1,0,100,30,60);
    	
    	try {
    		solver.addConstraint(((ActivityExpr)activity1).require(new ResourceExpr[]{(ResourceExpr)resource0,(ResourceExpr)resource1},1));
    		activity1.setStartTime(0);
    		solver.setAutoPropagate(false);
    		
    		((ActivityExpr)activity1).getAllAlternativeResourceSets()[0].assignTo(0);
    		solver.propagate();
    		assertTrue((resource0.maxAvailableResource(0,25)+resource1.maxAvailableResource(0,25))==1);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }

}
