package jopt.js.test.domain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DomainTestSuite extends TestCase {
    
    public DomainTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(ActivityDomainTest.class);
        suite.addTestSuite(ActivityTest.class);
        suite.addTestSuite(ActResourceDomainTest.class);
        suite.addTestSuite(OperationAssociationTest.class);
        suite.addTestSuite(ResourceDomainTest.class);
        suite.addTestSuite(UnaryResourceDomainTest.class);
        return suite;
    }
}
