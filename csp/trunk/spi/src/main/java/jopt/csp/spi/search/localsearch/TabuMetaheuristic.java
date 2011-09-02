package jopt.csp.spi.search.localsearch;

import java.util.List;

import jopt.csp.search.Metaheuristic;
import jopt.csp.search.NeighborCheck;
import jopt.csp.solution.SolverSolution;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspNumExpr;
import jopt.csp.variable.CspVariable;
import jopt.csp.variable.PropagationFailureException;

/**
 * Implementation of a tabu search metaheuristic that tracks moves performed during a local search
 * and maintains lists of moves that are forbidden.
 * 
 * @author  Nick Coleman
 * @version $Revision: 1.5 $
 */
public class TabuMetaheuristic implements Metaheuristic {
    private final static double DEFAULT_OBJECTIVE_GAP   = 0.0001d;
    
    private ConstraintStore store;
	private TabuList undoMoves;
    private TabuList alterMoves;
    private double objectiveGap;
    
    private SolverSolution initial;
    private double bestObjectiveVal;
    private boolean objectiveSet;
    
    private NeighborCheck nc;
    
    /**
     * Creates a new tabu search metaheuristic
     * 
     * @param store                 Constraint store that search is being performed upon
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @param forbiddenAlterMoves   Number of moves that will alter a previously changed variable in any way that are not allowed
     * @param objectiveGap          Amount objective value of move must be within initial objective value
     */
    public TabuMetaheuristic(ConstraintStore store, int forbiddenUndoMoves, int forbiddenAlterMoves, double objectiveGap) {
        this.store = store;
        
        // create list of undo moves, this will always exist
        this.undoMoves = new TabuList(false, forbiddenUndoMoves);
        
        // list of alter restricted moves, this may not exist
        if (forbiddenAlterMoves > 0)
            this.alterMoves = new TabuList(true, forbiddenAlterMoves);
        
    	this.objectiveGap = objectiveGap;
    }

    /**
     * Creates a new tabu search metaheuristic
     * 
     * @param store                 Constraint store that search is being performed upon
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @param forbiddenAlterMoves   Number of moves that will alter a previously changed variable in any way that are not allowed
     */
    public TabuMetaheuristic(ConstraintStore store, int forbiddenUndoMoves, int forbiddenAlterMoves) {
        this(store, forbiddenUndoMoves, forbiddenAlterMoves, DEFAULT_OBJECTIVE_GAP);
    }
    
    /**
     * Creates a new tabu search metaheuristic
     * 
     * @param store                 Constraint store that search is being performed upon
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     * @param objectiveGap          Amount objective value of move must be within initial objective value
     */
    public TabuMetaheuristic(ConstraintStore store, int forbiddenUndoMoves, double objectiveGap) {
        this(store, forbiddenUndoMoves, 0, objectiveGap);
    }

    /**
     * Creates a new tabu search metaheuristic
     * 
     * @param store                 Constraint store that search is being performed upon
     * @param forbiddenUndoMoves    Number of moves that will return a variable to a previous value that are not allowed
     */
    public TabuMetaheuristic(ConstraintStore store, int forbiddenUndoMoves) {
        this(store, forbiddenUndoMoves, 0, DEFAULT_OBJECTIVE_GAP);
    }

    // javadoc inherited from Metaheuristic
    public boolean setInitialSolution(SolverSolution initial) throws PropagationFailureException {
        this.initial = initial;
        
        // set the initial solution on the neighbor check
        if (nc != null)
            nc.setInitialSolution(initial);
        
        // post constraint that will limit solutions to be within 'gap' of initial objective
        
        // retrieve objective information for solution
        CspNumExpr obj = initial.getObjectiveExpression();
        double objVal = initial.getObjectiveVal();
        
        // create constraint that will improve solution
        CspConstraint constraint = null;
        if (initial.isMinimizeObjective()) {
            // determine if objective has improved from current best
            if (!objectiveSet) {
                bestObjectiveVal = objVal;
                objectiveSet = true;
            }
            else {
                bestObjectiveVal = Math.min(bestObjectiveVal, objVal);
            }
            
            // adjust objective by gap amount: obj <= objVal + gap
            double newObj = bestObjectiveVal + objectiveGap;
            
            constraint = ImproveSolutionAction.createImprovementConstraint(obj, newObj, true);
        }
        else if (initial.isMaximizeObjective()) {
            // determine if objective has improved from current best
            if (!objectiveSet) {
                bestObjectiveVal = objVal;
                objectiveSet = true;
            }
            else {
                bestObjectiveVal = Math.max(bestObjectiveVal, objVal);
            }
            
            // adjust objective by gap amount: obj >= objVal - gap
            double newObj = bestObjectiveVal - objectiveGap;
            
            constraint = ImproveSolutionAction.createImprovementConstraint(obj, newObj, false);
        }
        
        // post constraint if it was created
        if (constraint!=null)
            store.addConstraint(constraint, false);
        
        return true;
    }
    
    // javadoc inherited from Metaheuristic
    public boolean isAcceptableNeighbor(SolverSolution neighbor) {
        TabuMove move = createMove(neighbor, true);
        
        // check if move is in forbidden alter list
        if (alterMoves!=null && alterMoves.contains(move))
            return false;
        
        // check if move is in forbidden undo list
        if (undoMoves.contains(move))
            return false;
        
        if (nc != null && !nc.isValidNeighbor(neighbor))
            return false;
        
        return true;
    }
    
    // javadoc inherited from Metaheuristic
    public boolean isRestoredNeighborValid(SolverSolution neighbor) {
    	return true;
    }
    
    // javadoc inherited from Metaheuristic
    public void neighborSelected(SolverSolution neighbor) {
        // add out move to list of moves that cannot be undone
        TabuMove outMove = createMove(neighbor, false);
        undoMoves.add(outMove);
        
        // check if in moves are being tracked
        if (alterMoves!=null) {
            // add in move to list of moves that cannt be altered
            TabuMove inMove = createMove(neighbor, true);
            alterMoves.add(inMove);
        }
        
    }
    
    /**
     * creates tabu move from changes in neighbor to initial solution
     * 
     * @param neighbor  Neighbor to create move from
     */
    private TabuMove createMove(SolverSolution neighbor, boolean in) {
        TabuMove move = new TabuMove();
        List<CspVariable> variables = neighbor.variables();
        for (int i=0; i<variables.size(); i++) {
            CspVariable var = (CspVariable) variables.get(i);
            
            if (in)
                move.addVariableSolution(neighbor.getSolution(var));
            else
                move.addVariableSolution(initial.getSolution(var));
        }
        
        return move;
    }
    
    // javadoc inherited from Metaheuristic
    public boolean continueSearch() {
        // if no moves are currently forbidden, there is nothing that can be done
        // except possibly undoing the last move that was done which we should
        // not allow
    	if (undoMoves.size()<=1 && (alterMoves==null || alterMoves.size()==0))
            return false;
        
        // attempt to age the alteration list first since it is more restrictive
        // and is not as important as making sure a previous move was completely
        // undone
        if (alterMoves!=null && alterMoves.size()>0) {
        	alterMoves.age();
        }
        
        // remove a forbidden undo move from the list
        else {
        	undoMoves.age();
        }
        
        return true;
    }
    
    /**
     * Resets the tabu lists.  Useful for when all moves are tabu but we haven't
     * given up and want to keep trying.
     *
     */
    public void resetTabuLists() {
        if (undoMoves!=null) undoMoves.reset();
        if (alterMoves!=null) alterMoves.reset();
    }
    
    public void reduceTabuLists() {
        if (undoMoves!=null) undoMoves.reduce();
        if (alterMoves!=null) alterMoves.reduce();
    }

    // javadoc inherited from Metaheuristic    
    public NeighborCheck getNeighborCheck() {
        return nc;
    }

    // javadoc inherited from Metaheuristic
    public void setNeighborCheck(NeighborCheck nc) {
        this.nc = nc;
    }
}
