package jopt.csp.test.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.TernaryNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspAlgorithmStrength;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests some different comparison techniques on the
 * TernarnyNumSumArc
 * 
 * @author Nick
 */
public class TernaryNumSumArcTest extends TestCase {
    public TernaryNumSumArcTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Tests bounds consistency on Z = X + Y
     * 
     * min(z) = min(x) + min(y)
     * max(z) = max(x) + max(y)
     */
    public void testEqual() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.EQ);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 25), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 90), znode.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests bounds consistency on Z < X + Y
     * 
     * min(z) = 0
     * max(z) = max(x) + max(y) - 1
     */
    public void testLessThan() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 0), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 89), znode.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests bounds consistency on Z <= X + Y
     * 
     * min(z) = 0
     * max(z) = max(x) + max(y)
     */
    public void testLessThanEqual() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.LEQ);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 0), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 90), znode.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests bounds consistency on Z > X + Y
     * 
     * min(z) = min(x) + min(y) + 1
     * max(z) = 100
     */
    public void testGreaterThan() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.GT);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 26), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 100), znode.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests bounds consistency on Z >= X + Y
     * 
     * min(z) = min(x) + min(y)
     * max(z) = 100
     */
    public void testGreaterThanEqual() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.GEQ);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 25), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 100), znode.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests bounds consistency on Z =/= X + Y
     * 
     * This constraint will not remove any values from z until both X and Y are bound.
     * The reason for this is only one value needs shouuld be removed from Z and it
     * is impossible to determine until X and Y are determined. 
     */
    public void testNotEqual() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.NEQ);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 0), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 100), znode.getMax());
            assertTrue("z contains 25", znode.isInDomain(new MutableNumber( (int) 25)));
            
            xnode.setValue(new MutableNumber( (int) 5));
            arc.propagate();
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 5), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 0), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 100), znode.getMax());
            assertTrue("z contains 25", znode.isInDomain(new MutableNumber( (int) 25)));

            ynode.setValue(new MutableNumber( (int) 20));
            arc.propagate();
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 5), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 20), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 0), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 100), znode.getMax());
            assertFalse("z does not contain 25", znode.isInDomain(new MutableNumber( (int) 25)));
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    /**
     * Tests domain consistency on Z = X + Y.
     * 
     * min(z) = min(x) + min(y)
     * max(z) = max(x) + max(y)
     * 
     * Domain consistency will remove any values from Z that are not 
     * possible by summing X and Y, not just the min and max values.
     * 
     * Technically, domain consistancy only applies to equal constraints
     * and not <, >, etc.  Since these constraints really only place
     * restrictions on the upper or lower bounds of the Z variable.
     */
    public void testEqualDomain() {
        try {
            IntVariable x = new IntVariable("x", 0, 100);
            IntVariable y = new IntVariable("y", 0, 100);
            IntVariable z = new IntVariable("z", 0, 100);

            NumNode xnode = (NumNode)x.getNode();
            NumNode ynode = (NumNode)y.getNode();
            NumNode znode = (NumNode)z.getNode();
            
            xnode.setMin(new MutableNumber( (int) 5));
            xnode.setMax(new MutableNumber( (int) 20));
            xnode.removeRange(new MutableNumber( (int) 10), new MutableNumber( (int) 15));
            
            ynode.setMin(new MutableNumber( (int) 20));
            ynode.setMax(new MutableNumber( (int) 70));
            ynode.removeRange(new MutableNumber( (int) 21), new MutableNumber( (int) 30));
            
            NumArc arc = new TernaryNumSumArc(xnode, ynode, znode, NumberMath.INTEGER, NumArc.EQ);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode.getMax());
            assertEquals("y min", new MutableNumber( (int) 20), ynode.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode.getMax());
            assertEquals("z min", new MutableNumber( (int) 25), znode.getMin());
            assertEquals("z max", new MutableNumber( (int) 90), znode.getMax());
            for (int i=30; i<36; i++)
            	assertTrue("z contains " + i, znode.isInDomain(new MutableNumber( (int) i)));
            
            arc.setAlgorithmStrength(CspAlgorithmStrength.HYPER_ARC_CONSISTENCY);
            arc.propagate();
            for (int i=30; i<36; i++)
            	assertFalse("z contains " + i, znode.isInDomain(new MutableNumber( (int) i)));
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

}
