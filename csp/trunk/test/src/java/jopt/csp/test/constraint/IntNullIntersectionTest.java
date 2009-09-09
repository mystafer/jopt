/*
 * IntNullIntersectionTest.java
 * JUnit based test
 *
 * Created on May 15, 2004, 9:58 AM
 */
package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.set.NullIntersection;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests NullIntersection constraint for Integer Set variables
 *
 * @author nick
 */
public class IntNullIntersectionTest extends TestCase {
    private ChoicePointAlgorithm gac;
    private IntSetVariable x;
    private IntSetVariable y;

    public IntNullIntersectionTest(String testName) {
        super(testName);
    }

    /**
     * Called to initialize the test
     */
    protected void setUp() {
        gac = SolverImpl.createDefaultAlgorithm();
        x = new IntSetVariable("x", 1, 10);
        y = new IntSetVariable("y", 1, 10);
    }
    
    protected void tearDown() {
        gac = null;
        x = null;
        y = null;
    }
    
    /**
     * Tests intersection, propagation should succeed
     */
    public void testIntersectScenario1() {
        try {
            initVarsScenario1();
            gac.addConstraint(new NullIntersection(x, y));
            gac.propagate();

            // Check x domain is correct
            assertEquals("x domain possible cardinality", 8, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 1, x.getRequiredCardinality());
            assertTrue("x requires 5", x.isRequired(5));
            assertTrue("x possible 1", x.isPossible(1));
            assertFalse("x possible 2", x.isPossible(2));
            assertTrue("x possible 3", x.isPossible(3));
            assertFalse("x possible 4", x.isPossible(4));
            for (int i=5; i<=10; i++)
                assertTrue("x possible " + i, x.isPossible(i));

            // Check y domain is correct
            assertEquals("y domain possible cardinality", 8, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 2, y.getRequiredCardinality());
            assertTrue("y requires 2", y.isRequired(2));
            assertTrue("y requires 4", y.isRequired(4));
            for (int i=1; i<=4; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            assertFalse("y possible 5", y.isPossible(5));
            for (int i=6; i<=8; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            assertFalse("y possible 9", y.isPossible(9));
            assertTrue("y possible 10", y.isPossible(10));

            x.addRequired(3);
            x.addRequired(5);
            x.addRequired(6);
            gac.propagate();
            assertFalse("y possible 3", y.isPossible(3));
            assertFalse("y possible 5", y.isPossible(5));
            assertFalse("y possible 6", y.isPossible(6));

            y.addRequired(7);
            y.addRequired(8);
            y.addRequired(10);
            gac.propagate();
            assertFalse("y possible 7", x.isPossible(7));
            assertFalse("y possible 8", x.isPossible(8));
            assertFalse("y possible 10", x.isPossible(10));
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }

    /**
     * Tests intersection, propagation should fail
     */
    public void testIntersectScenario2() {
        try {
            // Intialize domains for X & Y
            initVarsScenario1();

            // Add constraint that should cause failure
            gac.addConstraint(new NullIntersection(x, y));
            x.addRequired(7);
            y.addRequired(7);

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Initializes variables for first union scenario
     */
     private void initVarsScenario1() {
         try {
            // not: size = possible card - required card + 1
            assertEquals("x domain size", 11, x.getSize());
            x.addRequired(5);
            assertEquals("x domain size", 10, x.getSize());
            assertEquals("x domain possible cardinality", 10, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 1, x.getRequiredCardinality());

            assertEquals("y domain size", 11, y.getSize());
            y.addRequired(2);
            y.addRequired(4);
            y.removePossible(9);
            assertEquals("y domain size", 8, y.getSize());
            assertEquals("y domain possible cardinality", 9, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 2, y.getRequiredCardinality());
         }
         catch(PropagationFailureException propx) {
             fail();
         }
     }
}
