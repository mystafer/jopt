package jopt.csp.test.util;

import jopt.csp.util.DoubleUtil;
import junit.framework.TestCase;

/**
 * Test DoubleUtil
 */
public class DoubleUtilTest extends TestCase {
	
	public void testIsEqual() {
	    assertFalse(DoubleUtil.isEqual(2,3,.01));
        assertTrue(DoubleUtil.isEqual(.01,.02,.01001));
        assertFalse(DoubleUtil.isEqual(.01,.02,.009999));
        assertTrue(DoubleUtil.isEqual(-.0001,.0001,.000201));
        assertFalse(DoubleUtil.isEqual(-.0001,.0001,.0001999));
        assertTrue(DoubleUtil.isEqual(-.01,-.02,.01001));
        assertFalse(DoubleUtil.isEqual(-.01,-.02,.009999));
        assertTrue(DoubleUtil.isEqual(1000000,1000001,.00000102));
        assertFalse(DoubleUtil.isEqual(1000000,1000001,.00000099));
        assertFalse(DoubleUtil.isEqual(-1000000,1000001,.00000102));
        assertFalse(DoubleUtil.isEqual(-1000000,1000001,.00000099));
        assertTrue(DoubleUtil.isEqual(-1000000,-1000001,.00000102));
        assertFalse(DoubleUtil.isEqual(-1000000,-1000001,.00000099));
    }

}
