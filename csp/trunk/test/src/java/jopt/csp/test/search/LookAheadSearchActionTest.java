package jopt.csp.test.search;

import jopt.csp.search.Search;
import jopt.csp.search.SearchAction;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.actions.AssignIntegerAction;
import jopt.csp.spi.search.actions.GenerateIntegerAction;
import jopt.csp.spi.search.actions.LookAheadAction;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.ChoicePoint;
import jopt.csp.spi.search.tree.CombinedAction;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Basic test of the look-ahead search action
 * utilizing the default algorithms and search structures
 */
public class LookAheadSearchActionTest extends TestCase {
    private ConstraintStore store;
    private CspVariableFactory varFactory;
    private CspIntVariable x;
    private CspIntVariable y;
    private CspIntVariable z;
    private SearchAction successAction;
    private SearchAction failureAction;
    
    public void setUp() {
        try {
            store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
            varFactory = store.getConstraintAlg().getVarFactory();
            x = varFactory.intVar("x", 0, 2); 
            y = varFactory.intVar("y", 0, 2); 
            z = varFactory.intVar("z", 0, 4);
            store.addConstraint(x.add(y).eq(z));
            successAction = new GenerateIntegerAction(new CspIntVariable[]{x, y, z});
            failureAction = new GenerateIntegerAction(new CspIntVariable[]{y, x, z});
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
        successAction = null;
        failureAction = null;
    }

    public void testLookAheadSearchActionSuccess() {
        SearchAction pseudoChange = new AssignIntegerAction(x, 2);
        SearchAction lookAhead = new LookAheadAction(store, pseudoChange, successAction, failureAction);
        Search search = new TreeSearch(store, lookAhead);
        doTestSearch(search, true);
    }
    
    public void testLookAheadSearchActionFailure() {
        SearchAction pseudoChange = new AssignIntegerAction(x, 3);
        SearchAction lookAhead = new LookAheadAction(store, pseudoChange, successAction, failureAction);
        Search search = new TreeSearch(store, lookAhead);
        doTestSearch(search, false);
    }
    
    public void testLookAheadSearchActionWithNull() {
        SearchAction pseudoChange = new AssignIntegerAction(x, 2);
        SearchAction lookAhead = new LookAheadAction(store, pseudoChange, null, failureAction);
        Search search = new TreeSearch(store, lookAhead);
        search.nextSolution();
        // essentially, nothing should have changed
        assertEquals("x min", 0, x.getMin());
        assertEquals("x max", 2, x.getMax());
        assertEquals("y min", 0, y.getMin());
        assertEquals("y max", 2, y.getMax());
        assertEquals("z min", 0, z.getMin());
        assertEquals("z max", 4, z.getMax());
    }
    
    public void testEmbeddedLookAheadSearchAction() {
        SearchAction assignXto0 = new AssignIntegerAction(x, 0);
        SearchAction assignXto1 = new AssignIntegerAction(x, 1);
        
        successAction = new GenerateIntegerAction(new CspIntVariable[]{y, z});
        failureAction = new GenerateIntegerAction(new CspIntVariable[]{z, y});
        SearchAction pseudoChange = new AssignIntegerAction(x, 0);
        SearchAction lookAhead = new LookAheadAction(store, pseudoChange, successAction, failureAction);
        
        SearchAction leftMove = new CombinedAction(assignXto0, lookAhead);
        SearchAction rightMove = new CombinedAction(assignXto1, lookAhead);
        
        ChoicePoint cp = new ChoicePoint(leftMove, rightMove);
        Search search = new TreeSearch(store, cp);
        
        if (!search.nextSolution()) fail("did not locate first solution");
        for (int i=0; i<5; i++) {
//            dumpSolution(i);
            assertSolution(i, true);
            if (!search.nextSolution()) fail("did not locate solution: " + (i+1));
        }
//        dumpSolution(5);
        assertSolution(5, true);
        if (search.nextSolution()) fail("located more than 6 solutions");
    }
    
    private void doTestSearch(Search search, boolean checkSuccess) {
        if (!search.nextSolution()) fail("did not locate first solution");
        for (int i=0; i<8; i++) {
//            dumpSolution(i);
            assertSolution(i, checkSuccess);
            if (!search.nextSolution()) fail("did not locate solution: " + (i+1));
        }
        
//        dumpSolution(8);
        assertSolution(8, checkSuccess);
        if (search.nextSolution()) fail("located more than 9 solutions");
    }

    private void assertSolution(int solutionNum, boolean checkSuccess) {
        int xval = 0;
        int yval = 0;
        int zval = 0;
        
        // Solution ordering:
        // success
        // 0 1 2 3 4 5 6 7 8
        // failure
        // 0 3 6 1 4 7 2 5 8 
        
        if (!checkSuccess) {
            // alter the solution num as necessary
            switch(solutionNum / 3) {
                case 0:
                    solutionNum *= 3;
                    break;
                case 1:
                    solutionNum = ((solutionNum % 3) * 3) + 1;
                    break;
                case 2:
                    solutionNum = ((solutionNum % 3) * 3) + 2;
                    break;
            }
        }
        
    	switch(solutionNum) {
            case 0:
                xval = 0;
                yval = 0;
                zval = 0;
                break;
                
            case 1:
                xval=0;
                yval=1;
                zval=1;
                break;
                
            case 2:
                xval=0;
                yval=2;
                zval=2;
                break;
                
            case 3:
                xval=1;
                yval=0;
                zval=1;
                break;
                
            case 4:
                xval=1;
                yval=1;
                zval=2;
                break;
                
            case 5:
                xval=1;
                yval=2;
                zval=3;
                break;
                
            case 6:
                xval=2;
                yval=0;
                zval=2;
                break;
                
            case 7:
                xval=2;
                yval=1;
                zval=3;
                break;
                
            case 8:
                xval=2;
                yval=2;
                zval=4;
                break;
        }

        assertEquals("solution[" + solutionNum + "]: x min", xval, x.getMin());
        assertEquals("solution[" + solutionNum + "]: x max", xval, x.getMax());
        assertEquals("solution[" + solutionNum + "]: y min", yval, y.getMin());
        assertEquals("solution[" + solutionNum + "]: y max", yval, y.getMax());
        assertEquals("solution[" + solutionNum + "]: z min", zval, z.getMin());
        assertEquals("solution[" + solutionNum + "]: z max", zval, z.getMax());
    }
    
    /*private void dumpSolution(int solNum) {
        System.out.println(solNum + ": ============");
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
    }*/
}
