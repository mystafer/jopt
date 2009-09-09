/*
 * Created on May 13, 2005
 */

package jopt.csp.test.constraint.generics.constantGen;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SummationConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.GenericIntConstant;
import jopt.csp.spi.arcalgorithm.variable.GenericIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.arcalgorithm.variable.VarMath;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.util.GenericIndex;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.CspGenericIndexRestriction;
import jopt.csp.variable.CspGenericIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SumConstraint violation with generics
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class ConstantGenericSummationConstraintTest extends TestCase {
	
	GenericIntConstant yiconst;
	GenericIntConstant miconst;
	GenericIntConstant niconst;
	GenericIntConstant yjconst;
	GenericIntConstant yijconst;
	IntVariable x1;
	IntVariable x2;
	IntVariable x3;
	IntVariable y1;
	IntVariable y2;
	IntVariable y3;
	IntVariable z11;
	IntVariable z12;
	IntVariable z13;
	IntVariable z21;
	IntVariable z22;
	IntVariable z23;
	IntVariable z31;
	IntVariable z32;
	IntVariable z33;
	GenericIntExpr xiexpr;
	GenericIntExpr ziexpr;
	GenericIntExpr zijexpr;
	ConstraintStore store;
	CspVariableFactory varFactory;
	GenericIndex idxI;
	GenericIndex idxJ;
	GenericIndex idxK;
	IntVariable y;
	IntVariable z;
	
	public void setUp () {
		store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
		store.setAutoPropagate(false);
		varFactory = store.getConstraintAlg().getVarFactory();
		idxI = new GenericIndex("i", 3);
		idxJ = new GenericIndex("j", 3);
		idxK = new GenericIndex("k", 3);
		x1 = new IntVariable("x1", 0, 300);
		x2 = new IntVariable("x2", 0, 300);
		x3 = new IntVariable("x3", 0, 300);
		
		y1 = new IntVariable("y1", 0, 100);
		y2 = new IntVariable("y2", 0, 100);
		y3 = new IntVariable("y3", 0, 100);        
		z11 = new IntVariable("z11", 0, 100);
		z12 = new IntVariable("z12", 0, 100);
		z13 = new IntVariable("z13", 0, 100);
		z31 = new IntVariable("z31", 0, 100);
		z32 = new IntVariable("z32", 0, 100);
		z33 = new IntVariable("z33", 0, 100);
		z21 = new IntVariable("z21", 0, 100);
		z22 = new IntVariable("z22", 0, 100);
		z23 = new IntVariable("z23", 0, 100);
		yiconst = new GenericIntConstant("yiconst", new GenericIndex[]{idxI}, new int[]{120,160,235});
		miconst = new GenericIntConstant("miconst", new GenericIndex[]{idxI}, new int[]{1,1,1});
		niconst = new GenericIntConstant("niconst", new GenericIndex[]{idxI}, new int[]{2,2,2});
		yjconst = new GenericIntConstant("yjconst", new GenericIndex[]{idxJ}, new int[]{36,19,23});
		yijconst = new GenericIntConstant("yijconst", new GenericIndex[]{idxI,idxJ}, new int[]{10,15,20,30,40,50,75,85,95});
		xiexpr = (GenericIntExpr)varFactory.genericInt("xi", idxI, new CspIntVariable[]{x1, x2, x3});
		zijexpr = (GenericIntExpr)varFactory.genericInt("zij", new GenericIndex[]{idxI,idxJ}, new CspIntVariable[]{z11, z12, z13, z21, z22, z23, z31, z32, z33});
	}
	
	public void tearDown() {
		yiconst = null;
		miconst = null;
		niconst = null;
		yjconst = null;
		yijconst = null;
		x1 = null;
		x2 = null;
		x3 = null;
		y1 = null;
		y2 = null;
		y3 = null;
		z11 = null;
		z12 = null;
		z13 = null;
		z21 = null;
		z22 = null;
		z23 = null;
		z31 = null;
		z32 = null;
		z33 = null;
		xiexpr = null;
		ziexpr = null;
		zijexpr = null;
		store = null;
		varFactory = null;
		idxI = null;
		idxJ = null;
		idxK = null;
		y = null;
		z = null;
	}
	
	public void testSummationTestFalse() {
		IdxRestriction sourceRestriction = new IdxRestriction(idxI);
		SummationConstraint constraint = null;
		constraint = new SummationConstraint(zijexpr, new GenericIndex[] {idxJ},
				sourceRestriction, xiexpr, ThreeVarConstraint.EQ) ;
		
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			z11.setValue(15);
			z12.setValue(20);
			z13.setValue(25);
			z21.setValue(40);
			z22.setValue(60);
			z23.setValue(80);
			z31.setValue(55);
			z32.setValue(65);
			z33.setValue(75);
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			
			x3.setDomainMin(new Integer(195));
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			x3.setDomainMin(new Integer(196));
			assertTrue("constraint is false ", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}
    
	public void testSummationTestTrue() {
		IdxRestriction sourceRestriction = new IdxRestriction(idxI);
		SummationConstraint constraint = null;
		constraint = new SummationConstraint(zijexpr, new GenericIndex[] {idxJ},
				sourceRestriction, xiexpr, ThreeVarConstraint.EQ) ;
		
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			z11.setValue(15);
			z12.setValue(20);
			z13.setValue(25);
			z21.setValue(40);
			z22.setValue(60);
			z23.setValue(80);
			z31.setValue(55);
			z32.setValue(65);
			z33.setValue(75);
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			
			x2.setValue(180);
			x3.setValue(195);
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			x1.setValue(60);
			assertFalse("constraint is still not false ", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
    
	public void testSummationGenericConstantFalse() {
		IdxRestriction sourceRestriction = new IdxRestriction(idxI);
		SummationConstraint constraint = null;
		constraint = new SummationConstraint(zijexpr, new GenericIndex[] {idxJ},
				sourceRestriction, yiconst, ThreeVarConstraint.EQ) ;
		
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			z11.setDomainMin(new Integer(15));
			z12.setDomainMin(new Integer(20));
			z13.setDomainMin(new Integer(25));
			z21.setDomainMin(new Integer(20));
			z22.setDomainMin(new Integer(40));
			z23.setDomainMin(new Integer(60));
			z31.setDomainMin(new Integer(30));
			z32.setDomainMin(new Integer(50));
			z33.setDomainMin(new Integer(70));
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z12.setDomainMin(new Integer(80));
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z12.setDomainMin(new Integer(81));
			assertTrue("constraint is now false ", constraint.isFalse());
			assertFalse("constraint is still not true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}	
    
	public void testSummationGenericConstantTrue() {
		IdxRestriction sourceRestriction = new IdxRestriction(idxI);
		SummationConstraint constraint = null;
		constraint = new SummationConstraint(zijexpr, new GenericIndex[] {idxJ},
				sourceRestriction, yiconst, ThreeVarConstraint.EQ) ;
		
		assertFalse("constraint is not false yet", constraint.isFalse());
		assertFalse("constraint is not true yet", constraint.isTrue());
		try {
			z11.setValue(40);
			z12.setValue(35);
			z13.setValue(45);
			z21.setValue(20);
			z22.setValue(90);
			z31.setValue(85);
			
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertFalse("constraint is not true yet", constraint.isTrue());
			z23.setValue(50);
			z32.setValue(90);
			z33.setValue(60);
			
			assertFalse("constraint is not false yet", constraint.isFalse());
			assertTrue("constraint is now true", constraint.isTrue());
		}
		catch(PropagationFailureException pfe) {
			fail();
		}
	}		
	
	public void testGenericAddSummation() {
		CspGenericIntExpr sum = xiexpr.add(miconst).add(niconst);
		CspGenericIntExpr sumProd = sum.multiply(zijexpr);
		VarMath varMath = (VarMath)store.getConstraintAlg().getVarFactory().getMath();
		CspGenericIntExpr summation = (GenericIntExpr)varMath.summation(sumProd,new CspGenericIndex[]{idxJ},null);
		try {
			store.addConstraint(summation.leq(3));
			z11.setValue(0);
			z12.setValue(0);
			z13.setValue(1);
			store.propagate();
			assertTrue("x1 should be bound to zero", x1.isBound());
			assertEquals("x1 should be 0", 0, x1.getMax());
		}
		catch(Exception e) {
            e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}
	
	private class IdxRestriction implements CspGenericIndexRestriction {
		private GenericIndex idx;
		
		public IdxRestriction(GenericIndex idx) {
			this.idx = idx;
		}
		
		public boolean currentIndicesValid() {
			return idx.currentVal() < 3;
		}
	}	
}
