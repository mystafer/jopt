package jopt.csp.example.spi;

import jopt.csp.spi.AC5;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * This particular solution to the 4 queens problem uses the automatic
 * propagation feature of the ConstraintStore
 * 
 * This is an example of 4 queen problem. Thinking about a chess board in rows
 * and columns, no two queens can be on the same row (y axis) or column
 * (x-axis). Queens cannot be on the same diagonal either. The current program
 * logic fix the Y axis value, so that the CSP only need to find X axis.
 * Therefore (x1,1), (x2,2), (x3,3) ...
 * 
 * @author Vili
 * @author Nick
 * @author Chris
 */
public class FourQueenAutoPropagation {
	private static ChoicePointAlgorithm gac = new AC5();
	//use a ConstraintStore that auto propagates whenever domain changes occur
	private static ChoicePointStack choicestack = new ChoicePointStack();
	private static ConstraintStore store = new ConstraintStore(gac, true, choicestack);
    private static CspVariableFactory varFactory = gac.getVarFactory();
    //create x variables
	private static CspIntVariable x1 = varFactory.intVar("x1", 1, 4);
	private static CspIntVariable x2 = varFactory.intVar("x2", 1, 4);
	private static CspIntVariable x3 = varFactory.intVar("x3", 1, 4);
	private static CspIntVariable x4 = varFactory.intVar("x4", 1, 4);

	/**
	 * Main program method
	 */
	public static void main(String[] args) throws Exception {
		
		//Have the ConstraintStore listen for variable change events
		//on the created variables
		((VariableChangeSource) x1).addVariableChangeListener(store);
		((VariableChangeSource) x2).addVariableChangeListener(store);
		((VariableChangeSource) x3).addVariableChangeListener(store);
		((VariableChangeSource) x4).addVariableChangeListener(store);
		
		//Prevent horizontal attack by ensuring no two queens
		//are on the same row. this is done by making sure
		//all x values are different

		store.addConstraint(x1.neq(x2));
		store.addConstraint(x1.neq(x3));
		store.addConstraint(x1.neq(x4));

		store.addConstraint(x2.neq(x3));
		store.addConstraint(x2.neq(x4));
		
		store.addConstraint(x3.neq(x4));

		// create expressions for limiting diagonal attack
		// We know that no two queens will be on the same row
		// because of the previous constraints.
		// We need to make sure that q2 is not on the row
		// above or below q1, q3 is not 2 rows above or below
		// q1, etc.
		// A simple function that represents this for
		// q1 and q2 would be |x2 - x1| != 1. This will
		// ensure that q1 and q2 are not one row apart.
		// The difference betweenn x1 and x3 cannot be 2 rows, etc.
        CspMath varMath = varFactory.getMath();
        CspIntExpr diagDist12 = varMath.abs(x2.subtract(x1));
        CspIntExpr diagDist13 = varMath.abs(x3.subtract(x1));
        CspIntExpr diagDist14 = varMath.abs(x4.subtract(x1));

        CspIntExpr diagDist23 = varMath.abs(x3.subtract(x2));
        CspIntExpr diagDist24 = varMath.abs(x4.subtract(x2));

        CspIntExpr diagDist34 = varMath.abs(x4.subtract(x3));

		//Prevent diagonal attack
		store.addConstraint(diagDist12.neq(1));
		store.addConstraint(diagDist13.neq(2));
		store.addConstraint(diagDist14.neq(3));

		store.addConstraint(diagDist23.neq(1));
		store.addConstraint(diagDist24.neq(2));

		store.addConstraint(diagDist34.neq(1));

		//turn on automatic propagation
		store.setAutoPropagate(true);
		
		// solve the problem
		try {
			System.out.println("solved: " + solve());
			System.out.println("---final---");
			dumpVariables();
		}
		catch (PropagationFailureException propx) {
			System.out.println("unable to solve problem");
		}
	}
	
	/**
	 * solves the problem
	 */
	private static boolean solve() throws PropagationFailureException {
		// propagate constraints for initial consistency
		store.propagate();
		
		// perform searching to finish reduction
		return search();
	}

	/**
	 * Helper function to simulate start of searching by
	 * assigning values to X1. It will then call searchX2 to continue
	 * searching.
	 */
	private static boolean search() 
	{
		// check if domain is bound
		if (!x1.isBound()) {
			// loop over all existing values in domain
			int val = x1.getMin();
			for (int i = 0; i < x1.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				// attempt to assign number to domain
				try {
					x1.setValue(val);
					//if search continues successfully, return success status
					if(searchX2())
						return true;
				} catch (PropagationFailureException propx) {}
								
				// propagation / search failed, rollback change to try next
				// value
				choicestack.pop();
				
				// retrieve next value
				val = x1.getNextHigher(val);
			}

			// unable to locate a valid value, return failure status
			return false;
		}

		// current node is bound, continue from X2 variable
		else if (searchX2()) {
			return true;
		}

		return false;
	}
	
	/**
	 * Helper function to simulate continuing of search by
	 * assigning values to X2. It will then call searchX3 to continue
	 * searching.
	 */
	private static boolean searchX2() 
	{	
		// check if domain is bound
		if (!x2.isBound()) {
			// loop over all existing values in domain
			int val = x2.getMin();
			for (int i = 0; i < x2.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				// attempt to assign number to domain
				try {
					x2.setValue(val);
					//if search continues successfully, return success status
					if(searchX3())
						return true;
				} catch (PropagationFailureException propx) {}

				// propagation / search failed, rollback change to try next
				// value
				choicestack.pop();
				
				// retrieve next value
				val = x2.getNextHigher(val);
			}

			// unable to locate a valid value, return failure status
			return false;
		}

		// current node is bound, continue from X3 variable
		else if (searchX3()) {
			return true;
		}

		return false;
	}
	
	/**
	 * Helper function to simulate continuing of search by
	 * assigning values to X3. It will then call searchX4 to continue
	 * searching.
	 */
	private static boolean searchX3()
	{
		// check if domain is bound
		if (!x3.isBound()) {
			// loop over all existing values in domain
			int val = x3.getMin();
			for (int i = 0; i < x3.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				// attempt to assign number to domain
				try {
					x3.setValue(val);
					//if search continues successfully, return success status
					if(searchX4())
						return true;
				} catch (PropagationFailureException propx) {}

				// propagation / search failed, rollback change to try next
				// value
				choicestack.pop();
				
				// retrieve next value
				val = x3.getNextHigher(val);
			}

			// unable to locate a valid value, return failure status
			return false;
		}

		// current node is bound, continue from X4 variable
		else if (searchX4()) {
			return true;
		}

		return false;
	}
	
	/**
	 * Helper function to simulate continuing of search of final variable
	 */
	private static boolean searchX4() 
	{
		// check if domain is bound
		if (!x4.isBound()) {
			// loop over all existing values in domain
			int val = x4.getMin();
			for (int i = 0; i < x4.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				// attempt to assign number to domain
				try {
					x4.setValue(val);
					// return success status
					return true;
				} catch (PropagationFailureException propx) {}

				// propagation / search failed, rollback change to try next
				// value
				choicestack.pop();
				
				// retrieve next value
				val = x4.getNextHigher(val);
			}

			// unable to locate a valid value, return failure status
			return false;
		}

		// current node is bound return success
		return true;
	}
	
	private static void dumpVariables() {
		System.out.println(x1);
		System.out.println(x2);
		System.out.println(x3);
		System.out.println(x4);
	}
}