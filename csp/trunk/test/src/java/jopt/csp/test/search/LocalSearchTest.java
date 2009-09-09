package jopt.csp.test.search;

import java.util.LinkedList;
import java.util.List;

import jopt.csp.CspSolver;
import jopt.csp.search.CurrentNeighbor;
import jopt.csp.search.LocalSearch;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SimpleNeighborhood;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.actions.AddConstraintAction;
import jopt.csp.spi.search.actions.GenerateIntegerAction;
import jopt.csp.spi.search.actions.RestoreSolutionAction;
import jopt.csp.spi.search.actions.StoreSolutionAction;
import jopt.csp.spi.search.goal.FirstSolutionGoal;
import jopt.csp.spi.search.localsearch.BrowseNeighborhoodAction;
import jopt.csp.spi.search.localsearch.FlipNeighborhood;
import jopt.csp.spi.search.localsearch.NeighborMoveAction;
import jopt.csp.spi.search.localsearch.SelectCurrentNeighborAction;
import jopt.csp.spi.search.localsearch.SwapNeighborhood;
import jopt.csp.spi.search.technique.DepthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.CombinedAction;
import jopt.csp.spi.search.tree.SearchTechniqueChange;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspNumVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Unit test for local search functionality
 */
public class LocalSearchTest extends TestCase {
    private ChoicePointAlgorithm alg;
    private ConstraintStore store;
    private CspVariableFactory varFactory;
	
    public LocalSearchTest(String testName) {
        super(testName);
    }
    
    public void testBrowseNeighbors() {
        CspIntVariable a = varFactory.intVar("a", 0, 1); 
        CspIntVariable b = varFactory.intVar("b", 0, 1); 
        CspIntVariable c = varFactory.intVar("c", 0, 1);
        SolutionScope abcScope = new SolutionScope();
        abcScope.add(a);
        abcScope.add(b);
        abcScope.add(c);
        
        // need to add variables to store since
        // they are not directly added by posting a constraint
        // this would be handled already if it was created through th
        // SearchActions interface
        store.addVariable(a, true);
        store.addVariable(b, true);
        store.addVariable(c, true);

        // create initial solution of all zeros
        SolverSolution initial = new SolverSolution(abcScope);
        initial.setValue(a, 0);
        initial.setValue(b, 0);
        initial.setValue(c, 0);
        
        // create solutions that will set each variable to 
        // 1 individually
        SolverSolution n1 = new SolverSolution();
        n1.setValue(a, 1);
        
        SolverSolution n2 = new SolverSolution();
        n2.setValue(b, 1);
        
        SolverSolution n3 = new SolverSolution();
        n3.setValue(c, 1);
        
        // create neighborhood to browse
        SimpleNeighborhood hood = new SimpleNeighborhood();
        hood.add(n1);
        hood.add(n2);
        hood.add(n3);
        
        // create browse neighbors search action
        SearchAction browse = new BrowseNeighborhoodAction(store, initial, hood, new CurrentNeighbor());
        
        // create action to store resulting solution
        SolverSolution result = new SolverSolution(abcScope);
        SearchAction storeSol = new StoreSolutionAction(store, result);
        
        // combined browse and store into single action
        List actions = new LinkedList();
        actions.add(browse);
        actions.add(storeSol);
        SearchAction browseAndStore = new CombinedAction(actions);
        
        // search for solutions
        TreeSearch search = new TreeSearch(store, browseAndStore, new DepthFirstSearch());
        for (int i=0; i<3; i++) {
            assertTrue("solution " + i + " located", search.nextSolution());
            
            switch(i) {
                case 0:
                    assertEquals("result 0 a", 1, result.getValue(a));
                    assertEquals("result 0 b", 0, result.getValue(b));
                    assertEquals("result 0 c", 0, result.getValue(c));
                    break;

                case 1:
                    assertEquals("result 1 a", 0, result.getValue(a));
                    assertEquals("result 1 b", 1, result.getValue(b));
                    assertEquals("result 1 c", 0, result.getValue(c));
                    break;

                case 2:
                    assertEquals("result 2 a", 0, result.getValue(a));
                    assertEquals("result 2 b", 0, result.getValue(b));
                    assertEquals("result 2 c", 1, result.getValue(c));
                    break;
            }
        }

        assertFalse("too many solutions", search.nextSolution());
    }
    
    public void testFlipNeighbors() {
        CspIntVariable a = varFactory.intVar("a", 0, 1); 
        CspIntVariable b = varFactory.intVar("b", 0, 1); 
        CspIntVariable c = varFactory.intVar("c", 0, 1);
        SolutionScope abcScope = new SolutionScope();
        abcScope.add(a);
        abcScope.add(b);
        abcScope.add(c);
        
        // need to add variables to store since
        // they are not directly added by posting a constraint
        // this would be handled already if it was created through th
        // SearchActions interface
        store.addVariable(a, true);
        store.addVariable(b, true);
        store.addVariable(c, true);

        // create initial solution of all zeros
        SolverSolution initial = new SolverSolution(abcScope);
        initial.setValue(a, 0);
        initial.setValue(b, 0);
        initial.setValue(c, 0);
        
        // create neighborhood to browse
        FlipNeighborhood hood = new FlipNeighborhood(new CspIntVariable[]{a, b, c});
        
        // create browse neighbors search action
        SearchAction browse = new BrowseNeighborhoodAction(store, initial, hood, new CurrentNeighbor());
        
        // create action to store resulting solution
        SolverSolution result = new SolverSolution(abcScope);
        SearchAction storeSol = new StoreSolutionAction(store, result);
        
        // combined browse and store into single action
        List actions = new LinkedList();
        actions.add(browse);
        actions.add(storeSol);
        SearchAction browseAndStore = new CombinedAction(actions);
        
        // search for solutions
        TreeSearch search = new TreeSearch(store, browseAndStore, new DepthFirstSearch());
        for (int i=0; i<3; i++) {
            search.nextSolution();
            
            assertTrue("a is bound", a.isBound());
            assertTrue("b is bound", b.isBound());
            assertTrue("c is bound", c.isBound());
            
            switch(i) {
                case 0:
                    assertEquals("result " + i + " a", 1, result.getValue(a));
                    assertEquals("result " + i + " b", 0, result.getValue(b));
                    assertEquals("result " + i + " c", 0, result.getValue(c));
                    break;

                case 1:
                    assertEquals("result " + i + " a", 0, result.getValue(a));
                    assertEquals("result " + i + " b", 1, result.getValue(b));
                    assertEquals("result " + i + " c", 0, result.getValue(c));
                    break;

                case 2:
                    assertEquals("result " + i + " a", 0, result.getValue(a));
                    assertEquals("result " + i + " b", 0, result.getValue(b));
                    assertEquals("result " + i + " c", 1, result.getValue(c));
                    break;
            }
        }

        assertFalse("too many solutions", search.nextSolution());
    }
    
    public void testFindFirstSolution() {
    	try {
            CspIntVariable x = varFactory.intVar("x", 0, 3); 
            CspIntVariable y = varFactory.intVar("y", 0, 3); 
            CspIntVariable z = varFactory.intVar("z", 0, 3);
            SolutionScope xyzScope = new SolutionScope();
            xyzScope.add(x);
            xyzScope.add(y);
            xyzScope.add(z);

            // adding x, y, z directly incase they are not added
            // by some constraint
            store.addVariable(x, true);
            store.addVariable(y, true);
            store.addVariable(z, true);

    		// duplicate scope and add objective
    		SolutionScope scope = new SolutionScope(xyzScope);
    		scope.setMaximizeObjective(z);
    		
    		// create initial solution of all zeros
    		SolverSolution solution = new SolverSolution(scope);
    		solution.setValue(x, 1);
    		solution.setValue(y, 0);
    		solution.setValue(z, 0);
            
    		// create neighborhood to browse
    		FlipNeighborhood hood = new FlipNeighborhood(new CspIntVariable[]{x, y, z});
    		
            // create browse neighbors search action
            CurrentNeighbor currentNeighbor = new CurrentNeighbor();
            SearchAction browse = new BrowseNeighborhoodAction(store, solution, hood, currentNeighbor);
            
    		// create search to choos first neighbor
            SearchAction findFirst = new SearchTechniqueChange(new FirstSolutionGoal(), new DepthFirstSearch(), browse);
    		
    		// create action to store resulting solution in solver back to 
    		// our solution
            SearchAction select = new SelectCurrentNeighborAction(store, solution, currentNeighbor);
    		
    		// combined find neighbor and store into single action
    		List actions = new LinkedList();
    		actions.add(findFirst);
    		actions.add(select);
    		SearchAction move = new CombinedAction(actions);
    		
    		// add constraint to ensure z is better than 0
    		store.addConstraint(x.add(y).eq(z));
    		store.addConstraint(z.gt(0));
    		
    		// search for first solution
            TreeSearch search = new TreeSearch(store, move, new DepthFirstSearch());
    		assertTrue("solution found", search.nextSolution());

            // verify solution
            assertEquals("x", 1, solution.getValue(x));
            assertEquals("y", 0, solution.getValue(y));
            assertEquals("z", 1, solution.getValue(z));
            
            // make sure it is the only solution
            assertFalse("too many solutions", search.nextSolution());
    	}
    	catch(PropagationFailureException failx) {
            failx.printStackTrace();
    		fail("failed adding constraint to store");
    	}
    }
    
    
    public void testMapColorManual() {
    	try {
            // create a map of western europe where each contry can be
            // painted one of two colors
    		CspIntVariable belgium = varFactory.intVar("belgium", 0, 1);
    		CspIntVariable denmark = varFactory.intVar("denmark", 0, 1);
    		CspIntVariable france = varFactory.intVar("france", 0, 1);
    		CspIntVariable germany = varFactory.intVar("germany", 0, 1);
    		CspIntVariable netherlands = varFactory.intVar("netherlands", 0, 1);
    		CspIntVariable luxembourg = varFactory.intVar("luxembourg", 0, 1);
            
            // create array containing all variables
            CspIntVariable vars[] = new CspIntVariable[]{belgium, denmark, france, germany, netherlands, luxembourg};
            
            // no bordering countries can be same color, with the exception
            // of luxembourg since since has 3 neighbors
    		store.addConstraint(france.neq(belgium));
    		store.addConstraint(france.neq(germany));
    		store.addConstraint(belgium.neq(netherlands));
    		store.addConstraint(germany.neq(netherlands));
    		store.addConstraint(germany.neq(denmark));

            // create a cost function that assigns a quality value based on which
            // country bording luxembourg shares the same color
            CspIntVariable quality = varFactory.intVar("quality", 0, 20000);
            
            // assign value to when luxemborg has a different color with each neighbor
            // france  = 1
            // germany = 15
            // belgium = 2
            //
            // this is done by casting neq to boolean [0,1] expressions and multiplying by
            // the correct cost
            CspIntExpr notFrance = varFactory.booleanVar("not france", luxembourg.neq(france)).multiply(1);
            CspIntExpr notGermany = varFactory.booleanVar("not germany", luxembourg.neq(germany)).multiply(15);
            CspIntExpr notBelgium = varFactory.booleanVar("not belgium", luxembourg.neq(belgium)).multiply(2);
            
            // constraint quality variable equal to sum of costs
            store.addConstraint(quality.eq(notFrance.add(notGermany).add(notBelgium)));
            
            // locate first solution
            SearchAction genVars = new GenerateIntegerAction(vars);
            TreeSearch initialSearch = new TreeSearch(store, genVars, new DepthFirstSearch());
            assertTrue("initial solution found", initialSearch.nextSolution());
            
            // store initial solution
            SolverSolution sol = new SolverSolution();
            sol.add(vars);
            sol.setMaximizeObjective(quality);

            // since we have six countries, we need to keep trying until we have failed
            // to change all six of them
            int cur = 0;
            int failed = 0;
            while (failed < 6) {
                // flip the color of the current country
                CspIntVariable curVar = vars[cur];
            	sol.setValue(curVar, 1 - sol.getValue(curVar));
                
                // create movement action
                SearchAction addCnst    = new AddConstraintAction(store, quality.geq(sol.getIntObjectiveVal() + 1));
                SearchAction restoreSol = new RestoreSolutionAction(store, sol);
                SearchAction storeSol   = new StoreSolutionAction(store, sol);
                SearchAction move       = new CombinedAction(addCnst, restoreSol, storeSol);
                
                // create search to process move
                TreeSearch search = new TreeSearch(store, move, new DepthFirstSearch());
                store.reset();
                if (search.nextSolution()) {
                    failed = 0;
                }
                else {
                    sol.setValue(curVar, 1 - sol.getValue(curVar));
                    failed++;
                }
                
                // work with next country
                cur = (cur+1) % 6;
            }
            
            // return to last solution
            SearchAction restoreSol = new RestoreSolutionAction(store, sol);
            TreeSearch search = new TreeSearch(store, restoreSol, new DepthFirstSearch());
            store.reset();
            assertTrue("returned to final solution", search.nextSolution());
            
            // verify final solution
            assertEquals("objective", 17, sol.getIntObjectiveVal());
            assertEquals("belgium", 0, sol.getValue(belgium));
            assertEquals("denmark", 1, sol.getValue(denmark));
            assertEquals("france", 1, sol.getValue(france));
            assertEquals("germany", 0, sol.getValue(germany));
            assertEquals("netherlands", 1, sol.getValue(netherlands));
            assertEquals("luxembourg", 1, sol.getValue(luxembourg));
        }
    	catch(PropagationFailureException failx) {
            failx.printStackTrace();
    		fail("failed adding constraint to store");
    	}
    }
    
    public void testMapColorNeighborhood() {
        try {
            // create a map of western europe where each contry can be
            // painted one of two colors
            CspIntVariable belgium = varFactory.intVar("belgium", 0, 1);
            CspIntVariable denmark = varFactory.intVar("denmark", 0, 1);
            CspIntVariable france = varFactory.intVar("france", 0, 1);
            CspIntVariable germany = varFactory.intVar("germany", 0, 1);
            CspIntVariable netherlands = varFactory.intVar("netherlands", 0, 1);
            CspIntVariable luxembourg = varFactory.intVar("luxembourg", 0, 1);
            
            // create array containing all variables
            CspIntVariable vars[] = new CspIntVariable[]{belgium, denmark, france, germany, netherlands, luxembourg};
            
            // no bordering countries can be same color, with the exception
            // of luxembourg since since has 3 neighbors
            store.addConstraint(france.neq(belgium));
            store.addConstraint(france.neq(germany));
            store.addConstraint(belgium.neq(netherlands));
            store.addConstraint(germany.neq(netherlands));
            store.addConstraint(germany.neq(denmark));

            // create a cost function that assigns a quality value based on which
            // country bording luxembourg shares the same color
            CspIntVariable quality = varFactory.intVar("quality", 0, 20000);
            
            // assign value to when luxemborg has a different color with each neighbor
            // france  = 1
            // germany = 15
            // belgium = 2
            //
            // this is done by casting neq to boolean [0,1] expressions and multiplying by
            // the correct cost
            CspIntExpr notFrance  = luxembourg.neq(france).toBoolean().multiply(1);
            CspIntExpr notGermany = luxembourg.neq(germany).toBoolean().multiply(15);
            CspIntExpr notBelgium = luxembourg.neq(belgium).toBoolean().multiply(2);
            
            // constraint quality variable equal to sum of costs
            store.addConstraint(quality.eq(notFrance.add(notGermany).add(notBelgium)));
            
            // locate first solution
            SearchAction genVars = new GenerateIntegerAction(vars);
            TreeSearch initialSearch = new TreeSearch(store, genVars, new DepthFirstSearch());
            assertTrue("initial solution found", initialSearch.nextSolution());
            
            // store initial solution
            SolverSolution sol = new SolverSolution();
            sol.add(vars);
            sol.setMaximizeObjective(quality);
            
            boolean moveMade = true;
            while (moveMade) {
                // create action to add a constraint that will be removed upon backtracking
                SearchAction addCnst = new AddConstraintAction(store, quality.gt(sol.getIntObjectiveVal() + 1));
                
                // create a neighborhood of moves based on solution
                Neighborhood hood = new FlipNeighborhood(vars);
                
                // create browse neighbors search action
                CurrentNeighbor currentNeighbor = new CurrentNeighbor();
                SearchAction browse = new BrowseNeighborhoodAction(store, sol, hood, currentNeighbor);
                
                // create search to find first neighbor
                SearchAction findNeighbor = new SearchTechniqueChange(new FirstSolutionGoal(), new DepthFirstSearch(), browse);
                
                // create action to store resulting solution in solver back to 
                // our solution
                SearchAction select = new SelectCurrentNeighborAction(store, sol, currentNeighbor);
                
                // combined find neighbor and store into single action
                SearchAction selectAndStore = new CombinedAction(addCnst, findNeighbor, select);
                
                // create search to move to next solution
                TreeSearch move = new TreeSearch(store, selectAndStore, new DepthFirstSearch());
                
                // attempt move
                store.reset();
                if (move.nextSolution())
                    moveMade = true;
                else
                    moveMade = false;
            }
            
            // return to last solution
            SearchAction restoreSol = new RestoreSolutionAction(store, sol);
            TreeSearch search = new TreeSearch(store, restoreSol, new DepthFirstSearch());
            store.reset();
            assertTrue("returned to final solution", search.nextSolution());
            
            // verify final solution
            assertEquals("objective", 17, sol.getIntObjectiveVal());
            assertEquals("belgium", 0, sol.getValue(belgium));
            assertEquals("denmark", 1, sol.getValue(denmark));
            assertEquals("france", 1, sol.getValue(france));
            assertEquals("germany", 0, sol.getValue(germany));
            assertEquals("netherlands", 1, sol.getValue(netherlands));
            assertEquals("luxembourg", 1, sol.getValue(luxembourg));
        }
        catch(PropagationFailureException failx) {
            failx.printStackTrace();
            fail("failed adding constraint to store");
        }
    }
    
    public void testSwapNeighborhood() {
        CspIntVariable a = varFactory.intVar("a", 0, 3); 
        CspIntVariable b = varFactory.intVar("b", 0, 3); 
        CspIntVariable c = varFactory.intVar("c", 0, 3); 
        CspIntVariable d = varFactory.intVar("d", 0, 3);
        
        // create initial solution
        SolverSolution solution = new SolverSolution();
        solution.setValue(a, 0);
        solution.setValue(b, 1);
        solution.setValue(c, 2);
        solution.setValue(d, 3);
        
        // create swap neighborhood
        Neighborhood hood = new SwapNeighborhood(new CspNumVariable[]{a, b, c, d});
        hood.setInitialSolution(solution);
        
        assertEquals("neighborhood size", 6, hood.size());
        
        for (int i=0; i<6; i++) {
        	SolverSolution neighbor = hood.getNeighbor(i);
            
            assertEquals("neighbor variable count", 2, neighbor.variables().size());
            
            switch(i) {
                case 0:
                    // swap a, b
                    assertTrue("a is bound", neighbor.isBound(a));
                    assertTrue("b is bound", neighbor.isBound(b));
                    assertEquals("a", 1, neighbor.getValue(a));
                    assertEquals("b", 0, neighbor.getValue(b));
                    break;
                    
                case 1:
                    // swap a, c
                    assertTrue("a is bound", neighbor.isBound(a));
                    assertTrue("c is bound", neighbor.isBound(c));
                    assertEquals("a", 2, neighbor.getValue(a));
                    assertEquals("c", 0, neighbor.getValue(c));
                    break;
                    
                case 2:
                    // swap a, d
                    assertTrue("a is bound", neighbor.isBound(a));
                    assertTrue("d is bound", neighbor.isBound(d));
                    assertEquals("a", 3, neighbor.getValue(a));
                    assertEquals("d", 0, neighbor.getValue(d));
                    break;
                    
                case 3:
                    // swap b, c
                    assertTrue("b is bound", neighbor.isBound(b));
                    assertTrue("c is bound", neighbor.isBound(c));
                    assertEquals("b", 2, neighbor.getValue(b));
                    assertEquals("c", 1, neighbor.getValue(c));
                    break;
                    
                case 4:
                    // swap b, d
                    assertTrue("b is bound", neighbor.isBound(b));
                    assertTrue("d is bound", neighbor.isBound(d));
                    assertEquals("b", 3, neighbor.getValue(b));
                    assertEquals("d", 1, neighbor.getValue(d));
                    break;
                    
                case 5:
                    // swap c, d
                    assertTrue("c is bound", neighbor.isBound(c));
                    assertTrue("d is bound", neighbor.isBound(d));
                    assertEquals("c", 3, neighbor.getValue(c));
                    assertEquals("d", 2, neighbor.getValue(d));
                    break;
                    
            }
        }
    }
    
    public void testBrowseSwapNeighborhood() {
        CspIntVariable a = varFactory.intVar("a", 0, 3); 
        CspIntVariable b = varFactory.intVar("b", 0, 3); 
        CspIntVariable c = varFactory.intVar("c", 0, 3); 
        CspIntVariable d = varFactory.intVar("d", 0, 3);
        store.addVariable(a, true);
        store.addVariable(b, true);
        store.addVariable(c, true);
        store.addVariable(d, true);
        
        // create array containing all variables
        CspIntVariable vars[] = new CspIntVariable[]{a, b, c, d};
        
        // create initial solution
        SolverSolution initial = new SolverSolution();
        initial.setValue(a, 0);
        initial.setValue(b, 1);
        initial.setValue(c, 2);
        initial.setValue(d, 3);
        
        // create result solution
        SolverSolution result = new SolverSolution();
        result.add(vars);
        
        // create swap neighborhood
        Neighborhood hood = new SwapNeighborhood(vars);
        
        // create browse action
        CurrentNeighbor currentNeighbor = new CurrentNeighbor();
        SearchAction browse = new BrowseNeighborhoodAction(store, initial, hood, currentNeighbor);
        
        // create store solution action
        SearchAction storeSol = new StoreSolutionAction(store, result);
        
        // create combined action to browse and select solution
        SearchAction browseAndStore = new CombinedAction(browse, storeSol);
        
        // create search for action
        TreeSearch search = new TreeSearch(store, browseAndStore, new DepthFirstSearch());
        
        // search for size solutions
        for (int i=0; i<6; i++) {
            assertTrue("located solution " + i, search.nextSolution());

            CspIntVariable var1 = null;
            CspIntVariable var2 = null;
            
            switch(i) {
                case 0:
                    // swap a, b
                    var1 = a;
                    var2 = b;
                    break;
                    
                case 1:
                    // swap a, c
                    var1 = a;
                    var2 = c;
                    break;
                    
                case 2:
                    // swap a, d
                    var1 = a;
                    var2 = d;
                    break;
                    
                case 3:
                    // swap b, c
                    var1 = b;
                    var2 = c;
                    break;
                    
                case 4:
                    // swap b, d
                    var1 = b;
                    var2 = d;
                    break;
                    
                case 5:
                    // swap c, d
                    var1 = c;
                    var2 = d;
                    break;
            }
            
            // verify vars were bound and swapped
            assertTrue(var1 + " bound", result.isBound(var1));
            assertTrue(var2 + " bound", result.isBound(var2));
            assertEquals(var2 + " result equals " + var1 + " initial", initial.getValue(var1), result.getValue(var2));
            assertEquals(var1 + " result equals " + var2 + " initial", initial.getValue(var2), result.getValue(var1));
            
            // loop over variables and verify variables not swapped are still at initial values
            for (int j=0; j<vars.length; j++) {
            	CspIntVariable var = vars[j];
                
                if (!var.equals(var1) && !var.equals(var2)) {
                    assertTrue(var + " bound", result.isBound(var));
                    assertEquals(var.toString(), initial.getValue(var), result.getValue(var));
                }
            }
        }
    }
    
    public void testLocalMove() {
        try {
            CspIntVariable x = varFactory.intVar("x", 0, 3); 
            CspIntVariable y = varFactory.intVar("y", 0, 3); 
            CspIntVariable z = varFactory.intVar("z", 0, 3);
            
            // add constraint to ensure z is better than 0
            store.addConstraint(x.add(y).eq(z));
            store.addConstraint(z.gt(0));
            
            // create array of all variables
            CspIntVariable vars[] = new CspIntVariable[]{x, y, z};
            
            // create initial solution of all zeros
            SolverSolution solution = new SolverSolution();
            solution.setValue(x, 1);
            solution.setValue(y, 0);
            solution.setValue(z, 0);
            solution.setMaximizeObjective(z);
            
            // create neighborhood to browse
            FlipNeighborhood hood = new FlipNeighborhood(vars);
            
            // create action to move not neighbor
            SearchAction move = new NeighborMoveAction(store, solution, hood);
            
            // search for first solution
            TreeSearch search = new TreeSearch(store, move, new DepthFirstSearch());
            assertTrue("solution found", search.nextSolution());
            
            // verify solution
            assertEquals("x", 1, solution.getValue(x));
            assertEquals("y", 0, solution.getValue(y));
            assertEquals("z", 1, solution.getValue(z));
            
            // make sure it is the only solution
            assertFalse("too many solutions", search.nextSolution());
        }
        catch(PropagationFailureException failx) {
            failx.printStackTrace();
            fail("failed adding constraint to store");
        }
    }
    
    public void testMapColorAPI() {
        try {
            CspSolver solver = CspSolver.createSolver();
            CspVariableFactory varFactory = solver.getVarFactory();
            SearchActions searchActions = solver.getSearchActions();
            LocalSearch localSearch = solver.getLocalSearch();

            // create a map of western europe where each contry can be
            // painted one of two colors
            CspIntVariable belgium = varFactory.intVar("belgium", 0, 1);
            CspIntVariable denmark = varFactory.intVar("denmark", 0, 1);
            CspIntVariable france = varFactory.intVar("france", 0, 1);
            CspIntVariable germany = varFactory.intVar("germany", 0, 1);
            CspIntVariable netherlands = varFactory.intVar("netherlands", 0, 1);
            CspIntVariable luxembourg = varFactory.intVar("luxembourg", 0, 1);
            
            // create array containing all variables
            CspIntVariable vars[] = new CspIntVariable[]{belgium, denmark, france, germany, netherlands, luxembourg};
            
            // no bordering countries can be same color, with the exception
            // of luxembourg since since has 3 neighbors
            solver.addConstraint(france.neq(belgium));
            solver.addConstraint(france.neq(germany));
            solver.addConstraint(belgium.neq(netherlands));
            solver.addConstraint(germany.neq(netherlands));
            solver.addConstraint(germany.neq(denmark));

            // create a cost function that assigns a quality value based on which
            // country bording luxembourg shares the same color
            CspIntVariable quality = varFactory.intVar("quality", 0, 20000);
            
            // assign value to when luxemborg has a different color with each neighbor
            // france  = 1
            // germany = 15
            // belgium = 2
            //
            // this is done by casting neq to boolean [0,1] expressions and multiplying by
            // the correct cost
            CspIntExpr notFrance  = luxembourg.neq(france).toBoolean().multiply(1);
            CspIntExpr notGermany = luxembourg.neq(germany).toBoolean().multiply(15);
            CspIntExpr notBelgium = luxembourg.neq(belgium).toBoolean().multiply(2);
            
            // constraint quality variable equal to sum of costs
            solver.addConstraint(quality.eq(notFrance.add(notGermany).add(notBelgium)));
            
            // locate first solution
            assertTrue("initial solution found", solver.solve(vars));
            
            // store initial solution
            SolverSolution solution = new SolverSolution();
            solution.add(vars);
            solution.setMaximizeObjective(quality);
            
            // create a flip neighborhood of relative solutions
            Neighborhood hood = localSearch.flipNeighborhood(vars);
            
            // create action to improve bounds of problem
            SearchAction improveBound = localSearch.improve(solution, 1);
            
            // create action move to neighboor solution 
            SearchAction neighborMove = localSearch.neighborMove(solution, hood);
            
            // create combined action that will be used to search for a solution
            SearchAction move = searchActions.combine(improveBound, neighborMove);
            
            // assert first solution is found
            assertTrue("first solution located", solver.solve(move));
            
            // find local optima
            while (solver.solve(move)){}
            
            // verify final solution
            assertEquals("objective", 17, solution.getIntObjectiveVal());
            assertEquals("belgium", 0, solution.getValue(belgium));
            assertEquals("denmark", 1, solution.getValue(denmark));
            assertEquals("france", 1, solution.getValue(france));
            assertEquals("germany", 0, solution.getValue(germany));
            assertEquals("netherlands", 1, solution.getValue(netherlands));
            assertEquals("luxembourg", 1, solution.getValue(luxembourg));
            
            // return to last solution
            // and verify variables are also set correctly
            solver.restoreSolution(solution, true);
            assertTrue("belgium bound", belgium.isBound());
            assertTrue("denmark bound", denmark.isBound());
            assertTrue("france bound", france.isBound());
            assertTrue("germany bound", germany.isBound());
            assertTrue("netherlands bound", netherlands.isBound());
            assertTrue("luxembourg bound", luxembourg.isBound());
            assertEquals("belgium var", 0, belgium.getMin());
            assertEquals("denmark var", 1, denmark.getMin());
            assertEquals("france var", 1, france.getMin());
            assertEquals("germany var", 0, germany.getMin());
            assertEquals("netherlands var", 1, netherlands.getMin());
            assertEquals("luxembourg var", 1, luxembourg.getMin());
        }
        catch(PropagationFailureException failx) {
            failx.printStackTrace();
            fail("failed adding constraint to store");
        }
    }
    
    
    public void setUp() {
        alg = SolverImpl.createDefaultAlgorithm();
        store = new ConstraintStore(alg);
        varFactory = alg.getVarFactory();
    }
    
    public void tearDown() {
        alg = null;
        store = null;
        varFactory = null;
    }
}
