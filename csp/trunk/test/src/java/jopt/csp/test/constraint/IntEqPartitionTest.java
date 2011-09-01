/*
 * IntEqPartitionTest.java
 * JUnit based test
 *
 * Created on May 15, 2004, 9:58 AM
 */
package jopt.csp.test.constraint;

import java.util.Iterator;
import java.util.Set;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.set.EqPartition;
import jopt.csp.spi.arcalgorithm.variable.IntSetVariable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;


/**
 * Tests EqUnion constraint for Integer Set variables
 *
 * @author nick
 */
public class IntEqPartitionTest extends TestCase {
    private ChoicePointAlgorithm gac;
    private IntSetVariable x;
    private IntSetVariable y;
    private IntSetVariable z;
    private IntSetVariable intersect;

    public IntEqPartitionTest(String testName) {
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
     * Tests partition without intersection, propagation should succeed
     */
    public void testPartitionScenario1() {
        try {
            initVarsScenario1();
            
            gac.addConstraint(new EqPartition<Integer>(x, y, z));
            gac.propagate();
            
            // Check union domain is correct
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 5, z.getRequiredCardinality());
            assertTrue("z requires 2", z.isRequired(1));
            assertTrue("z requires 2", z.isRequired(2));
            assertTrue("z requires 3", z.isRequired(3));
            assertTrue("z requires 4", z.isRequired(4));
            assertTrue("z requires 5", z.isRequired(5));
            for (int i=1; i<10; i++)
                assertTrue("z possible " + i, z.isPossible(i));
            assertFalse("z not possible 10", z.isPossible(10));

            // Retrieve required set of all variables
            Set<Integer> xRequired = x.getRequiredSet();
            Set<Integer> yRequired = y.getRequiredSet();
            Set<Integer> zRequired = z.getRequiredSet();

            // assert x required is subset of z required
            assertTrue("x subset of z", zRequired.containsAll(xRequired));

            // assert y required is subset of z required
            assertTrue("y subset of z", zRequired.containsAll(yRequired));

            // Retrieve possible sets for x and y
            Set<Integer> xPossible = x.getPossibleSet();
            Set<Integer> yPossible = y.getPossibleSet();

            // Ensure no required value in x possible in y
            Iterator<Integer> iterator = xRequired.iterator();
            while (iterator.hasNext()) {
                Object val = iterator.next();
                assertFalse("required x possible in y", yPossible.contains(val));
            }

            // Ensure no required value in y possible in x
            iterator = yRequired.iterator();
            while (iterator.hasNext()) {
                Object val = iterator.next();
                assertFalse("required y possible in x", xPossible.contains(val));
            }
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }

    /**
     * Tests partition with intersection to further reduce x and y
     * propagation should succeed
     */
    public void testPartitionWithIntersectScenario1() {
        try {
            initVarsScenario1();
            gac.addConstraint(new EqPartition<Integer>(x, y, z, true));
            gac.propagate();

            // Check union domain is correct
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 5, z.getRequiredCardinality());
            assertTrue("z requires 1", z.isRequired(1));
            assertTrue("z requires 2", z.isRequired(2));
            assertTrue("z requires 3", z.isRequired(3));
            assertTrue("z requires 4", z.isRequired(4));
            assertTrue("z requires 5", z.isRequired(5));
            for (int i=1; i<=9; i++)
                assertTrue("z possible " + i, z.isPossible(i));
            assertFalse("z not possible 10", z.isPossible(10));

            // Check x domain is correct
            assertEquals("x domain possible cardinality", 6, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 2, x.getRequiredCardinality());
            assertTrue("x requires 1", x.isRequired(1));
            assertTrue("x requires 5", x.isRequired(5));
            assertTrue("x possible 1", x.isPossible(1));
            for (int i=2; i<=4; i++)
                assertFalse("x not possible " + i, x.isPossible(i));
            for (int i=5; i<=9; i++)
                assertTrue("x possible " + i, x.isPossible(i));
            assertFalse("x not possible 10", x.isPossible(10));

            // Check y domain is correct
            assertEquals("y domain possible cardinality", 6, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 3, y.getRequiredCardinality());
            assertFalse("y not possible 1", y.isPossible(1));
            for (int i=2; i<=4; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            assertFalse("y not possible 5", y.isPossible(5));
            for (int i=6; i<=8; i++)
                assertTrue("y possible " + i, y.isPossible(i));
            for (int i=9; i<=10; i++)
                assertFalse("y not possible " + i, y.isPossible(i));
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }

    /**
     * Tests partition without intersection, propagation should fail
     */
    public void testPartitionScenario2() {
        try {
            initVarsScenario2();
            gac.addConstraint(new EqPartition<Integer>(x, y, z));

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
    public void testPartitionWithIntersectScenario2() {
        try {
            initVarsScenario2();
            gac.addConstraint(new EqPartition<Integer>(x, y, z, true));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests partition without intersection.
     * Propagation should fail but it might pass since the requirement in the
     * partition will not change the X and Y.  Ensure that X and Y are still
     * subsets of the partition.
     */
    public void testPartitionScenario3() {
        try {
            initVarsScenario3();
            gac.addConstraint(new EqPartition<Integer>(x, y, z));

            // propagation might succeed
            gac.propagate();

            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests union with intersection to further reduce x and y
     * Same as previous scenario buy should fail with propagation since
     * partition requirement should update X and Y variables
     */
    public void testPartitionWithIntersectScenario3() {
        try {
            initVarsScenario3();
            gac.addConstraint(new EqPartition<Integer>(x, y, z, true));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Tests partition without intersection, propagation should fail
     */
    public void testPartitionScenario4() {
        try {
            initVarsScenario4();
            gac.addConstraint(new EqPartition<Integer>(x, y, z));

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
    public void testPartitionWithIntersectScenario4() {
        try {
            initVarsScenario4();
            gac.addConstraint(new EqPartition<Integer>(x, y, z, true));

            // propagation should fail
            gac.propagate();
            fail("propagation should fail");
         }
         catch(PropagationFailureException propx) {}
    }

    /**
     * Initializes variables for forth partition scenario
     */
     private void initVarsScenario4() {
        try {
            initVarsScenario1();
            x.addRequired(1);
            y.addRequired(1);
        }
         catch(PropagationFailureException propx) {
             fail();
         }
     }

    /**
     * Initializes variables for third partition scenario
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
     * Initializes variables for third partition scenario
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
     * Initializes variables for first partition scenario
     */
     private void initVarsScenario1() {
         try {
            // not: size = possible card - required card + 1
            assertEquals("x domain size", 11, x.getSize());
            x.addRequired(1);
            x.addRequired(5);
            x.removePossible(3);
            assertEquals("x domain size", 8, x.getSize());
            assertEquals("x domain possible cardinality", 9, x.getPossibleCardinality());
            assertEquals("x domain required cardinality", 2, x.getRequiredCardinality());

            assertEquals("y domain size", 11, y.getSize());
            y.addRequired(2);
            y.addRequired(4);
            y.removePossible(9);
            assertEquals("y domain size", 8, y.getSize());
            assertEquals("y domain possible cardinality", 9, y.getPossibleCardinality());
            assertEquals("y domain required cardinality", 2, y.getRequiredCardinality());

            assertEquals("z domain size", 11, z.getSize());
            z.addRequired(3);
            z.removePossible(10);
            assertEquals("z domain size", 9, z.getSize());
            assertEquals("z domain possible cardinality", 9, z.getPossibleCardinality());
            assertEquals("z domain required cardinality", 1, z.getRequiredCardinality());

            assertEquals("intersect domain size", 11, intersect.getSize());
            assertEquals("intersect domain possible cardinality", 10, intersect.getPossibleCardinality());
            assertEquals("intersect domain required cardinality", 0,  intersect.getRequiredCardinality());
         }
         catch(PropagationFailureException propx) {
             fail();
         }
     }
}
