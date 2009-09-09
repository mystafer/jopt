package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanOrConstraint;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests some different comparison techniques on the
 * TernarnyNumSumArc
 * 
 * @author Nick
 */
public class OrConstraintTest extends TestCase {
	private BooleanVariable xVar;
	private BooleanVariable yVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private CspConstraint constraint;
	
    public OrConstraintTest(java.lang.String testName) {
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
    
    /**
     * 
     */
    public void testSettingXFalse() {
        try {
        	zVar = new BooleanVariable("z", xVar.or(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            ((BooleanNode) xVar.getNode()).setFalse();
            xVar.setFalse();

            //Now the constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should now be false
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar should not be false", zVar.isFalse());            
            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }

    /**
     * 
     */
    public void testSettingXandYTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.or(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            xVar.setTrue();
            //Now the constraint is false
            assertFalse("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should now be true
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            yVar.setTrue();

            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Now Z is true
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
   
    public void testSettingXTrueOrZFalse() {
        try {
        	zVar = new BooleanVariable("z", (xVar.or(yVar)));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            xVar.setTrue();
            //Now the constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should now be false
            assertTrue("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            zVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
//          Y should still be undetermined til propagation
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
        	fail();           
        }
        catch (PropagationFailureException pfe) {
		}    
    }   
    
    public void testSettingXFalseAndZFalse() {
        try {
        	zVar = new BooleanVariable("z", xVar.or(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            xVar.setFalse();
            //Now the constraint is false
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());

            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should now be undeterminable
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            zVar.setFalse();
            
            //We have not reached a contradiction yet
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());            
            
//          Y should still be undetermined til propagation
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Success!
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Now Y is still undeterminableF & _ = F => _=?
            assertFalse("yVar isTrue should not be true", yVar.isTrue());
            assertTrue("yVar isFalse should be false", yVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }  
    public void testConstXFalseTrueOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            xVar.setFalse();
            //Now Everything but Z should be determined,  which
            //should be T xor T = F
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, Z will be false
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }  
    public void testConstXTrueTrueOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            xVar.setTrue();
            //Now Everything but Z should be determined, which
            //should be T xor T = F
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should not be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, Z will be false
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
 
    public void testConstXFalseFalseOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,false,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            xVar.setFalse();
            //Now Everything but Z should be determined, which
            //should be T xor T = F
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, Z will be false
            assertFalse("zVar isTrue should be true", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testTrueConstSetZTrueOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            zVar.setTrue();
            //Now Everything but X should be determined, including the constraint, which
            //should be T xor T = F
            assertTrue("the constraint is  true", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, X will be true
            assertFalse("xVar isTrue isnt necessarily true", xVar.isTrue());
            assertFalse("xVar isFalse should not be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }    
    public void testTrueConstSetZFalseOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            zVar.setFalse();
            //Now Everything but X should be determined, including the constraint, which
            //should be T xor T = F
            assertFalse("the constraint is  false", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be not ntrue", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, X will be true
            assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    } 
    public void testFalseConstSetZFalseOr() {
    	try {
    		constraint = new BooleanOrConstraint(xVar,false,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            zVar.setFalse();
            //Now Everything but X should be determined, which
            //should be _ xor F = F
            assertFalse("the constraint is  false", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be not ntrue", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
            //After Propagation, X will be false
            assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testConstZSetXFalseOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, yVar, false, true);
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
    
    public void testConstZSetXTrueOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setTrue();
            
            store.propagate();
            
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstZSetXFalseYFalseOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, yVar, false, true);
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
    
    public void testConstZSetYFalseOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, yVar, false, true);
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
    
    public void testConstZSetYTrueOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, yVar, false, false);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            yVar.setTrue();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }
    
    public void testConstYSetZTrueOr() {
        try {
            constraint = new BooleanOrConstraint(xVar, false, zVar);
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
