package jopt.csp.test.solution;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SolutionTestSuite extends TestCase {
    
    public SolutionTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(SavedSolutionRestoreTest.class);
        
        return suite;
    }
}
