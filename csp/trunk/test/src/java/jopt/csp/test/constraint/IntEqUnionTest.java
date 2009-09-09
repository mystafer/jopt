/*
 * IntEqUnionTest.java
 * JUnit based test
 *
 * Created on May 15, 2004, 9:58 AM
 */
package jopt.csp.test.constraint;

import java.util.Set;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.set.EqIntersection;
import jopt.csp.spi.arcalgorithm.constraint.set.EqUnion;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests EqUnion constraint for Integer Set variables
 *
 * @author nick
 */
public class IntEqUnionTest extends TestCase {
    private ChoicePointAlgorithm gac;
    private IntSetVariable x;
    private IntSetVariable y;
    private IntSetVariable z;
    private IntSetVariable intersect;

    public IntEqUnionTest(String testName) {
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
        intersect = new IntSetVariable("intersect", 1, 10);
    }
    
    public void tearDown() {
        gac = null;
        x = null;
        y = null;
        z = null;
        intersect = null;
    }

    /**
     * Tests union without intersection, propagation should succeed
     */
    public void testUnionScenario1() {
        try {
            initVarsScenario1();
            gac.addConstraint(new EqUnion(x, y, z));
            gac.propagate();

            // Check union domain is correct
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 4, z.getRequiredCardinality());
            assertTrue("z requires 2", z.isRequired(2));
            assertTrue("z requires 3", z.isRequired(3));
            assertTrue("z requires 4", z.isRequired(4));
            assertTrue("z requires 5", z.isRequired(5));
            for (int i=1; i<10; i++)
                assertTrue("z possible " + i, z.isPossible(i));
            assertFalse("z not possible 10", z.isPossible(10));

            // Retrieve required set of all variables
            Set xRequired = x.getRequiredSet();
            Set yRequired = y.getRequiredSet();
            Set zRequired = z.getRequiredSet();

            // assert x required is subset of z required
            assertTrue("x subset of z", zRequired.containsAll(xRequired));

            // assert y required is subset of z required
            assertTrue("y subset of z", zRequired.containsAll(yRequired));
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }

    /**
     * Tests union with intersection to further reduce x and y
     * propagation should succeed
     */
    public void testUnionWithIntersectScenario1() {
        try {
            initVarsScenario1();
            gac.addConstraint(new EqIntersection(x, y, intersect));
            gac.addConstraint(new EqUnion(x, y, z, intersect));
            gac.propagate();
            
            // Check union domain is correct
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 4, z.getRequiredCardinality());
            assertTrue("z requires 2", z.isRequired(2));
            assertTrue("z requires 3", z.isRequired(3));
            assertTrue("z requires 4", z.isRequired(4));
            assertTrue("z requires 5", z.isRequired(5));
            for (int i=1; i<=9; i++)
                assertTrue("z possible " + i, z.isPossible(i));
            assertFalse("z not possible 10", z.isPossible(10));

            // Check x domain is correct
            assertEquals("x domain possible cardinality", 8, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 1, x.getRequiredCardinality());
            assertTrue("x requires 5", x.isRequired(5));
            for (int i=1; i<=2; i++)
                assertTrue("x possible " + i, x.isPossible(i));
            assertFalse("x not possible 3", x.isPossible(3));
            for (int i=4; i<=9; i++)
                assertTrue("x possible " + i, x.isPossible(i));
            assertFalse("x not possible 10", x.isPossible(10));

            // Check y domain is correct
            assertEquals("y domain possible cardinality", 8, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 3, y.getRequiredCardinality());
            assertTrue("y requires 2", y.isRequired(2));
            assertTrue("y requires 3", y.isRequired(3));
            assertTrue("y requires 4", y.isRequired(4));
            for (int i=1; i<=8; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            assertFalse("y not possible 9", y.isPossible(9));
            assertFalse("y not possible 10", y.isPossible(10));
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }

    /**
     * Tests union without intersection, propagation should fail
     */
    public void testUnionScenario2() {
        try {
            initVarsScenario2();
            gac.addConstraint(new EqUnion(x, y, z));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests union with intersection to further reduce x and y
     * propagation should fail
     */
    public void testUnionWithIntersectScenario2() {
        try {
            initVarsScenario2();
            gac.addConstraint(new EqIntersection(x, y, intersect));
            gac.addConstraint(new EqUnion(x, y, z, intersect));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests union without intersection
     * Propagation should fail but it might pass since the requirement in the
     * union will not change the X and Y.  Ensure that X and Y are still
     * subsets of the union.
     */
    public void testUnionScenario3() {
        try {
            initVarsScenario3();
            gac.addConstraint(new EqUnion(x, y, z));

            // propagation might succeed
            gac.propagate();

            // Retrieve required set of all variables
            Set xRequired = x.getRequiredSet();
            Set yRequired = y.getRequiredSet();
            Set zRequired = z.getRequiredSet();

            // assert x required is subset of z required
            assertTrue("x subset of z", zRequired.containsAll(xRequired));

            // assert y required is subset of z required
            assertTrue("y subset of z", zRequired.containsAll(yRequired));
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests union with intersection to further reduce x and y
     * Same as previous scenario buy should fail with propagation since
     * union requirement should update X and Y variables
     */
    public void testUnionWithIntersectScenario3() {
        try {
            initVarsScenario3();
            gac.addConstraint(new EqIntersection(x, y, intersect));
            gac.addConstraint(new EqUnion(x, y, z, intersect));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Initializes variables for third union scenario
     */
     private void initVarsScenario3() {
        try {
            initVarsScenario1();
            x.removePossible(3);
            y.removePossible(3);
            z.addRequired(3);
        }
         catch(PropagationFailureException propx) {
             fail();
         }
     }

    /**
     * Initializes variables for third union scenario
     */
     private void initVarsScenario2() {
        try {
            initVarsScenario1();
            y.addRequired(2);
            z.removePossible(2);
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
            x.removePossible(3);
            assertEquals("x domain size", 9, x.getSize());
            assertEquals("x domain possible cardinality", 9, x.getPossibleCardinality());
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

            assertEquals("intersect domain size", 11, intersect.getSize());
            assertEquals("intersect domain possible cardinality", 10, intersect.getPossibleCardinality());
            assertEquals("intersect domain required cardinality", 0,  intersect.getRequiredCardinality());
         }
         catch(PropagationFailureException propx) {
             fail();
         }
     }
}
