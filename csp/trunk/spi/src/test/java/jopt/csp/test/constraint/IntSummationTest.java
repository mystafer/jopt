/*
 * IntSummationTest.java
 * 
 * Created on May 3, 2005
 */
package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.GenericIntNode;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Various tests using a summation operation
 */
public class IntSummationTest extends TestCase {
    private ConstraintStore cs;
    private CspAlgorithm alg;
    private CspVariableFactory varFactory;
    
    private GenericIndex idxI;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private GenericIntExpr xiexpr;
    private GenericIntNode xi;
    
    private IntVariable y1;
    private IntVariable y2;
    private IntVariable y3;
    private NumNode ynode1;
    private NumNode ynode2;
    private NumNode ynode3;
    
    private IntVariable z1;
    private NumNode znode1;

    public void testSummationArcs() {
        try {
            NumArc arc1 = new GenericNumSummationArc(xi, new IntNode(), znode1, NumberMath.INTEGER, NumArc.LT, new GenericIndex[]{idxI}, null);
            arc1.propagate();
            					    
            assertEquals("x1 min", new MutableNumber ((int)5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber ((int)20), xnode1.getMax());
            assertEquals("x2 min", new MutableNumber ((int)3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber ((int)4), xnode2.getMax());
            assertEquals("x3 min", new MutableNumber ((int)15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber ((int)15), xnode3.getMax());
            assertEquals("z1 min", new MutableNumber ((int)0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber ((int)38), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            znode1.setMax(new MutableNumber ((int)25));
            NumArc arc2 = new GenericNumSummationReflexArc(znode1, xi, NumberMath.INTEGER, NumArc.LT, new GenericIndex[]{idxI}, null);
            arc2.propagate();
            
            assertEquals("x1 min", new MutableNumber ((int)5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber ((int)6), xnode1.getMax());
            assertEquals("x2 min", new MutableNumber ((int)3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber ((int)4), xnode2.getMax());
            assertEquals("x3 min", new MutableNumber ((int)15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber ((int)15), xnode3.getMax());
            assertEquals("z1 min", new MutableNumber ((int)0), znode1.getMin());
            assertEquals("z1 max", new MutableNumber ((int)25), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            znode1.setMin(new MutableNumber ((int)24));
            NumArc arc3 = new GenericNumSummationReflexArc(znode1, xi, NumberMath.INTEGER, NumArc.GT, new GenericIndex[]{idxI}, null);
            arc3.propagate();

            assertEquals("x1 min", new MutableNumber ((int)6), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber ((int)6), xnode1.getMax());
            assertEquals("x2 min", new MutableNumber ((int)4), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber ((int)4), xnode2.getMax());
            assertEquals("x3 min", new MutableNumber ((int)15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber ((int)15), xnode3.getMax());
            assertEquals("z1 min", new MutableNumber ((int)24), znode1.getMin());
            assertEquals("z1 max", new MutableNumber ((int)25), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());
            
            arc1.propagate();

            assertEquals("x1 min", new MutableNumber ((int)6), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber ((int)6), xnode1.getMax());
            assertEquals("x2 min", new MutableNumber ((int)4), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber ((int)4), xnode2.getMax());
            assertEquals("x3 min", new MutableNumber ((int)15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber ((int)15), xnode3.getMax());
            assertEquals("z1 min", new MutableNumber ((int)24), znode1.getMin());
            assertEquals("z1 max", new MutableNumber ((int)24), znode1.getMax());
            assertTrue("z1 is bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }


    public void testBasicSummationConstraint() {
        try {
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);

            CspMath math = varFactory.getMath();
            CspIntExpr summation = math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction);
            
            cs.addConstraint(summation.eq(z1));

            assertEquals("x1 min", new MutableNumber ((int)5), xnode1.getMin());
            assertEquals("x1 max", new MutableNumber ((int)20), xnode1.getMax());
            assertEquals("x2 min", new MutableNumber ((int)3), xnode2.getMin());
            assertEquals("x2 max", new MutableNumber ((int)4), xnode2.getMax());
            assertEquals("x3 min", new MutableNumber ((int)15), xnode3.getMin());
            assertEquals("x3 max", new MutableNumber ((int)15), xnode3.getMax());
            assertEquals("z1 min", new MutableNumber ((int)23), znode1.getMin());
            assertEquals("z1 max", new MutableNumber ((int)39), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }


    public void testRestrictedSumEq() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            CspGenericIntExpr xi = varFactory.genericInt("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            CspIntExpr summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            // sum(xi) = s,  s should be reduced to 0..301
            CspIntVariable s = varFactory.intVar("s", 0, 1000);
            cs.addConstraint(summation.eq(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 0, s.getMin());
            assertEquals("s max", 300, s.getMax());

            
            // set max s to 25, 1st 3 vars should be reduced to 0..25
            s.setMax(25);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 25, xi.getExpression(i).getMax());
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 0, s.getMin());
            assertEquals("s max", 25, s.getMax());
            
            s.setMin(24);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 25, xi.getExpression(i).getMax());
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 24, s.getMin());
            assertEquals("s max", 25, s.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    public void testRestrictedSumGt() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            CspGenericIntExpr xi = varFactory.genericInt("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            CspIntExpr summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            // sum(xi) = s,  s should be reduced to 0..299
            CspIntVariable s = varFactory.intVar("s", 0, 1000);
            cs.addConstraint(summation.gt(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 0, s.getMin());
            assertEquals("s max", 299, s.getMax());
            
            // set max s to 25, 1st 3 vars should not be affected
            s.setMin(225);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 26, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 225, s.getMin());
            assertEquals("s max", 299, s.getMax());
            
            // s should be > 30
            ((CspIntVariable) xi.getExpression(0)).setMin(50);
            ((CspIntVariable) xi.getExpression(1)).setMin(50);
            ((CspIntVariable) xi.getExpression(2)).setMin(50);
            
            assertEquals("x0 min", 50, xi.getExpression(0).getMin());
            assertEquals("x0 max", 100, xi.getExpression(0).getMax());
            assertEquals("x1 min", 50, xi.getExpression(1).getMin());
            assertEquals("x1 max", 100, xi.getExpression(1).getMax());
            assertEquals("x2 min", 50, xi.getExpression(2).getMin());
            assertEquals("x2 max", 100, xi.getExpression(2).getMax());
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 225, s.getMin());
            assertEquals("s max", 299, s.getMax());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    public void testRestrictedSumLt() {
    	CspGenericIntExpr xi = null;
    	CspIntExpr summation = null;
    	CspIntVariable s = null;
    	
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            xi = varFactory.genericInt("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            s = varFactory.intVar("s", 0, 1000);
            cs.addConstraint(summation.lt(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 1, s.getMin());
            assertEquals("s max", 1000, s.getMax());
            
            // set max s to 25, 1st 3 vars should be reduced to 0..24
            s.setMax(25);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 24, xi.getExpression(i).getMax());
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 1, s.getMin());
            assertEquals("s max", 25, s.getMax());
            
            // s should be > 3 if min of x1 is 3 and max of x0 and x2 should be reduced by 3
            ((CspIntVariable) xi.getExpression(1)).setMin(3);
            
            assertEquals("x0 min", 0, xi.getExpression(0).getMin());
            assertEquals("x0 max", 21, xi.getExpression(0).getMax());
            assertEquals("x1 min", 3, xi.getExpression(1).getMin());
            assertEquals("x1 max", 24, xi.getExpression(1).getMax());
            assertEquals("x2 min", 0, xi.getExpression(2).getMin());
            assertEquals("x2 max", 21, xi.getExpression(2).getMax());
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin());
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax());
            }
            assertEquals("s min", 4, s.getMin());
            assertEquals("s max", 25, s.getMax());
        }
        catch(PropagationFailureException propx) {
        	System.out.println("---------------------");
        	System.out.println("Propagation Failure - IntSummationTest - testRestrictedSumLt");
        	System.out.println(xi);
        	System.out.println(summation);
        	System.out.println(s);
            for (int i=0; i<100; i++) {
                System.out.println(xi.getExpression(i));
            }
            fail();
        }
    }
    
    private class IdxRestriction implements CspGenericIndexRestriction {
        private GenericIndex idx;
        
        public IdxRestriction(GenericIndex idx) {
            this.idx = idx;
        }
        
        public boolean currentIndicesValid() {
            return idx.currentVal() < 3;
        }
    }

    protected void setUp() throws Exception {
        super.setUp();


        cs = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        alg = cs.getConstraintAlg();
        varFactory = alg.getVarFactory();
        
        idxI = new GenericIndex("i", 3);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xi = (GenericIntNode)xiexpr.getNode();
        
        y1 = new IntVariable("y1", 0, 100);
        y2 = new IntVariable("y2", 0, 100);
        y3 = new IntVariable("y3", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        ynode3 = (NumNode)y3.getNode();
        
        z1 = new IntVariable("z1", 0, 100);
        znode1 = (NumNode)z1.getNode();

        // initialize index 1 nodes
        xnode1.setMin(new MutableNumber ((int)5));
        xnode1.setMax(new MutableNumber ((int)20));
        ynode1.setMin(new MutableNumber ((int)20));
        ynode1.setMax(new MutableNumber ((int)70));
        
        // initialize index 2 nodes
        xnode2.setMin(new MutableNumber ((int)3));
        xnode2.setMax(new MutableNumber ((int)4));
        ynode2.setMin(new MutableNumber ((int)1));
        ynode2.setMax(new MutableNumber ((int)2));
        
        // initialize index 3 nodes
        xnode3.setMin(new MutableNumber ((int)15));
        xnode3.setMax(new MutableNumber ((int)15));
        ynode3.setMin(new MutableNumber ((int)20));
        ynode3.setMax(new MutableNumber ((int)20));
        
    }
    
    protected void tearDown() {
        cs = null;
        alg = null;
        varFactory = null;
        
        idxI = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        xnode1 = null;
        xnode2 = null;
        xnode3 = null;
        xiexpr = null;
        xi = null;
        
        y1 = null;
        y2 = null;
        y3 = null;
        ynode1 = null;
        ynode2 = null;
        ynode3 = null;
        
        z1 = null;
        znode1 = null;
    }
}
