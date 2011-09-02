package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanImpliesTwoVarConstraint;
import jopt.csp.spi.arcalgorithm.graph.GraphConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests the mapping of constraints to boolean expressions
 * 
 * @author Chris Johnson
 */
public class ConstraintToBooleanTest extends TestCase {
    private IntVariable x;
	private IntVariable y;
	private BooleanVariable aVar;
	private BooleanVariable bVar;
	private BooleanVariable bv;
	private ConstraintStore store;
	private GraphConstraint pconstraint;
	private BooleanConstraint constraint;
    
    public ConstraintToBooleanTest(java.lang.String testName) {
        super(testName);
    }
    
    public void setUp() {
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
    	store.setAutoPropagate(false);
        x = new IntVariable("x", 0, 100);
        y = new IntVariable("y", 0, 100);
        aVar = new BooleanVariable("aVar");
        bVar = new BooleanVariable("bVar");
        ((VariableChangeSource) x).addVariableChangeListener(store);
    	((VariableChangeSource) y).addVariableChangeListener(store);
    	bv = new BooleanVariable("bv");
    	pconstraint = (GraphConstraint) x.neq(y);
    }
    
    public void tearDown() {
        x=null;
    	y=null;
    	aVar=null;
    	bVar=null;
    	bv=null;
    	store=null;
    	pconstraint=null;
    	constraint=null;
    }

    public void testConstraintToExprTrue() {
        try {
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the variable domains
            x.setDomainRange(new Integer(1), new Integer(6));
            y.setDomainRange(new Integer(5), new Integer(10));
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the domains again
            x.setDomainRange(new Integer(2), new Integer(4));
            y.setDomainRange(new Integer(6), new Integer(8));
            
            //Propagate
            store.getConstraintAlg().propagate();

            //The pconstraint should now be determined as should the boolean variable and constraint
            assertTrue("pconstraint is true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            assertTrue("bv isTrue should be true", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            assertTrue("constraint is true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstraintToExprFalse() {
        try {
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the variable domains
            x.setDomainRange(new Integer(2), new Integer(7));
            y.setDomainRange(new Integer(3), new Integer(8));
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the domains completely
            x.setDomainValue(new Integer(5));
            y.setDomainValue(new Integer(5));
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should now be determined as should the boolean variable and constraint
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertTrue("pconstraint is false", pconstraint.isFalse());
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertTrue("bv isFalse should be true", bv.isFalse());
            assertTrue("constraint is true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstraintToExprPost() {
        try {
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the variable domains
            x.setDomainRange(new Integer(5), new Integer(6));
            y.setDomainValue(new Integer(5));
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Determine the boolean variable
            bv.setTrue();
            
            //Propagate to post the associated constraint
            store.getConstraintAlg().propagate();
            
            //the constraint is now determined
            assertTrue("pconstraint is true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            assertTrue("bv isTrue should be true", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            assertTrue("constraint is true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Verify that the pconstraint was posted and propagated
            assertTrue("x is bound", x.isBound());
            assertTrue("y is bound", y.isBound());
            assertEquals("x is 6", 6, x.getMin());
            assertEquals("y is 5", 5, y.getMin());
            assertEquals("x is 6", 6, x.getMax());
            assertEquals("y is 5", 5, y.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testNumConstraintToExprPostOpposite() {
        try {
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound a variable domain
            x.setDomainValue(new Integer(5));
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Determine the boolean variable
            bv.setFalse();
            
            //Propagate to post the opposite of the associated constraint
            store.getConstraintAlg().propagate();
            
            //the constraint is now determined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertTrue("pconstraint is false", pconstraint.isFalse());
            assertTrue("constraint is true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Verify that the pconstraint was posted and propagated
            assertTrue("x is bound", x.isBound());
            assertTrue("y is bound", y.isBound());
            assertEquals("x is 5", 5, x.getMin());
            assertEquals("y is 5", 5, y.getMin());
            assertEquals("x is 5", 5, x.getMax());
            assertEquals("y is 5", 5, y.getMax());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testBoolConstraintToExprPostOpposite() {
        try {
            pconstraint = new BooleanImpliesTwoVarConstraint(aVar, bVar);
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound a variable domain
            bVar.setFalse();
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Determine the boolean variable
            bv.setFalse();
            
            //Propagate to post the opposite of the associated constraint
            store.getConstraintAlg().propagate();
            
            //the constraint is now determined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertTrue("pconstraint is false", pconstraint.isFalse());
            assertTrue("constraint is true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Verify that the opposite of the pconstraint was posted and propagated
            assertTrue("aVar isTrue should be true", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstraintToExprPostAndFail() {
        try {
            constraint = new BooleanEqTwoVarConstraint(bv, pconstraint);
            store.addConstraint(constraint);
            
            //The pconstraint should be undetermined
            assertFalse("pconstraint is not true", pconstraint.isTrue());
            assertFalse("pconstraint is not false", pconstraint.isFalse());
            //The boolean variable involved should also be undetermined
            assertFalse("bv isTrue should be false", bv.isTrue());
            assertFalse("bv isFalse should be false", bv.isFalse());
            //The wrapping constraint should be underterminded as well
            assertFalse("constraint is not true", constraint.isTrue());
            assertFalse("constraint is not false", constraint.isFalse());
            
            //Bound the variable domains
            x.setDomainValue(new Integer(5));
            y.setDomainValue(new Integer(5));
            
            //Determine the boolean variable
            bv.setTrue();
            
            //The constraint should be false
            assertFalse("constraint is not true", constraint.isTrue());
            assertTrue("constraint is false", constraint.isFalse());
            
            //Propagate to post the associated constraint
            store.getConstraintAlg().propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
            
        }
    }
}
