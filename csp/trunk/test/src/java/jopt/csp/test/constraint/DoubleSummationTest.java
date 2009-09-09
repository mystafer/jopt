/*
 * DoubleSummationTest.java
 * 
 * Created on May 3, 2005
 */
package jopt.csp.test.constraint;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationArc;
import jopt.csp.spi.arcalgorithm.graph.arc.generic.GenericNumSummationReflexArc;
import jopt.csp.spi.arcalgorithm.graph.node.DoubleNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericDoubleNode;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.DoubleVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericDoubleExpr;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Various tests using a summation operation
 */
public class DoubleSummationTest extends TestCase {
    private ConstraintStore cs;
    private CspAlgorithm alg;
    private CspVariableFactory varFactory;
    
    private GenericIndex idxI;
    
    private DoubleVariable x1;
    private DoubleVariable x2;
    private DoubleVariable x3;
    private NumNode xnode1;
    private NumNode xnode2;
    private NumNode xnode3;
    private GenericDoubleExpr xiexpr;
    private GenericDoubleNode xi;
    
    private DoubleVariable y1;
    private DoubleVariable y2;
    private DoubleVariable y3;
    private NumNode ynode1;
    private NumNode ynode2;
    private NumNode ynode3;
    
    private DoubleVariable z1;
    private NumNode znode1;

    public void testSummationArcs() {
        try {
            NumArc arc1 = new GenericNumSummationArc(xi, new DoubleNode(), znode1, NumberMath.DOUBLE, NumArc.LT, new GenericIndex[]{idxI}, null);
            arc1.propagate();

            assertEquals("x1 min", 5, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 20, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 0, znode1.getMin().doubleValue(), 0.0001);
//            assertEquals("z1 max", new MutableNumber((double) 39).previous(ReleasableManager.borrowList(), znode1.getPrecision()), znode1.getMax());
            assertFalse("z1 bound", znode1.isBound());

            znode1.setMax(new MutableNumber((double) 25));
            NumArc arc2 = new GenericNumSummationReflexArc(znode1, xi, NumberMath.DOUBLE, NumArc.LT, new GenericIndex[]{idxI}, null);
            arc2.propagate();
            
            assertEquals("x1 min", 5, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 7, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 0, znode1.getMin().doubleValue(), 0.0001);
            assertEquals("z1 max", 25, znode1.getMax().doubleValue(), 0.0001);
            assertFalse("z1 bound", znode1.isBound());

            znode1.setMin(new MutableNumber((double) 24));
            NumArc arc3 = new GenericNumSummationReflexArc(znode1, xi, NumberMath.DOUBLE, NumArc.GT, new GenericIndex[]{idxI}, null);
            arc3.propagate();

            assertEquals("x1 min", 5, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 7, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 24, znode1.getMin().doubleValue(), 0.0001);
            assertEquals("z1 max", 25, znode1.getMax().doubleValue(), 0.0001);
            assertFalse("z1 bound", znode1.isBound());
            
            arc1.propagate();

            assertEquals("x1 min", 5, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 7, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 24, znode1.getMin().doubleValue(), 0.0001);
            assertEquals("z1 max", 25, znode1.getMax().doubleValue(), 0.0001);
            assertFalse("z1 bound", znode1.isBound());

            znode1.setValue(new MutableNumber((double) 25));
            arc1.propagate();
            arc2.propagate();
            arc3.propagate();

            assertEquals("x1 min", 6, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 7, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 25, znode1.getMin().doubleValue(), 0.0001);
            assertEquals("z1 max", 25, znode1.getMax().doubleValue(), 0.0001);
            assertTrue("z1 bound", znode1.isBound());
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }

    public void tearDown() {
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
    
    public void testBasicSummationConstraint() {
        try {
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);

            CspMath math = varFactory.getMath();
            CspDoubleExpr summation = math.summation(xiexpr, new GenericIndex[]{idxI}, sourceRestriction);
            
            cs.addConstraint(summation.eq(z1));

            assertEquals("x1 min", 5, xnode1.getMin().doubleValue(), 0.0001);
            assertEquals("x1 max", 20, xnode1.getMax().doubleValue(), 0.0001);
            assertEquals("x2 min", 3, xnode2.getMin().doubleValue(), 0.0001);
            assertEquals("x2 max", 4, xnode2.getMax().doubleValue(), 0.0001);
            assertEquals("x3 min", 15, xnode3.getMin().doubleValue(), 0.0001);
            assertEquals("x3 max", 15, xnode3.getMax().doubleValue(), 0.0001);
            assertEquals("z1 min", 23, znode1.getMin().doubleValue(), 0.0001);
            assertEquals("z1 max", 39, znode1.getMax().doubleValue(), 0.0001);
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
            CspGenericDoubleExpr xi = varFactory.genericDouble("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            CspDoubleExpr summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            // sum(xi) = s,  s should be reduced to 0..301
            CspDoubleVariable s = varFactory.doubleVar("s", 0, 1000);
            cs.addConstraint(summation.eq(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 0, s.getMin(),.0001);
            assertEquals("s max", 300, s.getMax(),.0001);

            
            // set max s to 25, 1st 3 vars should be reduced to 0..25
            s.setMax(25);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 25, xi.getExpression(i).getMax(),.0001);
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 0, s.getMin(),.0001);
            assertEquals("s max", 25, s.getMax(),.0001);
            
            s.setMin(24);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 25, xi.getExpression(i).getMax(),.0001);
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 24, s.getMin(),.0001);
            assertEquals("s max", 25, s.getMax(),.00001);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    public void testRestrictedSumGt() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            CspGenericDoubleExpr xi = varFactory.genericDouble("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            CspDoubleExpr summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            // sum(xi) = s,  s should be reduced to 0..299
            CspDoubleVariable s = varFactory.doubleVar("s", 0, 1000);
            cs.addConstraint(summation.gt(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 0, s.getMin(),.0001);
            assertEquals("s max",300, s.getMax(),.0001);
            
            // set max s to 25, 1st 3 vars should not be affected
            s.setMin(225);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 25, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 225, s.getMin(),.00001);
            assertEquals("s max", 300, s.getMax(),.00001);
            
            // s should be > 30
            ((CspDoubleVariable) xi.getExpression(0)).setMin(50);
            ((CspDoubleVariable) xi.getExpression(1)).setMin(50);
            ((CspDoubleVariable) xi.getExpression(2)).setMin(50);
            
            assertEquals("x0 min", 50, xi.getExpression(0).getMin(),.0001);
            assertEquals("x0 max", 100, xi.getExpression(0).getMax(),.0001);
            assertEquals("x1 min", 50, xi.getExpression(1).getMin(),.0001);
            assertEquals("x1 max", 100, xi.getExpression(1).getMax(),.0001);
            assertEquals("x2 min", 50, xi.getExpression(2).getMin(),.0001);
            assertEquals("x2 max", 100, xi.getExpression(2).getMax(),.0001);
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 225, s.getMin(),.0001);
            assertEquals("s max", 300, s.getMax(),.0001);
        }
        catch(PropagationFailureException propx) {
            fail();
        }
    }
    
    
    public void testRestrictedSumLt() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            CspGenericDoubleExpr xi = varFactory.genericDouble("xi-basic", new GenericIndex[]{idxI}, 0, 100);
            
            IdxRestriction sourceRestriction = new IdxRestriction(idxI);
            
            CspMath math = varFactory.getMath();
            CspDoubleExpr summation = math.summation(xi, new GenericIndex[]{idxI}, sourceRestriction);
            
            // sum(xi) = s,  s should be reduced to 0..301
            CspDoubleVariable s = varFactory.doubleVar("s", 0, 1000);
            cs.addConstraint(summation.lt(s));
            
            for (int i=0; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 0, s.getMin(),.0001);
            assertEquals("s max", 1000, s.getMax(),.0001);
            
            // set max s to 25, 1st 3 vars should be reduced to 0..24
            s.setMax(25);
            
            for (int i=0; i<3; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 25, xi.getExpression(i).getMax(),.0001);
            }
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 0, s.getMin(),.0001);
            assertEquals("s max", 25, s.getMax(),.0001);
            
            // s should be > 3 if min of x1 is 3 and max of x0 and x2 should be reduced by 3
            ((CspDoubleVariable) xi.getExpression(1)).setMin(3);
            
            assertEquals("x0 min", 0, xi.getExpression(0).getMin(),.0001);
            assertEquals("x0 max", 22, xi.getExpression(0).getMax(),.0001);
            assertEquals("x1 min", 3, xi.getExpression(1).getMin(),.0001);
            assertEquals("x1 max", 25, xi.getExpression(1).getMax(),.0001);
            assertEquals("x2 min", 0, xi.getExpression(2).getMin(),.0001);
            assertEquals("x2 max", 22, xi.getExpression(2).getMax(),.0001);
            for (int i=3; i<100; i++) {
                assertEquals("x" + i + " min", 0, xi.getExpression(i).getMin(),.0001);
                assertEquals("x" + i + " max", 100, xi.getExpression(i).getMax(),.0001);
            }
            assertEquals("s min", 3, s.getMin(),3.0);
            assertEquals("s max", 25, s.getMax(),.0001);
        }
        catch(PropagationFailureException propx) {
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

        x1 = new DoubleVariable("x1", 0, 100);
        x2 = new DoubleVariable("x2", 0, 100);
        x3 = new DoubleVariable("x3", 0, 100);
        xnode1 = (NumNode)x1.getNode();
        xnode2 = (NumNode)x2.getNode();
        xnode3 = (NumNode)x3.getNode();
        xiexpr = (GenericDoubleExpr)varFactory.genericDouble("xi", idxI, new CspDoubleVariable[]{x1, x2, x3});
        xi = (GenericDoubleNode)xiexpr.getNode();
        
        y1 = new DoubleVariable("y1", 0, 100);
        y2 = new DoubleVariable("y2", 0, 100);
        y3 = new DoubleVariable("y3", 0, 100);
        ynode1 = (NumNode)y1.getNode();
        ynode2 = (NumNode)y2.getNode();
        ynode3 = (NumNode)y3.getNode();
        
        z1 = new DoubleVariable("z1", 0, 100);
        znode1 = (NumNode)z1.getNode();

        // initialize index 1 nodes
        xnode1.setMin(new MutableNumber((double) 5));
        xnode1.setMax(new MutableNumber((double) 20));
        ynode1.setMin(new MutableNumber((double) 20));
        ynode1.setMax(new MutableNumber((double) 70));
        
        // initialize index 2 nodes
        xnode2.setMin(new MutableNumber((double) 3));
        xnode2.setMax(new MutableNumber((double) 4));
        ynode2.setMin(new MutableNumber((double) 1));
        ynode2.setMax(new MutableNumber((double) 2));
        
        // initialize index 3 nodes
        xnode3.setMin(new MutableNumber((double) 15));
        xnode3.setMax(new MutableNumber((double) 15));
        ynode3.setMin(new MutableNumber((double) 20));
        ynode3.setMax(new MutableNumber((double) 20));
        
    }
}
