package jopt.js.test.solver;

import jopt.csp.CspSolver;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.spi.search.actions.GenerateActivityStartTimeThenResourceAction;
import jopt.js.api.variable.Activity;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.api.variable.Resource;
import jopt.js.spi.variable.ResourceExpr;
import junit.framework.TestCase;

/**
 * @author jboerkoel
 */
public class GenerateStartTimeThenResourceTest extends TestCase {

	CspSolver solver;
    Activity[] activities;
    Resource[] resources; 
	public GenerateStartTimeThenResourceTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	solver = CspSolver.createSolver();
    	activities = new Activity[2];
    	resources = new Resource[3];
    	
    	resources[0] = new ResourceExpr("res1",new UnaryResourceDomain(0,100));
    	resources[1] = new ResourceExpr("res2",new UnaryResourceDomain(0,100));
    	resources[2] = new ResourceExpr("res3",new UnaryResourceDomain(0,100));
    	
    	activities[0] = new ActivityExpr("a",1,0,100,30,60);
    	activities[1] = new ActivityExpr("b",2,0,100,30,60);
    	
    	try {
    		solver.addConstraint(((ActivityExpr)activities[0]).require(new ResourceExpr[]{(ResourceExpr)resources[0],(ResourceExpr)resources[1],(ResourceExpr)resources[2]},1));
    		solver.addConstraint(((ActivityExpr)activities[0]).require(new ResourceExpr[]{(ResourceExpr)resources[0],(ResourceExpr)resources[1],(ResourceExpr)resources[2]},1));
    		solver.addConstraint(((ActivityExpr)activities[1]).require(new ResourceExpr[]{(ResourceExpr)resources[0],(ResourceExpr)resources[1]},1));
    	}
    	catch (PropagationFailureException pfe) {
    		fail();
    	}
    }
    
	public void testGenerateStartTimeThenResource() {
		boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeThenResourceAction(activities),false);
		
		assertTrue(generatedStartTimes);
		assertTrue(activities[0].isBound());
		
		for (int i=0; i<activities.length; i++) {
			assertTrue(activities[i].isBound());
		}
	}
	
//	public void testGeneratSTParts() {
//		try {
//			activities[0].setStartTime(0);
//			((ActivityExpr)activities[0]).assignFirstResourceToNextOperation();
////			solver.propagate();
//			for (int i=0; i< activities.length; i++) {
//				System.out.println("act "+i+" : "+activities[i].toString());
//			}
//			
//			for (int i=0; i< resources.length; i++) {
//				System.out.println("res "+i+" : "+resources[i].toString());
//			}
//			
//		}
//		
//	catch(PropagationFailureException pfe) {
//		fail();
//	}
//	}

//  public void testNormalForwardConstraint() {
//	CspSolver solver = CspSolver.createSolver();
//
//Resource resource0 = new ResourceExpr("res1",new UnaryResourceDomain(0,100));
//Resource resource1 = new ResourceExpr("res2",new UnaryResourceDomain(0,100));
//
//Activity activity1 = new ActivityExpr("a",1,0,100,30,60);
//AssignActivityResourceAction aara = new AssignActivityResourceAction(activity1);
//
//try {
//	solver.addConstraint(((ActivityExpr)activity1).require(new ResourceExpr[]{(ResourceExpr)resource0,(ResourceExpr)resource1},1));
//	solver.addConstraint(((ActivityExpr)activity1).require(new ResourceExpr[]{(ResourceExpr)resource0,(ResourceExpr)resource1},1));
//	boolean solved = solver.solve(aara);
//	System.out.println("here is prob 4");
////	activity1.setStartTime(0);
////	solver.setAutoPropagate(false);
////	((ActivityExpr)activity1).assignFirstResourceToNextOperation();
////	solver.propagate();
////	assertTrue((resource0.maxAvailableResource(0,25)+resource1.maxAvailableResource(0,25))==1);
//	assertTrue(solved);
//	assertTrue(activity1.isBound());
//	System.out.println("ACTIVITY: "+activity1.toString());
//	
//}
//catch (PropagationFailureException pfe) {
//	fail();
//}
//}
//

}
