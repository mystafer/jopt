package jopt.csp.example.spi;

import java.util.HashMap;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.solver.ChoicePointAlgorithm;
import jopt.csp.spi.solver.ChoicePointStack;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.spi.solver.VariableChangeSource;
import jopt.csp.spi.util.StateStore;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspMath;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * This particular five queens solution is built off the auto propagation example
 * found at jopt.csp.example.FourQueenAutoPropagation, but it incorporates the use
 * of the solution storing capability of the ConstraintStore to attempt to find
 * a second solution to the problem.  Both solutions, once reached, are displayed
 * to verify proper functionality.
 * 
 * This is an example of the 5 queen problem. Thinking about a chess board in rows
 * and columns, no two queens can be on the same row (y axis) or column
 * (x-axis). Queens cannot be on the same diagonal either. The current program
 * logic fix the Y axis value, so that the CSP only need to find X axis.
 * Therefore (x1,1), (x2,2), (x3,3) ...
 * 
 * @author Chris
 */
public class FiveQueenMultipleSolutions {
	// create x variables
	private static ChoicePointAlgorithm gac = SolverImpl.createDefaultAlgorithm();
	//use a ConstraintStore that auto propagates whenever domain changes occur
	private static ChoicePointStack choicestack = new ChoicePointStack();
	private static ConstraintStore store = new ConstraintStore(gac, true, choicestack);
    private static CspVariableFactory varFactory = gac.getVarFactory();
	private static CspIntVariable x1 = varFactory.intVar("x1", 1, 5);
	private static CspIntVariable x2 = varFactory.intVar("x2", 1, 5);
	private static CspIntVariable x3 = varFactory.intVar("x3", 1, 5);
	private static CspIntVariable x4 = varFactory.intVar("x4", 1, 5);
	private static CspIntVariable x5 = varFactory.intVar("x5", 1, 5);

	/**
	 * Main program method
	 */
	public static void main(String[] args) throws Exception {
		
		//Have the ConstraintStore listen for domain change events
		//on the created variables
		((VariableChangeSource) x1).addVariableChangeListener(store);
		((VariableChangeSource) x2).addVariableChangeListener(store);
		((VariableChangeSource) x3).addVariableChangeListener(store);
		((VariableChangeSource) x4).addVariableChangeListener(store);
		((VariableChangeSource) x5).addVariableChangeListener(store);
		
		//Prevent horizontal attack by ensuring no two queens
		//are on the same row. this is done by making sure
		//all x values are different
		store.addConstraint(x1.neq(x2));
		store.addConstraint(x1.neq(x3));
		store.addConstraint(x1.neq(x4));
		store.addConstraint(x1.neq(x5));

		store.addConstraint(x2.neq(x3));
		store.addConstraint(x2.neq(x4));
		store.addConstraint(x2.neq(x5));
		
		store.addConstraint(x3.neq(x4));
		store.addConstraint(x3.neq(x5));
		
		store.addConstraint(x4.neq(x5));

		// create expressions for limiting diagonal attack
		// We know that no two queens will be on the same row
		// because of the previous constraints.
		// We need to make sure that q2 is not on the row
		// above or below q1, q3 is not 2 rows above or below
		// q1, etc.
		// A simple function that represents this for
		// q1 and q2 would be |x1 - x2| != 1. This will
		// ensure that q1 and q2 are not one row apart.
		// The difference betweenn x1 and x3 cannot be 2 rows, etc.
        CspMath varMath = varFactory.getMath();
		CspIntExpr diagDist12 = varMath.abs(x1.subtract(x2));
		CspIntExpr diagDist13 = varMath.abs(x1.subtract(x3));
		CspIntExpr diagDist14 = varMath.abs(x1.subtract(x4));
		CspIntExpr diagDist15 = varMath.abs(x1.subtract(x5));

		CspIntExpr diagDist23 = varMath.abs(x2.subtract(x3));
		CspIntExpr diagDist24 = varMath.abs(x2.subtract(x4));
		CspIntExpr diagDist25 = varMath.abs(x2.subtract(x5));

		CspIntExpr diagDist34 = varMath.abs(x3.subtract(x4));
		CspIntExpr diagDist35 = varMath.abs(x3.subtract(x5));
		
		CspIntExpr diagDist45 = varMath.abs(x4.subtract(x5));

		//Prevent diagonal attack
		store.addConstraint(diagDist12.neq(1));
		store.addConstraint(diagDist13.neq(2));
		store.addConstraint(diagDist14.neq(3));
		store.addConstraint(diagDist15.neq(4));

		store.addConstraint(diagDist23.neq(1));
		store.addConstraint(diagDist24.neq(2));
		store.addConstraint(diagDist25.neq(3));

		store.addConstraint(diagDist34.neq(1));
		store.addConstraint(diagDist35.neq(2));
		
		store.addConstraint(diagDist45.neq(1));

		// solve the problem
		try {
			solve();
		}
		catch (PropagationFailureException propx) {
			System.out.println("unable to solve problem");
		}
	}
	
	/**
	 * solves the problem
	 */
	private static void solve() throws PropagationFailureException {
		// propagate constraints for initial consistency
		store.propagate();
		
		//save the variables in the consistent state
		@SuppressWarnings("rawtypes")
		StateStore ss = (StateStore) ((HashMap) store.getCurrentState()).get("d");
		ss.storeState();
		System.out.println("--consistent state stored--");
		
		// perform searching to finish reduction
		boolean success = search();
		if(success) {
			System.out.println("--first solution--");
			dumpVariables();
		}
		
		ss.restoreState();
		System.out.println("--consistent state restored--");
		dumpVariables();
		//perform a second search starting from a known consistent state
		success = secondSearch();
		if(success) {
			System.out.println("--second solution--");
			dumpVariables();
		}
		ss.restoreState();
		System.out.println("--consistent state restored again--");
		dumpVariables();
	}

	
	/****** First Searching "Algorithm" ******/
	
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
	 * Helper function to simulate continuing of search by
	 * assigning values to X4. It will then call searchX5 to continue
	 * searching.
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
					//if search continues successfully, return success status
					if(searchX5())
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

		// current node is bound, continue from X4 variable
		else if (searchX5()) {
			return true;
		}

		return false;
	}
	
	/**
	 * Helper function to simulate continuing of search of final variable
	 */
	private static boolean searchX5() 
	{
		// check if domain is bound
		if (!x5.isBound()) {
			// loop over all existing values in domain
			int val = x5.getMin();
			for (int i = 0; i < x5.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				// attempt to assign number to domain
				try {
					x5.setValue(val);
					// return success status
					return true;
				} catch (PropagationFailureException propx) {}

				// propagation / search failed, rollback change to try next
				// value
				choicestack.pop();
				
				// retrieve next value
				val = x5.getNextHigher(val);
			}

			// unable to locate a valid value, return failure status
			return false;
		}

		// current node is bound return success
		return true;
	}
	
	
	/****** Second Searching "Algorithm" ******/
	
	/**
	 * Helper function to simulate start of searching by first assigning
	 * a paricular value to X1 (distinct from that of the first solution).
	 * It will then call searchX2 to continue searching.
	 * 
	 * This should result in a different solution that the <code>search()</code>
	 * method.
	 */
	private static boolean secondSearch() 
	{
		try {
			x1.setValue(5);
		}
		catch(PropagationFailureException propx) {
			propx.printStackTrace();
			System.out.println("Error occurred when setting x1 = 5");
		}
		
		// check if domain is bound
		if (!x1.isBound()) {
			// loop over all existing values in domain
			int val = x1.getMin();
			for (int i = 0; i < x1.getSize(); i++) {
				// push the stack before continuing
				choicestack.push();

				//attempt to assign number to domain
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

		// current node is bound, continue from X3 variable
		else if (searchX2()) {
			return true;
		}

		return false;
	}
	
	private static void dumpVariables() {
		System.out.println(x1);
		System.out.println(x2);
		System.out.println(x3);
		System.out.println(x4);
		System.out.println(x5);
	}
}
