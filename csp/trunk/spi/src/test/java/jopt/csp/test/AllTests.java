package jopt.csp.test;

import jopt.csp.test.arcalgorithm.util.ArcAlgorithmUtilTestSuite;
import jopt.csp.test.bool.BooleanTestSuite;
import jopt.csp.test.choicepoint.ChoicePointTestSuite;
import jopt.csp.test.constraint.ConstraintTestSuite;
import jopt.csp.test.function.FunctionTestSuite;
import jopt.csp.test.graph.GraphTestSuite;
import jopt.csp.test.search.SearchTestSuite;
import jopt.csp.test.solution.SolutionTestSuite;
import jopt.csp.test.trig.TrigTestSuite;
import jopt.csp.test.util.UtilTestSuite;
import jopt.csp.test.variable.VariableTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Suite containing all JOpt CSP tests
 */
public class AllTests extends TestCase {
    
    public AllTests(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        // jopt.csp.test.bool
        suite.addTest(BooleanTestSuite.suite());
        
        // jopt.csp.test.choicepoint
        suite.addTest(ChoicePointTestSuite.suite());
        
        // jopt.csp.test.constraint
        suite.addTest(ConstraintTestSuite.suite());
        
        // jopt.csp.test.function
        suite.addTest(FunctionTestSuite.suite());
        
        // jopt.csp.test.graph
        suite.addTest(GraphTestSuite.suite());

        // jopt.csp.test.search
        suite.addTest(SearchTestSuite.suite());
        
        // jopt.csp.test.solution
        suite.addTest(SolutionTestSuite.suite());
        
        // jopt.csp.test.trig
        suite.addTest(TrigTestSuite.suite());
        
        // jopt.csp.test.util
        suite.addTest(UtilTestSuite.suite());
        
        // jopt.csp.test.variable
        suite.addTest(VariableTestSuite.suite());
        
        // jopt.csp.test.arcalgorithm.util
        suite.addTest(ArcAlgorithmUtilTestSuite.suite());
                
        return suite;
    }
    
    public static void main(String args[]) {
        Test test = suite();
        TestResult result = new TestResult();
        test.run(result);
    }
}
