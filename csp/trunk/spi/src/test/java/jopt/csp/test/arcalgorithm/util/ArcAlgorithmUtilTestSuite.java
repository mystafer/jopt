package jopt.csp.test.arcalgorithm.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing arc algorithm utils
 *
 * @author nick
 */
public class ArcAlgorithmUtilTestSuite extends TestCase {
    
    public ArcAlgorithmUtilTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ArcQueueTest.class);
        
        return suite;
    }
}
