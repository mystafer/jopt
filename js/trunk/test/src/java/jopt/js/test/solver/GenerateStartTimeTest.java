package jopt.js.test.solver;

import jopt.csp.CspSolver;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.spi.search.actions.GenerateActivityStartTimeAction;
import jopt.js.spi.search.actions.GenerateActivityStartTimeThenResourceAction;
import jopt.js.api.variable.Activity;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.api.variable.Resource;
import jopt.js.spi.variable.ResourceExpr;
import junit.framework.TestCase;

/**
 * @author jboerkoel
 */
public class GenerateStartTimeTest extends TestCase {

	CspSolver solver;
    Activity[] activities;
    Resource resource; 
	public GenerateStartTimeTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	solver = CspSolver.createSolver();
    	activities = new Activity[5];
    	resource = new ResourceExpr("res",new UnaryResourceDomain(0,100));
    	
    	activities[0] = new ActivityExpr("a",1,0,100,5,5);
    	activities[1] = new ActivityExpr("b",2,0,100,5,5);
    	activities[2] = new ActivityExpr("c",3,0,100,5,5);
    	activities[3] = new ActivityExpr("d",4,0,100,5,5);
    	activities[4] = new ActivityExpr("e",5,0,100,5,5);
    	
    }
    
	public void testGenerateStartTime() {
		try {
			for (int i=0; i<activities.length-1; i++) {
				solver.addConstraint(activities[i].endsBeforeStartOf((ActivityExpr)activities[i+1]));
				solver.addConstraint(((ActivityExpr)activities[i]).require(new ResourceExpr[]{(ResourceExpr)resource},1));
			}
		solver.addConstraint(((ActivityExpr)activities[4]).require(new ResourceExpr[]{(ResourceExpr)resource},1));
			
		boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeAction(activities),false);
		
		assertTrue(generatedStartTimes);
		
		for (int i=0; i<activities.length; i++) {
			assertEquals(activities[i].getEarliestStartTime(),activities[i].getLatestStartTime());
		}
			
		
			
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
	
	public void testGenerateStartTimeThenResource() {
		try {
			for (int i=0; i<activities.length-1; i++) {
				solver.addConstraint(activities[i].endsBeforeStartOf((ActivityExpr)activities[i+1]));
				solver.addConstraint(((ActivityExpr)activities[i]).require(new ResourceExpr[]{(ResourceExpr)resource},1));
			}
		solver.addConstraint(((ActivityExpr)activities[4]).require(new ResourceExpr[]{(ResourceExpr)resource},1));
			
		boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeThenResourceAction(activities),false);
		
		assertTrue(generatedStartTimes);
		
		for (int i=0; i<activities.length; i++) {
			assertEquals(activities[i].getEarliestStartTime(),activities[i].getLatestStartTime());
			assertTrue(activities[i].isBound());
		}
			
		
			
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
	
	public void testGenerateStartTimeThenResourceWithAltResource() {
		Resource resource2 = new ResourceExpr("res2",new UnaryResourceDomain(0,100));
		try {
			for (int i=0; i<activities.length-1; i++) {
				solver.addConstraint(activities[i].endsBeforeStartOf((ActivityExpr)activities[i+1]));
				solver.addConstraint(((ActivityExpr)activities[i]).require(new ResourceExpr[]{(ResourceExpr)resource,(ResourceExpr)resource2},1));
			}
		solver.addConstraint(((ActivityExpr)activities[4]).require(new ResourceExpr[]{(ResourceExpr)resource},1));
			
		boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeThenResourceAction(activities),false);
		
		assertTrue(generatedStartTimes);
		
		for (int i=0; i<activities.length; i++) {
			assertEquals(activities[i].getEarliestStartTime(),activities[i].getLatestStartTime());
			assertTrue(activities[i].isBound());
		}
			
		
			
		}
		catch (PropagationFailureException pfe) {
			fail(pfe.getLocalizedMessage());
		}
	}
        

}
