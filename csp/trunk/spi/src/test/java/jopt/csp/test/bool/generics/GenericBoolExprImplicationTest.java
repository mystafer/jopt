package jopt.csp.test.bool.generics;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.SummationConstraint;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.variable.CspBooleanExpr;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspGenericBooleanExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test generic constraints with implications between them
 *  
 * @author Chris Johnson
 */
public class GenericBoolExprImplicationTest extends TestCase {
    
    BooleanVariable m1;
    BooleanVariable m2;
    BooleanVariable m3;
    BooleanVariable n1;
    BooleanVariable n2;
    BooleanVariable n3;
    BooleanVariable o1;
    BooleanVariable o2;
    BooleanVariable o3;
    BooleanVariable p1;
    BooleanVariable p2;
    BooleanVariable p3;
    
    IntVariable x1;
	IntVariable x2;
	IntVariable x3;
    IntVariable xj1;
	IntVariable xj2;
	IntVariable xj3;
	IntVariable x11;
	IntVariable x12;
	IntVariable x13;
	IntVariable x21;
	IntVariable x22;
	IntVariable x23;
	IntVariable x31;
	IntVariable x32;
	IntVariable x33;
	IntVariable y11;
	IntVariable y12;
	IntVariable y13;
	IntVariable y21;
	IntVariable y22;
	IntVariable y23;
	IntVariable y31;
	IntVariable y32;
	IntVariable y33;	
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z1;
	IntVariable z2;
	IntVariable z3;
	
	IntVariable z11;
	IntVariable z12;
	IntVariable z13;
	IntVariable z21;
	IntVariable z22;
	IntVariable z23;
	IntVariable z31;
	IntVariable z32;
	IntVariable z33;
	

	
	GenericBooleanExpr miexpr;
	GenericBooleanExpr niexpr;
	GenericBooleanExpr oiexpr;
	GenericBooleanExpr piexpr;
	
	GenericIntExpr xiexpr;
	GenericIntExpr xjexpr;
	GenericIntExpr xijexpr;
	GenericIntExpr yijexpr;
	GenericIntExpr yikexpr;
	GenericIntExpr yiexpr;
	GenericIntExpr yjexpr;
	GenericIntExpr ziexpr;
	GenericIntExpr zkexpr;
	GenericIntExpr zikexpr;
	GenericIntExpr zjkexpr;
	IntVariable x;
	IntVariable y;
	IntVariable z;
	GenericIntExpr zijkexpr;
	
	GenericIntConstant tkconst;
	GenericIntConstant xiconst;
	GenericIntConstant yiconst;
	
	CspSolver solver;
	CspVariableFactory varFactory;
	CspMath varMath;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
    
    public GenericBoolExprImplicationTest(java.lang.String testName) {
        super(testName);
    }

    public void setUp () {
        solver = CspSolver.createSolver();
        solver.setAutoPropagate(false);
        varFactory = solver.getVarFactory();
        varMath = varFactory.getMath();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
		
		m1 = new BooleanVariable("m1");
		m2 = new BooleanVariable("m2");
		m3 = new BooleanVariable("m3");
		n1 = new BooleanVariable("n1");
		n2 = new BooleanVariable("n2");
		n3 = new BooleanVariable("n3");
		o1 = new BooleanVariable("o1");
		o2 = new BooleanVariable("o2");
		o3 = new BooleanVariable("o3");
		p1 = new BooleanVariable("p1");
		p2 = new BooleanVariable("p2");
		p3 = new BooleanVariable("p3");
		
        x1 = new IntVariable("x1", 0, 1);
        x2 = new IntVariable("x2", 0, 1);
        x3 = new IntVariable("x3", 0, 1);
        xj1 = new IntVariable("x1", 0, 1);
        xj2 = new IntVariable("x2", 0, 1);
        xj3 = new IntVariable("x3", 0, 1);
        x11 = new IntVariable("x11", 0, 1);
        x12 = new IntVariable("x12", 0, 1);
        x13 = new IntVariable("x13", 0, 1);        
        x21 = new IntVariable("x21", 0, 1);
        x22 = new IntVariable("x22", 0, 1);
        x23 = new IntVariable("x23", 0, 1);
        x31 = new IntVariable("x31", 0, 1);
        x32 = new IntVariable("x32", 0, 1);
        x33 = new IntVariable("x33", 0, 1);
        y11 = new IntVariable("y11", 0, 1);
        y12 = new IntVariable("y12", 0, 1);
        y13 = new IntVariable("y13", 0, 1);        
        y21 = new IntVariable("y21", 0, 1);
        y22 = new IntVariable("y22", 0, 1);
        y23 = new IntVariable("y23", 0, 1);
        y31 = new IntVariable("y31", 0, 1);
        y32 = new IntVariable("y32", 0, 1);
        y33 = new IntVariable("y33", 0, 1); 
        y1 = new IntVariable("y1", 0, 1);
        y2 = new IntVariable("y2", 0, 1);
        y3 = new IntVariable("y3", 0, 1);        
        z1 = new IntVariable("z1", 0, 1);
        z2 = new IntVariable("z2", 0, 1);
        z3 = new IntVariable("z3", 0, 1);
        x = new IntVariable("x", 0, 1);
        y = new IntVariable("y", 0, 1);
        z = new IntVariable("z", 0, 1);
        
        z11 = new IntVariable("z11", 0, 1);
        z12 = new IntVariable("z12", 0, 1);
        z13 = new IntVariable("z13", 0, 1);
        z21 = new IntVariable("z21", 0, 1);
        z22 = new IntVariable("z22", 0, 1);
        z23 = new IntVariable("z23", 0, 1);
        z31 = new IntVariable("z31", 0, 1);
        z32 = new IntVariable("z32", 0, 1);
        z33 = new IntVariable("z33", 0, 1);
        
        miexpr = (GenericBooleanExpr)varFactory.genericBoolean("miexpr", new GenericIndex[]{idxI}, new CspBooleanExpr[]{m1, m2, m3});
        niexpr = (GenericBooleanExpr)varFactory.genericBoolean("niexpr", new GenericIndex[]{idxI}, new CspBooleanExpr[]{n1, n2, n3});
        oiexpr = (GenericBooleanExpr)varFactory.genericBoolean("oiexpr", new GenericIndex[]{idxI}, new CspBooleanExpr[]{o1, o2, o3});
        piexpr = (GenericBooleanExpr)varFactory.genericBoolean("piexpr", new GenericIndex[]{idxI}, new CspBooleanExpr[]{p1, p2, p3});
        
        xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
        xjexpr = (GenericIntExpr)varFactory.genericInt("xj", idxJ, new CspIntVariable[]{xj1, xj2, xj3});
        xijexpr = (GenericIntExpr)varFactory.genericInt("xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13,x21,x22,x23,x31,x32,x33});
        yijexpr = (GenericIntExpr)varFactory.genericInt("yij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yikexpr = (GenericIntExpr)varFactory.genericInt("yik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{y11,y12,y13,y21,y22,y23,y31,y32,y33});
        yiexpr = (GenericIntExpr)varFactory.genericInt("yi", idxI, new CspIntVariable[]{y1, y2, y3});
        yjexpr = (GenericIntExpr)varFactory.genericInt("yj", idxJ, new CspIntVariable[]{y1, y2, y3});
        ziexpr = (GenericIntExpr)varFactory.genericInt("zi", idxI, new CspIntVariable[]{z1, z2, z3});
        zkexpr = (GenericIntExpr)varFactory.genericInt("zk", idxK, new CspIntVariable[]{z1, z2, z3});
        zjkexpr = (GenericIntExpr)varFactory.genericInt("zjk", new GenericIndex[]{idxJ, idxK}, new CspIntVariable[]{z11, z12, z13,z21, z22, z23,z31, z32, z33});
        zikexpr = (GenericIntExpr)varFactory.genericInt("zik", new GenericIndex[]{idxI, idxK}, new CspIntVariable[]{z11, z12, z13,z21, z22, z23,z31, z32, z33});
        
        tkconst= new GenericIntConstant("tkconst",new GenericIndex[]{idxK},new int[]{0,1,3});
        yiconst= new GenericIntConstant("yiconst",new GenericIndex[]{idxI},new int[]{0,1,0});
        xiconst= new GenericIntConstant("xiconst",new GenericIndex[]{idxI},new int[]{0,1,1});
	}
    
    public void tearDown() {
        m1 = null;
        m2 = null;
        m3 = null;
        n1 = null;
        n2 = null;
        n3 = null;
        o1 = null;
        o2 = null;
        o3 = null;
        p1 = null;
        p2 = null;
        p3 = null;
        
        x1 = null;
        x2 = null;
        x3 = null;
        xj1 = null;
        xj2 = null;
        xj3 = null;
        x11 = null;
        x12 = null;
        x13 = null;
        x21 = null;
        x22 = null;
        x23 = null;
        x31 = null;
        x32 = null;
        x33 = null;
        y11 = null;
        y12 = null;
        y13 = null;
        y21 = null;
        y22 = null;
        y23 = null;
        y31 = null;
        y32 = null;
        y33 = null;
        y1 = null;
        y2 = null;
        y3 = null;
        z1 = null;
        z2 = null;
        z3 = null;
        
        z11 = null;
        z12 = null;
        z13 = null;
        z21 = null;
        z22 = null;
        z23 = null;
        z31 = null;
        z32 = null;
        z33 = null;

        miexpr = null;
        niexpr = null;
        oiexpr = null;
        piexpr = null;
        
        xiexpr = null;
        xjexpr = null;
        xijexpr = null;
        yijexpr = null;
        yikexpr = null;
        yiexpr = null;
        yjexpr = null;
        ziexpr = null;
        zkexpr = null;
        zikexpr = null;
        zjkexpr = null;
        x = null;
        y = null;
        z = null;
        zijkexpr = null;
        
        tkconst = null;
        xiconst = null;
        yiconst = null;
        
    	solver = null;
    	varFactory = null;
    	varMath = null;
    	idxI = null;
    	idxJ = null;
    	idxK = null;
    }
    
    public void testGenericNumConstraintImpliesGenericNumConstraintSameIndexPostPartial() {
        try {
            CspConstraint aConstraint = xiexpr.neq(0);
            GenericBooleanExpr aiexpr = (GenericBooleanExpr) varFactory.genericBoolean("aiexpr", new GenericIndex[]{idxI}, aConstraint);
            
            CspConstraint bConstraint = ziexpr.eq(0);
            GenericBooleanExpr biexpr = (GenericBooleanExpr) varFactory.genericBoolean("biexpr", new GenericIndex[]{idxI}, bConstraint);
            
            solver.addConstraint(aiexpr.implies(biexpr));
            
            //Determine domains
            x1.setValue(1);
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            assertTrue("a1 isTrue should be true", aiexpr.getBoolExpression(0).isTrue());
            assertFalse("a1 isFalse should be false", aiexpr.getBoolExpression(0).isFalse());
            assertTrue("b1 isTrue should be true", biexpr.getBoolExpression(0).isTrue());
            assertFalse("b1 isFalse should be false", biexpr.getBoolExpression(0).isFalse());
            assertFalse("biexpr isTrue should be false", biexpr.isTrue());
            assertFalse("biexpr isFalse should be false", biexpr.isFalse());
            assertEquals("z1 is 0", 0, z1.getMax());
            assertEquals("z1 is 0", 0, z1.getMin());
            assertFalse("z2 is not bound", z2.isBound());
            assertFalse("z3 is not bound", z3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericBoolConstraintImpliesGenericBoolConstraintSameIndexPostPartial() {
        try {
            GenericBooleanExpr aiexpr = (GenericBooleanExpr) miexpr.implies(niexpr);
            
            GenericBooleanExpr biexpr = (GenericBooleanExpr) oiexpr.implies(piexpr);
            
            solver.addConstraint(aiexpr.implies(biexpr));
            
            //Determine domains
            m1.setTrue();
            n1.setTrue();
                        
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            assertTrue("a1 isTrue should be true", aiexpr.getBoolExpression(0).isTrue());
            assertFalse("a1 isFalse should be false", aiexpr.getBoolExpression(0).isFalse());
            assertFalse("o1 is not bound", o1.isBound());
            assertFalse("o2 is not bound", o2.isBound());
            assertFalse("o3 is not bound", o3.isBound());
            assertFalse("p1 is not bound", p1.isBound());
            assertFalse("p2 is not bound", p2.isBound());
            assertFalse("p3 is not bound", p3.isBound());
            
            //Determine another domain
            o1.setTrue();
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());

            assertTrue("p1 isTrue should be true", p1.isTrue());
            assertFalse("p1 isFalse should be false", p1.isFalse());
            assertFalse("o2 is not bound", o2.isBound());
            assertFalse("o3 is not bound", o3.isBound());
            assertFalse("p2 is not bound", p2.isBound());
            assertFalse("p3 is not bound", p3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    

    
    public void testGenericNumConstraintImpliesGenericNumConstraintSameIndexPostOpposite() {
        try {
            CspConstraint aConstraint = xiexpr.neq(0);
            GenericBooleanExpr aiexpr = (GenericBooleanExpr) varFactory.genericBoolean("aiexpr", new GenericIndex[]{idxI}, aConstraint);
            solver.addConstraint(new BooleanEqTwoVarConstraint(aiexpr, (GraphConstraint) aConstraint));
            
            CspConstraint bConstraint = ziexpr.eq(0);
            GenericBooleanExpr biexpr = (GenericBooleanExpr) varFactory.genericBoolean("biexpr", new GenericIndex[]{idxI}, bConstraint);
            solver.addConstraint(new BooleanEqTwoVarConstraint(biexpr, (GraphConstraint) bConstraint));
            
            solver.addConstraint(aiexpr.implies(biexpr));
            
            //Determine domains
            z1.setValue(1);
            
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertTrue("b1 isFalse should be true", biexpr.getBoolExpression(0).isFalse());
            assertFalse("a1 isTrue should be false", aiexpr.getBoolExpression(0).isTrue());
            assertTrue("a1 isFalse should be true", aiexpr.getBoolExpression(0).isFalse());
            assertEquals("x1 is 0", 0, x1.getMax());
            assertEquals("x1 is 0", 0, x1.getMin());
            assertFalse("x2 is not bound", x2.isBound());
            assertFalse("x3 is not bound", x3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testGenericBoolConstraintImpliesGenericBoolConstraintSameIndexPostOpposite() {
        try {
            GenericBooleanExpr aiexpr = (GenericBooleanExpr) miexpr.implies(false);
            
            GenericBooleanExpr biexpr = (GenericBooleanExpr) oiexpr.implies(piexpr);
            
            solver.addConstraint(aiexpr.implies(biexpr));
            
            //Determine domains
            o1.setTrue();
            p1.setFalse();
                        
            //Propagate
            assertTrue("propagation succeeds", solver.propagate());
            
            assertFalse("b1 isTrue should be false", biexpr.getBoolExpression(0).isTrue());
            assertTrue("b1 isFalse should be true", biexpr.getBoolExpression(0).isFalse());
            assertTrue("m1 isTrue should be true", m1.isTrue());
            assertFalse("m1 isFalse should be false", m1.isFalse());
            assertFalse("m2 is not bound", m2.isBound());
            assertFalse("m3 is not bound", m3.isBound());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}
    }
    
    public void testConstraintFrag() {
        GraphConstraint xConstraint = (GraphConstraint)xiexpr.eq(xiconst);
        GraphConstraint yConstraint = (GraphConstraint)yiexpr.eq(yiconst);
        GenericBooleanExpr gbe1 = new GenericBooleanExpr("left",new GenericIndex[]{idxI},xConstraint);
        GenericBooleanExpr gbe2 = new GenericBooleanExpr("right",new GenericIndex[]{idxI},yConstraint);
        idxI.reset();
        idxI.next();
        
        try{
            solver.addConstraint(gbe1.implies(gbe2));
//            solver.addConstraint(gbe1Equiv.implies(gbe2Equiv));
//            solver.addConstraint(xExpr1.implies(yExpr1));
//            solver.addConstraint(xExpr1Equiv.implies(yExpr1Equiv));
            x1.setValue(0);
            solver.propagate();
            assertTrue("y1 should now be bound", y1.isBound());
            assertEquals("y1 must equal 0",0,y1.getMin());
            assertEquals("y1 must equal 0",0,y1.getMax());
            x2.setValue(0);
            solver.propagate();
            assertFalse("y2 should not be bound", y2.isBound());
            assertEquals("y2 must equal 0..1",0,y2.getMin());
            assertEquals("y2 must equal 0..1",1,y2.getMax());
            y2.setValue(0);
            y3.setValue(1);
            solver.propagate();
            assertTrue("x3 should be bound", x3.isBound());
            assertEquals("x3 must equal 0",0,x3.getMin());
            assertEquals("x3 must equal 0",0,x3.getMax());
            
        }
        catch(PropagationFailureException pfe) {
            fail(pfe.getMessage());
        }
                
        
    }
    
    public void testSumTemp(){
        GenericIntExpr xijsum =(GenericIntExpr)varMath.summation(xiexpr.multiply(xijexpr), new CspGenericIndex[]{idxJ}, null);
        GenericBooleanExpr gbe1 = new GenericBooleanExpr("left",new GenericIndex[]{idxI},xijsum.eq(xiconst));
        
        try {
            
            solver.addConstraint(gbe1);
//            solver.addConstraint(xijsum.eq(xiconst));
//            solver.addConstraint(constraint);
            solver.propagate();
            assertFalse("x1 should not be bound",x1.isBound());
            assertFalse("x11 should not be bound",x11.isBound());
            assertFalse("x12 should not be bound",x12.isBound());
            assertFalse("x13 should not be bound",x13.isBound());
            x1.setValue(0);
            solver.propagate();
            idxI.reset();
            idxI.next();
            assertTrue(gbe1.getBoolExpressionForIndex().isTrue());
//            assertTrue("x1 should be bound",x1.isBound());
//            assertTrue("x11 should be bound",x11.isBound());
//            assertTrue("x12 should be bound",x12.isBound());
//            assertTrue("x13 should be bound",x13.isBound());
            
//            assertEquals("x11 should be 0",0,x11.getMax());
//            assertEquals("x12 should be 0",0,x12.getMax());
//            assertEquals("x13 should be 0",0,x13.getMax());
            
            solver.propagate();
            
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testXprod(){
        GenericIntExpr xiprod = (GenericIntExpr)xiexpr.multiply(yiexpr);
        GenericBooleanExpr gbe1 = new GenericBooleanExpr("left",new GenericIndex[]{idxI}, xiprod.eq(0));
        
        BooleanVariable b1 = new BooleanVariable("b1");
        BooleanVariable b2 = new BooleanVariable("b2");
        BooleanVariable b3 = new BooleanVariable("b3");
        BooleanExpr[]  boolExprs = new BooleanExpr[]{b1,b2,b3};
        GenericBooleanExpr gbe2 = new GenericBooleanExpr("right",new GenericIndex[]{idxI},boolExprs);
        try{
            solver.addConstraint(gbe1.implies(gbe2));
            solver.propagate();
            y1.setValue(0);
            solver.propagate();
            assertTrue("b1 should be true",b1.isTrue());
            idxI.changeVal(0);
            assertTrue(xiprod.getExpressionForIndex().isBound());
        }
        catch(Exception e) {
            fail();
        }
    }
    
    
    public void testSumOperationNoSet() {
        IntExpr e1 = (IntExpr)x1.add(y1);
        IntExpr e2 = (IntExpr)x2.add(y2);
        IntExpr e3 = (IntExpr)x3.add(y3);
        GenericIntExpr genSum = new GenericIntExpr("xi+yi", new GenericIndex[]{idxI}, new NumExpr[]{e1,e2,e3});
        try{
            CspConstraint constraint = genSum.neq(2); 
            solver.addConstraint(constraint);
            solver.propagate();
            x1.setValue(1);
            solver.propagate();
            assertTrue("Y1 should be bound to 0",y1.isBound());
            assertEquals("Y1 should be bound to 0",0,y1.getMax());
            
        }
        catch(PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    public void testReverseSummationPropagation2() {
        idxI = new GenericIndex("i", 1);
        idxJ = new GenericIndex("j", 1);
        
        xijexpr = (GenericIntExpr)varFactory.genericInt("~xij", new CspGenericIndex[]{idxI,idxJ}, new CspIntVariable[]{x11});
        IntVariable xVar = new IntVariable("xVar",0,1);
       GenericIntExpr xijsum =(GenericIntExpr)varMath.summation((GenericIntExpr)xVar.multiply(xijexpr), new CspGenericIndex[]{idxI}, null);
        try {
//            CspConstraint constraint = new SummationConstraint(xijsum,new GenericIndex[]{idxJ},null, new Integer(1), NumConstants.NEQ);
            CspConstraint constraint = xijsum.neq(1);
            solver.addConstraint(constraint);
            solver.propagate();
            x11.setValue(1);


            solver.propagate();
            assertTrue("xVar should be bound to 0",xVar.isBound());
            assertEquals("xVar should be 0",0,xVar.getMax());
            
        }
        catch( PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    public void testReverseSummationPropagation() {
        idxI = new GenericIndex("i", 1);
        idxJ = new GenericIndex("j", 3);
        xiexpr = (GenericIntExpr)varFactory.genericInt("~xi", idxI, new CspIntVariable[]{x1});
        xijexpr = (GenericIntExpr)varFactory.genericInt("~xij", new GenericIndex[]{idxI, idxJ}, new CspIntVariable[]{x11,x12,x13});
        
        GenericIntExpr xijsum =(GenericIntExpr)varMath.summation(xiexpr.multiply(xijexpr), new CspGenericIndex[]{idxJ}, null);
        CspConstraint constraint = new SummationConstraint(xijsum,new GenericIndex[]{idxJ},null, new Integer(2), NumConstants.NEQ);
        try {
            constraint = xijsum.lt(1);
            solver.addConstraint(constraint);
            solver.propagate();
//            x1.setValue(1);
            x11.setValue(1);
            x12.setValue(1);
            x13.setValue(1);

            solver.propagate();
            assertTrue("x1 should be bound to 0",x1.isBound());
//            assertTrue("x12 should be bound to 1",x12.isBound());
//            assertEquals("x12 should be 0",0,x12.getMin());
            
        }
        catch( PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    
    public void testReverseSummationPropagationProd() {
        
        IntExpr xijsum =(IntExpr)varMath.summation(xiexpr.multiply(yiexpr), new CspGenericIndex[]{idxI}, null);
        try {
            solver.addConstraint(xijsum.neq(0));
            y2.setValue(0);
            y3.setValue(0);
            solver.propagate();
            
            assertTrue("x1 should be bound to 1",x1.isBound());
            assertTrue("y1 should be bound to 1",y1.isBound());
            assertEquals("x1 should be 1", 1, x1.getMin());
            assertEquals("y1 should be 1", 1, y1.getMin());
            
        }
        catch( PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    public void testReverseSummationPropagationSimple() {
        IntExpr xijsum =(IntExpr)varMath.summation(xiexpr, new CspGenericIndex[]{idxI}, null);
        try {
            solver.addConstraint(xijsum.neq(3));
            
            x2.setValue(1);
            x3.setValue(1);
            solver.propagate();
            assertTrue("x1 should be bound to 0",x1.isBound());
            assertEquals("x1 should be 0",0,x1.getMax());
            
        }
        catch( PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
    }
    
    public void testReverseImplies() {
        BooleanExpr b1 = new BooleanExpr("x=1",x.eq(1));
        BooleanExpr b2 = new BooleanExpr("y=0",y.eq(0));
        try {
            solver.addConstraint(b1.implies(b2));
            y.setValue(1);
            solver.propagate();
            assertTrue("b1 should now be false",b1.isFalse());
            assertEquals("x should equal 0", 0, x.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail(pfe.getLocalizedMessage());
        }
        
    }
    
    public void testGenericSummationUsingApi() {
        GenericIntExpr xijsum =(GenericIntExpr)varMath.summation(xiexpr.multiply(xijexpr), new CspGenericIndex[]{idxJ}, null);
        GenericBooleanExpr gbe1 = new GenericBooleanExpr("left",new GenericIndex[]{idxI,idxK},xijsum.eq(tkconst));
        GenericBooleanExpr gbe2 = new GenericBooleanExpr("right",new GenericIndex[]{idxI,idxK},zikexpr.eq(1));
        try{
            CspBooleanExpr entireExpr = gbe1.implies(gbe2);
            solver.addConstraint(entireExpr);
            solver.propagate();
 
            x1.setValue(0);
            solver.propagate();
            assertFalse("gbe1 isTrue is true",gbe1.isTrue());
            assertFalse("gbe1 isFalse is false",gbe1.isFalse());
            assertFalse("gbe2 isTrue is false",gbe2.isTrue());
            assertFalse("gbe2 isFalse is false",gbe2.isFalse());

            
            assertTrue("z11 should be bound now", z11.isBound());
            assertEquals("z11 should now be 1 by implication", 1,z11.getMax());
            assertEquals("z11 should now be 1 by implication", 1,z11.getMin());
            assertFalse("z12 should not be bound now", z12.isBound());
            assertEquals("z12 should still be 0..1", 1,z12.getMax());
            assertEquals("z12 should still be 0..1", 0,z12.getMin());
            assertFalse("z13 should not be bound now", z13.isBound());
            assertEquals("z13 should still be 0..1", 1,z13.getMax());
            assertEquals("z13 should still be 0..1", 0,z13.getMin());

            x2.setValue(1);
            x21.setValue(1);
            x22.setValue(1);
            x23.setValue(1);
            solver.propagate();
            
            assertFalse("z21 should not be bound now", z21.isBound());
            assertEquals("z21 should still be 0..1", 1,z21.getMax());
            assertEquals("z21 should still be 0..1", 0,z21.getMin());
            assertFalse("z22 should not be bound now", z22.isBound());
            assertEquals("z22 should still be 0..1", 1,z22.getMax());
            assertEquals("z22 should still be 0..1", 0,z22.getMin());
            assertTrue("z23 should be bound now", z23.isBound());
            assertEquals("z23 should be 1", 1,z23.getMax());
            assertEquals("z23 should be 1", 1,z23.getMin());
            
            z33.setValue(0);
            x31.setValue(1);
            x32.setValue(1);
            x33.setValue(1);
            solver.propagate();
            
            //These assume perfect domain reduction, which is not guaranteed
//            assertTrue("x3 should be bound now", x3.isBound());
//            assertEquals("x3 should now be 0 by reverse implication", 0,x3.getMax());
//            assertEquals("x3 should now be 0 by reverse implication", 0,x3.getMin());
            
            
            

        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
        
    }
    
    
    public void testGenericBooleanImplication() {
        BooleanVariable l1 = new BooleanVariable("l1",x.eq(0));
        BooleanVariable l2 = new BooleanVariable("l2");
        BooleanVariable l3 = new BooleanVariable("l3");
        BooleanVariable r1 = new BooleanVariable("r1", y.eq(1));
        BooleanVariable r2 = new BooleanVariable("r2");
        BooleanVariable r3 = new BooleanVariable("r3");
        CspGenericBooleanExpr gbe1 = varFactory.genericBoolean("left",idxI,new BooleanVariable[]{l1,l2,l3});
        CspGenericBooleanExpr gbe2 = varFactory.genericBoolean("right",idxI,new BooleanVariable[]{r1,r2,r3});
        try {
            solver.addConstraint(gbe1.implies(gbe2));
            x.setValue(0);
            assertFalse("r1 should not be true", r1.isTrue());
            solver.propagate();
            assertTrue("r1 should be true now", r1.isTrue());
            assertEquals("y should be 1", 1, y.getMin());
            r2.setFalse();
            assertFalse("l1 should not be false", l2.isFalse());
            solver.propagate();
            assertTrue("l1 should be false", l2.isFalse());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        
    }
    
    public void testConstraintBooleanImplication() {
        IntExpr xsum = (IntExpr)varMath.summation(xiexpr,new CspGenericIndex[]{idxI},null);
        BooleanExpr l1 = new BooleanExpr("xsum=1",xsum.eq(1));
        BooleanExpr r1 = new BooleanExpr("y=1",y.eq(1));
        try {
            solver.addConstraint(l1.implies(r1));
            x1.setValue(1);
            x2.setValue(0);
            x3.setValue(0);
            assertFalse("r1 should not be true", r1.isTrue());
            solver.propagate();
            assertTrue("r1 should be true now", r1.isTrue());
            assertTrue("l1 should  be true", l1.isTrue());
            assertTrue(y.isBound());
            assertEquals(1,y.getMax());
            assertEquals(1,y.getMin());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        
    }
    
    
    
    
    public void testnonGenericSummationUsingApi() {
        IntExpr xyisum =(IntExpr)varMath.summation((CspGenericIntExpr)xiexpr.multiply(y), new CspGenericIndex[]{idxI}, null);
        CspConstraint constraint = xyisum.eq(0);
        BooleanExpr b1 = new BooleanExpr ("b1",constraint);
        BooleanExpr b2 = new BooleanExpr ("b2",z.eq(1));
        CspBooleanExpr result = b1.implies(b2);
        try{
            solver.addConstraint(result);
            y.setValue(0);
            solver.propagate();
            assertTrue("xyisum should be bound to zero", xyisum.isBound());
            assertEquals("xyisum should be bound to zero", 0, xyisum.getMax());
            assertFalse("constraint should not be false",constraint.isFalse());
            assertTrue("constraint should be true",constraint.isTrue());
            
            
            assertFalse("b1 is not false", b1.isFalse());
            assertTrue("b1 is now true", b1.isTrue());
            assertTrue("z should be bound now", z.isBound());
            assertEquals("z should be equal to 1",1,z.getMin());
            assertTrue("b2 is now true", b2.isTrue());
        }
        catch(PropagationFailureException pfe) {
            fail();
        }
    }
    

}

