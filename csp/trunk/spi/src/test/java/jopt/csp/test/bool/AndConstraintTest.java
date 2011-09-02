package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanAndConstraint;
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
public class AndConstraintTest extends TestCase {
	private BooleanVariable xVar;
	private BooleanVariable yVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private CspConstraint constraint;
	
    public AndConstraintTest(java.lang.String testName) {
        super(testName);
    }

    public void tearDown(){
        xVar=null;
        yVar=null;
        zVar=null;
        store=null;
        constraint=null;
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
    
    /**
     * 
     */
    public void testSettingXFalse() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
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

//          Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should now be false
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar should now be false", zVar.isFalse());            
            
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
        	zVar = new BooleanVariable("z", xVar.and(yVar));
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
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            yVar.setTrue();
            

//          Z should still be undetermined til propagation
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            //The constraint should now be met
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
    
    public void testSettingXandZTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
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
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            zVar.setTrue();
            
            
//          Y should still be undetermined til propagation
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //The constraint should now be met
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            
            //Now Y is true T & _ = T => _=T
            assertTrue("yVar isTrue should be true", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }    
    public void testSettingXTrueandZFalse() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
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
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            zVar.setFalse();
            //Not all the variables are constrained yet
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
//          Y should still be undetermined til propagation
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //The constraint should now be met
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Now Y is true T & _ = F => _=F
            assertFalse("yVar isTrue should not be true", yVar.isTrue());
            assertTrue("yVar isFalse should be false", yVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }   
    public void testSettingXFalseandZTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            zVar.setTrue();
            xVar.setFalse();
                        
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //This shouldnt have propagated, so we should fail now
            fail();
        }
        catch (PropagationFailureException pfe) {
		}    
    }    
    public void testSettingZTrue() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be true
            zVar.setTrue();
 
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());   
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //All variables are not constrained yet
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();        	
		}    
    }     
    public void testSettingXFalseandZFalse() {
        try {
        	zVar = new BooleanVariable("z", xVar.and(yVar));
        	constraint = zVar.toConstraint();
        	store.addConstraint(constraint);
        	assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //Now we determine one of the variables to be false
            xVar.setFalse();
            //All variables are not constrained yet
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
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
            
            zVar.setFalse();
            //THis is enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());  
            
            
//          Y should still be undetermined til propagation
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //Now Y is still undeterminableF & _ = F => _=?
            assertFalse("yVar isTrue should not be true", yVar.isTrue());
            assertFalse("yVar isFalse should not be false", yVar.isFalse());            
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }  
    public void testConstXFalseTrueAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            xVar.setFalse();
            //Now Everything but Z should be determined, including the constraint, which
            //should be T xor T = F
            assertFalse("the constraint is  not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
            
            //This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());  
            
            //After Propagation, Z will be false
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }  
    
    public void testConstXTrueTrueAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is nottrue", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            xVar.setTrue();
            //Now Everything but Z should be determined, including the constraint, which
            //should be T xor T = F
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should not be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //After Propagation, Z will be false
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testConstXTrueFalseAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,false,zVar);
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
            assertFalse("the constraint is  false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should not be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be false", zVar.isFalse());
            store.getConstraintAlg().propagate();            
            
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //After Propagation, Z will be false
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    public void testConstXFalseFalseAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,false,zVar);
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
            
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //After Propagation, Z will be false
            assertFalse("zVar isTrue should be true", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testTrueConstSetZTrueAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,true,zVar);
    		store.addConstraint(constraint);
            //Nothing else should be affected yet
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            zVar.setTrue();
            //Now Everything but X should be determined, which
            //should be T xor _= T
            assertFalse("the constraint is  true", constraint.isTrue());
            assertFalse("the constraint is false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should not be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            //After Propagation, X will be true
            assertTrue("xVar isTrue should be true", xVar.isTrue());
            assertFalse("xVar isFalse should not be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }    
    public void testTrueConstSetZFalseAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,true,zVar);
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
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be not ntrue", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
            
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //After Propagation, X will be true
            assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertTrue("xVar isFalse should be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    } 
    public void testFalseConstSetZFalseAnd() {
    	try {
    		constraint = new BooleanAndConstraint(xVar,false,zVar);
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
            assertTrue("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
    		assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should be false", xVar.isFalse());
            assertFalse("zVar isTrue should be not ntrue", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse());
            store.getConstraintAlg().propagate();
            
//          This should be enough to constrain the problem
        	assertTrue("the unbound constraint is true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            
            //After Propagation, X will still be undeterminable
            assertFalse("xVar isTrue should not be true", xVar.isTrue());
            assertFalse("xVar isFalse should not be true", xVar.isFalse());
    		
    	}
    	catch (PropagationFailureException pfe) {
    	}    
    }
    
    public void testConstZSetXFalseAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }
    
    public void testConstZSetXTrueAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setTrue();
            
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
    
    public void testConstZSetXTrueYFalseAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            xVar.setTrue();
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
    
    public void testConstZSetYFalseAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
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
    
    public void testConstZSetYTrueAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, false);
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
            assertFalse("xVar isTrue should be false", xVar.isTrue());
            assertTrue("xVar isFalse should be true", xVar.isFalse());
        }
        catch (PropagationFailureException pfe) {
            fail();
        }
    }
    
    public void testConstZSetYTrueXFalseAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, yVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            yVar.setTrue();
            xVar.setFalse();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }
    
    public void testConstYSetZTrueAnd() {
        try {
            constraint = new BooleanAndConstraint(xVar, false, zVar);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());

            //Now we determine one of the variables
            zVar.setTrue();
            
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            
            store.propagate();
            
            //Propagation should fail
            fail();
        }
        catch (PropagationFailureException pfe) {
        }
    }

}
