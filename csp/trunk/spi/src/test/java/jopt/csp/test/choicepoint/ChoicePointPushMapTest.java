/*
 * Created on Apr 20, 2005
 */
package jopt.csp.test.choicepoint;

import jopt.csp.spi.arcalgorithm.domain.IntSparseDomain;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * @author Chris Johnson
 */
public class ChoicePointPushMapTest extends TestCase {

	public ChoicePointPushMapTest(String testName) {
        super(testName);
    }
	
	public void testPopPushState() {
		try {
			IntSparseDomain isd = new IntSparseDomain(1, 10);
			ChoicePointStack cps = new ChoicePointStack();
			isd.setChoicePointStack(cps);
			isd.removeRange(1, 4);
			cps.push();
			assertEquals("domain min", 5, isd.getMin());
			assertEquals("domain max", 10, isd.getMax());
			assertEquals("domain nums", 6, isd.getSize());
			
			isd.removeRange(5, 7);
			Object fiveThroughSeven = cps.popDelta();
			assertEquals("domain min", 5, isd.getMin());
			assertEquals("domain max", 10, isd.getMax());
			assertEquals("domain nums", 6, isd.getSize());
			
			cps.pushDelta(fiveThroughSeven);
			assertEquals("domain min", 8, isd.getMin());
			assertEquals("domain max", 10, isd.getMax());
			assertEquals("domain nums", 3, isd.getSize());
		}
		catch(PropagationFailureException propx) {
			fail("Unexpected error");
		}
	}

    public void test1() {
        try {
            IntSparseDomain isd = new IntSparseDomain(1, 10);
            ChoicePointStack cps = new ChoicePointStack();
            isd.setChoicePointStack(cps);
            
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
            
            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            cps.pop();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
        }
        catch(PropagationFailureException propx) {
            fail("Unexpected error");
        }
    }

    public void test2() {
        try {
            IntSparseDomain isd = new IntSparseDomain(1, 10);
            ChoicePointStack cps = new ChoicePointStack();
            isd.setChoicePointStack(cps);
            
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
            
            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            cps.pop();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());

            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            cps.push();
            isd.removeRange(5, 7);
            Object fiveThroughSeven = cps.popDelta();
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            cps.pushDelta(fiveThroughSeven);
            assertEquals("domain min", 8, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 3, isd.getSize());
        }
        catch(PropagationFailureException propx) {
            fail("Unexpected error");
        }
    }


    public void testReset() {
        try {
            IntSparseDomain isd = new IntSparseDomain(1, 10);
            ChoicePointStack cps = new ChoicePointStack();
            isd.setChoicePointStack(cps);
            
            // initial: [1..10]
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
            
            // remove [1..4] with result [5..10], stack size 1
            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            // return to original state [1..10], stack size 0
            cps.pop();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());

            // repeat [1..4] with result [5..10], stack size 1
            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            // remove [5..7] with result [8..10], stack size 2
            // then pop back to [5..10], stack size 1
            cps.push();
            isd.removeRange(5, 7);
            Object fiveThroughSeven = cps.popDelta();
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            // push the change that removed [5..7] to repeat it
            // to obtain result [8..10], stack size 2
            cps.pushDelta(fiveThroughSeven);
            assertEquals("domain min", 8, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 3, isd.getSize());
            
            // restart to return to original state [1..10], stack size 3
            cps.reset();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());

            // remove [1..4] with result [5..10], stack size 4
            cps.push();
            isd.removeRange(1, 4);
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            // remove [5..7] with result [8..10], stack size 5
            cps.pushDelta(fiveThroughSeven);
            assertEquals("domain min", 8, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 3, isd.getSize());
            
            // pop to return to [5..10], stack size 4
            cps.pop();
            assertEquals("domain min", 5, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 6, isd.getSize());
            
            // pop to return to restarted state [1..10], stack size 3
            cps.pop();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
            
            // should be no more changes to undo
            cps.pop();
            assertEquals("domain min", 1, isd.getMin());
            assertEquals("domain max", 10, isd.getMax());
            assertEquals("domain nums", 10, isd.getSize());
        }
        catch(PropagationFailureException propx) {
            fail("Unexpected error");
        }
    }
}
