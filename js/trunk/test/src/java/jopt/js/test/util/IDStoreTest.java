package jopt.js.test.util;

import jopt.js.spi.util.IDStore;
import junit.framework.TestCase;

/**
 * Tests the basic funtionality of the Association class including
 * the use of a ChoicePointStack to rollback, record, and push changes
 * 
 * @author Chris Johnson
 */
public class IDStoreTest extends TestCase {

    IDStore idStore;
    
	public IDStoreTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
        idStore = new IDStore();
    }
    
    //todo fix this test since it doesnt work in sequence of other tests
	public void testUniqueIDGeneration() { 
//		assertEquals(1, IDStore.generateUniqueID());
//		assertTrue(IDStore.registerID(2));
//		assertTrue(IDStore.registerID(3));
	}
        

}
