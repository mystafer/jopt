package jopt.csp.test.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SearchTestSuite extends TestCase {
    
    public SearchTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(BreadthFirstSearchTest.class);
        suite.addTestSuite(DepthFirstSearchTest.class);
        suite.addTestSuite(LocalSearchTest.class);
        suite.addTestSuite(LookAheadSearchActionTest.class);
        suite.addTestSuite(MetaheuristicTest.class);
        suite.addTestSuite(NonBinaryBreadthFirstSearchTest.class);
        suite.addTestSuite(NonBinaryDepthFirstSearchTest.class);
        suite.addTestSuite(SearchGoalTest.class);
        suite.addTestSuite(SearchRealTest.class);
        suite.addTestSuite(SetSearchTest.class);
        suite.addTestSuite(SolutionStoreTest.class);
        suite.addTestSuite(SplitBreadthFirstSearchTest.class);
        suite.addTestSuite(SplitDepthFirstSearchTest.class);
        suite.addTestSuite(VariableSelectorTest.class);
        
        return suite;
    }
}
