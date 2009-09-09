package jopt.csp.example.spi;

import jopt.csp.spi.AC5;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * Implementation of N-queen problem.  Generic version of 4-queen
 * problem that will solve an NxN problem.
 * 
 * @author Nick
 * @see FourQueen
 */
public class Nqueen {
	private static int n = 7;
	// use a ConstraintStore that auto propagates whenever domain changes occur
	// and provides a default csp algorithm
	private static ChoicePointStack choicestack = new ChoicePointStack();
	private static ConstraintStore store = new ConstraintStore(new AC5(), true, choicestack);
    private static CspVariableFactory varFactory = store.getConstraintAlg().getVarFactory();
	private static CspIntVariable xvar[] = null; 

	public static void main(String[] args) throws Exception {
		
		// define x variables
		xvar = new IntVariable[n]; 
		for (int i=0; i<n; i++) {
			CspIntVariable v = varFactory.intVar("x" + (i+1), 1, n);
			((VariableChangeSource) v).addVariableChangeListener(store);
			xvar[i] = v;
		}
		
        // add constraints that will eliminate queens from attacking each other
        CspMath varMath = varFactory.getMath();
	    for (int i=0; i<n; i++) {
		    for (int j=i+1; j<n; j++) {
		        // prevent horizontal attack
		    	store.addConstraint(xvar[i].neq(xvar[j]));
		    	
		        // prevent diagonal attack
		    	store.addConstraint(varMath.abs(xvar[j].subtract(xvar[i])).neq(j-i));
		    }
	    }
	    
        // start searching
        System.out.println("solved: " + solve());
        System.out.println("---final---");
        for (int i=0; i<xvar.length; i++)
        	System.out.println(xvar[i]);
	}
	
	/**
	 * solves the problem
	 */
	private static boolean solve() throws PropagationFailureException {
		// propagate constraints for initial consistency
		store.propagate();
		
		// perform searching to finish reduction
		return search(0);
	}
	
	/**
	 * Recursive function to handle searching
	 *  
	 * @param varNum  number of variable in xdom array to search on
	 */
	private static boolean search(int varNum) {
		//retrieve the variable to work with
		CspIntVariable var = xvar[varNum];
        // check if domain is bound
        if (!var.isBound()) {
        	
        	// loop over all existing values in domain
        	int val = var.getMin();
	        for (int i=0; i<var.getSize(); i++){
	    		// push the stack before continuing
	            choicestack.push();
	            
	        	// attempt to assign number to domain
	        	try {
	        		var.setValue(val);
	        		
	        		// if this is the last number, return success
	        		if (varNum == n-1) return true;
	        		
	        		// if search continues successfully, return success status
	        		if (search(varNum+1)) return true;
	        	}
	        	catch(PropagationFailureException propx) {
        		}
	        	
	        	// propagation / search failed, rollback change to try next value
        		choicestack.pop();
        		
	        	// retrieve next value
	        	val = var.getNextHigher(val);
	        }
	        
	        // unable to locate a valid value, return failure status
	        return false;
        }
        
        else {
    		// if this is the last number, return success
    		if (varNum == n-1) return true;
    		
    		// if search continues successfully, return success status
    		if (search(varNum+1)) return true;
        }
        
		return false;
	}
}
