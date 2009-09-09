package jopt.js.test.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActOperationDomain;
import jopt.js.spi.domain.activity.ActResourceDomain;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class OperationAssociationTest extends TestCase {
	ActOperationDomain oa;
	ActResourceDomain ra1;
	ActResourceDomain ra2;
	ActResourceDomain ra3;
    
	public OperationAssociationTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	oa = new ActOperationDomain(1,1,1);
    	ra1 = new ActResourceDomain(0,100,5,10);
    	ra2 = new ActResourceDomain(0,100,5,10);
    	ra3 = new ActResourceDomain(0,100,5,10);
    	try {
	    	oa.addResource(ra1);
	    	oa.addResource(ra2);
	    	oa.addResource(ra3);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    /*****************
     * BASIC TESTING *
     *****************/
    public void testSetup() {
    	assertEquals(oa.getEarliestStartTime(),ra1.getEarliestStartTime());
    	assertEquals(oa.getEarliestEndTime(),ra1.getEarliestEndTime());
    	assertEquals(oa.getLatestStartTime(),ra1.getLatestStartTime());
    	assertEquals(oa.getLatestEndTime(),ra1.getLatestEndTime());
    	assertEquals(oa.getDurationMax(),ra1.getDurationMax());
    	assertEquals(oa.getDurationMin(),ra1.getDurationMin());
    	
    	assertEquals(oa.getEarliestStartTime(),ra2.getEarliestStartTime());
    	assertEquals(oa.getEarliestEndTime(),ra2.getEarliestEndTime());
    	assertEquals(oa.getLatestStartTime(),ra2.getLatestStartTime());
    	assertEquals(oa.getLatestEndTime(),ra2.getLatestEndTime());
    	assertEquals(oa.getDurationMax(),ra2.getDurationMax());
    	assertEquals(oa.getDurationMin(),ra2.getDurationMin());
    	
    	assertEquals(oa.getEarliestStartTime(),ra3.getEarliestStartTime());
    	assertEquals(oa.getEarliestEndTime(),ra3.getEarliestEndTime());
    	assertEquals(oa.getLatestStartTime(),ra3.getLatestStartTime());
    	assertEquals(oa.getLatestEndTime(),ra3.getLatestEndTime());
    	assertEquals(oa.getDurationMax(),ra3.getDurationMax());
    	assertEquals(oa.getDurationMin(),ra3.getDurationMin());    	
    }
    
    public void testAlteringOperation() {
	    try {
    	//removing values from an operation should remove them from all resource associations
	    	oa.setDuration(7);
	    	oa.setEarliestStartTime(13);
	    	oa.removeEndTimes(97,130);
	    	
	    	assertEquals(oa.getEarliestStartTime(),ra1.getEarliestStartTime());
	    	assertEquals(oa.getEarliestEndTime(),ra1.getEarliestEndTime());
	    	assertEquals(oa.getLatestStartTime(),ra1.getLatestStartTime());
	    	assertEquals(oa.getLatestEndTime(),ra1.getLatestEndTime());
	    	assertEquals(oa.getDurationMax(),ra1.getDurationMax());
	    	assertEquals(oa.getDurationMin(),ra1.getDurationMin());
	    	
	    	assertEquals(oa.getEarliestStartTime(),ra2.getEarliestStartTime());
	    	assertEquals(oa.getEarliestEndTime(),ra2.getEarliestEndTime());
	    	assertEquals(oa.getLatestStartTime(),ra2.getLatestStartTime());
	    	assertEquals(oa.getLatestEndTime(),ra2.getLatestEndTime());
	    	assertEquals(oa.getDurationMax(),ra2.getDurationMax());
	    	assertEquals(oa.getDurationMin(),ra2.getDurationMin());
	    	
	    	assertEquals(oa.getEarliestStartTime(),ra3.getEarliestStartTime());
	    	assertEquals(oa.getEarliestEndTime(),ra3.getEarliestEndTime());
	    	assertEquals(oa.getLatestStartTime(),ra3.getLatestStartTime());
	    	assertEquals(oa.getLatestEndTime(),ra3.getLatestEndTime());
	    	assertEquals(oa.getDurationMax(),ra3.getDurationMax());
	    	assertEquals(oa.getDurationMin(),ra3.getDurationMin());
	    }
	    catch (PropagationFailureException pfe) {
	    	fail();
	    }
    }
    
    public void testOperationResponseToResourceChange() {
    	try{
	    	assertEquals(0, oa.getEarliestStartTime());
	    	ra1.setEarliestStartTime(17,true);
	    	assertEquals(0, oa.getEarliestStartTime());
	    	ra2.setEarliestStartTime(13,true);
	    	assertEquals(0, oa.getEarliestStartTime());
	    	ra3.setEarliestStartTime(15,true);
	    	assertEquals(13, oa.getEarliestStartTime());
	    	assertEquals(17, oa.getEarliestEndTime());
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    public void testCPS() {
    	try{
	    	ChoicePointStack cps = new ChoicePointStack();
	    	oa.setChoicePointStack(cps);
	    	cps.push();
	    	ra1.setEarliestStartTime(17,true);
	    	ra2.setEarliestStartTime(13,true);
	    	ra3.setEarliestStartTime(15,true);
	    	assertEquals(13, oa.getEarliestStartTime());
	    	assertEquals(17, oa.getEarliestEndTime());
	    	assertEquals(17, ra1.getEarliestStartTime());
	    	assertEquals(21, ra1.getEarliestEndTime());
	    	assertEquals(13, ra2.getEarliestStartTime());
	    	assertEquals(17, ra2.getEarliestEndTime());
	    	assertEquals(15, ra3.getEarliestStartTime());
	    	assertEquals(19, ra3.getEarliestEndTime());
	    	Object delta = cps.popDelta();
	    	assertEquals(0, oa.getEarliestStartTime());
	    	assertEquals(4, oa.getEarliestEndTime());
	    	assertEquals(0, ra1.getEarliestStartTime());
	    	assertEquals(4, ra1.getEarliestEndTime());
	    	assertEquals(0, ra2.getEarliestStartTime());
	    	assertEquals(4, ra2.getEarliestEndTime());
	    	assertEquals(0, ra3.getEarliestStartTime());
	    	assertEquals(4, ra3.getEarliestEndTime());
	    	cps.pushDelta(delta);
	    	assertEquals(17, ra1.getEarliestStartTime());
	    	assertEquals(21, ra1.getEarliestEndTime());
	    	assertEquals(13, ra2.getEarliestStartTime());
	    	assertEquals(17, ra2.getEarliestEndTime());
	    	assertEquals(15, ra3.getEarliestStartTime());
	    	assertEquals(19, ra3.getEarliestEndTime());
	    	assertEquals(13, oa.getEarliestStartTime());
	    	assertEquals(17, oa.getEarliestEndTime());
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    public void testConsistentDomains() {
    	//set lst to 9, and make sure domain is consistent
    	try{
			ActResourceDomain ra1 = new ActResourceDomain(10,31,3,3);
			ra1.removeStartTimes(13,17,true);
			ra1.removeStartTimes(21,25,true);
			ActResourceDomain ra2 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra3 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra4 = new ActResourceDomain(9,31,3,3);
			ActResourceDomain ra5 = new ActResourceDomain(9,31,3,3);
			ActOperationDomain oa = new ActOperationDomain(ActOperationDomain.REQUIRES,1,1);
			oa.addResource(ra1);
			oa.addResource(ra2);
			oa.addResource(ra3);
			oa.addResource(ra4);
			oa.addResource(ra5);
			
			oa.setLatestStartTime(9);
			
    	}
    	catch (PropagationFailureException pfe) {
    		fail(pfe.getLocalizedMessage());
    	}
    	
    }
    
    public void testCPSPop() {
    	ChoicePointStack cps = new ChoicePointStack();
    	
    	ActOperationDomain opA = new ActOperationDomain(ActOperationDomain.REQUIRES,1,1);
    	ActResourceDomain resA = new ActResourceDomain(10,50,5,5);
    	
    	try {
	    	opA.addResource(resA);
	    	opA.setChoicePointStack(cps);
    		opA.setEarliestEndTime(55);
    		fail();
    	}
    	catch (PropagationFailureException pfe) {
    		assertEquals(0,opA.getPossibleResourceIndices().length);
    		cps.pop();
    		assertEquals(1,opA.getPossibleResourceIndices().length);
    	}
    	
    	
    }
    
    
  
}
