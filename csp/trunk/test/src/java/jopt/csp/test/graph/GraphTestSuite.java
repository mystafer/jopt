package jopt.csp.test.graph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test Suite for testing graph-related material (nodes/arcs/etc)
 *
 * @author nick
 */
public class GraphTestSuite extends TestCase {
    
    public GraphTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(GenericDiffSeperateIdxTest.class);
        suite.addTestSuite(GenericDiffSharedIdxTest.class);
        suite.addTestSuite(GenericSumComboIdxTest.class);
        suite.addTestSuite(GenericSumSeperateIdxTest.class);
        suite.addTestSuite(GenericSumSharedIdxTest.class);
        suite.addTestSuite(GenericSumSourceControlTest.class);
        suite.addTestSuite(TernaryNumSumArcTest.class);
        return suite;
    }
}
