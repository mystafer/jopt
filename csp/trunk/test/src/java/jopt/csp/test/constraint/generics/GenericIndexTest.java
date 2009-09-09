package jopt.csp.test.constraint.generics;

import java.util.Arrays;

import jopt.csp.spi.util.GenericIndex;
import jopt.csp.spi.util.IndexIterator;
import junit.framework.TestCase;

public class GenericIndexTest extends TestCase {

	public void testIJsizeI3sizeJ1() {
	    GenericIndex iIdx = new GenericIndex("i", 3);
	    GenericIndex jIdx = new GenericIndex("j", 1);
	    IndexIterator iter = new IndexIterator(Arrays.asList(new GenericIndex[]{iIdx, jIdx}));
	    
	    assertTrue("has 1st combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertTrue("has 2nd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 1, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertTrue("has 3rd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 2, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertFalse("has 4th combination", iter.hasNext());
	}

	public void testIJsizeI2sizeJ2() {
	    GenericIndex iIdx = new GenericIndex("i", 2);
	    GenericIndex jIdx = new GenericIndex("j", 2);
	    IndexIterator iter = new IndexIterator(Arrays.asList(new GenericIndex[]{iIdx, jIdx}));
	    
	    assertTrue("has 1st combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertTrue("has 2nd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 1, jIdx.currentVal());
	    
	    assertTrue("has 3rd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 1, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertTrue("has 4th combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 1, iIdx.currentVal());
	    assertEquals("j", 1, jIdx.currentVal());
	    
	    assertFalse("has 5th combination", iter.hasNext());
	}

	public void testIJsizeI1sizeJ1() {
	    GenericIndex iIdx = new GenericIndex("i", 1);
	    GenericIndex jIdx = new GenericIndex("j", 1);
	    IndexIterator iter = new IndexIterator(Arrays.asList(new GenericIndex[]{iIdx, jIdx}));
	    
	    assertTrue("has 1st combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertFalse("has 2nd combination", iter.hasNext());
	}

	public void testIJsizeI1sizeJ3() {
	    GenericIndex iIdx = new GenericIndex("i", 1);
	    GenericIndex jIdx = new GenericIndex("j", 3);
	    IndexIterator iter = new IndexIterator(Arrays.asList(new GenericIndex[]{iIdx, jIdx}));
	    
	    assertTrue("has 1st combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 0, jIdx.currentVal());
	    
	    assertTrue("has 2nd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 1, jIdx.currentVal());
	    
	    assertTrue("has 3rd combination", iter.hasNext());
	    iter.next();
	    assertEquals("i", 0, iIdx.currentVal());
	    assertEquals("j", 2, jIdx.currentVal());
	    
	    assertFalse("has 4th combination", iter.hasNext());
	}
}
