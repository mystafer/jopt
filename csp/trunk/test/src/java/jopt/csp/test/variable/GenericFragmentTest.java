/*
 * GenericFragmentTest.java
 * 
 * Created on Jun 3, 2005
 */
package jopt.csp.test.variable;

import jopt.csp.spi.arcalgorithm.constraint.num.GenericNumExpr;
import jopt.csp.spi.arcalgorithm.constraint.num.NumExpr;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.util.GenericIndex;
import junit.framework.TestCase;

/**
 * Tests creation of fragments for use when generating fragments
 * of constraints
 * 
 * @author Nick Coleman
 */
public class GenericFragmentTest extends TestCase {
    private GenericIndex idxI;
    private GenericIndex idxJ;
    private GenericIndex idxK;
    private GenericIndex idxL;
    private GenericIndex idxM;
    private GenericIndex idxN;
    
    private IntVariable x1;
    private IntVariable x2;
    private IntVariable x3;
    private IntVariable x4;
    private IntVariable x5;
    private IntVariable x6;
    private IntVariable x7;
    private IntVariable x8;
    private IntVariable x9;
    private IntVariable x10;
    private IntVariable x11;
    private IntVariable x12;
    private IntVariable x13;
    private IntVariable x14;
    private IntVariable x15;
    private IntVariable x16;
    private GenericIntExpr xi;
    private GenericIntExpr xij;
    private GenericIntExpr xijk;
    private GenericIntExpr xl;
    private GenericIntExpr xlm;
    private GenericIntExpr xlmn;
    
    public GenericFragmentTest(java.lang.String testName) {
        super(testName);
    }

    public void testFragXi() {
        GenericIndex indices[] = new GenericIndex[]{idxI};
        
        // retrieve X1 fragment
        idxI.changeVal(0);
        NumExpr fragExpr = xi.createFragment(indices);
        assertEquals("x1 fragment", x1, fragExpr);
        
        // retrieve X2 fragment
        idxI.changeVal(1);
        fragExpr = xi.createFragment(indices);
        assertEquals("x2 fragment", x2, fragExpr);
        
        // retrieve X3 fragment
        idxI.changeVal(2);
        fragExpr = xi.createFragment(indices);
        assertEquals("x3 fragment", x3, fragExpr);
        
        // retrieve X4 fragment
        idxI.changeVal(3);
        fragExpr = xi.createFragment(indices);
        assertEquals("x4 fragment", x4, fragExpr);
        
        // retrieve a generic fragment covering all expressions
        fragExpr = xi.createFragment(new GenericIndex[]{});
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        for (int i=0; i<4; i++) {
            idxI.changeVal(i);
            assertEquals("x" + i + " expression for index", xi.getExpressionForIndex(), gexpr.getNumExpressionForIndex());
        }
    }
    
    public void testFragXijforIfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxI};
        
        // retrieve X0j fragment
        idxI.changeVal(0);
        NumExpr fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        
        // X00
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X01
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x2, gexpr.getNumExpressionForIndex());

        // retrieve X1j fragment
        idxI.changeVal(1);
        fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        
        // X10
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X11
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x4, gexpr.getNumExpressionForIndex());

        // retrieve X2j fragment
        idxI.changeVal(2);
        fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        
        // X20
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X21
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x6, gexpr.getNumExpressionForIndex());

        // retrieve X3j fragment
        idxI.changeVal(3);
        fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        
        // X30
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X31
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x8, gexpr.getNumExpressionForIndex());
    }
    
    
    public void testFragXijforJfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxJ};
        
        // retrieve Xi0 fragment
        idxJ.changeVal(0);
        NumExpr fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        
        // X00
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X10
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X20
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X30
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // retrieve Xi1 fragment
        idxJ.changeVal(1);
        fragExpr = xij.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        
        // X01
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X11
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // X21
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X31
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + " expression for index", x8, gexpr.getNumExpressionForIndex());
    }
    
    
    public void testFragXijkforIfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxI};
        
        // retrieve X0jk fragment
        idxI.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxI.reset();
        
        // X000
        idxJ.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X001
        idxJ.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X010
        idxJ.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X011
        idxJ.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x4, gexpr.getNumExpressionForIndex());

        
        // retrieve X1jk fragment
        idxI.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxI.reset();
        
        // X100
        idxJ.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X101
        idxJ.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X110
        idxJ.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X111
        idxJ.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x8, gexpr.getNumExpressionForIndex());

        
        // retrieve X2jk fragment
        idxI.changeVal(2);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxI.reset();
        
        // X200
        idxJ.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X201
        idxJ.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // X210
        idxJ.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // X211
        idxJ.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x12, gexpr.getNumExpressionForIndex());

        
        // retrieve X3jk fragment
        idxI.changeVal(3);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxI.reset();
        
        // X300
        idxJ.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // X301
        idxJ.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // X310
        idxJ.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // X311
        idxJ.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
    
    public void testFragXijkforJfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxJ};
        
        // retrieve Xi0k fragment
        idxJ.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 8", 8, gexpr.getExpressionCount());
        idxJ.reset();
        
        // X000
        idxI.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X001
        idxI.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X100
        idxI.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X101
        idxI.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X200
        idxI.changeVal(2);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X201
        idxI.changeVal(2);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // X300
        idxI.changeVal(3);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // X301
        idxI.changeVal(3);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // retrieve Xi1k fragment
        idxJ.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 8", 8, gexpr.getExpressionCount());
        idxJ.reset();
        
        // X010
        idxI.changeVal(0);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X011
        idxI.changeVal(0);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // X110
        idxI.changeVal(1);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X111
        idxI.changeVal(1);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x8, gexpr.getNumExpressionForIndex());
        
        // X210
        idxI.changeVal(2);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // X211
        idxI.changeVal(2);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x12, gexpr.getNumExpressionForIndex());
        
        // X310
        idxI.changeVal(3);
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // X311
        idxI.changeVal(3);
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
    
    public void testFragXijkforKfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxK};
        
        // retrieve Xij0 fragment
        idxK.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 8", 8, gexpr.getExpressionCount());
        idxK.reset();
        
        // X000
        idxI.changeVal(0);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X010
        idxI.changeVal(0);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X100
        idxI.changeVal(1);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X110
        idxI.changeVal(1);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X200
        idxI.changeVal(2);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X210
        idxI.changeVal(2);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // X300
        idxI.changeVal(3);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // X310
        idxI.changeVal(3);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // retrieve Xij1 fragment
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 8", 8, gexpr.getExpressionCount());
        idxK.reset();
        
        // X001
        idxI.changeVal(0);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X011
        idxI.changeVal(0);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // X101
        idxI.changeVal(1);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X111
        idxI.changeVal(1);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x8, gexpr.getNumExpressionForIndex());
        
        // X201
        idxI.changeVal(2);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // X211
        idxI.changeVal(2);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x12, gexpr.getNumExpressionForIndex());
        
        // X301
        idxI.changeVal(3);
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // X311
        idxI.changeVal(3);
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
    
    public void testFragXijkforIJfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxI, idxJ};
        
        // retrieve X00k fragment
        idxI.changeVal(0);
        idxJ.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X000
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X001
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // retrieve X01k fragment
        idxI.changeVal(0);
        idxJ.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X010
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X011
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // retrieve X10k fragment
        idxI.changeVal(1);
        idxJ.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X100
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X101
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // retrieve X11k fragment
        idxI.changeVal(1);
        idxJ.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X110
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X111
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x8, gexpr.getNumExpressionForIndex());
        
        // retrieve X20k fragment
        idxI.changeVal(2);
        idxJ.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X200
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X201
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // retrieve X21k fragment
        idxI.changeVal(2);
        idxJ.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X210
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // X211
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x12, gexpr.getNumExpressionForIndex());
        
        // retrieve X30k fragment
        idxI.changeVal(3);
        idxJ.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X300
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // X301
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // retrieve X31k fragment
        idxI.changeVal(3);
        idxJ.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxJ.reset();
        
        // X310
        idxK.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // X311
        idxK.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
    
    public void testFragXijkforIKfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxI, idxK};
        
        // retrieve X0j0 fragment
        idxI.changeVal(0);
        idxK.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X000
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X010
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // retrieve X0j1 fragment
        idxI.changeVal(0);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X001
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X011
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // retrieve X1j0 fragment
        idxI.changeVal(1);
        idxK.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X100
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X110
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // retrieve X1j1 fragment
        idxI.changeVal(1);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X101
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X111
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x8, gexpr.getNumExpressionForIndex());
        
        // retrieve X2j0 fragment
        idxI.changeVal(2);
        idxK.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X200
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X210
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // retrieve X2j1 fragment
        idxI.changeVal(2);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X201
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // X211
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x12, gexpr.getNumExpressionForIndex());
        
        // retrieve X3j0 fragment
        idxI.changeVal(3);
        idxK.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X300
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // X310
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // retrieve X3j1 fragment
        idxI.changeVal(3);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 2", 2, gexpr.getExpressionCount());
        idxI.reset();
        idxK.reset();
        
        // X301
        idxJ.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // X311
        idxJ.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
        
    
    public void testFragXijkforJKfrag() {
        GenericIndex indices[] = new GenericIndex[]{idxJ, idxK};
        
        // retrieve Xi00 fragment
        idxJ.changeVal(0);
        idxK.changeVal(0);
        NumExpr fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        idxK.reset();
        
        // X000
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // X100
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x5, gexpr.getNumExpressionForIndex());
        
        // X200
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x9, gexpr.getNumExpressionForIndex());
        
        // X300
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x13, gexpr.getNumExpressionForIndex());
        
        // retrieve Xi01 fragment
        idxJ.changeVal(0);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        idxK.reset();
        
        // X001
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x2, gexpr.getNumExpressionForIndex());
        
        // X101
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x6, gexpr.getNumExpressionForIndex());
        
        // X201
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x10, gexpr.getNumExpressionForIndex());
        
        // X301
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x14, gexpr.getNumExpressionForIndex());
        
        // retrieve Xi10 fragment
        idxJ.changeVal(1);
        idxK.changeVal(0);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        idxK.reset();
        
        // X010
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x3, gexpr.getNumExpressionForIndex());
        
        // X110
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x7, gexpr.getNumExpressionForIndex());
        
        // X210
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x11, gexpr.getNumExpressionForIndex());
        
        // X310
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x15, gexpr.getNumExpressionForIndex());
        
        // retrieve Xi11 fragment
        idxJ.changeVal(1);
        idxK.changeVal(1);
        fragExpr = xijk.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 4", 4, gexpr.getExpressionCount());
        idxJ.reset();
        idxK.reset();
        
        // X011
        idxI.changeVal(0);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x4, gexpr.getNumExpressionForIndex());
        
        // X111
        idxI.changeVal(1);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x8, gexpr.getNumExpressionForIndex());
        
        // X211
        idxI.changeVal(2);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x12, gexpr.getNumExpressionForIndex());
        
        // X311
        idxI.changeVal(3);
        assertEquals(idxI + "," + idxJ + ", " + idxK + " expression for index", x16, gexpr.getNumExpressionForIndex());
    }
    
    public void testFragXlmn() {
    	// retrieve X0 fragment
        GenericIndex indices[] = new GenericIndex[]{idxL};
    	idxL.changeVal(0);
    	NumExpr fragExpr = xl.createFragment(indices);
        assertEquals("frag is X1", x1, fragExpr);

        // retrieve X0m fragment
        idxL.changeVal(0);
        fragExpr = xlm.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        GenericNumExpr gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 1", 1, gexpr.getGenericIndices().length);
        idxL.reset();
        idxM.changeVal(0);
        assertEquals(idxL + "," + idxM + " expression for index", x1, gexpr.getNumExpressionForIndex());

        // retrieve X0mn fragment
        idxL.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 2", 2, gexpr.getGenericIndices().length);
        idxL.reset();
        idxM.changeVal(0);
        idxN.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        
        
        // retrieve Xl0 fragment
        indices = new GenericIndex[]{idxM};
        idxM.changeVal(0);
        fragExpr = xlm.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 1", 1, gexpr.getGenericIndices().length);
        idxM.reset();
        idxL.changeVal(0);
        assertEquals(idxL + "," + idxM + " expression for index", x1, gexpr.getNumExpressionForIndex());

        // retrieve Xl0n fragment
        idxM.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 2", 2, gexpr.getGenericIndices().length);
        idxM.reset();
        idxL.changeVal(0);
        idxN.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // retrieve Xlm0 fragment
        indices = new GenericIndex[]{idxN};
        idxN.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 2", 2, gexpr.getGenericIndices().length);
        idxN.reset();
        idxL.changeVal(0);
        idxM.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());

        
        // retrieve X00n fragment
        indices = new GenericIndex[]{idxL, idxM};
        idxL.changeVal(0);
        idxM.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 1", 1, gexpr.getGenericIndices().length);
        idxL.reset();
        idxM.reset();
        idxN.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());

        
        // retrieve X0m0 fragment
        indices = new GenericIndex[]{idxL, idxN};
        idxL.changeVal(0);
        idxN.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 1", 1, gexpr.getGenericIndices().length);
        idxL.reset();
        idxN.reset();
        idxM.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());
        
        // retrieve Xl00 fragment
        indices = new GenericIndex[]{idxM, idxN};
        idxM.changeVal(0);
        idxN.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 1", 1, gexpr.getGenericIndices().length);
        idxM.reset();
        idxN.reset();
        idxL.changeVal(0);
        assertEquals(idxL + "," + idxM + ", " + idxN + " expression for index", x1, gexpr.getNumExpressionForIndex());

        // retrieve X000 fragment
        indices = new GenericIndex[]{idxL, idxM, idxN};
        idxL.changeVal(0);
        idxM.changeVal(0);
        idxN.changeVal(0);
        fragExpr = xlmn.createFragment(indices);
        assertEquals("frag is X1", x1, fragExpr);

        // retrieve Xlmn fragment
        fragExpr = xlmn.createFragment(new GenericIndex[]{});
        assertTrue("frag is generic", fragExpr instanceof GenericNumExpr);
        gexpr = (GenericNumExpr) fragExpr;
        assertEquals("generic size 1", 1, gexpr.getExpressionCount());
        assertEquals("num indices 3", 3, gexpr.getGenericIndices().length);

    }
    
    protected void setUp() throws Exception {
        super.setUp();

        idxI = new GenericIndex("i", 4);
        idxJ = new GenericIndex("j", 2);
        idxK = new GenericIndex("k", 2);
        idxL = new GenericIndex("l", 1);
        idxM = new GenericIndex("m", 1);
        idxN = new GenericIndex("n", 1);

        x1 = new IntVariable("x1", 0, 100);
        x2 = new IntVariable("x2", 0, 100);
        x3 = new IntVariable("x3", 0, 100);
        x4 = new IntVariable("x4", 0, 100);
        x5 = new IntVariable("x5", 0, 100);
        x6 = new IntVariable("x6", 0, 100);
        x7 = new IntVariable("x7", 0, 100);
        x8 = new IntVariable("x8", 0, 100);
        x9 = new IntVariable("x9", 0, 100);
        x10 = new IntVariable("x10", 0, 100);
        x11 = new IntVariable("x11", 0, 100);
        x12 = new IntVariable("x12", 0, 100);
        x13 = new IntVariable("x13", 0, 100);
        x14 = new IntVariable("x14", 0, 100);
        x15 = new IntVariable("x15", 0, 100);
        x16 = new IntVariable("x16", 0, 100);
        xi = new GenericIntExpr("xi", new GenericIndex[]{idxI}, new NumExpr[]{x1, x2, x3, x4});
        xij = new GenericIntExpr("xij", new GenericIndex[]{idxI, idxJ}, new NumExpr[]{x1, x2, x3, x4, x5, x6, x7, x8});
        xijk = new GenericIntExpr("xijk", new GenericIndex[]{idxI, idxJ, idxK}, new NumExpr[]{x1, x2, x3, x4, x5, x6, x7, x8,
                                                                                              x9, x10, x11, x12, x13, x14, x15, x16});
        xl = new GenericIntExpr("xl", new GenericIndex[]{idxL}, new NumExpr[]{x1});
        xlm = new GenericIntExpr("xlm", new GenericIndex[]{idxL, idxM}, new NumExpr[]{x1});
        xlmn = new GenericIntExpr("xlmn", new GenericIndex[]{idxL, idxM, idxN}, new NumExpr[]{x1});
    }
}
