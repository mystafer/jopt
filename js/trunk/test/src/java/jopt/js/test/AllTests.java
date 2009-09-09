package jopt.js.test;

import jopt.js.test.arc.ArcTestSuite;
import jopt.js.test.constraint.ConstraintTestSuite;
import jopt.js.test.domain.DomainTestSuite;
import jopt.js.test.solver.SolverTestSuite;
import jopt.js.test.util.UtilTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Suite containing all JOpt JS tests
 */
public class AllTests extends TestCase {
    
    public AllTests(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(ArcTestSuite.suite());
        suite.addTest(ConstraintTestSuite.suite());
        suite.addTest(DomainTestSuite.suite());
        suite.addTest(SolverTestSuite.suite());
        suite.addTest(UtilTestSuite.suite());
        
        return suite;
    }
    
    public static void main(String args[]) {
        Test test = suite();
        TestResult result = new TestResult();
        test.run(result);
    }
}
