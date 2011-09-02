
package jopt.csp.test.util;

import jopt.csp.spi.util.GenericIndex;
import junit.framework.TestCase;

/**
 * Unit tests for generic indexes
 */
public class GenericIndexTest extends TestCase {

    /**
     * Constructor for GenericIntTest.
     */
    public GenericIndexTest(String name) {
        super(name);
    }
    
    public void testNameMethods() {
        GenericIndex idxI = new GenericIndex("i", 3);
        GenericIndex idxJ = new GenericIndex("j",3);
        assertEquals(idxI.getName(),"i");
        assertEquals(idxI.toString(),"idx-i[-1]");
        idxI.setName("j");
        assertEquals(idxI.getName(),"j");
        assertEquals(idxI.toString(),"idx-j[-1]");
        assertEquals(idxI, idxJ);
        idxI.changeVal(1);
        assertEquals(idxI.currentVal(),1);
        assertEquals(idxI.toString(),"idx-j[1]");
        assertFalse(idxI.equals("23"));
        try {
            idxI.changeVal(234);
            fail();
        }
        catch (IllegalStateException ise){}
        
        
    }
}
