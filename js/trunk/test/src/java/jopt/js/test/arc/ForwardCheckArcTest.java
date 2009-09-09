package jopt.js.test.arc;

import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.graph.arc.ForwardCheckArc;
import jopt.js.spi.graph.node.ActivityNode;
import jopt.js.spi.graph.node.ResourceNode;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ForwardCheckArcTest extends TestCase {
    ActivityNode act1;
    ActivityNode act2;
    ResourceNode res1;
    ResourceNode res2;
    ResourceNode res3;
    ForwardCheckArc arc1;
    ForwardCheckArc arc2;
    ForwardCheckArc arc3;
    
	public ForwardCheckArcTest(String testName) {
        super(testName);
    }
    
	
    public void setUp() {
    	/*
    	 * **ALL durations will be set at a constant 15**
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
    	res1 = new ResourceNode("res1", new DiscreteResourceDomain(0,100,1));
    	res2 = new ResourceNode("res2", new DiscreteResourceDomain(0,100,1));
    	res3 = new ResourceNode("res3", new DiscreteResourceDomain(0,100,1));
    	
    	
    	try {
    		act1 = new ActivityNode("act1",new ActivityDomain(0,100,6,6));
        	act2 = new ActivityNode("act2",new ActivityDomain(0,100,6,6));
	    	act1.addResource(1,res1.getID(),0,100,6,6);
	    	act1.addResource(1,res2.getID(),0,100,6,6);
	    	act1.addResource(2,res2.getID(),0,100,6,6);
	    	act1.addResource(2,res3.getID(),0,100,6,6);
	    	act2.addResource(3,res1.getID(),0,100,6,6);
	    	act2.addResource(3,res2.getID(),0,100,6,6);
	    	act2.addResource(3,res3.getID(),0,100,6,6);
	    	act1.setCapacity(1,1);
	    	act1.setCapacity(2,1);
	    	act2.setCapacity(3,1);
	    	
	    	arc1 = new ForwardCheckArc(act1, new ResourceNode[]{res1,res2}, 1);
	    	arc2 = new ForwardCheckArc(act1, new ResourceNode[]{res2,res3}, 2);
	    	arc3 = new ForwardCheckArc(act2, new ResourceNode[]{res1,res2,res3}, 3);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    	
    }
    
    private void propagate() throws PropagationFailureException {
    	arc1.propagate();
    	arc2.propagate();
    	arc3.propagate();
    }
    
    
    public void testSetup() {
    	assertEquals(0,act1.getEarliestStartTime());
    	assertEquals(5,act1.getEarliestEndTime());
    	assertEquals(0,act2.getEarliestStartTime());
    	assertEquals(5,act2.getEarliestEndTime());
    	
    	assertEquals(100,act1.getLatestStartTime());
    	assertEquals(105,act1.getLatestEndTime());
    	assertEquals(100,act2.getLatestStartTime());
    	assertEquals(105,act2.getLatestEndTime());
    	
    	assertEquals(2,act1.getAvailResourceCount(1));
    	assertEquals(2,act1.getAvailResourceCount(2));
    	assertEquals(0,act1.getAvailResourceCount(3));
    	
    	assertEquals(0,act2.getAvailResourceCount(1));
    	assertEquals(0,act2.getAvailResourceCount(2));
    	assertEquals(3,act2.getAvailResourceCount(3));
    	
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
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(105,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(105,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	act1.setRequiredResource(1,res1.getID());
	    	propagate();
	    	//This should not cause the resources to change by itself
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(100,act1.getLatestStartTime());
	    	assertEquals(105,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(105,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	
	    	act1.setLatestStartTime(4);
	    	propagate();
	    	//This should not cause the resources to change by itself
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(4,act1.getLatestStartTime());
	    	assertEquals(9,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(105,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,3));
	    	assertEquals(0,res1.maxAvailableResource(4,4));
	    	assertEquals(1,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	act1.setStartTime(3);
	    	
	    	propagate();
	    	
	    	assertEquals(3,act1.getEarliestStartTime());
	    	assertEquals(8,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(3,act1.getLatestStartTime());
	    	assertEquals(8,act1.getLatestEndTime());
	    	assertEquals(100,act2.getLatestStartTime());
	    	assertEquals(105,act2.getLatestEndTime());
	    	
	    	assertEquals(1,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,2));
	    	assertEquals(0,res1.maxAvailableResource(0,4));
	    	assertEquals(0,res1.maxAvailableResource(4,9));
	    	assertEquals(0,res1.maxAvailableResource(6,100));
	    	assertEquals(1,res1.maxAvailableResource(10,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
}
