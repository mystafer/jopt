package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanXorConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests various combinations of conditions involving BooleanXorConstraints
 * 
 * @author Chris Johnson
 * @author Jim Boerkoel
 */
public class XorConstraintTest extends TestCase {
    private BooleanVariable xVar;
	private BooleanVariable yVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private CspConstraint constraint;
	
    public XorConstraintTest(java.lang.String testName) {
        super(testName);
    }

    public void setUp() {
    	store= new ConstraintStore(SolverImpl.createDefaultAlgorithm());
    	store.setAutoPropagate(false);
    	xVar = new BooleanVariable("x");
    	yVar = new BooleanVariable("y");
    	zVar = new BooleanVariable("z");
    	((VariableChangeSource) xVar).addVariableChangeListener(store);
    	((VariableChangeSource) yVar).addVariableChangeListener(store);
    	((VariableChangeSource) zVar).addVariableChangeListener(store);    
    }
    
    public void tearDown() {
    	xVar = null;
    	yVar = null;
    	zVar = null;
    	store = null;
    	constraint = null;
    }
    
    public void testSettingXandYTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.xor(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Now we determine one of the variables
            xVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined as well
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Determine another variable
            yVar.setTrue();
            
            //The z should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is determined
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
            
            //Z should now be false
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    public void testSettingXFalseAndYTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.xor(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Now we determine one of the variables
            xVar.setFalse();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined as well
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Determine another variable
            yVar.setTrue();
            
            //The constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //Now the constraint is determined
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
            
            //The z should now be propagated to be true
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }

    public void testSettingXandZTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.xor(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Now we determine one of the variables
            xVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined as well
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Determine another variable
            zVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is determined
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
            
            //y should now be false
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertTrue("yVar isFalse should be true", yVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    public void testConstXTrueCTrueXor() {
    	try {
    		constraint = new BooleanXorConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
    		
            //Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            xVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, Z will be false and the constraint will be satisfied
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be false", zVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}
    }
    
    public void testConstXFalseCTrueXor() {
        try {
    		constraint = new BooleanXorConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
    		
            //Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            xVar.setFalse();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, Z will be true and the constraint will be satisfied
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}
    }
    
    public void testConstXFalseCFalseXor() {
    	try {
    		constraint = new BooleanXorConstraint(xVar,false,zVar);
    		store.addConstraint(constraint);
            
    		//Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            xVar.setFalse();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, Z will be false and the constraint will be satisfied
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be false", zVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testConstXTrueCFalseXor() {
    	try {
    		constraint = new BooleanXorConstraint(xVar,false,zVar);
    		store.addConstraint(constraint);
    		
            //Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            xVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, Z will be true and the constraint will be satisfied
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testConstZFalseCTrueXor() {
        try {
    		constraint = new BooleanXorConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
    		
            //Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            zVar.setFalse();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, X will be true and the constraint will be satisfied
            assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}
    }
    
    public void testConstZTrueCTrueXor() {
    	try {
    		constraint = new BooleanXorConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
    		
            //Nothing else should be affected yet
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Determine a variable
            zVar.setTrue();
            
            //Now the constraint is still undetermined
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //Propagate
            store.getConstraintAlg().propagate();
            
            //After Propagation, X will be false and the constraint will be satisfied
            assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertTrue("xVar isFalse should be false", xVar.isFalse());
            assertTrue("the constraint isTrue should be true", constraint.isTrue());
            assertFalse("the constraint isFalse should be false", constraint.isFalse());
    	}
    	catch (PropagationFailureException pfe) {
    	}
    }
    
    public void testConstraintDissatisfaction() {
        try {
            constraint = new BooleanXorConstraint(xVar,yVar,false,zVar);
        	store.addConstraint(constraint);
        	
        	//Determine all variables
        	xVar.setFalse();
        	yVar.setTrue();
        	zVar.setFalse();
        	
        	//The constraint should be false
        	assertTrue("xVar isFalse should be true", xVar.isFalse());
        	assertTrue("yVar isTrue should be true", yVar.isTrue());
        	assertTrue("zVar isFalse should be true", zVar.isFalse());
        	assertFalse("constraint is bound and false", constraint.isTrue());
        	assertTrue("constraint is bound and false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testPropFailure() {
        try {
        	constraint = new BooleanXorConstraint(xVar,yVar,false,zVar);
        	store.addConstraint(constraint);
        	
        	//Determin all variables
        	xVar.setTrue();
        	yVar.setTrue();
        	zVar.setTrue();
        	
        	//Verify that the constraint is dissatisfied
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
        	store.getConstraintAlg().propagate();
        	//if this propagates successfully, we should fail
        	fail();
        }
        catch (PropagationFailureException pfe) {
        }    
    }    
    
    public void testConstZSetXFalseXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            store.propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("yVar isTrue should be true", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstZSetXTrueXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setTrue();
            
            store.propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertTrue("yVar isFalse should be true", yVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstZSetXFalseYFalseXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine the variables
            xVar.setFalse();
            yVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }
    
    public void testConstZSetXTrueYTrueXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine the variables
            xVar.setTrue();
            yVar.setTrue();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }
    
    public void testConstZSetYFalseXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            yVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            store.propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstZSetYTrueXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, yVar, false, false);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            yVar.setTrue();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            
            store.propagate();

            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstYSetZTrueXor() {
        try {
            constraint = new BooleanXorConstraint(xVar, false, zVar);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            zVar.setTrue();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            store.propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
