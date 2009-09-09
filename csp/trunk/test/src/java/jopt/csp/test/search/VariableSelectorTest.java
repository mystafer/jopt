package jopt.csp.test.search;

import java.util.ArrayList;

import jopt.csp.CspSolver;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.search.MinDomainVariableSelector;
import jopt.csp.spi.search.RandomVariableSelector;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

public class VariableSelectorTest extends TestCase {
    
    private CspSolver solver;
    private CspVariableFactory varFactory;
    private ArrayList variables;
    private SearchActions actions;
    
    private static int MAX_TRIES = 20;
    
    public void setUp() {
        //Create our solver and variable factory
        solver = CspSolver.createSolver();
        actions = solver.getSearchActions();
        varFactory = solver.getVarFactory();
        variables = new ArrayList();
    }
    
    public void tearDown() {
        solver = null;
        actions = null;
        varFactory = null;
        variables = null;
    }
    
    /**
     * Test the search action to generate solution to array of CspDoubleVariables
     * This will use the default variable selection method of in-order.
     * 
     */
    public void testDoubleInOrder() {
        try {
            useDoubleVars();

            SearchAction action = actions.generate(
                    (CspDoubleVariable[]) variables.toArray(new CspDoubleVariable[0]), 0);
            
            SolverSolution solution = solveProblem(action);
            assertEquals(solution.getMin((CspDoubleVariable) variables.get(0)),1.0,0);
            assertEquals(solution.getMin((CspDoubleVariable) variables.get(1)),1.0000000000210003,0);
            assertEquals(solution.getMin((CspDoubleVariable) variables.get(2)),1.0000000000420006,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate solution to array of CspDoubleVariables.
     * This will use the random variable selection method.
     */
    public void testDoubleRandomOrder() {
        try {
            useDoubleVars();
            SearchAction action = actions.generate(
                    (CspDoubleVariable[]) variables.toArray(new CspDoubleVariable[0]), 0, true ,new RandomVariableSelector());

            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    
    /**
     * Test the search action to generate solution to array of CspIntVariables
     * This will use the default variable selection method of in-order.
     * 
     */
    public void testIntInOrder() {
        try {
            useIntVars();
            SearchAction action = actions.generate(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]));
            
            SolverSolution solution = solveProblem(action);  
            
            assertEquals(solution.getMin((CspIntVariable) variables.get(0)),1,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(1)),2,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(2)),3,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate solution to array of CspIntVariables.
     * This will use the random variable selection method.
     */
    public void testIntRandomOrder() {
        try {
            useIntVars();
            SearchAction action = actions.generate(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]), null, new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate solution to array of CspLongVariable
     * This will use the default variable selection method of in-order.
     */
    public void testLongInOrder() {
        try {
            useLongVars(); 
            SearchAction action = actions.generate(
                    (CspLongVariable[]) variables.toArray(new CspLongVariable[3]));
            
            SolverSolution solution = solveProblem(action);

            assertEquals(solution.getMin((CspLongVariable) variables.get(0)),1,0);
            assertEquals(solution.getMin((CspLongVariable) variables.get(1)),2,0);
            assertEquals(solution.getMin((CspLongVariable) variables.get(2)),3,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate solution to array of CspLongVariable.
     * This will use the random variable selection method.
     */
    public void testLongRandomOrder() {
        try {
            useLongVars();
            SearchAction action = actions.generate(
                    (CspLongVariable[]) variables.toArray(new CspLongVariable[0]), null, new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate a non binary solution to array of CspIntVariables.
     * This will use the default variable selection method of in-order.
     */
    public void testIntNonBinaryInOrder() {
        try {
            useIntVars();
            SearchAction action = actions.generateNonBinary(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]));
            
            SolverSolution solution = solveProblem(action);  
            
            assertEquals(solution.getMin((CspIntVariable) variables.get(0)),1,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(1)),2,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(2)),3,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate a non binary solution to array of CspIntVariables.
     * This will use the random variable selection method.
     */
    public void testIntNonBinaryRandomOrder() {
        try {
            useIntVars();
            SearchAction action = actions.generateNonBinary(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]), new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate a solution to array of CspIntVariables using the splitGenerate 
     * method. This will use the default variable selection method of in-order.
     */
    public void testIntSplitInOrder() {
        try {
            useIntVars();
            SearchAction action = actions.splitGenerate(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]));
            
            SolverSolution solution = solveProblem(action);  
            
            assertEquals(solution.getMin((CspIntVariable) variables.get(0)),1,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(1)),2,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(2)),3,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate a solution to array of CspIntVariables using the splitGenerate 
     * method. This will use the random variable selection method.
     */
    public void testIntSplitRandomOrder() {
        try {
            useIntVars();
            SearchAction action = actions.splitGenerate(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[0]),true, new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate a solution to array of CspLongVariables using the splitGenerate 
     * method. This will use the default variable selection method of in-order.
     */
    public void testLongSplitInOrder() {
        try {
            useLongVars(); 
            SearchAction action = actions.splitGenerate(
                    (CspLongVariable[]) variables.toArray(new CspLongVariable[3]));
            
            SolverSolution solution = solveProblem(action);

            assertEquals(solution.getMin((CspLongVariable) variables.get(0)),1,0);
            assertEquals(solution.getMin((CspLongVariable) variables.get(1)),2,0);
            assertEquals(solution.getMin((CspLongVariable) variables.get(2)),3,0);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        } 
    }
    
    /**
     * Test the search action to generate a solution to array of CspLongVariables using the splitGenerate 
     * method. This will use the random variable selection method.
     */
    public void testLongSplitRandomOrder() {
        try {
            useLongVars();
            SearchAction action = actions.splitGenerate(
                    (CspLongVariable[]) variables.toArray(new CspLongVariable[0]), true, new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);
            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);  
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    
    /**
     * Test the search action to generate solution to array of CspSetVariables.
     * This will use the default variable selection method of in-order.
     */
    public void testSetInOrder() {
        try {
            useSetVars(); 
            SearchAction action = actions.generate(
                    (CspSetVariable[]) variables.toArray(new CspSetVariable[3]));
            
            SolverSolution solution = solveProblem(action);

            assertTrue(solution.getSolution((CspSetVariable) variables.get(0)).getPossibleSet().contains(new Integer(1)));
            assertTrue(solution.getSolution((CspSetVariable) variables.get(0)).getPossibleSet().contains(new Integer(2)));
            assertTrue(solution.getSolution((CspSetVariable) variables.get(0)).getPossibleSet().contains(new Integer(3)));
        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        } 
    }
    
    /**
     * Test the search action to generate solution to array of CspSetVariables
     * This will use the random variable selection method.
     */
    public void testSetRandomOrder() {
        try {
            useSetVars(); 
            SearchAction action = actions.generate(
                    (CspSetVariable[]) variables.toArray(new CspSetVariable[3]),null,new RandomVariableSelector());
            
            SolverSolution solution1 = solveProblem(action);
            SolverSolution solution2 = solveProblem(action);

            int max = 0;
            //If random is working we will eventually get different solutions
            while(!solution1.isDifferent(solution2) && max <= MAX_TRIES) {
                solution2 = solveProblem(action);
                max++;
            }
            //If we found different solutions within the limit
            assertTrue(max < MAX_TRIES);        }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        }  
    }
    
    /**
     * Test the Min Domain variable selector using an array of CspIntVariables with 
     * different domain sizes;
     */
    public void testMinDomainOrder() {
        try {
            useIntVarsWithDomainDifferences(); 
            SearchAction action = actions.generate(
                    (CspIntVariable[]) variables.toArray(new CspIntVariable[3]), null, new MinDomainVariableSelector());
            
            SolverSolution solution = solveProblem(action);
            assertEquals(solution.getMin((CspIntVariable) variables.get(0)),3,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(1)),1,0);
            assertEquals(solution.getMin((CspIntVariable) variables.get(2)),2,0);
  }
        catch (PropagationFailureException ex) {
            ex.printStackTrace();
            assertTrue(false);
        } 
    } 
    
    /**
     * Helper method to solve the solution and return its stored solution.
     */
    private SolverSolution solveProblem(SearchAction action) {
        solver.reset();
        SolutionScope scope = new SolutionScope();
        for(int i=0;i<variables.size();i++) {
            scope.add((CspVariable) variables.get(i));
        }
        
        assertTrue(solver.solve(action));
        
        return solver.storeSolution(scope);
    }
    
    /**
     * Helper method to create CspDoubleVariables and constraints
     */
    private void useDoubleVars() throws PropagationFailureException {
        CspDoubleVariable x = varFactory.doubleVar("x", 1, 3);
        CspDoubleVariable y = varFactory.doubleVar("y", 1, 3);
        CspDoubleVariable z = varFactory.doubleVar("z", 1, 3);

        variables.add(x);
        variables.add(y);
        variables.add(z);
        
        solver.addConstraint(x.neq(y));
        solver.addConstraint(x.neq(z));
        solver.addConstraint(y.neq(z));

    }
    
    /**
     * Helper method to create CspIntVariables and constraints
     */
    private void useIntVars() throws PropagationFailureException {
        CspIntVariable x = varFactory.intVar("x", 1, 3);
        CspIntVariable y = varFactory.intVar("y", 1, 3);
        CspIntVariable z = varFactory.intVar("z", 1, 3);

        variables.add(x);
        variables.add(y);
        variables.add(z);
        
        solver.addConstraint(x.neq(y));
        solver.addConstraint(x.neq(z));
        solver.addConstraint(y.neq(z));
    }
    
    private void useIntVarsWithDomainDifferences() throws PropagationFailureException {
        CspIntVariable x = varFactory.intVar("x", 1, 10);
        CspIntVariable y = varFactory.intVar("y", 1, 3);
        CspIntVariable z = varFactory.intVar("z", 1, 6);

        variables.add(x);
        variables.add(y);
        variables.add(z);
        
        solver.addConstraint(x.neq(y));
        solver.addConstraint(x.neq(z));
        solver.addConstraint(y.neq(z));
    }
    
    /**
     * Helper method to create CspLongVariable and constraints
     */
    private void useLongVars() throws PropagationFailureException {
        CspLongVariable x = varFactory.longVar("x", 1, 3);
        CspLongVariable y = varFactory.longVar("y", 1, 3);
        CspLongVariable z = varFactory.longVar("z", 1, 3);
        
        variables.add(x);
        variables.add(y);
        variables.add(z);
        
        solver.addConstraint(x.neq(y));
        solver.addConstraint(x.neq(z));
        solver.addConstraint(y.neq(z));
        
    }
    
    /**
     * Helper method to create CspSetVariable and constraints
     */
    private void useSetVars() throws PropagationFailureException {
        CspSetVariable x = varFactory.intSetVar("x", 1, 3);
        CspSetVariable y = varFactory.intSetVar("y", 1, 3);
        CspSetVariable z = varFactory.intSetVar("z", 1, 3);
        
        variables.add(x);
        variables.add(y);
        variables.add(z);
        
        solver.addConstraint(varFactory.getSetConstraints().nullIntersection(new CspSetVariable[] {x,y,z}));
    }
    
}
