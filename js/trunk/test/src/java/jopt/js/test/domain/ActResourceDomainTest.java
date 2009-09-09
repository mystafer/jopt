package jopt.js.test.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.activity.ActResourceDomain;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ActResourceDomainTest extends TestCase {
	ActResourceDomain dom;
    
	public ActResourceDomainTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	dom = new ActResourceDomain(10,80,11,21);
    }
    
    /*****************
     * BASIC TESTING *
     *****************/
    public void testInitialProblemSetup() {
    	assertEquals(dom.getEarliestEndTime(),20);
    	assertEquals(dom.getLatestEndTime(),100);
    	assertEquals(dom.getEarliestStartTime(),10);
    	assertEquals(dom.getLatestStartTime(),80);
    	assertEquals(dom.getDurationMin(),11);
    	assertEquals(dom.getDurationMax(),21);
    }
    
    public void testSetLatestEndTime() {
    	try {
	    	assertEquals(dom.getEarliestEndTime(),20);
	    	assertEquals(dom.getLatestEndTime(),100);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),80);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	dom.setLatestEndTime(85,false);
	    	assertEquals(dom.getEarliestEndTime(),20);
	    	assertEquals(dom.getLatestEndTime(),85);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),75);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	dom.setDuration(19,false);
	    	assertEquals(dom.getEarliestEndTime(),28);
	    	assertEquals(dom.getLatestEndTime(),85);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),67);
	    	assertEquals(dom.getDurationMin(),19);
	    	assertEquals(dom.getDurationMax(),19);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    public void testGapRemoval() {
    	try{
	    	assertEquals(dom.getEarliestEndTime(),20);
	    	assertEquals(dom.getLatestEndTime(),100);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),80);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	dom.setLatestEndTime(85,false);
//	    	IntIntervalSet startTimes = dom.getStartTimes();
//	    	IntIntervalSet endTimes = dom.getEndTimes();
//	    	IntIntervalSet duration = dom.getDuration();
	    	
	    	dom.removeStartTimes(30,40,false);
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
    
    public void testSetCPSbySettingEST() {
    	try{
	    	ChoicePointStack cps = new ChoicePointStack();
	        dom.setChoicePointStack(cps);
	    	assertEquals(dom.getEarliestEndTime(),20);
	    	assertEquals(dom.getLatestEndTime(),100);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),80);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	cps.push();
	    	dom.setEarliestStartTime(32,false);
	    	assertEquals(dom.getEarliestEndTime(),42);
	    	assertEquals(dom.getLatestEndTime(),100);
	    	assertEquals(dom.getEarliestStartTime(),32);
	    	assertEquals(dom.getLatestStartTime(),80);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	
	    	Object delta = cps.popDelta();
	    	assertEquals(dom.getEarliestEndTime(),20);
	    	assertEquals(dom.getLatestEndTime(),100);
	    	assertEquals(dom.getEarliestStartTime(),10);
	    	assertEquals(dom.getLatestStartTime(),80);
	    	assertEquals(dom.getDurationMin(),11);
	    	assertEquals(dom.getDurationMax(),21);
	    	
	    	cps.pushDelta(delta);
	    	
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
}
