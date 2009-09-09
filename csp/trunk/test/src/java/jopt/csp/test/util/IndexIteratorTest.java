
package jopt.csp.test.util;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import junit.framework.TestCase;

/**
 * Unit tests for generic indexes
 */
public class IndexIteratorTest extends TestCase {

    /**
     * Constructor for GenericIntTest.
     */
    public IndexIteratorTest(String name) {
        super(name);
    }
    
    public void testSingleIndexIterator() {
        GenericIndex idxI = new GenericIndex("i", 3);
        
        ArrayList indexList = new ArrayList();
        indexList.add(idxI);
        
        IndexIterator idxItr = new IndexIterator(indexList);
        
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        idxItr.next();
        assertEquals("i = 2", 2, idxI.currentVal());
        try {
            idxItr.next();
            //this should cause an exception
            fail();
        }
        catch (NoSuchElementException nse) {}
    }
    
    public void testDoubleIndexIterator() {
        GenericIndex idxI = new GenericIndex("i", 3);
        GenericIndex idxJ = new GenericIndex("j", 2);
        
        ArrayList indexList = new ArrayList();
        indexList.add(idxI);
        indexList.add(idxJ);
        
        IndexIterator idxItr = new IndexIterator(indexList);
        
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 2", 2, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 2", 2, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        try {
            idxItr.next();
            //this should cause an exception
            fail();
        }
        catch (NoSuchElementException nse) {}
    }
    
    public void testDoubleIndexIteratorSizeOne() {
        GenericIndex idxI = new GenericIndex("i", 1);
        GenericIndex idxJ = new GenericIndex("j", 3);
        
        ArrayList indexList = new ArrayList();
        indexList.add(idxI);
        indexList.add(idxJ);
        
        IndexIterator idxItr = new IndexIterator(indexList);
        
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 2", 2, idxJ.currentVal());
        try {
            idxItr.next();
            //this should cause an exception
            fail();
        }
        catch (NoSuchElementException nse) {}
    }
    
    public void testTripleIndexIterator() {
        GenericIndex idxI = new GenericIndex("i", 2);
        GenericIndex idxJ = new GenericIndex("j", 2);
        GenericIndex idxK = new GenericIndex("k", 2);
        
        ArrayList indexList = new ArrayList();
        indexList.add(idxI);
        indexList.add(idxJ);
        indexList.add(idxK);
        
        IndexIterator idxItr = new IndexIterator(indexList);
        
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 1", 1, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 1", 1, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 1", 1, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 1", 1, idxK.currentVal());
        try {
            idxItr.next();
            //this should cause an exception
            fail();
        }
        catch (NoSuchElementException nse) {}
    }

    public void testTripleIndexIteratorSizeOne() {
        GenericIndex idxI = new GenericIndex("i", 3);
        GenericIndex idxJ = new GenericIndex("j", 2);
        GenericIndex idxK = new GenericIndex("k", 1);
        
        ArrayList indexList = new ArrayList();
        indexList.add(idxI);
        indexList.add(idxJ);
        indexList.add(idxK);
        
        IndexIterator idxItr = new IndexIterator(indexList);
        
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 0", 0, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 1", 1, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 2", 2, idxI.currentVal());
        assertEquals("j = 0", 0, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        idxItr.next();
        assertEquals("i = 2", 2, idxI.currentVal());
        assertEquals("j = 1", 1, idxJ.currentVal());
        assertEquals("k = 0", 0, idxK.currentVal());
        try {
            idxItr.next();
            //this should cause an exception
            fail();
        }
        catch (NoSuchElementException nse) {}
    }
}
