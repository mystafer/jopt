package jopt.csp.test.constraint.fragments;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Fragmented Constraints
 *
 * @author Chris Johnson
 */
public class FragmentTestSuite extends TestCase {
    
    public FragmentTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(SumConstraintFragmentTest.class);
        return suite;
    }
}
