package jopt.csp.test.choicepoint;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ChoicePointTestSuite extends TestCase {
    
    public ChoicePointTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        // jopt.csp.test.constraint
        suite.addTestSuite(ChoicePointPushMapTest.class);
        suite.addTestSuite(ChoicePointWithConstraintsTest.class);
        
        return suite;
    }
}
