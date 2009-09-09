package jopt.csp.test.search;

import java.util.ArrayList;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.set.EqUnion;
import jopt.csp.spi.arcalgorithm.constraint.set.NullIntersection;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.search.actions.GenerateSetAction;
import jopt.csp.spi.search.technique.BreadthFirstSearch;
import jopt.csp.spi.search.technique.DepthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.BasicSearchNode;
import jopt.csp.spi.search.tree.CrawlingSearchTree;
import jopt.csp.spi.search.tree.DeltaStateManager;
import jopt.csp.spi.search.tree.JumpingSearchTree;
import jopt.csp.spi.search.tree.ProblemStateManager;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.CspIntSetVariable;
import jopt.csp.variable.CspSetVariable;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Basic test of depth first searching
 */
public class SetSearchTest extends TestCase {
    private ConstraintStore store;
    private CspSetVariable vars[] = null; 

    public SetSearchTest(String testName) {
        super(testName);
    }
    
    public void tearDown() {
        store = null;
        vars = null; 
    }
    
    private void doTestSearch(TreeSearch search, int expectedSol) {
        boolean solFound = search.nextSolution();
        int solCount = 0;
        while (solFound) {
//            System.out.println("---------");
//            System.out.println("solution: " + solCount);
//            System.out.println(vars[0]);
//            System.out.println(vars[1]);
//            System.out.println(vars[2]);
            
            assertTrue("x1 bound", vars[0].isBound());
            assertTrue("x2 bound", vars[1].isBound());
            assertTrue("x3 bound", vars[2].isBound());
            
            solCount++;
            solFound = search.nextSolution();
        }
        
        assertEquals("num solutions", expectedSol, solCount);
    }
    
    public void testDFSProblemStateTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testDFSDeltaStateTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testDFSJumpingTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        search = new TreeSearch(tree, new DepthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testDFSRecalculatingTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        TreeSearch search = new TreeSearch(store, genVars, new DepthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        search = new TreeSearch(store, genVars, new DepthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testBFSProblemStateTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testBFSDeltaStateTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testBFSJumpingTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        SearchTree tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void testBFSRecalculatingTree() {
        setUp(false);
        SearchAction genVars = new GenerateSetAction(vars);
        TreeSearch search = new TreeSearch(store, genVars, new BreadthFirstSearch());
        doTestSearch(search, 27);

        setUp(true);
        genVars = new GenerateSetAction(vars);
        search = new TreeSearch(store, genVars, new BreadthFirstSearch());
        doTestSearch(search, 27);
    }
    
    public void setUp(boolean scenario1) {
        try {
            store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
            int n = 3;
            
            // define variables
            vars = new IntSetVariable[n];
            for (int i=0; i<n; i++) {
                ArrayList numbers = new ArrayList();
                for (int j=0; j<n; j++) {
                    numbers.add(new Integer(j));
                }
                
//                CspIntSetVariable v = varFactory.intSetVar("x"+(i+1), iSet);
                CspIntSetVariable v = new IntSetVariable("x"+(i+1), numbers);
                ((VariableChangeSource) v).addVariableChangeListener(store);
                vars[i] = v;
            }
            
            // add constraints that will stop queens from attacking each other
            for (int i=0; i<n; i++) {
                for (int j=i+1; j<n; j++) {
                    // The third is a union of the previous two
                	store.addConstraint(new EqUnion((IntSetVariable)vars[0],(IntSetVariable)vars[1], (IntSetVariable)vars[2]));
                    if(!store.getAutoPropagate()) {
                        store.propagate();
                    }
                    
                    // The first and second sets should have nothing in common
                    store.addConstraint(new NullIntersection((IntSetVariable)vars[0],(IntSetVariable)vars[1]));
                    if(!store.getAutoPropagate()) {
                        store.propagate();
                    }               
                }
            }
        }
        catch(PropagationFailureException failx) {
            fail("failed adding constraint to store");
        }
    }
}
