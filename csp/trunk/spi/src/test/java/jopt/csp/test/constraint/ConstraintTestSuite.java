package jopt.csp.test.constraint;

import jopt.csp.test.constraint.fragments.FragmentTestSuite;
import jopt.csp.test.constraint.generics.GenericTestSuite;
import jopt.csp.test.constraint.global.GlobalTestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConstraintTestSuite extends TestCase {
    
    public ConstraintTestSuite(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        // jopt.csp.test.constraint
        suite.addTestSuite(CombinationConstraintTest.class);
        suite.addTestSuite(IntDiffConstraintTest.class);
        suite.addTestSuite(DoubleDiffConstraintTest.class);
        suite.addTestSuite(IntEqIntersectionTest.class);
        suite.addTestSuite(IntEqPartitionTest.class);
        suite.addTestSuite(IntEqUnionTest.class);
        suite.addTestSuite(IntNullIntersectionTest.class);
        suite.addTestSuite(IntSummationTest.class);
        suite.addTestSuite(DoubleSummationTest.class);
        suite.addTestSuite(NumRelationConstraintTest.class);
        suite.addTestSuite(Preceding1SquareTest.class);
        suite.addTestSuite(ProdIntConstraintTest.class);
        suite.addTestSuite(ProdDoubleConstraintTest.class);
        suite.addTestSuite(QuotConstraintTest.class);
        suite.addTestSuite(SquareConstraintTest.class);
        suite.addTestSuite(IntSumConstraintTest.class);
        suite.addTestSuite(DoubleSumConstraintTest.class);

        // jopt.csp.test.constraint.generic
        suite.addTest(GenericTestSuite.suite());
        
        // jopt.csp.test.constraint.fragments
        suite.addTest(FragmentTestSuite.suite());
        
        suite.addTest(GlobalTestSuite.suite());
        
        return suite;
    }
}
