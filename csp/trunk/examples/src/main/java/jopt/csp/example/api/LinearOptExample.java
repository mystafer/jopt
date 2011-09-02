package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspDoubleExpr;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * While linear solvers are traditionally faster / better at solving
 * problems like the one presented below [a problem suggested to me
 * via e-mail] that is not to say that constraint satisfaction solvers
 * are not capable of finding solutions to them.  When a linear solver
 * is not available, CSP-based solvers may suffice - so long as
 * time and performance are not critical :).
 * <p>
 * Creates double-based variables and constraints to model the following problem: <br>
 * 1*c1 + 4*c2 + 4*c3 + 4*c4 = 2.5<br>
 * 1*c1 + 4*c2 + 1*c3 + 4*c4 = 3.0<br>
 * c1 + c2 + c3 + c4 = 1.0<br>
 * c1, c2, c3, c4 all >= 0;
 * 
 * @author Chris Johnson
 */
public class LinearOptExample {

    /**
     * Runs the suggested linear problem as a constraint satisfaction problem
     */
    public void run() {
        CspSolver solver = CspSolver.createSolver();
        CspVariableFactory varFactory  = solver.getVarFactory();
        
        // variables
        CspDoubleVariable c1Var = varFactory.doubleVar("c1", 0, 1);
        CspDoubleVariable c2Var = varFactory.doubleVar("c2", 0, 1);
        CspDoubleVariable c3Var = varFactory.doubleVar("c3", 0, 1);
        CspDoubleVariable c4Var = varFactory.doubleVar("c4", 0, 1);
        
        // expressions
        CspDoubleExpr c2VarTimes4 = c2Var.multiply(4);
        CspDoubleExpr c3VarTimes4 = c3Var.multiply(4);
        CspDoubleExpr c4VarTimes4 = c4Var.multiply(4);
        
        // constraints
        CspConstraint constraint1 = c1Var.add(c2VarTimes4).add(c3VarTimes4).add(c4VarTimes4).eq(3.5);
        CspConstraint constraint2 = c1Var.add(c2VarTimes4).add(c3Var).add(c4VarTimes4).eq(2.5);
        CspConstraint constraint3 = c1Var.add(c2Var).add(c3Var).add(c4Var).eq(1);
        
        try {
            solver.addConstraint(constraint1);
            solver.addConstraint(constraint2);
            solver.addConstraint(constraint3);
        }
        catch (PropagationFailureException pfe) {
            System.out.println("Constraints were impossible to satisfy");
        }
        
        SearchGoals goals = solver.getSearchGoals();
        // just find the first solution
        SearchGoal goal = goals.first();
        
        SearchActions actions = solver.getSearchActions();
        // a precision of 1/100th is sufficient
        SearchAction action = actions.generate(new CspDoubleVariable[]{c1Var, c2Var, c3Var, c4Var}, .01);
        
        boolean success = solver.solve(action, goal);
        
        if(success) {
            System.out.println("We've located a solution:");
            System.out.println(c1Var);
            System.out.println(c2Var);
            System.out.println(c3Var);
            System.out.println(c4Var);
        }
        else {
            System.out.println("We were unable to locate a valid solution");
        }
    }
    
    /**
     * Simple main method to get the problem runnin'
     */
    public static void main(String[] args) {
        new LinearOptExample().run(); 
    }
}
