package jopt.js.test.constraint;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConstraintTestSuite extends TestCase {
    
    public ConstraintTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(ForwardCheckConstraintTest.class);
        suite.addTestSuite(ForwardCheckConstraintWithUnaryResourcesTest.class);
        
        return suite;
    }
}
