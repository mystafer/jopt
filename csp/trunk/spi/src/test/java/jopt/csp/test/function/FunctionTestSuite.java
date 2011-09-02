package jopt.csp.test.function;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing function-related material (liner/piecewise/etc)
 * 
 * @author cjohnson
 */
public class FunctionTestSuite extends TestCase {
    
    public FunctionTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(PiecewiseStepFunctionTest.class);
        suite.addTestSuite(PiecewiseLinearFunctionTest.class);
        return suite;
    }
}
