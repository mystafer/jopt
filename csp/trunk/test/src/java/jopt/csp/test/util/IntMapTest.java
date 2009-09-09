package jopt.csp.test.util;

import jopt.csp.util.IntMap;
import junit.framework.TestCase;

/**
 * @author Chris Johnson
 */
public class IntMapTest extends TestCase {
    
    IntMap map;
    
	public IntMapTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
        map = new IntMap();
    }
    
    public void tearDown() {
        map = null;
    }
    
    public void testIntMap() {
        map.put(11, 111);
        map.put(22, 222);
        map.put(33, 333);
        map.put(44, 444);
        assertEquals(4, map.size());
        
        map.remove(33);
        map.remove(44);
        
        int[] keys = map.keySet();
        assertEquals(2, keys.length);
        assertTrue(map.containsKey(11));
        assertTrue(map.containsKey(22));
        assertFalse(map.containsKey(33));
        assertFalse(map.containsKey(44));
    }
    
}
