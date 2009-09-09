/*
 * IntEqIntersectionTest.java
 * JUnit based test
 *
 * Created on May 15, 2004, 9:58 AM
 */
package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.set.EqIntersection;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests EqIntersection constraint for Integer Set variables
 *
 * @author nick
 */
public class IntEqIntersectionTest extends TestCase {
    private ChoicePointAlgorithm gac;
    private IntSetVariable x;
    private IntSetVariable y;
    private IntSetVariable z;

    public IntEqIntersectionTest(String testName) {
        super(testName);
    }

    /**
     * Called to initialize the test
     */
    protected void setUp() {
        gac = SolverImpl.createDefaultAlgorithm();
        x = new IntSetVariable("x", 1, 10);
        y = new IntSetVariable("y", 1, 10);
        z = new IntSetVariable("z", 1, 10);
    }
    
    public void tearDown() {
        gac = null;
        x = null;
        y = null;
        z = null;
    }

    /**
     * Tests intersection, propagation should succeed
     */
    public void testIntersectScenario1() {
        try {
            initVarsScenario1();
            gac.addConstraint(new EqIntersection(x, y, z));
            gac.propagate();

            // Check intersect domain is correct
            assertEquals("z domain possible cardinality", 8, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 2, z.getRequiredCardinality());
            assertTrue("z requires 3", z.isRequired(3));
            assertTrue("z requires 5", z.isRequired(5));
            for (int i=1; i<=8; i++)
                assertTrue("z possible " + i, z.isPossible(i));
            assertFalse("z not possible 9", z.isPossible(9));
            assertFalse("z not possible 10", z.isPossible(10));

            // Check x domain is correct
            assertEquals("x domain possible cardinality", 10, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 2, x.getRequiredCardinality());
            assertTrue("x requires 3", x.isRequired(3));
            assertTrue("x requires 5", x.isRequired(5));
            for (int i=1; i<=10; i++)
                assertTrue("x possible " + i, x.isPossible(i));

            // Check y domain is correct
            assertEquals("y domain possible cardinality", 9, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 4, y.getRequiredCardinality());
            assertTrue("y requires 2", y.isRequired(2));
            assertTrue("y requires 3", y.isRequired(3));
            assertTrue("y requires 4", y.isRequired(4));
            assertTrue("y requires 5", y.isRequired(5));
            for (int i=1; i<=8; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            assertFalse("y possible 9", y.isPossible(9));
            assertTrue("y possible 10", y.isPossible(10));
         }
         catch(PropagationFailureException propx) {
         	propx.printStackTrace();
         	fail();
         }
    }

    /**
     * Tests intersection, propagation should fail
     */
    public void testIntersectScenario2() {
        try {
            initVarsScenario2();
            gac.addConstraint(new EqIntersection(x, y, z));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Initializes variables for third union scenario
     */
     private void initVarsScenario2() {
        try {
            initVarsScenario1();
            x.removePossible(3);
        }
         catch(PropagationFailureException propx) {
             fail();
         }
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

            assertEquals("z domain size", 11, z.getSize());
            z.addRequired(3);
            z.addRequired(5);
            z.removePossible(10);
            assertEquals("z domain size", 8, z.getSize());
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 2, z.getRequiredCardinality());
         }
         catch(PropagationFailureException propx) {
             fail();
         }
     }
}
