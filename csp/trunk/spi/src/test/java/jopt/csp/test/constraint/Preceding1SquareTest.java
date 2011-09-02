/*
 * Preceding1SquareTest.java
 * JUnit based test
 *
 * Created on May 15, 2004, 6:26 PM
 */
package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.domain.DoubleDomain;
import jopt.csp.spi.arcalgorithm.domain.FloatDomain;
import jopt.csp.spi.arcalgorithm.domain.IntDomain;
import jopt.csp.spi.arcalgorithm.domain.LongDomain;
import jopt.csp.spi.arcalgorithm.variable.DoubleVariable;
import jopt.csp.spi.arcalgorithm.variable.FloatVariable;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.arcalgorithm.variable.LongVariable;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;


/**
 * Tests numeric constraint using an eight digit number that
 * is a square and remains a square when 1 is concatenated
 * in front of its decimal notation
 *
 * @author nick
 */
public class Preceding1SquareTest extends TestCase {

    public Preceding1SquareTest(java.lang.String testName) {
        super(testName);
    }

    public void testInt() {
        try {
            ChoicePointAlgorithm gac = SolverImpl.createDefaultAlgorithm();
            IntVariable n = new IntVariable("n", 10000000, 99999999);
            IntVariable x = new IntVariable("x", 0, 20000);
            IntVariable y = new IntVariable("y", 0, 20000);

            gac.addConstraint(n.eq(x.multiply(x)));
            gac.addConstraint(n.add(100000000).eq(y.multiply(y)));
            gac.propagate();

            IntDomain ndomain = n.getIntDomain();
            IntDomain xdomain = x.getIntDomain();
            IntDomain ydomain = y.getIntDomain();

            assertEquals("n minimum", 23765625, ndomain.getMin());
            assertEquals("n maximum", 56250000, ndomain.getMax());
            assertEquals("x minimum", 4875, xdomain.getMin());
            assertEquals("x maximum", 7500, xdomain.getMax());
            assertEquals("y minimum", 11125, ydomain.getMin());
            assertEquals("y maximum", 12500, ydomain.getMax());
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace();
            fail();
        }
    }

    public void testLong() {
        try {
            ChoicePointAlgorithm gac = SolverImpl.createDefaultAlgorithm();
            LongVariable n = new LongVariable("n", 10000000, 99999999);
            LongVariable x = new LongVariable("x", 0, 20000);
            LongVariable y = new LongVariable("y", 0, 20000);

            gac.addConstraint(n.eq(x.multiply(x)));
            gac.addConstraint(n.add(100000000).eq(y.multiply(y)));
            gac.propagate();

            LongDomain ndomain = n.getLongDomain();
            LongDomain xdomain = x.getLongDomain();
            LongDomain ydomain = y.getLongDomain();

            assertEquals("n minimum", 23765625, ndomain.getMin());
            assertEquals("n maximum", 56250000, ndomain.getMax());
            assertEquals("x minimum", 4875, xdomain.getMin());
            assertEquals("x maximum", 7500, xdomain.getMax());
            assertEquals("y minimum", 11125, ydomain.getMin());
            assertEquals("y maximum", 12500, ydomain.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    public void testFloat() {
        try {
            ChoicePointAlgorithm gac = SolverImpl.createDefaultAlgorithm();
            FloatVariable n = new FloatVariable("n", 10000000, 99999999);
            FloatVariable x = new FloatVariable("x", 0, 20000);
            FloatVariable y = new FloatVariable("y", 0, 20000);

            gac.addConstraint(n.eq(x.multiply(x)));
            gac.addConstraint(n.add(100000000).eq(y.multiply(y)));
            gac.propagate();

            FloatDomain ndomain = n.getFloatDomain();
            FloatDomain xdomain = x.getFloatDomain();
            FloatDomain ydomain = y.getFloatDomain();

            assertEquals("n minimum", 1.0000008E7d, ndomain.getMin(), 0.01f);
            assertEquals("n maximum", 1.0E8d, ndomain.getMax(), 0.01f);
            assertEquals("x minimum", 3162.2788d, xdomain.getMin(), 0.01f);
            assertEquals("x maximum", 10000.0d, xdomain.getMax(), 0.01f);
            assertEquals("y minimum", 10488.089d, ydomain.getMin(), 0.01f);
            assertEquals("y maximum", 14142.136d, ydomain.getMax(), 0.01f);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    public void testDouble() {
        try {
            ChoicePointAlgorithm gac = SolverImpl.createDefaultAlgorithm();
            DoubleVariable n = new DoubleVariable("n", 10000000, 99999999);
            DoubleVariable x = new DoubleVariable("x", 0, 20000);
            DoubleVariable y = new DoubleVariable("y", 0, 20000);

            gac.addConstraint(n.eq(x.multiply(x)));
            gac.addConstraint(n.add(100000000).eq(y.multiply(y)));
            gac.propagate();

            DoubleDomain ndomain = n.getDoubleDomain();
            DoubleDomain xdomain = x.getDoubleDomain();
            DoubleDomain ydomain = y.getDoubleDomain();

            assertEquals("n minimum", 1.0000000000000002e7d, ndomain.getMin(), 0.01f);
            assertEquals("n maximum", 9.999999899999999e7d, ndomain.getMax(), 0.01f);
            assertEquals("x minimum", 3162.2776601683795d, xdomain.getMin(), 0.01f);
            assertEquals("x maximum", 9999.99995d, xdomain.getMax(), 0.01f);
            assertEquals("y minimum", 10488.088481701516d, ydomain.getMin(), 0.01f);
            assertEquals("y maximum", 14142.135588375611d, ydomain.getMax(), 0.01f);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
}
