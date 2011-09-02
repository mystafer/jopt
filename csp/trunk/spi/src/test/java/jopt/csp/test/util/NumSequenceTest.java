
package jopt.csp.test.util;

import jopt.csp.util.DoubleUtil;
import jopt.csp.util.FloatUtil;
import junit.framework.TestCase;

/**
 * Unit tests for generic indexes
 */
public class NumSequenceTest extends TestCase {

    /**
     * Constructor for GenericIntTest.
     */
    public NumSequenceTest(String name) {
        super(name);
    }
    
    public void testNextAndPreviousDoubleZero() {
        double n = DoubleUtil.next(0);
        double p = DoubleUtil.previous(n);
        assertTrue(0 == p);
    }
    
    public void testNextAndPreviousFloatZero() {
        float n = FloatUtil.next(0);
        float p = FloatUtil.previous(n);
        assertTrue(0 == p);
    }
    
    public void testNextAndPreviousDouble() {
    	for (int i=0; i<100; i++) {
	        double rndmDoub = Math.random();
	        double d1 = DoubleUtil.previous(DoubleUtil.next(rndmDoub));
	        assertTrue(rndmDoub == d1);
	        
	        double d2 = DoubleUtil.next(DoubleUtil.previous(rndmDoub));
	        assertTrue(rndmDoub == d2);
	        assertTrue(d1 == d2);
    	}
    }
    
    public void testNextAndPreviousFloat() {
    	for (int i=0; i<100; i++) {
    		float rndmFloat = (float)Math.random();
    		float f1 = FloatUtil.previous(FloatUtil.next(rndmFloat));
	        assertTrue(rndmFloat == f1);
	        
	        float f2 = FloatUtil.next(FloatUtil.previous(rndmFloat));
	        assertTrue(rndmFloat == f1);
	        assertTrue(f1 == f2);
    	}
    }
}

