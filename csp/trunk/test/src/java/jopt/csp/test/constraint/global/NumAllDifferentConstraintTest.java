package jopt.csp.test.constraint.global;

import jopt.csp.CspSolver;
import jopt.csp.spi.arcalgorithm.constraint.num.global.NumAllDiffConstraint;
import jopt.csp.spi.arcalgorithm.domain.IntSparseDomain;
import jopt.csp.spi.arcalgorithm.graph.arc.global.GenericNumAllDiffArc;
import jopt.csp.spi.arcalgorithm.graph.node.IntNode;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.util.IntSparseSet;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test NumRelationConstraints
 * 
 * @author Chris Johnson
 */
public class NumAllDifferentConstraintTest extends TestCase {

	CspSolver solver;
	CspVariableFactory varFactory;
	IntVariable a1;
	IntVariable a2;
	IntVariable a3;
	IntVariable a4;
	IntVariable a5;
	IntVariable a6;
	IntVariable[] aArray;
	
	public void setUp () {
	    solver = CspSolver.createSolver();
	    solver.setAutoPropagate(false);
        a1 = new IntVariable("a1", 0, 5);
        a2 = new IntVariable("a2", 0, 5);
        a3 = new IntVariable("a3", 0, 5);
        a4 = new IntVariable("a4", 0, 5);
        a5 = new IntVariable("a5", 0, 5);
        a6 = new IntVariable("a6", 0, 5);
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
	}
	
	public void testSetRemove() {
	    try {
	        CspConstraint constraint = new NumAllDiffConstraint(aArray);
	        solver.addConstraint(constraint);
	        a1.setValue(2);
	        solver.propagate();
	        assertFalse(a2.isInDomain(2));
	        assertFalse(a3.isInDomain(2));
	        assertFalse(a4.isInDomain(2));
	        assertFalse(a5.isInDomain(2));
	        assertFalse(a6.isInDomain(2));
	        
	        
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testToBreak() {
	    IntSparseSet set1 = new IntSparseSet();
	    set1.add(3);
	    set1.add(7);
	    IntNode n1 = new IntNode("n1",new IntSparseDomain(set1));
	    IntSparseSet set2 = new IntSparseSet();
	    set2.add(4);
	    set2.add(8);
	    IntNode n2 = new IntNode("n2",new IntSparseDomain(set2));
	    IntSparseSet set3 = new IntSparseSet();
	    set3.add(3);
	    set3.add(7);
	    IntNode n3 = new IntNode("n3",new IntSparseDomain(set3));
	    IntSparseSet set4 = new IntSparseSet();
	    set4.add(3,5);
	    IntNode n4 = new IntNode("n4",new IntSparseDomain(set4));
	    IntSparseSet set5 = new IntSparseSet();
	    set5.add(2,8);
	    IntNode n5 = new IntNode("n5",new IntSparseDomain(set5));
	    IntSparseSet set6 = new IntSparseSet();
	    set6.add(7,8);
	    IntNode n6 = new IntNode("n6",new IntSparseDomain(set6));
	    
	    
	    IntNode[] nodes = new IntNode[]{n1,n2,n3,n4,n5,n6};
	    
	    GenericNumAllDiffArc arc = new GenericNumAllDiffArc(nodes);
	    try {
	        arc.propagateNumNodeDomain();
	        assertTrue(n5.getSize()<=3);
	    }
	    catch(PropagationFailureException pfe) {
	        fail(pfe.getLocalizedMessage());
	    }
	    
	    
	    
	}
	
	public void testBoundAll() {
	    try {
	        solver.setAutoPropagate(true);
	        CspConstraint constraint = new NumAllDiffConstraint(aArray);
	        solver.addConstraint(constraint);
//	        a1.setValue(2);
//	        a2.setValue(0);
//	        a3.setValue(5);
//	        a4.setValue(3);
//	        a5.setValue(1);
//	        solver.propagate();
//	        assertTrue(a2.isBound());
//	        assertTrue(a3.isBound());
//	        assertTrue(a4.isBound());
//	        assertTrue(a5.isBound());
//	        assertTrue(a6.isBound());
//	        assertEquals(a6.getMax(),4);
//	        assertEquals(a6.getMin(),4);
	        
	        
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}

	public void testLogic() {
	    try {
	        CspConstraint constraint = new NumAllDiffConstraint(aArray);
	        solver.addConstraint(constraint);
	        a1.setMax(2);
	        a2.setMax(1);
	        a3.setMax(1);
	        assertEquals(2,a1.getMax());
	        assertEquals(1,a2.getMax());
	        assertEquals(1,a3.getMax());
	        
	        solver.propagate();
	        //a1 should be bound to 2 since between a2 and a3, they will both need 0 and 1
//	        TODO:  Should this amount of propagation occur?
	        assertFalse(a1.isInDomain(0));
	        assertFalse(a1.isInDomain(1));
	        
	        
	        
	        
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
	
	public void testOverBoundingProp() {
	    try{
	        CspConstraint constraint = new NumAllDiffConstraint(aArray);
	        solver.addConstraint(constraint);
	        solver.setAutoPropagate(false);
	        a1.setValue(1);
	        a2.setValue(1);
	        assertFalse(solver.propagate());
	        
		}
		catch(PropagationFailureException pfe) {
		}
	}
	
	public void testOverBoundingConstraintViolation() {
	    try{
	        CspConstraint constraint = new NumAllDiffConstraint(aArray);
	        solver.addConstraint(constraint);
	        solver.setAutoPropagate(false);
	        a1.setValue(1);
	        a2.setValue(2);
	        assertFalse(constraint.isTrue());
	        assertFalse(constraint.isFalse());
	        
	        //At this point we will be able to determine that the constraint is in fact false
	        a3.setValue(1);
	        assertFalse(constraint.isTrue());
	        assertTrue(constraint.isFalse());
	        
	        
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}


}
