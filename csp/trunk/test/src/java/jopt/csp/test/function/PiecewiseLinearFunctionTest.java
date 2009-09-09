package jopt.csp.test.function;

import jopt.csp.function.PiecewiseLinearFunction;
import junit.framework.TestCase;

/**
 * Tests the PiecewiseLinearFunction
 * 
 * @author cjohnson
 */
public class PiecewiseLinearFunctionTest extends TestCase {
	
	private final double precision = .0000000001;
	private PiecewiseLinearFunction function;
	
	public void setUp() {
        // start with a function that looks like the following:
        //
        //                                        / (10,10)
        //                                       /
        //                                      /
        //                                     /
        //                                    /
        //                                   /
        //                                  /
        //                                 /
        //                                /
        //           ____________________/
        //   (-5,0) /        (0,0)       (5,0)
        //         /
        //        /
        //       /
        //      /
        //     /
        //    /
        //   /
        //  /
        // /(-10,-10)
		function = new PiecewiseLinearFunction(-10, 10, 0);
		
		function.setSlope(-10, -5, -10, 2);
		function.setSlope(5, 10, 0, 2);
	}
	
	public void tearDown() {
		function = null;
	}
    
	public void testPiecewiseFunctionInterface() {
    	// start and end
    	assertEquals("starts at -10", -10, function.getIntervalMinX(), precision);
    	assertEquals("ends at 10", 10, function.getIntervalMaxX(), precision);
    	
    	// sampling of 'Y' values
    	assertEquals("f(-10) = -10", -10, function.getY(-10), precision);
    	assertEquals("f(-9) = -8", -8, function.getY(-9), precision);
    	assertEquals("f(-5) = 0", 0, function.getY(-5), precision);
    	assertEquals("f(0) = 0", 0, function.getY(0), precision);
    	assertEquals("f(5) = 0", 0, function.getY(5), precision);
    	assertEquals("f(9.9999999999) = 9.9999999998", 9.9999999998, function.getY(9.9999999999), precision);
    	
    	assertEquals("max[f(-10)->f(0)]", 0, function.getMaxY(-10, 0), precision);
    	assertEquals("max[f(0)->f(9.9)]", 9.8, function.getMaxY(0, 9.9), precision);
    	
    	assertEquals("min[f(-10)->f(0)]", -10, function.getMinY(-10, 0), precision);
    	assertEquals("min[f(0)->f(9.9)]", 0, function.getMinY(0, 9.9), precision);
	}
	
	public void testIsolateProblem() {
		PiecewiseLinearFunction secondFunction = new PiecewiseLinearFunction(-10, 10, 0);
		secondFunction.setSlope(5, 10, 0, 2);
		
		secondFunction.setMin(5, 0, 10, 5);
		
		assertEquals("f(0) = 0", 0, secondFunction.getY(0), precision);
    	assertEquals("f(5) = 5", 0, secondFunction.getY(0), precision);
    	assertEquals("f(7) = 2", 2, secondFunction.getY(7), precision);
    	assertEquals("f(9.9) = 4.9", 4.9, secondFunction.getY(9.9), precision);
	}
	
	public void testSimpleModification() {
		function.setMax(-10, -5, 0);
		
		assertEquals("f(-10) = 0", 0, function.getY(-10), precision);
    	assertEquals("f(-9) = 0", 0, function.getY(-9), precision);
    	assertEquals("f(-5) = 0", 0, function.getY(-5), precision);
    	assertEquals("f(0) = 0", 0, function.getY(0), precision);
    	assertEquals("f(5) = 0", 0, function.getY(5), precision);
    	
    	function.setMax(-10, 5, -5, -5);
    	
    	assertEquals("f(-10) = 5", 5, function.getY(-10), precision);
    	assertEquals("f(-9) = 3", 3, function.getY(-9), precision);
    	assertEquals("f(-7.5) = 0", 0, function.getY(-7.5), precision);
    	assertEquals("f(0) = 0", 0, function.getY(0), precision);
    	assertEquals("f(5) = 0", 0, function.getY(5), precision);
    	
    	function.setMin(5, 0, 10, 5);
    	
    	assertEquals("f(0) = 0", 0, function.getY(0), precision);
    	assertEquals("f(5) = 5", 0, function.getY(0), precision);
    	assertEquals("f(7) = 2", 2, function.getY(7), precision);
    	assertEquals("f(9.9) = 4.9", 4.9, function.getY(9.9), precision);
    	
    	function.setY(-1, 1, 1);
    	
    	assertEquals("f(-1.1) = 0", 0, function.getY(-1.1), precision);
    	assertEquals("f(-1) = 1", 1, function.getY(-1), precision);
    	assertEquals("f(.5) = 1", 1, function.getY(.5), precision);
    	assertEquals("f(1) = 0", 0, function.getY(1), precision);
    	
    	assertEquals("max[f(-10)->f(0)]", 5, function.getMaxY(-10, 0), precision);
    	assertEquals("max[f(0)->f(9.9)]", 4.9, function.getMaxY(0, 9.9), precision);
    	assertEquals("max[f(-2)->f(0)]", 1, function.getMaxY(-2, 0), precision);
    	
    	assertEquals("min[f(-10)->f(0)]", 0, function.getMinY(-10, 0), precision);
    	assertEquals("min[f(0)->f(9.9)]", 0, function.getMinY(0, 9.9), precision);
    	assertEquals("min[f(0)->f(2)]", 0, function.getMinY(0, 2), precision);
	}

}
