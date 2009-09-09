package jopt.csp.test.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericIntNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNumNode;
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
public class GenericSumComboIdxTest extends TestCase {
    private GenericIndex idxI;
    private GenericIndex idxJ;
    private GenericIndex idxK;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private IntVariable x4;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private NumNode xnode4;
    private GenericNumNode xi;
    private GenericNumNode xj;
    private GenericNumNode xk;
    private GenericNumNode xij;
    
    private IntVariable y1;
    private IntVariable y2;
    private IntVariable y3;
    private IntVariable y4;
    private NumNode ynode1;
    private NumNode ynode2;
    private NumNode ynode3;
    private NumNode ynode4;
    private GenericNumNode yi;
    private GenericNumNode yj;
    private GenericNumNode yk;
    private GenericNumNode yij;
    
    private IntVariable z1;
    private IntVariable z2;
    private IntVariable z3;
    private IntVariable z4;
    private NumNode znode1;
    private NumNode znode2;
    private NumNode znode3;
    private NumNode znode4;
    private GenericNumNode zi;
    private GenericNumNode zj;
    private GenericNumNode zij;
    
    public GenericSumComboIdxTest(java.lang.String testName) {
        super(testName);
    }

    /**
     * Tests bounds consistency on Zij = Xi + Yi
     */
    public void testEqualZijXiYi() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zij, NumberMath.INTEGER, NumArc.LT);
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
            assertEquals("z2 max", new MutableNumber( (int) 89), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 5), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 5), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zij = Xj + Yj
     */
    public void testEqualZijXjYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xj, yj, zij, NumberMath.INTEGER, NumArc.LT);
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

            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 89), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 5), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zij = Xij + Yij
     */
    public void testEqualZijXijYij() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xij, yij, zij, NumberMath.INTEGER, NumArc.LT);
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
            
            assertEquals("x4 min", new MutableNumber( (int) 12), xnode4.getMin());
            assertEquals("x4 max", new MutableNumber( (int) 43), xnode4.getMax());
            assertEquals("y4 min", new MutableNumber( (int) 9), ynode4.getMin());
            assertEquals("y4 max", new MutableNumber( (int) 44), ynode4.getMax());
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 86), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zij = Xij + Yi
     */
    public void testEqualZijXijYi() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xij, yi, zij, NumberMath.INTEGER, NumArc.LT);
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
            assertEquals("z2 max", new MutableNumber( (int) 73), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 16), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
            assertEquals("x4 min", new MutableNumber( (int) 12), xnode4.getMin());
            assertEquals("x4 max", new MutableNumber( (int) 43), xnode4.getMax());
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 44), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zij = Xi + Yij
     */
    public void testEqualZijXiYij() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yij, zij, NumberMath.INTEGER, NumArc.LT);
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
            assertEquals("z2 max", new MutableNumber( (int) 21), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 23), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
            assertEquals("y4 min", new MutableNumber( (int) 9), ynode4.getMin());
            assertEquals("y4 max", new MutableNumber( (int) 44), ynode4.getMax());
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 47), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zi = Xij + Yij
     */
    public void testEqualZiXijYij() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xij, yij, zi, NumberMath.INTEGER, NumArc.LT);
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
            assertEquals("z2 max", new MutableNumber( (int) 34), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            
            assertEquals("x4 min", new MutableNumber( (int) 12), xnode4.getMin());
            assertEquals("x4 max", new MutableNumber( (int) 43), xnode4.getMax());
            assertEquals("y4 min", new MutableNumber( (int) 9), ynode4.getMin());
            assertEquals("y4 max", new MutableNumber( (int) 44), ynode4.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    /**
     * Tests bounds consistency on Zj = Xij + Yij
     */
    public void testEqualZjXijYij() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xij, yij, zj, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
            assertEquals("x1 min", new MutableNumber( (int) 5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber( (int) 20), xnode1.getMax());
            assertEquals("y1 min", new MutableNumber( (int) 20), ynode1.getMin());
            assertEquals("y1 max", new MutableNumber( (int) 70), ynode1.getMax());
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 34), znode1.getMax());
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
            
            assertEquals("x4 min", new MutableNumber( (int) 12), xnode4.getMin());
            assertEquals("x4 max", new MutableNumber( (int) 43), xnode4.getMax());
            assertEquals("y4 min", new MutableNumber( (int) 9), ynode4.getMin());
            assertEquals("y4 max", new MutableNumber( (int) 44), ynode4.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    /**
     * Tests bounds consistency on Zj = Xi + Yj
     */
    public void testEqualZijXiYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yj, zij, NumberMath.INTEGER, NumArc.LT);
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
            assertEquals("z2 max", new MutableNumber( (int) 21), znode2.getMax());
            assertFalse("z2 bound", znode2.isBound());

            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 73), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 5), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    /**
     * Tests bounds consistency on Zi = Xj + Yk
     */
    public void testEqualZiXjYj() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xj, yk, zi, NumberMath.INTEGER, NumArc.LT);
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
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    /**
     * Tests bounds consistency on Zij = Xj + Yk
     */
    public void testEqualZijXkYk() {
        try {
            // propagate changes
            NumArc arc = new GenericNumSumArc(xk, yk, zij, NumberMath.INTEGER, NumArc.LT);
            arc.propagate();
            
//            System.out.println("x1: " + xnode1);
//            System.out.println("x2: " + xnode2);
//            System.out.println("x3: " + xnode3);
//            System.out.println("x4: " + xnode4);
//            System.out.println("y1: " + ynode1);
//            System.out.println("y2: " + ynode2);
//            System.out.println("y3: " + ynode3);
//            System.out.println("y4: " + ynode4);
//            System.out.println("z1: " + znode1);
//            System.out.println("z2: " + znode2);
//            System.out.println("z3: " + znode3);
//            System.out.println("z4: " + znode4);
            
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

//            assertEquals("x3 min", new MutableNumber( (int) 15), xnode3.getMin());
//            assertEquals("x3 max", new MutableNumber( (int) 15), xnode3.getMax());
//            assertEquals("y3 min", new MutableNumber( (int) 20), ynode3.getMin());
//            assertEquals("y3 max", new MutableNumber( (int) 20), ynode3.getMax());
            assertEquals("z3 min", new MutableNumber( (int) 0), znode3.getMin());
            assertEquals("z3 max", new MutableNumber( (int) 5), znode3.getMax());
            assertFalse("z3 bound", znode3.isBound());
            
//            assertEquals("x4 min", new MutableNumber( (int) 12), xnode4.getMin());
//            assertEquals("x4 max", new MutableNumber( (int) 43), xnode4.getMax());
//            assertEquals("y4 min", new MutableNumber( (int) 9), ynode4.getMin());
//            assertEquals("y4 max", new MutableNumber( (int) 44), ynode4.getMax());
            assertEquals("z4 min", new MutableNumber( (int) 0), znode4.getMin());
            assertEquals("z4 max", new MutableNumber( (int) 5), znode4.getMax());
            assertFalse("z4 bound", znode4.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        idxI = new GenericIndex("i", 2);
        idxJ = new GenericIndex("j", 2);
        idxK = new GenericIndex("k", 2);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x4 = new IntVariable("x4", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xnode4 = (NumNode)x4.getNode();
        xi = new GenericIntNode("xi", new GenericIndex[]{idxI}, new NumNode[]{xnode1, xnode2});
        xj = new GenericIntNode("xj", new GenericIndex[]{idxJ}, new NumNode[]{xnode1, xnode2});
        xk = new GenericIntNode("xk", new GenericIndex[]{idxK}, new NumNode[]{xnode1, xnode2});
        xij = new GenericIntNode("xij", new GenericIndex[]{idxI, idxJ}, new NumNode[]{xnode1, xnode2, xnode3, xnode4});
        
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        y4 = new IntVariable("y4", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        ynode3 = (NumNode)y3.getNode();
        ynode4 = (NumNode)y4.getNode();
        yi = new GenericIntNode("yi", new GenericIndex[]{idxI}, new NumNode[]{ynode1, ynode2});
        yj = new GenericIntNode("yj", new GenericIndex[]{idxJ}, new NumNode[]{ynode1, ynode2});
        yk = new GenericIntNode("yk", new GenericIndex[]{idxK}, new NumNode[]{ynode1, ynode2});
        yij = new GenericIntNode("yij", new GenericIndex[]{idxI, idxJ}, new NumNode[]{ynode1, ynode2, ynode3, ynode4});
        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        z3 = new IntVariable("z3", 0, 100);
        z4 = new IntVariable("z4", 0, 100);
        znode1 = (NumNode)z1.getNode();
        znode2 = (NumNode)z2.getNode();
        znode3 = (NumNode)z3.getNode();
        znode4 = (NumNode)z4.getNode();
        zi = new GenericIntNode("zi", new GenericIndex[]{idxI}, new NumNode[]{znode1, znode2});
        zj = new GenericIntNode("zj", new GenericIndex[]{idxJ}, new NumNode[]{znode1, znode2});
        zij = new GenericIntNode("zij", new GenericIndex[]{idxI, idxJ}, new NumNode[]{znode1, znode2, znode3, znode4});

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
        
        // initialize index 4 nodes
        xnode4.setMin(new MutableNumber( (int) 12));
        xnode4.setMax(new MutableNumber( (int) 43));
        ynode4.setMin(new MutableNumber( (int) 9));
        ynode4.setMax(new MutableNumber( (int) 44));
    }
    
    protected void tearDown() {
        idxI = null;
        idxJ = null;
        idxK = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        x4 = null;
        xnode1 = null;
        xnode2 = null;
        xnode3 = null;
        xnode4 = null;
        xi = null;
        xj = null;
        xk = null;
        xij = null;
        
        y1 = null;
        y2 = null;
        y3 = null;
        y4 = null;
        ynode1 = null;
        ynode2 = null;
        ynode3 = null;
        ynode4 = null;
        yi = null;
        yj = null;
        yk = null;
        yij = null;
        
        z1 = null;
        z2 = null;
        z3 = null;
        z4 = null;
        znode1 = null;
        znode2 = null;
        znode3 = null;
        znode4 = null;
        zi = null;
        zj = null;
        zij = null;
    }
}
