package jopt.csp.test.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericIntNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests some different comparison techniques on the
 * GenericNumSumArc
 * 
 * @author Nick
 */
public class GenericSumSharedIdxTest extends TestCase {
    private GenericIndex idxI;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private GenericIntNode xi;
    
    private IntVariable y1;
    private IntVariable y2;
    private IntVariable y3;
    private NumNode ynode1;
    private NumNode ynode2;
    private NumNode ynode3;
    private GenericIntNode yi;
    
    private IntVariable z1;
    private IntVariable z2;
    private IntVariable z3;
    private NumNode znode1;
    private NumNode znode2;
    private NumNode znode3;
    private GenericIntNode zi;
    
    public GenericSumSharedIdxTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Tests bounds consistency on Zi = Xi + Yi
     */
    public void testEqual() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.EQ);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 25), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 90), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 4), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 6), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 35), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 35), znode3.getMax());
            assertTrue("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi < Xi + Yi
     */
    public void testLessThan() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 89), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 5), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 34), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi <= Xi + Yi
     */
    public void testLessThanEqual() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.LEQ);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 90), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 6), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 35), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi > Xi + Yi
     */
    public void testGreaterThan() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.GT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 26), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 100), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 5), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 100), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 36), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 100), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi >= Xi + Yi
     */
    public void testGreaterThanEqual() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.GEQ);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 25), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 100), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 4), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 100), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 35), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 100), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi =/= Xi + Yi
     */
    public void testNotEqual() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.NEQ);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 100), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());
            assertEquals("z1 contains all elements", 101, znode1.getSize());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 100), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());
            assertEquals("z2 contains all elements", 101, znode2.getSize());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 100), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            assertEquals("z3 removed 1 element", 100, znode3.getSize());
            assertFalse("z3 contains 35", znode3.isInDomain(new MutableNumber( (int) 35)));
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Z < Xi + Yi
     */
    public void testLessThanZSingle() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, znode1, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            
            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            
            assertEquals("z min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z max", new MutableNumber( (int) 5), znode1.getMax());
            assertFalse("z bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Z < X + Yi
     */
    public void testLessThanZXSingle() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xnode1, yi, znode1, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x max", new MutableNumber( (int) 20), xnode1.getMax());
            
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());

            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            
            assertEquals("z min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z max", new MutableNumber( (int) 21), znode1.getMax());
            assertFalse("z bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    /**
     * Tests bounds consistency on Z < Xi + Y
     */
    public void testLessThanZYSingle() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, ynode1, znode1, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            
            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            
            assertEquals("y min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y max", new MutableNumber( (int) 70), ynode1.getMax());
            
            assertEquals("z min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z max", new MutableNumber( (int) 73), znode1.getMax());
            assertFalse("z bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi < x + Yi
     */
    public void testLessThanXConst() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(new MutableNumber( (int) 10), yi, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 79), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 11), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 29), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi < Xi + y
     */
    public void testLessThanYConst() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, new MutableNumber( (int) 10), zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 29), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 13), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 24), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        idxI = new GenericIndex("i", 3);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xi = new GenericIntNode("xi", new GenericIndex[]{idxI}, new NumNode[]{xnode1, xnode2, xnode3});
        
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        ynode3 = (NumNode)y3.getNode();
        yi = new GenericIntNode("yi", new GenericIndex[]{idxI}, new NumNode[]{ynode1, ynode2, ynode3});
        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        znode1 = (NumNode)z1.getNode();
        znode2 = (NumNode)z2.getNode();
        znode3 = (NumNode)z3.getNode();
        zi = new GenericIntNode("zi", new GenericIndex[]{idxI}, new NumNode[]{znode1, znode2, znode3});

        // initialize index 1 nodes
        xnode1.setMin(new MutableNumber( (int) 5));
        xnode1.setMax(new MutableNumber( (int) 20));
        ynode1.setMin(new MutableNumber( (int) 20));
        ynode1.setMax(new MutableNumber( (int) 70));
        
        // initialize index 2 nodes
        xnode2.setMin(new MutableNumber( (int) 3));
        xnode2.setMax(new MutableNumber( (int) 4));
        ynode2.setMin(new MutableNumber( (int) 1));
        ynode2.setMax(new MutableNumber( (int) 2));
        
        // initialize index 3 nodes
        xnode3.setMin(new MutableNumber( (int) 15));
        xnode3.setMax(new MutableNumber( (int) 15));
        ynode3.setMin(new MutableNumber( (int) 20));
        ynode3.setMax(new MutableNumber( (int) 20));
        
    }
    
    protected void tearDown() {
        idxI = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        xnode1 = null;
        xnode2 = null;
        xnode3 = null;
        xi = null;
        
        y1 = null;
        y2 = null;
        y3 = null;
        ynode1 = null;
        ynode2 = null;
        ynode3 = null;
        yi = null;
        
        z1 = null;
        z2 = null;
        z3 = null;
        znode1 = null;
        znode2 = null;
        znode3 = null;
        zi = null;
    }
}
