package jopt.csp.test.function;

import jopt.csp.function.PiecewiseStepFunction;
import junit.framework.TestCase;

/**
 * Tests the PiecewiseStepFunction
 * 
 * @author cjohnson
 */
public class PiecewiseStepFunctionTest extends TestCase {
	
	private final double precision = .0000000001;
	private PiecewiseStepFunction function;
	
    public void setUp() {
    	// start with a function that looks like the following:
    	//4 							    ______
    	//2               __________________
    	//0    ________________________________________
    	//  -10					 0					 10
    	//-2         _____
    	//-4   _____							  _____
    	//
    	function = new PiecewiseStepFunction(-10, 10, 2);
    	function.setValue(-10, -8, -4);
    	function.setValue(-8, -6, -2);
    	function.setValue(6, 8, 4);
    	function.setValue(8, 10, -4);
    }
    
    public void tearDown() {
    	function = null;
    }

    public void testPiecewiseFunctionInterface() {
    	// start and end
    	assertEquals("starts at -10", -10, function.getIntervalMinX(), precision);
    	assertEquals("ends at 10", 10, function.getIntervalMaxX(), precision);
    	
    	// sampling of 'Y' values
    	assertEquals("f(-9) = -4", -4, function.getY(-9), precision);
    	assertEquals("f(-8) = -2", -2, function.getY(-8), precision);
    	assertEquals("f(0) = 2", 2, function.getY(0), precision);
    	assertEquals("f(6) = 4", 4, function.getY(6), precision);
    	assertEquals("f(9) = -4", -4, function.getY(9), precision);
    	assertEquals("f(9.9999999999) = -4", -4, function.getY(9.9999999999), precision);
    	
    	assertEquals("max[f(-10)->f(0)]", 2, function.getMaxY(-10, 0), precision);
    	assertEquals("max[f(0)->f(9)]", 4, function.getMaxY(0, 9), precision);
    	assertEquals("max[f(-10)->f(9.9)]", 4, function.getMaxY(-10, 9.9), precision);
    	
    	assertEquals("min[f(-10)->f(0)]", -4, function.getMinY(-10, 0), precision);
    	assertEquals("min[f(0)->f(9)]", -4, function.getMinY(0, 9), precision);
    	assertEquals("min[f(-10)->f(9.9)]", -4, function.getMinY(-10, 9.9), precision);
	}
    
    public void testIsolateProblem() {
    	PiecewiseStepFunction secondFunction = new PiecewiseStepFunction(-10, 10, 2);
    	secondFunction.setValue(8, 10, -4);
    	
    	secondFunction.setMax(-10, 0, 7);
    	    	
    	assertEquals("f(9)=-4", -4, secondFunction.getY(9), precision);
    }
    
    public void testSimpleModification() {
    	function.setMax(-10, 0, -3);
    	
    	assertEquals("f(-10) = -3", -3, function.getY(-10), precision);
    	assertEquals("f(-9) = -3", -3, function.getY(-9), precision);
    	assertEquals("f(-8) = -2", -2, function.getY(-8), precision);
    	assertEquals("f(0) = 2", 2, function.getY(0), precision);
    	
    	assertEquals("max[f(-10)->f(-8)]", -2, function.getMaxY(-10, -8), precision);
    	assertEquals("max[f(-8)->f(2)]", 2, function.getMaxY(-8, 2), precision);
    	
    	assertEquals("min[f(-10)->f(7)]", -3, function.getMinY(-10, 7), precision);
    	assertEquals("min[f(0)->f(9)]", -4, function.getMinY(0, 9), precision);
    	
    	
    	function.setMin(0, 5, 1);
    	
    	assertEquals("f(0) = 1", 1, function.getY(0), precision);
    	assertEquals("f(4) = 1", 1, function.getY(4), precision);
    	assertEquals("f(5) = 2", 2, function.getY(5), precision);
    	assertEquals("f(9) = -4", -4, function.getY(9), precision);
    	
    	assertEquals("max[f(-10)->f(-8)]", -2, function.getMaxY(-10, -8), precision);
    	assertEquals("max[f(0)->f(4)]", 1, function.getMaxY(0, 4), precision);
    	
    	assertEquals("min[f(0)->f(7)]", 1, function.getMinY(0, 7), precision);
    	assertEquals("min[f(0)->f(9)]", -4, function.getMinY(0, 9), precision);
    }
    
    public void testComplexModification() {
    	function.setMax(-10, 0, 5);
    	function.setMin(0, 10, -5);
    	
    	PiecewiseStepFunction maxFunction = new PiecewiseStepFunction(-10, 10, -7);
    	maxFunction.setValue(-10, 5, 7);
    	function.setMax(maxFunction);
    	
    	assertEquals("f(-10)=7", 7, function.getY(-10), precision);
    	assertEquals("f(0)=7", 7, function.getY(0), precision);
    	assertEquals("f(5)=-5", -5, function.getY(5), precision);
    	assertEquals("f(9)=-5", -5, function.getY(9), precision);
    	
    	assertEquals("max[f(-10)->f(9.9)]", 7, function.getMaxY(-10, 9.9), precision);
    	assertEquals("min[f(-10)->f(9.9)]", -5, function.getMinY(-10, 9.9), precision);
    	
    	
    	function.setMin(-10, 0, -10);
    	function.setMax(0, 10, 10);
    	
    	PiecewiseStepFunction minFunction = new PiecewiseStepFunction(-10, 10, -15);
    	minFunction.setValue(-3, 3, 0);
    	minFunction.setValue(5, 10, 5);
    	function.setMin(minFunction);
    	
    	assertEquals("f(-10)=-15", -15, function.getY(-10), precision);
    	assertEquals("f(-4)=-4", -15, function.getY(-4), precision);
    	assertEquals("f(-3)=-10", -10, function.getY(-3), precision);
    	assertEquals("f(2)=0", 0, function.getY(2), precision);
    	assertEquals("f(5)=5", 5, function.getY(5), precision);
    	assertEquals("f(9.9)=5", 5, function.getY(9.9), precision);
    	
    	assertEquals("max[f(-10)->f(9.9)]", 5, function.getMaxY(-10, 9.9), precision);
    	assertEquals("min[f(-10)->f(9.9)]", -15, function.getMinY(-10, 9.9), precision);
    	
    	
    	function.setMin(-10, 10, -20);
    	
    	function.setSteps(new double[]{-5, 0, 5}, new double[]{2, 4, 6, 8});
    	
    	assertEquals("f(-10)=2", 2, function.getY(-10), precision);
    	assertEquals("f(-4)=4", 4, function.getY(-4), precision);
    	assertEquals("f(0)=6", 6, function.getY(0), precision);
    	assertEquals("f(9)=8", 8, function.getY(9), precision);
    	
    	assertEquals("max[f(-10)->f(9.9)]", 8, function.getMaxY(-10, 9.9), precision);
    	assertEquals("min[f(-10)->f(9.9)]", 2, function.getMinY(-10, 9.9), precision);
    }
    
    public void testAnotherIsolateProblem() {
        PiecewiseStepFunction secondFunction = new PiecewiseStepFunction(0, Double.POSITIVE_INFINITY, 0);
        secondFunction.setValue(1.0, 2.0, 4.0);
        
        secondFunction.setValue(2.0, 3.0, 4.0);
        
        secondFunction.setValue(2.0, 3.0, 4.0);
        
        //assertEquals("f(9)=-4", -4, secondFunction.getY(9), precision);
    }
}
