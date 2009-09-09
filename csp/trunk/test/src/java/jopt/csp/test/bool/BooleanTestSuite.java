package jopt.csp.test.bool;

import jopt.csp.test.bool.generics.GenericBoolTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Booleans
 *
 * @author nick
 */
public class BooleanTestSuite extends TestCase {
    
    public BooleanTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AndConstraintTest.class);
        suite.addTestSuite(BoolOppositeConstraintTest.class);
        suite.addTestSuite(BooleansWithConstraintsTest.class);
        suite.addTestSuite(ConstraintToBooleanTest.class);
        suite.addTestSuite(EqConstraintToBooleanNodeTest.class);
        suite.addTestSuite(EqThreeVarConstraintTest.class);
        suite.addTestSuite(EqTwoVarConstraintTest.class);
        suite.addTestSuite(ImpliesThreeVarConstraintTest.class);
        suite.addTestSuite(ImpliesTwoVarConstraintTest.class);
        suite.addTestSuite(NotConstraintTest.class);
        suite.addTestSuite(OrConstraintTest.class);
        suite.addTestSuite(XorConstraintTest.class);
        
        // jopt.csp.test.bool.generics
        suite.addTest(GenericBoolTestSuite.suite());
        
        return suite;
    }
}
