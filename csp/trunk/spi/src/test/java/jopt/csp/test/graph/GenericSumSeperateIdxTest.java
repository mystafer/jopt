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
public class GenericSumSeperateIdxTest extends TestCase {
    private GenericIndex idxI;
    private GenericIndex idxJ;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private GenericIntNode xi;
    private GenericIntNode xj;
    
    private IntVariable y1;
    private IntVariable y2;
    private IntVariable y3;
    private NumNode ynode1;
    private NumNode ynode2;
    private NumNode ynode3;
    private GenericIntNode yi;
    private GenericIntNode yj;
    
    private IntVariable z1;
    private IntVariable z2;
    private IntVariable z3;
    private NumNode znode1;
    private NumNode znode2;
    private NumNode znode3;
    private GenericIntNode zi;
    
    public GenericSumSeperateIdxTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Tests bounds consistency on Zi = Xi + Yj
     */
    public void testEqualZiXiYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yj, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 21), znode1.getMax());
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
            assertEquals("z3 max", new MutableNumber( (int) 16), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi = Xj + Yi
     */
    public void testEqualZiXjYi() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xj, yi, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 73), znode1.getMax());
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
            assertEquals("z3 max", new MutableNumber( (int) 23), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi = Xj + Yj
     */
    public void testEqualZiXjYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xj, yj, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 5), znode1.getMax());
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
            assertEquals("z3 max", new MutableNumber( (int) 5), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi = Xj + y
     */
    public void testEqualZiXjYcns() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xj, new MutableNumber( (int) 10), zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 13), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("x2 min", new MutableNumber( (int) 3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber( (int) 4), xnode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 13), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 13), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi = x + Yj
     */
    public void testEqualZiXcnsYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(new MutableNumber( (int) 10), yj, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 11), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            assertEquals("y2 min", new MutableNumber( (int) 1), ynode2.getMin());
            assertEquals("y2 max", new MutableNumber( (int) 2), ynode2.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 11), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 11), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        idxI = new GenericIndex("i", 3);
        idxJ = new GenericIndex("j", 3);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xi = new GenericIntNode("xi", new GenericIndex[]{idxI}, new NumNode[]{xnode1, xnode2, xnode3});
        xj = new GenericIntNode("xj", new GenericIndex[]{idxJ}, new NumNode[]{xnode1, xnode2, xnode3});
        
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        ynode3 = (NumNode)y3.getNode();
        yi = new GenericIntNode("yi", new GenericIndex[]{idxI}, new NumNode[]{ynode1, ynode2, ynode3});
        yj = new GenericIntNode("yj", new GenericIndex[]{idxJ}, new NumNode[]{ynode1, ynode2, ynode3});
        
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
    
    public void tearDown() {
        idxI = null;
        idxJ = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        xnode1 = null;
        xnode2 = null;
        xnode3 = null;
        xi = null;
        xj = null;
        
        y1 = null;
        y2 = null;
        y3 = null;
        ynode1 = null;
        ynode2 = null;
        ynode3 = null;
        yi = null;
        yj = null;
        
        z1 = null;
        z2 = null;
        z3 = null;
        znode1 = null;
        znode2 = null;
        znode3 = null;
        zi = null;
    }
}
