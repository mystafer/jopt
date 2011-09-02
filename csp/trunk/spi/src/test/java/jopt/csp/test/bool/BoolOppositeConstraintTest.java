package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanAndConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanNotConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanOrConstraint;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanXorConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;
/**
 * Tests some cases involving the opposite of a given constraint
 * 
 * @author jimmy b
 * @author Chris Johnson
 */
public class BoolOppositeConstraintTest extends TestCase {
	private BooleanVariable xVar;
	private BooleanVariable yVar;
	private BooleanVariable zVar;
	private ConstraintStore store;
	private BooleanConstraint constraint;
	
    public BoolOppositeConstraintTest(java.lang.String testName) {
        super(testName);
    }
    public void tearDown(){
        xVar=null;
        yVar=null;
        zVar=null;
        store=null;
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

    public void testAndOpposite() {
        try {
            constraint = new BooleanAndConstraint(xVar,yVar,false,zVar);
        	constraint = (BooleanAndConstraint)(constraint).getPostableOpposite();
        	store.addConstraint(constraint);
        	
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            xVar.setFalse();

            //Now the constraint still undeterminded
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is true
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should be determined
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse()); 
            
            //Determine the remaining variable
            yVar.setFalse();
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //F & F = F => F !& F = T
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse()); 
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    public void testOrOpposite() {
        try {
        	constraint = (BooleanOrConstraint)(new BooleanOrConstraint(xVar,yVar,false,zVar)).getPostableOpposite();
        	store.addConstraint(constraint);
        	
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            xVar.setTrue();

            //Now the constraint still undeterminded
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should be determined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertTrue("zVar isFalse should be true", zVar.isFalse()); 
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
    
    public void testXorOpposite() {
        try {
        	constraint = (BooleanXorConstraint)(new BooleanXorConstraint(xVar,yVar,false,zVar)).getPostableOpposite();
        	store.addConstraint(constraint);
        	
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            xVar.setTrue();

            //Now the constraint still undeterminded
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The other variables should still be undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Y is still undetermined
            assertFalse("yVar isTrue should be false", yVar.isTrue());
            assertFalse("yVar isFalse should be false", yVar.isFalse());
            //Z should still be undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse()); 
            
            //Determine another variable
            zVar.setTrue();
            //Now the constraint is still undetermined
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();
            
            //Now the constraint is finally determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }

    public void testNotOpposite() {
        try {
        	constraint = (BooleanNotConstraint)(new BooleanNotConstraint(xVar,zVar)).getPostableOpposite();
        	store.addConstraint(constraint);
        	
        	assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            //Now we determine one of the variables
            xVar.setTrue();

            //Now the constraint still undeterminded
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //The remaining variable is also undetermined
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            
            //Now we will propagate
            store.getConstraintAlg().propagate();

            //Now the constraint is determined
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            
            //Z should be determined
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse()); 
        }
        catch (PropagationFailureException pfe) {
        	fail();
		}    
    }
}
