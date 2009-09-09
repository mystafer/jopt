package jopt.js.test.arc;

import jopt.csp.variable.PropagationFailureException;
import jopt.js.api.util.Timeline;
import jopt.js.spi.domain.activity.ActivityDomain;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.graph.arc.ForwardCheckReflexArc;
import jopt.js.spi.graph.node.ActivityNode;
import jopt.js.spi.graph.node.ResourceNode;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ForwardCheckReflexArcTest extends TestCase {
    ActivityNode act1;
    ActivityNode act2;
    ResourceNode res1;
    ResourceNode res2;
    ResourceNode res3;
    ForwardCheckReflexArc arc1;
    ForwardCheckReflexArc arc2;
    ForwardCheckReflexArc arc3;
    
	public ForwardCheckReflexArcTest(String testName) {
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
	    	
	    	arc1 = new ForwardCheckReflexArc(new ResourceNode[]{res1,res2},act1, 1);
	    	arc2 = new ForwardCheckReflexArc(new ResourceNode[]{res2,res3},act1, 2);
	    	arc3 = new ForwardCheckReflexArc(new ResourceNode[]{res1,res2,res3},act2,  3);
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
	    	
	    	propagate();
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(1,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));	    	
	    	
	    	
	    	res1.setActualOperationTimeline(34,new Timeline(0,50));
	    	propagate();
	    	//This should not cause the activities to change by itself
	    	
	    	assertEquals(0,act1.getEarliestStartTime());
	    	assertEquals(5,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(51,100));
	    	assertEquals(1,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
	    	res2.setActualOperationTimeline(32,new Timeline(0,20));
	    	propagate();
	    	//This should not cause the activities to change by itself
	    	
	    	assertEquals(21,act1.getEarliestStartTime());
	    	assertEquals(26,act1.getEarliestEndTime());
	    	assertEquals(0,act2.getEarliestStartTime());
	    	assertEquals(5,act2.getEarliestEndTime());
	    	
	    	assertEquals(95,act1.getLatestStartTime());
	    	assertEquals(100,act1.getLatestEndTime());
	    	assertEquals(95,act2.getLatestStartTime());
	    	assertEquals(100,act2.getLatestEndTime());
	    	
	    	assertEquals(2,act1.getAvailResourceCount(1));
	    	assertEquals(2,act1.getAvailResourceCount(2));
	    	assertEquals(0,act1.getAvailResourceCount(3));
	    	
	    	assertEquals(0,act2.getAvailResourceCount(1));
	    	assertEquals(0,act2.getAvailResourceCount(2));
	    	assertEquals(3,act2.getAvailResourceCount(3));
	    	
	    	assertEquals(0,res1.maxAvailableResource(0,100));
	    	assertEquals(1,res1.maxAvailableResource(51,100));
	    	assertEquals(0,res2.maxAvailableResource(0,100));
	    	assertEquals(1,res2.maxAvailableResource(21,100));
	    	assertEquals(1,res3.maxAvailableResource(0,100));
	    	
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
}
