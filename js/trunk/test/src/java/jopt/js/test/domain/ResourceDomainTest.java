package jopt.js.test.domain;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.JsSolver;
import jopt.js.api.util.Timeline;
import jopt.js.api.variable.JsVariableFactory;
import jopt.js.api.variable.Resource;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import junit.framework.TestCase;


/**
 * @author James Boerkoel
 */
public class ResourceDomainTest extends TestCase {
	DiscreteResourceDomain dom;
	
	public ResourceDomainTest(String testName) {
		super(testName);
	}
	
	public void setUp() {
		dom = new DiscreteResourceDomain(20,80,10);
	}
	
	/*****************
	 * BASIC TESTING *
	 *****************/
	public void testSetUp() {
		IntIntervalSet iis = dom.findAvailIntervals(0,70,6);
		assertEquals(20,iis.getMin());
		assertEquals(70,iis.getMax());
		
		iis = dom.findAvailIntervals(0,90,6);
		assertEquals(20,iis.getMin());
		assertEquals(80,iis.getMax());
	}
	
	public void testRequireAndDislcaim() {
		try {
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			
			IntValIntervalSet ivis = new IntValIntervalSet();
			ivis.add(30,40,4);
			dom.setActualOperationTimeline(14,ivis);
			
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			ivis.add(35,45,2);
			dom.setActualOperationTimeline(14,ivis);
			assertEquals(6,dom.maxAvailableResource(30,34));
			assertEquals(4,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(8,dom.maxAvailableResource(41,80));
			assertEquals(10,dom.maxAvailableResource(46,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			ivis.remove(30,45,2);
			dom.setActualOperationTimeline(14,ivis);
			
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			assertEquals(8,dom.maxAvailableResource(30,34));
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			ivis = new IntValIntervalSet();
			ivis.add(30,45,2);
			dom.setActualOperationTimeline(15,ivis);
			
			assertEquals(2,dom.getNumberOfOperationsAssigned());
			
			assertEquals(6,dom.maxAvailableResource(30,34));
			assertEquals(4,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(8,dom.maxAvailableResource(41,80));
			assertEquals(10,dom.maxAvailableResource(46,80));
			assertEquals(2,dom.getNumberOfOperationsAssigned());
			
			ivis.remove(30,45,2);
			dom.setActualOperationTimeline(15,ivis);
			
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			assertEquals(8,dom.maxAvailableResource(30,34));
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
	
	public void testRequireAndDislcaimWithPushingAndPopping() {
		try {
			ChoicePointStack cps = new ChoicePointStack();
			dom.setChoicePointStack(cps);
			
			cps.push();
			
			assertEquals(10,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			
			IntValIntervalSet ivis = new IntValIntervalSet();
			ivis.add(30,40,4);
			
			dom.setActualOperationTimeline(14,ivis);
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			cps.push();
			ivis.add(35,45,2);
			dom.setActualOperationTimeline(14,ivis);
			assertEquals(6,dom.maxAvailableResource(30,34));
			assertEquals(4,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(8,dom.maxAvailableResource(41,80));
			assertEquals(10,dom.maxAvailableResource(46,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			Object delta1 = cps.popDelta();
			
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			Object delta2 = cps.popDelta();
			
			assertEquals(10,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			assertEquals(0,dom.getNumberOfOperationsAssigned());
			
			cps.pushDelta(delta2);
			
			assertEquals(6,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(10,dom.maxAvailableResource(41,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			cps.pushDelta(delta1);
			
			assertEquals(6,dom.maxAvailableResource(30,34));
			assertEquals(4,dom.maxAvailableResource(30,40));
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(8,dom.maxAvailableResource(41,80));
			assertEquals(10,dom.maxAvailableResource(46,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
		
	}
	
	public void testReplacementRequiring() {
		try{
			IntValIntervalSet ivis = new IntValIntervalSet();
			ivis.add(40,70,2);
			dom.setActualOperationTimeline(83,ivis);
			
			assertEquals(10,dom.maxAvailableResource(20,39));
			assertEquals(8,dom.maxAvailableResource(40,70));
			assertEquals(10,dom.maxAvailableResource(71,80));

			ivis.add(30,50,5);
			dom.setActualOperationTimeline(83,ivis);
			
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			assertEquals(10,dom.maxAvailableResource(20,29));
			assertEquals(5,dom.maxAvailableResource(30,39));
			assertEquals(3,dom.maxAvailableResource(30,50));
			assertEquals(8,dom.maxAvailableResource(51,80));
			assertEquals(10,dom.maxAvailableResource(71,80));
			
			dom.setActualOperationTimeline(23,new Timeline(20,80));
			
			assertEquals(2,dom.getNumberOfOperationsAssigned());
			
			assertEquals(9,dom.maxAvailableResource(20,29));
			assertEquals(4,dom.maxAvailableResource(30,39));
			assertEquals(2,dom.maxAvailableResource(30,50));
			assertEquals(7,dom.maxAvailableResource(51,80));
			assertEquals(9,dom.maxAvailableResource(71,80));
			
			assertEquals(2,dom.getNumberOfOperationsAssigned());
			ivis = new IntValIntervalSet();
			dom.setActualOperationTimeline(83,ivis);
			
			assertEquals(9,dom.maxAvailableResource(20,80));
			assertEquals(1,dom.getNumberOfOperationsAssigned());
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
	
	public void testRequiring() {
		try {
			dom = new DiscreteResourceDomain(0,100,1);
			dom.setActualOperationTimeline(15,new Timeline(5,5));
			
			assertEquals(1,dom.maxAvailableResource(0,4));
			assertEquals(0,dom.maxAvailableResource(5,5));
			assertEquals(1,dom.maxAvailableResource(6,100));
			
			dom.setActualOperationTimeline(15,new Timeline(4,9));
			
			assertEquals(1,dom.getNumberOfOperationsAssigned());
			
			assertEquals(1,dom.maxAvailableResource(0,3));
			assertEquals(0,dom.maxAvailableResource(0,4));
			assertEquals(0,dom.maxAvailableResource(5,5));
			assertEquals(0,dom.maxAvailableResource(6,100));
			assertEquals(1,dom.maxAvailableResource(10,100));
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
	
	public void testVarFactoryGranularResource() {
		JsSolver solver = JsSolver.createJsSolver();
		JsVariableFactory varFact = solver.getJsVarFactory();
		Resource res = varFact.createGranularDiscreteResource("resource1",-1,12,new int[]{5,6,7,8,9},4,1);
		
		IntIntervalSet iis = res.findAvailIntervals(-2,13,9);
		assertEquals(12,iis.getMin());
		assertEquals(12,iis.getMax());
		
		iis = res.findAvailIntervals(-2,13,8);
		assertEquals(8,iis.getMin());
		assertEquals(12,iis.getMax());
		
		iis = res.findAvailIntervals(-2,13,7);
		assertEquals(4,iis.getMin());
		assertEquals(12,iis.getMax());
		
		iis = res.findAvailIntervals(-2,13,6);
		assertEquals(0,iis.getMin());
		assertEquals(12,iis.getMax());
		
		iis = res.findAvailIntervals(-2,13,4);
		assertEquals(-1,iis.getMin());
		assertEquals(12,iis.getMax());
		
	}
}
