package jopt.js.test.util;

import jopt.js.api.util.TransitionTimeTable;
import junit.framework.TestCase;

/**
 * Tests the basic funtionality of the TransitionTimeTable class
 * 
 * @author James Boerkoel
 */
public class TransitionTimeTableTest extends TestCase {

    TransitionTimeTable ttt;
    
	public TransitionTimeTableTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
        ttt = null;
        System.gc();
        ttt = TransitionTimeTable.getInstance();
    }
    
    //todo fix this test since it doesnt work in sequence of other tests
	public void testBasicFunctionality() {
		ttt.add(0,1,5);
		
		assertEquals(5,ttt.get(0,1));
		
		ttt.registerOperationID(21,0,0);
		ttt.registerOperationID(23,1,1);
		
		assertEquals(5,ttt.getByOp(21,23));
		
		ttt.registerOperationID(25,2,2);
		ttt.registerOperationID(27,3,3);
		
		ttt.addByOp(25,27,20);
		
		assertEquals(20, ttt.getByOp(25,27));
		assertEquals(20,ttt.get(2,3));
		assertEquals(0,ttt.get(3,2));

	}
        
	public void testBasicFunctionality2() {
		
		ttt.registerOperationID(2,1,1);
		ttt.registerOperationID(4,3,3);
		
		ttt.addByOp(2,4,20);
		ttt.addByOp(4,2,10);
		ttt.addByOp(2,2,Integer.MAX_VALUE);
		ttt.addByOp(4,4,Integer.MIN_VALUE);
		
//		assertEquals(20,ttt.get(1,3));
//		assertEquals(10,ttt.get(3,1));

		assertEquals(20,ttt.getByOp(2,4));
		assertEquals(10,ttt.getByOp(4,2));

	}
	
	public void testBasicFunctionality3() {
		
		
		ttt.add(2,4,20);
		ttt.add(4,2,10);
		ttt.add(2,2,Integer.MAX_VALUE);
		ttt.add(4,4,Integer.MIN_VALUE);
		
		assertEquals(20,ttt.get(2,4));
		assertEquals(10,ttt.get(4,2));
	}
	
}
