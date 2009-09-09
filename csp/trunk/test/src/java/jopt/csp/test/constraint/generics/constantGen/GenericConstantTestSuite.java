package jopt.csp.test.constraint.generics.constantGen;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing Generic Constraints
 *
 * @author Chris Johnson
 */
public class GenericConstantTestSuite extends TestCase {
    
    public GenericConstantTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ConstantGenericNumRelationConstraintTest.class);
        suite.addTestSuite(ConstantGenericSumConstraintTest.class);
        suite.addTestSuite(ConstantGenericSummationConstraintTest.class);
        suite.addTestSuite(GenericConstantTest.class);
        
        return suite;
    }
}
