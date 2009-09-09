package jopt.csp.test.bool.generics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Generic Bool Constraints
 */
public class GenericBoolTestSuite extends TestCase {
    
    public GenericBoolTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(GenericAndConstraintTest.class);
        suite.addTestSuite(GenericBoolExprImplicationTest.class);
        suite.addTestSuite(GenericConstraintToGenericBooleanTest.class);
        suite.addTestSuite(GenericEqThreeVarConstraintTest.class);
        suite.addTestSuite(GenericEqTwoVarConstraintTest.class);
        suite.addTestSuite(GenericImpliesThreeVarConstraintTest.class);
        suite.addTestSuite(GenericImpliesTwoVarConstraintTest.class);
        suite.addTestSuite(GenericOrConstraintTest.class);
        suite.addTestSuite(GenericXorConstraintTest.class);
        return suite;
    }
    
    public static void main(String args[]) {
        Test test = suite();
        TestResult result = new TestResult();
        test.run(result);
    }
}
