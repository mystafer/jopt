package jopt.csp.test.trig;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TrigTestSuite extends TestCase {
    
    public TrigTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(CosConstraintTest.class);
        suite.addTestSuite(ExpConstraintTest.class);
        suite.addTestSuite(NatLogConstraintTest.class);
        suite.addTestSuite(PowerConstraintTest.class);
        suite.addTestSuite(SinConstraintTest.class);
        suite.addTestSuite(TanConstraintTest.class);
        
        return suite;
    }
}
