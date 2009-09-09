package jopt.csp.test.util;

import jopt.csp.CspSolver;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.util.DoubleUtil;
import jopt.csp.util.FloatUtil;
import jopt.csp.variable.CspVariableFactory;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints
 * 
 * @author Chris Johnson
 */
public class NumUtilsTest extends TestCase {

	CspSolver solver;
	CspVariableFactory varFactory;
	MutableNumber randInt;
	MutableNumber randLong;
	MutableNumber randDouble;
	MutableNumber randFloat;
	double precision;
	
	public void setUp () {
	    randInt = new MutableNumber((int)Math.random()/Math.random()/Math.random());
	    randLong = new MutableNumber((long)Math.random()/Math.random()/Math.random()/Math.random());
	    randDouble = new MutableNumber((float)Math.random());
	    randFloat = new MutableNumber((double)Math.random());
	    precision = Math.random()*Math.random()*Math.random();
	}
	
	public void tearDown() {
		solver = null;
		varFactory = null;
		randInt = null;
		randLong = null;
		randDouble = null;
		randFloat = null;
	}
	
	public void testPreviousNext() {
	    float nextFloat = FloatUtil.next(randFloat.floatValue());
	    double nextDouble = DoubleUtil.next(randDouble.doubleValue());
	    assertTrue(nextFloat>randFloat.floatValue());
	    assertTrue(nextDouble>randDouble.doubleValue());
	    float prevFloat = FloatUtil.previous(nextFloat);
	    double prevDouble = DoubleUtil.previous(nextDouble);
	    assertTrue(prevDouble<nextDouble);
	    assertTrue(prevFloat<nextFloat);
	    assertTrue(DoubleUtil.isEqual(prevDouble,randDouble.doubleValue(), precision*precision));
	    assertTrue(FloatUtil.isEqual(prevFloat,randFloat.floatValue(),(float)(precision)));
	}
	
	public void testFloorCeil() {
	    int floatFloor = FloatUtil.intFloor(randFloat.floatValue());
	    int floatCeil = FloatUtil.intCeil(randFloat.floatValue());
	    assertTrue(floatFloor == (floatCeil-1));
	    long lfloatFloor = FloatUtil.longFloor(randFloat.floatValue());
	    long lfloatCeil = FloatUtil.longCeil(randFloat.floatValue());
	    assertTrue(lfloatFloor == (lfloatCeil-1));
	    
	    int doubleFloor = DoubleUtil.intFloor(randDouble.doubleValue());
	    int doubleCeil = DoubleUtil.intCeil(randDouble.doubleValue());
	    assertTrue(doubleFloor == (doubleCeil-1));
	    long ldoubleFloor = DoubleUtil.longFloor(randDouble.doubleValue());
	    long ldoubleCeil = DoubleUtil.longCeil(randDouble.doubleValue());
	    assertTrue(ldoubleFloor == (ldoubleCeil-1));
	}
	
	public void testIntLongEquivalent() {
	    assertFalse(FloatUtil.isIntEquivalent(randFloat.floatValue()));
	    assertFalse(FloatUtil.isLongEquivalent(randFloat.floatValue()));
	    float hugeFloat = (float)Integer.MAX_VALUE;
	    hugeFloat = hugeFloat+2;
//	    assertFalse(FloatUtil.isIntEquivalent(hugeFloat));
	    assertTrue(FloatUtil.isLongEquivalent(hugeFloat));
	    float intFloat = (float)19.0;
	    assertTrue(FloatUtil.isIntEquivalent(intFloat));
	    assertTrue(FloatUtil.isLongEquivalent(intFloat));
	    
	    assertFalse(DoubleUtil.isIntEquivalent(randDouble.doubleValue()));
	    assertFalse(DoubleUtil.isLongEquivalent(randDouble.doubleValue()));
	    double hugeDouble = (double)Integer.MAX_VALUE;
	    hugeDouble = hugeDouble+2;
	    assertFalse(DoubleUtil.isIntEquivalent(hugeDouble));
	    assertTrue(DoubleUtil.isLongEquivalent(hugeDouble));
	    double intDouble = (double)19.0;
	    assertTrue(DoubleUtil.isIntEquivalent(intDouble));
	    assertTrue(DoubleUtil.isLongEquivalent(intDouble));
	    
	}


}
