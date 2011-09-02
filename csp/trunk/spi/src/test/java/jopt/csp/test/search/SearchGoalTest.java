package jopt.csp.test.search;

import jopt.csp.CspSolver;
import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.search.SearchTechniques;
import jopt.csp.spi.search.actions.GenerateIntegerAction;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Basic test of search goals for a variety of search techniques
 */
public class SearchGoalTest extends TestCase {
    private CspIntVariable x; 
    private CspIntVariable y; 
    private CspIntVariable z;
    private CspIntVariable a1; 
    private CspIntVariable a2; 
    private CspIntVariable a3;
    private CspGenericIntExpr ai;
    private GenericIndex idxI;
    private CspIntVariable b; 
    private SearchAction genVars;
    private SearchAction genVars2;
    private SearchGoals searchGoals;
    private SearchGoal goal;
    private SearchTechniques searchTechniques;
    private Search search;
    private CspSolver solver;
    private CspMath varMath;

    public SearchGoalTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
            solver = CspSolver.createSolver();
            CspVariableFactory varFactory = solver.getVarFactory();
            varMath = varFactory.getMath();
            searchGoals = solver.getSearchGoals();
            searchTechniques = solver.getSearchTechniques();
            x = varFactory.intVar("x", 0, 3); 
            y = varFactory.intVar("y", 0, 3); 
            z = varFactory.intVar("z", 0, 3);
            a1 = varFactory.intVar("a1", 1, 3);
            a2 = varFactory.intVar("a2", 1, 3);
            a3 = varFactory.intVar("a3", 1, 3);
            idxI = new GenericIndex("idxI", 3);
            ai = varFactory.genericInt("ai", new GenericIndex[]{idxI}, new CspIntVariable[]{a1, a2, a3});
            b = varFactory.intVar("b", 1, 7);
            genVars = new GenerateIntegerAction(new CspIntVariable[]{x, y, z});
            genVars2 = new GenerateIntegerAction(new CspIntVariable[]{a1, a2, a3, b});
    }
    
/*    private void dumpSolution(int solNum) {
        System.out.println(solNum + ": ============");
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }
    
    private void dumpSolution2(int solNum) {
        System.out.println(solNum + ": ============");
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
        System.out.println(b);
    }*/
    
    public void testMinimizeXGoalWithBfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.minimize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMax();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                int newBest = x.getMax();
                assertTrue("new best is as good or better", newBest<=best);
                best = newBest;
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("final best for x is 0", 0, best);
            assertEquals("4 solutions encountered", 4, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeXGoalWithBfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.minimize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("x is always 0", 0, x.getMax());
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("4 total solutions", 4, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXGoalWithBfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMin();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                int newBest = x.getMin();
                assertTrue("new best is as good or better", newBest>=best);
                best = newBest;
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("final best for x is 3", 3, best);
            assertEquals("4 solutions encountered", 4, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXGoalWithBfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("x is always 3", 3, x.getMin());
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("1 total solution", 1, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testFirstGoalWithBfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.first();
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            search.setGoal(goal);
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
//            dumpSolution(1);
            assertFalse("should be only one solution", search.nextSolution());
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testAddingMinimizeXGoalWithBfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.minimize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            search.setGoal(goal);
            int best = x.getMax();
            while(foundSolution) {
                int newBest = x.getMax();
                assertTrue("new best is as good or better", newBest<=best);
                best = newBest;
                foundSolution = search.nextSolution();
            }
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testAddingMaximizeXGoalWithBfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.bfs());
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate second solution");
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            while(foundSolution) {
                foundSolution = search.nextSolution();
                if(foundSolution)
                    assertEquals("x is always 3", 3, x.getMin());
            }
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeXGoalWithDfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.minimize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMax();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                numSolutions++;
                int newBest = x.getMax();
                assertTrue("new best is as good or better", newBest<=best);
                best = newBest;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("final best for x is 0", 0, best);
            assertEquals("4 solutions encountered", 4, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeXGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.minimize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("x is always 0", 0, x.getMax());
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("4 total solutions", 4, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXGoalWithDfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMin();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                int newBest = x.getMin();
                assertTrue("new best is as good or better", newBest>=best);
                best = newBest;
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("final best for x is 3", 3, best);
            assertEquals("10 solutions encountered", 10, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("x is always 3", 3, x.getMin());
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("1 total solution", 1, numSolutions);
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testFirstGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.first();
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
//            dumpSolution(1);
            assertFalse("should be only one solution", search.nextSolution());
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testAddingMinimizeXGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            for (int i=1; i<2; i++) {
                foundSolution = search.nextSolution();
                if (!foundSolution) fail("did not locate solution #"+i);
            }
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            while(foundSolution) {
                foundSolution = search.nextSolution();
                if(foundSolution) {
                    assertEquals("x is always 3", 3, x.getMin());
                }
            }
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testAddingMaximizeXGoalWithDfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            goal = searchGoals.maximize(x);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            for (int i=1; i<3; i++) {
                foundSolution = search.nextSolution();
                if (!foundSolution) fail("did not locate solution #"+i);
            }
            search.setGoal(goal);
            int best = x.getMax();
            while(foundSolution) {
                int newBest = x.getMax();
                assertTrue("new best is as good or better", newBest>=best);
                best = newBest;
                foundSolution = search.nextSolution();
            }
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeXPlusYGoalWithDfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            CspIntExpr xplusy = x.add(y);
            goal = searchGoals.minimize(xplusy);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMin()+y.getMin();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                int newBest = x.getMin()+y.getMin();
                assertTrue("new best is as good or better", newBest<=best);
                best = newBest;
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("1 solution encountered", 1, numSolutions);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXPlusYGoalWithDfsTechniqueContinuallyImprove() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            CspIntExpr xplusy = x.add(y);
            goal = searchGoals.maximize(xplusy);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            int best = x.getMax()+y.getMax();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                int newBest = x.getMin()+y.getMin();
                assertTrue("new best is as good or better", newBest>=best);
                best = newBest;
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("7 solutions encountered", 7, numSolutions);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeXPlusYGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            CspIntExpr xplusy = x.add(y);
            goal = searchGoals.minimize(xplusy);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("x is 0", 0, x.getMax());
                assertEquals("y is 0", 0, y.getMax());
                numSolutions++;
//                dumpSolution(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("1 solution", 1, numSolutions);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMaximizeXPlusYGoalWithDfsTechnique() {
        try {
            solver.addConstraint(x.add(y).eq(z));
            CspIntExpr xplusy = x.add(y);
            goal = searchGoals.maximize(xplusy);
            search = searchTechniques.search(genVars, searchTechniques.dfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                numSolutions++;
//                dumpSolution(numSolutions);
                
                if(x.getMin()==0)
                    assertEquals("y is 3", 3, y.getMin());
                else if(y.getMin()==0)
                    assertEquals("x is 3", 3, x.getMin());
                else if(x.getMin()==1)
                    assertEquals("y is 2", 2, y.getMin());
                else if(y.getMin()==1)
                    assertEquals("x is 2", 2, x.getMin());
                else
                    fail();
                foundSolution = search.nextSolution();
            }
            assertEquals("4 solutions", 4, numSolutions);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testMinimizeA1plusBGoalWithDfsTechnique() {
        try {
            CspIntExpr a1plusb = a1.add(b);
            goal = searchGoals.maximize(a1plusb);
            search = searchTechniques.search(genVars2, searchTechniques.dfs());
            search.setGoal(goal);
            search.setContinuallyImprove(false);
            solver.addConstraint(varMath.summation(ai, new CspGenericIndex[]{(CspGenericIndex) idxI}, null).eq(b));
            int numSolutions = 0;
            boolean foundSolution = search.nextSolution();
            if (!foundSolution) fail("did not locate first solution");
            while (foundSolution) {
                assertEquals("a1 is 3", 3, a1.getMax());
                assertEquals("b is 7", 7, b.getMax());
                numSolutions++;
//                dumpSolutionBoth(numSolutions);
                foundSolution = search.nextSolution();
            }
            assertEquals("3 solutions", 3, numSolutions);
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
