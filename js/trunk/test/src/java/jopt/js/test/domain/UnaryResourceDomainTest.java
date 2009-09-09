package jopt.js.test.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.api.util.Timeline;
import jopt.js.api.util.TransitionTimeTable;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class UnaryResourceDomainTest extends TestCase {
    UnaryResourceDomain dom;
    
	public UnaryResourceDomainTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	dom = new UnaryResourceDomain(20,80);
    	dom.setBuilt(true);
    }
    
    
    public void testPushinAndPoppin() {
    	try {
    		
    		ChoicePointStack cps = new ChoicePointStack();
    		dom.setChoicePointStack(cps);
    		
    		cps.push();
    		
    		IntIntervalSet iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getFirstIntervalIndex(),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		
    		dom.setActualOperationTimeline(4,new Timeline(45,55));
    		
    		
    		assertEquals(1,dom.getNumberOfOperationsAssigned());
    		
    		iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getNextIntervalIndex(iis.getFirstIntervalIndex()),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(44,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		assertEquals(56,iis.getIntervalStart(iis.getLastIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getLastIntervalIndex()));
    		
    		Object delta = cps.popDelta();
    		
    		assertEquals(0,dom.getNumberOfOperationsAssigned());
    		
    		iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getFirstIntervalIndex(),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		
    		cps.pushDelta(delta);
    		
    		assertEquals(1,dom.getNumberOfOperationsAssigned());
    		
    		iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getNextIntervalIndex(iis.getFirstIntervalIndex()),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(44,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		assertEquals(56,iis.getIntervalStart(iis.getLastIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getLastIntervalIndex()));

    		
    	}
    	catch (PropagationFailureException pfe) {
    		fail(pfe.getLocalizedMessage());
    	}
    }
    
    /*****************
     * BASIC TESTING *
     *****************/
    public void testFindAllIntervals() {
    	try {
    		IntIntervalSet iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getFirstIntervalIndex(),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		
    		dom.setActualOperationTimeline(4,new Timeline(45,55));
    		
    		iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getNextIntervalIndex(iis.getFirstIntervalIndex()),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(44,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		assertEquals(56,iis.getIntervalStart(iis.getLastIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getLastIntervalIndex()));
    		
    		TransitionTimeTable ttt = TransitionTimeTable.getInstance();
    		
    		ttt.registerOperationID(2,2,2);
    		ttt.registerOperationID(4,4,4);
    		
    		ttt.addByOp(2,4,20);
    		ttt.addByOp(4,2,10);
    		ttt.addByOp(2,2,Integer.MAX_VALUE);
    		ttt.addByOp(4,4,Integer.MIN_VALUE);
    		
    		assertEquals(20,ttt.get(2,4));
    		assertEquals(10,ttt.get(4,2));

    		assertEquals(20,ttt.getByOp(2,4));
    		assertEquals(10,ttt.getByOp(4,2));
    		
    		iis = dom.findAvailIntervals(2,0,100,1);
    		
    		assertEquals(iis.getNextIntervalIndex(iis.getFirstIntervalIndex()),iis.getLastIntervalIndex());
    		assertEquals(20,iis.getIntervalStart(iis.getFirstIntervalIndex()));
    		assertEquals(24,iis.getIntervalEnd(iis.getFirstIntervalIndex()));
    		assertEquals(66,iis.getIntervalStart(iis.getLastIntervalIndex()));
    		assertEquals(80,iis.getIntervalEnd(iis.getLastIntervalIndex()));
    		
    	}
    	catch (PropagationFailureException pfe) {
    		fail(pfe.getLocalizedMessage());
    	}
    }
    
    public void testConsistencyChecking() {
    	dom = new UnaryResourceDomain(4,16);
    	IntIntervalSet iis = dom.findAvailIntervals(1,4,16,1);
    	
    	assertEquals(4,iis.getMin());
    	assertEquals(16,iis.getMax());
    	assertTrue(iis.isIntervalContained(4,16));
    	try {
	    	dom.registerAllocatedOperation(2,6,16,4);
	    	
	    	iis = dom.findAvailIntervals(1,4,16,1);
	    	
	    	assertEquals(4,iis.getMin());
	    	assertEquals(16,iis.getMax());
	    	assertTrue(iis.isIntervalContained(4,16));
	    	
	    	dom.registerAllocatedOperation(1,4,16,2);
	    	
	    	iis = dom.findAvailIntervals(1,4,16,1);
	    	
	    	assertEquals(4,iis.getMin());
	    	assertEquals(16,iis.getMax());
	    	assertTrue(iis.isIntervalContained(4,16));
	    	
	    	dom.registerAllocatedOperation(3,5,15,5);
	    	
	    	iis = dom.findAvailIntervals(1,4,16,1);
	    	
	    	assertEquals(4,iis.getMin());
	    	assertEquals(16,iis.getMax());
	    	assertTrue(iis.isIntervalEmpty(8,13));
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    	
    }
    
}
