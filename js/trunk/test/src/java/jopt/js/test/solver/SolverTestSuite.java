package jopt.js.test.solver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SolverTestSuite extends TestCase {
    
    public SolverTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(GenerateResourceTest.class);
        suite.addTestSuite(GenerateStartTimeTest.class);
        suite.addTestSuite(GenerateStartTimeThenResourceTest.class);
        return suite;
    }
}
