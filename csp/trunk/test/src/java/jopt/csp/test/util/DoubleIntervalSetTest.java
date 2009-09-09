package jopt.csp.test.util;

import jopt.csp.util.DoubleIntervalSet;
import jopt.csp.util.DoubleIntervalSetListener;
import jopt.csp.util.DoubleUtil;
import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayIntList;

/**
 * @author Chris Johnson
 */
public class DoubleIntervalSetTest extends TestCase {
    
    DoubleIntervalSet set;
    TestListener listener;
    
	public DoubleIntervalSetTest(String testName) {
        super(testName);
    }
    
    public void setUp() {
        set = new DoubleIntervalSet();
        listener = new TestListener();
        set.setListener(listener, 7);
    }
    
    public void tearDown() {
        set = null;
        listener = null;
    }
    
    public void testIntervalAdditionSingle() {
        set.add(0, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 100, listener.addEnds.get(0), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionDoubleOverlapNone() {
        set.add(-50, -25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", -50, listener.addStarts.get(0), 0);
        assertEquals("interval added", -25, listener.addEnds.get(0), 0);
        
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 25, listener.addStarts.get(1), 0);
        assertEquals("interval added", 50, listener.addEnds.get(1), 0);
        
        
        assertEquals("min is -50", -50, set.getMin(), 0);
        assertEquals("max is 50", 50, set.getMax(), 0);
        
        // set is -50..-25  25..50
        for (int i=-50; i<=50; i++) {
            if (i < -24 || i > 24)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("25..50", 25, set.getMin(index), 0);
                    assertEquals("25..50", 50, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("-50..-25", -50, set.getMin(index), 0);
                    assertEquals("-50..-25", -25, set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionDoubleOverlapNoneDiffOrder() {
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 50, listener.addEnds.get(0), 0);

        set.add(-50, -25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", -50, listener.addStarts.get(1), 0);
        assertEquals("interval added", -25, listener.addEnds.get(1), 0);

        
        assertEquals("min is -50", -50, set.getMin(), 0);
        assertEquals("max is 50", 50, set.getMax(), 0);
        
        // set is -50..-25  25..50
        for (int i=-50; i<=50; i++) {
            if (i < -24 || i > 24)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("-50..-25", -50, set.getMin(index), 0);
                    assertEquals("-50..-25", -25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("25..50", 25, set.getMin(index), 0);
                    assertEquals("25..50", 50, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionDoubleOverlapStart() {
        set.add(25, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 100, listener.addEnds.get(0), 0);
        
        set.add(0, 30);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 0, listener.addStarts.get(1), 0);
        assertEquals("interval added", 25, listener.addEnds.get(1), .01);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionDoubleOverlapEnd() {
        set.add(0, 30);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 30, listener.addEnds.get(0), 0);
        
        set.add(25, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 30, listener.addStarts.get(1), .01);
        assertEquals("interval added", 100, listener.addEnds.get(1), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionDoubleOverlapAll() {
        set.add(25, 30);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 30, listener.addEnds.get(0), 0);
        
        set.add(0, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 0, listener.addStarts.get(1), 0);
        assertEquals("interval added", 25, listener.addEnds.get(1), .01);
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 30, listener.addStarts.get(2), .01);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionDoubleNoEffect() {
        set.add(0, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 100, listener.addEnds.get(0), 0);
        
        set.add(25, 75);
        
        assertEquals("no interval added", 1, listener.addCallbacks.size());
        assertEquals("no interval added", 1, listener.addStarts.size());
        assertEquals("no interval added", 1, listener.addEnds.size());
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionDoubleAdjacent() {
        set.add(25, 100);
        
        set.add(0, DoubleUtil.previous(25));
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        // everything is in a single "interval"
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..100", 0, set.getMin(index), 0);
                    assertEquals("0..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleOverlapNone() {
        set.add(-50, -25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", -50, listener.addStarts.get(0), 0);
        assertEquals("interval added", -25, listener.addEnds.get(0), 0);
        
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 25, listener.addStarts.get(1), 0);
        assertEquals("interval added", 50, listener.addEnds.get(1), 0);
        
        set.add(75, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 75, listener.addStarts.get(2), 0);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is -50", -50, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is -50..-25  25..50  75..100
        for (int i=-50; i<=100; i++) {
            if ((i < -24 ) || (i >= 25 && i <= 50) || (i > 74))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("-50..-25", -50, set.getMin(index), 0);
                    assertEquals("-50..-25", -25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("25..50", 25, set.getMin(index), 0);
                    assertEquals("25..50", 50, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleOverlapNoneDiffOrder() {
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 50, listener.addEnds.get(0), 0);
        
        set.add(-50, -25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", -50, listener.addStarts.get(1), 0);
        assertEquals("interval added", -25, listener.addEnds.get(1), 0);
        
        set.add(75, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 75, listener.addStarts.get(2), 0);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is -50", -50, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is -50..-25  25..50  75..100
        for (int i=-50; i<=100; i++) {
            if ((i < -24 ) || (i >= 25 && i <= 50) || (i > 74))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("25..50", 25, set.getMin(index), 0);
                    assertEquals("25..50", 50, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("-50..-25", -50, set.getMin(index), 0);
                    assertEquals("-50..-25", -25, set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleOverlapStartOne() {
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 50, listener.addEnds.get(0), 0);
        
        set.add(0, 30);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 0, listener.addStarts.get(1), 0);
        assertEquals("interval added", 25, listener.addEnds.get(1), .01);
        
        set.add(75, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 75, listener.addStarts.get(2), 0);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..50  75..100
        for (int i=0; i<=100; i++) {
            if (i <= 50 || i >= 75)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..50", 0, set.getMin(index), 0);
                    assertEquals("0..50", 50, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleOverlapStartBoth() {
        set.add(50, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 50, listener.addStarts.get(0), 0);
        assertEquals("interval added", 100, listener.addEnds.get(0), 0);
        
        set.add(25, 75);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 25, listener.addStarts.get(1), 0);
        assertEquals("interval added", 50, listener.addEnds.get(1), .01);
        
        set.add(0, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 0, listener.addStarts.get(2), 0);
        assertEquals("interval added", 25, listener.addEnds.get(2), .01);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..100
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionTripleOverlapStartOneEndOne() {
        set.add(50, 75);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 50, listener.addStarts.get(0), 0);
        assertEquals("interval added", 75, listener.addEnds.get(0), 0);
        
        set.add(0, 55);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 0, listener.addStarts.get(1), 0);
        assertEquals("interval added", 50, listener.addEnds.get(1), .01);
        
        set.add(70, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 75, listener.addStarts.get(2), .01);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..100
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionTripleOverlapEndOne() {
        set.add(50, 75);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 50, listener.addStarts.get(0), 0);
        assertEquals("interval added", 75, listener.addEnds.get(0), 0);
        
        set.add(70, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 75, listener.addStarts.get(1), .01);
        assertEquals("interval added", 100, listener.addEnds.get(1), 0);
        
        set.add(0, 25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 0, listener.addStarts.get(2), 0);
        assertEquals("interval added", 25, listener.addEnds.get(2), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..25  50..100
        for (int i=0; i<=100; i++) {
            if (i <= 25 || i >= 50)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..100", 50, set.getMin(index), 0);
                    assertEquals("50..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleOverlapEndBoth() {
        set.add(0, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 50, listener.addEnds.get(0), 0);
        
        set.add(25, 75);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 50, listener.addStarts.get(1), .01);
        assertEquals("interval added", 75, listener.addEnds.get(1), 0);
        
        set.add(50, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 75, listener.addStarts.get(2), .01);
        assertEquals("interval added", 100, listener.addEnds.get(2), 0);        
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..100
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionTripleOverlapAll() {
        set.add(25, 50);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 25, listener.addStarts.get(0), 0);
        assertEquals("interval added", 50, listener.addEnds.get(0), 0);
        
        set.add(55, 75);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 55, listener.addStarts.get(1), 0);
        assertEquals("interval added", 75, listener.addEnds.get(1), 0);
        
        set.add(0, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 0, listener.addStarts.get(2), 0);
        assertEquals("interval added", 25, listener.addEnds.get(2), .01);
        assertEquals("interval added", 7, listener.addCallbacks.get(3));
        assertEquals("interval added", 50, listener.addStarts.get(3), .01);
        assertEquals("interval added", 55, listener.addEnds.get(3), .01);
        assertEquals("interval added", 7, listener.addCallbacks.get(4));
        assertEquals("interval added", 75, listener.addStarts.get(4), .01);
        assertEquals("interval added", 100, listener.addEnds.get(4), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..100
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionTripleOverlapAllExact() {
        set.add(0, 25);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 25, listener.addEnds.get(0), 0);
        
        set.add(75, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 75, listener.addStarts.get(1), 0);
        assertEquals("interval added", 100, listener.addEnds.get(1), 0);
        
        set.add(0, 100);
        
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 25, listener.addStarts.get(2), .01);
        assertEquals("interval added", 75, listener.addEnds.get(2), .01);        
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..100
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalAdditionTripleAdjacentOne() {
        set.add(0, 25);
        
        set.add(75, 100);
        
        set.add(DoubleUtil.next(25), 50);
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..50, 75..100
        for (int i=0; i<=100; i++) {
            if (i < 51 || i > 74)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        // everything is in a single "interval"
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("0..50", 0, set.getMin(index), 0);
                    assertEquals("0..50", 50, set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleAdjacentBoth() {
        set.add(0, 25);
        
        set.add(75, 100);
        
        set.add(DoubleUtil.next(25), DoubleUtil.previous(75));
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        // everything is in a single "interval"
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..100", 0, set.getMin(index), 0);
                    assertEquals("0..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalAdditionTripleAdjacentOneOverlapOther() {
        set.add(0, 25);
        
        set.add(75, 100);
        
        set.add(22, DoubleUtil.previous(75));
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set contains everything between 0 and 100 inclusive
        for (int i=0; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        // everything is in a single "interval"
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..100", 0, set.getMin(index), 0);
                    assertEquals("0..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalInner() {
        set.add(0, 100);
        
        set.remove(25, 75);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 25, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..24  76..100
        for (int i=0; i<=100; i++) {
            if (i < 25 || i > 75)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..24", 0, set.getMin(index), 0);
                    assertEquals("0..24", DoubleUtil.previous(25), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("76..100", DoubleUtil.next(75), set.getMin(index), 0);
                    assertEquals("76..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalEnd() {
        set.add(0, 100);
        
        set.remove(75, 125);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 75, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(0), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 74", DoubleUtil.previous(75), set.getMax(), 0);
        
        // set is 0..74
        for (int i=0; i<=74; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalRemovalEndExact() {
        set.add(0, 100);
        
        set.remove(75, 100);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 75, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(0), 0);
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 74", DoubleUtil.previous(75), set.getMax(), 0);
        
        // set is 0..74
        for (int i=0; i<=74; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalRemovalStart() {
        set.add(0, 100);
        
        set.remove(-25, 25);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        
        
        assertEquals("min is 26", DoubleUtil.next(25), set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 26..100
        for (int i=26; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalRemovalStartExact() {
        set.add(0, 100);
        
        set.remove(0, 25);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        
        
        assertEquals("min is 26", DoubleUtil.next(25), set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 26..100
        for (int i=26; i<=100; i++) {
            assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int firstIdx = set.getFirstIntervalIndex();
        int lastIdx = set.getLastIntervalIndex();
        assertTrue("set contains a single interval", firstIdx == lastIdx);
        assertEquals("set contains a single interval", -1, set.getNextIntervalIndex(firstIdx));
        assertEquals("set contains a single interval", -1, set.getPreviousIntervalIndex(lastIdx));
    }
    
    public void testIntervalRemovalDoubleAll() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(-10, 110);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 75, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        
        assertTrue("set is empty", set.size() == 0);
    }
    
    public void testIntervalRemovalDoubleAllExact() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(0, 100);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 75, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        
        assertTrue("set is empty", set.size() == 0);
    }
    
    public void testIntervalRemovalDoublePartialEnd() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(20, 50);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        
        // set is 0..19  75..100
        for (int i=0; i<=100; i++) {
            if (i < 20 || i > 74)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalDoublePartialStart() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(50, 80);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 75, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 80, listener.rmEnds.get(0), 0);
        
        // set is 0..25  81..100
        for (int i=0; i<=100; i++) {
            if (i < 26 || i > 80)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("81..100", DoubleUtil.next(80), set.getMin(index), 0);
                    assertEquals("81..100", 100, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalDoublePartialStartAndEnd() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(20, 80);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 75, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 80, listener.rmEnds.get(1), 0);
        
        
        // set is 0..19  81..100
        for (int i=0; i<=100; i++) {
            if (i < 20 || i > 80)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("81..100", DoubleUtil.next(80), set.getMin(index), 0);
                    assertEquals("81..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalDoubleNoEffect() {
        set.add(0, 25);
        set.add(75, 100);
        
        set.remove(26, 74);
        
        assertEquals("no interval removed", 0, listener.rmCallbacks.size());
        assertEquals("no interval removed", 0, listener.rmStarts.size());
        assertEquals("no interval removed", 0, listener.rmEnds.size());
        
        
        assertEquals("min is 0", 0, set.getMin(), 0);
        assertEquals("max is 100", 100, set.getMax(), 0);
        
        // set is 0..25  75..100
        for (int i=0; i<=100; i++) {
            if (i < 26 || i > 74)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("75..100", 75, set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTripleAll() {
        set.add(0, 25);
        set.add(33, 66);
        set.add(DoubleUtil.next(66), 100);
        
        set.remove(-10, 110);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 33, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        
        assertTrue("set is empty", set.size() == 0);
    }
    
    public void testIntervalRemovalTripleInner() {
        set.add(0, 25);
        set.add(33, 66);
        set.add(DoubleUtil.next(66), 100);
        
        set.remove(63, 68);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 63, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 68, listener.rmEnds.get(0), 0);
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
    }
    
    public void testIntervalRemovalTripleAllExact() {
        set.add(0, 25);
        set.add(33, 66);
        set.add(DoubleUtil.next(66), 100);
        
        set.remove(0, 100);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 33, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        
        assertTrue("set is empty", set.size() == 0);
    }
    
    public void testIntervalRemovalTriplePartialEndFirst() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(20, 49);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        
        // set is 0..19  50..75  80..100
        for (int i=0; i<=100; i++) {
            if ((i <= 19) || (i >= 50 && i <= 75) || (i >= 80))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..75", 50, set.getMin(index), 0);
                    assertEquals("50..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialEndSecond() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(70, 79);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 70, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        
        // set is 0..25  50..69  80..100
        for (int i=0; i<=100; i++) {
            if ((i <= 25) || (i >= 50 && i <= 69) || (i >= 80))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..69", 50, set.getMin(index), 0);
                    assertEquals("50..69", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialEndThird() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(95, 100);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 95, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(0), 0);
        
        // set is 0..25  50..75  80..94
        for (int i=0; i<=94; i++) {
            if ((i <= 25) || (i >= 50 && i <= 75) || (i >= 80))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getLastIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("80..94", 80, set.getMin(index), 0);
                    assertEquals("80..94", DoubleUtil.previous(95), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..75", 50, set.getMin(index), 0);
                    assertEquals("50..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
            }
            index = set.getPreviousIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialStartSecond() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(30, 55);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 50, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 55, listener.rmEnds.get(0), 0);
        
        // set is 0..25  56..75  80..100
        for (int i=0; i<=100; i++) {
            if ((i <= 25) || (i >= 56 && i <= 75) || (i >= 80))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("56..75", DoubleUtil.next(55), set.getMin(index), 0);
                    assertEquals("56..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialEndFirstStartSecond() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(20, 55);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 50, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 55, listener.rmEnds.get(1), 0);
        
        // set is 0..19  56..75  80..100
        for (int i=0; i<=100; i++) {
            if ((i <= 19) || (i >= 56 && i <= 75) || (i >= 80))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("56..75", DoubleUtil.next(55), set.getMin(index), 0);
                    assertEquals("56..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialEndSecondStartThird() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(70, 85);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 70, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 80, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 85, listener.rmEnds.get(1), 0);
        
        // set is 0..25  50..69  86..100
        for (int i=0; i<=100; i++) {
            if ((i <= 25) || (i >= 50 && i <= 69) || (i >= 86))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..69", 50, set.getMin(index), 0);
                    assertEquals("50..69", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("86..100", DoubleUtil.next(85), set.getMin(index), 0);
                    assertEquals("86..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialEndFirstStartThird() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(20, 85);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 50, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(1), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(2));
        assertEquals("interval removed", 80, listener.rmStarts.get(2), 0);
        assertEquals("interval removed", 85, listener.rmEnds.get(2), 0);
        
        // set is 0..19  86..100
        for (int i=0; i<=100; i++) {
            if (i <= 19 || i >= 86)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("86..100", DoubleUtil.next(85), set.getMin(index), 0);
                    assertEquals("86..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalTriplePartialSecond() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        
        set.remove(70, 75);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 70, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        
        // set is 0..25  50..69  80..100
        for (int i=0; i<=100; i++) {
            if (i <= 25 || (i>=50 && i<=69) || i >= 80)
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..69", 50, set.getMin(index), 0);
                    assertEquals("50..69", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadruplePartialEndFirstStartSecond() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(20, 55);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 20, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 25, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 50, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 55, listener.rmEnds.get(1), 0);
        
        // set is 0..19  56..75  80..100  110..130
        for (int i=0; i<=130; i++) {
            if ((i <= 19) || (i >= 56 && i <= 75) || (i >= 80 && i <= 100) || (i>=110))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..19", 0, set.getMin(index), 0);
                    assertEquals("0..19", DoubleUtil.previous(20), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("56..75", DoubleUtil.next(55), set.getMin(index), 0);
                    assertEquals("56..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..100", 80, set.getMin(index), 0);
                    assertEquals("80..100", 100, set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("110..130", 110, set.getMin(index), 0);
                    assertEquals("110..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadruplePartialEndSecondStartThird() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(70, 85);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 70, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 80, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 85, listener.rmEnds.get(1), 0);
        
        // set is 0..25  50..69  86..100  110..130
        for (int i=0; i<=130; i++) {
            if ((i <= 25) || (i >= 50 && i <= 69) || (i >= 86 && i <= 100) || (i>=110))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..69", 50, set.getMin(index), 0);
                    assertEquals("50..69", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("86..100", DoubleUtil.next(85), set.getMin(index), 0);
                    assertEquals("86..100", 100, set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("110..130", 110, set.getMin(index), 0);
                    assertEquals("110..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadruplePartialEndThirdStartFourth() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(95, 115);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 95, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 110, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 115, listener.rmEnds.get(1), 0);
        
        // set is 0..25  50..75  80..94  116..130
        for (int i=0; i<=130; i++) {
            if ((i <= 25) || (i >= 50 && i <= 75) || (i >= 80 && i <= 94) || (i>=116))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..75", 50, set.getMin(index), 0);
                    assertEquals("50..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..94", 80, set.getMin(index), 0);
                    assertEquals("80..94", DoubleUtil.previous(95), set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("116..130", DoubleUtil.next(115), set.getMin(index), 0);
                    assertEquals("116..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadruplePartialEndSecondAllThirdStartFourth() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(70, 115);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 70, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 80, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(2));
        assertEquals("interval removed", 110, listener.rmStarts.get(2), 0);
        assertEquals("interval removed", 115, listener.rmEnds.get(2), 0);
        
        // set is 0..25  50..69  116..130
        for (int i=0; i<=130; i++) {
            if ((i <= 25) || (i >= 50 && i <= 69) || (i>=116))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..69", 50, set.getMin(index), 0);
                    assertEquals("50..69", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("116..130", DoubleUtil.next(115), set.getMin(index), 0);
                    assertEquals("116..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadrupleAllSecondAllThirdStartFourth() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(49, 115);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 50, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 75, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 80, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 100, listener.rmEnds.get(1), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(2));
        assertEquals("interval removed", 110, listener.rmStarts.get(2), 0);
        assertEquals("interval removed", 115, listener.rmEnds.get(2), 0);
        
        // set is 0..25  116..130
        for (int i=0; i<=130; i++) {
            if ((i <= 25) || (i>=116))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("116..130", DoubleUtil.next(115), set.getMin(index), 0);
                    assertEquals("116..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testIntervalRemovalQuadrupleInnerThird() {
        set.add(0, 25);
        set.add(50, 75);
        set.add(80, 100);
        set.add(110, 130);
        
        set.remove(85, 95);
        
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 85, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 95, listener.rmEnds.get(0), 0);
        
        // set is 0..19  50..75  80..84  96..100  110..130
        for (int i=0; i<=130; i++) {
            if ((i <= 19) || (i >= 56 && i <= 75) || (i >= 80 && i <= 84) || (i >= 96 && i <= 100) || (i>=110))
                assertTrue("set contains "+i, set.contains(i));
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", 25, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..75", 50, set.getMin(index), 0);
                    assertEquals("50..75", 75, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("80..84", 80, set.getMin(index), 0);
                    assertEquals("80..84", DoubleUtil.previous(85), set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("96..100", DoubleUtil.next(95), set.getMin(index), 0);
                    assertEquals("96..100", 100, set.getMax(index), 0);
                    break;
                case 4:
                    assertEquals("110..130", 110, set.getMin(index), 0);
                    assertEquals("110..130", 130, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testMultipleIntervalAdditionAndRemoval() {
        set.add(0, 10);
        assertEquals("interval added", 7, listener.addCallbacks.get(0));
        assertEquals("interval added", 0, listener.addStarts.get(0), 0);
        assertEquals("interval added", 10, listener.addEnds.get(0), 0);
        
        set.add(20, 30);
        assertEquals("interval added", 7, listener.addCallbacks.get(1));
        assertEquals("interval added", 20, listener.addStarts.get(1), 0);
        assertEquals("interval added", 30, listener.addEnds.get(1), 0);
        
        set.add(40, 50);
        assertEquals("interval added", 7, listener.addCallbacks.get(2));
        assertEquals("interval added", 40, listener.addStarts.get(2), 0);
        assertEquals("interval added", 50, listener.addEnds.get(2), 0);
        
        set.add(60, 70);
        assertEquals("interval added", 7, listener.addCallbacks.get(3));
        assertEquals("interval added", 60, listener.addStarts.get(3), 0);
        assertEquals("interval added", 70, listener.addEnds.get(3), 0);
        
        set.add(65, 75);
        assertEquals("interval added", 7, listener.addCallbacks.get(4));
        assertEquals("interval added", 70, listener.addStarts.get(4), .01);
        assertEquals("interval added", 75, listener.addEnds.get(4), 0);
        
        set.add(90, 100);
        assertEquals("interval added", 7, listener.addCallbacks.get(5));
        assertEquals("interval added", 90, listener.addStarts.get(5), 0);
        assertEquals("interval added", 100, listener.addEnds.get(5), 0);
        
        set.add(70, 95);
        assertEquals("interval added", 7, listener.addCallbacks.get(6));
        assertEquals("interval added", 75, listener.addStarts.get(6), .01);
        assertEquals("interval added", 90, listener.addEnds.get(6), .01);
        
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        int index = set.getFirstIntervalIndex();
        int count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..10", 0, set.getMin(index), 0);
                    assertEquals("0..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("20..30", 20, set.getMin(index), 0);
                    assertEquals("20..30", 30, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("60..100", 60, set.getMin(index), 0);
                    assertEquals("60..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // remove first two intervals
        set.remove(0, 30);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(0));
        assertEquals("interval removed", 0, listener.rmStarts.get(0), 0);
        assertEquals("interval removed", 10, listener.rmEnds.get(0), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(1));
        assertEquals("interval removed", 20, listener.rmStarts.get(1), 0);
        assertEquals("interval removed", 30, listener.rmEnds.get(1), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("60..100", 60, set.getMin(index), 0);
                    assertEquals("60..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // remove middle of last interval
        set.remove(70, 90);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(2));
        assertEquals("interval removed", 70, listener.rmStarts.get(2), 0);
        assertEquals("interval removed", 90, listener.rmEnds.get(2), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("60..70", 60, set.getMin(index), 0);
                    assertEquals("60..70", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("90..100", DoubleUtil.next(90), set.getMin(index), 0);
                    assertEquals("90..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // add interval to "middle"
        set.add(75, 85);
        assertEquals("interval added", 7, listener.addCallbacks.get(7));
        assertEquals("interval added", 75, listener.addStarts.get(7), 0);
        assertEquals("interval added", 85, listener.addEnds.get(7), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("60..70", 60, set.getMin(index), 0);
                    assertEquals("60..70", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("75..85", 75, set.getMin(index), 0);
                    assertEquals("75..85", 85, set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("90..100", DoubleUtil.next(90), set.getMin(index), 0);
                    assertEquals("90..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // add new first interval
        set.add(0, 10);
        assertEquals("interval added", 7, listener.addCallbacks.get(8));
        assertEquals("interval added", 0, listener.addStarts.get(8), 0);
        assertEquals("interval added", 10, listener.addEnds.get(8), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..10", 0, set.getMin(index), 0);
                    assertEquals("0..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("60..70", 60, set.getMin(index), 0);
                    assertEquals("60..70", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("75..85", 75, set.getMin(index), 0);
                    assertEquals("75..85", 85, set.getMax(index), 0);
                    break;
                case 4:
                    assertEquals("90..100", DoubleUtil.next(90), set.getMin(index), 0);
                    assertEquals("90..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // add new end interval
        set.add(110, 120);
        assertEquals("interval added", 7, listener.addCallbacks.get(9));
        assertEquals("interval added", 110, listener.addStarts.get(9), 0);
        assertEquals("interval added", 120, listener.addEnds.get(9), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..10", 0, set.getMin(index), 0);
                    assertEquals("0..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("40..50", 40, set.getMin(index), 0);
                    assertEquals("40..50", 50, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("60..70", 60, set.getMin(index), 0);
                    assertEquals("60..70", DoubleUtil.previous(70), set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("75..85", 75, set.getMin(index), 0);
                    assertEquals("75..85", 85, set.getMax(index), 0);
                    break;
                case 4:
                    assertEquals("90..100", DoubleUtil.next(90), set.getMin(index), 0);
                    assertEquals("90..100", 100, set.getMax(index), 0);
                    break;
                case 5:
                    assertEquals("110..120", 110, set.getMin(index), 0);
                    assertEquals("110..120", 120, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // remove interval that overlaps several existing intervals
        set.remove(30, 80);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(3));
        assertEquals("interval removed", 40, listener.rmStarts.get(3), 0);
        assertEquals("interval removed", 50, listener.rmEnds.get(3), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(4));
        assertEquals("interval removed", 60, listener.rmStarts.get(4), 0);
        assertEquals("interval removed", 70, listener.rmEnds.get(4), .01);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(5));
        assertEquals("interval removed", 75, listener.rmStarts.get(5), 0);
        assertEquals("interval removed", 80, listener.rmEnds.get(5), 0);
        
        index = set.getFirstIntervalIndex();
        count = 0;
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..10", 0, set.getMin(index), 0);
                    assertEquals("0..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("80..85", DoubleUtil.next(80), set.getMin(index), 0);
                    assertEquals("80..85", 85, set.getMax(index), 0);
                    break;
                case 2:
                    assertEquals("90..100", DoubleUtil.next(90), set.getMin(index), 0);
                    assertEquals("90..100", 100, set.getMax(index), 0);
                    break;
                case 3:
                    assertEquals("110..120", 110, set.getMin(index), 0);
                    assertEquals("110..120", 120, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // add interval that overlaps serval existing intervals
        set.add(80, 115);
        assertEquals("interval added", 7, listener.addCallbacks.get(10));
        assertEquals("interval added", 80, listener.addStarts.get(10), 0);
        assertEquals("interval added", 80, listener.addEnds.get(10), .01);
        assertEquals("interval added", 7, listener.addCallbacks.get(11));
        assertEquals("interval added", 85, listener.addStarts.get(11), .01);
        assertEquals("interval added", 90, listener.addEnds.get(11), .01);
        assertEquals("interval added", 7, listener.addCallbacks.get(12));
        assertEquals("interval added", 100, listener.addStarts.get(12), .01);
        assertEquals("interval added", 110, listener.addEnds.get(12), .01);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..10", 0, set.getMin(index), 0);
                    assertEquals("0..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("80..120", 80, set.getMin(index), 0);
                    assertEquals("80..120", 120, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        // extend first interval
        set.add(-5, 5);
        assertEquals("interval added", 7, listener.addCallbacks.get(13));
        assertEquals("interval added", -5, listener.addStarts.get(13), 0);
        assertEquals("interval added", 0, listener.addEnds.get(13), .01);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("-5..10", -5, set.getMin(index), 0);
                    assertEquals("-5..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("80..120", 80, set.getMin(index), 0);
                    assertEquals("80..120", 120, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // extend last interval
        set.add(115, 125);
        assertEquals("interval added", 7, listener.addCallbacks.get(14));
        assertEquals("interval added", 120, listener.addStarts.get(14), .01);
        assertEquals("interval added", 125, listener.addEnds.get(14), 0);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("-5..10", -5, set.getMin(index), 0);
                    assertEquals("-5..10", 10, set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("80..125", 80, set.getMin(index), 0);
                    assertEquals("80..125", 125, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        assertEquals("size is infinite", Integer.MAX_VALUE, set.size(), 0);
        
        // remove everything
        set.remove(-1000, 1000);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(6));
        assertEquals("interval removed", -5, listener.rmStarts.get(6), 0);
        assertEquals("interval removed", 10, listener.rmEnds.get(6), 0);
        assertEquals("interval removed", 7, listener.rmCallbacks.get(7));
        assertEquals("interval removed", 80, listener.rmStarts.get(7), 0);
        assertEquals("interval removed", 125, listener.rmEnds.get(7), 0);
        
        assertEquals("size is 0", 0, set.size());
        assertTrue("empty", set.getFirstIntervalIndex() == set.getLastIntervalIndex());
        
        // add something back in
        set.add(0, 100);
        assertEquals("interval added", 7, listener.addCallbacks.get(15));
        assertEquals("interval added", 0, listener.addStarts.get(15), 0);
        assertEquals("interval added", 100, listener.addEnds.get(15), 0);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..100", 0, set.getMin(index), 0);
                    assertEquals("0..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // remove the middle
        set.remove(25, 75);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", DoubleUtil.previous(25), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("75..100", DoubleUtil.next(75), set.getMin(index), 0);
                    assertEquals("75..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
        
        // add something adjacent
        set.add(50, 75);
        
        while (index != -1) {
            switch (count) {
                case 0:
                    assertEquals("0..25", 0, set.getMin(index), 0);
                    assertEquals("0..25", DoubleUtil.previous(25), set.getMax(index), 0);
                    break;
                case 1:
                    assertEquals("50..100", 50, set.getMin(index), 0);
                    assertEquals("50..100", 100, set.getMax(index), 0);
                    break;
            }
            index = set.getNextIntervalIndex(index);
            count++;
        }
    }
    
    public void testRetainAllWithOverlap() {
        set.add(3, 10);
        
        DoubleIntervalSet retain = new DoubleIntervalSet();
        retain.add(5, 7);
        
        set.retainAll(retain);
        
        assertEquals("5..7", 5, set.getMin(), 0);
        assertEquals("5..7", 7, set.getMax(), 0);
    }
    
    public void testRetainAllWithoutOverlap() {
        set.add(3, 10);
        
        DoubleIntervalSet retain = new DoubleIntervalSet();
        retain.add(11, 15);
        
        set.retainAll(retain);
        
        assertEquals("the set is empty", 0, set.size());
    }
    
    public void testClone() {
        set.add(3, 10);
        
        DoubleIntervalSet clone = (DoubleIntervalSet) set.clone();
        clone.remove(3, 7);
        
        assertEquals("3..10", 3, set.getMin(), 0);
        assertEquals("3..10", 10, set.getMax(), 0);
    }
    
    public class TestListener implements DoubleIntervalSetListener {
        ArrayIntList rmCallbacks;
        ArrayDoubleList rmStarts;
        ArrayDoubleList rmEnds;
        ArrayIntList addCallbacks;
        ArrayDoubleList addStarts;
        ArrayDoubleList addEnds;
        
        public TestListener() {
            addCallbacks = new ArrayIntList();
            addStarts = new ArrayDoubleList();
            addEnds = new ArrayDoubleList();
            rmCallbacks = new ArrayIntList();
            rmStarts = new ArrayDoubleList();
            rmEnds = new ArrayDoubleList();
        }
        
        public void intervalAdded(int callback, double start, double end) {
            addCallbacks.add(callback);
            addStarts.add(start);
            addEnds.add(end);
        }
        
        public void intervalRemoved(int callback, double start, double end) {
            rmCallbacks.add(callback);
            rmStarts.add(start);
            rmEnds.add(end);
        }
    }
    
}
