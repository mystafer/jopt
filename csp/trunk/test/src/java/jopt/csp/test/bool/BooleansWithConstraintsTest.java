package jopt.csp.test.bool;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests the mapping of vars to constraints
 * 
 * @author Chris Johnson
 */
public class BooleansWithConstraintsTest extends TestCase {
	private BooleanVariable aVar;
    private BooleanVariable bVar;
    private BooleanVariable cVar;
    private BooleanVariable dVar;
    private IntVariable i1;
    private IntVariable i2;
    private IntVariable i3;
    private IntVariable i4;
    private BooleanExpr xExpr;
	private CspSolver solver;
	
    public BooleansWithConstraintsTest(java.lang.String testName) {
        super(testName);
    }
    
    public void tearDown(){
        aVar=null;
        bVar=null;
        cVar=null;
        dVar=null;
        i1=null;
        i2=null;
        i3=null;
        i4=null;
        xExpr=null;
        solver=null;
    }
    
    public void setUp() {
    	solver = CspSolver.createSolver();
    	solver.setAutoPropagate(false);
    	aVar = new BooleanVariable("aVar");
        bVar = new BooleanVariable("bVar");
        cVar = new BooleanVariable("cVar");
        dVar = new BooleanVariable("dVar");
        i1 = new IntVariable("i1", 0, 10);
        i2 = new IntVariable("i2", 0, 10);
        i3 = new IntVariable("i3", 0, 10);
        i4 = new IntVariable("i4", 0, 10);
    }
    
    public void testHelper() {
        try {
            CspIntExpr sumExpr = i1.add(i2);
            IntVariable sumVar = new IntVariable("sumVar", sumExpr);
            solver.addConstraint(sumExpr.eq(sumVar));
            
            i1.setValue(3);
            i2.setValue(4);
            solver.propagate();
            
            assertEquals("sumExpr is 7", 7, sumExpr.getMax());
            assertEquals("sumExpr is 7", 7, sumExpr.getMin());
            assertEquals("sumVar is 7", 7, sumVar.getMax());
            assertEquals("sumVar is 7", 7, sumVar.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testVarToExprEquality() {
        try {
            xExpr = new BooleanVariable("xExpr");
            BooleanVariable xVar = new BooleanVariable("xVar", xExpr);
            solver.addConstraint(xVar);
            
            xVar.setTrue();
            solver.propagate();
            
            assertTrue("xExpr is true", xExpr.isTrue());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testExprToVarEquality() {
        try {
            xExpr = (BooleanExpr) aVar.eq(bVar);
            BooleanVariable xVar = new BooleanVariable("xVar", xExpr);
            solver.addConstraint(xVar);
            
            aVar.setTrue();
            bVar.setTrue();
            solver.propagate();
            
            assertTrue("xExpr is true", xExpr.isTrue());
            assertTrue("xVar is true", xVar.isTrue());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleVariableImplicationWithNumericConstraints() {
        try {
            CspConstraint aConstraint = i1.lt(i2);
            BooleanVariable a = new BooleanVariable("a", aConstraint);
            
            CspConstraint bConstraint = i3.eq(i4);
            BooleanVariable b = new BooleanVariable("b", bConstraint);
            
            solver.addConstraint(a.implies(b));

            //Determine domains
            i1.setValue(0);
            i2.setValue(1);
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("a is true", a.isTrue());
            assertFalse("a is not false", a.isFalse());
            assertTrue("b is true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
            
            //Determine another domain
            i3.setValue(5);
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertEquals("i4 is 5", 5, i4.getMax());
            assertEquals("i4 is 5", 5, i4.getMin());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleExpressionImplicationWithNumericConstraints() {
        try {
            CspConstraint aConstraint = i1.lt(i2);
            BooleanExpr a = new BooleanExpr("a", aConstraint);
            
            CspConstraint bConstraint = i3.eq(i4);
            BooleanExpr b = new BooleanExpr("b", bConstraint);
            
            solver.addConstraint(a.implies(b));

            //Determine domains
            i1.setValue(0);
            i2.setValue(1);
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("a is true", a.isTrue());
            assertFalse("a is not false", a.isFalse());
            assertFalse("b is not true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
            
            //Determine another domain
            i3.setValue(5);
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertEquals("i4 is 5", 5, i4.getMax());
            assertEquals("i4 is 5", 5, i4.getMin());
            assertTrue("b is true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleVariableEqualityWithBooleanConstraints() {
        try {
            CspBooleanExpr aExpr = aVar.eq(bVar);
            BooleanVariable a = new BooleanVariable("a", aExpr);
            
            CspBooleanExpr bExpr = cVar.eq(dVar);
            BooleanVariable b = new BooleanVariable("b", bExpr);
            
            solver.addConstraint(a.eq(b));

            //Determine domains
            aVar.setFalse();
            bVar.setFalse();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("a is true", a.isTrue());
            assertFalse("a is not false", a.isFalse());
            assertTrue("b is true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
            
            //Determine another domain
            dVar.setTrue();
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("cVar isTrue should be true", cVar.isTrue());
            assertFalse("cVar isFalse should be false", cVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleVariableImplicationWithBooleanConstraints() {
        try {
            CspBooleanExpr aExpr = aVar.implies(bVar);
            
            CspBooleanExpr bExpr = cVar.implies(false);
            
            solver.addConstraint(aExpr.implies(bExpr));

            //Determine domains
            aVar.setFalse();
            bVar.setFalse();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("aExpr is true", aExpr.isTrue());
            assertFalse("aExpr is not false", aExpr.isFalse());
            assertTrue("bExpr is true", bExpr.isTrue());
            assertFalse("bExpr is not false", bExpr.isFalse());
            
            //Check
            assertFalse("cVar isTrue should be false", cVar.isTrue());
            assertTrue("cVar isFalse should be true", cVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleExpressionEqualityWithBooleanConstraints() {
        try {
            BooleanExpr aExpr = (BooleanExpr) aVar.eq(bVar);
            BooleanExpr a = new BooleanExpr("a", aExpr.toConstraint());
            
            BooleanExpr bExpr = (BooleanExpr) cVar.eq(dVar);
            BooleanExpr b = new BooleanExpr("b", bExpr.toConstraint());
            
            solver.addConstraint(a.eq(b));

            //Determine domains
            aVar.setFalse();
            bVar.setFalse();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("a is true", a.isTrue());
            assertFalse("a is not false", a.isFalse());
            assertFalse("b is not true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
            
            //Determine another domain
            dVar.setTrue();
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertTrue("cVar isTrue should be true", cVar.isTrue());
            assertFalse("cVar isFalse should be false", cVar.isFalse());
            assertTrue("b is true", b.isTrue());
            assertFalse("b is not false", b.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleVariableEqualityWithBooleanConstraintsOpposite() {
        try {
            CspBooleanExpr aExpr = aVar.eq(bVar);
            BooleanVariable a = new BooleanVariable("a", aExpr);
            
            CspBooleanExpr bExpr = cVar.eq(dVar);
            BooleanVariable b = new BooleanVariable("b", bExpr);
            
            solver.addConstraint(a.eq(b));

            //Determine domains
            aVar.setFalse();
            bVar.setTrue();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertFalse("a is not true", a.isTrue());
            assertTrue("a is false", a.isFalse());
            assertFalse("b is not true", b.isTrue());
            assertTrue("b is false", b.isFalse());
            
            //Determine another domain
            dVar.setTrue();
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertFalse("cVar isTrue should be false", cVar.isTrue());
            assertTrue("cVar isFalse should be true", cVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testSimpleVariableEqualityWithBooleanConstraintsNot() {
        try {
            //TODO how do we fix this the right way, next line is a HACK
//            bVar.getNode().setChoicePointStack(new ChoicePointStack());
            CspBooleanExpr aExpr = aVar.eq(bVar.not());
            BooleanVariable a = new BooleanVariable("a", aExpr);
            
            CspBooleanExpr bExpr = cVar.eq(dVar);
            BooleanVariable b = new BooleanVariable("b", bExpr);
            
            solver.addConstraint(a.eq(b));

            //Determine domains
            aVar.setFalse();
            bVar.setFalse();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertFalse("a is not true", a.isTrue());
            assertTrue("a is false", a.isFalse());
            assertFalse("b is not true", b.isTrue());
            assertTrue("b is false", b.isFalse());
            
            //Determine another domain
            dVar.setTrue();
            
            //Propagate again
            assertTrue("propagation succeeds", solver.propagate());
            
            //Check
            assertFalse("cVar isTrue should be false", cVar.isTrue());
            assertTrue("cVar isFalse should be true", cVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
