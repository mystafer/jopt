/*
 * GenericIntTest.java
 * 
 * Created on Apr 20, 2005
 */
package jopt.csp.test.variable;

import java.util.Arrays;

import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import junit.framework.TestCase;

/**
 * Unit tests for generic int variables
 */
public class GenericNumConstantTest extends TestCase {

    GenericIndex idxI;
    GenericIndex idxJ;
    GenericIntConstant intConst;
    GenericIntConstant intConst2I;
    GenericIntConstant intConst2J;
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		idxI = new GenericIndex("idxI",4);
		idxJ = new GenericIndex("idxJ",3);
        intConst = new GenericIntConstant("GI",new GenericIndex[]{idxI},new int[]{1,2,3,4});
        intConst2I = new GenericIntConstant("GI",new GenericIndex[]{idxI},new int[]{11,13,15,17});
        intConst2J = new GenericIntConstant("GJ",new GenericIndex[]{idxJ},new int[]{3,6,9});
    }

    /**
     * Tests that the add function works properly
     */
    public void testAdd() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.add(new Integer(7));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1+7=8",newGI.getIntegerForIndex(),new Integer(8));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2+7=9",newGI.getIntegerForIndex(),new Integer(9));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3+7=10",newGI.getIntegerForIndex(),new Integer(10));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4+7=11",newGI.getIntegerForIndex(),new Integer(11));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the add function works properly
     */
    public void testGenericAdd() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.add(intConst2I);
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1+11=12",new Integer(12),newGI.getIntegerForIndex());
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2+13=15",new Integer(15),newGI.getIntegerForIndex());
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3+15=18",new Integer(18),newGI.getIntegerForIndex());
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4+17=21",new Integer(21),newGI.getIntegerForIndex());
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the add function works properly
     */
    public void testGenericAddDiffIndex() {
        GenericIntConstant newGIJ = (GenericIntConstant)intConst2I.add(intConst2J);
        IndexIterator iter= new IndexIterator(Arrays.asList(newGIJ.getIndices()));
        iter.reset();
        iter.next();
        assertEquals("The first of new item should be 3+11=14",new Integer(14),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 3+13=16",new Integer(16),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 3+15=18",new Integer(18),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 3+17=20",new Integer(20),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 6+11=14",new Integer(17),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 6+13=16",new Integer(19),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 6+15=18",new Integer(21),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 6+17=20",new Integer(23),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 9+11=14",new Integer(20),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 9+13=16",new Integer(22),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 9+15=18",new Integer(24),newGIJ.getIntegerForIndex());
        iter.next();
        assertEquals("The first of new item should be 9+17=20",new Integer(26),newGIJ.getIntegerForIndex());
    }
    
    public void testExpression1() {
        GenericIntConstant newSubtractGIJ = (GenericIntConstant)intConst2I.subtract(intConst2J);
        GenericIntConstant newSubtractGJI = (GenericIntConstant)intConst2J.subtract(intConst2I);
        GenericIntConstant oppGJI = (GenericIntConstant)newSubtractGJI.subtractFrom(new Integer(0));
        IndexIterator iter= new IndexIterator(Arrays.asList(newSubtractGIJ.getIndices()));
        iter.reset();
        while(iter.hasNext()){
            iter.next();
            assertEquals("These values should be equal", oppGJI.getNumberForIndex(),newSubtractGIJ.getNumberForIndex());
        }
    }
    
    public void testExpression2() {
        GenericIntConstant expr1= (GenericIntConstant)(intConst2I.add(intConst2J)).multiply(intConst);
        GenericIntConstant expr2 = (GenericIntConstant)(intConst2I.multiply(intConst)).add(intConst2J.multiply(intConst));
        IndexIterator iter= new IndexIterator(Arrays.asList(expr1.getIndices()));
        iter.reset();
        while(iter.hasNext()){
            iter.next();
            assertEquals("These values should be equal", expr1.getNumberForIndex(),expr2.getNumberForIndex());
        }
    }
    
    /**
     * Tests that the subtract function works properly
     */
    public void testSubtractFrom() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.subtractFrom(new Integer(7));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 7-1=6",newGI.getIntegerForIndex(),new Integer(6));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 7-2=5",newGI.getIntegerForIndex(),new Integer(5));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 7-3=4",newGI.getIntegerForIndex(),new Integer(4));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 7-4=3",newGI.getIntegerForIndex(),new Integer(3));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the subtract function works properly
     */
    public void testSubtractFromGeneric() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.subtractFrom(intConst2I);
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 11-1=10",newGI.getIntegerForIndex(),new Integer(10));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 13-2=11",newGI.getIntegerForIndex(),new Integer(11));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 17-4=13",newGI.getIntegerForIndex(),new Integer(13));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the subtract function works properly
     */
    public void testSubtract() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.subtract(new Integer(7));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1-7=-6",newGI.getIntegerForIndex(),new Integer(-6));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2-7=-5",newGI.getIntegerForIndex(),new Integer(-5));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3-7=-4",newGI.getIntegerForIndex(),new Integer(-4));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4-7=-3",newGI.getIntegerForIndex(),new Integer(-3));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the subtract function works properly
     */
    public void testSubtractGeneric() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.subtract(intConst2I);
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1-11=-10",newGI.getIntegerForIndex(),new Integer(-10));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2-13=-11",newGI.getIntegerForIndex(),new Integer(-11));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3-15=-12",newGI.getIntegerForIndex(),new Integer(-12));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4-17=-13",newGI.getIntegerForIndex(),new Integer(-13));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the multiply function works properly
     */
    public void testMultiply() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.multiply(new Integer(7));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1*7=7",newGI.getIntegerForIndex(),new Integer(7));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2*7=14",newGI.getIntegerForIndex(),new Integer(14));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3*7=21",newGI.getIntegerForIndex(),new Integer(21));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4*7=28",newGI.getIntegerForIndex(),new Integer(28));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the multiply function works properly
     */
    public void testMultiplyGeneric() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.multiply(intConst2I);
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1*11=11",newGI.getIntegerForIndex(),new Integer(11));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2*13=26",newGI.getIntegerForIndex(),new Integer(26));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3*15=45",newGI.getIntegerForIndex(),new Integer(45));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4*17=68",newGI.getIntegerForIndex(),new Integer(68));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the divide by ceiling function works properly
     */
    public void testDivideByCeil() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.divideByCeil(new Integer(2));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1/2=1",newGI.getIntegerForIndex(),new Integer(1));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2/2=1",newGI.getIntegerForIndex(),new Integer(1));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3/2=2",newGI.getIntegerForIndex(),new Integer(2));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4/2=2",newGI.getIntegerForIndex(),new Integer(2));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the divide by floor function works properly
     */
    public void testDivideByFloor() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.divideByCeil(new Integer(2));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 1/2=0",newGI.getIntegerForIndex(),new Integer(1));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 2/2=1",newGI.getIntegerForIndex(),new Integer(1));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 3/2=1",newGI.getIntegerForIndex(),new Integer(2));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 4/2=2",newGI.getIntegerForIndex(),new Integer(2));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the divide function works properly
     */
    public void testDivide() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.divide(new Integer(24));
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 24/1=24",newGI.getIntegerForIndex(),new Integer(24));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 24/2=12",newGI.getIntegerForIndex(),new Integer(12));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 24/3=8",newGI.getIntegerForIndex(),new Integer(8));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 24/4=6",newGI.getIntegerForIndex(),new Integer(6));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
    
    /**
     * Tests that the divide function works properly
     */
    public void testDivideGeneric() {
        GenericIntConstant newGI = (GenericIntConstant)intConst.divideFloor(intConst2I);
        idxI.reset();
        idxI.next();
        assertEquals("The first of new item should be 11/1=11",newGI.getIntegerForIndex(),new Integer(11));
        assertEquals("The first of original item should still be 1",intConst.getIntegerForIndex(),new Integer(1));
        idxI.next();
        assertEquals("The first of new item should be 13/2=6",newGI.getIntegerForIndex(),new Integer(6));
        assertEquals("The first of original item should still be 2",intConst.getIntegerForIndex(),new Integer(2));
        idxI.next();
        assertEquals("The first of new item should be 15/3=5",newGI.getIntegerForIndex(),new Integer(5));
        assertEquals("The first of original item should still be 3",intConst.getIntegerForIndex(),new Integer(3));
        idxI.next();
        assertEquals("The first of new item should be 17/4=4",newGI.getIntegerForIndex(),new Integer(4));
        assertEquals("The first of original item should still be 4",intConst.getIntegerForIndex(),new Integer(4));
    }
      


}
