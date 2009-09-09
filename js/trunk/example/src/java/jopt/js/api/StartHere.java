package jopt.js.api;


import java.util.Date;

import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.JsSolver;
import jopt.js.api.search.ActivitySelector;
import jopt.js.api.search.JsSearchActions;
import jopt.js.api.variable.Activity;
import jopt.js.api.variable.JsVariableFactory;
import jopt.js.api.variable.Resource;

/**
 * This is meant to be an easy to follow introduction to the job scheduler portion of the jOpt project.
 * 
 * This class will emulate building a house.  The process will be modeled by eleven activities.
 * Each activity will have a duration where the duration is simply the amount of time it takes to 
 * complete the given task.  In addition to having a duration, each activity will require a resource.
 * There are four resources available each of which can each accomplish a subset of the tasks.
 * 
 * The problem is laid out as follows:
 * 
 * Resources available:
 * Bob's plumbing and electric : plumbing, electric, and fixtures
 * Raul's Siding: framing, siding, roofing, ceilings/walls, painting
 * Steve's Foundation: foundation, framing, roofing
 * Latisha's Interior Design: painting, flooring, furniture, fixtures
 * 
 * Task	Description		Duration		Predecessor		Available Resources
 *  a	 foundation		 6					 -			 	 S
 *  b	 framing		 4					 a			 	 S,R
 *  c	 plumbing		 5					 b			 	 B
 *  d	 electricity	 4					 b			 	 B
 *  e	 roofing		 3					 b			 	 S, R
 *  f	 ceilings/walls	 3					 c,d			 R, L
 *  g	 fixtures		 1					 f,i			 B, L
 *  h	 siding			 2					 b			 	 R
 *  i	 painting		 2					 h, f			 L, R
 *  j 	 flooring		 4					 i				 L
 *  k	 furniture		 1					 j				 L
 *
 * Entire job of building a house will take between 0 and 35 units of time
 * One could decide to try to optimize many things
 * For example:
 *	- minimize the time it takes to build the house
 *	- minimize the cost of labor (the focus of this example)
 * 
 * @author jboerkoel
 */
public class StartHere {

	public void run() {
		
		//First we create an instance of the solver - used to construct and solve job scheduling problems
		JsSolver solver = JsSolver.createJsSolver();
		
		//Obtain a variable factory from the solver to create our variables
		JsVariableFactory varFactory = solver.getJsVarFactory();
		
		//Here we will set up variables for each task.
        //If the activities were all done 1 at a time, they would take no more than 35 time units to complete.
		//Since the activities will occur before 35, we will just use 0  35 as our 
		//earliest possible start time and latest possible start time, respectively.
        //The ids are simply chosen to be all unique, with a = 1000, b = 1001, etc.
		Activity actA = varFactory.createActivity("foundation",1000,0,35,6);
		Activity actB = varFactory.createActivity("framing",1001,0,35,4);
		Activity actC = varFactory.createActivity("plumbing",1002,0,35,5);
		Activity actD = varFactory.createActivity("electricity",1003,0,35,4);
		Activity actE = varFactory.createActivity("roofing",1004,0,35,3);
		Activity actF = varFactory.createActivity("ceilings/walls",1005,0,35,3);
		Activity actG = varFactory.createActivity("fixtures",1006,0,35,1);
		Activity actH = varFactory.createActivity("siding",1007,0,35,2);
		Activity actI = varFactory.createActivity("painting",1008,0,35,2);
		Activity actJ = varFactory.createActivity("flooring",1009,0,35,4);
		Activity actK = varFactory.createActivity("furniture",1010,0,35,1);
		
		//Now, we will set up the Resources; the resources will all be available the entire time
		Resource bob = varFactory.createUnaryResource("bob",0,35);
		Resource raul = varFactory.createUnaryResource("raul",0,35);
		Resource steve = varFactory.createUnaryResource("steve",0,35);
		Resource latisha = varFactory.createUnaryResource("latisha",0,35);
	
		System.out.println("Resources created...");
		
		try {
			//We now must let the solver know of all the constraints that this problem has.
			//First we constrain the activities to start before or after other activities.
			//As you will notice there are many ways to say the same thing,
            //ie 'A' ends before 'B' starts, or 'B' starts after 'A' ends
			solver.addConstraint(actB.startsAfterEndOf(actA));
			
			solver.addConstraint(actC.startsAfterEndOf(actB));
			
			solver.addConstraint(actD.startsAfterEndOf(actB));
			
			solver.addConstraint(actE.startsAfterEndOf(actB));
			
			solver.addConstraint(actF.startsAfterEndOf(actC));
			solver.addConstraint(actF.startsAfterEndOf(actD));
			
			solver.addConstraint(actG.startsAfterEndOf(actF));
			solver.addConstraint(actG.startsAfterEndOf(actI));
			
			solver.addConstraint(actH.startsAfterEndOf(actB));
			
			solver.addConstraint(actI.startsAfterEndOf(actH));
			solver.addConstraint(actI.startsAfterEndOf(actF));
			
			solver.addConstraint(actJ.startsAfterEndOf(actI));
			
			solver.addConstraint(actK.startsAfterEndOf(actJ));
			
			System.out.println("Temporal constraints created...");
			
			//Also we will let each activity know which resources it can use.
            //In each case, it will only need 1 unit of capacity
			solver.addConstraint(actA.require(new Resource[]{steve},1));
			solver.addConstraint(actB.require(new Resource[]{steve,raul},1));
			solver.addConstraint(actC.require(new Resource[]{bob},1));
			solver.addConstraint(actD.require(new Resource[]{bob},1));
			solver.addConstraint(actE.require(new Resource[]{steve,raul},1));
			solver.addConstraint(actF.require(new Resource[]{raul,latisha},1));
			solver.addConstraint(actG.require(new Resource[]{bob, latisha},1));
			solver.addConstraint(actH.require(new Resource[]{raul},1));
			solver.addConstraint(actI.require(new Resource[]{latisha, raul},1));
			solver.addConstraint(actJ.require(new Resource[]{latisha},1));
			solver.addConstraint(actK.require(new Resource[]{latisha},1));
			
			System.out.println("FC constraints created...");
			
			//Now to get the first solution
	        //Get the pre-defined search actions
	        JsSearchActions actions = solver.getJsSearchActions();
	        
	        //We'll attempt to locate our optimal solution by assigning a resource
            //and a start time to each of the 11 activities
            Activity[] activities = new Activity[]{actA, actB, actC, actD, actE, actF, actG, actH, actI, actJ, actK};
	        SearchAction action = actions.generateResourceAndStartTimes(activities);
			
	        System.out.println("actions created...");
	        
	        //We will want to quantify the cost of this job.  For creativity's sake, let's say that every resource
	        //but Bob charges per time unit of work whereas Bob charges based on the number of jobs that he works on.
	        //Let us set up the following rates for labor:
	        // Bob charges 120 / job
	        // Raul charges 80 / time unit
	        // Steve charges 30 / time unit
	        // Latisha charges 95 / time unit
	        
	        //We will now set up the cost for each person ...
	        //Bob is based of the number of operations that he accomplishes
	        CspIntExpr costOfBob = bob.getNumOperationsExpr().multiply(120);
	        //The makespan is the length of time a resource is used -
            //from the beginning of the first activity to the end of the last activity.
            //Thus, the cost of the remaining resources is the makespan * resource cost per unit of time.
	        CspIntExpr costOfRaul = raul.getMakeSpanExpr().multiply(80);
	        CspIntExpr costOfSteve= steve.getMakeSpanExpr().multiply(30);
	        CspIntExpr costOfLatisha = latisha.getMakeSpanExpr().multiply(95);
	        
	        //The total cost, then, of building the house, is the sum of the costs of all 4 resources
	        CspIntExpr totalCost = costOfBob.add(costOfRaul).add(costOfSteve).add(costOfLatisha);
	        
	        System.out.println("total cost created...");
	        
	        //Our intention, our goal, is to minimize the cost of building the house
	        //When we "strictly" minimize, it means we will only find solutions that are better than a previous solution
            //(rather than finding solutions that are either as good or better than previous solutions).
	        SearchGoal goal = solver.getJsSearchGoals().strictlyMinimize(totalCost, 1);
	        
	        System.out.println("this goal created...");
	        
	        Date then = new Date();
            //Indicate to the solver that we are done setting up the problem.
	        solver.problemBuilt();
	        System.out.println("About to propagate...");
	        solver.propagate();
	        System.out.println("About to solve...");

	        //This will get the first viable solution.
			solver.solve(action, goal, true);
			
            Resource[] resources = new Resource[]{steve, raul, bob, latisha};
			//Here we will print the first solution that we obtain
			//Print activities
			for (int i=0; i<activities.length; i++) {
				System.out.println(activities[i].toString());
			}
			//Print resources
			for (int i=0; i<resources.length; i++) {
				System.out.println(resources[i].toString());
			}
			System.out.println("Total cost: "+totalCost.getMin()+".."+totalCost.getMax());
			System.out.println("runTime: "+((new Date()).getTime()-then.getTime())+"\n");
			
			//Now we look for better solutions.
            //We print the costs as we go, to ensure that we are actually getting better
			while (solver.nextSolution()) {
			    //Print trucks
				for (int i=0; i<resources.length; i++) {
					System.out.println(resources[i].toString());
				}
				System.out.println("steve's cost: "+costOfSteve);
				System.out.println("raul's cost: "+costOfRaul);
				System.out.println("bob's cost: "+costOfBob);
				System.out.println("latisha's cost: "+costOfLatisha);
				System.out.println("Total cost: "+totalCost.getMin()+".."+totalCost.getMax());
				System.out.println("runTime: "+((new Date()).getTime()-then.getTime())+"\n");
			}
			
	    }
	    catch (PropagationFailureException pfe) {
	        // The solver attempts to 'propagate' the constraints immediately;
	        // that is, it attempts to make the variable values consistent with
	        // those allowed by the constraints.
	        // Propagation should not fail in this particular example.
	        System.out.println("Constraint was impossible to satisfy");
	    }
	}
	
    /**
     * Simple main method to get the problem runnin'
     * 
     * @param args
     */
    public static void main(String[] args) {
        new StartHere().run(); 
    }
    
    public class HouseActivitySelector implements ActivitySelector {
    	/**
    	 * A method that will select which activity should be selected next
    	 * @param activities
    	 * @return Avitity that has been selected 
    	 */
        public Activity select(Activity[] activities) {
        	int index = selectIndex(activities);
        	if (index<0) {
        		return null;
        	}
        	return activities[index];
        }
        
        /**
    	 * A method that will select the activity at which index should be selected next
    	 * @param activities
    	 * @return index of activity that has been selected
    	 */
        public int selectIndex(Activity[] activities) {
        	int minVal=Integer.MAX_VALUE;
        	int minIndex=-1;
        	for (int i=0; i<activities.length; i++) {
        		if (!activities[i].isBound()) {
        			if (minVal > activities[i].getLatestStartTime()-activities[i].getEarliestStartTime()) {
        				minVal = activities[i].getLatestStartTime()-activities[i].getEarliestStartTime();
        				minIndex = i;
        			}
        		}
        	}
        	return minIndex;
        }
    }
}
