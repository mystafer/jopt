package jopt.csp.test.search;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.actions.NonBinaryGenerateIntegerAction;
import jopt.csp.spi.search.technique.BreadthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.BasicSearchNode;
import jopt.csp.spi.search.tree.CrawlingSearchTree;
import jopt.csp.spi.search.tree.DeltaStateManager;
import jopt.csp.spi.search.tree.JumpingSearchTree;
import jopt.csp.spi.search.tree.ProblemStateManager;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Basic test of depth first searching
 */
public class NonBinaryBreadthFirstSearchTest extends TestCase {
    private ConstraintStore store;
    private CspVariableFactory varFactory;
    private CspIntVariable x; 
    private CspIntVariable y; 
    private CspIntVariable z;

    public NonBinaryBreadthFirstSearchTest(String testName) {
        super(testName);
    }
    
/*    private void dumpSolution(int solNum) {
        System.out.println(solNum + ": ============");
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }*/
    
    private void doTestSearch(TreeSearch search) {
        if (!search.nextSolution()) fail("did not locate first solution");
        for (int i=1; i<10; i++) {
//            dumpSolution(i);
            if (!search.nextSolution()) fail("did not locate solution: " + (i+1));
        }
        
//        dumpSolution(10);
        if (search.nextSolution()) fail("located more than 10 solutions");
    }
    
    public void testRecalculatingTree() {
        SearchAction genVars = new NonBinaryGenerateIntegerAction(new CspIntVariable[]{x, y, z});
        TreeSearch search = new TreeSearch(store, genVars, new BreadthFirstSearch());
        doTestSearch(search);
    }
    
    public void testProblemStateTree() {
        SearchAction genVars = new NonBinaryGenerateIntegerAction(new CspIntVariable[]{x, y, z});
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search);
    }
    
    public void testDeltaStateTree() {
        SearchAction genVars = new NonBinaryGenerateIntegerAction(new CspIntVariable[]{x, y, z});
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search);
    }
    
    public void testJumpingTree() {
        SearchAction genVars = new NonBinaryGenerateIntegerAction(new CspIntVariable[]{x, y, z});
        SearchTree tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        doTestSearch(search);
    }
    
    public void setUp() {
        try {
            store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
            varFactory = store.getConstraintAlg().getVarFactory();
            x = varFactory.intVar("x", 0, 3); 
            y = varFactory.intVar("y", 0, 3); 
            z = varFactory.intVar("z", 0, 3);
            store.addConstraint(x.add(y).eq(z));
        }
        catch(PropagationFailureException failx) {
            fail("failed adding constraint to store");
        }
    }
    
    public void tearDown() {
        store = null;
        varFactory = null;
        x = null; 
        y = null; 
        z = null;
    }
}
