/*
 * MetaheuristicTest.java
 * 
 * Created on Jul 1, 2005
 */
package jopt.csp.test.search;

import jopt.csp.CspSolver;
import jopt.csp.search.LocalSearch;
import jopt.csp.search.Metaheuristic;
import jopt.csp.search.Neighborhood;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.localsearch.TabuMetaheuristic;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests the Tabu Search Metaheueristic implementation
 */
public class MetaheuristicTest extends TestCase {
    public MetaheuristicTest(String testName) {
        super(testName);
    }
    
    public void testTabuForbiddenUndoMoves() {
        try {
            ChoicePointAlgorithm alg = SolverImpl.createDefaultAlgorithm();
            ConstraintStore store = new ConstraintStore(alg);
            CspVariableFactory varFactory = alg.getVarFactory();
            
            CspIntVariable a = varFactory.intVar("a", 0, 1); 
            CspIntVariable b = varFactory.intVar("b", 0, 1); 
            CspIntVariable c = varFactory.intVar("c", 0, 1);
            
            // need to add variables to store since
            // they are not directly added by posting a constraint
            // this would be handled already if it was created through th
            // SearchActions interface
            store.addVariable(a, true);
            store.addVariable(b, true);
            store.addVariable(c, true);
    
            // create initial solution of all zeros
            SolverSolution initial = new SolverSolution();
            initial.setValue(a, 0);
            initial.setValue(b, 0);
            initial.setValue(c, 0);
            
            // create solutions that will set each variable to 
            // 1 and 0 individually
            SolverSolution a0 = new SolverSolution();
            a0.setValue(a, 0);
            
            SolverSolution a1 = new SolverSolution();
            a1.setValue(a, 1);
            
            SolverSolution b0 = new SolverSolution();
            b0.setValue(b, 0);
            
            SolverSolution b1 = new SolverSolution();
            b1.setValue(b, 1);
            
            SolverSolution c0 = new SolverSolution();
            c0.setValue(c, 0);
            
            SolverSolution c1 = new SolverSolution();
            c1.setValue(c, 1);
            
            // initialize tabu metaheuristic
            // 2 forbidden undo moves
            TabuMetaheuristic tabu = new TabuMetaheuristic(store, 2, 0, 1);
            tabu.setInitialSolution(initial);
            
            // all neighbors should be valid
            assertTrue("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("c0 is valid", tabu.isAcceptableNeighbor(c0));
            assertTrue("c1 is valid", tabu.isAcceptableNeighbor(c1));
            
            // select a1 neighbor and update initial solution
            tabu.neighborSelected(a1);
            initial.setValue(a, 1);
            tabu.setInitialSolution(initial);
            
            // a0 should no longer be valid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("c0 is valid", tabu.isAcceptableNeighbor(c0));
            assertTrue("c1 is valid", tabu.isAcceptableNeighbor(c1));
            
            // select b1 neighbor and update initial solution
            tabu.neighborSelected(b1);
            initial.setValue(b, 1);
            tabu.setInitialSolution(initial);
            
            // a0 and b0 should no longer be valid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertFalse("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("c0 is valid", tabu.isAcceptableNeighbor(c0));
            assertTrue("c1 is valid", tabu.isAcceptableNeighbor(c1));
            
            // select c1 neighbor and update initial solution
            tabu.neighborSelected(c1);
            initial.setValue(c, 1);
            tabu.setInitialSolution(initial);
            
            // a0 should now be valid, but b0 and c0 should no longer be valid
            assertTrue("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertFalse("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertFalse("c0 is valid", tabu.isAcceptableNeighbor(c0));
            assertTrue("c1 is valid", tabu.isAcceptableNeighbor(c1));
            
            // b0 is now valid
            assertTrue("search can continue", tabu.continueSearch());
            assertTrue("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertFalse("c0 is valid", tabu.isAcceptableNeighbor(c0));
            assertTrue("c1 is valid", tabu.isAcceptableNeighbor(c1));
            
            // cannot undo very last move
            assertFalse("search can continue", tabu.continueSearch());
        }
        catch(PropagationFailureException propx) {
        	fail();
        }
        
    }

    public void testTabuForbiddenAlterMoves() {
        try {
            ChoicePointAlgorithm alg = SolverImpl.createDefaultAlgorithm();
            ConstraintStore store = new ConstraintStore(alg);
            CspVariableFactory varFactory = alg.getVarFactory();
            
            CspIntVariable a = varFactory.intVar("a", 0, 2); 
            CspIntVariable b = varFactory.intVar("b", 0, 2); 
            
            // need to add variables to store since
            // they are not directly added by posting a constraint
            // this would be handled already if it was created through th
            // SearchActions interface
            store.addVariable(a, true);
            store.addVariable(b, true);
    
            // create initial solution of all zeros
            SolverSolution initial = new SolverSolution();
            initial.setValue(a, 0);
            initial.setValue(b, 0);
            
            // create solutions that will set each variable to 
            // from 0 to 2
            SolverSolution a0 = new SolverSolution();
            a0.setValue(a, 0);
            
            SolverSolution a1 = new SolverSolution();
            a1.setValue(a, 1);
            
            SolverSolution a2 = new SolverSolution();
            a2.setValue(a, 2);
            
            SolverSolution b0 = new SolverSolution();
            b0.setValue(b, 0);
            
            SolverSolution b1 = new SolverSolution();
            b1.setValue(b, 1);
            
            SolverSolution b2 = new SolverSolution();
            b2.setValue(b, 2);
            
            // initialize tabu metaheuristic
            // 2 forbidden undo moves, 1 forbidden alter
            TabuMetaheuristic tabu = new TabuMetaheuristic(store, 2, 1, 1);
            tabu.setInitialSolution(initial);
            
            // all neighbors should be valid
            assertTrue("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            
            // select a1 neighbor and update initial solution
            tabu.neighborSelected(a1);
            initial.setValue(a, 1);
            tabu.setInitialSolution(initial);
            
            // changes that do not leave A=1 should no longer be valid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertFalse("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("b2 is valid", tabu.isAcceptableNeighbor(b2));
            
            // select b1 neighbor and update initial solution
            tabu.neighborSelected(b1);
            initial.setValue(b, 1);
            tabu.setInitialSolution(initial);
            
            // changes that do not leave B=1 or sets A back to 0 should no longer be valid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertFalse("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertFalse("b2 is valid", tabu.isAcceptableNeighbor(b2));
            
            // select a2 neighbor and update initial solution
            tabu.neighborSelected(a2);
            initial.setValue(a, 2);
            tabu.setInitialSolution(initial);
            
            // changes that do not leave A=2 or sets B back to 0 should no longer be valid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertFalse("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertFalse("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("b2 is valid", tabu.isAcceptableNeighbor(b2));

            
            // re-initialize tabu metaheuristic without alter move restriction
            initial.setValue(a, 0);
            initial.setValue(b, 0);
            tabu = new TabuMetaheuristic(store, 2, 0, 1);
            tabu.setInitialSolution(initial);

            // all neighbors should be valid
            assertTrue("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            
            // select a1 neighbor and update initial solution
            tabu.neighborSelected(a1);
            initial.setValue(a, 1);
            tabu.setInitialSolution(initial);
            
            // changes that assign A=0 is invalid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertTrue("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("b2 is valid", tabu.isAcceptableNeighbor(b2));
            
            // select a2 neighbor and update initial solution
            tabu.neighborSelected(a2);
            initial.setValue(a, 2);
            tabu.setInitialSolution(initial);
            
            // changes that assign A=0 or A=1 is invalid
            assertFalse("a0 is valid", tabu.isAcceptableNeighbor(a0));
            assertFalse("a1 is valid", tabu.isAcceptableNeighbor(a1));
            assertTrue("a2 is valid", tabu.isAcceptableNeighbor(a2));
            assertTrue("b0 is valid", tabu.isAcceptableNeighbor(b0));
            assertTrue("b1 is valid", tabu.isAcceptableNeighbor(b1));
            assertTrue("b2 is valid", tabu.isAcceptableNeighbor(b2));
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    public void testStuckLocalOptima() {
    	try{
            CspSolver solver = CspSolver.createSolver();
    		CspVariableFactory varFactory = solver.getVarFactory();
            SearchActions searchActions = solver.getSearchActions();
            LocalSearch localSearch = solver.getLocalSearch();
            
    		CspIntVariable a0 = varFactory.intVar("a0", 0, 1);
    		CspIntVariable a1 = varFactory.intVar("a1", 0, 1);
    		CspIntVariable cost = varFactory.intVar("cost", 0, 2);
    		
    		// variable is 1 if a0 and a1 are different
    		CspIntExpr isDiff = a0.neq(a1).toBoolean();
    		
    		// add constraint c = a0 + a1 + isDiff
    		solver.addConstraint(cost.eq(a0.add(a1).add(isDiff)));
            
            // create initial solution: 11
            SolverSolution solution = new SolverSolution();
            solution.setValue(a0, 1);
            solution.setValue(a1, 1);
            solution.setMinimizeObjective(cost);
            
            // greedy improvement requirement
            SearchAction improve = localSearch.improve(solution, 1);
            
            // create neighbor hood for solution
            Neighborhood hood = localSearch.flipNeighborhood(new CspIntVariable[]{a0, a1});
            
            // create action to move to neighboring solution
            SearchAction select = localSearch.neighborMove(solution, hood);
            SearchAction move = searchActions.combine(improve, select);
            
            // record best known solution
            SolverSolution best = new SolverSolution();
            best.copy(solution);
            
            // perform 50 moves looking for solution
            int numMoves = 0;
            while (solver.solve(move) && numMoves < 50) {
                
                // update best known value if a better solution is located
                if (solution.getIntObjectiveVal() < best.getIntObjectiveVal())
                    best.copy(solution);
                
                if (!solution.equals(best))
                    System.out.println(solution);
                
                numMoves++;
            }
            
            // verify solution was stuck: 11
            assertEquals("a0", 1, best.getValue(a0));
            assertEquals("a1", 1, best.getValue(a1));
    	}
    	catch(PropagationFailureException propx) {
    		propx.printStackTrace();
    		fail();
    	}
    }

    public void testHillClimb() {
        try{
            CspSolver solver = CspSolver.createSolver();
            CspVariableFactory varFactory = solver.getVarFactory();
            LocalSearch localSearch = solver.getLocalSearch();
            
            CspIntVariable a0 = varFactory.intVar("a0", 0, 1);
            CspIntVariable a1 = varFactory.intVar("a1", 0, 1);
            CspIntVariable cost = varFactory.intVar("cost", 0, 2);
            
            // variable is 1 if a0 and a1 are different
            CspIntExpr isDiff = a0.neq(a1).toBoolean();
            
            // add constraint c = a0 + a1 + isDiff
            solver.addConstraint(cost.eq(a0.add(a1).add(isDiff)));
            
            // create initial solution: 11
            SolverSolution solution = new SolverSolution();
            solution.setValue(a0, 1);
            solution.setValue(a1, 1);
            solution.setMinimizeObjective(cost);
            
            // create neighbor hood for solution
            Neighborhood hood = localSearch.flipNeighborhood(new CspIntVariable[]{a0, a1});
            
            // create the tabu search heuristic with 1 forbidden move
            Metaheuristic tabu = localSearch.tabu(1);
            
            // create action to move to solution
            SearchAction move = localSearch.neighborMove(solution, hood, tabu);

            // record best known solution
            SolverSolution best = new SolverSolution();
            best.copy(solution);
            
            // perform 50 moves looking for solution
            int numMoves = 0;
            while (solver.solve(move) && numMoves < 50) {
                
                // update best known value if a better solution is located
                if (solution.getIntObjectiveVal() < best.getIntObjectiveVal())
                    best.copy(solution);
                
                numMoves++;
            }
            
            // verify solution located: 00
            assertEquals("a0", 0, best.getValue(a0));
            assertEquals("a1", 0, best.getValue(a1));
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace();
            fail();
        }
    }

    public void setUp() {
    }
}
