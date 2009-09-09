/*
 * SolutionStoreTest.java
 * 
 * Created on Jun 16, 2005
 */
package jopt.csp.test.search;

import java.util.LinkedList;
import java.util.List;

import jopt.csp.search.SearchAction;
import jopt.csp.solution.IntSolution;
import jopt.csp.solution.SolutionScope;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.actions.GenerateIntegerAction;
import jopt.csp.spi.search.actions.RestoreSolutionAction;
import jopt.csp.spi.search.actions.StoreSolutionAction;
import jopt.csp.spi.search.technique.DepthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.BasicSearchNode;
import jopt.csp.spi.search.tree.ChoicePoint;
import jopt.csp.spi.search.tree.CombinedAction;
import jopt.csp.spi.search.tree.CrawlingSearchTree;
import jopt.csp.spi.search.tree.DeltaStateManager;
import jopt.csp.spi.search.tree.JumpingSearchTree;
import jopt.csp.spi.search.tree.ProblemStateManager;
import jopt.csp.spi.search.tree.RecalculatingStateManager;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.search.tree.TreeNode;
import jopt.csp.spi.search.tree.TreeStateManager;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests storing an restoring solutions in different
 * search trees
 */
public class SolutionStoreTest extends TestCase {
	private ChoicePointAlgorithm alg;
	private ConstraintStore store;
	
	private CspIntVariable x;
	private CspIntVariable y;
	private CspIntVariable z;
    private CspIntVariable a;
    private CspIntVariable b;
    
    private SolutionScope xyzScope;
    private SolverSolution xyzSol;

    private SearchAction genX;
    private SearchAction genY;
    private SearchAction storeXyzSol;
    private SearchAction combinedXyzActions;
	
	public SolutionStoreTest(String testName) {
		super(testName);
	}
	
    public void testRecalculatingStore() {
        TreeNode rootNode = new BasicSearchNode(combinedXyzActions);
        TreeStateManager mgr = new RecalculatingStateManager(store);
        SearchTree tree = new CrawlingSearchTree(rootNode, mgr);
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestStore(search);
    }
    
    public void testDeltaStore() {
        TreeNode rootNode = new BasicSearchNode(combinedXyzActions);
        TreeStateManager mgr = new DeltaStateManager(store);
        SearchTree tree = new CrawlingSearchTree(rootNode, mgr);
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestStore(search);
    }
    
    public void testJumpingStore() {
        TreeNode rootNode = new BasicSearchNode(combinedXyzActions);
        SearchTree tree = new JumpingSearchTree(rootNode, new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestStore(search);
    }
    
    public void testRecalculatingRestore() {
        TreeStateManager mgr = new RecalculatingStateManager(store);
        SearchTree tree = new CrawlingSearchTree(createRestoreScenarioNode(), mgr);
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestRestore(search);
    }

    public void testDeltaRestore() {
        TreeStateManager mgr = new DeltaStateManager(store);
        SearchTree tree = new CrawlingSearchTree(createRestoreScenarioNode(), mgr);
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestRestore(search);
    }

    public void testJumpingRestore() {
        SearchTree tree = new JumpingSearchTree(createRestoreScenarioNode(), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        doTestRestore(search);
    }

    private TreeNode createRestoreScenarioNode() {
		SolverSolution sol1 = new SolverSolution(xyzScope);
		IntSolution xSol = sol1.getSolution(x);
		xSol.setValue(0);
		IntSolution ySol = sol1.getSolution(y);
		ySol.setValue(0);
		IntSolution zSol = sol1.getSolution(z);
		zSol.setValue(0);
		SearchAction restoreSol1 = new RestoreSolutionAction(store, sol1);
		
		SolverSolution sol2 = new SolverSolution(xyzScope);
		xSol = sol2.getSolution(x);
		xSol.setValue(1);
		ySol = sol2.getSolution(y);
		ySol.setValue(1);
		zSol = sol2.getSolution(z);
		zSol.setValue(2);
		SearchAction restoreSol2 = new RestoreSolutionAction(store, sol2);
		
		SearchAction generateA = new GenerateIntegerAction(new CspIntVariable[]{a});
		SearchAction generateB = new GenerateIntegerAction(new CspIntVariable[]{b});
		
		List actionList = new LinkedList();
		actionList.add(restoreSol1);
		actionList.add(generateA);
		SearchAction restoreAndGenA = new CombinedAction(actionList);
		
		actionList = new LinkedList();
		actionList.add(restoreSol2);
		actionList.add(generateB);
		SearchAction restoreAndGenB = new CombinedAction(actionList);
		
		actionList = new LinkedList();
		actionList.add(restoreAndGenA);
		actionList.add(restoreAndGenB);
		SearchAction abChoice = new ChoicePoint(actionList);
		
		TreeNode rootNode = new BasicSearchNode(abChoice);
		return rootNode;
    }
    
    private void doTestRestore(TreeSearch search) {
            
//            System.out.println("--- before ---");
//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(z);
//            System.out.println(a);
//            System.out.println(b);
            int solNum = 0;
            while (search.nextSolution()) {
//                System.out.println("--- " + solNum + " ---");
//                System.out.println(x);
//                System.out.println(y);
//                System.out.println(z);
//                System.out.println(a);
//                System.out.println(b);
                
                if (solNum<5) {
                    assertTrue("x is bound", x.isBound());
                	assertEquals("x", 0, x.getMin());
                    assertTrue("y is bound", y.isBound());
                    assertEquals("y", 0, y.getMin());
                    assertTrue("z is bound", z.isBound());
                    assertEquals("z", 0, z.getMin());
                    assertTrue("a is bound", a.isBound());
                    assertEquals("b min", 0, b.getMin());
                    assertEquals("b max", 4, b.getMax());
                }
                else if (solNum<10) {
                    assertTrue("x is bound", x.isBound());
                    assertEquals("x", 1, x.getMin());
                    assertTrue("y is bound", y.isBound());
                    assertEquals("y", 1, y.getMin());
                    assertTrue("z is bound", z.isBound());
                    assertEquals("z", 2, z.getMin());
                    assertEquals("a min", 0, a.getMin());
                    assertEquals("a max", 4, a.getMax());
                    assertTrue("b is bound", b.isBound());
                }
                else {
                	fail("too many solutions");
                }
                
                switch (solNum) {
                    case 0:
                        assertEquals("a", 0, a.getMin());
                        break;
                    case 1:
                        assertEquals("a", 1, a.getMin());
                        break;
                    case 2:
                        assertEquals("a", 2, a.getMin());
                        break;
                    case 3:
                        assertEquals("a", 3, a.getMin());
                        break;
                    case 4:
                        assertEquals("a", 4, a.getMin());
                        break;
                    case 5:
                        assertEquals("b", 0, b.getMin());
                        break;
                    case 6:
                        assertEquals("b", 1, b.getMin());
                        break;
                    case 7:
                        assertEquals("b", 2, b.getMin());
                        break;
                    case 8:
                        assertEquals("b", 3, b.getMin());
                        break;
                    case 9:
                        assertEquals("b", 4, b.getMin());
                        break;
                }
                solNum++;
            }
            
            assertEquals("num solutions", 10, solNum);
    }
    
	private void doTestStore(TreeSearch search) {
        // no solution should exist
        IntSolution xSol = xyzSol.getSolution(x);
        IntSolution ySol = xyzSol.getSolution(y);
        IntSolution zSol = xyzSol.getSolution(z);
        assertFalse("x is bound", xSol.isBound());
        assertEquals("x min 0", 0, xSol.getMin());
        assertEquals("x max 2", 2, xSol.getMax());
        assertFalse("y is bound", ySol.isBound());
        assertEquals("y min 0", 0, ySol.getMin());
        assertEquals("y max 2", 2, ySol.getMax());
        assertFalse("z is bound", zSol.isBound());
        assertEquals("z min 0", 0, zSol.getMin());
        assertEquals("z max 2", 2, zSol.getMax());
        
        int solNum = 0;
        while (search.nextSolution()) {
            switch (solNum++) {
                case 0:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 0, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 0, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 0, zSol.getValue());
                    break;
                    
                case 1:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 0, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 1, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 1, zSol.getValue());
                    break;
                    
                case 2:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 0, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 2, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 2, zSol.getValue());
                    break;
                    
                case 3:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 1, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 0, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 1, zSol.getValue());
                    break;
                    
                case 4:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 1, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 1, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 2, zSol.getValue());
                    break;
                    
                case 5:
                    assertTrue("x is bound", xSol.isBound());
                    assertEquals("x", 2, xSol.getValue());
                    assertTrue("y is bound", ySol.isBound());
                    assertEquals("y", 0, ySol.getValue());
                    assertTrue("z is bound", zSol.isBound());
                    assertEquals("z", 2, zSol.getValue());
                    break;
                    
                default:
                    fail();
            }
        }
	}
	
	public void setUp() {
		try {
			alg = SolverImpl.createDefaultAlgorithm();
			store = new ConstraintStore(alg);
			
			x = alg.getVarFactory().intVar("x", 0, 2);
			y = alg.getVarFactory().intVar("y", 0, 2);
			z = alg.getVarFactory().intVar("z", 0, 2);
			store.addConstraint(x.add(y).eq(z));

            a = alg.getVarFactory().intVar("a", 0, 4);
            b = alg.getVarFactory().intVar("b", 0, 4);
            store.addConstraint(a.geq(0));
            store.addConstraint(b.geq(0));
            
            xyzScope = new SolutionScope();
            xyzScope.add(x);
            xyzScope.add(y);
            xyzScope.add(z);

            xyzSol = new SolverSolution(xyzScope);
            
            genX = new GenerateIntegerAction(new CspIntVariable[]{x});
            genY = new GenerateIntegerAction(new CspIntVariable[]{y});
            storeXyzSol = new StoreSolutionAction(store, xyzSol);
            
            List allActions = new LinkedList();
            allActions.add(genX);
            allActions.add(genY);
            allActions.add(storeXyzSol);
            
            combinedXyzActions = new CombinedAction(allActions);
        }
		catch(PropagationFailureException propx) {
			fail();
		}
	}
	
	public void tearDown() {
		alg = null;
		store = null;
		
		x = null;
		y = null;
		z = null;
	    a = null;
	    b = null;
	    
	    xyzScope = null;
	    xyzSol = null;

	    genX = null;
	    genY = null;
	    storeXyzSol = null;
	    combinedXyzActions = null;
	}
}
