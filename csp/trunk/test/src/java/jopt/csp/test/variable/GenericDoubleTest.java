/*
 * GenericIntTest.java
 * 
 * Created on Apr 20, 2005
 */
package jopt.csp.test.variable;

import java.util.ArrayList;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import jopt.csp.variable.CspAlgorithm;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspGenericDoubleExpr;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Unit tests for generic int variables
 */
public class GenericDoubleTest extends TestCase {
    private ConstraintStore cs;
    private CspAlgorithm alg;
    private CspVariableFactory varFactory;

    /**
     * Constructor for GenericIntTest.
     */
    public GenericDoubleTest(String name) {
        super(name);
    }

    /**
     * Tests that iteration over indices is working properly
     */
    public void testIdxLoop() {
        GenericIndex idxI = new GenericIndex("i", 100);
        
        int curI = 0;
        while (idxI.next()) {
        	assertEquals("index next", curI++, idxI.currentVal());
        }
        
        ArrayList l = new ArrayList();
        l.add(idxI);
        IndexIterator idxIterator = new IndexIterator(l);
        curI = 0;
        while (idxIterator.hasNext()) {
            idxIterator.next();
            assertEquals("iterator next", curI++, idxI.currentVal());
        }
        
        GenericIndex idxJ = new GenericIndex("j", 100);
        l.add(idxJ);
        idxIterator = new IndexIterator(l);
        curI = 0;
        int total = 0;
        int curJ = 0;
        while (idxIterator.hasNext()) {
            idxIterator.next();
            assertEquals("i iterator next", curI, idxI.currentVal());
            assertEquals("j iterator next", curJ, idxJ.currentVal());
            
            total++;
            curI = total / 100;
            curJ = total % 100;
        }
    }
    
    /**
     * Basic test for summation constraint Zi = Xi + Yi
     */
    public void testZiEqXiAddYi() {
        try {
            // create basic variables
            CspGenericIndex idxI = varFactory.genericIndex("i", 100);
            CspGenericDoubleExpr xi = varFactory.genericDouble("xi", idxI, 0, 100);
            CspGenericDoubleExpr yi = varFactory.genericDouble("yi", idxI, 0, 100);
            CspGenericDoubleExpr zi = varFactory.genericDouble("zi", idxI, 0, 100);
            
            // initialize x and y variables
            for (int i=0; i<idxI.size(); i++) {
                CspDoubleVariable x = (CspDoubleVariable) xi.getExpression(i);
                x.setValue(i);
    
                CspDoubleVariable y = (CspDoubleVariable) yi.getExpression(i);
                y.setValue(100-i);
            }
            
            // create constraint Zi = Xi +Yi
            cs.addConstraint(zi.eq(xi.add(yi)));
    
            // loop over z values and ensure that they are correct 
            for (int i=0; i<idxI.size(); i++) {
                CspDoubleVariable z = (CspDoubleVariable) zi.getExpression(i);
                assertEquals("z" + i + " min", 100d, z.getMin(), 0.001);
                assertEquals("z" + i + " max", 100d, z.getMax(), 0.001);
            }
        }
        catch(PropagationFailureException propx){
            fail("error propagating constraint");
        }
    }

    /**
     * Basic test for summation constraint Zi <= Xi + Yi
     */
    public void testZiLeqXiAddYi() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            CspGenericIntExpr xi = varFactory.genericInt("xi", new GenericIndex[]{idxI}, 0, 100);
            CspGenericIntExpr yi = varFactory.genericInt("yi", new GenericIndex[]{idxI}, 0, 100);
            CspGenericIntExpr zi = varFactory.genericInt("zi", new GenericIndex[]{idxI}, 0, 100);
            
            // initialize x and y variables
            for (int i=0; i<idxI.size(); i++) {
                CspIntVariable x = (CspIntVariable) xi.getExpression(i);
                x.setValue(i);
    
                CspIntVariable y = (CspIntVariable) yi.getExpression(i);
                y.setValue(i);
            }
            
            // create constraint Zi = Xi +Yi
            cs.addConstraint(zi.leq(xi.add(yi)));
    
            // loop over z values and ensure that they are correct 
            for (int i=0; i<idxI.size(); i++) {
                CspIntVariable z = (CspIntVariable) zi.getExpression(i);
                
                int maxVal = Math.min(100, i*2);
                assertEquals("z" + i + " min", 0, z.getMin());
                assertEquals("z" + i + " max", maxVal, z.getMax());
            }
        }
        catch(PropagationFailureException propx){
            fail("error propagating constraint");
        }
    }


    /**
     * Basic test for summation constraint Zi < Xj
     */
    public void testZiLtXj() {
        try {
            // create basic variables
            GenericIndex idxI = new GenericIndex("i", 100);
            GenericIndex idxJ = new GenericIndex("j", 100);
            CspGenericIntExpr xj = varFactory.genericInt("xj", new GenericIndex[]{idxJ}, 1, 101);
            CspGenericIntExpr zi = varFactory.genericInt("zi", new GenericIndex[]{idxI}, 0, 100);
            
            // create constraint Zi = Xi +Yi
            cs.addConstraint(zi.lt(xj));
            
            // loop and update each xj
            for (int j=0; j<100; j++) {
                CspIntVariable x = (CspIntVariable) xj.getExpression(j);
            	x.setValue(101-j);
                
                for (int i=0; i<idxI.size(); i++) {
                    CspIntVariable z = (CspIntVariable) zi.getExpression(i);
                    
                    assertEquals("z" + idxI.currentVal() + " min", 0, z.getMin());
                    assertEquals("z" + idxI.currentVal() + " max", 100-j, z.getMax());
                }
            }
        }
        catch(PropagationFailureException propx){
            fail("error propagating constraint");
        }
    }
    
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

        cs = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        alg = cs.getConstraintAlg();
        varFactory = alg.getVarFactory();
    }
}
