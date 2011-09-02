/*
 * JUnit based test
 *
 * Created on May 15, 2004, 6:06 PM
 */

package jopt.csp.test.constraint.global;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Generic Constraints
 *
 * @author jb
 */
public class GlobalTestSuite extends TestCase {
    
    public GlobalTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
//        suite.addTestSuite(AllDiffTest.class);
        suite.addTestSuite(GlobalCardinalityConstraintTest.class);
        suite.addTestSuite(NumAllDifferentConstraintTest.class);
        suite.addTestSuite(GlobalCardinalityCountConstraintTest.class);
        
        return suite;
    }
}
