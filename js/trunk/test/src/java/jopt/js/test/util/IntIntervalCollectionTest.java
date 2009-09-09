package jopt.js.test.util;

import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.util.IntIntervalSet;
import jopt.csp.util.IntValIntervalSet;
import jopt.js.api.util.IntIntervalCollection;
import junit.framework.TestCase;

/**
 * @author Chris Johnson
 */
public class IntIntervalCollectionTest extends TestCase {
    
    IntIntervalCollection collection;
    
	public IntIntervalCollectionTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
    	collection = new IntIntervalCollection();
    	collection.setBuilt(true);

    }
    
    public void tearDown() {
    	collection = null;

    }
    
    public void testAddingAndChoicePoint() {
    	ChoicePointStack cps = new ChoicePointStack();
    	collection.setChoicePointStack(cps);
    	cps.push();
    	IntValIntervalSet set1 = new IntValIntervalSet();
    	set1.add(10,20,4);
    	set1.add(15,25,6);
    	collection.set(105,set1);
    	
    	assertEquals (10,collection.getMin());
    	assertEquals (25,collection.getMax());
    	assertEquals(4, collection.getWorth(collection.getFirstIntervalIndex()));
    	assertEquals(10, collection.getWorth(collection.getNextIntervalIndex(collection.getFirstIntervalIndex())));
    	assertEquals(6, collection.getWorth(collection.getLastIntervalIndex()));
    	
    	cps.push();
    	
    	IntIntervalSet set2 = new IntIntervalSet();
    	set2.add(40,60);
    	collection.set(106,set2);
    	
    	assertEquals (10,collection.getMin());
    	assertEquals (60,collection.getMax());
    	assertEquals(4, collection.getWorth(collection.getFirstIntervalIndex()));
    	assertEquals(10, collection.getWorth(collection.getNextIntervalIndex(collection.getFirstIntervalIndex())));
    	assertEquals(1, collection.getWorth(collection.getLastIntervalIndex()));
    	
    	Object delta1 = cps.popDelta();
    	
    	assertEquals (10,collection.getMin());
    	assertEquals (25,collection.getMax());
    	assertEquals(4, collection.getWorth(collection.getFirstIntervalIndex()));
    	assertEquals(10, collection.getWorth(collection.getNextIntervalIndex(collection.getFirstIntervalIndex())));
    	assertEquals(6, collection.getWorth(collection.getLastIntervalIndex()));
    	
    	Object delta2 = cps.popDelta();
    	
    	assertEquals(Integer.MAX_VALUE, collection.getMin());
    	assertEquals(Integer.MIN_VALUE, collection.getMax());
    	
    	cps.pushDelta(delta2);
    	assertEquals (10,collection.getMin());
    	assertEquals (25,collection.getMax());
    	assertEquals(4, collection.getWorth(collection.getFirstIntervalIndex()));
    	assertEquals(10, collection.getWorth(collection.getNextIntervalIndex(collection.getFirstIntervalIndex())));
    	assertEquals(6, collection.getWorth(collection.getLastIntervalIndex()));
    	
    	cps.pushDelta(delta1);
    	
    	assertEquals (10,collection.getMin());
    	assertEquals (60,collection.getMax());
    	assertEquals(4, collection.getWorth(collection.getFirstIntervalIndex()));
    	assertEquals(10, collection.getWorth(collection.getNextIntervalIndex(collection.getFirstIntervalIndex())));
    	assertEquals(1, collection.getWorth(collection.getLastIntervalIndex()));
    	
    }
    

}
