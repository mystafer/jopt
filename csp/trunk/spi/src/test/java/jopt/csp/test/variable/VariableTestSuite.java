package jopt.csp.test.variable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class VariableTestSuite extends TestCase {
    
    public VariableTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(GenericDoubleTest.class);
        suite.addTestSuite(GenericFragmentTest.class);
        suite.addTestSuite(GenericIntTest.class);
        suite.addTestSuite(GenericNumConstantTest.class);
        
        
        return suite;
    }
}
