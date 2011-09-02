package jopt.csp.test.util;

import jopt.csp.util.SortableIntList;
import junit.framework.TestCase;

/**
 * @author Chris Johnson
 */
public class SortableListTest extends TestCase {
    
    SortableIntList intList;
    
    public SortableListTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
        intList = new SortableIntList();
        
        intList.add(5);
        intList.add(2);
        intList.add(7);
        intList.add(4);
        intList.add(1);
    }
    
    public void tearDown() {
        intList = null;
    }
    
    public void testListSort() {
        intList.sort();
        assertEquals("[1, 2, 4, 5, 7]", 1, intList.get(0));
        assertEquals("[1, 2, 4, 5, 7]", 2, intList.get(1));
        assertEquals("[1, 2, 4, 5, 7]", 4, intList.get(2));
        assertEquals("[1, 2, 4, 5, 7]", 5, intList.get(3));
        assertEquals("[1, 2, 4, 5, 7]", 7, intList.get(4));
    }
    
    public void testListReverse() {
        intList.reverse();
        assertEquals("[1, 4, 7, 2, 5]", 1, intList.get(0));
        assertEquals("[1, 4, 7, 2, 5]", 4, intList.get(1));
        assertEquals("[1, 4, 7, 2, 5]", 7, intList.get(2));
        assertEquals("[1, 4, 7, 2, 5]", 2, intList.get(3));
        assertEquals("[1, 4, 7, 2, 5]", 5, intList.get(4));
    }
    
}
