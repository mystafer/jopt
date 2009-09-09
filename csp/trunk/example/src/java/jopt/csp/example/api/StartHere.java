package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * The intention of this class is to demonstrate the simplicity
 * and the power of the jOpt CSP module.  It should give
 * new users a high-level view of creating constraints and
 * combining constraint satisfaction with searching for optimal
 * solutions.  By running this simple example through a
 * debugger, the "curious" user will better understand the
 * "magic" that occurs beneath the surface including (but not
 * limited to) the creation of arcs from constraints, building
 * of a node-arc graph, propagating, and searching.
 * 
 * Any new developer or user of jOpt should start here - hence the
 * not-so-subtle title.
 * 
 * @author Chris Johnson
 */
public class StartHere {

    /**
     * This method will create a simple problem involving 3 variables - x, y, and z.
     * The constraint indicates that x * y <= z, and the intention is to find the
     * largest value of z satisfying the inequality.
     */
    public void run() {
        // create an instance of the solver - used to construct and solve CSP problems
        CspSolver solver = CspSolver.createSolver();
        
        // Obtain a variable factory from the solver to create our variables
        // This utility class helps us instantiate variables and also
        // keeps a registry of them, making sure no two variables are created
        // with the same name.
        CspVariableFactory varFactory  = solver.getVarFactory();
        
        // create the necessary 'variables'
        CspIntVariable xVar = varFactory.intVar("x", 1, 4);   // x = 1..4
        CspIntVariable yVar = varFactory.intVar("y", 1, 3);   // y = 1..3
        CspIntVariable zVar = varFactory.intVar("z", 1, 12);  // z = 1..12
        
        // create the x * y <= z 'constraint'
        CspConstraint constraint = xVar.multiply(yVar).leq(zVar);
        
        // add the created constraint to the solver
        try {
            solver.addConstraint(constraint);
        }
        catch (PropagationFailureException pfe) {
            // The solver attempts to 'propagate' the constraint immediately;
            // that is, it attempts to make the variable values consistent with
            // those allowed by the constraint.
            // Because the values are consistent, propagation will not fail.
            System.out.println("Constraint was impossible to satisfy");
        }
        
        // Get the pre-defined search goals
        // This is a utility class that must be instantiated because it is
        // hidden in the spi.  It is essentially stateless.
        SearchGoals goals = solver.getSearchGoals();
        
        // recall that our intention, our 'goal', is to maximize the value of the z-variable
        SearchGoal goal = goals.maximize(zVar);
        
        // get the pre-defined search actions
        // This is another utility class that must be instantiated because it is
        // hidden in the spi.  It is essentially stateless.
        SearchActions actions = solver.getSearchActions();
        
        // We'll attempt to locate our optimal solution by generating the possible values for z;
        // that is, we'll try z=1, z=2, ... z=12
        SearchAction action = actions.generate(new CspIntVariable[]{zVar});
        
        // use the solver to find our optimal solution
        // note that it will do so using the default alogrithm (currently 'AC5')
        // as well as the default search technique (currently 'depth-first')
        boolean success = solver.solve(action, goal);
        
        if(success) {
            System.out.println("We've located an optimal solution:");
            System.out.println(xVar);
            System.out.println(yVar);
            System.out.println(zVar);   // z = 12
            // Note that x and y are not 'bound'; that is, they don't have a single value.
            // This makes sense because 1*1, 2*3, etc. are all less than or equal to 12.
        }
        else {
            System.out.println("We were unable to locate a valid solution");
        }
    }
    
    /**
     * Simple main method to get the problem runnin'
     * 
     * @param args
     */
    public static void main(String[] args) {
        new StartHere().run(); 
    }

}
