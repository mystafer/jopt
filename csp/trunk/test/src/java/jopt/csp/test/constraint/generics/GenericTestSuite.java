/*
 * JUnit based test
 *
 * Created on May 15, 2004, 6:06 PM
 */

package jopt.csp.test.constraint.generics;

import jopt.csp.test.constraint.generics.constantGen.GenericConstantTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Generic Constraints
 *
 * @author jb
 */
public class GenericTestSuite extends TestCase {
    
    public GenericTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(GenericCombinationConstraintTest.class);
        suite.addTestSuite(GenericDiffConstraintTest.class);
        suite.addTestSuite(GenericIndexTest.class);
        suite.addTestSuite(GenericNumRelationConstraintTest.class);
        suite.addTestSuite(GenericProdConstraintTest.class);
        suite.addTestSuite(GenericQuotConstraintTest.class);
        suite.addTestSuite(GenericSquareConstraintTest.class);
        suite.addTestSuite(GenericSumConstraintTest.class);
        suite.addTestSuite(SummationConstraintTest.class);
        
        // jopt.csp.test.constraint.generic.constantGen
        suite.addTest(GenericConstantTestSuite.suite());
        
        return suite;
    }
}
