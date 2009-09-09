package jopt.csp.test.graph;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSumArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericIntNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ChoicePointStack;
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
public class GenericSumSourceControlTest extends TestCase {
    private GenericIndex idxI;
    private GenericIndex idxJ;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private IntVariable x4;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private NumNode xnode4;
    private GenericIntNode xi;
    private GenericIntNode xij;
    
    private IntVariable y1;
    private IntVariable y2;
    private NumNode ynode1;
    private NumNode ynode2;
    private GenericIntNode yi;
    
    private IntVariable z1;
    private IntVariable z2;
    private NumNode znode1;
    private NumNode znode2;
    private GenericIntNode zi;
    private GenericIntNode zj;
    
    public GenericSumSourceControlTest(java.lang.String testName) {
        super(testName);
    }
    
    /**
     * Tests that indices are updated correctly when setIndicesToNodeOffset
     * is called on a node
     */
    public void testSetIndicesNodeOffset() {
    	xij.setIndicesToNodeOffset(0);
        assertEquals("i", 0, idxI.currentVal());
        assertEquals("j", 0, idxJ.currentVal());

        xij.setIndicesToNodeOffset(1);
        assertEquals("i", 0, idxI.currentVal());
        assertEquals("j", 1, idxJ.currentVal());

        xij.setIndicesToNodeOffset(2);
        assertEquals("i", 1, idxI.currentVal());
        assertEquals("j", 0, idxJ.currentVal());

        xij.setIndicesToNodeOffset(3);
        assertEquals("i", 1, idxI.currentVal());
        assertEquals("j", 1, idxJ.currentVal());
    }

    /**
     * Tests that min / max offsets are updated correctly as internal nodes
     * are modified
     */
    public void testDomainChangeTracking() {
        try {
            xi.clearDelta();
            
            assertEquals("xi value modified min", -1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", -1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", -1, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", -1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", -1, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", -1, xi.domainModifiedMaxOffset());
            
            xnode2.removeValue(new MutableNumber( (int) 5));
            assertEquals("xi value modified min", -1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", -1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", -1, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", -1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 1, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());

            xnode1.removeValue(new MutableNumber( (int) 5));
            assertEquals("xi value modified min", -1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", -1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", -1, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", -1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 0, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());

            xi.clearDelta();
            xnode2.setMax(new MutableNumber( (int) 5));
            assertEquals("xi value modified min", -1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", -1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", 1, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", 1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 1, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());

            xnode1.setMax(new MutableNumber( (int) 5));
            assertEquals("xi value modified min", -1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", -1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", 0, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", 1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 0, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());

            xi.clearDelta();
            xnode2.setValue(new MutableNumber( (int) 3));
            assertEquals("xi value modified min", 1, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", 1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", 1, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", 1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 1, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());

            xnode1.setValue(new MutableNumber( (int) 3));
            assertEquals("xi value modified min", 0, xi.valueModifiedMinOffset());
            assertEquals("xi value modified max", 1, xi.valueModifiedMaxOffset());
            assertEquals("xi range modified min", 0, xi.rangeModifiedMinOffset());
            assertEquals("xi range modified max", 1, xi.rangeModifiedMaxOffset());
            assertEquals("xi domain modified min", 0, xi.domainModifiedMinOffset());
            assertEquals("xi domain modified max", 1, xi.domainModifiedMaxOffset());
        }
        catch(PropagationFailureException propx) {
            propx.printStackTrace();
        	fail();
        }
    }
    

    /**
     * Tests bounds consistency on Zi = Xi + Yi where propagation is initiated by X
     */
    public void testXControlled() {
        try {
            ChoicePointStack cpstack = new ChoicePointStack();

            xi.clearDelta();
            xi.setChoicePointStack(cpstack);
            yi.clearDelta();
            yi.setChoicePointStack(cpstack);
            zi.clearDelta();
            zi.setChoicePointStack(cpstack);
            
            // initialize index 1 nodes
            xnode1.setMin(new MutableNumber( (int) 5));
            xnode1.setMax(new MutableNumber( (int) 20));
            ynode1.setMin(new MutableNumber( (int) 20));
            ynode1.setMax(new MutableNumber( (int) 70));
            
            xi.clearDelta();
          
            // initialize index 2 nodes
            xnode2.setMin(new MutableNumber( (int) 3));
            xnode2.setMax(new MutableNumber( (int) 4));
            ynode2.setMin(new MutableNumber( (int) 1));
            ynode2.setMax(new MutableNumber( (int) 2));

            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate(xi);
           
            // only z2 should be affected
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 100), znode1.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 5), znode2.getMax());

        
            cpstack.pop();
            
            // initialize index 2 nodes
            xnode2.setMin(new MutableNumber( (int) 3));
            xnode2.setMax(new MutableNumber( (int) 4));
            ynode2.setMin(new MutableNumber( (int) 1));
            ynode2.setMax(new MutableNumber( (int) 2));

            xi.clearDelta();
          
            // initialize index 1 nodes
            xnode1.setMin(new MutableNumber( (int) 5));
            xnode1.setMax(new MutableNumber( (int) 20));
            ynode1.setMin(new MutableNumber( (int) 20));
            ynode1.setMax(new MutableNumber( (int) 70));
            
            // propagate changes
            arc.propagate(xi);
           
            // only z1 should be affected
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 89), znode1.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 100), znode2.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    

    /**
     * Tests bounds consistency on Zi = Xi + Yi where propagation is initiated by Y
     */
    public void testYControlled() {
        try {
            ChoicePointStack cpstack = new ChoicePointStack();

            xi.clearDelta();
            xi.setChoicePointStack(cpstack);
            yi.clearDelta();
            yi.setChoicePointStack(cpstack);
            zi.clearDelta();
            zi.setChoicePointStack(cpstack);
            
            // initialize index 1 nodes
            xnode1.setMin(new MutableNumber( (int) 5));
            xnode1.setMax(new MutableNumber( (int) 20));
            ynode1.setMin(new MutableNumber( (int) 20));
            ynode1.setMax(new MutableNumber( (int) 70));
            
            yi.clearDelta();
          
            // initialize index 2 nodes
            xnode2.setMin(new MutableNumber( (int) 3));
            xnode2.setMax(new MutableNumber( (int) 4));
            ynode2.setMin(new MutableNumber( (int) 1));
            ynode2.setMax(new MutableNumber( (int) 2));

            // propagate changes
            NumArc arc = new GenericNumSumArc(xi, yi, zi, NumberMath.INTEGER, NumArc.LT);
            arc.propagate(yi);
           
            // only z2 should be affected
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 100), znode1.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 5), znode2.getMax());

        
            cpstack.pop();
            
            // initialize index 2 nodes
            xnode2.setMin(new MutableNumber( (int) 3));
            xnode2.setMax(new MutableNumber( (int) 4));
            ynode2.setMin(new MutableNumber( (int) 1));
            ynode2.setMax(new MutableNumber( (int) 2));

            yi.clearDelta();
          
            // initialize index 1 nodes
            xnode1.setMin(new MutableNumber( (int) 5));
            xnode1.setMax(new MutableNumber( (int) 20));
            ynode1.setMin(new MutableNumber( (int) 20));
            ynode1.setMax(new MutableNumber( (int) 70));
            
            // propagate changes
            arc.propagate(yi);
           
            // only z1 should be affected
            assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber( (int) 89), znode1.getMax());
            assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
            assertEquals("z2 max", new MutableNumber( (int) 100), znode2.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
     /**
      * Tests bounds consistency on Zj = Xi + y where propagation is initiated by X
      */
     public void testXiControlledZj() {
         try {
             ChoicePointStack cpstack = new ChoicePointStack();
    
             xi.clearDelta();
             xi.setChoicePointStack(cpstack);
             zi.clearDelta();
             zi.setChoicePointStack(cpstack);
             
             // initialize index 1 nodes
             xnode1.setMin(new MutableNumber( (int) 5));
             xnode1.setMax(new MutableNumber( (int) 20));
             
             xi.clearDelta();
           
             // initialize index 2 nodes
             xnode2.setMin(new MutableNumber( (int) 3));
             xnode2.setMax(new MutableNumber( (int) 4));
    
             // propagate changes
             NumArc arc = new GenericNumSumArc(xi, new MutableNumber( (int) 20), zj, NumberMath.INTEGER, NumArc.LT);
             arc.propagate(xi);
            
             // both z1 & z2 should be affected
             assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
             assertEquals("z1 max", new MutableNumber( (int) 23), znode1.getMax());
             assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
             assertEquals("z2 max", new MutableNumber( (int) 23), znode2.getMax());
    
         
             cpstack.pop();
             
             // initialize index 2 nodes
             xnode2.setMin(new MutableNumber( (int) 3));
             xnode2.setMax(new MutableNumber( (int) 4));
    
             xi.clearDelta();
             
             // initialize index 1 nodes
             xnode1.setMin(new MutableNumber( (int) 5));
             xnode1.setMax(new MutableNumber( (int) 20));
             
             // propagate changes
             arc.propagate(xi);
            
             // both z1 & z2 should be affected
             assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
             assertEquals("z1 max", new MutableNumber( (int) 39), znode1.getMax());
             assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
             assertEquals("z2 max", new MutableNumber( (int) 39), znode2.getMax());
         }
         catch(PropagationFailureException propx) {
         	 propx.printStackTrace();
         	 fail();
         }
    }
     
     /**
      * Tests bounds consistency on Zj = x + Yi where propagation is initiated by Y
      */
     public void testYiControlledZj() {
         try {
             ChoicePointStack cpstack = new ChoicePointStack();
    
             yi.clearDelta();
             yi.setChoicePointStack(cpstack);
             zi.clearDelta();
             zi.setChoicePointStack(cpstack);
             
             // initialize index 1 nodes
             ynode1.setMin(new MutableNumber( (int) 5));
             ynode1.setMax(new MutableNumber( (int) 20));
             
             yi.clearDelta();
           
             // initialize index 2 nodes
             ynode2.setMin(new MutableNumber( (int) 3));
             ynode2.setMax(new MutableNumber( (int) 4));
    
             // propagate changes
             NumArc arc = new GenericNumSumArc(new MutableNumber( (int) 20), yi, zj, NumberMath.INTEGER, NumArc.LT);
             arc.propagate(yi);
            
             // both z1 & z2 should be affected
             assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
             assertEquals("z1 max", new MutableNumber( (int) 23), znode1.getMax());
             assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
             assertEquals("z2 max", new MutableNumber( (int) 23), znode2.getMax());
    
         
             cpstack.pop();
             
             // initialize index 2 nodes
             ynode2.setMin(new MutableNumber( (int) 3));
             ynode2.setMax(new MutableNumber( (int) 4));
    
             yi.clearDelta();
             
             // initialize index 1 nodes
             ynode1.setMin(new MutableNumber( (int) 5));
             ynode1.setMax(new MutableNumber( (int) 20));
             
             // propagate changes
             arc.propagate(yi);
            
             // both z1 & z2 should be affected
             assertEquals("z1 min", new MutableNumber( (int) 0), znode1.getMin());
             assertEquals("z1 max", new MutableNumber( (int) 39), znode1.getMax());
             assertEquals("z2 min", new MutableNumber( (int) 0), znode2.getMin());
             assertEquals("z2 max", new MutableNumber( (int) 39), znode2.getMax());
         }
         catch(PropagationFailureException propx) {
             fail();
         }
    }
 

    protected void setUp() throws Exception {
        super.setUp();
        
        idxI = new GenericIndex("i", 2);
        idxJ = new GenericIndex("j", 2);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x4 = new IntVariable("x4", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xnode4 = (NumNode)x4.getNode();
        xi = new GenericIntNode("xi", new GenericIndex[]{idxI}, new NumNode[]{xnode1, xnode2});
        xij = new GenericIntNode("xij", new GenericIndex[]{idxI, idxJ}, new NumNode[]{xnode1, xnode2, xnode3, xnode4});
        
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        yi = new GenericIntNode("yi", new GenericIndex[]{idxI}, new NumNode[]{ynode1, ynode2});
        
        z1 = new IntVariable("z1", 0, 100);
        z2 = new IntVariable("z2", 0, 100);
        znode1 = (NumNode)z1.getNode();
        znode2 = (NumNode)z2.getNode();
        zi = new GenericIntNode("zi", new GenericIndex[]{idxI}, new NumNode[]{znode1, znode2});
        zj = new GenericIntNode("zj", new GenericIndex[]{idxJ}, new NumNode[]{znode1, znode2});
    }
    
    protected void tearDown() {
        idxI = null;
        idxJ = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        x4 = null;
        xnode1 = null;
        xnode2 = null;
        xnode3 = null;
        xnode4 = null;
        xi = null;
        xij = null;
        
        y1 = null;
        y2 = null;
        ynode1 = null;
        ynode2 = null;
        yi = null;
        
        z1 = null;
        z2 = null;
        znode1 = null;
        znode2 = null;
        zi = null;
        zj = null;
    }
}
