
package jopt.csp.test.util;

import java.util.HashSet;

import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumConstants;
import jopt.csp.spi.util.NumberMath;
import junit.framework.TestCase;

/**
 * Unit tests for generic indexes
 */
public class NumberMathTest extends TestCase {

    private MutableNumber v1 = new MutableNumber();
    private MutableNumber v2 = new MutableNumber();
    
    /**
     * Constructor for GenericIntTest.
     */
    public NumberMathTest(String name) {
        super(name);
    }
    
    public void testMutableNumberHashCode() {
        HashSet set = new HashSet();
        set.add(new Integer(6));
        set.add(new MutableNumber((int) 6));
        assertTrue(set.size()==1);
        set.add(new Double(6.7));
        set.add(new MutableNumber((double) 6.7));
        assertTrue(set.size()==2);
        set.add(new Float(0.123));
        set.add(new MutableNumber((float) 0.123));
        assertTrue(set.size()==3);
        set.add(new Long(24444446));
        set.add(new MutableNumber((long) 24444446));
        assertTrue(set.size()==4);
    }
    
    public void testAdd() {
        MutableNumber a = new MutableNumber((int)4);
        Double b = new Double(3.7);
        
        v1.setIntValue(7);
        NumberMath.add(a, b, NumConstants.INTEGER, v2);
        assertEquals("integer addition", v1, v2);
        v1.setLongValue(7);
        NumberMath.add(a, b, NumConstants.LONG, v2);
        assertEquals("integer addition", v1, v2);
        v1.setFloatValue(7.7);
        NumberMath.add(a, b, NumConstants.FLOAT, v2);
        assertEquals("integer addition", v1, v2);
        v1.setDoubleValue(7.7);
        NumberMath.add(a, b, NumConstants.DOUBLE, v2);
        assertEquals("integer addition", v1, v2);
    }
    
    public void testAddNoInvalid() {
        MutableNumber a = new MutableNumber((int)4);
        Double b = new Double(3.7);
        
        v1.setIntValue(7);
        NumberMath.addNoInvalid(a, b, NumConstants.INTEGER, v2);
        assertEquals(v1, v2);
        v1.setLongValue(7);
        NumberMath.addNoInvalid(a, b, NumConstants.LONG, v2);
        assertEquals(v1, v2);
        v1.setFloatValue(7.7);
        NumberMath.addNoInvalid(a, b, NumConstants.FLOAT, v2);
        assertEquals(v1, v2);
        v1.setDoubleValue(7.7);
        NumberMath.addNoInvalid(a, b, NumConstants.DOUBLE, v2);
        assertEquals(v1, v2);
        
        Long c = new Long(Long.MAX_VALUE);
        Double d = new Double(Double.MAX_VALUE);
        v1.setInvalid(false);
        NumberMath.addNoInvalid(c, d, NumConstants.INTEGER, v1);
        assertFalse(v1.isInvalid());
        v1.setInvalid(false);
        NumberMath.addNoInvalid(c, d, NumConstants.LONG, v1);
        assertFalse(v1.isInvalid());
        v1.setInvalid(false);
        NumberMath.addNoInvalid(c, d, NumConstants.FLOAT, v1);
        assertFalse(v1.isInvalid());
        v1.setInvalid(false);
        NumberMath.addNoInvalid(c, d, NumConstants.DOUBLE, v1);
        assertFalse(v1.isInvalid());
    }
    
    public void testZero() {
        assertEquals(new Integer((int)0), NumberMath.zero(NumConstants.INTEGER));
        assertEquals(new Long(0), NumberMath.zero(NumConstants.LONG));
        assertEquals(new Float(0), NumberMath.zero(NumConstants.FLOAT));
        assertEquals(new Double(0), NumberMath.zero(NumConstants.DOUBLE));
    }
    
    public void testSubtract() {
        MutableNumber a = new MutableNumber((int)4);
        Double b = new Double(3.7);
        
        v1.setIntValue(1);
        NumberMath.subtract(a, b, NumConstants.INTEGER, v2);
        assertEquals(v1, v2);
        
        v1.setLongValue(1);
        NumberMath.subtract(a, b, NumConstants.LONG, v2);
        assertEquals(v1, v2);
        
        NumberMath.subtract(a, b, NumConstants.FLOAT, v2);
        assertEquals(0.000001, 0.3, v2.doubleValue());
        
        NumberMath.subtract(a, b, NumConstants.DOUBLE, v2);
        assertEquals(0.000000001, 0.3, v2.doubleValue());
    }
    
    public void testSubtractNoInvalid() {
        MutableNumber a = new MutableNumber((int)4);
        Double b = new Double(3.7);
        NumberMath.subtractNoInvalid(a,b,NumConstants.INTEGER,v2);
        assertEquals(null, new MutableNumber((int)1),v2);
        NumberMath.subtractNoInvalid(a,b,NumConstants.LONG,v2);
        assertEquals(null, new MutableNumber((long)1),v2);
        NumberMath.subtractNoInvalid(a,b,NumConstants.DOUBLE,v2);
        assertEquals(0.000000001,0.3,v2.doubleValue());
        
        Long c = new Long(Long.MAX_VALUE);
        Double d = new Double(Double.MIN_VALUE);
        NumberMath.subtractNoInvalid(c,d,NumConstants.INTEGER,v2);
        assertNotNull(v2);
        NumberMath.subtractNoInvalid(c,d,NumConstants.LONG,v2);
        assertNotNull(v2);
        NumberMath.subtractNoInvalid(c,d,NumConstants.FLOAT,v2);
        assertNotNull(v2);
        NumberMath.subtractNoInvalid(c,d,NumConstants.DOUBLE,v2);
        assertNotNull(v2);
    }
    
    public void testMultiply() {
        MutableNumber a = new MutableNumber((int)4);
        Double b = new Double(3.7);
        NumberMath.multiply(a,b,NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber (12),v1);
        NumberMath.multiply(a,b,NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)12),v1);
        NumberMath.multiply(a,b,NumConstants.FLOAT,v1);
        assertEquals(0.000000001,14.8,v1.floatValue());
        NumberMath.multiply(a,b,NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,14.8,v1.doubleValue());
    }
    
    public void testMultiplyNoInvalid() {
        MutableNumber a = new MutableNumber((int)4);
        MutableNumber b = new MutableNumber((double)3.7);
        NumberMath.multiplyNoInvalid(a,b,NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber ((int)12),v1);
        NumberMath.multiplyNoInvalid(a,b,NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber ((long)12),v1);
        NumberMath.multiplyNoInvalid(a,b,NumConstants.FLOAT,v1);
        assertEquals(0.000000001,14.8,v1.floatValue());
        NumberMath.multiplyNoInvalid(a,b,NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,14.8,v1.doubleValue());
        MutableNumber c = new MutableNumber((long)Long.MAX_VALUE);
        MutableNumber d = new MutableNumber((double)Double.MIN_VALUE);
        NumberMath.multiplyNoInvalid(c,d,NumConstants.INTEGER,v1);
        assertNotNull(v1);
        NumberMath.multiplyNoInvalid(c,d,NumConstants.LONG,v1);
        assertNotNull(v1);
        NumberMath.multiplyNoInvalid(c,d,NumConstants.FLOAT,v1);
        assertNotNull(v1);
        NumberMath.multiplyNoInvalid(c,d,NumConstants.DOUBLE,v1);
        assertNotNull(v1);
    }
    
    public void testDivide() {
        MutableNumber a = new MutableNumber((int)44);
        MutableNumber b = new MutableNumber((double)12.3);
//        NumberMath.divide(a,b,NumConstants.INTEGER,v1);
//        assertEquals(null,v1);
//        NumberMath.divide(a,b,NumConstants.LONG,v1);
//        assertEquals(null,v1);
        NumberMath.divide(a,b,NumConstants.FLOAT,v1);
        assertEquals((new MutableNumber(3.5772357f)),v1);
        NumberMath.divide(a,b,NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,3.0769230769230769230769230769231,v1.doubleValue());
    }
    
    public void testDivideCeil() {
        MutableNumber a = new MutableNumber((int)44);
        Double b = new Double(12.3);
        NumberMath.divideCeil(a,b,NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber (4),v1);
        NumberMath.divideCeil(a,b,NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)4),v1);
        NumberMath.divideCeil(a,b,NumConstants.FLOAT,v1);
        assertEquals(0.000000001,3.0769230769230769230769230769231,v1.floatValue());
        NumberMath.divideCeil(a,b,NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,3.0769230769230769230769230769231,v1.doubleValue());
        NumberMath.divideCeil(a,new MutableNumber((int)0),NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber (Integer.MAX_VALUE),v1);
        NumberMath.divideCeil(a,new MutableNumber((int)0),NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber (Long.MAX_VALUE),v1);
    }
    
    public void testDivideFloor() {
        MutableNumber a = new MutableNumber((int)44);
        Double b = new Double(12.3);
        NumberMath.divideFloor(a,b,NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber(3),v1);
        NumberMath.divideFloor(a,b,NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)3),v1);
        NumberMath.divideFloor(a,b,NumConstants.FLOAT,v1);
        assertEquals(0.000000001,3.0769230769230769230769230769231,v1.floatValue());
        NumberMath.divideFloor(a,b,NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,3.0769230769230769230769230769231,v1.doubleValue());
        NumberMath.divideFloor(a,new MutableNumber((int)0),NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber(Integer.MAX_VALUE),v1);
        NumberMath.divideFloor(a,new MutableNumber((int)0),NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber(Long.MAX_VALUE),v1);
    }
    
    public void testIsRealType() {
        assertFalse(NumberMath.isRealType(NumConstants.INTEGER));
        assertFalse(NumberMath.isRealType(NumConstants.LONG));
        assertTrue(NumberMath.isRealType(NumConstants.DOUBLE));
        assertTrue(NumberMath.isRealType(NumConstants.FLOAT));
    }
    
    public void testNumberType() {
        assertEquals(NumConstants.INTEGER, NumberMath.numberType(new MutableNumber((int)0)));
        assertEquals(NumConstants.LONG, NumberMath.numberType(new Long(0)));
        assertEquals(NumConstants.FLOAT, NumberMath.numberType(new Float(0)));
        assertEquals(NumConstants.DOUBLE, NumberMath.numberType(new Double(0)));
        assertEquals(NumConstants.INTEGER, NumberMath.numberType(new MutableNumber((int)0)));
        assertEquals(NumConstants.LONG, NumberMath.numberType(new MutableNumber((long)0)));
        assertEquals(NumConstants.FLOAT, NumberMath.numberType(new MutableNumber((float)0)));
        assertEquals(NumConstants.DOUBLE, NumberMath.numberType(new MutableNumber((double)0)));
    }
    
    public void testNeg() {
        NumberMath.neg(new MutableNumber((int)0),v2);
        assertEquals(null, new MutableNumber((int)-0), v2);
        NumberMath.neg(new Long(23423),v2);
        assertEquals(null, new MutableNumber ((long)-23423), v2);
        NumberMath.neg(new Float(23423.3),v2);
        assertEquals(null, new MutableNumber((float)-23423.3), v2);
        NumberMath.neg(new Double(-1.0001),v2);
        assertEquals(null, new MutableNumber((double)1.0001), v2);
    }
    
    public void testAbs() {
        NumberMath.abs(new MutableNumber((int)0),v2);
        assertEquals(null, new MutableNumber((int)0), v2);
        NumberMath.abs(new Long(23423),v2);
        assertEquals(null, new MutableNumber((long)23423),v2);
        NumberMath.abs(new MutableNumber((int)-10),v2);
        assertEquals(null, new MutableNumber((int)10), v2);
        NumberMath.abs(new Long(-23423),v2);
        assertEquals(null, new MutableNumber((long)23423), v2);
        NumberMath.abs(new Float(-23423.3),v2);
        assertEquals(null, new MutableNumber((float)23423.3), v2);
        NumberMath.abs(new Double(-1.0001),v2);
        assertEquals(null, new MutableNumber((double)1.0001), v2);
    }
    
    //Note that this tests the 4 case tests as well as 2 and 3 case
    public void testMin() {
        NumberMath.min(new MutableNumber((int)7), new Long(3423), new Float(-1.3f), new Double(4.7),v1);
        assertTrue((new MutableNumber((double)-1.3f)).equals(v1,0.000001));
        NumberMath.min(new MutableNumber((int)7), new MutableNumber((int)3423), new MutableNumber((int)-1), new MutableNumber((int)4),v1);
        assertEquals(new MutableNumber((int)-1), v1);
        NumberMath.min(new Long(7), new Long(3423), new Long(-1), new Long(4),v1);
        assertEquals(new MutableNumber((long)-1), v1);
        NumberMath.min(new Double(7), new Double(3423), new Double(-1), new Double(4),v1);
        assertEquals(new MutableNumber((double)-1), v1);
    }
    

    //Note that test 4 case tests 2 and 3 case
    public void testMax() {
        NumberMath.max(new MutableNumber((int)7), new Long(3423), new Float(4.7), new Double(-1.3),v1);
        assertEquals(new MutableNumber((double)3423), v1);
        NumberMath.max(new MutableNumber((int)7), new MutableNumber((int)3423), new MutableNumber((int)-1), new MutableNumber((int)4),v1);
        assertEquals(new MutableNumber((int)3423), v1);
        NumberMath.max(new Long(7), new Long(3423), new Long(-1), new Long(4),v1);
        assertEquals(new MutableNumber((long)3423), v1);
        NumberMath.max(new Float(7), new Float(3423), new Double(-1), new Float(4),v1);
        assertEquals(new MutableNumber((double)3423), v1);
        NumberMath.max(new Double(7), new Double(3423), new Double(-1), new Double(4),v1);
        assertEquals(new MutableNumber((double)3423), v1);
    }
    
    public void testSquare() {
        NumberMath.square(new Double(2),NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)4), v1);
        NumberMath.square(new Double(3.1),NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)9), v1);
        NumberMath.square(new Double(-3.1),NumConstants.FLOAT,v1);
        assertEquals(null, new MutableNumber((float)9.61), v1);
        NumberMath.square(new Double(-3.1),NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,9.61, v1.doubleValue());
    }
    
    public void testSquareNoInvalid() {
        NumberMath.squareNoInvalid(new Double(2),NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)4), v1);
        NumberMath.squareNoInvalid(new Double(3.1),NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)9), v1);
        NumberMath.squareNoInvalid(new Double(-3.1),NumConstants.FLOAT,v1);
        assertEquals(null, new MutableNumber((float)9.61), v1);
        NumberMath.squareNoInvalid(new Double(-3.1),NumConstants.DOUBLE,v1);
        assertEquals(0.000000001,9.61, (v1).doubleValue());
    }
    
    public void testSqrtFloor() {
        NumberMath.sqrtFloor(new MutableNumber((int)8), NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)2), v1);
        NumberMath.sqrtFloor(new MutableNumber((int)9), NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)3), v1);
        NumberMath.sqrtFloor(new MutableNumber((int)9), NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)3), v1);
        NumberMath.sqrtFloor(new MutableNumber((int)10), NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)3), v1);
        NumberMath.sqrtFloor(new MutableNumber((int)10), 2322,v1);
        assertEquals(null, new MutableNumber((double)3.1622776601683793319988935444327), v1);
    }
    
    public void testSqrtCeil() {
        NumberMath.sqrtCeil(new MutableNumber((int)8), NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)3), v1);
        NumberMath.sqrtCeil(new MutableNumber((int)9), NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)3), v1);
        NumberMath.sqrtCeil( new MutableNumber((int)9), NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)3), v1);
        NumberMath.sqrtCeil(new MutableNumber((int)10), NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)4), v1);
        NumberMath.sqrtCeil(new MutableNumber((int)10), 2322,v1);
        assertEquals(null, new MutableNumber((double)3.1622776601683793319988935444327), v1);
    }
    
    public void testSqrt() {
        NumberMath.sqrt(new MutableNumber((int)8), NumConstants.DOUBLE,v1);
        assertEquals(null, new MutableNumber((double)2.8284271247461900976033774484194), v1);
        NumberMath.sqrt(new MutableNumber((int)9), NumConstants.FLOAT,v1);
        assertEquals(null, new MutableNumber((float)3), v1);
        NumberMath.sqrt(new MutableNumber((int)9), NumConstants.DOUBLE,v1);
        assertEquals(null, new MutableNumber((double)3), v1);
        NumberMath.sqrt(new MutableNumber((int)10), NumConstants.FLOAT,v1);
        assertEquals(null, new MutableNumber((float)3.1622776601683793319988935444327), v1);
        NumberMath.sqrt(new MutableNumber((int)9), NumConstants.INTEGER,v1);
        assertEquals(null, new MutableNumber((int)3), v1);
        NumberMath.sqrt(new MutableNumber((int)9), NumConstants.LONG,v1);
        assertEquals(null, new MutableNumber((long)3), v1);
    }
    
    
    
}

