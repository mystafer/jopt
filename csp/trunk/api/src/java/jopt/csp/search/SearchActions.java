package jopt.csp.search;

import jopt.csp.solution.SolverSolution;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspFloatVariable;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspLongVariable;
import jopt.csp.variable.CspSetVariable;

/**
 * Interface to common search actions.
 */
public interface SearchActions {
	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * @param vars      Array of variables to instantiate
	 */
	public SearchAction generate(CspIntVariable vars[]);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 */
	public SearchAction generate(CspIntVariable vars[], IntegerSelector selector);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 * @param varSelector	Used to select next variable to instantiate
	 */
	public SearchAction generate(CspIntVariable vars[], IntegerSelector selector, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * @param vars      Array of variables to instantiate
	 */
	public SearchAction generate(CspLongVariable vars[]);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 */
	public SearchAction generate(CspLongVariable vars[], LongSelector selector);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 *  
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 * @param varSelector	Used to select next variable to instantiate 
	 */
	public SearchAction generate(CspLongVariable vars[], LongSelector selector, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 */
	public SearchAction generate(CspFloatVariable vars[], float precision);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half                  
	 */
	public SearchAction generate(CspFloatVariable vars[], float precision, boolean minFirst);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated. 
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 * @param varSelector	Method to select next variable to instantiate                      
	 */
	public SearchAction generate(CspFloatVariable vars[], float precision, boolean minFirst, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated.
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 */
	public SearchAction generate(CspDoubleVariable vars[], double precision);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated.
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 */
	public SearchAction generate(CspDoubleVariable vars[], double precision, boolean minFirst);


	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * Since real numbers can be divided an infinite number of times, a precision
	 * value must be specified to indicate when the range of the variable is small
	 * enough to consider the variable completely instantiated.
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param precision     Minimum precision to which variable domain will be reduced
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 * @param varSelector	Method to select next variable to instantiate
	 */
	public SearchAction generate(CspDoubleVariable vars[], double precision, boolean minFirst, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating a choice point for each 
	 * variable with one child for each possible value.  Each child will attempt
	 * to assign a single value to the variable.
	 * 
	 * This differs from standard generation by creating a tree that may have more
	 * than two child nodes at a choice point.  This could have performance implications
	 * that should be considered when choosing between the two operations. 
	 * 
	 * @param vars      Array of variables to instantiate
	 */
	public SearchAction generateNonBinary(CspIntVariable vars[]);

	/**
	 * Creates an action that will instantiate an array of variable.  The
	 * generator will instantiate each variable by creating a choice point for each 
	 * variable with one child for each possible value.  Each child will attempt
	 * to assign a single value to the variable.
	 * 
	 * This differs from standard generation by creating a tree that may have more
	 * than two child nodes at a choice point.  This could have performance implications
	 * that should be considered when choosing between the two operations. 
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param varSelector	Method to select next variable to instantiate
	 */
	public SearchAction generateNonBinary(CspIntVariable vars[], VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * @param vars          Array of variables to instantiate
	 */
	public SearchAction splitGenerate(CspIntVariable vars[]);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half          
	 */
	public SearchAction splitGenerate(CspIntVariable vars[], boolean minFirst);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 * @param varSelector	Method to select next variable to instantiate            
	 */
	public SearchAction splitGenerate(CspIntVariable vars[], boolean minFirst, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * @param vars          Array of variables to instantiate
	 */
	public SearchAction splitGenerate(CspLongVariable vars[]);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 */
	public SearchAction splitGenerate(CspLongVariable vars[], boolean minFirst);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will reduce each variable
	 * by removing either the upper or lower half of values within the domain of
	 * the variable.
	 * 
	 * This generation technique is similar to the way in which float and double
	 * variables are generated, but the variable will be reduced to a single value
	 * rather than a range of values within a precision value.
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars          Array of variables to instantiate
	 * @param minFirst      True if bottom half of domain should be used to restrict domain first, 
	 *                          false to use upper half
	 * @param varSelector	Method to select next variable to instantiate
	 */
	public SearchAction splitGenerate(CspLongVariable vars[], boolean minFirst, VariableSelector varSelector);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * @param vars      Array of variables to instantiate
	 */
	public <T> SearchAction generate(CspSetVariable<T> vars[]);

	/**
	 * Creates an action that will instantiate an array of variables in order.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 */
	public <T> SearchAction generate(CspSetVariable<T> vars[], SetSelector<T> selector);

	/**
	 * Creates an action that will instantiate an array of variables.  The
	 * generator will instantiate each variable by creating binary choice points for the
	 * search tree to traverse.  At each point, the choice will be either to 
	 * assign an available value to the variable or remove the value and look
	 * for another value to assign.  
	 * 
	 * A selector method will be used to determine which value of the remaining
	 * values in the domain should be assigned to a variable at the next choice
	 * point in the search tree.
	 * 
	 * A variable selector method will be used to determine which variable will be
	 * instantiated next. 
	 * 
	 * @param vars      	Array of variables to instantiate
	 * @param selector  	Used to select next value to reduce domain of variable
	 * @param varSelector	Method to select next variable to instantiate
	 */
	public <T> SearchAction generate(CspSetVariable<T> vars[], SetSelector<T> selector, VariableSelector varSelector);

	/**
	 * Creates an action that will store all information for the current problem
	 * state in a solution that can be restored at a later date.
	 * 
	 * @param solution  Solution to store information in
	 */
	public SearchAction storeSolution(SolverSolution solution);

	/**
	 * Action that will push all current changes onto the stack and restore
	 * another solution.  This action can then be undone to return to other
	 * choices in the search tree.  This is useful when trying to manipulate
	 * an existing solution and determining if it is valid.
	 * 
	 * @param solution  Solution to restore from
	 */
	public SearchAction restoreSolution(SolverSolution solution);

	/**
	 * Adds a constraint during a search that can be undone as the search
	 * routine backtracks
	 */
	public SearchAction addConstraint(CspConstraint constraint);

	/**
	 * Creates a choice point between two different actions
	 *   
	 * @param action1   First choice
	 * @param action2   Second choice
	 */
	public SearchAction choice(SearchAction action1, SearchAction action2);

	/**
	 * Creates a choice point between an array of actions
	 *  
	 * @param actions   Array of actions that should be separated into individual search nodes
	 */
	public SearchAction choice(SearchAction actions[]);

	/**
	 * Creates a new combined action
	 *  
	 * @param action1   First action to perform
	 * @param action2   Second action to perform
	 */
	public SearchAction combine(SearchAction action1, SearchAction action2);

	/**
	 * Creates a new combined action
	 *  
	 * @param action1   First action to perform
	 * @param action2   Second action to perform
	 * @param action3   Third action to perform
	 */
	public SearchAction combine(SearchAction action1, SearchAction action2, SearchAction action3);

	/**
	 * Creates a new combined action
	 *  
	 * @param actions       Collection of actions that should be combined into one actions
	 */
	public SearchAction combine(SearchAction actions[]);

	/**
	 * Creates a look ahead action, a unique combination of actions that allows the 
	 * search to try a change (an assignment for instance) before deciding how to proceed
	 * 
	 * @param pseudoChange  Used to determine success or failure, its impact will always be discarded (rolled back)
	 * @param successAction The action returned if the pseudo-change was applied (and revoked) without error
	 * @param failureAction The action returned if the pseudo-change caused an error
	 * @return Either successAction or failureAction depending on the application of the pseudo-change
	 */
	public SearchAction lookAhead(SearchAction pseudoChange, SearchAction successAction, SearchAction failureAction);
}
