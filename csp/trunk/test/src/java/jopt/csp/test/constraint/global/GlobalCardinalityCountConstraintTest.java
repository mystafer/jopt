package jopt.csp.test.constraint.global;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.global.GlobalCardinalityCountConstraint;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints
 * 
 * @author Chris Johnson
 */
public class GlobalCardinalityCountConstraintTest extends TestCase {

	CspSolver solver;
	CspVariableFactory varFactory;
	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable a4;
	IntVariable a5;
	IntVariable a6;
	IntVariable[] aArray;
	Number[] vals;
	IntVariable[] count;
	
	public void setUp () {
	    solver = CspSolver.createSolver();
	    solver.setAutoPropagate(false);
        a1 = new IntVariable("a1", 0, 5);
        a2 = new IntVariable("a2", 0, 5);
        a3 = new IntVariable("a3", 0, 5);
        a4 = new IntVariable("a4", 0, 5);
        a5 = new IntVariable("a5", 0, 5);
        a6 = new IntVariable("a6", 0, 5);
        vals = new Number[]{new Integer(0),new Integer(1),new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
        count = new IntVariable[]{	new IntVariable("0-count",0,1),new IntVariable("1-count",0,2),
        						new IntVariable("2-count",0,2),new IntVariable("3-count",0,1),
        						new IntVariable("4-count",0,2),new IntVariable("5-count",0,2)};
        aArray = new IntVariable[] {a1,a2,a3,a4,a5,a6};
	}
	
	public void tearDown() {
		solver = null;
		varFactory = null;
		a1 = null;
		a2 = null;
		a3 = null;
		a4 = null;
		a5 = null;
		a6 = null;
		aArray = null;
		vals = null;
		count = null;
	}
	
	public void testSetRemove() {
	    try {
	        CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
	        solver.addConstraint(constraint);
	        a1.setMax(1);
	        a2.setMax(1);
	        a3.setMax(1);
	        solver.propagate();
	        
	        //Should have removed the values 0 and 1 from subsequent variables
	        assertFalse(a4.isInDomain(0));
	        assertFalse(a4.isInDomain(1));
	        assertFalse(a5.isInDomain(0));
	        assertFalse(a5.isInDomain(1));
	        assertFalse(a6.isInDomain(0));
	        assertFalse(a6.isInDomain(1));
	        
	        a5.setValue(3);
	        solver.propagate();

	        //should now remove 3 as well
	        assertFalse(a4.isInDomain(3));
	        assertFalse(a6.isInDomain(3));
	        
	        a2.setValue(0);
	        solver.propagate();
	        
	        assertTrue(a1.isBound());
	        assertTrue(a2.isBound());
	        assertTrue(a3.isBound());
	        assertTrue(a5.isBound());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	
	public void testOverConstrainedUB() {
		IntVariable count0 = new IntVariable("0-count",0,1);
		IntVariable count1 = new IntVariable("1-count",0,2);
		IntVariable count2 = new IntVariable("2-count",0,2);
		IntVariable count3 = new IntVariable("3-count",0,1);
		IntVariable count4 = new IntVariable("4-count",0,2);
		IntVariable count5 = new IntVariable("5-count",0,2);
		try {
			count = new IntVariable[]{	count0,count1,count2,count3, count4, count5};
			CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
			solver.setAutoPropagate(true);
			solver.addConstraint(constraint);
			count1.setMax(1);
			count2.setMax(1);
			count4.setMax(1);
			count5.setMax(1);
			solver.propagate();
			
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		try {
			solver.setAutoPropagate(true);
			count1.setMax(0);
			fail();
		}
		catch(PropagationFailureException pfe) {
			
		}
		
	}
	public void testValidCountMinsAndMaxs() {
		IntVariable count0 = new IntVariable("0-count",2,6);
		IntVariable count1 = new IntVariable("1-count",0,2);
		IntVariable count2 = new IntVariable("2-count",2,5);
		IntVariable count3 = new IntVariable("3-count",0,3);
		IntVariable count4 = new IntVariable("4-count",0,2);
		IntVariable count5 = new IntVariable("5-count",0,3);
		
		try {
			count = new IntVariable[]{	count0,count1,count2,count3, count4, count5};
			CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
			solver.setAutoPropagate(true);
			solver.addConstraint(constraint);
		//Since there are "2 degrees of freedom" the max of any given count variable can only be 2 more than its min
			assertEquals(count0.getMin()+2, count0.getMax());
			assertEquals(count1.getMin()+2, count1.getMax());
			assertEquals(count2.getMin()+2, count2.getMax());
			assertEquals(count3.getMin()+2, count3.getMax());
			assertEquals(count4.getMin()+2, count4.getMax());
			assertEquals(count5.getMin()+2, count5.getMax());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		
	}
	
	
	public void testConstraintCountToBeEqual() {
		IntVariable count0 = new IntVariable("0-count",0,6);
		IntVariable count1 = new IntVariable("1-count",0,2);
		IntVariable count2 = new IntVariable("2-count",0,2);
		IntVariable count3 = new IntVariable("3-count",0,3);
		IntVariable count4 = new IntVariable("4-count",0,2);
		IntVariable count5 = new IntVariable("5-count",0,3);
		
		
		
		try {
			count = new IntVariable[]{	count0,count1,count2,count3, count4, count5};
			CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
			solver.setAutoPropagate(true);
			solver.addConstraint(constraint);
			for (int i=1; i<count.length; i++) {
				solver.addConstraint(count[i].eq(count[i-1]));
			}
			assertEquals(count1.getMin(), count0.getMin());
			assertEquals(count1.getMin(), count2.getMin());
			assertEquals(count1.getMin(), count3.getMin());
			assertEquals(count1.getMin(), count4.getMin());
			assertEquals(count1.getMin(), count5.getMin());
			assertEquals(count2.getMax(), count0.getMax());
			assertEquals(count2.getMax(), count1.getMax());
			assertEquals(count2.getMax(), count3.getMax());
			assertEquals(count2.getMax(), count4.getMax());
			assertEquals(count2.getMax(), count5.getMax());
			
			a1.setValue(3);
			solver.propagate();
			assertEquals(count1.getMin(), count0.getMin());
			assertEquals(count1.getMin(), count2.getMin());
			assertEquals(count1.getMin(), count3.getMin());
			assertEquals(count1.getMin(), count4.getMin());
			assertEquals(count1.getMin(), count5.getMin());
			assertEquals(count2.getMax(), count0.getMax());
			assertEquals(count2.getMax(), count1.getMax());
			assertEquals(count2.getMax(), count3.getMax());
			assertEquals(count2.getMax(), count4.getMax());
			assertEquals(count2.getMax(), count5.getMax());
			
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
		try {
			solver.setAutoPropagate(true);
			count1.setMax(0);
			fail();
		}
		catch(PropagationFailureException pfe) {
			
		}
		
	}
	
	public void testZeroUB(){
	    try {
	        count = new IntVariable[]{	new IntVariable("0-count",0,6),new IntVariable("1-count",0,0),
					new IntVariable("2-count",0,6),new IntVariable("3-count",0,0),
					new IntVariable("4-count",0,6),new IntVariable("5-count",0,6)};
	        CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
	        solver.addConstraint(constraint);
	        solver.propagate();
	        
	        assertFalse(a1.isInDomain(3));
	        assertFalse(a1.isInDomain(1));
	        assertFalse(a2.isInDomain(3));
	        assertFalse(a2.isInDomain(1));
	        assertFalse(a3.isInDomain(3));
	        assertFalse(a3.isInDomain(1));
	        assertFalse(a4.isInDomain(3));
	        assertFalse(a4.isInDomain(1));
	        assertFalse(a5.isInDomain(3));
	        assertFalse(a5.isInDomain(1));
	        assertFalse(a6.isInDomain(3));
	        assertFalse(a6.isInDomain(1));
	        
	        count[0].setDomainValue(new Integer(0));
	        solver.propagate();
	        assertFalse(a1.isInDomain(0));
	        assertFalse(a2.isInDomain(0));
	        assertFalse(a3.isInDomain(0));
	        assertFalse(a4.isInDomain(0));
	        assertFalse(a5.isInDomain(0));
	        assertFalse(a6.isInDomain(0));
	        
	    }
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testLB() {
	    try{
	        count = new IntVariable[]{	new IntVariable("0-count",0,1),new IntVariable("1-count",0,2),
										new IntVariable("2-count",0,2),new IntVariable("3-count",0,2)};
	        vals = new Number[]{new Integer(1), new Integer(2), new Integer(3), new Integer(4)};
	        CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
	        solver.setAutoPropagate(false);
	        solver.addConstraint(constraint);
	        a1.setRange(2,2);
	        a2.setRange(1,2);
	        a3.setRange(2,3);
	        a4.setRange(2,3);
	        a5.setRange(1,4);
	        a6.setRange(3,4);
	        solver.propagate();
	    }
	    catch(PropagationFailureException pfe) {
	        fail();
	    }
	}
	
	public void testBindingToMaxCount() {
		try {
			count = new IntVariable[]{	new IntVariable("0-count",-100,100),new IntVariable("1-count",-100,100),
					new IntVariable("2-count",-100,100),new IntVariable("3-count",-100,100),
					new IntVariable("4-count",-100,100),new IntVariable("5-count",-100,100)};
			
			CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
			solver.addConstraint(constraint);
			solver.propagate();
			//We will set the lower bound of the number of times 4 should show up to 5
			count[3].setMin(5);
			solver.propagate();
			//Nothing has been set yet
			assertEquals(a1.getSize(),6);
			assertEquals(a2.getSize(),6);
			assertEquals(a3.getSize(),6);
			assertEquals(a4.getSize(),6);
			assertEquals(a5.getSize(),6);
			assertEquals(a6.getSize(),6);
			
			a1.setMax(2);
			solver.propagate();
			
			//Setting the max of a1 to 2 means that all others should now be 4
			assertEquals(0,count[0].getMin());
			assertEquals(0,count[1].getMin());
			assertEquals(0,count[2].getMin());
			assertEquals(5,count[3].getMin());
			assertEquals(0,count[4].getMin());
			assertEquals(0,count[5].getMin());
			
			assertEquals(1,count[0].getMax());
			assertEquals(1,count[1].getMax());
			assertEquals(1,count[2].getMax());
			assertEquals(5,count[3].getMax());
			assertEquals(0,count[4].getMax());
			assertEquals(0,count[5].getMax());
		}
		
		catch (PropagationFailureException pfe) {
			fail(pfe.toString());
		}
	}
	
	
	public void testCount() {
		try {
			count = new IntVariable[]{	new IntVariable("0-count",-100,100),new IntVariable("1-count",-100,100),
										new IntVariable("2-count",-100,100),new IntVariable("3-count",-100,100),
										new IntVariable("2-count",-100,100),new IntVariable("3-count",-100,100)};
			
			CspConstraint constraint = new GlobalCardinalityCountConstraint(aArray,vals,count);
			solver.addConstraint(constraint);
			solver.propagate();
//			Since counts really pose no real restrictions on the variable, this should do no removing yet, but rather simply restrict count to be appropriate values
			//namely 0-6
			assertEquals(0,count[0].getMin());
			assertEquals(0,count[1].getMin());
			assertEquals(0,count[2].getMin());
			assertEquals(0,count[3].getMin());
			assertEquals(0,count[4].getMin());
			assertEquals(0,count[5].getMin());
			
			assertEquals(6,count[0].getMax());
			assertEquals(6,count[1].getMax());
			assertEquals(6,count[2].getMax());
			assertEquals(6,count[3].getMax());
			assertEquals(6,count[4].getMax());
			assertEquals(6,count[5].getMax());
			
			//Now set the max on a few variables and make sure the counts adjust correctly.
			a1.setMax(1);
			a2.setMax(1);
			solver.propagate();
			//There are no variables that have to occur, but now the maximum time that the values 2-5 can occur have been decremented
			assertEquals(0,count[0].getMin());
			assertEquals(0,count[1].getMin());
			assertEquals(0,count[2].getMin());
			assertEquals(0,count[3].getMin());
			assertEquals(0,count[4].getMin());
			assertEquals(0,count[5].getMin());
			
			assertEquals(6,count[0].getMax());
			assertEquals(6,count[1].getMax());
			assertEquals(4,count[2].getMax());
			assertEquals(4,count[3].getMax());
			assertEquals(4,count[4].getMax());
			assertEquals(4,count[5].getMax());
			
			a1.setValue(1);
			solver.propagate();
			//1 has been set, and can still be present in all
			assertEquals(0,count[0].getMin());
			assertEquals(1,count[1].getMin());
			assertEquals(0,count[2].getMin());
			assertEquals(0,count[3].getMin());
			assertEquals(0,count[4].getMin());
			assertEquals(0,count[5].getMin());
			
			assertEquals(5,count[0].getMax());
			assertEquals(6,count[1].getMax());
			assertEquals(4,count[2].getMax());
			assertEquals(4,count[3].getMax());
			assertEquals(4,count[4].getMax());
			assertEquals(4,count[5].getMax());
			
			a2.setValue(0);
			a3.setValue(0);
			a4.setValue(0);
			a5.setValue(0);
			a6.setValue(0);
			solver.propagate();
			
			assertEquals(5,count[0].getMin());
			assertEquals(1,count[1].getMin());
			assertEquals(0,count[2].getMin());
			assertEquals(0,count[3].getMin());
			assertEquals(0,count[4].getMin());
			assertEquals(0,count[5].getMin());
			
			assertEquals(5,count[0].getMax());
			assertEquals(1,count[1].getMax());
			assertEquals(0,count[2].getMax());
			assertEquals(0,count[3].getMax());
			assertEquals(0,count[4].getMax());
			assertEquals(0,count[5].getMax());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}


}
